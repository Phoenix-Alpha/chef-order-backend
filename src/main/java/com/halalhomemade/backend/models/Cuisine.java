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
@Table(name = "cuisine")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuisine implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 32)
  @NaturalId
  @Enumerated(EnumType.STRING)
  private FoodCuisine name;

  @ManyToMany(mappedBy = "cuisines")
  private Set<Chef> chefs = new HashSet<>();
  
  @ManyToMany(mappedBy = "cuisines")
  private Set<Offer> offers = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Cuisine cuisine = (Cuisine) o;
    return Objects.equals(name, cuisine.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
