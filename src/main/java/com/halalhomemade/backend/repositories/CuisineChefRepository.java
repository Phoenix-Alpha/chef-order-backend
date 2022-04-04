package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.CuisineChef;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuisineChefRepository extends JpaRepository<CuisineChef, Long> {

  Optional<CuisineChef> findOneByChefId(Long id);
  
  Long deleteByChefId(Long chefId);

  Long deleteAllByChefId(Long chefId);
}
