package de.switajski.priebes.flexibleorders.web;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.NotFoundException;
import de.switajski.priebes.flexibleorders.json.JsonObjectResponse;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.repository.specification.HasCustomerSpecification;
import de.switajski.priebes.flexibleorders.service.ReportingService;
import de.switajski.priebes.flexibleorders.service.conversion.ReportItemToItemDtoConversionService;
import de.switajski.priebes.flexibleorders.service.helper.StatusFilterDispatcher;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;
import de.switajski.priebes.flexibleorders.web.helper.ExtJsResponseCreator;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;
import de.switajski.priebes.flexibleorders.web.helper.ProductionState;

@RequestMapping("/reportitems")
@Controller
public class ReportItemController extends ExceptionController {

    /**
     * retrieve reportItems by order or by orderItem
     */
    boolean BY_ORDER = true;

    private static final String CUSTOMER_FILTER = "customerNumber";

    public static final String STATUS_STRING = "status";

    @Autowired
    private ReportingService reportingService;
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private StatusFilterDispatcher filterDispatcher;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportItemToItemDtoConversionService ri2DtoConversionService;

    @RequestMapping(value = "/ordered", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listAllToBeConfirmed(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "filter", required = false) String filters)
                    throws Exception {

        Customer customer = null;
        PageRequest pageable = new PageRequest((page - 1), limit);
        Map<String, String> filterMap = new HashMap<String, String>();
        if (filters != null) filterMap = JsonSerializationHelper
                .deserializeFiltersJson(filters);
        Page<ItemDto> ordered;

        if (containsFilter(filterMap, CUSTOMER_FILTER)) {
            customer = customerRepo.findByCustomerNumber(Long.parseLong(filterMap
                    .get(CUSTOMER_FILTER)));
            if (customer == null) throw new IllegalArgumentException(
                    "Kunde mit gegebener Id nicht gefunden");
            ordered = reportingService.toBeConfirmedByCustomer(
                    customer,
                    pageable);
        }
        else {
            ordered = reportingService.toBeConfirmed(pageable);
        }
        return ExtJsResponseCreator.createResponse(ordered);
    }

    @RequestMapping(value = "/listAllToBeProcessed", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listAllToBeProcessed(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "filter", required = true) String filters)
                    throws Exception {

        PageRequest pageable = new PageRequest((page - 1), limit);
        HashMap<String, String> filterMap = JsonSerializationHelper
                .deserializeFiltersJson(filters);
        ProductionState state = mapFilterToProcessStep(filterMap);

        Set<Specification<ReportItem>> specs = new HashSet<Specification<ReportItem>>();
        specs.add(filterDispatcher.dispatchStatus(state));

        if (containsFilter(filterMap, CUSTOMER_FILTER)) {
            specs.add(new HasCustomerSpecification(retrieveCustomerSafely(filterMap.get(CUSTOMER_FILTER))));
        }

        Page<ItemDto> missingItems = reportingService.createMissing(
                new PageRequest((page - 1), limit),
                combineSpecsToOne(specs));

        if (state == ProductionState.SHIPPED) {
            missingItems = addShippingCosts(pageable, missingItems);
        }

        // TODO: replace this workaround with spec
        if (containsFilter(filterMap, "asdf")) {
            removeAgreed(missingItems);
        }
        return ExtJsResponseCreator.createResponse(missingItems);

    }

    private Page<ItemDto> addShippingCosts(
            PageRequest pageable,
            Page<ItemDto> openItems) {
        HashSet<String> documentNumbers = getDocumentNumbersOf(openItems);
        for (String documentNumber : documentNumbers) {
            Report report = reportRepository.findByDocumentNumber(documentNumber);
            DeliveryNotes dn = (DeliveryNotes) report;
            List<ItemDto> shippingCosts = new ArrayList<ItemDto>();
            if (dn.hasShippingCosts()) {
                shippingCosts.add(ri2DtoConversionService.createShippingCosts(dn));
            }
            List<ItemDto> temp = shippingCosts;
            shippingCosts.addAll(openItems.getContent());
            openItems = new PageImpl<ItemDto>(temp, pageable, openItems.getTotalElements());
        }
        return openItems;
    }

    private HashSet<String> getDocumentNumbersOf(Page<ItemDto> openItems) {
        HashSet<String> documentNumbers = new HashSet<String>();
        for (ItemDto item : openItems) {
            documentNumbers.add(item.getDeliveryNotesNumber());
        }
        return documentNumbers;
    }

    private ProductionState mapFilterToProcessStep(HashMap<String, String> filterMap) {
        return ProductionState.mapFromString(filterMap.get(STATUS_STRING));
    }

    private void removeAgreed(Page<ItemDto> openItems) {
        for (Iterator<ItemDto> iterator = openItems.iterator(); iterator.hasNext();) {
            ItemDto item = iterator.next();
            if (!item.isAgreed()) {
                iterator.remove();
            }
        }
    }

    private Specification<ReportItem> combineSpecsToOne(Set<Specification<ReportItem>> specs) {
        Iterator<Specification<ReportItem>> itr = specs.iterator();
        Specifications<ReportItem> combinedSpecifications = where(itr.next());
        while (itr.hasNext())
            combinedSpecifications = combinedSpecifications.and(itr.next());
        return combinedSpecifications;
    }

    @RequestMapping(value = "/listInvoiceNumbers", method = RequestMethod.GET)
    public @ResponseBody JsonObjectResponse listInvoiceNumbers(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = true) Integer limit,
            @RequestParam(value = "sort", required = false) String sorts,
            @RequestParam(value = "filter", required = false) String filters)
                    throws Exception {

        throw new RuntimeException("Not implemented yet");
    }

    private Customer retrieveCustomerSafely(String customerNo) {
        Customer customer = customerRepo.findByCustomerNumber(
                Long.parseLong(customerNo));
        if (customer == null) throw new NotFoundException(
                "Kunde mit gegebener Id nicht gefunden");
        return customer;
    }

    private boolean containsFilter(Map<String, String> filterMap, String key) {
        return filterMap != null && filterMap.containsKey(key)
                && filterMap.get(key) != null;
    }

}
