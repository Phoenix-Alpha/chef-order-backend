package com.halalhomemade.backend.dtos.request;

import java.math.BigDecimal;
import java.time.Instant;
import javax.validation.constraints.NotNull;
import com.halalhomemade.backend.models.OfferSortMode;
import com.halalhomemade.backend.models.OfferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicSearchOffersRequest {

	@NotNull
	private String userAddress;
	
	@NotNull
	private BigDecimal distance;
	
	@NotNull
	private OfferSortMode sortMode;
	
	private String offerName;
	
	private Long chefId;
	
	private String chefName;
	
	private BigDecimal chefRating;
	
	private OfferType offerType; // if null or not set, all offer types are considered...
	
	private Boolean isDelivery;
	
	private Boolean isPickup;
	
    private Instant servingDate;
}
