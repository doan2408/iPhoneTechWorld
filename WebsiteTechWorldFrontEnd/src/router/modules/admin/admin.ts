// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import adminLayout from "@/layouts/AdminLayout.vue";
import clientAdminRoutes from "./ClientRoutes/clientRoutes";
import staffAdminRoutes from "./StaffRoutes/staffRoutes";
import hoaDonRoutes from "./HoaDonRoutes/hoaDonRoutes";
import giaoHangRoutes from "./GiaoHangRoutes/giaoHangRoutes";

const adminRoutes: RouteRecordRaw[] = [
  {
    path: "/admin",
    component: adminLayout,
    children: [
      { path: "products/create", component: () => import("@/views/Admin/SanPham/ProductAdminCreate.vue") },
      { path: "xuatXu", component: () => import("@/views/Admin/SanPham/XuatXuAdmin.vue") },
      { path: "pin", component: () => import("@/views/Admin/SanPham/PinAdmin.vue") },
      { path: "rom", component: () => import("@/views/Admin/SanPham/RomAdmin.vue") },
      { path: "ram", component: () => import("@/views/Admin/SanPham/RamAdmin.vue") },
      { path: "mauSac", component: () => import("@/views/Admin/SanPham/MauSacAdmin.vue") },
      { path: "manHinh", component: () => import("@/views/Admin/SanPham/ManHinhAdmin.vue") },
      { path: "nhaCungCap", component: () => import("@/views/Admin/SanPham/NhaCungCapAdmin.vue") },
      { path: "loai", component: () => import("@/views/Admin/SanPham/LoaiAdmin.vue") },
      { path: "imei", component: () => import("@/views/Admin/SanPham/IemiAdmin.vue") },
      { path: "heDieuHanh", component: () => import("@/views/Admin/SanPham/HeDieuHanhAdmin.vue") },
      { path: "cpu", component: () => import("@/views/Admin/SanPham/CpuAdmin.vue") },
      { path: "cameraTruoc", component: () => import("@/views/Admin/SanPham/CameraTruocAdmin.vue") },
      { path: "cameraSau", component: () => import("@/views/Admin/SanPham/CameraSauAdmin.vue") },
      { path: "products", component: () => import("@/views/Admin/SanPham/ProductAdmin.vue"), meta: { title: 'Products Manage'} },
      { path: "products/:id", component: () => import("@/views/Admin/SanPham/PoductAdminDetail.vue"), meta: { title: 'Product Detail'} },
      { path: "promotions", component: () => import("@/views/Admin/PhieuGiamGia/PhieuGiamGiaAdmin.vue"), meta: { title: 'Voucher Manage'}  },
      { path: "statistical", component: () => import("@/views/Admin/ThongKe/ThongKeAdmin.vue") },
      ...clientAdminRoutes,
      ...staffAdminRoutes,
      ...hoaDonRoutes,
      ...giaoHangRoutes

    ],
  },
];

export default adminRoutes;
