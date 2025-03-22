// src/utils/storage.utils.ts
const CURRENT_VERSION = 1;

interface StorageItem<T> {
  value: T;
  expiry?: number;
  version?: number;
}

export const storageUtils = {
  /**
   * Sets an item in storage with versioning and optional TTL.
   * @param key - Key under which the item is stored
   * @param value - Value to store
   * @param ttlMinutes - Time-to-live in minutes (optional)
   */
  set<T>(key: string, value: T, ttlMinutes?: number): void {
    try {
      const item: StorageItem<T> = {
        value,
        version: CURRENT_VERSION,
        expiry: ttlMinutes
          ? new Date().getTime() + ttlMinutes * 60 * 1000
          : undefined,
      };

      localStorage.setItem(key, JSON.stringify(item));
    } catch (error) {
      console.error(`Error storing ${key}:`, error);
    }
  },

  /**
   * Retrieves an item from storage, checking version and expiry.
   * @param key - Key of the item to retrieve
   * @param defaultValue - Default value if the item doesn't exist or is invalid
   * @returns The stored value or the default value
   */
  get<T>(key: string, defaultValue: T): T {
    try {
      const raw = localStorage.getItem(key);
      if (!raw) return defaultValue;

      const item: StorageItem<T> = JSON.parse(raw);

      if (item.version !== CURRENT_VERSION || (item.expiry && item.expiry < new Date().getTime())) {
        this.remove(key);
        return defaultValue;
      }

      return item.value;
    } catch {
      return defaultValue;
    }
  },

  /**
   * Removes an item from storage.
   * @param key - Key of the item to remove
   */
  remove(key: string): void {
    localStorage.removeItem(key);
  },

  /**
   * Clears all storage except specified keys.
   * @param preserveKeys - Array of keys to preserve
   */
  clear(preserveKeys: string[] = []): void {
    const preserved = preserveKeys.reduce<Record<string, string>>((acc, key) => {
      const value = localStorage.getItem(key);
      if (value) acc[key] = value;
      return acc;
    }, {});

    localStorage.clear();

    Object.entries(preserved).forEach(([key, value]) => {
      localStorage.setItem(key, value);
    });
  },
};

