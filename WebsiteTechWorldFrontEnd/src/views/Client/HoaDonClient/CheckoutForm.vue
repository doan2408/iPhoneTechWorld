<template>
    <div class="container">
        <div class="checkout-card">
            <h1 class="title">Thanh Toán</h1>

            <!-- Địa chỉ nhận hàng -->
            <div class="section">
                <h2 class="section-title">Địa chỉ nhận hàng</h2>
                <div class="current-address-display">
                    <p class="address-name-display">{{ shippingAddress.tenNguoiNhan }}</p>
                    <p class="address-phone-display">{{ shippingAddress.sdtNguoiNhan }}</p>
                    <p class="address-detail-display">{{ shippingAddress.soNha + ', ' + shippingAddress.tenDuong + ', '
                        + shippingAddress.xaPhuong
                        + ', ' + shippingAddress.quanHuyen + ', ' + shippingAddress.tinhThanhPho }}</p>
                    <button @click="openAddressModal" class="change-button">Thay đổi địa chỉ</button>
                </div>
            </div>

            <!-- Sản phẩm -->
            <div class="section">
                <h2 class="section-title">Sản phẩm</h2>
                <div class="product-item-wrapper" v-for="product in product" :key="product.idGioHangChiTiet">
                    <div class="product-item">
                        <input type="checkbox" class="product-checkbox" checked disabled />
                        <div class="product-image-container">
                            <img :src="product.imageUrl" :alt="product.tenSanPham" class="product-image" />
                        </div>
                        <div class="product-info">
                            <div class="product-name">{{ product.tenSanPham }}</div>
                            <div class="product-type">{{ product.phienBan }}</div>
                        </div>
                        <div class="product-pricing">
                            <div class="product-price">₫{{ product.gia.toLocaleString() }}</div>
                            <div class="quantity-control">
                                <button class="quantity-button">-</button>
                                <input type="text" :value="product.soLuong" class="quantity-input" readonly />
                                <button class="quantity-button">+</button>
                            </div>
                            <div class="product-subtotal">
                                ₫{{ (product.gia * product.soLuong).toLocaleString() }}
                            </div>
                            <button class="delete-button">
                                <!-- icon trash -->
                            </button>
                        </div>
                    </div>
                </div>


                <div class="separator"></div>

                <div class="insurance-item">
                    <input type="checkbox" id="fashion-insurance" v-model="hasInsurance" class="checkbox-field" />
                    <div class="insurance-details">
                        <label for="fashion-insurance" class="insurance-label">
                            {{ insurance.name }} <span class="new-tag">Mới</span>
                        </label>
                        <p class="insurance-description">
                            {{ insurance.description }}
                            <a href="#" class="learn-more-link">Tìm hiểu thêm</a>
                        </p>
                    </div>
                    <div class="insurance-pricing">
                        <div class="insurance-price">₫{{ insurance.gia }}</div>
                        <div class="insurance-quantity">x{{ insurance.quantity }}</div>
                        <div class="insurance-total">
                            ₫{{ (insurance.gia * insurance.quantity) }}
                        </div>
                    </div>
                </div>
            </div>

            <!-- Phương thức vận chuyển -->
            <div class="section">
                <h2 class="section-title">Phương thức vận chuyển</h2>
                <div class="radio-group">
                    <label class="radio-option">
                        <div class="radio-content">
                            <input type="radio" name="shipping-method" value="standard" v-model="selectedShippingMethod"
                                class="radio-field" />
                            <span>Vận chuyển tiêu chuẩn</span>
                        </div>
                        <span class="shipping-cost">₫25.000</span>
                    </label>
                    <label class="radio-option">
                        <div class="radio-content">
                            <input type="radio" name="shipping-method" value="express" v-model="selectedShippingMethod"
                                class="radio-field" />
                            <span>Vận chuyển nhanh</span>
                        </div>
                        <span class="shipping-cost">₫40.000</span>
                    </label>
                </div>
            </div>

            <!-- Mã giảm giá / Voucher -->
            <div class="section">
                <h2 class="section-title">Mã giảm giá / Voucher</h2>
                <div class="voucher-display-area">
                    <div v-if="appliedVoucher.code" class="applied-voucher-info">
                        <span class="applied-voucher-text">Mã đã áp dụng: <strong>{{ appliedVoucher.code
                                }}</strong></span>
                        <span class="applied-voucher-discount">- ₫{{ appliedVoucher.discount.toLocaleString() }}</span>
                    </div>
                    <div v-else class="no-voucher-text">Chưa có mã giảm giá nào được áp dụng.</div>
                    <button @click="openVoucherModal" class="change-button">Áp dụng mã giảm giá</button>
                </div>
            </div>

            <!-- Áp dụng điểm -->
            <div class="section">
                <h2 class="section-title">Áp dụng điểm</h2>
                <div class="points-input-group">
                    <input v-model="pointsToApply" type="number" placeholder="Nhập số điểm muốn áp dụng"
                        class="input-field flex-grow" />
                    <button class="apply-button">Áp dụng</button>
                </div>
            </div>

            <!-- Phương thức thanh toán -->
            <div class="section">
                <h2 class="section-title">Phương thức thanh toán</h2>
                <div class="payment-methods">
                    <label class="payment-method-option" v-for="method in paymentMethods" :key="method.code">
                        <input type="radio" name="paymentMethod" :value="method.code" v-model="selectedPaymentMethod">
                        <img :src="getIconUrl(method.code)" :alt="method.displayName" class="payment-icon">
                        <span>{{ method.displayName }}</span>
                    </label>
                </div>
            </div>

            <!-- Tổng cộng -->
            <div class="summary-section">
                <h2 class="section-title">Tổng cộng</h2>
                <div class="summary-details">
                    <!-- <div class="summary-row">
                        <span>Tổng tiền sản phẩm:</span>
                        <span class="summary-value">₫{{ product.gia }}</span>
                    </div>
                    <div v-if="hasInsurance" class="summary-row">
                        <span>Bảo hiểm:</span>
                        <span class="summary-value">₫{{ insurance.gia }}</span>
                    </div> -->
                    <div class="summary-row">
                        <span>Phí vận chuyển:</span>
                        <span class="summary-value">₫{{ getShippingCost.toLocaleString() }}</span>
                    </div>
                    <div v-if="appliedVoucher.code" class="summary-row voucher-discount-row">
                        <span>Giảm giá Voucher ({{ appliedVoucher.code }}):</span>
                        <span class="summary-value">- ₫{{ appliedVoucher.discount.toLocaleString() }}</span>
                    </div>
                    <div class="separator my-2"></div>
                    <div class="summary-row total-row">
                        <span>Tổng thanh toán:</span>
                        <span class="summary-value">₫{{ calculateTotal.toLocaleString() }}</span>
                    </div>
                </div>
                <button @click="handleBuy" class="buy-button">
                    Mua hàng
                </button>
            </div>
        </div>

        <!-- Address Selection Modal (Inline) -->
        <transition name="modal-fade">
            <div v-if="isAddressModalOpen" class="modal-overlay" @click.self="closeAddressModal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title">Chọn Địa chỉ giao hàng</h3>
                        <button class="modal-close-button" @click="closeAddressModal">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                stroke-linejoin="round" class="lucide lucide-x">
                                <path d="M18 6 6 18" />
                                <path d="m6 6 12 12" />
                            </svg>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="address-modal-content">
                            <div class="radio-group address-modal-options">
                                <label v-for="address in userAddresses" :key="address.id"
                                    class="radio-option address-option">
                                    <div class="radio-content">
                                        <input type="radio" name="modal-shipping-address" :value="address.id"
                                            v-model="modalSelectedAddressId" class="radio-field" />
                                        <div class="address-info">
                                            <span class="address-name">{{ address.tenNguoiNhan+ '-' }}</span>
                                            <span class="address-phone">{{ address.sdtNguoiNhan+ '-' }}</span>
                                            <span class="address-detail">{{ address.soNha + ', ' + address.tenDuong + ','+ address.xaPhuong
                                                + ', ' + address.quanHuyen + ', ' + address.tinhThanhPho }}</span>
                                        </div>
                                    </div>
                                </label>
                                <label class="radio-option address-option">
                                    <div class="radio-content">
                                        <input type="radio" name="modal-shipping-address" value="new"
                                            v-model="modalSelectedAddressId" class="radio-field" />
                                        <span class="address-name">Thêm địa chỉ mới</span>
                                    </div>
                                </label>
                            </div>

                            <div v-if="modalSelectedAddressId === 'new'"
                                class="form-grid address-form-fields modal-new-address-form">
                                <div>
                                    <label for="modal-name" class="label">Họ và tên</label>
                                    <input id="modal-name" v-model="modalNewAddress.name" type="text"
                                        placeholder="Nhập họ và tên" class="input-field" />
                                </div>
                                <div>
                                    <label for="modal-phone" class="label">Số điện thoại</label>
                                    <input id="modal-phone" v-model="modalNewAddress.phone" type="tel"
                                        placeholder="Nhập số điện thoại" class="input-field" />
                                </div>
                                <div>
                                    <label for="modal-address" class="label">Địa chỉ</label>
                                    <input id="modal-address" v-model="modalNewAddress.address" type="text"
                                        placeholder="Nhập địa chỉ chi tiết" class="input-field" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button @click="confirmAddressSelection" class="apply-button">Xác nhận</button>
                        <button @click="closeAddressModal" class="cancel-button">Hủy</button>
                    </div>
                </div>
            </div>
        </transition>

        <!-- Voucher Application Modal (Inline) -->
        <transition name="modal-fade">
            <div v-if="isVoucherModalOpen" class="modal-overlay" @click.self="closeVoucherModal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title">Áp dụng mã giảm giá</h3>
                        <button class="modal-close-button" @click="closeVoucherModal">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                stroke-linejoin="round" class="lucide lucide-x">
                                <path d="M18 6 6 18" />
                                <path d="m6 6 12 12" />
                            </svg>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="voucher-modal-content">
                            <div class="voucher-input-group">
                                <input v-model="modalVoucherCode" type="text" placeholder="Nhập mã giảm giá"
                                    class="input-field flex-grow" />
                                <button @click="handleApplyVoucherInModal" class="apply-button">Áp dụng</button>
                            </div>
                            <div v-if="modalAppliedVoucher.code" class="applied-voucher-info">
                                <span class="applied-voucher-text">Mã đã áp dụng: <strong>{{ modalAppliedVoucher.code
                                        }}</strong></span>
                                <span class="applied-voucher-discount">- ₫{{
                                    modalAppliedVoucher.discount.toLocaleString() }}</span>
                            </div>
                            <div v-if="modalVoucherError" class="voucher-error">{{ modalVoucherError }}</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button @click="closeVoucherModal" class="apply-button">Đóng</button>
                    </div>
                </div>
            </div>
        </transition>
    </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { getDiaChiByClient } from '@/Service/ClientService/TaiKhoan/DiaChiServices'
