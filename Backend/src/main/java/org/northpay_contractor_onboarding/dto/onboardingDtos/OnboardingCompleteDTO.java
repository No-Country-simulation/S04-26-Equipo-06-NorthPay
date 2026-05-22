package org.northpay_contractor_onboarding.dto.onboardingDtos;

import java.util.List;
import java.util.UUID;

import org.northpay_contractor_onboarding.enums.OnboardingStatus;
import org.northpay_contractor_onboarding.enums.PaymentMethodTypes;
import org.northpay_contractor_onboarding.model.ContactInformation;

import org.northpay_contractor_onboarding.model.Onboarding;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnboardingCompleteDTO {

        private UUID id;
        private Integer currentStep;
        private OnboardingStatus status;
        private PersonalInfoSection personalInformation;
        private DocumentationSection documentation;
        private PaymentAndContractSection paymentInformation;
       

        public OnboardingCompleteDTO(Onboarding onboarding) {
                if (onboarding == null)
                        return;

                var payment = onboarding.getPaymentMethod();
                var contract = onboarding.getContract();
                var documents = onboarding.getDocuments();
                var personalData = onboarding.getContractor();

                this.id = onboarding.getId();
                this.currentStep = onboarding.getCurrentStep();
                this.status = onboarding.getStatus();

                String completeName = "Pending";
                String email = "Pending";
                String address = "N/A";
                String phone = "N/A";
                String country = "N/A";

                if (personalData != null) {

                        completeName = personalData.getFirstName() + " " + personalData.getLastName();
                        email = personalData.getEmail();

                        if (personalData.getContactInformation() != null) {
                                address = personalData.getContactInformation().getAddress();
                                phone = personalData.getContactInformation().getPhoneNumber();
                                country = personalData.getContactInformation().getCountry();
                        }
                }
                this.personalInformation = new PersonalInfoSection(completeName, email, phone, country, address);
                this.documentation = new DocumentationSection("NOT_SPECIFIED", "N/A", List.of());
                if (documents != null && !documents.isEmpty()) {

                        List<FileDTO> fileMap = onboarding.getDocuments().stream()
                                        .map(docu -> new FileDTO(
                                                        docu.getFileType(),

                                                        docu.getFileUrl()))
                                        .toList();
                        var firstDoc = onboarding.getDocuments().get(0);

                        String typeDoc = (firstDoc != null && firstDoc.getFileType() != null)
                                        ? firstDoc.getFileType()
                                        : "DNI";
                        String numDoc = "42345678";;
                        // TODO me falta todavia conseguir el dni de la validacion de indentidad
                        // if (onboarding.getContractor() != null
                        // && onboarding.getContractor().getDocumentNumber() != null) {
                        // numDoc = onboarding.getContractor().getDocumentNumber();
                        // }
                       

                        this.documentation = new DocumentationSection(typeDoc, numDoc, fileMap);

                }

                String platform = "";
                String walletEmail = "";
                String network = "";
                String walletAddress = "";
                String accountDetail = "N/A";

                if (payment != null && payment.getPaymentMethodType() != null) {
                        if (payment.getPaymentMethodType() == PaymentMethodTypes.DIGITAL_PLATFORM) {
                                platform = payment.getPlatform();
                                walletEmail = payment.getWalletEmail();
                                accountDetail = payment.getWalletEmail();
                        } else if (payment.getPaymentMethodType() == PaymentMethodTypes.CRYPTO_CURRENCY) {
                                network = payment.getNetwork();
                                walletAddress = payment.getWalletAddress();
                                accountDetail = payment.getWalletAddress();
                        }
                }

                Boolean signed = false;

                if (contract != null) {

                        signed = Boolean.TRUE.equals(contract.getSigned());
                }

                this.paymentInformation = new PaymentAndContractSection(
                                platform,
                                walletEmail,
                                network,
                                walletAddress,
                                accountDetail,
                                signed);

        }

}
