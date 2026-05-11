package org.northpay_contractor_onboarding.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class Operators {
  @Id @GeneratedValue
  private UUID id;
  @Column(unique = true)
  private String email;
  private String name;
  private String password;
}
