<template>
    <header class="client-header">
    <!-- Nội dung header -->
    <ClientHeader />
  </header>
    <main class="page-content">
    <router-view />
  </main>
    <Footer></Footer>
</template>

<script setup>
import ClientHeader from '@/components/Client/DefaultHeader.vue'
import Banner from '@/components/Client/Banner.vue';
import Footer from '@/components/Client/Footer.vue';
</script>

<style scoped>
.client-header {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* Đẩy toàn bộ nội dung xuống đúng bằng chiều cao header */
.page-content {
    margin-top: 69px; /* thay 80px bằng đúng chiều cao header của bạn */
}
</style>