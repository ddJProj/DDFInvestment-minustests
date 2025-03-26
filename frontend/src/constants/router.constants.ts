// src/constants/router.constants.ts
import { UserRole } from "../types/auth.types";

// defining the various front end routes
export const ROUTES = {
  LOGIN: "/login",
  REGISTER: "/register", 
  DASHBOARD: "/dashboard",
  ADMIN: "/dashboard/admin",
  EMPLOYEE: "/dashboard/employee",
  CLIENT: "/dashboard/client",
  UNAUTHORIZED: "/unauthorized", 
};

// definition of route configs
export const ROUTER_CONFIG = [
  {
    path: ROUTES.LOGIN,
    isProtected: false,
  },
  {
    path: ROUTES.REGISTER, // registration route
    isProtected: false,
  },
  {
    path: ROUTES.DASHBOARD,
    isProtected: true,
  },
  {
    path: ROUTES.ADMIN,
    isProtected: true,
    roles: [UserRole.Admin],// Use UserRole enum
  },
  {
    path: ROUTES.EMPLOYEE,
    isProtected: true,
    roles: [UserRole.Employee],// Use UserRole enum
  },
  {
    path: ROUTES.CLIENT,
    isProtected: true,
    roles: [UserRole.Client],// Use UserRole enum
  }, 
  {
    path: ROUTES.UNAUTHORIZED,
    isProtected: false,
  },
];