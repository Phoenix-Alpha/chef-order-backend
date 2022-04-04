package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.ChefOfferResponse;
import com.halalhomemade.backend.dtos.response.ChefOrderResponse;
import com.halalhomemade.backend.models.Order;
import com.halalhomemade.backend.repositories.OfferRepository;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityToChefOrderResponseMapper implements Function<Order, ChefOrderResponse> {

	@Autowired private OfferEntityToDtoMapper offerEntityToDtoMapper;
	
	@Override
	public ChefOrderResponse apply(Order order) {
		
		ChefOfferResponse offerResponse = offerEntityToDtoMapper.apply(order.getOffer());
		
		ChefOrderResponse response = ChefOrderResponse.builder()
			.uuid(order.getUuid())
			.orderNumber(order.getOrderNumber())
			.status(order.getStatus())
			.customerEmail(order.getCustomerEmail())
			.customerFirstName(order.getCustomerFirstName())
			.customerLastName(order.getCustomerLastName())
			.customerPhoneNumber(order.getCustomerPhoneNumber())
			.deliveryMethod(order.getDeliveryMethod())
			.deliveryCity(order.getDeliveryCity())
			.deliveryPostcode(order.getDeliveryPostcode())
			.deliveryStreetAddress(order.getDeliveryStreetAddress())
			.deliveryCost(order.getDeliveryCost())
			.pickupDate(order.getPickupDate())
			.specialNote(order.getSpecialNote())
			.offer(offerResponse)
			.quantity(order.getQuantity())
			.paymentMethod(order.getPaymentMethod())
			.paymentStatus(order.getPaymentStatus())
//			.paymentLogs(order.getPaymentLogs())
			.totalDiscountedCost(order.getTotalDiscountedCost())
			.totalNonDiscountedCost(order.getTotalNonDiscountedCost())
			.coupon(order.getCoupon())
			.build();
		
		return response;
	}
	
}

