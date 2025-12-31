import { useEffect, useState } from "react";
import api from "../api";
import { useAuthStore } from "../Store/Auth";

type Balance = { accountId: string; balance: number; currency: string };

export default function Dashboard() {
  const token = useAuthStore((s) => s.accessToken);
  const [data, setData] = useState<Balance | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const run = async () => {
      if (!token) return;
      setLoading(true);
      try {
        const resp = await api.get<Balance>("vaultcore/v1/accounts/<your-account-id>/balance");
        setData(resp.data);
      } finally { setLoading(false); }
    };
    run();
  }, [token]);

  return (
    <div style={{ maxWidth: 640, margin: "40px auto" }}>
      <h2>Dashboard</h2>
      {loading ? <p>Loading...</p> : data ? (
        <div>
          <p><strong>Account:</strong> {data.accountId}</p>
          <p><strong>Balance:</strong> {data.balance.toFixed(2)} {data.currency}</p>
        </div>
      ) : <p>No data</p>}
    </div>
  );
}
