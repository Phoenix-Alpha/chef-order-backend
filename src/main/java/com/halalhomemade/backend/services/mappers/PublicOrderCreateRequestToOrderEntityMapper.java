package com.halalhomemade.backend.services.mappers;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.halalhomemade.backend.dtos.request.PublicOrderCreateRequest;
import com.halalhomemade.backend.dtos.response.PublicOrderResponse;
import com.halalhomemade.backend.models.DeliveryMethod;
import com.halalhomemade.backend.models.IPublicOffer;
import com.halalhomemade.backend.models.Offer;
import com.halalhomemade.backend.models.Order;
import com.halalhomemade.backend.models.OrderStatus;
import com.halalhomemade.backend.models.PaymentStatus;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.OfferRepository;
import com.halalhomemade.backend.repositories.OrderRepository;
import com.halalhomemade.backend.utils.TokenUtils;
import com.halalhomemade.backend.constants.IApplicationConstants;

@Component
public class PublicOrderCreateRequestToOrderEntityMapper implements BiFunction<PublicOrderCreateRequest, IPublicOffer, Order> {
	
	@Autowired OrderRepository orderRepository;
	@Autowired OfferRepository offerRepository;
	@Autowired ChefRepository chefRepository;
	
	@Override
    public Order apply(PublicOrderCreateRequest request, IPublicOffer iPublicOffer) {
		try {
			Optional<Offer> existingOffer = offerRepository.findOneById(request.getOfferId());
			
			if (!existingOffer.isPresent()) {
				throw new Exception("Offer does not exist");
			}
			
			Offer offer = existingOffer.get();
			
			Order order = Order.builder()
				.uuid(UUID.randomUUID().toString())
				.deviceIdentifier(request.getDeviceIdentifier())
				.status(OrderStatus.SUBMITTED)
				.customerEmail(request.getCustomerEmail())
				.customerFirstName(request.getCustomerFirstName())
				.customerLastName(request.getCustomerLastName())
				.customerPhoneNumber(request.getCustomerPhoneNumber())
				.paymentMethod(request.getPaymentMethod())
				.paymentStatus(PaymentStatus.INITIAL)
				.quantity(request.getQuantity())
				.orderNumber(TokenUtils.generateToken(IApplicationConstants.ORDER_NUMBER_LENGTH, false))
				.specialNote(request.getSpecialNote())
				.pickupCode(TokenUtils.generateToken(IApplicationConstants.ORDER_PICKUP_CODE_LENGTH, false))
				.pickupDate(request.getPickupDate())
				.specialNote(request.getSpecialNote())
				.deliveryPostcode(request.getDeliveryPostcode())
				.deliveryStreetAddress(request.getDeliveryStreetAddress())
				.deliveryCity(request.getDeliveryCity())
				.deliveryLatitude(iPublicOffer.getLatitude())
				.deliveryLongitude(iPublicOffer.getLongitude())
				.deliveryMethod(request.getDeliveryMethod())
				.deliveryCost(iPublicOffer.getDeliveryCost())
				.build();
			
			order.setOffer(offer);
			
			order.setTotalNonDiscountedCost(offer.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
			order.setTotalDiscountedCost(order.getTotalNonDiscountedCost());
			
			Optional<BigDecimal> existingDeliveryCost = Optional.ofNullable(iPublicOffer.getDeliveryCost());
			
			// Add delivery cost if the delivery method is "DELIVERY"
			if (request.getDeliveryMethod().equals(DeliveryMethod.DELIVERY) && existingDeliveryCost.isPresent()) {
				order.setTotalNonDiscountedCost(order.getTotalDiscountedCost().add(existingDeliveryCost.get()));
				order.setTotalDiscountedCost(order.getTotalNonDiscountedCost());
			}
						
			Optional<String> existingCoupon = Optional.ofNullable(request.getCoupon());
			if (existingCoupon.isPresent()) {
				order.setCoupon(existingCoupon.get());
				// Validate coupon code here and apply to total discounted cost...
				order.setTotalDiscountedCost(order.getTotalNonDiscountedCost());	
			}
			
			return order;	
		} catch (Exception e) {
			return null;
		}
	}
}
