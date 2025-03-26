/* eslint-disable @typescript-eslint/no-explicit-any */
// src/hooks/useRegistration.ts
//
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiService } from '../services/api.service';

export function useRegister() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleRegister = async (
    firstName: string,
    lastName: string,
    email: string,
    password: string
  ) => {
    setIsLoading(true);
    setError(null);

    try {
      await apiService.auth.register(firstName, lastName, email, password);
      navigate('/login', {
        state: {
          message:
            'Registration successful! Please log in with your new account.',
        },
      });
      return true;
    } catch (err: any) {
      const errorMessage =
        err.response?.data?.message || 'Registration failed. Please try again.';
      setError(errorMessage);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  return { handleRegister, isLoading, error };
}
