package org.northpay_contractor_onboarding.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/* import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne; */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data
public class InvitationTokens {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(unique = true)
  private String token;
  private Boolean used;

  @Column(updatable = false)
  private LocalDateTime expiresAt;

  /* @OneToOne
  @JoinColumn(name = "onboarding_id")
  private Onboarding onboarding; */
  private UUID onboardingId; // reemplazar por la relación
}
