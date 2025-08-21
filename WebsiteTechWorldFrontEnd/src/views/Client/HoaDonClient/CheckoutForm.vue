<template>
    <div class="container">
        <div class="checkout-card">
            <h1 class="title">Thanh Toán</h1>

            <!-- Địa chỉ nhận hàng -->
            <div class="section">
                <h2 class="section-title">Địa chỉ nhận hàng</h2>
                <div class="current-address-display" v-if="hasShippingInfo">
                    <p class="address-name-display"><span style="color: blue;">Họ tên:</span> {{
                        shippingAddress.tenNguoiNhan || 'Chưa có' }}</p>
                    <p class="address-phone-display"><span style="color: blue;">Số điện thoại:</span> {{
                        shippingAddress.sdtNguoiNhan || 'Chưa có' }}</p>
                    <p class="address-phone-display"><span style="color: blue;">Email:</span> {{
                        shippingAddress.emailNguoiNhan || 'Chưa có' }}</p>
                    <p class="address-detail-display"><span style="color: blue;">Địa chỉ nhận hàng:</span> {{
                        shippingAddress.soNha + ', ' +
                        shippingAddress.tenDuong + ', '
                        + shippingAddress.xaPhuong + ', ' + shippingAddress.tinhThanhPho || 'Chưa có' }}</p>
                    <button @click="openAddressModal" class="change-button">Thay đổi địa chỉ</button>
                </div>
                <div v-else class="no-address">
                    <p style="color: red;">Chưa cập nhật thông tin giao hàng</p>
                    <button @click="openAddressModal" class="change-button">Thêm địa chỉ</button>
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
                        <span class="shipping-cost">
                            đ{{ formatCurrency(phiShipTieuChuan) }}
                        </span>
                    </label>
                    <label class="radio-option">
                        <div class="radio-content">
                            <input type="radio" name="shipping-method" value="express" v-model="selectedShippingMethod"
                                class="radio-field" />
                            <span>Vận chuyển nhanh</span>
                        </div>
                        <span class="shipping-cost">
                            ₫{{ formatCurrency(phishipDisplay) }}
                        </span>
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
                    <div class="summary-row" v-for="item in product" :key="item.id">
                        <span>Tổng tiền sản phẩm:</span>
                        <span class="summary-value">₫{{ (item.gia * item.soLuong).toLocaleString() }}</span>
                    </div>
                    <!--
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
                    <span v-if="isLoading">
                        <i class="fas fa-spinner fa-spin"></i> Đang xử lý...
                    </span>
                    <span v-else>
                        Mua hàng
                    </span>
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
                                            <span class="address-name">{{ address.tenNguoiNhan + '-' }}</span>
                                            <span class="address-phone">{{ address.sdtNguoiNhan + '-' }}</span>
                                            <span class="address-detail">{{ address.soNha + ', ' + address.tenDuong +
                                                ',' + address.xaPhuong
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
                                    <span v-if="errors.name" class="error-text">{{ errors.name }}</span>
                                </div>
                                <div>
                                    <label for="modal-phone" class="label">Số điện thoại</label>
                                    <input id="modal-phone" v-model="modalNewAddress.phone" type="tel"
                                        placeholder="Nhập số điện thoại" class="input-field" />
                                    <span v-if="errors.phone" class="error-text">{{ errors.phone }}</span>
                                </div>
                                <div>
                                    <label for="modal-address" class="label">Địa chỉ</label>
                                    <input id="modal-address" v-model="modalNewAddress.address" type="text"
                                        placeholder="Nhập địa chỉ chi tiết" class="input-field" />
                                    <span v-if="errors.address" class="error-text">{{ errors.address }}</span>
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
                                <input v-if="discountList?.length" v-model="modalVoucherCode" type="text"
                                    placeholder="Nhập mã giảm giá" class="input-field flex-grow" />
                            </div>
                            <ul class="voucher-list">
                                <p v-if="!discountList?.length" style="text-align: center; font-style: italic;">
                                    Khách hàng không có giảm giá nào
                                </p>
                                <li v-for="discount in discountList" :key="discount.id" class="voucher-item">
                                    <div>
                                        <strong>{{ discount.tenGiamGia }}</strong>
                                        <small>Giảm: {{ discount.giaTriGiamGia }}%</small>
                                    </div>
                                    <button @click="applyDiscount(discount)" class="apply-button">
                                        Áp dụng
                                    </button>
                                </li>
                            </ul>
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
                        <button v-if="!discountList?.length" @click="closeVoucherModal"
                            class="apply-button">Đóng</button>
                    </div>
                </div>
            </div>
        </transition>
    </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import { getDiaChiByClient } from '@/Service/ClientService/TaiKhoan/DiaChiServices'
import { useRoute } from 'vue-router';
import { loadPaymentMethod, thanhToanClient } from "@/Service/ClientService/HoaDon/MyOrderClient";
import { useToast } from "vue-toastification";
import router from '@/router';
import { getLatLon, getDistance } from '@/Service/ClientService/HoaDon/MyOrderClient'
import { cartService } from '@/service/ClientService/GioHang/GioHangClientService';
import codIcon from '@/assets/HinhAnh/images.jpg'
import vnPayIcon from '@/assets/HinhAnh/vnpay.png'
import { useStore } from "vuex";
import headerState from "@/components/Client/modules/headerState";

import { getAllPhieuGiamGia } from '@/Service/Clientservice/HoaDon/PhieuGiamGiaClient';

import Store from "@/Service/LoginService/Store";
const user = computed(() => Store.getters.user || null);

const store = useStore();
const count = ref(0);

if (!store.hasModule('headerState')) {
    store.registerModule('headerState', headerState)
}

const guiLenHeader = () => {
    store.commit("headerState/setCartItemCount", count.value);
};

const idKhachHang = user.value?.id;

const toast = useToast()
const route = useRoute();
const isLoading = ref(false)

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
    nextTick(() => {
        if (shippingAddress.value && shippingAddress.value.tinhThanhPho) {
            updatePhiShip();
        }
    });
}, { immediate: false })

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

