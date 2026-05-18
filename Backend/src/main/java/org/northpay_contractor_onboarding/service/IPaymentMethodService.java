package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.PaymentMethodRequestDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.northpay_contractor_onboarding.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPaymentMethodService {

    List<PaymentMethodResponseDTO> getAllPaymentMethods();
    PaymentMethodResponseDTO getPaymentMethodById(String id);
    PaymentMethodResponseDTO createPaymentMethod(PaymentMethodRequestDTO paymentMethod);
    PaymentMethodResponseDTO updatePaymentMethod(String id, PaymentMethodRequestDTO paymentMethod);
    void deletePaymentMethod(String id);

}
