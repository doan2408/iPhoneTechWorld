// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";

const hoaDonAdminRoutes: RouteRecordRaw[] = [
        { path: "bill", component: () => import("@/views/Admin/HoaDon/HoaDonAdmin.vue") , meta: { title: 'Client Manage'} },
        // { path: "/add", component: () => import("@/views/Admin/TaiKhoan/Client/AddClient.vue"), meta: { title: 'Add Client'} },
        { path: "ban-hang", component: () => import("@/views/Admin/HoaDon/BanHangAdmin.vue"), meta: { title: 'Client Manage' } },
        { path: "cap-nhat/:id", name: "CapNhatHoaDon", component: () => import("@/views/Admin/HoaDon/CapNhatHoaDon.vue"), meta: { title: 'Cập nhật hóa đơn' }}
];

export default hoaDonAdminRoutes;
