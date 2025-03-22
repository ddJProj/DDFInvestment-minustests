// src/constants/router.constants.ts
import { UserRole } from "../types/auth.types";
import Login from "../components/pages/login/Login";
import Registration from "../components/pages/login/Registration";
import Dashboard from "../components/pages/dashboard/Dashboard";
import AdminDashboard from "../components/pages/dashboard/AdminDashboard";
import EmployeeDashboard from "../components/pages/dashboard/EmployeeDashboard";
import ClientDashboard from "../components/pages/dashboard/ClientDashboard";

const ROUTES = {
  LOGIN: "/login",
  REGISTER: "/register", 
  DASHBOARD: "/dashboard",
  ADMIN: "/dashboard/admin",
  EMPLOYEE: "/dashboard/employee",
  CLIENT: "/dashboard/client",
};

const ROUTER_CONFIG = [
  {
    path: ROUTES.LOGIN,
    element: Login, 
    isProtected: false,
  },
  {
    path: ROUTES.REGISTER, // registration route
    element: Registration,
    isProtected: false,
  },
  {
    path: ROUTES.DASHBOARD,
    element: Dashboard, 
    isProtected: true,
  },
  {
    path: ROUTES.ADMIN,
    element: AdminDashboard, 
    isProtected: true,
    roles: [UserRole.Admin],// Use UserRole enum
  },
  {
    path: ROUTES.EMPLOYEE,
    element: EmployeeDashboard, // React Component
    isProtected: true,
    roles: [UserRole.Employee],// Use UserRole enum
  },
  {
    path: ROUTES.CLIENT,
    element: ClientDashboard, 
    isProtected: true,
    roles: [UserRole.Client],// Use UserRole enum
  },
];

export { ROUTES, ROUTER_CONFIG };

