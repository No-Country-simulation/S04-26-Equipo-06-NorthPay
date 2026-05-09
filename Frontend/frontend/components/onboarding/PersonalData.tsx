import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: string) => void;
}

export default function PersonalData({ data, onChange }: Props) {
  return (
    <div className="space-y-6">
      <div>
        <label className="block text-sm font-medium text-slate-700">Full Name</label>
        <input
          value={data.fullName}
          onChange={(event) => onChange("fullName", event.target.value)}
          className="mt-3 w-full rounded-3xl text-slate-900 border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
          placeholder="e.g. Maria Perez"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-slate-700">Email Address</label>
        <input
          type="email"
          value={data.email}
          onChange={(event) => onChange("email", event.target.value)}
          className="mt-3 w-full rounded-3xl text-slate-900 border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
          placeholder="email@example.com"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-slate-700">Phone Number</label>
        <input
          value={data.phone}
          onChange={(event) => onChange("phone", event.target.value)}
          className="mt-3 w-full rounded-3xl text-slate-900 border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
          placeholder="+54 9 11 1234 5678"
        />
      </div>
    </div>
  );
}
