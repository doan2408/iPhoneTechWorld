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
      { path: '', component: () => import('@/views/Client/Home.vue') },
      { path: 'home', component: () => import('@/views/Client/Home.vue') },
      { path: 'login', component: () => import('@/views/Authentical/LoginView.vue') },
      { path: 'category', component: () => import('@/views/Client/Category.vue') },
      { path: 'forgot-password', component: () => import('@/views/Authentical/ForgotView.vue')},
      { path: "verify-code", component: () => import('@/views/Authentical/VerifyCode.vue')},
      { path: "reset-password", component: () => import('@/views/Authentical/ResetPassword.vue')},
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




export default router;
