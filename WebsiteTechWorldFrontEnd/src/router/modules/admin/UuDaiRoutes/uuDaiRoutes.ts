import type { RouteRecordRaw } from "vue-router";

const uuDaiAdminRoutes: RouteRecordRaw[] = [
       { path: "voucher",  component: () => import("@/views/Admin/PhieuGiamGia/PhieuGiamGiaAdmin.vue"), meta: { title: 'Voucher Manage'}  },
       { path: "promotions",  component: () => import("@/views/Admin/KhuyenMai/KhuyenMaiSanPhamAdmin.vue"), meta: { title: 'Promotions Manage'}  },
];

export default uuDaiAdminRoutes;