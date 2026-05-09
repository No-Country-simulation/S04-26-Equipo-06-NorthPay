import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: any) => void;
  onPaymentDetailChange: (field: string, value: string) => void;
}

export default function PaymentMethod({ data, onChange, onPaymentDetailChange }: Props) {
  return (
    <div className="space-y-6">
      <p className="text-sm text-slate-600">Set up the payment method where you will receive your commissions.</p>
      <div className="grid gap-4 sm:grid-cols-2">
        {[
          { id: "bank", label: "Bank Account", description: "Transfer via CBU/CVU" },
          { id: "wallet", label: "Digital Wallet", description: "Mercado Pago, Lemon, etc." },
        ].map((option) => (
          <button
            type="button"
            key={option.id}
            onClick={() => onChange("paymentMethod", option.id)}
            className={`rounded-3xl border px-5 py-4 text-left transition ${data.paymentMethod === option.id ? "border-sky-600 bg-sky-50 shadow-sm" : "border-slate-200 bg-white hover:border-slate-300"}`}>
            <p className="font-semibold text-slate-900">{option.label}</p>
            <p className="mt-2 text-xs text-slate-600">{option.description}</p>
          </button>
        ))}
      </div>

      {data.paymentMethod === "bank" && (
        <div className="animate-in fade-in slide-in-from-top-2 duration-300 space-y-4 rounded-3xl border border-slate-100 bg-slate-50 p-6">
          <div className="grid gap-4 sm:grid-cols-2">
            <div>
              <label className="text-xs font-semibold uppercase tracking-wider text-slate-500">Bank / Institution</label>
              <input
                value={data.paymentDetails.bankName || ""}
                onChange={(e) => onPaymentDetailChange("bankName", e.target.value)}
                className="mt-2 w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-900 outline-none focus:border-sky-500"
                placeholder="e.g. Banco Galicia"
              />
            </div>
            <div>
              <label className="text-xs font-semibold uppercase tracking-wider text-slate-500">Account Type</label>
              <select
                value={data.paymentDetails.accountType || ""}
                onChange={(e) => onPaymentDetailChange("accountType", e.target.value)}
                className="mt-2 w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-900 outline-none focus:border-sky-500"
              >
                <option value="">Select...</option>
                <option value="ca_pesos">Savings Account ($)</option>
                <option value="cc_pesos">Checking Account ($)</option>
              </select>
            </div>
          </div>
          <div>
            <label className="text-xs font-semibold uppercase tracking-wider text-slate-500">CBU / CVU (22 digits)</label>
            <input
              value={data.paymentDetails.accountNumber || ""}
              onChange={(e) => onPaymentDetailChange("accountNumber", e.target.value.replace(/\D/g, "").slice(0, 22))}
              className="mt-2 w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-900 outline-none focus:border-sky-500"
              placeholder="0000000000000000000000"
            />
          </div>
        </div>
      )}

      {data.paymentMethod === "wallet" && (
        <div className="animate-in fade-in slide-in-from-top-2 duration-300 space-y-4 rounded-3xl border border-slate-100 bg-slate-50 p-6">
          <div>
            <label className="text-xs font-semibold uppercase tracking-wider text-slate-500">Alias or Wallet CVU</label>
            <input
              value={data.paymentDetails.walletAlias || ""}
              onChange={(e) => onPaymentDetailChange("walletAlias", e.target.value)}
              className="mt-2 w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-900 outline-none focus:border-sky-500"
              placeholder="e.g. my.payment.alias"
            />
            <p className="mt-2 text-xs text-slate-500">Make sure the alias matches your identity.</p>
          </div>
        </div>
      )}
    </div>
  );
}
