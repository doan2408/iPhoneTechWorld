// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";
import clientAdminRoutes from "./ClientRoutes/clientRoutes";
import staffAdminRoutes from "./StaffRoutes/staffRoutes";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      { path: "products", component: () => import("@/views/Admin/SanPham/ProductAdmin.vue"), meta: { title: 'Products Manage'} },
      { path: "products/:id", component: () => import("@/views/Admin/SanPham/PoductAdminDetail.vue"), meta: { title: 'Product Detail'} },
      
      { path: "promotions", component: () => import("@/views/Admin/PhieuGiamGia/PhieuGiamGiaAdmin.vue"), meta: { title: 'Voucher Manage'}  },
      ...clientAdminRoutes,
      ...staffAdminRoutes
    ],
  },
];

export default adminRoutes;
