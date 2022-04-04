package com.halalhomemade.backend.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "currency")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Currency implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 8)
  @Column(name = "name", unique = true)
  private String name;

  @NotBlank
  @Size(max = 3)
  @Column(name = "iso_code", unique = true)
  private String isoCode;

  @NotBlank
  @Size(max = 5)
  @Column(name = "symbol")
  private String symbol;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Currency)) return false;

    Currency other = (Currency) o;

    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
