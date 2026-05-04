"use client";

import { useMemo, useState } from "react";
import Link from "next/link";

type OnboardingData = {
  fullName: string;
  email: string;
  phone: string;
  documentName: string;
  contractAccepted: boolean;
  paymentMethod: string;
  verificationNotes: string;
};

const initialData: OnboardingData = {
  fullName: "",
  email: "",
  phone: "",
  documentName: "",
  contractAccepted: false,
  paymentMethod: "",
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
  const [data, setData] = useState<OnboardingData>(initialData);
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState("");

  const currentStep = steps[stepIndex];

  const progress = useMemo(() => Math.round(((stepIndex + 1) / steps.length) * 100), [stepIndex]);

  const handleChange = (field: keyof OnboardingData, value: string | boolean) => {
    setData((prev) => ({ ...prev, [field]: value }));
  };

  const validateStep = () => {
    if (stepIndex === 0) {
      if (!data.fullName || !data.email || !data.phone) {
        return "Complete all personal data fields.";
      }
    }
    if (stepIndex === 1) {
      if (!data.documentName) {
        return "Upload at least one document.";
      }
    }
    if (stepIndex === 2) {
      if (!data.contractAccepted) {
        return "You must accept the contract to continue.";
      }
    }
    if (stepIndex === 3) {
      if (!data.paymentMethod) {
        return "Select a payment method.";
      }
    }
    return "";
  };

  const handleNext = () => {
    const validation = validateStep();
    if (validation) {
      setError(validation);
      return;
    }
    setError("");
    if (stepIndex < steps.length - 1) {
      setStepIndex((index) => index + 1);
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
              {steps.map((title, index) => (
                <div key={title} className="flex items-center gap-3">
                  <span className={`flex h-9 w-9 items-center justify-center rounded-full text-sm font-semibold ${index <= stepIndex ? "bg-sky-600 text-white" : "border border-slate-300 text-slate-500"}`}>
                    {index + 1}
                  </span>
                  <div>
                    <p className="text-sm font-semibold text-slate-900">{title}</p>
                    <p className="text-sm text-slate-500">{index < stepIndex ? "Completed" : index === stepIndex ? "Current" : "Pending"}</p>
                  </div>
                </div>
              ))}
            </div>
          </aside>

          <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
            <div className="flex flex-col gap-2">
              <p className="text-sm uppercase tracking-[0.24em] text-slate-500">{currentStep}</p>
              <h2 className="text-2xl font-semibold text-slate-900">{submitted ? "Solicitud enviada" : `Paso ${stepIndex + 1}: ${currentStep}`}</h2>
            </div>

            <div className="mt-8">
              {submitted ? (
                <div className="rounded-3xl border border-sky-100 bg-sky-50 p-6 text-slate-800">
                  <p className="text-lg font-semibold">¡Listo!</p>
                  <p className="mt-3 text-slate-600">Hemos recibido tu información. Queda en estado <span className="font-semibold text-slate-900">pendiente de verificación</span>. Recibirás notificaciones en cuanto haya novedades.</p>
                  <div className="mt-6 flex flex-col gap-3 sm:flex-row">
                    <Link href="/admin" className="rounded-2xl bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700">
                      Ver estado en panel de operaciones
                    </Link>
                    <Link href="/" className="rounded-2xl border border-slate-300 px-5 py-3 text-sm font-semibold text-slate-900 transition hover:bg-slate-100">
                      Volver al inicio
                    </Link>
                  </div>
                </div>
              ) : (
                <form className="space-y-8">
                  {stepIndex === 0 && (
                    <div className="space-y-6">
                      <div>
                        <label className="block text-sm font-medium text-slate-700">Nombre completo</label>
                        <input
                          value={data.fullName}
                          onChange={(event) => handleChange("fullName", event.target.value)}
                          className="mt-3 w-full rounded-3xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                          placeholder="Ej. María Pérez"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-slate-700">Correo electrónico</label>
                        <input
                          type="email"
                          value={data.email}
                          onChange={(event) => handleChange("email", event.target.value)}
                          className="mt-3 w-full rounded-3xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                          placeholder="correo@ejemplo.com"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-slate-700">Teléfono</label>
                        <input
                          value={data.phone}
                          onChange={(event) => handleChange("phone", event.target.value)}
                          className="mt-3 w-full rounded-3xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                          placeholder="+54 9 11 1234 5678"
                        />
                      </div>
                    </div>
                  )}

                  {stepIndex === 1 && (
                    <div className="space-y-6">
                      <p className="text-sm text-slate-600">Sube tus documentos oficiales para continuar con la verificación.</p>
                      <label className="flex cursor-pointer items-center justify-between rounded-3xl border border-dashed border-slate-300 bg-slate-50 px-4 py-5 text-sm text-slate-700 transition hover:border-slate-400">
                        <span>{data.documentName ? data.documentName : "Seleccionar documento"}</span>
                        <input
                          type="file"
                          onChange={(event) => {
                            const file = event.target.files?.[0];
                            if (file) handleChange("documentName", file.name);
                          }}
                          className="hidden"
                        />
                      </label>
                    </div>
                  )}

                  {stepIndex === 2 && (
                    <div className="space-y-6">
                      <p className="text-sm text-slate-600">Lee y acepta las condiciones del contrato digitalizado.</p>
                      <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5 text-sm leading-7 text-slate-700">
                        Al firmar electrónicamente, autorizas a NorthPay a procesar tu pago y documentación como parte del onboarding.
                      </div>
                      <label className="flex items-center gap-3 text-sm text-slate-700">
                        <input
                          type="checkbox"
                          checked={data.contractAccepted}
                          onChange={(event) => handleChange("contractAccepted", event.target.checked)}
                          className="h-5 w-5 rounded border-slate-300 text-sky-600 focus:ring-sky-500"
                        />
                        Acepto el contrato digital y autorizo el proceso de activación.
                      </label>
                    </div>
                  )}

                  {stepIndex === 3 && (
                    <div className="space-y-6">
                      <p className="text-sm text-slate-600">Configura el método de pago donde recibirás tus comisiones.</p>
                      <div className="grid gap-4 sm:grid-cols-2">
                        {[
                          { id: "bank", label: "Cuenta bancaria" },
                          { id: "wallet", label: "Billetera digital" },
                        ].map((option) => (
                          <button
                            type="button"
                            key={option.id}
                            onClick={() => handleChange("paymentMethod", option.id)}
                            className={`rounded-3xl border px-5 py-4 text-left transition ${data.paymentMethod === option.id ? "border-sky-600 bg-sky-50 shadow-sm" : "border-slate-200 bg-white hover:border-slate-300"}`}>
                            <p className="font-semibold text-slate-900">{option.label}</p>
                            <p className="mt-2 text-sm text-slate-600">Selecciona el canal de pago preferido para recibir tus tarifas.</p>
                          </button>
                        ))}
                      </div>
                    </div>
                  )}

                  {stepIndex === 4 && (
                    <div className="space-y-6">
                      <p className="text-sm text-slate-600">Finaliza con la verificación de identidad. Nuestro equipo revisará tus datos y documentos.</p>
                      <textarea
                        value={data.verificationNotes}
                        onChange={(event) => handleChange("verificationNotes", event.target.value)}
                        rows={4}
                        placeholder="Comparte algún dato adicional que ayude a la verificación"
                        className="w-full rounded-3xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
                      />
                    </div>
                  )}

                  {error && <p className="rounded-2xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">{error}</p>}

                  <div className="flex flex-col gap-3 sm:flex-row sm:justify-between">
                    <button
                      type="button"
                      onClick={handlePrevious}
                      disabled={stepIndex === 0}
                      className="rounded-3xl border border-slate-300 bg-white px-6 py-3 text-sm font-semibold text-slate-900 transition disabled:cursor-not-allowed disabled:opacity-50 hover:bg-slate-100"
                    >
                      Atrás
                    </button>
                    <button
                      type="button"
                      onClick={handleNext}
                      className="rounded-3xl bg-sky-600 px-6 py-3 text-sm font-semibold text-white transition hover:bg-sky-700"
                    >
                      {stepIndex === steps.length - 1 ? "Enviar solicitud" : "Siguiente paso"}
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
