package de.switajski.priebes.flexibleorders.domain;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;

import de.switajski.priebes.flexibleorders.json.CustomerIdDeserializer;
import de.switajski.priebes.flexibleorders.json.CustomerToIdSerializer;
import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;
import de.switajski.priebes.flexibleorders.json.ProductNumberDeserializer;
import de.switajski.priebes.flexibleorders.json.ProductToProductNumberSerializer;
import de.switajski.priebes.flexibleorders.reference.Status;

@JsonAutoDetect
@RooJavaBean
@RooToString
@RooJpaEntity(inheritanceType = "TABLE_PER_CLASS")
public abstract class Item implements Comparable<Item> {

    /**
     */
    @NotNull
    @OneToOne
    private Product product;

    /**
     */
    @NotNull
    @ManyToOne
    private Customer customer;

    /**
     */
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

    /**
     */
    @NotNull
    private int quantity;

    /**
     */
    @Min(0L)
    @NotNull
    private BigDecimal priceNet;

    /**
     */
    @Transient
    private Status status;

    /**
     */
    @NotNull
    private String productName;

    /**
     */
    @NotNull
    private Long productNumber;

    /**
     */
    private Long orderConfirmationNumber;

    /**
     */
    private Long invoiceNumber;

    /**
     */
    private Long accountNumber;

    /**
     */
    @NotNull
    private Long orderNumber;
    
    
    /**
     * Data like productnumber can change over time. 
     * In order to get time-specific data it is nessecary to historize it.
     * 
     * @param item from which data will be copied
     */
    public void historize(Item item){
    	setAccountNumber(item.getAccountNumber());
    	setCustomer(item.getCustomer());
    	setInvoiceNumber(item.getInvoiceNumber());
    	setOrderNumber(item.getOrderNumber());
    	setAccountNumber(item.getAccountNumber());
    	setOrderConfirmationNumber(item.getOrderConfirmationNumber());
    	setPriceNet(item.getPriceNet());
    	setProduct(item.getProduct());
    	setProductName(item.getProductName());
    	setProductNumber(item.getProductNumber());
    	setQuantity(item.getQuantity());
    }

	public void setStatus(Status status) {
        this.status = status;
    }

	@JsonDeserialize(using=ProductNumberDeserializer.class)
	public void setProduct(Product product) {
        this.product = product;
        this.productNumber = product.getProductNumber();
        this.productName = product.getName();
        if (getPriceNet()==null){
        	this.setPriceNet(product.getPriceNet());
        }

    }
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getCreated() {
        return this.created;
    }

	@JsonDeserialize(using=JsonDateDeserializer.class)
	public void setCreated(Date created) {
        this.created = created;
    }

	@JsonSerialize(using=CustomerToIdSerializer.class)
	public Customer getCustomer() {
        return this.customer;
    }

	@JsonDeserialize(using=CustomerIdDeserializer.class)
	public void setCustomer(Customer customer) {
        this.customer = customer;
    }

	@JsonSerialize(using=ProductToProductNumberSerializer.class)
	public Product getProduct() {
        return this.product;
    }

	public Status getStatus() {
        return this.status;
		/*TODO: Status von Ids abh�ngig machen
		 * if (this.accountNumber!=null) return Status.COMPLETED;
        if (this.invoiceNumber!=null) return Status.SHIPPED;
        if (this.orderConfirmationNumber!=null) return Status.CONFIRMED;
        if (this.orderNumber!=null) return Status.ORDERED;
		return null;*/
    }
	
	@Deprecated
	public void stepBackward(){
		if (this.accountNumber!=null){
			setStatus(Status.SHIPPED);
			setAccountNumber(null);
		}
        if (this.invoiceNumber!=null){
        	setStatus(Status.CONFIRMED);
        	setInvoiceNumber(null);
        }
        if (this.orderConfirmationNumber!=null){
        	setStatus(Status.ORDERED);
        	setOrderConfirmationNumber(null);
        }
	}

	/**
	 * @deprecated should not be used - only public because of roo's integration tests
	 * @param accountNumber
	 */
	public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

	
	/**
	 * @deprecated should not be used - only public because of roo's integration tests
	 * @param invoiceNumber
	 */
	public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

	/**
	 * @deprecated should not be used - only public because of roo's integration tests
	 * @param orderConfirmationNumber
	 */
	public void setOrderConfirmationNumber(Long orderConfirmationNumber) {
        this.orderConfirmationNumber = orderConfirmationNumber;
    }

	/**
	 * @deprecated should not be used - only public because of roo's integration tests
	 * @param orderNumber
	 */
	public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }
}
