import { OnboardingData } from "../../app/onboarding/types";

type PersonalDataField = "firstName" | "lastName" | "email" | "phone" | "country" | "address";

type PersonalDataErrors = Partial<Record<PersonalDataField, string>>;

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: string) => void;
  errors: PersonalDataErrors;
}

export default function PersonalData({ data, onChange, errors }: Props) {
  const hasError = (field: PersonalDataField) => {
    return Boolean(errors[field]);
  };

  const getInputClassName = (field: PersonalDataField) => {
    return `w-full rounded-2xl border bg-white px-5 py-3.5 text-sm text-slate-900 outline-none transition focus:ring-4 ${
      hasError(field)
        ? "border-rose-300 focus:border-rose-500 focus:ring-rose-500/10"
        : "border-slate-200 focus:border-sky-500 focus:ring-sky-500/10"
    }`;
  };

  const renderError = (field: PersonalDataField) => {
    if (!errors[field]) {
      return null;
    }

    return <p className="text-xs font-medium text-rose-600">{errors[field]}</p>;
  };

  return (
    <div className="space-y-6">
      <p className="text-sm text-slate-600">
        Please provide your legal personal information as it appears on your
        official ID.
      </p>

      <div className="grid gap-5 sm:grid-cols-2">
        <div className="space-y-2">
          <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">
            First Name
          </label>

          <input
            id="firstName"
            type="text"
            value={data.firstName ?? ""}
            onChange={(e) => onChange("firstName", e.target.value)}
            className={getInputClassName("firstName")}
            placeholder="e.g. John"
            aria-invalid={hasError("firstName")}
            aria-describedby={
              hasError("firstName") ? "firstName-error" : undefined
            }
          />

          <div id="firstName-error">{renderError("firstName")}</div>
        </div>

        <div className="space-y-2">
          <label
            htmlFor="lastName"
            className="text-[11px] font-bold uppercase tracking-widest text-slate-400"
          >
            Last Name
          </label>

          <input
            id="lastName"
            type="text"
            value={data.lastName ?? ""}
            onChange={(e) => onChange("lastName", e.target.value)}
            className={getInputClassName("lastName")}
            placeholder="e.g. Doe"
            aria-invalid={hasError("lastName")}
            aria-describedby={
              hasError("lastName") ? "lastName-error" : undefined
            }
          />

          <div id="lastName-error">{renderError("lastName")}</div>
        </div>
      </div>

      <div className="grid gap-5 sm:grid-cols-2">
        <div className="space-y-2">
          <label
            htmlFor="email"
            className="text-[11px] font-bold uppercase tracking-widest text-slate-400"
          >
            Email Address
          </label>

          <input
            id="email"
            type="email"
            value={data.email}
            onChange={(e) => onChange("email", e.target.value)}
            className={getInputClassName("email")}
            placeholder="john.doe@example.com"
            aria-invalid={hasError("email")}
            aria-describedby={hasError("email") ? "email-error" : undefined}
          />

          <div id="email-error">{renderError("email")}</div>
        </div>

        <div className="space-y-2">
          <label
            htmlFor="phone"
            className="text-[11px] font-bold uppercase tracking-widest text-slate-400"
          >
            Phone Number
          </label>

          <input
            id="phone"
            type="tel"
            value={data.phone}
            onChange={(e) => onChange("phone", e.target.value)}
            className={getInputClassName("phone")}
            placeholder="+1 (555) 000-0000"
            aria-invalid={hasError("phone")}
            aria-describedby={hasError("phone") ? "phone-error" : undefined}
          />

          <div id="phone-error">{renderError("phone")}</div>
        </div>
      </div>

      <div className="grid gap-5 sm:grid-cols-2">
        <div className="space-y-2">
          <label
            htmlFor="country"
            className="text-[11px] font-bold uppercase tracking-widest text-slate-400"
          >
            Country
          </label>

          <input
            id="country"
            type="text"
            value={data.country ?? ""}
            onChange={(e) => onChange("country", e.target.value)}
            className={getInputClassName("country")}
            placeholder="e.g. Argentina"
            aria-invalid={hasError("country")}
            aria-describedby={hasError("country") ? "country-error" : undefined}
          />

          <div id="country-error">{renderError("country")}</div>
        </div>

        <div className="space-y-2">
          <label
            htmlFor="address"
            className="text-[11px] font-bold uppercase tracking-widest text-slate-400"
          >
            Address
          </label>

          <input
            id="address"
            type="text"
            value={data.address ?? ""}
            onChange={(e) => onChange("address", e.target.value)}
            className={getInputClassName("address")}
            placeholder="e.g. 123 Main St, Apt 4B"
            aria-invalid={hasError("address")}
            aria-describedby={hasError("address") ? "address-error" : undefined}
          />

          <div id="address-error">{renderError("address")}</div>
        </div>
      </div>
    </div>
  );
}

