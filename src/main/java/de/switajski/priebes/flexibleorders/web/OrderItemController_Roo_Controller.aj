// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.web;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.repository.ProductRepository;
import de.switajski.priebes.flexibleorders.service.CustomerService;
import de.switajski.priebes.flexibleorders.service.OrderItemService;
import de.switajski.priebes.flexibleorders.web.OrderItemController;
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

privileged aspect OrderItemController_Roo_Controller {
    
    @Autowired
    OrderItemService OrderItemController.orderItemService;
    
    @Autowired
    CustomerService OrderItemController.customerService;
    
    @Autowired
    ProductRepository OrderItemController.productRepository;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String OrderItemController.create(@Valid OrderItem orderItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, orderItem);
            return "orderitems/create";
        }
        uiModel.asMap().clear();
        orderItemService.saveOrderItem(orderItem);
        return "redirect:/orderitems/" + encodeUrlPathSegment(orderItem.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String OrderItemController.createForm(Model uiModel) {
        populateEditForm(uiModel, new OrderItem());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (customerService.countAllCustomers() == 0) {
            dependencies.add(new String[] { "customer", "customers" });
        }
        if (productRepository.count() == 0) {
            dependencies.add(new String[] { "product", "products" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "orderitems/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String OrderItemController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("orderitem", orderItemService.findOrderItem(id));
        uiModel.addAttribute("itemId", id);
        return "orderitems/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String OrderItemController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("orderitems", orderItemService.findOrderItemEntries(firstResult, sizeNo));
            float nrOfPages = (float) orderItemService.countAllOrderItems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("orderitems", orderItemService.findAllOrderItems());
        }
        addDateTimeFormatPatterns(uiModel);
        return "orderitems/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String OrderItemController.update(@Valid OrderItem orderItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, orderItem);
            return "orderitems/update";
        }
        uiModel.asMap().clear();
        orderItemService.updateOrderItem(orderItem);
        return "redirect:/orderitems/" + encodeUrlPathSegment(orderItem.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String OrderItemController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, orderItemService.findOrderItem(id));
        return "orderitems/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String OrderItemController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        OrderItem orderItem = orderItemService.findOrderItem(id);
        orderItemService.deleteOrderItem(orderItem);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/orderitems";
    }
    
    void OrderItemController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("orderItem_created_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("orderItem_expecteddelivery_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void OrderItemController.populateEditForm(Model uiModel, OrderItem orderItem) {
        uiModel.addAttribute("orderItem", orderItem);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("customers", customerService.findAllCustomers());
        uiModel.addAttribute("products", productRepository.findAll());
        uiModel.addAttribute("statuses", Arrays.asList(Status.values()));
    }
    
    String OrderItemController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
