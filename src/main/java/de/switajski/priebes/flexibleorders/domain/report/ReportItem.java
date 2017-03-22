package de.switajski.priebes.flexibleorders.domain.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.GenericEntity;
import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.json.OrderItemIdSerializer;
import de.switajski.priebes.flexibleorders.repository.specification.OverdueItemSpecification;
import de.switajski.priebes.flexibleorders.web.helper.LanguageTranslator;

@JsonAutoDetect
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@JsonPropertyOrder({ "position", "created", "type", "quantitiy" })
public abstract class ReportItem extends GenericEntity implements
        Comparable<ReportItem> {

    @JsonIgnore
    @OneToOne(optional = true)
    private ReportItem predecessor;

    @JsonProperty("predecessor")
    @Transient
    private Long predecessorId;

    public Long getPredecessorId() {
        if (predecessor != null) return predecessor.getId();
        else return orderItem.getId();
    }

    @JsonProperty("id")
    public Long getIdForCouchDB() {
        return super.getId();
    }

    private Integer position;

    /**
     * performance improvement by caching overdue in database
     */
    @JsonIgnore
    private Integer overdue;

    @JsonIgnore
    @OneToMany(mappedBy = "predecessor")
    private Set<ReportItem> successors;

    @NotNull
    private Integer quantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    protected Report report;

    @NotNull
    @ManyToOne
    @JsonSerialize(using = OrderItemIdSerializer.class)
    @JsonProperty("rootLineItem")
    private OrderItem orderItem;

    protected ReportItem() {}

    public ReportItem(OrderItem item, Integer quantity, Date created) {
        if (item == null || quantity == null) {
            throw new IllegalArgumentException();
        }
        this.orderItem = item;
        this.quantity = quantity;
        setCreated(created);

        if (!orderItem.getReportItems().contains(this)) orderItem.addReportItem(this);
        if (report != null) {
            if (report.getItems().contains(this)) return;
            report.addItem(this);
        }
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
        if (report.getItems().contains(this)) return;
        report.addItem(this);
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem item) {
        this.orderItem = item;

        // handle bidirectional relationship:
        if (item.getReportItems().contains(this)) return;
        item.addReportItem(this);
    }

    @Override
    public int compareTo(ReportItem o) {
        if (o.hashCode() > this.hashCode()) return 1;
        else if (o.hashCode() == this.hashCode()) return 0;
        else return -1;
    }

    @JsonIgnore
    public abstract String provideStatus();

    @Override
    public String toString() {
        return new StringBuilder("#" + getId() + " " + this.getClass().getSimpleName())
                .append(": ").append(" ").append(getQuantity()).append(" ").append(getOrderItem()).toString();
    }

    public Product getProduct() {
        return orderItem.getProduct();
    }

    /**
     * convenience method
     *
     * @return
     */
    @JsonIgnore
    public Customer getCustomer() {
        return this.getOrderItem().getOrder().getCustomer();
    }

    @PreRemove
    @PreUpdate
    public void updateOverdue() {
        // performance improvement by caching overdue in database.
        for (ReportItem p : predecessors())
            p.overdue = p.overdue();
        overdue = overdue();
    }

    @PrePersist
    protected void validateQuantity() {
        if (quantity < 1) {
            throw new IllegalStateException("Quantity must be at least 1");
        }
        if (sum() > orderItem.getOrderedQuantity()) {
            String word = LanguageTranslator.translate(this);
            StringBuilder messageBuilder = new StringBuilder()
                    .append(this.quantity)
                    .append(" Stk. wurde eingegeben.<br>")
                    .append(orderItem.getOrderedQuantity())
                    .append(" ")
                    .append(orderItem.getProduct().getName())
                    .append(" wurden in ")
                    .append(orderItem.getOrder().getOrderNumber())
                    .append(" bestellt.<br>");
            if (sum() != quantity) {
                messageBuilder.append(sum() - quantity)
                        .append(" Stk. haben bereits andere ")
                        .append(word)
                        .append("(en)")
                        .append("<br>");
            }
            throw new IllegalStateException(
                    messageBuilder.append("<br>").toString());
        }
        updateOverdue();

    }

    private int sum() {
        int sum = 0;
        for (ReportItem specificReportItem : getOrderItem().getReportItems(this.getClass())) {
            sum += specificReportItem.getQuantity();
        }
        return sum;
    }

    public ReportItem getPredecessor() {
        return predecessor;
    }

    /**
     * Retrieves all predecessors of this report item.
     * 
     * @return
     */
    public List<ReportItem> predecessors() {
        List<ReportItem> ris = new ArrayList<ReportItem>();
        ReportItem lPredecessor = predecessor;
        while (lPredecessor != null) {
            ris.add(lPredecessor);
            lPredecessor = lPredecessor.getPredecessor();
        }
        return ris;
    }

    public void setPredecessor(ReportItem predecessor) {
        this.predecessor = predecessor;
    }

    public Set<ReportItem> getSuccessors() {
        return successors;
    }

    public void setSuccessors(Set<ReportItem> successors) {
        this.successors = successors;
    }

    /**
     * @return sum(successors) - quantity
     */
    public int overdue() {
        if (successors == null) return quantity;

        int sum = 0;
        for (ReportItem ri : successors)
            sum += ri.getQuantity();
        return quantity - sum;
    }

    public boolean isOverdue() {
        return new OverdueItemSpecification().test(this);
    }

    /**
     * This attribute is for performance improvements. Use {@link #overdue()}.
     * The getter is for
     * 
     * @return
     */
    protected Integer getOverdue() {
        return overdue;
    }

    /**
     * Setter is only JPA's sake here.</br>
     * 
     * See {@link #overdue()} how to change overdue qty.
     * 
     * @param overdue
     */
    protected void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }
}
