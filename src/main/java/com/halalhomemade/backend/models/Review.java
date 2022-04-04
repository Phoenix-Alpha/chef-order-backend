package com.halalhomemade.backend.models;

import com.halalhomemade.backend.models.audit.DateAudit;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "review")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Review extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "reviewer_id")
  private User reviewer;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "chef_id")
  private Chef chef;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "offer_id", nullable = false)
  private Offer offer;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;
  
  @NotNull
  @Column(name = "rating")
  private BigDecimal rating;
  
  @Size(max = 32)
  @Column(name = "reviewer_first_name")
  private String reviewerFirstName;

  @Size(max = 32)
  @Column(name = "reviewer_last_name")
  private String reviewerLastName;
  
  @Size(max = 512)
  @Column(name = "comment")
  private String comment;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Review)) return false;
    return Objects.equals(id, ((Review) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
