// src/components/pages/dashboard/index.tsx
import React from "react";
import { usePermissions } from "../../../hooks/usePermissions";
import { UserRole } from "../../../types/auth.types";

const Dashboard: React.FC = () => {
  const { hasRole, isLoading, error } = usePermissions();

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  if (hasRole(UserRole.Admin)) {
    return <p>Welcome Admin! You have full access to manage the system.</p>;
  }

  if (hasRole(UserRole.Employee)) {
    return <p>Welcome Employee! Here are your tasks and reports.</p>;
  }

  if (hasRole(UserRole.Client)) {
    return <p>Welcome Client! Access your account and manage your services here.</p>;
  }

  return <p>Unauthorized or role not recognized</p>;
};

export default Dashboard;

