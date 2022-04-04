package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.dtos.request.ChefOrderDetailFetchRequest;
import com.halalhomemade.backend.dtos.request.ChefOrdersFetchByStatusRequest;
import com.halalhomemade.backend.dtos.request.PublicOrderCreateRequest;
import com.halalhomemade.backend.dtos.request.PublicOrderDetailFetchRequest;
import com.halalhomemade.backend.dtos.request.PublicOrderReviewRequest;
import com.halalhomemade.backend.dtos.request.PublicOrdersFetchByStatusRequest;
import com.halalhomemade.backend.dtos.response.ChefOrderResponse;
import com.halalhomemade.backend.dtos.response.PublicOrderResponse;
import com.halalhomemade.backend.services.PublicOrderService;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@Validated
public class PublicOrderController extends BaseController {

	@Autowired
	private PublicOrderService publicOrderService;
	
	@PostMapping(
			value = "/create",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PublicOrderResponse> createOrder(@RequestBody final @Valid  PublicOrderCreateRequest request) {
		return publicOrderService.createOrder(request);
	}
	
	@PostMapping(
			value = "/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PublicOrderResponse> getChefOfferDetail(@RequestBody final @Valid PublicOrderDetailFetchRequest request) {
		return publicOrderService.getPublicOrderDetail(request);
	}
	
	@PostMapping(
			value = "/list/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublicOrderResponse>> getOrdersByStatus(@RequestBody final @Valid PublicOrdersFetchByStatusRequest request) {
		return publicOrderService.getOrdersByStatus(request);
	}
	
	@PostMapping(
			value = "/review",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity leaveReview(@RequestBody final @Valid PublicOrderReviewRequest request) {
		return publicOrderService.review(request);
	}
}
