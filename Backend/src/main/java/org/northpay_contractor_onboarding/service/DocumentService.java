package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.DocumentRequestDTO;
import org.northpay_contractor_onboarding.dto.DocumentResponseDTO;
import org.northpay_contractor_onboarding.enums.DocumentStatus;
import org.northpay_contractor_onboarding.model.Document;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.repository.DocumentRepository;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService implements IDocumentService{

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private OnboardingRepository onboardingRepository;

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

    //GET DOCUMENT BY ONBOARDING ID
    @Override
    public List<DocumentResponseDTO> getDocumentsByOnboardingId(UUID onboardingId){
        return null;
    }

    //UPLOAD DOCUMENT
    @Override
    public DocumentResponseDTO uploadDocument(MultipartFile file,
                                              UUID onboardingId) throws IOException {
        try {
            validateFile(file);

            Onboarding onboarding = onboardingRepository.findById(onboardingId).orElseThrow(
                    () -> new IllegalArgumentException("Onboarding not found")
            );

            String fileHash = calculateHash(file);

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path path = Paths.get(fileName);

            if (!Files.exists(path)){
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "/uploads/" + fileName;

            Document document = new Document();

            document.setFileUrl(fileUrl);
            document.setFileHash(fileHash);
            document.setFileSize(String.valueOf(file.getSize()));
            document.setFileType(file.getContentType());
            String originalName = file.getOriginalFilename();
            if(originalName != null && originalName.contains(".")){
                String extension = originalName.substring(originalName.lastIndexOf(".") + 1);
                document.setFileExtension(extension);
            }
            document.setVersion(1);
            document.setActiveVersion(true);
            document.setStatus(DocumentStatus.PENDING_REVIEW);
            document.setOnboarding(onboarding);

            Document savedDocument = documentRepository.save(document);

            return mapDocumentToDTO(savedDocument);
        }catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    //VALIDATE FILE
    private void validateFile(MultipartFile file){

        List<String> allowedExtensions = List.of(
                "application/pdf",
                "image/jpg",
                "image/jpeg",
                "image/png");

        if(!allowedExtensions.contains(file.getContentType())){
            throw new IllegalArgumentException("Invalid file type");
        }

        long maxSize = 5 * 1024 * 1024;

        if(file.getSize() > maxSize){
            throw new IllegalArgumentException("File size exceeds 1MB");
        }

    }

    //CALCULATE HASH
    private String calculateHash(MultipartFile file){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(file.getBytes());

            return String.format("%064x", new java.math.BigInteger(1, hash));

        }catch(Exception e){
            throw new RuntimeException("Error calculating hash: " + e.getMessage());
        }
    }

    //MAP DOCUMENT TO DTO
    private DocumentResponseDTO mapDocumentToDTO(Document document){
        DocumentResponseDTO dto = new DocumentResponseDTO();

        dto.setDocument_id(document.getDocument_id().toString());
        dto.setFileType(document.getFileType());
        dto.setFileUrl(document.getFileUrl());
        dto.setStatus(document.getStatus());
        dto.setVersion(document.getVersion());
        dto.setActiveVersion(document.getActiveVersion());
        dto.setFileHash(document.getFileHash());
        dto.setFileSize(document.getFileSize());
        dto.setFileExtension(document.getFileExtension());
        dto.setOnboarding_id(String.valueOf(document.getOnboarding().getId()));
        return dto;
    }


}
