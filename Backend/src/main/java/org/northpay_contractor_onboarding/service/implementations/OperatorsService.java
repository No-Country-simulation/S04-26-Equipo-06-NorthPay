package org.northpay_contractor_onboarding.service.implementations;

import java.time.LocalDateTime;
import java.util.List;

import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorDTO;
import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorRegistrationDTO;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.model.ContactInformation;
import org.northpay_contractor_onboarding.model.Contractor;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.model.Operators;
import org.northpay_contractor_onboarding.repository.ContractorRepository;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.northpay_contractor_onboarding.repository.OperatorRepository;
import org.northpay_contractor_onboarding.service.interfaces.IOperatorsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class OperatorsService implements IOperatorsService {
  private final OperatorRepository operatorRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  
  private final OnboardingRepository onboardingRepository;
  private final ContractorRepository contractorRepository;

  @Override @Transactional(readOnly = true)
  public List<OperatorDTO> getAll() {
    return operatorRepository.findAll().stream().map(
      (entity) -> new OperatorDTO(entity.getId().toString(), entity.getEmail(), entity.getName())
    ).toList();
  }

  @Override @Transactional(readOnly = true)
  public OperatorDTO getByEmail(@Email String email) {
    Operators found = operatorRepository.findByEmail(email).orElseThrow(
      () -> new RuntimeException()
    );

    return new OperatorDTO(found.getId().toString(), found.getEmail(), found.getName());
  }

  @Override @Transactional
  public OperatorDTO create(@Valid OperatorRegistrationDTO registrationDTO) {
    if (!registrationDTO.password().equals(registrationDTO.passwordConfirmation())) {
      throw new RuntimeException("La contraseña definida y su confirmación no coinciden");
    }

    Operators registered = operatorRepository.save(
      Operators.builder()
        .email(registrationDTO.email())
        .name(registrationDTO.username())
        .password(passwordEncoder.encode(registrationDTO.password()))
      .build()
    );

    return new OperatorDTO(registered.getId().toString(), registered.getEmail(), registered.getName());
  }

  // method for dev env
  public Onboarding createOnboardingTEST() {
    Contractor contractor = contractorRepository.save(Contractor.builder()
      .firstName("fN")
      .lastName("LN")
      .contactInformation(ContactInformation.builder().email("contractor@email.com").build())
      .createdAt(LocalDateTime.now())
    .build());

    return onboardingRepository.save(Onboarding.builder()
      .contractor(contractor)
      .status(OnboardingStatus.INVITED)
      .currentStep(0)
    .build());
  }
}
