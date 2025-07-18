<template>
    <div class="page-container">
        <header class="header">
            <div class="header-right">
                <div class="search-container">
                    <SearchIcon class="search-icon" />
                    <input type="search" placeholder="Bạn có thể tìm kiếm theo ID đơn hàng hoặc Tên Sản phẩm"
                        class="search-input" v-model="searchTerm" />
                </div>
            </div>
        </header>

        <main class="main-content">
            <div class="tabs-container">
                <div class="tabs-list">
                    <button :class="['tab-trigger', { active: activeTab === 'all' }]" @click="setActiveTab('all')">
                        Tất cả
                    </button>
                    <button :class="['tab-trigger', { active: activeTab === 'Chờ thanh toán' }]"
                        @click="setActiveTab('Chờ thanh toán')">
                        Chờ thanh toán
                    </button>
                    <button :class="['tab-trigger', { active: activeTab === 'Vận chuyển' }]"
                        @click="setActiveTab('Vận chuyển')">
                        Vận chuyển
                    </button>
                    <button :class="['tab-trigger', { active: activeTab === 'Chờ giao hàng' }]"
                        @click="setActiveTab('Chờ giao hàng')">
                        Chờ giao hàng
                    </button>
                    <button :class="['tab-trigger', { active: activeTab === 'Hoàn thành' }]"
                        @click="setActiveTab('Hoàn thành')">
                        Hoàn thành
                    </button>
                    <button :class="['tab-trigger', { active: activeTab === 'Đã hủy' }]"
                        @click="setActiveTab('Đã hủy')">
                        Đã hủy
                    </button>
                    <button :class="['tab-trigger', { active: activeTab === 'Trả hàng/Hoàn tiền' }]"
                        @click="setActiveTab('Trả hàng/Hoàn tiền')">
                        Trả hàng/Hoàn tiền
                    </button>
                </div>
            </div>

            <div class="order-list">
                <div v-if="1 === 0" class="empty-state">
                    Không tìm thấy đơn hàng nào phù hợp.
                </div>
                <div v-for="order in allOrderValue" :key="order.idHoaDon" class="order-card">
                    <div class="order-header">
                        <!-- Removed shop info and chat/view shop buttons -->
                        <div class="order-status-info">
                            <TruckIcon class="delivery-icon" />
                            <span>{{ order.trangThaiGiaoHang }}</span>
                            <InfoIcon class="info-icon" />
                            <span :class="['order-status-badge', getOrderStatusClass(order.trangThaiGiaoHang)]">{{
                                order.trangThaiGiaoHang.toUpperCase() }}</span>
                        </div>
                    </div>

                    <div class="order-products">
                        <div v-for="product in order.myOrderClientResponseList" :key="product.idSanPhamChiTiet"
                            class="product-item">
                            <img :src="product.urlImage" :alt="product.tenSanPham" class="product-image" />
                            <div class="product-details">
                                <div class="product-name">{{ product.tenSanPham }}</div>
                                <div class="product-variant">Phân loại hàng: {{ product.colorName + product.dungLuongRom }}</div>
                                <div class="product-quantity">x{{ product.soLuong }}</div>
                            </div>
                            <div class="product-prices">
                                <!-- <span v-if="product.originalPrice !== product.discountedPrice"
                                    class="original-price">₫{{ product.originalPrice }}</span> -->
                                <span class="discounted-price">₫{{ product.giaSanPham }}</span>
                            </div>
                        </div>
                    </div>

                    <div class="order-footer">
                        <div class="order-total">
                            Thành tiền: <span class="total-amount">₫{{ order.thanhTien }}</span>
                        </div>
                        <div class="order-actions">
                            <button class="action-button buy-again-button">Mua Lại</button>
                            <button class="action-button contact-seller-button">Liên Hệ Người Bán</button>
                        </div>
                    </div>
                </div>
            </div>

            <div v-if="0 > 1" class="pagination-controls">
                <button class="pagination-button" :disabled="currentPage === 1" @click="currentPage--">
                    Trước
                </button>
                <span v-for="page in [1,2,3]" :key="page">
                    <button :class="['pagination-button', { active: currentPage === page }]"
                        @click="currentPage = page">
                        {{ page }}
                    </button>
                </span>
                <button class="pagination-button" :disabled="currentPage === 1" @click="currentPage++">
                    Sau
                </button>
            </div>
        </main>
    </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { Package2Icon, SearchIcon, TruckIcon, InfoIcon } from 'lucide-vue-next'
import { getMyOrder } from '@/Service/ClientService/HoaDon/MyOrderClient'

// Dummy data for orders
const allOrderValue = ref ([])

const allMyOrde = async () => {
    const res = await getMyOrder();
    allOrderValue.value = res.data
    console.log(res);
    console.log(allOrderValue.value);
    
    
}

const searchTerm = ref("")
const activeTab = ref("all")
const isUserMenuOpen = ref(false)
const activeOrderActions = ref(null)

// Pagination state
const currentPage = ref(1)
const itemsPerPage = 5 // Number of orders per page

const setActiveTab = (tab) => {
    activeTab.value = tab
    currentPage.value = 1 // Reset to first page on tab change
    activeOrderActions.value = null
}

const toggleUserMenu = () => {
    isUserMenuOpen.value = !isUserMenuOpen.value
    activeOrderActions.value = null
}

const toggleOrderActions = (orderId) => {
    activeOrderActions.value = activeOrderActions.value === orderId ? null : orderId
    isUserMenuOpen.value = false
}

// Close dropdowns when clicking outside
window.addEventListener('click', (event) => {
    if (!event.target.closest('.dropdown-menu')) {
        isUserMenuOpen.value = false;
        activeOrderActions.value = null;
    }
});

onMounted(async () => {
    await allMyOrde();
})


const getOrderStatusClass = (status) => {
    switch (status) {
        case "Hoàn thành":
            return "status-completed"
        case "Đang vận chuyển":
            return "status-shipping"
        case "Chờ thanh toán":
            return "status-pending-payment"
        case "Vận chuyển":
            return "status-in-transit"
        case "Chờ giao hàng":
            return "status-awaiting-delivery"
        case "Đã hủy":
            return "status-cancelled"
        case "Trả hàng/Hoàn tiền":
            return "status-return-refund"
        default:
            return ""
    }
}
</script>

<style scoped src="@/style/HoaDon/MyOrder.css">
</style>
