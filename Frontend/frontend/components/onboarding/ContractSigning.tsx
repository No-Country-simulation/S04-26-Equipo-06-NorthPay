import { useState, useEffect, useRef } from "react";
import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: any) => void;
  onboardingId: string;
}

interface ContractInfo {
  contract_id: string;
  content: string;
  signedBy: string | null;
  signedAt: string | null;
  contractHash: string | null;
  signatureReference: string | null;
  status: string;
  signed: boolean;
}

export default function ContractSigning({ data, onChange, onboardingId }: Props) {
  const [contract, setContract] = useState<ContractInfo | null>(null);
  const [loading, setLoading] = useState(true);
  const [signing, setSigning] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [signatureText, setSignatureText] = useState("");
  const [isCheckboxChecked, setIsCheckboxChecked] = useState(data.contractAccepted);
  const timeoutRef = useRef<NodeJS.Timeout | null>(null);
  const hasFetched = useRef(false);

  const fetchOrCreateContract = async () => {
    setLoading(true);
    setError(null);

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 10000); // 10s timeout

    try {
      // 1. Try to get contract
      const response = await fetch(`http://localhost:8080/api/v1/contract/getByOnboardingId/${onboardingId}`, {
        signal: controller.signal
      });

      clearTimeout(timeoutId);

      if (response.ok) {
        const contractData = await response.json();
        setContract(contractData);
        if (contractData.signed) {
          onChange("contractAccepted", true);
        }
        return;
      }

      // 2. If not found (404/500/etc), try to create a default contract
      console.log("Contract not found, creating a default one...");
      const createController = new AbortController();
      const createTimeoutId = setTimeout(() => createController.abort(), 10000);

      const createResponse = await fetch(`http://localhost:8080/api/v1/contract/create/${onboardingId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          content: `NORTHPAY SERVICES AGREEMENT

This Services Agreement (the "Agreement") is made and entered into by and between NorthPay Inc. (together with its affiliates, the "Company") and the Contractor signing below (the "Contractor").

1. SERVICES AND DELIVERABLES
The Contractor agrees to perform professional services as outlined in individual Statements of Work (SOW) issued from time to time. The Services shall be performed in a professional and workmanlike manner.

2. COMPENSATION AND PAYMENTS
As full compensation for the Services, the Company shall pay the Contractor in accordance with the rates and terms specified in the SOW. Payments will be made using the payment method details provided during onboarding.

3. CONFIDENTIALITY AND OWNERSHIP
The Contractor agrees to keep confidential all proprietary or non-public information disclosed by the Company. All designs, code, documents, and other work product developed by the Contractor under this Agreement shall belong solely and exclusively to the Company.

4. TERM AND TERMINATION
This Agreement will commence on the date of execution and continue until terminated by either party upon thirty (30) days prior written notice.

5. SIGNATURE & BINDING AGREEMENT
By checking the acceptance box and typing your full legal name in the signature block below, you declare and agree that your electronic signature constitutes the legal equivalent of a hand-written physical signature, binding you to the terms of this Agreement.`
        }),
        signal: createController.signal
      });

      clearTimeout(createTimeoutId);

      if (!createResponse.ok) {
        throw new Error(`Failed to create contract: ${createResponse.status}`);
      }

      const newContractData = await createResponse.json();
      setContract(newContractData);
    } catch (err: any) {
      console.error("Contract fetch/creation error:", err);
      if (err.name === "AbortError") {
        setError("The request timed out. Please check your internet connection and backend status.");
      } else {
        setError(`Could not load the digitized contract: ${err.message || err}. Please ensure the backend is running and try again.`);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (hasFetched.current) return;
    hasFetched.current = true;
    fetchOrCreateContract();
    return () => {
      if (timeoutRef.current) clearTimeout(timeoutRef.current);
    };
  }, []);

  const handleSign = async () => {
    if (!contract) return;
    if (!signatureText.trim()) {
      setError("Please type your name in the signature field.");
      return;
    }
    if (!isCheckboxChecked) {
      setError("You must accept the terms of the digital contract.");
      return;
    }

    setSigning(true);
    setError(null);

    try {
      const response = await fetch(`http://localhost:8080/api/v1/contract/sign_contract/${contract.contract_id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          signature: signatureText.trim()
        })
      });

      if (!response.ok) {
        throw new Error("Failed to register signature on server.");
      }

      // Re-fetch contract to show updated signed state
      await fetchOrCreateContract();
      onChange("contractAccepted", true);
    } catch (err: any) {
      console.error("Signature registration error:", err);
      setError("Failed to register your signature on the server. Please try again.");
    } finally {
      setSigning(false);
    }
  };

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center py-12 space-y-4">
        <div className="h-10 w-10 animate-spin rounded-full border-4 border-slate-200 border-t-sky-600"></div>
        <p className="text-sm text-slate-500 font-medium">Retrieving digitized contract...</p>
      </div>
    );
  }

  if (error && !contract) {
    return (
      <div className="rounded-3xl border border-rose-100 bg-rose-50/50 p-6 text-center space-y-4">
        <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-rose-100 text-rose-600 text-xl font-bold">
          ⚠
        </div>
        <div className="space-y-1">
          <h3 className="font-semibold text-slate-900">Contract Load Failed</h3>
          <p className="text-xs text-slate-600 max-w-md mx-auto">{error}</p>
        </div>
        <button
          type="button"
          onClick={fetchOrCreateContract}
          className="inline-flex items-center gap-2 rounded-2xl bg-slate-900 px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-slate-800"
        >
          🔄 Retry Connection
        </button>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <header>
        <p className="text-sm text-slate-600">
          Please review the digital services agreement below. You must read the document, accept the terms, and provide your digital signature to proceed.
        </p>
      </header>

      {error && (
        <div className="rounded-2xl border border-rose-100 bg-rose-50 px-4 py-3 text-xs text-rose-700 flex items-center justify-between">
          <span>{error}</span>
          <button onClick={() => setError(null)} className="text-rose-900 hover:underline font-bold">Dismiss</button>
        </div>
      )}

      {/* Visor de Contrato Embebido con Scroll Completo */}
      <div className="relative rounded-3xl border border-slate-200 bg-white shadow-inner overflow-hidden">
        <div className="h-96 overflow-y-scroll p-6 sm:p-8 font-serif text-sm leading-8 text-slate-800 select-none bg-[radial-gradient(#e2e8f0_1px,transparent_1px)] [background-size:16px_16px]">
          <div className="max-w-2xl mx-auto space-y-4 whitespace-pre-line bg-white/95 p-6 shadow-md rounded-2xl border border-slate-100">
            {contract?.content}
          </div>
        </div>
        <div className="absolute bottom-0 left-0 right-0 h-8 bg-gradient-to-t from-slate-50 to-transparent pointer-events-none" />
      </div>

      {contract?.signed ? (
        /* Confirmación Visual del Contrato Firmado */
        <div className="rounded-3xl border border-emerald-100 bg-emerald-50/20 p-6 space-y-4">
          <div className="flex items-center gap-3">
            <span className="flex h-8 w-8 items-center justify-center rounded-full bg-emerald-100 text-emerald-800 text-sm font-bold">
              ✓
            </span>
            <div>
              <h3 className="font-semibold text-slate-900">Contract Signed Digitally</h3>
              <p className="text-xs text-slate-500">Document finalized and legally validated</p>
            </div>
          </div>
          <div className="grid gap-3 sm:grid-cols-2 rounded-2xl bg-white border border-emerald-50 p-4 text-xs">
            <div>
              <span className="text-slate-400 block uppercase tracking-wider font-bold text-[9px]">Signed By</span>
              <span className="font-semibold text-slate-900">{contract.signedBy}</span>
            </div>
            <div>
              <span className="text-slate-400 block uppercase tracking-wider font-bold text-[9px]">Signed At</span>
              <span className="font-semibold text-slate-900">
                {contract.signedAt ? new Date(contract.signedAt + "Z").toLocaleString() : "N/A"}
              </span>
            </div>
            <div className="sm:col-span-2 border-t border-slate-100 pt-2 mt-1">
              <span className="text-slate-400 block uppercase tracking-wider font-bold text-[9px]">Signature Reference ID</span>
              <span className="font-mono text-slate-600 break-all">{contract.signatureReference}</span>
            </div>
            <div className="sm:col-span-2 border-t border-slate-100 pt-2 mt-1">
              <span className="text-slate-400 block uppercase tracking-wider font-bold text-[9px]">SHA-256 Digital Hash</span>
              <span className="font-mono text-slate-600 break-all">{contract.contractHash}</span>
            </div>
          </div>
        </div>
      ) : (
        /* Panel de Firma */
        <div className="rounded-3xl border border-slate-200 bg-white p-6 space-y-6">
          <div className="grid gap-6 sm:grid-cols-2">
            {/* Campo de Firma */}
            <div className="space-y-2">
              <label className="text-[11px] font-bold uppercase tracking-widest text-slate-400">
                Type Your Full Name to Sign
              </label>
              <input
                type="text"
                value={signatureText}
                onChange={(e) => setSignatureText(e.target.value)}
                placeholder= "e.g. John Doe"
                className="w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-sm text-black focus:border-sky-500 focus:outline-none focus:ring-2 focus:ring-sky-500/20"
                style={{ color: "#0f172a", backgroundColor: "#ffffff" }}
                disabled={signing}
              />
            </div>

            {/* Vista Previa de la Firma Cursiva */}
            <div className="space-y-2">
              <span className="text-[11px] font-bold uppercase tracking-widest text-slate-400 block">
                Digital Signature Preview
              </span>
              <div className="h-14 w-full rounded-2xl border border-dashed border-slate-300 bg-slate-50 flex items-center justify-center px-4 overflow-hidden">
                {signatureText.trim() ? (
                  <span 
                    style={{ fontFamily: "var(--font-dancing-script), 'Brush Script MT', cursive" }}
                    className="text-3xl text-sky-700 tracking-wider truncate select-none py-1"
                  >
                    {signatureText}
                  </span>
                ) : (
                  <span className="text-xs text-slate-400 italic">Signature preview will appear here</span>
                )}
              </div>
            </div>
          </div>

          {/* Casilla de Aceptación */}
          <label className="flex items-start gap-3 text-sm text-slate-700 select-none cursor-pointer">
            <input
              type="checkbox"
              checked={isCheckboxChecked}
              onChange={(e) => {
                const checked = e.target.checked;
                setIsCheckboxChecked(checked);
                if (!checked) {
                  onChange("contractAccepted", false);
                }
              }}
              disabled={signing}
              className="mt-1 h-5 w-5 rounded border-slate-300 text-sky-600 focus:ring-sky-500"
            />
            <span>
              I accept the digital contract, terms of services, and authorize the contract activation process.
            </span>
          </label>

          {/* Botón de Firma */}
          <div className="flex justify-end">
            <button
              type="button"
              onClick={handleSign}
              disabled={signing || !signatureText.trim() || !isCheckboxChecked}
              className="inline-flex items-center gap-2 rounded-3xl bg-slate-900 px-6 py-3.5 text-sm font-semibold text-white transition hover:bg-slate-800 disabled:bg-slate-100 disabled:text-slate-400 disabled:cursor-not-allowed"
            >
              {signing ? (
                <>
                  <span className="h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent"></span>
                  Signing digitally...
                </>
              ) : (
                "🖊 Confirm and Sign Contract"
              )}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

