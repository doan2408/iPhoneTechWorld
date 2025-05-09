// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import clientLayout from "@/layouts/ClientLayout.vue";

const clientRoutes: RouteRecordRaw[] = [
  {
    path: "/client/home",
    component: clientLayout,
    children: [
      { path: "", component: () => import("@/views/Client/Home.vue") },
    ],
  },
];

export default clientRoutes;
