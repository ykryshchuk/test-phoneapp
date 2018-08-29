package com.nix.phoneapp.comp.orders.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.nix.phoneapp.comp.orders.dto.OrderItem;
import com.nix.phoneapp.comp.orders.integration.Phone;
import com.nix.phoneapp.comp.orders.integration.PhoneCatalog;

@RunWith(MockitoJUnitRunner.class)
public class OrderItemValidatorTest {

	@Mock
	private PhoneCatalog phoneCatalog;

	@InjectMocks
	private OrderItemValidator unit;

	@Mock
	private Phone phone;

	@Test
	public void skipItemIfMissing() {
		final OrderItem item = new OrderItem();
		item.setName("dummy phone");
		Mockito.when(phoneCatalog.byName("dummy phone")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		final boolean result = unit.test(item);
		Assert.assertFalse(result);
	}

	@Test(expected = HttpClientErrorException.class)
	public void failIfSomeClientReason() {
		final OrderItem item = new OrderItem();
		item.setName("dummy phone");
		Mockito.when(phoneCatalog.byName("dummy phone"))
				.thenThrow(new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED));
		unit.test(item);
	}

	@Test
	public void fixPrice() {
		final OrderItem item = new OrderItem();
		item.setName("nokia");
		item.setPrice(200);
		Mockito.when(phone.getPrice()).thenReturn(210l);
		Mockito.when(phoneCatalog.byName("nokia")).thenReturn(phone);
		final boolean result = unit.test(item);
		Assert.assertTrue(result);
		Assert.assertEquals(210, item.getPrice());
	}

	@Test
	public void nofixPrice() {
		final OrderItem item = Mockito.mock(OrderItem.class);
		Mockito.when(item.getName()).thenReturn("nokia");
		Mockito.when(item.getPrice()).thenReturn(300l);
		Mockito.when(phone.getPrice()).thenReturn(300l);
		Mockito.when(phoneCatalog.byName("nokia")).thenReturn(phone);
		final boolean result = unit.test(item);
		Assert.assertTrue(result);
		Mockito.verify(item, Mockito.never()).setPrice(Mockito.anyLong());
	}

}
