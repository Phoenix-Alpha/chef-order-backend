package com.halalhomemade.backend.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class PublicOfferSuggestResponse {
  	private Long offerId;
	private String title;
}
