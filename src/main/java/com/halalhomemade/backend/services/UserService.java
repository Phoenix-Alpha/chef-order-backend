package com.halalhomemade.backend.services;

import com.halalhomemade.backend.repositories.RoleRepository;
import com.halalhomemade.backend.repositories.RoleUserRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.events.PhoneNumberUpdatedEvent;
import com.halalhomemade.backend.services.mappers.UpdateUserDetailRequestToEntityMapper;
import com.halalhomemade.backend.services.mappers.UserEntityToDtoMapper;
import com.halalhomemade.backend.services.mappers.UserFetchOrRegisterRequestToEntityMapper;
import com.halalhomemade.backend.services.mappers.UserRegistrationRequestToEntityMapper;
import com.halalhomemade.backend.dtos.request.PhoneVerificationRequest;
import com.halalhomemade.backend.dtos.request.UpdateUserDetailRequest;
import com.halalhomemade.backend.dtos.request.UpdateUserPhoneNumberRequest;
import com.halalhomemade.backend.dtos.request.UserFetchOrRegisterRequest;
import com.halalhomemade.backend.dtos.request.UserRegistrationRequest;
import com.halalhomemade.backend.dtos.response.UserResponse;
import com.halalhomemade.backend.exceptions.DuplicateUserException;
import com.halalhomemade.backend.exceptions.UserNotFoundException;
import com.halalhomemade.backend.models.Role;
import com.halalhomemade.backend.models.RoleUser;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.UserRole;
import com.halalhomemade.backend.models.VerificationStatus;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService extends AbstractService {
	@Autowired private ApplicationEventPublisher eventPublisher;
	@Autowired private UserRepository userRepository;
	@Autowired private RoleUserRepository roleUserRepository;
	@Autowired private RoleRepository roleRepository;
	@Autowired private UserRegistrationRequestToEntityMapper userRegistrationRequestToEntityMapper;
	@Autowired private UserFetchOrRegisterRequestToEntityMapper userFetchOrRegisterRequestToEntityMapper;
	@Autowired private UpdateUserDetailRequestToEntityMapper updateUserDetailRequestToEntityMapper;
	@Autowired private UserEntityToDtoMapper userEntityToDtoMapper;
  
	
	public UserResponse fetchOrRegister(UserFetchOrRegisterRequest request) {
		Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
		if (existingUser.isPresent()) {
			return userEntityToDtoMapper.apply(existingUser.get());  
		} else {
			User savedUser = userRepository.save(userFetchOrRegisterRequestToEntityMapper.apply(request));
			Optional<Role> role = roleRepository.findOneByName(UserRole.CUSTOMER);
			if (role.isPresent()) {
				roleUserRepository.save(RoleUser.builder()
						.user(savedUser)
				        .role(role.get())
				        .build());
				savedUser.addRole(role.get());
			}
			return userEntityToDtoMapper.apply(savedUser);
		}
	}
	
	public UserResponse register(UserRegistrationRequest request) {
		Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
		if (existingUser.isPresent()) {
			throw new DuplicateUserException(String.format("User already exist with email: %s", request.getEmail()));
		}
		User savedUser = userRepository.save(userRegistrationRequestToEntityMapper.apply(request));
		Optional<Role> role = roleRepository.findOneByName(UserRole.CUSTOMER);
		if (role.isPresent()) {
			roleUserRepository.save(RoleUser.builder()
					.user(savedUser)
			        .role(role.get())
			        .build());
			savedUser.addRole(role.get());
		}
		return userEntityToDtoMapper.apply(savedUser);
	}
  
	public UserResponse getUserDetail(String email) throws Exception {
		User user = this.getUserByEmail(email);
		return userEntityToDtoMapper.apply(user);  
	}
  
	public ResponseEntity<UserResponse> updateUserDetail(UpdateUserDetailRequest request) {
		try {
			this.getUserByEmail(request.getEmail());
			Optional<User> existingUser = Optional.ofNullable(updateUserDetailRequestToEntityMapper.apply(request));
			if (existingUser.isPresent()) {
				User savedUser = userRepository.save(existingUser.get());
				return new ResponseEntity<UserResponse>(userEntityToDtoMapper.apply(savedUser), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}	
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
  
	public ResponseEntity verifyPhoneNumber(PhoneVerificationRequest request) throws Exception {
		User user = this.getUserByEmail(request.getEmail());
		Instant createdAt = user.getPhoneVerificationCodeCreatedAt();
		Long diffMins = ChronoUnit.MINUTES.between(createdAt, Instant.now());
		Boolean codeIdentical = request.getPhoneVerificationCode().equals(user.getPhoneVerificationCode());
		if (codeIdentical) {
			if (diffMins > 5) {
				user.setPhoneVerified(VerificationStatus.BOUNCED);
				userRepository.save(user);
				throw new Exception("Verification code expired.");
			} else {
				user.setPhoneVerified(VerificationStatus.VERIFIED);
				userRepository.save(user);
				return ResponseEntity.ok().build();
			}
		} else {
			user.setPhoneVerified(VerificationStatus.BOUNCED);
			userRepository.save(user);
			throw new Exception("Invalid verification code.");
		}
	}
  
	public ResponseEntity updatePhoneNumber(UpdateUserPhoneNumberRequest request) throws Exception {
		User user = this.getUserByEmail(request.getEmail());
		user.setPhoneCode(request.getPhoneCode());
		user.setPhoneNumber(request.getPhoneNumber());
		// Generate phone verification code
		String newPhoneVerificationCode = RandomStringUtils.randomNumeric(6);
		user.setPhoneVerificationCode(newPhoneVerificationCode);
		user.setPhoneVerificationCodeCreatedAt(Instant.now());
		user.setPhoneVerified(VerificationStatus.UNVERIFIED);
	  
		userRepository.save(user);
	  
		eventPublisher.publishEvent(new PhoneNumberUpdatedEvent(user));
	  
		return ResponseEntity.ok().build();
	}
  
	@Transactional(readOnly = true)
	public User getUserByEmail(String email) throws Exception {
		Optional<User> user = userRepository.findOneByEmail(email);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new UserNotFoundException(String.format("No user found with email: %s", email));
		}
	}
}
