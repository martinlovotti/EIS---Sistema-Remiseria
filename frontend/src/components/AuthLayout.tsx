import { Box, Container, Paper, Typography } from "@mui/material";
import type { ReactNode } from "react";

interface AuthLayoutProps {
  title: string;
  subtitle?: string;
  children: ReactNode;
}

export default function AuthLayout({
  title,
  subtitle,
  children,
}: AuthLayoutProps) {
  return (
    <Box
      sx={{
        minHeight: "100vh",
        position: "relative",
        backgroundImage: 'url("/images/auth-bg.jpg")',
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* Overlay oscuro */}
      <Box
        sx={{
          position: "absolute",
          inset: 0,
          background: "rgba(0,0,0,0.45)",
        }}
      />

      {/* Contenido */}
      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          px: 4,
          py: 3,
        }}
      >
               
      <Typography
        variant="h4"
        sx={{
          fontFamily: "'Manrope', sans-serif",
          fontWeight: 800,
          color: "#f7f2f2",
          letterSpacing: "1px",
          lineHeight: 1,
        }}
      >
        Remisería B&G
      </Typography>

      </Box>

      <Box
        sx={{
          position: "relative",
          zIndex: 1,
          flex: 1,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          px: 2,
        }}
      >
        <Container maxWidth="sm">
          <Paper
            elevation={0}
            sx={{
              p: 4,
              borderRadius: 3,
              border: "1px solid rgba(255,255,255,0.15)",
              bgcolor: "rgba(255,255,255,0.96)",
              backdropFilter: "blur(6px)",
            }}
          >
            <Typography
              variant="h4"
              sx={{
                fontWeight: 700,
                color: "#111",
                mb: 1,
              }}
            >
              {title}
            </Typography>

            {subtitle && (
              <Typography
                variant="body1"
                sx={{
                  color: "#666",
                  mb: 3,
                }}
              >
                {subtitle}
              </Typography>
            )}

            {children}
          </Paper>
        </Container>
      </Box>
    </Box>
  );
}