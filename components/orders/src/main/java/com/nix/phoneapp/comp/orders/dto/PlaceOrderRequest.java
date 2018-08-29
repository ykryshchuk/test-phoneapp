package com.nix.phoneapp.comp.orders.dto;

import java.util.Collections;
import java.util.List;

public class PlaceOrderRequest {

	private final Customer customer;
	
	private final List<OrderItem> items;
	
	public PlaceOrderRequest(final Customer customer, final List<OrderItem> items) {
		this.customer = customer;
		this.items = Collections.unmodifiableList(items);
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public List<OrderItem> getItems() {
		return items;
	}
	
}
