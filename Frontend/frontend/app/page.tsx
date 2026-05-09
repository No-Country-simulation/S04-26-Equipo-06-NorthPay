import Link from "next/link";

export default function Home() {
  return (
    <div className="min-h-screen bg-slate-50 py-16 px-6 text-slate-900 sm:px-10">
      <div className="mx-auto max-w-6xl rounded-[2rem] border border-slate-200 bg-white p-10 shadow-xl shadow-slate-200/30">
        <div className="grid gap-12 lg:grid-cols-[1.2fr_0.8fr] lg:items-center">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.24em] text-sky-600">NorthPay</p>
            <h1 className="mt-4 text-4xl font-semibold tracking-tight text-slate-900 sm:text-5xl">
              Onboarding Portal for Remote Contractors
            </h1>
            <p className="mt-6 max-w-2xl text-lg leading-8 text-slate-600">
              Centralize activations, reduce onboarding times, and provide operational visibility in a single portal.
              Contractors complete a 5-step flow, and the operations team monitors the status in real time.
            </p>

            <div className="mt-10 flex flex-col gap-4 sm:flex-row">
              <Link href="/onboarding" className="inline-flex items-center justify-center rounded-3xl bg-sky-600 px-6 py-4 text-sm font-semibold text-white shadow-lg shadow-sky-600/20 transition hover:bg-sky-700">
                Start onboarding
              </Link>
              <Link href="/admin" className="inline-flex items-center justify-center rounded-3xl border border-slate-300 bg-white px-6 py-4 text-sm font-semibold text-slate-900 transition hover:border-slate-400 hover:bg-slate-50">
                Operations Panel
              </Link>
            </div>
          </div>

          <div className="rounded-[2rem] bg-slate-950 p-8 text-white shadow-2xl shadow-slate-950/20">
            <div className="space-y-6">
              <div className="rounded-[1.5rem] bg-slate-900 p-6">
                <p className="text-sm uppercase tracking-[0.24em] text-sky-400">Expected Results</p>
                <ul className="mt-5 space-y-4 text-sm text-slate-300">
                  <li>• Activation in less than 3 days</li>
                  <li>• 5-step onboarding flow</li>
                  <li>• Status change notifications</li>
                  <li>• Operational visibility panel</li>
                </ul>
              </div>
              <div className="rounded-[1.5rem] bg-slate-900 p-6">
                <p className="text-sm uppercase tracking-[0.24em] text-sky-400">Users</p>
                <p className="mt-4 text-sm text-slate-300">Remote contractors</p>
                <p className="text-sm text-slate-300">NorthPay internal operators</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
