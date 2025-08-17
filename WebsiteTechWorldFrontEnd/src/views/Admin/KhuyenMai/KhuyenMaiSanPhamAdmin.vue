<template>
    <div class="promotion-container">
        <div class="page-header">
            <div class="header-content">
                <div class="title-section">
                    <h1 class="page-title">Quản lý khuyến mãi sản phẩm</h1>
                    <p class="page-subtitle">Tạo và quản lý các chương trình khuyến mãi sản phẩm</p>
                </div>
                <el-button v-if="isAdmin" type="primary" size="large" class="create-btn" @click="openCreateDialog">
                    <el-icon class="btn-icon">
                        <Plus />
                    </el-icon>
                    Thêm khuyến mãi
                </el-button>
            </div>
        </div>

        <el-card class="filters-card" shadow="never">
            <div class="filters-content">
                <div class="filter-group">
                    <div class="filter-item">
                        <label class="filter-label">Tìm kiếm</label>
                        <el-input v-model="search" placeholder="Tìm kiếm theo mã hoặc tên..." clearable
                            class="filter-input" @input="fetchKhuyenMai">
                            <template #prefix>
                                <el-icon class="search-icon">
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </div>
                    <div class="filter-item">
                        <label class="filter-label">Trạng thái</label>
                        <el-select v-model="trangThai" placeholder="Chọn trạng thái" clearable class="filter-select"
                            @change="fetchKhuyenMai">
                            <el-option label="Tất cả" value="" />
                            <el-option label="Đang hoạt động" value="ACTIVE" />
                            <el-option label="Hết hạn" value="IN_ACTIVE" />
                        </el-select>
                    </div>
                    <div class="filter-item">
                        <label class="filter-label">Thời gian bắt đầu</label>
                        <el-date-picker v-model="ngayBatDauFilter" type="date" placeholder="Chọn thời gian bắt đầu"
                            value-format="YYYY-MM-DD" clearable class="filter-date" @change="fetchKhuyenMai" />
                    </div>
                    <div class="filter-item">
                        <label class="filter-label">Thời gian kết thúc</label>
                        <el-date-picker v-model="ngayKetThucFilter" type="date" placeholder="Chọn thời gian kết thúc"
                            value-format="YYYY-MM-DD" clearable class="filter-date" @change="fetchKhuyenMai" />
                    </div>
                    <div class="filter-actions">
                        <el-button @click="clearFilters" class="reset-btn">
                            <el-icon>
                                <Refresh />
                            </el-icon>
                            Làm mới
                        </el-button>
                    </div>
                </div>
            </div>
        </el-card>

        <el-card class="table-card" shadow="never">
            <el-table v-loading="loading" :data="khuyenMais" class="promotion-table" :row-class-name="getRowClassName"
                empty-text="Không có dữ liệu">
                <el-table-column label="STT" width="75" align="center">
                    <template #default="scope">
                        <span class="row-index">{{ scope.$index + 1 + (pagination.page - 1) * pagination.size }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="maKhuyenMai" label="Mã khuyến mãi" min-width="145">
                    <template #default="scope">
                        <div class="code-cell">
                            <el-tag type="info" size="small" class="code-tag">
                                {{ scope.row.maKhuyenMai }}
                            </el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="tenKhuyenMai" label="Tên khuyến mãi" min-width="170">
                    <template #default="scope">
                        <div class="name-cell">
                            <span class="promotion-name">{{ scope.row.tenKhuyenMai }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="Phần trăm giảm" width="140" align="center">
                    <template #default="scope">
                        <div class="value-cell">
                            <span class="discount-percent">{{ scope.row.phanTramGiam }}%</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="Thời gian" width="180">
                    <template #default="scope">
                        <div class="date-range">
                            <div class="date-item">
                                <el-icon class="date-icon">
                                    <Calendar />
                                </el-icon>
                                <span>{{ formatDateTime(scope.row.ngayBatDau) }}</span>
                            </div>
                            <div class="date-separator">→</div>
                            <div class="date-item">
                                <el-icon class="date-icon">
                                    <Calendar />
                                </el-icon>
                                <span>{{ formatDateTime(scope.row.ngayKetThuc) }}</span>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="Trạng thái" width="140" align="center">
                    <template #default="scope">
                        <el-tag :type="getStatusType(scope.row.trangThai)" size="small">
                            {{ convertTrangThai(scope.row.trangThai) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column v-if="isAdmin" label="Thao tác" width="200" align="center" fixed="right">
                    <template #default="scope">
                        <div class="action-buttons">
                            <el-tooltip content="Xem chi tiết" placement="top">
                                <el-button size="small" type="info" :icon="View" circle
                                    @click="openDetailDialog(scope.row)" class="action-btn view-btn" />
                            </el-tooltip>
                            <el-tooltip content="Chỉnh sửa" placement="top">
                                <el-button size="small" type="primary" :icon="Edit" circle
                                    @click="openEditDialog(scope.row)" class="action-btn edit-btn" />
                            </el-tooltip>
                            <el-tooltip content="Xóa" placement="top">
                                <el-button size="small" type="danger" :icon="Delete" circle
                                    @click="xoaKhuyenMai(scope.row)" class="action-btn delete" />
                            </el-tooltip>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination-wrapper">
                <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                    :current-page="pagination.page" :page-size="pagination.size" :page-sizes="[5, 10, 20, 50]"
                    :total="pagination.total" @current-change="updatePage" @size-change="updateSize"
                    class="custom-pagination" />
            </div>
        </el-card>

        <el-dialog v-model="dialog" width="1100px" style="margin-top: 30px;" class="promotion-dialog"
            :close-on-click-modal="false">
            <div class="dialog-content">
                <el-form :model="form" label-position="top" class="promotion-form" :rules="rules" ref="formRef">
                    <div class="form-grid">
                        <div class="form-section">
                            <h3 class="section-title">Thông tin cơ bản</h3>
                            <div class="form-row">
                                <el-form-item label="Mã khuyến mãi" prop="maKhuyenMai" class="form-item">
                                    <el-input v-model="form.maKhuyenMai" placeholder="Mã tự động" />
                                </el-form-item>
                                <el-form-item label="Tên khuyến mãi *" prop="tenKhuyenMai" class="form-item">
                                    <el-input v-model="form.tenKhuyenMai" placeholder="Nhập tên khuyến mãi" />
                                </el-form-item>
                            </div>
                            <div class="form-row">
                                <el-form-item label="Phần trăm giảm" prop="phanTramGiam" class="form-item">
                                    <el-input-number v-model="form.phanTramGiam" :min="1" :max="100" placeholder="0"
                                        class="full-width" />
                                </el-form-item>
                                <el-form-item label="Đối tượng áp dụng" prop="doiTuongApDung" class="form-item">
                                    <el-select v-model="form.doiTuongApDung" placeholder="Chọn đối tượng áp dụng">
                                        <el-option label="Tất cả" value="ALL" />
                                        <el-option label="Khách hàng mới" value="NEW_CUSTOMER" />
                                        <el-option label="Khách hàng cũ" value="OLD_CUSTOMER" />
                                    </el-select>
                                </el-form-item>
                            </div>
                            <el-form-item label="Mô tả" prop="moTa" class="form-item full-width">
                                <el-input v-model="form.moTa" type="textarea" :rows="3"
                                    placeholder="Nhập mô tả khuyến mãi" />
                            </el-form-item>
                        </div>
                        <div class="form-section">
                            <h3 class="section-title">Thời gian hiệu lực</h3>
                            <div class="form-row">
                                <el-form-item label="Thời gian bắt đầu" prop="ngayBatDau" class="form-item">
                                    <el-date-picker v-model="form.ngayBatDau" type="datetime"
                                        placeholder="Chọn thời gian bắt đầu" value-format="YYYY-MM-DD HH:mm:ss"
                                        class="full-width" />
                                </el-form-item>
                                <el-form-item label="Thời gian kết thúc" prop="ngayKetThuc" class="form-item">
                                    <el-date-picker v-model="form.ngayKetThuc" type="datetime"
                                        placeholder="Chọn thời gian kết thúc" value-format="YYYY-MM-DD HH:mm:ss"
                                        class="full-width" />
                                </el-form-item>
                            </div>
                        </div>
                        <div class="form-section">
                            <h3 class="section-title">Sản phẩm áp dụng</h3>
                            <div class="filter-item">
                                <el-input v-model="sanPhamSearch" placeholder="Tìm kiếm theo mã hoặc tên..." clearable
                                    class="filter-input" @input="fetchSanPhams">
                                    <template #prefix>
                                        <el-icon class="search-icon">
                                            <Search />
                                        </el-icon>
                                    </template>
                                </el-input>
                            </div>

                            <div class="quick-filter-buttons" style="margin-top: 10px; display: flex; gap: 10px;">
                                <el-button type="primary" size="small" @click="filterLowStockProducts">Sản phẩm sắp hết
                                    hàng</el-button>
                                <el-button type="info" size="small" @click="filterHighStockProducts">Sản phẩm tồn kho
                                    nhiều</el-button>
                                <el-button type="default" size="small" @click="resetProductFilters">Tất cả sản
                                    phẩm</el-button>
                            </div>
                            <el-form-item label="Danh sách sản phẩm" prop="idSanPhamChiTietList"
                                class="form-item full-width" style="margin-top: 10px;">
                                <el-table :data="displaySanPhams" class="product-table" border ref="productTable"
                                    v-loading="sanPhamLoading" :row-key="row => row.id">
                                    <el-table-column prop="selected" width="80">
                                        <template #header>
                                            <el-checkbox
                                                :model-value="displaySanPhams.length > 0 && displaySanPhams.every(sp => sp.selected)"
                                                @change="toggleAllSpSelection">
                                            </el-checkbox>
                                        </template>
                                        <template #default="{ row }">
                                            <el-checkbox v-model="row.selected" />
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="maSanPham" label="Mã sản phẩm" width="200" />
                                    <el-table-column prop="tenSanPham" label="Tên sản phẩm" min-width="200" />
                                </el-table>

                                <div class="pagination-wrapper">
                                    <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                                        :current-page="sanPhamPagination.page" :page-size="sanPhamPagination.size"
                                        :page-sizes="[5, 10, 20, 50]" :total="sanPhamPagination.total"
                                        @current-change="updateSanPhamPage" @size-change="updateSanPhamSize"
                                        class="custom-pagination" />
                                </div>
                            </el-form-item>

                            <el-form-item label="Sản phẩm chi tiết" class="form-item full-width"
                                v-if="selectedSanPhamIds.length">
                                <el-table :data="paginatedSanPhamChiTiets" class="product-detail-table" border
                                    ref="sanPhamChiTietTable" v-loading="sanPhamChiTietLoading"
                                    :row-key="row => row.id">
                                    <el-table-column prop="selected" width="70">
                                        <template #header>
                                            <el-checkbox
                                                :model-value="paginatedSanPhamChiTiets.length > 0 && paginatedSanPhamChiTiets.every(spct => spct.selected)"
                                                @change="toggleAllSpctSelection">
                                            </el-checkbox>
                                        </template>
                                        <template #default="{ row }">
                                            <el-checkbox v-model="row.selected"
                                                @change="checkExistingPromotions(row)" />
                                        </template>
                                    </el-table-column>
                                    <el-table-column prop="maSanPhamChiTiet" label="Mã SPCT" width="150" />
                                    <el-table-column prop="soLuongSPCT" label="Số lượng" min-width="100" />
                                    <el-table-column prop="tenMau" label="Màu sắc" width="150" />
                                    <el-table-column prop="dungLuongRom" label="Kích thước" width="130" />
                                    <el-table-column prop="giaBan" label="Giá bán" width="150">
                                        <template #default="scope">
                                            {{ formatCurrency(scope.row.giaBan) }}
                                        </template>
                                    </el-table-column>
                                    <el-table-column label="Khuyến mãi hiện tại" width="180">
                                        <template #default="{ row }">
                                            <el-tag v-if="row.idKhuyenMai" type="warning">
                                                {{ row.maKhuyenMai }}
                                            </el-tag>
                                            <span v-else>Chưa áp dụng</span>
                                        </template>
                                    </el-table-column>
                                </el-table>

                                <!-- Dialog hiển thị khuyến mãi hiện tại khi có xung đột -->
                                <el-dialog v-model="promotionConflictDialog" title="Xung đột khuyến mãi" width="800px"
                                    style="margin-top: 30px;" :close-on-click-modal="false"
                                    class="promotion-conflict-dialog">
                                    <div class="conflict-dialog-content">
                                        <p class="conflict-message">Các sản phẩm chi tiết sau đã được áp dụng trong các
                                            khuyến
                                            mãi khác:</p>
                                        <!-- Thêm trường tìm kiếm -->
                                        <el-input v-model="conflictSearch"
                                            placeholder="Tìm kiếm theo mã SPCT hoặc tên khuyến mãi..." clearable
                                            class="conflict-search-input" @input="resetConflictPagination">
                                            <template #prefix>
                                                <el-icon class="search-icon">
                                                    <Search />
                                                </el-icon>
                                            </template>
                                        </el-input>
                                        <el-table :data="paginatedConflictedSanPhamChiTiets" border
                                            class="conflict-table">
                                            <!-- Cột checkbox -->
                                            <el-table-column width="80">
                                                <template #header>
                                                    <el-checkbox
                                                        :model-value="paginatedConflictedSanPhamChiTiets.length > 0 && paginatedConflictedSanPhamChiTiets.every(spct => spct.selected)"
                                                        @change="toggleAllConflictSelection">
                                                    </el-checkbox>
                                                </template>
                                                <template #default="{ row }">
                                                    <el-checkbox v-model="row.selected" />
                                                </template>
                                            </el-table-column>
                                            <el-table-column prop="maSanPhamChiTiet" label="Mã SPCT" width="150" />
                                            <el-table-column label="Khuyến mãi hiện tại" min-width="200">
                                                <template #default="{ row }">
                                                    <el-tag type="warning" class="promotion-tag">
                                                        {{ row.maKhuyenMai || 'Không xác định'
                                                        }}
                                                    </el-tag>
                                                </template>
                                            </el-table-column>
                                        </el-table>
                                        <!-- Thêm phân trang -->
                                        <div class="pagination-wrapper">
                                            <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                                                :current-page="conflictPagination.page"
                                                :page-size="conflictPagination.size" :page-sizes="[5, 10, 20, 50]"
                                                :total="filteredConflictedSanPhamChiTiets.length"
                                                @current-change="updateConflictPage" @size-change="updateConflictSize"
                                                class="custom-pagination" />
                                        </div>
                                        <p class="conflict-prompt">Bạn có muốn xóa các sản phẩm được chọn khỏi khuyến
                                            mãi hiện
                                            tại để áp dụng khuyến mãi mới?</p>
                                    </div>
                                    <template #footer>
                                        <div class="dialog-footer">
                                            <el-button @click="cancelPromotionSelection" size="large"
                                                class="cancel-btn">Hủy</el-button>
                                            <el-button type="primary" @click="applyAllPromotionSelection" size="large"
                                                class="confirm-btn"
                                                :disabled="!paginatedConflictedSanPhamChiTiets.some(spct => spct.selected)">Xác
                                                nhận</el-button>
                                        </div>
                                    </template>
                                </el-dialog>
                                <div class="pagination-wrapper">
                                    <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                                        :current-page="sanPhamChiTietPagination.page"
                                        :page-size="sanPhamChiTietPagination.size" :page-sizes="[5, 10, 20, 50]"
                                        :total="sanPhamChiTietPagination.total"
                                        @current-change="updateSanPhamChiTietPage"
                                        @size-change="updateSanPhamChiTietSize" class="custom-pagination" />
                                </div>
                            </el-form-item>
                        </div>
                    </div>
                </el-form>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialog = false" size="large" class="cancel-btn">Hủy</el-button>
                    <el-button type="primary" @click="saveKhuyenMai" size="large" class="save-btn" :loading="loading">
                        {{ isEdit ? 'Cập nhật' : 'Tạo mới' }}
                    </el-button>
                </div>
            </template>
        </el-dialog>

        <el-dialog v-model="detailDialog" width="1000px" style="margin-top: 30px;" class="promotion-dialog"
            :close-on-click-modal="false">
            <div class="dialog-content">
                <h3 class="section-title">Chi tiết khuyến mãi</h3>
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="Mã khuyến mãi">{{ selectedKhuyenMai?.maKhuyenMai
                        }}</el-descriptions-item>
                    <el-descriptions-item label="Tên khuyến mãi">{{ selectedKhuyenMai?.tenKhuyenMai
                        }}</el-descriptions-item>
                    <el-descriptions-item label="Phần trăm giảm">{{ selectedKhuyenMai?.phanTramGiam
                        }}%</el-descriptions-item>
                    <el-descriptions-item label="Đối tượng áp dụng">{{
                        convertDoiTuongApDung(selectedKhuyenMai?.doiTuongApDung)
                        }}</el-descriptions-item>
                    <el-descriptions-item label="Thời gian bắt đầu">{{ formatDateTime(selectedKhuyenMai?.ngayBatDau)
                        }}</el-descriptions-item>
                    <el-descriptions-item label="Thời gian kết thúc">{{ formatDateTime(selectedKhuyenMai?.ngayKetThuc)
                        }}</el-descriptions-item>
                    <el-descriptions-item label="Trạng thái">{{ convertTrangThai(selectedKhuyenMai?.trangThai)
                        }}</el-descriptions-item>
                    <el-descriptions-item label="Mô tả" :span="2">{{ selectedKhuyenMai?.moTa || 'Không có mô tả'
                        }}</el-descriptions-item>
                </el-descriptions>
                <div class="form-section" style="margin-top: 20px;">
                    <h3 class="section-title">Sản phẩm chi tiết áp dụng</h3>
                    <el-table :data="paginatedDetailSanPhamChiTiets" class="product-detail-table" border
                        v-loading="detailLoading">
                        <el-table-column prop="maSanPhamChiTiet" label="Mã SPCT" width="160" />
                        <el-table-column prop="soLuongSPCT" label="Số lượng" min-width="160" />
                        <el-table-column prop="tenMau" label="Màu sắc" width="180" />
                        <el-table-column prop="dungLuongRom" label="Kích thước" width="160" />
                        <el-table-column prop="giaBan" label="Giá bán" width="200">
                            <template #default="scope">
                                {{ formatCurrency(scope.row.giaBan) }}
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination-wrapper">
                        <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                            :current-page="detailSanPhamChiTietPagination.page"
                            :page-size="detailSanPhamChiTietPagination.size" :page-sizes="[5, 10, 20, 50]"
                            :total="detailSanPhamChiTietPagination.total"
                            @current-change="updateDetailSanPhamChiTietPage"
                            @size-change="updateDetailSanPhamChiTietSize" class="custom-pagination" />
                    </div>
                </div>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="detailDialog = false" size="large" class="cancel-btn">Đóng</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick, onUnmounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Edit, Delete, Search, Refresh, Calendar, View } from '@element-plus/icons-vue';
import { createKhuyenMai, deleteKhuyenMai, getAllKhuyenMai, updateKhuyenMai, getAllSanPham, getSanPhamChiTietsBySanPhamIds, getSanPhamChiTietByIdKhuyenMai, removeProductFromPromotions, getKhuyenMai, nextDelay } from '@/Service/Adminservice/KhuyenMai/KhuyenMaiSanPhamService';
import store from '@/Service/LoginService/Store';

const search = ref('');
const trangThai = ref('');
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);
const sanPhamSearch = ref('');
const loading = ref(false);
const sanPhamLoading = ref(false);
const sanPhamChiTietLoading = ref(false);
const detailLoading = ref(false);
const dialog = ref(false);
const detailDialog = ref(false);
const isEdit = ref(false);
const khuyenMais = ref([]);
const sanPhams = ref([]);
const sanPhamChiTiets = ref([]);
const detailSanPhamChiTiets = ref([]);
const selectedSanPhamIds = ref([]);
const selectedSanPhamChiTietIds = ref([]);
const selectedKhuyenMai = ref(null);
const productTable = ref(null);
const sanPhamChiTietTable = ref(null);
const pagination = ref({
    page: 1,
    size: 5,
    total: 0,
});
const sanPhamPagination = ref({
    page: 1,
    size: 5,
    total: 0,
});
const sanPhamChiTietPagination = ref({
    page: 1,
    size: 5,
    total: 0,
});
const detailSanPhamChiTietPagination = ref({
    page: 1,
    size: 5,
    total: 0,
});
const form = ref({
    id: null,
    maKhuyenMai: '',
    tenKhuyenMai: '',
    moTa: '',
    phanTramGiam: 0,
    ngayBatDau: '',
    ngayKetThuc: '',
    trangThai: '',
    doiTuongApDung: 'ALL',
    idSanPhamChiTietList: [],
});

const conflictedSanPhamChiTiets = ref([]);

const formRef = ref(null);
const rules = {
    maKhuyenMai: [
        { min: 3, message: 'Mã khuyến mãi phải có ít nhất 3 ký tự', trigger: 'blur' },
    ],
    tenKhuyenMai: [
        { required: true, message: 'Tên khuyến mãi là bắt buộc', trigger: 'blur' },
        { min: 3, message: 'Tên khuyến mãi phải có ít nhất 3 ký tự', trigger: 'blur' },
    ],
    phanTramGiam: [
        { type: 'number', min: 1, max: 100, message: 'Phần trăm giảm phải từ 1-60', trigger: 'blur' },
    ],
    ngayBatDau: [
        { required: true, message: 'Thời gian bắt đầu là bắt buộc', trigger: 'change' },
    ],
    ngayKetThuc: [
        { required: true, message: 'Thời gian kết thúc là bắt buộc', trigger: 'change' },
        {
            validator: (rule, value, callback) => {
                if (value && form.value.ngayBatDau && new Date(value) <= new Date(form.value.ngayBatDau)) {
                    callback(new Error('Thời gian kết thúc phải sau thời gian bắt đầu'));
                } else {
                    callback();
                }
            },
            trigger: 'change',
        },
    ],
    idSanPhamList: [
        { required: true, message: 'Vui lòng chọn ít nhất một sản phẩm', trigger: 'change', type: 'array', min: 1 },
    ],
    idSanPhamChiTietList: [
        { required: true, message: 'Vui lòng chọn ít nhất một sản phẩm chi tiết', trigger: 'change', type: 'array', min: 1 },
    ],
};

const isAdmin = computed(() => {
    const roles = store.state.roles;
    return Array.isArray(roles) && roles
        .map((role) => (typeof role === 'string' ? role : role.authority))
        .includes('ROLE_ADMIN');
});

const displaySanPhams = computed(() => {
    const start = (sanPhamPagination.value.page - 1) * sanPhamPagination.value.size;
    const end = start + sanPhamPagination.value.size;
    return sanPhams.value.slice(start, end);
});

const paginatedSanPhamChiTiets = computed(() => {
    const start = (sanPhamChiTietPagination.value.page - 1) * sanPhamChiTietPagination.value.size;
    const end = start + sanPhamChiTietPagination.value.size;
    return sanPhamChiTiets.value.slice(start, end);
});

const paginatedDetailSanPhamChiTiets = computed(() => {
    const start = (detailSanPhamChiTietPagination.value.page - 1) * detailSanPhamChiTietPagination.value.size;
    const end = start + detailSanPhamChiTietPagination.value.size;
    return detailSanPhamChiTiets.value.slice(start, end);
});

const fetchKhuyenMai = async () => {
    loading.value = true;
    try {
        const response = await getAllKhuyenMai({
            search: search.value,
            trangThai: trangThai.value,
            ngayBatDau: ngayBatDauFilter.value,
            ngayKetThuc: ngayKetThucFilter.value,
            page: pagination.value.page - 1,
            size: pagination.value.size,
        });
        khuyenMais.value = response.content;
        pagination.value.total = response.totalElements;
    } catch (error) {
        ElMessage.error(error?.response?.data?.message || 'Lỗi khi lấy danh sách khuyến mãi');
    } finally {
        loading.value = false;
    }
};

const productFilter = ref('ALL'); // ALL, LOW_STOCK, HIGH_STOCK

// Hàm lọc sản phẩm sắp hết hàng (ví dụ: số lượng < 10)
const filterLowStockProducts = async () => {
    productFilter.value = 'LOW_STOCK';
    sanPhamPagination.value.page = 1;
    await fetchSanPhams();
};

// Hàm lọc sản phẩm tồn kho nhiều (ví dụ: số lượng > 100)
const filterHighStockProducts = async () => {
    productFilter.value = 'HIGH_STOCK';
    sanPhamPagination.value.page = 1;
    await fetchSanPhams();
};

// Hàm reset bộ lọc
const resetProductFilters = async () => {
    productFilter.value = 'ALL';
    sanPhamSearch.value = '';
    sanPhamPagination.value.page = 1;
    await fetchSanPhams();
};

const fetchSanPhams = async () => {
    sanPhamLoading.value = true;
    try {
        const response = await getAllSanPham({
            search: sanPhamSearch.value,
            page: sanPhamPagination.value.page - 1,
            size: sanPhamPagination.value.size,
            filter: productFilter.value
        });
        sanPhams.value = response.map(sp => ({
            ...sp,
            selected: selectedSanPhamIds.value.includes(sp.id)
        }));
        sanPhamPagination.value.total = response.length;
    } catch (error) {
        ElMessage.error('Lỗi khi lấy danh sách sản phẩm');
        console.error(error)
    } finally {
        sanPhamLoading.value = false;
    }
};

// Thêm biến để quản lý dialog xung đột khuyến mãi
const promotionConflictDialog = ref(false);
const selectedProductDetail = ref(null);

const conflictSearch = ref('');
const conflictPagination = ref({
    page: 1,
    size: 5,
    total: 0
});

// Computed property để lọc danh sách xung đột dựa trên tìm kiếm
const filteredConflictedSanPhamChiTiets = computed(() => {
    const searchTerm = conflictSearch.value.toLowerCase();
    if (!searchTerm) return conflictedSanPhamChiTiets.value;
    return conflictedSanPhamChiTiets.value.filter(spct =>
        spct.maSanPhamChiTiet.toLowerCase().includes(searchTerm) ||
        spct.isKhuyenMai.tenKhuyenMai.toLowerCase().includes(searchTerm)
    );
});

// Computed property để hiển thị danh sách phân trang
const paginatedConflictedSanPhamChiTiets = computed(() => {
    const start = (conflictPagination.value.page - 1) * conflictPagination.value.size;
    const end = start + conflictPagination.value.size;
    return filteredConflictedSanPhamChiTiets.value.slice(start, end);
});

// Thêm hàm để chọn/bỏ chọn tất cả trong dialog xung đột
const toggleAllConflictSelection = (val) => {
    conflictedSanPhamChiTiets.value = conflictedSanPhamChiTiets.value.map(spct => ({
        ...spct,
        selected: val
    }));
};

// Thêm hàm để reset trang khi tìm kiếm
const resetConflictPagination = () => {
    conflictPagination.value.page = 1;
};

// Thêm hàm để cập nhật trang
const updateConflictPage = (page) => {
    conflictPagination.value.page = page;
};

// Thêm hàm để cập nhật kích thước trang
const updateConflictSize = (size) => {
    conflictPagination.value.size = size;
    conflictPagination.value.page = 1;
};

const toggleAllSpctSelection = async (val) => {
    try {
        if (!sanPhamChiTiets.value.length) {
            ElMessage.warning('Không có sản phẩm chi tiết để xử lý');
            return;
        }

        const start = (sanPhamChiTietPagination.value.page - 1) * sanPhamChiTietPagination.value.size;
        const end = Math.min(start + sanPhamChiTietPagination.value.size, sanPhamChiTiets.value.length);

        conflictedSanPhamChiTiets.value = [];

        if (val) {
            const currentPageSpcts = sanPhamChiTiets.value.slice(start, end);
            for (const spct of currentPageSpcts) {
                if (spct.idKhuyenMai && (!isEdit.value || spct.idKhuyenMai !== form.value.id)) {
                    spct.selected = true;
                    conflictedSanPhamChiTiets.value.push(spct);
                }
            }
            if (conflictedSanPhamChiTiets.value.length > 0) {
                conflictSearch.value = '';
                conflictPagination.value.page = 1;
                promotionConflictDialog.value = true;
                return;
            }
            sanPhamChiTiets.value = sanPhamChiTiets.value.map((spct, index) => {
                if (index >= start && index < end) {
                    return { ...spct, selected: true };
                }
                return spct;
            });
        } else {
            sanPhamChiTiets.value = sanPhamChiTiets.value.map((spct, index) => {
                if (index >= start && index < end) {
                    return { ...spct, selected: false };
                }
                return spct;
            });
        }
        updateSanPhamChiTietSelection();
    } catch (error) {
        console.error('Lỗi trong toggleAllSpctSelection:', error);
        ElMessage.error({
            message: error.response?.data?.message || 'Lỗi khi xử lý lựa chọn sản phẩm chi tiết',
            type: 'error',
        });
    }
};

// Hàm kiểm tra khuyến mãi hiện tại của sản phẩm chi tiết
const checkExistingPromotions = async (row) => {
    if (row.selected) {
        try {
            // Giả sử có API để kiểm tra khuyến mãi hiện tại của sản phẩm chi tiết
            const response = await getKhuyenMai(row.id);
            if (response) {
                row.idKhuyenMai = response.id;
                row.selected = true;
                conflictedSanPhamChiTiets.value = [row];
                conflictSearch.value = '';
                conflictPagination.value.page = 1;
                promotionConflictDialog.value = true;
            } else {
                row.isKhuyenMai = 0;
                updateSanPhamChiTietSelection();
            }
        } catch (error) {
            console.error(error)
            ElMessage.error('Lỗi khi kiểm tra khuyến mãi hiện tại');
        }
    } else {
        row.isKhuyenMai = 0;
        updateSanPhamChiTietSelection();
    }
};


// Hàm hủy chọn khuyến mãi
const cancelPromotionSelection = () => {
    conflictedSanPhamChiTiets.value.forEach(spct => {
        spct.selected = false;
        spct.isKhuyenMai = [];
    });
    conflictedSanPhamChiTiets.value = [];
    conflictSearch.value = '';
    conflictPagination.value.page = 1;
    promotionConflictDialog.value = false;
    updateSanPhamChiTietSelection();
};

// Hàm áp dụng lựa chọn khuyến mãi
const applyAllPromotionSelection = async () => {
    try {
        const selectedSpcts = conflictedSanPhamChiTiets.value.filter(spct => spct.selected);
        if (selectedSpcts.length === 0) {
            ElMessage.warning('Vui lòng chọn ít nhất một sản phẩm để thay đổi khuyến mãi');
            return;
        }
        for (const spct of selectedSpcts) {
            if (spct.idKhuyenMai) {
                await removeProductFromPromotions(spct.id, spct.idKhuyenMai);
            }
            spct.idKhuyenMai = 0;
        }
        ElMessage.success('Đã xóa các sản phẩm được chọn khỏi khuyến mãi hiện tại');
        conflictedSanPhamChiTiets.value = [];
        conflictSearch.value = '';
        conflictPagination.value.page = 1;
        promotionConflictDialog.value = false;
        updateSanPhamChiTietSelection();
    } catch (error) {
        console.error('Lỗi cập nhật khuyến mãi:', error);
        ElMessage.error(error?.response?.data?.message || 'Lỗi khi cập nhật khuyến mãi');
    }
};

const updateSanPhamChiTietSelection = () => {
    selectedSanPhamChiTietIds.value = sanPhamChiTiets.value
        .filter(spct => spct.selected)
        .map(spct => spct.id);
    form.value.idSanPhamChiTietList = selectedSanPhamChiTietIds.value;
};

const fetchSanPhamChiTiets = async (sanPhamIds) => {
    if (!sanPhamIds.length) {
        sanPhamChiTiets.value = [];
        form.value.idSanPhamChiTietList = [];
        sanPhamChiTietPagination.value.total = 0;
        return;
    }
    sanPhamChiTietLoading.value = true;
    try {
        const response = await getSanPhamChiTietsBySanPhamIds(sanPhamIds);
        console.log('response', response)
        sanPhamChiTiets.value = response.map(spct => ({
            ...spct,
            selected: selectedSanPhamChiTietIds.value.includes(spct.id)
        }));
        sanPhamChiTietPagination.value.total = response.length;
    } catch (error) {
        ElMessage.error('Lỗi khi lấy danh sách sản phẩm chi tiết');
        console.error(error)
    } finally {
        sanPhamChiTietLoading.value = false;
    }
};


const fetchDetailSanPhamChiTiets = async (idKhuyenMai) => {
    if (!idKhuyenMai) {
        detailSanPhamChiTiets.value = [];
        detailSanPhamChiTietPagination.value.total = 0;
        return;
    }
    detailLoading.value = true;
    try {
        const response = await getSanPhamChiTietByIdKhuyenMai(idKhuyenMai);
        detailSanPhamChiTiets.value = response
        detailSanPhamChiTietPagination.value.total = detailSanPhamChiTiets.value.length;
    } catch (error) {
        ElMessage.error('Lỗi khi lấy danh sách sản phẩm chi tiết');
    } finally {
        detailLoading.value = false;
    }
};

const updateSelectedSanPhams = () => {
    const selection = sanPhams.value.filter(sp => sp.selected);
    selectedSanPhamIds.value = selection.map(item => item.id);
    fetchSanPhamChiTiets(selectedSanPhamIds.value);
};



const openCreateDialog = () => {
    isEdit.value = false;
    form.value = {
        id: null,
        maKhuyenMai: '',
        tenKhuyenMai: '',
        moTa: '',
        phanTramGiam: 0,
        ngayBatDau: '',
        ngayKetThuc: '',
        trangThai: '',
        doiTuongApDung: 'ALL',
        idSanPhamChiTietList: [],
    };
    selectedSanPhamIds.value = [];
    selectedSanPhamChiTietIds.value = [];
    sanPhamChiTiets.value = [];
    sanPhamSearch.value = '';
    sanPhamPagination.value.page = 1;
    sanPhamChiTietPagination.value.page = 1;
    dialog.value = true;
    nextTick(() => {
        fetchSanPhams();
        if (productTable.value) productTable.value.clearSelection();
        if (sanPhamChiTietTable.value) sanPhamChiTietTable.value.clearSelection();
    });
};

const openEditDialog = async (item) => {
    isEdit.value = true;
    form.value = {
        ...item,
        doiTuongApDung: item.doiTuongApDung || 'ALL',
        idSanPhamChiTietList: item.idSanPhamChiTietList || []
    };
    selectedSanPhamChiTietIds.value = form.value.idSanPhamChiTietList;
    selectedSanPhamIds.value = [];

    if (form.value.idSanPhamChiTietList.length) {
        const response = await getSanPhamChiTietByIdKhuyenMai(item.id);
        const sanPhamIds = response.map(spct => spct.idSanPham);

        selectedSanPhamIds.value = [...new Set(sanPhamIds)];
        await fetchSanPhams();
        await fetchSanPhamChiTiets(sanPhamIds);
    }
    dialog.value = true;
};

const openDetailDialog = async (item) => {
    selectedKhuyenMai.value = item;
    detailSanPhamChiTiets.value = [];
    detailSanPhamChiTietPagination.value.page = 1;
    detailDialog.value = true;
    if (item.id) {
        await fetchDetailSanPhamChiTiets(item.id);
    }
};

const saveKhuyenMai = async () => {
    formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                loading.value = true;
                const payload = {
                    ...form.value,
                    trangThai: 'ACTIVE',
                    ngayBatDau: formatToSQLDateTime(form.value.ngayBatDau),
                    ngayKetThuc: formatToSQLDateTime(form.value.ngayKetThuc),
                };
                // Kiểm tra và xóa khuyến mãi hiện tại cho các SanPhamChiTiet được chọn
                for (const spctId of form.value.idSanPhamChiTietList) {
                    const isKhuyenMai = await getKhuyenMai(spctId);
                    if (isKhuyenMai > 0 && (!isEdit.value || isKhuyenMai.id !== form.value.id)) {
                        await removeProductFromPromotions(spctId, isKhuyenMai.id);
                    }
                }
                if (isEdit.value) {
                    await updateKhuyenMai(form.value.id, payload);
                    ElMessage.success('Cập nhật khuyến mãi thành công');
                } else {
                    await createKhuyenMai(payload);
                    ElMessage.success('Tạo khuyến mãi thành công');
                }
                dialog.value = false;
                fetchKhuyenMai();
            } catch (error) {
                ElMessage.error(error || 'Lỗi khi lưu khuyến mãi');
            } finally {
                loading.value = false;
            }
        } else {
            ElMessage.error('Vui lòng kiểm tra các lỗi trong form');
        }
    });
};

