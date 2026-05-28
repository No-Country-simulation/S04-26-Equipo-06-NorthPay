interface Props {
  onClose: () => void;
}

export default function ReviewModal({ onClose }: Props) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/40 px-6">
      <div
        role="dialog"
        aria-modal="true"
        aria-labelledby="contractor-review-title"
        className="max-h-[90vh] w-full max-w-3xl overflow-y-auto rounded-3xl border border-slate-200 bg-white p-6 shadow-2xl shadow-slate-950/20"
      >
        <header className="flex items-start justify-between gap-4 border-b border-slate-200 pb-5">
  <div>
    <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">
      Contractor Review
    </p>

    <h2
      id="contractor-review-title"
      className="mt-3 text-2xl font-semibold text-slate-900"
    >
      ID: inv-demo-001
    </h2>
  </div>

  <button
    type="button"
    onClick={onClose}
    className="rounded-full border border-slate-200 bg-white px-3 py-1 text-sm font-semibold text-slate-500 transition hover:bg-slate-100 hover:text-slate-900"
    aria-label="Close review modal"
  >
    ×
  </button>
</header>

        <section className="mt-6 space-y-4">
          <div className="flex items-center justify-between gap-4">
            <div>
              <h3 className="text-sm font-semibold text-slate-900">
                Onboarding Progress
              </h3>
              <p className="mt-1 text-sm text-slate-500">
                Registration steps completed
              </p>
            </div>

            <span className="rounded-full bg-emerald-100 px-3 py-1 text-sm font-semibold text-emerald-700">
              5/5 steps
            </span>
          </div>

          <div className="h-3 overflow-hidden rounded-full bg-slate-200">
            <div className="h-full w-full rounded-full bg-emerald-500" />
          </div>
        </section>

        <section className="mt-8 grid gap-5 md:grid-cols-2">
          <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5">
            <h3 className="text-lg font-semibold text-slate-900">
              Personal Information
            </h3>

            <dl className="mt-5 space-y-4 text-sm">
              <div>
                <dt className="font-medium text-slate-500">Full Name:</dt>
                <dd className="mt-1 font-semibold text-slate-900">
                  Maria Garcia
                </dd>
              </div>

              <div>
                <dt className="font-medium text-slate-500">Email:</dt>
                <dd className="mt-1 text-slate-900">
                  maria.garcia@example.com
                </dd>
              </div>

              <div>
                <dt className="font-medium text-slate-500">Phone:</dt>
                <dd className="mt-1 text-slate-900">
                  +34 612 345 678
                </dd>
              </div>

              <div>
                <dt className="font-medium text-slate-500">Country:</dt>
                <dd className="mt-1 text-slate-900">
                  Spain
                </dd>
              </div>

              <div>
                <dt className="font-medium text-slate-500">Address:</dt>
                <dd className="mt-1 text-slate-900">
                  Calle Mayor 15, Madrid
                </dd>
              </div>
            </dl>
          </div>

          <div className="space-y-5">
            <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5">
              <h3 className="text-lg font-semibold text-slate-900">
                Documentation
              </h3>

              <dl className="mt-5 space-y-4 text-sm">
                <div>
                  <dt className="font-medium text-slate-500">
                    Document Type:
                  </dt>
                  <dd className="mt-1 font-semibold text-slate-900">
                    DNI
                  </dd>
                </div>

                <div>
                  <dt className="font-medium text-slate-500">
                    Document Number:
                  </dt>
                  <dd className="mt-1 text-slate-900">
                    12345678A
                  </dd>
                </div>

                <div>
                  <dt className="font-medium text-slate-500">
                    Uploaded Files:
                  </dt>
                  <dd className="mt-2 space-y-2">
                    <span className="block rounded-2xl bg-white px-3 py-2 text-slate-700">
                      dni_front.jpg
                    </span>
                    <span className="block rounded-2xl bg-white px-3 py-2 text-slate-700">
                      dni_back.jpg
                    </span>
                    <span className="block rounded-2xl bg-white px-3 py-2 text-slate-700">
                      proof_address.pdf
                    </span>
                  </dd>
                </div>
              </dl>
            </div>

            <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5">
              <h3 className="text-lg font-semibold text-slate-900">
                Payment Information
              </h3>

              <dl className="mt-5 space-y-4 text-sm">
                <div>
                  <dt className="font-medium text-slate-500">
                    Payment Method:
                  </dt>
                  <dd className="mt-1 font-semibold text-slate-900">
                    Bank Transfer
                  </dd>
                </div>

                <div>
                  <dt className="font-medium text-slate-500">
                    Account:
                  </dt>
                  <dd className="mt-1 break-all text-slate-900">
                    ES91 2100 0418 4502 0005 1332
                  </dd>
                </div>

                <div>
                  <dt className="font-medium text-slate-500">
                    Contract Signed:
                  </dt>
                  <dd className="mt-1 font-semibold text-emerald-700">
                    Yes
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        </section>

                <section className="mt-6 rounded-3xl border border-slate-200 bg-white p-5">
          <label
            htmlFor="operator-notes"
            className="text-lg font-semibold text-slate-900"
          >
            Operator Notes
          </label>

          <textarea
            id="operator-notes"
            rows={4}
            placeholder="Add notes about this contractor's application..."
            className="mt-4 w-full resize-none rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
          />

          <div className="mt-5 grid gap-3 sm:grid-cols-2">
            <button
              type="button"
              className="rounded-2xl bg-emerald-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-emerald-700"
            >
              Approve Application
            </button>

            <button
              type="button"
              className="rounded-2xl bg-rose-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-rose-700"
            >
              Request Corrections
            </button>
          </div>
        </section>
      </div>
    </div>
  );
}