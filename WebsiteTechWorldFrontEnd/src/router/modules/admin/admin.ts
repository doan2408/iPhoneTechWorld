// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";
import clientAdminRoutes from "./ClientRoutes/clientRoutes";
import staffAdminRoutes from "./StaffRoutes/staffRoutes";
import hoaDonRoutes from "./HoaDonRoutes/hoaDonRoutes";
import giaoHangRoutes from "./GiaoHangRoutes/giaoHangRoutes";
import sanPhamAdminRouters from "./SanPhamRotes/sanPhamRouters";
import uuDaiAdminRoutes from "./UuDaiRoutes/uuDaiRoutes";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      { path: "statistical", component: () => import("@/views/Admin/ThongKe/ThongKeAdmin.vue") , meta: {Title : "Statisticial"}},
      { path: "bill", component: () => import("@/views/Admin/HoaDon/HoaDonAdmin.vue") },
      { path: "shipping", component: () => import("@/views/Admin/GiaoHang/GiaoHangAdmin.vue") },
      { path: "danhGiaSanPham", component: () => import("@/views/Admin/DanhGiaSanPham/DanhGiaSanPham.vue") },
      ...clientAdminRoutes,
      ...staffAdminRoutes,
      ...hoaDonRoutes,
      ...giaoHangRoutes,
      ...sanPhamAdminRouters,
      ...uuDaiAdminRoutes
    ],
  },
];

export default adminRoutes;
