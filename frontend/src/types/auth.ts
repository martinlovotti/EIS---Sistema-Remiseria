export type Role = "ADMIN" | "USUARIO" | "CHOFER";

export interface LoginRequestDTO {
  username: string;
  password: string;
}

export interface LoginResponseDTO {
  token: string;
}

export interface RegisterRequestDTO {
  username: string;
  password: string;
  role: Role;
}

export interface JwtPayload {
  sub: string;
  role: Role;
  exp?: number;
  iat?: number;
}