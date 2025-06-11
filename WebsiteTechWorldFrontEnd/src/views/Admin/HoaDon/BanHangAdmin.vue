<template>
    <div class="pos-system">
        <!-- Header với tabs hóa đơn -->
        <div class="pos-header">
            <div class="left-section">
                <!-- Tìm kiếm hàng hóa -->
                <div class="search-box">
                    <Search class="search-icon" />
                    <input v-model="productSearchQuery" type="text" placeholder="Tìm hàng hóa (F3)" class="search-input"
                        @keydown.f3.prevent="focusProductSearch" />
                </div>

                <!-- Tabs hóa đơn -->
                <div class="invoice-tabs">
                    <div v-for="invoice in invoices" :key="invoice.id" @click="selectInvoice(invoice.id)"
                        :class="['invoice-tab', { active: currentInvoiceId === invoice.id }]">
                        <span>{{ invoice.name }}</span>
                        <button v-if="invoices.length > 1" @click.stop="closeInvoice(invoice.id)" class="close-tab-btn">
                            <X class="close-icon" />
                        </button>
                    </div>
                    <button @click="addNewInvoice" class="add-tab-btn">
                        <Plus class="add-icon" />
                    </button>
                </div>
            </div>

            <!-- Header actions -->
            <div class="header-actions">
                <button class="action-btn" title="Khóa màn hình">
                    <Lock class="btn-icon" />
                </button>
                <button class="action-btn" title="Quay lại">
                    <Undo class="btn-icon" />
                </button>
                <button class="action-btn" title="Làm mới">
                    <RotateCcw class="btn-icon" />
                </button>
                <button class="action-btn" title="In">
                    <Printer class="btn-icon" />
                </button>
                <span class="phone-number">0395346933</span>
                <button class="menu-btn">
                    <Menu class="menu-icon" />
                </button>
            </div>
        </div>

        <!-- Main content -->
        <div class="pos-main">
            <!-- Left panel - Categories & Products -->
            <div class="left-panel">
                <!-- Categories -->
                <div class="categories-section">
                    <h3>Danh mục sản phẩm</h3>
                    <div class="categories-list">
                        <button v-for="category in categories" :key="category.id"
                            @click="selectedCategory = category.id"
                            :class="['category-btn', { active: selectedCategory === category.id }]">
                            <component :is="category.icon" class="category-icon" />
                            {{ category.name }}
                        </button>
                    </div>
                </div>

                <!-- Quick actions -->
                <div class="quick-actions">
                    <button class="quick-btn">
                        <History class="btn-icon" />
                        Lịch sử bán hàng
                    </button>
                    <button class="quick-btn">
                        <FileText class="btn-icon" />
                        Hóa đơn chờ
                    </button>
                    <button class="quick-btn">
                        <Users class="btn-icon" />
                        Khách hàng
                    </button>
                </div>
            </div>

            <!-- Right panel - Products & Cart -->
            <div class="right-panel">
                <!-- Customer search -->
                <div class="customer-search">
                    <div class="search-box">
                        <Search class="search-icon" />
                        <input v-model="customerSearchQuery" type="text" placeholder="Tìm khách hàng (F4)"
                            class="search-input" @keydown.f4.prevent="focusCustomerSearch" @blur="searchCustomer" />
                    </div>
                    <div class="customer-actions">
                        <button @click="showCustomerModal = true" class="customer-btn" title="Thêm khách hàng">
                            <Plus class="btn-icon" />
                        </button>
                        <button class="customer-btn" title="Danh sách khách hàng">
                            <List class="btn-icon" />
                        </button>
                        <button class="customer-btn" title="Lọc">
                            <Filter class="btn-icon" />
                        </button>
                        <button class="customer-btn" title="Xem chi tiết">
                            <Eye class="btn-icon" />
                        </button>
                    </div>
                </div>

                <!-- Selected customer info -->
                <div v-if="currentInvoice.customer.name" class="selected-customer">
                    <div class="customer-info">
                        <User class="customer-icon" />
                        <div>
                            <strong>{{ currentInvoice.customer.name }}</strong>
                            <span class="customer-phone">{{ currentInvoice.customer.phone }}</span>
                        </div>
                    </div>
                    <button @click="clearCustomer" class="clear-customer-btn">
                        <X class="clear-icon" />
                    </button>
                </div>

                <!-- Products grid -->
                <div class="products-section">
                    <div class="products-grid">
                        <div v-for="product in filteredProducts" :key="product.id" @click="addToCart(product)"
                            class="product-card">
                            <div class="product-image">
                                <img :src="product.image" :alt="product.name" />
                                <div v-if="product.stock === 0" class="out-of-stock">
                                    Hết hàng
                                </div>
                                <div v-else-if="product.stock <= 5" class="low-stock">
                                    {{ product.stock }}
                                </div>
                            </div>
                            <div class="product-info">
                                <h4>{{ product.name }}</h4>
                                <div class="product-price">{{ formatCurrency(product.price) }}</div>
                                <div class="product-stock">{{ product.stock }}</div>
                            </div>
                        </div>
                    </div>

                    <!-- Pagination -->
                    <div class="pagination">
                        <button @click="previousPage" :disabled="currentPage === 1" class="page-btn">
                            <ChevronLeft class="page-icon" />
                        </button>
                        <span class="page-info">{{ currentPage }}/{{ totalPages }}</span>
                        <button @click="nextPage" :disabled="currentPage === totalPages" class="page-btn">
                            <ChevronRight class="page-icon" />
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer - Cart & Payment -->
        <div class="pos-footer">
            <div class="cart-summary">
                <!-- Order notes -->
                <div class="order-notes">
                    <Edit class="notes-icon" />
                    <input v-model="currentInvoice.notes" type="text" placeholder="Ghi chú đơn hàng"
                        class="notes-input" />
                </div>

                <!-- Cart items (if any) -->
                <div v-if="currentInvoice.items.length > 0" class="cart-items-summary">
                    <div class="cart-header">
                        <span>Sản phẩm đã chọn ({{ currentInvoice.items.length }})</span>
                        <button @click="showCartDetails = !showCartDetails" class="toggle-cart-btn">
                            <ChevronUp v-if="showCartDetails" class="toggle-icon" />
                            <ChevronDown v-else class="toggle-icon" />
                        </button>
                    </div>

                    <div v-if="showCartDetails" class="cart-items-list">
                        <div v-for="item in currentInvoice.items" :key="item.id" class="cart-item">
                            <img :src="item.image" :alt="item.name" class="cart-item-image" />
                            <div class="cart-item-info">
                                <span class="item-name">{{ item.name }}</span>
                                <span class="item-price">{{ formatCurrency(item.price) }}</span>
                            </div>
                            <div class="cart-item-controls">
                                <button @click="decreaseQuantity(item)" class="qty-btn">
                                    <Minus class="qty-icon" />
                                </button>
                                <span class="quantity">{{ item.quantity }}</span>
                                <button @click="increaseQuantity(item)" class="qty-btn">
                                    <Plus class="qty-icon" />
                                </button>
                            </div>
                            <div class="item-total">{{ formatCurrency(item.price * item.quantity) }}</div>
                            <button @click="removeFromCart(item)" class="remove-item-btn">
                                <Trash2 class="remove-icon" />
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Total -->
                <div class="total-section">
                    <span class="total-label">Tổng tiền hàng:</span>
                    <span class="total-amount">{{ formatCurrency(currentInvoice.total) }}</span>
                </div>
            </div>

            <!-- Payment button -->
            <button @click="processPayment" :disabled="currentInvoice.items.length === 0" class="payment-btn">
                THANH TOÁN
            </button>
        </div>

        <!-- Customer Modal -->
        <div v-if="showCustomerModal" class="modal-overlay" @click="showCustomerModal = false">
            <div class="modal-content" @click.stop>
                <div class="modal-header">
                    <h3>Thêm khách hàng mới</h3>
                    <button @click="showCustomerModal = false" class="close-modal-btn">
                        <X class="close-icon" />
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>Tên khách hàng *</label>
                        <input v-model="newCustomer.name" type="text" class="form-input" required />
                    </div>
                    <div class="form-group">
                        <label>Số điện thoại *</label>
                        <input v-model="newCustomer.phone" type="tel" class="form-input" required />
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input v-model="newCustomer.email" type="email" class="form-input" />
                    </div>
                    <div class="form-group">
                        <label>Địa chỉ</label>
                        <textarea v-model="newCustomer.address" class="form-textarea"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button @click="showCustomerModal = false" class="cancel-btn">Hủy</button>
                    <button @click="addCustomer" class="save-btn">Lưu</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import {
    Search, X, Plus, Lock, Undo, RotateCcw, Printer, Menu,
    User, Users, List, Filter, Eye, Edit, ChevronLeft, ChevronRight,
    ChevronUp, ChevronDown, Minus, Trash2, History, FileText,
    Smartphone, Laptop, Watch, Headphones, Camera, Gamepad2
} from 'lucide-vue-next'

