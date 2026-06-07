import {
  Box, Button, Card, CardContent, Divider,
  Paper, Stack, Typography, CircularProgress, Alert,
} from "@mui/material";
import { useState } from "react";
import type { ChoferDTO } from "../api/admin";
import {
  getChoferConMasViajes,
  getChoferConMasKm,
  getChoferConMasFacturacion,
} from "../api/admin";

interface MetricaChofer {
  label: string;
  chofer: ChoferDTO | null;
  loading: boolean;
  error: string | null;
}

export default function AdminPage() {
  const [masViajes, setMasViajes] = useState<MetricaChofer>({ label: "más viajes", chofer: null, loading: false, error: null });
  const [masKm, setMasKm] = useState<MetricaChofer>({ label: "más km", chofer: null, loading: false, error: null });
  const [masFacturacion, setMasFacturacion] = useState<MetricaChofer>({ label: "más facturación", chofer: null, loading: false, error: null });

  async function fetchMetrica(
    setter: React.Dispatch<React.SetStateAction<MetricaChofer>>,
    fetcher: () => Promise<ChoferDTO>,
    label: string
  ) {
    setter(prev => ({ ...prev, loading: true, error: null }));
    try {
      const chofer = await fetcher();
      setter(prev => ({ ...prev, chofer, loading: false }));
    } catch {
      setter(prev => ({ ...prev, loading: false, error: `No hay datos de ${label}` }));
    }
  }

  function ChoferCard({ metrica, onConsultar, titulo, descripcion }: {
    metrica: MetricaChofer;
    onConsultar: () => void;
    titulo: string;
    descripcion: string;
  }) {
    return (
      <Card elevation={0} sx={{ borderRadius: 3, border: "1px solid #ececec", bgcolor: "#fcfcfc" }}>
        <CardContent>
          <Typography sx={{ fontWeight: 700, color: "#111", mb: 1 }}>{titulo}</Typography>
          <Typography variant="body1" sx={{ color: "#666" }}>{descripcion}</Typography>
          <Divider sx={{ my: 2 }} />

          {metrica.loading && <CircularProgress size={20} />}
          {metrica.error && <Alert severity="warning" sx={{ mb: 2 }}>{metrica.error}</Alert>}
          {metrica.chofer && !metrica.loading && (
            <Box sx={{ mb: 2, p: 2, borderRadius: 2, bgcolor: "#f5f5f5" }}>
              <Typography variant="body2" sx={{ color: "#666" }}>Chofer destacado</Typography>
              <Typography sx={{ fontWeight: 700 }}>{metrica.chofer.nombre}</Typography>
              <Typography variant="body2" sx={{ color: "#888" }}>Patente: {metrica.chofer.patente}</Typography>
            </Box>
          )}

          <Button
            variant="contained"
            onClick={onConsultar}
            disabled={metrica.loading}
            sx={{
              bgcolor: "#111", color: "#fff", textTransform: "none",
              fontWeight: 700, borderRadius: 2, boxShadow: "none",
              "&:hover": { bgcolor: "#000", boxShadow: "none" },
            }}
          >
            Ver chofer destacado
          </Button>
        </CardContent>
      </Card>
    );
  }

  return (
    <Box sx={{ maxWidth: 1200, mx: "auto" }}>
      <Stack spacing={1} sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ fontWeight: 700, color: "#111" }}>Panel de administración</Typography>
        <Typography variant="body1" sx={{ color: "#666" }}>Visualizá métricas generales y actividad reciente del sistema.</Typography>
      </Stack>

      <Paper elevation={0} sx={{ p: 3, borderRadius: 3, border: "1px solid #e8e8e8", bgcolor: "#fff" }}>
        <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>Métricas destacadas</Typography>
        <Stack spacing={2}>
          <ChoferCard
            metrica={masViajes}
            onConsultar={() => fetchMetrica(setMasViajes, getChoferConMasViajes, "más viajes")}
            titulo="Chofer con más viajes"
            descripcion="Consultá cuál es el chofer con mayor cantidad de viajes realizados."
          />
          <ChoferCard
            metrica={masKm}
            onConsultar={() => fetchMetrica(setMasKm, getChoferConMasKm, "más km")}
            titulo="Chofer con más kilómetros"
            descripcion="Consultá cuál es el chofer con mayor cantidad de kilómetros recorridos."
          />
          <ChoferCard
            metrica={masFacturacion}
            onConsultar={() => fetchMetrica(setMasFacturacion, getChoferConMasFacturacion, "más facturación")}
            titulo="Chofer con más facturación"
            descripcion="Consultá cuál es el chofer con mayor facturación total."
          />
        </Stack>
      </Paper>
    </Box>
  );
}