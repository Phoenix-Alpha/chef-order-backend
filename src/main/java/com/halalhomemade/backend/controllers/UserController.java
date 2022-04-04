package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.dtos.request.PhoneVerificationRequest;
import com.halalhomemade.backend.dtos.request.UpdateUserDetailRequest;
import com.halalhomemade.backend.dtos.request.UpdateUserPhoneNumberRequest;
import com.halalhomemade.backend.dtos.request.UserFetchOrRegisterRequest;
import com.halalhomemade.backend.dtos.request.UserRegistrationRequest;
import com.halalhomemade.backend.dtos.response.UserResponse;
import com.halalhomemade.backend.exceptions.DuplicateUserException;
import com.halalhomemade.backend.repositories.RoleRepository;
import com.halalhomemade.backend.repositories.RoleUserRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.UserService;
import com.halalhomemade.backend.services.mappers.UserEntityToDtoMapper;
import com.halalhomemade.backend.services.mappers.UserRegistrationRequestToEntityMapper;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.VerificationStatus;

import java.time.Instant;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/user")
@Validated
public class UserController extends BaseController {
	
	@Autowired private UserService userService;
	
	@PostMapping(
			value = "/fetchorregister",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResponse fetchOrRegister(@RequestBody final @Valid UserFetchOrRegisterRequest request) throws Exception {
		request.setEmail(this.getEmail());
		return userService.fetchOrRegister(request);
	}
	
	@PostMapping(
			value = "/register",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResponse register(@RequestBody final @Valid UserRegistrationRequest request) throws Exception {
		request.setEmail(this.getEmail());
		return userService.register(request);
	}
	
	@GetMapping(
			value = "/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CUSTOMER')")
	public UserResponse getUserDetail() throws Exception {
		return userService.getUserDetail(this.getEmail());
	}
	
	@PostMapping(
			value = "/update",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<UserResponse> updateUserDetail(@RequestBody final @Valid UpdateUserDetailRequest request) throws Exception {
		request.setEmail(this.getEmail());
		return userService.updateUserDetail(request);	
	}
	  
	@PostMapping(
			value = "/phone/verify",
			consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity verifyPhoneNumber(@RequestBody final @Valid PhoneVerificationRequest request) throws Exception {
		request.setEmail(this.getEmail());
		return userService.verifyPhoneNumber(request);
	}
	
	@PostMapping(
			value = "/phone/update",
			consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity verifyPhoneNumber(@RequestBody final @Valid UpdateUserPhoneNumberRequest request) throws Exception {
		request.setEmail(this.getEmail());
		return userService.updatePhoneNumber(request);
	}
	
	@GetMapping(
			value = "/test",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public String test(@RequestBody final @Valid UserRegistrationRequest userRequest) {
		return this.getEmail();
	}
}
