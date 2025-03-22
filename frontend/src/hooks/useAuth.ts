// src/hooks/useAuth.ts
import { useState } from "react";
import { authUtils } from "../utils/auth.utils";

interface AuthResponse {
  token: string;
  role: string;
}

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(authUtils.isAuthenticated());
  const [authError, setAuthError] = useState<string | null>(null);

  const login = async (email: string, password: string): Promise<AuthResponse | null> => {
    try {
      const response = await fetch("http://127.0.0.1:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Invalid credentials");
      }

      const data = await response.json();
      authUtils.setAuthData(data.token, { role: data.role });
      setIsAuthenticated(true);
      return data;
    } catch (error) {
      setAuthError(error instanceof Error ? error.message : "Login failed");
      setIsAuthenticated(false);
      return null;
    }
  };

  const logout = () => {
    authUtils.clearAuthData();
    setIsAuthenticated(false);
  };

  return { login, logout, isAuthenticated, authError };
};