const xoaKhuyenMai = async (item) => {
    try {
        await ElMessageBox.confirm("Bạn có chắc chắn muốn xóa khuyến mãi này?", 'Xác nhận', {
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy',
            type: 'warning'
        });
        loading.value = true;
        await deleteKhuyenMai(item.id);
        ElMessage.success('Xóa khuyến mãi thành công');
        fetchKhuyenMai();
    } catch (error) {
        if (error === 'cancel') {
            ElMessage.info('Đã hủy xóa');
        } else {
            console.error(error.message || "Lỗi khi xóa khuyến mãi");
            ElMessage.error('Lỗi khi xóa khuyến mãi');
        }
    } finally {
        loading.value = false;
    }
};

const toggleAllSpSelection = (val) => {
    sanPhams.value = sanPhams.value.map((sp, index) => {
        const start = (sanPhamPagination.value.page - 1) * sanPhamPagination.value.size;
        const end = start + sanPhamPagination.value.size;
        if (index >= start && index < end) {
            return { ...sp, selected: val };
        }
        return sp;
    });
};

const toggleAllXungDotSpctSelection = async (val) => {
    if (val) {
        // Khi chọn tất cả, kiểm tra xung đột khuyến mãi
        const start = (sanPhamChiTietPagination.value.page - 1) * sanPhamChiTietPagination.value.size;
        const end = start + sanPhamChiTietPagination.value.size;
        const currentPageSpcts = sanPhamChiTiets.value.slice(start, end);

        try {
            conflictedSanPhamChiTiets.value = [];
            for (const spct of currentPageSpcts) {
                const isKhuyenMai = await getKhuyenMai(spct.id);
                if (isKhuyenMai > 0 && (!isEdit.value || isKhuyenMai.id !== form.value.id)) {
                    spct.isKhuyenMai = isKhuyenMai;
                    conflictedSanPhamChiTiets.value.push(spct);
                }
            }

            if (conflictedSanPhamChiTiets.value.length > 0) {
                // Nếu có xung đột, hiển thị dialog
                promotionConflictDialog.value = true;
            } else {
                // Nếu không có xung đột, cập nhật trạng thái chọn
                sanPhamChiTiets.value = sanPhamChiTiets.value.map((spct, index) => {
                    if (index >= start && index < end) {
                        return { ...spct, selected: true, isKhuyenMai };
                    }
                    return spct;
                });
                updateSanPhamChiTietSelection();
            }
        } catch (error) {
            console.error('Lỗi khi kiểm tra xung đột khuyến mãi:', error);
            ElMessage.error(error?.response?.data?.message || 'Lỗi khi kiểm tra khuyến mãi hiện tại');
        }
    } else {
        // Khi bỏ chọn tất cả, xóa trạng thái chọn và danh sách xung đột
        const start = (sanPhamChiTietPagination.value.page - 1) * sanPhamChiTietPagination.value.size;
        const end = start + sanPhamChiTietPagination.value.size;
        sanPhamChiTiets.value = sanPhamChiTiets.value.map((spct, index) => {
            if (index >= start && index < end) {
                return { ...spct, selected: false, isKhuyenMai };
            }
            return spct;
        });
        conflictedSanPhamChiTiets.value = [];
        updateSanPhamChiTietSelection();
    }
};

