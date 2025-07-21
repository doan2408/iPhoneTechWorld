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

        <div class="cart-content" :style="cartData.length < 3 ? { marginBottom: '310px' } : {}">
            <div v-if="cartData.length === 0 || totalItems === 0" class="empty-cart">
                <div class="empty-illustration">
                    <el-icon class="empty-icon">
                        <ShoppingCartFull />
                    </el-icon>
                </div>
                <h3 class="empty-title">Giỏ hàng trống</h3>
                <p class="empty-description">Hãy thêm sản phẩm vào giỏ hàng để bắt đầu mua sắm</p>
                <router-link :to="{ path: user && user.id ? '/client/home' : '/' }">
                    <el-button type="primary" size="large" class="continue-shopping-btn">
                        <el-icon>
                            <Plus />
                        </el-icon>
                        Tiếp tục mua sắm
                    </el-button>
                </router-link>
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
                            <el-input-number v-model="product.soLuong" :min="1" :max="product.soLuongTon" size="large"
                                :controls="false" readonly class="quantity-input" @change="updateQuantity(product)" />
                            <el-button size="small" :disabled="product.soLuong >= product.soLuongTon"
                                @click="increaseQuantity(product)" class="quantity-btn">
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
import { useStore } from 'vuex';
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from 'element-plus';
import {
    ShoppingCart, Search, ShoppingCartFull, Delete, Plus,
    Picture, Minus, ShoppingBag
} from '@element-plus/icons-vue';
import { cartService } from '@/service/ClientService/GioHang/GioHangClientService';
import { CartService } from '@/Service/ClientService/GioHang/CartService';
import { createStore } from 'vuex';
import headerState from '@/components/Client/modules/headerState';

const count = ref(0)
const store = useStore()

if (!store.hasModule('headerState')) {
    store.registerModule('headerState', headerState)
}

const route = useRoute();
const selectedId = route.query.selected;

const search = ref('');
const selectAll = ref(false);
const cartData = ref([]);
const user = ref(JSON.parse(localStorage.getItem("user")) || null);

onMounted(async () => {
    await fetchCart();
    window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
});

const guiLenHeader = () => {
    store.commit('headerState/setCartItemCount', count.value)
}

async function fetchCart() {
    cartData.value = [];
    try {
        if (user.value?.id) {
            const response = await cartService.getCart(user.value.id);
            cartData.value = response.items.map(item => ({
                idGioHangChiTiet: item.idGioHangChiTiet,
                idSanPhamChiTiet: item.idSanPhamChiTiet,
                tenSanPham: item.tenSanPham || 'Sản phẩm',
                phienBan: item.phienBan || 'Mặc định',
                imageUrl: item.imageUrl || '',
                gia: item.gia || 0,
                soLuong: item.soLuong || 1,
                soLuongTon: item.soLuongTon || 0,
                ngayThem: item.ngayThem || new Date().toISOString(),
                selected: false
            }));
        } else {
            const storedCart = localStorage.getItem('shoppingCart');
            if (storedCart) {
                const currentTime = new Date();
                let localCart = JSON.parse(storedCart);

                localCart = localCart.filter(item => {
                    const addedTime = new Date(item.ngayThem);
                    const timeDiff = (currentTime - addedTime) / (1000 * 60);
                    return timeDiff <= 30;
                });
                localStorage.setItem('shoppingCart', JSON.stringify(localCart));

                cartData.value = localCart.map(item => ({
                    idGioHangChiTiet: item.idGioHangChiTiet || null,
                    idSanPhamChiTiet: item.idSanPhamChiTiet,
                    tenSanPham: item.tenSanPham || 'Sản phẩm',
                    phienBan: item.phienBan || 'Mặc định',
                    imageUrl: item.imageUrl || '',
                    gia: item.gia || 0,
                    soLuong: item.soLuong || 1,
                    soLuongTon: item.soLuongTon || 0,
                    ngayThem: item.ngayThem || new Date().toISOString(),
                    selected: false
                }));
            }
        }
        if (selectedId) {
            cartData.value = cartData.value.map(item => ({
                ...item,
                selected: item.idSanPhamChiTiet == selectedId
            }));
        }
        try {
            count.value = await cartService.cartCount(user.value.id);
        } catch (error) {
            console.error('Lỗi khi tải giỏ hàng:', error);
        }
        guiLenHeader()
    } catch (error) {
        console.error('Lỗi khi tải giỏ hàng:', error);
        ElMessage.error('Không thể tải giỏ hàng');
    }
}

