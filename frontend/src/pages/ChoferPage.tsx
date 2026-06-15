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
} from '@mui/material';
import { useEffect, useState } from 'react';

import { useAuth } from '../context/AuthContext';

import {
  getViajes,
  aceptarViaje,
  iniciarViaje,
  finalizarViaje,
  type ViajeDTO,
} from '../api/viaje';

export default function ChoferPage() {
  const { entidadId } = useAuth();
  const [viajes, setViajes] = useState<ViajeDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [viajeActivo, setViajeActivo] = useState<ViajeDTO | null>(null);
  const [viajeIniciado, setViajeIniciado] = useState(false);

  useEffect(() => {
    const fetchViajes = async () => {
      try {
        const viajes = await getViajes();
        setViajes(viajes);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchViajes();
  }, []);

  useEffect(() => {
    const activo = viajes.find(
      (v) =>
        v.chofer?.id === entidadId &&
        ['ACEPTADO', 'EN_CURSO'].includes(v.estado),
    );

    setViajeActivo(activo ?? null);
    setViajeIniciado(activo?.estado === 'EN_CURSO');
  }, [viajes, entidadId]);

  async function handleAceptarViaje(id: number) {
    console.log('aceptando viaje', id, entidadId);
    try {
      if (!entidadId) {
        throw new Error('No se encontró el id del chofer');
      }
      await aceptarViaje(id, entidadId);

      const viajeAceptado = viajes.find((v) => v.id === id);

      if (viajeAceptado) {
        setViajeActivo({
          ...viajeAceptado,
          estado: 'ACEPTADO',
        });
      }

      const viajesRecuperados = await getViajes();
      setViajes(viajesRecuperados);
    } catch (error) {
      if (error instanceof Error) {
        setError(error.message);
        setOpenSnackbar(true);
      }
    }
  }

  async function handleIniciarViaje(id: number) {
    try {
      await iniciarViaje(id);
      setViajeIniciado(true);
    } catch (error) {
      if (error instanceof Error) {
        setError(error.message);
        setOpenSnackbar(true);
      }
    }
  }

  async function handleFinalizarViaje(id: number) {
    try {
      await finalizarViaje(id);

      setViajeActivo(null);
      setViajeIniciado(false);

      const viajes = await getViajes();
      setViajes(viajes);
    } catch (error) {
      if (error instanceof Error) {
        setError(error.message);
        setOpenSnackbar(true);
      }
    }
  }

  return (
    <Box sx={{ maxWidth: 1200, mx: 'auto' }}>
      <Stack spacing={1} sx={{ mb: 4 }}>
        <Typography variant='h4' sx={{ fontWeight: 700, color: '#111' }}>
          Panel de chofer
        </Typography>
        <Typography variant='body1' sx={{ color: '#666' }}>
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
              border: '1px solid #e8e8e8',
              bgcolor: '#fff',
            }}
          >
            <Typography variant='h6' sx={{ fontWeight: 700, mb: 2 }}>
              Viajes disponibles
            </Typography>

            <Stack spacing={2}>
              {loading ? (
                <Typography>Cargando viajes...</Typography>
              ) : (
                viajes
                  .filter((viaje) => viaje.estado === 'PENDIENTE')
                  .map((viaje) => (
                    <Card
                      key={viaje.id}
                      elevation={0}
                      sx={{
                        borderRadius: 3,
                        border: '1px solid #ececec',
                        bgcolor: '#fcfcfc',
                      }}
                    >
                      <CardContent>
                        <Stack
                          direction={{ xs: 'column', sm: 'row' }}
                          spacing={2}
                          sx={{
                            justifyContent: 'space-between',
                            alignItems: { xs: 'flex-start', sm: 'center' },
                          }}
                        >
                          <Box>
                            <Typography sx={{ fontWeight: 700, color: '#111' }}>
                              {viaje.origen} → {viaje.destino}
                            </Typography>
                            <Typography
                              variant='body2'
                              sx={{ color: '#666', mt: 0.5 }}
                            >
                              Pasajero: {viaje.usuario.nombre}
                            </Typography>
                            <Typography variant='body2' sx={{ color: '#666' }}>
                              Viaje #{viaje.id}
                            </Typography>
                          </Box>

                          <Chip
                            label={viaje.estado}
                            variant='outlined'
                            sx={{ alignSelf: 'flex-start' }}
                          />
                        </Stack>

                        <Divider sx={{ my: 2 }} />

                        <Stack
                          direction='row'
                          sx={{
                            justifyContent: 'flex-end',
                          }}
                        >
                          <Button
                            variant='contained'
                            onClick={() => handleAceptarViaje(viaje.id)}
                            sx={{
                              bgcolor: '#111',
                              color: '#fff',
                              textTransform: 'none',
                              fontWeight: 700,
                              borderRadius: 2,
                              boxShadow: 'none',
                              '&:hover': {
                                bgcolor: '#000',
                                boxShadow: 'none',
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
              border: '1px solid #e8e8e8',
              bgcolor: '#fff',
              height: '100%',
            }}
          >
            <Typography variant='h6' sx={{ fontWeight: 700, mb: 2 }}>
              Viaje activo
            </Typography>

            <Stack spacing={2}>
              {viajeActivo && (
                <Card
                  key={viajeActivo.id}
                  elevation={0}
                  sx={{
                    borderRadius: 3,
                    border: '1px solid #ececec',
                    bgcolor: '#fcfcfc',
                  }}
                >
                  <CardContent>
                    <Typography sx={{ fontWeight: 700, color: '#111' }}>
                      {viajeActivo.origen} → {viajeActivo.destino}
                    </Typography>

                    <Typography variant='body2' sx={{ color: '#666', mt: 0.5 }}>
                      Pasajero: {viajeActivo.usuario.nombre}
                    </Typography>

                    <Typography variant='body2' sx={{ color: '#666' }}>
                      Viaje #{viajeActivo.id}
                    </Typography>

                    <Divider sx={{ my: 2 }} />

                    <Stack spacing={1.5}>
                      {!viajeIniciado && (
                        <Button
                          variant='outlined'
                          fullWidth
                          onClick={() => handleIniciarViaje(viajeActivo.id)}
                          sx={{
                            textTransform: 'none',
                            borderColor: '#d0d0d0',
                            color: '#111',
                            fontWeight: 600,
                            '&:hover': {
                              borderColor: '#111',
                              bgcolor: '#fafafa',
                            },
                          }}
                        >
                          Iniciar viaje
                        </Button>
                      )}

                      <Button
                        variant='contained'
                        fullWidth
                        onClick={() => handleFinalizarViaje(viajeActivo.id)}
                        sx={{
                          bgcolor: '#111',
                          color: '#fff',
                          textTransform: 'none',
                          fontWeight: 700,
                          borderRadius: 2,
                          boxShadow: 'none',
                          '&:hover': {
                            bgcolor: '#000',
                            boxShadow: 'none',
                          },
                        }}
                      >
                        Finalizar viaje
                      </Button>
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
        <Alert severity='error' onClose={() => setOpenSnackbar(false)}>
          {error}
        </Alert>
      </Snackbar>
    </Box>
  );
}
