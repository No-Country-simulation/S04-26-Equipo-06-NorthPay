package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.util.ArrayList;
import java.util.List;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnboardingChangeRequested {
    
    private OnboardingStatus onboardingStatus;
    @NotBlank(message = "you must specify a reason")
    @Size(max = 255)
    private String reason;

    private List<Integer> steps = new ArrayList<>();
}
