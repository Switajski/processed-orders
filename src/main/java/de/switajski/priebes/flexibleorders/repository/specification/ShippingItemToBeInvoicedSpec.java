package de.switajski.priebes.flexibleorders.repository.specification;

import de.switajski.priebes.flexibleorders.domain.report.ConfirmationItem;
import de.switajski.priebes.flexibleorders.domain.report.InvoiceItem;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;

public class ShippingItemToBeInvoicedSpec extends AbstractOpenReportItemSpecification {

    @Override
    Class<? extends ReportItem> getReportItemClassToRetrieve() {
        return ConfirmationItem.class;
    }
    
	@Override
	Class<? extends ReportItem> getReportItemClassToSubtract() {
		return InvoiceItem.class;
	}

}
