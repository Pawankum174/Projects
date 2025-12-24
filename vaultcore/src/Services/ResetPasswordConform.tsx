import { useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import api from "../api";


export default function ResetPasswordConfirm() {
  const [sp] = useSearchParams(); const navigate = useNavigate();
  const token = sp.get("token");
  const [pw, setPw] = useState(""); const [confirm, setConfirm] = useState(""); const [msg, setMsg] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!token) return setMsg("Missing token");
    if (pw !== confirm) return setMsg("Passwords do not match");
    try { await api.post("/auth/reset-password/confirm", { token, newPassword: pw }); setMsg("Password updated, redirecting..."); setTimeout(()=>navigate("/"), 1500); }
    catch { setMsg("Failed to reset"); }
  };

  return (
    <div className="login-container">
      <h2>Set New Password</h2>
      <form onSubmit={submit} className="login-form">
        <label>New Password</label><input type="password" value={pw} onChange={(e) => setPw(e.target.value)} required />
        <label>Confirm</label><input type="password" value={confirm} onChange={(e) => setConfirm(e.target.value)} required />
        <button type="submit">Update Password</button>
      </form>
      {msg && <p className={msg.includes("updated") ? "success-message" : "error-message"}>{msg}</p>}
    </div>
  );
}
