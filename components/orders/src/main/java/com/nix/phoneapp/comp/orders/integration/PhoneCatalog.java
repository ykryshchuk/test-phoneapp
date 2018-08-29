package com.nix.phoneapp.comp.orders.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public interface PhoneCatalog {

	@Gateway(requestChannel = "integration.PhoneCatalog.byName.requestChannel")
	@Payload("new java.util.Date()")
	public Phone byName(@Header(value = "phoneName") String name);

}
