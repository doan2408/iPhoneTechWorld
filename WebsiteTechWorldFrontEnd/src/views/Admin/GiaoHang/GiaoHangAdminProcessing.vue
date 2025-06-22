<template>
    <div class="delivery-tracking-container">
        <!-- Order Header -->
        <div class="order-header">
            <div class="order-info">
                <h1>Đơn hàng #{{ order.maVanDon }} </h1>
                <p class="order-date">
                    <Clock class="icon-small" />
                    Ngày đặt: {{ formatDate(order.ngayDatHang) }}
                </p>
            </div>
            <div class="order-summary">
                <div class="total-info">
                    <span class="total-label">Tổng tiền</span>
                    <span class="total-amount">{{ formatCurrency(order.thanhTien) }}</span>
                </div>
                <span :class="'status status-' + getStatusKey(order.trangThaiDonHang)">
                    {{ getStatusText(order.trangThaiDonHang) }}
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
                                <h4>{{ order.tenNguoiNhan }}</h4>
                                <span class="customer-type">{{ order.maKhachHang }} *Hạng</span>
                            </div>
                        </div>
                        <div class="contact-info">
                            <div class="contact-item">
                                <Phone class="icon-small" />
                                <span>{{ order.sdtNguoiNhan }}</span>
                                <button @click="callCustomer" class="contact-btn">Gọi</button>
                            </div>
                            <div class="contact-item">
                                <Mail class="icon-small" />
                                <span>{{ order.sdtNguoiNhan }} *Email</span>
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
                                <label>Trạng thái</label>
                                <span :class="'payment-status ' + getPaymentStatusClass(order.trangThaiThanhToan)">
                                    {{ getPaymentStatusText(order.trangThaiThanhToan) }}
                                </span>
                            </div>
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
                                <p v-else class="no-driver">Chưa phân công</p>
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

        <!-- Action Buttons -->
        <div class="actions-section">
            <div class="left-actions">
                <button v-if="canConfirm" @click="updateOrderStatus('Đã đóng gói')" class="action-btn confirm-btn">
                    <CheckCircle class="icon-small" />
                    XÁC NHẬN ĐÓNG GÓI
                </button>

                <button v-if="canCancel" @click="updateOrderStatus('Đã trả lại')" class="action-btn cancel-btn">
                    <X class="icon-small" />
                    HỦY ĐƠN
                </button>

                <!-- <button v-if="order.trangThaiDonHang === 'Chờ xử lý'" @click="updateOrderStatus('Đã đóng gói')"
                    class="action-btn prepare-btn">
                    <Clock class="icon-small" />
                    BẮT ĐẦU CHUẨN BỊ
                </button> -->

                <button v-if="order.trangThaiDonHang === 'Đã đóng gói'" @click="updateOrderStatus('Đang giao')"
                    class="action-btn ship-btn">
                    <Truck class="icon-small" />
                    BẮT ĐẦU GIAO HÀNG
                </button>

                <button v-if="order.trangThaiDonHang === 'Đang giao'" @click="updateOrderStatus('Đã giao')"
                    class="action-btn complete-btn">
                    <CheckCircle class="icon-small" />
                    HOÀN THÀNH
                </button>
            </div>

            <div class="right-actions">
                <button @click="printInvoice" class="action-btn print-btn">
                    <Printer class="icon-small" />
                    IN HÓA ĐƠN
                </button>

                <button @click="printDeliveryNote" class="action-btn delivery-btn">
                    <FileText class="icon-small" />
                    IN PHIẾU GIAO HÀNG
                </button>

                <button @click="editOrder" class="action-btn edit-btn">
                    <Edit class="icon-small" />
                    CHỈNH SỬA
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import {
    Clock,
    User,
    Phone,
    Mail,
    MapPin,
    Package,
    CreditCard,
    CheckCircle,
    X,
    Truck,
    Printer,
    FileText,
    Edit
} from 'lucide-vue-next'
import { hoaDonDetail } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices'
import { changeStatusOrder } from '@/Service/Adminservice/GiaoHang/GiaoHangServices'
import { useRoute } from 'vue-router'
import { id } from 'element-plus/es/locales.mjs'

