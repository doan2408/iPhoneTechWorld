import { createRouter, createWebHistory } from "vue-router";
import adminRoutes from "./modules/admin/admin";
import clientRoutes from "./modules/client/client";
import staffRoutes from "./modules/staff/staff";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
    path: '/',
    component: DefaultLayout,
    children: [
      { path: '', component: () => import('@/views/Guest/Home.vue'), meta: { title: 'Trang Chủ'} },
      {path: 'detail/:id', component: () => import('@/views/Guest/DetailProductGuest.vue'), meta: { title: 'Buy'} },
      {path: 'shopping-cart',name: "shoppingCardGuest", component: () => import('@/views/Client/ShoppingCart.vue'), meta: { title: 'Cart'} },
      // { path: 'home', component: () => import('@/views/Client/Home.vue'), meta: { title: 'Trang Chủ'} },
      { path: 'login', component: () => import('@/views/Authentical/LoginView.vue'), meta: { title: 'Login'} },
      { path: 'category', component: () => import('@/views/Client/Category.vue'), meta: { title: 'Category'} },
      { path: 'forgot-password', component: () => import('@/views/Authentical/ForgotView.vue'), meta: { title: 'Forgot password'}},
      { path: "verify-code", component: () => import('@/views/Authentical/VerifyCode.vue'), meta: { title: 'Verify'}},
      { path: "reset-password", component: () => import('@/views/Authentical/ResetPassword.vue'), meta: { title: 'Reset password'}},
      { path: "checkout-form", name: "clientDatHang", component: () => import("@/views/Guest/CheckOutForm/CheckoutFormGuest.vue"), meta: { title: "CheckOutForm" } },
      { path: "order-succes", name: "successClient", component: () => import("@/views/Guest/OrderSuccesGuest.vue"), meta: { title: "OrderSucces" } },
      { path: "compare-page", name: "comparePage", component: () => import("@/views/Client/Compare/CompareProduct.vue"), meta: { title: "So sánh sản phẩm" } },
      { path: "verify-register", name: "verifyRegister", component: () => import("@/views/Authentical/VerifyRegister.vue"), meta: { title: "Xác nhận đăng ký" } },
    ]
  },
  

  
//   Khi đang ở /, hiển thị HeaderDefault + Home.vue.

// Khi vào /admin, hiển thị HeaderAdmin + Dashboard.vue.

// Không bao giờ hiển thị trùng 2 cái header cả.

    ...adminRoutes,
    ...staffRoutes,
    ...clientRoutes,
  ],
});


// router.beforeEach là một navigation guard trong Vue Router,
//  được sử dụng để kiểm soát quá trình điều hướng (navigation) trước khi chuyển đến một tuyến đường (route) mới.
//  Nó cho phép bạn thực thi một số logic trước khi người dùng chuyển sang trang mới,
//  chẳng hạn như xác thực người dùng, kiểm tra quyền truy cập, hoặc lưu trạng thái trước khi điều hướng.
router.beforeEach(async (to, from, next) => {
  const store = (await import("@/Service/LoginService/Store")).default;
  
  // Kiểm tra nếu tuyến đường yêu cầu xác thực
  if (to.meta.requiresAuth) {
    await store.dispatch("fetchCurrentUser");
    console.log("User từ store sau khi fetch:", store.state.user);
    
    // Kiểm tra nếu không có thông tin người dùng
    if (!store.state.user) {
      // Lưu lại đường dẫn hiện tại vào query redirect để quay lại sau khi đăng nhập
      return next(`/login?redirect=${encodeURIComponent(to.fullPath)}`);  // Điều hướng đến trang login và lưu lại đường dẫn hiện tại
    }

    // Kiểm tra quyền của người dùng và điều hướng phù hợp
    if (to.meta.requiresAdmin && !store.getters.isAdmin) {
      return next("/product");  // Chuyển hướng nếu không phải admin
    }
    if (to.meta.requiresStaff && !store.getters.isStaff) {
      return next("/product");  // Chuyển hướng nếu không phải staff
    }
    if (to.meta.requiresCustomer && !store.getters.isCustomer) {
      return next("/product");  // Chuyển hướng nếu không phải customer
    }

    next();  // Nếu tất cả các điều kiện đều đúng, tiếp tục điều hướng
  } else {
    next();  // Nếu không cần xác thực, tiếp tục điều hướng bình thường
  }
});


router.afterEach((to) => {
  const defaultTitle = 'TechWorld';
  if (typeof window !== 'undefined' && document) {
    document.title = (to.meta?.title as string) || defaultTitle;
  }
});


export default router;
