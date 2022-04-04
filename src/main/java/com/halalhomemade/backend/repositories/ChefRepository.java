package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Chef;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long>, JpaSpecificationExecutor<Chef> {
	Optional<Chef> findOneById(Long id);
	Optional<Chef> findOneByUserId(Long userId);
	List<Chef> findByProfileNameLike(String chefName);
}
