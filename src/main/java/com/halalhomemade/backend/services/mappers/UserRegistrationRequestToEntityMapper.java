package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.request.UserRegistrationRequest;
import com.halalhomemade.backend.exceptions.NoAuthenticationMethodException;
import com.halalhomemade.backend.models.*;
import com.halalhomemade.backend.repositories.RoleRepository;
import com.halalhomemade.backend.repositories.RoleUserRepository;
import com.halalhomemade.backend.services.LanguageService;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationRequestToEntityMapper implements Function<UserRegistrationRequest, User> {

	@Autowired private LanguageService languageService;
	@Autowired private RoleRepository roleRepository;
	@Autowired private RoleUserRepository roleUserRepository;
	@Autowired private PasswordEncoder passwordEncoder;

	@Override
	public User apply(UserRegistrationRequest userRegistrationRequest) {
		Language preferredLanguage =
			Optional.ofNullable(userRegistrationRequest.getPreferredLanguageId())
            	.map(l -> languageService.getLanguageById(l))
            	.orElse(languageService.getLanguageByName(Language.FRENCH_LANGUAGE_NAME));
		User user =	User.builder()
				.email(userRegistrationRequest.getEmail())
        		.emailVerified(VerificationStatus.VERIFIED)
        		.firstName(userRegistrationRequest.getFirstName())
        		.lastName(userRegistrationRequest.getLastName())
        		.phoneVerified(VerificationStatus.UNVERIFIED)
        		.status(UserStatus.UNCONFIRMED)
        		.preferredLanguage(preferredLanguage)
        		.address("")
        		.city("")
	            .postCode("")
	            .deviceToken("")
	            .build();
		
		if (userRegistrationRequest.getSocial() != null) {
			if (userRegistrationRequest.getSocial() == AuthenticationProvider.Google) {
				user.setAuthenticationMethod(AuthenticationMethod.GOOGLE);
				user.setEmailVerified(VerificationStatus.VERIFIED);
				user.setStatus(UserStatus.ENABLED);
			} else if (userRegistrationRequest.getSocial() == AuthenticationProvider.Facebook) {
				user.setAuthenticationMethod(AuthenticationMethod.FACEBOOK);
				user.setEmailVerified(VerificationStatus.VERIFIED);
				user.setStatus(UserStatus.ENABLED);  
			} else {
				throw new NoAuthenticationMethodException("You should either specify a password or register using your social account");
			}
      	} else if (userRegistrationRequest.getPassword() != null) {
      		String encodedPassword = passwordEncoder.encode(userRegistrationRequest.getPassword());
      		user.setPassword(encodedPassword);
      		user.setAuthenticationMethod(AuthenticationMethod.PASSWORD);
      		user.setEmailVerified(VerificationStatus.UNVERIFIED);
      		user.setStatus(UserStatus.DISABLED);
      	} else {
      		throw new NoAuthenticationMethodException("You should either specify a password or register using your social account");
      	}
		return user;
	}
}
