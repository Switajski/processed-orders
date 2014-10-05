package de.switajski.priebes.flexibleorders.web.itextpdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ReportDto;
import de.switajski.priebes.flexibleorders.web.itextpdf.parameter.ExtInfoTableParameter;

@Component
public class DeliveryNotesPdfView extends PriebesIText5PdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ReportDto report = (ReportDto) model.get(ReportDto.class.getSimpleName());

        String date = "Lieferdatum: "
                + dateFormat.format(report.created);
        String packageNo = "Pakete: " + report.documentNumber;
        String customerNo = "Kundennummer: " + report.customerNumber;
        Address adresse = report.shippedAddress;
        String heading = "Lieferschein " + report.documentNumber;

        for (Paragraph p : ReportViewHelper.createAddress(adresse))
            document.add(p);
        
        document.add(ReportViewHelper.createDate(date));

        for (Paragraph p : ReportViewHelper.createHeading(heading))
            document.add(p);

        if (report.customerDetails == null) {
            document.add(ReportViewHelper.createInfoTable(
                    packageNo, customerNo, "", date));
        }
        else {
            ExtInfoTableParameter param = new ExtInfoTableParameter();
            ReportViewHelper.mapDocumentNumbersToParam(report.deliveryHistory, param);
            param.customerDetails = report.customerDetails;
            param.expectedDelivery = ExpectedDeliveryStringCreator.createDeliveryWeekString(
                    report.expectedDelivery, report.deliveryHistory);
            param.date = date;
            param.customerNo = customerNo;
            param.purchaseAgreement = report.purchaseAgreement;

            document.add(ReportViewHelper.createExtInfoTable(param));
        }

        document.add(ParagraphBuilder.createEmptyLine());
        // insert main table
        document.add(ReportViewHelper.createExtendedTable(report));

    }

}
