import type { RouteRecordRaw } from "vue-router";
import staffLayout from "@/layouts/StaffLayout.vue";

const staffRoutes: RouteRecordRaw[] = [
  {
    path: "/staff",
    component: staffLayout,
    children: [
      { path: "products", component: () => import("@/views/Staff/ProductStaff.vue") },
    ],
  },
];

export default staffRoutes;
