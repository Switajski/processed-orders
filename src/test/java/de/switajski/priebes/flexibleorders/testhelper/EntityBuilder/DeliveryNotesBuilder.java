package de.switajski.priebes.flexibleorders.testhelper.EntityBuilder;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;
import de.switajski.priebes.flexibleorders.domain.embeddable.DeliveryMethod;
import de.switajski.priebes.flexibleorders.domain.report.DeliveryNotes;

/**
 * @author Marek Switajski
 *
 */
public class DeliveryNotesBuilder extends ReportBuilder<DeliveryNotes, Builder<DeliveryNotes>> {

    private Address shippedAddress;
    private Amount shippingCosts;
    private DeliveryMethod deliveryMethod;

    @Override
    public DeliveryNotes build() {
        DeliveryNotes dn = new DeliveryNotes();
        super.build(dn);
        dn.setShippedAddress(shippedAddress);
        dn.setShippingCosts(shippingCosts);
        dn.setDeliveryMethod(deliveryMethod);
        return dn;
    }

    public DeliveryNotesBuilder setShippedAddress(Address shippedAddress) {
        this.shippedAddress = shippedAddress;
        return this;
    }

    public DeliveryNotesBuilder setShippingCosts(Amount shippingCosts) {
        this.shippingCosts = shippingCosts;
        return this;
    }

    public DeliveryNotesBuilder setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        return this;
    }
}
