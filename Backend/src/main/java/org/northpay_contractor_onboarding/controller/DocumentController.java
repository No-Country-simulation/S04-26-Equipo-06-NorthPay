package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.DocumentResponseDTO;
import org.northpay_contractor_onboarding.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    //CREATE DOCUMENT
    @PostMapping("/create")
    public ResponseEntity<DocumentResponseDTO> createDocument(
            @RequestBody DocumentResponseDTO document){
        return null;
    }

    //UPDATE DOCUMENT
    @PatchMapping("/update/{id}")
    public ResponseEntity<DocumentResponseDTO> updateDocument(@PathVariable String id,
                                                              @RequestBody DocumentResponseDTO document){
        return null;
    }

    //DELETE DOCUMENT
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String id){
        return null;
    }
}
