package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.dtos.request.ChefOrderConfirmDeliveryRequest;
import com.halalhomemade.backend.dtos.request.ChefOrderDetailFetchRequest;
import com.halalhomemade.backend.dtos.request.ChefOrderHandleRequest;
import com.halalhomemade.backend.dtos.request.ChefOrdersFetchByStatusRequest;
import com.halalhomemade.backend.dtos.response.ChefOrderResponse;
import com.halalhomemade.backend.services.ChefOrderService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/chef/order")
@Validated
public class ChefOrderController extends BaseController {

	@Autowired private ChefService chefService;
	@Autowired private ChefOrderService chefOrderService;
	
	@PostMapping(
			value = "/handle",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity handleOrder(@RequestBody final @Valid ChefOrderHandleRequest request) {
		request.setEmail(this.getEmail());
		return chefOrderService.handleOrder(request);
	}
	
	@PostMapping(
			value = "/confirm-delivery",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity confirmDelivery(@RequestBody final @Valid ChefOrderConfirmDeliveryRequest request) {
		request.setEmail(this.getEmail());
		return chefOrderService.confirmDelivery(request);
	}
	
	@PostMapping(
			value = "/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<ChefOrderResponse> getChefOfferDetail(@RequestBody final @Valid ChefOrderDetailFetchRequest request) {
		request.setEmail(this.getEmail());
		return chefOrderService.getChefOrderDetail(request);
	}
	
	@PostMapping(
			value = "/list/get",
		    produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('CHEF')")
	public ResponseEntity<List<ChefOrderResponse>> getOrdersByStatus(@RequestBody final @Valid ChefOrdersFetchByStatusRequest request) {
		request.setEmail(this.getEmail());
		return chefOrderService.getOrdersByStatus(request);
	}
}
