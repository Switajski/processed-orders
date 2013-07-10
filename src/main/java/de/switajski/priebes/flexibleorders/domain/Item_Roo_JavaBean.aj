// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.Item;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.reference.Status;
import java.math.BigDecimal;
import java.util.Date;

privileged aspect Item_Roo_JavaBean {
    
    public Product Item.getProduct() {
        return this.product;
    }
    
    public void Item.setProduct(Product product) {
        this.product = product;
    }
    
    public Customer Item.getCustomer() {
        return this.customer;
    }
    
    public void Item.setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Date Item.getCreated() {
        return this.created;
    }
    
    public void Item.setCreated(Date created) {
        this.created = created;
    }
    
    public int Item.getQuantity() {
        return this.quantity;
    }
    
    public void Item.setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal Item.getPriceNet() {
        return this.priceNet;
    }
    
    public void Item.setPriceNet(BigDecimal priceNet) {
        this.priceNet = priceNet;
    }
    
    public Status Item.getStatus() {
        return this.status;
    }
    
    public String Item.getProductName() {
        return this.productName;
    }
    
    public void Item.setProductName(String productName) {
        this.productName = productName;
    }
    
    public Long Item.getProductNumber() {
        return this.productNumber;
    }
    
    public void Item.setProductNumber(Long productNumber) {
        this.productNumber = productNumber;
    }
    
    public Long Item.getOrderConfirmationNumber() {
        return this.orderConfirmationNumber;
    }
    
    public void Item.setOrderConfirmationNumber(Long orderConfirmationNumber) {
        this.orderConfirmationNumber = orderConfirmationNumber;
    }
    
    public Long Item.getInvoiceNumber() {
        return this.invoiceNumber;
    }
    
    public void Item.setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    public Long Item.getAccountNumber() {
        return this.accountNumber;
    }
    
    public void Item.setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public Long Item.getOrderNumber() {
        return this.orderNumber;
    }
    
    public void Item.setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }
    
}
