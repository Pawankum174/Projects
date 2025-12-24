import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { useAuthStore } from "./Store/Auth";
import Login from "./Pages/Login";
import Register from "./Pages/Register";
import ResetPassword from "./Services/ResetPassword";
import ResetPasswordConfirm from "./Services/ResetPasswordConform";
import Dashboard from "./Pages/Dashboard";
import './Pages/Login.css';
function Private({ children }: { children: React.ReactElement }) {
  const token = useAuthStore((s) => s.accessToken);
  return token ? children : <Navigate to="/" replace />;
}

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/reset-password" element={<ResetPassword />} />
        <Route path="/reset-password/confirm" element={<ResetPasswordConfirm />} />
        <Route path="/dashboard" element={<Private><Dashboard /></Private>} />
      </Routes>
    </Router>
  );
}
