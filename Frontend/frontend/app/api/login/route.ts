import { NextResponse } from "next/server";

export async function POST(request: Request) {
  const body = await request.json().catch(() => null);

  if (!body || typeof body.email !== "string" || typeof body.password !== "string") {
    return NextResponse.json({ message: "Invalid credentials." }, { status: 400 });
  }

  // Replace this validation with your real auth logic.
  if (!body.email || !body.password) {
    return NextResponse.json({ message: "Email and password are required." }, { status: 400 });
  }

  return NextResponse.json({
    returnedToken: "demo-token",
    userAuthenticated: body.email || "martin123",
  });
}