// Search queries
const productSearchQuery = ref('')
const customerSearchQuery = ref('')

// Categories
const categories = ref([
    { id: 'all', name: 'Tất cả', icon: List },
    { id: 'phone', name: 'Điện thoại', icon: Smartphone },
    { id: 'laptop', name: 'Laptop', icon: Laptop },
    { id: 'watch', name: 'Đồng hồ', icon: Watch },
    { id: 'headphone', name: 'Tai nghe', icon: Headphones },
    { id: 'camera', name: 'Camera', icon: Camera },
    { id: 'gaming', name: 'Gaming', icon: Gamepad2 }
])

const selectedCategory = ref('all')

// Products
const products = ref([
    {
        id: 1,
        name: 'Apple Watch Series 9 GPS + Cellular',
        price: 12990000,
        stock: 0,
        category: 'watch',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 2,
        name: 'Apple Watch Series 9 GPS + Cellular',
        price: 12990000,
        stock: 0,
        category: 'watch',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 3,
        name: 'Đồng hồ thông minh Huawei Kids',
        price: 2990000,
        stock: 0,
        category: 'watch',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 4,
        name: 'Đồng hồ thông minh Huawei Kids',
        price: 2990000,
        stock: 0,
        category: 'watch',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 5,
        name: 'Vòng đeo tay thông minh Xiaomi Mi',
        price: 1290000,
        stock: 0,
        category: 'watch',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 6,
        name: 'Vòng đeo tay thông minh Xiaomi Mi',
        price: 1290000,
        stock: 0,
        category: 'watch',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 7,
        name: 'iPhone 14 256GB',
        price: 23990000,
        stock: 0,
        category: 'phone',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 8,
        name: 'iPhone 14 256GB',
        price: 23990000,
        stock: 0,
        category: 'phone',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 9,
        name: 'Samsung Galaxy S23 Ultra 256GB',
        price: 23990000,
        stock: 15,
        category: 'phone',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 10,
        name: 'Samsung Galaxy S23 Ultra 256GB',
        price: 23990000,
        stock: 8,
        category: 'phone',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 11,
        name: 'Xiaomi Redmi Note 13 Pro 128GB',
        price: 7290000,
        stock: 25,
        category: 'phone',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 12,
        name: 'Xiaomi Redmi Note 13 Pro 128GB',
        price: 7290000,
        stock: 12,
        category: 'phone',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 13,
        name: 'Macbook Air 13 inch M2 256GB',
        price: 23690000,
        stock: 5,
        category: 'laptop',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 14,
        name: 'Dell Inspiron 15 3520 i5 1235U',
        price: 15490000,
        stock: 18,
        category: 'laptop',
        image: '/placeholder.svg?height=80&width=80'
    },
    {
        id: 15,
        name: 'Dell Inspiron 15 3520 i5 1235U',
        price: 15490000,
        stock: 22,
        category: 'laptop',
        image: '/placeholder.svg?height=80&width=80'
    }
])