import { useRoute } from 'vue-router';
import { loadPaymentMethod } from "@/Service/ClientService/HoaDon/MyOrderClient";

const route = useRoute();

// --- Address Management ---
const userAddresses = ref([])
onMounted(async () => {
    try {
        const response = await getDiaChiByClient();
        const data = response.data;

        if (Array.isArray(data) && data.length > 0) {
            userAddresses.value = data;
            selectedAddressId.value = data[0].id; 
            console.log('Dữ liệu địa chỉ trả về:', data);
        } else {
            userAddresses.value = [];
            selectedAddressId.value = 'new'; 
        }
    } catch (error) {
        console.error('Lỗi khi tải địa chỉ:', error);
    }
});

const isAddressModalOpen = ref(false)
const selectedAddressId = ref(userAddresses.value.length > 0 ? userAddresses.value[0].id : 'new')
const modalSelectedAddressId = ref(selectedAddressId.value) // State for modal's radio selection

const newAddress = ref({ name: '', phone: '', address: '' })
const modalNewAddress = ref({ name: '', phone: '', address: '' }) // State for modal's new address form

const shippingAddress = ref({ // This holds the final address for checkout
    name: '',
    phone: '',
    address: '',
})

// Initialize shippingAddress based on initial selectedAddressId
watch(selectedAddressId, (newVal) => {
    if (newVal === 'new') {
        shippingAddress.value = { name: '', phone: '', address: '' }
    } else {
        const selected = userAddresses.value.find(addr => addr.id === newVal)
        if (selected) {
            shippingAddress.value = { ...selected }
        }
    }
}, { immediate: true })

