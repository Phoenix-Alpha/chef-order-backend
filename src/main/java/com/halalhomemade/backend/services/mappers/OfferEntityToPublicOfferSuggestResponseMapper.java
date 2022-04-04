package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.PublicOfferSuggestResponse;
import com.halalhomemade.backend.models.Offer;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class OfferEntityToPublicOfferSuggestResponseMapper implements Function<Offer, PublicOfferSuggestResponse> {

	@Override
	public PublicOfferSuggestResponse apply(Offer offer) {
		return PublicOfferSuggestResponse.builder()
				.offerId(offer.getId())
				.title(offer.getTitle())
				.build();
	}
	
}

