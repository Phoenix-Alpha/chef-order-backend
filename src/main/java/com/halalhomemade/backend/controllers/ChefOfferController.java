package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.dtos.request.ChefOfferCreateRequest;
import com.halalhomemade.backend.dtos.request.ChefOfferDetailFetchRequest;
import com.halalhomemade.backend.dtos.request.FetchOffersByStatusRequest;
import com.halalhomemade.backend.dtos.request.UpdateChefOfferDetailRequest;
import com.halalhomemade.backend.dtos.response.ChefOfferResponse;
import com.halalhomemade.backend.dtos.response.UploadOfferPictureResponse;
import com.halalhomemade.backend.services.ChefOfferService;
import com.halalhomemade.backend.services.ChefService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth/chef/offer")
@Validated
public class ChefOfferController extends BaseController {

	@Autowired private ChefService chefService;
	@Autowired private ChefOfferService chefOfferService;
	
	@PostMapping(
			value = "/create",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefOfferResponse> create(@RequestBody final @Valid ChefOfferCreateRequest request) {
		request.setEmail(this.getEmail());
		return chefOfferService.create(request);
	}
	
	@PostMapping(
			value = "/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefOfferResponse> getChefOfferDetail(@RequestBody final @Valid ChefOfferDetailFetchRequest request) {
		request.setEmail(this.getEmail());
		return chefOfferService.getChefOfferDetail(request);	
	}
	
	@PostMapping(
			value = "/list/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<List<ChefOfferResponse>> getOffersByStatus(@RequestBody final @Valid FetchOffersByStatusRequest request) {
		request.setEmail(this.getEmail());
		return chefOfferService.getOffersByStatus(request);	
	}
	
	@PostMapping(
			value = "/update",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefOfferResponse> updateChefOfferDetail(@RequestBody final @Valid UpdateChefOfferDetailRequest request) {
		request.setEmail(this.getEmail());
		return chefOfferService.updateChefOfferDetail(request);	
	}

	@ResponseBody
	@PostMapping(value = "/picture/upload")
	@PreAuthorize("hasRole('CHEF')")
    public UploadOfferPictureResponse uploadOfferPicture(@RequestParam("file") MultipartFile file) {
       return chefOfferService.uploadOfferPicture(this.getEmail(), file);
    }
	
}
