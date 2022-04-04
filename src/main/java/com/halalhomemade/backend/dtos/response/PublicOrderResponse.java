package com.halalhomemade.backend.dtos.response;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.halalhomemade.backend.models.Currency;
import com.halalhomemade.backend.models.DeliveryMethod;
import com.halalhomemade.backend.models.Offer;
import com.halalhomemade.backend.models.OrderStatus;
import com.halalhomemade.backend.models.PaymentMethod;
import com.halalhomemade.backend.models.PaymentStatus;
import com.halalhomemade.backend.models.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class PublicOrderResponse {
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
	private DeliveryMethod deliveryMethod;
	private PublicOfferResponse offer;
	private Integer quantity;
	private BigDecimal totalNonDiscountedCost;
	private BigDecimal totalDiscountedCost;
	private String coupon;
	private PaymentMethod paymentMethod;
	private PaymentStatus paymentStatus;
	private String pickupCode;
	private String paymentLogs;
	private Instant pickupDate;
//	private String stripeCheckoutSessionUrl;
	private String paymentIntentClientSecret;
	private String ephemeralKey;
	private String stripeCustomerId;
}
