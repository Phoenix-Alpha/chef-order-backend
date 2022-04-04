package com.halalhomemade.backend.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class OfferDeliveryZone {
	private BigDecimal maxDistance;
	private BigDecimal deliveryPrice;
}
