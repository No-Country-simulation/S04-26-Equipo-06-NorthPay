package org.northpay_contractor_onboarding.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    // TODO lo comente para que compile ya mapie las clase que me faltaban en onboarding
    // private UUID onboarding_id;

    @OneToOne
    @JoinColumn(name = "onboarding_id", nullable = false)
    private Onboarding onboarding;

}
