import {
  createContext,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import {
  loginRequest,
  registerChoferRequest,
  registerUsuarioRequest,
} from "../api/auth";
import type {
  LoginRequestDTO,
  RegisterChoferRequestDTO,
  RegisterUsuarioRequestDTO,
  Role,
} from "../types/auth";

interface AuthContextType {
  token: string;
  role: Role | "";
  username: string;
  entidadId: number | null;
  isAuthenticated: boolean;
  login: (credentials: LoginRequestDTO) => Promise<string>;
  registerUsuario: (data: RegisterUsuarioRequestDTO) => Promise<void>;
  registerChofer: (data: RegisterChoferRequestDTO) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

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
  const [entidadId, setEntidadId] = useState<number | null>(() => {
    const value = localStorage.getItem("entidadId");
    return value ? Number(value) : null;
  });

  const isAuthenticated = !!token;

  async function login(credentials: LoginRequestDTO): Promise<string> {
    const response = await loginRequest(credentials);

    const newToken = response.token;
    const newRole = response.role;
    const newEntidadId = response.entidadId;
    const newUsername = credentials.username;

    localStorage.setItem("token", newToken);
    localStorage.setItem("role", newRole);
    localStorage.setItem("username", newUsername);

    if (newEntidadId !== null && newEntidadId !== undefined) {
      localStorage.setItem("entidadId", String(newEntidadId));
    } else {
      localStorage.removeItem("entidadId");
    }

    setToken(newToken);
    setRole(newRole);
    setUsername(newUsername);
    setEntidadId(newEntidadId ?? null);

    return getPathByRole(newRole);
  }

  async function registerUsuario(
      data: RegisterUsuarioRequestDTO
  ): Promise<void> {
    await registerUsuarioRequest(data);
  }

  async function registerChofer(
      data: RegisterChoferRequestDTO
  ): Promise<void> {
    await registerChoferRequest(data);
  }

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("username");
    localStorage.removeItem("entidadId");

    setToken("");
    setRole("");
    setUsername("");
    setEntidadId(null);
  }

  const value = useMemo(
      () => ({
        token,
        role,
        username,
        entidadId,
        isAuthenticated,
        login,
        registerUsuario,
        registerChofer,
        logout,
      }),
      [token, role, username, entidadId, isAuthenticated]
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
