package de.switajski.priebes.flexibleorders.itextpdf.table;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

import de.switajski.priebes.flexibleorders.domain.OrderItem;
import de.switajski.priebes.flexibleorders.domain.Product;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.itextpdf.builder.ColumnFormat;
import de.switajski.priebes.flexibleorders.itextpdf.builder.PdfPTableBuilder;
import de.switajski.priebes.flexibleorders.itextpdf.dto.ReportInPdf;
import de.switajski.priebes.flexibleorders.itextpdf.template.BusinessLetterPdfTemplate;

public class TableWithPricesAndWithPackageNumberCreator extends ReportItemsPdfPTableCreator {

    @Override
    public PdfPTable create(ReportInPdf report)
            throws DocumentException {
        PdfPTableBuilder builder = new PdfPTableBuilder(
                tableProperties());
        // Refactor - see #71
        for (ReportItem ri : report.getItemsByOrder()) {
            List<String> list = new ArrayList<String>();
            // Art.Nr.
            OrderItem oi = ri.getOrderItem();
            Product product = oi.getProduct();
            list.add(sku(product.getProductNumber()));
            // Artikel
            list.add(articleNameWithAdditionalInfo(oi.getAdditionalInfo(), product.getName()));
            // Paketnr.
            if (ri instanceof ShippingItem) {
                list.add(((ShippingItem) ri).getPackageNumber());
            }
            else {
                list.add("");
            }
            // Anzahl
            list.add(String.valueOf(ri.getQuantity()));
            // EK per Stueck
            list.add(oi.getNegotiatedPriceNet().toString());
            // Bestellnr
            list.add(oi.getOrder().getOrderNumber());
            // gesamt
            list.add(oi.getNegotiatedPriceNet()
                    .multiply(ri.getQuantity()).toString());

            builder.addBodyRow(list);
        }

        return builder.withFooter(false).build();
    }

    private ArrayList<ColumnFormat> tableProperties() {
        ArrayList<ColumnFormat> rowProperties = new ArrayList<ColumnFormat>();
        rowProperties.add(new ColumnFormat("Art. Nr.", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Artikel", Element.ALIGN_LEFT, 40));
        rowProperties.add(new ColumnFormat("Paketnr.", Element.ALIGN_CENTER, 8));
        rowProperties.add(new ColumnFormat("Anzahl", Element.ALIGN_CENTER, 7));
        rowProperties.add(new ColumnFormat("EK/Stk.", Element.ALIGN_LEFT, 10));
        rowProperties.add(new ColumnFormat("Bestellnr.", Element.ALIGN_LEFT, 18, BusinessLetterPdfTemplate.eightSizeFont));
        rowProperties.add(new ColumnFormat("gesamt", Element.ALIGN_RIGHT, 12));
        return rowProperties;
    }

}
