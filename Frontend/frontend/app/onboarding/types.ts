export type OnboardingData = {
  fullName: string;
  email: string;
  phone: string;
  documentName: string;
  contractAccepted: boolean;
  paymentMethod: string;
  paymentDetails: {
    bankName?: string;
    accountNumber?: string;
    accountType?: string;
    walletAlias?: string;
  };
  verificationNotes: string;
};
