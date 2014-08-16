package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;

/**
 * holds all available attributes for a creation of a order confirmation
 * 
 * @author Marek Switajski
 * 
 */
@Entity
public class OrderConfirmation extends Report {

	@AttributeOverrides({
			@AttributeOverride(name = "name1", column = @Column(name = "invoice_name1")),
			@AttributeOverride(name = "name2", column = @Column(name = "invoice_name2")),
			@AttributeOverride(name = "street", column = @Column(name = "invoice_street")),
			@AttributeOverride(name = "postalCode", column = @Column(name = "invoice_postal_code")),
			@AttributeOverride(name = "city", column = @Column(name = "invoice_city")),
			@AttributeOverride(name = "country", column = @Column(name = "invoice_country"))
	})
	private Address invoiceAddress;

	@AttributeOverrides({
			@AttributeOverride(name = "name1", column = @Column(name = "shipping_name1")),
			@AttributeOverride(name = "name2", column = @Column(name = "shipping_name2")),
			@AttributeOverride(name = "street", column = @Column(name = "shipping_street")),
			@AttributeOverride(name = "postalCode", column = @Column(name = "shipping_postal_code")),
			@AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
			@AttributeOverride(name = "country", column = @Column(name = "shipping_country"))
	})
	private Address shippingAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date expectedDelivery;

	@Embedded
	private CustomerDetails customerDetails;

	public OrderConfirmation() {
	}

	/**
	 * 
	 * @param orderConfirmationNumber
	 * @param invoiceAddress
	 * @param shippingAddress
	 * @param confirmedSpec
	 */
	public OrderConfirmation(
			String orderConfirmationNumber,
			Address invoiceAddress,
			Address shippingAddress) {
		super(orderConfirmationNumber);
		this.invoiceAddress = invoiceAddress;
		this.shippingAddress = shippingAddress;
	}

	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(Date expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}
	
	public void setCustomerDetails(CustomerDetails customerDetails){
		this.customerDetails = customerDetails;
	}

	public CustomerDetails getCustomerDetails() {
		return this.customerDetails;
	}

}