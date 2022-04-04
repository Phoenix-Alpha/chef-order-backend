package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.UserResponse;
import com.halalhomemade.backend.dtos.response.UserResponse.UserResponseBuilder;
import com.halalhomemade.backend.models.Country;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.UserRole;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDtoMapper implements Function<User, UserResponse> {
	
	@Autowired private LanguageEntityToDtoMapper languageEntityToDtoMapper;
	
	@Override
	public UserResponse apply(User user) {
		UserResponseBuilder userResponseBuilder = UserResponse.builder()
				.id(user.getId())
				.email(user.getEmail())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.address(user.getAddress())
				.city(user.getCity())
				.postCode(user.getPostCode())
				.preferredLanguage(languageEntityToDtoMapper.apply(user.getPreferredLanguage()))
				.phoneCode(user.getPhoneCode())
				.phoneNumber(user.getPhoneNumber())
				.phoneVerified(user.getPhoneVerified())
				.authenticationMethod(user.getAuthenticationMethod())
				.supperAdmin(user.isSuperAdmin())
				.birthdate(user.getBirthdate())
				.registeredAt(user.getCreatedAt());
		
		Optional<Country> existingCountry = Optional.ofNullable(user.getCountry());
		if (existingCountry.isPresent()) {
			userResponseBuilder.country(existingCountry.get().getName());
		}
		
//		if (user.getUserRoles().size() > 0) {
//			userRegistrationResponseBuilder.userRole(user.getUserRoles().get(0).getRole().getName());
//		}
		
//		Set<String> roles = new HashSet<>();
//	    Optional.ofNullable(user.getRoles())
//	        .ifPresent(
//	            userRoles ->
//	                userRoles.stream()
//	                    .map(role -> role.getName().name())
//	                    .forEach(name -> roles.add(name)));
//	    Optional.ofNullable(user.getUserRoles())
//	        .ifPresent(
//	            userRoles ->
//	                userRoles.stream()
//	                    .map(role -> role.getRole().getName().name())
//	                    .forEach(name -> roles.add(name)));
//	    userResponseBuilder.roles(roles);
		
		Set<UserRole> roles = new HashSet<>();
		Optional.ofNullable(user.getRoles()).ifPresent(rs -> rs.forEach(r -> roles.add(r.getName())));
		if (roles.contains(UserRole.CHEF)) {
			userResponseBuilder.isChef(true);
		}
		userResponseBuilder.roles(roles);
		
		return userResponseBuilder.build();
	}
}
