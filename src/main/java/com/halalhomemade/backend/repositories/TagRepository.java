package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Tag;
import com.halalhomemade.backend.models.FoodTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  Optional<Tag> findOneByName(FoodTag name);
}
