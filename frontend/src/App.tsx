//import reactLogo from './assets/react.svg'
//import viteLogo from '/vite.svg'
// src/App.tsx
import React, { createContext, useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './components/pages/login/Login';
import Registration from './components/pages/login/Registration';
import AdminDashboard from './components/pages/dashboard/AdminDashboard';
import EmployeeDashboard from './components/pages/dashboard/EmployeeDashboard';
import ClientDashboard from './components/pages/dashboard/ClientDashboard';
import Dashboard from './components/pages/dashboard/Dashboard';
import UnauthorizedPage from './components/pages/errors/UnauthorizedPage';
import { authUtils } from './utils/auth.utils';
import { UserRole } from './types/auth.types';

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
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(authUtils.isAuthenticated());
  const [userRole, setUserRole] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  useEffect(() => {
    // Check authentication status on mount
    const checkAuth = () => {
      const isAuth = authUtils.isAuthenticated();
      setIsAuthenticated(isAuth);
      
      if (isAuth) {
        const user = authUtils.getUser();
        setUserRole(user?.role || null);
      }
      
      setIsLoading(false);
    };
    
    checkAuth();
  }, []);

  const logout = () => {
    authUtils.clearAuthData();
    setIsAuthenticated(false);
    setUserRole(null);
  };

  // Protected route component
  const ProtectedRoute = ({ 
    children, 
    allowedRoles = [] 
  }: { 
    children: React.ReactNode; 
    allowedRoles?: string[];
  }) => {
    if (!isAuthenticated) {
      return <Navigate to="/login" />;
    }

    if (allowedRoles.length > 0 && userRole && !allowedRoles.includes(userRole)) {
      return <Navigate to="/unauthorized" />;
    }

    return <>{children}</>;
  };

  if (isLoading) {
    return <div className="flex items-center justify-center h-screen">Loading...</div>;
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, userRole, logout }}>
      <Router>
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Login />} />
          <Route path="/register" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Registration />} />
          <Route path="/unauthorized" element={<UnauthorizedPage />} />
          
          {/* Protected Routes */}
          <Route 
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            } 
          />
          
          <Route 
            path="/dashboard/admin"
            element={
              <ProtectedRoute allowedRoles={[UserRole.Admin]}>
                <AdminDashboard />
              </ProtectedRoute>
            } 
          />
          
          <Route 
            path="/dashboard/employee"
            element={
              <ProtectedRoute allowedRoles={[UserRole.Employee]}>
                <EmployeeDashboard />
              </ProtectedRoute>
            } 
          />
          
          <Route 
            path="/dashboard/client"
            element={
              <ProtectedRoute allowedRoles={[UserRole.Client]}>
                <ClientDashboard />
              </ProtectedRoute>
            } 
          />
          
          {/* Redirect to login for any other routes */}
          <Route path="*" element={<Navigate to={isAuthenticated ? "/dashboard" : "/login"} />} />
        </Routes>
      </Router>
    </AuthContext.Provider>
  );
};

export default App;