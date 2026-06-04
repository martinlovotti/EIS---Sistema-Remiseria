import { api } from "./axios";
import type {
  LoginRequestDTO,
  LoginResponseDTO,
  RegisterRequestDTO,
} from "../types/auth";

export async function loginRequest(
  data: LoginRequestDTO
): Promise<LoginResponseDTO> {
  try {
    const response = await api.post<LoginResponseDTO>("/auth/login", data);
    return response.data;
  } catch (error: any) {
    throw new Error(error.response?.data || "Error al iniciar sesión");
  }
}

export async function registerRequest(
  data: RegisterRequestDTO
): Promise<string> {
  try {
    const response = await api.post<string>("/auth/register", data);
    return response.data;
  } catch (error: any) {
    throw new Error(error.response?.data || "Error al registrarse");
  }
}
