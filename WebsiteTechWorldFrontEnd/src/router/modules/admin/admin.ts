// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";
import clientAdminRoutes from "./ClientRoutes/clientRoutes";
import staffAdminRoutes from "./StaffRoutes/staffRoutes";
import hoaDonRoutes from "./HoaDonRoutes/hoaDonRoutes";
import giaoHangRoutes from "./GiaoHangRoutes/giaoHangRoutes";
import sanPhamAdminRouters from "./SanPhamRotes/sanPhamRouters";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      { path: "promotions", component: () => import("@/views/Admin/PhieuGiamGia/PhieuGiamGiaAdmin.vue"), meta: { title: 'Voucher Manage'}  },
      { path: "statistical", component: () => import("@/views/Admin/ThongKe/ThongKeAdmin.vue") },
      ...clientAdminRoutes,
      ...staffAdminRoutes,
      ...hoaDonRoutes,
      ...giaoHangRoutes
      ...sanPhamAdminRouters
    ],
  },
];

export default adminRoutes;
