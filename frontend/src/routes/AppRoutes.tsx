
// src/routes/AppRoutes.tsx
import React from "react";
import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
import { ROUTER_CONFIG } from "../constants/router.constants";

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      {ROUTER_CONFIG.map(({ path, element, isProtected, roles }) => (
        <Route
          key={path}
          path={path}
          element={
            <ProtectedRoute element={<>{element}</>} isProtected={isProtected} roles={roles} />
          }
        />
      ))}
    </Routes>
  );
};

export default AppRoutes;

