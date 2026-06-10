import {api} from "./axios.ts";

interface SolicitarViajeRequestDTO {
    usuarioId: number;
    origen: string;
    destino: string;
    observaciones?: string;
}

interface ViajeDTO {
    id: string;
    origen: string;
    destino: string;
    estado: string;
    fecha: string;
    fechaCreacion?: string;
}

export async function solicitarViaje(data: SolicitarViajeRequestDTO) {
    return await api.post('/viaje', data)
}

export async function recuperarTodosLosViajes(usuarioId: number): Promise<ViajeDTO[]> {
    const response = await api.get(`/usuario/${usuarioId}/viaje`);
    return response.data;
}

export async function cancelarViaje(viajeId: string) {
    return await api.post(`/viaje/${viajeId}/cancelar`);
}