<template>
    <div class="shopping-cart-container">
        <div class="cart-header">
            <div class="header-content">
                <div class="brand-section">
                    <div class="brand-logo">
                        <el-icon class="logo-icon">
                            <ShoppingCart />
                        </el-icon>
                    </div>
                    <div class="brand-info">
                        <h1 class="brand-title">Giỏ hàng của bạn</h1>
                    </div>
                </div>

                <div class="search-section">
                    <el-input v-model="search" placeholder="Tìm kiếm sản phẩm trong giỏ hàng..." class="search-input"
                        size="large">
                        <template #prefix>
                            <el-icon class="search-icon">
                                <Search />
                            </el-icon>
                        </template>
                    </el-input>
                </div>
            </div>
        </div>

        <div class="cart-content" :style="cartData.length > 3 ? { marginBottom: '100px' } : {}">
            <div v-if="cartData.length === 0 || totalItems === 0" class="empty-cart">
                <div class="empty-illustration">
                    <el-icon class="empty-icon">
                        <ShoppingCartFull />
                    </el-icon>
                </div>
                <h3 class="empty-title">Giỏ hàng trống</h3>
                <p class="empty-description">Hãy thêm sản phẩm vào giỏ hàng để bắt đầu mua sắm</p>
                <el-button type="primary" size="large" class="continue-shopping-btn">
                    <el-icon>
                        <Plus />
                    </el-icon>
                    Tiếp tục mua sắm
                </el-button>
            </div>

            <div v-else class="products-container">
                <div v-for="(product, productIndex) in filteredCartData" :key="productIndex" class="product-item"
                    :class="{ 'selected': product.selected }">
                    <div class="product-selection">
                        <el-checkbox v-model="product.selected" @change="handleProductSelect" />
                    </div>

                    <div class="product-image">
                        <el-image :src="product.imageUrl" fit="cover" class="product-img"
                            :preview-src-list="[product.imageUrl]" :preview-teleported="true">
                            <template #error>
                                <div class="image-error">
                                    <el-icon>
                                        <Picture />
                                    </el-icon>
                                </div>
                            </template>
                        </el-image>
                    </div>

                    <div class="product-info">
                        <div class="product-name">{{ product.tenSanPham }}</div>
                        <div class="product-variant">
                            <el-tag size="small" type="info">{{ product.phienBan }}</el-tag>
                        </div>
                        <div class="product-features">
                            <span class="feature-tag">Miễn phí đổi trả</span>
                            <span class="feature-tag">Chính hãng</span>
                        </div>
                    </div>

                    <div class="product-price">
                        <div class="label">Giá</div>
                        <div
                            style="display: flex; height: 90px; flex-direction: column; justify-content: space-around; align-items: flex-end; gap: 4px;">
                            <div class="current-price">{{ formatPrice(product.gia) }}</div>
                            <!-- <div v-if="product.originalPrice" class="original-price">
                                {{ formatPrice(product.originalPrice) }}
                            </div>
                            <div v-if="product.discount" class="discount-badge">
                                -{{ product.discount }}%
                            </div> -->
                        </div>
                    </div>

                    <div class="product-quantity">
                        <div class="label">Số lượng</div>
                        <div class="quantity-controls">
                            <el-button size="small" :disabled="product.soLuong <= 1" @click="decreaseQuantity(product)"
                                class="quantity-btn">
                                <el-icon>
                                    <Minus />
                                </el-icon>
                            </el-button>
                            <el-input-number v-model="product.soLuong" :min="1" :max="99" size="large" :controls="false" readonly
                                class="quantity-input" @change="updateTotaḷ" />
                            <el-button size="small" :disabled="product.soLuong >= product.soLuongTon" @click="increaseQuantity(product)" class="quantity-btn">
                                <el-icon>
                                    <Plus />
                                </el-icon>
                            </el-button>
                        </div>
                    </div>

                    <div class="product-total">
                        <div class="label">Thành tiền</div>
                        <div class="total-price">{{ formatPrice(product.gia * product.soLuong) }}</div>
                    </div>

                    <div class="product-actions">
                        <el-button size="small" @click="removeProduct(product.idSanPhamChiTiet)" class="delete-item">
                            <el-icon>
                                <Delete />
                            </el-icon>
                        </el-button>
                    </div>
                </div>
            </div>
        </div>

        <div v-if="totalItems > 0" class="checkout-section">
            <div class="checkout-container">
                <div class="checkout-left">
                    <el-checkbox v-model="selectAll" @change="handleSelectAll">
                        Chọn tất cả ({{ totalItems }})
                    </el-checkbox>
                    <el-button type="danger" text :disabled="selectedItems.length === 0" @click="handleBulkDelete">
                        Xóa
                    </el-button>
                </div>

                <div class="checkout-right">
                    <div class="checkout-summary">
                        <!-- <div class="summary-row">
                            <span class="summary-label">Tạm tính ({{ selectedItems.length }} sản phẩm):</span>
                            <span class="summary-value">{{ formatPrice(selectedTotal) }}</span>
                        </div>
                        <div class="summary-row">
                            <span class="summary-label">Phí vận chuyển:</span>
                            <span class="summary-value shipping-free">Miễn phí</span>
                        </div> -->
                        <div class="summary-row">
                            <span class="summary-label">Tổng cộng:</span>
                            <span class="summary-total">{{ formatPrice(selectedTotal) }}</span>
                        </div>
                    </div>

                    <el-button type="danger" size="large" class="checkout-btn" :disabled="selectedItems.length === 0"
                        @click="handleCheckout">
                        <el-icon>
                            <ShoppingBag />
                        </el-icon>
                        Mua Hàng ({{ selectedItems.length }})
                    </el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
    ShoppingCart, Search, ShoppingCartFull, Delete, Plus,
    Picture, Minus, ShoppingBag
} from '@element-plus/icons-vue';
import { cartService } from '@/service/ClientService/GioHang/GioHangClientService';

