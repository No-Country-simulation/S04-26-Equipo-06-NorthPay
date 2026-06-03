import React from 'react';

export type Contractor = {
  id: string;
  name: string;
  email: string;
  stage: string;
  status: string;
  startDate: string;
  lastUpdate: string;
  rawDate: number;
};

const statusStyles: Record<string, string> = {
  PENDING: "bg-amber-100 text-amber-700",
  PENDING_VERIFICATION: "bg-orange-100 text-orange-700",
  CHANGES_REQUESTED: "bg-rose-100 text-rose-700",
  APPROVED: "bg-emerald-100 text-emerald-700",
  INVITED: "bg-slate-100 text-slate-700",
};

interface ContractorsTableProps {
  filteredContractors: Contractor[];
  paginatedContractors: Contractor[];
  isLoading: boolean;
  currentPage: number;
  itemsPerPage: number;
  totalPages: number;
  setCurrentPage: (updater: (prev: number) => number) => void;
  setSelectedContractor: (contractor: Contractor) => void;
}

export default function ContractorsTable({
  filteredContractors,
  paginatedContractors,
  isLoading,
  currentPage,
  itemsPerPage,
  totalPages,
  setCurrentPage,
  setSelectedContractor
}: ContractorsTableProps) {
  return (
    <>
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
    </>
  );
}
