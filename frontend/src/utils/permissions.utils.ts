// src/utils/permissions.utils.ts
import { UserRole, Permissions } from "../types/auth.types";

// updated to reflect backend role definitions (Spring):
//   Admin, Employee, Client, Guest
export const ROLE_PERMISSIONS: Record<UserRole, { permissions: Permissions[] }> = {
  [UserRole.Admin]: {
    permissions: [
      Permissions.VIEW_ACCOUNT,
      Permissions.VIEW_ACCOUNTS,
      Permissions.EDIT_MY_DETAILS,
      Permissions.UPDATE_MY_PASSWORD,
      Permissions.CREATE_USER,
      Permissions.EDIT_USER,
      Permissions.DELETE_USER,
      Permissions.EDIT_EMPLOYEE,
      Permissions.CREATE_EMPLOYEE,
      Permissions.UPDATE_OTHER_PASSWORD,
      Permissions.CREATE_CLIENT,
      Permissions.EDIT_CLIENT,
      Permissions.VIEW_CLIENT,
      Permissions.VIEW_CLIENTS,
      Permissions.ASSIGN_CLIENT,
      Permissions.CREATE_INVESTMENT,
      Permissions.EDIT_INVESTMENT,
      Permissions.VIEW_EMPLOYEES,
      Permissions.VIEW_EMPLOYEE,
      Permissions.VIEW_INVESTMENT,
      Permissions.REQUEST_CLIENT_ACCOUNT
    ],
  },
  [UserRole.Employee]: {
    permissions: [
      Permissions.VIEW_ACCOUNT,
      Permissions.EDIT_MY_DETAILS,
      Permissions.UPDATE_MY_PASSWORD,
      Permissions.CREATE_USER,
      Permissions.CREATE_CLIENT,
      Permissions.EDIT_CLIENT,
      Permissions.VIEW_CLIENT,
      Permissions.VIEW_CLIENTS,
      Permissions.ASSIGN_CLIENT,
      Permissions.CREATE_INVESTMENT,
      Permissions.EDIT_INVESTMENT,
      Permissions.VIEW_INVESTMENT
    ],
  },
  [UserRole.Client]: {
    permissions: [
      Permissions.VIEW_ACCOUNT,
      Permissions.EDIT_MY_DETAILS,
      Permissions.UPDATE_MY_PASSWORD,
      Permissions.CREATE_USER,
      Permissions.VIEW_INVESTMENT
    ],
  },
  [UserRole.Guest]: {
    permissions: [
      Permissions.VIEW_ACCOUNT,
      Permissions.EDIT_MY_DETAILS,
      Permissions.UPDATE_MY_PASSWORD,
      Permissions.CREATE_USER,
      Permissions.REQUEST_CLIENT_ACCOUNT
    ],
  },
};

