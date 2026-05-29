package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.PaymentMethodRequestDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodVerificationDTO;
import org.northpay_contractor_onboarding.repository.PaymentMethodRepository;
import org.northpay_contractor_onboarding.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment-method")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    //GET ALL PAYMENT METHODS
    @GetMapping
    public ResponseEntity<List<PaymentMethodResponseDTO>> getAllPaymentMethods(){
        return ResponseEntity.status(200).body(paymentMethodService.getAllPaymentMethods());
    }

    //GET PAYMENT METHOD BY ID
    @GetMapping("/getById/{paymentMethodId}")
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethodById(@PathVariable UUID paymentMethodId){

        return ResponseEntity.status(200).body(paymentMethodService.getPaymentMethodById(paymentMethodId));
    }

    //GET PAYMENT METHOD BY ONBOARDING ID
    @GetMapping("/getByOnboardingId/{onboardingId}")
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethodByOnboardingId(@PathVariable UUID onboardingId){
        return ResponseEntity.status(200).body(paymentMethodService.getPaymentMethodByOnboardingId(onboardingId));
    }

    //CREATE PAYMENT METHOD
    @PostMapping("/create/{onboardingId}")
    public ResponseEntity<PaymentMethodResponseDTO> createPaymentMethod(
            @RequestBody PaymentMethodRequestDTO paymentMethod,
            @PathVariable UUID onboardingId){
        return ResponseEntity.
                status(HttpStatus.OK).
                body(paymentMethodService.createPaymentMethod(paymentMethod, onboardingId));
    }

    //UPDATE PAYMENT METHOD
    @PatchMapping("/update/{payment_method_id}")
    public ResponseEntity<PaymentMethodResponseDTO> updatePaymentMethod(
            @PathVariable UUID payment_method_id,
            @RequestBody PaymentMethodRequestDTO dto
    ){
        return ResponseEntity.status(200).body(paymentMethodService
                .updatePaymentMethod(dto, payment_method_id));
    }

    //VERIFY PAYMENT METHOD
    @PatchMapping("/verify/{payment_method_id}")
    public String verifyPaymentMethod(@PathVariable UUID payment_method_id,
                                      @RequestBody PaymentMethodVerificationDTO dto){
        return paymentMethodService.verifyPaymentMethod(payment_method_id, dto);
    }

    //REJECT PAYMENT METHOD
    @PatchMapping("/reject/{payment_method_id}")
    public String rejectPaymentMethod(@PathVariable UUID payment_method_id,
                                      @RequestBody PaymentMethodVerificationDTO dto){
        return paymentMethodService.rejectPaymentMethod(payment_method_id, dto);
    }

    //DELETE PAYMENT METHOD
    @DeleteMapping("/delete/{id}")
    public void deletePaymentMethod(@PathVariable String id){}

}
