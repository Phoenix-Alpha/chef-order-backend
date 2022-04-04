package com.halalhomemade.backend.models;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "language")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language implements Serializable {

  public static final String ENGLISH_LANGUAGE_NAME = "English";
  public static final String FRENCH_LANGUAGE_NAME = "French";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 128)
  private String name;

  @Size(max = 6)
  private String locale;

//  @Size(max = 10)
//  private String code;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Language)) return false;

    Language other = (Language) o;

    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
