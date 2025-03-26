import { request } from './api.service';
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable no-useless-catch */
// src/services/api.service..ts

import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';
import { authUtils } from '../utils/auth.utils';

const API_URL = 'http://localhost:8080/api';

const axiosInstance: AxiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// request interceptor to attach auth token to every request
axiosInstance.interceptors.request.use(
  (config) => {
    const token = authUtils.getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// response interceptor to handle common errors
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    // added to improve error tracking/handling
    console.error('API error: ', error.request?.status, error.response?.data); 
    // token expiration
    if (error.response && error.response.status === 401) {
      authUtils.clearAuthData();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// API service endpoints
export const apiService = {
  // authentication endpoints
  auth: {
    login: (email: string, password: string) =>
      axiosInstance.post('/auth/authenticate', { email, password }),
    
    register: (
      firstName: string,
      lastName: string,
      email: string,
      password: string
    ) =>
      axiosInstance.post('/auth/register', {
        firstName,
        lastName,
        email,
        password,
      }),

    // client upgrade request
    requestUpgrade: (userAccountId: number, details: string) =>
      axiosInstance.post('/guests/request-client-upgrade', {
        userAccountId,
        details,
      }),
  },

  // user account endpoints
  user: {
    getProfile: (id: number) => axiosInstance.get(`/users/${id}`),
    updateProfile: (id: number, data: any) =>
      axiosInstance.put(`/users/${id}`, data),
    updatePassword: (
      id: number,
      currentPassword: string,
      newPassword: string,
      passwordConfirmation: string
    ) =>
      axiosInstance.post(`/users/${id}/update-password`, {
        currentPassword,
        newPassword,
        passwordConfirmation,
      }),
  },

  // admin endpoints
  admin: {
    getSystemInfo: () => axiosInstance.get('/admin/system-info'),
    getPendingRequests: () => axiosInstance.get('/admin/pending-requests'),
    approveRequest: (id: number, clientData: any) =>
      axiosInstance.put(`/admin/upgrade-requests/${id}/approve`, clientData),
    rejectRequest: (id: number, reason: string) =>
      axiosInstance.put(
        `/admin/upgrade-requests/${id}/reject?reason=${encodeURIComponent(reason)}`
      ),
    getAllUsers: () => axiosInstance.get('/users'),
    getUsersByRole: (role: string) =>
      axiosInstance.get(`/users/by-role/${role}`),
  },

  // employee endpoints
  employee: {
    getAllClients: () => axiosInstance.get('/client'),
    getClientsByEmployeeId: (employeeId: string) =>
      axiosInstance.get(`/client/by-employee/${employeeId}`),
    assignClient: (clientId: string, employeeId: string) =>
      axiosInstance.post('/client/assign', { clientId, employeeId }),
  },

  // client endpoints
  client: {
    getClientDetails: (id: number) => axiosInstance.get(`/client/${id}`),
  },
};

// general request function 
export const request = async <T>(config: AxiosRequestConfig): Promise<T> => {
  try {
    const response = await axiosInstance(config);
    return response.data;
  } catch (error) {
    throw error;
  }
};
