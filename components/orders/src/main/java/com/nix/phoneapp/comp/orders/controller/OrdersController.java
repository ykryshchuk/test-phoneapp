package com.nix.phoneapp.comp.orders.controller;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nix.phoneapp.comp.orders.dto.Order;
import com.nix.phoneapp.comp.orders.dto.OrderItem;
import com.nix.phoneapp.comp.orders.dto.PlaceOrderRequest;
import com.nix.phoneapp.comp.orders.persistence.CustomerEntity;
import com.nix.phoneapp.comp.orders.persistence.OrderEntity;
import com.nix.phoneapp.comp.orders.persistence.OrderItemEntity;
import com.nix.phoneapp.comp.orders.persistence.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrdersController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

	@Autowired
	private OrderRepository orderRepository;

	@Resource(name = "orderItemValidator")
	private Predicate<OrderItem> validator;

	@Resource(name = "orderDump")
	private Consumer<OrderEntity> orderDump;

	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("/{orderId}")
	public Order findOrder(@PathVariable(name = "orderId") final Integer orderId) {
		LOGGER.debug("Asked for an order {}", orderId);
		final OrderEntity order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
		return modelMapper.map(order, Order.class);
	}

	@PostMapping
	public Integer placeOrder(@RequestBody final PlaceOrderRequest request) {
		final OrderEntity order = new OrderEntity();
		order.setCustomer(modelMapper.map(request.getCustomer(), CustomerEntity.class));
		final List<OrderItemEntity> orderItems = request.getItems().stream()
				// Validate the phones from catalog
				.filter(validator)
				// Convert to entities and wrap into list
				.map(new OrderItemConverter(order)).collect(Collectors.toList());
		order.setItems(orderItems);
		// Calculate actual order total and save it
		order.setTotalPrice(orderItems.stream().mapToLong(this::calculateOrderItem).sum());
		final OrderEntity savedOrder = orderRepository.save(order);
		orderDump.accept(savedOrder);
		return savedOrder.getId();
	}

	private long calculateOrderItem(OrderItemEntity entity) {
		return entity.getPrice() * entity.getQuantity();
	}

	private class OrderItemConverter implements Function<OrderItem, OrderItemEntity> {

		private final OrderEntity order;

		private OrderItemConverter(final OrderEntity order) {
			this.order = order;
		}

		@Override
		public OrderItemEntity apply(OrderItem item) {
			final OrderItemEntity entity = modelMapper.map(item, OrderItemEntity.class);
			entity.setOrder(order);
			return entity;
		}

	}

}
