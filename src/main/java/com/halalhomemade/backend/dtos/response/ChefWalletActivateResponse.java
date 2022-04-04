package com.halalhomemade.backend.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ChefWalletActivateResponse {
  private WalletResponse walletResponse;
  private String accountLinkUrl;
  private Long expiresAt;
}
