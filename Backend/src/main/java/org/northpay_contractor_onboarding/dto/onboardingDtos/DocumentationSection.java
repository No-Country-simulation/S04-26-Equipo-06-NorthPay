package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.util.List;




import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentationSection {

    private final String documentType;

    private final String documentNumber;

    private final List<FileDTO> urlFiles;
    
}
