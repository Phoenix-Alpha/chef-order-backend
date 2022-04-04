package com.halalhomemade.backend.dtos.request;

import com.halalhomemade.backend.models.SellPlan;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChefRegistrationRequest {

  @NotBlank
  @NotNull
  @Size(max = 254)
  private String email;
  
  @Size(max = 254)
  private String profilePicture;

  @NotBlank
  @NotNull
  @Size(max = 32)
  private String profileName;

  @NotBlank
  @NotNull
  @Size(max = 256)
  private String aboutMe;

  @NotNull
  private SellPlan sellPlan;
  
  @NotNull
  private List<String> cuisines;
  
  private String referralCode;
}
