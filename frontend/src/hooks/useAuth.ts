/* eslint-disable @typescript-eslint/no-explicit-any */
// src/hooks/useAuth.ts
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiService } from '../services/api.service';
import { authUtils } from '../utils/auth.utils';
import { UserRole } from '../types/auth.types';

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(
    authUtils.isAuthenticated()
  );
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  const login = async (email: string, password: string) => {
    setLoading(true);
    setError(null);

    try {
      const response = await apiService.auth.login(email, password);

      // Store the token and user info
      const { token, role } = response.data;

      // TODO: add logic to extract role from JWT payload?
      const userData = {
        email,
        role: role || UserRole.Guest, // fallback to userRole Guest / limited permission
      };

      authUtils.setAuthData(token, userData);
      setIsAuthenticated(true);

      // redirect based on role
      switch(userData.role){
        case UserRole.Admin:
          navigate('/dashboard/admin');
          break;
        case UserRole.Employee:
          navigate('/dashboard/employee');
          break;
        case UserRole.Client:
          navigate('/dashboard/client');
          break;
        default:
          navigate('/dashboard');
      }

      return true;

    } catch (err: any) {
      console.error('Error logging in: ', err);  // added to troubleshoot
      setError(
        err.response?.data?.message ||
          'Login failed. Please check your credentials.'
      );
      return false;
    } finally {
      setLoading(false);
    }
  };

  const register = async (
    firstName: string,
    lastName: string,
    email: string,
    password: string
  ) => {
    setLoading(true);
    setError(null);

    try {
      await apiService.auth.register(firstName, lastName, email, password);
      navigate('/login', {
        state: { message: 'Registration successful! Please log in.' },
      });
      return true;
    } catch (err: any) {
      setError(
        err.response?.data?.message || 'Registration failed. Please try again.'
      );
      return false;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    authUtils.clearAuthData();
    setIsAuthenticated(false);
    navigate('/login');
  };

  return {
    isAuthenticated,
    loading,
    error,
    login,
    register,
    logout,
  };
};
