// src/utils/session.utils.ts
import { UserAccount } from "../types/auth.types";

export const fetchSession = async (token: string): Promise<UserAccount> => {
  try {
    const response = await fetch("http://127.0.0.1:8080/session", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Failed to fetch session data");
    }

    const data = await response.json();
    return {
      id: data.id,
      username: data.username,
      role: data.role,
      permissions: data.permissions,
    };
  } catch (error) {
    console.error("Error fetching session:", error);
    throw error;
  }
};

export const getSession = (): { token: string | null } => {
  const token = localStorage.getItem("authToken");
  return { token };
};

