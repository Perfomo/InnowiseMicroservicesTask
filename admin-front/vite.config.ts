import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5000,
    proxy: {
      "/realms": {
        target: "http://172.17.0.1:8080",
        changeOrigin: true,
      },
      "/api/users": {
        target: "http://172.17.0.1:8081",
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api\/users/, '\/users')
      },
      "/api/orders": {
        target: "http://172.17.0.1:8081",
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api\/orders/, '\/orders')
      },
      "/api/products": {
        target: "http://172.17.0.1:8081",
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api\/products/, '\/products')
      },
      "/api/inventory": {
        target: "http://172.17.0.1:8081",
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api\/inventory/, '\/inventory')
      },
    }
  }
})
