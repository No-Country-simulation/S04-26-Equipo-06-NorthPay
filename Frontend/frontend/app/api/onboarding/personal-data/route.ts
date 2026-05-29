import { NextResponse } from "next/server";

type PersonalDataField = "firstName" | "lastName" | "email" | "phone";

type PersonalDataErrors = Partial<Record<PersonalDataField, string>>;

const validatePersonalData = (
  body: Record<string, unknown>
): PersonalDataErrors => {
  const errors: PersonalDataErrors = {};

  const nameRegex = /^[\p{L}' -]+$/u;
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const phoneRegex = /^[0-9+() -]+$/;

  const firstName =
    typeof body.firstName === "string" ? body.firstName.trim() : "";

  const lastName =
    typeof body.lastName === "string" ? body.lastName.trim() : "";

  const email =
    typeof body.email === "string" ? body.email.trim() : "";

  const phone =
    typeof body.phone === "string" ? body.phone.trim() : "";

  if (!firstName) {
    errors.firstName = "First name is required.";
  } else if (firstName.length < 2) {
    errors.firstName = "First name must be at least 2 characters.";
  } else if (!nameRegex.test(firstName)) {
    errors.firstName =
      "Only letters, spaces, apostrophes, and hyphens are allowed.";
  }

  if (!lastName) {
    errors.lastName = "Last name is required.";
  } else if (lastName.length < 2) {
    errors.lastName = "Last name must be at least 2 characters.";
  } else if (!nameRegex.test(lastName)) {
    errors.lastName =
      "Only letters, spaces, apostrophes, and hyphens are allowed.";
  }

  if (!email) {
    errors.email = "Email is required.";
  } else if (!emailRegex.test(email)) {
    errors.email = "Enter a valid email address.";
  }

  if (!phone) {
    errors.phone = "Phone number is required.";
  } else if (phone.replace(/\D/g, "").length < 8) {
    errors.phone = "Phone number must include at least 8 digits.";
  } else if (!phoneRegex.test(phone)) {
    errors.phone =
      "Only numbers, spaces, +, parentheses, and hyphens are allowed.";
  }

  return errors;
};

export async function POST(request: Request) {
  const body = await request.json().catch(() => null);

  if (!body || typeof body !== "object" || Array.isArray(body)) {
    return NextResponse.json(
      {
        message: "Invalid personal data payload.",
      },
      {
        status: 400,
      }
    );
  }

  const fieldErrors = validatePersonalData(body as Record<string, unknown>);

  if (Object.keys(fieldErrors).length > 0) {
    return NextResponse.json(
      {
        message: "Personal data contains invalid fields.",
        fieldErrors,
      },
      {
        status: 422,
      }
    );
  }

  return NextResponse.json(
    {
      message: "Personal data saved successfully.",
      onboardingState: "personal-data-completed",
      contractor: {
        firstName: String(body.firstName).trim(),
        lastName: String(body.lastName).trim(),
        email: String(body.email).trim(),
        phone: String(body.phone).trim(),
      },
      savedAt: new Date().toISOString(),
    },
    {
      status: 201,
    }
  );
}
