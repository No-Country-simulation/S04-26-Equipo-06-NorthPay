package org.northpay_contractor_onboarding.service;

import java.time.LocalDateTime;

import org.northpay_contractor_onboarding.dto.OnboardingDTO;

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
    public Contractor saveContractor(OnboardingDTO.RequestOnboarding requestOnboarding) {

        Contractor contractor = Contractor.builder()
                .firstName(requestOnboarding.getName())
                .lastName(requestOnboarding.getLastName())
                .createdAt(LocalDateTime.now())
                .contactInformation(ContactInformation.builder()
                        .email(requestOnboarding.getEmail())
                        .phoneNumber(requestOnboarding.getPhoneNumber())
                        .country(requestOnboarding.getCountry())
                        .address(requestOnboarding.getAddress())
                        .build())
                .build();

        return contractorRepository.save(contractor);
    }

}
