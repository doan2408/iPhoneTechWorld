import type { RouteRecordRaw } from "vue-router";

const xuLySauBanHangAdminRoutes: RouteRecordRaw[] = [
        { path: "handle", component: () => import("@/views/Admin/XuLySauBanHang/XuLySauBanHang.vue") , meta: { title: 'Client Manage'} },
        { path: "handle-detail", component: () => import("@/views/Admin/XuLySauBanHang/XuLyChiTietSauBanHang.vue"), meta: { title: 'Client Manage' } },
];

export default xuLySauBanHangAdminRoutes;
