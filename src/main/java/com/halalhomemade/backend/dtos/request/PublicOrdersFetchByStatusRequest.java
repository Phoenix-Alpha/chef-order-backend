package com.halalhomemade.backend.dtos.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import com.halalhomemade.backend.models.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicOrdersFetchByStatusRequest {

	@NotNull
	private String deviceIdentifier;
	
	@NotNull
	private List<OrderStatus> statusList;

}
