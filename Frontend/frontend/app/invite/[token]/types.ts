// Types for the Invitation Token API, based on API dtos and expected responses
export type InvitationTokenResponse = {
  id: string,
  tokenUrl: string,
  contractorEmail: string,
  used: boolean,
  isValid: boolean,
  expiresAt: string,
  createdAt: string,
  createdBy: string,
  onboardingId: string
}

export type InvitationTokenContractorRegistration = {
  tokenUrl: string,
  password: string,
  passwordConfirmation: string
}

export type InvitationTokenContractorLogin = {
  tokenUrl: string,
  password: string
}