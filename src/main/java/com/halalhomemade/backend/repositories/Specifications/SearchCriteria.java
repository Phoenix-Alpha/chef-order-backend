package com.halalhomemade.backend.repositories.Specifications;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria implements Serializable {

  private String key;
  private Object value;
}
