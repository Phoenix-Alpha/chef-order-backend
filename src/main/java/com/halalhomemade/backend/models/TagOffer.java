package com.halalhomemade.backend.models;

import java.io.Serializable;
import java.util.Objects;

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
@Table(name = "offer_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagOffer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "offer_id", nullable = false)
  private Offer offer;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "tag_id", nullable = false)
  private Tag tag;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof TagOffer)) return false;

    TagOffer other = (TagOffer) o;

    return id != null && id.equals(other.getId());
  }

}
