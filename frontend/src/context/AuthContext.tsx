import {
  createContext,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import { loginRequest, registerRequest } from "../api/auth";
import type {
  JwtPayload,
  LoginRequestDTO,
  RegisterRequestDTO,
  Role,
} from "../types/auth";

interface AuthContextType {
  token: string;
  role: Role | "";
  username: string;
  isAuthenticated: boolean;
  login: (credentials: LoginRequestDTO) => Promise<string>;
  register: (data: RegisterRequestDTO) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

function decodeJwt(token: string): JwtPayload | null {
  try {
    const payload = token.split(".")[1];
    const base64 = payload.replace(/-/g, "+").replace(/_/g, "/");
    const json = atob(base64);
    return JSON.parse(json) as JwtPayload;
  } catch {
    return null;
  }
}

function getPathByRole(role: Role | ""): string {
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

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string>(localStorage.getItem("token") || "");
  const [role, setRole] = useState<Role | "">(
    (localStorage.getItem("role") as Role) || ""
  );
  const [username, setUsername] = useState<string>(
    localStorage.getItem("username") || ""
  );

  const isAuthenticated = !!token;

  async function login(credentials: LoginRequestDTO): Promise<string> {
    const response = await loginRequest(credentials);
    const newToken = response.token;

    const decoded = decodeJwt(newToken);
    const newRole = decoded?.role || "";
    const newUsername = decoded?.sub || "";

    localStorage.setItem("token", newToken);
    localStorage.setItem("role", newRole);
    localStorage.setItem("username", newUsername);

    setToken(newToken);
    setRole(newRole);
    setUsername(newUsername);

    return getPathByRole(newRole);
  }

  async function register(data: RegisterRequestDTO): Promise<void> {
    await registerRequest(data);
  }

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("username");

    setToken("");
    setRole("");
    setUsername("");
  }

  const value = useMemo(
    () => ({
      token,
      role,
      username,
      isAuthenticated,
      login,
      register,
      logout,
    }),
    [token, role, username, isAuthenticated]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth debe usarse dentro de AuthProvider");
  }
  return context;
}