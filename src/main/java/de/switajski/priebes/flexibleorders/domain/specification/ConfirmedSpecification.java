package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.Embeddable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

import de.switajski.priebes.flexibleorders.domain.FlexibleOrder;
import de.switajski.priebes.flexibleorders.domain.HandlingEvent;
import de.switajski.priebes.flexibleorders.domain.HandlingEventType;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

/**
 * Defines and validates an item in a confirmed state.</br>
 *   
 * @author Marek Switajski
 *
 */
@Embeddable
public class ConfirmedSpecification extends ItemSpecification{

	private Boolean sendOrderConfirmationEmail;
	
	private Boolean sendOrderConfirmationLetter;
	
	protected ConfirmedSpecification() {}

	/**
	 * 
	 * 
	 * @param sendEmail
	 * @param sendLetter
	 */
	public ConfirmedSpecification( 
			boolean sendEmail,
			boolean sendLetter
			) {
		this.sendOrderConfirmationEmail = sendEmail;
		this.sendOrderConfirmationLetter = sendLetter;
	}
	
	public boolean isSatisfiedBy(final OrderItem item){
		//TODO: take account to executed tasks (includeTaskExecuted) 
		if (item.getDeliveryHistory().isEmpty()) return false;
		for (HandlingEvent he: item.getAllHesOfType(HandlingEventType.CONFIRM)){
			if (he.getReport() == null || he.getOrderConfirmation() == null ||
					he.getOrderConfirmation().getInvoiceAddress() == null ||
					//TODO: check VAT_RATE
					item.getNegotiatedPriceNet() == null ||
					item.getOrder().getCustomer() == null) 
				return false;
		}
		
		int confirmedQuantity = this.getHandledQuantityFromEvents(item, HandlingEventType.CONFIRM);
		if (confirmedQuantity != item.getOrderedQuantity()) return false;
		return true;
	}
	
	@Override
	public Predicate toPredicate(Root<OrderItem> root,
			CriteriaQuery<?> query, CriteriaBuilder cb) {
		SetJoin<OrderItem, HandlingEvent> heJoin = root.joinSet("deliveryHistory");
		
		Predicate confirmedPred = cb.and(
				cb.isNotNull(root.<FlexibleOrder>get("flexibleOrder")),
				cb.equal(heJoin.<HandlingEventType>get("type"), cb.literal(HandlingEventType.CONFIRM)),
				cb.isNotNull(heJoin.get("report")),
				//TODO add deliveryNotes check
//				cb.greaterThan(root.get("flexibleOrder").get("orderedQuantity").as(Integer.class), 0),
				cb.isNotNull(root.get("product").get("name")),
				cb.isNotNull(root.get("product").get("productNumber")),
				cb.isNotNull(root.get("negotiatedPriceNet"))
				);
		return confirmedPred;
	}

	public boolean isSendEmail() {
		return sendOrderConfirmationEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendOrderConfirmationEmail = sendEmail;
	}

	public boolean isSendLetter() {
		return sendOrderConfirmationLetter;
	}

	public void setSendLetter(boolean sendLetter) {
		this.sendOrderConfirmationLetter = sendLetter;
	}

}