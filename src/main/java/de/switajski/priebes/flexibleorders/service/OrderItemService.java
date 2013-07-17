package de.switajski.priebes.flexibleorders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.roo.addon.layers.service.RooService;

import de.switajski.priebes.flexibleorders.domain.Customer;
import de.switajski.priebes.flexibleorders.domain.OrderItem;

@RooService(domainTypes = { de.switajski.priebes.flexibleorders.domain.OrderItem.class })
public interface OrderItemService {

	List<OrderItem> findByOrderNumber(Long orderNumber);

	Page<OrderItem> findByOrderNumber(Long orderNumber, Pageable pageable);

	Page<OrderItem> findByOrderItemNumber(Long orderItemNumber, Pageable pageable);
	
	Page<OrderItem> findAll(Pageable pageable);
	
	public abstract long countAllOrderItems();

	
	public abstract void deleteOrderItem(OrderItem orderItem);


	public abstract OrderItem findOrderItem(Long id);


	public abstract List<OrderItem> findAllOrderItems();


	public abstract List<OrderItem> findOrderItemEntries(int firstResult, int maxResults);


	public abstract void saveOrderItem(OrderItem orderItem);


	public abstract OrderItem updateOrderItem(OrderItem orderItem);

}