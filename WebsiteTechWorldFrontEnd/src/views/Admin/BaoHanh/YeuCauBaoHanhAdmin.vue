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

                <div v-if="searchError" class="error-message">
                    {{ searchError }}
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
                        <p><strong>Ngày mua:</strong> {{ productInfo.ngayMuaHang }}</p>
                    </div>

                    <div class="warranty-types">
                        <h4>Các Loại Bảo Hành Có Sẵn:</h4>
                        <div class="warranty-list">
                            <div v-if="productInfo.lstBaoHanh && productInfo.lstBaoHanh.length > 0">
                                <div v-for="warranty in productInfo.lstBaoHanh" :key="warranty.idBaoHanh"
                                    class="warranty-item" :class="{ expired: !warranty.isValid }">

                                    <div class="warranty-info">
                                        <h5>{{ warranty.tenLoaiBaoHanh }}</h5>
                                        <p>Thời hạn: {{ warranty.thoiGianThang }} tháng</p>
                                        <p>Hết hạn: {{ warranty.ngayHetHan }}</p>
                                        <span :class="['status', warranty.trangThaiBaoHanh]">
                                            {{ warranty.trangThaiBaoHanh }}
                                        </span>
                                    </div>

                                    <button v-if="warranty.trangThaiBaoHanh === 'UNDER_WARRANTY'" @click="selectWarrantyType(warranty)"
                                        class="select-warranty-btn">
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
            <div class="table-container">
                <table class="warranty-table">
                    <thead>
                        <tr>
                            <th>Mã Đơn</th>
                            <th>IMEI</th>
                            <th>Sản Phẩm</th>
                            <th>Loại BH</th>
                            <th>Khách Hàng</th>
                            <th>Ngày Tạo</th>
                            <th>Trạng Thái</th>
                            <th>Thao Tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="warranty in warrantyOrders" :key="warranty.id" class="table-row">
                            <td>{{ warranty.id }}</td>
                            <td>{{ warranty.imei }}</td>
                            <td>{{ warranty.productName }}</td>
                            <td>{{ warranty.warrantyType }}</td>
                            <td>{{ warranty.customerName }}</td>
                            <td>{{ warranty.createdDate }}</td>
                            <td>
                                <span :class="['status-badge', warranty.status]">
                                    {{ warranty.status === 'pending' ? 'Đang xử lý' : 'Hoàn thành' }}
                                </span>
                            </td>
                            <td>
                                <button v-if="warranty.status === 'pending'" @click="completeWarranty(warranty.id)"
                                    class="complete-btn">
                                    Hoàn Thành
                                </button>
                                <button @click="viewDetails(warranty)" class="detail-btn">Chi Tiết</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Tab Lịch Sử Bảo Hành -->
        <div v-if="activeTab === 'history'" class="tab-content">
            <h2>Lịch Sử Bảo Hành</h2>
            <div class="history-search">
                <input v-model="historyImei" type="text" placeholder="Nhập IMEI để xem lịch sử" class="imei-input">
                <button @click="searchHistory" class="search-btn">Tìm Kiếm</button>
            </div>

            <div v-if="historyData.length > 0" class="history-results">
                <h3>Lịch Sử Bảo Hành - IMEI: {{ historyImei }}</h3>
                <div class="history-list">
                    <div v-for="item in historyData" :key="item.id" class="history-item">
                        <div class="history-header">
                            <span class="order-id">Đơn #{{ item.id }}</span>
                            <span :class="['status-badge', item.status]">
                                {{ item.status === 'completed' ? 'Hoàn thành' : 'Đang xử lý' }}
                            </span>
                        </div>
                        <div class="history-details">
                            <p><strong>Loại bảo hành:</strong> {{ item.warrantyType }}</p>
                            <p><strong>Mô tả:</strong> {{ item.description }}</p>
                            <p><strong>Ngày tạo:</strong> {{ item.createdDate }}</p>
                            <p v-if="item.completedDate"><strong>Ngày hoàn thành:</strong> {{ item.completedDate }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Chi Tiết -->
        <div v-if="showDetailModal" class="modal-overlay" @click="closeModal">
            <div class="modal-content" @click.stop>
                <div class="modal-header">
                    <h3>Chi Tiết Đơn Bảo Hành #{{ selectedOrder.id }}</h3>
                    <button @click="closeModal" class="close-btn">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="detail-section">
                        <h4>Thông Tin Sản Phẩm</h4>
                        <p><strong>IMEI:</strong> {{ selectedOrder.imei }}</p>
                        <p><strong>Sản phẩm:</strong> {{ selectedOrder.productName }}</p>
                        <p><strong>Loại bảo hành:</strong> {{ selectedOrder.warrantyType }}</p>
                    </div>
                    <div class="detail-section">
                        <h4>Thông Tin Khách Hàng</h4>
                        <p><strong>Tên:</strong> {{ selectedOrder.customerName }}</p>
                        <p><strong>SĐT:</strong> {{ selectedOrder.phone }}</p>
                    </div>
                    <div class="detail-section">
                        <h4>Chi Tiết Bảo Hành</h4>
                        <p><strong>Mô tả lỗi:</strong> {{ selectedOrder.description }}</p>
                        <p><strong>Ngày tạo:</strong> {{ selectedOrder.createdDate }}</p>
                        <p v-if="selectedOrder.completedDate"><strong>Ngày hoàn thành:</strong> {{
                            selectedOrder.completedDate }}</p>
                        <p><strong>Trạng thái:</strong>
                            <span :class="['status-badge', selectedOrder.status]">
                                {{ selectedOrder.status === 'completed' ? 'Hoàn thành' : 'Đang xử lý' }}
                            </span>
                        </p>
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
import { checkedWarranty, createRequestWarranty } from '@/Service/Adminservice/BaoHanh/BaoHanhService'
import { tr } from 'element-plus/es/locales.mjs'

const activeTab = ref('lookup')
const searchImei = ref('')
const searchError = ref('')
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

// Dữ liệu mẫu sản phẩm
// const sampleProducts = {
//     '123456789012345': {
//         imei: '123456789012345',
//         name: 'iPhone 14 Pro Max',
//         model: 'A2896',
//         purchaseDate: '2023-01-15',
//         warranties: [
//             {
//                 type: 'Bảo hành chính hãng',
//                 duration: '12 tháng',
//                 expireDate: '2024-01-15',
//                 isValid: true
//             },
//             {
//                 type: 'Bảo hành mở rộng',
//                 duration: '24 tháng',
//                 expireDate: '2025-01-15',
//                 isValid: true
//             }
//         ]
//     },
//     '987654321098765': {
//         imei: '987654321098765',
//         name: 'Samsung Galaxy S23 Ultra',
//         model: 'SM-S918B',
//         purchaseDate: '2022-06-10',
//         warranties: [
//             {
//                 type: 'Bảo hành chính hãng',
//                 duration: '12 tháng',
//                 expireDate: '2023-06-10',
//                 isValid: false
//             },
//             {
//                 type: 'Bảo hành quốc tế',
//                 duration: '18 tháng',
//                 expireDate: '2023-12-10',
//                 isValid: false
//             }
//         ]
//     },
//     '111111111111111': {
//         imei: '111111111111111',
//         name: 'Xiaomi 13 Pro',
//         model: '2210132C',
//         purchaseDate: '2023-08-20',
//         warranties: [
//             {
//                 type: 'Bảo hành chính hãng',
//                 duration: '18 tháng',
//                 expireDate: '2025-02-20',
//                 isValid: true
//             }
//         ]
//     }
// }


// Hàm tiện ích Toast
function showToastMsg(message, type = 'success') {
    toastMessage.value = message
    toastType.value = type
    showToast.value = true
    setTimeout(() => { showToast.value = false }, 3000)
}

// ===== METHODS =====
async function searchWarranty() {
    searchError.value = ''
    productInfo.value = null
    selectedWarranty.value = null

    if (!searchImei.value) {
        searchError.value = 'Vui lòng nhập IMEI'
        return
    }

    if (searchImei.value.length !== 15 || !/^\d+$/.test(searchImei.value)) {
        searchError.value = 'IMEI phải có đúng 15 số'
        return
    }

    const res = await checkedWarranty(searchImei.value);
    if (!res) {
        searchError.value = 'Không tìm thấy thông tin sản phẩm với IMEI này'
        return
    }

    productInfo.value = res.data
    showToastMsg('Tìm thấy thông tin sản phẩm!', 'success')
}

function selectWarrantyType(warranty) {
    selectedWarranty.value = warranty
    showToastMsg(`Đã chọn ${warranty.type}`, 'success')
}

async function createWarranty() {
    if (!warrantyForm.description ) {
        showToastMsg('Vui lòng điền đầy đủ thông tin', 'error')
        return
    }

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

    showToastMsg('Tạo đơn bảo hành thành công!', 'success')
}

function cancelWarranty() {
    selectedWarranty.value = null
    warrantyForm.description = ''
}

function completeWarranty(orderId) {
    const order = warrantyOrders.value.find(o => o.id === orderId)
    if (order) {
        order.status = 'completed'
        order.completedDate = new Date().toLocaleDateString('vi-VN')
        saveWarrantyOrders()
        showToastMsg('Đã hoàn thành đơn bảo hành!', 'success')
    }
}

function viewDetails(order) {
    selectedOrder.value = order
    showDetailModal.value = true
}

function closeModal() {
    showDetailModal.value = false
    selectedOrder.value = null
}

function searchHistory() {
    if (!historyImei.value) {
        showToastMsg('Vui lòng nhập IMEI', 'error')
        return
    }

    historyData.value = warrantyOrders.value.filter(order => order.imei === historyImei.value)

    if (historyData.value.length === 0) {
        showToastMsg('Không tìm thấy lịch sử bảo hành cho IMEI này', 'error')
    }
}

function loadWarrantyOrders() {
    const 
}

// Lifecycle
onMounted(() => {
    loadWarrantyOrders()
})
</script>


<style scoped>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

.warranty-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
    background: linear-gradient(135deg, #667eea, #764ba2);
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

.status.valid {
    background: #d4edda;
    color: #155724;
}

.status.expired {
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

.status-badge.pending {
    background: #fff3cd;
    color: #856404;
}

.status-badge.completed {
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
</style>