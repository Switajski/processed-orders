package de.switajski.priebes.flexibleorders.service.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.switajski.priebes.flexibleorders.domain.embeddable.Address;
import de.switajski.priebes.flexibleorders.domain.report.Invoice;
import de.switajski.priebes.flexibleorders.domain.report.ReportItem;
import de.switajski.priebes.flexibleorders.domain.report.ShippingItem;
import de.switajski.priebes.flexibleorders.repository.ReportItemRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.PurchaseAgreementReadService;
import de.switajski.priebes.flexibleorders.testdata.ItemDtoShorthand;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.AddressBuilder;
import de.switajski.priebes.flexibleorders.testhelper.EntityBuilder.CatalogProductBuilder;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

public class InvoicingServiceTest {

    @InjectMocks
    InvoicingService invoicingService = new InvoicingService();
    @Mock
    ReportRepository reportRepo;
    @Mock
    PurchaseAgreementReadService purchaseAgreementService;
    @Mock
    ReportItemRepository reportItemRepo;

    InvoicingParameter invoicingParameter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        invoicingParameter = new InvoicingParameter();
        invoicingParameter.setInvoiceNumber("L123");
    }

    @Test
    public void shouldSaveInvoice() throws Exception {
        givenDocumentNumberDoesNotExist();
        givenInvoicingAddresses(1);
        givenItemsInParameter();

        whenValidatingAndInvoicing();

        verify(reportRepo).save(Matchers.any(Invoice.class));

    }

    @Test(expected = IllegalStateException.class)
    public void shouldRejectInvoicingIfNoInvoicingAddressesExist() throws Exception {
        givenDocumentNumberDoesNotExist();
        givenItemsInParameter(1);
        when(reportItemRepo.findOne(1L)).thenReturn(new ShippingItem());
        when(purchaseAgreementService.invoiceAddressesWithoutDeviation(Matchers.anySetOf(ReportItem.class))).thenReturn(java.util.Collections.emptySet());

        whenValidatingAndInvoicing();
    }

    private List<ItemDto> givenItemsInParameter(Integer... integers) {
        List<ItemDto> items = new ArrayList<>();
        for (Integer i : integers) {
            ItemDto item = ItemDtoShorthand.item(
                    CatalogProductBuilder.buildWithGeneratedAttributes(i),
                    i,
                    invoicingParameter.getInvoiceNumber());
            item.setId(Long.valueOf(i.toString()));
            items.add(item);
        }
        invoicingParameter.setItems(items);
        return items;
    }

    private void givenDocumentNumberDoesNotExist() {
        when(reportRepo.findByDocumentNumber(invoicingParameter.getInvoiceNumber())).thenReturn(null);
    }

    private void givenInvoicingAddresses(Integer... integers) {
        Set<Address> as = new HashSet<>();
        for (Integer i : integers)
            as.add(AddressBuilder.buildWithGeneratedAttributes(i));

        when(purchaseAgreementService.invoiceAddresses(Matchers.anySetOf(ReportItem.class))).thenReturn(as);
    }

    private void whenValidatingAndInvoicing() throws Exception {
        invoicingService.invoice(invoicingParameter);
    }

}
