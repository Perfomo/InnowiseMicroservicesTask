import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/realms": {
        target: "http://172.17.0.1:8080",
        changeOrigin: true,
      },
      "/users": {
        target: "http://172.17.0.1:8081",
        changeOrigin: true,
      },
      "/orders": {
        target: "http://172.17.0.1:8081",
        changeOrigin: true,
      },
      "/products": {
        target: "http://172.17.0.1:8081",
        changeOrigin: true,
      },
    }
  }
})
