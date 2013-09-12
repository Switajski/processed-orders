// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.web;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;
import de.switajski.priebes.flexibleorders.web.ArchiveItemController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ArchiveItemController_Roo_Controller {
    
    @Autowired
    ArchiveItemRepository ArchiveItemController.archiveItemRepository;
    
    @Autowired
    ProductRepository ArchiveItemController.productRepository;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ArchiveItemController.create(@Valid ArchiveItem archiveItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, archiveItem);
            return "archiveitems/create";
        }
        uiModel.asMap().clear();
        archiveItemRepository.save(archiveItem);
        return "redirect:/archiveitems/" + encodeUrlPathSegment(archiveItem.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ArchiveItemController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ArchiveItem());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (customerService.countAllCustomers() == 0) {
            dependencies.add(new String[] { "customer", "customers" });
        }
        if (productRepository.count() == 0) {
            dependencies.add(new String[] { "product", "products" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "archiveitems/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ArchiveItemController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("archiveitem", archiveItemRepository.findOne(id));
        uiModel.addAttribute("itemId", id);
        return "archiveitems/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ArchiveItemController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("archiveitems", archiveItemRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / sizeNo, sizeNo)).getContent());
            float nrOfPages = (float) archiveItemRepository.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("archiveitems", archiveItemRepository.findAll());
        }
        addDateTimeFormatPatterns(uiModel);
        return "archiveitems/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ArchiveItemController.update(@Valid ArchiveItem archiveItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, archiveItem);
            return "archiveitems/update";
        }
        uiModel.asMap().clear();
        archiveItemRepository.save(archiveItem);
        return "redirect:/archiveitems/" + encodeUrlPathSegment(archiveItem.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ArchiveItemController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, archiveItemRepository.findOne(id));
        return "archiveitems/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ArchiveItemController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ArchiveItem archiveItem = archiveItemRepository.findOne(id);
        archiveItemRepository.delete(archiveItem);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/archiveitems";
    }
    
    void ArchiveItemController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("archiveItem_created_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("archiveItem_expecteddelivery_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void ArchiveItemController.populateEditForm(Model uiModel, ArchiveItem archiveItem) {
        uiModel.addAttribute("archiveItem", archiveItem);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("customers", customerService.findAllCustomers());
        uiModel.addAttribute("products", productRepository.findAll());
        uiModel.addAttribute("countrys", Arrays.asList(Country.values()));
        uiModel.addAttribute("statuses", Arrays.asList(Status.values()));
    }
    
    String ArchiveItemController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
