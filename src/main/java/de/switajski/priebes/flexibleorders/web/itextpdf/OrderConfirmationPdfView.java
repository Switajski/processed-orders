package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.application.AmountCalculator;
import de.switajski.priebes.flexibleorders.domain.Address;
import de.switajski.priebes.flexibleorders.domain.Amount;
import de.switajski.priebes.flexibleorders.domain.OrderConfirmation;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.Unicode;

@Component
public class ConfirmationReportPdfView extends PriebesIText5PdfView {

	// TODO: make VAT_RATE dependent from order
	public static final Double VAT_RATE = 0.19d;

	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		OrderConfirmation report = (OrderConfirmation) model
				.get(OrderConfirmation.class.getSimpleName());

		String heading = "Auftragsbest" + Unicode.aUml + "tigung";
		Address adresse = report.getInvoiceAddress();
		String documentNo = "Auftragsnummer: "
				+ report.getDocumentNumber().toString();
		Date expectedDelivery = report.getExpectedDelivery();

		String expectedDeliveryString = "";
		if (expectedDelivery != null)
			expectedDeliveryString = "voraus. Lieferung: KW "
					+ weekDateFormat.format(expectedDelivery);
		
		String date = "Auftragsdatum: "
				+ dateFormat.format(report.getCreated());
		String customerNo = "Kundennummer: " + report.getCustomerNumber();

		Amount netGoods = AmountCalculator.sum(AmountCalculator
				.getAmountsTimesQuantity(report));
		Amount vat = netGoods.multiply(report.getVatRate());
		Amount gross = netGoods.add(vat);

		for (Paragraph p : ReportViewHelper.insertAddress(report
				.getInvoiceAddress())) {
			document.add(p);
		}

		for (Paragraph p : ReportViewHelper.insertHeading(heading)) {
			document.add(p);
		}

		if (report.getCustomerDetails() == null){
			document.add(ReportViewHelper.insertInfoTable(
					customerNo,//rightTop, 
					expectedDeliveryString,//rightBottom, 
					documentNo,//leftTop, 
					date//leftBottom
					));
		} else {

			PdfHelper.insertExtInfoTable(
					document,
					report.getCustomerDetails(),
					report.getShippingAddress(),
					documentNo,
					expectedDeliveryString,
					date,
					customerNo);
		}
		

		document.add(ParagraphBuilder.createEmptyLine());

		// insert main table
		document.add(createTable(report));

		// insert footer table
		CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
				.createFooterBuilder(
						netGoods, vat, null, gross, null)
				.withTotalWidth(PriebesIText5PdfView.WIDTH);

		PdfPTable footer = footerBuilder.build();

		footer.writeSelectedRows(0, -1,
				/* xPos */PriebesIText5PdfView.PAGE_MARGIN_LEFT,
				/* yPos */PriebesIText5PdfView.PAGE_MARGIN_BOTTOM
						+ FOOTER_MARGIN_BOTTOM,
				writer.getDirectContent());
	}

	private PdfPTable createTable(OrderConfirmation cReport)
			throws DocumentException {
		PdfPTableBuilder builder = new PdfPTableBuilder(
				PdfPTableBuilder.createPropertiesWithSixCols());
		for (ReportItem he : cReport.getItemsOrdered()) {
			List<String> list = new ArrayList<String>();
			// Art.Nr.:
			Long pNo = he.getOrderItem().getProduct().getProductNumber();
			list.add(pNo.equals(0L) ? "n.a." : pNo.toString());
			// Artikel
			list.add(he.getOrderItem().getProduct().getName());
			// Anzahl
			list.add(String.valueOf(he.getQuantity()));
			// EK per Stueck
			list.add(he.getOrderItem().getNegotiatedPriceNet().toString());
			// Bestellnr
			list.add(he.getOrderItem().getOrder().getOrderNumber());
			// gesamt
			list.add(he
					.getOrderItem()
					.getNegotiatedPriceNet()
					.multiply(he.getQuantity())
					.toString());

			builder.addBodyRow(list);
		}

		return builder.withFooter(false).build();
	}

}