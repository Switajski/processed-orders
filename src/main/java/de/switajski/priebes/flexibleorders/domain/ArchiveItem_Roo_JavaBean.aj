// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.ArchiveItem;
import de.switajski.priebes.flexibleorders.reference.Country;
import java.util.Date;

privileged aspect ArchiveItem_Roo_JavaBean {
    
    public Date ArchiveItem.getExpectedDelivery() {
        return this.expectedDelivery;
    }
    
    public void ArchiveItem.setExpectedDelivery(Date expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }
    
    public Boolean ArchiveItem.getAnNaeherei() {
        return this.anNaeherei;
    }
    
    public void ArchiveItem.setAnNaeherei(Boolean anNaeherei) {
        this.anNaeherei = anNaeherei;
    }
    
    public String ArchiveItem.getShippingName1() {
        return this.shippingName1;
    }
    
    public void ArchiveItem.setShippingName1(String shippingName1) {
        this.shippingName1 = shippingName1;
    }
    
    public String ArchiveItem.getShippingName2() {
        return this.shippingName2;
    }
    
    public void ArchiveItem.setShippingName2(String shippingName2) {
        this.shippingName2 = shippingName2;
    }
    
    public String ArchiveItem.getShippingStreet() {
        return this.shippingStreet;
    }
    
    public void ArchiveItem.setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }
    
    public String ArchiveItem.getShippingCity() {
        return this.shippingCity;
    }
    
    public void ArchiveItem.setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }
    
    public int ArchiveItem.getShippingPostalCode() {
        return this.shippingPostalCode;
    }
    
    public void ArchiveItem.setShippingPostalCode(int shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }
    
    public Country ArchiveItem.getShippingCountry() {
        return this.shippingCountry;
    }
    
    public void ArchiveItem.setShippingCountry(Country shippingCountry) {
        this.shippingCountry = shippingCountry;
    }
    
    public String ArchiveItem.getInvoiceName1() {
        return this.invoiceName1;
    }
    
    public void ArchiveItem.setInvoiceName1(String invoiceName1) {
        this.invoiceName1 = invoiceName1;
    }
    
    public String ArchiveItem.getInvoiceName2() {
        return this.invoiceName2;
    }
    
    public void ArchiveItem.setInvoiceName2(String invoiceName2) {
        this.invoiceName2 = invoiceName2;
    }
    
    public String ArchiveItem.getInvoiceStreet() {
        return this.invoiceStreet;
    }
    
    public void ArchiveItem.setInvoiceStreet(String invoiceStreet) {
        this.invoiceStreet = invoiceStreet;
    }
    
    public String ArchiveItem.getInvoiceCity() {
        return this.invoiceCity;
    }
    
    public void ArchiveItem.setInvoiceCity(String invoiceCity) {
        this.invoiceCity = invoiceCity;
    }
    
    public int ArchiveItem.getInvoicePostalCode() {
        return this.invoicePostalCode;
    }
    
    public void ArchiveItem.setInvoicePostalCode(int invoicePostalCode) {
        this.invoicePostalCode = invoicePostalCode;
    }
    
    public Country ArchiveItem.getInvoiceCountry() {
        return this.invoiceCountry;
    }
    
    public void ArchiveItem.setInvoiceCountry(Country invoiceCountry) {
        this.invoiceCountry = invoiceCountry;
    }
    
}
