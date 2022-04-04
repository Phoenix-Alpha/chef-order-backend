package com.halalhomemade.backend.services;

import com.halalhomemade.backend.dtos.request.PublicOrderCreateRequest;
import com.halalhomemade.backend.dtos.request.PublicOrderDetailFetchRequest;
import com.halalhomemade.backend.dtos.request.PublicOrderReviewRequest;
import com.halalhomemade.backend.dtos.request.PublicOrdersFetchByStatusRequest;
import com.halalhomemade.backend.dtos.response.PublicOfferResponse;
import com.halalhomemade.backend.dtos.response.PublicOrderResponse;
import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.IPublicOffer;
import com.halalhomemade.backend.models.OfferStatus;
import com.halalhomemade.backend.models.OfferType;
import com.halalhomemade.backend.models.Order;
import com.halalhomemade.backend.models.OrderStatus;
import com.halalhomemade.backend.models.PaymentStatus;
import com.halalhomemade.backend.models.Position;
import com.halalhomemade.backend.models.Review;
import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.Wallet;
import com.halalhomemade.backend.repositories.ChefRepository;
import com.halalhomemade.backend.repositories.OfferRepository;
import com.halalhomemade.backend.repositories.OrderRepository;
import com.halalhomemade.backend.repositories.ReviewRepository;
import com.halalhomemade.backend.repositories.WalletRepository;
import com.halalhomemade.backend.services.mappers.IPublicOfferToPublicOfferResponseMapper;
import com.halalhomemade.backend.services.mappers.PublicOrderCreateRequestToOrderEntityMapper;
import com.halalhomemade.backend.utils.EncDecSymmetric;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PublicOrderService {

	@Autowired private OfferRepository offerRepository;
	@Autowired private ChefRepository chefRepository;
	@Autowired private OrderRepository orderRepository;
	@Autowired private ReviewRepository reviewRepository;
	@Autowired private WalletRepository walletRepository;
	
	@Autowired private GoogleGeoApiService googleGeoApiService;
	@Autowired private StripeService stripeService;
	
	@Autowired private PublicOrderCreateRequestToOrderEntityMapper publicOrderCreateRequestToOrderEntityMapper;
	@Autowired private IPublicOfferToPublicOfferResponseMapper iPublicOfferToPublicOfferResponseMapper;
	@Autowired private EncDecSymmetric encDecSymmetric;
	
	public ResponseEntity<PublicOrderResponse> createOrder(PublicOrderCreateRequest request) {
		try {
			// Form full address
			String fullAddress = request.getDeliveryStreetAddress() + ", " + request.getDeliveryCity() + ", " + request.getDeliveryPostcode();
			
			// Retrieve latitude & longitude from user address
			Optional<Position> geoResult = Optional.ofNullable(googleGeoApiService.getGeoCodingResult(fullAddress));
		    BigDecimal userLat = null;
		    BigDecimal userLon = null;
			if (geoResult.isPresent()) {
				userLat = geoResult.get().getLatitude();
				userLon = geoResult.get().getLongitude();
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			// Retrieve offer from offer id in request
			Optional<IPublicOffer> existingOffer = offerRepository.searchOfferByIdAndAddress(request.getOfferId(), OfferStatus.ACTIVE.toString(), userLat, userLon);
			if (!existingOffer.isPresent()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			IPublicOffer offer = existingOffer.get();
			
			Optional<Chef> existingChef = chefRepository.findOneById(offer.getChefId());
			if (!existingChef.isPresent()) {
				throw new Exception("Chef not existing with the offer");
			}
			Chef chef = existingChef.get();
			
			// Create order from request
			Optional<Order> existingOrder = Optional.ofNullable(publicOrderCreateRequestToOrderEntityMapper.apply(request, offer));
			if (!existingOrder.isPresent()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			Order order = existingOrder.get();
			
			Customer customer = null;
			// Retrieve Stripe customer with email
			Optional<Customer> existingCustomer = Optional.ofNullable(stripeService.retrieveStripeCustomerByEmail(request.getCustomerEmail()));
			if (!existingCustomer.isPresent()) {
				// Create Stripe customer and store customer id to order
				customer = stripeService.createStripeCustomer(order);
				order.setStripeCustomerId(customer.getId());
			} else {
				customer = existingCustomer.get();
				order.setStripeCustomerId(customer.getId());
			}
			
			EphemeralKey ephemeralKey = stripeService.createStripeEphemeralKey(customer);
			
			PaymentIntent paymentIntent = stripeService.createStripePaymentIntent(order, customer);
			
			PublicOfferResponse offerResponse = iPublicOfferToPublicOfferResponseMapper.apply(existingOffer.get());
			
			PublicOrderResponse response = PublicOrderResponse.builder()
				.offer(offerResponse)
				.uuid(order.getUuid())
				.orderNumber(order.getOrderNumber())
				.status(order.getStatus())
				.pickupCode(order.getPickupCode())
				.customerEmail(order.getCustomerEmail())
				.customerFirstName(order.getCustomerFirstName())
				.customerLastName(order.getCustomerLastName())
				.customerPhoneNumber(order.getCustomerPhoneNumber())
				.deliveryMethod(order.getDeliveryMethod())
				.deliveryCity(order.getDeliveryCity())
				.deliveryPostcode(order.getDeliveryPostcode())
				.deliveryStreetAddress(order.getDeliveryStreetAddress())
				.pickupDate(order.getPickupDate())
				.pickupCode(order.getPickupCode())
				.quantity(order.getQuantity())
				.paymentMethod(order.getPaymentMethod())
				.paymentStatus(order.getPaymentStatus())
				.paymentLogs(order.getPaymentLogs())
				.totalDiscountedCost(order.getTotalDiscountedCost())
				.totalNonDiscountedCost(order.getTotalNonDiscountedCost())
				.coupon(order.getCoupon())
				.stripeCustomerId(customer.getId())
				.paymentIntentClientSecret(paymentIntent.getClientSecret())
				.ephemeralKey(ephemeralKey.getSecret())
				.build();
			
			// Finally, save order to repository
			Order savedOrder = orderRepository.save(order);
			
			// Update chef states
			
			// If offer type is PREORDER, plus one to mealsInprep
			// If offer type is ONDEMAND, plus one to mealsPending
			if (order.getOffer().getOfferType().equals(OfferType.PREORDER)) {
				chef.setMealsInprep(chef.getMealsInprep() + 1);
			} else if (order.getOffer().getOfferType().equals(OfferType.ONDEMAND)) {
				chef.setMealsPending(chef.getMealsPending() + 1);
			}
			Chef savedChef = chefRepository.save(chef);
			
			return new ResponseEntity<PublicOrderResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void updateOrderPaymentStatus(String uuid, PaymentStatus status) {
		try {
			Optional<Order> existingOrder = orderRepository.findOneByUuid(uuid);
			if (existingOrder.isPresent()) {
				Order order = existingOrder.get();
				Chef chef = order.getOffer().getChef();
				User user = chef.getUser();
				Optional<Wallet> existingWallet = walletRepository.findOneByUserId(chef.getUser().getId());
				if (!existingWallet.isPresent()) {
					throw new Exception(String.format("Stripe Webhook: Chef not existing with the order %s", uuid));
				}
				Wallet wallet = existingWallet.get();
				
				if (status.equals(PaymentStatus.SUCCESSFULLY_COMPLETED)) {
					// Order paid, updating chef wallet by adding it to hold...
					wallet.setHold(wallet.getHold().add(order.getTotalDiscountedCost()));
				} else if (status.equals(PaymentStatus.FAILED)) {
					// Order payment failed
				}
				// Save order status
				order.setPaymentStatus(status);
				Order savedOrder = orderRepository.save(order);
				
				// Save wallet status
				Wallet savedWallet = walletRepository.save(wallet);
			} else {
				
			}	
		} catch (Exception e) {
			System.out.println(e.toString());
			
		}
	}
	
	public ResponseEntity<PublicOrderResponse> getPublicOrderDetail(PublicOrderDetailFetchRequest request) {
		try {
			Optional<Order> existingOrder = orderRepository.findOneByUuidAndDeviceIdentifier(request.getOrderUuid(), request.getDeviceIdentifier());
			if (!existingOrder.isPresent()) {
				throw new Exception(String.format("Order not existing with uuid %s for device %s", request.getOrderUuid(), request.getDeviceIdentifier()));
			}
			Order order = existingOrder.get();
			
			Optional<IPublicOffer> existingOffer = offerRepository.searchOfferByIdAndAddress(order.getOffer().getId(), null, order.getDeliveryLatitude(), order.getDeliveryLongitude());
			if (!existingOffer.isPresent()) {
				throw new Exception(String.format("Offer not existing for order with uuid %s", request.getOrderUuid()));
			}
			IPublicOffer offer = existingOffer.get();
			
			PublicOfferResponse offerResponse = iPublicOfferToPublicOfferResponseMapper.apply(offer);
			
			PublicOrderResponse response = PublicOrderResponse.builder()
					.offer(offerResponse)
					.uuid(order.getUuid())
					.orderNumber(order.getOrderNumber())
					.status(order.getStatus())
					.pickupCode(order.getPickupCode())
					.customerEmail(order.getCustomerEmail())
					.customerFirstName(order.getCustomerFirstName())
					.customerLastName(order.getCustomerLastName())
					.customerPhoneNumber(order.getCustomerPhoneNumber())
					.deliveryMethod(order.getDeliveryMethod())
					.deliveryCity(order.getDeliveryCity())
					.deliveryPostcode(order.getDeliveryPostcode())
					.deliveryStreetAddress(order.getDeliveryStreetAddress())
					.pickupDate(order.getPickupDate())
					.pickupCode(order.getPickupCode())
					.quantity(order.getQuantity())
					.paymentMethod(order.getPaymentMethod())
					.paymentStatus(order.getPaymentStatus())
					.paymentLogs(order.getPaymentLogs())
					.totalDiscountedCost(order.getTotalDiscountedCost())
					.totalNonDiscountedCost(order.getTotalNonDiscountedCost())
					.coupon(order.getCoupon())
					.build();
			
			return new ResponseEntity<PublicOrderResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity<List<PublicOrderResponse>> getOrdersByStatus(PublicOrdersFetchByStatusRequest request) {
		try {
			List<Order> orders = orderRepository.findAllByStatusInAndDeviceIdentifier(request.getStatusList(), request.getDeviceIdentifier());
			
			List<PublicOrderResponse> response = new ArrayList<>();
			
			for(int i = 0; i < orders.size(); i++) {
				
				Optional<IPublicOffer> existingOffer = offerRepository.searchOfferByIdAndAddress(orders.get(i).getOffer().getId(), null, orders.get(i).getDeliveryLatitude(), orders.get(i).getDeliveryLongitude());
				if (!existingOffer.isPresent()) {
					throw new Exception(String.format("Offer not existing for order with uuid %s", orders.get(i).getOffer().getId()));
				}
				IPublicOffer offer = existingOffer.get();
				
				PublicOfferResponse offerResponse = iPublicOfferToPublicOfferResponseMapper.apply(offer);
				
				PublicOrderResponse publicOrderResponse = PublicOrderResponse.builder()
					.offer(offerResponse)
					.uuid(orders.get(i).getUuid())
					.orderNumber(orders.get(i).getOrderNumber())
					.status(orders.get(i).getStatus())
					.pickupCode(orders.get(i).getPickupCode())
					.customerEmail(orders.get(i).getCustomerEmail())
					.customerFirstName(orders.get(i).getCustomerFirstName())
					.customerLastName(orders.get(i).getCustomerLastName())
					.customerPhoneNumber(orders.get(i).getCustomerPhoneNumber())
					.deliveryMethod(orders.get(i).getDeliveryMethod())
					.deliveryCity(orders.get(i).getDeliveryCity())
					.deliveryPostcode(orders.get(i).getDeliveryPostcode())
					.deliveryStreetAddress(orders.get(i).getDeliveryStreetAddress())
					.pickupDate(orders.get(i).getPickupDate())
					.pickupCode(orders.get(i).getPickupCode())
					.quantity(orders.get(i).getQuantity())
					.paymentMethod(orders.get(i).getPaymentMethod())
					.paymentStatus(orders.get(i).getPaymentStatus())
					.paymentLogs(orders.get(i).getPaymentLogs())
					.totalDiscountedCost(orders.get(i).getTotalDiscountedCost())
					.totalNonDiscountedCost(orders.get(i).getTotalNonDiscountedCost())
					.coupon(orders.get(i).getCoupon())
					.build();
				
				response.add(publicOrderResponse);
			}
			
			return new ResponseEntity<List<PublicOrderResponse>>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	public ResponseEntity review(PublicOrderReviewRequest request) {
		try {
			Optional<Order> existingOrder = orderRepository.findOneByUuidAndDeviceIdentifier(request.getOrderUuid(), request.getDeviceIdentifier());
			if (!existingOrder.isPresent()) {
				throw new Exception(String.format("Order not existing with uuid %s and device identifer %s", request.getOrderUuid(), request.getDeviceIdentifier()));
			}
			Order order = existingOrder.get();
			
			// Check if the order status is 'CONFIRMED'
			if (!order.getStatus().equals(OrderStatus.CONFIRMED)) {
				throw new Exception(String.format("Order with uuid %s is not in confirmed status and cannot leave review", request.getOrderUuid()));
			}
			
			// Create review and save
			Review review = Review.builder()
				.offer(order.getOffer())
				.reviewerFirstName(order.getCustomerFirstName())
				.reviewerLastName(order.getCustomerLastName())
				.order(order)
				.reviewerFirstName(order.getCustomerFirstName())
				.reviewerLastName(order.getCustomerLastName())
				.chef(order.getOffer().getChef())
				.comment(request.getComment())
				.rating(request.getRating())
				.build();
			Review savedReview = reviewRepository.save(review);
			
			// Update order status
			order.setStatus(OrderStatus.REVIEWED);
			Order savedOrder = orderRepository.save(order);
			
			// Update chef status
			Chef chef = order.getOffer().getChef();
			chef.setTotalReviews(chef.getTotalReviews() + 1);
			Chef savedChef = chefRepository.save(chef);
			
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return ResponseEntity.badRequest().build();
		}
	}
	
}
