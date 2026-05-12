package org.northpay_contractor_onboarding.service.implementations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.dto.InvitationTokenDTO;
import org.northpay_contractor_onboarding.exception.NotFoundException;
import org.northpay_contractor_onboarding.model.InvitationTokens;
import org.northpay_contractor_onboarding.repository.InvitationTokenRepository;
import org.northpay_contractor_onboarding.service.interfaces.IInvitationTokenService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class InvitationTokenService implements IInvitationTokenService {
  private final InvitationTokenRepository invitationTokenRepository;
  
  @Override
  public List<InvitationTokenDTO> getAll() {
    return invitationTokenRepository.findAll().stream().map(
      entity -> new InvitationTokenDTO(entity.getId().toString(), entity.getToken(), entity.getUsed(), entity.getExpiresAt().toString(), entity.getOnboardingId().toString()) 
    ).toList();
  }

  @Override
  public InvitationTokens create(@NotNull UUID onboardingId, @NotNull String contractorEmail) {
    // encontrar primero el onboarding según esa id y validarla
    InvitationTokens newInvToken = invitationTokenRepository.save(InvitationTokens.builder()
      .token(UUID.randomUUID().toString())
      .used(false)
      .expiresAt(LocalDateTime.now().plusHours(24))
      .onboardingId(onboardingId)
    .build());

    // si el paso anterior es exitoso acá se llamaría al servicio que manda el mail al contratista

    // a la par se guarda registro de esta creación

    return newInvToken;
  }

  @Override
  public boolean checkInvitationTokenIsExpired(String token) {
    return invitationTokenRepository.findByToken(token).orElseThrow(
      () -> new NotFoundException("")
    ).getExpiresAt().isBefore(LocalDateTime.now());
  }

  @Override
  public void accessToToken(String token) {
    InvitationTokens tokenEntity = invitationTokenRepository.findByToken(token).orElseThrow(
      () -> new NotFoundException("")
    );

    // llamar al servicio del mail para enviar la contraseña de un solo uso al contratista (OTP - One Time Password)
    // si ese servicio falla debe devolver algún status 500

    // también debería generar el jwt?
  }

  @Override
  public void validateOtpForToken(String token, String code) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'validateOtpForToken'");
  }
}
