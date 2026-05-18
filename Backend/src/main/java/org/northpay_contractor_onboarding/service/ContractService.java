package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.ContractRequestDTO;
import org.northpay_contractor_onboarding.dto.ContractResponseDTO;
import org.northpay_contractor_onboarding.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService implements IContractService{

    @Autowired
    private ContractRepository contractRepository;

    //GET ALL CONTRACTS
    @Override
    public List<ContractResponseDTO> getAllContracts(){
        return null;
    }

    //GET CONTRACT BY ID
    @Override
    public ContractResponseDTO getContractById(String id){
        return null;
    }

    //CREATE CONTRACT
    @Override
    public ContractResponseDTO createContract(ContractRequestDTO contract){
        return null;
    }

    //UPDATE CONTRACT
    @Override
    public ContractResponseDTO updateContract(String id, ContractRequestDTO contract){
        return null;
    }

    //DELETE CONTRACT
    @Override
    public void deleteContract(String id){}


}
