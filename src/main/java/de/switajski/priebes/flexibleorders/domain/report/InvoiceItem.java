package de.switajski.priebes.flexibleorders.domain.report;

import java.util.Date;

import javax.persistence.Entity;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@Entity
public class InvoiceItem extends ReportItem {

    public InvoiceItem() {}

    public InvoiceItem(
            Invoice invoice,
            OrderItem orderItem,
            Integer quantityToDeliver,
            Date date) {
        super(orderItem, quantityToDeliver, date);
        if (invoice != null) invoice.addItem(this);
    }

    @Override
    public String provideStatus() {
        return "in Rechnung gestellt";
    }

}