// Open Address Modal and sync its state
const openAddressModal = () => {
    modalSelectedAddressId.value = selectedAddressId.value;
    if (selectedAddressId.value === 'new') {
        modalNewAddress.value = { ...newAddress.value }; // Copy current new address data
    } else {
        modalNewAddress.value = { name: '', phone: '', address: '' }; // Clear new address form if existing is selected
    }
    isAddressModalOpen.value = true;
};

const closeAddressModal = () => {
    isAddressModalOpen.value = false;
};

const confirmAddressSelection = () => {
    if (modalSelectedAddressId.value === 'new') {
        if (modalNewAddress.value.name && modalNewAddress.value.phone && modalNewAddress.value.address) {
            const newId = (userAddresses.value.length + 1).toString();
            const savedAddress = { id: newId, ...modalNewAddress.value };
            userAddresses.value.push(savedAddress);
            selectedAddressId.value = newId; // Update main form's selected ID
            shippingAddress.value = { ...savedAddress }; // Update main form's shipping address
            modalNewAddress.value = { name: '', phone: '', address: '' }; // Clear modal's new address fields
            closeAddressModal();
            alert('Địa chỉ mới đã được lưu và chọn!');
        } else {
            alert('Vui lòng điền đầy đủ thông tin địa chỉ mới.');
        }
    } else {
        selectedAddressId.value = modalSelectedAddressId.value; // Update main form's selected ID
        const selected = userAddresses.value.find(addr => addr.id === selectedAddressId.value);
        if (selected) {
            shippingAddress.value = { ...selected }; // Update main form's shipping address
        }
        closeAddressModal();
    }
}

