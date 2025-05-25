// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      { path: "products", component: () => import("@/views/Admin/SanPham/ProductAdmin.vue") },
      { path: "products/:id", component: () => import("@/views/Admin/SanPham/PoductAdminDetail.vue") },
      { path: "products/create", component: () => import("@/views/Admin/SanPham/ProductAdminCreate.vue") },
      { path: "xuatXu", component: () => import("@/views/Admin/SanPham/XuatXuAadmin.vue") },
      { path: "staff", component: () => import("@/views/Admin/TaiKhoan/StaffAdmin.vue") },
    ],
  },
];

export default adminRoutes;