const filteredCartData = computed(() => {
    let result = cartData.value;

    if (search.value) {
        result = result.filter(product =>
            product.tenSanPham.toLowerCase().includes(search.value.toLowerCase()) ||
            product.phienBan.toLowerCase().includes(search.value.toLowerCase())
        );
    }
    return result.sort((a, b) => new Date(b.ngayThem) - new Date(a.ngayThem));
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

async function updateQuantity(product) {
    try {
        if (product.soLuong <= 0 || product.soLuong > product.soLuongTon) {
            throw new Error(`Số lượng không hợp lệ. Phải từ 1 đến ${product.soLuongTon}.`);
        }
        if (user.value?.id) {
            await cartService.updateQuantity(product.idGioHangChiTiet, product.soLuong);
        } else {
            const success = CartService.capNhatSoLuong(product.idSanPhamChiTiet, product.soLuong);
        }
        ElMessage.success('Cập nhật số lượng thành công');
    } catch (error) {
        console.error('Lỗi khi cập nhật số lượng:', error);
        ElMessage.error(error.message || 'Không thể cập nhật số lượng');
        await fetchCart(); // Tải lại giỏ hàng để đảm bảo dữ liệu nhất quán
    }
}

async function decreaseQuantity(product) {
    if (product.soLuong <= 1) return;
    product.soLuong--;
    await updateQuantity(product);
}

async function increaseQuantity(product) {
    if (product.soLuong >= product.soLuongTon) return;
    product.soLuong++;
    await updateQuantity(product);
}

async function removeProduct(idSanPhamChiTiet) {
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
        if (user.value?.id) {
            await cartService.removeItem(idSanPhamChiTiet);
        } else {
            const success = CartService.xoaSanPhamKhoiGio(idSanPhamChiTiet)
        }
        await fetchCart();
        ElMessage.success('Đã xóa sản phẩm khỏi giỏ hàng');
    } catch (error) {
        console.error('Lỗi khi xóa sản phẩm:', error);
        ElMessage.info('Đã hủy thao tác');
    }
}

async function handleBulkDelete() {
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
        if (user.value?.id) {
            for (const item of selectedItems.value) {
                await cartService.removeItem(item.idSanPhamChiTiet);
            }
        } else {
            for (const item of selectedItems.value) {
                const success = CartService.xoaSanPhamKhoiGio(item.idSanPhamChiTiet);
            }
        }
        await fetchCart();
        ElMessage.success('Đã xóa các sản phẩm đã chọn');
    } catch (error) {
        console.error('Lỗi khi xóa hàng loạt:', error);
        ElMessage.info('Đã hủy thao tác');
    }
}

const handleSelectAll = (value) => {
    cartData.value.forEach(product => {
        product.selected = value;
    });
    selectAll.value = value;
};

const handleProductSelect = () => {
    const allSelected = cartData.value.length > 0 && cartData.value.every(product => product.selected);
    selectAll.value = allSelected;
};

const handleCheckout = () => {
    if (selectedItems.value.length === 0) {
        ElMessage.warning('Vui lòng chọn ít nhất một sản phẩm để thanh toán');
        return;
    }
    ElMessage.success(`Đang chuyển đến trang thanh toán với ${selectedItems.value.length} sản phẩm`);
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

.quantity-input {
    width: 60px;
}

.quantity-input :deep(.el-input__wrapper) {
    border-radius: 8px;
    text-align: center;
    padding: 4px 4px;
    justify-content: center;
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

.checkout-section {
    position: sticky;
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

@media (max-width: 1200px) {
    .header-content {
        padding: 16px 24px;
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