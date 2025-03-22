// app.tsx
//import reactLogo from './assets/react.svg'
//import viteLogo from '/vite.svg'

import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import { ROUTER_CONFIG } from "./constants/router.constants";
import { fetchSession, getSession } from "./utils/session.utils";

const App: React.FC = () => {
  const [session, setSession] = useState<{ role: string; permissions: string[] } | null>(null);
  const { token } = getSession();

  useEffect(() => {
    if (token) {
      fetchSession(token)
        .then((data) => setSession(data))
        .catch(() => {
          setSession(null);
          localStorage.clear(); // Log out the user if session fetch fails
        });
    }
  }, [token]);

const renderRoute = ({ element: Component, isProtected, roles }: any) => {
  if (isProtected) {
    if (!token || !session) {
      return <Navigate to="/login" />;
    }

    // Check role-based permissions for protected routes
    if (roles && !roles.some((role: string) => session.role === role)) {
      return <Navigate to="/unauthorized" />;
    }
  }

  return <Component />;
};

  return (
    <Router>
      <Routes>
        {ROUTER_CONFIG.map((route) => (
          <Route
            key={route.path}
            path={route.path}
            element={renderRoute(route)} // Call renderRoute to render the route element
          />
        ))}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
};

export default App;

