import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: string) => void;
}

export default function PersonalData({ data, onChange }: Props) {
  return (
    <div className="space-y-6">
      <p className="text-sm text-slate-600">Please provide your legal personal information as it appears on your official ID.</p>
      
      <div className="grid gap-5 sm:grid-cols-2">
        <div className="space-y-2">
          <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">First Name</label>
          <input
            type="text"
            value={data.firstName}
            onChange={(e) => onChange("firstName", e.target.value)}
            className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-3.5 text-sm text-slate-900 outline-none transition focus:border-sky-500 focus:ring-4 focus:ring-sky-500/10"
            placeholder="e.g. John"
          />
        </div>
        <div className="space-y-2">
          <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">Last Name</label>
          <input
            type="text"
            value={data.lastName}
            onChange={(e) => onChange("lastName", e.target.value)}
            className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-3.5 text-sm text-slate-900 outline-none transition focus:border-sky-500 focus:ring-4 focus:ring-sky-500/10"
            placeholder="e.g. Doe"
          />
        </div>
      </div>

      <div className="grid gap-5 sm:grid-cols-2">
        <div className="space-y-2">
          <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">Email Address</label>
          <input
            type="email"
            value={data.email}
            onChange={(e) => onChange("email", e.target.value)}
            className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-3.5 text-sm text-slate-900 outline-none transition focus:border-sky-500 focus:ring-4 focus:ring-sky-500/10"
            placeholder="john.doe@example.com"
          />
        </div>
        <div className="space-y-2">
          <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">Phone Number</label>
          <input
            type="tel"
            value={data.phone}
            onChange={(e) => onChange("phone", e.target.value)}
            className="w-full rounded-2xl border border-slate-200 bg-white px-5 py-3.5 text-sm text-slate-900 outline-none transition focus:border-sky-500 focus:ring-4 focus:ring-sky-500/10"
            placeholder="+1 (555) 000-0000"
          />
        </div>
      </div>
    </div>
  );
}
