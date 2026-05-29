"use client";

import ReviewModal from "@/components/onboarding/ReviewModal";
import { useMemo, useState, useEffect } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import Link from "next/link";

type Contractor = {
  id: string;
  name: string;
  email: string;
  stage: string;
  status: string;
  startDate: string;
  lastUpdate: string;
  rawDate: number;
};

type MetricsDTO = {
  totalOnboardings: number;
  approvedOnboardings: number;
  changesRequestedOnboardings: number;
  notStartedOnboardings: number;
  averageSecondsActivationTimeOfTokens: number;
};

const statusStyles: Record<string, string> = {
  PENDING: "bg-amber-100 text-amber-700",
  PENDING_VERIFICATION: "bg-orange-100 text-orange-700",
  CHANGES_REQUESTED: "bg-rose-100 text-rose-700",
  APPROVED: "bg-emerald-100 text-emerald-700",
  INVITED: "bg-slate-100 text-slate-700",
};

export default function AdminPanel() {
  const [contractors, setContractors] = useState<Contractor[]>([]);
  const [metrics, setMetrics] = useState<MetricsDTO | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;
  const [isRealTimeFailed, setIsRealTimeFailed] = useState(false);
  const [notification, setNotification] = useState<string>("");
  const [selectedContractor, setSelectedContractor] = useState<Contractor | null>(null);

  const [inviteEmail, setInviteEmail] = useState("");
  const [isInviting, setIsInviting] = useState(false);
  const [inviteResult, setInviteResult] = useState<React.ReactNode | null>(null);

  const fetchMetrics = async () => {
    try {
      const token = document.cookie
        .split("; ")
        .find((row) => row.startsWith("returnedToken="))
        ?.split("=")[1];
      const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
      const res = await fetch(`${API_URL}/metrics`, {
        headers: {
          Authorization: `Bearer ${token ? decodeURIComponent(token) : ""}`,
        },
      });
      if (res.ok) {
        const data = await res.json();
        setMetrics(data);
      }
    } catch (e) {
      console.error("Failed to fetch metrics", e);
    }
  };

  const fetchContractors = async () => {
    try {
      const token = document.cookie
        .split("; ")
        .find((row) => row.startsWith("returnedToken="))
        ?.split("=")[1];

      const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";
      const res = await fetch(`${API_URL}/api/v1/onboarding/admin/list?size=1000&page=0`, {
        headers: {
          Authorization: `Bearer ${token ? decodeURIComponent(token) : ""}`,
        },
      });
      if (res.ok) {
        const data = await res.json();
        const mapped = data.content.map((item: any) => ({
          id: item.id,
          name: item.applicantName || "N/A",
          email: item.applicantEmail || "N/A",
          stage: `Step ${item.currentStep || 1}`,
          status: item.status || "PENDING",
          startDate: new Date(item.createdAt || Date.now()).toLocaleDateString(),
          lastUpdate: new Date(item.updatedAt || item.createdAt || Date.now()).toLocaleString(),
          rawDate: new Date(item.createdAt || Date.now()).getTime(),
        }));
        setContractors(mapped);
      }
    } catch (e) {
      console.error("Failed to fetch contractors", e);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchContractors();
    fetchMetrics();
    
    // Fallback polling
    const interval = setInterval(fetchContractors, 30000);

    let stompClient: Client;
    const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

    try {
      const socket = new SockJS(`${API_URL}/ws`);
      stompClient = new Client({
        webSocketFactory: () => socket as any,
        onConnect: () => {
          setIsRealTimeFailed(false);
          stompClient?.subscribe('/topic/onboarding-status', () => {
            fetchContractors();
            fetchMetrics();
          });
          stompClient?.subscribe('/topic/metrics', (message) => {
            const newMetrics = JSON.parse(message.body);
            setMetrics(newMetrics);
          });
        },
        onStompError: () => setIsRealTimeFailed(true),
        onWebSocketError: () => setIsRealTimeFailed(true)
      });
      stompClient.activate();
    } catch (e) {
      setIsRealTimeFailed(true);
    }

    return () => {
      clearInterval(interval);
      if (stompClient) stompClient.deactivate();
    };
  }, []);

  // Reset to page 1 when filters change
  useEffect(() => {
    setCurrentPage(1);
  }, [search, statusFilter]);

  const filteredContractors = useMemo(() => {
    let filtered = contractors.filter(c => {
      const matchesSearch = c.name?.toLowerCase().includes(search.toLowerCase()) || 
                            c.email?.toLowerCase().includes(search.toLowerCase());
      const matchesStatus = statusFilter === "ALL" || c.status === statusFilter;
      return matchesSearch && matchesStatus;
    });
    return filtered.sort((a, b) => b.rawDate - a.rawDate);
  }, [contractors, search, statusFilter]);

  const totalPages = Math.ceil(filteredContractors.length / itemsPerPage);
  const paginatedContractors = useMemo(() => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    return filteredContractors.slice(startIndex, startIndex + itemsPerPage);
  }, [filteredContractors, currentPage]);

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

      // Fetch all tokens to find the one we just created
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
              <span>Invitation created!{/*  Testing link: <a href={inviteUrl} target="_blank" rel="noopener noreferrer" className="text-sky-600 underline">{inviteUrl}</a> */}</span>
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
      setInviteResult(`Error: ${error instanceof Error ? error.message : "Something went wrong"}`);
    } finally {
      setIsInviting(false);
    }
  };

  const handleLogout = () => {
    document.cookie = 'returnedToken=; Max-Age=0; path=/';
    window.location.href = '/login';
  };

  const formatAvgTime = (seconds: number) => {
    if (!seconds) return { value: "0", unit: "days" };
    const days = Math.floor(seconds / 86400);
    const hours = Math.floor((seconds % 86400) / 3600);
    if (days > 0) return { value: days.toString(), unit: days > 1 ? 'days' : 'day' };
    if (hours > 0) return { value: hours.toString(), unit: hours > 1 ? 'hrs' : 'hr' };
    return { value: Math.floor(seconds / 60).toString(), unit: "mins" };
  };

  const avgTime = formatAvgTime(metrics?.averageSecondsActivationTimeOfTokens || 0);

  const openReviewModal = (contractor: Contractor) => {
    setSelectedContractor(contractor);
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
                Monitor the status of each onboarding and send automatic notifications to the contractor.
              </p>
            </div>
            <div className="flex gap-4">
              <button
                onClick={handleLogout}
                className="inline-flex items-center rounded-3xl bg-rose-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-rose-700"
              >
                Logout
              </button>
            </div>
          </div>
          
          <div className="mt-8">
            <h2 className="text-lg font-semibold text-slate-900 mb-4">
              Operational Metrics
            </h2>
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
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
                  <span className="text-3xl font-bold text-slate-900">{metrics?.totalOnboardings || 0}</span>
                </div>
              </div>

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
                  <span className="text-3xl font-bold text-slate-900">{avgTime.value}</span>
                  <span className="text-sm font-medium text-slate-600">{avgTime.unit}</span>
                </div>
              </div>

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
                    <span className="text-slate-600">Not Started</span>
                    <span className="font-semibold text-slate-900">{metrics?.notStartedOnboardings || 0}</span>
                  </div>
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-slate-600">Needs Correction</span>
                    <span className="font-semibold text-slate-900">{metrics?.changesRequestedOnboardings || 0}</span>
                  </div>
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-slate-600">Approved</span>
                    <span className="font-semibold text-emerald-600">{metrics?.approvedOnboardings || 0}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

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
              className={`mt-6 rounded-2xl p-4 text-sm ${typeof inviteResult === "string" && inviteResult.includes("Error") ? "bg-rose-50 text-rose-700 border border-rose-200" : "bg-emerald-50 text-emerald-800 border border-emerald-200"}`}
            >
              {typeof inviteResult === "string" && inviteResult.includes("Error") ? (
                <p>{inviteResult}</p>
              ) : (
                <div className="flex flex-col gap-2">
                  <div className="font-semibold">{inviteResult}</div>
                </div>
              )}
            </div>
          )}
        </div>

        {notification && (
          <div className="rounded-3xl border border-sky-200 bg-sky-50 px-6 py-4 text-slate-900 shadow-sm">
            {notification}
          </div>
        )}

        
        <div className="flex flex-col sm:flex-row gap-4 mb-6">
          <input 
            type="text" 
            placeholder="Search by name or email..." 
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="flex-1 rounded-2xl border border-slate-200 px-4 py-2 text-sm focus:border-sky-500 focus:outline-none focus:ring-1 focus:ring-sky-500"
          />
          <select 
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="rounded-2xl border border-slate-200 px-4 py-2 text-sm focus:border-sky-500 focus:outline-none focus:ring-1 focus:ring-sky-500"
          >
            <option value="ALL">All Statuses</option>
            <option value="INVITED">Invited</option>
            <option value="PERSONAL_DATA_COMPLETED">Personal Data</option>
            <option value="DOCUMENTS_UPLOADED">Documents</option>
            <option value="CONTRACT_SIGNED">Contract Signed</option>
            <option value="PAYMENT_CONFIGURED">Payment Configured</option>
            <option value="PENDING_VERIFICATION">Pending Verification</option>
            <option value="CHANGES_REQUESTED">Changes Requested</option>
            <option value="APPROVED">Approved</option>
          </select>
        </div>

        <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
          <table className="min-w-full divide-y divide-slate-200 text-sm">
            <thead className="bg-slate-50 text-left text-[0.86rem] uppercase tracking-[0.18em] text-slate-500">
              <tr>
                <th className="px-6 py-4">ID</th>
                <th className="px-6 py-4">Contractor</th>
                <th className="px-6 py-4">Stage</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Start Date</th>
                <th className="px-6 py-4">Last Update</th>
                <th className="px-6 py-4">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200 bg-white">
              {paginatedContractors.map((contractor, idx) => (
                <tr key={contractor.id} className="hover:bg-slate-50">
                  <td className="px-6 py-5 font-semibold text-slate-900" title={contractor.id}>
                    {(currentPage - 1) * itemsPerPage + idx + 1}
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
                      className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${statusStyles[contractor.status] || "bg-slate-100 text-slate-700"}`}
                    >
                      {contractor.status.replace(/_/g, " ")}
                    </span>
                  </td>
                  <td className="px-6 py-5 text-slate-500">
                    {contractor.startDate}
                  </td>
                  <td className="px-6 py-5 text-slate-500">
                    {contractor.lastUpdate}
                  </td>
                  <td className="px-6 py-5">
                    <button
                      onClick={() => setSelectedContractor(contractor)}
                      className="rounded-2xl bg-sky-50 px-4 py-2 text-xs font-semibold text-sky-600 transition hover:bg-sky-100"
                    >
                      Review
                    </button>
                  </td>
                </tr>
              ))}
              {isLoading && (
                <tr>
                  <td colSpan={7} className="px-6 py-8 text-center text-slate-500">
                    Loading contractors...
                  </td>
                </tr>
              )}
              {!isLoading && filteredContractors.length === 0 && (
                <tr>
                  <td colSpan={7} className="px-6 py-8 text-center text-slate-500">
                    No contractors found.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        {totalPages > 1 && (
          <div className="mt-6 flex items-center justify-between">
            <p className="text-sm text-slate-500">
              Showing <span className="font-medium">{(currentPage - 1) * itemsPerPage + 1}</span> to <span className="font-medium">{Math.min(currentPage * itemsPerPage, filteredContractors.length)}</span> of <span className="font-medium">{filteredContractors.length}</span> contractors
            </p>
            <div className="flex gap-2">
              <button 
                onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
                disabled={currentPage === 1}
                className="rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-50 disabled:opacity-50"
              >
                Previous
              </button>
              <button 
                onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
                disabled={currentPage === totalPages}
                className="rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-50 disabled:opacity-50"
              >
                Next
              </button>
            </div>
          </div>
        )}

      </div>
      {selectedContractor && (
        <ReviewModal
          contractorId={selectedContractor.id}
          onClose={() => {
            setSelectedContractor(null);
            fetchContractors(); // Refresh on close just in case
          }}
        />
      )}
    </div>
  );
}
