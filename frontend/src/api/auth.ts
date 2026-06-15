import { api } from "./axios";
import type {
  LoginRequestDTO,
  LoginResponseDTO,
  RegisterChoferRequestDTO,
  RegisterUsuarioRequestDTO,
} from "../types/auth";

function getErrorMessage(error: any, fallback: string): string {
  return error.response.data.message || fallback;
}

export async function loginRequest(
  data: LoginRequestDTO
): Promise<LoginResponseDTO> {
  try {
    const response = await api.post<LoginResponseDTO>("/auth/login", data);
    return response.data;
  } catch (error: any) {
    throw new Error(getErrorMessage(error, "Error al iniciar sesión"));
  }
}

export async function registerUsuarioRequest(
    data: RegisterUsuarioRequestDTO
): Promise<string> {
  try {
    const response = await api.post<string>("/auth/register/usuario", data);
    return response.data;
  } catch (error: any) {
    throw new Error(getErrorMessage(error, "Error al registrar usuario"));
  }
}

export async function registerChoferRequest(
    data: RegisterChoferRequestDTO
): Promise<string> {
  try {
    const response = await api.post<string>("/auth/register/chofer", data);
    return response.data;
  } catch (error: any) {
    throw new Error(getErrorMessage(error, "Error al registrar chofer"));
  }
}