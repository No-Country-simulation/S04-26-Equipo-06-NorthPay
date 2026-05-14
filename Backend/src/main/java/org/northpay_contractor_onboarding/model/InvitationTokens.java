package org.northpay_contractor_onboarding.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data @Builder(toBuilder = true)
public class InvitationTokens {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(unique = true)
  private String tokenUrl;
  private Boolean used;
  private Boolean isValid;
  private String contractorEmail;
  private String password;
  /**
   * Esta valor puede sacarse del token JWT, para así no tener que llamar a la base de datos
   */
  @Column(updatable = false, name = "created_by")
  private String operatorEmail;

  @CreationTimestamp
  private Instant createdAt;
  @Column(updatable = false)
  private LocalDateTime expiresAt;

  @OneToOne
  @JoinColumn(name = "onboarding_id")
  private Onboarding onboarding;
}