// --- Voucher Management ---
const isVoucherModalOpen = ref(false)
const voucherCode = ref(''); // Main form's voucher input (not used directly now)
const modalVoucherCode = ref(''); // Voucher input inside the modal
const appliedVoucher = ref({ code: '', discount: 0 }); // Main form's applied voucher
const modalAppliedVoucher = ref({ code: '', discount: 0 }); // Voucher applied inside modal (for display)
const voucherError = ref(''); // Main form's error
const modalVoucherError = ref(''); // Error inside the modal

// Open Voucher Modal and sync its state
const openVoucherModal = () => {
    modalVoucherCode.value = voucherCode.value; // Sync current voucher code
    modalAppliedVoucher.value = { ...appliedVoucher.value }; // Sync currently applied voucher
    modalVoucherError.value = ''; // Clear errors on open
    isVoucherModalOpen.value = true;
};

const closeVoucherModal = () => {
    isVoucherModalOpen.value = false;
};

const handleApplyVoucherInModal = () => {
    modalVoucherError.value = '';
    modalAppliedVoucher.value = { code: '', discount: 0 };

    const code = modalVoucherCode.value.trim().toUpperCase();

    if (code === 'FREESHIP') {
        modalAppliedVoucher.value = { code: 'FREESHIP', discount: getShippingCost.value };
        alert('Mã giảm giá FREESHIP đã được áp dụng! Miễn phí vận chuyển.');
    } else if (code === 'SALE50') {
        modalAppliedVoucher.value = { code: 'SALE50', discount: 50000 };
        alert('Mã giảm giá SALE50 đã được áp dụng! Giảm 50.000₫.');
    } else if (code) {
        modalVoucherError.value = 'Mã giảm giá không hợp lệ.';
    } else {
        modalVoucherError.value = 'Vui lòng nhập mã giảm giá.';
    }
    // Update the main form's applied voucher state
    appliedVoucher.value = { ...modalAppliedVoucher.value };
    voucherCode.value = modalVoucherCode.value; // Keep main form's input synced
};


