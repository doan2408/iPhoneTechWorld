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
      { path: "staff", component: () => import("@/views/Admin/TaiKhoan/Staff/StaffAdmin.vue") },
      { path: "staff/:id", component: () => import("@/views/Admin/TaiKhoan/Staff/StaffDetailAdmin.vue") },
      { path: "client", component: () => import("@/views/Admin/TaiKhoan/Client/ClientAdmin.vue") },
      { path: "client/:id", component: () => import("@/views/Admin/TaiKhoan/Client/ClientAdmin.vue") },
      { path: "promotions", component: () => import("@/views/Admin/PhieuGiamGia/PhieuGiamGiaAdmin.vue") },
    ],
  },
];

export default adminRoutes;
