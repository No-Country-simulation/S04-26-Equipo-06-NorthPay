package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.PaymentMethodRequestDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodVerificationDTO;
import org.northpay_contractor_onboarding.model.Onboarding;
import org.northpay_contractor_onboarding.model.PaymentMethod;
import org.northpay_contractor_onboarding.repository.OnboardingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.northpay_contractor_onboarding.repository.PaymentMethodRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.northpay_contractor_onboarding.service.StateMachineService;
import org.northpay_contractor_onboarding.enums.OnboardingStatus;

@Service
public class PaymentMethodService implements IPaymentMethodService{

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private OnboardingRepository onboardingRepository;

    @Autowired
    private StateMachineService stateMachineService;

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
    @Transactional
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
                .created_at(LocalDateTime.now())
                .isPaymentVerified(false)
                .onboarding(onboarding)
                .build();

        PaymentMethod saved = paymentMethodRepository.save(paymentMethodEntity);

        if(onboarding.getCurrentStep() == null || onboarding.getCurrentStep() < 5) {
            stateMachineService.transitionTo(onboarding, OnboardingStatus.PAYMENT_CONFIGURED, "USER");

            onboarding.setCurrentStep(5);
            onboardingRepository.save(onboarding);
        }

        return mapToDTO(saved);
    }

    //UPDATE PAYMENT METHOD
    @Override
    public PaymentMethodResponseDTO updatePaymentMethod(PaymentMethodRequestDTO paymentMethod, UUID paymentMethodId){

        PaymentMethod paymentMethodEntity = paymentMethodRepository.findById(paymentMethodId).orElseThrow(
                ()-> new IllegalArgumentException("Payment Method not found")
        );

        if(Boolean.TRUE.equals(paymentMethodEntity.getIsPaymentVerified())){
            throw new IllegalStateException("Payment Method is already verified");
        }

        if(paymentMethod.getPlatform() != null && !paymentMethod.getPlatform().isBlank()){
            paymentMethodEntity.setPlatform(paymentMethod.getPlatform());
        }

        if(paymentMethod.getWalletEmail() != null && !paymentMethod.getWalletEmail().isBlank()){
            paymentMethodEntity.setWalletEmail(
                    paymentMethod.getWalletEmail());
        }

        if(paymentMethod.getNetwork() != null && !paymentMethod.getNetwork().isBlank()){
            paymentMethodEntity.setNetwork(
                    paymentMethod.getNetwork());
        }

        if(paymentMethod.getWalletAddress() != null && !paymentMethod.getWalletAddress().isBlank()){
            paymentMethodEntity.setWalletAddress(
                    paymentMethod.getWalletAddress());
        }

        PaymentMethod paymentMethodUpdated = paymentMethodRepository.save(paymentMethodEntity);

        return mapToDTO(paymentMethodUpdated);
    }

    //VERIFY PAYMENT METHOD
    @Override
    public String verifyPaymentMethod(UUID paymentMethodId,
    PaymentMethodVerificationDTO dto){

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
                                      PaymentMethodVerificationDTO dto){
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
        dto.setCreated_at(paymentMethod.getCreated_at());
        dto.setOnboarding_id(paymentMethod.getOnboarding().getId().toString());

        return dto;
    }

}
