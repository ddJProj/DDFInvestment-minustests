//import reactLogo from './assets/react.svg'
//import viteLogo from '/vite.svg'
// src/App.tsx
import React, { createContext, useEffect, useState } from 'react';
import { Route, Routes, Navigate } from 'react-router-dom';
import Login from './components/pages/login/Login';
import Registration from './components/pages/login/Registration';
import AdminDashboard from './components/pages/dashboard/AdminDashboard';
import EmployeeDashboard from './components/pages/dashboard/EmployeeDashboard';
import ClientDashboard from './components/pages/dashboard/ClientDashboard';
import Dashboard from './components/pages/dashboard/Dashboard';
import UnauthorizedPage from './components/pages/errors/UnauthorizedPage';
import { authUtils } from './utils/auth.utils';
import { UserRole } from './types/auth.types';
import { ROUTES } from "./constants/router.constants";
import ProtectedRoute from './routes/ProtectedRoute';

// Create auth context for global state management
export const AuthContext = createContext<{
  isAuthenticated: boolean;
  userRole: string | null;
  logout: () => void;
}>({
  isAuthenticated: false,
  userRole: null,
  logout: () => {},
});

const App: React.FC = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [userRole, setUserRole] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    // Check authentication status on mount and when storage changes
    const checkAuth = () => {
      const isAuth = authUtils.isAuthenticated();
      console.log("App.tsx: Checking auth state:", isAuth);
      setIsAuthenticated(isAuth);
      
      if (isAuth) {
        const user = authUtils.getUser();
        setUserRole(user?.role || null);
        console.log("App.tsx: User authenticated with role:", user?.role);
      } else {
        setUserRole(null);
        console.log("App.tsx: User not authenticated");
      }
      
      setIsLoading(false);
    };
    
    checkAuth();
    
    // Add event listener for storage changes to keep auth state in sync
    window.addEventListener('storage', checkAuth);
    
    return () => {
      window.removeEventListener('storage', checkAuth);
    };
  }, []);

  const logout = () => {
    console.log("App.tsx: Logging out user");
    authUtils.clearAuthData();
    setIsAuthenticated(false);
    setUserRole(null);
    window.location.href = ROUTES.LOGIN;
  };

  if (isLoading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, userRole, logout }}>
        <Routes>
          {/* Public Routes */}
          <Route 
            path={ROUTES.LOGIN} 
            element={isAuthenticated ? <Navigate to={ROUTES.DASHBOARD} /> : <Login />} 
          />
          
          <Route 
            path={ROUTES.REGISTER} 
            element={isAuthenticated ? <Navigate to={ROUTES.DASHBOARD} /> : <Registration />} 
          />
          
          <Route path={ROUTES.UNAUTHORIZED} element={<UnauthorizedPage />} />
          
          {/* Protected Routes */}
          <Route 
            path={ROUTES.DASHBOARD}
            element={
              <ProtectedRoute 
                element={<Dashboard />} 
                isProtected={true} 
              />
            } 
          />
          
          <Route 
            path={ROUTES.ADMIN}
            element={
              <ProtectedRoute 
                element={<AdminDashboard />} 
                isProtected={true} 
                roles={[UserRole.Admin]}
              />
            } 
          />
          
          <Route 
            path={ROUTES.EMPLOYEE}
            element={
              <ProtectedRoute 
                element={<EmployeeDashboard />} 
                isProtected={true} 
                roles={[UserRole.Employee]}
              />
            } 
          />
          
          <Route 
            path={ROUTES.CLIENT}
            element={
              <ProtectedRoute 
                element={<ClientDashboard />} 
                isProtected={true} 
                roles={[UserRole.Client]}
              />
            } 
          />
          
          {/* Redirect to login for any other routes */}
          <Route path="*" element={<Navigate to={isAuthenticated ? ROUTES.DASHBOARD : ROUTES.LOGIN} />} />
        </Routes>
    </AuthContext.Provider>
  );
};

export default App;