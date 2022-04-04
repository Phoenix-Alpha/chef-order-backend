package com.halalhomemade.backend.services;

import com.halalhomemade.backend.exceptions.LanguageNotFoundException;
import com.halalhomemade.backend.models.Language;
import com.halalhomemade.backend.repositories.LanguageRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LanguageService {

  @Autowired private LanguageRepository languageRepository;

  @Transactional(readOnly = true)
  public Language getLanguageById(Long id) {
    return languageRepository
        .findById(id)
        .orElseThrow(
            () ->
                new LanguageNotFoundException(String.format("No language found with id: %d", id)));
  }

  @Transactional(readOnly = true)
  public Language getLanguageByName(String name) {
    return languageRepository
        .findOneByName(name)
        .orElseThrow(
            () ->
                new LanguageNotFoundException(
                    String.format("No language found with name: %s", name)));
  }

  @Transactional(readOnly = true)
  public Language getLanguageByLocale(String locale) {
    return languageRepository
        .findOneByLocale(locale)
        .orElseThrow(
            () ->
                new LanguageNotFoundException(
                    String.format("No language found with locale: %s", locale)));
  }

}
