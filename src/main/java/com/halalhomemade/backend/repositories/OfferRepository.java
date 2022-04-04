package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Chef;
import com.halalhomemade.backend.models.Offer;
import com.halalhomemade.backend.models.OfferStatus;
import com.halalhomemade.backend.models.OfferType;
import com.halalhomemade.backend.models.IPublicOffer;
import com.halalhomemade.backend.models.IPublicOfferMaxMin;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
//public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {
public interface OfferRepository extends JpaRepository<Offer, Long> {
	
	Optional<Offer> findOneById(Long offerId);
	
	List<Offer> findAllByChefAndStatus(Chef chef, OfferStatus status);
	
	List<Offer> findByTitleLike(String offerName);
	
	@Query(
			value = "SELECT * "
					+ "FROM offer o "
					+ "WHERE (:status IS NULL OR o.status = :status) "
					+ "LIMIT :limit",
			nativeQuery = true)
	List<Offer> searchTopOffers(String status, Integer limit);
	
	@Query(
			value = "SELECT *, "
					+ "(oc2.distanceScore * 2.0 + oc2.priceScore + oc2.dateScore) AS totalScore "
					+ "FROM (SELECT *, "
					+ "((oc1.distance - :minDistance) / (:maxDistance - :minDistance)) AS distanceScore, "
					+ "((oc1.price - :minPrice) / (:maxPrice - :minPrice)) AS priceScore, "
					+ "CASE "
					+ "WHEN (oc1.minFreeDeliveryAmount IS NOT NULL AND oc1.price > oc1.minFreeDeliveryAmount) THEN 0 "
					+ "WHEN (oc1.zone1MaxDistance IS NOT NULL AND oc1.zone1DeliveryPrice IS NOT NULL AND oc1.distance < oc1.zone1MaxDistance) THEN oc1.zone1DeliveryPrice "
					+ "WHEN (oc1.zone2MaxDistance IS NOT NULL AND oc1.zone2DeliveryPrice IS NOT NULL AND oc1.distance < oc1.zone2MaxDistance) THEN oc1.zone2DeliveryPrice "
					+ "WHEN (oc1.zone3MaxDistance IS NOT NULL AND oc1.zone3DeliveryPrice IS NOT NULL AND oc1.distance < oc1.zone3MaxDistance) THEN oc1.zone3DeliveryPrice "
					+ "ELSE 0 "
					+ "END AS deliveryCost, "
					+ "CASE "
					+ "WHEN (oc1.dateDiff >= 0) THEN ((oc1.dateDiff - :minDateDiff) / (:maxDateDiff - :minDateDiff)) "
					+ "ELSE  0 "
					+ "END AS dateScore "
					+ "FROM (SELECT c.id AS chefId, c.profile_name AS chefProfileName, c.profile_picture AS chefProfilePicture, c.total_reviews AS chefTotalReviews, c.rating AS chefRating, "
					+ "o.id AS offerId, o.status AS status, o.offer_type AS offerType, o.title AS title, "
					+ "o.description AS description, o.weight AS weight, "
					+ "o.serving_address AS servingAddress, o.serving_postcode AS servingPostcode, o.serving_city AS servingCity, "
					+ "o.latitude AS latitude, o.longitude AS longitude, "
					+ "(((ACOS(SIN((:userLat * PI() / 180.0)) * SIN((latitude * PI() / 180.0))"
					+ " + COS((:userLat * PI() / 180.0)) * COS((latitude * PI() / 180.0))"
					+ " * COS(((:userLon - longitude) * PI() / 180.0)))) * 180.0 / PI()) * 60.0 * 1.1515 * 1.609344) AS distance, "
					+ "o.zone1_max_distance AS zone1MaxDistance, o.zone1_delivery_price AS zone1DeliveryPrice, "
					+ "o.zone2_max_distance AS zone2MaxDistance, o.zone2_delivery_price AS zone2DeliveryPrice, "
					+ "o.zone3_max_distance AS zone3MaxDistance, o.zone3_delivery_price AS zone3DeliveryPrice, "
					+ "o.offer_picture1 AS offerPicture1, o.offer_picture2 AS offerPicture2, o.offer_picture3 AS offerPicture3, "
					+ "o.price AS price, "
					+ "o.serving_start AS servingStart, o.serving_end AS servingEnd, o.order_until AS orderUntil, "
					+ "TIMESTAMPDIFF(HOUR, :now, o.serving_start) as dateDiff, "
					+ "o.max_quantity AS maxQuantity, o.quantity_available AS quantityAvailable, "
					+ "o.is_pickup AS isPickup, o.is_delivery AS isDelivery, "
					+ "o.min_free_delivery_amount AS minFreeDeliveryAmount, "
					+ "o.min_preorder_hours AS minPreorderHours "
					+ "FROM offer o "
					+ "JOIN chef c "
					+ "ON o.chef_id = c.id) AS oc1 "
					+ "WHERE "
					+ "(:chefId IS NULL OR oc1.chefId = :chefId) AND "
					+ "(:status IS NULL OR oc1.status = :status) AND "
					+ "(:offerType IS NULL OR oc1.offerType = :offerType) AND "
					+ "(oc1.distance < :distance) AND "
					+ "(:chefRating IS NULL OR oc1.chefRating >= :chefRating) AND "
					+ "(:isPickup IS NULL OR oc1.isPickup = :isPickup) AND "
					+ "(:isDelivery IS NULL OR oc1.isDelivery = :isDelivery) AND "
					+ "(:servingDateFrom IS NULL OR :servingDateTo IS NULL OR ((servingStart IS NOT NULL) AND (servingEnd IS NOT NULL) AND (servingStart BETWEEN :servingDateFrom AND :servingDateTo) AND (servingEnd BETWEEN :servingDateFrom AND :servingDateTo)))) AS oc2 "
					+ "ORDER BY "
					+ "CASE WHEN (:sortMode = 'BESTMATCH') THEN totalScore END DESC, "
					+ "CASE WHEN (:sortMode = 'REVIEWS') THEN chefTotalReviews END DESC, "
					+ "CASE WHEN (:sortMode = 'DISTANCE') THEN distance END ASC, "
					+ "CASE WHEN (:sortMode = 'PRICE') THEN price END ASC, "
					+ "CASE WHEN (:sortMode = 'DELIVERYCOST') THEN deliveryCost END ASC, "
					+ "CASE WHEN (:sortMode = 'SERVINGDATE') THEN servingStart END ASC ",
			nativeQuery = true)
	List<IPublicOffer> searchOffersList(Long chefId, String status, BigDecimal userLat, BigDecimal userLon, BigDecimal distance, BigDecimal minDistance, BigDecimal maxDistance, BigDecimal minPrice, BigDecimal maxPrice, String offerType, BigDecimal chefRating, Boolean isDelivery, Boolean isPickup, Instant servingDateFrom, Instant servingDateTo, Instant now, BigDecimal minDateDiff, BigDecimal maxDateDiff, String sortMode);
	
	
	@Query(
			value = "SELECT *, "
					+ "CASE "
					+ "WHEN (oc1.minFreeDeliveryAmount IS NOT NULL AND oc1.price > oc1.minFreeDeliveryAmount) THEN 0 "
					+ "WHEN (oc1.zone1MaxDistance IS NOT NULL AND oc1.zone1DeliveryPrice IS NOT NULL AND oc1.distance IS NOT NULL AND oc1.distance < oc1.zone1MaxDistance) THEN oc1.zone1DeliveryPrice "
					+ "WHEN (oc1.zone2MaxDistance IS NOT NULL AND oc1.zone2DeliveryPrice IS NOT NULL AND oc1.distance IS NOT NULL AND oc1.distance < oc1.zone2MaxDistance) THEN oc1.zone2DeliveryPrice "
					+ "WHEN (oc1.zone3MaxDistance IS NOT NULL AND oc1.zone3DeliveryPrice IS NOT NULL AND oc1.distance IS NOT NULL AND oc1.distance < oc1.zone3MaxDistance) THEN oc1.zone3DeliveryPrice "
					+ "ELSE NULL "
					+ "END AS deliveryCost "
					+ "FROM (SELECT c.id AS chefId, c.profile_name AS chefProfileName, c.profile_picture AS chefProfilePicture, c.total_reviews AS chefTotalReviews, c.rating AS chefRating, "
					+ "o.id AS offerId, o.status AS status, o.offer_type AS offerType, o.title AS title, "
					+ "o.description AS description, o.weight AS weight, "
					+ "o.serving_address AS servingAddress, o.serving_postcode AS servingPostcode, o.serving_city AS servingCity, "
					+ "o.latitude AS latitude, o.longitude AS longitude, "
					+ "CASE "
					+ "WHEN (:userLat IS NOT NULL AND :userLon IS NOT NULL) THEN "
					+ "(((ACOS(SIN((:userLat * PI() / 180.0)) * SIN((latitude * PI() / 180.0))"
					+ " + COS((:userLat * PI() / 180.0)) * COS((latitude * PI() / 180.0))"
					+ " * COS(((:userLon - longitude) * PI() / 180.0)))) * 180.0 / PI()) * 60.0 * 1.1515 * 1.609344) "
					+ "ELSE NULL "
					+ "END AS distance, "
					+ "o.zone1_max_distance AS zone1MaxDistance, o.zone1_delivery_price AS zone1DeliveryPrice, "
					+ "o.zone2_max_distance AS zone2MaxDistance, o.zone2_delivery_price AS zone2DeliveryPrice, "
					+ "o.zone3_max_distance AS zone3MaxDistance, o.zone3_delivery_price AS zone3DeliveryPrice, "
					+ "o.offer_picture1 AS offerPicture1, o.offer_picture2 AS offerPicture2, o.offer_picture3 AS offerPicture3, "
					+ "o.price AS price, "
					+ "o.serving_start AS servingStart, o.serving_end AS servingEnd, o.order_until AS orderUntil, "
					+ "o.max_quantity AS maxQuantity, o.quantity_available AS quantityAvailable, "
					+ "o.is_pickup AS isPickup, o.is_delivery AS isDelivery, "
					+ "o.min_free_delivery_amount AS minFreeDeliveryAmount, "
					+ "o.min_preorder_hours AS minPreorderHours "
					+ "FROM offer o "
					+ "JOIN chef c "
					+ "ON o.chef_id = c.id) AS oc1 "
					+ "WHERE "
					+ "(oc1.offerId = :offerId) AND "
					+ "(:status IS NULL OR oc1.status = :status)",
			nativeQuery = true)
	Optional<IPublicOffer> searchOfferByIdAndAddress(Long offerId, String status, BigDecimal userLat, BigDecimal userLon);
	
