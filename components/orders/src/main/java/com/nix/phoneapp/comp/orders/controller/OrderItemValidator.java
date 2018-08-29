package com.nix.phoneapp.comp.orders.controller;

import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.nix.phoneapp.comp.orders.dto.OrderItem;
import com.nix.phoneapp.comp.orders.integration.Phone;
import com.nix.phoneapp.comp.orders.integration.PhoneCatalog;

/**
 * Validates the order item. If there is no such item in catalog, then throw it
 * away and keep only valid items. Also updates the actual pricing for the item.
 * 
 * @author ykryshchuk
 *
 */
public class OrderItemValidator implements Predicate<OrderItem> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemValidator.class);

	@Autowired
	private PhoneCatalog phoneCatalog;

	@Override
	public boolean test(OrderItem item) {
		LOGGER.debug("Validating the {}", item.getName());
		try {
			final Phone phone = phoneCatalog.byName(item.getName());
			if (phone.getPrice() != item.getPrice()) {
				LOGGER.warn("Cheating with {} :) Will reset the given price {} to one from catalog {}", item.getName(),
						item.getPrice(), phone.getPrice());
				item.setPrice(phone.getPrice());
			}
			return true;
		} catch (final HttpClientErrorException e) {
			if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
				LOGGER.warn("Cheating with order item {}, no such phone in catalog any more", item.getName());
				return false;
			} else {
				// something else, just re-throw for someone else
				throw e;
			}
		}
	}

}