// Pagination
const currentPage = ref(1)
const itemsPerPage = 12

// Invoices management
const invoices = ref([
    {
        id: 1,
        name: 'Hóa đơn 1',
        items: [],
        customer: { name: '', phone: '', email: '', address: '' },
        notes: '',
        total: 0
    }
])

const currentInvoiceId = ref(1)
let nextInvoiceId = 2

// Customer modal
const showCustomerModal = ref(false)
const newCustomer = reactive({
    name: '',
    phone: '',
    email: '',
    address: ''
})

// Cart display
const showCartDetails = ref(false)

// Computed properties
const currentInvoice = computed(() => {
    return invoices.value.find(inv => inv.id === currentInvoiceId.value) || invoices.value[0]
})

const filteredProducts = computed(() => {
    let filtered = products.value

    if (selectedCategory.value !== 'all') {
        filtered = filtered.filter(p => p.category === selectedCategory.value)
    }

    if (productSearchQuery.value) {
        filtered = filtered.filter(p =>
            p.name.toLowerCase().includes(productSearchQuery.value.toLowerCase())
        )
    }

    // Pagination
    const start = (currentPage.value - 1) * itemsPerPage
    const end = start + itemsPerPage
    return filtered.slice(start, end)
})

const totalPages = computed(() => {
    let filtered = products.value

    if (selectedCategory.value !== 'all') {
        filtered = filtered.filter(p => p.category === selectedCategory.value)
    }

    if (productSearchQuery.value) {
        filtered = filtered.filter(p =>
            p.name.toLowerCase().includes(productSearchQuery.value.toLowerCase())
        )
    }

    return Math.ceil(filtered.length / itemsPerPage)
})