	@Query(
			value = "SELECT *, "
					+ "(oc2.distanceScore * 2.0 + oc2.priceScore + oc2.dateScore) AS totalScore "
					+ "FROM (SELECT *, "
					+ "((oc1.distance - :minDistance) / (:maxDistance - :minDistance)) AS distanceScore, "
					+ "((oc1.price - :minPrice) / (:maxPrice - :minPrice)) AS priceScore, "
					+ "CASE "
					+ "WHEN (oc1.minFreeDeliveryAmount IS NOT NULL AND oc1.price > oc1.minFreeDeliveryAmount) THEN 0 "
					+ "WHEN (oc1.zone1MaxDistance IS NOT NULL AND oc1.zone1DeliveryPrice IS NOT NULL AND oc1.distance < oc1.zone1MaxDistance) THEN oc1.zone1DeliveryPrice "
					+ "WHEN (oc1.zone2MaxDistance IS NOT NULL AND oc1.zone2DeliveryPrice IS NOT NULL AND oc1.distance < oc1.zone2MaxDistance) THEN oc1.zone2DeliveryPrice "
					+ "WHEN (oc1.zone3MaxDistance IS NOT NULL AND oc1.zone3DeliveryPrice IS NOT NULL AND oc1.distance < oc1.zone3MaxDistance) THEN oc1.zone3DeliveryPrice "
					+ "ELSE 0 "
					+ "END AS deliveryCost, "
					+ "CASE "
					+ "WHEN (oc1.dateDiff >= 0) THEN ((oc1.dateDiff - :minDateDiff) / (:maxDateDiff - :minDateDiff)) "
					+ "ELSE  0 "
					+ "END AS dateScore "
					+ "FROM (SELECT c.id AS chefId, c.profile_name AS chefProfileName, c.profile_picture AS chefProfilePicture, c.total_reviews AS chefTotalReviews, c.rating AS chefRating, "
					+ "o.id AS offerId, o.status AS status, o.offer_type AS offerType, o.title AS title, "
					+ "o.description AS description, o.weight AS weight, "
					+ "o.serving_address AS servingAddress, o.serving_postcode AS servingPostcode, o.serving_city AS servingCity, "
					+ "o.latitude AS latitude, o.longitude AS longitude, "
					+ "(((ACOS(SIN((:userLat * PI() / 180.0)) * SIN((latitude * PI() / 180.0))"
					+ " + COS((:userLat * PI() / 180.0)) * COS((latitude * PI() / 180.0))"
					+ " * COS(((:userLon - longitude) * PI() / 180.0)))) * 180.0 / PI()) * 60.0 * 1.1515 * 1.609344) AS distance, "
					+ "o.zone1_max_distance AS zone1MaxDistance, o.zone1_delivery_price AS zone1DeliveryPrice, "
					+ "o.zone2_max_distance AS zone2MaxDistance, o.zone2_delivery_price AS zone2DeliveryPrice, "
					+ "o.zone3_max_distance AS zone3MaxDistance, o.zone3_delivery_price AS zone3DeliveryPrice, "
					+ "o.offer_picture1 AS offerPicture1, o.offer_picture2 AS offerPicture2, o.offer_picture3 AS offerPicture3, "
					+ "o.price AS price, "
					+ "o.serving_start AS servingStart, o.serving_end AS servingEnd, o.order_until AS orderUntil, "
					+ "TIMESTAMPDIFF(HOUR, :now, o.serving_start) as dateDiff, "
					+ "o.max_quantity AS maxQuantity, o.quantity_available AS quantityAvailable, "
					+ "o.is_pickup AS isPickup, o.is_delivery AS isDelivery, "
					+ "o.min_free_delivery_amount AS minFreeDeliveryAmount, "
					+ "o.min_preorder_hours AS minPreorderHours "
					+ "FROM offer o "
					+ "JOIN chef c "
					+ "ON o.chef_id = c.id) AS oc1 "
					+ "WHERE "
					+ "(:chefId IS NULL OR oc1.chefId = :chefId) AND "
					+ "(:chefName IS NULL OR oc1.chefProfileName LIKE :chefName) AND "
					+ "(:offerName IS NULL OR oc1.title LIKE :offerName) AND "
					+ "(:status IS NULL OR oc1.status = :status) AND "
					+ "(:offerType IS NULL OR oc1.offerType = :offerType) AND "
					+ "(oc1.distance < :distance) AND "
					+ "(:chefRating IS NULL OR oc1.chefRating >= :chefRating) AND "
					+ "(:isPickup IS NULL OR oc1.isPickup = :isPickup) AND "
					+ "(:isDelivery IS NULL OR oc1.isDelivery = :isDelivery) AND "
					+ "(:servingDateFrom IS NULL OR :servingDateTo IS NULL OR ((servingStart IS NOT NULL) AND (servingEnd IS NOT NULL) AND (servingStart BETWEEN :servingDateFrom AND :servingDateTo) AND (servingEnd BETWEEN :servingDateFrom AND :servingDateTo)))) AS oc2 "
					+ "ORDER BY "
					+ "CASE WHEN (:sortMode = 'BESTMATCH') THEN totalScore END DESC, "
					+ "CASE WHEN (:sortMode = 'REVIEWS') THEN chefTotalReviews END DESC, "
					+ "CASE WHEN (:sortMode = 'DISTANCE') THEN distance END ASC, "
					+ "CASE WHEN (:sortMode = 'PRICE') THEN price END ASC, "
					+ "CASE WHEN (:sortMode = 'DELIVERYCOST') THEN deliveryCost END ASC, "
					+ "CASE WHEN (:sortMode = 'SERVINGDATE') THEN servingStart END ASC ",
			nativeQuery = true)
	Page<IPublicOffer> searchOffer(Long chefId, String chefName, String offerName, String status, BigDecimal userLat, BigDecimal userLon, BigDecimal distance, BigDecimal minDistance, BigDecimal maxDistance, BigDecimal minPrice, BigDecimal maxPrice, String offerType, BigDecimal chefRating, Boolean isDelivery, Boolean isPickup, Instant servingDateFrom, Instant servingDateTo, Instant now, BigDecimal minDateDiff, BigDecimal maxDateDiff, String sortMode, Pageable pageable);
	