const clearFilters = () => {
    search.value = '';
    trangThai.value = '';
    ngayBatDauFilter.value = null;
    ngayKetThucFilter.value = null;
    pagination.value.page = 1;
    fetchKhuyenMai();
};

const updatePage = (page) => {
    pagination.value.page = page;
    fetchKhuyenMai();
};

const updateSize = (size) => {
    pagination.value.size = size;
    pagination.value.page = 1;
    fetchKhuyenMai();
};

const updateSanPhamPage = (page) => {
    sanPhamPagination.value.page = page;
};

const updateSanPhamSize = (size) => {
    sanPhamPagination.value.size = size;
    sanPhamPagination.value.page = 1;
};

const updateSanPhamChiTietPage = (page) => {
    sanPhamChiTietPagination.value.page = page;
};

const updateSanPhamChiTietSize = (size) => {
    sanPhamChiTietPagination.value.size = size;
    sanPhamChiTietPagination.value.page = 1;
};

const updateDetailSanPhamChiTietPage = (page) => {
    detailSanPhamChiTietPagination.value.page = page;
};

const updateDetailSanPhamChiTietSize = (size) => {
    detailSanPhamChiTietPagination.value.size = size;
    detailSanPhamChiTietPagination.value.page = 1;
};

const formatDateTime = (dateStr) => {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${day}/${month}/${year} ${hours}:${minutes}`;
};

const formatCurrency = (value) => {
    if (value == null) return '0 ₫';
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const convertTrangThai = (trangThai) => {
    const trangThaiLabels = {
        NOT_STARTED: "Chưa bắt đầu",
        ACTIVE: "Đang hoạt động",
        EXPIRED: "Đã hết hạn",
    };
    return trangThaiLabels[trangThai] || 'Không xác định';
};

const convertDoiTuongApDung = (doiTuongApDung) => {
    const doiTuongLabels = {
        ALL: 'Tất cả',
        NEW_CUSTOMER: 'Khách hàng mới',
        OLD_CUSTOMER: 'Khách hàng cũ',
    };
    return doiTuongLabels[doiTuongApDung] || 'Không xác định';
};

const getStatusType = (status) => {
    const statusTypes = {
        NOT_STARTED: 'info',
        ACTIVE: 'success',
        EXPIRED: 'danger'
    };
    return statusTypes[status] || 'info';
};

const calculateStatus = (ngayBatDau, ngayKetThuc) => {
  const now = new Date();
  const startDate = new Date(ngayBatDau);
  const endDate = new Date(ngayKetThuc);

  if (now < startDate) {
    return 'NOT_STARTED';
  } else if (now >= startDate && now <= endDate) {
    return 'ACTIVE';
  } else {
    return 'EXPIRED';
  }
};

const updateStatuses = () => {
  khuyenMais.value = khuyenMais.value.map(item => ({
    ...item,
    trangThai: calculateStatus(item.ngayBatDau, item.ngayKetThuc)
  }));
};

const getRowClassName = ({ rowIndex }) => {
    return rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
};

function formatToSQLDateTime(date) {
    if (!date) return null;
    const d = new Date(date);

    const pad = (n) => (n < 10 ? '0' + n : n);

    const year = d.getFullYear();
    const month = pad(d.getMonth() + 1);
    const day = pad(d.getDate());
    const hours = pad(d.getHours());
    const minutes = pad(d.getMinutes());
    const seconds = pad(d.getSeconds());

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

const getNextUpdateDelay = async () => {
  try {
    const response = await nextDelay()
    const delay = Math.max(1000, response.data.delay || 60_000);
    return delay;
  } catch (error) {
    if (error.name !== 'AbortError') {
      console.error('Không thể lấy thời gian cập nhật:', error);
      toast.error('Không thể lấy thời gian cập nhật');
    }
    return 60_000; 
  }
};

watch(
    () => sanPhams.value.map(sp => ({ id: sp.id, selected: sp.selected })),
    () => {
        updateSelectedSanPhams();
    },
    { deep: true }
);

watch(
    () => sanPhamChiTiets.value.map(spct => ({ id: spct.id, selected: spct.selected })),
    () => {
        updateSanPhamChiTietSelection();
    },
    { deep: true }
);

watch([search, trangThai, ngayBatDauFilter, ngayKetThucFilter], () => {
    pagination.value.page = 1;
    fetchKhuyenMai();
});

watch(sanPhamSearch, () => {
    sanPhamPagination.value.page = 1;
    fetchSanPhams();
});

let statusUpdateInterval = null;

onMounted(() => {
    fetchKhuyenMai();
    fetchSanPhams();
    const initialDelay = getNextUpdateDelay();
  console.log('1', initialDelay)
    statusUpdateInterval = setInterval(updateStatuses, initialDelay);
});

onUnmounted(() => {
    if (statusUpdateInterval) {
        clearInterval(statusUpdateInterval);
        statusUpdateInterval = null;
    }
});
</script>

<style scoped>
.promotion-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    padding: 24px;
    font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.page-header {
    background: white;
    border-radius: 16px;
    padding: 32px;
    margin-bottom: 24px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 24px;
}

.title-section {
    flex: 1;
}

.page-title {
    font-size: 32px;
    font-weight: 700;
    color: #1a202c;
    margin: 0 0 8px 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.page-subtitle {
    font-size: 16px;
    color: #64748b;
    margin: 0;
}

.create-btn {
    height: 48px;
    padding: 0 24px;
    border-radius: 12px;
    font-weight: 600;
    font-size: 16px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    transition: all 0.3s ease;
}

.create-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
}

.btn-icon {
    margin-right: 8px;
}

.filters-card {
    margin-bottom: 24px;
    border-radius: 16px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.filters-card :deep(.el-card__body) {
    padding: 24px;
}

.filters-content {
    width: 100%;
}

.filter-group {
    display: grid;
    grid-template-columns: 2fr 1.5fr 1.5fr 1.5fr auto;
    gap: 20px;
    align-items: end;
}

.filter-item {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.filter-label {
    font-size: 14px;
    font-weight: 500;
    color: #374151;
}

.filter-input :deep(.el-input__wrapper),
.filter-select :deep(.el-input__wrapper),
.filter-date :deep(.el-input__wrapper) {
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    transition: all 0.3s ease;
}

.filter-input :deep(.el-input__wrapper:hover),
.filter-select :deep(.el-input__wrapper:hover),
.filter-date :deep(.el-input__wrapper:hover) {
    border-color: #cbd5e1;
}

.filter-input :deep(.el-input__wrapper.is-focus),
.filter-select :deep(.el-input__wrapper.is-focus),
.filter-date :deep(.el-input__wrapper.is-focus) {
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-icon {
    color: #9ca3af;
}

.reset-btn {
    padding: 0 16px;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    background: white;
    color: #6b7280;
    transition: all 0.3s ease;
}

.reset-btn:hover {
    border-color: #cbd5e1;
    background: #f9fafb;
}

.table-card {
    border-radius: 16px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.table-card :deep(.el-card__body) {
    padding: 0;
}

.promotion-table,
.product-table,
.product-detail-table {
    border-radius: 16px;
    overflow: hidden;
}

.promotion-table :deep(.el-table__header),
.product-table :deep(.el-table__header),
.product-detail-table :deep(.el-table__header) {
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.promotion-table :deep(.el-table__header th),
.product-table :deep(.el-table__header th),
.product-detail-table :deep(.el-table__header th) {
    background: transparent;
    color: #374151;
    font-weight: 600;
    font-size: 14px;
    border-bottom: 2px solid #e2e8f0;
    padding: 16px 12px;
}

.promotion-table :deep(.el-table__body tr),
.product-table :deep(.el-table__body tr),
.product-detail-table :deep(.el-table__body tr) {
    transition: all 0.3s ease;
}

.promotion-table :deep(.el-table__body tr:hover),
.product-table :deep(.el-table__body tr:hover),
.product-detail-table :deep(.el-table__body tr:hover) {
    background: #f8fafc;
}

.promotion-table :deep(.even-row),
.product-table :deep(.even-row),
.product-detail-table :deep(.even-row) {
    background: #fafbfc;
}

.promotion-table :deep(.odd-row),
.product-table :deep(.odd-row),
.product-detail-table :deep(.odd-row) {
    background: white;
}

.promotion-table :deep(.el-table__body td),
.product-table :deep(.el-table__body td),
.product-detail-table :deep(.el-table__body td) {
    padding: 16px 12px;
    border-bottom: 1px solid #f1f5f9;
}

.row-index {
    font-weight: 600;
    color: #6b7280;
}

.code-cell {
    display: flex;
    align-items: center;
}

.code-tag {
    font-family: 'Monaco', 'Menlo', monospace;
    font-weight: 600;
}

.name-cell {
    display: flex;
    align-items: center;
}

.promotion-name {
    font-weight: 500;
    color: #1f2937;
}

.value-cell {
    text-align: center;
}

.discount-percent {
    font-weight: 700;
    color: #059669;
    font-size: 16px;
}

.date-range {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.date-item {
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 12px;
    color: #6b7280;
}

.date-icon {
    font-size: 12px;
    color: #9ca3af;
}

.date-separator {
    text-align: center;
    color: #9ca3af;
    font-size: 12px;
}

.action-buttons {
    display: flex;
    gap: 8px;
    justify-content: center;
}

.action-btn {
    width: 32px;
    height: 32px;
    border-radius: 8px;
    margin-left: 0;
    transition: all 0.3s ease;
}

.view-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(100, 116, 139, 0.4);
}

.edit-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.delete:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(239, 68, 68, 0.4);
}

.pagination-wrapper {
    padding: 24px;
    display: flex;
    justify-content: center;
    background: #fafbfc;
    border-top: 1px solid #f1f5f9;
}

.custom-pagination :deep(.el-pagination) {
    gap: 8px;
}

.custom-pagination :deep(.el-pager li) {
    border-radius: 8px;
    margin: 0 2px;
    transition: all 0.3s ease;
}

.custom-pagination :deep(.el-pager li:hover) {
    background: #667eea;
    color: white;
}

.custom-pagination :deep(.el-pager li.is-active) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
}

.promotion-dialog :deep(.el-dialog) {
    border-radius: 16px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.promotion-dialog :deep(.el-dialog__header) {
    padding: 24px 24px 0;
    border-bottom: none;
}

.promotion-dialog :deep(.el-dialog__title) {
    font-size: 24px;
    font-weight: 700;
    color: #1a202c;
}

.dialog-content {
    padding: 0 24px;
    max-height: 70vh;
    overflow-y: auto;
}

.promotion-form {
    padding: 24px 0;
}

.form-grid {
    display: flex;
    flex-direction: column;
    gap: 32px;
}

.form-section {
    background: #f8fafc;
    border-radius: 12px;
    padding: 24px;
    border: 1px solid #e2e8f0;
}

.section-title {
    font-size: 18px;
    font-weight: 600;
    color: #374151;
    margin: 0 0 20px 0;
    padding-bottom: 12px;
    border-bottom: 2px solid #e2e8f0;
}

.form-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
    margin-bottom: 20px;
}

.form-item {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.form-item.full-width {
    grid-column: 1 / -1;
}

.form-item :deep(.el-form-item__label) {
    font-weight: 500;
    color: #374151;
    margin-bottom: 8px;
}

.form-item :deep(.el-input__wrapper),
.form-item :deep(.el-select .el-input__wrapper),
.form-item :deep(.el-date-editor.el-input),
.form-item :deep(.el-textarea__inner) {
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    transition: all 0.3s ease;
}

.form-item :deep(.el-input__wrapper:hover),
.form-item :deep(.el-select .el-input__wrapper:hover),
.form-item :deep(.el-date-editor.el-input:hover),
.form-item :deep(.el-textarea__inner:hover) {
    border-color: #cbd5e1;
}

.form-item :deep(.el-input__wrapper.is-focus),
.form-item :deep(.el-select .el-input__wrapper.is-focus),
.form-item :deep(.el-date-editor.el-input.is-focus),
.form-item :deep(.el-textarea__inner:focus) {
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-item :deep(.el-form-item__error) {
    font-size: 12px;
    color: #ef4444;
    margin-top: 4px;
}

.full-width {
    width: 100%;
}

.dialog-footer {
    padding: 24px;
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    border-top: 1px solid #f1f5f9;
}

.cancel-btn {
    height: 44px;
    padding: 0 24px;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    background: white;
    color: #6b7280;
    font-weight: 500;
}

.save-btn,
.delete-btn {
    height: 44px;
    padding: 0 24px;
    border-radius: 8px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    font-weight: 600;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.delete-btn {
    background: #ef4444;
    box-shadow: 0 4px 15px rgba(239, 68, 68, 0.4);
}

.save-btn:hover,
.delete-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

.quick-filter-buttons {
    display: flex;
    gap: 10px;
    margin-bottom: 16px;
}

.quick-filter-buttons .el-button {
    border-radius: 8px;
    font-weight: 500;
}

.promotion-conflict-dialog :deep(.el-dialog) {
    border-radius: 16px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
    background: #ffffff;
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.promotion-conflict-dialog :deep(.el-dialog__header) {
    padding: 24px 24px 0;
    border-bottom: none;
}

.promotion-conflict-dialog :deep(.el-dialog__title) {
    font-size: 24px;
    font-weight: 700;
    color: #1a202c;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.conflict-dialog-content {
    padding: 0 24px;
    max-height: 60vh;
    overflow-y: auto;
}

.conflict-message,
.conflict-prompt {
    font-size: 16px;
    color: #374151;
    margin: 0 0 16px 0;
    line-height: 1.5;
}

.conflict-message {
    font-weight: 500;
}

.conflict-prompt {
    margin-top: 24px;
    font-style: italic;
}

.conflict-search-input {
    margin-bottom: 16px;
}

.conflict-search-input :deep(.el-input__wrapper) {
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    transition: all 0.3s ease;
}

.conflict-search-input :deep(.el-input__wrapper:hover) {
    border-color: #cbd5e1;
}

.conflict-search-input :deep(.el-input__wrapper.is-focus) {
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.conflict-table {
    border-radius: 12px;
    overflow: hidden;
    background: #fafbfc;
}

.conflict-table :deep(.el-table__header) {
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.conflict-table :deep(.el-table__header th) {
    background: transparent;
    color: #374151;
    font-weight: 600;
    font-size: 14px;
    padding: 16px 12px;
    border-bottom: 2px solid #e2e8f0;
}

.conflict-table :deep(.el-table__body tr) {
    transition: all 0.3s ease;
}

.conflict-table :deep(.el-table__body tr:hover) {
    background: #f1f5f9;
}

.conflict-table :deep(.el-table__body td) {
    padding: 12px;
    border-bottom: 1px solid #f1f5f9;
    color: #1f2937;
    font-size: 14px;
}

.conflict-table :deep(.promotion-tag) {
    font-weight: 500;
    border-radius: 8px;
    padding: 4px 12px;
    background: #fef3c7;
    color: #92400e;
    border: none;
}

.dialog-footer {
    padding: 24px;
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    border-top: 1px solid #f1f5f9;
    background: #fafbfc;
}

.cancel-btn {
    height: 44px;
    padding: 0 24px;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    background: white;
    color: #6b7280;
    font-weight: 500;
    transition: all 0.3s ease;
}

.cancel-btn:hover {
    background: #f9fafb;
    border-color: #cbd5e1;
    transform: translateY(-1px);
}

.confirm-btn {
    height: 44px;
    padding: 0 24px;
    border-radius: 8px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    font-weight: 600;
    color: white;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    transition: all 0.3s ease;
}

.confirm-btn:hover {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
}

.confirm-btn:disabled {
    background: #d1d5db;
    box-shadow: none;
    cursor: not-allowed;
}

.pagination-wrapper {
    padding: 16px 0;
    display: flex;
    justify-content: center;
    background: #fafbfc;
}

.custom-pagination :deep(.el-pagination) {
    gap: 8px;
}

.custom-pagination :deep(.el-pager li) {
    border-radius: 8px;
    margin: 0 2px;
    transition: all 0.3s ease;
}

.custom-pagination :deep(.el-pager li:hover) {
    background: #667eea;
    color: white;
}

.custom-pagination :deep(.el-pager li.is-active) {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
}

@media (max-width: 768px) {
    .promotion-conflict-dialog :deep(.el-dialog) {
        width: 90%;
        margin: 10px auto;
    }

    .conflict-dialog-content {
        max-height: 50vh;
        padding: 0 16px;
    }

    .promotion-conflict-dialog :deep(.el-dialog__title) {
        font-size: 20px;
    }

    .conflict-message,
    .conflict-prompt {
        font-size: 14px;
    }

    .conflict-table :deep(.el-table__header th),
    .conflict-table :deep(.el-table__body td) {
        padding: 10px 8px;
        font-size: 12px;
    }

    .dialog-footer {
        flex-direction: column;
        gap: 8px;
        padding: 16px;
    }

    .cancel-btn,
    .confirm-btn {
        width: 100%;
    }
}

@media (max-width: 480px) {
    .promotion-conflict-dialog :deep(.el-dialog__title) {
        font-size: 18px;
    }

    .conflict-table :deep(.el-table__header th),
    .conflict-table :deep(.el-table__body td) {
        padding: 8px 4px;
        font-size: 11px;
    }

    .conflict-table :deep(.promotion-tag) {
        padding: 2px 8px;
        font-size: 12px;
    }
}

@media (max-width: 1200px) {
    .filter-group {
        grid-template-columns: 1fr;
        gap: 16px;
    }

    .form-row {
        grid-template-columns: 1fr;
    }
}

@media (max-width: 768px) {
    .promotion-container {
        padding: 16px;
    }

    .page-header {
        padding: 24px;
    }

    .header-content {
        flex-direction: column;
        align-items: stretch;
        gap: 16px;
    }

    .page-title {
        font-size: 24px;
    }

    .promotion-table :deep(.el-table__header th),
    .promotion-table :deep(.el-table__body td),
    .product-table :deep(.el-table__header th),
    .product-table :deep(.el-table__body td),
    .product-detail-table :deep(.el-table__header th),
    .product-detail-table :deep(.el-table__body td) {
        padding: 12px 8px;
    }

    .action-buttons {
        flex-direction: column;
        gap: 4px;
    }

    .dialog-content {
        max-height: 60vh;
    }

    .form-section {
        padding: 16px;
    }

    .dialog-footer {
        flex-direction: column;
        gap: 8px;
    }

    .cancel-btn,
    .save-btn,
    .delete-btn {
        width: 100%;
    }
}

@media (max-width: 480px) {

    .promotion-table :deep(.el-table__header th),
    .promotion-table :deep(.el-table__body td),
    .product-table :deep(.el-table__header th),
    .product-table :deep(.el-table__body td),
    .product-detail-table :deep(.el-table__header th),
    .product-detail-table :deep(.el-table__body td) {
        padding: 8px 4px;
        font-size: 12px;
    }

    .date-range {
        font-size: 10px;
    }

    .action-btn {
        width: 28px;
        height: 28px;
    }
}
</style>