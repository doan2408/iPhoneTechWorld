import { createRouter, createWebHistory } from "vue-router";
import adminRoutes from "./modules/admin/admin";
import clientRoutes from "./modules/client/client";
import staffRoutes from "./modules/staff/staff";
import clientLayout from "@/layouts/ClientLayout.vue";
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
    path: '/',
    component: clientLayout,
    children: [
      { path: '', component: () => import('@/views/Client/Home.vue') },
      { path: 'home', component: () => import('@/views/Client/Home.vue') },
      { path: 'login', component: () => import('@/views/Login/LoginView.vue') },
    ]
  },
//   Khi đang ở /, hiển thị HeaderClient + Home.vue.

// Khi vào /admin, hiển thị HeaderAdmin + Dashboard.vue.

// Không bao giờ hiển thị trùng 2 cái header cả.

    ...adminRoutes,
    ...staffRoutes,
    ...clientRoutes,
  ],
});

router.beforeEach(async (to, from, next) => {
  const store = (await import("@/Service/LoginService/Store")).default;
  if (to.meta.requiresAuth) {
    await store.dispatch("fetchCurrentUser");
    if (!store.state.user) {
      return next("/login");
    }
    if (to.meta.requiresAdmin && !store.getters.isAdmin) {
      return next("/product");
    }
  }
  next();
});

export default router;
