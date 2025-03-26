//src/components/navigation/Navigate.tsx
import { useNavigate } from "react-router-dom";
import { ROUTES } from "../../constants/router.constants";

// Export the component if you plan to use it elsewhere
export const Login: React.FC = () => {
  const navigate = useNavigate();
  
  const handleLogin = () => {
    // Redirect to dashboard after login
    navigate(ROUTES.DASHBOARD);
  };
  
  return <button onClick={handleLogin}>Login</button>;
};
