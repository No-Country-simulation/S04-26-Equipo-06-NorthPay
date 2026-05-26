import { NextResponse } from "next/server";

export async function POST(request: Request) {
  const body = await request.json().catch(() => null);

  if (!body || typeof body.email !== "string" || typeof body.password !== "string") {
    return NextResponse.json({ message: "Invalid credentials." }, { status: 400 });
  }

  try {
    // For server-side fetches inside Docker, we must use the container name 'backend' instead of 'localhost'
    const BACKEND_INTERNAL_URL = process.env.BACKEND_INTERNAL_URL || "http://backend:8080";
    const backendResponse = await fetch(`${BACKEND_INTERNAL_URL}/api/v1/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email: body.email, password: body.password }),
    });

    if (!backendResponse.ok) {
      return NextResponse.json({ message: "Invalid credentials." }, { status: 401 });
    }

    const data = await backendResponse.json();
    return NextResponse.json(data);
  } catch (error) {
    return NextResponse.json({ message: "Backend connection failed." }, { status: 500 });
  }
}
