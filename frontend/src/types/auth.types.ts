
// src/types/auth.types.ts

// Enum for user roles, matching backend's Role
export enum UserRole {
  Admin = "Admin",
  Employee = "Employee",
  Client = "Client",
  Restricted = "Restricted",
}

// Enum for permissions, matching backend's Permission
export enum Permissions {
  ManageUsers = "ManageUsers",
  ManageRoles = "ManageRoles",
  ManageSystem = "ManageSystem",
  ViewAllData = "ViewAllData",
  ManageClients = "ManageClients",
  ViewClientData = "ViewClientData",
  ModifyClientService = "ModifyClientService",
  AssignClients = "AssignClients",
  ViewOwnData = "ViewOwnData",
  UpdateProfile = "UpdateProfile",
  RequestService = "RequestService",
}

// UserAccount structure
export interface UserAccount {
  id: number;
  username: string;
  role: UserRole;        // Uses the updated UserRole enum
  permissions: Permissions[]; // Array of permissions using the updated Permission enum
}

