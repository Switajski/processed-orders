package de.switajski.priebes.flexibleorders.web.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import de.switajski.priebes.flexibleorders.domain.OrderItem;

@JsonAutoDetect
public class OrderDto {

    private String id;

    private Long customer;

    private Set<OrderItem> items = new HashSet<OrderItem>();

    private Double vatRate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }

    public Double getVatRate() {
        return vatRate;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }
}
