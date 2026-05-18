package org.northpay_contractor_onboarding.controller;

import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.northpay_contractor_onboarding.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-method")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    //GET ALL PAYMENT METHODS
    @GetMapping
    public ResponseEntity<List<PaymentMethodResponseDTO>> getAllPaymentMethods(){
        return null;
    }

    //GET PAYMENT METHOD BY ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<PaymentMethodResponseDTO> getPaymentMethodById(@PathVariable String id){
        return null;
    }

    //CREATE PAYMENT METHOD
    @PostMapping("/create")
    public ResponseEntity<PaymentMethodResponseDTO> createPaymentMethod(
            @RequestBody PaymentMethodResponseDTO paymentMethod){
        return null;
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
