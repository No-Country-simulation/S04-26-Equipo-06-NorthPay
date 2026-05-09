package org.northpay_contractor_onboarding.model;

import java.time.LocalDateTime;

public class Onboarding {
  private Long id;
  private Contractor contractor;
  private int currentStep;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt; 

}