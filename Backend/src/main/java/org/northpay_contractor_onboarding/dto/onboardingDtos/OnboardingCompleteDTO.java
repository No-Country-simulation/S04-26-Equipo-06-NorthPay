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
        private IdentityVerificationSection identityVerification;

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
                        String firstName = personalData.getFirstName();
                        String lastName = personalData.getLastName();
                        if (firstName == null && lastName == null) {
                                completeName = "Guest";
                        } else {
                                completeName = ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();
                        }

                        String rawEmail = personalData.getEmail();
                        email = (rawEmail != null) ? rawEmail.replaceAll("(?<=.{3}).(?=.*@)", "*") : "Pending";

                        String rawPhone = personalData.getContactInformation().getPhoneNumber();
                        if (rawPhone != null && rawPhone.length() > 4) {
                                phone = "xxxx-xxxx-" + rawPhone.substring(rawPhone.length() - 4);
                        } else {
                                phone = (rawPhone != null) ? rawPhone : "N/A";
                        }
                        
                        String rawCountry = personalData.getContactInformation().getCountry();
                        country = (rawCountry != null) ? rawCountry : "N/A";

                        String rawAddress = personalData.getContactInformation().getAddress();
                        // Censuramos la dirección parcialmente por privacidad
                        if (rawAddress != null && rawAddress.length() > 5) {
                                address = rawAddress.substring(0, 3) + "***";
                        } else {
                                address = (rawAddress != null) ? rawAddress : "N/A";
                        }
                }

                this.personalInformation = new PersonalInfoSection(completeName, email, phone, country, address);

                this.documentation = new DocumentationSection("NOT_SPECIFIED", "N/A", List.of());
                if (documents != null && !documents.isEmpty()) {
                        List<FileDTO> fileMap = onboarding.getDocuments().stream()
                                        .map(docu -> new FileDTO(docu.getFileType(), docu.getFileUrl()))
                                        .toList();
                        var firstDoc = onboarding.getDocuments().get(0);

                        String typeDoc = (firstDoc != null && firstDoc.getFileType() != null) ? firstDoc.getFileType()
                                        : "DNI";
                        String numDoc = (personalData != null && personalData.getTextId() != null) ? personalData.getTextId() : "N/A";

                        String maskedDoc = numDoc.length() > 4 ? "xx.xxx." + numDoc.substring(numDoc.length() - 3)
                                        : numDoc;

                        this.documentation = new DocumentationSection(typeDoc, maskedDoc, fileMap);
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

                                accountDetail = (walletEmail != null) ? walletEmail.replaceAll("(?<=.{3}).(?=.*@)", "*")
                                                : "N/A";
                        } else if (payment.getPaymentMethodType() == PaymentMethodTypes.CRYPTO_CURRENCY) {
                                network = payment.getNetwork();
                                walletAddress = payment.getWalletAddress();

                                accountDetail = (walletAddress != null && walletAddress.length() > 4)
                                                ? "xx.xxx." + walletAddress.substring(walletAddress.length() - 4)
                                                : "N/A";
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

                String verificationNotes = (personalData != null && personalData.getVerificationNotes() != null) 
                                        ? personalData.getVerificationNotes() : "N/A";
                this.identityVerification = new IdentityVerificationSection(verificationNotes);
        }

}
