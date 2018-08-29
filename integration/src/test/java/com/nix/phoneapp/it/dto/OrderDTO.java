package com.nix.phoneapp.it.dto;

import java.util.List;

public class OrderDTO {

	public CustomerDTO customer;
	
	public List<PhoneOrderItemDTO> items;
	
	public long totalPrice;
	
}
