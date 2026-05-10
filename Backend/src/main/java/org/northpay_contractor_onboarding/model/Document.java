package org.northpay_contractor_onboarding.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID document_id;

    private String type;
    private String url;
    private String status;
    private UUID onboarding_id;

    //@OneToOne
    //@JoinColumn(name="onboarding_id", nullable = false)
    //private Onboarding onboarding;

}
