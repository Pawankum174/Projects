import axios from "axios";
import { useAuthStore } from "./Store/Auth";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  withCredentials: true,
  timeout: 10000,
});

api.interceptors.request.use((config) => {
  const token = useAuthStore.getState().accessToken;
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export default api;