const search = ref('');
const selectAll = ref(false);
const cartData = ref([]);
const customerId = ref(1);

onMounted(async () => {
    await fetchCart();
});

async function fetchCart() {
    const user = JSON.parse(localStorage.getItem("user"));
    customerId.value = user?.id;
    try {
        const response = await cartService.getCart(customerId.value);
        cartData.value = response.items.map(item => ({
            idGioHangChiTiet: item.idGioHangChiTiet,
            idSanPhamChiTiet: item.idSanPhamChiTiet,
            tenSanPham: item.tenSanPham,
            phienBan: item.phienBan,
            imageUrl: item.imageUrl || '',
            gia: item.gia,
            soLuong: item.soLuong,
            soLuongTon: item.soLuongTon,
            ngayThem: item.ngayThem,
            selected: false
        }));
        console.log(cartData.value)
    } catch (error) {
        ElMessage.error('Không thể tải giỏ hàng');
    }
}

const filteredCartData = computed(() => {
    if (!search.value) return cartData.value;
    return cartData.value.filter(product =>
        product.tenSanPham.toLowerCase().includes(search.value.toLowerCase()) ||
        product.phienBan.toLowerCase().includes(search.value.toLowerCase())
    );
});

const totalItems = computed(() => {
    return cartData.value.reduce((sum, product) => sum + product.soLuong, 0);
});

const selectedItems = computed(() => {
    return cartData.value.filter(product => product.selected);
});

const selectedTotal = computed(() => {
    return selectedItems.value.reduce((sum, item) => sum + item.gia * item.soLuong, 0);
});

const formatPrice = (price) => {
    return price.toLocaleString('vi-VN') + ' ₫';
};

const updateTotal = () => {
    
};

const removeProduct = async (idSanPhamChiTiet) => {
    try {
        await ElMessageBox.confirm(
            'Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?',
            'Xác nhận xóa',
            {
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy',
                type: 'warning',
            }
        );
        await cartService.removeItem(idSanPhamChiTiet);
        await fetchCart();
        ElMessage.success('Đã xóa sản phẩm khỏi giỏ hàng');
    } catch {
        ElMessage.info('Đã hủy thao tác');
    }
};

const handleSelectAll = (value) => {
    cartData.value.forEach(product => {
        product.selected = value;
    });
    selectAll.value = value;
};

