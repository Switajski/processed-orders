package de.switajski.priebes.flexibleorders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.switajski.priebes.flexibleorders.domain.ConfirmationReport;
import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.DeliveryNotes;
import de.switajski.priebes.flexibleorders.domain.Invoice;
import de.switajski.priebes.flexibleorders.domain.Order;
import de.switajski.priebes.flexibleorders.domain.Receipt;
import de.switajski.priebes.flexibleorders.domain.Report;
import de.switajski.priebes.flexibleorders.domain.ReportItem;
import de.switajski.priebes.flexibleorders.repository.OrderRepository;
import de.switajski.priebes.flexibleorders.repository.ReportRepository;
import de.switajski.priebes.flexibleorders.service.helper.ReportFilterHelper;
import de.switajski.priebes.flexibleorders.web.dto.ItemDto;

@Service
public class ReportItemServiceImpl {

	private OrderRepository orderRepo;
	private ReportRepository reportRepo;
	private ItemDtoConverterService itemDtoConverterService;

	@Autowired
	public ReportItemServiceImpl(
			OrderRepository orderRepo,
			ReportRepository reportRepo,
			ItemDtoConverterService itemDtoConverterService) {
		this.orderRepo = orderRepo;
		this.reportRepo = reportRepo;
		this.itemDtoConverterService = itemDtoConverterService;
	}

	/**
	 * 
	 * @param pageRequest
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllCompleted(PageRequest pageRequest) {
		Page<Report> toBeCompleted = reportRepo.findAllCompleted(
				pageRequest);
		List<Report> invoicesToBePaid = ReportFilterHelper.filter(
				toBeCompleted.getContent(),
				Receipt.class);
		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageRequest,
				itemDtoConverterService.convert(invoicesToBePaid));
	}

	/**
	 * 
	 * @param customer
	 * @param pageRequest
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllCompleted(Customer customer,
			PageRequest pageRequest) {
		Page<Report> toBeCompleted = reportRepo.findAllCompletedByCustomer(
				customer.getCustomerNumber(),
				pageRequest);
		List<Report> invoicesToBePaid = ReportFilterHelper.filter(
				toBeCompleted.getContent(),
				Receipt.class);

		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageRequest,
				itemDtoConverterService.convert(invoicesToBePaid));

	}

	/**
	 * 
	 * @param customer
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeConfirmedByCustomer(Customer customer,
			Pageable pageable) {
		Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmedByCustomer(
				customer,
				pageable);
		return createPageImpl(
				toBeConfirmed.getTotalElements(),
				pageable,
				itemDtoConverterService.convertOrders(toBeConfirmed
						.getContent()));
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeConfirmed(PageRequest pageable) {
		Page<Order> toBeConfirmed = orderRepo.findAllToBeConfirmed(pageable);
		return createPageImpl(
				toBeConfirmed.getTotalElements(),
				pageable,
				itemDtoConverterService.convertOrders(toBeConfirmed
						.getContent()));
	}

	/**
	 * retrieves all report item to be shipped. Paging is fixed on reports.
	 * 
	 * @param customer
	 * @param pageable
	 * @return empty page if none found
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeShipped(Customer customer,
			PageRequest pageable) {
		Page<Report> toBeShipped = reportRepo
				.findAllToBeShippedByCustomerNumber(
						customer.getCustomerNumber(), pageable);
		List<Report> confirmationReportsToBeShipped = ReportFilterHelper.filter(
				toBeShipped.getContent(),
				ConfirmationReport.class);
		return createPageImpl(
				toBeShipped.getTotalElements(),
				pageable,
				itemDtoConverterService.convert(confirmationReportsToBeShipped));
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeShipped(PageRequest pageable) {
		Page<Report> toBeShipped = reportRepo.findAllToBeShipped(pageable);
		List<Report> confirmationReportsToBeShipped = ReportFilterHelper.filter(
				toBeShipped.getContent(),
				ConfirmationReport.class);
		return createPageImpl(
				toBeShipped.getTotalElements(),
				pageable,
				itemDtoConverterService.convert(confirmationReportsToBeShipped));
	}

	/**
	 * 
	 * @param customer
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBePaid(Customer customer,
			PageRequest pageable) {
		Page<Report> toBePaid = reportRepo.findAllToBePaidByCustomer(
				customer.getCustomerNumber(),
				pageable);
		List<Report> invoicesToBePaid = ReportFilterHelper.filter(
				toBePaid.getContent(),
				Invoice.class);
		return createPageImpl(
				toBePaid.getTotalElements(),
				pageable,
				itemDtoConverterService.convert(invoicesToBePaid));
	}

	/**
	 * 
	 * @param pageable
	 * @param byOrder
	 * @return
	 */
	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBePaid(PageRequest pageable) {
		Page<Report> toBePaid = reportRepo.findAllToBePaid(pageable);
		List<Report> invoicesToBePaid = ReportFilterHelper.filter(
				toBePaid.getContent(),
				Invoice.class);
		return createPageImpl(
				toBePaid.getTotalElements(),
				pageable,
				itemDtoConverterService.convert(invoicesToBePaid));
	}

