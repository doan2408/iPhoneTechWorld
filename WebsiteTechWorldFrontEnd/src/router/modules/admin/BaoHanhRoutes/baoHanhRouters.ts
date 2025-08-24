import type { RouteRecordRaw } from "vue-router";

const baoHanhRouters: RouteRecordRaw[] = [
       { path: "warranty", component: () => import("@/views/Admin/BaoHanh/BaoHanhAdmin.vue"), meta: { title: 'Bảo hành'}  },
       { path: "warranty-type",  component: () => import("@/views/Admin/BaoHanh/LoaiBaoHanhAdmin.vue"), meta: { title: 'Loại bảo hành'}  },
       { path: "warranty-request", component: () => import("@/views/Admin/BaoHanh/YeuCauBaoHanhAdmin.vue"), meta: { title: 'Yêu cầu bảo hành' } },
];

export default baoHanhRouters;