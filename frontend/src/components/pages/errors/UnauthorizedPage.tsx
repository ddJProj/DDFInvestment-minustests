// src/components/pages/errors/UnauthorizedPage.tsx
import React from 'react';
import { Link } from 'react-router-dom';

const UnauthorizedPage: React.FC = () => {
  return (
    <div className="flex h-screen items-center justify-center bg-gray-100">
      <div className="rounded-lg bg-white p-8 text-center shadow-md">
        <h1 className="mb-4 text-3xl font-bold text-red-600">Access Denied</h1>
        <p className="mb-6 text-gray-700">
          You don't have permission to access this page. Please contact your
          administrator if you believe this is an error.
        </p>
        <div className="flex justify-center space-x-4">
          <Link
            to="/dashboard"
            className="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
          >
            Go to Dashboard
          </Link>
          <Link
            to="/login"
            className="rounded bg-gray-500 px-4 py-2 text-white hover:bg-gray-600"
          >
            Log Out
          </Link>
        </div>
      </div>
    </div>
  );
};

export default UnauthorizedPage;
