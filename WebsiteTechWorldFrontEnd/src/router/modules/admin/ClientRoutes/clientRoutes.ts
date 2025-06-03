// src/router/modules/product.ts
import type { RouteRecordRaw } from "vue-router";

const clientAdminRoutes: RouteRecordRaw[] = [
      { path: "client", component: () => import("@/views/Admin/TaiKhoan/Client/ClientAdmin.vue"), meta: { title: 'Client Manage'} },
      { path: "client/add", component: () => import("@/views/Admin/TaiKhoan/Client/AddClient.vue"), meta: { title: 'Add Client'} },
      { path: "client/:id", component: () => import("@/views/Admin/TaiKhoan/Client/ClientDetailAdmin.vue"), meta: { title: 'Client Detail'}  },
      { path: "client/addresses/:idClient", component: () => import("@/views/Admin/TaiKhoan/Client/AddressClientAdmin.vue"), meta: { title: 'CLient Addresse'}  }, //địa chỉ khách hàng phía admin
      { path: "client/address/:idAddress/:idClient", component: () => import("@/views/Admin/TaiKhoan/Client/EditAddress.vue"), meta: { title: 'Edit Addresse'}  },
];

export default clientAdminRoutes;
