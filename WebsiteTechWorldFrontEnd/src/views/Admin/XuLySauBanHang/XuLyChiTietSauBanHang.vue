<template>
    <div class="processing-detail">
        <!-- Header -->
        <div class="page-header">
            <div class="header-content">
                <button class="back-btn" @click="goBack">
                    ← Quay lại
                </button>
                <div class="header-info">
                    <h1>Xử lý đơn hàng #{{ orderInformation.maHoaDon }}</h1>
                    <span class="order-status" :class="orderInformation.trangThaiDonHang">{{
                        getStatusText(orderInformation.trangThaiDonHang) }}</span>
                </div>
            </div>
        </div>

        <div class="main-content">
            <!-- Order Information -->
            <div class="info-section">
                <h2>Thông tin đơn hàng</h2>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Mã vận đơn:</label>
                        <span>{{ orderInformation.maVanDon }}</span>
                    </div>
                    <div class="info-item">
                        <label>Khách hàng:</label>
                        <span>{{ orderInformation.tenKhachHang || 'Khách vãng lai' }}</span>
                    </div>
                    <div class="info-item">
                        <label>Số điện thoại:</label>
                        <span>{{ orderInformation.sdtNguoiNhan }}</span>
                    </div>
                    <div class="info-item">
                        <label>Địa chỉ giao hàng:</label>
                        <span>{{ orderInformation.diaChiGiaoHang }}</span>
                    </div>
                    <div class="info-item">
                        <label>Ngày đặt hàng:</label>
                        <span>{{ formatDate(orderInformation.ngayDatHang) }}</span>
                    </div>
                    <div class="info-item">
                        <label>Giá trị đơn hàng:</label>
                        <span class="price">{{ formatPrice(orderInformation.thanhTien) }}</span>
                    </div>
                </div>
            </div>

            <div class="imei-section">
                <div class="section-header">
                    <h2>Danh sách IMEI</h2>
                    <div class="header-actions">
                        <button class="btn-select-all" @click="selectAll">
                            {{ allSelected ? 'Bỏ chọn tất cả' : 'Chọn tất cả' }}
                        </button>
                        <span class="selected-count">{{ selectedImeis.length }}/{{ orderProduct.length }} đã chọn</span>
                    </div>
                </div>

                <div class="table-toolbar">
                    <div class="bulk-actions-top">
                        <span class="toolbar-label">Xử lý hàng loạt ({{ selectedImeis.length }} đã chọn):</span>
                        <div class="toolbar-buttons">
                            <button class="toolbar-btn retry" @click="processBulk('retry')"
                                :disabled="selectedImeis.length === 0" title="Giao lại">
                                🚚 Giao lại
                            </button>

                            <button class="toolbar-btn cancel" @click="processBulk('cancel')"
                                :disabled="selectedImeis.length === 0" title="Hủy bỏ">
                                ❌ Hủy bỏ
                            </button>

                            <button class="toolbar-btn return" @click="processBulk('return-to-stock')"
                                :disabled="selectedImeis.length === 0" title="Trả kho">
                                📦 Trả kho
                            </button>

                            <button class="toolbar-btn refund" @click="processBulk('refund')"
                                :disabled="selectedImeis.length === 0" title="Hoàn tiền">
                                💰 Hoàn tiền
                            </button>
                        </div>
                    </div>

                    <div class="export-actions">
                        <button class="export-btn" @click="exportSelected" :disabled="selectedImeis.length === 0">
                            📄 Xuất danh sách
                        </button>
                    </div>
                </div>

                <!-- Simplified container layout without side panel -->
                <div class="imei-table-container">
                    <div class="imei-table-wrapper">
                        <table class="imei-table">
                            <thead>
                                <tr>
                                    <th class="checkbox-col">
                                        <input type="checkbox" :checked="allSelected" @change="selectAll"
                                            class="header-checkbox">
                                    </th>
                                    <th>IMEI</th>
                                    <th>Sản phẩm</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="imei in orderProduct" :key="imei.idXlbh" class="imei-row">
                                    <td class="checkbox-col">
                                        <input type="checkbox" :value="imei.soImei" v-model="selectedImeis"
                                            class="row-checkbox">
                                    </td>
                                    <td class="imei-code">{{ imei.soImei }}</td>
                                    <td class="product-name">{{ imei.tenSanPham +' '+ imei.mau +' '+ imei.dungLuong }}
                                    </td>
                                    <td>
                                        <span class="imei-status" :class="imei.trangThaiDon">
                                            {{ getImeiStatusText(imei.trangThaiDon) }}
                                        </span>
                                    </td>
                                    <td class="action-col">
                                        <div class="row-actions">
                                            <!-- <button class="action-btn retry"
                                                @click="openConfirm('Bạn có chắc chắn muốn dữ lại?', () => processImei(imei.soImei,'hold'))"
                                                title="Dữ lại">
                                                ♻️
                                            </button> -->

                                            <button class="action-btn cancel"
                                                @click="openConfirm('Bạn có chắc chắn muốn hủy yêu cầu này?', () => processImei(imei.soImei, 'cancel'))"
                                                title="Hủy bỏ">
                                                ❌
                                            </button>

                                            <button class="action-btn return"
                                                @click="openConfirm('Bạn có chắc chắn muốn gửi yêu cầu nhập kho?', () => processImei(imei.soImei, 'return_to_stock'))"
                                                title="Trả kho">
                                                📦
                                            </button>

                                            <button class="action-btn refund"
                                                @click="openConfirm('Bạn có chắc chắn muốn xác nhận là đã hoàn tiền?', () => processImei(imei.soImei, 'refund'))"
                                                title="Hoàn tiền">
                                                💰
                                            </button>
                                            <ConfirmModal v-if="showConfirm" :message="confirmMessage"
                                                @confirm="handleConfirm" @cancel="showConfirm = false" />
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="mt-4">
                            <strong>Số tiền cần trả:</strong>
                            {{ formatPrice(orderProduct[0]?.soTienHoan) }}
                        </div>
                    </div>
                </div>
            </div>

            <!-- Customer Contact -->
            <div class="contact-section">
                <h2>Thông tin liên hệ khách hàng</h2>
                <div class="contact-grid">
                    <div class="contact-item">
                        <label>Email:</label>
                        <span>{{ orderInformation.emailNguoiNhan }}</span>
                        <button class="btn-link" @click="sendEmail">Gửi email</button>
                    </div>
                    <div class="contact-item">
                        <label>Zalo:</label>
                        <span>{{ orderInformation.sdtNguoiNhan }}</span>
                        <button class="btn-link" @click="openZalo">Mở Zalo</button>
                    </div>
                    <div class="contact-item">
                        <label>Ghi chú khách hàng:</label>
                        <span>{{ orderInformation.customerNote }}</span>
                    </div>
                </div>
            </div>

            <!-- Warranty Tracking -->
            <div class="warranty-section">
                <h2>Theo dõi bảo hành ( Mẫu )</h2>
                <div class="warranty-grid">
                    <div class="warranty-item">
                        <label>Thời gian bảo hành còn lại:</label>
                        <span class="warranty-time">8 tháng 15 ngày</span>
                    </div>
                    <div class="warranty-item">
                        <label>Trung tâm bảo hành gần nhất:</label>
                        <span>123 Đường XYZ, Quận 3, TP.HCM</span>
                        <button class="btn-link" @click="viewWarrantyCenter">Xem chi tiết</button>
                    </div>
                    <div class="warranty-item">
                        <label>Lịch sử bảo hành:</label>
                        <div class="warranty-history">
                            <div class="history-item">
                                <span class="history-date">10/01/2024</span>
                                <span class="history-content">Thay màn hình - Hoàn thành</span>
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
import { getAllCtXuLy, changeStatusPending } from '@/Service/GuestService/ActionAfterCaseService/ActionAfterCaseServices'
import { useRoute } from 'vue-router'
import { hoaDonDetailGuest } from '@/Service/ClientService/HoaDon/MyOrderClient'
import ConfirmModal from '@/views/Popup/ConfirmModal.vue'
import router from '@/router'

