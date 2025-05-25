<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="content-container">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import AdminSidebar from '@/components/Admin/Sidebar.vue'
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
}

.content-container {
  flex: 1;
  padding: 20px;
  margin-left: 220px; /* Khớp với width của sidebar */
  width: calc(100% - 220px);
  box-sizing: border-box;
  overflow-y: auto;
}

@media screen and (max-width: 768px) {
  .content-container {
    margin-left: 180px;
    width: calc(100% - 180px);
  }
}

@media screen and (max-width: 480px) {
  .admin-layout {
    flex-direction: column;
  }
  .content-container {
    margin-left: 0;
    width: 100%;
  }
}
</style>

