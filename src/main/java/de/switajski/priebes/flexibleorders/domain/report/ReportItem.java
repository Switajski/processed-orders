package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class ReportItem extends GenericEntity implements
		Comparable<ReportItem> {

	@NotNull
	private Integer quantity;

	@ManyToOne(fetch = FetchType.EAGER)
	protected Report report;

	@NotNull
	@ManyToOne
	private OrderItem orderItem;

	protected ReportItem() {
	}

	// TODO: refactor to no constructor - use factory instead
	public ReportItem(Report report, OrderItem item,
			Integer quantity, Date created) {
		if (report == null || item == null || quantity == null)
			throw new IllegalArgumentException();
		this.report = report;
		this.orderItem = item;
		this.quantity = quantity;
		setCreated(created);

		if (!orderItem.getReportItems().contains(this))
			orderItem.addReportItem(this);
		if (report.getItems().contains(this))
			return;
		report.addItem(this);
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
		if (report.getItems().contains(this))
			return;
		this.report = report;
		report.addItem(this);
	}

	@JsonIgnore
	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem item) {
		this.orderItem = item;

		// handle bidirectional relationship:
		if (item.getReportItems().contains(this))
			return;
		item.addReportItem(this);
	}

	@Override
	public int compareTo(ReportItem o) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Not quantity left is set!
	 * 
	 * @return
	 * @see OrderItem#toReportItems(ReportItemType)
	 */
	@Deprecated
	public ItemDto toItemDto() {
		// TODO: enhance mapping by a mapping framework
		ItemDto item = new ItemDto();
		item.setDocumentNumber(getReport().getDocumentNumber());

		if (this.getReport() instanceof OrderConfirmation) {
			item.setOrderConfirmationNumber(getReport().getDocumentNumber());
		}
		if (this.getReport() instanceof Invoice) {
			Invoice invoice = (Invoice) this.getReport();
			item.setInvoiceNumber(getReport().getDocumentNumber());
			item.setDeliveryNotesNumber(getReport().getDocumentNumber());
			item.setPaymentConditions(invoice.getPaymentConditions());
		}
		if (this.getReport() instanceof DeliveryNotes) {
			DeliveryNotes deliveryNotes = (DeliveryNotes) this.getReport();
			item.setDeliveryNotesNumber(getReport().getDocumentNumber());
			item.setTrackNumber(deliveryNotes.getTrackNumber());
			item.setPackageNumber(deliveryNotes.getPackageNumber());
		}
		if (this.getReport() instanceof Receipt) {
			item.setReceiptNumber(getReport().getDocumentNumber());
		}
		item.setCreated(getCreated());
		item.setCustomer(getOrderItem().getOrder().getCustomer().getId());
		item.setCustomerNumber(getOrderItem()
				.getOrder()
				.getCustomer()
				.getCustomerNumber());
		item.setCustomerName(getOrderItem()
				.getOrder()
				.getCustomer()
				.getLastName());
		item.setDocumentNumber(this.getReport().getDocumentNumber());
		item.setId(getId());
		item.setOrderNumber(getOrderItem().getOrder().getOrderNumber());
		if (getOrderItem().getNegotiatedPriceNet() != null)
			item.setPriceNet(getOrderItem().getNegotiatedPriceNet().getValue());
		item.setProduct(getOrderItem().getProduct().getProductNumber());
		item.setProductName(getOrderItem().getProduct().getName());
		item.setQuantity(getQuantity());
		item.setStatus(provideStatus());
		return item;
	}

	public abstract String provideStatus();

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.getReport().getDocumentNumber() + " " + getQuantity() + " x "
				+ getOrderItem().getProduct().getName();
	}

}