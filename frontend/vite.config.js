import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/chat': 'http://localhost:8080',
      '/stream': 'http://localhost:8080',
      '/dogs': 'http://localhost:8080',
      '/recommendations': 'http://localhost:8080',
      '/actuator': 'http://localhost:8080',
    },
  },
})
