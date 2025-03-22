import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173, // default Vite port
  },
  preview: {
    port: 5173,
  },
  // Base configuration for proper routing
  base: '/',
});