const handleProductSelect = () => {
    updateSelectAllState();
};

const updateSelectAllState = () => {
    const allSelected = cartData.value.length > 0 && cartData.value.every(product => product.selected);
    selectAll.value = allSelected;
};

const handleBulkDelete = async () => {
    if (selectedItems.value.length === 0) return;

    try {
        await ElMessageBox.confirm(
            `Bạn có chắc chắn muốn xóa ${selectedItems.value.length} sản phẩm đã chọn?`,
            'Xác nhận xóa',
            {
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy',
                type: 'warning',
            }
        );
        for (const item of selectedItems.value) {
            await cartService.removeItem(item.idSanPhamChiTiet);
        }
        await fetchCart();
        ElMessage.success('Đã xóa các sản phẩm đã chọn');
    } catch {
        ElMessage.info('Đã hủy thao tác');
    }
};

const decreaseQuantity = async (product) => {
    if (product.soLuong > 1) {
        product.soLuong--;
        console.log(product.idGioHangChiTiet, product.soLuong)
        try {
            await cartService.updateQuantity(product.idGioHangChiTiet, product.soLuong);
            updateTotal();
        } catch {
            ElMessage.error('Không thể cập nhật số lượng');
            product.soLuong++; 
        }
    }
};

const increaseQuantity = async (product) => {
    product.soLuong++;
    try {
        await cartService.updateQuantity(product.idGioHangChiTiet, product.soLuong);
        updateTotal();
    } catch {
        ElMessage.error('Không thể cập nhật số lượng');
        product.soLuong--; 
    }
};

const handleCheckout = () => {
    if (selectedItems.value.length === 0) {
        ElMessage.warning('Vui lòng chọn ít nhất một sản phẩm để thanh toán');
        return;
    }
    ElMessage.success(`Đang chuyển đến trang thanh toán với ${selectedItems.value.length} sản phẩm`);
    // Implement checkout logic here
};
</script>

<style scoped>
.shopping-cart-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.cart-header {
    background: white;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    position: sticky;
    top: 0;
    z-index: 100;
}

.header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 32px;
    max-width: 1400px;
    margin: 0 auto;
}

.brand-section {
    display: flex;
    align-items: center;
    gap: 16px;
}

.brand-logo {
    width: 48px;
    height: 48px;
    background: linear-gradient(135deg, #120350 0%, #136893 100%);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 4px 12px rgba(2, 1, 3, 0.3);
}

.logo-icon {
    font-size: 24px;
    color: white;
}

.brand-info {
    display: flex;
    flex-direction: column;
}

.brand-title {
    font-size: 24px;
    font-weight: 700;
    color: #887ed3;
    margin: 0;
    line-height: 1;
}

.brand-subtitle {
    font-size: 14px;
    color: #64748b;
    margin: 4px 0 0 0;
}

.search-section {
    flex: 1;
    max-width: 500px;
    margin-left: 40px;
}

.search-input :deep(.el-input__wrapper) {
    border-radius: 24px;
    border: 2px solid #e2e8f0;
    background: #f8fafc;
    transition: all 0.3s ease;
}

.search-input :deep(.el-input__wrapper:hover) {
    border-color: #887ed3;
    background: white;
}

.search-input :deep(.el-input__wrapper.is-focus) {
    border-color: #887ed3;
    background: white;
    box-shadow: 0 0 0 3px rgba(255, 107, 53, 0.1);
}

.search-icon {
    color: #9ca3af;
}

.cart-summary-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 32px;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    border-top: 1px solid #e2e8f0;
    max-width: 1400px;
    margin: 0 auto;
}

.summary-stats {
    display: flex;
    align-items: center;
    gap: 24px;
}

.stat-item {
    display: flex;
    align-items: center;
    gap: 8px;
}

.stat-icon {
    font-size: 18px;
    color: #887ed3;
}

.stat-number {
    font-size: 16px;
    font-weight: 700;
    color: #1a202c;
}

.stat-label {
    font-size: 14px;
    color: #64748b;
}

.stat-divider {
    width: 1px;
    height: 24px;
    background: #cbd5e1;
}

.bulk-actions {
    display: flex;
    align-items: center;
    gap: 16px;
}

