package com.halalhomemade.backend.models;

import com.halalhomemade.backend.models.audit.DateAudit;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "chef")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Chef extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private ChefStatus status;

  @Size(max = 256)
  @Column(name = "profile_picture")
  private String profilePicture;
  
  @Size(max = 32)
  @Column(name = "profile_name")
  private String profileName;
  
  @NotBlank
  @NotNull
  @Size(max = 256)
  @Column(name = "about_me")
  private String aboutMe;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "sell_plan")
  private SellPlan sellPlan;
  
  @Column(name = "active_offers")
  private Integer activeOffers;
  
  @Column(name = "meals_pending")
  private Integer mealsPending;
  
  @Column(name = "meals_inprep")
  private Integer mealsInprep;
  
  @Column(name = "meals_served")
  private Integer mealsServed;
  
  @Column(name = "total_reviews")
  private Integer totalReviews;
  
  @Size(max = 20)
  @Column(name = "referral_code")
  private String referralCode;

  @NotNull
  @Column(name = "rating")
  private BigDecimal rating;
  
  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "chef_cuisine",
      joinColumns = @JoinColumn(name = "chef_id"),
      inverseJoinColumns = @JoinColumn(name = "cuisine_id"))
  @Builder.Default
  private Set<Cuisine> cuisines = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "chef")
  @Builder.Default
  private Set<CuisineChef> chefCuisines = new HashSet<>();

  public void addCuisine(Cuisine cuisine) {
	if (!cuisines.contains(cuisine)) {
		cuisines.add(cuisine);
		cuisine.getChefs().add(this);
	}
  }

  public void removeCuisine(Cuisine cuisine) {
    cuisines.remove(cuisine);
    cuisine.getChefs().remove(this);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Chef)) return false;
    return Objects.equals(id, ((Chef) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
