package com.halalhomemade.backend.dtos.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.halalhomemade.backend.models.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchOffersByStatusRequest {

  @Size(max = 254)
  private String email;
  
  @NotNull
  private OfferStatus status;
    
}