const order = reactive({})
const route = useRoute()
const statusUpdate = null;



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

// ham cap nha tien do giao dien giao hang
const orderSteps = computed(() => [
    {
        id: 1,
        title: 'Đặt Hàng Thành Công',
        icon: FileText,
        // Bước đầu luôn hiện là completed (hoặc current nếu mới tạo)
        status: order.trangThaiDonHang === 'Chờ xử lý' ? 'current' :
            ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: order.ngayDatHang,
        description: 'Khách hàng đặt hàng qua ứng dụng'
    },
    {
        id: 2,
        title: 'Đã Đóng Gói',
        icon: Package,
        status: order.trangThaiDonHang === 'Đã đóng gói' ? 'current' :
            ['Đang giao', 'Đã giao', 'Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? order.packedAt : null,
        description: ['Đã đóng gói', 'Đang giao', 'Đã giao', 'Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? 'Đơn hàng đã được đóng gói' : null
    },
    {
        id: 3,
        title: 'Đang Giao Hàng',
        icon: Truck,
        status: order.trangThaiDonHang === 'Đang giao' ? 'current' :
            ['Đã giao', 'Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: ['Đang giao', 'Đã giao', 'Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? order.shippingAt : null,
        description: ['Đang giao', 'Đã giao', 'Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? `${order.driver?.name} đang giao hàng` : null
    },
    {
        id: 4,
        title: 'Giao Hàng Thành Công',
        icon: CheckCircle,
        status: order.trangThaiDonHang === 'Đã giao' ? 'current' :
            ['Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? 'completed' : 'pending',
        timestamp: order.trangThaiDonHang === 'Đã giao' ? order.deliveredAt : null,
        description: order.trangThaiDonHang === 'Đã giao' ? 'Khách hàng đã nhận hàng' : null
    },
    {
        id: 5,
        title: 'Giao Thất Bại / Trả Lại',
        icon: CheckCircle,
        status: ['Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? 'current' : 'pending',
        timestamp: ['Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? order.failedAt : null,
        description: ['Giao thất bại', 'Đã trả lại'].includes(order.trangThaiDonHang) ? 'Đơn hàng bị trả lại hoặc giao thất bại' : null
    }
])
// bien trang thai
const canConfirm = computed(() => order.trangThaiDonHang === 'Chờ xử lý');

const canCancel = computed(() =>
    ['Chờ xử lý', 'Đã xác nhận'].includes(order.trangThaiDonHang)
);

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
        pending: ['Chờ xử lý', 'Chờ xác nhận', 'Đang chờ'],
        packed: ['Đã đóng gói', 'Đóng gói xong'],
        shipping: ['Đang giao', 'Đang vận chuyển'],
        delivered: ['Đã giao', 'Giao thành công'],
        failed: ['Giao thất bại', 'Giao không thành công'],
        returned: ['Đã trả lại', 'Trả hàng'],
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
        COMPLETED: 'Đã thanh toán',
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
        COMPLETED: 'paid',
        CANCELLED: 'cancelled',
        REFUNDED: 'refunded'
    };

    return statusClassMap[status.toUpperCase()] || 'pending';
};

const updateOrderStatus = async (newStatus) => {
    const id = route.params.id
    try {
        console.log(id)
        console.log(newStatus)
        const response = await changeStatusOrder(id, newStatus)
        // Cập nhật trạng thái sau khi API thành công
        order.trangThaiDonHang = newStatus
        console.log(response.data)
        console.log(`Order status updated to: ${newStatus}`)
    } catch (error) {
        console.error('Failed to update order status:', error.response?.data || error.message)
    }
}

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
</script>

<style scoped src="@/style/GiaoHang/GiaoHangProcessing.css">

</style>