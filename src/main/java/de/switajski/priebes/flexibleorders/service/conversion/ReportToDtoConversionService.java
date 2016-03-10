package de.switajski.priebes.flexibleorders.service.conversion;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.CustomerDetails;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.exceptions.ContradictoryAddressException;
import de.switajski.priebes.flexibleorders.itextpdf.PdfUtils;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;
import de.switajski.priebes.flexibleorders.itextpdf.dto.DeliveryNotesDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.InvoiceDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.OrderConfirmationDto;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportDto;
import de.switajski.priebes.flexibleorders.service.CustomerDetailsService;
import de.switajski.priebes.flexibleorders.service.DeliveryMethodService;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;

@Service
public class ReportToDtoConversionService {

    @Autowired
    ExpectedDeliveryService expectedDeliveryService;
    @Autowired
    CustomerDetailsService customerDetailsService;
    @Autowired
    PurchaseAgreementReadService purchaseAgreementService;
    @Autowired
    DeliveryMethodService deliveryMethodService;
    @Autowired
    CustomerDetailsService customerService;

    /**
     *
     * @param report
     * @return if no converter for given report is defined.
     * @throws ContradictoryAddressException
     * 
     */
    @Transactional(readOnly = true)
    public ReportDto convert(Report report) throws ContradictoryAddressException {

        if (report instanceof DeliveryNotes) return toDto((DeliveryNotes) report);
        else if (report instanceof Invoice) return toDto((Invoice) report);
        else if (report instanceof OrderConfirmation) return toDto((OrderConfirmation) report);

        return null;
    }

    private DeliveryNotesDto toDto(DeliveryNotes report) throws ContradictoryAddressException {
        DeliveryNotesDto dto = new DeliveryNotesDto();
        amendCommonAttributes(report, dto);
        dto.address = retrieveActualShippingAddress(report);
        dto.subject = "Lieferschein " + report.getDocumentNumber();
        dto.date = "Lieferdatum: "
                + PdfUtils.dateFormat.format(report.getCreated());

        dto.shippingSpecific_trackNumber = report.getTrackNumber();
        dto.shippingSpecific_packageNumber = report.getPackageNumber();
        dto.showPricesInDeliveryNotes = report.isShowPrices();
        return dto;
    }

    private InvoiceDto toDto(Invoice invoice) throws ContradictoryAddressException {
        InvoiceDto dto = new InvoiceDto();
        amendCommonAttributes(invoice, dto);
        dto.address = retrieveInvoicingAddress(invoice);
        dto.shippingSpecific_shippingCosts = invoice.getShippingCosts();
        dto.subject = "Rechnung " + invoice.getDocumentNumber();
        dto.date = "Rechnungsdatum: "
                + PdfUtils.dateFormat.format(invoice.getCreated());
        dto.noteOnDate = "Rechnungsdatum gleich Lieferdatum";
        dto.orderConfirmationSpecific_paymentConditions = purchaseAgreementService.retrieveSingle(invoice.getItems()).getPaymentConditions();
        dto.invoiceSpecific_billing = invoice.getBilling();
        dto.invoiceSpecific_discountText = invoice.getDiscountText();
        dto.invoiceSpecific_discountRate = invoice.getDiscountRate();
        return dto;
    }

    private OrderConfirmationDto toDto(OrderConfirmation orderConfirmation) throws ContradictoryAddressException {
        OrderConfirmationDto dto = new OrderConfirmationDto();
        dto.address = retrieveInvoicingAddress(orderConfirmation);
        dto.shippingSpecific_shippingAddress = retrieveShippingAddress(orderConfirmation);
        amendCommonAttributes(orderConfirmation, dto);
        dto.subject = "Auftragsbest" + Unicode.A_UML + "tigung " + orderConfirmation.getDocumentNumber();
        Date oldestOrderDate = oldestOrderDate(orderConfirmation.getItems());
        dto.date = "AB-Datum: " + PdfUtils.dateFormat.format(orderConfirmation.getCreated());
        dto.noteOnDate = "Bestelldatum: " + PdfUtils.dateFormat.format(oldestOrderDate);
        if (orderConfirmation.isAgreed()) dto.orderConfirmationNumber = orderConfirmation.getOrderAgreementNumber();
        dto.orderConfirmationSpecific_paymentConditions = purchaseAgreementService.retrieveSingle(orderConfirmation.getItems()).getPaymentConditions();
        dto.orderConfirmationSpecific_oldestOrderDate = oldestOrderDate;
        return dto;
    }

    private Date oldestOrderDate(Set<ReportItem> items) {
        Date oldestOrderDate = null;
        for (ReportItem item : items) {
            Date created = item.getOrderItem().getOrder().getCreated();
            if (oldestOrderDate == null) {
                oldestOrderDate = created;
            }
            else {
                if (oldestOrderDate.after(created)) {
                    oldestOrderDate = created;
                }
            }
        }
        return oldestOrderDate;
    }

