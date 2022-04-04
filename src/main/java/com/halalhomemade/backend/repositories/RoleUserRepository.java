package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.RoleUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {

  Optional<RoleUser> findOneByUserId(Long id);

  Long deleteByUserId(Long userId);

  Long deleteAllByUserId(Long userId);
}
