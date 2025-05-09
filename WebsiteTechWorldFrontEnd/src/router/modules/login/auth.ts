// src/router/modules/auth.ts
import type { RouteRecordRaw } from 'vue-router'

const authRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/Login/LoginView.vue'),
  },
]

export default authRoutes
