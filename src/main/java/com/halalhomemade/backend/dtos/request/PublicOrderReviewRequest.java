package com.halalhomemade.backend.dtos.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicOrderReviewRequest {

	@NotNull
	private String orderUuid;
	
	@NotNull
	private String deviceIdentifier;
	
	@NotNull
	@Size(max = 512)
	private String comment;
	
	@NotNull
	private BigDecimal rating;
	
}
