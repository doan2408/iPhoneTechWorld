import type { RouteRecordRaw } from "vue-router";

const sanPhamAdminRouters: RouteRecordRaw[] = [
      { path: "products",name: "products", component: () => import("@/views/Admin/SanPham/ProductAdmin.vue") },
      { path: "products/create", component: () => import("@/views/Admin/SanPham/testCreateSanPham.vue") },
      { path: "products/create/modelSanPham", component: () => import("@/views/Admin/SanPham/test.vue") },
      { path: "xuatXu", component: () => import("@/views/Admin/SanPham/XuatXuAdmin.vue") },
      { path: "pin", component: () => import("@/views/Admin/SanPham/PinAdmin.vue") },
      { path: "rom", component: () => import("@/views/Admin/SanPham/RomAdmin.vue") },
      { path: "ram", component: () => import("@/views/Admin/SanPham/RamAdmin.vue") },
      { path: "mauSac", component: () => import("@/views/Admin/SanPham/MauSacAdmin.vue") },
      { path: "manHinh", component: () => import("@/views/Admin/SanPham/ManHinhAdmin.vue") },
      { path: "nhaCungCap", component: () => import("@/views/Admin/SanPham/NhaCungCapAdmin.vue") },
      { path: "loai", component: () => import("@/views/Admin/SanPham/LoaiAdmin.vue") },
      { path: "imei", component: () => import("@/views/Admin/SanPham/ImeiAdmin.vue") },
      { path: "heDieuHanh", component: () => import("@/views/Admin/SanPham/HeDieuHanhAdmin.vue") },
      { path: "cpu", component: () => import("@/views/Admin/SanPham/CpuAdmin.vue") },
      { path: "cameraTruoc", component: () => import("@/views/Admin/SanPham/CameraTruocAdmin.vue") },
      { path: "cameraSau", component: () => import("@/views/Admin/SanPham/CameraSauAdmin.vue") },
      { path: "products", component: () => import("@/views/Admin/SanPham/ProductAdmin.vue"), meta: { title: 'Products Manage'} },
      { path: "products/:id", component: () => import("@/views/Admin/SanPham/PoductAdminDetail.vue"), meta: { title: 'Product Detail'} },
      { path: "products/view/:id", component: () => import("@/views/Admin/SanPham/ProductAdminView.vue"), meta: { title: 'Product View'} },
];

export default sanPhamAdminRouters;



    