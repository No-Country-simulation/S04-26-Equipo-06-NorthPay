export type OnboardingData = {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  country: string;
  address: string;
  documentName: string;
  documentType: string;
  dniNumber: string;
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
