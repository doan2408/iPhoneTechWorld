// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      { path: "products", component: () => import("@/views/Admin/ProductAdmin.vue") },
    ],
  },
];

export default adminRoutes;
