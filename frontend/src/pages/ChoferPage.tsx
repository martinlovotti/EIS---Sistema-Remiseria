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
  Typography,
} from "@mui/material";

const viajesDisponibles = [
  {
    id: 27,
    origen: "Quilmes Centro",
    destino: "Bernal",
    pasajero: "Juan Pérez",
    estado: "Pendiente",
  },
  {
    id: 31,
    origen: "Don Bosco",
    destino: "Ezpeleta",
    pasajero: "Ana Gómez",
    estado: "Pendiente",
  },
  {
    id: 35,
    origen: "Villa Argentina",
    destino: "Quilmes Oeste",
    pasajero: "Luis Fernández",
    estado: "Pendiente",
  },
];

const viajesActivos = [
  {
    id: 18,
    origen: "Bernal",
    destino: "Quilmes Centro",
    pasajero: "María López",
    estado: "Aceptado",
  },
];

export default function ChoferPage() {
  function handleAceptarViaje(id: number) {
    // TODO: conectar con PATCH /viaje/{idViaje}/aceptarViaje/{idChofer}
    console.log("TODO aceptar viaje", id);
  }

  function handleIniciarViaje(id: number) {
    // TODO: conectar con POST /viaje/{id}/iniciar
    console.log("TODO iniciar viaje", id);
  }

  function handleFinalizarViaje(id: number) {
    // TODO: conectar con POST /viaje/{id}/finalizar
    console.log("TODO finalizar viaje", id);
  }

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
              {viajesDisponibles.map((viaje) => (
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
                          Pasajero: {viaje.pasajero}
                        </Typography>
                        <Typography variant="body2" sx={{ color: "#666" }}>
                          Viaje #{viaje.id}
                        </Typography>
                      </Box>

                      <Chip
                        label={viaje.estado}
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
              ))}
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
              {viajesActivos.map((viaje) => (
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
                    <Typography sx={{ fontWeight: 700, color: "#111" }}>
                      {viaje.origen} → {viaje.destino}
                    </Typography>

                    <Typography variant="body2" sx={{ color: "#666", mt: 0.5 }}>
                      Pasajero: {viaje.pasajero}
                    </Typography>

                    <Typography variant="body2" sx={{ color: "#666" }}>
                      Viaje #{viaje.id}
                    </Typography>

                    <Divider sx={{ my: 2 }} />

                    <Stack spacing={1.5}>
                      <Button
                        variant="outlined"
                        fullWidth
                        onClick={() => handleIniciarViaje(viaje.id)}
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

                      <Button
                        variant="contained"
                        fullWidth
                        onClick={() => handleFinalizarViaje(viaje.id)}
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
                    </Stack>
                  </CardContent>
                </Card>
              ))}
            </Stack>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}