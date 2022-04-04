package com.halalhomemade.backend.models;

import java.math.BigDecimal;

public interface IPublicOfferMaxMin {
	
	public BigDecimal getMinDistance();
	public BigDecimal getMaxDistance();
	
	public BigDecimal getMinPrice();
	public BigDecimal getMaxPrice();
	
	public BigDecimal getMinDateDiff();
	public BigDecimal getMaxDateDiff();
	
	public Integer getTotalElements();
}
