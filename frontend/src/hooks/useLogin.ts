// src/hooks/useLogin.ts
import { useState } from "react";
import { useAuth } from "./useAuth";
import { useNavigate } from "react-router-dom";

export const useLogin = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleLogin = async (email: string, password: string) => {
    setIsLoading(true);
    setError(null);

    try {
      const result = await login(email, password);
      if (result) {
        navigate("/dashboard");
      } else {
        setError("Invalid email or password");
      }
    } catch (err) {
      setError("An error occurred during login");
    } finally {
      setIsLoading(false);
    }
  };

  return { handleLogin, isLoading, error };
};