.cart-content {
    max-width: 1400px;
    margin: 0 auto;
    padding: 24px 32px;
}

.empty-cart {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 80px 20px;
    text-align: center;
}

.empty-illustration {
    width: 120px;
    height: 120px;
    background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 24px;
}

.empty-icon {
    font-size: 48px;
    color: #9ca3af;
}

.empty-title {
    font-size: 24px;
    font-weight: 600;
    color: #374151;
    margin: 0 0 12px 0;
}

.empty-description {
    font-size: 16px;
    color: #6b7280;
    margin: 0 0 32px 0;
}

.continue-shopping-btn {
    height: 48px;
    padding: 0 32px;
    border-radius: 24px;
    background: linear-gradient(135deg, #120350 0%, #136893 100%);
    border: none;
    font-weight: 600;
}

.shops-container {
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.shop-card {
    background: white;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid #e2e8f0;
    overflow: hidden;
    transition: all 0.3s ease;
}

.shop-card:hover {
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
    transform: translateY(-2px);
}

/* Shop Header */
.shop-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    background: linear-gradient(135deg, #fafbfc 0%, #f3f4f6 100%);
    border-bottom: 1px solid #e2e8f0;
}

.shop-info {
    display: flex;
    align-items: center;
    gap: 16px;
}

.shop-checkbox {
    transform: scale(1.2);
}

.shop-details {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.shop-name {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 18px;
    font-weight: 600;
    color: #1a202c;
}

.shop-icon {
    font-size: 20px;
    color: #ff6b35;
}

.shop-badges {
    display: flex;
    gap: 8px;
}

.favorite-tag {
    background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%);
    color: #dc2626;
    border: 1px solid #fecaca;
}

.verified-tag {
    background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%);
    color: #166534;
    border: 1px solid #bbf7d0;
}

.shop-actions {
    display: flex;
    gap: 12px;
}

.products-container {
    padding: 0;
}

.product-item {
    display: grid;
    grid-template-columns: auto 120px 1fr 150px 180px 120px auto;
    gap: 20px;
    align-items: center;
    padding: 24px;
    border-bottom: 1px solid #f1f5f9;
    transition: all 0.3s ease;
}

.product-item:hover {
    background: #fafbfc;
}

.product-item.selected {
    background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
    border-left: 4px solid #3b82f6;
}

.product-item:last-child {
    border-bottom: none;
}

.product-selection {
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-image {
    position: relative;
}

.product-img {
    width: 100px;
    height: 100px;
    border-radius: 12px;
    border: 1px solid #e2e8f0;
    cursor: pointer;
    transition: all 0.3s ease;
}

.product-img:hover {
    transform: scale(1.05);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.image-error {
    width: 100px;
    height: 100px;
    background: #f3f4f6;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #9ca3af;
}

.product-info {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.product-name {
    font-size: 16px;
    font-weight: 500;
    color: #1a202c;
    line-height: 1.4;
}

.product-variant {
    margin: 4px 0;
}

.product-features {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.feature-tag {
    font-size: 11px;
    color: #059669;
    background: #ecfdf5;
    padding: 2px 6px;
    border-radius: 4px;
    border: 1px solid #a7f3d0;
}

.product-price {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 4px;
}

.current-price {
    font-size: 18px;
    font-weight: 700;
    color: #dc2626;
}

.original-price {
    font-size: 14px;
    color: #9ca3af;
    text-decoration: line-through;
}

.discount-badge {
    font-size: 12px;
    color: #dc2626;
    background: #fee2e2;
    padding: 2px 6px;
    border-radius: 4px;
    font-weight: 600;
}

.product-quantity {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
}

.label {
    font-size: 14px;
    color: #6b7280;
    font-weight: 500;
}

.quantity-controls {
    display: flex;
    align-items: center;
    gap: 8px;
    height: 90px;
}

.quantity-btn {
    width: 32px;
    height: 32px;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    background: white;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.quantity-btn:hover {
    border-color: #ff6b35;
    background: #fff7ed;
}

.quantity-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.quantity-controls {
    display: flex;
    align-items: center;
    gap: 4px;
}

.quantity-input {
    width: 60px;
}

.quantity-input :deep(.el-input__wrapper) {
    border-radius: 8px;
    text-align: center;
    padding: 4px 4px;
    justify-content: center;
}

.quantity-btn {
    border-radius: 8px;
    padding: 4px 8px;
}

.product-total {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
}

.total-price {
    font-size: 16px;
    font-weight: 700;
    color: #dc2626;
    height: 90px;
    display: flex;
    align-items: center;
}

.product-actions {
    display: flex;
    justify-content: center;
}

.more-btn {
    width: 32px;
    height: 32px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.delete-item {
    width: 32px;
    height: 32px;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    color: red;
    background: white;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
}

.delete-item:hover {
    color: white;
    background-color: #07295f;
}

/* Shop Total */
.shop-total {
    padding: 16px 24px;
    background: #fafbfc;
    border-top: 1px solid #e2e8f0;
}

.shop-total-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.total-items {
    font-size: 14px;
    color: #6b7280;
}

.total-amount {
    font-size: 16px;
    font-weight: 600;
    color: #1a202c;
}

.checkout-section {
    position: fixed;
    bottom: 0;
    background: white;
    border-top: 1px solid #e2e8f0;
    box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.1);
    z-index: 50;
    width: 100%;
    padding: 16px 24px;
}

.checkout-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 32px;
    max-width: 1400px;
    margin: 0 auto;
}

.checkout-left {
    display: flex;
    align-items: center;
    gap: 24px;
}

.checkout-right {
    display: flex;
    align-items: center;
    gap: 32px;
}

.checkout-summary {
    display: flex;
    flex-direction: column;
    gap: 8px;
    text-align: right;
}

.summary-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 24px;
}

.summary-label {
    font-size: 14px;
    color: #6b7280;
}

.summary-value {
    font-size: 14px;
    color: #1a202c;
    font-weight: 500;
}

.shipping-free {
    color: #059669;
    font-weight: 600;
}

.total-row {
    padding-top: 8px;
    border-top: 1px solid #e2e8f0;
    margin-top: 4px;
}

.summary-total {
    font-size: 20px;
    font-weight: 700;
    color: #dc2626;
}

.checkout-btn {
    height: 48px;
    padding: 0 32px;
    border-radius: 24px;
    background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%);
    border: none;
    font-weight: 600;
    font-size: 16px;
    box-shadow: 0 4px 15px rgba(220, 38, 38, 0.4);
    transition: all 0.3s ease;
}

.checkout-btn:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(220, 38, 38, 0.6);
}

.checkout-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
}

/* Responsive Design */
@media (max-width: 1200px) {
    .header-content {
        padding: 16px 24px;
    }

    .cart-summary-bar {
        padding: 12px 24px;
    }

    .cart-content {
        padding: 16px 24px;
    }

    .checkout-container {
        padding: 16px 24px;
    }
}

@media (max-width: 768px) {
    .header-content {
        flex-direction: column;
        gap: 16px;
    }

    .search-section {
        width: 100%;
        margin-left: 0;
    }

    .cart-summary-bar {
        flex-direction: column;
        gap: 16px;
    }

    .summary-stats {
        justify-content: space-around;
        width: 100%;
    }

    .product-item {
        grid-template-columns: auto 80px 1fr;
        gap: 12px;
        padding: 16px;
    }

    .product-price,
    .product-quantity,
    .product-total,
    .product-actions {
        grid-column: 1 / -1;
        justify-self: stretch;
        margin-top: 12px;
    }

    .product-quantity,
    .product-total {
        flex-direction: row;
        justify-content: space-between;
    }

    .checkout-container {
        flex-direction: column;
        gap: 16px;
    }

    .checkout-right {
        width: 100%;
        justify-content: space-between;
    }
}

@media (max-width: 480px) {
    .brand-title {
        font-size: 20px;
    }

    .product-img {
        width: 60px;
        height: 60px;
    }

    .image-error {
        width: 60px;
        height: 60px;
    }

    .product-name {
        font-size: 14px;
    }

    .current-price,
    .total-price {
        font-size: 16px;
    }

    .summary-total {
        font-size: 18px;
    }
}
</style>
