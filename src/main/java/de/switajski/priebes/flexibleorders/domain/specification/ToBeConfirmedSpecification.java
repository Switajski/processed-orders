package de.switajski.priebes.flexibleorders.domain.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

public class ToBeConfirmedSpecification extends ItemSpecification {

	@Override
	public Predicate toPredicate(Root<OrderItem> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSatisfiedBy(OrderItem item) {
		return !(new ConfirmedSpecification().isSatisfiedBy(item));
	}

}