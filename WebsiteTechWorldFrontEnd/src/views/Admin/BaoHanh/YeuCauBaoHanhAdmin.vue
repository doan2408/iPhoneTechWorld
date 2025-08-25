<template>
    <div class="warranty-container">
        <div class="header">
            <h1>Hệ Thống Quản Lý Bảo Hành</h1>
        </div>

        <div class="tabs">
            <button :class="['tab-btn', { active: activeTab === 'lookup' }]" @click="activeTab = 'lookup'">
                Tra Cứu & Tạo Đơn
            </button>
            <button :class="['tab-btn', { active: activeTab === 'manage' }]" @click="activeTab = 'manage'">
                Quản Lý Đơn Bảo Hành
            </button>
            <button :class="['tab-btn', { active: activeTab === 'history' }]" @click="activeTab = 'history'">
                Lịch Sử Bảo Hành
            </button>
        </div>

        <!-- Tab Tra Cứu & Tạo Đơn -->
        <div v-if="activeTab === 'lookup'" class="tab-content">
            <div class="search-section">
                <h2>Tra Cứu Thông Tin Bảo Hành</h2>
                <div class="search-form">
                    <input v-model="searchImei" type="text" placeholder="Nhập IMEI (15 số)" class="imei-input"
                        maxlength="15">
                    <button @click="searchWarranty" class="search-btn">Tra Cứu</button>
                </div>
            </div>

            <!-- Hiển thị thông tin sản phẩm -->
            <div v-if="productInfo" class="product-info">
                <h3>Thông Tin Sản Phẩm</h3>
                <div class="product-card">
                    <div class="product-details">
                        <p><strong>IMEI:</strong> {{ productInfo.soImei }}</p>
                        <p><strong>Tên sản phẩm:</strong> {{ productInfo.tenSanPham + ' ' + productInfo.mau + ' ' +
                            productInfo.dungLuong }}</p>
                        <!-- <p><strong>Model:</strong> {{ productInfo.model }}</p> -->
                        <p><strong>Ngày mua:</strong> {{ formatDate(productInfo.ngayMuaHang) }}</p>
                    </div>

                    <div class="warranty-types">
                        <h4>Các Loại Bảo Hành Có Sẵn:</h4>
                        <div class="warranty-list">
                            <div v-if="productInfo.lstBaoHanh && productInfo.lstBaoHanh.length > 0">
                                <div v-for="warranty in productInfo.lstBaoHanh" :key="warranty.idBaoHanh"
                                    class="warranty-item"
                                    :class="{ expired: warranty.trangThaiBaoHanh === 'WARRANTY_EXPIRED' }">
                                    <div class="warranty-info">
                                        <h5>{{ warranty.tenLoaiBaoHanh }}</h5>
                                        <p>Thời hạn: {{ warranty.thoiGianThang }} tháng</p>
                                        <p>Hết hạn: {{ warranty.ngayHetHan }}</p>
                                        <span :class="['status', warranty.trangThaiBaoHanh]">
                                            {{ warranty.trangThaiBaoHanh === 'UNDER_WARRANTY' ? 'Còn bảo hành' : 'Hết bảo hành' }}
                                        </span>
                                    </div>

                                    <button v-if="warranty.trangThaiBaoHanh === 'UNDER_WARRANTY'"
                                        @click="selectWarrantyType(warranty)" class="select-warranty-btn">
                                        Chọn
                                    </button>
                                </div>
                            </div>

                            <!-- Nếu không có bảo hành -->
                            <div v-else class="no-warranty">
                                <p style="color: red;">Không có loại bảo hành khả dụng</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Form tạo đơn bảo hành -->
            <div v-if="selectedWarranty" class="create-warranty-form">
                <h3>Tạo Đơn Bảo Hành</h3>
                <div class="form-card">
                    <div class="selected-warranty">
                        <p><strong>Loại bảo hành đã chọn:</strong> {{ selectedWarranty.tenLoaiBaoHanh }}</p>
                    </div>

                    <div class="form-group">
                        <label>Mô tả lỗi:</label>
                        <textarea v-model="warrantyForm.description" placeholder="Mô tả chi tiết lỗi của sản phẩm"
                            rows="4"></textarea>
                        <p v-if="fieldErrors.lyDoBaoHanh" class="error-reason">{{ fieldErrors.lyDoBaoHanh }}</p>
                    </div>

                    <div class="form-actions">
                        <button @click="createWarranty" class="create-btn">Tạo Đơn Bảo Hành</button>
                        <button @click="cancelWarranty" class="cancel-btn">Hủy</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tab Quản Lý Đơn Bảo Hành -->
        <div v-if="activeTab === 'manage'" class="tab-content">
            <h2>Danh Sách Đơn Bảo Hành</h2>
            <br>
            <div class="table-container">
                <table class="warranty-table">
                    <thead>
                        <tr>
                            <th>Mã Đơn</th>
                            <th>IMEI</th>
                            <th>Sản Phẩm</th>
                            <th>Loại BH</th>
                            <th>Khách Hàng</th>
                            <th>Ngày Tiếp Nhận</th>
                            <th>Ngày Hoàn Thành</th>
                            <th>Trạng Thái</th>
                            <th>Thao Tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="warranty in warrantyOrders" :key="warranty.idLsbh" class="table-row">
                            <td>{{ warranty.idLsbh }}</td>
                            <td>{{ warranty.soImei }}</td>
                            <td>{{ warranty.tenSanPham + '-' + warranty.mau + '-' + warranty.rom }}</td>
                            <td>{{ warranty.loaiBaoHanh }}</td>
                            <td>{{ warranty.tenKhachHang }}</td>
                            <td>{{ warranty.ngayTiepNhan }}</td>
                            <td>{{ warranty.ngayHoanThanh || 'Đơn hàng đang được xử lý'}}</td>
                            <td>
                                <span :class="['status-badge', warranty.status]">
                                    {{ warranty.trangThai === 'IN_REPAIR' ? 'Đang sửa chữa' : 'Đã sửa chữa' }}
                                </span>
                            </td>
                            <td>
                                <button v-if="warranty.trangThai === 'IN_REPAIR'"
                                    @click="completeWarranty(warranty.idLsbh)" class="complete-btn">
                                    Hoàn Thành
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <br>
                <div class="pagination-controls" style="margin-top: 0;">
                    <button @click="previousPage()" :disabled="pageNo === 0">Trước</button>
                    <span>Trang {{ pageNo + 1 }} / {{ totalPages }}</span>
                    <button @click="nextPageKH()" :disabled="pageNo >= totalPages - 1">Sau</button>
                </div>
                <br>
            </div>
        </div>

        <!-- Tab Lịch Sử Bảo Hành -->
        <div v-if="activeTab === 'history'" class="tab-content">
            <h2>Lịch Sử Bảo Hành</h2>
            <br>
            <div class="history-search">
                <input v-model="historyImei" type="text" placeholder="Nhập IMEI để xem lịch sử" class="imei-input">
                <button @click="searchHistory" class="search-btn">Tìm Kiếm</button>
            </div>

            <div v-if="historyData.length > 0" class="history-results">
                <h3>Lịch Sử Bảo Hành - IMEI: {{ historyImei }}</h3>
                <div class="history-list">
                    <div v-for="(item, index) in historyData" :key="item.idLsbh" class="history-item">
                        <div class="history-header">
                            <span class="order-id">Lần #{{ index + 1 }}</span>
                            <span :class="['status-badge', item.status]">
                                {{ item.status === 'IN_REPAIR' ? 'Đang sửa chữa' : 'Đã sửa chữa' }}
                            </span>
                        </div>
                        <div class="history-details">
                            <p><strong>Loại bảo hành:</strong> {{ item.loaiBaoHanh }}</p>
                            <p><strong>Mô tả:</strong> {{ item.moTa }}</p>
                            <p><strong>Ngày yêu cầu:</strong> {{ item.ngayYeuCau }}</p>
                            <p v-if="item.ngayHoanThanh"><strong>Ngày hoàn thành:</strong> {{ item.ngayHoanThanh }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <!-- Toast Notification -->
        <div v-if="showToast" :class="['toast', toastType]">
            {{ toastMessage }}
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { checkedWarranty, createRequestWarranty, findDonBaoHanh, findHistoryBaoHanh, hoanThanhDon } from '@/Service/Adminservice/BaoHanh/BaoHanhService'
import { tr } from 'element-plus/es/locales.mjs'
import { useToast } from 'vue-toastification'

const activeTab = ref('lookup')
const searchImei = ref('')
const productInfo = ref(null)
const selectedWarranty = ref(null)
const warrantyForm = reactive({
    description: ''
})
const warrantyOrders = ref([])
const historyImei = ref('')
const historyData = ref([])
const showDetailModal = ref(false)
const selectedOrder = ref(null)
const showToast = ref(false)
const toastMessage = ref('')
const toastType = ref('success')
const pageNo = ref(0)
const pageSize = ref(5)
const totalPages = ref(0)
const toast = useToast()

// Hàm tiện ích Toast
function showToastMsg(message, type = 'success') {
    toastMessage.value = message
    toastType.value = type
    showToast.value = true
    setTimeout(() => { showToast.value = false }, 3000)
}

// ===== METHODS =====
async function searchWarranty() {
    productInfo.value = null
    selectedWarranty.value = null

    if (!searchImei.value) {
        toast.warning('Vui lòng nhập IMEI')
        return
    }

    if (searchImei.value.length !== 15 || !/^\d+$/.test(searchImei.value)) {
        toast.warning('IMEI phải có đúng 15 số')
        return
    }

    const res = await checkedWarranty(searchImei.value);
    if (!res || res.data === '') {
        toast.warning('Không tìm thấy thông tin sản phẩm với IMEI này')
        return
    }

    productInfo.value = res.data
    toast.success('Tìm thấy thông tin sản phẩm!')
}

function selectWarrantyType(warranty) {
    selectedWarranty.value = warranty
    // showToastMsg(`Đã chọn ${warranty.type}`, 'success')
    toast.success("Đã chọn loại bảo hành "+ selectedWarranty.value.tenLoaiBaoHanh)
}

const fieldErrors = ref({})

async function createWarranty() {
    fieldErrors.value = {} // reset lỗi cũ

    try {
        const newWarranty = {
            idBaoHanh: selectedWarranty.value.idBaoHanh,
            lyDoBaoHanh: warrantyForm.description,
        }

        await createRequestWarranty(newWarranty)

        // Reset form
        productInfo.value = null
        selectedWarranty.value = null
        searchImei.value = ''
        warrantyForm.description = ''
        loadWarrantyOrders(pageNo.value)
        toast.success('Tạo đơn bảo hành thành công!')

    } catch (error) {
        if (error.response?.status === 400 && error.response.data) {
            fieldErrors.value = error.response.data
            if (fieldErrors.value.idBaoHanh) {
                showToastMsg(fieldErrors.value.idBaoHanh, 'error')
            }
        } else {
            showToastMsg('Có lỗi xảy ra', 'error')
        }
    }
}

function cancelWarranty() {
    selectedWarranty.value = null
    warrantyForm.description = ''
}

async function completeWarranty(orderId) {
    try {
        await hoanThanhDon(orderId);
        loadWarrantyOrders(pageNo.value)
    } catch (error) {
        console.error(error)
    }
}

async function searchHistory() {
    if (!historyImei.value) {
        showToastMsg('Vui lòng nhập IMEI', 'error')
        return
    }

    const res = await findHistoryBaoHanh(historyImei.value);
    historyData.value = res.data

    if (historyData.value.length === 0) {
        showToastMsg('Không tìm thấy lịch sử bảo hành cho IMEI này', 'error')
    }
}

async function loadWarrantyOrders(pageNoPage) {
    pageNo.value = pageNoPage
    const res = await findDonBaoHanh(pageNo.value,pageSize.value);
    warrantyOrders.value = res.data.content
    totalPages.value = res.data.totalPages
}

const nextPageKH = () => {
    if (pageNo.value < totalPages.value - 1) {
        loadWarrantyOrders(pageNo.value + 1);
    }
};

const previousPage = () => {
    if (pageNo.value > 0) {
        loadWarrantyOrders(pageNo.value - 1);
    }
};

// Lifecycle
onMounted(() => {
    loadWarrantyOrders(pageNo.value)
})

function formatDate(date) {
    if (!date) return "";
    return new Intl.DateTimeFormat("vi-VN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
    }).format(new Date(date));
}
</script>


