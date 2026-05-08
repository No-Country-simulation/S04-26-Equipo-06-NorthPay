package org.northpay_contractor_onboarding.model;
import jakarta.persistence.*;
import lombok.*;
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
    private String iban;
    private String bank_name;
    private String masked_data;
    private LocalDateTime created_at;

    private UUID onboarding_id;

    //@OneToOne
    //@JoinColumn(name="onboarding_id", nullable = false)
    //private Onboarding onboarding;



}
