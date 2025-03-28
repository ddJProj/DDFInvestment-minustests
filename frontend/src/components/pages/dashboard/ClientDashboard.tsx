// src/components/pages/dashboard/ClientDashboard.tsx
import React, { useEffect, useState } from 'react';
import { apiService } from '../../../services/api.service';
import { authUtils } from '../../../utils/auth.utils';
import { useAuth } from '../../../hooks/useAuth';

interface Client {
  id: number;
  clientId: string;
  firstName: string;
  lastName: string;
  email: string;
  assignedEmployeeId: string;
  assignedEmployeeName: string;
  userAccountId: number;
}

interface Investment {
  id: number;
  name: string;
  type: string;
  amount: number;
  date: string;
  status: string;
  clientId: string;
}

const ClientDashboard: React.FC = () => {
  const { logout } = useAuth();
  const [activeTab, setActiveTab] = useState('overview');
  const [client, setClient] = useState<Client | null>(null);
  const [investments, setInvestments] = useState<Investment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [notification, setNotification] = useState<{
    type: 'success' | 'error' | 'info',
    message: string
  } | null>(null);
  const user = authUtils.getUser();

  // fetch client details
  useEffect(() => {
    const fetchClientDetails = async () => {
      if (!user?.id) return;
      
      try {
        setLoading(true);
        
        // Get client by user account ID
        try {
          const clientResponse = await apiService.client.getClientByUserId(user.id);
          setClient(clientResponse.data);
        } catch (clientErr) {
          console.error('Error fetching client by user ID:', clientErr);
          
          // Fallback: try to find client by checking all clients
          const allClientsResponse = await apiService.client.getAllClients();
          const clientData = allClientsResponse.data.find(
            (cl: Client) => cl.userAccountId === user.id
          );
          
          if (clientData) {
            setClient(clientData);
          } else {
            setError('Could not retrieve your client profile');
          }
        }
        
        // Once we have the client data, fetch their investments
        if (client?.clientId) {
          try {
            const investmentsResponse = await apiService.client.getClientInvestments(client.clientId);
            setInvestments(investmentsResponse.data);
          } catch (investmentErr) {
            console.warn('Error fetching investments:', investmentErr);
            // In case the investments API isn't fully implemented yet, use sample data
            setInvestments([
              {
                id: 1,
                name: 'Tech Growth Fund',
                type: 'Mutual Fund',
                amount: 10000,
                date: '2024-01-15',
                status: 'Active',
                clientId: client.clientId
              },
              {
                id: 2,
                name: 'Green Energy ETF',
                type: 'ETF',
                amount: 5000,
                date: '2024-02-20',
                status: 'Active',
                clientId: client.clientId
              }
            ]);
          }
        }
        
        setLoading(false);
      } catch (err) {
        setError('Failed to load client information');
        console.error(err);
        setLoading(false);
      }
    };

    fetchClientDetails();
  }, [user?.id]);

  // Handle profile update
  const handleUpdateProfile = async () => {
    if (!client?.id) return;
    
    try {
      // Implement profile update logic when backend is ready
      // await apiService.client.updateProfile(client.id, updatedData);
      
      setNotification({
        type: 'info',
        message: 'Profile update feature coming soon'
      });
      
      setTimeout(() => setNotification(null), 3000);
    } catch (err) {
      setError('Failed to update profile');
      console.error(err);
    }
  };

  // Handle password change
  const handleChangePassword = async () => {
    if (!user?.id) return;
    
    try {
      // This will be implemented when the password change feature is built
      // await apiService.user.updatePassword(user.id, currentPassword, newPassword, confirmPassword);
      
      setNotification({
        type: 'info',
        message: 'Password change feature coming soon'
      });
      
      setTimeout(() => setNotification(null), 3000);
    } catch (err) {
      setError('Failed to change password');
      console.error(err);
    }
  };

  // Handle user logout with proper API call
  const handleLogout = async () => {
    try {
      await logout();
      // Redirect happens in the logout function
    } catch (err) {
      console.error('Logout failed:', err);
      setNotification({
        type: 'error',
        message: 'Logout failed. Please try again.'
      });
    }
  };

  const renderOverview = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Account Overview</h2>
      
      {loading ? (
        <div className="flex justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : client ? (
        <div>
          <div className="bg-white p-6 rounded-lg shadow-md mb-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <h3 className="text-gray-500 font-medium">Client ID</h3>
                <p className="text-lg">{client.clientId}</p>
              </div>
              
              <div>
                <h3 className="text-gray-500 font-medium">Name</h3>
                <p className="text-lg">{`${client.firstName} ${client.lastName}`}</p>
              </div>
              
              <div>
                <h3 className="text-gray-500 font-medium">Email</h3>
                <p className="text-lg">{client.email}</p>
              </div>
              
              <div>
                <h3 className="text-gray-500 font-medium">Account Manager</h3>
                <p className="text-lg">{client.assignedEmployeeName || "Not assigned"}</p>
              </div>
            </div>
          </div>
          
          <h3 className="text-lg font-semibold mb-3">Your Investments</h3>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {investments.length > 0 ? (
              investments.map(investment => (
                <div key={investment.id} className="bg-white p-4 rounded shadow">
                  <h4 className="font-medium">{investment.name}</h4>
                  <p className="text-sm text-gray-500">{investment.type}</p>
                  <p className="text-lg font-bold mt-2">${investment.amount.toLocaleString()}</p>
                  <div className="flex justify-between mt-2">
                    <span className="text-xs">{new Date(investment.date).toLocaleDateString()}</span>
                    <span className={`text-xs px-2 py-1 rounded ${
                      investment.status === 'Active' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {investment.status}
                    </span>
                  </div>
                </div>
              ))
            ) : (
              <div className="col-span-3 bg-white p-4 rounded shadow text-center">
                <p>You don't have any investments yet.</p>
                <button className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                  Explore Investment Options
                </button>
              </div>
            )}
          </div>
        </div>
      ) : (
        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <p className="text-lg mb-2">No client account found.</p>
          <p className="text-gray-600">Please contact customer service to set up your account.</p>
        </div>
      )}
    </div>
  );

  const renderInvestments = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">My Investments</h2>
      
      {loading ? (
        <div className="flex justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : investments.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white">
            <thead>
              <tr className="bg-gray-100">
                <th className="py-2 px-4 text-left">Investment</th>
                <th className="py-2 px-4 text-left">Type</th>
                <th className="py-2 px-4 text-left">Amount</th>
                <th className="py-2 px-4 text-left">Date</th>
                <th className="py-2 px-4 text-left">Status</th>
                <th className="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {investments.map((investment) => (
                <tr key={investment.id} className="border-t">
                  <td className="py-2 px-4">{investment.name}</td>
                  <td className="py-2 px-4">{investment.type}</td>
                  <td className="py-2 px-4">${investment.amount.toLocaleString()}</td>
                  <td className="py-2 px-4">{new Date(investment.date).toLocaleDateString()}</td>
                  <td className="py-2 px-4">
                    <span className={`px-2 py-1 rounded text-xs ${
                      investment.status === 'Active' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'
                    }`}>
                      {investment.status}
                    </span>
                  </td>
                  <td className="py-2 px-4">
                    <button 
                      className="text-blue-500 hover:underline"
                      onClick={() => window.location.href = `/investments/${investment.id}`}
                    >
                      Details
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <p className="text-lg mb-2">You don't have any investments yet.</p>
          <button className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
            Explore Investment Options
          </button>
        </div>
      )}
    </div>
  );
  
  const renderProfile = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">My Profile</h2>
      
      {loading ? (
        <div className="flex justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
      ) : client ? (
        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <h3 className="text-gray-500 font-medium">Client ID</h3>
              <p className="text-lg">{client.clientId}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Name</h3>
              <p className="text-lg">{`${client.firstName} ${client.lastName}`}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Email</h3>
              <p className="text-lg">{client.email}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Account Manager</h3>
              <p className="text-lg">{client.assignedEmployeeName || "Not assigned"}</p>
            </div>
          </div>
          
          <div className="mt-6">
            <button 
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 mr-2"
              onClick={handleUpdateProfile}
            >
              Edit Profile
            </button>
            <button 
              className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
              onClick={handleChangePassword}
            >
              Change Password
            </button>
          </div>
        </div>
      ) : (
        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <p className="text-lg mb-2">No profile information found.</p>
          <p className="text-gray-600">Please contact customer service if you believe this is an error.</p>
        </div>
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
          <h1 className="text-white text-xl font-bold mb-6">Client Portal</h1>
          
          <nav>
            <ul>
              <li className="mb-2">
                <button 
                  onClick={() => setActiveTab('overview')}
                  className={`w-full text-left px-4 py-2 rounded ${
                    activeTab === 'overview' ? 'bg-gray-700 text-white' : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  Overview
                </button>
              </li>
              <li className="mb-2">
                <button 
                  onClick={() => setActiveTab('investments')}
                  className={`w-full text-left px-4 py-2 rounded ${
                    activeTab === 'investments' ? 'bg-gray-700 text-white' : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  My Investments
                </button>
              </li>
              <li className="mb-2">
                <button 
                  onClick={() => setActiveTab('profile')}
                  className={`w-full text-left px-4 py-2 rounded ${
                    activeTab === 'profile' ? 'bg-gray-700 text-white' : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  My Profile
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
              {activeTab === 'overview' && 'Account Overview'}
              {activeTab === 'investments' && 'My Investments'}
              {activeTab === 'profile' && 'My Profile'}
            </h1>
          </header>
          
          {/* Error message */}
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded m-4">
              {error}
            </div>
          )}
          
          {/* Content based on active tab */}
          {activeTab === 'overview' && renderOverview()}
          {activeTab === 'investments' && renderInvestments()}
          {activeTab === 'profile' && renderProfile()}
        </div>
      </div>
    </div>
  );
};

export default ClientDashboard;