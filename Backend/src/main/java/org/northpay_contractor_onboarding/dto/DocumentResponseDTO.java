package org.northpay_contractor_onboarding.dto;


import lombok.*;
import org.northpay_contractor_onboarding.enums.DocumentStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponseDTO {

    private String document_id;
    private String fileType;
    private String fileSize;
    private String fileExtension;
    private String fileUrl;
    private String fileHash;
    private Integer version;
    private Boolean activeVersion;
    private DocumentStatus status;
    private String onboarding_id;

}
