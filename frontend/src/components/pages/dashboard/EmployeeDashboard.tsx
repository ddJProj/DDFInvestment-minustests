// src/components/pages/dashboard/EmployeeDashboard.tsx
import React from "react";
import { usePermissions } from "../../../hooks/usePermissions";
import { UserRole, Permissions } from "../../../types/auth.types";

const EmployeeDashboard: React.FC = () => {
  const { canRender, hasRole, isLoading, error } = usePermissions();

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div>
      <h1>Employee Dashboard</h1>
      {hasRole(UserRole.Employee) && <p>Welcome, Employee!</p>}
      
      {canRender([Permissions.ViewClientData], (
        <div>
          <h2>Client Management</h2>
          <p>View assigned clients and their information.</p>
        </div>
      ))}

      {canRender([Permissions.ModifyClientService], (
        <div>
          <h2>Service Management</h2>
          <button>Modify Client Services</button>
        </div>
      ))}

      {canRender([Permissions.AssignClients], (
        <div>
          <h2>Client Assignment</h2>
          <button>Manage Client Assignments</button>
        </div>
      ))}

      {canRender([Permissions.ViewOwnData], (
        <div>
          <h2>Personal Dashboard</h2>
          <p>View your tasks, schedules, and performance metrics.</p>
        </div>
      ))}
    </div>
  );
};

export default EmployeeDashboard;
