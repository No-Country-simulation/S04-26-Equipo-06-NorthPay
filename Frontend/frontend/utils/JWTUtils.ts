import { JWTPayload } from "@/types/global";

export function jwtHeader(): Headers {
  return new Headers({
    "Authorization": `Bearer ${getTokenFromCookie()}`,
  });
}

export function getTokenFromCookie(): string | null {
  const match = document.cookie.match(new RegExp("(^| )returnedToken=([^;]+)"));
  return match ? decodeURIComponent(match[2]) : null;
}

export function parseJWTPayload(): JWTPayload | null {
  try {
    const token = getTokenFromCookie();
    if (!token) return null;

    const payloadBase64 = token.split(".")[1];
    const payloadJson = atob(payloadBase64);
    return JSON.parse(payloadJson) as JWTPayload;
  } catch (error) {
    console.error("Failed to parse JWT payload:", error);
    return null;
  }
}
