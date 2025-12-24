import { useState } from "react";
import api from "../api";


export default function Register() {
  const [username, setUsername] = useState(""); const [password, setPassword] = useState("");
  const [confirm, setConfirm] = useState(""); const [msg, setMsg] = useState<string | null>(null);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (password !== confirm) return setMsg("Passwords do not match");
    try { await api.post("/auth/register", { username, password }); setMsg("Registration successful"); }
    catch { setMsg("Registration failed"); }
  };

  return (
    <div className="login-container">
      <h2>Register</h2>
      <form onSubmit={submit} className="login-form">
        <label>Username</label><input value={username} onChange={(e) => setUsername(e.target.value)} required />
        <label>Password</label><input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <label>Confirm</label><input type="password" value={confirm} onChange={(e) => setConfirm(e.target.value)} required />
        <button type="submit">Register</button>
      </form>
      {msg && <p className={msg.includes("successful") ? "success-message" : "error-message"}>{msg}</p>}
    </div>
  );
}
