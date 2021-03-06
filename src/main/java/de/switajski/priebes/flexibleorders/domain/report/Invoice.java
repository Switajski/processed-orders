package de.switajski.priebes.flexibleorders.domain.report;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.embeddable.Amount;

@Entity
public class Invoice extends Report {

    private String discountText;

    private BigDecimal discountRate;

    @NotNull
    private Address invoiceAddress;

    /**
     * net shipping costs
     */
    @Embedded
    private Amount shippingCosts;

    /**
     * Date on which due date is calculated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date evaluationDate;

    private String billing;

    public Invoice() {}

    public Invoice(String invoiceNumber, Address invoiceAddress) {
        super(invoiceNumber);
        this.invoiceAddress = invoiceAddress;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(Address invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public Amount getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(Amount shippingCosts) {
        this.shippingCosts = shippingCosts;
    }

    public void addShippingCosts(Amount shippingCost) {
        this.shippingCosts.add(shippingCost);
    }

    @Override
    public double getVatRate() {
        return Order.VAT_RATE;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public String getDiscountText() {
        return discountText;
    }

    public void setDiscountText(String discountText) {
        this.discountText = discountText;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

}
