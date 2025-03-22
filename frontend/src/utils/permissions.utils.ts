// src/utils/permissions.utils.ts
import { UserRole, Permissions } from "../types/auth.types";

// Updated to reflect the Rust role definitions:
//   Admin, Employee, Client, Restricted
export const ROLE_PERMISSIONS: Record<UserRole, { permissions: Permissions[] }> = {
  [UserRole.Admin]: {
    permissions: [
      Permissions.ManageUsers,
      Permissions.ManageRoles,
      Permissions.ManageSystem,
      Permissions.ViewAllData,
      Permissions.ManageClients,
      Permissions.ViewClientData,
      Permissions.ModifyClientService,
      Permissions.AssignClients,
      Permissions.ViewOwnData,
      Permissions.UpdateProfile,
      Permissions.RequestService,
    ],
  },
  [UserRole.Employee]: {
    permissions: [
      Permissions.ViewOwnData,
      Permissions.UpdateProfile,
      Permissions.ViewClientData,
      Permissions.ModifyClientService,
      Permissions.AssignClients,
      // Depending on your logic, employees may or may not manage clients:
      // Permissions.ManageClients,
    ],
  },
  [UserRole.Client]: {
    permissions: [
      Permissions.ViewOwnData,
      Permissions.UpdateProfile,
      Permissions.RequestService,
    ],
  },
  [UserRole.Restricted]: {
    permissions: [
      Permissions.ViewOwnData,
      // Consider whether Restricted should be able to update profile or request service.
      // If not, leave as is; if yes, add Permissions.UpdateProfile or Permissions.RequestService.
    ],
  },
};

