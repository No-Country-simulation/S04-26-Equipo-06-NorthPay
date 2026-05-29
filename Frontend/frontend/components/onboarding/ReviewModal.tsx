"use client";
import { useState, useEffect } from "react";

interface Props {
  contractorId: string;
  onClose: () => void;
  onUpdate?: () => void;
}

export default function ReviewModal({ contractorId, onClose, onUpdate }: Props) {
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [isApproving, setIsApproving] = useState(false);
  const [isRejecting, setIsRejecting] = useState(false);
  const [notes, setNotes] = useState("");

  useEffect(() => {
    const fetchDetail = async () => {
      try {
        const token = document.cookie
          .split("; ")
          .find((row) => row.startsWith("returnedToken="))
          ?.split("=")[1];
        const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
        const res = await fetch(`${API_URL}/api/v1/onboarding/admin/${contractorId}`, {
          headers: {
            Authorization: `Bearer ${token ? decodeURIComponent(token) : ""}`,
          },
        });
        if (res.ok) {
          const detailData = await res.json();
          setData(detailData);
        } else {
          setError("Failed to load onboarding details.");
        }
      } catch (err) {
        setError("Error connecting to the server.");
      } finally {
        setLoading(false);
      }
    };
    fetchDetail();
  }, [contractorId]);

  const handleApprove = async () => {
    if (!window.confirm("Are you sure you want to approve this onboarding?")) return;
    setIsApproving(true);
    try {
      const token = document.cookie
        .split("; ")
        .find((row) => row.startsWith("returnedToken="))
        ?.split("=")[1];
      const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
      const res = await fetch(`${API_URL}/api/v1/onboarding/${contractorId}/approve`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token ? decodeURIComponent(token) : ""}`,
        },
        body: JSON.stringify({
          onboardingStatus: "APPROVED",
          reason: notes || "Approved by operator"
        })
      });
      if (res.ok) {
        onUpdate?.();
        onClose();
      } else {
        alert("Failed to approve onboarding");
      }
    } catch (e) {
      console.error(e);
      alert("Error approving onboarding");
    } finally {
      setIsApproving(false);
    }
  };

  const handleRequestCorrections = async () => {
    if (!notes) {
      alert("Please provide Operator Notes detailing the required corrections.");
      return;
    }
    setIsRejecting(true);
    try {
      const token = document.cookie
        .split("; ")
        .find((row) => row.startsWith("returnedToken="))
        ?.split("=")[1];
      const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
      const res = await fetch(`${API_URL}/api/v1/onboarding/${contractorId}/changeRequested`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token ? decodeURIComponent(token) : ""}`,
        },
        body: JSON.stringify({
          onboardingStatus: "CHANGES_REQUESTED",
          reason: notes,
          steps: [1] // Defaulting to step 1 for now, ideally this would be selected
        })
      });
      if (res.ok) {
        onUpdate?.();
        onClose();
      } else {
        alert("Failed to request corrections");
      }
    } catch (e) {
      console.error(e);
      alert("Error requesting corrections");
    } finally {
      setIsRejecting(false);
    }
  };

  if (loading) {
    return (
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/40 px-6">
        <div className="w-full max-w-sm rounded-3xl bg-white p-6 shadow-2xl text-center">
          <p className="text-slate-500 font-semibold">Loading details...</p>
        </div>
      </div>
    );
  }

  if (error || !data) {
    return (
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/40 px-6">
        <div className="w-full max-w-sm rounded-3xl bg-white p-6 shadow-2xl text-center">
          <p className="text-rose-500 font-semibold">{error}</p>
          <button onClick={onClose} className="mt-4 rounded-2xl bg-slate-100 px-4 py-2 text-sm font-semibold text-slate-700">Close</button>
        </div>
      </div>
    );
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/40 px-6 py-6">
      <div
        role="dialog"
        aria-modal="true"
        aria-labelledby="contractor-review-title"
        className="max-h-full w-full max-w-3xl overflow-y-auto rounded-3xl border border-slate-200 bg-white p-6 shadow-2xl shadow-slate-950/20"
      >
        <header className="flex items-start justify-between gap-4 border-b border-slate-200 pb-5">
          <div>
            <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">
              Contractor Review
            </p>
            <h2 id="contractor-review-title" className="mt-3 text-2xl font-semibold text-slate-900">
              ID: {contractorId}
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
              <h3 className="text-sm font-semibold text-slate-900">Onboarding Progress</h3>
              <p className="mt-1 text-sm text-slate-500">Registration steps completed</p>
            </div>
            <span className="rounded-full bg-emerald-100 px-3 py-1 text-sm font-semibold text-emerald-700">
              {data.currentStep || 5}/5 steps
            </span>
          </div>
          <div className="h-3 overflow-hidden rounded-full bg-slate-200">
            <div className={`h-full rounded-full bg-emerald-500 w-full`} style={{ width: `${((data.currentStep || 5)/5)*100}%` }} />
          </div>
        </section>

        <section className="mt-8 grid gap-5 md:grid-cols-2">
          <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5">
            <h3 className="text-lg font-semibold text-slate-900">Personal Information</h3>
            <dl className="mt-5 space-y-4 text-sm">
              <div>
                <dt className="font-medium text-slate-500">Full Name:</dt>
                <dd className="mt-1 font-semibold text-slate-900">{data.personalInformation?.fullName || 'N/A'}</dd>
              </div>
              <div>
                <dt className="font-medium text-slate-500">Email:</dt>
                <dd className="mt-1 text-slate-900">{data.personalInformation?.email || 'N/A'}</dd>
              </div>
              <div>
                <dt className="font-medium text-slate-500">Phone:</dt>
                <dd className="mt-1 text-slate-900">{data.personalInformation?.phone || 'N/A'}</dd>
              </div>
              <div>
                <dt className="font-medium text-slate-500">Country:</dt>
                <dd className="mt-1 text-slate-900">{data.personalInformation?.country || 'N/A'}</dd>
              </div>
              <div>
                <dt className="font-medium text-slate-500">Address:</dt>
                <dd className="mt-1 text-slate-900">{data.personalInformation?.address || 'N/A'}</dd>
              </div>
            </dl>
          </div>

          <div className="space-y-5">
            <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5">
              <h3 className="text-lg font-semibold text-slate-900">Documentation</h3>
              <dl className="mt-5 space-y-4 text-sm">
                <div>
                  <dt className="font-medium text-slate-500">Document Type:</dt>
                  <dd className="mt-1 font-semibold text-slate-900">{data.documentation?.documentType || 'N/A'}</dd>
                </div>
                <div>
                  <dt className="font-medium text-slate-500">Document Number:</dt>
                  <dd className="mt-1 text-slate-900">{data.documentation?.documentNumber || 'N/A'}</dd>
                </div>
                <div>
                  <dt className="font-medium text-slate-500">Uploaded Files:</dt>
                  <dd className="mt-2 space-y-2">
                    {data.documentation?.urlFiles && data.documentation?.urlFiles.length > 0 ? (
                      data.documentation?.urlFiles.map((file: any, i: number) => (
                        <a href={file.fileUrl} target="_blank" rel="noreferrer" key={i} className="block rounded-2xl bg-white px-3 py-2 text-slate-700 hover:bg-slate-100 transition truncate">
                          {file.fileType || `Document ${i+1}`}
                        </a>
                      ))
                    ) : (
                      <span className="text-slate-500 italic">No files uploaded</span>
                    )}
                  </dd>
                </div>
              </dl>
            </div>

            <div className="rounded-3xl border border-slate-200 bg-slate-50 p-5">
              <h3 className="text-lg font-semibold text-slate-900">Payment Information</h3>
              <dl className="mt-5 space-y-4 text-sm">
                <div>
                  <dt className="font-medium text-slate-500">Payment Method:</dt>
                  <dd className="mt-1 font-semibold text-slate-900">{data.paymentInformation?.platform || data.paymentInformation?.network || 'N/A'}</dd>
                </div>
                <div>
                  <dt className="font-medium text-slate-500">Account:</dt>
                  <dd className="mt-1 break-all text-slate-900">{data.paymentInformation?.account || 'N/A'}</dd>
                </div>
                <div>
                  <dt className="font-medium text-slate-500">Contract Signed:</dt>
                  <dd className={`mt-1 font-semibold ${data.paymentInformation?.contractSigned ? "text-emerald-700" : "text-rose-600"}`}>
                    {data.paymentInformation?.contractSigned ? "Yes" : "No"}
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        </section>

        <section className="mt-6 rounded-3xl border border-slate-200 bg-white p-5">
          <label htmlFor="operator-notes" className="text-lg font-semibold text-slate-900">
            Operator Notes
          </label>
          <textarea
            id="operator-notes"
            rows={4}
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            placeholder="Add notes about this contractor's application... (Required for requesting corrections)"
            className="mt-4 w-full resize-none rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 outline-none transition focus:border-sky-500 focus:ring-2 focus:ring-sky-100"
          />

          <div className="mt-5 grid gap-3 sm:grid-cols-2">
            <button
              type="button"
              onClick={handleApprove}
              disabled={isApproving || data.status !== "PENDING_VERIFICATION"}
              className="rounded-2xl bg-emerald-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-emerald-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isApproving ? "Approving..." : "Approve Application"}
            </button>
            <button
              type="button"
              onClick={handleRequestCorrections}
              disabled={isRejecting || data.status !== "PENDING_VERIFICATION"}
              className="rounded-2xl bg-rose-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-rose-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isRejecting ? "Requesting..." : "Request Corrections"}
            </button>
          </div>
        </section>
      </div>
    </div>
  );
}