// Methods
const selectInvoice = (id) => {
    currentInvoiceId.value = id
}

const addNewInvoice = () => {
    const newInvoice = {
        id: nextInvoiceId++,
        name: `Hóa đơn ${nextInvoiceId - 1}`,
        items: [],
        customer: { name: '', phone: '', email: '', address: '' },
        notes: '',
        total: 0
    }
    invoices.value.push(newInvoice)
    currentInvoiceId.value = newInvoice.id
}

const closeInvoice = (id) => {
    if (invoices.value.length === 1) return

    const index = invoices.value.findIndex(inv => inv.id === id)
    if (index > -1) {
        invoices.value.splice(index, 1)
        if (currentInvoiceId.value === id) {
            currentInvoiceId.value = invoices.value[0].id
        }
    }
}

const addToCart = (product) => {
    if (product.stock === 0) return

    const invoice = currentInvoice.value
    const existingItem = invoice.items.find(item => item.id === product.id)

    if (existingItem) {
        if (existingItem.quantity < product.stock) {
            existingItem.quantity++
        }
    } else {
        invoice.items.push({
            ...product,
            quantity: 1
        })
    }

    calculateTotal()
}

const removeFromCart = (item) => {
    const invoice = currentInvoice.value
    const index = invoice.items.findIndex(cartItem => cartItem.id === item.id)
    if (index > -1) {
        invoice.items.splice(index, 1)
        calculateTotal()
    }
}

const increaseQuantity = (item) => {
    const product = products.value.find(p => p.id === item.id)
    if (item.quantity < product.stock) {
        item.quantity++
        calculateTotal()
    }
}

const decreaseQuantity = (item) => {
    if (item.quantity > 1) {
        item.quantity--
        calculateTotal()
    } else {
        removeFromCart(item)
    }
}

const calculateTotal = () => {
    const invoice = currentInvoice.value
    invoice.total = invoice.items.reduce((sum, item) => sum + (item.price * item.quantity), 0)
}

const searchCustomer = () => {
    // Simulate customer search
    if (customerSearchQuery.value === '0901234567') {
        currentInvoice.value.customer = {
            name: 'Nguyễn Văn A',
            phone: '0901234567',
            email: 'nguyenvana@email.com',
            address: '123 Đường ABC, Quận 1, TP.HCM'
        }
    }
}

