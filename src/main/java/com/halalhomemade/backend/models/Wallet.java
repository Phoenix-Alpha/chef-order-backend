package com.halalhomemade.backend.models;

import com.halalhomemade.backend.models.audit.DateAudit;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "wallet")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Wallet extends DateAudit {
	
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  
  @NotNull
  @Column(name = "wallet_type")
  @Enumerated(EnumType.STRING)
  private WalletType type;
  
  @NotNull
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private WalletStatus status;
  
  @NotNull
  @Column(name = "hold")
  private BigDecimal hold;
  
  @NotNull
  @Column(name = "balance")
  private BigDecimal balance;
  
  @Column(name = "stripe_account_id")
  private String stripeAccountId;
  
  
}
