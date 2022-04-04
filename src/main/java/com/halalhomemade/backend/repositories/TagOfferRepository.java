package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.TagOffer;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagOfferRepository extends JpaRepository<TagOffer, Long> {
	Optional<TagOffer> findByOfferId(Long id);
	List<TagOffer> findAllByOfferId(Long id);
	Long deleteByOfferId(Long offerId);
	Long deleteAllByOfferId(Long offerId);
}
