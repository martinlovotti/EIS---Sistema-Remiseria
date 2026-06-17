import { api } from "./axios";
import axios from "axios";

export type UsuarioDTO = {
  id: number;
  nombre: string;
};

export type ChoferDTO = {
  id: number;
  nombre: string;
  patente: string;
};

export type EstadoViaje =
    | "PENDIENTE"
    | "ACEPTADO"
    | "EN_CURSO"
    | "CANCELADO"
    | "FINALIZADO";

export type ViajeDTO = {
  id: number;
  origen: string;
  destino: string;
  distancia: number | null;
  precio: number | null;
  estado: EstadoViaje;
  usuario: UsuarioDTO;
  chofer: ChoferDTO | null;
  calificacion: number | null;
  fechaCreacion: string;
};

function getAxiosErrorMessage(
    error: unknown,
    fallback: string
): string {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data;

    if (typeof data === "string") {
      return data;
    }

    if (data?.message) {
      return data.message;
    }
  }

  return fallback;
}

export const getViajes = async (): Promise<ViajeDTO[]> => {
  const response = await api.get<ViajeDTO[]>("/viaje");
  return response.data;
};

export const getViajesDisponibles = async (): Promise<ViajeDTO[]> => {
  const viajes = await getViajes();
  return viajes.filter((viaje) => viaje.estado === "PENDIENTE");
};

export const aceptarViaje = async (
    idViaje: number,
    idChofer: number
): Promise<void> => {
  try {
    await api.patch(`/viaje/${idViaje}/aceptarViaje/${idChofer}`);
  } catch (error: unknown) {
    throw new Error(
        getAxiosErrorMessage(error, "No se pudo aceptar el viaje"),
        { cause: error }
    );
  }
};

export const iniciarViaje = async (idViaje: number): Promise<void> => {
  try {
    await api.post(`/viaje/${idViaje}/iniciar`);
  } catch (error: unknown) {
    throw new Error(
        getAxiosErrorMessage(error, "No se pudo iniciar el viaje"),
        { cause: error }
    );
  }
};

export const finalizarViaje = async (idViaje: number): Promise<void> => {
  try {
    await api.post(`/viaje/${idViaje}/finalizar`);
  } catch (error: unknown) {
    throw new Error(
        getAxiosErrorMessage(error, "No se pudo finalizar el viaje"),
        { cause: error }
    );
  }
};