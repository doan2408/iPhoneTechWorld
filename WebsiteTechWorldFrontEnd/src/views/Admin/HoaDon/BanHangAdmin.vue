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
                        :class="['invoice-tab', { active: currentInvoiceId == invoice.id }]">
                        <span>{{ invoice.maHoaDon }}</span>
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
                <!-- <span class="phone-number">0395346933</span> -->
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
                        <button class="category-btn" @click="loadProducts({ tenSanPham: 'all' })"
                            :class="{ 'active': selectedCategory === 'all' }">
                            <Smartphone class="category-icon" />
                            All
                        </button>
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

                <!-- Chon chon khach hang -->
                <!-- <div v-if="currentInvoice && currentInvoice.maHoaDon" class="selected-customer">
                    <div class="customer-info">
                        <input type="hidden" value="{{ currentInvoice.id }}">
                        <User class="customer-icon" />
                        <div>
                            <strong>{{ currentInvoice.maHoaDon }}</strong> -->
                <!-- <span class="customer-phone">{{ currentInvoice.customer.phone }}</span> -->
                <!-- </div>
                    </div>
                    <button @click="clearCustomer" class="clear-customer-btn">
                        <X class="clear-icon" />
                    </button>
                </div> -->

                <!-- Products grid -->
                <div class="products-section">
                    <div class="products-grid">
                        <div v-for="product in products" :key="product.id" @click="openImeiModal(product)"
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
                                    {{ product.soLuong === 0 ? 'Hết hàng' : 'Số lượng: ' + product.soLuong }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div v-if="isImeiModalOpen" class="modal-overlay">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Chọn IMEI cho {{ selectedProductForImei?.tenSanPham }}</h2>
                            <div class="quantity-input-group">
                                <label for="quantityToSelect">Số lượng:</label>
                                <input type="number" id="quantityToSelect" v-model.number="quantityToSelect"
                                    :max="imeiTotalItems" @input="handleQuantityInputChange" />
                            </div>
                            <button @click="closeImeiModal" class="close-button">&times;</button>
                        </div>

                        <div class="modal-body">
                            <div class="imei-list">
                                <div v-for="imei in availableImeis" :key="imei.id" class="imei-item">
                                    <input type="checkbox" :id="`imei-${imei.id}`" :value="imei"
                                        v-model="selectedImeis" />
                                    <label :for="`imei-${imei.id}`">{{ imei.imei }}</label>
                                </div>
                            </div>
                        </div>

                        <div class="pagination-controls">
                            <button @click="goToImeiPage(imeiCurrentPage - 1)"
                                :disabled="imeiCurrentPage === 0">Trước</button>
                            <span>Trang {{ imeiCurrentPage + 1 }} / {{ imeiTotalPages }}</span>
                            <button @click="goToImeiPage(imeiCurrentPage + 1)"
                                :disabled="imeiCurrentPage+1 === imeiTotalPages">Sau</button>
                        </div>

                        <div class="modal-footer">
                            <button @click="autoSelectImeis" class="btn auto-select-btn"> Tự động chọn ({{ quantityToSelect || 0 }} IMEI)
                            </button>
                            <button @click="confirmImeiSelection" class="btn confirm-btn">
                                Xác nhận ({{ selectedImeis.length }} / {{ quantityToSelect || 0 }} IMEI)
                            </button>
                            <button @click="closeImeiModal" class="btn cancel-btn">Hủy</button>
                        </div>
                    </div>
                </div>




                <!-- Phan trang san pham -->
                <div class="pagination">
                    <button @click="previousPageProduct(currentCategory)" :disabled="pageNoProduct === 0"
                        class="page-btn">
                        <ChevronLeft class="page-icon" />
                    </button>
                    <span class="page-info">{{ pageNoProduct + 1 }}/{{ totalPagesProdut }}</span>
                    <button @click="nextPageProduct(currentCategory)" :disabled="pageNoProduct + 1 === totalPagesProdut"
                        class="page-btn">
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
            <!-- <div class="order-notes">
                <Edit class="notes-icon" />
                <input v-model="currentInvoice.notes" type="text" placeholder="Ghi chú đơn hàng" class="notes-input" />
            </div> -->

            <!-- Cart items (if any) : san pham da chon -->
            <div v-if="currentInvoiceDetail?.chiTietHoaDonAdminResponseList?.length > 0" class="cart-items-summary">
                <div class="cart-header">
                    <span>Sản phẩm đã chọn ({{ currentInvoiceDetail?.chiTietHoaDonAdminResponseList?.length }})</span>
                    <button @click="showCartDetails = !showCartDetails" class="toggle-cart-btn">
                        <ChevronUp v-if="showCartDetails" class="toggle-icon" />
                        <ChevronDown v-else class="toggle-icon" />
                    </button>
                </div>


                <div v-if="showCartDetails" class="cart-items-list">
                    <div v-for="item in currentInvoiceDetail?.chiTietHoaDonAdminResponseList"
                        :key="item.idHoaDonChiTiet" class="cart-item">
                        <img :src="item.imageSanPham" :alt="item.tenSanPham" class="cart-item-image" />
                        <div class="cart-item-info">
                            <span class="item-name">{{ item.tenSanPham }}</span>
                            <span class="item-price">{{ formatCurrency(item.donGia) }}</span>
                        </div>
                        <div class="cart-item-controls">
                            <button @click="decreaseQuantity(item)" class="qty-btn">
                                <Minus class="qty-icon" />
                            </button>
                            <span class="quantity">{{ item.soLuong }}</span>
                            <button @click="increaseQuantity(item)" class="qty-btn">
                                <Plus class="qty-icon" />
                            </button>
                        </div>
                        <div class="item-total">{{ formatCurrency(item.soLuong * item.donGia) }}</div>
                        <button @click="confirmRemoveFromCart(item)" class="remove-item-btn">
                            <Undo class="remove-icon" />
                        </button>

                        <Transition name="modal-fade">
                            <div v-if="showDeleteConfirmModal" class="modal-overlay">
                                <div class="modal-content-2">
                                    <h2>Xác nhận xóa sản phẩm</h2>
                                    <p>Bạn muốn xóa sản phẩm này như thế nào?</p>
                                    <div class="modal-actions">
                                        <button @click="removeFromCart" class="btn btn-danger">Trả toàn phần</button>
                                        <button @click="handleRemovePartial" class="btn btn-warning">Trả một
                                            phần</button>
                                        <button @click="closeModal" class="btn btn-secondary">Hủy</button>
                                    </div>
                                </div>
                            </div>
                        </Transition>

                        <Transition name="modal-fade">
                            <div v-if="showPartialDeleteModal" class="modal-overlay">
                                <div class="modal-content-2">
                                    <h2>Xóa một phần sản phẩm</h2>
                                    <p>Bạn muốn xóa bao nhiêu sản phẩm của <strong>{{ itemToDelete ?
                                            itemToDelete.ten_san_pham : '' }}</strong>?</p>

                                    <div class="quantity-input-group">
                                        <label for="quantity-to-remove">Số lượng:</label>
                                        <input type="number" id="quantity-to-remove" v-model.number="quantityToRemove"
                                            :min="1" :max="maxQuantity" @input="validateQuantity" />
                                        <span>/ {{ maxQuantity }}</span>
                                    </div>

                                    <div class="modal-actions">
                                        <button @click="confirmPartialRemove" class="btn btn-primary">Xác nhận
                                            xóa</button>
                                        <button @click="closePartialModal" class="btn btn-secondary">Hủy</button>
                                    </div>
                                </div>
                            </div>
                        </Transition>
                    </div>
                </div>
            </div>

            <!-- Total tong tien hang -->
            <div class="total-section">
                <span class="total-label">Tổng tiền hàng:</span>
                <span class="total-amount">{{ formatCurrency(invoices.total) }}</span>
            </div>
        </div>

        <!-- Payment button -->
        <!-- <button @click="processPayment" :disabled="currentInvoice.items.length === 0" class="payment-btn">
            THANH TOÁN
        </button> -->
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
import { ref, reactive, computed, onMounted, markRaw, watch, watchEffect, nextTick } from 'vue'
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
import { fetchImeisJs } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';
import { deleteDetailInvoice } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';
import { addProductIntoInvoice } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';
import { loadHoaDonByIdNhanVien } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';
import { ca } from 'element-plus/es/locales.mjs';
import { useRoute, useRouter } from 'vue-router';
// Search queries
const productSearchQuery = ref('')
const customerSearchQuery = ref('')
const selectedCategory = ref('all')
const products = ref([])
const tenSanPham = ref('')
const pageNo = ref(0)
const pageSize = ref(5)
const pageNoProduct = ref(0)
const pageSizeProduct = ref(8)
const categoryProduct = ref([])
const totalPagesCategory = ref(1)
const totalPagesProdut = ref(1)
const currentCategory = ref({ tenSanPham: 'All' })
const tabHoaDon = ref([])
const invoices = ref([]) // hien thi tab
const currentInvoiceId = ref(null)
const route = useRoute();
const router = useRouter();
const currentInvoiceDetail = ref([]);

// category san pham 
const danhMucSanPham = async () => {
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

//load tab hoa don
const loadTabHoaDon = async () => {
    // Bước 1: Load các hóa đơn của nhân viên
    const response = await loadHoaDonByIdNhanVien();
    response.data.forEach(inv => addOrUpdateInvoice(inv));
    tabHoaDon.value = [...invoices.value]; // clone ra nếu cần reactive riêng

    const queryId = route.query.invoiceId;
    const storedId = localStorage.getItem("selectedInvoiceId");

    // Bước 2: Nếu storedId chưa có trong danh sách → fetch
    if (storedId && !invoices.value.find(i => i.id == storedId)) {
        try {
            const res = await hoaDonDetail(storedId);
            if (res.data) addOrUpdateInvoice(res.data);
            if (res.data) {
                currentInvoiceDetail.value = res.data;
                console.log(currentInvoiceDetail.value);

            }
        } catch (e) {
            console.warn("Không thể tải hóa đơn từ localStorage:", e);
        }
    }

    // Bước 3: Xác định finalId
    let finalId = null;
    if (queryId && invoices.value.some(i => i.id == queryId)) {
        finalId = queryId;
    } else if (storedId && invoices.value.some(i => i.id == storedId)) {
        finalId = storedId;
    } else if (invoices.value.length > 0) {
        finalId = invoices.value[0].id;
    }

    // Bước 4: Gán finalId chính xác duy nhất ở đây
    if (finalId) {
        currentInvoiceId.value = finalId;
        localStorage.setItem('selectedInvoiceId', finalId);

        // Nếu chưa có chi tiết → gọi thêm
        const selected = invoices.value.find(i => i.id == finalId);
        if (!selected?.chiTietHoaDonAdminResponseList) {
            try {
                const res = await hoaDonDetail(finalId);
                if (res.data) addOrUpdateInvoice(res.data);
                if (res.data) {
                    currentInvoiceDetail.value = res.data;
                    console.log(currentInvoiceDetail.value);
                }
            } catch (e) {
                console.error("Không thể load chi tiết:", e);
            }
        }
    }

    if (queryId) {
        router.replace({ query: { ...route.query, invoiceId: undefined } });
    }
};


const addOrUpdateInvoice = (invoice) => {
    const index = invoices.value.findIndex(i => i.id === invoice.id);
    if (index !== -1) {
        invoices.value[index] = { ...invoices.value[index], ...invoice };
    } else {
        // Tránh thêm nếu đã có 1 hóa đơn cùng maHoaDon
        if (!invoices.value.some(i => i.maHoaDon === invoice.maHoaDon)) {
            invoices.value.push(invoice);
        }
    }
};
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
const showCartDetails = ref(true)

// handle invoice tab logic 
const currentInvoice = computed(() => {
    const id = Number(currentInvoiceId.value);
    const invoice = invoices.value.find(inv => inv.id === id) || invoices.value[0];
    return invoice;
});






// ham chon hoa don tab
const selectInvoice = async (id) => {
    currentInvoiceId.value = id;
    localStorage.setItem('selectedInvoiceId', id);

    const invoice = invoices.value.find(i => i.id === id);
    const hasDetail = invoice?.chiTietSanPham && invoice.chiTietSanPham.length > 0;

    if (!hasDetail) {
        try {
            const res = await hoaDonDetail(id);
            if (res.data) {
                addOrUpdateInvoice(res.data);
            }
        } catch (error) {
            console.error("Lỗi khi load chi tiết hóa đơn:", error);
        }
    }
};



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


const showDeleteConfirmModal = ref(false);
const itemToDelete = ref(null);
const showPartialDeleteModal = ref(false);
const quantityToRemove = ref(1); // số lượng mặc định muốn xóa
const maxQuantity = ref(0);

// mo modal xácd nhận muốn xóa
const confirmRemoveFromCart = (item) => {
    itemToDelete.value = item;
    showDeleteConfirmModal.value = true;
};

// đóng modal
const closeModal = () => {
    showDeleteConfirmModal.value = false;
    itemToDelete.value = null; // tra về nulk khi đóng modal
};
//xoas hoa đơn chi tiết
const removeFromCart = async () => {
    try {
        const res = await deleteDetailInvoice(itemToDelete.value.idHoaDonChiTiet);
        await loadTabHoaDon();
        showDeleteConfirmModal.value = false;
    } catch (err) {
        console.error("Xóa thất bại", err);
    }
}

// hàm nhập ssố lượng tra 1 phần
const handleRemovePartial = () => {
    if (!itemToDelete.value) return;

    closeModal();

    // maxQuantity.value = itemToDelete.value.soLuong || 1;
    // quantityToRemove.value = 1; 

    showPartialDeleteModal.value = true;
};

// hàm đóng modal trả 1 phần
const closePartialModal = () => {
    showPartialDeleteModal.value = false;
    quantityToRemove.value = 1;
    itemToDelete.value = null;
    maxQuantity.value = 0;
};

//ham xac nhan modal tra 1 phan
const confirmPartialRemove = async () => {
    showPartialDeleteModal.value = false;
}


const increaseQuantity = (item) => {
    const product = products.value.find(p => p.id === item.id)
    if (item.quantity < product.stock) {
        item.quantity++
        // calculateTotal()
    }
}

const decreaseQuantity = (item) => {
    if (item.quantity > 1) {
        item.quantity--
        // calculateTotal()
    } else {
        removeFromCart(item)
    }
}

// const calculateTotal = () => {
//     const invoice = currentInvoice.value
//     invoice.total = invoice.items.reduce((sum, item) => sum + (item.price * item.quantity), 0)
// }

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

//logic mo modal imei
const isImeiModalOpen = ref(false);
const selectedProductForImei = ref(null); // sản phẩm đng được chọn để mở imei
const availableImeis = ref([]); // danh sách imei hợp lệ cho sp đã chọn
const selectedImeis = ref([]); // danh sách imei mà nhân viên đẫ chọn trong modal
const imeiCurrentPage = ref(0);
const imeiPageSize = ref(9); // Số IMEI mỗi trang
const imeiTotalItems = ref(0); // Tổng số IMEI có sẵn
const imeiTotalPages = ref(0); // Tổng số trang IMEI
const quantityToSelect = ref(1); // lưu số lượng 


// mở modal imei
const openImeiModal = async (product) => {
    if (product) {
        selectedProductForImei.value = product;

        selectedImeis.value = []; // Reset các IMEI đã chọn trước đó
        imeiCurrentPage.value = 0; // Luôn bắt đầu từ trang 1
        quantityToSelect.value = 1; // <-- Reset về null khi mở modal mới để input trống

        // 1. Tải IMEI từ backend
        await fetchImeis(product.id, imeiCurrentPage.value, imeiPageSize.value);

        // 2. Mở modal sau khi tải xong (đảm bảo data đã có)
        isImeiModalOpen.value = true;

        // KHÔNG TỰ ĐỘNG GỌI autoSelectImeis Ở ĐÂY NỮA
        // Người dùng sẽ nhấn nút "Tự động chọn" sau khi nhập số lượng
    } else {
        alert("Không thể mở modal IMEI. Dữ liệu sản phẩm không hợp lệ.");
    }
};

// --- Hàm để lấy danh sách IMEI từ backend ---
const fetchImeis = async (productId, page, size) => {
    try {
        const response = await fetchImeisJs(productId,page,size)
        availableImeis.value = response.data.content;
        imeiTotalItems.value = response.data.totalElements;
        imeiTotalPages.value = response.data.totalPages;
    } catch (error) {
        console.error("Lỗi khi tải IMEI:", error);
        availableImeis.value = [];
        imeiTotalItems.value = 0;
        imeiTotalPages.value = 0;
    }
};

// xử lý phân trang imei
const goToImeiPage = async (page) => {
    console.log('page: ', page);
    if (page >= 0 && page <= imeiTotalPages.value) {
        console.log('page: ',page);
        console.log('imeiTotalPages: ', imeiTotalPages.value);
        imeiCurrentPage.value = page;
        console.log('imeiCurrentPage: ', imeiCurrentPage.value);
        await fetchImeis(selectedProductForImei.value.id, imeiCurrentPage.value, imeiPageSize.value);
    }
};

// Hàm xử lý khi input số lượng thay đổi
const handleQuantityInputChange = () => {
    if (quantityToSelect.value === null || isNaN(quantityToSelect.value)) {
        selectedImeis.value = []; 
        return;
    }
    quantityToSelect.value = parseInt(quantityToSelect.value);
    if (quantityToSelect.value > imeiTotalItems.value) {
        quantityToSelect.value = imeiTotalItems.value;
    }
    if (quantityToSelect.value <= 0) { 
        quantityToSelect.value = 0; 
    }

    selectedImeis.value = []; 
};

// chọn bỏ chọn imei bằng check box
const toggleImeiSelection = (imei) => {
    const index = selectedImeis.value.findIndex(item => item.id === imei.id);
    if (index > -1) {
        selectedImeis.value.splice(index, 1); // Bỏ chọn
    } else {
        if (selectedImeis.value.length < 1) { // Ví dụ: Giới hạn chỉ chọn 1 IMEI mỗi lần
            selectedImeis.value.push(imei); // Chọn
        } else {
            alert(`Bạn chỉ có thể chọn 1 IMEI cho sản phẩm này.`);
        }
    }
};

// kiểm tra xem imei có đnag dc chọn ko
const isImeiSelected = (imei) => {
    return selectedImeis.value.some(item => item.id === imei.id);
};

// tự động chọn imei
const autoSelectImeis = () => {
    selectedImeis.value = []; // Xóa các lựa chọn cũ


    // Lặp qua danh sách IMEI có sẵn trên trang hiện tại và chọn đủ số lượng
    const countToSelectOnCurrentPage = Math.min(quantityToSelect.value, availableImeis.value.length);
    for (let i = 0; i < countToSelectOnCurrentPage; i++) {
        selectedImeis.value.push(availableImeis.value[i]);
    }

    // Ghi chú: Logic này chỉ tự động chọn trên trang hiện tại.
    // Nếu số lượng cần chọn lớn hơn số IMEI trên trang, bạn cần xử lý phức tạp hơn
    // (như tự động chuyển trang hoặc thông báo cho người dùng).
};

// dong modal imei
const closeImeiModal = () => {
    isImeiModalOpen.value = false;
    selectedImeis.value = [];
    selectedProductForImei.value = null;
    quantityToSelect.value = null; // Reset về null khi đóng
};

// hàm xác nhận
const confirmImeiSelection = async () => {
    if (selectedImeis.value.length === quantityToSelect.value ) {
        await addToCartWithImeis(selectedProductForImei.value, selectedImeis.value);
        closeImeiModal();
    } else {
        alert(`Bạn phải chọn chính xác ${quantityToSelect.value || 0} IMEI.`);
    }
};



// them vao gio hang
const addToCartWithImeis = async (product, imeiList) => {
    const storedId = localStorage.getItem("selectedInvoiceId");

    const data = {
        idHoaDon: storedId,
        idSanPhamChiTiet: product.id,
        tenSanPham: product.tenSanPham,
        moTa: product.moTa,
        soLuong: imeiList.length, 
        imeiIds: imeiList.map(imei => imei.imei)
    };

    try {
        const response = await addProductIntoInvoice(storedId, data);
        await loadTabHoaDon(); // Tải lại dữ liệu hóa đơn sau khi thêm
    } catch (err) {
        console.error("Thêm sản phẩm và IMEI thất bại:", err);
    }
    // calculateTotal()
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

watch(currentInvoiceId, async (newId) => {
    if (!newId) return;

    const selected = invoices.value.find(i => i.id == newId);
    // Nếu chưa có chi tiết thì mới gọi
    if (!selected?.chiTietHoaDonAdminResponseList) {
        try {
            const res = await hoaDonDetail(newId);
            console.log("Reload chi tiết khi tab change:", res.data);
            if (res.data) {
                addOrUpdateInvoice(res.data);
                currentInvoiceDetail.value = res.data; // Gán lại chi tiết mới
            }
        } catch (e) {
            console.error("Lỗi khi load detail khi tab change:", e);
        }
    } else {
        currentInvoiceDetail.value = selected;
    }
});

// Watcher để tự động xóa lựa chọn khi chuyển trang
watch(imeiCurrentPage, async () => {
    selectedImeis.value = []; // Xóa các lựa chọn khi chuyển trang
    // Không tự động chọn lại ở đây. Người dùng sẽ tự chọn hoặc nhấn nút "Tự động chọn".
});

// Watcher cho quantityToSelect để tự động xóa lựa chọn khi số lượng thay đổi
watch(quantityToSelect, (newValue, oldValue) => {
    // Chỉ reset khi giá trị thực sự thay đổi và khác với giá trị cũ đã được chọn
    // Tránh reset khi giá trị được điều chỉnh nội bộ (ví dụ: ép về max)
    if (newValue !== oldValue) {
        selectedImeis.value = [];
    }
});

// Initialize
onMounted(async () => {
    // calculateTotal();
    await danhMucSanPham();
    selectedCategory.value = 'all';
    await loadProducts({ tenSanPham: 'all' });
    loadTabHoaDon();
}); 
</script>

<style scoped src="@/style/HoaDon/BanHang.css"></style>
