package com.halalhomemade.backend.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "allergen")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allergen implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 32)
  @NaturalId
  @Enumerated(EnumType.STRING)
  private FoodAllergen name;
  
  @NotBlank
  @Size(max = 64)
  private String label;
  
  @ManyToMany(mappedBy = "allergens")
  private Set<Offer> offers = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Allergen allergen = (Allergen) o;
    return Objects.equals(name, allergen.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
