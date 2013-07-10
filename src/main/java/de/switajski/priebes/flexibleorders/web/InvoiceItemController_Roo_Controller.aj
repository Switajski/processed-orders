// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.web;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.CustomerService;
import de.switajski.priebes.flexibleorders.domain.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.InvoiceItemService;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.ProductService;
import de.switajski.priebes.flexibleorders.reference.Country;
import de.switajski.priebes.flexibleorders.reference.Status;
import de.switajski.priebes.flexibleorders.web.InvoiceItemController;
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

privileged aspect InvoiceItemController_Roo_Controller {
    
    @Autowired
    InvoiceItemService InvoiceItemController.invoiceItemService;
    
    @Autowired
    CustomerService InvoiceItemController.customerService;
    
    @Autowired
    ProductService InvoiceItemController.productService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String InvoiceItemController.create(@Valid InvoiceItem invoiceItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, invoiceItem);
            return "invoiceitems/create";
        }
        uiModel.asMap().clear();
        invoiceItemService.saveInvoiceItem(invoiceItem);
        return "redirect:/invoiceitems/" + encodeUrlPathSegment(invoiceItem.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String InvoiceItemController.createForm(Model uiModel) {
        populateEditForm(uiModel, new InvoiceItem());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (productService.countAllProducts() == 0) {
            dependencies.add(new String[] { "product", "products" });
        }
        if (customerService.countAllCustomers() == 0) {
            dependencies.add(new String[] { "customer", "customers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "invoiceitems/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String InvoiceItemController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("invoiceitem", invoiceItemService.findInvoiceItem(id));
        uiModel.addAttribute("itemId", id);
        return "invoiceitems/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String InvoiceItemController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("invoiceitems", invoiceItemService.findInvoiceItemEntries(firstResult, sizeNo));
            float nrOfPages = (float) invoiceItemService.countAllInvoiceItems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("invoiceitems", invoiceItemService.findAllInvoiceItems());
        }
        addDateTimeFormatPatterns(uiModel);
        return "invoiceitems/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String InvoiceItemController.update(@Valid InvoiceItem invoiceItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, invoiceItem);
            return "invoiceitems/update";
        }
        uiModel.asMap().clear();
        invoiceItemService.updateInvoiceItem(invoiceItem);
        return "redirect:/invoiceitems/" + encodeUrlPathSegment(invoiceItem.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String InvoiceItemController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, invoiceItemService.findInvoiceItem(id));
        return "invoiceitems/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String InvoiceItemController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        InvoiceItem invoiceItem = invoiceItemService.findInvoiceItem(id);
        invoiceItemService.deleteInvoiceItem(invoiceItem);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/invoiceitems";
    }
    
    void InvoiceItemController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("invoiceItem_created_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void InvoiceItemController.populateEditForm(Model uiModel, InvoiceItem invoiceItem) {
        uiModel.addAttribute("invoiceItem", invoiceItem);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("customers", customerService.findAllCustomers());
        uiModel.addAttribute("products", productService.findAllProducts());
        uiModel.addAttribute("countrys", Arrays.asList(Country.values()));
        uiModel.addAttribute("statuses", Arrays.asList(Status.values()));
    }
    
    String InvoiceItemController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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