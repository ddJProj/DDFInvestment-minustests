// src/components/pages/dashboard/AdminDashboard.tsx
import React, { useEffect, useState } from 'react';
import { UserRole } from '../../../types/auth.types';
import { authUtils } from '../../../utils/auth.utils';
import { apiService } from '../../../services/api.service';
import { useAuth } from '../../../hooks/useAuth';

// interfaces for our data structures
interface SystemInfo {
  totalUserAccounts: number;
  totalAdminAccounts: number;
  totalEmployeeAccounts: number;
  totalClientAccounts: number;
  totalGuestAccounts: number;
  countEmployeeRepo?: number;
  countClientRepo?: number;
}

interface UpgradeRequest {
  id: number;
  userAccountId: number;
  userEmail: string;
  userFirstName: string;
  userLastName: string;
  requestDate: string;
  details: string;
  status: string;
}

interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
  permissions?: string[];
}

const AdminDashboard: React.FC = () => {
  const { logout } = useAuth();
  
  // state for the active tab
  const [activeTab, setActiveTab] = useState('dashboard');
  
  // state for the dashboard data
  const [systemInfo, setSystemInfo] = useState<SystemInfo | null>(null);
  const [pendingRequests, setPendingRequests] = useState<UpgradeRequest[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  
  // state for UI controls
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterRole, setFilterRole] = useState<string | null>(null);
  
  // setup a notification system
  const [notification, setNotification] = useState<{
    type: 'success' | 'error' | 'info',
    message: string
  } | null>(null);

  // fetch system info for the dashboard
  useEffect(() => {
    const fetchSystemInfo = async () => {
      setLoading(true);
      try {
        const response = await apiService.admin.getSystemInfo();
        setSystemInfo(response.data);
        setLoading(false);
      } catch (err) {
        console.error('Failed to load system information', err);
        // dont set error state (prevent UI issues during dev)
        // provide default data instead of crashing
        setSystemInfo({
          totalUserAccounts: 0,
          totalAdminAccounts: 0,
          totalEmployeeAccounts: 0,
          totalClientAccounts: 0,
          totalGuestAccounts: 0
        });
        setLoading(false);
      }
    };

    if (activeTab === 'dashboard') {
      fetchSystemInfo();
    }
  }, [activeTab]);

  // fetch pending upgrade requests
  useEffect(() => {
    const fetchPendingRequests = async () => {
      setLoading(true);
      try {
        const response = await apiService.admin.getPendingRequests();
        setPendingRequests(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to load pending requests');
        console.error(err);
        setLoading(false);
      }
    };

    if (activeTab === 'requests') {
      fetchPendingRequests();
    }
  }, [activeTab]);

  // fetch users based on filter
  useEffect(() => {
    const fetchUsers = async () => {
      setLoading(true);
      try {
        let response;
        if (filterRole) {
          response = await apiService.admin.getUsersByRole(filterRole);
        } else {
          response = await apiService.admin.getAllUsers();
        }
        setUsers(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to load user accounts');
        console.error(err);
        setLoading(false);
      }
    };

    if (activeTab === 'users') {
      fetchUsers();
    }
  }, [activeTab, filterRole]);

  // handle approving an upgrade request
  const handleApproveRequest = async (id: number) => {
    try {
      const request = pendingRequests.find(req => req.id === id);
      if (!request) return;
      
      const clientData = {
        userAccountId: request.userAccountId,
        firstName: request.userFirstName,
        lastName: request.userLastName,
        email: request.userEmail
      };
      
      await apiService.admin.approveRequest(id, clientData);
      
      // show success notification
      setNotification({
        type: 'success',
        message: `Successfully approved upgrade request for ${request.userFirstName} ${request.userLastName}`
      });
      
      // update the requests list
      setPendingRequests(pendingRequests.filter(req => req.id !== id));
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    } catch (err) {
      setError('Failed to approve request');
      console.error(err);
      
      // show error notification
      setNotification({
        type: 'error',
        message: 'Failed to approve the upgrade request. Please try again.'
      });
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    }
  };

  // handle rejecting an upgrade request
  const handleRejectRequest = async (id: number) => {
    try {
      const request = pendingRequests.find(req => req.id === id);
      if (!request) return;
      
      const reason = 'Request rejected by administrator.'; // You could show a modal to get this
      await apiService.admin.rejectRequest(id, reason);
      
      // Show success notification
      setNotification({
        type: 'success',
        message: `Successfully rejected upgrade request for ${request.userFirstName} ${request.userLastName}`
      });
      
      // update the requests list
      setPendingRequests(pendingRequests.filter(req => req.id !== id));
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    } catch (err) {
      setError('Failed to reject request');
      console.error(err);
      
      // show error notification
      setNotification({
        type: 'error',
        message: 'Failed to reject the upgrade request. Please try again.'
      });
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    }
  };

  // handle changing a user's role
  const handleChangeRole = async (userId: number, newRole: string) => {
    try {
      await apiService.admin.changeUserRole(userId, newRole);
      
      // update user list UI
      setUsers(users.map(user => 
        user.id === userId ? { ...user, role: newRole } : user
      ));
      
      // show success notification
      setNotification({
        type: 'success',
        message: `Successfully changed user's role to ${newRole}`
      });
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    } catch (err) {
      setError('Failed to change user role');
      console.error(err);
      
      // show error notification
      setNotification({
        type: 'error',
        message: 'Failed to change user role. Please try again.'
      });
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    }
  };

  // handle user logout with proper API call
  const handleLogout = async () => {
    try {
      // call the logout function from useAuth which will trigger the backend API
      await logout();
      // redirect happens automatically in the logout function
    } catch (err) {
      console.error('Logout failed:', err);
      setNotification({
        type: 'error',
        message: 'Logout failed. Please try again.'
      });
    }
  };

  // render system dashboard
  const renderDashboard = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">System Dashboard</h2>
      
      {loading ? (
        <div className="flex justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : systemInfo ? (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold text-gray-700">User Accounts</h3>
            <p className="text-3xl font-bold">{systemInfo.totalUserAccounts}</p>
          </div>
          
          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold text-gray-700">Admin Accounts</h3>
            <p className="text-3xl font-bold">{systemInfo.totalAdminAccounts}</p>
          </div>
          
          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold text-gray-700">Employee Accounts</h3>
            <p className="text-3xl font-bold">{systemInfo.totalEmployeeAccounts}</p>
            {systemInfo.countEmployeeRepo !== undefined && (
              <p className="text-sm text-gray-500">Repository count: {systemInfo.countEmployeeRepo}</p>
            )}
          </div>
          
          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold text-gray-700">Client Accounts</h3>
            <p className="text-3xl font-bold">{systemInfo.totalClientAccounts}</p>
            {systemInfo.countClientRepo !== undefined && (
              <p className="text-sm text-gray-500">Repository count: {systemInfo.countClientRepo}</p>
            )}
          </div>
          
          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold text-gray-700">Guest Accounts</h3>
            <p className="text-3xl font-bold">{systemInfo.totalGuestAccounts}</p>
          </div>
          
          <div className="bg-white p-4 rounded shadow">
            <h3 className="font-semibold text-gray-700">Pending Requests</h3>
            <p className="text-3xl font-bold">{pendingRequests.length}</p>
            <button 
              className="mt-2 text-blue-500 hover:underline"
              onClick={() => setActiveTab('requests')}
            >
              View Requests →
            </button>
          </div>
        </div>
      ) : (
        <p>No system information available.</p>
      )}
    </div>
  );

  // render pending upgrade requests
  const renderRequests = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Pending Upgrade Requests</h2>
      
      {loading ? (
        <div className="flex justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : pendingRequests.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white">
            <thead>
              <tr className="bg-gray-100">
                <th className="py-2 px-4 text-left">Name</th>
                <th className="py-2 px-4 text-left">Email</th>
                <th className="py-2 px-4 text-left">Request Date</th>
                <th className="py-2 px-4 text-left">Details</th>
                <th className="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {pendingRequests.map((request) => (
                <tr key={request.id} className="border-t">
                  <td className="py-2 px-4">{`${request.userFirstName} ${request.userLastName}`}</td>
                  <td className="py-2 px-4">{request.userEmail}</td>
                  <td className="py-2 px-4">{new Date(request.requestDate).toLocaleDateString()}</td>
                  <td className="py-2 px-4">{request.details}</td>
                  <td className="py-2 px-4 flex space-x-2">
                    <button 
                      onClick={() => handleApproveRequest(request.id)}
                      className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                    >
                      Approve
                    </button>
                    <button 
                      onClick={() => handleRejectRequest(request.id)}
                      className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                    >
                      Reject
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <p className="text-lg mb-2">No pending upgrade requests.</p>
          <p className="text-gray-600">When guest users request an upgrade to client status, they will appear here.</p>
        </div>
      )}
    </div>
  );

  // render user management
  const renderUsers = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">User Management</h2>
      
      <div className="mb-4 flex items-center">
        <label className="mr-2 font-semibold">Filter by Role:</label>
        <select 
          className="p-2 border rounded"
          value={filterRole || ''}
          onChange={(e) => setFilterRole(e.target.value || null)}
        >
          <option value="">All Users</option>
          <option value={UserRole.Admin}>Admins</option>
          <option value={UserRole.Employee}>Employees</option>
          <option value={UserRole.Client}>Clients</option>
          <option value={UserRole.Guest}>Guests</option>
        </select>
      </div>
      
      {loading ? (
        <div className="flex justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : users.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white">
            <thead>
              <tr className="bg-gray-100">
                <th className="py-2 px-4 text-left">ID</th>
                <th className="py-2 px-4 text-left">Name</th>
                <th className="py-2 px-4 text-left">Email</th>
                <th className="py-2 px-4 text-left">Role</th>
                <th className="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id} className="border-t">
                  <td className="py-2 px-4">{user.id}</td>
                  <td className="py-2 px-4">{`${user.firstName} ${user.lastName}`}</td>
                  <td className="py-2 px-4">{user.email}</td>
                  <td className="py-2 px-4">
                    <span className={`px-2 py-1 rounded text-xs ${
                      user.role === UserRole.Admin 
                        ? 'bg-purple-100 text-purple-800' 
                        : user.role === UserRole.Employee
                        ? 'bg-blue-100 text-blue-800'
                        : user.role === UserRole.Client
                        ? 'bg-green-100 text-green-800'
                        : 'bg-gray-100 text-gray-800'
                    }`}>
                      {user.role}
                    </span>
                  </td>
                  <td className="py-2 px-4">
                    <div className="flex space-x-2">
                      <select 
                        className="p-1 border rounded text-sm"
                        value=""
                        onChange={(e) => {
                          if (e.target.value) {
                            handleChangeRole(user.id, e.target.value);
                            e.target.value = '';
                          }
                        }}
                      >
                        <option value="">Change Role</option>
                        <option value={UserRole.Admin} disabled={user.role === UserRole.Admin}>Admin</option>
                        <option value={UserRole.Employee} disabled={user.role === UserRole.Employee}>Employee</option>
                        <option value={UserRole.Client} disabled={user.role === UserRole.Client}>Client</option>
                        <option value={UserRole.Guest} disabled={user.role === UserRole.Guest}>Guest</option>
                      </select>
                      
                      <button 
                        className="text-blue-500 hover:underline"
                        onClick={() => window.location.href = `/users/edit/${user.id}`}
                      >
                        Edit
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p>No users found.</p>
      )}
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Notification toast */}
      {notification && (
        <div className={`fixed top-4 right-4 z-50 p-4 rounded-lg shadow-lg ${
          notification.type === 'success' ? 'bg-green-100 border-l-4 border-green-500 text-green-700' :
          notification.type === 'error' ? 'bg-red-100 border-l-4 border-red-500 text-red-700' :
          'bg-blue-100 border-l-4 border-blue-500 text-blue-700'
        }`}>
          <div className="flex items-center">
            {notification.type === 'success' && (
              <svg className="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
              </svg>
            )}
            {notification.type === 'error' && (
              <svg className="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            )}
            {notification.type === 'info' && (
              <svg className="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            )}
            <span>{notification.message}</span>
          </div>
        </div>
      )}

      <div className="flex">
        {/* Sidebar Navigation */}
        <div className="w-64 bg-gray-800 min-h-screen p-4">
          <h1 className="text-white text-xl font-bold mb-6">Admin Portal</h1>
          
          <nav>
            <ul>
              <li className="mb-2">
                <button 
                  onClick={() => setActiveTab('dashboard')}
                  className={`w-full text-left px-4 py-2 rounded ${
                    activeTab === 'dashboard' ? 'bg-gray-700 text-white' : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  Dashboard
                </button>
              </li>
              <li className="mb-2">
                <button 
                  onClick={() => setActiveTab('requests')}
                  className={`w-full text-left px-4 py-2 rounded ${
                    activeTab === 'requests' ? 'bg-gray-700 text-white' : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  Upgrade Requests
                </button>
              </li>
              <li className="mb-2">
                <button 
                  onClick={() => setActiveTab('users')}
                  className={`w-full text-left px-4 py-2 rounded ${
                    activeTab === 'users' ? 'bg-gray-700 text-white' : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  User Management
                </button>
              </li>
            </ul>
          </nav>
          
          <div className="absolute bottom-4 left-4 right-4">
            <button
              onClick={handleLogout}
              className="w-full text-left px-4 py-2 text-gray-300 hover:bg-gray-700 rounded"
            >
              Logout
            </button>
          </div>
        </div>
        
        {/* Main Content */}
        <div className="flex-1">
          {/* Header */}
          <header className="bg-white shadow p-4">
            <h1 className="text-2xl font-bold text-gray-800">
              {activeTab === 'dashboard' && 'System Dashboard'}
              {activeTab === 'requests' && 'Upgrade Requests'}
              {activeTab === 'users' && 'User Management'}
            </h1>
          </header>
          
          {/* Error message */}
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded m-4">
              {error}
            </div>
          )}
          
          {/* Content based on active tab */}
          {activeTab === 'dashboard' && renderDashboard()}
          {activeTab === 'requests' && renderRequests()}
          {activeTab === 'users' && renderUsers()}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;