package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.ContractRequestDTO;
import org.northpay_contractor_onboarding.dto.ContractResponseDTO;
import org.northpay_contractor_onboarding.dto.SignContractRequestDTO;
import org.northpay_contractor_onboarding.dto.UpdateContractStatusDTO;
import org.northpay_contractor_onboarding.enums.ContractStatus;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.repository.ContractRepository;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.northpay_contractor_onboarding.model.Contract;
import org.northpay_contractor_onboarding.service.StateMachineService;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContractService implements IContractService{

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private StateMachineService stateMachineService;

    //GET ALL CONTRACTS
    @Override
    public List<ContractResponseDTO> getAllContracts(){
        List<Contract> contracts = contractRepository.findAll();

        return contracts.stream().map(this::mapToDTO).toList();
    }

    //GET CONTRACT BY ID
    @Override
    public ContractResponseDTO getContractById(String id){
        Contract contract = contractRepository.findById(UUID.fromString(id))
                .orElseThrow(()-> new IllegalArgumentException("Contract not found"));
        return mapToDTO(contract);
    }

    //GET CONTRACT BY ONBOARDING ID
    @Override
    public ContractResponseDTO getContractByOnboardingId(UUID onboardingId){
        Contract contract = contractRepository.findByOnboardingId(onboardingId);
        if(contract == null) {
            throw new IllegalArgumentException("Contract not found");
        }

        return mapToDTO(contract);
    }

    //CREATE CONTRACT
    @Override
    public ContractResponseDTO createContract(ContractRequestDTO contract, UUID onboardingId){

        Onboarding onboarding = onboardingRepository.findById(onboardingId).orElseThrow(
                ()-> new IllegalArgumentException("Onboarding not found")
        );

        if (contractRepository.findByOnboardingId(onboardingId) != null){
            throw new IllegalArgumentException("Contract already exists for this onboarding");
        }

        Contract newContract = Contract.builder()
                .content(contract.getContent())
                .signed(false)
                .status(ContractStatus.PENDING_SIGNATURE)
                .created_at(LocalDateTime.now())
                .onboarding(onboarding)
                .build();

        Contract saved =  contractRepository.save(newContract);

        return mapToDTO(saved);
    }

    //SIGN CONTRACT
    @Override
    @Transactional
    public String signContract(UUID contractId,
                               SignContractRequestDTO requestDTO){

        Contract contract = contractRepository.findById(contractId).orElseThrow(
                () -> new NotFoundException("Contract not found with ID: " + contractId)
        );

        contract.setSignedAt(LocalDateTime.now());
        contract.setContractHash(generateHash(contract.getContent()));
        contract.setSignatureReference(UUID.randomUUID().toString());
        contract.setSigned(true);
        contract.setSignedBy(requestDTO.getSignature());
        contract.setStatus(ContractStatus.SIGNED);
        contractRepository.save(contract);

        Onboarding onboarding = contract.getOnboarding();
        if(onboarding != null && (onboarding.getCurrentStep() == null || onboarding.getCurrentStep() < 4)) {
            stateMachineService.transitionTo(onboarding, OnboardingStatus.CONTRACT_SIGNED, "USER");

            onboarding.setCurrentStep(4);
            onboardingRepository.save(onboarding);
        }

        return "Contract has been signed Successfully";
    }

    //UPDATE CONTRACT STATUS
    @Override
    public String changeContractStatus(UUID contractId,
                                      UpdateContractStatusDTO dto){
        Contract contract = contractRepository.findById(contractId).orElseThrow(
                ()-> new NotFoundException("Contract not found with ID: " + contractId)
        );

        contract.setStatus(ContractStatus.valueOf(dto.getStatus()));
        contractRepository.save(contract);
        return "Contract status has been updated successfully";
    }

    //DELETE CONTRACT
    @Override
    public void cancelContract(UUID contractId){}

    //MAP TO DTO
    public ContractResponseDTO mapToDTO(Contract contract){

        ContractResponseDTO dto = ContractResponseDTO.builder()
                .contract_id(contract.getContract_id().toString())
                .content(contract.getContent())
                .signedBy(contract.getSignedBy())
                .signatureReference(contract.getSignatureReference())
                .contractHash(contract.getContractHash())
                .signedAt(contract.getSignedAt())
                .signed(contract.getSigned())
                .created_at(contract.getCreated_at())
                .status(contract.getStatus().toString())
                .onboarding_id(String.valueOf(contract.getOnboarding().getId()))
                .build();

        return dto;
    }

    public String generateHash(String content){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }
            return hexString.toString();

        }catch(NoSuchAlgorithmException e){
            throw new  RuntimeException(e);
        }
    }

}
