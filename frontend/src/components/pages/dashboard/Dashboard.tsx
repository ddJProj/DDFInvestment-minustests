// src/components/pages/dashboard/Dashboard.tsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { usePermissions } from "../../../hooks/usePermissions";
import { UserRole } from "../../../types/auth.types";
import { ROUTES } from "../../../constants/router.constants";
import { authUtils } from "../../../utils/auth.utils";
import { apiService } from "../../../services/api.service";
import { useAuth } from "../../../hooks/useAuth";

interface UpgradeRequest {
  id: number;
  userAccountId: number;
  requestDate: string;
  status: string;
  details: string;
}

const Dashboard: React.FC = () => {
  const { hasRole, hasPermission, isLoading, error } = usePermissions();
  const navigate = useNavigate();
  const { logout } = useAuth();
  const user = authUtils.getUser();
  
  // State for guest functionality
  const [upgradeRequests, setUpgradeRequests] = useState<UpgradeRequest[]>([]);
  const [requestDetails, setRequestDetails] = useState("");
  const [showRequestForm, setShowRequestForm] = useState(false);
  const [requestLoading, setRequestLoading] = useState(false);
  const [requestError, setRequestError] = useState<string | null>(null);
  const [requestSuccess, setRequestSuccess] = useState<string | null>(null);
  const [dashboardLoading, setDashboardLoading] = useState(true);

  // Redirect based on role
  useEffect(() => {
    if (!isLoading && !error) {
      if (hasRole(UserRole.Admin)) {
        navigate(ROUTES.ADMIN);
        return;
      } else if (hasRole(UserRole.Employee)) {
        navigate(ROUTES.EMPLOYEE);
        return;
      } else if (hasRole(UserRole.Client)) {
        navigate(ROUTES.CLIENT);
        return;
      } else if (hasRole(UserRole.Guest)) {
        // For guests, we stay on this page and load their upgrade requests
        loadGuestUpgradeRequests();
      }
      setDashboardLoading(false);
    }
  }, [hasRole, isLoading, error, navigate]);

  // Load any existing upgrade requests for the guest
  const loadGuestUpgradeRequests = async () => {
    if (!user?.id) return;
    
    try {
      // This endpoint would need to be added to the backend
      const response = await apiService.auth.getUserUpgradeRequests(user.id);
      setUpgradeRequests(response.data);
    } catch (err) {
      console.error("Failed to load upgrade requests:", err);
      // Initialize with empty array if the endpoint isn't available yet
      setUpgradeRequests([]);
    } finally {
      setDashboardLoading(false);
    }
  };

  // Handle submitting an upgrade request
  const handleSubmitUpgradeRequest = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!user?.id) return;
    
    setRequestLoading(true);
    setRequestError(null);
    setRequestSuccess(null);
    
    try {
      await apiService.auth.requestUpgrade(user.id, requestDetails);
      setRequestSuccess("Your request has been submitted successfully! Our team will review it soon.");
      setShowRequestForm(false);
      setRequestDetails("");
      
      // Reload the requests to show the new one
      loadGuestUpgradeRequests();
    } catch (err) {
      console.error("Failed to submit upgrade request:", err);
      setRequestError("Failed to submit your request. Please try again later.");
    } finally {
      setRequestLoading(false);
    }
  };

  // Handle user logout
  const handleLogout = async () => {
    try {
      await logout();
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  // Guest dashboard content
  const renderGuestDashboard = () => (
    <div className="p-6 max-w-4xl mx-auto">
      <div className="bg-white p-6 rounded-lg shadow-md mb-6">
        <h2 className="text-2xl font-bold mb-4">Welcome to DD Financial Investment</h2>
        <p className="text-gray-700 mb-4">
          Thank you for creating an account with us. As a guest user, you can explore our platform and request an upgrade to a client account to access our investment services.
        </p>
        
        {/* Status of previous upgrade requests */}
        {upgradeRequests.length > 0 && (
          <div className="mt-6">
            <h3 className="text-xl font-semibold mb-3">Your Account Upgrade Requests</h3>
            <div className="overflow-x-auto">
              <table className="min-w-full bg-white">
                <thead>
                  <tr className="bg-gray-100">
                    <th className="py-2 px-4 text-left">Request Date</th>
                    <th className="py-2 px-4 text-left">Status</th>
                    <th className="py-2 px-4 text-left">Details</th>
                  </tr>
                </thead>
                <tbody>
                  {upgradeRequests.map((request) => (
                    <tr key={request.id} className="border-t">
                      <td className="py-2 px-4">{new Date(request.requestDate).toLocaleDateString()}</td>
                      <td className="py-2 px-4">
                        <span className={`px-2 py-1 rounded text-xs ${
                          request.status === 'APPROVED' ? 'bg-green-100 text-green-800' :
                          request.status === 'REJECTED' ? 'bg-red-100 text-red-800' :
                          'bg-yellow-100 text-yellow-800'
                        }`}>
                          {request.status}
                        </span>
                      </td>
                      <td className="py-2 px-4">{request.details}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
        
        {/* Request upgrade form toggle button */}
        {!showRequestForm && hasPermission('REQUEST_CLIENT_ACCOUNT') && 
         !upgradeRequests.some(req => req.status === 'PENDING') && (
          <button
            onClick={() => setShowRequestForm(true)}
            className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Request Client Account Upgrade
          </button>
        )}
        
        {/* Only show the pending message if there's a pending request */}
        {upgradeRequests.some(req => req.status === 'PENDING') && (
          <div className="mt-4 p-4 bg-yellow-50 border-l-4 border-yellow-400 text-yellow-700">
            <p className="font-medium">You have a pending upgrade request</p>
            <p>Our team is reviewing your request and will process it soon.</p>
          </div>
        )}
        
        {/* Request form */}
        {showRequestForm && (
          <div className="mt-6 p-4 border border-gray-200 rounded-lg">
            <h3 className="text-xl font-semibold mb-3">Request Client Account</h3>
            <form onSubmit={handleSubmitUpgradeRequest}>
              <div className="mb-4">
                <label className="block text-gray-700 mb-2" htmlFor="details">
                  Please tell us why you'd like to become a client:
                </label>
                <textarea
                  id="details"
                  rows={4}
                  className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                  value={requestDetails}
                  onChange={(e) => setRequestDetails(e.target.value)}
                  required
                ></textarea>
              </div>
              
              <div className="flex justify-end">
                <button
                  type="button"
                  onClick={() => setShowRequestForm(false)}
                  className="mr-2 px-4 py-2 text-gray-600 border border-gray-300 rounded hover:bg-gray-100"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={requestLoading}
                  className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:opacity-50"
                >
                  {requestLoading ? "Submitting..." : "Submit Request"}
                </button>
              </div>
            </form>
            
            {requestError && (
              <div className="mt-4 p-3 bg-red-100 text-red-700 rounded">
                {requestError}
              </div>
            )}
          </div>
        )}
        
        {/* Success message */}
        {requestSuccess && (
          <div className="mt-4 p-4 bg-green-50 border-l-4 border-green-400 text-green-700">
            {requestSuccess}
          </div>
        )}
      </div>
      
      {/* Resources section */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h3 className="text-xl font-semibold mb-3">Resources</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="p-4 border border-gray-200 rounded-lg">
            <h4 className="font-medium mb-2">Investment Education</h4>
            <p className="text-gray-600 mb-2">
              Learn about different investment options and strategies.
            </p>
            <a href="#" className="text-blue-500 hover:underline">
              View Resources →
            </a>
          </div>
          
          <div className="p-4 border border-gray-200 rounded-lg">
            <h4 className="font-medium mb-2">Financial Planning Tools</h4>
            <p className="text-gray-600 mb-2">
              Access calculators and planning tools to help with your financial goals.
            </p>
            <a href="#" className="text-blue-500 hover:underline">
              Explore Tools →
            </a>
          </div>
        </div>
      </div>
      
      {/* Logout button */}
      <div className="mt-6 text-center">
        <button
          onClick={handleLogout}
          className="px-4 py-2 text-gray-600 border border-gray-300 rounded hover:bg-gray-100"
        >
          Log Out
        </button>
      </div>
    </div>
  );

  if (dashboardLoading || isLoading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-6 max-w-4xl mx-auto">
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
          <p className="font-bold">Error</p>
          <p>{error}</p>
        </div>
      </div>
    );
  }

  // will briefly show before redirection happens
  if (hasRole(UserRole.Admin)) {
    return <p>Welcome Admin! Redirecting to admin dashboard...</p>;
  }

  if (hasRole(UserRole.Employee)) {
    return <p>Welcome Employee! Redirecting to employee dashboard...</p>;
  }

  if (hasRole(UserRole.Client)) {
    return <p>Welcome Client! Redirecting to client dashboard...</p>;
  }

  // If we get here, user is likely a Guest
  if (hasRole(UserRole.Guest)) {
    return renderGuestDashboard();
  }

  // Fallback for any unexpected state
  return (
    <div className="p-6 max-w-4xl mx-auto">
      <div className="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded">
        <p className="font-bold">Notice</p>
        <p>Your account type could not be determined. Please contact support if this issue persists.</p>
      </div>
    </div>
  );
};

export default Dashboard;