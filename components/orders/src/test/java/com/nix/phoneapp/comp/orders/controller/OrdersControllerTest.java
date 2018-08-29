package com.nix.phoneapp.comp.orders.controller;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import com.nix.phoneapp.comp.orders.dto.Customer;
import com.nix.phoneapp.comp.orders.dto.Order;
import com.nix.phoneapp.comp.orders.dto.OrderItem;
import com.nix.phoneapp.comp.orders.dto.PlaceOrderRequest;
import com.nix.phoneapp.comp.orders.persistence.OrderEntity;
import com.nix.phoneapp.comp.orders.persistence.OrderRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrdersControllerTest {

	@InjectMocks
	private OrdersController unit;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private Predicate<OrderItem> validator;

	@Mock
	private Consumer<OrderEntity> orderDump;

	@Spy
	private ModelMapper modelMapper = new ModelMapper();

	@Mock
	private OrderEntity orderEntity777;

	@Mock
	private Order order777;

	@Mock
	private OrderEntity order;

	@Test
	public void orderExists() {
		Mockito.when(orderRepository.findById(707)).thenReturn(Optional.of(orderEntity777));
		Mockito.doReturn(order777).when(modelMapper).map(orderEntity777, Order.class);
		final Order order = unit.findOrder(707);
		Assert.assertSame(order777, order);
	}

	@Test(expected = OrderNotFoundException.class)
	public void orderDoesntExist() {
		Mockito.when(orderRepository.findById(808)).thenReturn(Optional.empty());
		unit.findOrder(808);
	}

	@Test
	public void goodOrder() {
		final PlaceOrderRequest request = new PlaceOrderRequest(new Customer("Tester", "Testeroff", "test@test.com"),
				Arrays.asList(new OrderItem(200, 2), new OrderItem(300, 1)));
		ArgumentCaptor<OrderEntity> newOrder = ArgumentCaptor.forClass(OrderEntity.class);
		Mockito.when(validator.test(Mockito.any())).thenReturn(Boolean.TRUE);
		// Autosetup for PK
		Mockito.when(order.getId()).thenReturn(Integer.valueOf(555));
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order);
		final Integer orderId = unit.placeOrder(request);
		Assert.assertEquals(Integer.valueOf(555), orderId);
		Mockito.verify(orderRepository).save(newOrder.capture());
		Assert.assertEquals("Tester", newOrder.getValue().getCustomer().getName());
		Assert.assertEquals("Testeroff", newOrder.getValue().getCustomer().getSurname());
		Assert.assertEquals("test@test.com", newOrder.getValue().getCustomer().getEmail());
		Assert.assertEquals(700, newOrder.getValue().getTotalPrice());
		Assert.assertEquals(2, newOrder.getValue().getItems().size());
	}
	
	// Here should be another test to cover the items filtering
	
	// And one more test to fix the item prices
	
	// But these will be covered by more interesting integration tests

}
