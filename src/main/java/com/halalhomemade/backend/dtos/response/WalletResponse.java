package com.halalhomemade.backend.dtos.response;

import java.math.BigDecimal;
import com.halalhomemade.backend.models.WalletStatus;
import com.halalhomemade.backend.models.WalletType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class WalletResponse {
  private Long id;
  private Long userId;
  private WalletType type;
  private WalletStatus status;
  private BigDecimal hold;
  private BigDecimal balance;
  private String stripeAccountId;
}
