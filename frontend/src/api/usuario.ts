import { api } from "./axios";
import type { EstadoViaje, ViajeDTO } from "./viaje";

export interface SolicitarViajeRequestDTO {
    usuarioId: number;
    origen: string;
    destino: string;
    observaciones?: string;
}

export async function solicitarViaje(data: SolicitarViajeRequestDTO) {
    return await api.post("/viaje", data);
}

export async function recuperarTodosLosViajes(
    usuarioId: number
): Promise<ViajeDTO[]> {
    const response = await api.get<ViajeDTO[]>(`/usuario/${usuarioId}/viaje`);
    return response.data;
}

export async function recuperarViajesPorEstado(
    usuarioId: number,
    estado: EstadoViaje
): Promise<ViajeDTO[]> {
    const response = await api.get<ViajeDTO[]>(`/usuario/${usuarioId}/viajes`, {
        params: { estado },
    });
    return response.data;
}

export async function cancelarViaje(viajeId: number) {
    return await api.post(`/viaje/${viajeId}/cancelar`);
}

export async function calificarViaje(
    viajeId: number,
    usuarioId: number,
    calificacion: number
) {
    return await api.put(
        `/viaje/${viajeId}/calificar/${usuarioId}/${calificacion}`
    );
}
