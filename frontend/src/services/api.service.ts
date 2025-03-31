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
      
      // dont clear token for specific endpoints
      const isAuthEndpoint = error.config?.url?.includes('/api/auth/');
      const isAdminEndpoint = error.config?.url?.includes('/api/admin/');
      
      // token expiration: only redirect auth errors for non-admin and non-auth endpoints
      // only log out on 401 (unauthorized) not 403 (forbidden)
      if (error.response && error.response.status === 401) {
        console.log("Unauthorized access detected (401), logging out");
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

    logout: () => axiosInstance.post('/auth/logout'),

    // client upgrade request
    requestUpgrade: (userAccountId: number, details: string) =>
      axiosInstance.post('/guests/request-client-upgrade', {
        userAccountId,
        details,
      }),

    // Get a user's upgrade requests
    getUserUpgradeRequests: (userAccountId: number) =>
      axiosInstance.get(`/guests/upgrade-requests/${userAccountId}`),

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
    changeUserRole: (id: number, newRole: string) =>
      axiosInstance.put(`/users/${id}/role?updatedRole=${newRole}`),
  },

  // employee endpoints
  employee: {
    getAllClients: () => axiosInstance.get('/client'),
    getClientsByEmployeeId: (employeeId: string) =>
      axiosInstance.get(`/client/by-employee/${employeeId}`),
    assignClient: (clientId: string, employeeId: string) =>
      axiosInstance.post('/client/assign', { clientId, employeeId }),
    // Added new methods to support employee dashboard
    getAllEmployees: () => axiosInstance.get('/employees'),
    getEmployeeById: (id: number) => axiosInstance.get(`/employees/${id}`),
    getEmployeeByUserId: (userId: number) => axiosInstance.get(`/employees/by-user/${userId}`),
    getEmployeeByEmployeeId: (employeeId: string) => axiosInstance.get(`/employees/employee-id/${employeeId}`),
    updateEmployee: (id: number, data: any) => axiosInstance.put(`/employees/${id}`, data),
},

  // client endpoints
  client: {
    getClientDetails: (id: number) => axiosInstance.get(`/client/${id}`),
    getAllClients: () => axiosInstance.get('/client'),
    getClientByUserId: (userId: number) => axiosInstance.get(`/client/by-user/${userId}`),
    getClientByClientId: (clientId: string) => axiosInstance.get(`/client/client-id/${clientId}`),
    updateClient: (id: number, data: any) => axiosInstance.put(`/client/${id}`, data),
    getClientInvestments: (clientId: string) => axiosInstance.get(`/investments/client/${clientId}`),
  },
};

// test for direct auth:
export const testDirectAuth = () => {
  console.log("Testing direct authentication...");
  
  axios.post('http://localhost:8080/api/auth/authenticate', {
    email: 'admin@example.com',
    password: 'password'
  }, {
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    console.log("Authentication successful:", response.data);
    return response.data;
  })
  .catch(error => {
    console.error("Authentication failed:", error);
    console.error("Status:", error.response?.status);
    console.error("Data:", error.response?.data);
    console.error("Headers:", error.response?.headers);
    throw error;
  });
};


// general request function 
export const apiRequest = async <T>(config: AxiosRequestConfig): Promise<T> => {
  try {
    const response = await axiosInstance(config);
    return response.data;
  } catch (error) {
    throw error;
  }



};
