import {
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Divider,
  Grid,
  Paper,
  Stack,
  TextField,
  Typography,
  Alert,
} from "@mui/material";
import { useState, useEffect } from "react";
import { solicitarViaje, cancelarViaje, recuperarTodosLosViajes } from "../api/usuario.ts";
import { useAuth } from "../context/AuthContext.tsx";

interface Viaje {
  id: string;
  origen: string;
  destino: string;
  estado: string;
  fechaCreacion?: string;
}

function mapEstado(estadoBackend: string): string {
  const estadoMap: { [key: string]: string } = {
    "FINALIZADO": "Finalizado",
    "CANCELADO": "Cancelado",
    "PENDIENTE": "Pendiente",
    "ACEPTADO": "Aceptado",
    "EN_CURSO": "En curso",
  };
  return estadoMap[estadoBackend] || estadoBackend;
}

function formatearFecha(fechaCreacion?: string): string {
  if (!fechaCreacion) return "";
  try {
    [date, time] = fechaCreacion.split(" ")
    [year, month, day] = date.split("/")
    [hour, minute] = time.split(":")
    const fecha = new Date(year, month-1, day, hour, minute);
    return fecha.toLocaleDateString("es-AR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
      hour12: false
    })
  } catch {
    return fechaCreacion;
  }
}

function getStatusColor(estado: string) {
  if (estado === "Finalizado") return "success";
  if (estado === "Cancelado") return "default";
  if (estado === "En curso") return "warning";
  if (estado === "Aceptado") return "info";
  return "default";
}

