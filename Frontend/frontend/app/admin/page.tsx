import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import AdminPanel from "./AdminPanel";

export default async function AdminPage() {
  const token = (await cookies()).get("returnedToken")?.value;
  if (!token) {
    redirect("/login");
  }

  return <AdminPanel />;
}
