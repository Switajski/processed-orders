package de.switajski.priebes.flexibleorders.domain.report;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.reference.Currency;
import de.switajski.priebes.flexibleorders.service.process.parameter.DeliverParameter;

@Entity
public class DeliveryNotes extends Report {

    @AttributeOverrides({
            @AttributeOverride(name = "name1", column = @Column(name = "shipped_name1")),
            @AttributeOverride(name = "name2", column = @Column(name = "shipped_name2")),
            @AttributeOverride(name = "street", column = @Column(name = "shipped_street")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "shipped_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "shipped_city")),
            @AttributeOverride(name = "country", column = @Column(name = "shipped_country")) })
    private Address shippedAddress;

    private Amount shippingCosts;

    private String packageNumber, trackNumber;

    @Embedded
    private DeliveryMethod deliveryMethod;

    private Boolean showPrices;

    public DeliveryNotes() {}

    public DeliveryNotes(DeliverParameter deliverParameter) {
        super(deliverParameter.getDeliveryNotesNumber());
        setCreated(deliverParameter.getCreated() == null ? new Date() : Date.from(deliverParameter.getCreated().atStartOfDay().atZone(
                ZoneId.systemDefault()).toInstant()));
        setShippingCosts(new Amount(deliverParameter.getShipment(), Currency.EUR));
        setDeliveryMethod(deliverParameter.getDeliveryMethod());
        setShowPrices(deliverParameter.isShowPricesInDeliveryNotes());
        setPackageNumber(deliverParameter.getPackageNumber());
        setTrackNumber(deliverParameter.getTrackNumber());
    }

    public Amount getNetAmount() {
        Amount summed = new Amount();
        for (ReportItem he : this.getItems())
            summed.add(he.getOrderItem().getNegotiatedPriceNet());
        return summed;
    }

    public Address getShippedAddress() {
        return shippedAddress;
    }

    public void setShippedAddress(Address shippedAddress) {
        this.shippedAddress = shippedAddress;
    }

    public Amount getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(Amount shippingCosts) {
        this.shippingCosts = shippingCosts;
    }

    public boolean hasShippingCosts() {
        if (getShippingCosts() == null) return false;
        if (getShippingCosts().isGreaterZero()) return true;
        return false;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    @JsonIgnore
    public Set<ShippingItem> getShippingItems() {
        Set<ShippingItem> shippingItems = new HashSet<ShippingItem>();
        for (ReportItem reportItem : this.items) {
            shippingItems.add((ShippingItem) reportItem);
        }
        return shippingItems;
    }

    public boolean isShowPrices() {
        return showPrices;
    }

    public void setShowPrices(Boolean showPrices) {
        this.showPrices = showPrices;
    }

    @JsonIgnore
    public Set<PendingItem> getPendingItems() {
        Set<PendingItem> pis = new HashSet<PendingItem>();
        for (ReportItem pi : items) {
            if (pi instanceof PendingItem) pis.add((PendingItem) pi);
        }
        return pis;

    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public void setPackageNumber(String packageNumber) {
        this.packageNumber = packageNumber;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

}
