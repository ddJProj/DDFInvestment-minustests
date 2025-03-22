// src/routes/AppRoutes.tsx
import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { usePermissions } from "../../hooks/usePermissions";
import AdminDashboard from "../pages/dashboard/AdminDashboard";
import EmployeeDashboard from "../pages/dashboard/EmployeeDashboard";
import ClientDashboard from "../pages/dashboard/ClientDashboard";
import ROUTES from "../../constants/routes.constants";
import { UserRole } from "../../types/auth.types";

const AppRoutes: React.FC = () => {
  const { hasRole } = usePermissions();

  return (
    <Routes>
      {hasRole(UserRole.Admin) && (
        <Route path={ROUTES.ADMIN_DASHBOARD} element={<AdminDashboard />} />
      )}
      {hasRole(UserRole.Employee) && (
        <Route
          path={ROUTES.EMPLOYEE_DASHBOARD}
          element={<EmployeeDashboard />}
        />
      )}
      {hasRole(UserRole.Client) && (
        <Route path={ROUTES.CLIENT_DASHBOARD} element={<ClientDashboard />} />
      )}
      <Route path="*" element={<Navigate to={ROUTES.UNAUTHORIZED} />} />
    </Routes>
  );
};

export default AppRoutes;

