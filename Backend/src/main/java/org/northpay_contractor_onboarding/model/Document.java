package org.northpay_contractor_onboarding.model;


import jakarta.persistence.*;
import lombok.*;
import org.northpay_contractor_onboarding.enums.DocumentStatus;
import org.northpay_contractor_onboarding.model.Onboarding;

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

    private String fileType;
    private String fileSize;
    private String fileExtension;
    private String fileUrl;
    @Column(unique = true)
    private String fileHash;
    private Integer version;
    private Boolean activeVersion;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status = DocumentStatus.PENDING_REVIEW;

    @OneToOne
    @JoinColumn(name="onboarding_id",
            referencedColumnName = "id"
            , nullable = false)
    private Onboarding onboarding;

}
