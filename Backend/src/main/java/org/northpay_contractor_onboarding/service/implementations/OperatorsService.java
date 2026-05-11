package org.northpay_contractor_onboarding.service.implementations;

import java.util.List;

import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorDTO;
import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorRegistrationDTO;
import org.northpay_contractor_onboarding.model.Operators;
import org.northpay_contractor_onboarding.repository.OperatorRepository;
import org.northpay_contractor_onboarding.service.interfaces.IOperatorsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class OperatorsService implements IOperatorsService {
  private final OperatorRepository operatorRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public List<OperatorDTO> getAll() {
    return operatorRepository.findAll().stream().map(
      (entity) -> new OperatorDTO(entity.getId().toString(), entity.getEmail(), entity.getName())
    ).toList();
  }

  @Override
  public OperatorDTO getByEmail(@NotNull String email) {
    Operators found = operatorRepository.findByEmail(email).orElseThrow(
      () -> new RuntimeException()
    );

    return new OperatorDTO(found.getId().toString(), found.getEmail(), found.getName());
  }

  @Override
  public OperatorDTO create(OperatorRegistrationDTO registrationDTO) {
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
}
