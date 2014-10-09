package de.switajski.priebes.flexibleorders.service.conversion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.application.DeliveryHistory;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.report.Receipt;
import de.switajski.priebes.flexibleorders.domain.report.Report;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.service.ExpectedDeliveryService;
import de.switajski.priebes.flexibleorders.service.QuantityLeftCalculatorService;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ItemDtoConverterService {

    @Autowired
    QuantityLeftCalculatorService quantityLeftCalculatorService;
    
    @Autowired
    ExpectedDeliveryService edService; 

    @Autowired
    private ReportItemRepository reportItemRepo;

    public List<ItemDto> convertOrderItems(Collection<OrderItem> orderItems) {
        List<ItemDto> items = new ArrayList<ItemDto>();
        for (OrderItem oi : orderItems)
            items.add(convert(oi));
        return items;
    }

    public ItemDto convert(OrderItem orderItem) {
        ItemDto item = new ItemDto();
        Order order = orderItem.getOrder();
        if (order != null) {
            item.customer = order.getCustomer().getId();
            item.customerNumber = order
                    .getCustomer()
                    .getCustomerNumber();
            item.customerName = order.getCustomer().getLastName();
            item.orderNumber = order.getOrderNumber();
        }
        item.created = orderItem.getCreated();
        item.id = orderItem.getId();
        if (orderItem.getNegotiatedPriceNet() != null) item.priceNet = orderItem.getNegotiatedPriceNet().getValue();
        item.product = orderItem.getProduct().getProductNumber();
        item.productName = orderItem.getProduct().getName();
        item.status = DeliveryHistory.of(orderItem).provideStatus();
        item.quantity = orderItem.getOrderedQuantity();
        item.quantityLeft = quantityLeftCalculatorService.calculateLeft(orderItem);
        return item;
    }

    @Transactional(readOnly = true)
    public Set<ReportItem> convert(List<ItemDto> itemDtos) {
        Set<ReportItem> ris = new HashSet<ReportItem>();
        for (ItemDto entry : itemDtos) {
            ReportItem ri = reportItemRepo.findOne(entry.id);
            if (ri != null) ris.add(ri);
        }
        return ris;
    }

    @Transactional(readOnly = true)
    public Map<ReportItem, Integer> mapItemDtosToReportItemsWithQty(Collection<ItemDto> itemDtos) {
        Map<ReportItem, Integer> risWithQty = new HashMap<ReportItem, Integer>();
        for (ItemDto agreementItemDto : itemDtos) {
            ReportItem agreementItem = reportItemRepo.findOne(agreementItemDto.id);
            if (agreementItem == null) throw new IllegalArgumentException("Angegebene Position nicht gefunden");
            risWithQty.put(
                    agreementItem,
                    agreementItemDto.quantityLeft); // TODO: GUI sets
                                                    // quanitityToDeliver at
                                                    // this nonsense
                                                    // parameter
        }
        return risWithQty;
    }

    public Set<ShippingItem> convertToShippingItems(
            List<ItemDto> shippingItemDtos) {
        Set<ShippingItem> sis = new HashSet<ShippingItem>();
        Set<ReportItem> ris = convert(shippingItemDtos);
        for (ReportItem ri : ris)
            if (ri instanceof ShippingItem) sis.add((ShippingItem) ri);
            else throw new IllegalArgumentException("Given ItemDto is not a shipping item");
        return sis;
    }

    public List<ItemDto> convert(Collection<Report> reports) {
        List<ItemDto> ris = new ArrayList<ItemDto>();
        for (Report report : reports) {
            ris.addAll(convertReport(report));
        }
        return ris;
    }

    public List<ItemDto> convertReport(Report report) {
        List<ItemDto> ris = new ArrayList<ItemDto>();
        for (ReportItem ri : report.getItems()) {
            ris.add(convert(ri));
        }
        return ris;
    }

    public ItemDto convert(ReportItem ri) {
        ItemDto item = new ItemDto();
        item.documentNumber = ri.getReport().getDocumentNumber();
        // TODO: instanceof: this is not subject of this class
        if (ri.getReport() instanceof OrderConfirmation) {
            item.orderConfirmationNumber = ri.getReport().getDocumentNumber();
            //TODO: should be done by a service
            OrderConfirmation orderConfirmation = (OrderConfirmation) ri.getReport();
            item.agreed = orderConfirmation.isAgreed();
            PurchaseAgreement pa = orderConfirmation.getPurchaseAgreement();
            if (pa != null) {
                item.expectedDelivery = pa.getExpectedDelivery();
            }
        }
        if (ri.getReport() instanceof Invoice) {
            Invoice invoice = (Invoice) ri.getReport();
            item.invoiceNumber = ri.getReport().getDocumentNumber();
            item.deliveryNotesNumber = ri.getReport().getDocumentNumber();
            item.paymentConditions = invoice.getPaymentConditions();
            item.shareHistory = (DeliveryHistory.of(ri).getInvoiceNumbers().size() > 1) ? true : false;
        }
        if (ri.getReport() instanceof DeliveryNotes) {
            DeliveryNotes deliveryNotes = (DeliveryNotes) ri.getReport();
            item.deliveryNotesNumber = ri.getReport().getDocumentNumber();
            item.trackNumber = deliveryNotes.getTrackNumber();
            item.packageNumber = deliveryNotes.getPackageNumber();
            // TODO refactor to separate class
            item.shareHistory = (DeliveryHistory.of(ri).getDeliveryNotesNumbers().size() > 1) ? true : false;
        }
        if (ri.getReport() instanceof Receipt) {
            item.receiptNumber = ri.getReport().getDocumentNumber();
        }
        item.created = ri.getCreated();
        Order order = ri.getOrderItem().getOrder();
        item.customer = order.getCustomer().getId();
        item.customerNumber = order.getCustomer().getCustomerNumber();
        item.customerName = order.getCustomer().getLastName();
        item.documentNumber = ri.getReport().getDocumentNumber();
        item.id = ri.getId();
        item.orderNumber = order.getOrderNumber();
        if (ri.getOrderItem().getNegotiatedPriceNet() != null) {
            item.priceNet = ri.getOrderItem().getNegotiatedPriceNet().getValue();
        }
        item.product = ri.getOrderItem().getProduct().getProductNumber();
        item.productName = ri.getOrderItem().getProduct().getName();
        item.quantity = ri.getQuantity();
        item.status = ri.provideStatus();
        item.quantityLeft = quantityLeftCalculatorService.calculateLeft(ri);
        return item;
    }

    public List<ItemDto> convert(Order order) {
        List<ItemDto> ois = new ArrayList<ItemDto>();
        for (OrderItem orderItem : order.getItems()) {
            ois.add(convert(orderItem));
        }
        return ois;
    }

    public List<ItemDto> convertOrders(Collection<Order> orders) {
        List<ItemDto> items = new ArrayList<ItemDto>();
        for (Order order : orders) {
            items.addAll(convert(order));
        }
        return items;
    }

    public List<ItemDto> convertReportItems(List<ReportItem> content) {
        List<ItemDto> ris = new ArrayList<ItemDto>();
        for (ReportItem ri : content) {
            ris.add(convert(ri));
        }
        return ris;
    }

}