const route = useRoute()
const idHoaDon = route.params.idHoaDon

const orderInformation = ref([])
const orderInformations = async () => {
    const res = await hoaDonDetailGuest(idHoaDon);
    orderInformation.value = res.data
}

const orderProduct = ref([])

const orderSanPham = async () => {
    const res = await getAllCtXuLy(idHoaDon);
    orderProduct.value = res.data
}

const selectedAction = ref('')
const deliveryDate = ref('')
const contactMethod = ref('phone')
const refundAmount = ref(0)
const processingNote = ref('')
const selectedImeis = ref([])
const uploadedFiles = ref([])

const allSelected = computed(() => {
    return selectedImeis.value.length === orderProduct.value.length
})

const getStatusText = (status) => {
    const statusMap = {
        'delivery-failed': 'Giao hàng thất bại',
        'return-request': 'Yêu cầu trả hàng',
        'processing': 'Đang xử lý',
        'completed': 'Hoàn thành'
    }
    return statusMap[status] || status
}

const getImeiStatusText = (status) => {
    const statusMap = {
        'active': 'Hoạt động',
        'returned': 'Đã trả',
        'warranty': 'Bảo hành'
    }
    return statusMap[status] || status
}

const formatPrice = (price) => {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(price)
}

const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const selectAll = () => {
    if (allSelected.value) {
        selectedImeis.value = []
    } else {
        selectedImeis.value = orderProduct.value.map(imei => imei.soImei)
    }
}

