package com.nix.phoneapp.comp.orders.controller;

import java.util.function.Consumer;

import com.nix.phoneapp.comp.orders.persistence.OrderEntity;

public class OrderConsoleDump implements Consumer<OrderEntity> {

	@Override
	public void accept(OrderEntity order) {
		// Better would be use the dedicated logger instead.
		// But we have the requirement to anyway log to console, and the logger can be
		// reconfigured to skip the logging.
		// So we stay with this ugly call.
		System.out.println("######### Here you go #########");
		System.out.printf("New order placed %s with total price %.2f for %s consisting of %s %n", order.getId(),
				order.getTotalPrice() / 100.0, order.getCustomer(), order.getItems());
	}

}
