import { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import {
  Alert,
  Box,
  Button,
  Link,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import AuthLayout from "../components/AuthLayout";
import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    username: "",
    password: "",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");

    if (!form.username.trim()) {
      setError("El usuario es obligatorio");
      return;
    }

    if (!form.password.trim()) {
      setError("La contraseña es obligatoria");
      return;
    }

    setLoading(true);

    try {
      const path = await login({
        username: form.username,
        password: form.password,
      });

      navigate(path);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error al iniciar sesión");
    } finally {
      setLoading(false);
    }
  }

  return (
      <AuthLayout
          title="Iniciar sesión"
          subtitle="Accedé con tu usuario y contraseña"
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
                variant="outlined"
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
                variant="outlined"
                sx={{
                  "& .MuiOutlinedInput-root": {
                    borderRadius: 2,
                    bgcolor: "#fff",
                  },
                }}
            />

            {error && <Alert severity="error">{error}</Alert>}

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
              {loading ? "Ingresando..." : "Ingresar"}
            </Button>

            <Typography
                variant="body2"
                sx={{ textAlign: "center", color: "#666" }}
            >
              ¿No tenés cuenta?{" "}
              <Link
                  component={RouterLink}
                  to="/register"
                  underline="hover"
                  sx={{ color: "#111", fontWeight: 600 }}
              >
                Registrate
              </Link>
            </Typography>
          </Stack>
        </Box>
      </AuthLayout>
  );
}