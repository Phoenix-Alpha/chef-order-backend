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
@Table(name = "offer")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Offer extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "chef_id", nullable = false)
  private Chef chef;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private OfferStatus status;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "offer_type")
  private OfferType offerType;
  
  @NotNull
  @Size(max = 32)
  @Column(name = "title")
  private String title;
  
  @NotNull
  @Size(max = 64)
  @Column(name = "description")
  private String description;
  
  @Column(name = "weight")
  private Integer weight;
  
  @Size(max = 128)
  @Column(name = "serving_address")
  private String servingAddress;
  
  @Size(max = 16)
  @Column(name = "serving_postcode")
  private String servingPostcode;
  
  @Size(max = 64)
  @Column(name = "serving_city")
  private String servingCity;
  
  @Column(name = "latitude")
  private BigDecimal latitude;
  
  @Column(name = "longitude")
  private BigDecimal longitude;
  
  @Column(name = "serving_start")
  private Instant servingStart;
  
  @Column(name = "serving_end")
  private Instant servingEnd;
  
  @Column(name = "order_until")
  private Instant orderUntil;
  
  // @ManyToOne(fetch = FetchType.EAGER)
  // @JoinColumn(name = "currency_id", nullable = false)
  // private Currency currency;

  @NotNull
  @Column(name = "price")
  private BigDecimal price;
  
  @Column(name = "min_free_delivery_amount")
  private BigDecimal minFreeDeliveryAmount;
  
  @Column(name = "max_quantity")
  private Integer maxQuantity;
  
  @Column(name = "quantity_available")
  private Integer quantityAvailable;
  
  @NotNull
  @Column(name = "is_pickup")
  private Boolean isPickup;
  
  @NotNull
  @Column(name = "is_delivery")
  private Boolean isDelivery;
  
  @Column(name = "min_preorder_hours")
  private Integer minPreorderHours;
  
  @Column(name = "zone1_max_distance")
  private BigDecimal zone1MaxDistance;
  
  @Column(name = "zone1_delivery_price")
  private BigDecimal zone1DeliveryPrice;
  
  @Column(name = "zone2_max_distance")
  private BigDecimal zone2MaxDistance;
  
  @Column(name = "zone2_delivery_price")
  private BigDecimal zone2DeliveryPrice;
  
  @Column(name = "zone3_max_distance")
  private BigDecimal zone3MaxDistance;
  
  @Column(name = "zone3_delivery_price")
  private BigDecimal zone3DeliveryPrice;
  
  @Size(max = 256)
  @Column(name = "offer_picture1")
  private String offerPicture1;
  
  @Size(max = 256)
  @Column(name = "offer_picture2")
  private String offerPicture2;
  
  @Size(max = 256)
  @Column(name = "offer_picture3")
  private String offerPicture3;
  
  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "offer_cuisine",
      joinColumns = @JoinColumn(name = "offer_id"),
      inverseJoinColumns = @JoinColumn(name = "cuisine_id"))
  @Builder.Default
  private Set<Cuisine> cuisines = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "offer")
  @Builder.Default
  private Set<CuisineOffer> offerCuisines = new HashSet<>();

  public void addCuisine(Cuisine cuisine) {
	if (!cuisines.contains(cuisine)) {
		cuisines.add(cuisine);
		cuisine.getOffers().add(this);
	}
  }

  public void removeCuisine(Cuisine cuisine) {
    cuisines.remove(cuisine);
    cuisine.getOffers().remove(this);
  }
  
  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "offer_tag",
      joinColumns = @JoinColumn(name = "offer_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  @Builder.Default
  private Set<Tag> tags = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "offer")
  @Builder.Default
  private Set<TagOffer> offerTags = new HashSet<>();

  public void addTag(Tag tag) {
	if (!tags.contains(tag)) {
		tags.add(tag);
		tag.getOffers().add(this);
	}
  }
  
  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "offer_allergen",
      joinColumns = @JoinColumn(name = "offer_id"),
      inverseJoinColumns = @JoinColumn(name = "allergen_id"))
  @Builder.Default
  private Set<Allergen> allergens = new HashSet<>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "offer")
  @Builder.Default
  private Set<AllergenOffer> offerAllergens = new HashSet<>();

  public void addAllergen(Allergen allergen) {
	if (!allergens.contains(allergen)) {
		allergens.add(allergen);
		allergen.getOffers().add(this);
	}
  }

  public void removeTag(Allergen allergen) {
	allergens.remove(allergen);
    allergen.getOffers().remove(this);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Offer)) return false;
    return Objects.equals(id, ((Offer) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