const clearCustomer = () => {
    currentInvoice.value.customer = { name: '', phone: '', email: '', address: '' }
    customerSearchQuery.value = ''
}

const addCustomer = () => {
    if (!newCustomer.name || !newCustomer.phone) return

    currentInvoice.value.customer = { ...newCustomer }
    customerSearchQuery.value = newCustomer.phone

    // Reset form
    Object.assign(newCustomer, { name: '', phone: '', email: '', address: '' })
    showCustomerModal.value = false
}

const previousPage = () => {
    if (currentPage.value > 1) {
        currentPage.value--
    }
}

const nextPage = () => {
    if (currentPage.value < totalPages.value) {
        currentPage.value++
    }
}

const processPayment = () => {
    if (currentInvoice.value.items.length === 0) return

    console.log('Processing payment for invoice:', currentInvoice.value)
    alert('Chuyển đến trang thanh toán...')
}

const focusProductSearch = () => {
    document.querySelector('.search-input').focus()
}

const focusCustomerSearch = () => {
    document.querySelector('.customer-search .search-input').focus()
}

const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN').format(amount)
}

// Initialize
onMounted(() => {
    calculateTotal()
})
</script>

<style scoped>
.pos-system {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background: #f5f7fa;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* Header */
.pos-header {
    background: #4f46e5;
    color: white;
    padding: 12px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.left-section {
    display: flex;
    align-items: center;
    gap: 20px;
    flex: 1;
}

.search-box {
    position: relative;
    display: flex;
    align-items: center;
}

.search-icon {
    position: absolute;
    left: 12px;
    width: 18px;
    height: 18px;
    color: #9ca3af;
    z-index: 1;
}

.search-input {
    padding: 10px 12px 10px 40px;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    width: 280px;
    font-size: 14px;
    background: white;
}

.invoice-tabs {
    display: flex;
    align-items: center;
    gap: 4px;
}

.invoice-tab {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.3s ease;
    font-size: 14px;
    border: 1px solid transparent;
}

.invoice-tab:hover {
    background: rgba(255, 255, 255, 0.2);
}

.invoice-tab.active {
    background: white;
    color: #4f46e5;
    border-color: #e5e7eb;
}

.close-tab-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 2px;
    border-radius: 3px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.close-tab-btn:hover {
    background: rgba(0, 0, 0, 0.1);
}

.close-icon {
    width: 14px;
    height: 14px;
}

.add-tab-btn {
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    color: white;
    padding: 8px;
    border-radius: 6px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
}

.add-tab-btn:hover {
    background: rgba(255, 255, 255, 0.2);
}

.add-icon {
    width: 16px;
    height: 16px;
}

.header-actions {
    display: flex;
    align-items: center;
    gap: 12px;
}

.action-btn {
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    color: white;
    padding: 8px;
    border-radius: 6px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
}

.action-btn:hover {
    background: rgba(255, 255, 255, 0.2);
}

.btn-icon {
    width: 16px;
    height: 16px;
}

.phone-number {
    font-size: 14px;
    font-weight: 500;
}

.menu-btn {
    background: rgba(255, 255, 255, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.2);
    color: white;
    padding: 8px;
    border-radius: 6px;
    cursor: pointer;
}

.menu-icon {
    width: 16px;
    height: 16px;
}

/* Main Content */
.pos-main {
    flex: 1;
    display: flex;
    gap: 20px;
    padding: 20px;
    overflow: hidden;
}

/* Left Panel */
.left-panel {
    width: 280px;
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.categories-section {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.categories-section h3 {
    margin: 0 0 16px 0;
    font-size: 16px;
    color: #1f2937;
}

.categories-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.category-btn {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 12px 16px;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    background: white;
    cursor: pointer;
    transition: all 0.3s ease;
    font-size: 14px;
    text-align: left;
}

.category-btn:hover {
    background: #f9fafb;
    border-color: #d1d5db;
}

.category-btn.active {
    background: #4f46e5;
    color: white;
    border-color: #4f46e5;
}

.category-icon {
    width: 18px;
    height: 18px;
}

.quick-actions {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.quick-btn {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 12px 16px;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    background: white;
    cursor: pointer;
    transition: all 0.3s ease;
    font-size: 14px;
    width: 100%;
    text-align: left;
    margin-bottom: 8px;
}

.quick-btn:last-child {
    margin-bottom: 0;
}

.quick-btn:hover {
    background: #f9fafb;
    border-color: #d1d5db;
}

/* Right Panel */
.right-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.customer-search {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 20px;
    padding-bottom: 16px;
    border-bottom: 1px solid #e5e7eb;
}

.customer-search .search-box {
    flex: 1;
}

.customer-search .search-input {
    width: 100%;
}

.customer-actions {
    display: flex;
    gap: 8px;
}

.customer-btn {
    background: #f3f4f6;
    border: 1px solid #d1d5db;
    padding: 10px;
    border-radius: 6px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
}

.customer-btn:hover {
    background: #e5e7eb;
}

.selected-customer {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: #f0f9ff;
    border: 1px solid #bae6fd;
    border-radius: 6px;
    padding: 12px 16px;
    margin-bottom: 20px;
}

.customer-info {
    display: flex;
    align-items: center;
    gap: 10px;
}

.customer-icon {
    width: 20px;
    height: 20px;
    color: #0369a1;
}

.customer-phone {
    display: block;
    font-size: 12px;
    color: #6b7280;
    margin-top: 2px;
}

.clear-customer-btn {
    background: #ef4444;
    color: white;
    border: none;
    padding: 6px;
    border-radius: 4px;
    cursor: pointer;
}

.clear-icon {
    width: 14px;
    height: 14px;
}

.products-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    /* Thêm dòng này */
}

.products-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    /* Giảm minmax từ 200px xuống 180px */
    gap: 12px;
    /* Giảm gap từ 16px xuống 12px */
    flex: 1;
    overflow-y: auto;
    margin-bottom: 16px;
    padding: 4px;
    /* Thêm padding nhỏ */
}

.product-card {
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 12px;
    /* Giảm padding từ 16px xuống 12px */
    cursor: pointer;
    transition: all 0.3s ease;
    background: white;
    position: relative;
    min-height: 160px;
    /* Thêm chiều cao tối thiểu */
    display: flex;
    flex-direction: column;
}

.product-info {
    flex: 1;
    /* Thêm để chiếm hết không gian còn lại */
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.product-info h4 {
    margin: 0 0 8px 0;
    font-size: 13px;
    /* Giảm font-size từ 14px xuống 13px */
    color: #1f2937;
    line-height: 1.3;
    /* Giảm line-height */
    height: 34px;
    /* Giảm height từ 40px xuống 34px */
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}

.product-price {
    font-size: 14px;
    /* Giảm từ 16px xuống 14px */
    font-weight: 600;
    color: #4f46e5;
    margin-bottom: 4px;
}

.product-stock {
    font-size: 11px;
    /* Giảm từ 12px xuống 11px */
    color: #6b7280;
}

/* Responsive cho products grid */
@media (max-width: 1200px) {
    .products-grid {
        grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    }
}

@media (max-width: 1024px) {
    .products-grid {
        grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    }
}

@media (max-width: 768px) {
    .products-grid {
        grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
        gap: 8px;
    }

    .product-card {
        padding: 8px;
        min-height: 140px;
    }

    .product-info h4 {
        font-size: 12px;
        height: 30px;
    }

    .product-price {
        font-size: 13px;
    }
}

/* Footer */
.pos-footer {
    background: white;
    border-top: 1px solid #e5e7eb;
    padding: 20px;
    display: flex;
    gap: 20px;
    align-items: flex-end;
    box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
}

.cart-summary {
    flex: 1;
}

.order-notes {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 16px;
}

.notes-icon {
    width: 18px;
    height: 18px;
    color: #6b7280;
}

.notes-input {
    flex: 1;
    padding: 10px 12px;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    font-size: 14px;
}

.cart-items-summary {
    background: #f9fafb;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    margin-bottom: 16px;
}

.cart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    border-bottom: 1px solid #e5e7eb;
    font-weight: 600;
    color: #1f2937;
}

.toggle-cart-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 4px;
}

.toggle-icon {
    width: 16px;
    height: 16px;
}

.cart-items-list {
    max-height: 200px;
    overflow-y: auto;
}

.cart-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    border-bottom: 1px solid #e5e7eb;
}

.cart-item:last-child {
    border-bottom: none;
}

.cart-item-image {
    width: 40px;
    height: 40px;
    object-fit: cover;
    border-radius: 4px;
}

.cart-item-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 2px;
}

