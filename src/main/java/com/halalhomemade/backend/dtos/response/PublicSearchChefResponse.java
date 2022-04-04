package com.halalhomemade.backend.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class PublicSearchChefResponse {
  private Long chefId;
  private String chefProfileName;
}
