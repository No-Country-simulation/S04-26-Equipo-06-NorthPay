import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: boolean) => void;
}

export default function ContractSigning({ data, onChange }: Props) {
  return (
    <div className="space-y-6">
      <p className="text-sm text-slate-600">Read and accept the terms of the digitized contract.</p>
      <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5 text-sm leading-7 text-slate-700">
        By signing electronically, you authorize NorthPay to process your payment and documentation as part of the onboarding.
      </div>
      <label className="flex items-center gap-3 text-sm text-slate-700">
        <input
          type="checkbox"
          checked={data.contractAccepted}
          onChange={(event) => onChange("contractAccepted", event.target.checked)}
          className="h-5 w-5 rounded border-slate-300 text-sky-600 focus:ring-sky-500"
        />
        I accept the digital contract and authorize the activation process.
      </label>
    </div>
  );
}
