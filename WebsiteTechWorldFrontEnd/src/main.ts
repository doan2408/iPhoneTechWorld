// src/main.js
import './assets/main.css';

import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';
import store from '@/Service/LoginService/Store'; // Import store
import ElementPlus from 'element-plus'; //cấu hình  element
import 'element-plus/dist/index.css'; //cấu hình  element
import 'bootstrap'
import '@fortawesome/fontawesome-free/css/all.css'
import '@fortawesome/fontawesome-free/js/all.js'
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'

// Cấu hình Axios trước khi khởi tạo ứng dụng
axios.defaults.baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080';
axios.defaults.withCredentials = true;

const app = createApp(App);

app.use(store); // Đăng ký store
app.use(router); // Đăng ký router
app.use(ElementPlus); //cấu hình  element
app.use(Toast);
app.mount('#app');