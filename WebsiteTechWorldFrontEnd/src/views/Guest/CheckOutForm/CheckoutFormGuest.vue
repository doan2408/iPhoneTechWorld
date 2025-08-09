<template>
    <div class="container">
        <div class="checkout-card">
            <h1 class="title">Thanh Toán</h1>

            <!-- Địa chỉ nhận hàng -->
            <div class="section">
                <h2 class="section-title">Địa chỉ nhận hàng</h2>
                <div class="current-address-display">
                    <p class="address-name-display">{{ shippingAddress.name || '' }}</p>
                    <p class="address-phone-display">{{ shippingAddress.phone || ''}}</p>
                    <p class="address-phone-display">{{ shippingAddress.email || '' }}</p>
                    <p v-if="shippingAddress.address" class="address-detail-display">
                        {{ shippingAddress.address }}
                    </p>
                    <p v-else class="address-detail-display text-gray-400">
                        Chưa có địa chỉ nhận hàng
                    </p>
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
            <div v-if="shippingAddress.address" class="section">
                <h2 class="section-title">Phương thức vận chuyển</h2>
                <div class="radio-group">
                    <label class="radio-option">
                        <div class="radio-content">
                            <input type="radio" name="shipping-method" value="ghtk" v-model="selectedShippingMethod"
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
                    Mua hàng
                </button>
            </div>
        </div>

        <!-- Address Selection Modal (Inline) -->
        <transition name="modal-fade">
            <div v-if="isAddressModalOpen" class="modal-overlay" @click.self="closeAddressModal">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="address-modal-content">

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
                                    <label for="modal-phone" class="label">Email</label>
                                    <input id="modal-phone" v-model="modalNewAddress.email" type="tel"
                                        placeholder="Nhập email người nhận" class="input-field" />
                                        <span v-if="errors.email" class="error-text">{{ errors.email }}</span>
                                </div>
                                <label>Chọn tỉnh:</label>
                                <select v-model="selectedTinh" @change="onTinhChange" class="select-box">
                                    <option disabled value="">-- Tỉnh/Thành phố --</option>
                                    <option v-for="t in tinhList" :key="t.code" :value="t">{{ t.name }}
                                    </option>
                                </select>
                                <span v-if="errors.tinh" class="error-text">{{ errors.tinh }}</span>

                                <label>Chọn Xã:</label>
                                <select v-model="selectedXa" @change="onXaChange" class="select-box"
                                    :disabled="!selectedTinh">
                                    <option disabled value="">-- Phường/Xã --</option>
                                    <option v-for="x in xaList" :key="x.code" :value="x">{{ x.name }}
                                    </option>
                                </select>
                                <span v-if="errors.xa" class="error-text">{{ errors.xa }}</span>

                                <div>
                                    <label for="modal-address" class="label">Địa chỉ</label>
                                    <input id="modal-address" v-model="addressDetail" type="text"
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
                                <input v-model="modalVoucherCode" type="text" placeholder="Nhập mã giảm giá"
                                    class="input-field flex-grow" />
                            </div>
                            <ul class="voucher-list">
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
                        <button @click="closeVoucherModal" class="apply-button">Đóng</button>
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
import { loadPaymentMethod, thanhToanGuest } from "@/Service/ClientService/HoaDon/MyOrderClient";
import { useToast } from "vue-toastification";
import router from '@/router';
import { getLatLon, getDistance } from '@/Service/ClientService/HoaDon/MyOrderClient'
import provinceData from '@/assets/JsonTinhThanh/province.json'
import wardData from '@/assets/JsonTinhThanh/ward.json'
import { add } from '@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService';

import { CartService } from "@/Service/ClientService/GioHang/CartService";
import { getAllPhieuGiamGia } from '@/Service/Clientservice/HoaDon/PhieuGiamGiaClient';

const toast = useToast()
const route = useRoute();
const isLoading = ref(false)   

// --- Address Management ---
const userAddresses = ref([])

const formattedAddress = computed(() => {
    const a = shippingAddress.value;
    if (!a) return '';

    // Gom các phần của địa chỉ thành mảng, rồi lọc bỏ null/undefined/chuỗi rỗng
    const parts = [
        a.soNha,
        a.tenDuong,
        a.xaPhuong,
        a.quanHuyen,
        a.tinhThanhPho
    ].filter(Boolean); // Lọc bỏ các giá trị falsy

    return parts.join(', ');
});

