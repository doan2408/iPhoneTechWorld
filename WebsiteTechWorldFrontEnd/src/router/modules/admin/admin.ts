// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      //các children này sẽ là các tùy chọn trên thanh header
      { path: "products", component: () => import("@/views/Admin/ProductAdmin.vue") },
      { path: "products/:id", component: () => import("@/views/Admin/PoductAdminDetail.vue") },
    ],
  },
];

export default adminRoutes;
