import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { usePermissions } from "../../../hooks/usePermissions";
import { UserRole } from "../../../types/auth.types";
import { ROUTES } from "../../../constants/router.constants";

const Dashboard: React.FC = () => {
  const { hasRole, isLoading, error } = usePermissions();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoading && !error) {
      if (hasRole(UserRole.Admin)) {
        navigate(ROUTES.ADMIN);
      } else if (hasRole(UserRole.Employee)) {
        navigate(ROUTES.EMPLOYEE);
      } else if (hasRole(UserRole.Client)) {
        navigate(ROUTES.CLIENT);
      }
    }
  }, [hasRole, isLoading, error, navigate]);

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  // will briefly show before redirection happens
  if (hasRole(UserRole.Admin)) {
    return <p>Welcome Admin! You have full access to manage the system.</p>;
  }

  if (hasRole(UserRole.Employee)) {
    return <p>Welcome Employee! Here are your tasks and reports.</p>;
  }

  if (hasRole(UserRole.Client)) {
    return <p>Welcome Client! Access your account and manage your services here.</p>;
  }

  return <p>Unauthorized, Guest, or role not recognized</p>;
};

export default Dashboard;