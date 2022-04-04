package com.halalhomemade.backend.repositories;

import com.halalhomemade.backend.models.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {
  Optional<Wallet> findOneByUserId(Long userId); 
  Optional<Wallet> findOneByStripeAccountId(String stripeAccountId);
}