.item-name {
    font-size: 14px;
    color: #1f2937;
    font-weight: 500;
}

.item-price {
    font-size: 12px;
    color: #6b7280;
}

.cart-item-controls {
    display: flex;
    align-items: center;
    gap: 8px;
}

.qty-btn {
    width: 24px;
    height: 24px;
    border: 1px solid #d1d5db;
    background: white;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
}

.qty-icon {
    width: 12px;
    height: 12px;
}

.quantity {
    min-width: 24px;
    text-align: center;
    font-weight: 600;
    font-size: 14px;
}

.item-total {
    min-width: 80px;
    text-align: right;
    font-weight: 600;
    color: #4f46e5;
    font-size: 14px;
}

.remove-item-btn {
    background: #ef4444;
    color: white;
    border: none;
    width: 24px;
    height: 24px;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
}

.remove-icon {
    width: 12px;
    height: 12px;
}

.total-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
}

.total-amount {
    color: #4f46e5;
}

.payment-btn {
    background: #4f46e5;
    color: white;
    border: none;
    padding: 16px 32px;
    border-radius: 8px;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    min-width: 160px;
}

.payment-btn:hover:not(:disabled) {
    background: #4338ca;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
}

.payment-btn:disabled {
    background: #d1d5db;
    color: #9ca3af;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
}

