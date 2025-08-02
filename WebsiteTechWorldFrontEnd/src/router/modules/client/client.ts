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
      { path: "my-order", name:"myorder", component: () => import("@/views/Client/HoaDonClient/MyOrder.vue"), meta: { title: "Đơn hàng của tôi" } },
      { path: "order-tracking-search", component: () => import("@/views/Client/HoaDonClient/OrderTrackingSearch.vue"), meta: { title: "Tra cuu don hang" } },
      { path: "order-tracking/:id",name: "orderTracking", component: () => import("@/views/Client/HoaDonClient/OrderTracking.vue"), meta: { title: "Tra cuu don hang" } },
      { path: "checkout-form", name: "checkoutForm", component: () => import("@/views/Client/HoaDonClient/CheckoutForm.vue"), meta: { title: "CheckOutForm" } },
      { path: "order-succes", name: "ordersucces", component: () => import("@/views/Client/HoaDonClient/OrderSucces.vue"), meta: { title: "OrderSucces" } },
      { path: "lichSuDiem", component: () => import("@/views/Client/TaiKhoanClient/LichSuDiem.vue"), meta: { title: "Lịch sử điểm" } },
      { path: "doiDiem", component: () => import("@/views/Client/TaiKhoanClient/DoiDiem.vue"), meta: { title: "Quy đổi điểm" } },
      { path: "compare-page", name: "comparePage", component: () => import("@/views/Client/Compare/CompareProduct.vue"), meta: { title: "So sánh sản phẩm" } },
      { path: "favorites-page", name: "favoritesPage", component: () => import("@/views/Client/FavoritesPage/PageFavorites.vue"), meta: { title: "Sản phẩm yêu thích" } },
      
    ],
  },
];

export default clientRoutes;
