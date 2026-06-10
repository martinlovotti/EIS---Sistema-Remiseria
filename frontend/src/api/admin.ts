import { api } from "./axios";

export type ChoferDTO = {
  id: number;
  nombre: string;
  patente: string;
}

export const getChoferConMasViajes = async (): Promise<ChoferDTO> => {
  const response = await api.get<ChoferDTO>("/admin/chofer-mas-viajes");
  return response.data;
};

export const getChoferConMasKm = async (): Promise<ChoferDTO> => {
  const response = await api.get<ChoferDTO>("/admin/chofer-mas-km");
  return response.data;
};

export const getChoferConMasFacturacion = async (): Promise<ChoferDTO> => {
  const response = await api.get<ChoferDTO>("/admin/chofer-mas-facturacion");
  return response.data;
};