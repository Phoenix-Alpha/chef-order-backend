package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.CuisineOffer;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineOfferRepository extends JpaRepository<CuisineOffer, Long> {
	Optional<CuisineOffer> findByOfferId(Long id);
	List<CuisineOffer> findAllByOfferId(Long id);
	Long deleteByOfferId(Long offerId);
	Long deleteAllByOfferId(Long offerId);
}
