package com.halalhomemade.backend.dtos.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicOrderDetailFetchRequest {

	@NotNull
	private String orderUuid;
	
	@NotNull
	private String deviceIdentifier;
  
}
