<template>
    <ClientHeader />
    <router-view />
    <Footer></Footer>
</template>

<script setup>
import ClientHeader from '@/components/Client/DefaultHeader.vue'
import Banner from '@/components/Client/Banner.vue';
import Footer from '@/components/Client/Footer.vue';
</script>