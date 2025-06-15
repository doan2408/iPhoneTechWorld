// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";

const clientAdminRoutes: RouteRecordRaw[] = [
        { path: "bill", component: () => import("@/views/Admin/HoaDon/HoaDonAdmin.vue") , meta: { title: 'Client Manage'} },
        // { path: "/add", component: () => import("@/views/Admin/TaiKhoan/Client/AddClient.vue"), meta: { title: 'Add Client'} },
        { path: "ban-hang", component: () => import("@/views/Admin/HoaDon/BanHangAdmin.vue"), meta: { title: 'Client Manage' } },
];

export default clientAdminRoutes;
