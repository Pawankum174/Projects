import { useState } from "react";
import type { FormEvent } from "react";
import axios from "axios";

export default function AuthPage() {
  const [form, setForm] = useState({ username: "", password: "", email: "" });
  const [mode, setMode] = useState("register"); // "register" or "login"
  const [message, setMessage] = useState("");
  const [tokens, setTokens] = useState({ access: "", refresh: "" });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      if (mode === "register") {
        const res = await axios.post("http://localhost:8081/vaultcore/api/auth/register", {
          username: form.username,
          password: form.password,
          email: form.email,
        });
        setMessage(res.data.message);
      } else {
        const res = await axios.post("http://localhost:8081/vaultcore/api/auth/login", {
          username: form.username,
          password: form.password,
        });
        setTokens({
          access: res.data.access_token,
          refresh: res.data.refresh_token,
        });
        setMessage("Login successful!");
      }
    } catch (err) {
      const errorMessage = axios.isAxiosError(err) ? err.response?.data?.message : "Error occurred";
      setMessage(errorMessage || "Error occurred");
    }
  };

  const refreshToken = async () => {
    try {
      const res = await axios.post("http://localhost:8081/vaultcore/api/auth/token/refresh", {
        refresh_token: tokens.refresh,
      });
      setTokens({
        access: res.data.access_token,
        refresh: res.data.refresh_token,
      });
      setMessage("Token refreshed!");
    } catch (err) {
      setMessage(axios.isAxiosError(err) ? err.response?.data?.message || "Refresh failed" : "Refresh failed");
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "2rem auto", padding: "1rem", border: "1px solid #ccc" }}>
      <h2>{mode === "register" ? "Register" : "Login"}</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="username"
          placeholder="Username"
          value={form.username}
          onChange={handleChange}
          required
        />
        <br />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          required
        />
        <br />
        {mode === "register" && (
          <>
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={form.email}
              onChange={handleChange}
              required
            />
            <br />
          </>
        )}
        <button type="submit">{mode === "register" ? "Register" : "Login"}</button>
      </form>

      <p>{message}</p>

      {mode === "login" && tokens.access && (
        <div>
          <p><strong>Access Token:</strong> {tokens.access}</p>
          <p><strong>Refresh Token:</strong> {tokens.refresh}</p>
          <button onClick={refreshToken}>Refresh Token</button>
        </div>
      )}

      <hr />
      <button onClick={() => setMode("register")}>Switch to Register</button>
      <button onClick={() => setMode("login")}>Switch to Login</button>
    </div>
  );
}
