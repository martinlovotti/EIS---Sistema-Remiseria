export type Role = "ADMIN" | "USUARIO" | "CHOFER";

export interface LoginRequestDTO {
  username: string;
  password: string;
}

export interface LoginResponseDTO {
  token: string;
  role: Role;
  entidadId: number
}

export interface RegisterUsuarioRequestDTO {
  username: string;
  password: string;
  nombre: string;
}

export interface RegisterChoferRequestDTO {
  username: string;
  password: string;
  nombre: string;
  patente: string;
}