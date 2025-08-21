<template>
    <div class="warranty-container">
        <div class="page-header">
            <div class="header-content">
                <div class="title-section">
                    <h1 class="page-title">Quản lý loại bảo hành</h1>
                    <p class="page-subtitle">Tạo và quản lý các loại bảo hành</p>
                </div>
                <el-button v-if="isAdmin" type="primary" size="large" class="create-btn" @click="openCreateDialog()">
                    <el-icon class="btn-icon">
                        <Plus />
                    </el-icon>
                    Thêm loại bảo hành
                </el-button>
            </div>
        </div>

        <el-card class="filters-card" shadow="never">
            <div class="filters-content">
                <div class="filter-group">
                    <div class="filter-item">
                        <label class="filter-label">Tìm kiếm</label>
                        <el-input v-model="search" placeholder="Tìm kiếm theo tên hoặc mô tả..." clearable
                            class="filter-input" @input="fetchLoaiBaoHanh">
                            <template #prefix>
                                <el-icon class="search-icon">
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
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
            <el-table v-loading="loading" :data="loaiBaoHanhs" class="warranty-table" :row-class-name="getRowClassName"
                empty-text="Không có dữ liệu">
                <el-table-column label="STT" width="75" align="center">
                    <template #default="scope">
                        <span class="row-index">{{ scope.$index + 1 + (pagination.page - 1) * pagination.size }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="tenLoaiBaoHanh" label="Tên loại bảo hành" min-width="170">
                    <template #default="scope">
                        <div class="code-cell">
                            <el-tag type="info" size="small" class="code-tag">
                                {{ scope.row.tenLoaiBaoHanh }}
                            </el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="Thời gian tháng" width="160" align="center">
                    <template #default="scope">
                        <div class="value-cell">
                            <span class="warranty-percent">{{ scope.row.thoiGianThang }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="moTa" label="Mô tả" min-width="300">
                    <template #default="scope">
                        <div class="name-cell">
                            <span class="warranty-name">{{ scope.row.moTa }}</span>
                        </div>
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
                                    @click="xoaBaoHanh(scope.row)" class="action-btn delete" />
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
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus, Edit, Delete, Search, Refresh, Calendar, View } from '@element-plus/icons-vue';
import { getAllLoaiBaoHanh } from '@/Service/Adminservice/BaoHanh/LoaiBaoHanhService';
import store from '@/Service/LoginService/Store';

const search = ref('');
const trangThai = ref('');
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);
const loading = ref(false);
const loaiBaoHanhs = ref([]);
const selectedBaoHanh = ref(null);
const pagination = ref({
    page: 1,
    size: 5,
    total: 0,
});
const form = ref({
    id: null,
    tenLoaiBaoHanh: '',
    thoiGianThang: '',
    moTa: ''
});
const formRef = ref(null);

const isAdmin = computed(() => {
    const roles = store.state.roles;
    return Array.isArray(roles) && roles
        .map((role) => (typeof role === 'string' ? role : role.authority))
        .includes('ROLE_ADMIN');
});

const fetchLoaiBaoHanh = async () => {
    loading.value = true;
    try {
        const response = await getAllLoaiBaoHanh({
            search: search.value,
            page: pagination.value.page - 1,
            size: pagination.value.size,
        });
        loaiBaoHanhs.value = response.content;
        pagination.value.total = response.totalElements;
    } catch (error) {
        ElMessage.error(error?.response?.data?.message || 'Lỗi khi lấy danh sách loại bảo hành');
    } finally {
        loading.value = false;
    }
};

const clearFilters = () => {
    search.value = '';
    pagination.value.page = 1;
    fetchLoaiBaoHanh();
};

const updatePage = (page) => {
    pagination.value.page = page;
    fetchLoaiBaoHanh();
};

const updateSize = (size) => {
    pagination.value.size = size;
    pagination.value.page = 1;
    fetchLoaiBaoHanh();
};

const getRowClassName = ({ rowIndex }) => {
    return rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
};

watch(search, () => {
    pagination.value.page = 1;
    fetchLoaiBaoHanh();
});

onMounted(() => {
    fetchLoaiBaoHanh();
});
</script>

<style scoped>
.warranty-container {
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

.warranty-table {
    border-radius: 16px;
    overflow: hidden;
}

.warranty-table :deep(.el-table__header) {
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
}

.warranty-table :deep(.el-table__header th) {
    background: transparent;
    color: #374151;
    font-weight: 600;
    font-size: 14px;
    border-bottom: 2px solid #e2e8f0;
    padding: 16px 12px;
}

.warranty-table :deep(.el-table__body tr) {
    transition: all 0.3s ease;
}

.warranty-table :deep(.el-table__body tr:hover) {
    background: #f8fafc;
}

.warranty-table :deep(.even-row) {
    background: #fafbfc;
}

.warranty-table :deep(.odd-row) {
    background: white;
}

.warranty-table :deep(.el-table__body td) {
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

.warranty-name {
    font-weight: 500;
    color: #1f2937;
}

.value-cell {
    text-align: center;
}

.warranty-percent {
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
    .warranty-container {
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

    .warranty-table :deep(.el-table__header th),
    .warranty-table :deep(.el-table__body td) {
        padding: 12px 8px;
    }

    .action-buttons {
        flex-direction: column;
        gap: 4px;
    }
}

@media (max-width: 480px) {

    .warranty-table :deep(.el-table__header th),
    .warranty-table :deep(.el-table__body td) {
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