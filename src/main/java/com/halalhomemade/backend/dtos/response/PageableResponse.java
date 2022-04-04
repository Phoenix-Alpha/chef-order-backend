package com.halalhomemade.backend.dtos.response;

import java.util.List;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class PageableResponse<T> {

  private int totalPages;
  private long totalElements;
  private int pageNumber;
  private int pageSize;
  private List<T> data;
}
