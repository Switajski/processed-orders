package de.switajski.priebes.flexibleorders.service.helper;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.CustomerRepository;
import de.switajski.priebes.flexibleorders.repository.specification.OpenShippingItemSpec;
import de.switajski.priebes.flexibleorders.repository.specification.HasCustomerSpec;
import de.switajski.priebes.flexibleorders.repository.specification.InvoiceItemToBePaidSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ReceiptItemCompletedSpec;
import de.switajski.priebes.flexibleorders.repository.specification.ShippingItemToBeInvoicedSpec;
import de.switajski.priebes.flexibleorders.web.helper.JsonSerializationHelper;

@Controller
public class ReportItemFilterDispatcher {

	private static final String CUSTOMER_NO = "customer";

	private static final String STATUS = "status";

	@Autowired
	private CustomerRepository customerRepo;

	public Specifications<ReportItem> dispatchToSpecifications(String filters)
			throws Exception {
		HashMap<String, String> filterMap = JsonSerializationHelper
				.deserializeFiltersJson(filters);
		validate(filterMap);

		Specifications<ReportItem> spec = dispatchStatus(filterMap.get(STATUS));

		if (containsCustomer(filterMap))
			spec = spec.and(new HasCustomerSpec(
					retrieveCustomerSafely(filterMap.get(CUSTOMER_NO))));

		return spec;
	}

	private void validate(HashMap<String, String> filterMap) {
		if (filterMap.get(STATUS) == null)
			throw new IllegalArgumentException("Keinen Statusfilter angegeben");
	}

	private Customer retrieveCustomerSafely(String customerNo) {
		Customer customer = customerRepo.findOne(
				Long.parseLong(customerNo));
		if (customer == null)
			throw new IllegalArgumentException(
					"Kunde mit gegebener Id nicht gefunden");
		return customer;
	}

	private boolean containsCustomer(HashMap<String, String> filterMap) {
		return filterMap != null && filterMap.containsKey(CUSTOMER_NO)
				&& filterMap.get(CUSTOMER_NO) != null;
	}

	public static Specifications<ReportItem> dispatchStatus(String status) {
		Specifications<ReportItem> spec = null;
		if (status.equals("ordered"))
			throw new IllegalArgumentException(
					"Filter 'ordered' cannot be applied to Reports, but Orders");
		if (status.equals("ship") || status.equals("confirmed"))
			spec = where(new OpenShippingItemSpec());
		if (status.equals("shipped"))
			spec = where(new ShippingItemToBeInvoicedSpec());
		if (status.equals("invoiced"))
			spec = where(new InvoiceItemToBePaidSpec());
		if (status.equals("completed"))
			spec = where(new ReceiptItemCompletedSpec());
		if (spec == null)
			throw new IllegalArgumentException("Status nicht angegeben");
		return where(spec);
	}

}