const exportSelected = () => {
    if (selectedImeis.value.length === 0) return

    const exportData = selectedImeis.value.join('\n')
    const blob = new Blob([exportData], { type: 'text/plain' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `imei_export_${order.id}.txt`
    a.click()
    window.URL.revokeObjectURL(url)
}

const sendEmail = () => {
    window.open(`mailto:${order.email}?subject=Về đơn hàng ${order.id}`)
}

const openZalo = () => {
    window.open(`https://zalo.me/${order.zalo}`)
}

const cancelOrder = () => {
    selectedAction.value = 'cancel'
}

const returnToStock = () => {
    selectedAction.value = 'return-to-stock'
}

const processRefund = () => {
    selectedAction.value = 'refund'
    refundAmount.value = order.amount
}

const processExchange = () => {
    selectedAction.value = 'exchange'
}

const viewWarrantyCenter = () => {
    console.log('[v0] Opening warranty center details')
    alert('Mở thông tin trung tâm bảo hành')
}

const goBack = () => {
    router.push(`/admin/handle`)
}

const saveDraft = () => {
    console.log('[v0] Saving draft with note:', processingNote.value)
    alert('Đã lưu nháp thành công!')
}

const processOrder = () => {
    if (!selectedAction.value) {
        alert('Vui lòng chọn hành động xử lý!')
        return
    }

    console.log('[v0] Processing order with action:', selectedAction.value)
    console.log('[v0] Processing note:', processingNote.value)

    // Reset form
    selectedAction.value = ''
    processingNote.value = ''
    deliveryDate.value = ''
    refundAmount.value = 0

    alert('Đã xử lý đơn hàng thành công!')
}

const getActionText = (action) => {
    const actionMap = {
        'retry': 'RETRY - Giao lại hàng',
        'cancel': 'CANCEL - Hủy bỏ đơn hàng',
        'return-to-stock': 'RETURN_TO_STOCK - Trả lại kho',
        'refund': 'REFUND - Hoàn tiền',
        'exchange': 'EXCHANGE - Trao đổi'
    }
    return actionMap[action] || action
}

const processBulk = async (action) => {
    if (selectedImeis.value.length === 0) {
        alert('Vui lòng chọn ít nhất một IMEI!')
        return
    }
    console.log(selectedImeis.value);
    console.log(`[v0] Processing ${selectedImeis.value.length} IMEIs with action: ${action}`)
    alert(`Xử lý ${selectedImeis.value.length} IMEI với hành động: ${getActionText(action)}`)

    switch (action) {
        case 'retry':
            retryDelivery()
            break
        default:
            alert(`Hành động ${action} chưa được hỗ trợ!`)
    }

    selectedImeis.value = []
}


const retryDelivery = () => {
    selectedAction.value = 'retry'
    deliveryDate.value = new Date(Date.now() + 86400000).toISOString().split('T')[0]
}

const processImei = async (imeiCode, action) => {
    console.log(`[v0] Processing IMEI ${imeiCode} with action: ${action}`)
    const status = action.toUpperCase()    
    const res = await changeStatusPending(imeiCode,status)
    orderSanPham()
    orderInformations()
}

function formatDate(date) {
    if (!date) return "";
    return new Intl.DateTimeFormat("vi-VN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
    }).format(new Date(date));
}

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

onMounted( async () => {
    orderSanPham();
    orderInformations();
})
</script>

<style scoped>
.processing-detail {
    min-height: 100vh;
    background-color: #f8fafc;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.page-header {
    background: white;
    border-bottom: 1px solid #e2e8f0;
    padding: 1rem 0;
    position: sticky;
    top: 0;
    z-index: 100;
}

.header-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
    display: flex;
    align-items: center;
    gap: 1rem;
}

.back-btn {
    background: #f1f5f9;
    border: 1px solid #cbd5e1;
    padding: 0.5rem 1rem;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    transition: all 0.2s;
}

.back-btn:hover {
    background: #e2e8f0;
}

