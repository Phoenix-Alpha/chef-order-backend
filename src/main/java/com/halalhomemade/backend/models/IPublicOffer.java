package com.halalhomemade.backend.models;

import java.math.BigDecimal;
import java.time.Instant;

public interface IPublicOffer {
	public Long getChefId();
	public String getChefProfileName();
	public String getChefProfilePicture();
	public Integer getChefTotalReviews();
	public BigDecimal getChefRating();
	
	public Long getOfferId();
	public OfferStatus getStatus();
	public OfferType getOfferType();
	public String getTitle();
	public String getDescription();
	public Integer getWeight();
	
	public String getServingAddress();
	public String getServingPostcode();
	public String getServingCity();
	public BigDecimal getLatitude();
	public BigDecimal getLongitude();
	public BigDecimal getDistance();
	public BigDecimal getDistanceScore();
	
	public Instant getServingStart();
	public Instant getServingEnd();
	public Instant getOrderUntil();
	public BigDecimal getDateDiff();
	public BigDecimal getDateScore();
	
	public BigDecimal getPrice();
	public BigDecimal getPriceScore();
	
	public BigDecimal getMinFreeDeliveryAmount();
	public BigDecimal getDeliveryCost();
	public BigDecimal getDeliveryCostScore();
	
	public Integer getMaxQuantity();
	public Integer getQuantityAvailable();
	
	public Boolean getIsPickup();
	public Boolean getIsDelivery();
	public Integer getMinPreorderHours();
	
	public BigDecimal getZone1MaxDistance();
	public BigDecimal getZone1DeliveryPrice();
	public BigDecimal getZone2MaxDistance();
	public BigDecimal getZone2DeliveryPrice();
	public BigDecimal getZone3MaxDistance();
	public BigDecimal getZone3DeliveryPrice();
	public String getOfferPicture1();
	public String getOfferPicture2();
	public String getOfferPicture3();
	
	public BigDecimal getTotalScore();
}
