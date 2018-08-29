package com.nix.phoneapp.it;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.nix.phoneapp.it.dto.CustomerDTO;
import com.nix.phoneapp.it.dto.OrderDTO;
import com.nix.phoneapp.it.dto.PhoneDTO;
import com.nix.phoneapp.it.dto.PhoneOrderItemDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/context.xml")
public class ITPurchase {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void verifyDetails() throws Exception {
		final PhoneDTO[] phones = restTemplate.getForObject("http://localhost:18080/phones", PhoneDTO[].class);
		Assert.assertEquals(4, phones.length);
		final PhoneDTO phoneFromList = phones[1];
		final PhoneDTO phoneFromService = restTemplate
				.getForObject("http://localhost:18080/phones/" + phoneFromList.name, PhoneDTO.class);
		Assert.assertEquals(phoneFromList.name, phoneFromService.name);
		Assert.assertEquals(phoneFromList.description, phoneFromService.description);
		Assert.assertEquals(phoneFromList.image, phoneFromService.image);
		Assert.assertEquals(phoneFromList.price, phoneFromService.price);
	}

	/**
	 * Here we just list the phones from Catalog and try to place an order for
	 * second (x2) and third phones from a list. Then we query for a placed order
	 * and verify the items there and calculated price.
	 */
	@Test
	public void goodCase() throws Exception {
		// Will purchase the second and third phones from the list
		final PhoneDTO[] phones = restTemplate.getForObject("http://localhost:18080/phones", PhoneDTO[].class);
		// Buy two phone2
		final PhoneDTO phone2 = phones[1];
		final PhoneOrderItemDTO entry1 = new PhoneOrderItemDTO();
		entry1.name = phone2.name;
		entry1.price = phone2.price;
		entry1.quantity = 2;
		// Buy one phone3
		final PhoneDTO phone3 = phones[2];
		final PhoneOrderItemDTO entry2 = new PhoneOrderItemDTO();
		entry2.name = phone3.name;
		entry2.price = phone3.price;
		entry2.quantity = 1;
		// Expected order total
		final long expectedTotal = phone2.price * 2 + phone3.price * 1;
		//
		final CustomerDTO customer = new CustomerDTO();
		customer.name = "Donald";
		customer.surname = "Trump";
		customer.email = "donald.trump@president.us";
		//
		final OrderDTO orderRequest = new OrderDTO();
		orderRequest.customer = customer;
		orderRequest.items = Arrays.asList(entry1, entry2);
		// Place an order
		final String orderId = restTemplate.postForObject("http://localhost:28080/orders", orderRequest, String.class);
		Assert.assertNotNull(orderId);
		// Now check what is calculated
		final OrderDTO order = restTemplate.getForObject("http://localhost:28080/orders/" + orderId, OrderDTO.class);
		Assert.assertEquals(2, order.items.size());
		Assert.assertEquals("donald.trump@president.us", order.customer.email);
		Assert.assertEquals(expectedTotal, order.totalPrice);
	}

	/**
	 * Here we just list the phones from Catalog and try to place an order for third
	 * (x2) and fourth phones from a list. For the order placement we modify the
	 * original price for fourth phone. Then we query for a placed order and verify
	 * the items there and calculated price. We expect that validator fixes the
	 * price for changed order item.
	 */
	@Test
	public void cheatingWithPrice() throws Exception {
		// Will purchase the second and third phones from the list
		final PhoneDTO[] phones = restTemplate.getForObject("http://localhost:18080/phones", PhoneDTO[].class);
		// Buy two phone2
		final PhoneDTO phone3 = phones[2];
		final PhoneOrderItemDTO entry1 = new PhoneOrderItemDTO();
		entry1.name = phone3.name;
		entry1.price = phone3.price;
		entry1.quantity = 2;
		// Buy one phone3
		final PhoneDTO phone4 = phones[3];
		final PhoneOrderItemDTO entry2 = new PhoneOrderItemDTO();
		entry2.name = phone4.name;
		entry2.price = phone4.price + 100;
		entry2.quantity = 1;
		// Expected order total
		final long expectedTotal = phone3.price * 2 + phone4.price * 1;
		//
		final CustomerDTO customer = new CustomerDTO();
		customer.name = "Donald";
		customer.surname = "Trump";
		customer.email = "donald.trump@president.us";
		//
		final OrderDTO orderRequest = new OrderDTO();
		orderRequest.customer = customer;
		orderRequest.items = Arrays.asList(entry1, entry2);
		// Place an order
		final String orderId = restTemplate.postForObject("http://localhost:28080/orders", orderRequest, String.class);
		Assert.assertNotNull(orderId);
		// Now check what is calculated
		final OrderDTO order = restTemplate.getForObject("http://localhost:28080/orders/" + orderId, OrderDTO.class);
		Assert.assertEquals(2, order.items.size());
		Assert.assertEquals("donald.trump@president.us", order.customer.email);
		Assert.assertEquals(expectedTotal, order.totalPrice);
	}

	/**
	 * Here we just list the phones from Catalog and try to place an order for first
	 * (x4) phone from a list. We also try to add some unexisting phone to the
	 * order. Then we query for a placed order and verify the items there and
	 * calculated price. We expect that order validator skips the second item in an
	 * order as that is an invalid position for catalog.
	 */
	@Test
	public void cheatingWithPhone() throws Exception {
		// Will purchase the second and third phones from the list
		final PhoneDTO[] phones = restTemplate.getForObject("http://localhost:18080/phones", PhoneDTO[].class);
		// Buy two phone2
		final PhoneDTO phone1 = phones[0];
		final PhoneOrderItemDTO entry1 = new PhoneOrderItemDTO();
		entry1.name = phone1.name;
		entry1.price = phone1.price;
		entry1.quantity = 4;
		// Buy one phone3
		final PhoneOrderItemDTO entry2 = new PhoneOrderItemDTO();
		entry2.name = "Landline phone";
		entry2.price = 100;
		entry2.quantity = 99;
		// Expected order total
		final long expectedTotal = phone1.price * 4;
		//
		final CustomerDTO customer = new CustomerDTO();
		customer.name = "Donald";
		customer.surname = "Trump";
		customer.email = "donald.trump@president.us";
		//
		final OrderDTO orderRequest = new OrderDTO();
		orderRequest.customer = customer;
		orderRequest.items = Arrays.asList(entry1, entry2);
		// Place an order
		final String orderId = restTemplate.postForObject("http://localhost:28080/orders", orderRequest, String.class);
		Assert.assertNotNull(orderId);
		// Now check what is calculated
		final OrderDTO order = restTemplate.getForObject("http://localhost:28080/orders/" + orderId, OrderDTO.class);
		Assert.assertEquals(1, order.items.size());
		Assert.assertEquals("donald.trump@president.us", order.customer.email);
		Assert.assertEquals(expectedTotal, order.totalPrice);
	}

}
