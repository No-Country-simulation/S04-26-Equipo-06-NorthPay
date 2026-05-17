package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.ContractRequestDTO;
import org.northpay_contractor_onboarding.dto.ContractResponseDTO;
import org.northpay_contractor_onboarding.dto.SignContractRequestDTO;
import org.northpay_contractor_onboarding.dto.UpdateContractStatusDTO;
import org.northpay_contractor_onboarding.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    //GET ALL CONTRACTS
    @GetMapping
    public ResponseEntity<List<ContractResponseDTO>> getAllContracts(){

        List<ContractResponseDTO> contracts = contractService.getAllContracts();

        return ResponseEntity.status(200).body(contracts);
    }

    //GET CONTRACT BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<ContractResponseDTO> getContractById(@PathVariable String id){

        ContractResponseDTO contract = contractService.getContractById(id);

        return ResponseEntity.status(200).body(contract);
    }

    //GET CONTRACT BY ONBOARDING ID
    @GetMapping("/getByOnboardingId/{onboardingId}")
    public ResponseEntity<ContractResponseDTO> getContractByOnboardingId(@PathVariable UUID onboardingId){
        return ResponseEntity.status(200).body(contractService.getContractByOnboardingId(onboardingId));
    }

    //CREATE CONTRACT
    @PostMapping("/create/{onboardingId}")
    public ResponseEntity<ContractResponseDTO> createContract(
            @RequestBody ContractRequestDTO contract,
            @PathVariable UUID onboardingId){
       return ResponseEntity.status(HttpStatus.OK).
               body(contractService.createContract(contract, onboardingId));
    }

    //SIGN CONTRACT
    @PostMapping("/sign_contract/{contractId}")
    public String signContract(@RequestBody SignContractRequestDTO dto,
                               @PathVariable UUID contractId){
        return contractService.signContract(contractId, dto);
    }

    //UPDATE CONTRACT STATUS
    @PatchMapping("/update_contract_status/{contractId}")
    public ResponseEntity<String> updateContractStatus(@PathVariable UUID contractId,
                                                       @RequestBody UpdateContractStatusDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(contractService.changeContractStatus(
                contractId, dto
        ));
    }

    //CANCEL CONTRACT
    @DeleteMapping("/delete/{id}")
    public void cancelContract(@PathVariable String id){

    }


}
