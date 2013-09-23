package de.switajski.priebes.flexibleorders.report.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.report.Order;

/**
 * Pdf view customized for the display of an order
 * 
 * @author Marek
 *
 */
@Component
public class OrderPdfView extends PriebesIText5PdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Order bestellung =  (Order) model.get("Order");
        insertAdresse(document, bestellung.getCustomer().getInvoiceAddress());
        insertSubject(document,"Bestellung Nr." + bestellung.getOrderNumber());
        insertInfo(document,"Bestelldatum: " + dateFormat.format(bestellung.getCreated()));
        this.insertEmptyLines(document, 1);
        OrderPdfTable table = new OrderPdfTable(bestellung);
        document.add(table.build());

	}

}
