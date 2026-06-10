import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import type { Role } from "../types/auth";
import type { ReactNode } from "react";

interface ProtectedRouteProps {
  children: ReactNode;
  allowedRoles?: Role[];
}

function getHomeByRole(role: Role | "") {
  switch (role) {
    case "ADMIN":
      return "/admin";
    case "CHOFER":
      return "/chofer";
    case "USUARIO":
      return "/usuario";
    default:
      return "/login";
  }
}

export default function ProtectedRoute({
  children,
  allowedRoles,
}: ProtectedRouteProps) {
  const { isAuthenticated, role } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(role as Role)) {
    return <Navigate to={getHomeByRole(role)} replace />;
  }

  return <>{children}</>;
}