.header-info {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.header-info h1 {
    margin: 0;
    font-size: 1.5rem;
    color: #1e293b;
}

.order-status {
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 500;
}

.order-status.delivery-failed {
    background: #fef2f2;
    color: #dc2626;
}

.order-status.return-request {
    background: #fef3c7;
    color: #d97706;
}

.order-status.processing {
    background: #dbeafe;
    color: #2563eb;
}

.main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem 1rem;
    display: flex;
    flex-direction: column;
    gap: 2rem;
}

.info-section,
.imei-section,
.contact-section,
.warranty-section {
    background: white;
    border-radius: 8px;
    padding: 1.5rem;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.info-section h2,
.imei-section h2,
.contact-section h2,
.warranty-section h2 {
    margin: 0 0 1rem 0;
    color: #1e293b;
    font-size: 1.2rem;
    border-bottom: 2px solid #3b82f6;
    padding-bottom: 0.5rem;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1rem;
}

.info-item {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
}

.info-item label {
    font-weight: 600;
    color: #64748b;
    font-size: 0.9rem;
}

.info-item span {
    color: #1e293b;
}

.price {
    font-weight: 600;
    color: #059669;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
}

.header-actions {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.btn-select-all {
    background: #3b82f6;
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    transition: all 0.2s;
}

.btn-select-all:hover {
    background: #2563eb;
}

.selected-count {
    font-size: 0.9rem;
    color: #64748b;
    font-weight: 500;
}

.imei-container {
    display: flex;
    gap: 2rem;
    align-items: flex-start;
}

.imei-table-wrapper {
    flex: 1;
    overflow-x: auto;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
}

.imei-table {
    width: 100%;
    border-collapse: collapse;
    background: white;
}

.imei-table th {
    background: #f8fafc;
    padding: 1rem;
    text-align: left;
    font-weight: 600;
    color: #374151;
    border-bottom: 2px solid #e2e8f0;
}

.imei-table td {
    padding: 1rem;
    border-bottom: 1px solid #f1f5f9;
}

.checkbox-col {
    width: 50px;
    text-align: center;
}

.header-checkbox,
.row-checkbox {
    width: 18px;
    height: 18px;
    cursor: pointer;
}

.imei-row:hover {
    background: #f8fafc;
}

.imei-code {
    font-family: 'Courier New', monospace;
    font-weight: 600;
    color: #1e293b;
    font-size: 0.9rem;
}

.product-name {
    color: #64748b;
}

.action-col {
    width: 200px;
}

.row-actions {
    display: flex;
    gap: 0.5rem;
}

.action-btn {
    width: 32px;
    height: 32px;
    border: 1px solid #e2e8f0;
    background: white;
    border-radius: 6px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 0.9rem;
    transition: all 0.2s;
}

.action-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.action-btn.retry:hover {
    border-color: #059669;
    background: #f0fdf4;
}

.action-btn.cancel:hover {
    border-color: #dc2626;
    background: #fef2f2;
}

.action-btn.return:hover {
    border-color: #7c3aed;
    background: #faf5ff;
}

.action-btn.refund:hover {
    border-color: #d97706;
    background: #fffbeb;
}

.action-btn.exchange:hover {
    border-color: #3b82f6;
    background: #eff6ff;
}

.table-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem;
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    margin-bottom: 1rem;
    flex-wrap: wrap;
    gap: 1rem;
}

.bulk-actions-top {
    display: flex;
    align-items: center;
    gap: 1rem;
    flex-wrap: wrap;
}

.toolbar-label {
    font-weight: 500;
    color: #374151;
    font-size: 0.9rem;
}

.toolbar-buttons {
    display: flex;
    gap: 0.5rem;
    flex-wrap: wrap;
}

.toolbar-btn {
    padding: 0.5rem 1rem;
    border: 1px solid #e2e8f0;
    background: white;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.85rem;
    font-weight: 500;
    transition: all 0.2s;
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.toolbar-btn:hover:not(:disabled) {
    transform: translateY(-1px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.toolbar-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.toolbar-btn.retry:hover:not(:disabled) {
    border-color: #059669;
    background: #f0fdf4;
}

.toolbar-btn.cancel:hover:not(:disabled) {
    border-color: #dc2626;
    background: #fef2f2;
}

.toolbar-btn.return:hover:not(:disabled) {
    border-color: #7c3aed;
    background: #faf5ff;
}

.toolbar-btn.refund:hover:not(:disabled) {
    border-color: #d97706;
    background: #fffbeb;
}

.toolbar-btn.exchange:hover:not(:disabled) {
    border-color: #3b82f6;
    background: #eff6ff;
}

.export-actions {
    display: flex;
    gap: 0.5rem;
}

.export-btn {
    padding: 0.5rem 1rem;
    background: #6366f1;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 500;
    font-size: 0.85rem;
    transition: all 0.2s;
}

.export-btn:hover:not(:disabled) {
    background: #5b21b6;
    transform: translateY(-1px);
}

.export-btn:disabled {
    background: #9ca3af;
    cursor: not-allowed;
}

.imei-table-container {
    width: 100%;
}

.imei-table-wrapper {
    overflow-x: auto;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
}

.contact-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1rem;
}

.contact-item {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    padding: 1rem;
    border: 1px solid #e2e8f0;
    border-radius: 6px;
}

.contact-item label {
    font-weight: 600;
    color: #64748b;
    font-size: 0.9rem;
}

.btn-link {
    background: none;
    border: none;
    color: #3b82f6;
    cursor: pointer;
    font-size: 0.9rem;
    text-decoration: underline;
    padding: 0;
    align-self: flex-start;
}

.btn-link:hover {
    color: #2563eb;
}

.contact-history,
.warranty-history {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.history-item {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
    padding: 0.5rem;
    background: #f8fafc;
    border-radius: 4px;
}

.history-date {
    font-size: 0.8rem;
    color: #64748b;
    font-weight: 500;
}

.history-content {
    color: #1e293b;
    font-size: 0.9rem;
}

.warranty-section {
    background: white;
    border-radius: 8px;
    padding: 1.5rem;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.warranty-section h2 {
    margin: 0 0 1rem 0;
    color: #1e293b;
    font-size: 1.2rem;
    border-bottom: 2px solid #7c3aed;
    padding-bottom: 0.5rem;
}

.warranty-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1rem;
}

.warranty-item {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
    padding: 1rem;
    border: 1px solid #e2e8f0;
    border-radius: 6px;
}

.warranty-time {
    font-weight: 600;
    color: #059669;
}

.actions-section {
    background: white;
    border-radius: 8px;
    padding: 1.5rem;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.actions-section h2 {
    margin: 0 0 1rem 0;
    color: #1e293b;
    font-size: 1.2rem;
    border-bottom: 2px solid #3b82f6;
    padding-bottom: 0.5rem;
}

.action-form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.form-group label {
    font-weight: 600;
    color: #374151;
}

.form-group select,
.form-group input,
.form-group textarea {
    padding: 0.75rem;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    font-size: 0.9rem;
}

.form-group select:focus,
.form-group input:focus,
.form-group textarea:focus {
    outline: none;
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.form-actions {
    display: flex;
    gap: 1rem;
    margin-top: 1rem;
}

.btn-primary,
.btn-secondary {
    padding: 0.75rem 1.5rem;
    border-radius: 6px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s;
    border: none;
}

.btn-primary {
    background: #3b82f6;
    color: white;
}

.btn-primary:hover:not(:disabled) {
    background: #2563eb;
}

.btn-primary:disabled {
    background: #9ca3af;
    cursor: not-allowed;
}

.btn-secondary {
    background: #f1f5f9;
    color: #475569;
    border: 1px solid #cbd5e1;
}

.btn-secondary:hover {
    background: #e2e8f0;
}

@media (max-width: 1024px) {
    .imei-container {
        flex-direction: column;
    }

    .bulk-actions-panel {
        width: 100%;
        position: static;
    }

    .bulk-action-buttons {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 0.75rem;
    }
}

@media (max-width: 768px) {
    .header-content {
        flex-direction: column;
        align-items: flex-start;
        gap: 0.5rem;
    }

    .info-grid,
    .contact-grid {
        grid-template-columns: 1fr;
    }

    .form-actions,
    .imei-actions {
        flex-direction: column;
    }

    .quick-actions-grid {
        grid-template-columns: 1fr;
    }

    .imei-label {
        flex-direction: column;
        align-items: flex-start;
        gap: 0.5rem;
    }

    .imei-code {
        min-width: auto;
    }

    .section-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 1rem;
    }

    .header-actions {
        width: 100%;
        justify-content: space-between;
    }

    .imei-table-wrapper {
        font-size: 0.8rem;
    }

    .imei-table th,
    .imei-table td {
        padding: 0.5rem;
    }

    .row-actions {
        flex-wrap: wrap;
        gap: 0.25rem;
    }

    .action-btn {
        width: 28px;
        height: 28px;
        font-size: 0.8rem;
    }

    .bulk-action-buttons {
        grid-template-columns: 1fr;
    }

    .table-toolbar {
        flex-direction: column;
        align-items: stretch;
    }

    .bulk-actions-top {
        flex-direction: column;
        align-items: stretch;
    }

    .toolbar-buttons {
        justify-content: center;
    }

    .export-actions {
        justify-content: center;
    }
}
</style>
