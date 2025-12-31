import { useState } from "react";
import api from "../api";
import { useAuthStore } from "../Store/Auth";
import { useNavigate } from "react-router-dom";


export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const setAccessToken = useAuthStore((s) => s.setAccessToken);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true); setError(null);
    try {
      const resp = await api.post("/vaultcore/api/auth/login", { username, password });
      setAccessToken(resp.data.access_token);
      navigate("/dashboard");
    } catch (err) {
      setError("Invalid credentials");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <h2>VaultCore Login</h2>
      <form onSubmit={handleLogin} className="login-form">
        <label>Username</label>
        <input value={username} onChange={(e) => setUsername(e.target.value)} required />
        <label>Password</label>
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <button type="submit" disabled={loading}>{loading ? "Signing in..." : "Login"}</button>
        <div className="login-actions">
          <button type="button" className="secondary-btn" onClick={() => navigate("/register")}>Register</button>
          <button type="button" className="secondary-btn" onClick={() => navigate("/reset-password")}>Forgot Password</button>
        </div>
        {error && <p className="error-message">{error}</p>}
      </form>
    </div>
  );
}
