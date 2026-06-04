import type { ReactNode } from "react";
import { useNavigate } from "react-router-dom";
import {
    AppBar,
    Box,
    Button,
    Chip,
    Container,
    Stack,
    Toolbar,
    Typography,
} from "@mui/material";
import { useAuth } from "../context/AuthContext";

export default function AppLayout({ children }: { children: ReactNode }) {
    const { username, role, logout } = useAuth();
    const navigate = useNavigate();

    function handleLogout() {
        logout();
        navigate("/login");
    }

    function getRoleLabel(currentRole: string) {
        switch (currentRole) {
            case "ADMIN":
                return "Admin";
            case "USUARIO":
                return "Usuario";
            case "CHOFER":
                return "Chofer";
            default:
                return currentRole;
        }
    }

    return (
        <Box
            sx={{
                minHeight: "100vh",
                bgcolor: "#f6f6f6",
            }}
        >
            <AppBar
                position="sticky"
                elevation={0}
                sx={{
                    bgcolor: "#fff",
                    color: "#111",
                    borderBottom: "1px solid #e8e8e8",
                }}
            >
                <Toolbar
                    sx={{
                        minHeight: "72px",
                        px: { xs: 2, sm: 3 },
                    }}
                >
                    <Container
                        maxWidth="xl"
                        sx={{
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                            px: "0 !important",
                            paddingY: 2,
                        }}
                    >
                        <Stack >
                            <Typography
                                variant="h5"
                                sx={{
                                    fontFamily: "'Manrope', sans-serif",
                                    fontWeight: 700,
                                    color: "#111",
                                    letterSpacing: "0.5px",
                                    lineHeight: 1.1,
                                }}
                            >
                                Remisería B&G
                            </Typography>

                            <Stack
                                direction="row"
                                spacing={1}
                                sx={{
                                    alignItems: "center",
                                    flexWrap: "wrap",
                                }}
                            >
                                <Typography variant="body2" sx={{ color: "#666" }}>
                                    {username}
                                </Typography>

                                <Chip
                                    label={getRoleLabel(role)}
                                    size="small"
                                    sx={{
                                        bgcolor: "#f3f3f3",
                                        color: "#111",
                                        borderRadius: "8px",
                                        fontWeight: 600,
                                    }}
                                />
                            </Stack>
                        </Stack>

                        <Stack
                            direction={{ xs: "column", sm: "row" }}
                            spacing={1.5}
                            sx={{
                                alignItems: { xs: "flex-end", sm: "center" },
                            }}
                        >

                            <Button
                                variant="contained"
                                onClick={handleLogout}
                                sx={{
                                    bgcolor: "#111",
                                    color: "#fff",
                                    textTransform: "none",
                                    fontWeight: 700,
                                    borderRadius: 2,
                                    px: 2,
                                    boxShadow: "none",
                                    "&:hover": {
                                        bgcolor: "#000",
                                        boxShadow: "none",
                                    },
                                }}
                            >
                                Cerrar sesión
                            </Button>
                        </Stack>
                    </Container>
                </Toolbar>
            </AppBar>

            <Box
                component="main"
                sx={{
                    py: 4,
                    px: { xs: 2, sm: 3 },
                }}
            >
                <Container maxWidth="xl">{children}</Container>
            </Box>
        </Box>
    );
}