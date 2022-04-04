package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Cuisine;
import com.halalhomemade.backend.models.FoodCuisine;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
  Optional<Cuisine> findOneByName(FoodCuisine name);
}
