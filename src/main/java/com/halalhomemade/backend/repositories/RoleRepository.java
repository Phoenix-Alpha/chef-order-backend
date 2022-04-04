package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Role;
import com.halalhomemade.backend.models.UserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findOneByName(UserRole name);
}
