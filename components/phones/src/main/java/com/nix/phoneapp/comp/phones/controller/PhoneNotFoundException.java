package com.nix.phoneapp.comp.phones.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "phone not found")
public class PhoneNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
