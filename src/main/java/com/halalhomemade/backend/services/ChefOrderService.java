package com.halalhomemade.backend.services;

import com.halalhomemade.backend.dtos.request.ChefOrderConfirmDeliveryRequest;
import com.halalhomemade.backend.dtos.request.ChefOrderDetailFetchRequest;
import com.halalhomemade.backend.dtos.request.ChefOrderHandleRequest;
import com.halalhomemade.backend.dtos.request.ChefOrdersFetchByStatusRequest;
import com.halalhomemade.backend.dtos.response.ChefOrderResponse;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.Order;
import com.halalhomemade.backend.models.OrderStatus;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.OrderRepository;
import com.halalhomemade.backend.repositories.UserRepository;
import com.halalhomemade.backend.services.mappers.ChefOfferCreateRequestToEntityMapper;
import com.halalhomemade.backend.services.mappers.OrderEntityToChefOrderResponseMapper;
import com.stripe.param.QuoteListParams.Status;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChefOrderService extends AbstractService {

	@Autowired private UserRepository userRepository;
	@Autowired private ChefRepository chefRepository;
	@Autowired private OrderRepository orderRepository;
	
	@Autowired private OrderEntityToChefOrderResponseMapper orderEntityToChefOrderResponseMapper;
	
	public ResponseEntity handleOrder(ChefOrderHandleRequest request) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("User not existing with email %s and cannot register as chef", request.getEmail()));
	  		}
			User user = existingUser.get();
			
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
	  		if (!existingChef.isPresent()) {
	  			throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));
	  		}
	  		Chef chef = existingChef.get();
	  		
	  		Optional<Order> existingOrder = orderRepository.findOneByUuid(request.getOrderUuid());
			if (!existingOrder.isPresent()) {
				throw new Exception(String.format("Order not exsting: %s", request.getOrderUuid()));
			}
			Order order = existingOrder.get();
			
			if (order.getStatus().equals(OrderStatus.SUBMITTED)) {
				if (request.getStatus().equals(OrderStatus.APPROVED)) {
					// If order is handled as APPROVED, minus 1 from mealsPending & plus 1 to mealsInprep
					chef.setMealsPending(chef.getMealsPending() - 1);
					chef.setMealsInprep(chef.getMealsInprep() + 1);
				} else if (request.getStatus().equals(OrderStatus.REJECTED)) {
					// If order is handled as APPROVED, minus 1 from mealsPending
					chef.setMealsPending(chef.getMealsPending() - 1);
				}
				order.setStatus(request.getStatus());
			} else {
				throw new Exception("Invalid request or confirm code");
			}
			
			// Finally, saving the changed chef & order
			Chef savedChef = chefRepository.save(chef);
			Order savedOrder = orderRepository.save(order);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return ResponseEntity.badRequest().build();
		}
	}
	
	public ResponseEntity confirmDelivery(ChefOrderConfirmDeliveryRequest request) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("User not existing with email %s and cannot register as chef", request.getEmail()));
	  		}
			User user = existingUser.get();
			
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
	  		if (!existingChef.isPresent()) {
	  			throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));
	  		}
	  		Chef chef = existingChef.get();
	  		
	  		Optional<Order> existingOrder = orderRepository.findOneByUuid(request.getOrderUuid());
			if (!existingOrder.isPresent()) {
				throw new Exception(String.format("Order not exsting: %s", request.getOrderUuid()));
			}
			Order order = existingOrder.get();
			
			if (order.getPickupCode().equals(request.getConfirmCode()) && order.getStatus().equals(OrderStatus.APPROVED)) {
				
				// Upadate order status 
				order.setStatus(OrderStatus.CONFIRMED);
				Order savedOrder = orderRepository.save(order);
				
				// Update chef status
				if (chef.getActiveOffers() > 0) {
					chef.setActiveOffers(chef.getActiveOffers() - 1);
				}
				
				
			} else {
				throw new Exception("Invalid request or confirm code");
			}
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return ResponseEntity.badRequest().build();
		}
	}
	
	public ResponseEntity<ChefOrderResponse> getChefOrderDetail(ChefOrderDetailFetchRequest request) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("User not existing with email %s and cannot register as chef", request.getEmail()));
	  		}
			User user = existingUser.get();
			
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
	  		if (!existingChef.isPresent()) {
	  			throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));
	  		}
	  		
	  		Optional<Order> existingOrder = orderRepository.findOneByUuid(request.getOrderUuid());
			if (!existingOrder.isPresent()) {
				throw new Exception(String.format("Order not exsting: %s", request.getOrderUuid()));
			}
			Order order = existingOrder.get();
			
			ChefOrderResponse response = orderEntityToChefOrderResponseMapper.apply(order);
			
			return new ResponseEntity<ChefOrderResponse>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<List<ChefOrderResponse>> getOrdersByStatus(ChefOrdersFetchByStatusRequest request) {
		try {
			Optional<User> existingUser = userRepository.findOneByEmail(request.getEmail());
			if (!existingUser.isPresent()) {
	  			throw new Exception(String.format("User not existing with email %s and cannot register as chef", request.getEmail()));
	  		}
			User user = existingUser.get();
			
			Optional<Chef> existingChef = chefRepository.findOneByUserId(user.getId());
	  		if (!existingChef.isPresent()) {
	  			throw new Exception(String.format("Chef not exsting with email: %s", request.getEmail()));
	  		}
	  		
	  		List<Order> orders = orderRepository.findAllByStatusIn(request.getStatusList());
	  		
	  		List<ChefOrderResponse> response = new ArrayList<>();
	  		
	  		orders.forEach(order -> {
	  			ChefOrderResponse chefOrderResponse = orderEntityToChefOrderResponseMapper.apply(order);
	  			response.add(chefOrderResponse);
	  		});
	  		
	  		return new ResponseEntity<List<ChefOrderResponse>>(response, HttpStatus.OK);
	  		
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
