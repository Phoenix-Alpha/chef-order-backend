package com.halalhomemade.backend.dtos.request;

import java.time.Instant;
import javax.validation.constraints.NotNull;

import com.halalhomemade.backend.models.DeliveryMethod;
import com.halalhomemade.backend.models.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicOrderCreateRequest {

	@NotNull
	private Long offerId;
	
	@NotNull
	private String deviceIdentifier;
	
	@NotNull
	private String customerEmail;
	
	@NotNull
	private String customerPhoneNumber;
	
	@NotNull
	private String customerFirstName;
	
	@NotNull
	private String customerLastName;
	
	@NotNull
	private String deliveryStreetAddress;
	
	@NotNull
	private String deliveryCity;
	
	@NotNull
	private String deliveryPostcode;
	
	@NotNull
	private DeliveryMethod deliveryMethod;
	
	@NotNull
	private Integer quantity;
	
	@NotNull
	private PaymentMethod paymentMethod;
	
	@NotNull
	private Instant pickupDate;
	
	private String specialNote;
	
	private String coupon;

}
