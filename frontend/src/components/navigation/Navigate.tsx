//src/components/navigation/Navigate.tsx
import { useNavigate } from "react-router-dom";

// Export the component if you plan to use it elsewhere
export const Login: React.FC = () => {
  const navigate = useNavigate();
  
  const handleLogin = () => {
    // Redirect to dashboard after login
    navigate("/dashboard");
  };
  
  return <button onClick={handleLogin}>Login</button>;
};
