package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Invoice extends Report {

	private String paymentConditions;
	
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
	
	protected Invoice() {}
	
	public Invoice(String invoiceNumber, String paymentConditions, Address invoiceAddress) {
		super(invoiceNumber);
		this.paymentConditions = paymentConditions;
		this.invoiceAddress = invoiceAddress;
	}
	
	public String getPaymentConditions() {
		return paymentConditions;
	}

	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
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
	
	public void addShippingCosts(Amount shippingCost){
		this.shippingCosts.add(shippingCost);
	}

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
	
	public void setBilling(String billing){
		this.billing = billing;
	}

}