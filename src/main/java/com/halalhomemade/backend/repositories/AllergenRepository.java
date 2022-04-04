package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Allergen;
import com.halalhomemade.backend.models.FoodAllergen;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, Long> {
  Optional<Allergen> findOneByName(FoodAllergen name);
}
