package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.resendMailer.EmailRequestDTO;
import org.northpay_contractor_onboarding.exception.CustomMailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailSenderService {
  private final JavaMailSender mailSender;

  @Value("${MAIL_USERNAME}")
  private String mailUsername;

  @Value("${MAIL_PASSWORD}")
  private String mailPassword;

  public void sendEmail(EmailRequestDTO request) {
    try {
      MimeMessage message = mailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(request.to().get(0)); // TODO: en caso de que haya que enviar a varios destinatarios, revisar esto
      helper.setSubject(request.subject());
      helper.setText(request.html(), true);
      helper.setFrom(mailUsername);

      mailSender.send(message);
    } catch (Exception e) {
      System.err.println("Failed to send email to " + request.to() + ". Error: " + e.getMessage());
      throw new CustomMailException("Failed to send email to " + request.to() + ". Error: " + e.getMessage());
    }
  }

  public void sendInvitationEmail(String to, String invitationToken, String expirationDateTime) {
    EmailRequestDTO request = EmailRequestDTO.invitationEmail(to, invitationToken, expirationDateTime);
    sendEmail(request);
  }

  public void sendContractorPasswordCreatedEmail(String to) {
    EmailRequestDTO request = EmailRequestDTO.contractorPasswordSetEmail(to);
    sendEmail(request);
  }

  public void sendOnboardingApprovedEmail(String to) {
    EmailRequestDTO request = EmailRequestDTO.onboardingApprovedEmail(to);
    sendEmail(request);
  }

  public void sendOnboardingNeedCorrectionsEmail(String to, String tokenUrl, String reason) {
    EmailRequestDTO request = EmailRequestDTO.onboardingNeedCorrectionsEmail(to, tokenUrl, reason);
    sendEmail(request);
  }

  public void sendOnboardingPendingVerificationEmail(String to) {
    EmailRequestDTO request = EmailRequestDTO.onboardingPendingVerificationEmail(to);
    sendEmail(request);
  }
}