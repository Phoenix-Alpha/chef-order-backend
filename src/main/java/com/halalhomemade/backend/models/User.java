package com.halalhomemade.backend.models;

import com.halalhomemade.backend.models.audit.DateAudit;
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
@Table(
    name = "users",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"email"})
    })
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends DateAudit {
	
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Size(max = 100)
  private String password;
  
  @NaturalId
  @NotBlank
  @Size(max = 254)
  @Email
  @Column(name = "email")
  private String email;
  
  @NotNull
  @Column(name = "email_verified")
  @Enumerated(EnumType.STRING)
  private VerificationStatus emailVerified;

  @Size(max = 32)
  @Column(name = "first_name")
  private String firstName;

  @Size(max = 32)
  @Column(name = "last_name")
  private String lastName;
  
  @Column(name = "birthdate")
  private Instant birthdate;
  
  @Size(max = 6)
  @Column(name = "phone_country_code")
  private String phoneCode;

  @Size(max = 16)
  @Column(name = "phone_number")
  private String phoneNumber;

  @NotNull
  @Column(name = "phone_verified")
  @Enumerated(EnumType.STRING)
  private VerificationStatus phoneVerified;
  
  @Size(max = 6)
  @Column(name = "phone_verification_code")
  private String phoneVerificationCode;
  
  @Column(name = "phone_verification_code_created_at")
  private Instant phoneVerificationCodeCreatedAt;

  @Size(max = 16)
  @Column(name = "post_code")
  private String postCode;
  
  @Size(max = 16)
  @Column(name = "city")
  private String city;
  
  @Size(max = 254)
  @Column(name = "address")
  private String address;
  
  @Size(max = 64)
  @Column(name = "device_token")
  private String deviceToken;
    
  @NotNull
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @NotNull
  @Column(name = "authentication_method")
  @Enumerated(EnumType.STRING)
  private AuthenticationMethod authenticationMethod;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "preferred_language_id", nullable = false)
  private Language preferredLanguage;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "country_id", nullable = true)
  private Country country;

//  @Column(name = "last_login_at")
//  private Instant lastLoginAt;

//  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//  @JoinColumn(name = "google_account_id", referencedColumnName = "id")
//  private SocialAccount googleAccount;
//
//  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//  @JoinColumn(name = "facebook_account_id", referencedColumnName = "id")
//  private SocialAccount facebookAccount;

  @ManyToMany(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_user",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

//  @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
//  @Builder.Default
//  private List<RoleUser> userRoles = new ArrayList<>();

  public void addRole(Role role) {
    roles.add(role);
    role.getUsers().add(this);
  }

  public void removeRole(Role role) {
    roles.remove(role);
    role.getUsers().remove(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    return Objects.equals(email, ((User) o).getEmail());
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  public boolean isSuperAdmin() {
    return roles.stream()
        .filter(role -> role.getName() == UserRole.SUPER_ADMIN)
        .findFirst()
        .isPresent();
  }
}
