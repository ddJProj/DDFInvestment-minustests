// src/types/auth.types.ts

// Enum for user roles, matches backend's Role
export enum UserRole {
  Admin = 'admin',
  Employee = 'employee',
  Client = 'client',
  Guest = 'guest',
}

// Enum for permissions, matches backend's Permission
export enum Permissions {
  VIEW_ACCOUNT = 'VIEW_ACCOUNT',
  VIEW_ACCOUNTS = 'VIEW_ACCOUNTS',
  EDIT_MY_DETAILS = 'EDIT_MY_DETAILS',
  UPDATE_MY_PASSWORD = 'UPDATE_MY_PASSWORD',
  CREATE_USER = 'CREATE_USER',
  EDIT_USER = 'EDIT_USER',
  DELETE_USER = 'DELETE_USER',
  EDIT_EMPLOYEE = 'EDIT_EMPLOYEE',
  CREATE_EMPLOYEE = 'CREATE_EMPLOYEE',
  UPDATE_OTHER_PASSWORD = 'UPDATE_OTHER_PASSWORD',
  CREATE_CLIENT = 'CREATE_CLIENT',
  EDIT_CLIENT = 'EDIT_CLIENT',
  VIEW_CLIENT = 'VIEW_CLIENT',
  VIEW_CLIENTS = 'VIEW_CLIENTS',
  ASSIGN_CLIENT = 'ASSIGN_CLIENT',
  CREATE_INVESTMENT = 'CREATE_INVESTMENT',
  EDIT_INVESTMENT = 'EDIT_INVESTMENT',
  VIEW_EMPLOYEES = 'VIEW_EMPLOYEES',
  VIEW_EMPLOYEE = 'VIEW_EMPLOYEE',
  VIEW_INVESTMENT = 'VIEW_INVESTMENT',
  REQUEST_CLIENT_ACCOUNT = 'REQUEST_CLIENT_ACCOUNT',
}

// UserAccount structure
export interface UserAccount {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  permissions: Permissions[];
}
