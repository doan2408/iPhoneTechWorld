<template>
    <div class="page-container">
        <!-- left  -->
        <aside class="sidebar">
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Bộ Lọc Đơn Hàng</h2>
                </div>
                <div class="card-content space-y-6">
                    <!-- Filter by Status -->
                    <div class="space-y-2">
                        <label for="status-filter" class="label">Trạng thái</label>
                        <select id="status-filter" v-model="filterStatus" class="select-input">
                            <option value="all">Tất cả</option>
                            <option v-for="status in orderStatuses" :key="status" :value="status">
                                {{ status }}
                            </option>
                        </select>
                    </div>

                    <!-- Filter by Price Range -->
                    <div class="space-y-2">
                        <label class="label">Khoảng giá</label>
                        <div class="flex-group">
                            <input type="number" placeholder="Min" v-model="filterMinPrice" class="text-input flex-1" />
                            <span class="separator">-</span>
                            <input type="number" placeholder="Max" v-model="filterMaxPrice" class="text-input flex-1" />
                        </div>
                    </div>

                    <!-- Filter by Date Range -->
                    <div class="space-y-2">
                        <label class="label">Khoảng ngày</label>
                        <div class="space-y-2">
                            <div>
                                <label for="start-date" class="sr-only">Ngày bắt đầu</label>
                                <input id="start-date" type="date" v-model="filterStartDate" class="text-input" />
                            </div>
                            <div>
                                <label for="end-date" class="sr-only">Ngày kết thúc</label>
                                <input id="end-date" type="date" v-model="filterEndDate" class="text-input" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card-footer flex-col space-y-2">
                    <button @click="handleApplyFilters" class="button primary-button w-full">
                        Áp dụng bộ lọc
                    </button>
                    <button @click="handleResetFilters" class="button outline-button w-full">
                        Đặt lại bộ lọc
                    </button>
                </div>
            </div>
        </aside>

        <!-- right  -->
        <main class="main-content">
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
                    <div v-if="allOrderValue.length === 0" class="empty-state">
                        Không tìm thấy đơn hàng nào phù hợp.
                    </div>
                    <div v-for="order in allOrderValue" :key="order.idHoaDon" class="order-card">
                        <div class="order-status-bar">
                            <div class="order-status">
                                🧾 Trạng thái đơn: <span>{{ order.trangThaiGiaoHang }}</span>
                            </div>
                            <div class="payment-status">
                                💳 Thanh toán: <span>{{ order.trangThaiThanhToan }}</span>
                            </div>
                        </div>
                        <div class="order-products">
                            <div v-for="product in order.myOrderClientResponseList" :key="product.idSanPhamChiTiet"
                                class="product-item">
                                <img :src="product.urlImage" :alt="product.tenSanPham" class="product-image" />
                                <div class="product-details">
                                    <div class="product-name">{{ product.tenSanPham }}</div>
                                    <div class="product-variant">Phân loại hàng: {{ product.colorName +
                                        product.dungLuongRom
                                        }}</div>
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
                                Thành tiền: <span class="total-amount">{{ order.thanhTien }} VNĐ</span>
                            </div>
                            <div class="order-actions">
                                <button class="action-button buy-again-button">Mua Lại</button>
                                <button class="action-button contact-seller-button">Liên Hệ Người Bán</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div v-if="totalElements > pageSizeMyOrder" class="pagination-controls">
                    <button class="pagination-button" :disabled="currentPage === 0" @click="prevPage()">
                        Trước
                    </button>
                    <span v-for="page in totalPages" :key="page">
                        <button :class="['pagination-button', { active: currentPage === page-1 }]"
                            @click="changePage(page-1)">
                            {{ page }}
                        </button>
                    </span>
                    <button class="pagination-button" :disabled="currentPage ===  totalPages - 1" @click="nextPage()">
                        Sau
                    </button>
                </div>
            </main>
        </main>
        <section class="orders-stats">
            <!-- Thống kê đơn hàng -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Thống Kê Đơn Hàng</h2>
                </div>
                <div class="card-content space-y-4">
                    <p>Tổng đơn: <strong>{{ totalOrders }}</strong></p>
                    <p>Đã giao: <strong>{{ deliveredOrders }}</strong></p>
                    <p>Chờ xử lý: <strong>{{ pendingOrders }}</strong></p>
                    <p>Tổng doanh thu: <strong>₫{{ totalRevenue }}</strong></p>
                </div>
            </div>
        </section>

    </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { Package2Icon, SearchIcon, TruckIcon, InfoIcon } from 'lucide-vue-next'
import { getMyOrder } from '@/Service/ClientService/HoaDon/MyOrderClient'
import { pa } from 'element-plus/es/locales.mjs'

// Dummy data for orders
const allOrderValue = ref([])

const pageNoMyOrder = ref(0)
const pageSizeMyOrder = ref(5)
const totalElements = ref(0)
const totalPages = ref(0)
const currentPage = ref(0)


const allMyOrde = async () => {
    const res = await getMyOrder(currentPage.value, pageSizeMyOrder.value);
    allOrderValue.value = res.data.content
    totalElements.value = res.data.totalElements
    totalPages.value = res.data.totalPages
}

const changePage = (page) => {
    currentPage.value = page
} 

const prevPage = () => {
    if (currentPage.value > 0) {
        currentPage.value--
    }
}

const nextPage = () => {
    if (currentPage.value < totalPages.value - 1 ) currentPage.value++
}

watch(currentPage,(newPage)=>{
    allMyOrde()
})

const searchTerm = ref("")
const activeTab = ref("all")
const isUserMenuOpen = ref(false)
const activeOrderActions = ref(null)

// Pagination state
const itemsPerPage = 5 

const setActiveTab = (tab) => {
    activeTab.value = tab
    currentPage.value = 1 
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

<style scoped src="@/style/HoaDon/MyOrder.css"></style>
