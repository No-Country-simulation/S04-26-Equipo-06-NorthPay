package org.northpay_contractor_onboarding.service;

import org.northpay_contractor_onboarding.dto.PaymentMethodRequestDTO;
import org.northpay_contractor_onboarding.dto.PaymentMethodResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.northpay_contractor_onboarding.repository.PaymentMethodRepository;

import java.util.List;

@Service
public class PaymentMethodService implements IPaymentMethodService{

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    //GET ALL PAYMENT METHODS
    @Override
    public List<PaymentMethodResponseDTO> getAllPaymentMethods(){
        return null;
    }

    //GET PAYMENT METHOD BY ID
    @Override
    public PaymentMethodResponseDTO getPaymentMethodById(String id){
        return null;
    }

    //CREATE PAYMENT METHOD
    @Override
    public PaymentMethodResponseDTO createPaymentMethod(PaymentMethodRequestDTO paymentMethod){
        return null;
    }

    //UPDATE PAYMENT METHOD
    @Override
    public PaymentMethodResponseDTO updatePaymentMethod(String id, PaymentMethodRequestDTO paymentMethod){
        return null;
    }

    //DELETE PAYMENT METHOD
    @Override
    public void deletePaymentMethod(String id){

    }

}
