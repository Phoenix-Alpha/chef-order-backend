package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Language;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

  Language findByLocale(String locale);

  Optional<Language> findOneByLocale(String locale);

  Optional<Language> findOneByName(String name);
}
