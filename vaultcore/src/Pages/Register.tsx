import { useState } from "react";
import axios, { AxiosError } from "axios";


export default function RegisterPage() {
  const [form, setForm] = useState({ username: "", password: "", email: "" });
  const [message, setMessage] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const res = await axios.post("http://localhost:8081/vaultcore/api/auth/register", form);
      setMessage(res.data.message);
    } catch (err) {
     const axiosError = err as AxiosError<{ message: string }>;
      setMessage(axiosError.response?.data?.message || "Error occurred");
    }
  };

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <input name="username" placeholder="Username" onChange={handleChange} required />
        <input type="password" name="password" placeholder="Password" onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
        <button type="submit">Register</button>
      </form>
      <p>{message}</p>
    </div>
  );
}
