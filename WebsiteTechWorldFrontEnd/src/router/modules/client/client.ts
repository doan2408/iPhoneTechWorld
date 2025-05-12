// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";
import clientLayout from "@/layouts/ClientLayout.vue";

const clientRoutes: RouteRecordRaw[] = [
  {
    path: "/client",
    component: clientLayout,
    children: [
      //các children này sẽ là các tùy chọn trên thanh header
      { path: "home", component: () => import("@/views/Client/Home.vue") },
      { path: "category", component: () => import("@/views/Client/Category.vue") },
    ],
  },
];

export default clientRoutes;
