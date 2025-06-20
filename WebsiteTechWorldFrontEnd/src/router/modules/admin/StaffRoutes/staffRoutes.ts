// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";

const staffAdminRoutes: RouteRecordRaw[] = [
       { path: "staff", component: () => import("@/views/Admin/TaiKhoan/Staff/StaffAdmin.vue"), meta: { title: 'Staff Manage'} },
       { path: "staff/:id", component: () => import("@/views/Admin/TaiKhoan/Staff/StaffDetailAdmin.vue"), meta: { title: 'Staff Detail'} },
       { path: "staff/add", component: () => import("@/views/Admin/TaiKhoan/Staff/AddStaffAdmin.vue"), meta: { title: 'Staff Add'} },
       {path: "staff/infor", component: () => import("@/views/Admin/TaiKhoan/Staff/PrivateInformation.vue"), meta: { title: 'Private Information'} },
];

export default staffAdminRoutes;
