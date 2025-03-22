//src/components/login/login.tsx
import React from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useLogin } from "../../../hooks/useLogin";
import { Link } from "react-router-dom"; // Import Link from react-router-dom
import logo from "../../../assets/dd-asset-management-high-resolution-logo-transparent.svg";

const loginSchema = z.object({
  email: z.string().email("Invalid email address").nonempty("Email is required"),
  password: z.string().min(8, "Password must be at least 8 characters"),
});

type LoginFormInputs = z.infer<typeof loginSchema>;

const Login: React.FC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormInputs>({
    resolver: zodResolver(loginSchema),
  });

  const { handleLogin } = useLogin();

  const onSubmit = async (data: LoginFormInputs) => {
    await handleLogin(data.email, data.password);
  };

  return (
    <section className="bg-gray-50 dark:bg-gray-900">
      <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-0">
        <a
          href="#"
          className="flex flex-col items-center justify-center mb-6 text-2xl font-semibold text-gray-900 dark:text-white w-full"
        >
          <img
            className="min-w-[1100px] max-w-[1500px] w-3/4 sm:w-2/3 md:w-1/2 lg:w-1/3 xl:w-1/4 h-auto mb-0"
            src={logo}
            alt="logo"
          />
        </a>

        <div className="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-800 dark:border-gray-700">
          <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
            <h1 className="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
              Sign in to your account
            </h1>
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4 md:space-y-6">
              <div>
                <label
                  htmlFor="email"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Your email
                </label>
                <input
                  id="email"
                  type="email"
                  {...register("email")}
                  className={`bg-gray-50 border ${
                    errors.email ? "border-red-500" : "border-gray-300"
                  } text-gray-900 rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white`}
                  placeholder="name@company.com"
                />
                {errors.email && (
                  <span className="text-sm text-red-500">{errors.email.message}</span>
                )}
              </div>
              <div>
                <label
                  htmlFor="password"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Password
                </label>
                <input
                  id="password"
                  type="password"
                  {...register("password")}
                  className={`bg-gray-50 border ${
                    errors.password ? "border-red-500" : "border-gray-300"
                  } text-gray-900 rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white`}
                  placeholder="••••••••"
                />
                {errors.password && (
                  <span className="text-sm text-red-500">{errors.password.message}</span>
                )}
              </div>
              <button
                type="submit"
                disabled={isSubmitting}
                className="w-full text-white bg-primary-600 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
              >
                {isSubmitting ? "Logging in..." : "Sign in"}
              </button>
            </form>
            <div className="text-center">
              <Link
                to="/register"
                className="text-primary-600 hover:underline dark:text-primary-500"
              >
                Need an account? Register here.
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Login;