<style scoped>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

.warranty-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #5c90e2 0%, #b7c49c 100%);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    padding: 20px;
}

.header {
    text-align: center;
    margin-bottom: 30px;
}

.header h1 {
    color: white;
    font-size: 2.5rem;
    font-weight: 600;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.tabs {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
    gap: 10px;
}

.tab-btn {
    padding: 12px 24px;
    border: none;
    background: rgba(255, 255, 255, 0.2);
    color: white;
    border-radius: 25px;
    cursor: pointer;
    font-size: 1rem;
    font-weight: 500;
    transition: all 0.3s ease;
    backdrop-filter: blur(10px);
}

.tab-btn:hover {
    background: rgba(255, 255, 255, 0.3);
    transform: translateY(-2px);
}

.tab-btn.active {
    background: white;
    color: #667eea;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.tab-content {
    max-width: 1200px;
    margin: 0 auto;
}

.search-section {
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    margin-bottom: 30px;
}

.search-section h2 {
    color: #333;
    margin-bottom: 20px;
    font-size: 1.5rem;
}

.search-form {
    display: flex;
    gap: 15px;
    align-items: center;
}

.imei-input {
    flex: 1;
    padding: 15px;
    border: 2px solid #e1e5e9;
    border-radius: 10px;
    font-size: 1rem;
    transition: border-color 0.3s ease;
}

.imei-input:focus {
    outline: none;
    border-color: #667eea;
}

.search-btn {
    padding: 15px 30px;
    background: linear-gradient(135deg, #143adf 0%, #14a8cd 100%);
    color: white;
    border: none;
    border-radius: 10px;
    cursor: pointer;
    font-size: 1rem;
    font-weight: 600;
    transition: all 0.3s ease;
}

.search-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.error-message {
    color: #e74c3c;
    margin-top: 15px;
    padding: 10px;
    background: #ffeaea;
    border-radius: 8px;
    border-left: 4px solid #e74c3c;
}

.product-info {
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    margin-bottom: 30px;
}

.product-info h3 {
    color: #333;
    margin-bottom: 20px;
    font-size: 1.5rem;
}

.product-card {
    border: 1px solid #e1e5e9;
    border-radius: 10px;
    overflow: hidden;
}

.product-details {
    padding: 20px;
    background: #f8f9fa;
    border-bottom: 1px solid #e1e5e9;
}

.product-details p {
    margin-bottom: 10px;
    color: #555;
}

.warranty-types {
    padding: 20px;
}

.warranty-types h4 {
    color: #333;
    margin-bottom: 15px;
}

.warranty-list {
    display: grid;
    gap: 15px;
}

.warranty-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    border: 2px solid #e1e5e9;
    border-radius: 10px;
    transition: all 0.3s ease;
}

.warranty-item:hover {
    border-color: #667eea;
    box-shadow: 0 2px 10px rgba(102, 126, 234, 0.1);
}

.warranty-item.expired {
    opacity: 0.6;
    border-color: #e74c3c;
}

.warranty-info h5 {
    color: #333;
    margin-bottom: 5px;
}

.warranty-info p {
    color: #666;
    font-size: 0.9rem;
    margin-bottom: 3px;
}

.status {
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 600;
}

.status.UNDER_WARRANTY {
    background: #d4edda;
    color: #155724;
}

.status.WARRANTY_EXPIRED {
    background: #f8d7da;
    color: #721c24;
}

.select-warranty-btn {
    padding: 10px 20px;
    background: linear-gradient(135deg, #28a745, #20c997);
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;
}

.select-warranty-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(40, 167, 69, 0.3);
}

.create-warranty-form {
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.create-warranty-form h3 {
    color: #333;
    margin-bottom: 20px;
    font-size: 1.5rem;
}

.form-card {
    border: 1px solid #e1e5e9;
    border-radius: 10px;
    padding: 20px;
}

.selected-warranty {
    background: #e8f5e8;
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 20px;
    border-left: 4px solid #28a745;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 8px;
    color: #333;
    font-weight: 600;
}

.form-group input,
.form-group textarea {
    width: 100%;
    padding: 12px;
    border: 2px solid #e1e5e9;
    border-radius: 8px;
    font-size: 1rem;
    transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
    outline: none;
    border-color: #667eea;
}

.form-actions {
    display: flex;
    gap: 15px;
    justify-content: flex-end;
}

.create-btn {
    padding: 12px 24px;
    background: linear-gradient(135deg, #28a745, #20c997);
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;
}

.create-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(40, 167, 69, 0.3);
}

.cancel-btn {
    padding: 12px 24px;
    background: #6c757d;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.3s ease;
}

.cancel-btn:hover {
    background: #5a6268;
    transform: translateY(-2px);
}

.table-container {
    background: white;
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.warranty-table {
    width: 100%;
    border-collapse: collapse;
    background: white;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.warranty-table th {
    background: #667eea;
    color: white;
    padding: 15px 12px;
    text-align: left;
    font-weight: 600;
    font-size: 14px;
}

.warranty-table td {
    padding: 15px 12px;
    border-bottom: 1px solid #e9ecef;
    font-size: 14px;
}

.table-row {
    transition: background-color 0.3s ease;
}

.table-row:hover {
    background-color: #f8f9fa;
}

.status-badge {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 600;
}

.status-badge.REPAIRED {
    background: #fff3cd;
    color: #856404;
}

.status-badge.IN_REPAIR {
    background: #d4edda;
    color: #155724;
}

.complete-btn {
    padding: 8px 16px;
    background: linear-gradient(135deg, #28a745, #20c997);
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    margin-right: 8px;
    transition: all 0.3s ease;
}

.complete-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 3px 8px rgba(40, 167, 69, 0.3);
}

.detail-btn {
    padding: 8px 16px;
    background: linear-gradient(135deg, #007bff, #0056b3);
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-size: 0.9rem;
    transition: all 0.3s ease;
}

.detail-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 3px 8px rgba(0, 123, 255, 0.3);
}

.history-search {
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    margin-bottom: 30px;
    display: flex;
    gap: 15px;
    align-items: center;
}

.history-results {
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.history-results h3 {
    color: #333;
    margin-bottom: 20px;
}

.history-list {
    display: grid;
    gap: 20px;
}

.history-item {
    border: 1px solid #e1e5e9;
    border-radius: 10px;
    padding: 20px;
    transition: all 0.3s ease;
}

.history-item:hover {
    border-color: #667eea;
    box-shadow: 0 2px 10px rgba(102, 126, 234, 0.1);
}

.history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

.order-id {
    font-weight: 600;
    color: #333;
    font-size: 1.1rem;
}

.history-details p {
    margin-bottom: 8px;
    color: #555;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    border-radius: 15px;
    max-width: 900px;
    width: 95%;
    max-height: 90vh;
    overflow-y: auto;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
    padding: 20px 30px;
}
@media (max-width: 768px) {
    .modal-content {
        max-width: 100%;
        width: 100%;
        border-radius: 0;
    }
}

.modal-content::-webkit-scrollbar {
    width: 6px;
}

.modal-content::-webkit-scrollbar-thumb {
    background-color: #ccc;
    border-radius: 3px;
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 30px;
    border-bottom: 1px solid #e1e5e9;
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: white;
    border-radius: 15px 15px 0 0;
}

.modal-header h3 {
    margin: 0;
}

.close-btn {
    background: none;
    border: none;
    color: white;
    font-size: 1.5rem;
    cursor: pointer;
    padding: 0;
    width: 30px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: background-color 0.3s ease;
}

.close-btn:hover {
    background: rgba(255, 255, 255, 0.2);
}

.modal-body {
    padding: 30px;
}

.detail-section {
    margin-bottom: 25px;
    padding-bottom: 20px;
    border-bottom: 1px solid #e1e5e9;
}

.detail-section:last-child {
    border-bottom: none;
    margin-bottom: 0;
}

.detail-section h4 {
    color: #333;
    margin-bottom: 15px;
    font-size: 1.2rem;
}

.detail-section p {
    margin-bottom: 10px;
    color: #555;
}

.toast {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 15px 25px;
    border-radius: 8px;
    color: white;
    font-weight: 600;
    z-index: 1001;
    animation: slideIn 0.3s ease;
}

.toast.success {
    background: linear-gradient(135deg, #28a745, #20c997);
}

.toast.error {
    background: linear-gradient(135deg, #dc3545, #c82333);
}

@keyframes slideIn {
    from {
        transform: translateX(100%);
        opacity: 0;
    }

    to {
        transform: translateX(0);
        opacity: 1;
    }
}

@media (max-width: 768px) {
    .warranty-container {
        padding: 10px;
    }

    .header h1 {
        font-size: 2rem;
    }

    .tabs {
        flex-direction: column;
        align-items: center;
    }

    .search-form {
        flex-direction: column;
    }

    .warranty-table {
        font-size: 12px;
    }

    .warranty-table th,
    .warranty-table td {
        padding: 10px 8px;
    }

    .form-actions {
        flex-direction: column;
    }

    .modal-content {
        width: 95%;
        margin: 20px;
    }

    .modal-body {
        padding: 20px;
    }
}
.pagination-controls {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
    margin-top: 12px;
    font-size: 14px;
}

/* Pagination - ĐƠN GIẢN */
.pagination {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 15px;
    padding: 15px 0;
}

.page-btn {
    padding: 8px 12px;
    border: 1px solid #ddd;
    border-radius: 6px;
    background: white;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
}

.page-btn:hover:not(:disabled) {
    background: #f8f9fa;
    border-color: #007bff;
}

.page-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    background: #f5f5f5;
}

.page-icon {
    width: 16px;
    height: 16px;
}

.page-info {
    font-size: 14px;
    color: #666;
    font-weight: 500;
    min-width: 60px;
    text-align: center;
}

/* Mobile */
@media (max-width: 480px) {
    .pagination {
        gap: 10px;
        padding: 10px 0;
    }

    .page-btn {
        padding: 6px 10px;
    }

    .page-icon {
        width: 14px;
        height: 14px;
    }

    .page-info {
        font-size: 13px;
        min-width: 50px;
    }
}
.pagination-controls {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 15px;
    /* Thêm padding ngang để căn chỉnh với imei-list */
    padding: 0 10px;
}

.pagination-controls button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 18px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.95em;
    transition: background-color 0.2s ease;
}

.pagination-controls button:hover:not(:disabled) {
    background-color: #0056b3;
}

.pagination-controls button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
    opacity: 0.7;
}

.pagination-controls span {
    font-weight: 500;
    color: #666;
    font-size: 1em;
}
.error-text {
    color: red;
    font-size: 13px;
    margin-top: 4px;
}
.error-reason {
    color: #e74c3c;
    /* Màu đỏ nổi bật */
    font-size: 14px;
    margin-top: 4px;
    display: block;
    font-style: italic;
}
</style>