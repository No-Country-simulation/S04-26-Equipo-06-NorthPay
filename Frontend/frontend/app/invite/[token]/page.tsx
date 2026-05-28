"use client";

import { useState, useEffect, use } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { OnboardingData } from "../../onboarding/types";
import { getToken, sendLoginContractorToken, sendRegistrationContractorToken } from "./requests";

const steps = [
  "Personal data",
  "Document upload",
  "Contract signing",
  "Payment method",
  "Identity verification",
];

export type StatusType = "loading" | "error" | "not_valid" | "not_found" | "success" | "first_time" | "login";

export default function InviteWelcomePage({ params }: { params: Promise<{ token: string }> }) {
  const router = useRouter();
  const { token } = use(params);

  const [status, setStatus] = useState<StatusType>("loading");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [preloadedData, setPreloadedData] = useState<{ email: string; onboardingId?: string } | null>(null);

  const [tokenFormData, setTokenFormData] = useState({
    password: "",
    passwordConfirmation: ""
  });

  useEffect(() => {
    getToken(setStatus, token, setPreloadedData);
  }, [token]);

  const handleStartProcess = () => {
    if (!preloadedData) return;

    const initialData: OnboardingData = {
      firstName: "",
      lastName: "",
      email: preloadedData.email,
      phone: "",
      country: "",
      address: "",
      documentName: "",
      documentType: "",
      dniNumber: "",
      contractAccepted: false,
      paymentMethod: "",
      paymentDetails: {
        walletAddress: "0x71C7656EC7ab88b098defB751B7401B5f6d8976F",
      },
      isPaymentVerified: false,
      verificationNotes: "",
    };

    // We pass the preloaded email and the onboardingId if we got it from the real backend!
    sessionStorage.setItem(
      "onboarding_progress",
      JSON.stringify({ stepIndex: 0, data: initialData, maxStepReached: 0, onboardingId: preloadedData.onboardingId || null })
    );

    router.push("/onboarding");
  };

  if (status === "loading") {
    return (
      <div className="flex min-h-screen items-center justify-center bg-slate-50">
        <div className="flex flex-col items-center gap-4">
          <div className="h-10 w-10 animate-spin rounded-full border-4 border-slate-200 border-t-sky-600" />
          <p className="text-sm font-medium text-slate-500 animate-pulse">Validating invitation...</p>
        </div>
      </div>
    );
  }

  if (status === "not_found") {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center bg-slate-50 px-6">
        <div className="max-w-md text-center">
          <p className="text-sm font-semibold uppercase tracking-widest text-sky-600">404 Error</p>
          <h1 className="mt-4 text-3xl font-bold text-slate-900">Invitation not found</h1>
          <p className="mt-4 text-slate-600">
            The invitation link is invalid or malformed. Please check the URL and try again.
          </p>
          <Link
            href="/"
            className="mt-8 inline-block rounded-3xl bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-800"
          >
            Go to homepage
          </Link>
        </div>
      </div>
    );
  }

  if (status === "not_valid") {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center bg-slate-50 px-6">
        <div className="max-w-md text-center">
          <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-rose-100">
            <svg className="h-8 w-8 text-rose-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h1 className="mt-6 text-2xl font-bold text-slate-900">Invitation Expired</h1>
          <p className="mt-4 text-slate-600">
            This invitation link has expired or has been invalidated. You need a new invitation to proceed.
          </p>
          <a
            href="mailto:support@northpay.com"
            className="mt-8 inline-block rounded-3xl bg-sky-600 px-6 py-3 text-sm font-semibold text-white transition hover:bg-sky-700"
          >
            Contact Support
          </a>
        </div>
      </div>
    );
  }

  if (status === "error") {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center bg-slate-50 px-6">
        <div className="max-w-md text-center">
          <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-slate-200">
            <svg className="h-8 w-8 text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h1 className="mt-6 text-2xl font-bold text-slate-900">Connection Error</h1>
          <p className="mt-4 text-slate-600">
            {errorMessage || "We had trouble connecting to the server. Please check your internet connection and try again."}
          </p>
          <button
            onClick={() => getToken(setStatus, token, setPreloadedData)}
            className="mt-8 inline-block rounded-3xl bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-800"
          >
            Retry
          </button>
        </div>
      </div>
    );
  }

  if (status === "first_time") {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center bg-slate-50 px-6">
        <div className="max-w-md text-center">
          <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-sky-100">
            <svg className="h-8 w-8 text-sky-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
          <h1 className="mt-6 text-2xl font-bold text-slate-900">Welcome!</h1>
          <p className="mt-4 text-slate-600">
            You have been invited to join the platform. Please create a password to complete your registration.
          </p>
          <form onSubmit={(e) => sendRegistrationContractorToken(e, token, tokenFormData, setStatus, setErrorMessage)} className="mt-6 space-y-4">
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-slate-700">Password</label>
              <input
                type="password"
                id="password"
                value={tokenFormData.password}
                onChange={(e) => setTokenFormData({ ...tokenFormData, password: e.target.value })}
                className="mt-1 block w-full rounded-md border border-slate-300 bg-slate-50 py-2 px-3 shadow-sm focus:outline-none focus:ring-2 focus:ring-sky-500"
              />
            </div>
            <button
              type="submit"
              className="inline-block rounded-3xl bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-800"
            >
              Create Account
            </button>
          </form>
        </div>
      </div>
    );
  }

  if (status === "login") {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center bg-slate-50 px-6">
        <div className="max-w-md text-center">
          <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-slate-200">
            <svg className="h-8 w-8 text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
            </svg>
          </div>
          <h1 className="mt-6 text-2xl font-bold text-slate-900">Login to Your Account</h1>
          <p className="mt-4 text-slate-600">
            Please use the password you created to access your account and proceed.
          </p>
          <form onSubmit={(e) => sendLoginContractorToken(e, token, tokenFormData, setStatus, setErrorMessage)} className="mt-6 space-y-4">
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-slate-700">Password</label>
              <input
                type="password"
                value={tokenFormData.password}
                onChange={(e) => setTokenFormData({ ...tokenFormData, password: e.target.value })}
                className="mt-1 block w-full rounded-md border border-slate-300 bg-slate-50 py-2 px-3 shadow-sm focus:outline-none focus:ring-2 focus:ring-sky-500"
              />
            </div>
            <button
              type="submit"
              className="inline-block rounded-3xl bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-800"
            >
              Login
            </button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-6 sm:px-10">
      <div className="mx-auto max-w-4xl rounded-3xl border border-slate-200 bg-white p-8 shadow-lg shadow-slate-200/30">
        <div className="flex flex-col md:flex-row gap-12">
          
          <div className="flex-1 space-y-6">
            <div>
              <p className="text-sm font-semibold uppercase tracking-[0.24em] text-sky-600">
                Welcome to NorthPay
              </p>
              <h1 className="mt-3 text-3xl font-semibold text-slate-900 sm:text-4xl">
                Ready to get started?
              </h1>
              <p className="mt-4 text-slate-600 text-lg leading-relaxed">
                Hi! Let&apos;s get your account set up. You have been invited to join the platform. 
                Start your onboarding process to complete your profile and begin operating with us.
              </p>
            </div>

            <div className="rounded-2xl bg-slate-50 p-6 border border-slate-100">
              <h3 className="text-sm font-semibold text-slate-900 uppercase tracking-wider mb-4">Your details</h3>
              <div className="space-y-3">
                <div>
                  <p className="text-xs text-slate-500">Email</p>
                  <p className="font-medium text-slate-900">{preloadedData?.email}</p>
                </div>
              </div>
            </div>

            <button
              onClick={handleStartProcess}
              className="w-full md:w-auto rounded-3xl bg-sky-600 px-8 py-4 text-base font-semibold text-white transition hover:bg-sky-700 hover:shadow-md hover:-translate-y-0.5"
            >
              Start Onboarding Process
            </button>
          </div>

          <aside className="w-full md:w-72 shrink-0 rounded-3xl border border-slate-200 bg-slate-50 p-6">
            <h3 className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500 mb-6">
              Process Overview
            </h3>

            <div className="space-y-6">
              {steps.map((title, index) => {
                const isFirst = index === 0;

                return (
                  <div key={title} className="flex gap-4">
                    <div className="flex flex-col items-center">
                      <div
                        className={`flex h-8 w-8 shrink-0 items-center justify-center rounded-full text-xs font-semibold ${
                          isFirst
                            ? "bg-sky-600 text-white shadow-md shadow-sky-600/20"
                            : "border-2 border-slate-200 bg-white text-slate-400"
                        }`}
                      >
                        {index + 1}
                      </div>
                      {index !== steps.length - 1 && (
                        <div className={`w-0.5 h-full min-h-[2rem] mt-2 ${isFirst ? "bg-sky-200" : "bg-slate-200"}`} />
                      )}
                    </div>
                    
                    <div className="pb-2 pt-1.5">
                      <p className={`text-sm font-medium ${isFirst ? "text-slate-900" : "text-slate-500"}`}>
                        {title}
                      </p>
                      {isFirst && (
                        <p className="text-xs text-sky-600 mt-1 font-medium">Starts here</p>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </aside>
          
        </div>
      </div>
    </div>
  );
}
