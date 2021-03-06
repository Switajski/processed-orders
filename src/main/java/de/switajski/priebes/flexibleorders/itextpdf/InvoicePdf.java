package de.switajski.priebes.flexibleorders.itextpdf;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.switajski.priebes.flexibleorders.itextpdf.builder.CustomPdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.builder.InvoiceCalculation;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ParagraphBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.parameter.ExtInfoTableParameter;
import de.switajski.priebes.flexibleorders.itextpdf.table.ExtendedTableHeaderCreator;
import de.switajski.priebes.flexibleorders.itextpdf.table.TableForInvoiceCreator;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

public class InvoicePdf implements PdfDocumentAppender {

    PdfUtils pdfUtils;
    private ReportInPdf report;
    private PdfWriter writer;

    public InvoicePdf(ReportInPdf invoiceDto, PdfWriter writer) {
        this.report = invoiceDto;
        this.writer = writer;
        this.pdfUtils = new PdfUtils();
    }

    @Override
    public void append(Document document) throws DocumentException {

        InvoiceCalculation calculation = new InvoiceCalculation(report);

        ExtInfoTableParameter param = new ExtInfoTableParameter(report);
        param.billing = StringUtils.isEmpty(report.invoiceSpecific_billing) ? "" : "Abrechnung: " + report.invoiceSpecific_billing;

        document.add(new ExtendedTableHeaderCreator().create(param));

        document.add(ParagraphBuilder.createEmptyLine());

        // insert main table
        document.add(new TableForInvoiceCreator().create(report));

        // insert footer table
        CustomPdfPTableBuilder footerBuilder = CustomPdfPTableBuilder
                .createFooterBuilder(calculation, report.orderConfirmationSpecific_paymentConditions)
                .withTotalWidth(BusinessLetterPdfTemplate.WIDTH);

        if (report.orderConfirmationSpecific_paymentConditions != null) {
            pdfUtils.addPaymentConditions(report.orderConfirmationSpecific_paymentConditions, footerBuilder);
        }

        PdfPTable footer = footerBuilder.build();

        footer.writeSelectedRows(
                0,
                -1,
                BusinessLetterPdfTemplate.PAGE_MARGIN_LEFT, // xPos
                footerYPos(), // yPos
                writer.getDirectContent());

    }

    protected int footerYPos() {
        return BusinessLetterPdfTemplate.PAGE_MARGIN_BOTTOM + BusinessLetterPdfTemplate.FOOTER_MARGIN_BOTTOM;
    }

}
