import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueDevTools from "vite-plugin-vue-devtools";

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(), vueDevTools()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },  
  server: {
    host: "0.0.0.0", // Cho phép truy cập từ IP khác (vd: điện thoại trong LAN)
    port: 5173,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
    allowedHosts: [
      'localhost',
      '329c8f9a0365.ngrok-free.app' // Thay thế bằng địa chỉ ngrok của bạn
    ],
    // hmr: {
    //   protocol: 'ws',
    //   host: '172.17.192.1', // hoặc IP thật nếu truy cập từ máy khác
    //   port: 5173,
    //   clientPort: 5173
    // },
  },
  define: {
    global: "window",   // Fix cho sockjs-client
  }
});
