package com.halalhomemade.backend.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Position {
	private BigDecimal latitude;
	private BigDecimal longitude;
}
