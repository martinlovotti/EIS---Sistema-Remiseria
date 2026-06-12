import { api } from './axios';
import axios from 'axios';

export type UsuarioDTO = {
  id: number;
  nombre: string;
};

export type ChoferDTO = {
  id: number;
  nombre: string;
  patente: string;
};

export type ViajeDTO = {
  id: number;
  origen: string;
  destino: string;
  estado: string;
  usuario: UsuarioDTO;
  chofer: ChoferDTO | null;
  calificacion: number | null;
};

export const getViajesDisponibles = async (): Promise<ViajeDTO[]> => {
  const response = await api.get<ViajeDTO[]>('/viaje/solicitados');
  return response.data;
};

export const aceptarViaje = async (
  idViaje: number,
  idChofer: number,
): Promise<void> => {
  try {
    await api.patch(`/viaje/${idViaje}/aceptarViaje/${idChofer}`);
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data ?? 'No se pudo aceptar el viaje', {
        cause: error,
      });
    }

    throw new Error('Error inesperado', { cause: error });
  }
};

export const iniciarViaje = async (idViaje: number): Promise<void> => {
  try {
    await api.post(`/viaje/${idViaje}/iniciar`);
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data ?? 'No se pudo iniciar el viaje', {
        cause: error,
      });
    }

    throw new Error('Error inesperado', { cause: error });
  }
};

export const finalizarViaje = async (idViaje: number): Promise<void> => {
  try {
    await api.post(`/viaje/${idViaje}/finalizar`);
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data ?? 'No se pudo finalizar el viaje', {
        cause: error,
      });
    }

    throw new Error('Error inesperado', { cause: error });
  }
};

export const getViajes = async (): Promise<ViajeDTO[]> => {
  const response = await api.get<ViajeDTO[]>('/viaje');
  return response.data;
};
