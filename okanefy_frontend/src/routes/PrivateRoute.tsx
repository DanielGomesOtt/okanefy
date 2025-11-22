import { JSX, useEffect, useState } from "react";
import { Navigate } from "react-router";
import { verifyToken } from "../utils/verifyToken";
import { Progress } from "@heroui/react";
import { BASE_URL } from "../utils/constants";

export default function PrivateRoute({ children }: { children: JSX.Element }) {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

  useEffect(() => {
    const checkAuth = async () => {
      const valid = await verifyToken(BASE_URL);
      setIsAuthenticated(valid);
    };
    checkAuth();
  }, []);

  if (isAuthenticated === null) {
    return <div className="w-full h-full flex items-center justify-center"><Progress isIndeterminate aria-label="Loading..." className="max-w-md" size="sm" />;</div>;
  }

  if (!isAuthenticated) {
    localStorage.removeItem("name")
    localStorage.removeItem("email")
    localStorage.removeItem("id")
    localStorage.removeItem("token")
    return <Navigate to="/" replace />;
  }

  return children;
}
