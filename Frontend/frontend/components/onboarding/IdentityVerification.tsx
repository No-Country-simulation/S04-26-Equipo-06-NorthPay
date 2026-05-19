import { useState, useRef, useEffect } from "react";
import { OnboardingData } from "../../app/onboarding/types";

interface Props {
  data: OnboardingData;
  onChange: (field: keyof OnboardingData, value: string) => void;
  clearError?: () => void;
}

type InternalStep = "instructions" | "capture" | "processing" | "completed";

export default function IdentityVerification({ data, onChange, clearError }: Props) {
  const isAlreadyVerified = data.verificationNotes?.toLowerCase().includes("verification in progress");
  const [internalStep, setInternalStep] = useState<InternalStep>(isAlreadyVerified ? "completed" : "instructions");
  const [isCameraAccessDenied, setIsCameraAccessDenied] = useState(false);
  const [isRetrying, setIsRetrying] = useState(false);
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    if (isAlreadyVerified && internalStep === "instructions") {
      setInternalStep("completed");
    }
  }, [isAlreadyVerified]);

  useEffect(() => {
    if (internalStep === "capture" && !isCameraAccessDenied) {
      startCamera();
    }
    return () => stopCamera();
  }, [internalStep, isCameraAccessDenied]);

  const startCamera = async () => {
    setIsRetrying(true);
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: { facingMode: "user" } });
      if (videoRef.current) videoRef.current.srcObject = stream;
      setIsCameraAccessDenied(false);
    } catch (err) {
      console.error("Camera access error:", err);
      setIsCameraAccessDenied(true);
    } finally {
      setIsRetrying(false);
    }
  };

  const stopCamera = () => {
    if (videoRef.current && videoRef.current.srcObject) {
      const stream = videoRef.current.srcObject as MediaStream;
      stream.getTracks().forEach((track) => track.stop());
    }
  };

  const handleCapture = () => {
    if (videoRef.current && canvasRef.current) {
      const video = videoRef.current;
      const canvas = canvasRef.current;
      canvas.width = video.videoWidth;
      canvas.height = video.videoHeight;
      const ctx = canvas.getContext("2d");
      if (ctx) {
        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
        const image = canvas.toDataURL("image/jpeg");
        stopCamera();
        uploadImage(image);
      }
    }
  };

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => uploadImage(reader.result as string);
      reader.readAsDataURL(file);
    }
  };

  const uploadImage = async (image: string) => {
    setInternalStep("processing");
    try {
      
      /* LLAMADO AL BACKEND. Cambiar por el endpoint real
      const response = await fetch("https://api.northpay.com/v1/auth/verify-identity", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ 
          image, 
          email: data.email,
          documentType: data.documentType // Opcional, según backend
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Verification failed");
      }
      */
      

      // Simulación de éxito para desarrollo:
      await new Promise((resolve) => setTimeout(resolve, 2000));
      
      setInternalStep("completed");
      onChange("verificationNotes", "Verification in progress");
    } catch (error) {
      console.error("Verification error:", error);
      setInternalStep("capture");
    }
  };

  return (
    <div className="space-y-6">
      {internalStep === "instructions" && (
        <div className="animate-in fade-in slide-in-from-bottom-4 duration-500 space-y-6">
          <div className="rounded-3xl bg-sky-50 p-8 border border-sky-100">
            <div className="flex items-center gap-4 mb-6">
              <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-sky-600 text-white shadow-lg">
                <span className="text-2xl">🛡️</span>
              </div>
              <h3 className="text-lg font-semibold text-slate-900">Identity Verification</h3>
            </div>
            <p className="text-sm text-slate-600 leading-relaxed">
              To finalize your onboarding, we need a live selfie to match your identity with the documents provided in Step 2.
            </p>
          </div>
          <button
            onClick={() => {
              setInternalStep("capture");
              if (clearError) clearError();
            }}
            className="w-full rounded-2xl bg-slate-900 px-6 py-4 text-sm font-semibold text-white hover:bg-slate-700 transition shadow-xl"
          >
            Start selfie capture
          </button>
        </div>
      )}

      {internalStep === "capture" && (
        <div className="animate-in fade-in zoom-in-95 duration-500 space-y-6">
          <div className="relative aspect-video overflow-hidden rounded-3xl border-2 border-dashed border-slate-200 bg-slate-50 flex flex-col items-center justify-center min-h-[300px]">
            {isCameraAccessDenied ? (
              <div className="p-8 text-center space-y-6 max-w-sm">
                <div className="flex h-16 w-16 mx-auto items-center justify-center rounded-full bg-rose-50 text-rose-500">
                  <span className="text-3xl">🚫</span>
                </div>
                <div>
                  <p className="font-bold text-slate-900 text-lg">Camera access blocked</p>
                  <p className="text-sm text-slate-500 mt-2 leading-relaxed">
                    To continue, click the <span className="font-bold text-slate-900">lock icon 🔒</span> in your address bar and set Camera to <span className="text-emerald-600 font-bold">"Allow"</span>.
                  </p>
                </div>
                <div className="space-y-3">
                  <button onClick={startCamera} className="w-full rounded-2xl bg-slate-900 px-6 py-3 text-sm font-semibold text-white hover:bg-slate-700 transition">
                    {isRetrying ? "Checking..." : "Try camera again"}
                  </button>
                  <label className="flex w-full cursor-pointer items-center justify-center rounded-2xl border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-900 hover:bg-slate-50 transition">
                    Upload selfie manually
                    <input type="file" className="hidden" accept="image/*" onChange={(e) => {
                      handleFileUpload(e);
                      if (clearError) clearError();
                    }} />
                  </label>
                </div>
              </div>
            ) : (
              <>
                <video ref={videoRef} autoPlay playsInline className="h-full w-full object-cover" />
                <div className="pointer-events-none absolute inset-0 flex items-center justify-center">
                  <div className="h-64 w-64 rounded-[4rem] border-2 border-white/50 shadow-[0_0_0_9999px_rgba(0,0,0,0.4)]" />
                </div>
                <div className="absolute bottom-6 flex gap-4">
                  <button
                    type="button"
                    onClick={handleCapture}
                    className="flex h-16 w-16 items-center justify-center rounded-full bg-white text-slate-900 shadow-2xl transition hover:scale-105 active:scale-95"
                  >
                    <div className="h-12 w-12 rounded-full border-4 border-slate-900" />
                  </button>
                </div>
              </>
            )}
            <canvas ref={canvasRef} className="hidden" />
          </div>
        </div>
      )}

      {internalStep === "processing" && (
        <div className="animate-in fade-in zoom-in-95 duration-500 py-12 text-center space-y-6">
          <div className="relative mx-auto h-24 w-24">
            <div className="absolute inset-0 animate-spin rounded-full border-4 border-slate-100 border-t-sky-600" />
            <div className="absolute inset-0 flex items-center justify-center">
              <span className="text-2xl animate-pulse">⏳</span>
            </div>
          </div>
          <h3 className="text-lg font-semibold text-slate-900">Uploading selfie...</h3>
        </div>
      )}

      {internalStep === "completed" && (
        <div className="animate-in fade-in slide-in-from-top-4 duration-500 space-y-6 text-center">
          <div className="rounded-3xl border border-sky-100 bg-sky-50 p-8 space-y-4">
            <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-sky-600 text-white shadow-lg">
              <span className="text-3xl">⏳</span>
            </div>
            <div>
              <h3 className="text-lg font-semibold text-slate-900">Verification in Progress</h3>
              <p className="text-sm text-slate-600 mt-2">
                Your selfie has been received. The final validation will be completed shortly after submission.
              </p>
            </div>
          </div>
          <button onClick={() => {
            setInternalStep("capture");
            if (clearError) clearError();
          }} className="text-sm font-medium text-slate-500 underline underline-offset-4">
            Retake photo
          </button>
        </div>
      )}
    </div>
  );
}
