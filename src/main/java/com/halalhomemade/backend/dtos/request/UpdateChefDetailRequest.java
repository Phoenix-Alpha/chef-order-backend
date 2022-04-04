package com.halalhomemade.backend.dtos.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.halalhomemade.backend.models.SellPlan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateChefDetailRequest {

  @NotBlank
  @NotNull
  @Size(max = 254)
  private String email;
  
  @Size(max = 20)
  private String profileName;

  @Size(max = 256)
  private String aboutMe;

  private SellPlan sellPlan;
  
  @NotNull
  private List<String> cuisines;
  
}
