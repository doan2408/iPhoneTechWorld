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
                        <el-checkbox v-model="product.selected" :disabled="product.soLuongTon <= 0"
                            @change="handleProductSelect" />
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

                        <div v-if="product.soLuongTon <= 0" class="out-of-stock-badge">
                            <el-icon>
                                <Warning />
                            </el-icon>
                            <span> Hết hàng</span>
                        </div>
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
                            <template v-if="product.giaTruocKhuyenMai && product.giaTruocKhuyenMai !== product.gia">
                                <div class="current-price">{{ formatPrice(product.gia) }}</div>
                                <div class="old-price">{{ formatPrice(product.giaTruocKhuyenMai) }}</div>
                            </template>
                            <template v-else>
                                <div class="current-price">{{ formatPrice(product.gia) }}</div>
                            </template>
                        </div>
                    </div>

                    <div class="product-quantity">
                        <div class="label">Số lượng</div>
                        <div class="quantity-controls">
                            <el-button size="small" :disabled="product.soLuong <= 1 || product.soLuongTon <= 0"
                                @click="decreaseQuantity(product)" class="quantity-btn">
                                <el-icon>
                                    <Minus />
                                </el-icon>
                            </el-button>
                            <el-input-number v-model="product.soLuong" size="large" :controls="false" readonly
                                class="quantity-input" @change="updateQuantity(product)" />
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
                        <el-button size="small" @click="removeProduct(product.idGioHangChiTiet)" class="delete-item">
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
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from 'element-plus';
import {
    ShoppingCart, Search, ShoppingCartFull, Delete, Plus,
    Picture, Minus, ShoppingBag,
    Warning
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
import router from '@/router';
import { nextDelayClient } from '../../Service/Adminservice/KhuyenMai/KhuyenMaiSanPhamService';

const search = ref('');
const selectAll = ref(false);
const cartData = ref([]);
const user = ref(JSON.parse(localStorage.getItem("user")) || null);

const getNextUpdateDelay = async () => {
    try {
        const response = await nextDelayClient()
        const delay = Math.max(1000, response.delay || 60_000);
        return delay;
    } catch (error) {
        if (error.name !== 'AbortError') {
            console.error('Không thể lấy thời gian cập nhật:', error);
            toast.error('Không thể lấy thời gian cập nhật');
        }
        return 60_000;
    }
};

let statusUpdateInterval = null;

onMounted(async () => {
    await fetchCart();
    window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
    const initialDelay = await getNextUpdateDelay();
    statusUpdateInterval = setInterval(() => {
        fetchCart()
    }, initialDelay);
});

onUnmounted(() => {
    if (statusUpdateInterval) {
        clearInterval(statusUpdateInterval);
        statusUpdateInterval = null;
    }
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
                giaTruocKhuyenMai: item.giaTruocKhuyenMai || 0,
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
            count.value = await cartService.cartCount(user.value?.id);
        } catch (error) {
            console.error('Lỗi khi tải giỏ hàng:', error);
        }
        guiLenHeader()
        if (hetHangList.value.length > 0) {
            hetHangList.value.forEach(item => {
                ElMessage.error(`Sản phẩm "${item.tenSanPham}" đã hết hàng`)
            })
        }
    } catch (error) {
        console.error('Lỗi khi tải giỏ hàng:', error);
        ElMessage.error('Không thể tải giỏ hàng');
    }
}

