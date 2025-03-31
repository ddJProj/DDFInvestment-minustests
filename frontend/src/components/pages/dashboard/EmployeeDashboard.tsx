// src/components/pages/dashboard/EmployeeDashboard.tsx
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
  assignedEmployeeId?: string;
  assignedEmployeeName?: string;
}

interface Employee {
  id: number;
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  locationId: string;
  title: string;
  userAccountId: number;
  assignedClientList: string[];
}

const EmployeeDashboard: React.FC = () => {
  const { logout } = useAuth();
  const [activeTab, setActiveTab] = useState('clients');
  const [clients, setClients] = useState<Client[]>([]);
  const [employee, setEmployee] = useState<Employee | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [notification, setNotification] = useState<{
    type: 'success' | 'error' | 'info',
    message: string
  } | null>(null);
  const user = authUtils.getUser();

  // fetch employee details
  useEffect(() => {
    const fetchEmployeeDetails = async () => {
      if (!user?.id) return;
      
      try {
        setLoading(true);
        // get employee by associated user account ID
        // TODO: adjust the API endpoint late if need be
        const response = await apiService.employee.getEmployeeByUserId(user.id);
        setEmployee(response.data);
      } catch (err) {
        console.error('Failed to load employee details', err);


        // fallback - try to find the employee with getAll
        if (!employee){
          try {
            const allEmployeesResponse = await apiService.employee.getAllEmployees();
            const employeeData = allEmployeesResponse.data.find(
              (emp: Employee) => emp.email === user.email
            );
            if (employeeData) {
              setEmployee(employeeData);
            }
          } catch (fallbackErr) {
            // dont set error state to maintain ui stability during dev
            console.error('Fallback employee lookup failed:', fallbackErr);

          } finally {
            setLoading(false);
          }
        }
      }
    };

    fetchEmployeeDetails();
  }, [user?.id, user?.email]);

  // fetch assigned clients
  useEffect(() => {
    const fetchClients = async () => {
      try{
      setLoading(true);
      let clientList = [];


        try {
          if (employee?.employeeId) {
            const response = await apiService.employee.getClientsByEmployeeId(employee.employeeId);
            clientList = response.data;
          } else {
            // if no employee ID yet, fetch all clients (simplified)
            const response = await apiService.employee.getAllClients();
            clientList = response.data;
          }
        } catch (err) {
          console.error("Failed to load the client list data: ", err);
          // set list as empty when err occurs
          clientList = [];
        }
        setClients(clientList);
        setLoading(false);

      } catch (err){
          console.error("An error occurred while fetching client: ", err);
          setClients([]);
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
      
      // show success 
      setNotification({
        type: 'success',
        message: 'Client assigned successfully'
      });
      
      // refresh client list
      const response = await apiService.employee.getClientsByEmployeeId(employee.employeeId);
      setClients(response.data);
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    } catch (err) {
      setError('Failed to assign client');
      console.error(err);
      
      // show error notification
      setNotification({
        type: 'error',
        message: 'Failed to assign client. Please try again.'
      });
      
      // clear notification after 3 seconds
      setTimeout(() => setNotification(null), 3000);
    }
  };

  // handle profile update
  const handleUpdateProfile = async () => {
    if (!employee?.id) return;
    
    try {
      // implement profile update logic
      // TODO: placeholder for actual profile update functionality
      // using:
      // await apiService.employee.updateProfile(employee.id, updatedData);
      
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

  // handle user logout with proper API call
  const handleLogout = async () => {
    try {
      await logout();
      // redirect happens in logout function
    } catch (err) {
      console.error('Logout failed:', err);
      setNotification({
        type: 'error',
        message: 'Logout failed. Please try again.'
      });
    }
  };

  const renderClients = () => (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">My Clients</h2>
      
      {loading ? (
        <div className="flex justify-center">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        </div>
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
                    {!client.assignedEmployeeId && (
                      <button 
                        className="text-green-500 hover:underline"
                        onClick={() => handleAssignClient(client.clientId)}
                      >
                        Assign to Me
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <p className="text-lg mb-2">No clients assigned yet.</p>
          <p className="text-gray-600">When clients are added to the system, they will appear here.</p>
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
      ) : employee ? (
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
            <button 
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
              onClick={handleUpdateProfile}
            >
              Edit Profile
            </button>
          </div>
        </div>
      ) : (
        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <p className="text-lg mb-2">No employee profile found.</p>
          <p className="text-gray-600">Please contact an administrator to set up your employee profile.</p>
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

export default EmployeeDashboard;