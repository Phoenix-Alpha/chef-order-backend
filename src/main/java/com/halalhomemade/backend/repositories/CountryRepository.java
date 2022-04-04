package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Country;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
  Optional<Country> findOneByName(String name);
}
