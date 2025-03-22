// src/hooks/useRegistration.ts
//
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export function useRegister() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleRegister = async (firstName: string, lastName: string, email: string, password: string) => {
    setIsLoading(true);
    setError(null);

    try {
      const response = await fetch("http://127.0.0.1:8080/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          firstName,
          lastName,
          email,
          password,
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to register. Please try again.");
      }

      navigate("/login"); // Redirect to login after successful registration
    } catch (err) {
      setError(err instanceof Error ? err.message : "Something went wrong");
    } finally {
      setIsLoading(false);
    }
  };

  return { handleRegister, isLoading, error };
}