// --- Other Checkout Data ---
const selectedShippingMethod = ref('standard')
const pointsToApply = ref('')
const hasInsurance = ref(false)

const product = ref([]);

onMounted(() => {
    if (route.query.products) {
        try {
            const parsed = JSON.parse(decodeURIComponent(route.query.products));
            product.value = Array.isArray(parsed) ? parsed : [parsed];
        } catch (e) {
            console.error('Lỗi parse dữ liệu sản phẩm:', e);
        }
    }
});

const insurance = ref({
    id: 'fashion-insurance',
    name: 'Bảo hiểm Thời trang',
    description:
        'Bảo vệ sản phẩm được bảo hiểm khỏi thiệt hại do sự cố bất ngờ, tiếp xúc với chất lỏng hoặc hư hỏng trong quá trình sử dụng.',
    price: 1199,
    quantity: 1,
})

const getShippingCost = computed(() => {
    switch (selectedShippingMethod.value) {
        case 'standard':
            return 25000
        case 'express':
            return 40000
        default:
            return 0
    }
})

const calculateSubtotal = computed(() => {
    let subtotal = product.value.price * product.value.quantity
    if (hasInsurance.value) {
        subtotal += insurance.value.price * insurance.value.quantity
    }
    return subtotal
})

const calculateTotal = computed(() => {
    let total = calculateSubtotal.value + getShippingCost.value;
    // Apply voucher discount
    total -= appliedVoucher.value.discount;
    return Math.max(0, total); // Ensure total doesn't go below zero
})


const handleBuy = () => {
    const diaChiGiaoHang = [
        shippingAddress.value.soNha,
        shippingAddress.value.tenDuong,
        shippingAddress.value.xaPhuong,
        shippingAddress.value.quanHuyen,
        shippingAddress.value.tinhThanhPho
    ].filter(Boolean).join(', ');
    
    if (!shippingAddress.value.tenNguoiNhan || !shippingAddress.value.sdtNguoiNhan || !diaChiGiaoHang) {
        alert('Vui lòng chọn hoặc nhập địa chỉ giao hàng.');
        return;
    }
    console.log('Purchase details:', {
        shippingAddress: shippingAddress.value,
        product: product.value,
        hasInsurance: hasInsurance.value,
        selectedShippingMethod: selectedShippingMethod.value,
        selectedPaymentMethod: selectedPaymentMethod.value,
        pointsToApply: pointsToApply.value,
        appliedVoucher: appliedVoucher.value,
        total: calculateTotal.value,
    })
    alert('Mua hàng thành công! (Simulated)');
}

const paymentMethods = ref([]);
const selectedPaymentMethod = ref(null);
const agreedToTerms = ref(false);

const fetchPaymentMethods = async () => {
    console.log(selectedPaymentMethod.value);
    try {
        const response = await loadPaymentMethod();
        paymentMethods.value = response.data;
        if (paymentMethods.value.length > 0) {
            selectedPaymentMethod.value = paymentMethods.value[0].code; // Chọn mặc định cái đầu tiên
        }
    } catch (error) {
        console.error('Lỗi khi tải phương thức thanh toán:', error);
        toast.error('Không thể tải các phương thức thanh toán. Vui lòng thử lại sau.');
    }
};
const getIconUrl = (code) => {
    switch (code) {
        case 'TIEN_MAT':
            return '/icons/cod.png'; // Đảm bảo file cod.png có trong public/icons
        case 'NGAN_HANG':
            return '/icons/bank.png'; // Đảm bảo file bank.png có trong public/icons
        default:
            return '/icons/default.png'; // Icon mặc định
    }
};
onMounted(async () => {
    fetchPaymentMethods();
})
</script>

<style scoped src="@/style/HoaDon/CheckoutForm.css">

</style>