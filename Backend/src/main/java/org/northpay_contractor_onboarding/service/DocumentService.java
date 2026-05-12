package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.DocumentRequestDTO;
import org.northpay_contractor_onboarding.dto.DocumentResponseDTO;
import org.northpay_contractor_onboarding.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService implements IDocumentService{

    @Autowired
    private DocumentRepository documentRepository;

    //GET ALL DOCUMENTS
    @Override
    public List<DocumentResponseDTO> getAllDocuments(){
        return null;
    }

    //GET DOCUMENT BY ID
    @Override
    public DocumentResponseDTO getDocumentById(String id){
        return null;
    }

    //CREATE DOCUMENT
    @Override
    public DocumentResponseDTO createDocument(DocumentRequestDTO document){
        return null;
    }

    //UPDATE DOCUMENT
    @Override
    public DocumentResponseDTO updateDocument(String id, DocumentRequestDTO document){
        return null;
    }

    //DELETE DOCUMENT
    @Override
    public void deleteDocument(String id){

    }

}