	@Query(
			value = "SELECT "
					+ "COUNT(*) as totalElements, "
					+ "MIN(oc1.distance) as minDistance, MAX(oc1.distance) as maxDistance, "
					+ "MIN(oc1.price) as minPrice, MAX(oc1.price) as maxPrice,"
					+ "GREATEST(MIN(oc1.dateDiff), 0) as minDateDiff, LEAST(GREATEST(MAX(oc1.dateDiff), 0), 600) as maxDateDiff "
					+ "FROM (SELECT c.id AS chefId, c.profile_name AS chefProfileName, c.profile_picture AS chefProfilePicture, c.total_reviews AS chefTotalReviews, c.rating AS chefRating, "
					+ "o.id AS offerId, o.status AS status, o.offer_type AS offerType, o.title AS title, "
					+ "o.description AS description, o.weight AS weight, "
					+ "o.serving_address AS servingAddress, o.serving_postcode AS servingPostcode, o.serving_city AS servingCity, "
					+ "o.latitude AS latitude, o.longitude AS longitude, "
					+ "(((ACOS(SIN((:userLat * PI() / 180.0)) * SIN((latitude * PI() / 180.0))"
					+ " + COS((:userLat * PI() / 180.0)) * COS((latitude * PI() / 180.0))"
					+ " * COS(((:userLon - longitude) * PI() / 180.0)))) * 180.0 / PI()) * 60.0 * 1.1515 * 1.609344) AS distance, "
					+ "o.zone1_max_distance AS zone1MaxDistance, o.zone1_delivery_price AS zone1DeliveryPrice, "
					+ "o.zone2_max_distance AS zone2MaxDistance, o.zone2_delivery_price AS zone2DeliveryPrice, "
					+ "o.zone3_max_distance AS zone3MaxDistance, o.zone3_delivery_price AS zone3DeliveryPrice, "
					+ "o.offer_picture1 AS offerPicture1, o.offer_picture2 AS offerPicture2, o.offer_picture3 AS offerPicture3, "
					+ "o.price AS price, "
					+ "o.serving_start AS servingStart, o.serving_end AS servingEnd, o.order_until AS orderUntil, "
					+ "TIMESTAMPDIFF(HOUR, :now, o.serving_start) as dateDiff, "
					+ "o.max_quantity AS maxQuantity, o.quantity_available AS quantityAvailable, "
					+ "o.is_pickup AS isPickup, o.is_delivery AS isDelivery, "
					+ "o.min_free_delivery_amount AS minFreeDeliveryAmount, "
					+ "o.min_preorder_hours AS minPreorderHours "
					+ "FROM offer o "
					+ "JOIN chef c "
					+ "ON o.chef_id = c.id) AS oc1 "
					+ "WHERE "
					+ "(:chefId IS NULL OR oc1.chefId = :chefId) AND "
					+ "(:chefName IS NULL OR oc1.chefProfileName LIKE :chefName) AND "
					+ "(:offerName IS NULL OR oc1.title LIKE :offerName) AND "
					+ "(:status IS NULL OR oc1.status = :status) AND "
					+ "(:offerType IS NULL OR oc1.offerType = :offerType) AND "
					+ "(oc1.distance < :distance) AND "
					+ "(:chefRating IS NULL OR oc1.chefRating >= :chefRating) AND "
					+ "(:isPickup IS NULL OR oc1.isPickup = :isPickup) AND "
					+ "(:isDelivery IS NULL OR oc1.isDelivery = :isDelivery) AND "
					+ "(:servingDateFrom IS NULL OR :servingDateTo IS NULL OR ((servingStart IS NOT NULL) AND (servingEnd IS NOT NULL) AND (servingStart BETWEEN :servingDateFrom AND :servingDateTo) AND (servingEnd BETWEEN :servingDateFrom AND :servingDateTo)))",
			nativeQuery = true)
	IPublicOfferMaxMin findMaxMinForSearchOffer(Long chefId, String chefName, String offerName, String status, BigDecimal userLat, BigDecimal userLon, BigDecimal distance, String offerType, BigDecimal chefRating, Boolean isDelivery, Boolean isPickup, Instant servingDateFrom, Instant servingDateTo, Instant now);
	
}
