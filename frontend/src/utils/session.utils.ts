// src/utils/session.utils.ts
import { UserAccount } from "../types/auth.types";
import { authUtils } from "./auth.utils";

export const fetchSession = async (token: string): Promise<UserAccount> => {
  try {
    const storedUser = authUtils.getUser();
    if(!storedUser) {
      throw new Error("No matching user data could be found.");
    }

    return {
      id: storedUser.id || 0 ,
      email: storedUser.email || '',
      firstName: storedUser.firstName || '',
      lastName: storedUser.lastName || '',
      role: storedUser.role || '',
      permissions: storedUser.permissions || []
    };

    // old logic from rust backend
    // const response = await fetch("http://127.0.0.1:8080/session", {
    //   headers: {
    //     Authorization: `Bearer ${token}`,
    //   },
    // });

    // if (!response.ok) {
    //   throw new Error("Failed to fetch session data");
    // }

    // const data = await response.json();
    // return {
    //   id: data.id,
    //   username: data.username,
    //   role: data.role,
    //   permissions: data.permissions,
    // };
  } catch (error) {
    console.error("Error fetching session:", error);
    throw error;
  }
};

export const getSession = (): { token: string | null } => {
  const token = localStorage.getItem("authToken");
  return { token };
};