const hetHangList = computed(() =>
    cartData.value.filter(item => item.soLuongTon <= 0)
)

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
        if (product.soLuong <= 0) {
            throw new Error(`Số lượng không hợp lệ. Phải từ 1 đến ${product.soLuongTon}.`);
        }
        if (product.soLuong > product.soLuongTon) {
            ElMessage.warning('Sản phẩm còn: ' + product.soLuongTon);
            product.soLuong = product.soLuongTon
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
        await fetchCart();
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

async function removeProduct(idGioHangChiTiet) {
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
            await cartService.removeItem(idGioHangChiTiet);
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
                await cartService.removeItem(item.idGioHangChiTiet);
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
    const selectedProducts = JSON.stringify(selectedItems.value);

    console.log('product', selectedProducts);

    if (route.path.startsWith('/client/')) {
        router.push({
            path: '/client/checkout-form',
            query: {
                products: encodeURIComponent(selectedProducts)
            }
        });
    } else {
        router.push({
            name: 'clientDatHang',
            query: {
                products: encodeURIComponent(selectedProducts)
            }
        });
    }
    // Implement checkout logic here
};
</script>

<style scoped>
.shopping-cart-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #0066CC 0%, #5de2e9 100%);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    position: relative;
}

.shopping-cart-container::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grid" width="20" height="20" patternUnits="userSpaceOnUse"><path d="M 20 0 L 0 0 0 20" fill="none" stroke="rgba(255,255,255,0.03)" stroke-width="1"/></pattern></defs><rect width="100" height="100" fill="url(%23grid)"/></svg>');
    pointer-events: none;
}

.cart-header {
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(20px);
    box-shadow: 0 8px 32px rgba(0, 102, 204, 0.15);
    border-bottom: 1px solid rgba(0, 102, 204, 0.1);
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
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 4px 12px rgba(255, 102, 0, 0.3);
    position: relative;
}

