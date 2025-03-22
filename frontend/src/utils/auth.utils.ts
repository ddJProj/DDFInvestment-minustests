// src/utils/auth.utils.ts
import { jwtDecode } from "jwt-decode";

interface TokenPayload {
  exp?: number;
}

const AUTH_KEYS = {
  ACCESS_TOKEN: "authToken",
  USER: "authUser",
};

export const authUtils = {
  /**
   * Stores tokens and user data in storage.
   * @param token - Authentication token
   * @param user - User data
   */
  setAuthData(token: string, user: Record<string, any>): void {
    localStorage.setItem(AUTH_KEYS.ACCESS_TOKEN, token);
    localStorage.setItem(AUTH_KEYS.USER, JSON.stringify(user));
  },

  /**
   * Retrieves the stored token.
   * @returns The stored token or null
   */
  getToken(): string | null {
    return localStorage.getItem(AUTH_KEYS.ACCESS_TOKEN);
  },

  /**
   * Retrieves the stored user data.
   * @returns User data object or null
   */
  getUser(): Record<string, any> | null {
    const user = localStorage.getItem(AUTH_KEYS.USER);
    return user ? JSON.parse(user) : null;
  },

  /**
   * Clears all authentication data from storage.
   */
  clearAuthData(): void {
    localStorage.removeItem(AUTH_KEYS.ACCESS_TOKEN);
    localStorage.removeItem(AUTH_KEYS.USER);
  },

  /**
   * Checks if the stored token is valid and not expired.
   * @returns Whether the token is valid
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const { exp } = jwtDecode<TokenPayload>(token);
      return exp ? exp > Math.floor(Date.now() / 1000) : false;
    } catch {
      return false;
    }
  },

  /**
   * Refreshes the token if close to expiration.
   * @param thresholdMinutes - Minutes before expiration to refresh
   * @returns Whether a refresh is required
   */
  needsRefresh(thresholdMinutes = 5): boolean {
    const token = this.getToken();
    if (!token) return false;

    const { exp } = jwtDecode<TokenPayload>(token);
    const currentTime = Math.floor(Date.now() / 1000);
    return exp ? exp - currentTime < thresholdMinutes * 60 : false;
  },
};

