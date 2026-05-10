package org.northpay_contractor_onboarding.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID contract_id;

    private String document_url;
    private Boolean signed;
    private LocalDateTime created_at;

    private UUID onboarding_id;

    //@OneToOne
    //@JoinColumn(name="onboarding_id", nullable = false)
    //private Onboarding onboarding;

}