export default function UsuarioPage() {
  const { entidadId } = useAuth();
  const [origen, setOrigen] = useState("");
  const [destino, setDestino] = useState("");
  const [observaciones, setObservaciones] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [errorHistorial, setErrorHistorial] = useState("");
  const [success, setSuccess] = useState("");
  const [filtroEstado, setFiltroEstado] = useState<string | null>(null);
  const [viajesFiltrados, setViajesFiltrados] = useState<Viaje[]>([]);
  const [todosLosViajes, setTodosLosViajes] = useState<Viaje[]>([]);
  const [cargandoHistorial, setCargandoHistorial] = useState(true);

  useEffect(() => {
    cargarHistorial();
  }, []);

  async function cargarHistorial() {
    if (!entidadId) {
      setErrorHistorial("No se pudo obtener tu ID de usuario");
      setCargandoHistorial(false);
      return;
    }

    try {
      setCargandoHistorial(true);
      const viajes = await recuperarTodosLosViajes(entidadId);
      setTodosLosViajes(viajes);
      setViajesFiltrados(viajes);
    } catch (err) {
      setErrorHistorial(
        err instanceof Error ? err.response.data.message : "Error al cargar el historial"
      );
    } finally {
      setCargandoHistorial(false);
    }
  }

  async function handleSolicitarViaje() {
    if (!origen.trim() || !destino.trim()) {
      setError("Por favor completa origen y destino");
      return;
    }

    if (!entidadId) {
      setError("No se pudo obtener tu ID de usuario");
      return;
    }

    setLoading(true);
    setError("");
    setSuccess("");

    try {
      await solicitarViaje({
        usuarioId: entidadId,
        origen,
        destino,
        observaciones: observaciones || undefined,
      });
      setSuccess("¡Viaje solicitado correctamente!");
      setOrigen("");
      setDestino("");
      setObservaciones("");
      await cargarHistorial();
    } catch (err) {
      setError(
        err instanceof Error ? err.response?.data?.message : "Error al solicitar el viaje"
      );
    } finally {
      setLoading(false);
    }
  }

  async function handleFiltrarHistorial(estado: "FINALIZADO" | "CANCELADO") {
    if (filtroEstado === estado) {
      setFiltroEstado(null);
      setViajesFiltrados(todosLosViajes);
      return;
    }

    const viajesOriginales = todosLosViajes.filter(
      (v) => v.estado === estado.toUpperCase()
    );
    setViajesFiltrados(viajesOriginales);
    setFiltroEstado(estado);
  }

  async function handleCancelarViaje(id: string) {
    if (!confirm("¿Estás seguro de que deseas cancelar este viaje?")) {
      return;
    }

    setLoading(true);
    setError("");

    try {
      await cancelarViaje(id);
      setSuccess("Viaje cancelado correctamente");
      await cargarHistorial();
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "Error al cancelar el viaje"
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <Box sx={{ maxWidth: 1200, mx: "auto" }}>
      <Stack spacing={1} sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ fontWeight: 700, color: "#111" }}>
          Tus viajes
        </Typography>
        <Typography variant="body1" sx={{ color: "#666" }}>
          Solicitá un viaje y revisá tu historial.
        </Typography>
      </Stack>

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, md: 5 }}>
          <Paper
            elevation={0}
            sx={{
              p: 3,
              borderRadius: 3,
              border: "1px solid #e8e8e8",
              bgcolor: "#fff",
              height: "100%",
            }}
          >
            <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
              Solicitar viaje
            </Typography>

            <Stack spacing={2}>
              {error && <Alert severity="error">{error}</Alert>}
              {success && <Alert severity="success">{success}</Alert>}

              <TextField
                label="Origen"
                placeholder="Ej. Quilmes Centro"
                fullWidth
                value={origen}
                onChange={(e) => setOrigen(e.target.value)}
                disabled={loading}
              />

              <TextField
                label="Destino"
                placeholder="Ej. Bernal"
                fullWidth
                value={destino}
                onChange={(e) => setDestino(e.target.value)}
                disabled={loading}
              />

              <TextField
                label="Observaciones"
                placeholder="Ej. Viajo con equipaje"
                fullWidth
                multiline
                minRows={3}
                value={observaciones}
                onChange={(e) => setObservaciones(e.target.value)}
                disabled={loading}
              />

              <Button
                variant="contained"
                fullWidth
                onClick={handleSolicitarViaje}
                disabled={loading}
                sx={{
                  py: 1.4,
                  mt: 1,
                  bgcolor: "#111",
                  color: "#fff",
                  textTransform: "none",
                  fontWeight: 700,
                  borderRadius: 2,
                  boxShadow: "none",
                  "&:hover": {
                    bgcolor: "#000",
                    boxShadow: "none",
                  },
                }}
              >
                {loading ? "Enviando..." : "Solicitar viaje"}
              </Button>
            </Stack>
          </Paper>
        </Grid>

        <Grid size={{ xs: 12, md: 7 }}>
          <Paper
            elevation={0}
            sx={{
              p: 3,
              borderRadius: 3,
              border: "1px solid #e8e8e8",
              bgcolor: "#fff",
            }}
          >
            <Stack
              direction={{ xs: "column", sm: "row" }}
              spacing={2}
              sx={{
                mb: 2,
                justifyContent: "space-between",
                alignItems: { xs: "stretch", sm: "center" },
              }}
            >
              <Typography variant="h6" sx={{ fontWeight: 700 }}>
                Historial
                {filtroEstado && ` (${filtroEstado})`}
              </Typography>

              <Stack direction="row" spacing={1}>
                <Button
                  variant={filtroEstado === "Finalizado" ? "contained" : "outlined"}
                  onClick={() => handleFiltrarHistorial("Finalizado")}
                  disabled={loading || cargandoHistorial}
                  sx={{
                    textTransform: "none",
                    borderColor: "#d0d0d0",
                    color: filtroEstado === "Finalizado" ? "#fff" : "#111",
                    bgcolor: filtroEstado === "Finalizado" ? "#111" : "transparent",
                    "&:hover": {
                      borderColor: "#111",
                      bgcolor: filtroEstado === "Finalizado" ? "#000" : "#fafafa",
                    },
                  }}
                >
                  Ver finalizados
                </Button>

                <Button
                  variant={filtroEstado === "Cancelado" ? "contained" : "outlined"}
                  onClick={() => handleFiltrarHistorial("Cancelado")}
                  disabled={loading || cargandoHistorial}
                  sx={{
                    textTransform: "none",
                    borderColor: "#d0d0d0",
                    color: filtroEstado === "Cancelado" ? "#fff" : "#111",
                    bgcolor: filtroEstado === "Cancelado" ? "#111" : "transparent",
                    "&:hover": {
                      borderColor: "#111",
                      bgcolor: filtroEstado === "Cancelado" ? "#000" : "#fafafa",
                    },
                  }}
                >
                  Ver cancelados
                </Button>
              </Stack>
            </Stack>

            <Stack spacing={2}>
              {cargandoHistorial ? (
                <Typography sx={{ color: "#999", textAlign: "center", py: 4 }}>
                  Cargando historial...
                </Typography>
              ) : errorHistorial ?
                  <Alert severity="error">{errorHistorial}</Alert> :
                  viajesFiltrados.length === 0 ? (
                <Typography sx={{ color: "#999", textAlign: "center", py: 4 }}>
                  {filtroEstado
                    ? `No hay viajes ${filtroEstado.toLowerCase()}`
                    : "No hay viajes aún"}
                </Typography>
              ) : (
                viajesFiltrados.map((viaje) => (
                  <Card
                    key={viaje.id}
                    elevation={0}
                    sx={{
                      borderRadius: 3,
                      border: "1px solid #ececec",
                      bgcolor: "#fcfcfc",
                    }}
                  >
                    <CardContent>
                      <Stack
                        direction={{ xs: "column", sm: "row" }}
                        spacing={2}
                        sx={{
                          mb: 2,
                          justifyContent: "space-between",
                          alignItems: { xs: "stretch", sm: "center" },
                        }}
                      >
                        <Box>
                          <Typography sx={{ fontWeight: 700, color: "#111" }}>
                            {viaje.origen} → {viaje.destino}
                          </Typography>
                          <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                            Viaje #{viaje.id} · {formatearFecha(viaje.fechaCreacion)}
                          </Typography>
                        </Box>

                        <Chip
                          label={mapEstado(viaje.estado)}
                          color={getStatusColor(mapEstado(viaje.estado)) as any}
                          variant="outlined"
                          sx={{ alignSelf: "flex-start" }}
                        />
                      </Stack>

                      <Divider sx={{ my: 2 }} />

                      <Stack direction="row" sx={{ justifyContent: "flex-end" }}>
                        <Button
                          onClick={() => handleCancelarViaje(viaje.id)}
                          disabled={loading || viaje.estado === "CANCELADO" || viaje.estado === "FINALIZADO" || viaje.estado === "EN_CURSO"}
                          sx={{
                            textTransform: "none",
                            color: "#111",
                            fontWeight: 600,
                          }}
                        >
                          {viaje.estado === "CANCELADO" ? "Cancelado" : "Cancelar viaje"}
                        </Button>
                      </Stack>
                    </CardContent>
                  </Card>
                ))
              )}
            </Stack>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}