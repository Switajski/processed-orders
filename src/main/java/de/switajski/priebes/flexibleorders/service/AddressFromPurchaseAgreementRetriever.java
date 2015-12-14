package de.switajski.priebes.flexibleorders.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.PurchaseAgreement;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

@Service
public abstract class AddressFromPurchaseAgreementRetriever {

    @Autowired
    PurchaseAgreementService paService;

    abstract Address chooseAddressOf(PurchaseAgreement purchaseAgreement);

    @Transactional(readOnly = true)
    public Set<Address> retrieve(Collection<ReportItem> reportItems) {
        Set<Address> addresses = new HashSet<Address>();
        for (PurchaseAgreement pa : paService.retrieve(reportItems)) {
            addresses.add(chooseAddressOf(pa));
        }
        return addresses;
    }

}
