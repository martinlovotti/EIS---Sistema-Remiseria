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
  Snackbar,
  Stack,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import {
  getViajes,
  aceptarViaje,
  iniciarViaje,
  finalizarViaje,
  type ViajeDTO,
} from "../api/viaje";

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

function parseFechaCreacion(fechaCreacion?: string): number {
  if (!fechaCreacion) return 0;

  try {
    const [fecha, hora] = fechaCreacion.split(" ");
    const [dia, mes, anio] = fecha.split("/").map(Number);
    const [horas, minutos] = hora.split(":").map(Number);

    return new Date(anio, mes - 1, dia, horas, minutos).getTime();
  } catch {
    return 0;
  }
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

function getErrorMessage(err: unknown, fallback: string): string {
  return err instanceof Error ? err.message : fallback;
}

export default function ChoferPage() {
  const { entidadId } = useAuth();

  const [viajes, setViajes] = useState<ViajeDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [viajeActivo, setViajeActivo] = useState<ViajeDTO | null>(null);
  const [viajeIniciado, setViajeIniciado] = useState(false);

  useEffect(() => {
    void fetchViajes();
  }, []);

  useEffect(() => {
    const activo = viajes.find(
        (v) =>
            v.chofer?.id === entidadId &&
            ["ACEPTADO", "EN_CURSO"].includes(v.estado)
    );

    setViajeActivo(activo ?? null);
    setViajeIniciado(activo?.estado === "EN_CURSO");
  }, [viajes, entidadId]);

  async function fetchViajes() {
    try {
      setLoading(true);
      const viajesRecuperados = await getViajes();
      setViajes(viajesRecuperados);
    } catch (err) {
      setError(getErrorMessage(err, "Error al cargar los viajes"));
      setOpenSnackbar(true);
    } finally {
      setLoading(false);
    }
  }

  async function handleAceptarViaje(id: number) {
    try {
      if (!entidadId) {
        throw new Error("No se encontró el id del chofer");
      }

      await aceptarViaje(id, entidadId);
      await fetchViajes();
    } catch (err) {
      setError(getErrorMessage(err, "No se pudo aceptar el viaje"));
      setOpenSnackbar(true);
    }
  }

  async function handleIniciarViaje(id: number) {
    try {
      await iniciarViaje(id);
      await fetchViajes();
    } catch (err) {
      setError(getErrorMessage(err, "No se pudo iniciar el viaje"));
      setOpenSnackbar(true);
    }
  }

  async function handleFinalizarViaje(id: number) {
    try {
      await finalizarViaje(id);
      await fetchViajes();
    } catch (err) {
      setError(getErrorMessage(err, "No se pudo finalizar el viaje"));
      setOpenSnackbar(true);
    }
  }

  const viajesDisponibles = viajes
      .filter((viaje) => viaje.estado === "PENDIENTE")
      .sort(
          (a, b) => parseFechaCreacion(b.fechaCreacion) - parseFechaCreacion(a.fechaCreacion)
      );


  return (
      <Box sx={{ maxWidth: 1200, mx: "auto" }}>
        <Stack spacing={1} sx={{ mb: 4 }}>
          <Typography variant="h4" sx={{ fontWeight: 700, color: "#111" }}>
            Panel de chofer
          </Typography>
          <Typography variant="body1" sx={{ color: "#666" }}>
            Revisá viajes disponibles y gestioná tus viajes activos.
          </Typography>
        </Stack>

        <Grid container spacing={3}>
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
              <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                Viajes disponibles
              </Typography>

              <Stack spacing={2}>
                {loading ? (
                    <Typography>Cargando viajes...</Typography>
                ) : viajesDisponibles.length === 0 ? (
                    <Typography sx={{ color: "#999", textAlign: "center", py: 4 }}>
                      No hay viajes pendientes para aceptar.
                    </Typography>
                ) : (
                    viajesDisponibles.map((viaje) => (
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
                                  justifyContent: "space-between",
                                  alignItems: { xs: "flex-start", sm: "center" },
                                }}
                            >
                              <Box>
                                <Typography sx={{ fontWeight: 700, color: "#111" }}>
                                  {viaje.origen} → {viaje.destino}
                                </Typography>

                                <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                                  Pasajero: {viaje.usuario.nombre}
                                </Typography>

                                <Typography variant="body2" sx={{ color: "#666" }}>
                                  Viaje #{viaje.id} · {formatearFecha(viaje.fechaCreacion)}
                                </Typography>

                                <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                                  Precio: {formatPrecio(viaje.precio)}
                                </Typography>

                                <Typography variant="body2" sx={{ color: "#666" }}>
                                  Distancia: {formatDistancia(viaje.distancia)}
                                </Typography>

                                {viaje.calificacion != null && (
                                    <Typography variant="body2" sx={{ color: "#666" }}>
                                      Calificación: {formatCalificacion(viaje.calificacion)}
                                    </Typography>
                                )}
                              </Box>

                              <Chip
                                  label={mapEstado(viaje.estado)}
                                  variant="outlined"
                                  sx={{ alignSelf: "flex-start" }}
                              />
                            </Stack>

                            <Divider sx={{ my: 2 }} />

                            <Stack
                                direction="row"
                                sx={{
                                  justifyContent: "flex-end",
                                }}
                            >
                              <Button
                                  variant="contained"
                                  onClick={() => handleAceptarViaje(viaje.id)}
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
                                Aceptar viaje
                              </Button>
                            </Stack>
                          </CardContent>
                        </Card>
                    ))
                )}
              </Stack>
            </Paper>
          </Grid>

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
                Viaje activo
              </Typography>

              <Stack spacing={2}>
                {!viajeActivo ? (
                    <Typography sx={{ color: "#999" }}>
                      No tenés ningún viaje activo.
                    </Typography>
                ) : (
                    <Card
                        key={viajeActivo.id}
                        elevation={0}
                        sx={{
                          borderRadius: 3,
                          border: "1px solid #ececec",
                          bgcolor: "#fcfcfc",
                        }}
                    >
                      <CardContent>
                        <Typography sx={{ fontWeight: 700, color: "#111" }}>
                          {viajeActivo.origen} → {viajeActivo.destino}
                        </Typography>

                        <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                          Pasajero: {viajeActivo.usuario.nombre}
                        </Typography>

                        <Typography variant="body2" sx={{ color: "#666" }}>
                          Viaje #{viajeActivo.id} · {formatearFecha(viajeActivo.fechaCreacion)}
                        </Typography>

                        <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                          Precio: {formatPrecio(viajeActivo.precio)}
                        </Typography>

                        <Typography variant="body2" sx={{ color: "#666" }}>
                          Distancia: {formatDistancia(viajeActivo.distancia)}
                        </Typography>

                        {viajeActivo.calificacion != null && (
                            <Typography variant="body2" sx={{ color: "#666" }}>
                              Calificación: {formatCalificacion(viajeActivo.calificacion)}
                            </Typography>
                        )}

                        <Divider sx={{ my: 2 }} />

                        <Stack spacing={1.5}>
                          {!viajeIniciado && (
                              <Button
                                  variant="outlined"
                                  fullWidth
                                  onClick={() => handleIniciarViaje(viajeActivo.id)}
                                  sx={{
                                    textTransform: "none",
                                    borderColor: "#d0d0d0",
                                    color: "#111",
                                    fontWeight: 600,
                                    "&:hover": {
                                      borderColor: "#111",
                                      bgcolor: "#fafafa",
                                    },
                                  }}
                              >
                                Iniciar viaje
                              </Button>
                          )}

                          {viajeIniciado && (
                              <Button
                                  variant="contained"
                                  fullWidth
                                  onClick={() => handleFinalizarViaje(viajeActivo.id)}
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
                                Finalizar viaje
                              </Button>
                          )}
                        </Stack>
                      </CardContent>
                    </Card>
                )}
              </Stack>
            </Paper>
          </Grid>
        </Grid>

        <Snackbar
            open={openSnackbar}
            autoHideDuration={4000}
            onClose={() => setOpenSnackbar(false)}
        >
          <Alert severity="error" onClose={() => setOpenSnackbar(false)}>
            {error}
          </Alert>
        </Snackbar>
      </Box>
  );
}