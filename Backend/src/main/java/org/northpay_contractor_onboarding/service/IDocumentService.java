package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.DocumentRequestDTO;
import org.northpay_contractor_onboarding.dto.DocumentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IDocumentService {

    List<DocumentResponseDTO> getAllDocuments();
    List<DocumentResponseDTO> getDocumentsByOnboardingId(UUID onboardingId);
    DocumentResponseDTO getDocumentById(String id);
    DocumentResponseDTO uploadDocument(MultipartFile file, UUID onboardingId) throws IOException;

}
