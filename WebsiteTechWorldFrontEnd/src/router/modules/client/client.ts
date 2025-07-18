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
      { path: "category", component: () => import("@/views/Client/Category.vue"), meta: { title: "Danh mục"} },
      { path: "shopping-cart", component: () => import("@/views/Client/ShoppingCart.vue"), meta: { title: "Giỏ hàng"} },
      { path: "profile", component: () => import("@/views/Client/TaiKhoanClient/PrivateInfor.vue"), meta: { title: "Thông tin cá nhân"} },
      { path: "addresses", component: () => import("@/views/Client/TaiKhoanClient/AddressesClient.vue"), meta: { title: "Địa chỉ"} },
      { path: "my-order", component: () => import("@/views/Client/HoaDonClient/MyOrder.vue"), meta: { title: "Đơn hàng của tôi" } },
      { path: "order-tracking-search", component: () => import("@/views/Client/HoaDonClient/OrderTrackingSearch.vue"), meta: { title: "Tra cuu don hang" } },
    ],
  },
];

export default clientRoutes;
