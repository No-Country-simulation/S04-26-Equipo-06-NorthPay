package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.DocumentResponseDTO;
import org.northpay_contractor_onboarding.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {


    @Autowired
    private DocumentService documentService;

    //GET ALL DOCUMENTS
    @GetMapping("/getAll")
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments(){
        return null;
    }

    //GET DOCUMENT BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable String id){
        return null;
    }

    //GET DOCUMENT BY ONBOARDING ID
    @GetMapping("/getByOnboardingId/{onboardingId}")
    public ResponseEntity<List<DocumentResponseDTO>> getDocumentsByOnboardingId(@PathVariable String onboardingId){
        return null;
    }

    //UPLOAD DOCUMENT
    @PostMapping("/upload")
    public ResponseEntity<DocumentResponseDTO> uploadDocument(@RequestParam MultipartFile file,
                                                              @RequestParam UUID onboardingId) throws IOException {


        DocumentResponseDTO response = documentService.uploadDocument(file, onboardingId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

}
