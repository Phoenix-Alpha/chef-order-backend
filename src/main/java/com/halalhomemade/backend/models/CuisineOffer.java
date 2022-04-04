package com.halalhomemade.backend.models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offer_cuisine")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuisineOffer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "offer_id", nullable = false)
  private Offer offer;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "cuisine_id", nullable = false)
  private Cuisine cuisine;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof CuisineOffer)) return false;

    CuisineOffer other = (CuisineOffer) o;

    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
