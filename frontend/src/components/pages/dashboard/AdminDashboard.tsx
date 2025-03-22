// src/components/pages/dashboard/AdminDashboard.tsx
import React, { useEffect, useState } from 'react';
import { apiService } from '../../../services/api.service';
import { UserRole } from '../../../types/auth.types';

interface SystemInfo {
  totalUserAccounts: number;
  totalAdminAccounts: number;
  totalEmployeeAccounts: number;
  totalClientAccounts: number;
  totalGuestAccounts: number;
}

interface UpgradeRequest {
  id: number;
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
}

const AdminDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [systemInfo, setSystemInfo] = useState<SystemInfo | null>(null);
  const [pendingRequests, setPendingRequests] = useState<UpgradeRequest[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterRole, setFilterRole] = useState<string | null>(null);

  // fetch system info
  useEffect(() => {
    const fetchSystemInfo = async () => {
      try {
        const response = await apiService.admin.getSystemInfo();
        setSystemInfo(response.data);
      } catch (err) {
        setError('Failed to load system information');
        console.error(err);
      }
    };

    if (activeTab === 'dashboard') {
      fetchSystemInfo();
    }
  }, [activeTab]);

  // fetch pending upgrade requests
  useEffect(() => {
    const fetchPendingRequests = async () => {
      try {
        const response = await apiService.admin.getPendingRequests();
        setPendingRequests(response.data);
      } catch (err) {
        setError('Failed to load pending requests');
        console.error(err);
      }
    };

    if (activeTab === 'requests') {
      fetchPendingRequests();
    }
  }, [activeTab]);

  // fetch users
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
      } catch (err) {
        setError('Failed to load user accounts');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    if (activeTab === 'users') {
      fetchUsers();
    }
  }, [activeTab, filterRole]);

  const handleApproveRequest = async (id: number) => {
    try {
      // client data - additional fields can be populated in a real form
      const clientData = {
        userAccountId: pendingRequests.find((req) => req.id === id)?.id || 0,
      };

      await apiService.admin.approveRequest(id, clientData);

      // update requests list
      setPendingRequests(pendingRequests.filter((req) => req.id !== id));
    } catch (err) {
      setError('Failed to approve request');
      console.error(err);
    }
  };

  const handleRejectRequest = async (id: number) => {
    try {
      await apiService.admin.rejectRequest(id, 'Request rejected by admin');

      // update requests list
      setPendingRequests(pendingRequests.filter((req) => req.id !== id));
    } catch (err) {
      setError('Failed to reject request');
      console.error(err);
    }
  };

  const renderDashboard = () => (
    <div className="p-4">
      <h2 className="mb-4 text-xl font-bold">System Dashboard</h2>

      {systemInfo ? (
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <div className="rounded bg-white p-4 shadow">
            <h3 className="font-semibold text-gray-700">User Accounts</h3>
            <p className="text-3xl font-bold">{systemInfo.totalUserAccounts}</p>
          </div>

          <div className="rounded bg-white p-4 shadow">
            <h3 className="font-semibold text-gray-700">Admin Accounts</h3>
            <p className="text-3xl font-bold">
              {systemInfo.totalAdminAccounts}
            </p>
          </div>

          <div className="rounded bg-white p-4 shadow">
            <h3 className="font-semibold text-gray-700">Employee Accounts</h3>
            <p className="text-3xl font-bold">
              {systemInfo.totalEmployeeAccounts}
            </p>
          </div>

          <div className="rounded bg-white p-4 shadow">
            <h3 className="font-semibold text-gray-700">Client Accounts</h3>
            <p className="text-3xl font-bold">
              {systemInfo.totalClientAccounts}
            </p>
          </div>

          <div className="rounded bg-white p-4 shadow">
            <h3 className="font-semibold text-gray-700">Guest Accounts</h3>
            <p className="text-3xl font-bold">
              {systemInfo.totalGuestAccounts}
            </p>
          </div>
        </div>
      ) : (
        <p>Loading system information...</p>
      )}
    </div>
  );

  const renderRequests = () => (
    <div className="p-4">
      <h2 className="mb-4 text-xl font-bold">Pending Upgrade Requests</h2>

      {pendingRequests.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white">
            <thead>
              <tr className="bg-gray-100">
                <th className="px-4 py-2 text-left">Name</th>
                <th className="px-4 py-2 text-left">Email</th>
                <th className="px-4 py-2 text-left">Request Date</th>
                <th className="px-4 py-2 text-left">Details</th>
                <th className="px-4 py-2 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {pendingRequests.map((request) => (
                <tr key={request.id} className="border-t">
                  <td className="px-4 py-2">{`${request.userFirstName} ${request.userLastName}`}</td>
                  <td className="px-4 py-2">{request.userEmail}</td>
                  <td className="px-4 py-2">
                    {new Date(request.requestDate).toLocaleDateString()}
                  </td>
                  <td className="px-4 py-2">{request.details}</td>
                  <td className="flex space-x-2 px-4 py-2">
                    <button
                      onClick={() => handleApproveRequest(request.id)}
                      className="rounded bg-green-500 px-3 py-1 text-white hover:bg-green-600"
                    >
                      Approve
                    </button>
                    <button
                      onClick={() => handleRejectRequest(request.id)}
                      className="rounded bg-red-500 px-3 py-1 text-white hover:bg-red-600"
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
        <p>No pending upgrade requests.</p>
      )}
    </div>
  );

  const renderUsers = () => (
    <div className="p-4">
      <h2 className="mb-4 text-xl font-bold">User Management</h2>

      <div className="mb-4">
        <select
          className="rounded border p-2"
          value={filterRole || ''}
          onChange={(e) => setFilterRole(e.target.value || null)}
        >
          <option value="">All Users</option>
          <option value={UserRole.Admin}>Admins</option>
          <option value={UserRole.Employee}>Employees</option>
          <option value={UserRole.Client}>Clients</option>
          <option value={UserRole.Restricted}>Guests</option>
        </select>
      </div>

      {loading ? (
        <p>Loading users...</p>
      ) : users.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white">
            <thead>
              <tr className="bg-gray-100">
                <th className="px-4 py-2 text-left">ID</th>
                <th className="px-4 py-2 text-left">Name</th>
                <th className="px-4 py-2 text-left">Email</th>
                <th className="px-4 py-2 text-left">Role</th>
                <th className="px-4 py-2 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id} className="border-t">
                  <td className="px-4 py-2">{user.id}</td>
                  <td className="px-4 py-2">{`${user.firstName} ${user.lastName}`}</td>
                  <td className="px-4 py-2">{user.email}</td>
                  <td className="px-4 py-2">{user.role}</td>
                  <td className="px-4 py-2">
                    <button className="text-blue-500 hover:underline">
                      Edit
                    </button>
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
      <div className="flex">
        {/* Sidebar Navigation */}
        <div className="min-h-screen w-64 bg-gray-800 p-4">
          <h1 className="mb-6 text-xl font-bold text-white">Admin Portal</h1>

          <nav>
            <ul>
              <li className="mb-2">
                <button
                  onClick={() => setActiveTab('dashboard')}
                  className={`w-full rounded px-4 py-2 text-left ${
                    activeTab === 'dashboard'
                      ? 'bg-gray-700 text-white'
                      : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  Dashboard
                </button>
              </li>
              <li className="mb-2">
                <button
                  onClick={() => setActiveTab('requests')}
                  className={`w-full rounded px-4 py-2 text-left ${
                    activeTab === 'requests'
                      ? 'bg-gray-700 text-white'
                      : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  Upgrade Requests
                </button>
              </li>
              <li className="mb-2">
                <button
                  onClick={() => setActiveTab('users')}
                  className={`w-full rounded px-4 py-2 text-left ${
                    activeTab === 'users'
                      ? 'bg-gray-700 text-white'
                      : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  User Management
                </button>
              </li>
            </ul>
          </nav>
        </div>

        {/* Main Content */}
        <div className="flex-1">
          {/* Header */}
          <header className="bg-white p-4 shadow">
            <h1 className="text-2xl font-bold text-gray-800">
              {activeTab === 'dashboard' && 'System Dashboard'}
              {activeTab === 'requests' && 'Upgrade Requests'}
              {activeTab === 'users' && 'User Management'}
            </h1>
          </header>

          {/* error message */}
          {error && (
            <div className="m-4 rounded border border-red-400 bg-red-100 px-4 py-3 text-red-700">
              {error}
            </div>
          )}

          {/* content based on active tab */}
          {activeTab === 'dashboard' && renderDashboard()}
          {activeTab === 'requests' && renderRequests()}
          {activeTab === 'users' && renderUsers()}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
