package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.util.List;


import org.northpay_contractor_onboarding.serializers.ProtectDataSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentationSection {

    private final String documentType;

    @JsonSerialize(using = ProtectDataSerializer.class)
    private final String documentNumber;

    private final List<FileDTO> urlFiles;
    
}
