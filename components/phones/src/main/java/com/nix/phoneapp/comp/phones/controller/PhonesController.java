package com.nix.phoneapp.comp.phones.controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nix.phoneapp.comp.phones.dto.Phone;
import com.nix.phoneapp.comp.phones.persistence.PhoneEntity;
import com.nix.phoneapp.comp.phones.persistence.PhoneRepository;

@RestController()
@RequestMapping("/phones")
public class PhonesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PhonesController.class);

	private static final long PAGE_SIZE = 5;

	@Autowired
	private PhoneRepository phoneRepository;

	private ModelMapper modelMapper = new ModelMapper();

	private final Function<PhoneEntity, Phone> converter = entity -> modelMapper.map(entity, Phone.class);

	@GetMapping
	public List<Phone> listPhones(@RequestParam(name = "page", defaultValue = "0") final int page) {
		LOGGER.debug("Requested phones for page {}", page);
		return StreamSupport.stream(phoneRepository.findAll().spliterator(), false)
				// Move to a specified page
				.skip(page * PAGE_SIZE)
				// Convert to DTO and wrap in list
				.map(converter).collect(Collectors.toList());
	}

	@GetMapping("/{name}")
	public Phone phone(@PathVariable(name = "name") final String name) {
		LOGGER.debug("Asked for phone {}", name);
		final Optional<PhoneEntity> entity = phoneRepository.findById(name);
		final Phone phone = entity.map(converter).orElseThrow(PhoneNotFoundException::new);
		LOGGER.debug("The phone found is {}", phone);
		return phone;
	}

}
