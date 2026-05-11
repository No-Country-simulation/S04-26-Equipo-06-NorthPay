package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.ContractResponseDTO;
import org.northpay_contractor_onboarding.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    //GET ALL CONTRACTS
    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> getAllContracts(){
        return null;
    }

    //GET CONTRACT BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<ContractResponseDTO> getContractById(@PathVariable String id){
        return null;
    }

    //CREATE CONTRACT
    @PostMapping("/create")
    public ResponseEntity<ContractResponseDTO> createContract(
            @RequestBody ContractResponseDTO contract){
        return null;
    }

    //UPDATE CONTRACT
    @PatchMapping("/update/{id}")
    public ResponseEntity<ContractResponseDTO> updateContract(
            @PathVariable String id,
            @RequestBody ContractResponseDTO contract
    ){
        return null;
    }

    //DELETE CONTRACT
    @DeleteMapping("/delete/{id}")
    public void deleteContract(@PathVariable String id){

    }


}