    // TODO: mapShippingAddress is a dirty hack in order to not refactor yet
    private void amendCommonAttributes(Report report, ReportDto dto) throws ContradictoryAddressException {
        dto.created = report.getCreated();
        dto.documentNumber = report.getDocumentNumber();
        dto.items = report.getItems();

        DeliveryHistory dh = DeliveryHistory.of(report);
        dto.related_creditNoteNumbers = dh.relatedCreditNoteNumbers();
        dto.related_deliveryNotesNumbers = dh.relatedDeliveryNotesNumbers();
        dto.related_invoiceNumbers = dh.relatedInvoiceNumbers();
        dto.related_orderAgreementNumbers = dh.relatedOrderAgreementNumbers();
        dto.related_orderNumbers = dh.relatedOrderNumbers();
        dto.related_orderConfirmationNumbers = dh.relatedOrderConfirmationNumbers();

        Collection<Customer> customers = report.getCustomers();
        if (report.getCustomers().size() > 1) {
            throw new IllegalStateException("Mehr als einen Kunden f" + Unicode.U_UML + "r gegebene Positionen gefunden");
        }
        else if (report.getCustomers().size() == 1) {
            Customer customer = customers.iterator().next();
            dto.customerFirstName = customer.getFirstName();
            dto.customerLastName = customer.getLastName();
            dto.customerEmail = customer.getEmail();
            dto.customerPhone = customer.getPhone();
            dto.customerNumber = customer.getCustomerNumber();
        }

        mapCustomerDetails(dto, retrieveCustomerDetails(report));
        dto.shippingSpecific_expectedDelivery = retrieveExpectedDelivery(report);
        dto.shippingSpecific_deliveryMethod = retrieveDeliveryMethod(report);

        dto.vatRate = 0.19d; // TODO: implement VAT-Service

        dto.netGoods = AmountCalculator.sum(AmountCalculator
                .getAmountsTimesQuantity(report.getItems()));

    }

    private DeliveryMethod retrieveDeliveryMethod(Report report) throws ContradictoryAddressException {
        DeliveryMethod dm = null;
        Set<DeliveryMethod> deliveryMethods = deliveryMethodService.retrieve(report.getItems());
        if (deliveryMethods.size() > 1) throw new ContradictoryAddressException("Mehr als eine Zustellungsart f" + Unicode.U_UML
                + "r gegebene Positionen gefunden");
        else if (deliveryMethods.size() == 1) {
            dm = deliveryMethods.iterator().next();
        }
        return dm;
    }

    private LocalDate retrieveExpectedDelivery(Report report) {
        Set<LocalDate> eDates = expectedDeliveryService.retrieve(report.getItems());
        LocalDate expectedDelivery = null;
        // FIXME: shouldn't happen
        if (eDates.size() > 1) expectedDelivery = eDates.iterator().next();
        else if (eDates.size() == 1) expectedDelivery = eDates.iterator().next();
        return expectedDelivery;
    }

    private CustomerDetails retrieveCustomerDetails(Report report) {
        CustomerDetails customerDetails = null;
        Set<CustomerDetails> customerDetailss = customerDetailsService.retrieve(report.getItems());
        if (customerDetailss.size() > 1) throw new IllegalStateException("Widerspr" + Unicode.U_UML + "chliche Kundenstammdaten f" + Unicode.U_UML
                + "r gegebene Positionen gefunden");
        else if (customerDetailss.size() == 1) {
            customerDetails = customerDetailss.iterator().next();
        }
        return customerDetails;
    }

    private void mapCustomerDetails(ReportDto dto, CustomerDetails det) {
        if (det != null) {
            dto.customerSpecific_contactInformation = det.getContactInformation();
            dto.customerSpecific_mark = det.getMark();
            dto.customerSpecific_saleRepresentative = det.getSaleRepresentative();
            dto.customerSpecific_vatIdNo = det.getVatIdNo();
            dto.customerSpecific_vendorNumber = det.getVendorNumber();
        }
    }

    private Address retrieveShippingAddress(Report report) throws ContradictoryAddressException {
        Address shippingAddress = null;
        Set<Address> shippingAddresses = purchaseAgreementService.shippingAddressesWithoutDeviations(report.getItems());
        if (shippingAddresses.size() > 1) {
            throw new ContradictoryAddressException("Mehr als eine Lieferadresse f" + Unicode.U_UML
                    + "r gegebene Positionen gefunden");
        }
        else if (shippingAddresses.size() == 1) shippingAddress = shippingAddresses.iterator().next();
        return shippingAddress;
    }

    private Address retrieveActualShippingAddress(Report report) throws ContradictoryAddressException {
        Address shippingAddress = null;
        Set<Address> shippingAddresses = purchaseAgreementService.shippingAddresses(report.getItems());
        if (shippingAddresses.size() > 1) throw new ContradictoryAddressException("Mehr als eine Lieferadresse f" + Unicode.U_UML
                + "r gegebene Positionen gefunden");
        else if (shippingAddresses.size() == 1) shippingAddress = shippingAddresses.iterator().next();
        return shippingAddress;
    }

    private Address retrieveInvoicingAddress(Report report) {
        Set<Address> invoicingAddresses = purchaseAgreementService.invoiceAddressesWithoutDeviation(report.getItems());
        if (invoicingAddresses.size() > 1) {
            return invoicingAddresses.iterator().next();
            // FIXME: #105
            // throw new ContradictoryPurchaseAgreementException("Mehr als eine
            // Rechungsaddresse f" + Unicode.U_UML
            // + "r gegebene Positionen gefunden");
        }
        else if (invoicingAddresses.size() == 1) {
            return invoicingAddresses.iterator().next();
        }
        return null;
    }

}
