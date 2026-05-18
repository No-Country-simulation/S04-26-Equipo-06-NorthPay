package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.ContractRequestDTO;
import org.northpay_contractor_onboarding.dto.ContractResponseDTO;

import java.util.List;

public interface IContractService {

    List<ContractResponseDTO> getAllContracts();
    ContractResponseDTO getContractById(String id);
    ContractResponseDTO createContract(ContractRequestDTO contract);
    ContractResponseDTO updateContract(String id, ContractRequestDTO contract);
    void deleteContract(String id);

}