const isAddressModalOpen = ref(false)
const selectedAddressId = ref(userAddresses.value.length > 0 ? userAddresses.value[0].id : 'new')
const modalSelectedAddressId = ref(selectedAddressId.value) // State for modal's radio selection

const newAddress = ref({ name: '', phone: '', email: '', address: '' })
const modalNewAddress = ref({ name: '', phone: '', email: '', address: '' }) // State for modal's new address form

const shippingAddress = ref({ // This holds the final address for checkout
    name: '',
    phone: '',
    email: '',
    address: '',
})
watch(() => modalNewAddress.value.name, (newVal) => {
    shippingAddress.value.name = newVal;
});

watch(() => modalNewAddress.value.phone, (newVal) => {
    shippingAddress.value.phone = newVal;
});
watch(() => modalNewAddress.value.email, (newVal) => {
    shippingAddress.value.email = newVal;
});

const errors = ref({});

const validateAddressForm = () => {
  errors.value = {};

  if (!modalNewAddress.value.name?.trim()) {
    errors.value.name = "Vui lòng nhập họ và tên";
  }

  if (!modalNewAddress.value.phone?.trim()) {
    errors.value.phone = "Vui lòng nhập số điện thoại";
  } else if (!/^0\d{9}$/.test(modalNewAddress.value.phone)) {
    errors.value.phone = "Số điện thoại không hợp lệ";
  }

  if (!modalNewAddress.value.email?.trim()) {
    errors.value.email = "Vui lòng nhập email";
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(modalNewAddress.value.email)) {
    errors.value.email = "Email không hợp lệ";
  }

  if (!selectedTinh.value) {
    errors.value.tinh = "Vui lòng chọn tỉnh/thành phố";
  }

  if (!selectedXa.value) {
    errors.value.xa = "Vui lòng chọn xã/phường";
  }

  if (!addressDetail.value?.trim()) {
    errors.value.address = "Vui lòng nhập địa chỉ chi tiết";
  }

  return Object.keys(errors.value).length === 0;
};


function confirmAddressSelection() {
    if (validateAddressForm()) {
        if (modalSelectedAddressId.value === 'new') {
            shippingAddress.value = { ...modalNewAddress.value };
            applySelectedAddress();
        } else {
            const selected = userAddresses.value.find(addr => addr.id === modalSelectedAddressId.value);
            if (selected) shippingAddress.value = selected;
        }
        isAddressModalOpen.value = false;
    }
}

// Initialize shippingAddress based on initial selectedAddressId
watch(selectedAddressId, () => {
    applySelectedAddress();
});


function applySelectedAddress() {
    if (modalSelectedAddressId.value === 'new') {
        const fullAddress = [addressDetail.value, selectedXa.value?.name, selectedTinh.value?.name]
            .filter(Boolean)
            .join(', ');

        shippingAddress.value = {
            name: modalNewAddress.value.name,
            phone: modalNewAddress.value.phone,
            email: modalNewAddress.value.email,
            address: fullAddress, 
        };
    } else {
        const selected = userAddresses.value.find(
            (addr) => addr.id === modalSelectedAddressId.value
        );

        if (selected) {
            shippingAddress.value = { ...selected };
        }
    }

    nextTick(() => {
        if (shippingAddress.value) {
            updatePhiShip();
        }
    });

    isAddressModalOpen.value = false;
}

// Open Address Modal and sync its state
const openAddressModal = () => {
    errors.value = {};

    modalSelectedAddressId.value = selectedAddressId.value;
    if (selectedAddressId.value === 'new') {
        modalNewAddress.value = { ...newAddress.value }; // Copy current new address data
    } else {
        modalNewAddress.value = { name: '', phone: '',email: '', address: '' }; // Clear new address form if existing is selected
    }
    isAddressModalOpen.value = true;
};

const closeAddressModal = () => {
    isAddressModalOpen.value = false;
};


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
        const response = await getAllPhieuGiamGia(modalVoucherCode.value, null, calculateSubtotal.value)
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
    loadDiscountList()
});

