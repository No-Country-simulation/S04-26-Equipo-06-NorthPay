package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.DocumentRequestDTO;
import org.northpay_contractor_onboarding.dto.DocumentResponseDTO;

import java.util.List;

public interface IDocumentService {

    List<DocumentResponseDTO> getAllDocuments();
    DocumentResponseDTO getDocumentById(String id);
    DocumentResponseDTO createDocument(DocumentRequestDTO document);
    DocumentResponseDTO updateDocument(String id, DocumentRequestDTO document);
    void deleteDocument(String id);

}
