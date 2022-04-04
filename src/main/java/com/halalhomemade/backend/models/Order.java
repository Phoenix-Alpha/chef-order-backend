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
@Table(name = "orders")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "offer_id", nullable = false)
  private Offer offer;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private OrderStatus status;
  
  @NotNull
  @Size(max = 40)
  @Column(name = "uuid")
  private String uuid;
  
  @NotNull
  @Size(max = 40)
  @Column(name = "device_identifier")
  private String deviceIdentifier;
  
  @NotNull
  @Size(max = 6)
  @Column(name = "order_number")
  private String orderNumber;
  
  @NotNull
  @Size(max = 32)
  @Column(name = "customer_first_name")
  private String customerFirstName;
  
  @NotNull
  @Size(max = 32)
  @Column(name = "customer_last_name")
  private String customerLastName;
  
  @NotNull
  @Size(max = 64)
  @Column(name = "customer_email")
  private String customerEmail;
  
  @NotNull
  @Size(max = 64)
  @Column(name = "customer_phone_number")
  private String customerPhoneNumber;
  
  @NotNull
  @Size(max = 512)
  @Column(name = "delivery_street_address")
  private String deliveryStreetAddress;
  
  @NotNull
  @Size(max = 64)
  @Column(name = "delivery_city")
  private String deliveryCity;
  
  @NotNull
  @Size(max = 16)
  @Column(name = "delivery_postcode")
  private String deliveryPostcode;
  
  @Column(name = "delivery_latitude")
  private BigDecimal deliveryLatitude;
  
  @Column(name = "delivery_longitude")
  private BigDecimal deliveryLongitude;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "delivery_method")
  private DeliveryMethod deliveryMethod;
  
  @Column(name = "delivery_cost")
  private BigDecimal deliveryCost;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentMethod paymentMethod;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status")
  private PaymentStatus paymentStatus;
  
  @Column(name = "quantity")
  private Integer quantity;
  
  @Column(name = "total_non_discounted_cost")
  private BigDecimal totalNonDiscountedCost;
  
  @Column(name = "total_discounted_cost")
  private BigDecimal totalDiscountedCost;
  
  // @ManyToOne(fetch = FetchType.EAGER)
  // @JoinColumn(name = "currency_id", nullable = false)
  // private Currency currency;
  
  @Size(max = 20)
  @Column(name = "coupon")
  private String coupon;
  
  @Size(max = 512)
  @Column(name = "special_note")
  private String specialNote;
  
  @Size(max = 512)
  @Column(name = "payment_logs")
  private String paymentLogs;
  
  @NotNull
  @Size(max = 20)
  @Column(name = "pickup_code")
  private String pickupCode;
  
  @NotNull
  @Column(name = "pickup_date")
  private Instant pickupDate;
  
  @Size(max = 64)
  @Column(name = "stripe_customer_id")
  private String stripeCustomerId;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Order)) return false;
    return Objects.equals(id, ((Order) o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

}
