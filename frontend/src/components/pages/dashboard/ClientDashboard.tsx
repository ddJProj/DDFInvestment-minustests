// src/components/pages/dashboard/ClientDashboard.tsx
import React, { useEffect, useState } from 'react';
import { apiService } from '../../../services/api.service';
import { authUtils } from '../../../utils/auth.utils';

interface Client {
  id: number;
  clientId: string;
  firstName: string;
  lastName: string;
  email: string;
  assignedEmployeeId: string;
  assignedEmployeeName: string;
}

interface Investment {
  id: number;
  name: string;
  type: string;
  amount: number;
  date: string;
  status: string;
}

const ClientDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState('overview');
  const [client, setClient] = useState<Client | null>(null);
  const [investments, setInvestments] = useState<Investment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const user = authUtils.getUser();

  // fetch client details
  useEffect(() => {
    const fetchClientDetails = async () => {
      if (!user?.id) return;
      
      try {
        // Simulate the content with this test client / mock  
        setClient({
          id: 1,
          clientId: 'C10045',
          firstName: 'John',
          lastName: 'Doe',
          email: 'john.doe@example.com',
          assignedEmployeeId: 'E10001',
          assignedEmployeeName: 'Sarah Johnson'
        });
        
        // mock investments data
        setInvestments([
          {
            id: 1,
            name: 'Tech Growth Fund',
            type: 'Mutual Fund',
            amount: 10000,
            date: '2024-01-15',
            status: 'Active'
          },
          {
            id: 2,
            name: 'Green Energy ETF',
            type: 'ETF',
            amount: 5000,
            date: '2024-02-20',
            status: 'Active'
          },
          {
            id: 3,
            name: 'Blue Chip Portfolio',
            type: 'Stock Bundle',
            amount: 15000,
            date: '2024-03-10',
            status: 'Processing'
          }
        ]);
        
        setLoading(false);
      } catch (err) {
        setError('Failed to load client information');
        console.error(err);
        setLoading(false);
      }
    };

    fetchClientDetails();
  }, [user?.id]);

  const renderOverview = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Account Overview</h2>
      
      {loading ? (
        <p>Loading your account information...</p>
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
                <p className="text-lg">{client.assignedEmployeeName}</p>
              </div>
            </div>
          </div>
          
          <h3 className="text-lg font-semibold mb-3">Your Investments</h3>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {investments.map(investment => (
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
            ))}
          </div>
        </div>
      ) : (
        <p>No account information found.</p>
      )}
    </div>
  );

  const renderInvestments = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">My Investments</h2>
      
      {loading ? (
        <p>Loading your investments...</p>
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
        <div>
          <p>You don't have any investments yet.</p>
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
        <p>Loading profile information...</p>
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
              <p className="text-lg">{client.assignedEmployeeName}</p>
            </div>
          </div>
          
          <div className="mt-6">
            <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 mr-2">
              Edit Profile
            </button>
            <button className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
              Change Password
            </button>
          </div>
        </div>
      ) : (
        <p>No profile information found.</p>
      )}
    </div>
  );
  
  return (
    <div className="min-h-screen bg-gray-50">
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