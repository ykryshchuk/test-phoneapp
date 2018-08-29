package com.nix.phoneapp.comp.orders.dto;

import java.util.List;

public class Order {

	private Customer customer;

	private long totalPrice;

	private List<OrderItem> items;

	public Customer getCustomer() {
		return customer;
	}

	public long getTotalPrice() {
		return totalPrice;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setTotalPrice(long totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

}
