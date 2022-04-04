package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.request.UserFetchOrRegisterRequest;
import com.halalhomemade.backend.dtos.request.UserRegistrationRequest;
import com.halalhomemade.backend.exceptions.NoAuthenticationMethodException;
import com.halalhomemade.backend.models.*;
import com.halalhomemade.backend.repositories.RoleRepository;
import com.halalhomemade.backend.services.LanguageService;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFetchOrRegisterRequestToEntityMapper implements Function<UserFetchOrRegisterRequest, User> {

	@Autowired private LanguageService languageService;
	@Autowired private RoleRepository roleRepository;
	
	@Override
	public User apply(UserFetchOrRegisterRequest request) {
		User user =	User.builder()
				.email(request.getEmail())
        		.emailVerified(VerificationStatus.VERIFIED)
        		.firstName(request.getFirstName())
        		.lastName(request.getLastName())
        		.phoneVerified(VerificationStatus.UNVERIFIED)
        		.status(UserStatus.UNCONFIRMED)
        		.authenticationMethod(request.getProvider())
        		.address("")
        		.city("")
	            .postCode("")
	            .preferredLanguage(languageService.getLanguageByName(Language.FRENCH_LANGUAGE_NAME))
	            .deviceToken("")
	            .build();
		return user;
	}
}