watch(product, (newVal) => {
    console.log("👀 product thay đổi:", newVal);
    console.log("💵 Subtotal mới:", calculateSubtotal.value);
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
        case 'ghtk':
            return 25000;
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
        sdtNguoiNhan: shippingAddress.value.phone,
        tenNguoiNhan: shippingAddress.value.name,
        emailNguoiNhan: shippingAddress.value.email,
        diaChiGiaoHang: [
            shippingAddress.value.address
        ].filter(Boolean).join(', '),
        sanPhamRequests: product.value.map(p => ({
            idSanPham: p.idSanPhamChiTiet,
            soLuong: p.soLuong
        })),
        idPhieuGiamGia: selectedDiscount.value?.id 
    };
    console.log('shipping', shippingConfirm);
    if (getShippingCost.value == 0) {
        toast.warning('Chưa chọn phương thức giao hàng')
        return
    }

    if (!shippingAddress.value.phone || !shippingAddress.value.name || !shippingConfirm.diaChiGiaoHang) {
        alert('Vui lòng chọn hoặc nhập địa chỉ giao hàng.');
        return;
    }

    isLoading.value = true;

    const toastId = toast.info('Đang xử lý đơn hàng...', {
        timeout: false,
        closeButton: false,
    });

    try {
        const res = await thanhToanGuest(shippingConfirm);

        if (res.data.message === 'REDIRECT_VNPAY') {
            window.location.href = res.data.paymentUrl;
            return;
        }

        if (res.data.message === 'Đặt hàng thành công') {
            toast.dismiss(toastId); // Hủy toast loading
            toast.success('Đặt hàng thành công');
            product.value.forEach(p => {
                const success = CartService.xoaSanPhamKhoiGio(p.idSanPhamChiTiet)
            });
            router.push({ name: 'successClient' });
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

const phishipDisplay = ref(0);
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN').format(amount)
}
const storeAddress = 'Hà Nội';
const selectedTinh = ref('')
const selectedXa = ref('')
const addressDetail = ref('')
const tinhList = ref(provinceData)
const allXaList = ref(Object.values(wardData))
const xaList = ref([])
const tinhThanhList = ref([])
const updatePhiShip = async () => {


    const fullAddress = [selectedTinh.value?.name]
        .filter(Boolean)
        .join(', ');

    console.log("Địa chỉ người nhận đầy đủ (fullAddress):", fullAddress);
    console.log("Địa chỉ cửa hàng (storeAddress):", storeAddress);

    try {
        const [from, to] = await Promise.all([
            getLatLonFromAddress(storeAddress),
            getLatLonFromAddress(fullAddress),
        ]);

        console.log("Tọa độ cửa hàng (from):", from);
        console.log("Tọa độ người nhận (to):", to);

        if (!from || !to) {
            phishipDisplay.value = 30000; // Phí mặc định
            console.warn("Không tìm thấy tọa độ cho ít nhất một trong hai địa chỉ. Áp dụng phí mặc định: 30,000 VNĐ.");
            return;
        }

        const distance = await getDistanceInKm(from, to);
        console.log("Khoảng cách tính được:", distance);

        // const maxDistance = 50;
        // let adjustedDistance = distance;
        // if (distance > maxDistance) {
        //     console.warn(`Khoảng cách quá lớn (${distance} km), giới hạn về ${maxDistance} km.`);
        //     adjustedDistance = maxDistance;
        // }

        phishipDisplay.value = calcPhiShip(distance);
        console.log(
            `Khoảng cách: ${distance} km, Phí ship: ${phishipDisplay.value.toLocaleString('vi-VN')} VNĐ`
        );
    } catch (err) {
        console.error("Lỗi khi tính phí ship:", err);
        phishipDisplay.value = 30000; // Phí mặc định
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
    const baseFee = 15000;
    const additionalFeePerKm = 2000;

    if (km <= 2) return baseFee;

    const calculatedFee = baseFee + (km - 2) * additionalFeePerKm;

    return calculatedFee;
};
const onTinhChange = async () => {
    selectedXa.value = '';
    xaList.value = [];
    if (selectedTinh.value?.name) {
        try {
            xaList.value = allXaList.value.filter(ward =>
                ward.path.includes(selectedTinh.value.name)
            )
            console.log("Danh sách Phường/Xã:", xaList.value)
        } catch (error) {
            console.error("Lỗi khi lấy danh sách huyện:", error);
        }
    }
};
const onXaChange = async () => {
    console.log("Xã được chọn:", selectedXa.value);
    phishipDisplay.value = null;
    if (selectedXa.value) {
        console.log('Calculating shipping fee after selecting ward:', selectedXa.value.name);
    }
};


</script>

<style scoped src="@/style/HoaDon/CheckoutForm.css"></style>