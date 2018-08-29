package com.nix.phoneapp.comp.phones.dto;

public class Phone {

	private String name;

	private String description;

	private String image;

	private long price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * The phone price in cents (secondary currency unit). For instance, consider
	 * the Euro currency, the value 1590 means 15 Euro and 90 cents.
	 * 
	 * @return the price in cents
	 */
	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return String.format("Phone %s ($ %.2f)", getName(), getPrice()/100.0);
	}

}
