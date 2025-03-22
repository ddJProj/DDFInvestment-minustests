// src/components/pages/dashboard/AdminDashboard.tsx

import React from "react";
import { usePermissions } from "../../../hooks/usePermissions";
import { UserRole, Permissions } from "../../../types/auth.types";

const AdminDashboard: React.FC = () => {
  const { canRender, hasRole, isLoading, error } = usePermissions();

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      <h1>Admin Dashboard</h1>
      {hasRole(UserRole.Admin) && <p>Welcome, Admin!</p>}
      
      {canRender([Permissions.ManageRoles], (
        <button>Modify Client Role</button>
      ))}

      {canRender([Permissions.ManageUsers], (
        <button>Create New User</button>
      ))}
    </div>
  );
};

export default AdminDashboard;
