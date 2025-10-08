import { JSX } from "react";
import { Navigate } from "react-router";

export default function PublicRoute({ children }: { children: JSX.Element }) {
  const token = localStorage.getItem("token");

  if (token) {
    return <Navigate to="/geral" replace />;
  }

  return children;
}
