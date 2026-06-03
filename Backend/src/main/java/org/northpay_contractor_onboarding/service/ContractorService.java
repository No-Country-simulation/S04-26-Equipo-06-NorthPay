package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;
import java.util.UUID;

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
    public Contractor saveContractor(UUID contractorId, OnboardingDTO.RequestOnboarding requestOnboarding , String emailLogeado) {
              
        Contractor contractor = Contractor.builder()
                .id(contractorId) // si esto es nulo se crearía una nueva entidad, sino actualizaría la ya existente
                .firstName(requestOnboarding.getName())
                .lastName(requestOnboarding.getLastName())
                .textId(requestOnboarding.getDniNumber())
                .verificationNotes(requestOnboarding.getVerificationNotes())
                .createdAt(LocalDateTime.now())
                .contactInformation(ContactInformation.builder()
                        .email(requestOnboarding.getEmail())
                        .phoneNumber(requestOnboarding.getPhoneNumber())
                        .country(requestOnboarding.getCountry())
                        .address(requestOnboarding.getAddress())
                        .build())
                .build();
                if(emailLogeado == null){
             contractor.getContactInformation().setEmail("prueba@gmail.com");
              }
              else{
                contractor.getContactInformation().setEmail(emailLogeado);
              }


        return contractorRepository.save(contractor);
    }

}
