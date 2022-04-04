package com.halalhomemade.backend.dtos.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.halalhomemade.backend.models.DeliveryMethod;
import com.halalhomemade.backend.models.OrderStatus;
import com.halalhomemade.backend.models.PaymentMethod;
import com.halalhomemade.backend.models.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ChefOrderResponse {
	private String uuid;
	private String orderNumber;
	private OrderStatus status;
	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;
	private String customerPhoneNumber;
	private String deliveryStreetAddress;
	private String deliveryCity;
	private String deliveryPostcode;
	private BigDecimal deliveryCost;
	private DeliveryMethod deliveryMethod;
	private ChefOfferResponse offer;
	private Integer quantity;
	private BigDecimal totalNonDiscountedCost;
	private BigDecimal totalDiscountedCost;
	private String coupon;
	private String specialNote;
	private Instant pickupDate;
	private PaymentMethod paymentMethod;
	private PaymentStatus paymentStatus;
//	private String paymentLogs;
}
