import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Divider,
  Grid,
  Paper,
  Rating,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import {
  solicitarViaje,
  cancelarViaje,
  recuperarTodosLosViajes,
  calificarViaje,
} from "../api/usuario";
import type { ViajeDTO } from "../api/viaje";
import { useAuth } from "../context/AuthContext";

function mapEstado(estadoBackend: string): string {
  const estadoMap: Record<string, string> = {
    FINALIZADO: "Finalizado",
    CANCELADO: "Cancelado",
    PENDIENTE: "Pendiente",
    ACEPTADO: "Aceptado",
    EN_CURSO: "En curso",
  };

  return estadoMap[estadoBackend] || estadoBackend;
}

function formatearFecha(fechaCreacion?: string): string {
  if (!fechaCreacion) return "";
  return fechaCreacion;
}

function formatPrecio(precio?: number | null): string {
  if (precio == null) return "-";
  return `$${precio.toFixed(2)}`;
}

function formatDistancia(distancia?: number | null): string {
  if (distancia == null) return "-";
  return `${distancia.toFixed(1)} km`;
}

function formatCalificacion(calificacion?: number | null): string {
  if (calificacion == null) return "-";
  return calificacion.toFixed(1);
}

function getStatusColor(estado: string) {
  if (estado === "Finalizado") return "success";
  if (estado === "Cancelado") return "default";
  if (estado === "En curso") return "warning";
  if (estado === "Aceptado") return "info";
  return "default";
}

function getErrorMessage(err: unknown, fallback: string): string {
  return err instanceof Error ? err.message : fallback;
}

