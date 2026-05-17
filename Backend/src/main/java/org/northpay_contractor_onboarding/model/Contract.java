package org.northpay_contractor_onboarding.model;

import jakarta.persistence.*;
import lombok.*;
import org.northpay_contractor_onboarding.enums.ContractStatus;

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

    @Column(columnDefinition = "TEXT")
    private String content;

    private String signedBy;
    private Boolean signed;
    private String contractHash;
    private String signatureReference;
    private LocalDateTime signedAt;
    private LocalDateTime created_at;
    @Enumerated(EnumType.STRING)
    private ContractStatus status = ContractStatus.PENDING_SIGNATURE ;

    @OneToOne
    @JoinColumn(name="onboardingId", nullable = false)
    private Onboarding onboarding;

}
