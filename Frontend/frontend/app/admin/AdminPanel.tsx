"use client";

import { useMemo, useState } from "react";
import Link from "next/link";

type Contractor = {
  id: string;
  name: string;
  email: string;
  stage: string;
  status: "pending" | "needs-correction" | "approved";
  lastUpdate: string;
};

const initialContractors: Contractor[] = [
  {
    id: "C-001",
    name: "Maria Perez",
    email: "maria.perez@example.com",
    stage: "Document upload",
    status: "pending",
    lastUpdate: "1h ago",
  },
  {
    id: "C-002",
    name: "Jorge Gomez",
    email: "jorge.gomez@example.com",
    stage: "Contract signing",
    status: "needs-correction",
    lastUpdate: "2h ago",
  },
  {
    id: "C-003",
    name: "Lucia Fernandez",
    email: "lucia.fernandez@example.com",
    stage: "Payment method",
    status: "approved",
    lastUpdate: "4h ago",
  },
];

const statusStyles: Record<string, string> = {
  pending: "bg-amber-100 text-amber-700",
  "needs-correction": "bg-rose-100 text-rose-700",
  approved: "bg-emerald-100 text-emerald-700",
};

export default function AdminPanel() {
  const [contractors, setContractors] = useState(initialContractors);
  const [notification, setNotification] = useState<string>("");

  // Invite functionality state
  const [inviteEmail, setInviteEmail] = useState("");
  const [isInviting, setIsInviting] = useState(false);
  const [inviteResult, setInviteResult] = useState<string | null>(null);

  const handleInvite = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!inviteEmail) return;

    setIsInviting(true);
    setInviteResult(null);

    const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

    try {
      const token = document.cookie
        .split("; ")
        .find((row) => row.startsWith("returnedToken="))
        ?.split("=")[1];

      const response = await fetch(
        `${API_URL}/api/v1/onboarding/createOnboarding?destinedContractorEmail=${encodeURIComponent(inviteEmail)}`,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token ? decodeURIComponent(token) : ""}`,
          },
        },
      );

      if (response.status === 401 || response.status === 403) {
        document.cookie = 'returnedToken=; Max-Age=0; path=/';
        window.location.href = '/login';
        return;
      }

      if (!response.ok) {
        throw new Error("Failed to create onboarding and send invitation.");
      }

      // Fetch all tokens to find the one we just created (since createOnboarding doesn't return it)
      try {
        const tokensResponse = await fetch(`${API_URL}/api/v1/invitation-token`, {
          headers: { Authorization: `Bearer ${token ? decodeURIComponent(token) : ""}` }
        });
        if (tokensResponse.ok) {
          const tokens = await tokensResponse.json();
          // Find the newest token for this email
          const newestToken = tokens
            .filter((t: any) => t.contractorEmail === inviteEmail)
            .sort((a: any, b: any) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())[0];
          
          if (newestToken) {
            const inviteUrl = `http://localhost:3000/invite/${newestToken.tokenUrl}`;
            setInviteResult(
              `Invitation created! Testing link: ${inviteUrl}`
            );
          } else {
            setInviteResult("Invitation created! The contractor will receive an email with their access link.");
          }
        } else {
          setInviteResult("Invitation created! The contractor will receive an email with their access link.");
        }
      } catch (err) {
        console.error("Failed to fetch tokens for logging", err);
        setInviteResult("Invitation created! The contractor will receive an email with their access link.");
      }

      setInviteEmail("");
    } catch (error) {
      setInviteResult(
        `Error: ${error instanceof Error ? error.message : "Something went wrong"}`,
      );
    } finally {
      setIsInviting(false);
    }
  };

  const activeCount = useMemo(
    () =>
      contractors.filter((contractor) => contractor.status !== "approved")
        .length,
    [contractors],
  );

  const updateStatus = (id: string, status: Contractor["status"]) => {
    setContractors((current) =>
      current.map((contractor) =>
        contractor.id === id
          ? { ...contractor, status, lastUpdate: "Now" }
          : contractor,
      ),
    );
    setNotification(`Status updated to ${status.replace("-", " ")} for ${id}.`);
    setTimeout(() => setNotification(""), 3500);
  };

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-6 sm:px-10">
      <div className="mx-auto max-w-6xl space-y-6">
        <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-lg shadow-slate-200/30">
          <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <p className="text-sm font-semibold uppercase tracking-[0.24em] text-slate-500">
                Operations
              </p>
              <h1 className="mt-3 text-3xl font-semibold text-slate-900">
                Admin Panel
              </h1>
              <p className="mt-2 text-slate-600">
                Monitor the status of each onboarding and send automatic
                notifications to the contractor.
              </p>
            </div>
            <div className="flex gap-4">
              <Link
                href="/onboarding"
                className="inline-flex items-center rounded-3xl bg-slate-100 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-200"
              >
                View onboarding
              </Link>
              <button
                onClick={() => {
                  document.cookie = 'returnedToken=; Max-Age=0; path=/';
                  window.location.href = '/login';
                }}
                className="inline-flex items-center rounded-3xl bg-rose-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-rose-700"
              >
                Logout
              </button>
            </div>
          </div>
          {/* --- OPERATIONAL METRICS DASHBOARD (Mocked) --- */}
          <div className="mt-8">
            <h2 className="text-lg font-semibold text-slate-900 mb-4">
              Operational Metrics
            </h2>
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
              {/* Metric 1: Total Volume */}
              <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-sky-100 text-sky-600">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M15 19.128a9.38 9.38 0 002.625.372 9.337 9.337 0 004.121-.952 4.125 4.125 0 00-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 018.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0111.964-3.07M12 6.375a3.375 3.375 0 11-6.75 0 3.375 3.375 0 016.75 0zm8.25 2.25a2.625 2.625 0 11-5.25 0 2.625 2.625 0 015.25 0z" />
                    </svg>
                  </div>
                  <p className="text-sm font-medium text-slate-500">Total Onboardings</p>
                </div>
                <div className="mt-4 flex items-baseline gap-2">
                  <span className="text-3xl font-bold text-slate-900">142</span>
                  <span className="text-xs font-medium text-emerald-600">+12% this week</span>
                </div>
              </div>

              {/* Metric 2: Average Activation Time */}
              <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-indigo-100 text-indigo-600">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                  </div>
                  <p className="text-sm font-medium text-slate-500">Avg. Activation Time</p>
                </div>
                <div className="mt-4 flex items-baseline gap-2">
                  <span className="text-3xl font-bold text-slate-900">2.4</span>
                  <span className="text-sm font-medium text-slate-600">days</span>
                </div>
              </div>

              {/* Metric 3: Onboardings by Status */}
              <div className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
                <div className="flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-amber-100 text-amber-600">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
                      <path strokeLinecap="round" strokeLinejoin="round" d="M10.5 6a7.5 7.5 0 107.5 7.5h-7.5V6z" />
                      <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 10.5H21A7.5 7.5 0 0013.5 3v7.5z" />
                    </svg>
                  </div>
                  <p className="text-sm font-medium text-slate-500">Status Distribution</p>
                </div>
                <div className="mt-4 flex flex-col gap-2">
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-slate-600">Pending</span>
                    <span className="font-semibold text-slate-900">45</span>
                  </div>
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-slate-600">Needs Correction</span>
                    <span className="font-semibold text-slate-900">12</span>
                  </div>
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-slate-600">Approved</span>
                    <span className="font-semibold text-slate-900">85</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* --- INVITE NEW CONTRACTOR SECTION --- */}
        <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
          <h2 className="text-xl font-semibold text-slate-900">
            Invite New Contractor
          </h2>
          <p className="mt-1 text-sm text-slate-500">
            Send an onboarding invitation link directly to their email.
          </p>

          <form
            onSubmit={handleInvite}
            className="mt-6 flex flex-col gap-4 sm:flex-row sm:items-end"
          >
            <div className="flex-1">
              <label
                htmlFor="invite-email"
                className="block text-sm font-medium text-slate-700"
              >
                Contractor Email
              </label>
              <input
                id="invite-email"
                type="email"
                required
                value={inviteEmail}
                onChange={(e) => setInviteEmail(e.target.value)}
                placeholder="contractor@example.com"
                className="mt-2 block w-full rounded-2xl border border-slate-300 px-4 py-3 text-sm focus:border-sky-500 focus:outline-none focus:ring-1 focus:ring-sky-500"
                disabled={isInviting}
              />
            </div>
            <button
              type="submit"
              disabled={isInviting || !inviteEmail}
              className="rounded-2xl bg-slate-900 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-800 disabled:opacity-50 sm:w-auto"
            >
              {isInviting ? "Generating..." : "Generate Invitation"}
            </button>
          </form>

          {inviteResult && (
            <div
              className={`mt-6 rounded-2xl p-4 text-sm ${inviteResult.includes("Error") ? "bg-rose-50 text-rose-700 border border-rose-200" : "bg-emerald-50 text-emerald-800 border border-emerald-200"}`}
            >
              {inviteResult.includes("Error") ? (
                <p>{inviteResult}</p>
              ) : (
                <div className="flex flex-col gap-2">
                  <p className="font-semibold">{inviteResult}</p>
                </div>
              )}
            </div>
          )}
        </div>
        {/* ------------------------------------- */}

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
                <th className="px-6 py-4">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200 bg-white">
              {contractors.map((contractor) => (
                <tr key={contractor.id} className="hover:bg-slate-50">
                  <td className="px-6 py-5 font-semibold text-slate-900">
                    {contractor.id}
                  </td>
                  <td className="px-6 py-5">
                    <p className="font-semibold text-slate-900">
                      {contractor.name}
                    </p>
                    <p className="text-slate-500">{contractor.email}</p>
                  </td>
                  <td className="px-6 py-5 text-slate-600">
                    {contractor.stage}
                  </td>
                  <td className="px-6 py-5">
                    <span
                      className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${statusStyles[contractor.status]}`}
                    >
                      {contractor.status.replace("-", " ")}
                    </span>
                  </td>
                  <td className="px-6 py-5 text-slate-500">
                    {contractor.lastUpdate}
                  </td>
                  <td className="px-6 py-5 space-x-2">
                    <button
                      type="button"
                      onClick={() => updateStatus(contractor.id, "approved")}
                      className="rounded-2xl bg-emerald-600 px-4 py-2 text-xs font-semibold text-white transition hover:bg-emerald-700"
                    >
                      Approve
                    </button>
                    <button
                      type="button"
                      onClick={() =>
                        updateStatus(contractor.id, "needs-correction")
                      }
                      className="rounded-2xl bg-rose-600 px-4 py-2 text-xs font-semibold text-white transition hover:bg-rose-700"
                    >
                      Correct
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
