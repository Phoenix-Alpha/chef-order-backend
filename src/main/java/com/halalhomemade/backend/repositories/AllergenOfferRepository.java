package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.AllergenOffer;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergenOfferRepository extends JpaRepository<AllergenOffer, Long> {
	Optional<AllergenOffer> findByOfferId(Long id);
	List<AllergenOffer> findAllByOfferId(Long id);
	Long deleteByOfferId(Long offerId);
	Long deleteAllByOfferId(Long offerId);
}
