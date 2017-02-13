package de.switajski.priebes.flexibleorders.domain.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.GenericEntity;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

@JsonAutoDetect
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@JsonPropertyOrder({ "_id", "created", "type", "typeExtends" })
public abstract class Report extends GenericEntity {

    @Transient
    private static final double VAT_RATE = Order.VAT_RATE;

    /**
     * Natural id of a Report.
     */
    @JsonProperty("_id")
    @NotNull
    @Column(unique = true)
    private String documentNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    protected Set<ReportItem> items = new HashSet<ReportItem>();

    protected Report() {}

    public Report(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getTypeExtends() {
        return Report.class.getSimpleName();
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Set<ReportItem> getItems() {
        return items;
    }

    @JsonIgnore
    public List<ReportItem> getItemsOrdered() {
        List<ReportItem> ris = new ArrayList<ReportItem>(getItems());
        Collections.sort(ris, new Comparator<ReportItem>() {

            @Override
            public int compare(ReportItem r1, ReportItem r2) {
                Product p1 = r1.getOrderItem().getProduct();
                Product p2 = r2.getOrderItem().getProduct();
                if (p1.hasProductNo() && p2.hasProductNo()) return p1.getProductNumber().compareTo(p2.getProductNumber());
                else if (!p1.hasProductNo() && !p2.hasProductNo()) {
                    return p1.getName().compareTo(p2.getName());
                }
                else if (p1.hasProductNo()) {
                    return 1;
                }
                else if (p2.hasProductNo()) {
                    return -1;
                }

                else return 0;

            }

        });
        return ris;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void addItem(ReportItem item) {
        if (items.contains(item)) return;
        items.add(item);
        item.setReport(this);
        item.setPosition(nextPosition());
    }

    public void removeItem(ReportItem item) {
        this.items.remove(item);
    }

    public double getVatRate() {
        return Order.VAT_RATE;
    }

    /**
     * convenience method
     *
     * @return
     */
    @JsonIgnore
    public Collection<Customer> getCustomers() {
        if (!getItems().isEmpty()) {
            Set<Customer> customers = new HashSet<Customer>();
            for (ReportItem ri : getItems())
                customers.add(ri.getCustomer());
            return Collections.unmodifiableCollection(customers);
        }
        else return Collections.emptySet();
    }

    @JsonIgnore
    public Customer getCustomerSafely() {
        Collection<Customer> customers = this.getCustomers();
        if (customers.size() > 1) throw new IllegalStateException("Mehr als einen Kunden f" + Unicode.U_UML + "r gegebene Positionen gefunden");
        else if (customers.size() == 1) return customers.iterator().next();
        throw new IllegalStateException("No customer found");
    }

    public boolean hasConsecutiveDocuments() {
        for (ReportItem ri : getItems()) {
            if (!ri.getSuccessors().isEmpty()) return true;
        }
        return false;
    }

    @PreRemove
    protected void updateCache() {
        items.stream().forEach(ri -> {
            ri.setQuantity(0);
            ri.updateOverdue();
        });
    }

    @JsonIgnore
    public Set<ReportItem> allRelatedReportItems() {
        Set<ReportItem> ris = new HashSet<ReportItem>();
        if (!getItems().isEmpty()) {
            for (ReportItem ri : getItems()) {
                ris.addAll(ri.getOrderItem().getReportItems());
            }
        }
        return Collections.unmodifiableSet(ris);
    }

    public Integer nextPosition() {
        return items.size();
    }

}
