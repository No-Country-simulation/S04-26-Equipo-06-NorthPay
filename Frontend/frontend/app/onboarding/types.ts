export type OnboardingData = {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  documentName: string;
  contractAccepted: boolean;
  paymentMethod: string;
  paymentDetails: {
    platform?: string;
    walletEmail?: string;
    network?: string;
    walletAddress?: string;
  };
  isPaymentVerified: boolean;
  verificationNotes: string;
};