.brand-logo::before {
    content: '';
    position: absolute;
    inset: 2px;
    background: linear-gradient(135deg, #FF7700 0%, #FF5500 100%);
    border-radius: 10px;
    z-index: -1;
}

.logo-icon {
    font-size: 24px;
    color: white;
    filter: drop-shadow(0 2px 4px rgba(0,0,0,0.2));
}

.brand-info {
    display: flex;
    flex-direction: column;
}

.brand-title {
    font-size: 24px;
    font-weight: 700;
    background: linear-gradient(135deg, #0066CC 0%, #004499 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    margin: 0;
    line-height: 1.2;
    letter-spacing: -0.5px;
}

.search-section {
    flex: 1;
    max-width: 500px;
    margin-left: 40px;
}

.search-input :deep(.el-input__wrapper) {
    border-radius: 24px;
    border: 2px solid rgba(0, 102, 204, 0.15);
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 20px rgba(0, 102, 204, 0.08);
    height: 48px;
}

.search-input :deep(.el-input__wrapper:hover) {
    border-color: #0066CC;
    background: rgba(255, 255, 255, 1);
    box-shadow: 0 8px 32px rgba(0, 102, 204, 0.15);
    transform: translateY(-1px);
}

.search-input :deep(.el-input__wrapper.is-focus) {
    border-color: #FF6600;
    background: rgba(255, 255, 255, 1);
    box-shadow: 0 0 0 4px rgba(255, 102, 0, 0.1), 0 8px 32px rgba(0, 102, 204, 0.2);
    transform: translateY(-2px);
}

.search-icon {
    color: #0066CC;
    font-size: 16px;
}

.cart-content {
    max-width: 1400px;
    margin: 0 auto;
    padding: 24px 32px;
    position: relative;
    z-index: 1;
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
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 24px;
    box-shadow: 0 16px 48px rgba(0, 102, 204, 0.2);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.3);
}

.empty-icon {
    font-size: 48px;
    color: #0066CC;
}

.empty-title {
    font-size: 24px;
    font-weight: 600;
    color: white;
    margin: 0 0 12px 0;
    text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.empty-description {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.9);
    margin: 0 0 32px 0;
    text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.continue-shopping-btn {
    height: 48px;
    padding: 0 32px;
    border-radius: 24px;
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
    border: none;
    font-weight: 600;
    font-size: 16px;
    box-shadow: 0 4px 15px rgba(255, 102, 0, 0.4);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.continue-shopping-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(255, 102, 0, 0.6);
}

.products-container {
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(20px);
    border-radius: 16px;
    box-shadow: 0 8px 25px rgba(0, 102, 204, 0.15);
    border: 1px solid rgba(255, 255, 255, 0.3);
    overflow: hidden;
}

.product-item {
    display: grid;
    grid-template-columns: auto 120px 1fr 150px 180px 120px auto;
    gap: 20px;
    align-items: center;
    padding: 24px;
    border-bottom: 1px solid rgba(0, 102, 204, 0.08);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
}

.product-item:hover {
    background: linear-gradient(135deg, rgba(0, 102, 204, 0.02) 0%, rgba(255, 102, 0, 0.02) 100%);
    transform: translateY(-1px);
}

.product-item.selected {
    background: linear-gradient(135deg, rgba(0, 102, 204, 0.08) 0%, rgba(255, 102, 0, 0.04) 100%);
    border-left: 4px solid #FF6600;
    box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

.product-item:last-child {
    border-bottom: none;
}

.product-selection {
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-selection :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    background-color: #FF6600;
    border-color: #FF6600;
}

.product-image {
    position: relative;
}

.product-img {
    width: 100px;
    height: 100px;
    border-radius: 12px;
    border: 2px solid rgba(0, 102, 204, 0.1);
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 4px 12px rgba(0, 102, 204, 0.1);
}

.product-img:hover {
    transform: scale(1.05) translateY(-2px);
    box-shadow: 0 8px 25px rgba(0, 102, 204, 0.25);
    border-color: #FF6600;
}

.image-error {
    width: 100px;
    height: 100px;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #0066CC;
    border: 2px solid rgba(0, 102, 204, 0.1);
}

.product-info {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.product-name {
    font-size: 16px;
    font-weight: 500;
    color: #1a365d;
    line-height: 1.4;
    letter-spacing: -0.2px;
}

.product-variant {
    margin: 4px 0;
}

.product-variant :deep(.el-tag) {
    background: linear-gradient(135deg, #0066CC 0%, #004499 100%);
    color: white;
    border: none;
    border-radius: 8px;
    padding: 2px 8px;
    font-weight: 500;
    font-size: 12px;
}

.product-features {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
}

.feature-tag {
    font-size: 11px;
    color: #ff0000;
    background: linear-gradient(135deg, rgba(255, 102, 0, 0.1) 0%, rgba(255, 102, 0, 0.05) 100%);
    padding: 2px 6px;
    border-radius: 4px;
    border: 1px solid rgba(255, 102, 0, 0.2);
    font-weight: 500;
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
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.old-price {
    text-decoration: line-through;
    color: #94a3b8;
    font-size: 14px;
    font-weight: 500;
}

.out-of-stock-badge {
    position: absolute;
    top: 8px;
    right: 8px;
    background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
    color: white;
    font-size: 10px;
    padding: 3px 6px;
    border-radius: 6px;
    font-weight: 600;
    box-shadow: 0 2px 8px rgba(239, 68, 68, 0.3);
    display: flex;
    align-items: center;
    gap: 2px;
}

.product-quantity {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
}

.label {
    font-size: 14px;
    color: #64748b;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 0.5px;
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
    border: 2px solid rgba(0, 102, 204, 0.15);
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    color: #0066CC;
    font-weight: 600;
}

.quantity-btn:hover:not(:disabled) {
    border-color: #FF6600;
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
    color: white;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(255, 102, 0, 0.3);
}

.quantity-btn:disabled {
    opacity: 0.4;
    cursor: not-allowed;
}

.quantity-input {
    width: 60px;
}

.quantity-input :deep(.el-input__wrapper) {
    border-radius: 8px;
    border: 2px solid rgba(0, 102, 204, 0.15);
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%);
    text-align: center;
    padding: 4px 4px;
    justify-content: center;
    font-weight: 600;
    color: #1a365d;
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
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
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
    border: 2px solid rgba(239, 68, 68, 0.15);
    color: #ef4444;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.delete-item:hover {
    color: white;
    background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
    border-color: #ef4444;
    transform: translateY(-2px);
    box-shadow: 0 8px 20px rgba(239, 68, 68, 0.3);
}

.checkout-section {
    position: sticky;
    bottom: 0;
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(20px);
    border-top: 1px solid rgba(0, 102, 204, 0.1);
    box-shadow: 0 -8px 32px rgba(0, 102, 204, 0.15);
    z-index: 50;
    width: 100%;
    padding: 20px 32px;
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

.checkout-left :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    background-color: #0066CC;
    border-color: #0066CC;
}

.checkout-left :deep(.el-button--danger.is-text) {
    color: #ef4444;
    font-weight: 600;
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
    gap: 32px;
}

.summary-label {
    font-size: 16px;
    color: #64748b;
    font-weight: 500;
}

.summary-total {
    font-size: 24px;
    font-weight: 800;
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.checkout-btn {
    height: 56px;
    padding: 0 40px;
    border-radius: 28px;
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
    border: none;
    font-weight: 700;
    font-size: 16px;
    box-shadow: 0 8px 24px rgba(255, 102, 0, 0.4);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.checkout-btn::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0.1) 100%);
    opacity: 0;
    transition: opacity 0.3s ease;
}

.checkout-btn:hover:not(:disabled) {
    transform: translateY(-3px);
    box-shadow: 0 16px 48px rgba(255, 102, 0, 0.6);
}

.checkout-btn:hover:not(:disabled)::before {
    opacity: 1;
}

.checkout-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    transform: none;
    box-shadow: 0 4px 12px rgba(255, 102, 0, 0.2);
}

/* Responsive Design */
@media (max-width: 1200px) {
    .header-content {
        padding: 20px 32px;
    }

    .cart-content {
        padding: 24px 32px;
    }

    .checkout-container {
        padding: 20px 32px;
    }
}

@media (max-width: 768px) {
    .header-content {
        flex-direction: column;
        gap: 20px;
        padding: 20px 24px;
    }

    .search-section {
        width: 100%;
        margin-left: 0;
        max-width: none;
    }

    .cart-content {
        padding: 20px 16px;
    }

    .products-container {
        border-radius: 16px;
    }

    .product-item {
        grid-template-columns: auto 80px 1fr;
        gap: 16px;
        padding: 20px 16px;
    }

    .product-price,
    .product-quantity,
    .product-total,
    .product-actions {
        grid-column: 1 / -1;
        justify-self: stretch;
        margin-top: 16px;
    }

    .product-quantity,
    .product-total {
        flex-direction: row;
        justify-content: space-between;
    }

    .checkout-container {
        flex-direction: column;
        gap: 20px;
        padding: 20px 16px;
    }

    .checkout-right {
        width: 100%;
        justify-content: space-between;
    }

    .empty-cart {
        padding: 80px 20px;
    }

    .empty-illustration {
        width: 120px;
        height: 120px;
    }

    .empty-icon {
        font-size: 48px;
    }

    .empty-title {
        font-size: 24px;
    }
}

@media (max-width: 480px) {
    .brand-title {
        font-size: 22px;
    }

    .product-img {
        width: 80px;
        height: 80px;
        border-radius: 12px;
    }

    .image-error {
        width: 80px;
        height: 80px;
        border-radius: 12px;
    }

    .product-name {
        font-size: 16px;
    }

    .current-price,
    .total-price {
        font-size: 18px;
    }

    .summary-total {
        font-size: 20px;
    }

    .checkout-btn {
        height: 48px;
        font-size: 14px;
        padding: 0 28px;
    }
}

/* Animation keyframes */
@keyframes float {
    0%, 100% {
        transform: translateY(0px);
    }
    50% {
        transform: translateY(-5px);
    }
}

.brand-logo {
    animation: float 3s ease-in-out infinite;
}

/* Smooth scrollbar */
::-webkit-scrollbar {
    width: 8px;
}

::-webkit-scrollbar-track {
    background: rgba(0, 102, 204, 0.1);
    border-radius: 4px;
}

::-webkit-scrollbar-thumb {
    background: linear-gradient(135deg, #0066CC 0%, #004499 100%);
    border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(135deg, #FF6600 0%, #FF4400 100%);
}
</style>