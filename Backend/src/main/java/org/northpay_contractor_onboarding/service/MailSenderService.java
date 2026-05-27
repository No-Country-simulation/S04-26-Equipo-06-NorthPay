package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.resendMailer.ResendRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSenderService {

  private final WebClient webClient;

  @Value("${RESEND_API_KEY}")
  private String apiKey;

  public String sendEmail(ResendRequestDTO request) {
    String response = webClient.post()
      .uri("https://api.resend.com/emails")
      .header("Authorization", "Bearer " + apiKey)
      .bodyValue(request)
      .retrieve()
      .bodyToMono(String.class)
    .block();

    return response;
  }

  public String sendInvitationEmail(String to, String invitationToken, String expirationDateTime) {
    ResendRequestDTO request = ResendRequestDTO.invitationEmail(to, invitationToken, expirationDateTime);
    return sendEmail(request);
  }

  public String sendContractorPasswordCreatedEmail(String to) {
    ResendRequestDTO request = ResendRequestDTO.contractorPasswordSetEmail(to);
    return sendEmail(request);
  }
}