/* Modal */
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    border-radius: 12px;
    width: 90%;
    max-width: 500px;
    max-height: 90vh;
    overflow-y: auto;
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    border-bottom: 1px solid #e5e7eb;
}

.modal-header h3 {
    margin: 0;
    font-size: 18px;
    color: #1f2937;
}

.close-modal-btn {
    background: none;
    border: none;
    cursor: pointer;
    padding: 4px;
}

.modal-body {
    padding: 24px;
}

.form-group {
    margin-bottom: 16px;
}

.form-group label {
    display: block;
    margin-bottom: 6px;
    font-weight: 600;
    color: #374151;
}

.form-input {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    font-size: 14px;
}

.form-textarea {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    font-size: 14px;
    resize: vertical;
    min-height: 80px;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    padding: 20px 24px;
    border-top: 1px solid #e5e7eb;
}

.cancel-btn {
    padding: 10px 20px;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    background: white;
    cursor: pointer;
}

.save-btn {
    padding: 10px 20px;
    background: #4f46e5;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}

/* Responsive */
@media (max-width: 1024px) {
    .pos-main {
        flex-direction: column;
    }

    .left-panel {
        width: 100%;
        flex-direction: row;
        gap: 20px;
    }

    .categories-section,
    .quick-actions {
        flex: 1;
    }

    .products-grid {
        grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    }
}

@media (max-width: 768px) {
    .pos-header {
        flex-direction: column;
        gap: 12px;
    }

    .left-section {
        width: 100%;
        justify-content: space-between;
    }

    .search-input {
        width: 200px;
    }

    .invoice-tabs {
        overflow-x: auto;
    }

    .left-panel {
        flex-direction: column;
    }

    .categories-list {
        flex-direction: row;
        overflow-x: auto;
        gap: 8px;
    }

    .category-btn {
        white-space: nowrap;
    }

    .pos-footer {
        flex-direction: column;
        gap: 16px;
    }

    .payment-btn {
        width: 100%;
    }
}
</style>
  
