import { useEffect, useState } from "react";
import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: any) => void;
  onPaymentDetailChange: (field: string, value: string) => void;
}

export default function PaymentMethod({ data, onChange, onPaymentDetailChange }: Props) {
  const [isValidating, setIsValidating] = useState(false);
  const [validationResult, setValidationResult] = useState<{ status: "idle" | "success" | "error"; message: string }>({
    status: "idle",
    message: "",
  });

  useEffect(() => {
    setValidationResult({ status: "idle", message: "" });
    onChange("isPaymentVerified", false);
  }, [data.paymentMethod, data.paymentDetails.platform, data.paymentDetails.walletEmail, data.paymentDetails.network, data.paymentDetails.walletAddress]);

  // 1. Regex Validation Logic
  const validateFormat = () => {
    if (data.paymentMethod === "wallet") {
      if (!data.paymentDetails.platform) {
        return "Please select a digital platform to continue.";
      }
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(data.paymentDetails.walletEmail || "")) {
        return "Please enter a valid account email.";
      }
    } else if (data.paymentMethod === "crypto") {
      if (!data.paymentDetails.network) {
        return "Please select a network";
      }
      if ((data.paymentDetails.walletAddress?.length || 0) < 10) {
        return "Wallet address is too short.";
      }
    }
    return null;
  };

  // 2 & 3. Simulation with Latency and Keywords
  const handleVerify = async () => {
    const formatError = validateFormat();
    if (formatError) {
      setValidationResult({ status: "error", message: formatError });
      return;
    }

    setIsValidating(true);
    setValidationResult({ status: "idle", message: "" });

    // Genera un número aleatorio entre 2000 (2 segundos) y 5000 (5 segundos) y hace que el código se "detenga" ese tiempo. Esto imita lo que tardaría una respuesta real de internet.
    const delay = Math.floor(Math.random() * (5000 - 2000 + 1)) + 2000;
    await new Promise((resolve) => setTimeout(resolve, delay));

    // Simulation logic based on "Keywords" (Selective by method)
    const isErrorEmail = data.paymentMethod === "wallet" && data.paymentDetails.walletEmail === "error@test.com";
    const isErrorWallet = data.paymentMethod === "crypto" && data.paymentDetails.walletAddress === "0x000";

    if (isErrorEmail || isErrorWallet) {
      setValidationResult({
        status: "error",
        message: "Backend Rejection: Account not found or restricted by compliance.",
      });
      onChange("isPaymentVerified", false);
    } else {
      setValidationResult({
        status: "success",
        message: "Account verified successfully! You can proceed.",
      });
      onChange("isPaymentVerified", true);
    }
    setIsValidating(false);
  };

  return (
    <div className="space-y-8">
      <header>
        <p className="text-sm text-slate-600">Select your preferred digital payment channel</p>
      </header>

      {/* Method Selection Cards */}
      <div className="grid gap-4 sm:grid-cols-2">
        {[
          { id: "wallet", label: "Digital Platforms", description: "Wise, PayPal", icon: "💳" },
          { id: "crypto", label: "Cryptocurrencies", description: "USDT, USDC", icon: "₿" },
        ].map((option) => (
          <button
            type="button"
            key={option.id}
            onClick={() => onChange("paymentMethod", option.id)}
            className={`group relative rounded-3xl border p-6 text-left transition-all duration-300 ${
              data.paymentMethod === option.id 
                ? "border-sky-600 bg-sky-50 shadow-md ring-2 ring-sky-100" 
                : "border-slate-200 bg-white hover:border-sky-300 hover:bg-slate-50"
            }`}
          >
            <span className="text-3xl">{option.icon}</span>
            <p className="mt-4 font-semibold text-slate-900 group-hover:text-sky-700">{option.label}</p>
            <p className="mt-1 text-xs text-slate-500 leading-relaxed">{option.description}</p>
          </button>
        ))}
      </div>

      {/* Dynamic Form + Verification Button */}
      {data.paymentMethod && (
        <div className="animate-in fade-in slide-in-from-top-4 duration-500 rounded-[2rem] border border-slate-100 bg-slate-50/50 p-8 space-y-6">
          
          <div className="grid gap-6 sm:grid-cols-2">
            {data.paymentMethod === "wallet" ? (
              <>
                <div className="space-y-2">
                  <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">Platform</label>
                  <select
                    value={data.paymentDetails.platform || ""}
                    onChange={(e) => onPaymentDetailChange("platform", e.target.value)}
                    className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-4 text-sm text-slate-900 outline-none transition focus:border-sky-500"
                  >
                    <option value="">Select platform...</option>
                    <option value="wise">Wise</option>
                    <option value="paypal">PayPal</option>
                  </select>
                </div>
                <div className="space-y-2">
                  <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">Account Email</label>
                  <input
                    value={data.paymentDetails.walletEmail || ""}
                    onChange={(e) => onPaymentDetailChange("walletEmail", e.target.value)}
                    className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-4 text-sm text-slate-900 outline-none transition focus:border-sky-500"
                    placeholder="account@email.com"
                  />
                  <p className="text-xs text-slate-400">Use <span className="font-mono font-bold text-slate-800">error@test.com</span> to test a failure.</p>
                </div>
              </>
            ) : (
              <>
                <div className="space-y-2">
                  <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">Network</label>
                  <select
                    value={data.paymentDetails.network || ""}
                    onChange={(e) => onPaymentDetailChange("network", e.target.value)}
                    className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-4 text-sm text-slate-900 outline-none transition focus:border-sky-500"
                  >
                    <option value="">Select network...</option>
                    <option value="trc20">TRC20 (Tron)</option>
                    <option value="erc20">ERC20 (Ethereum)</option>
                  </select>
                </div>
                <div className="space-y-2">
                  <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">Wallet Address</label>
                  <input
                    value={data.paymentDetails.walletAddress || ""}
                    onChange={(e) => onPaymentDetailChange("walletAddress", e.target.value)}
                    className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-4 text-sm text-slate-900 outline-none transition focus:border-sky-500"
                    placeholder="0x..."
                  />
                </div>
              </>
            )}
          </div>

          {/* SIMULATION BUTTON */}
          <div className="pt-4">
            <button
              type="button"
              onClick={handleVerify}
              disabled={isValidating}
              className={`inline-flex items-center gap-2 rounded-2xl px-6 py-3 text-xs font-bold uppercase tracking-widest transition-all ${
                isValidating 
                  ? "bg-slate-200 text-slate-400 cursor-not-allowed" 
                  : "bg-slate-900 text-white hover:bg-sky-600 shadow-lg shadow-slate-900/10"
              }`}
            >
              {isValidating ? (
                <>
                  <span className="h-3 w-3 animate-spin rounded-full border-2 border-slate-400 border-t-transparent" />
                  Checking...
                </>
              ) : (
                "Verify Account"
              )}
            </button>
          </div>

          {/* VALIDATION FEEDBACK */}
          {validationResult.status !== "idle" && (
            <div className={`rounded-2xl p-4 text-sm font-medium animate-in zoom-in-95 duration-300 ${
              validationResult.status === "success" ? "bg-emerald-50 text-emerald-700 border border-emerald-100" : "bg-rose-50 text-rose-700 border border-rose-100"
            }`}>
              <div className="flex items-center gap-3">
                <span className="text-lg">{validationResult.status === "success" ? "✅" : "⚠️"}</span>
                {validationResult.message}
              </div>
            </div>
          )}
        </div>
      )}

      <footer className="rounded-2xl bg-sky-50/50 p-5 border border-sky-100/50">
        <p className="text-xs text-sky-700 leading-relaxed flex items-start gap-3">
          <span className="shrink-0 text-lg">🔒</span>
          All verification requests are processed via NorthPay's secure compliance tunnel (BE-US-09).
        </p>
      </footer>
    </div>
  );
}
