import {
  Box,
  Button,
  Card,
  CardContent,
  Divider,
  Grid,
  Paper,
  Stack,
  Typography,
} from "@mui/material";

const metricas = [
  { titulo: "Viajes totales", valor: "1.284" },
  { titulo: "Viajes finalizados", valor: "1.102" },
  { titulo: "Viajes cancelados", valor: "182" },
  { titulo: "Choferes activos", valor: "24" },
];

const actividadReciente = [
  "Chofer #12 finalizó un viaje en Bernal",
  "Usuario #5 solicitó un nuevo viaje",
  "Chofer #7 aceptó un viaje en Quilmes Centro",
  "Se canceló un viaje en Don Bosco",
];

export default function AdminPage() {
  function handleVerChoferConMasViajes() {
    // TODO: conectar con GET /admin/chofer-mas-viajes
    console.log("TODO ver chofer con más viajes");
  }

  function handleVerReporte() {
    // TODO: conectar con métricas adicionales
    console.log("TODO ver reporte");
  }

  return (
    <Box sx={{ maxWidth: 1200, mx: "auto" }}>
      <Stack spacing={1} sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ fontWeight: 700, color: "#111" }}>
          Panel de administración
        </Typography>
        <Typography variant="body1" sx={{ color: "#666" }}>
          Visualizá métricas generales y actividad reciente del sistema.
        </Typography>
      </Stack>

      <Grid container spacing={3}>
        {metricas.map((metrica) => (
          <Grid key={metrica.titulo} size={{ xs: 12, sm: 6, md: 3 }}>
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
              <Typography variant="body2" sx={{ color: "#666", mb: 1 }}>
                {metrica.titulo}
              </Typography>
              <Typography variant="h4" sx={{ fontWeight: 700, color: "#111" }}>
                {metrica.valor}
              </Typography>
            </Paper>
          </Grid>
        ))}
      </Grid>

      <Grid container spacing={3} sx={{ mt: 0.5 }}>
        <Grid size={{ xs: 12, md: 7 }}>
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
                Métricas destacadas
              </Typography>

              <Button
                variant="outlined"
                onClick={handleVerReporte}
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
                Ver reporte completo
              </Button>
            </Stack>

            <Card
              elevation={0}
              sx={{
                borderRadius: 3,
                border: "1px solid #ececec",
                bgcolor: "#fcfcfc",
              }}
            >
              <CardContent>
                <Typography sx={{ fontWeight: 700, color: "#111", mb: 1 }}>
                  Chofer con más viajes
                </Typography>

                <Typography variant="body1" sx={{ color: "#666" }}>
                  Consultá cuál es el chofer con mayor cantidad de viajes realizados.
                </Typography>

                <Divider sx={{ my: 2 }} />

                <Button
                  variant="contained"
                  onClick={handleVerChoferConMasViajes}
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
                  Ver chofer destacado
                </Button>
              </CardContent>
            </Card>
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
              Actividad reciente
            </Typography>

            <Stack spacing={1.5}>
              {actividadReciente.map((item, index) => (
                <Box
                  key={index}
                  sx={{
                    p: 2,
                    borderRadius: 2,
                    bgcolor: "#fafafa",
                    border: "1px solid #efefef",
                  }}
                >
                  <Typography variant="body2" sx={{ color: "#333" }}>
                    {item}
                  </Typography>
                </Box>
              ))}
            </Stack>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}