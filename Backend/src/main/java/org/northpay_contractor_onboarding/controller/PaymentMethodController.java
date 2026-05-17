package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.PaymentMethodRequestDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.northpay_contractor_onboarding.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-method")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    //GET ALL PAYMENT METHODS
    @GetMapping
    public ResponseEntity<List<PaymentMethodResponseDTO>> getAllPaymentMethods(){
        return ResponseEntity.status(200).body(paymentMethodService.getAllPaymentMethods());
    }

    //GET PAYMENT METHOD BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethodById(@PathVariable UUID id){

        return ResponseEntity.status(200).body(paymentMethodService.getPaymentMethodById(id));
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
    @PatchMapping("/update/{id}")
    public ResponseEntity<PaymentMethodResponseDTO> updatePaymentMethod(
            @PathVariable String id,
            @RequestBody PaymentMethodResponseDTO paymentMethod
    ){
        return null;
    }

    //DELETE PAYMENT METHOD
    @DeleteMapping("/delete/{id}")
    public void deletePaymentMethod(@PathVariable String id){}

}
