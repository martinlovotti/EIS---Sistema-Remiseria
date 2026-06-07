import { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import {
  Alert,
  Box,
  Button,
  Link,
  MenuItem,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import AuthLayout from "../components/AuthLayout";
import { useAuth } from "../context/AuthContext";
import type { Role } from "../types/auth";

type RegisterRole = Extract<Role, "USUARIO" | "CHOFER">;

export default function RegisterPage() {
  const { registerUsuario, registerChofer } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState<{
    username: string;
    password: string;
    nombre: string;
    patente: string;
    role: RegisterRole;
  }>({
    username: "",
    password: "",
    nombre: "",
    patente: "",
    role: "USUARIO",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(
      e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!form.username.trim()) {
      setError("El usuario es obligatorio");
      return;
    }

    if (!form.password.trim()) {
      setError("La contraseña es obligatoria");
      return;
    }

    if (!form.nombre.trim()) {
      setError("El nombre es obligatorio");
      return;
    }

    if (form.role === "CHOFER" && !form.patente.trim()) {
      setError("La patente es obligatoria para chofer");
      return;
    }

    setLoading(true);

    try {
      if (form.role === "USUARIO") {
        await registerUsuario({
          username: form.username,
          password: form.password,
          nombre: form.nombre,
        });
      } else {
        await registerChofer({
          username: form.username,
          password: form.password,
          nombre: form.nombre,
          patente: form.patente,
        });
      }

      setSuccess("Registro exitoso. Ahora podés iniciar sesión.");
      setTimeout(() => navigate("/login"), 1000);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error al registrarse");
    } finally {
      setLoading(false);
    }
  }

  return (
      <AuthLayout
          title="Crear cuenta"
          subtitle="Completá tus datos para registrarte"
      >
        <Box component="form" onSubmit={handleSubmit}>
          <Stack spacing={2.2}>
            <TextField
                label="Usuario"
                name="username"
                value={form.username}
                onChange={handleChange}
                fullWidth
                required
                sx={{
                  "& .MuiOutlinedInput-root": {
                    borderRadius: 2,
                    bgcolor: "#fff",
                  },
                }}
            />

            <TextField
                label="Contraseña"
                name="password"
                type="password"
                value={form.password}
                onChange={handleChange}
                fullWidth
                required
                sx={{
                  "& .MuiOutlinedInput-root": {
                    borderRadius: 2,
                    bgcolor: "#fff",
                  },
                }}
            />

            <TextField
                label="Nombre"
                name="nombre"
                value={form.nombre}
                onChange={handleChange}
                fullWidth
                required
                sx={{
                  "& .MuiOutlinedInput-root": {
                    borderRadius: 2,
                    bgcolor: "#fff",
                  },
                }}
            />

            <TextField
                select
                label="Rol"
                name="role"
                value={form.role}
                onChange={handleChange}
                fullWidth
                required
                sx={{
                  "& .MuiOutlinedInput-root": {
                    borderRadius: 2,
                    bgcolor: "#fff",
                  },
                }}
            >
              <MenuItem value="USUARIO">Usuario</MenuItem>
              <MenuItem value="CHOFER">Chofer</MenuItem>
            </TextField>

            {form.role === "CHOFER" && (
                <TextField
                    label="Patente"
                    name="patente"
                    value={form.patente}
                    onChange={handleChange}
                    fullWidth
                    required
                    sx={{
                      "& .MuiOutlinedInput-root": {
                        borderRadius: 2,
                        bgcolor: "#fff",
                      },
                    }}
                />
            )}

            {error && <Alert severity="error">{error}</Alert>}
            {success && <Alert severity="success">{success}</Alert>}

            <Button
                type="submit"
                variant="contained"
                fullWidth
                disabled={loading}
                sx={{
                  py: 1.4,
                  borderRadius: 2,
                  textTransform: "none",
                  fontWeight: 700,
                  bgcolor: "#111",
                  boxShadow: "none",
                  "&:hover": {
                    bgcolor: "#000",
                    boxShadow: "none",
                  },
                }}
            >
              {loading ? "Registrando..." : "Registrarme"}
            </Button>

            <Typography
                variant="body2"
                sx={{ textAlign: "center", color: "#666" }}
            >
              ¿Ya tenés cuenta?{" "}
              <Link
                  component={RouterLink}
                  to="/login"
                  underline="hover"
                  sx={{ color: "#111", fontWeight: 600 }}
              >
                Iniciá sesión
              </Link>
            </Typography>
          </Stack>
        </Box>
      </AuthLayout>
  );
}