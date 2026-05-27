package org.northpay_contractor_onboarding.dto.resendMailer;

import java.util.List;

import lombok.Builder;

@Builder
public record ResendRequestDTO (
  String from,
  List<String> to,
  String subject,
  String html
) {
  public static ResendRequestDTO invitationEmail(String to, String invitationToken, String expirationDateTime) {
    return ResendRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Invitation to complete contractor onboarding")
      .html("""
        <p>Please to complete the contractor onboarding process, click the link below:</p>
        <a href="http://localhost:3000/invite/%s">Complete your onboarding</a>
        <p>This link will expire on %s.</p>
      """.formatted(invitationToken, expirationDateTime))
    .build();
  }

  public static ResendRequestDTO contractorPasswordSetEmail(String to) {
    return ResendRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Password Set")
      .html("""
        <p>Your password has been set successfully. If you did not initiate this change, please contact support.</p>
      """)
    .build();
  }

  public static ResendRequestDTO onboardingNeedCorrectionsEmail(String to) {
    return ResendRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Onboarding Needs Corrections")
      .html("""
        <p>Your onboarding submission needs corrections. Please review the feedback and resubmit.</p>
      """)
    .build();
  }

  public static ResendRequestDTO onboardingApprovedEmail(String to) {
    return ResendRequestDTO.builder()
      .from("onboarding@resend.dev")
      .to(List.of(to))
      .subject("Onboarding Approved")
      .html("""
        <p>Congratulations! Your onboarding has been approved.</p>
      """)
    .build();
  }
}
