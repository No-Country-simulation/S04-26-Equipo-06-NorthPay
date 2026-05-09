import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: string) => void;
}

export default function IdentityVerification({ data, onChange }: Props) {
  return (
    <div className="space-y-6">
      <p className="text-sm text-slate-600">Finish with identity verification. Our team will review your data and documents.</p>
      <textarea
        value={data.verificationNotes}
        onChange={(event) => onChange("verificationNotes", event.target.value)}
        rows={4}
        placeholder="Share any additional information that helps with verification"
        className="w-full rounded-3xl text-slate-900 border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
      />
    </div>
  );
}
