package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.request.UpdateUserDetailRequest;
import com.halalhomemade.backend.models.*;
import com.halalhomemade.backend.repositories.CountryRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.LanguageService;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserDetailRequestToEntityMapper implements Function<UpdateUserDetailRequest, User> {

	
	@Autowired private UserRepository userRepository;
	@Autowired private CountryRepository countryRespository;
	@Autowired private LanguageService languageService;
  
	@Override
	public User apply(UpdateUserDetailRequest request) {
		User existingUser = userRepository.findOneByEmail(request.getEmail()).get();
		
		Optional<Language> preferredLanguage = Optional.ofNullable(request.getPreferredLanguageId())
				.map(l -> languageService.getLanguageById(l));
		if (preferredLanguage.isPresent()) {
			existingUser.setPreferredLanguage(preferredLanguage.get());
		}
		
		Optional<String> firstName = Optional.ofNullable(request.getFirstName());
		if (firstName.isPresent()) {
			existingUser.setFirstName(firstName.get());
		}
		
		Optional<String> lastName = Optional.ofNullable(request.getLastName());
		if (lastName.isPresent()) {
			existingUser.setLastName(lastName.get());
		}
		
		Optional<Instant> birthdate = Optional.ofNullable(request.getBirthdate());
		if (birthdate.isPresent()) {
			existingUser.setBirthdate(birthdate.get());
		}
		
		Optional<String> address = Optional.ofNullable(request.getAddress());
		if (address.isPresent()) {
			existingUser.setAddress(address.get());
		}
		
		Optional<String> city = Optional.ofNullable(request.getCity());
		if (city.isPresent()) {
			existingUser.setCity(city.get());
		}
		
		Optional<String> postCode = Optional.ofNullable(request.getPostCode());
		if (postCode.isPresent()) {
			existingUser.setPostCode(postCode.get());
		}
		
		Optional<String> countryStr = Optional.ofNullable(request.getCountry());
		if (countryStr.isPresent()) {
			Optional<Country> existingCountry = countryRespository.findOneByName(countryStr.get());
			if (existingCountry.isPresent()) {
				existingUser.setCountry(existingCountry.get());
			} else {
				return null;
			}
		}
		
		Optional<String> phoneCountryCode = Optional.ofNullable(request.getPhoneCode());
		Optional<String> phoneNumber = Optional.ofNullable(request.getPhoneNumber());
		if (phoneCountryCode.isPresent() && phoneNumber.isPresent()) {
			existingUser.setPhoneCode(phoneCountryCode.get());
			existingUser.setPhoneNumber(phoneNumber.get());
		} else if (phoneCountryCode.isPresent() & phoneNumber.isPresent()) {
			return null;
		}
		
		
		
		return existingUser;
  }
}