export default function UsuarioPage() {
  const { entidadId } = useAuth();

  const [origen, setOrigen] = useState("");
  const [destino, setDestino] = useState("");
  const [observaciones, setObservaciones] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [errorHistorial, setErrorHistorial] = useState("");

  const [successSolicitud, setSuccessSolicitud] = useState("");
  const [successCalificacion, setSuccessCalificacion] = useState("");

  const [filtroEstado, setFiltroEstado] = useState<"FINALIZADO" | "CANCELADO" | null>(null);
  const [viajesFiltrados, setViajesFiltrados] = useState<ViajeDTO[]>([]);
  const [todosLosViajes, setTodosLosViajes] = useState<ViajeDTO[]>([]);
  const [cargandoHistorial, setCargandoHistorial] = useState(true);

  const [ratingDrafts, setRatingDrafts] = useState<Record<number, number | null>>({});
  const [ratingLoadingId, setRatingLoadingId] = useState<number | null>(null);

  useEffect(() => {
    void cargarHistorial();
  }, [entidadId]);

  async function cargarHistorial() {
    if (!entidadId) {
      setErrorHistorial("No se pudo obtener tu ID de usuario");
      setCargandoHistorial(false);
      return;
    }

    try {
      setCargandoHistorial(true);
      setErrorHistorial("");

      const viajes = await recuperarTodosLosViajes(entidadId);
      setTodosLosViajes(viajes);

      if (filtroEstado) {
        setViajesFiltrados(viajes.filter((v) => v.estado === filtroEstado));
      } else {
        setViajesFiltrados(viajes);
      }
    } catch (err) {
      setErrorHistorial(getErrorMessage(err, "Error al cargar el historial"));
    } finally {
      setCargandoHistorial(false);
    }
  }

  async function handleSolicitarViaje() {
    if (!origen.trim() || !destino.trim()) {
      setError("Por favor completá origen y destino");
      return;
    }

    if (!entidadId) {
      setError("No se pudo obtener tu ID de usuario");
      return;
    }

    setLoading(true);
    setError("");
    setSuccessSolicitud("");

    try {
      await solicitarViaje({
        usuarioId: entidadId,
        origen,
        destino,
        observaciones: observaciones || undefined,
      });

      setSuccessSolicitud("¡Viaje solicitado correctamente!");
      setOrigen("");
      setDestino("");
      setObservaciones("");

      await cargarHistorial();
    } catch (err) {
      setError(getErrorMessage(err, "Error al solicitar el viaje"));
    } finally {
      setLoading(false);
    }
  }

  function handleFiltrarHistorial(estado: "FINALIZADO" | "CANCELADO") {
    if (filtroEstado === estado) {
      setFiltroEstado(null);
      setViajesFiltrados(todosLosViajes);
      return;
    }

    setFiltroEstado(estado);
    setViajesFiltrados(todosLosViajes.filter((v) => v.estado === estado));
  }

  async function handleCancelarViaje(id: number) {
    if (!window.confirm("¿Estás segura de que querés cancelar este viaje?")) {
      return;
    }

    setLoading(true);
    setError("");
    setSuccessSolicitud("");

    try {
      await cancelarViaje(id);
      setSuccessSolicitud("Viaje cancelado correctamente");
      await cargarHistorial();
    } catch (err) {
      setError(getErrorMessage(err, "Error al cancelar el viaje"));
    } finally {
      setLoading(false);
    }
  }

  async function handleCalificarViaje(viajeId: number) {
    if (!entidadId) {
      setError("No se pudo obtener tu ID de usuario");
      return;
    }

    const calificacion = ratingDrafts[viajeId];

    if (!calificacion || calificacion < 1 || calificacion > 5) {
      setError("Seleccioná una calificación entre 1 y 5 estrellas");
      return;
    }

    setError("");
    setSuccessCalificacion("");
    setRatingLoadingId(viajeId);

    try {
      await calificarViaje(viajeId, entidadId, calificacion);
      setSuccessCalificacion("Calificación guardada correctamente");
      await cargarHistorial();

      setRatingDrafts((prev) => ({
        ...prev,
        [viajeId]: null,
      }));
    } catch (err) {
      setError(getErrorMessage(err, "Error al calificar el viaje"));
    } finally {
      setRatingLoadingId(null);
    }
  }

  const viajeActual =
      todosLosViajes.find(
          (viaje) =>
              viaje.estado === "PENDIENTE" ||
              viaje.estado === "ACEPTADO" ||
              viaje.estado === "EN_CURSO"
      ) ?? null;

  const viajesParaMostrarEnHistorial = viajesFiltrados.filter(
      (viaje) => viaje.id !== viajeActual?.id
  );

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
                {successSolicitud && <Alert severity="success">{successSolicitud}</Alert>}

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
            {viajeActual && (
                <Paper
                    elevation={0}
                    sx={{
                      p: 3,
                      mb: 3,
                      borderRadius: 3,
                      border: "1px solid #e8e8e8",
                      bgcolor: "#fff",
                    }}
                >
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                    Viaje actual
                  </Typography>

                  <Card
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
                            {viajeActual.origen} → {viajeActual.destino}
                          </Typography>

                          <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                            Viaje #{viajeActual.id} · {formatearFecha(viajeActual.fechaCreacion)}
                          </Typography>

                          <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                            Precio: {formatPrecio(viajeActual.precio)}
                          </Typography>

                          <Typography variant="body2" sx={{ color: "#666" }}>
                            Distancia: {formatDistancia(viajeActual.distancia)}
                          </Typography>

                          {viajeActual.chofer && (
                              <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                                Chofer: {viajeActual.chofer.nombre} · {viajeActual.chofer.patente}
                              </Typography>
                          )}
                        </Box>

                        <Chip
                            label={mapEstado(viajeActual.estado)}
                            color={getStatusColor(mapEstado(viajeActual.estado)) as any}
                            variant="outlined"
                            sx={{ alignSelf: "flex-start" }}
                        />
                      </Stack>

                      <Divider sx={{ my: 2 }} />

                      <Stack direction="row" sx={{ justifyContent: "flex-end" }}>
                        <Button
                            onClick={() => handleCancelarViaje(viajeActual.id)}
                            disabled={loading || viajeActual.estado !== "PENDIENTE"}
                            sx={{
                              textTransform: "none",
                              color: "#111",
                              fontWeight: 600,
                            }}
                        >
                          {viajeActual.estado === "PENDIENTE"
                              ? "Cancelar viaje"
                              : mapEstado(viajeActual.estado)}
                        </Button>
                      </Stack>
                    </CardContent>
                  </Card>
                </Paper>
            )}

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
                  {filtroEstado && ` (${mapEstado(filtroEstado)})`}
                </Typography>

                <Stack direction="row" spacing={1}>
                  <Button
                      variant={filtroEstado === "FINALIZADO" ? "contained" : "outlined"}
                      onClick={() => handleFiltrarHistorial("FINALIZADO")}
                      disabled={loading || cargandoHistorial}
                      sx={{
                        textTransform: "none",
                        borderColor: "#d0d0d0",
                        color: filtroEstado === "FINALIZADO" ? "#fff" : "#111",
                        bgcolor: filtroEstado === "FINALIZADO" ? "#111" : "transparent",
                        "&:hover": {
                          borderColor: "#111",
                          bgcolor: filtroEstado === "FINALIZADO" ? "#000" : "#fafafa",
                        },
                      }}
                  >
                    Ver finalizados
                  </Button>

                  <Button
                      variant={filtroEstado === "CANCELADO" ? "contained" : "outlined"}
                      onClick={() => handleFiltrarHistorial("CANCELADO")}
                      disabled={loading || cargandoHistorial}
                      sx={{
                        textTransform: "none",
                        borderColor: "#d0d0d0",
                        color: filtroEstado === "CANCELADO" ? "#fff" : "#111",
                        bgcolor: filtroEstado === "CANCELADO" ? "#111" : "transparent",
                        "&:hover": {
                          borderColor: "#111",
                          bgcolor: filtroEstado === "CANCELADO" ? "#000" : "#fafafa",
                        },
                      }}
                  >
                    Ver cancelados
                  </Button>
                </Stack>
              </Stack>

              {successCalificacion && (
                  <Alert severity="success" sx={{ mb: 2 }}>
                    {successCalificacion}
                  </Alert>
              )}

              <Stack spacing={2}>
                {cargandoHistorial ? (
                    <Typography sx={{ color: "#999", textAlign: "center", py: 4 }}>
                      Cargando historial...
                    </Typography>
                ) : errorHistorial ? (
                    <Alert severity="error">{errorHistorial}</Alert>
                ) : viajesParaMostrarEnHistorial.length === 0 ? (
                    <Typography sx={{ color: "#999", textAlign: "center", py: 4 }}>
                      {filtroEstado
                          ? `No hay viajes ${mapEstado(filtroEstado).toLowerCase()}`
                          : "No hay viajes aún"}
                    </Typography>
                ) : (
                    viajesParaMostrarEnHistorial.map((viaje) => (
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

                                <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                                  Precio: {formatPrecio(viaje.precio)}
                                </Typography>

                                <Typography variant="body2" sx={{ color: "#666" }}>
                                  Distancia: {formatDistancia(viaje.distancia)}
                                </Typography>

                                {viaje.calificacion != null && (
                                    <Box sx={{ mt: 1 }}>
                                      <Typography variant="body2" sx={{ color: "#666", mb: 0.5 }}>
                                        Calificación: {formatCalificacion(viaje.calificacion)}
                                      </Typography>
                                      <Rating value={viaje.calificacion} precision={0.5} readOnly />
                                    </Box>
                                )}

                                {viaje.chofer && (
                                    <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                                      Chofer: {viaje.chofer.nombre} · {viaje.chofer.patente}
                                    </Typography>
                                )}
                              </Box>

                              <Chip
                                  label={mapEstado(viaje.estado)}
                                  color={getStatusColor(mapEstado(viaje.estado)) as any}
                                  variant="outlined"
                                  sx={{ alignSelf: "flex-start" }}
                              />
                            </Stack>

                            <Divider sx={{ my: 2 }} />

                            {viaje.estado === "FINALIZADO" && viaje.calificacion == null && (
                                <Box sx={{ mb: 2 }}>
                                  <Typography
                                      variant="body2"
                                      sx={{ color: "#111", fontWeight: 600, mb: 1 }}
                                  >
                                    Calificá este viaje
                                  </Typography>

                                  <Stack
                                      direction={{ xs: "column", sm: "row" }}
                                      spacing={2}
                                      sx={{
                                        justifyContent: "space-between",
                                        alignItems: { xs: "stretch", sm: "center" },
                                      }}
                                  >
                                    <Rating
                                        name={`rating-${viaje.id}`}
                                        value={ratingDrafts[viaje.id] ?? 0}
                                        onChange={(_, newValue) => {
                                          setRatingDrafts((prev) => ({
                                            ...prev,
                                            [viaje.id]: newValue,
                                          }));
                                        }}
                                    />

                                    <Button
                                        variant="contained"
                                        onClick={() => handleCalificarViaje(viaje.id)}
                                        disabled={
                                            ratingLoadingId === viaje.id ||
                                            !ratingDrafts[viaje.id]
                                        }
                                        sx={{
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
                                      {ratingLoadingId === viaje.id
                                          ? "Guardando..."
                                          : "Guardar calificación"}
                                    </Button>
                                  </Stack>
                                </Box>
                            )}

                            <Stack direction="row" sx={{ justifyContent: "flex-end" }}>
                              <Button
                                  onClick={() => handleCancelarViaje(viaje.id)}
                                  disabled={
                                      loading ||
                                      viaje.estado === "CANCELADO" ||
                                      viaje.estado === "FINALIZADO" ||
                                      viaje.estado === "EN_CURSO"
                                  }
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