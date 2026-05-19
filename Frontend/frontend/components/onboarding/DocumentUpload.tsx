import { useEffect, useState, useRef } from "react";
import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: string) => void;
}

interface UploadState {
  status: "pending" | "uploading" | "paused" | "error" | "uploaded";
  progress: number;
  fileName: string | null;
  errorMsg: string | null;
}

const REQUIRED_DOC = {
  id: "id_passport",
  label: "National ID or Passport",
  description: "Please upload a clear scan or photo of both sides of your official identity card or the main page of your passport.",
};

export default function DocumentUpload({ data, onChange }: Props) {
  const [upload, setUpload] = useState<UploadState>({
    status: "pending",
    progress: 0,
    fileName: null,
    errorMsg: null
  });
  
  const [hasErroredOnce, setHasErroredOnce] = useState(false);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);
  const fileRef = useRef<File | null>(null);

  // Sync state with parent component data on mount
  useEffect(() => {
    if (data.documentName) {
      setUpload({
        status: "uploaded",
        progress: 100,
        fileName: data.documentName,
        errorMsg: null
      });
    }
  }, []);

  // Clean up interval on unmount
  useEffect(() => {
    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
    };
  }, []);

  const startUploadSimulated = (file: File, resumeProgress = 0) => {
    fileRef.current = file;
    
    setUpload({
      status: "uploading",
      progress: resumeProgress,
      fileName: file.name,
      errorMsg: null
    });

    let currentProgress = resumeProgress;
    
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
    }

    intervalRef.current = setInterval(() => {
      // Automatically simulate a connection glitch at 60% only on the first attempt
      if (!hasErroredOnce && currentProgress >= 50 && currentProgress < 65) {
        clearInterval(intervalRef.current!);
        setHasErroredOnce(true);
        setUpload(prev => ({
          ...prev,
          status: "error",
          errorMsg: "Connection interrupted. Please verify your network and retry."
        }));
        return;
      }

      const increment = Math.floor(Math.random() * 15) + 8; // 8% to 23%
      currentProgress = Math.min(currentProgress + increment, 100);

      setUpload(prev => ({
        ...prev,
        progress: currentProgress
      }));

      if (currentProgress >= 100) {
        clearInterval(intervalRef.current!);
        setUpload({
          status: "uploaded",
          progress: 100,
          fileName: file.name,
          errorMsg: null
        });
        onChange("documentName", file.name);
      }
    }, 250);
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // 1. Validate file format (PDF, JPG, JPEG, PNG)
    const allowedTypes = ["application/pdf", "image/jpg", "image/jpeg", "image/png"];
    const extension = file.name.split(".").pop()?.toLowerCase();
    const allowedExtensions = ["pdf", "jpg", "jpeg", "png"];
    
    if (!allowedTypes.includes(file.type) && !allowedExtensions.includes(extension || "")) {
      setUpload({
        status: "error",
        progress: 0,
        fileName: file.name,
        errorMsg: "Format not supported. Only PDF, JPG, JPEG, and PNG files are accepted."
      });
      return;
    }

    // 2. Validate file size (max 5MB)
    const maxSize = 5 * 1024 * 1024;
    if (file.size > maxSize) {
      setUpload({
        status: "error",
        progress: 0,
        fileName: file.name,
        errorMsg: "The selected file exceeds the 5MB size limit."
      });
      return;
    }

    // Reset error state for new uploads
    setHasErroredOnce(false);
    startUploadSimulated(file);
  };

  const handleRetry = () => {
    const file = fileRef.current;
    if (file) {
      startUploadSimulated(file, upload.progress);
    }
  };

  const handleReplace = () => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
    }
    onChange("documentName", "");
    setUpload({
      status: "pending",
      progress: 0,
      fileName: null,
      errorMsg: null
    });
  };

  return (
    <div className="space-y-6">
      <header>
        <p className="text-sm text-slate-600">
          Please upload your identity documentation to proceed with compliance verification.
        </p>
      </header>

      {/* Document Card */}
      <div 
        className={`rounded-[2rem] border p-6 transition-all duration-300 ${
          upload.status === "uploaded" 
            ? "border-emerald-100 bg-emerald-50/20" 
            : upload.status === "error" 
            ? "border-rose-100 bg-rose-50/20" 
            : "border-slate-200 bg-white"
        }`}
      >
        <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
          <div className="space-y-1">
            <div className="flex items-center gap-3">
              <h3 className="font-semibold text-slate-900">{REQUIRED_DOC.label}</h3>
              {upload.status === "uploaded" && (
                <span className="inline-flex items-center gap-1 rounded-full bg-emerald-100 px-2.5 py-0.5 text-xs font-semibold text-emerald-800">
                  ✓ Uploaded
                </span>
              )}
              {upload.status === "uploading" && (
                <span className="inline-flex items-center gap-1 rounded-full bg-sky-100 px-2.5 py-0.5 text-xs font-semibold text-sky-800 animate-pulse">
                  ⌛ Loading...
                </span>
              )}
              {upload.status === "error" && (
                <span className="inline-flex items-center gap-1 rounded-full bg-rose-100 px-2.5 py-0.5 text-xs font-semibold text-rose-800">
                  ⚠ Error
                </span>
              )}
              {upload.status === "pending" && (
                <span className="inline-flex items-center gap-1 rounded-full bg-slate-100 px-2.5 py-0.5 text-xs font-semibold text-slate-600">
                  ● Required
                </span>
              )}
            </div>
            <p className="text-xs text-slate-500 max-w-xl">{REQUIRED_DOC.description}</p>
          </div>

          {upload.fileName && upload.status !== "pending" && (
            <div className="text-xs font-medium text-slate-600 truncate max-w-xs sm:text-right">
              📄 {upload.fileName}
            </div>
          )}
        </div>

        {/* Action / Progress Area */}
        <div className="mt-5">
          {/* 1. Pending Dropzone */}
          {upload.status === "pending" && (
            <label className="flex w-full cursor-pointer flex-col items-center justify-center rounded-2xl border border-dashed border-slate-300 bg-slate-50/50 py-8 transition-all hover:bg-slate-50 hover:border-sky-400">
              <span className="text-3xl mb-2">📤</span>
              <span className="text-xs font-semibold text-sky-600 hover:text-sky-700">Choose file to upload</span>
              <span className="text-[10px] text-slate-400 mt-1">PDF, JPG, JPEG, PNG (max 5MB)</span>
              <input 
                type="file" 
                onChange={handleFileChange} 
                className="hidden" 
                accept=".pdf,.jpg,.jpeg,.png"
              />
            </label>
          )}

          {/* 2. Progress Bar */}
          {upload.status === "uploading" && (
            <div className="space-y-2">
              <div className="flex items-center justify-between text-xs font-semibold text-slate-600">
                <span>Uploading document...</span>
                <span>{upload.progress}%</span>
              </div>
              <div className="h-2 w-full overflow-hidden rounded-full bg-slate-100">
                <div 
                  className="h-full rounded-full bg-sky-600 transition-all duration-300 ease-out" 
                  style={{ width: `${upload.progress}%` }}
                />
              </div>
            </div>
          )}

          {/* 3. Connection Loss Error */}
          {upload.status === "error" && (
            <div className="space-y-3">
              <div className="flex items-center gap-2 text-xs font-medium text-rose-600">
                <span>⚠️</span>
                <span>{upload.errorMsg || "Upload failed."}</span>
              </div>
              <div className="flex gap-3">
                <button 
                  type="button" 
                  onClick={handleRetry}
                  className="rounded-xl bg-slate-900 px-4 py-2 text-xs font-bold uppercase tracking-wider text-white transition hover:bg-sky-600"
                >
                  🔄 Retry Upload
                </button>
                <button 
                  type="button" 
                  onClick={handleReplace}
                  className="rounded-xl border border-slate-300 bg-white px-4 py-2 text-xs font-bold uppercase tracking-wider text-slate-700 transition hover:bg-slate-50"
                >
                  Cancel
                </button>
              </div>
            </div>
          )}

          {/* 4. Uploaded State */}
          {upload.status === "uploaded" && (
            <div className="flex items-center justify-between rounded-2xl bg-emerald-500/10 px-4 py-3 border border-emerald-500/10 text-emerald-800">
              <div className="flex items-center gap-2 text-xs font-medium">
                <span>🛡️</span>
                <span>File verified successfully (PENDING_REVIEW)</span>
              </div>
              <button 
                type="button"
                onClick={handleReplace}
                className="text-xs font-bold text-sky-600 hover:text-sky-700 transition underline underline-offset-4"
              >
                Replace file
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
