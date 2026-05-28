"use client";

import ReviewModal from "@/components/onboarding/ReviewModal";
import { useMemo, useState } from "react";
import Link from "next/link";

type Contractor = {
  id: string;
  name: string;
  email: string;
  stage: string;
  status: "in-progress" | "pending-verification" | "needs-correction" | "approved";
  startDate: string;
  lastUpdate: string;
};

const initialContractors: Contractor[] = [
  { id: "C-001", name: "Maria Perez", email: "maria.perez@example.com", stage: "Document upload", status: "in-progress", lastUpdate: "1h ago", startDate: "2026-05-20" },
  { id: "C-002", name: "Jorge Gomez", email: "jorge.gomez@example.com", stage: "Contract signing", status: "needs-correction", lastUpdate: "2h ago", startDate: "2026-05-20" },
  { id: "C-003", name: "Lucia Fernandez", email: "lucia.fernandez@example.com", stage: "Payment method", status: "approved", lastUpdate: "4h ago", startDate: "2026-05-20" },
   { id: "C-004", name: "Carlos Rodriguez", email: "Carlos.Rodriguez@example.com", stage: "Payment method", status: "pending-verification", lastUpdate: "4h ago", startDate: "2026-05-20" }
];

const statusStyles: Record<Contractor["status"], string> = {
  "in-progress": "bg-sky-100 text-sky-700",
  "pending-verification": "bg-amber-100 text-amber-700",
  "needs-correction": "bg-rose-100 text-rose-700",
  approved: "bg-emerald-100 text-emerald-700",
};

const statusLabels: Record<Contractor["status"], string> = {
  "in-progress": "In progress",
  "pending-verification": "Pending verification",
  "needs-correction": "Requires correction",
  approved: "Approved",
};

export default function AdminPanel() {
  const [contractors, setContractors] = useState(initialContractors);
  const [notification, setNotification] = useState<string>("");
  const [selectedContractor, setSelectedContractor] = useState<Contractor | null>(null);

  const activeCount = useMemo(
  () => contractors.length,
  [contractors]
);

  const updateStatus = (id: string, status: Contractor["status"]) => {
    setContractors((current) =>
      current.map((contractor) =>
        contractor.id === id ? { ...contractor, status, lastUpdate: "Now" } : contractor
      )
    );
    setNotification(`Status updated to ${status.replace("-", " ")} for ${id}.`);
    setTimeout(() => setNotification(""), 3500);
  };


  const openReviewModal = (contractor: Contractor) => {
  setSelectedContractor(contractor);
};

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-6 sm:px-10">
      <div className="mx-auto max-w-6xl space-y-6">
        <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-lg shadow-slate-200/30">
          <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">Operations</p>
              <h1 className="mt-3 text-3xl font-semibold text-slate-900">Admin Panel</h1>
              <p className="mt-2 text-slate-600">Monitor the status of each onboarding and send automatic notifications to the contractor.</p>
            </div>
            <Link href="/onboarding" className="inline-flex items-center rounded-3xl bg-sky-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-sky-700">
              View onboarding
            </Link>
          </div>
          <div className="mt-6 rounded-3xl bg-slate-50 p-5 text-sm text-slate-700">
            <p>
              Active contractors: <span className="font-semibold text-slate-900">{activeCount}</span>
            </p>
            <p className="mt-1">Each status change is reflected immediately and notifies the contractor.</p>
          </div>
        </div>

        {notification && (
          <div className="rounded-3xl border border-sky-200 bg-sky-50 px-6 py-4 text-slate-900 shadow-sm">
            {notification}
          </div>
        )}

        <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
          <table className="min-w-full divide-y divide-slate-200 text-sm">
            <thead className="bg-slate-50 text-left text-[0.86rem] uppercase tracking-[0.18em] text-slate-500">
              <tr>
                <th className="px-6 py-4">ID</th>
                <th className="px-6 py-4">Contractor</th>
                <th className="px-6 py-4">Stage</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Last Update</th>
                <th className="w-[320px] px-6 py-4">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200 bg-white">
              {contractors.map((contractor) => (
                <tr key={contractor.id} className="hover:bg-slate-50">
                  <td className="px-6 py-5 font-semibold text-slate-900">{contractor.id}</td>
                  <td className="px-6 py-5">
                    <p className="font-semibold text-slate-900">{contractor.name}</p>
                    <p className="text-slate-500">{contractor.email}</p>
                  </td>
                  <td className="px-6 py-5 text-slate-600">{contractor.stage}</td>
                  <td className="px-6 py-5">
                  <span className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${statusStyles[contractor.status]}`}>
                      {statusLabels[contractor.status]}
                  </span>
                  </td>
                  <td className="px-6 py-5 text-slate-500">{contractor.lastUpdate}</td>
               <td className="px-6 py-5">
  <div className="flex flex-wrap items-center gap-2">
    <button
    type="button"
    onClick={() => openReviewModal(contractor)}
    className="w-full rounded-2xl border border-slate-300 bg-white px-4 py-2 text-xs font-semibold text-slate-700 shadow-sm transition hover:border-sky-300 hover:bg-sky-50 hover:text-sky-700"
  >
    Review
  </button>



{/* 
    <button
      type="button"
      onClick={() => updateStatus(contractor.id, "approved")}
      className="rounded-2xl bg-emerald-600 px-4 py-2 text-xs font-semibold text-white transition hover:bg-emerald-700"
    >
      Approve
    </button>

    <button
      type="button"
      onClick={() => updateStatus(contractor.id, "needs-correction")}
      className="rounded-2xl bg-rose-600 px-4 py-2 text-xs font-semibold text-white transition hover:bg-rose-700"
    >
      Correct
    </button> */}
  </div>
</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      {selectedContractor && (
  <ReviewModal
    onClose={() => setSelectedContractor(null)}
  />
)}
    </div>
  );
}
