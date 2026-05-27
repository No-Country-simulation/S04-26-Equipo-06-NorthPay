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
            <Link
              href="/onboarding"
              className="inline-flex items-center rounded-3xl bg-sky-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-sky-700"
            >
              View onboarding
            </Link>
          </div>
          <div className="mt-6 rounded-3xl bg-slate-50 p-5 text-sm text-slate-700">
            <p>
              Active contractors:{" "}
              <span className="font-semibold text-slate-900">
                {activeCount}
              </span>
            </p>
            <p className="mt-1">
              Each status change is reflected immediately and notifies the
              contractor.
            </p>
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
