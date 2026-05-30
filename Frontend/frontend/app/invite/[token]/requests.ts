import { ErrorResponse, JWTResponse } from "@/types/global";
import { SetStateAction } from "react";
import { StatusType } from "./page";
import { InvitationTokenContractorRegistration, InvitationTokenResponse } from "./types";
import { parseJWTPayload } from "@/utils/JWTUtils";

export const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export const getToken = async (
  setStatus: (value: SetStateAction<StatusType>) => void,
  token: string,
  setErrorMessage: (value: SetStateAction<string | null>) => void,
  setPreloadedData: (value: SetStateAction<{ email: string; onboardingId: string, name: string } | null>) => void
) => {
  setStatus("loading");
  try {
    // ====================================================
    // Fetch validation and pre-loaded data from backend (BE-US-01 & BE-US-02)
    const response = await fetch(`${API_URL}/api/v1/invitation-token/validate/${token}`);

    if (!response.ok) {
      if (response.status === 404) {
        return setStatus("not_found");
      }
      
      if (response.status === 401) {
        return setStatus("not_valid");
      }

      const errorData: ErrorResponse = await response.json();
      setErrorMessage(errorData.message);
      throw new Error("Connection loss");
    }

    const data: InvitationTokenResponse = await response.json();
    setPreloadedData({
      email: data.contractorEmail,
      onboardingId: data.onboardingId,
      name: ""
    });
    console.log(data.onboardingId, data)
    if(!data.used) {
      setStatus("first_time");
    }
    if (data.used) {
      setStatus("login");
    }
  } catch (e) {
    console.error((e as Error).toString());
    setStatus("error");
  }
};

export const sendRegistrationContractorToken = async (
  e: React.SubmitEvent<HTMLFormElement>,
  token: string,
  tokenFormData: { password: string; passwordConfirmation: string },
  setStatus: (value: SetStateAction<StatusType>) => void,
  setErrorMessage: (value: SetStateAction<string | null>) => void,
  setIsDoingRequest: (value: SetStateAction<boolean>) => void
) => {
  e.preventDefault();
  setIsDoingRequest(true);

  const body: InvitationTokenContractorRegistration = {
    tokenUrl: token,
    password: tokenFormData.password,
    passwordConfirmation: tokenFormData.passwordConfirmation
  };

  const response = await fetch(`${API_URL}/api/v1/invitation-token/first-time-use`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    switch (response.status) {
      case 404:
        setStatus("not_found");
        break;
      case 401: 
        setStatus("not_valid");
        break;
      default:
        const failureData: ErrorResponse = await response.json();
        setErrorMessage(failureData.message)
        setStatus("error");
        break;
    }
    return;
  }

  setErrorMessage(null);
  setIsDoingRequest(false);
  setStatus("login");
}

export const sendLoginContractorToken = async (
  e: React.SubmitEvent<HTMLFormElement>,
  token: string,
  tokenFormData: { password: string },
  setStatus: (value: SetStateAction<StatusType>) => void,
  setErrorMessage: (value: SetStateAction<string | null>) => void,
  setPreloadedData: (value: SetStateAction<{ email: string; onboardingId: string, name: string } | null>) => void,
  setIsDoingRequest: (value: SetStateAction<boolean>) => void
) => {
  e.preventDefault();
  setIsDoingRequest(true);

  const body = {
    tokenUrl: token,
    password: tokenFormData.password,
  };

  const response = await fetch(`${API_URL}/api/v1/invitation-token/login`, {
    method: "POST",
    headers: { 
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    switch (response.status) {
      case 404:
        setStatus("not_found");
        break;
      case 401: 
        setStatus("not_valid");
        break;
      default:
        const failureData: ErrorResponse = await response.json();
        setErrorMessage(failureData.message)
        setStatus("error");
        break;
    }
    return;
  }

  setErrorMessage(null);
  setIsDoingRequest(false);
  setStatus("success");

  const tokenDto: JWTResponse = await response.json();
  // de esta cookie se saca el token para enviarlo en los headers de Bearer en las siguientes requests
  document.cookie = `returnedToken=${encodeURIComponent(tokenDto.returnedToken)};`;
  const tokenPayload = parseJWTPayload();
  const sanitizedName = tokenPayload?.name && !/^(null\s*)+$/i.test(tokenPayload.name.trim()) ? tokenPayload.name : "";
  setPreloadedData(prev => tokenPayload ? {
    email: tokenPayload.sub,
    name: sanitizedName,
    onboardingId: prev?.onboardingId || 
  setPreloadedData(prev => tokenPayload ? {
    email: tokenPayload.sub,
    name: tokenPayload.name,
    onboardingId: prev?.onboardingId || ""
  } : null);
}
