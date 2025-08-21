import type { RouteRecordRaw } from "vue-router";

const baoHanhRouters: RouteRecordRaw[] = [
       { path: "warranty", component: () => import("@/views/Admin/BaoHanh/BaoHanhAdmin.vue"), meta: { title: 'Bảo hành'}  },
       { path: "warranty-type",  component: () => import("@/views/Admin/BaoHanh/LoaiBaoHanhAdmin.vue"), meta: { title: 'Loại bảo hành'}  },
];

export default baoHanhRouters;