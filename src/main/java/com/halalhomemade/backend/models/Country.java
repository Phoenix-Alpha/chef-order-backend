package com.halalhomemade.backend.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "country")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Country implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 32)
  @Column(name = "name", unique = true)
  private String name;

  @NotBlank
  @Size(max = 32)
  @Column(name = "code", unique = true)
  private String code;
  
  @NotBlank
  @Size(max = 6)
  @Column(name = "phone_code", unique = true)
  private String phoneCode;

  @Size(max = 11)
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "currency_id", nullable = false)
  private Currency currency;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Country)) return false;

    Country other = (Country) o;

    return id != null && id.equals(other.getId());
  }

  @Override
  public int hashCode() {
    return 34;
  }
}
