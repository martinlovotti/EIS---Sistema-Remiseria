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
} from "@mui/material";

const historial = [
  {
    id: "#1842",
    origen: "Quilmes Centro",
    destino: "Bernal",
    estado: "Finalizado",
    fecha: "03/06/2026",
  },
  {
    id: "#1831",
    origen: "Don Bosco",
    destino: "Quilmes Oeste",
    estado: "Cancelado",
    fecha: "01/06/2026",
  },
  {
    id: "#1825",
    origen: "Ezpeleta",
    destino: "Bernal",
    estado: "Finalizado",
    fecha: "29/05/2026",
  },
];

function getStatusColor(estado: string) {
  if (estado === "Finalizado") return "success";
  if (estado === "Cancelado") return "default";
  return "warning";
}

export default function UsuarioPage() {
  function handleSolicitarViaje() {
    // TODO: conectar con POST /viaje
    console.log("TODO solicitar viaje");
  }

  function handleFiltrarHistorial() {
    // TODO: conectar con GET /usuario/{id}/viajes?estado=...
    console.log("TODO filtrar historial");
  }

  function handleCancelarViaje(id: string) {
    // TODO: conectar con POST /viaje/{id}/cancelar
    console.log("TODO cancelar viaje", id);
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
              <TextField
                label="Origen"
                placeholder="Ej. Quilmes Centro"
                fullWidth
              />

              <TextField
                label="Destino"
                placeholder="Ej. Bernal"
                fullWidth
              />

              <TextField
                label="Observaciones"
                placeholder="Ej. Viajo con equipaje"
                fullWidth
                multiline
                minRows={3}
              />

              <Button
                variant="contained"
                fullWidth
                onClick={handleSolicitarViaje}
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
                Solicitar viaje
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
              </Typography>

              <Stack direction="row" spacing={1}>
                <Button
                  variant="outlined"
                  onClick={handleFiltrarHistorial}
                  sx={{
                    textTransform: "none",
                    borderColor: "#d0d0d0",
                    color: "#111",
                    "&:hover": {
                      borderColor: "#111",
                      bgcolor: "#fafafa",
                    },
                  }}
                >
                  Ver finalizados
                </Button>

                <Button
                  variant="outlined"
                  onClick={handleFiltrarHistorial}
                  sx={{
                    textTransform: "none",
                    borderColor: "#d0d0d0",
                    color: "#111",
                    "&:hover": {
                      borderColor: "#111",
                      bgcolor: "#fafafa",
                    },
                  }}
                >
                  Ver cancelados
                </Button>
              </Stack>
            </Stack>

            <Stack spacing={2}>
              {historial.map((viaje) => (
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
                          Viaje {viaje.id} · {viaje.fecha}
                        </Typography>
                      </Box>

                      <Chip
                        label={viaje.estado}
                        color={getStatusColor(viaje.estado)}
                        variant="outlined"
                        sx={{ alignSelf: "flex-start" }}
                      />
                    </Stack>

                    <Divider sx={{ my: 2 }} />

                    <Stack direction="row" sx={{ justifyContent: "flex-end" }}>
                      <Button
                        onClick={() => handleCancelarViaje(viaje.id)}
                        sx={{
                          textTransform: "none",
                          color: "#111",
                          fontWeight: 600,
                        }}
                      >
                        Cancelar viaje
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