const errors = ref({});

const validateNewAddress = () => {
    errors.value = {};

    if (!modalNewAddress.value.name?.trim()) {
        errors.value.name = "Vui lòng nhập họ và tên";
    }

    if (!modalNewAddress.value.phone?.trim()) {
        errors.value.phone = "Vui lòng nhập số điện thoại";
    } else if (!/^0\d{9}$/.test(modalNewAddress.value.phone)) {
        errors.value.phone = "Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0)";
    }

    if (!modalNewAddress.value.address?.trim()) {
        errors.value.address = "Vui lòng nhập địa chỉ";
    }

    return Object.keys(errors.value).length === 0;
};

const hasShippingInfo = computed(() => {
    const addr = shippingAddress.value;
    return addr
        && addr.tenNguoiNhan
        && addr.sdtNguoiNhan
        && addr.emailNguoiNhan
        && addr.soNha
        && addr.tenDuong
        && addr.xaPhuong
        && addr.tinhThanhPho;
});

const confirmAddressSelection = () => {
    if (modalSelectedAddressId.value === 'new') {
        errors.value = {};
        if (validateNewAddress()) {
            const newId = (userAddresses.value.length + 1).toString();
            const savedAddress = { id: newId, ...modalNewAddress.value };
            userAddresses.value.push(savedAddress);
            selectedAddressId.value = newId; // Update main form's selected ID
            shippingAddress.value = { ...savedAddress }; // Update main form's shipping address
            modalNewAddress.value = { name: '', phone: '', address: '' }; // Clear modal's new address fields
            closeAddressModal();
            toast.success('Địa chỉ mới đã được lưu và chọn!');
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

const discountList = ref([])
const selectedDiscount = ref(null)
const giam = ref(0)

const loadDiscountList = async () => {
    try {
        const response = await getAllPhieuGiamGia(modalVoucherCode.value, idKhachHang, calculateSubtotal.value)
        discountList.value = response.data
    } catch (err) {
        console.error(err || "Lỗi lấy danh sách phiếu giảm giá");
    }
}

// Open Voucher Modal and sync its state
const openVoucherModal = () => {
    modalVoucherCode.value = voucherCode.value; // Sync current voucher code
    modalAppliedVoucher.value = { ...appliedVoucher.value }; // Sync currently applied voucher
    modalVoucherError.value = ''; // Clear errors on open
    isVoucherModalOpen.value = true;
    loadDiscountList()
};

const closeVoucherModal = () => {
    isVoucherModalOpen.value = false;
};

const applyDiscount = (discount) => {
    selectedDiscount.value = discount;

    console.log('1', selectedDiscount.value)
    if (selectedDiscount.value?.loaiGiamGia === 'Phần trăm') {
        giam.value = calculateSubtotal.value * selectedDiscount.value?.giaTriGiamGia / 100;
        if (selectedDiscount.value?.giaTriGiamGiaToiDa < giam.value) {
            giam.value = selectedDiscount.value?.giaTriGiamGiaToiDa
        }
    } else if (selectedDiscount.value?.giaTriGiamGia) {
        giam.value = selectedDiscount.value.giaTriGiamGia;
    }

    const giamGia = { code: discount.maGiamGia, discount: giam.value }
    calculateTotal.value = calculateTotal.value - giamGia
    appliedVoucher.value = giamGia
    isVoucherModalOpen.value = false;
};

const handleApplyVoucherInModal = () => {
    modalVoucherError.value = '';
    modalAppliedVoucher.value = { code: '', discount: 0 };

    const code = modalVoucherCode.value.trim().toUpperCase();

    if (code === 'FREESHIP') {
        modalAppliedVoucher.value = { code: 'FREESHIP', discount: getShippingCost.value };
        console.log('FREESHIP', modalAppliedVoucher.value)
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
const phiShipTieuChuan = ref(0);
const phishipDisplay = ref(0);
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
    loadDiscountList();
});

watch(product, (newVal) => {
    console.log("product thay đổi:", newVal);
    console.log("Subtotal mới:", calculateSubtotal.value);
    loadDiscountList()
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
            return phiShipTieuChuan.value;
        case 'express':
            return phishipDisplay.value;
        default:
            return 0;
    }
});

const calculateSubtotal = computed(() => {
    let subtotal = 0;
    if (Array.isArray(product.value)) {
        for (const item of product.value) {
            const price = Number(item.gia) || 0;
            const quantity = Number(item.soLuong) || 0;
            subtotal += price * quantity;
        }
    }

    if (hasInsurance.value && insurance.value?.price && insurance.value?.quantity) {
        subtotal += Number(insurance.value.price) * Number(insurance.value.quantity);
    }

    return subtotal;
});

const calculateTotal = computed(() => {
    return Number(calculateSubtotal.value || 0) + Number(getShippingCost.value || 0) - Number(giam.value || 0);
})
console.log('total', calculateTotal.value)


const handleBuy = async () => {
    const shippingConfirm = {
        hinhThucThanhToan: selectedPaymentMethod.value,
        soTienKhachDua: calculateTotal.value,
        thanhTien: calculateTotal.value,
        phiShip: getShippingCost.value,
        shippingMethod: selectedShippingMethod.value.toUpperCase(),
        sdtNguoiNhan: shippingAddress.value.sdtNguoiNhan,
        tenNguoiNhan: shippingAddress.value.tenNguoiNhan,
        emailNguoiNhan: shippingAddress.value.emailNguoiNhan,
        diaChiGiaoHang: [
            shippingAddress.value.soNha,
            shippingAddress.value.tenDuong,
            shippingAddress.value.xaPhuong,
            shippingAddress.value.quanHuyen,
            shippingAddress.value.tinhThanhPho
        ].filter(Boolean).join(', '),
        sanPhamRequests: product.value.map(p => ({
            idSanPham: p.idSanPhamChiTiet,
            soLuong: p.soLuong
        })),
        idPhieuGiamGia: selectedDiscount.value?.id
    };
    if (getShippingCost.value == 0) {
        toast.warning('Chưa chọn phương thức giao hàng')
        return
    }

    if (!shippingAddress.value.tenNguoiNhan || !shippingAddress.value.sdtNguoiNhan || !shippingConfirm.diaChiGiaoHang) {
        alert('Vui lòng chọn hoặc nhập địa chỉ giao hàng.');
        return;
    }

    isLoading.value = true;

    const toastId = toast.info('Đang xử lý đơn hàng...', {
        timeout: false,
        closeButton: false,
    });

    try {
        const res = await thanhToanClient(shippingConfirm);

        if (res.data.message === 'REDIRECT_VNPAY') {
            window.location.href = res.data.paymentUrl;
            return;
        }

        if (res.data.message === 'Đặt hàng thành công') {
            toast.dismiss(toastId); // Hủy toast loading
            toast.success('Đặt hàng thành công');
            try {
                count.value = await cartService.cartCount(idKhachHang);
            } catch (error) {
                console.error("Lỗi khi tải giỏ hàng:", error);
            }
            guiLenHeader();
            router.push({ name: 'ordersucces' });
        } else {
            toast.dismiss(toastId);
            toast.error(res.data.message || 'Có lỗi xảy ra');
        }
    } catch (error) {
        toast.dismiss(toastId);
        toast.error('Đã xảy ra lỗi. Vui lòng thử lại.');
        console.error('Lỗi khi thanh toán:', error);
    } finally {
        isLoading.value = false; // Kết thúc trạng thái loading
    }
}

onMounted(() => {
    const responseCode = route.query.vnp_ResponseCode;

    if (responseCode === '24') {
        router.push({ name: 'shoppingCardClient' });
    }
});

const paymentMethods = ref([]);
const selectedPaymentMethod = ref(null);
const agreedToTerms = ref(false);

const fetchPaymentMethods = async () => {
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
        case 'COD':
            return codIcon; 
        case 'VNPAY':
            return vnPayIcon; 
        default:
            return '/icons/default.png'; 
    }
};
onMounted(async () => {
    fetchPaymentMethods();
})
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN').format(amount)
}
const storeAddress = 'Hà Nội';
const updatePhiShip = async () => {

    const fullAddress = `${shippingAddress.value.tinhThanhPho}`;

    console.log("Địa chỉ người nhận đầy đủ (fullAddress):", fullAddress);
    console.log("Địa chỉ cửa hàng (storeAddress):", storeAddress);

    try {
        const [from, to] = await Promise.all([
            getLatLonFromAddress(storeAddress),
            getLatLonFromAddress(fullAddress),
        ]);

        console.log("Tọa độ cửa hàng (from):", from);
        console.log("Tọa độ người nhận (to):", to);

        const distance = await getDistanceInKm(from, to);
        console.log("Khoảng cách tính được:", distance);

        // const maxDistance = 50;
        // let adjustedDistance = distance;
        // if (distance > maxDistance) {
        //     console.warn(`Khoảng cách quá lớn (${distance} km), giới hạn về ${maxDistance} km.`);
        //     adjustedDistance = maxDistance;
        // }

        phishipDisplay.value = calcPhiShip(distance);
        phiShipTieuChuan.value = calcPhiShipTieuChuan(distance)
        console.log(
            `Khoảng cách: ${distance} km, Phí ship: ${phishipDisplay.value.toLocaleString('vi-VN')} VNĐ`
        );
    } catch (err) {
        console.error("Lỗi khi tính phí ship:", err);
        phishipDisplay.value = 50000; 
        phiShipTieuChuan.value = 30000;
        console.log("Áp dụng phí mặc định do lỗi: 30,000 VNĐ");
    }
};


