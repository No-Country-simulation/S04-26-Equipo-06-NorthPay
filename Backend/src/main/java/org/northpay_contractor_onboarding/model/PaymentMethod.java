package org.northpay_contractor_onboarding.model;
import jakarta.persistence.*;
import lombok.*;
import org.northpay_contractor_onboarding.enums.PaymentMethodTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PaymentMethod {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private UUID payment_method_id;

    @Enumerated(EnumType.STRING)
    private PaymentMethodTypes paymentMethodType;

    //DIGITAL PLATFORMS
    private String platform;
    private String walletEmail;

    //CRYPTO CURRENCIES
    private String network;
    private String walletAddress;

    //DATE DATA
    private LocalDateTime created_at;

    //VERIFICATION
    private Boolean isPaymentVerified;
    private String verificationNotes;

    @OneToOne
    @JoinColumn(name="onboarding_id",
            referencedColumnName = "id"
            , nullable = false)
    private Onboarding onboarding;
}
