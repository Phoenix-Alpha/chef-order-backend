package com.halalhomemade.backend.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageResponse {
  private Long id;
  private String name;
  private String locale;
//  private String code;
}
