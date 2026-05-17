package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.ContractRequestDTO;
import org.northpay_contractor_onboarding.dto.ContractResponseDTO;
import org.northpay_contractor_onboarding.dto.SignContractRequestDTO;
import org.northpay_contractor_onboarding.dto.UpdateContractStatusDTO;

import java.util.List;
import java.util.UUID;

public interface IContractService {

    List<ContractResponseDTO> getAllContracts();
    ContractResponseDTO getContractById(String id);
    ContractResponseDTO getContractByOnboardingId(UUID onboardingId);
    ContractResponseDTO createContract(ContractRequestDTO contract,
                                       UUID onboardingId);
    String signContract(UUID contractId, SignContractRequestDTO requestDTO);
    String changeContractStatus(UUID contractId,
                                UpdateContractStatusDTO updateContractStatusDTO);
    void cancelContract(UUID contractId);

}
