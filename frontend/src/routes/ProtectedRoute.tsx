// src/routes/ProtectedRoute.tsx
import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { usePermissions } from "../hooks/usePermissions";
import { UserRole } from "../types/auth.types";
import { ROUTES } from "../constants/router.constants";
import { authUtils } from "../utils/auth.utils";
import { AuthContext } from "../App";


interface ProtectedRouteProps {
  element: React.ReactElement; 
  isProtected: boolean;
  roles?: UserRole[]; 
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ element, isProtected, roles }) => {
  const { hasRole, isLoading } = usePermissions();
  const { isAuthenticated } = useContext(AuthContext);
  
  console.log("ProtectedRoute check:", { 
    isProtected, 
    isAuthenticated, 
    isLoading,
    userRole: authUtils.getUser()?.role,
    requiredRoles: roles
  });

  if (isLoading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  // First check if user is authenticated for protected routes
  if (isProtected && !isAuthenticated) {
    console.log("Not authenticated, redirecting to login");
    return <Navigate to={ROUTES.LOGIN} />;
  }

  // Then check for role-specific access
  if (isProtected && roles && !roles.some((role: UserRole) => hasRole(role))) {
    return <Navigate to={ROUTES.UNAUTHORIZED}/>;
  }

  return element;
};

export default ProtectedRoute;