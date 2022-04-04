package com.halalhomemade.backend.dtos.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.halalhomemade.backend.models.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefOrdersFetchByStatusRequest {

  @Size(max = 254)
  private String email;
  
  @NotNull
  private List<OrderStatus> statusList;
}
