package org.northpay_contractor_onboarding.model;

import java.time.LocalDateTime;

public class Onboarding {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @ManyToOne(fetch = FetchType.LAZY)
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

  

}