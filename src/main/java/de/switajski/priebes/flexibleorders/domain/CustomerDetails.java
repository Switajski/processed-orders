package de.switajski.priebes.flexibleorders.domain;

import javax.persistence.Embeddable;

@Embeddable
public class CustomerDetails {

	private String vendorNumber, vatIdNo, paymentConditions;
	
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public String getVatIdNo() {
		return vatIdNo;
	}

	public void setVatIdNo(String valueAddedTaxIdNo) {
		this.vatIdNo = valueAddedTaxIdNo;
	}

	public void setPaymentConditions(String paymentConditions) {
		this.paymentConditions = paymentConditions;
	}

	public String getPaymentConditions() {
		return paymentConditions;
	}
}