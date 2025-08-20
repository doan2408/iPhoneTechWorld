<template>
    <div class="delivery-tracking-container">
        <!-- Order Header -->
        <div class="order-header">
            <div class="order-info">
                <h1 v-if="order.maVanDon">Đơn hàng #{{ order.maVanDon }} </h1>
                <h1 v-else>Hóa đơn #{{ order.maHoaDon }} </h1>
                <p class="order-date">
                    <Clock class="icon-small" />
                    Ngày đặt: {{ formatDate(order.ngayDatHang) }}
                </p>
            </div>
            <div class="info-item">
                <strong>Trạng thái đơn hàng</strong>
                <br>
                <span :class="'status status-' + getStatusKey(order.trangThaiDonHang)">
                    {{ getStatusText(order.trangThaiDonHang) }}
                </span>
            </div>
            <div class="order-summary">
                <div class="total-info">
                    <span class="total-label"><strong>Tổng tiền</strong></span>
                    <span class="total-amount">{{ formatCurrency(order.thanhTien) }}</span>
                </div>
                <span :class="'payment-status ' + getPaymentStatusClass(order.trangThaiThanhToan)">
                    {{ getPaymentStatusText(order.trangThaiThanhToan) }}
                </span>
            </div>
        </div>


        <!-- Timeline Section -->
        <div class="timeline-section">
            <div class="timeline-wrapper">
                <div class="timeline-line"></div>

                <div v-for="(step, index) in orderSteps" :key="step.id" class="timeline-step" :class="{
                    'active': step.status === 'completed',
                    'current': step.status === 'current',
                    'pending': step.status === 'pending'
                }">
                    <div class="step-icon">
                        <component :is="step.icon" class="icon" />
                    </div>

                    <div class="step-content">
                        <div class="step-title">{{ step.title }}</div>
                        <div v-if="step.timestamp" class="step-time">
                            {{ formatDate(step.timestamp) }}
                        </div>
                        <div v-if="step.description" class="step-description">
                            {{ step.description }}
                        </div>
                    </div>

                    <!-- Arrow for completed/current steps -->
                    <div v-if="step.status !== 'pending'" class="step-arrow"></div>
                </div>
            </div>
        </div>
        <div class="timeline-section-row">
            <div class="timeline-section timeline-shipping">
                <div class="timeline-wrapper">
                    <div class="timeline-line"></div>

                    <div v-for="(step, index) in orderStepsShipping" :key="step.id" class="timeline-step" :class="{
                        'active': step.status === 'completed',
                        'current': step.status === 'current',
                        'pending': step.status === 'pending'
                    }">
                        <div class="step-icon">
                            <component :is="step.icon" class="icon" />
                        </div>

                        <div class="step-content">
                            <div class="step-title">{{ step.title }}</div>
                            <div v-if="step.timestamp" class="step-time">
                                {{ formatDate(step.timestamp) }}
                            </div>
                            <div v-if="step.description" class="step-description">
                                {{ step.description }}
                            </div>
                        </div>

                        <!-- Arrow for completed/current steps -->
                        <div v-if="step.status !== 'pending'" class="step-arrow-shipping"></div>
                    </div>
                </div>
            </div>
            <div class="timeline-section timeline-false">
                <div class="timeline-wrapper">
                    <div class="timeline-line"></div>

                    <div v-for="(step, index) in orderStepsFalse" :key="step.id" class="timeline-step-false" :class="{
                        'active': step.status === 'completed',
                        'current': step.status === 'current',
                        'pending': step.status === 'pending'
                    }">
                        <div class="step-icon">
                            <component :is="step.icon" class="icon" />
                        </div>

                        <div class="step-content">
                            <div class="step-title">{{ step.title }}</div>
                            <div v-if="step.timestamp" class="step-time">
                                {{ formatDate(step.timestamp) }}
                            </div>
                            <div v-if="step.description" class="step-description">
                                {{ step.description }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Action Buttons -->
        <div class="actions-section">
            <div class="left-actions">
                <button v-if="canCancel"
                    @click="openConfirm('Bạn có chắc muốn hủy đơn?', () => updateOrderStatus('Đã hủy'))"
                    class="action-btn cancel-btn">
                    <X class="icon-small" /> HỦY ĐƠN
                </button>

                <button v-if="canReturn" @click="openPopupReturn()" class="action-btn cancel-btn">
                    <X class="icon-small" /> TRẢ HÀNG
                </button>

                <div v-if="showReturnPopup" class="return-overlay">
                    <div class="return-modal">
                        <h3 class="return-title">Chọn sản phẩm/IMEI muốn trả</h3>

                        <div class="return-imei-list">
                            <label v-for="imei in imeis" :key="imei.idImei" class="return-imei-item">
                                <input type="checkbox" v-model="imei.selected" />
                                <div class="return-imei-info">
                                    <span class="imei-main">{{ imei.soImei }}</span>
                                    <span class="imei-sub">
                                        {{ imei.tenSanPham }} - {{ imei.dungLuongRom }} - {{ imei.tenMau }}
                                        ({{ imei.giaBan.toLocaleString() }}đ)
                                    </span>
                                </div>
                            </label>
                        </div>

                        <div v-if="imeis.some(i => i.selected)" class="return-selected">
                            <h4>Danh sách trả hàng</h4>
                            <table class="return-table">
                                <thead>
                                    <tr>
                                        <th>IMEI</th>
                                        <th>Sản phẩm</th>
                                        <th>Lý do trả</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="imei in imeis.filter(i => i.selected)" :key="imei.idImei">
                                        <td>{{ imei.soImei }}</td>
                                        <td>{{ imei.tenSanPham }} - {{ imei.dungLuongRom }} - {{ imei.tenMau }}</td>
                                        <td>
                                            <select v-model="imei.reasonId" class="return-select">
                                                <option disabled value="">-- Chọn lý do --</option>
                                                <option v-for="reason in returnReasons" :key="reason.idReason"
                                                    :value="reason.idReason">
                                                    {{ reason.tenLyDo }}
                                                </option>
                                            </select>
                                            <div class="upload-group">
                                                <label class="upload-label">Ảnh lỗi:</label>
                                                <input type="file" accept="image/*"
                                                    @change="e => handleUpload(e, imei, 'image')" />
                                                <span v-if="imei.urlHinh" class="upload-preview">✅ Đã upload ảnh</span>
                                            </div>

                                            <!-- Upload video -->
                                            <div class="upload-group">
                                                <label class="upload-label">Video lỗi:</label>
                                                <input type="file" accept="video/*"
                                                    @change="e => handleUpload(e, imei, 'video')" />
                                                <span v-if="imei.urlVideo" class="upload-preview">✅ Đã upload
                                                    video</span>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <div class="return-actions">
                            <button class="return-btn cancel" @click="showReturnPopup = false">Hủy</button>
                            <button class="return-btn confirm"
                                :disabled="!imeis.some(i => i.selected) || imeis.filter(i => i.selected).some(i => !i.reasonId)"
                                @click="confirmReturn">
                                Xác nhận
                            </button>
                        </div>
                    </div>
                </div>


            </div>

            <div class="right-actions">
                <button @click="printInvoice" class="action-btn print-btn">
                    <Printer class="icon-small" /> IN HÓA ĐƠN
                </button>

                <button @click="printDeliveryNote" class="action-btn delivery-btn">
                    <FileText class="icon-small" /> IN PHIẾU GIAO HÀNG
                </button>

                <button @click="editOrder" class="action-btn edit-btn">
                    <Edit class="icon-small" /> CHỈNH SỬA
                </button>
            </div>
        </div>

        <!-- Confirm Modal -->
        <ConfirmModal v-if="showConfirm" :message="confirmMessage" @confirm="handleConfirm"
            @cancel="showConfirm = false" />

        <br><br>

        <!-- Delivery Details Section -->
        <div class="delivery-details">
            <div class="details-grid">
                <!-- Customer Info -->
                <div class="detail-card customer-card">
                    <div class="card-header">
                        <User class="icon" />
                        <h3>Thông tin khách hàng</h3>
                    </div>
                    <div class="card-content">
                        <div class="customer-info">
                            <div class="customer-avatar">
                                <User class="avatar-icon" />
                            </div>
                            <div class="customer-details">
                                <h4>{{ order.tenNguoiMua || 'Khách vãng lai' }} ( Người mua )</h4>
                                <div :class="['customer-type', getCustomerTypeClass(order.hangKhachHang)]">
                                    <span>{{ getCustomerTypeText(order.hangKhachHang) }}</span>
                                </div>
                            </div>
                        </div>
                        <br>
                        <div class="contact-info">
                            <h5>Thông tin người nhận</h5>
                            <div class="contact-item">
                                <User class="icon-small" />
                                <span>{{ order.tenNguoiNhan }}</span>
                            </div>
                            <div class="contact-item">
                                <Phone class="icon-small" />
                                <span>{{ order.sdtNguoiNhan }}</span>
                                <button @click="callCustomer" class="contact-btn">Gọi</button>
                            </div>
                            <div class="contact-item">
                                <Mail class="icon-small" />
                                <span>{{ order.emailNguoiNhan }}</span>
                                <button @click="emailCustomer" class="contact-btn">Email</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Delivery Info -->
                <div class="detail-card delivery-card">
                    <div class="card-header">
                        <MapPin class="icon" />
                        <h3>Thông tin giao hàng</h3>
                    </div>
                    <div class="card-content">
                        <div class="delivery-info">
                            <div class="info-item">
                                <label>Địa chỉ giao hàng</label>
                                <p>{{ order.diaChiGiaoHang }}</p>
                            </div>
                            <!-- <div class="info-item">
                                <label>Thời gian dự kiến</label>
                                <p>{{ order.estimatedDelivery ? formatDateTime(order.estimatedDelivery) : 'Chưa xác định' }}</p>
                            </div> -->
                            <div class="info-item">
                                <label>Người giao hàng</label>
                                <div v-if="order.driver" class="driver-info">
                                    <div class="driver-avatar">
                                        <Truck class="icon-small" />
                                    </div>
                                    <div>
                                        <p class="driver-name">{{ order.driver.name }}</p>
                                        <p class="driver-phone">{{ order.driver.phone }}</p>
                                    </div>
                                    <button @click="callDriver" class="contact-btn">Gọi</button>
                                </div>
                                <p v-else class="no-driver">Nhân viên cửa hàng</p>
                            </div>
                            <div class="info-item">
                                <label>Ghi chú</label>
                                <p>{{ order.deliveryNotes || 'Không có ghi chú' }}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Order Items Summary -->
                <div class="detail-card items-card">
                    <div class="card-header">
                        <Package class="icon" />
                        <h3>Sản phẩm ({{ order.chiTietHoaDonAdminResponseList?.length || 0 }} món)</h3>
                    </div>
                    <div class="card-content">
                        <div class="items-summary">
                            <div v-for="item in order.chiTietHoaDonAdminResponseList" :key="item.id" class="item-row">
                                <img :src="item.imageSanPham" :alt="item.name" class="item-image" />
                                <div class="item-info">
                                    <span class="item-name">{{ item.tenSanPham }}</span>
                                    <span class="item-qty">x{{ item.soLuong }}</span>
                                </div>
                                <span class="item-price">{{ formatCurrency(item.donGia) }}</span>
                            </div>
                        </div>
                        <div class="total-summary">
                            <div class="total-row">
                                <span>Tổng cộng:</span>
                                <span class="total-price">{{ formatCurrency(order.thanhTien) }}</span>
                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>


    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import {
    Clock, User, Phone, Mail, MapPin, Package, CreditCard, ScrollText, Check, XCircle, AlertTriangle,
    CheckCircle, X, Truck, Printer, FileText, Edit, Star, Gem, Crown, CheckSquare, Box, RefreshCcw
} from 'lucide-vue-next'
import { hoaDonDetail } from '@/Service/ClientService/HoaDon/MyOrderClient'
import { useRoute } from 'vue-router'
import { id } from 'element-plus/es/locales.mjs'
import { useToast } from 'vue-toastification'
import ConfirmModal from '@/views/Popup/ConfirmModal.vue'
import { getAllFalseReasonByCaseReason } from '@/Service/GuestService/FalseReasonServices/FalseReasonServices'
import { createActionAfterCase, createActionAfterCaseReturn, uploadAnhAndVid } from '@/Service/GuestService/ActionAfterCaseService/ActionAfterCaseServices'
import { findHdctByImeiDaBan } from '@/Service/ClientService/HoaDon/MyOrderClient'
import { changeStatusOrderKhachHang } from '@/Service/Adminservice/GiaoHang/GiaoHangServices'


const order = reactive({})
const route = useRoute()
const statusUpdate = null;
const statusFlase = ['Trả hàng', 'Giao thất bại', 'Đã hủy']
const toast = useToast()



// ham view giao hang
const viewOrderDetail = async () => {
    const id = route.params.id
    if (id) {
        try {
            const response = await hoaDonDetail(id)
            Object.assign(order, response.data)
        } catch (error) {
            console.error('Lỗi khi tải chi tiết đơn hàng:', error)
        }
    }
}

const getCustomerTypeClass = (type) => {
    switch (type) {
        case 'SILVER': return 'silver';
        case 'GOLD': return 'gold';
        case 'DIAMOND': return 'diamond';
        default: return 'member';
    }
}

const getCustomerTypeText = (type) => {
    switch (type) {
        case 'SILVER': return 'Bạc';
        case 'GOLD': return 'Vàng';
        case 'DIAMOND': return 'Kim cương';
        case 'MEMBER': return 'Thành viên';
        default: return 'Không có hạng';
    }
}
const getCustomerTypeIcon = (type) => {
    switch (type) {
        case 'SILVER': return 'fas fa-medal';
        case 'GOLD': return 'fas fa-trophy';
        case 'DIAMOND': return 'fas fa-gem';
        default: return 'fas fa-user';
    }
}

// ham cap nha tien do giao dien giao hang
const orderSteps = computed(() => [
    {
        id: 1,
        title: 'Đặt Hàng Thành Công',
        icon: FileText,
        // Bước đầu luôn hiện là completed (hoặc current nếu mới tạo)
        status: order.trangThaiDonHang === 'Chờ xử lý' ? 'current' :
            ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng', 'Đã xác nhận',
                'Sẵn sàng giao', 'Đã hủy'
            ].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: order.ngayDatHang,
        description: 'Đơn hàng đang chờ được xác nhận'
    },
    {
        id: 2,
        title: 'Đơn hàng đã được xác nhận',
        icon: CheckSquare,
        status: order.trangThaiDonHang === 'Đã xác nhận' ? 'current' :
            ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng',
                'Sẵn sàng giao'
            ].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: null,
        description: 'Đơn hàng đang chờ được đóng gói'
    },
    {
        id: 3,
        title: 'Đã Đóng Gói',
        icon: Package,
        status: order.trangThaiDonHang === 'Đã đóng gói' ? 'current' :
            ['Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng', 'Sẵn sàng giao'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? order.packedAt : null,
        description: ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? 'Đơn hàng đã được đóng gói' : null
    },
    {
        id: 4,
        title: 'Sẵn sàng giao',
        icon: Check,
        status: order.trangThaiDonHang === 'Sẵn sàng giao' ? 'current' :
            ['Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? order.packedAt : null,
        description: ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? 'Đơn hàng sắp dược giao' : null
    },
])

const orderStepsShipping = computed(() => [
    {
        id: 1,
        title: 'Đang Giao',
        icon: Truck,
        status: order.trangThaiDonHang === 'Đang giao' ? 'current' :
            ['Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: ['Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? order.shippingAt : null,
        description: ['Đang giao', 'Đã giao', 'Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? `Đơn hàng đang được giao` : null
    },
    {
        id: 2,
        title: 'Giao Hàng Thành Công',
        icon: CheckCircle,
        status: order.trangThaiDonHang === 'Đã giao' ? 'current' :
            ['Trả hàng'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: order.trangThaiDonHang === 'Đã giao' ? order.deliveredAt : null,
        description: order.trangThaiDonHang === 'Đã giao' ? 'Khách hàng đã nhận hàng' : null
    },
])

const orderStepsFalse = computed(() => [
    {
        id: 1,
        title: 'Giao Thất Bại',
        icon: XCircle,
        status: ['Giao thất bại'].includes(order.trangThaiDonHang) ? 'current' : 'pending',
        timestamp: ['Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? order.failedAt : null,
        description: ['Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? 'Đơn hàng giao thất bại' : null
    },
    {
        id: 2,
        title: 'Trả hàng',
        icon: AlertTriangle,
        status: ['Trả hàng'].includes(order.trangThaiDonHang) ? 'current' : 'pending',
        timestamp: ['Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? order.failedAt : null,
        description: ['Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? 'Đơn hàng Trả hàng' : null
    },
    {
        id: 3,
        title: 'Đã hủy',
        icon: RefreshCcw,
        status: ['Đã hủy'].includes(order.trangThaiDonHang) ? 'current' : 'pending',
        timestamp: ['Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? order.failedAt : null,
        description: ['Giao thất bại', 'Trả hàng'].includes(order.trangThaiDonHang) ? 'Đơn hàng đã bị hủy' : null
    }
])
// bien trang thai
const canConfirm = computed(() => order.trangThaiDonHang === 'Chờ xử lý');
const canPack = computed(() => order.trangThaiDonHang === 'Đã xác nhận');
const canReady = computed(() => order.trangThaiDonHang === 'Đã đóng gói');
const canCancel = computed(() =>
    ['Chờ xử lý'].includes(order.trangThaiDonHang)
);
const canShippingFalse = computed(() => order.trangThaiDonHang === 'Đang giao');
const canReturn = computed(() => order.trangThaiDonHang === 'Đã giao');

//format date 
const formatDate = (dateString) => {
    if (!dateString) {
        return 'N/A';
    }
    const date = new Date(dateString);
    if (isNaN(date.getTime())) {
        return 'Invalid Date';
    }

    const day = date.getDate().toString().padStart(2, '0');      // ngày, 2 chữ số
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // tháng, 2 chữ số (tháng bắt đầu từ 0)
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
};

// format currency
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount)
}

// chuyển enum qua kiểu tiếng việt 
const getStatusText = (status) => {
    const statusMap = {
        pending: 'Chờ xác nhận',
        confirmed: 'Đã xác nhận',
        preparing: 'Đang chuẩn bị',
        shipping: 'Đang giao hàng',
        delivered: 'Đã giao hàng',
        cancelled: 'Đã hủy'
    }
    return statusMap[status] || status
}

// chuyển chuỗi thân thiện qua enum
function getStatusKey(text) {
    const statusMap = {
        pending: ['Chờ xử lý', 'Đã xác nhận'],
        packed: ['Đã đóng gói', 'Sẵn sàng giao'],
        shipping: ['Đang giao'],
        delivered: ['Đã giao'],
        failed: ['Giao thất bại'],
        returned: ['Trả hàng', 'Đã hủy'],
    }
    if (typeof text !== 'string') {
        return text;
    }
    const normalized = text.trim().toLowerCase();

    for (const [key, aliases] of Object.entries(statusMap)) {
        if (aliases.some(alias => alias.toLowerCase() === normalized)) {
            return key;
        }
    }

    return text;
}

const getPaymentStatusText = (status) => {
    if (!status || typeof status !== 'string') return 'Chưa thanh toán';

    const statusMap = {
        PENDING: 'Chưa thanh toán',
        CONFIRMED: 'Chưa thanh toán',
        PAID: 'Đã thanh toán',
        COMPLETED: 'Hoàn tất',
        CANCELLED: 'Hủy',
        REFUNDED: 'Hoàn tiền',
    };

    return statusMap[status.toUpperCase()] || 'Chưa thanh toán';
};
const getPaymentStatusClass = (status) => {
    if (!status || typeof status !== 'string') return 'pending';

    const statusClassMap = {
        PENDING: 'pending',
        CONFIRMED: 'pending',
        PAID: 'paid',
        COMPLETED: 'completed',
        CANCELLED: 'cancelled',
        REFUNDED: 'refunded'
    };

    return statusClassMap[status.toUpperCase()] || 'pending';
};

const showFailPopup = ref(false)
const failReason = ref("")
const failReasons = ref([])

const getAllFalseReason = async () => {
    try {
        const res = await getAllFalseReasonByCaseReason('FAILED_DELIVERY')
        failReasons.value = res.data
    } catch (err) {
        console.error("Lỗi load lý do:", err)
    }
}

const openPopupShippingFalse = () => {
    showFailPopup.value = true
    failReason.value = ""

    getAllFalseReason()

    console.log(showFailPopup.value);

}

const confirmFail = async () => {
    updateOrderStatus('Giao thất bại')
    await createActionAfterCase(order.idHoaDon, failReason.value)
    showFailPopup.value = false
    failReason.value = ""
}

const showReturnPopup = ref(false)
const imeis = ref([])
const returnReason = ref("")
const returnReasons = ref([])

const getAllReturnReason = async () => {
    try {
        const res = await getAllFalseReasonByCaseReason('RETURN')
        returnReasons.value = res.data
    } catch (err) {
        console.error("Lỗi load lý do:", err)
    }
}
const openPopupReturn = async () => {
    showReturnPopup.value = true
    imeis.value = []

    for (const cthd of order.chiTietHoaDonAdminResponseList) {
        const resImeis = await findHdctByImeiDaBan(cthd.idHoaDonChiTiet)
        imeis.value.push(...resImeis.data.map(i => ({ ...i, reasonId: "" })))
    }

    console.log(imeis.value)
    await getAllReturnReason()
}

const handleUpload = async (event, imei, type) => {
    const file = event.target.files[0]
    if (!file) return

    const formData = new FormData()
    formData.append("idImei", imei.idImei)
    formData.append("file", file)

    try {
        const res = await uploadAnhAndVid(formData)
        const url = res.data.url || res.data // tùy backend trả về

        if (type === "image") imei.urlHinh = url
        else imei.urlVideo = url
    } catch (err) {
        console.error("Upload lỗi:", err)
    }
}

const confirmReturn = async () => {
    try {
        const payload = {
            idHoaDon: order.idHoaDon,
            imeis: imeis.value
                .filter(i => i.selected)
                .map(i => ({
                    idHoaDonChiTiet: i.idHoaDonChiTiet,
                    soImei: i.soImei,
                    idFailReason: i.reasonId,
                    urlHinh: i.urlHinh || null,
                    urlVideo: i.urlVideo || null
                }))
        }

        await createActionAfterCaseReturn(payload)

        updateOrderStatus('Trả hàng')

        // Reset popup
        showReturnPopup.value = false
        imeis.value.forEach(i => {
            i.selected = false
            i.reasonId = ""
        })
    } catch (error) {
        console.error("Lỗi khi trả hàng:", error)
    }
}

const isProcessing = ref(false);
const updateOrderStatus = async (newStatus) => {
    const id = route.params.id;
    try {
        if (newStatus === 'Sẵn sàng giao') {
            isProcessing.value = true;
            toast.info("Đang tạo mã vận đơn...", { autoClose: 2000 });
        }
        const response = await changeStatusOrderKhachHang(id, newStatus);
        order.trangThaiDonHang = newStatus;
        console.log(response.data);
        console.log(`Order status updated to: ${newStatus}`);
        await viewOrderDetail();
    } catch (error) {
        console.error('Failed to update order status:', error.response?.data || error.message);
    } finally {
        isProcessing.value = false;
    }
};

const callCustomer = () => {
    window.open(`tel:${order.sdtNguoiNhan}`)
}

const emailCustomer = () => {
    window.open(`mailto:${order.customer.email}`)
}

const callDriver = () => {
    if (order.driver) {
        window.open(`tel:${order.driver.phone}`)
    }
}

const printInvoice = () => {
    console.log('Printing invoice')
    window.print()
}

const printDeliveryNote = () => {
    console.log('Printing delivery note')
}

const editOrder = () => {
    console.log('Editing order')
}

onMounted(() => {
    viewOrderDetail()
})
const showConfirm = ref(false)
const confirmMessage = ref('')
let confirmCallback = null

function openConfirm(message, callback) {
    confirmMessage.value = message
    confirmCallback = callback
    showConfirm.value = true
}

function handleConfirm() {
    if (confirmCallback) confirmCallback()
    showConfirm.value = false
}
</script>

<style scoped src="@/style/GiaoHang/GiaoHangProcessing.css"></style>