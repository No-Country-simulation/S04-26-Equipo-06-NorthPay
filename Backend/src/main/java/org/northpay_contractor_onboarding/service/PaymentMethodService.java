package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.PaymenMethodVerificationDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodRequestDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.model.PaymentMethod;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.northpay_contractor_onboarding.repository.PaymentMethodRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentMethodService implements IPaymentMethodService{

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private OnboardingRepository onboardingRepository;

    //GET ALL PAYMENT METHODS
    @Override
    public List<PaymentMethodResponseDTO> getAllPaymentMethods(){

        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();

        return paymentMethods.stream().map(this::mapToDTO).toList();
    }

    //GET PAYMENT METHOD BY ID
    @Override
    public PaymentMethodResponseDTO getPaymentMethodById(UUID id){

        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("Payment Method not found")
        );

        return mapToDTO(paymentMethod);
    }

    //GET PAYMENT METHOD BY ONBOARDING
    @Override
    public PaymentMethodResponseDTO getPaymentMethodByOnboardingId(UUID onboardingId){
        PaymentMethod paymentMethod = paymentMethodRepository.findByOnboardingId(onboardingId);
        if(paymentMethod == null) {
            throw new IllegalArgumentException("Payment Method not found");
        }

        return mapToDTO(paymentMethod);
    }

    //CREATE PAYMENT METHOD
    @Override
    public PaymentMethodResponseDTO createPaymentMethod(PaymentMethodRequestDTO paymentMethod, UUID onboardingId){

        Onboarding onboarding = onboardingRepository.findById(onboardingId).orElseThrow(
                ()-> new IllegalArgumentException("Onboarding not found")
        );

        PaymentMethod paymentMethodEntity = PaymentMethod.builder()
                .paymentMethodType(paymentMethod.getPaymentMethodType())
                .platform(paymentMethod.getPlatform())
                .walletEmail(paymentMethod.getWalletEmail())
                .network(paymentMethod.getNetwork())
                .walletAddress(paymentMethod.getWalletAddress())
                .onboarding(onboarding)
                .build();

        PaymentMethod saved = paymentMethodRepository.save(paymentMethodEntity);

        return mapToDTO(saved);
    }

    //UPDATE PAYMENT METHOD
    @Override
    public PaymentMethodResponseDTO updatePaymentMethod(PaymentMethodRequestDTO paymentMethod, UUID paymentMethodId){

        PaymentMethod paymentMethodEntity = paymentMethodRepository.findById(paymentMethodId).orElseThrow(
                ()-> new IllegalArgumentException("Payment Method not found")
        );

        paymentMethodEntity.setPaymentMethodType(paymentMethod.getPaymentMethodType());
        paymentMethodEntity.setPlatform(paymentMethod.getPlatform());
        paymentMethodEntity.setWalletEmail(paymentMethod.getWalletEmail());
        paymentMethodEntity.setNetwork(paymentMethod.getNetwork());
        paymentMethodEntity.setWalletAddress(paymentMethod.getWalletAddress());
        PaymentMethod paymentMethodUpdated = paymentMethodRepository.save(paymentMethodEntity);

        return mapToDTO(paymentMethodUpdated);
    }

    //VERIFY PAYMENT METHOD
    @Override
    public String verifyPaymentMethod(UUID paymentMethodId,
    PaymenMethodVerificationDTO dto){

        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(
                ()-> new IllegalArgumentException("Payment Method not found")
        );

        paymentMethod.setIsPaymentVerified(dto.getIsVerified());
        paymentMethod.setVerificationNotes(dto.getVerificationNotes());

        paymentMethodRepository.save(paymentMethod);

        return "Payment Method has been verified successfully";
    }

    //REJECT PAYMENT METHOD
    @Override
    public String rejectPaymentMethod(UUID paymentMethodId,
                                      PaymenMethodVerificationDTO dto){
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(
                ()-> new IllegalArgumentException("Payment Method not found")
        );

        paymentMethod.setIsPaymentVerified(dto.getIsVerified());
        paymentMethod.setVerificationNotes(dto.getVerificationNotes());

        return "Payment Method has been rejected successfully";
    }

    //DELETE PAYMENT METHOD
    @Override
    public void deletePaymentMethod(String id){

    }

    //MAP TO DTO
    public PaymentMethodResponseDTO mapToDTO(PaymentMethod paymentMethod){

        PaymentMethodResponseDTO dto = new PaymentMethodResponseDTO();
        dto.setPayment_method_id(paymentMethod.getPayment_method_id().toString());
        dto.setPayment_method_type(paymentMethod.getPaymentMethodType().toString());
        dto.setPlatform(paymentMethod.getPlatform());
        dto.setWallet_email(paymentMethod.getWalletEmail());
        dto.setNetwork(paymentMethod.getNetwork());
        dto.setWallet_address(paymentMethod.getWalletAddress());
        dto.setIs_payment_verified(paymentMethod.getIsPaymentVerified());
        dto.setVerification_notes(paymentMethod.getVerificationNotes());
        dto.setOnboarding_id(paymentMethod.getOnboarding().getId().toString());

        return dto;
    }

}
