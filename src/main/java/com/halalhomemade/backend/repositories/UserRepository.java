package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.User;
import com.halalhomemade.backend.models.UserStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  Optional<User> findOneByEmail(String email);
  Optional<User> findOneByEmailAndStatus(String email, UserStatus status);
}
