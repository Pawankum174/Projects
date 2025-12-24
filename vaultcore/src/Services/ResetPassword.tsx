import { useState } from "react";
import api from "../api";


export default function ResetPassword() {
  const [email, setEmail] = useState(""); const [msg, setMsg] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    try { await api.post("/auth/reset-password", { email }); setMsg("Reset link sent"); }
    catch { setMsg("Failed to send reset link"); }
  };

  return (
    <div className="login-container">
      <h2>Reset Password</h2>
      <form onSubmit={submit} className="login-form">
        <label>Email</label><input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <button type="submit">Send Reset Link</button>
      </form>
      {msg && <p className={msg.includes("sent") ? "success-message" : "error-message"}>{msg}</p>}
    </div>
  );
}
