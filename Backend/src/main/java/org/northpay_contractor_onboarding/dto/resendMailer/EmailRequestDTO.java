package org.northpay_contractor_onboarding.dto.resendMailer;

import java.util.List;

import lombok.Builder;

@Builder
public record EmailRequestDTO (
  String from,
  List<String> to,
  String subject,
  String html
) {
  public static EmailRequestDTO invitationEmail(String to, String invitationToken, String expirationDateTime) {
    return EmailRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Invitation to complete contractor onboarding")
      .html("""
        <h1>You're invited to complete your contractor onboarding!</h1>
        <p>Please to complete the contractor onboarding process, click the link below:</p>
        <a href="http://localhost:3000/invite/%s">Complete your onboarding</a>
        <p>This link will expire on %s.</p>
      """.formatted(invitationToken, expirationDateTime))
    .build();
  }

  public static EmailRequestDTO contractorPasswordSetEmail(String to) {
    return EmailRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Password Set")
      .html("""
        <h1>Password Set</h1>
        <p>Your password has been set successfully. <strong>If you did not initiate this change, please contact support.</strong></p>
      """)
    .build();
  }

  public static EmailRequestDTO onboardingNeedCorrectionsEmail(String to) {
    return EmailRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Onboarding Needs Corrections")
      .html("""
        <h1>Onboarding Needs Corrections</h1>
        <p>Your onboarding submission needs corrections. Please review the feedback and resubmit.</p>
      """)
    .build();
  }

  public static EmailRequestDTO onboardingApprovedEmail(String to) {
    return EmailRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Onboarding Approved")
      .html("""
        <h1>Onboarding Approved</h1>
        <p>Congratulations! Your onboarding has been approved.</p>
      """)
    .build();
  }
}
