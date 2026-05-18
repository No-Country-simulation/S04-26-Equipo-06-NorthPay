package org.northpay_contractor_onboarding.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Onboarding {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "contractor_id")
  private Contractor contractor;
  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private OnboardingStatus status;
  @Column(name = "current_step")
  private int currentStep;
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "update_at")
  private LocalDateTime updatedAt;
  @OneToOne(mappedBy = "onboarding", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PaymentMethod paymentMethod;
  @OneToOne(mappedBy = "onboarding", cascade = CascadeType.ALL)
  private Contract contract;
  @OneToMany(mappedBy = "onboarding", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Document> documents;
  @OneToMany(mappedBy = "onboarding", cascade = CascadeType.ALL)
  private List<OnboardingHistory> history;

}