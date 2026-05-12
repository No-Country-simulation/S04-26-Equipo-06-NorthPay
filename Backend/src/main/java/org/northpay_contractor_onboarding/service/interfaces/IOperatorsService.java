package org.northpay_contractor_onboarding.service.interfaces;

import java.util.List;

import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorDTO;
import org.northpay_contractor_onboarding.dto.operatorDtos.OperatorRegistrationDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public interface IOperatorsService {
  List<OperatorDTO> getAll();
  OperatorDTO getByEmail(@Email String email);
  OperatorDTO create(@Valid OperatorRegistrationDTO registrationDTO);
}
