package com.nix.phoneapp.comp.orders.dto;

public class OrderItem {

	private String name;

	private long price;

	private int quantity;

	public OrderItem() {
	}

	public OrderItem(long price, int quantity) {
		this.price = price;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public long getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
