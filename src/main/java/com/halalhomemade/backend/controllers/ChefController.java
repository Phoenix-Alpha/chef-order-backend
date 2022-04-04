package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.dtos.request.ChefActivateWalletRequest;
import com.halalhomemade.backend.dtos.request.ChefRegistrationRequest;
import com.halalhomemade.backend.dtos.request.ChefWalletAddCardRequest;
import com.halalhomemade.backend.dtos.request.UpdateChefDetailRequest;
import com.halalhomemade.backend.dtos.response.ChefResponse;
import com.halalhomemade.backend.dtos.response.ChefWalletActivateResponse;
import com.halalhomemade.backend.dtos.response.ChefWalletStripeDashboardLoginResponse;
import com.halalhomemade.backend.dtos.response.UploadAvatarResponse;
import com.halalhomemade.backend.dtos.response.WalletResponse;
import com.halalhomemade.backend.services.ChefService;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/auth/chef")
@Validated
public class ChefController extends BaseController {

	@Autowired private ChefService chefService;
	
	@PostMapping(
			value = "/register",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<ChefResponse> register(@RequestBody final @Valid ChefRegistrationRequest request) {
		request.setEmail(this.getEmail());
		return chefService.register(request);	
	}
	
	@GetMapping(
			value = "/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefResponse> getChefDetail() {
		return chefService.getChefDetail(this.getEmail());	
	}
	
	@PostMapping(
			value = "/update",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefResponse> updateUserDetail(@RequestBody final @Valid UpdateChefDetailRequest request) {
		request.setEmail(this.getEmail());
		return chefService.updateChefDetail(request);
	}

	@PostMapping(value = "/avatar/upload", consumes = "multipart/form-data")
	@PreAuthorize("hasRole('CUSTOMER')")
    public UploadAvatarResponse uploadAvatar(@RequestParam("file") MultipartFile file) {
       return chefService.uploadChefAvatar(this.getEmail(), file);
    }
	
	@PostMapping(value = "/avatar/update", consumes = "multipart/form-data")
	@PreAuthorize("hasRole('CHEF')")
    public UploadAvatarResponse updateAvatar(@RequestParam("file") MultipartFile file) {
       return chefService.updateChefAvatar(this.getEmail(), file);
    }
	
//	@PostMapping(
//			value = "/wallet/activate",
//			consumes = MediaType.APPLICATION_JSON_VALUE,
//		    produces = MediaType.APPLICATION_JSON_VALUE)
//	@PreAuthorize("hasRole('CHEF')")
//	public ResponseEntity<ChefWalletActivateResponse> activateWallet(@RequestBody final @Valid ChefActivateWalletRequest request, HttpServletRequest httpRequest) {
//		request.setEmail(this.getEmail());
//		request.setIp(httpRequest.getRemoteAddr());
//		return chefService.activateWallet(request);	
//	}
	
	@PostMapping(
			value = "/wallet/activate",
			consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefWalletActivateResponse> activateWallet(@RequestBody final @Valid ChefActivateWalletRequest request, HttpServletRequest httpRequest) {
//		request.setEmail(this.getEmail());
//		request.setIp(httpRequest.getRemoteAddr());
		return chefService.activateWallet(this.getEmail());	
	}
	
	@GetMapping(
			value = "/wallet/get",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<WalletResponse> getWalletDetail() {
		return chefService.getWalletDetail(this.getEmail());	
	}
	
	@PostMapping(
			value = "/wallet/stripe-dashboard",
			consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefWalletStripeDashboardLoginResponse> redirectToDashboard() {
		return chefService.redirectToStripeExpressDashboard(this.getEmail());	
	}
	
//	@PostMapping(
//			value = "/wallet/payment-method/add",
//			consumes = MediaType.APPLICATION_JSON_VALUE,
//		    produces = MediaType.APPLICATION_JSON_VALUE)
//	@PreAuthorize("hasRole('CHEF')")
//	public ResponseEntity<WalletResponse> addPaymentMethod(ChefWalletAddCardRequest request) {
//		request.setEmail(this.getEmail());
//		return chefService.addCardToChefWallet(request);	
//	}
	
	
}
