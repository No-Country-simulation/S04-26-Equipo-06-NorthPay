package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.PaymentMethodRequestDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodVerificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface IPaymentMethodService {

    List<PaymentMethodResponseDTO> getAllPaymentMethods();
    PaymentMethodResponseDTO getPaymentMethodById(UUID id);
    PaymentMethodResponseDTO getPaymentMethodByOnboardingId(UUID onboardingId);
    PaymentMethodResponseDTO createPaymentMethod(PaymentMethodRequestDTO paymentMethod,
                                                         UUID onboardingId);
    PaymentMethodResponseDTO updatePaymentMethod(PaymentMethodRequestDTO paymentMethod
    , UUID paymentMethodId);
    String verifyPaymentMethod(UUID paymentMethodId,
                               PaymentMethodVerificationDTO verificationDTO);
    String rejectPaymentMethod(UUID paymentMethodId,
                                PaymentMethodVerificationDTO verificationDTO);
    void deletePaymentMethod(String id);

}