	@Transactional(readOnly = true)
	public List<String> retrieveOrderNumbersLike(String orderNumber) {
		List<Order> orders = orderRepo.findByOrderNumberLike(orderNumber);
		return extractOrderNumbers(orders);
	}

	private List<String> extractOrderNumbers(List<Order> orders) {
		List<String> orderNumbers = new ArrayList<String>();
		for (Order order : orders) {
			orderNumbers.add(order.getOrderNumber());
		}
		return orderNumbers;
	}

	@Transactional(readOnly = true)
	public Page<String> retrieveOrderNumbersByCustomer(Customer customer,
			PageRequest pageRequest) {

		Page<Order> orders = orderRepo.findByCustomer(customer, pageRequest);
		Page<String> result = extractOrderNumber(orders);
		return result;
	}

	private Page<String> extractOrderNumber(Page<Order> orders) {
		// if no orders are found return empty list
		if (orders.getSize() < 1)
			return new PageImpl<String>(new ArrayList<String>());

		List<String> ordersList = new ArrayList<String>();
		for (Order order : orders)
			ordersList.add(order.getOrderNumber());

		Page<String> result = new PageImpl<String>(
				ordersList, new PageRequest(
						orders.getSize(),
						orders.getNumber() + 1),
				orders.getTotalElements()
				);
		return result;
	}

	@Transactional(readOnly = true)
	public List<ItemDto> retrieveAllByDocumentNumber(String string) {
		if (string == null)
			throw new IllegalArgumentException(
					"Dokumentennummer nicht angegeben");
		Report report = reportRepo.findByDocumentNumber(string);
		List<ItemDto> reportItems = new ArrayList<ItemDto>();
		for (ReportItem he : report.getItems())
			reportItems.add(itemDtoConverterService.convert(he));
		return reportItems;
	}

	// TODO: move to OrderServiceImpl
	@Transactional(readOnly = true)
	public Order retrieveOrder(String orderNumber) {
		Order order = orderRepo.findByOrderNumber(orderNumber);
		order.getCustomer();
		order.getItems();
		return orderRepo.findByOrderNumber(orderNumber);
	}

	private PageImpl<ItemDto> createPageImpl(Long totalElements,
			Pageable pageable, List<ItemDto> ris) {
		return new PageImpl<ItemDto>(ris, pageable, totalElements);
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeInvoiced(PageRequest pageable) {
		Page<Report> toBeCompleted = reportRepo.findAllToBeInvoiced(
				pageable);
		List<Report> deliveryNotesToBeInvoiced = ReportFilterHelper.filter(
				toBeCompleted.getContent(),
				DeliveryNotes.class);

		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageable,
				itemDtoConverterService.convert(deliveryNotesToBeInvoiced));
	}

	@Transactional(readOnly = true)
	public Page<ItemDto> retrieveAllToBeInvoiced(Customer customer,
			PageRequest pageable) {
		Page<Report> toBeCompleted = reportRepo.findAllToBeInvoicedByCustomer(
				customer.getCustomerNumber(),
				pageable);
		List<Report> deliveryNotesToBeInvoiced = ReportFilterHelper.filter(
				toBeCompleted.getContent(),
				DeliveryNotes.class);

		return createPageImpl(
				toBeCompleted.getTotalElements(),
				pageable,
				itemDtoConverterService.convert(deliveryNotesToBeInvoiced));
	}

}
