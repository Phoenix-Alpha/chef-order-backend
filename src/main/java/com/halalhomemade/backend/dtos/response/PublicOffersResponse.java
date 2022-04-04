package com.halalhomemade.backend.dtos.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PublicOffersResponse extends PageableResponse<PublicOfferResponse> {}
