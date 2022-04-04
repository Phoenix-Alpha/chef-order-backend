package com.halalhomemade.backend.services.mappers;

import com.halalhomemade.backend.dtos.response.LanguageResponse;
import com.halalhomemade.backend.models.Language;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class LanguageEntityToDtoMapper implements Function<Language, LanguageResponse> {

  @Override
  public LanguageResponse apply(Language language) {
    return LanguageResponse.builder()
        .id(language.getId())
        .name(language.getName())
        .locale(language.getLocale())
//        .code(language.getCode())
        .build();
  }
}
