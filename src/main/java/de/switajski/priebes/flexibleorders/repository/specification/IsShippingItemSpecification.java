package de.switajski.priebes.flexibleorders.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;

public class IsShippingItemSpecification implements Specification<ReportItem> {

    @Override
    public Predicate toPredicate(Root<ReportItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.type(), ShippingItem.class);
    }

}
