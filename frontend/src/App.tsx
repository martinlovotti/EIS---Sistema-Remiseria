import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import AppLayout from "./components/AppLayout";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import AdminPage from "./pages/AdminPage";
import UsuarioPage from "./pages/UsuarioPage";
import ChoferPage from "./pages/ChoferPage";

function HomeRedirect() {
  const { role, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (role === "ADMIN") {
    return <Navigate to="/admin" replace />;
  }

  if (role === "CHOFER") {
    return <Navigate to="/chofer" replace />;
  }

  return <Navigate to="/usuario" replace />;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeRedirect />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route
            path="/admin"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <AppLayout>
                  <AdminPage />
                </AppLayout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/usuario"
            element={
              <ProtectedRoute allowedRoles={["USUARIO"]}>
                <AppLayout>
                  <UsuarioPage />
                </AppLayout>
              </ProtectedRoute>
            }
          />

          <Route
            path="/chofer"
            element={
              <ProtectedRoute allowedRoles={["CHOFER"]}>
                <AppLayout>
                  <ChoferPage />
                </AppLayout>
              </ProtectedRoute>
            }
          />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}