// Hàm lấy tọa độ từ địa chỉ
const getLatLonFromAddress = async (address) => {
    console.log("Đang gọi API lấy tọa độ cho:", address);
    try {
        // Thử địa chỉ gốc
        const res = await getLatLon(address);
        const parsedData = res.data;
        console.log("Phản hồi từ API /geo (địa chỉ gốc):", parsedData);

        if (Array.isArray(parsedData) && parsedData.length > 0 && parsedData[0].lat && parsedData[0].lon) {
            const coords = {
                lat: parseFloat(parsedData[0].lat),
                lon: parseFloat(parsedData[0].lon),
            };
            console.log(`Tọa độ trả về cho ${address}:`, coords);
            return coords;
        }

        // Thử định dạng thay thế (xóa dấu phẩy thừa)
        const cleanAddress = address.replace(/^,\s*/, '').trim();
        console.log("Thử định dạng thay thế:", cleanAddress);
        const altRes = await getLatLon(cleanAddress);
        const altParsedData = altRes.data;
        console.log("Phản hồi từ API /geo (thay thế):", altParsedData);

        if (Array.isArray(altParsedData) && altParsedData.length > 0 && altParsedData[0].lat && altParsedData[0].lon) {
            const coords = {
                lat: parseFloat(altParsedData[0].lat),
                lon: parseFloat(altParsedData[0].lon),
            };
            console.log(`Tọa độ trả về cho ${cleanAddress}:`, coords);
            return coords;
        }

        console.warn("Không tìm thấy tọa độ hợp lệ cho địa chỉ:", address, parsedData);
        return null;
    } catch (error) {
        console.error("Lỗi khi lấy tọa độ cho địa chỉ:", address, error);
        return null;
    }
};
// Hàm tính khoảng cách giữa 2 tọa độ (đã có sẵn)
const getDistanceInKm = async (from, to) => {
    if (from.lat === to.lat && from.lon === to.lon) {
        console.warn('Hai điểm trùng nhau, khoảng cách = 0 km');
        return 0;
    }
    try {
        const res = await getDistance(from, to);
        const parsedData = res.data;
        const distanceInMeters = parsedData?.routes?.[0]?.distance;

        if (distanceInMeters != null) {
            return Math.ceil(distanceInMeters / 1000);
        }

        console.warn('Không lấy được khoảng cách giữa 2 điểm (kiểm tra parsedData):', parsedData);
        return 0;

    } catch (error) {
        console.error('Lỗi khi tính khoảng cách giữa 2 điểm:', error);
        return 0;
    }
};
const calcPhiShip = (km) => {
    return 50000
};


const calcPhiShipTieuChuan = (km) => {

    if (km <= 30) return 30000;

    if (km >= 30 || km <= 50) return 40000;

    if (km >= 50 || km <= 100) {
        return 1500 * km;
    }

    if (km >= 100 || km <= 500) {
        return 1200 * km;
    }

    if (km => 1000) {
        return 1000 * km;
    }
};
</script>

<style scoped src="@/style/HoaDon/CheckoutForm.css"></style>