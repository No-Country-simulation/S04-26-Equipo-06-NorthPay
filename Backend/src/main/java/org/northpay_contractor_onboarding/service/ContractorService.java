package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;

import org.northpay_contractor_onboarding.dto.onboardingDtos.OnboardingDTO;
import org.northpay_contractor_onboarding.model.ContactInformation;
import org.northpay_contractor_onboarding.model.Contractor;
import org.northpay_contractor_onboarding.repository.ContractorRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContractorService implements IContractorService {

    private final ContractorRepository contractorRepository;

    @Override
    public Contractor saveContractor(OnboardingDTO.RequestOnboarding requestOnboarding , String emialLogeado) {
              
        
        Contractor contractor = Contractor.builder()
                .firstName(requestOnboarding.getName())
                .lastName(requestOnboarding.getLastName())
                .createdAt(LocalDateTime.now())
                .contactInformation(ContactInformation.builder()
                        .phoneNumber(requestOnboarding.getPhoneNumber())
                        .country(requestOnboarding.getCountry())
                        .address(requestOnboarding.getAddress())
                        .build())
                .build();
                if(emialLogeado == null){
             contractor.getContactInformation().setEmail("prueba@gmail.com");
              }
              else{
                contractor.getContactInformation().setEmail(emialLogeado);
              }


        return contractorRepository.save(contractor);
    }

}
