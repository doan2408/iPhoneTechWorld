// src/router/modules/auth.ts
import type { RouteRecordRaw } from 'vue-router'

const authRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/Authentical/SignInView.vue'),
  }
]

export default authRoutes
