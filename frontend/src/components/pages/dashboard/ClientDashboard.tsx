// src/components/pages/dashboard/ClientDashboard.tsx
import React from "react";
import { usePermissions } from "../../../hooks/usePermissions";
import { UserRole, Permissions } from "../../../types/auth.types";

const ClientDashboard: React.FC = () => {
  const { canRender, hasRole, isLoading, error } = usePermissions();

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      <h1>Client Dashboard</h1>
      {hasRole(UserRole.Client) && <p>Welcome, Client!</p>}
      
      {canRender([Permissions.ViewOwnData], (
        <div>
          <h2>Account Details</h2>
          <p>View your account information here.</p>
        </div>
      ))}

      {canRender([Permissions.RequestService], (
        <div>
          <h2>Services</h2>
          <button>Request New Service</button>
          <p>View your active services here.</p>
        </div>
      ))}

      {canRender([Permissions.UpdateProfile], (
        <div>
          <h2>Profile Settings</h2>
          <button>Update Profile</button>
        </div>
      ))}
    </div>
  );
};

export default ClientDashboard;
