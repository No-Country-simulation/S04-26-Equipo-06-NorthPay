import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: string) => void;
}

export default function DocumentUpload({ data, onChange }: Props) {
  return (
    <div className="space-y-6">
      <p className="text-sm text-slate-600">Upload your official documents to continue with verification.</p>
      <label className="flex cursor-pointer items-center justify-between rounded-3xl border border-dashed border-slate-300 bg-slate-50 px-4 py-5 text-sm text-slate-700 transition hover:border-slate-400">
        <span>{data.documentName ? data.documentName : "Select document"}</span>
        <input
          type="file"
          onChange={(event) => {
            const file = event.target.files?.[0];
            if (file) onChange("documentName", file.name);
          }}
          className="hidden"
        />
      </label>
    </div>
  );
}
