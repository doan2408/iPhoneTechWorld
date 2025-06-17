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

        <!-- Main content : nội dung -->
        <div class="pos-main">
            <!--  phần bên trái -->
            <div class="left-panel">
                <!-- Categories : danh mục sản phẩm -->
                <div class="categories-section">
                    <h3>Danh mục sản phẩm</h3>
                    <div class="categories-list">
                        <button v-for="category in categoryProduct" :key="category.tenSanPham" @click="selectedCategory = category.tenSanPham,
                            loadProducts(category)"
                            :class="['category-btn', { active: selectedCategory === category.tenSanPham }]">
                            <component :is="category.icon" class="category-icon" />
                            {{ category.tenSanPham }}
                        </button>
                        <!-- phan trang category -->
                        <div class="pagination-controls">
                            <button @click="prevPage" :disabled="pageNo === 0"> &lt; </button>
                            <span>Trang {{ pageNo + 1 }} / {{ totalPagesCategory }}</span>
                            <button @click="nextPage" :disabled="pageNo + 1 >= totalPagesCategory"> &gt; </button>
                        </div>
                    </div>
                </div>

                <!-- Quick actions : hành động  -->
                <div class="quick-actions">
                    <button class="quick-btn">
                        <History class="btn-icon" />
                        Lịch sử bán hàng
                    </button>
                </div>
            </div>

            <!-- phần bên phải : button chọn khách hàng , sản phẩm , giỏ hàng-->
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
                                <img :src="product.url" :alt="product.tenSanPham" />
                                <div v-if="product.soLuong === 0" class="out-of-stock">
                                    Hết hàng
                                </div>
                                <div v-else-if="product.soLuong <= 5" class="low-stock">
                                    {{ product.soLuong }}
                                </div>
                            </div>
                            <div class="product-info">
                                <h4>{{ product.tenSanPham }} {{ product.rom }}</h4>
                                <div class="product-color">{{ product.mau }}</div>
                                <div class="product-price">{{ formatCurrency(product.giaBan) }}</div>
                                <div class="product-stock" :class="{
                                    'out-of-stock-text': product.soLuong === 0,
                                    'low-stock-text': product.soLuong > 0 && product.soLuong <= 5,
                                    'in-stock-text': product.soLuong > 5
                                }">
                                    {{ product.soLuong === 0 ? 'Hết hàng' : 'Số lượng: '+ product.soLuong }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Phan trang san pham -->
                <div class="pagination">
                    <button @click="previousPageProduct(currentCategory)" :disabled="pageNoProduct === 0" class="page-btn">
                        <ChevronLeft class="page-icon" />
                    </button>
                    <span class="page-info">{{ pageNoProduct +1 }}/{{ totalPagesProdut }}</span>
                    <button @click="nextPageProduct(currentCategory)" :disabled="pageNoProduct + 1  === totalPagesProdut" class="page-btn">
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
                <input v-model="currentInvoice.notes" type="text" placeholder="Ghi chú đơn hàng" class="notes-input" />
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
</template>

<script setup>
import { ref, reactive, computed, onMounted, markRaw } from 'vue'
import {
    Search, X, Plus, Lock, Undo, RotateCcw, Printer, Menu,
    User, Users, List, Filter, Eye, Edit, ChevronLeft, ChevronRight,
    ChevronUp, ChevronDown, Minus, Trash2, History, FileText,
    Smartphone, Laptop, Watch, Headphones, Camera, Gamepad2
} from 'lucide-vue-next'
import { findSanPhamBanHang } from '@/Service/Adminservice/Products/ProductAdminService';
import { loadSanPhamChiTiet } from '@/Service/Adminservice/Products/ProductAdminService';
import { loadCategory } from '@/Service/Adminservice/Products/ProductAdminService';
import { hoaDonDetail } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';
import { ca } from 'element-plus/es/locales.mjs';
// Search queries
const productSearchQuery = ref('')
const customerSearchQuery = ref('')
const selectedCategory = ref('all')
const products = ref([])
const tenSanPham = ref('')
const pageNo = ref(0)
const pageSize = ref(5)
const pageNoProduct = ref(0)
const pageSizeProduct = ref(10)
const categoryProduct = ref([])
const totalPagesCategory = ref(1)
const totalPagesProdut = ref(1)
const currentCategory = ref({ tenSanPham: 'All' })


// category san pham 
const danhMucSanPham =  async () => {
    try {
        const response = await loadCategory(pageNo.value, pageSize.value)
        const danhMuc = response.data.content.map(ten => ({
            tenSanPham: ten,
            icon: markRaw(Smartphone)
        }))
        categoryProduct.value = danhMuc
        totalPagesCategory.value = response.data.totalPages
    } catch (error) {
        console.error('Lỗi khi load danh mục sản phẩm:', error)
    }
}

const loadProducts = async (category) => {
    selectedCategory.value = category.tenSanPham;
    let response;
    if (selectedCategory.value.toLowerCase() === 'all') {
        response = await loadSanPhamChiTiet(pageNoProduct.value, pageSizeProduct.value);
    } else {
        response = await findSanPhamBanHang(selectedCategory.value, pageNoProduct.value, pageSizeProduct.value);
    }
    products.value = response.data.content;
    totalPagesProdut.value = response.data.totalPages

};


// Pagination
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


// ham loc
const filteredProducts = computed(() => {
    let filtered = products.value;

    // Nếu KHÔNG phải 'all' thì mới lọc theo category
    if (selectedCategory.value && selectedCategory.value.toLowerCase() !== 'all') {
        filtered = filtered.filter(p =>
            p.tenSanPham.toLowerCase().includes(selectedCategory.value.toLowerCase())
        );
    }

    // Nếu có nhập tìm kiếm thì tiếp tục lọc theo search
    if (productSearchQuery.value && productSearchQuery.value.trim() !== '') {
        filtered = filtered.filter(p =>
            p.tenSanPham.toLowerCase().includes(productSearchQuery.value.toLowerCase())
        );
    }

    return filtered;
});



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

// chuyen trang category
const nextPage = async () => {
    if (pageNo.value + 1 < totalPagesCategory.value) {
        pageNo.value++
        await danhMucSanPham()
    }

}

const prevPage = async () => {
    if (pageNo.value > 0) {
        pageNo.value--
        await danhMucSanPham()
    }

}
//chuyen trang product
const nextPageProduct = async (category) => {
    if (pageNoProduct.value + 1 < totalPagesProdut.value) {
        pageNoProduct.value++
        await loadProducts(category)
    }

}

const previousPageProduct = async (category) => {
    if (pageNoProduct.value > 0) {
        pageNoProduct.value--
        await loadProducts(category)
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
onMounted(async () => {
    calculateTotal();
    await danhMucSanPham();
    selectedCategory.value = 'all';
    await loadProducts({ tenSanPham: 'all' });
}); 
</script>

<style scoped src="@/style/HoaDon/BanHang.css">

</style>
  
