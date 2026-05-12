"use client";

import { useMemo, useState, useEffect } from "react";
import Link from "next/link";
import { OnboardingData } from "./types";

import PersonalData from "../../components/onboarding/PersonalData";
import DocumentUpload from "../../components/onboarding/DocumentUpload";
import ContractSigning from "../../components/onboarding/ContractSigning";
import PaymentMethod from "../../components/onboarding/PaymentMethod";
import IdentityVerification from "../../components/onboarding/IdentityVerification";

const initialData: OnboardingData = {
  firstName: "",
  lastName: "",
  email: "",
  phone: "",
  documentName: "",
  contractAccepted: false,
  paymentMethod: "",
  paymentDetails: {
    walletAddress: "0x71C7656EC7ab88b098defB751B7401B5f6d8976F",
  },
  isPaymentVerified: false,
  verificationNotes: "",
};

const steps = [
  "Personal data",
  "Document upload",
  "Contract signing",
  "Payment method",
  "Identity verification",
];

export default function OnboardingPage() {
  const [stepIndex, setStepIndex] = useState(0);
  const [maxStepReached, setMaxStepReached] = useState(0);
  const [data, setData] = useState<OnboardingData>(initialData);
  const [submitted, setSubmitted] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);
  const [error, setError] = useState("");

  // 1. Load progress from sessionStorage on mount
  useEffect(() => {
    const saved = sessionStorage.getItem("onboarding_progress");
    if (saved) {
      try {
        const { stepIndex: s, data: d, maxStepReached: m } = JSON.parse(saved);
        setStepIndex(s);
        setData(d);
        setMaxStepReached(m);
      } catch (e) {
        console.error("Error loading onboarding progress", e);
      }
    }
  }, []);

  // 2. Save progress to sessionStorage on every change
  useEffect(() => {
    sessionStorage.setItem("onboarding_progress", JSON.stringify({ stepIndex, data, maxStepReached }));
  }, [stepIndex, data, maxStepReached]);

  // Clear error when changing steps
  useEffect(() => {
    setError("");
  }, [stepIndex]);

  const currentStep = steps[stepIndex];
  const progress = useMemo(() => Math.round(((stepIndex + 1) / steps.length) * 100), [stepIndex]);

  const handleChange = (field: keyof OnboardingData, value: any) => {
    setData((prev) => ({ ...prev, [field]: value }));
  };

  const handlePaymentDetailsChange = (field: string, value: string) => {
    setData((prev) => ({
      ...prev,
      paymentDetails: { ...prev.paymentDetails, [field]: value },
    }));
  };

  const validateStep = () => {
    if (stepIndex === 0) {
      if (!data.firstName.trim() || !data.lastName.trim() || !data.email.trim() || !data.phone.trim()) {
        return "Please complete all personal data fields.";
      }
    }
    if (stepIndex === 1) {
      if (!data.documentName) {
        return "Please upload at least one document.";
      }
    }
    if (stepIndex === 2) {
      if (!data.contractAccepted) {
        return "You must accept the contract to continue.";
      }
    }
    if (stepIndex === 3) {
      if (!data.paymentMethod || !data.isPaymentVerified) {
        return "PENDING_VERIFICATION";
      }
    }
    return "";
  };

  const handleNext = async () => {
    const validation = validateStep();
    if (validation) {
      setError(validation);
      return;
    }
    setError("");

    // Simulated secure submission for Step 4 (index 3)
    if (stepIndex === 3) {
      console.log("Sending encrypted payment data to BE-US-09...", data.paymentDetails);
    }

    if (stepIndex < steps.length - 1) {
      const nextStep = stepIndex + 1;
      setStepIndex(nextStep);
      setMaxStepReached((prev) => Math.max(prev, nextStep));
    } else {
      setSubmitted(true);
    }
  };

  const handlePrevious = () => {
    setError("");
    if (stepIndex > 0) {
      setStepIndex((index) => index - 1);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-6 sm:px-10">
      <div className="mx-auto max-w-5xl rounded-3xl border border-slate-200 bg-white p-8 shadow-lg shadow-slate-200/30">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.24em] text-sky-600">NorthPay</p>
            <h1 className="mt-3 text-3xl font-semibold text-slate-900 sm:text-4xl">Contractor onboarding</h1>
            <p className="mt-2 text-slate-600">Complete the process in 5 steps and keep your activation status visible.</p>
          </div>
          <div className="rounded-3xl bg-slate-100 px-4 py-3 text-sm text-slate-700">
            Step {stepIndex + 1} of {steps.length}
          </div>
        </div>

        <div className="mt-8 grid gap-6 lg:grid-cols-[260px_1fr]">
          <aside className="rounded-3xl border border-slate-200 bg-slate-50 p-6">
            <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Progress</p>
            <div className="mt-4 h-3 overflow-hidden rounded-full bg-slate-200">
              <div className="h-full rounded-full bg-sky-600" style={{ width: `${progress}%` }} />
            </div>
            <p className="mt-3 text-sm text-slate-600">{progress}% complete</p>
            <div className="mt-6 space-y-4">
              {steps.map((title, index) => {
                const isClickable = true
                const isCurrent = index === stepIndex;
                const isCompleted = index < stepIndex;
                return (
                  <button
                    key={title}
                    type="button"
                    onClick={() => isClickable && setStepIndex(index)}
                    disabled={!isClickable}
                    className={`flex w-full items-center gap-3 text-left transition ${isClickable ? "cursor-pointer hover:opacity-80" : "cursor-not-allowed opacity-50"}`}
                  >
                    <span className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-full text-sm font-semibold ${index <= stepIndex ? "bg-sky-600 text-white" : isClickable ? "bg-sky-100 text-sky-600" : "border border-slate-300 text-slate-500"}`}>
                      {index + 1}
                    </span>
                    <div>
                      <p className="text-sm font-semibold text-slate-900">{title}</p>
                      <p className="text-sm text-slate-500">{isCompleted ? "Completed" : isCurrent ? "Current" : "Pending"}</p>
                    </div>
                  </button>
                );
              })}
            </div>
          </aside>

          <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
            <div className="flex flex-col gap-2">
              <p className="text-sm uppercase tracking-[0.24em] text-slate-500">{currentStep}</p>
              <h2 className="text-2xl font-semibold text-slate-900">
                {submitted ? "Application Sent" : `Step ${stepIndex + 1}: ${currentStep}`}
              </h2>
            </div>

            <div className="mt-8">
              {submitted ? (
                <div className="rounded-3xl border border-sky-100 bg-sky-50 p-6 text-slate-800">
                  <p className="text-lg font-semibold">Done!</p>
                  <p className="mt-3 text-slate-600">
                    We have received your information. Your status is now <span className="font-semibold text-slate-900">pending verification</span>. You will receive notifications as soon as there are updates.
                  </p>
                  <div className="mt-6 flex flex-col gap-3 sm:flex-row">
                    <Link href="/admin" className="rounded-2xl bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700">
                      Check status in operations panel
                    </Link>
                    <Link href="/" className="rounded-2xl border border-slate-300 px-5 py-3 text-sm font-semibold text-slate-900 transition hover:bg-slate-100">
                      Go back home
                    </Link>
                  </div>
                </div>
              ) : (
                <form className="space-y-8">
                  {/* Step Components */}
                  {stepIndex === 0 && <PersonalData data={data} onChange={handleChange} />}
                  {stepIndex === 1 && <DocumentUpload data={data} onChange={handleChange} />}
                  {stepIndex === 2 && <ContractSigning data={data} onChange={handleChange} />}
                  {stepIndex === 3 && (
                    <PaymentMethod
                      data={data}
                      onChange={handleChange}
                      onPaymentDetailChange={handlePaymentDetailsChange}
                    />
                  )}
                  {stepIndex === 4 && <IdentityVerification data={data} onChange={handleChange} />}

                  {error && <p className="rounded-2xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">{error}</p>}

                  <div className="flex flex-col gap-3 sm:flex-row sm:justify-between">
                    <button
                      type="button"
                      onClick={handlePrevious}
                      disabled={stepIndex === 0}
                      className="rounded-3xl border border-slate-300 bg-white px-6 py-3 text-sm font-semibold text-slate-900 transition disabled:cursor-not-allowed disabled:opacity-50 hover:bg-slate-100"
                    >
                      Back
                    </button>
                    <button
                      type="button"
                      onClick={handleNext}
                      disabled={(stepIndex === 3 && !!validateStep()) || isProcessing}
                      className="rounded-3xl bg-sky-600 px-6 py-3 text-sm font-semibold text-white transition hover:bg-sky-700 disabled:cursor-not-allowed disabled:bg-slate-200 disabled:text-slate-400"
                    >
                      {isProcessing ? (
                        <>
                          <span className="h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent" />
                          Processing...
                        </>
                      ) : (
                        stepIndex === steps.length - 1 ? "Submit application" : "Next step"
                      )}
                    </button>
                  </div>
                </form>
              )}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}
