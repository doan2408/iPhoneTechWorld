import type { RouteRecordRaw } from "vue-router";
import clientLayout from "@/layouts/ClientLayout.vue";

const clientRoutes: RouteRecordRaw[] = [
  {
    path: "/client",
    component: clientLayout,
    children: [
      //các children này sẽ là các tùy chọn trên thanh header
      { path: "home", component: () => import("@/views/Client/CustomerHome.vue"), meta: { title: "Trang chủ"} },
      { path: "detail/:id", component: () => import("@/views/Client/DetailProduct.vue"), meta: { title: "Chi tiết sản phẩm"} },
      { path: "category", component: () => import("@/views/Client/Category.vue") },
      { path: "shopping-cart", component: () => import("@/views/Client/ShoppingCart.vue") },
    ],
  },
];

export default clientRoutes;
