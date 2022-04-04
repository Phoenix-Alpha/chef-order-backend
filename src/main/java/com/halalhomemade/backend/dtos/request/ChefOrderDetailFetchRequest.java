package com.halalhomemade.backend.dtos.request;

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
public class ChefOrderDetailFetchRequest {

  @Size(max = 254)
  private String email;
  
  @NotNull
  private String orderUuid;
    
}
