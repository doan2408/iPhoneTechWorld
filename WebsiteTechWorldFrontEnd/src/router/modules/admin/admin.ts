// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";
import clientAdminRoutes from "./ClientRoutes/clientRoutes";
import staffAdminRoutes from "./StaffRoutes/staffRoutes";
import sanPhamAdminRouters from "./SanPhamRotes/sanPhamRouters";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      { path: "promotions", component: () => import("@/views/Admin/PhieuGiamGia/PhieuGiamGiaAdmin.vue"), meta: { title: 'Voucher Manage'}  },
      { path: "statistical", component: () => import("@/views/Admin/ThongKe/ThongKeAdmin.vue") },
      { path: "bill", component: () => import("@/views/Admin/HoaDon/HoaDonAdmin.vue") },
      { path: "shipping", component: () => import("@/views/Admin/GiaoHang/GiaoHangAdmin.vue") },
      ...clientAdminRoutes,
      ...staffAdminRoutes,
      ...sanPhamAdminRouters
    ],
  },
];

export default adminRoutes;
