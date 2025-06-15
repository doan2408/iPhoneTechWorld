// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";

const clientAdminRoutes: RouteRecordRaw[] = [
    { path: "shipping", component: () => import("@/views/Admin/GiaoHang/GiaoHangAdmin.vue"), meta: { title: 'Giao Hang'} },
    { path: "shipping/detail/:id", name: "GiaoHangProcessing", component: () => import("@/views/Admin/GiaoHang/GiaoHangAdminProcessing.vue"), meta: { title: 'GiaoHang Detail'} },
];

export default clientAdminRoutes;
