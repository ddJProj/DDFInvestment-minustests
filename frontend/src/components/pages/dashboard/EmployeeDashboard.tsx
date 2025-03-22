/* eslint-disable @typescript-eslint/no-unused-vars */
// src/components/pages/dashboard/EmployeeDashboard.tsx
import React, { useEffect, useState } from 'react';
import { apiService } from '../../../services/api.service';
import { authUtils } from '../../../utils/auth.utils';

interface Client {
  id: number;
  clientId: string;
  firstName: string;
  lastName: string;
  email: string;
}

interface Employee {
  id: number;
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  locationId: string;
  title: string;
  assignedClientList: string[];
}

const EmployeeDashboard: React.FC = () => {
  const [activeTab, setActiveTab] = useState('clients');
  const [clients, setClients] = useState<Client[]>([]);
  const [employee, setEmployee] = useState<Employee | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const user = authUtils.getUser();

  // fetch employee details
  useEffect(() => {
    const fetchEmployeeDetails = async () => {
      if (!user?.id) return;
      
      try {
        
        // this is a proof of concept application.
        // for a full app, would fetch the employee's profile by their user ID

        const response = await fetch(`/api/employees/user/${user.id}`);
        const data = await response.json();
        setEmployee(data);
      } catch (err) {
        console.error('Failed to load employee details', err);
      }
    };

    fetchEmployeeDetails();
  }, [user?.id]);

  // fetch assigned clients
  useEffect(() => {
    const fetchClients = async () => {
      setLoading(true);
      
      try {
        if (employee?.employeeId) {
          const response = await apiService.employee.getClientsByEmployeeId(employee.employeeId);
          setClients(response.data);
        } else {
          // if no employee ID yet, fetch all clients (simplified approach)
          const response = await apiService.employee.getAllClients();
          setClients(response.data);
        }
      } catch (err) {
        setError('Failed to load client information');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    if (activeTab === 'clients') {
      fetchClients();
    }
  }, [activeTab, employee?.employeeId]);

  const handleAssignClient = async (clientId: string) => {
    if (!employee?.employeeId) return;
    
    try {
      await apiService.employee.assignClient(clientId, employee.employeeId);
      // refresh client list
      const response = await apiService.employee.getClientsByEmployeeId(employee.employeeId);
      setClients(response.data);
    } catch (err) {
      setError('Failed to assign client');
      console.error(err);
    }
  };

  const renderClients = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">My Clients</h2>
      
      {loading ? (
        <p>Loading clients...</p>
      ) : clients.length > 0 ? (
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white">
            <thead>
              <tr className="bg-gray-100">
                <th className="py-2 px-4 text-left">Client ID</th>
                <th className="py-2 px-4 text-left">Name</th>
                <th className="py-2 px-4 text-left">Email</th>
                <th className="py-2 px-4 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {clients.map((client) => (
                <tr key={client.id} className="border-t">
                  <td className="py-2 px-4">{client.clientId}</td>
                  <td className="py-2 px-4">{`${client.firstName} ${client.lastName}`}</td>
                  <td className="py-2 px-4">{client.email}</td>
                  <td className="py-2 px-4">
                    <button 
                      className="text-blue-500 hover:underline mr-2"
                      onClick={() => window.location.href = `/client/${client.id}`}
                    >
                      View
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <p>No clients assigned yet.</p>
      )}
    </div>
  );

  const renderProfile = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">My Profile</h2>
      
      {employee ? (
        <div className="bg-white p-6 rounded-lg shadow-md">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <h3 className="text-gray-500 font-medium">Employee ID</h3>
              <p className="text-lg">{employee.employeeId}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Name</h3>
              <p className="text-lg">{`${employee.firstName} ${employee.lastName}`}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Email</h3>
              <p className="text-lg">{employee.email}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Location</h3>
              <p className="text-lg">{employee.locationId}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Title</h3>
              <p className="text-lg">{employee.title}</p>
            </div>
            
            <div>
              <h3 className="text-gray-500 font-medium">Assigned Clients</h3>
              <p className="text-lg">{employee.assignedClientList?.length || 0}</p>
            </div>
          </div>
          
          <div className="mt-6">
            <button className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
              Edit Profile
            </button>
          </div>
        </div>
      ) : (
        <p>Loading profile information...</p>
      )}
    </div>
  );
  
  return (
    <div className="min-h-screen bg-gray-50">
      <div className="flex">
        {/* Sidebar Navigation */}
        <div className="w-64 bg-gray-800 min-h-screen p-4">
          <h1 className="text-white text-xl font-bold mb-6">Employee Portal</h1>
          
          <nav>
            <ul>
              <li className="mb-2">
                <button 
                  onClick={() => setActiveTab('clients')}
                  className={`w-full text-left px-4 py-2 rounded ${
                    activeTab === 'clients' ? 'bg-gray-700 text-white' : 'text-gray-300 hover:bg-gray-700'
                  }`}
                >
                  My Clients
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
              {activeTab === 'clients' && 'Client Management'}
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
          {activeTab === 'clients' && renderClients()}
          {activeTab === 'profile' && renderProfile()}
        </div>
      </div>
    </div>
  );
};