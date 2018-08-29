package com.nix.phoneapp.comp.phones.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.nix.phoneapp.comp.phones.dto.Phone;
import com.nix.phoneapp.comp.phones.persistence.PhoneEntity;
import com.nix.phoneapp.comp.phones.persistence.PhoneRepository;

@RunWith(MockitoJUnitRunner.class)
public class PhonesControllerTest {

	@InjectMocks
	private PhonesController unit;

	@Mock
	private PhoneRepository phoneRepository;

	@Mock
	private PhoneEntity phone;

	/**
	 * Verify that exception is thrown if looking for unexisting phone in catalog
	 */
	@Test(expected = PhoneNotFoundException.class)
	public void wrongPhone() {
		Mockito.when(phoneRepository.findById("echo dot")).thenReturn(Optional.empty());
		unit.phone("echo dot");
		Assert.fail("Should not get here");
	}

	/**
	 * Verify proper converting for known phone
	 */
	@Test
	public void phoneHere() {
		Mockito.when(phone.getName()).thenReturn("iphone");
		Mockito.when(phone.getDescription()).thenReturn("apple garbage");
		Mockito.when(phone.getImage()).thenReturn("http://imgur.com/23234551");
		Mockito.when(phone.getPrice()).thenReturn(99900l);
		Mockito.when(phoneRepository.findById("iphone")).thenReturn(Optional.of(phone));
		Phone dto = unit.phone("iphone");
		Assert.assertEquals("iphone", dto.getName());
		Assert.assertEquals("apple garbage", dto.getDescription());
		Assert.assertEquals("http://imgur.com/23234551", dto.getImage());
		Assert.assertEquals(99900l, dto.getPrice());
	}

	// Some test should be added to verify that the entities are properly converted
	// to DTOs when returning the list

	/**
	 * Verify that entities 6 and 7 (converted) are returned for the second page if
	 * there are 7 entities in repo.
	 */
	@Test
	public void soTiredWithTjoseUnitTestsLetMeCompleteThisOneAndThatsEnough() {
		// 6-th element in entities list
		final PhoneEntity phone6 = new PhoneEntity();
		phone6.setName("Alcatel");
		Iterable<PhoneEntity> entities = Arrays.asList(new PhoneEntity(), new PhoneEntity(), new PhoneEntity(),
				new PhoneEntity(), new PhoneEntity(), phone6, new PhoneEntity());
		Mockito.when(phoneRepository.findAll()).thenReturn(entities);
		List<Phone> phones = unit.listPhones(1);
		// from the total 7 entities in repo we expect only second page (two last) be
		// returned
		Assert.assertEquals(2, phones.size());
		Assert.assertEquals(phone6.getName(), phones.get(0).getName());
	}

}
