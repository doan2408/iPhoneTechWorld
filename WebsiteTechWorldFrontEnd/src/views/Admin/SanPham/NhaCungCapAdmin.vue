<template>
    <div class="container mt-4">
        <el-row :gutter="20" class="mb-4 justify-content-center">
            <el-col :span="6">
                <el-input v-model="searchQuery" placeholder="Tìm kiếm" clearable />
            </el-col>
            <el-col :span="3">
                <el-button type="primary" @click="handleSearch" class="w-100">Tìm kiếm</el-button>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="mb-2" justify="end">
            <el-col :span="3">
                <el-button type="primary" @click="handleCreate" class="w-100">Tạo mới</el-button>
            </el-col>
            <el-col :span="3">
                <el-button type="primary" @click="handleRefresh" class="w-100">Làm mới</el-button>
            </el-col>
        </el-row>

        <h2>Danh sách pin</h2>
        <div class="table-responsive mb-4" style="margin-top: 20px;">
            <el-table :data="tableNCC" border style="width: 100%">
                <el-table-column type="index" :index="indexMethod" label="STT" width="80" />
                <el-table-column prop="tenNhaCungCap" label="Tên nhà cung cấp" />
                <el-table-column prop="diaChi" label="Địa chỉ" />
                <el-table-column prop="sdt" label="Số điện thoại" />
                <el-table-column prop="email" label="Email" />
                <el-table-column label="Thao tác" width="180">
                    <template #default="{ row }">
                        <div class="action-buttons-horizontal">
                            <el-button size="small" type="primary" :icon="Edit" @click="openDetail(row)" circle />
                            <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(row.id)" />
                            <!-- <router-link :to="`/admin/products/detail/${row.id}`">
                                <el-button size="small" type="info" :icon="View" circle />
                            </router-link> -->
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div class="pagination-fixed">
            <div class="d-flex justify-content-center align-items-center gap-3">
                <el-pagination background layout="prev, pager, next" :page-size="pageSize" :current-page="currentPage"
                    :total="totalItems" :pager-count="7" prev-text="< Trước" next-text="Sau >"
                    @current-change="handlePageChange" />
            </div>
        </div>

        <el-dialog v-model="dialogVisible" :title="isEditMode ? 'Chỉnh sửa hệ điều hành' : 'Thêm mới hệ điều hành'"
            width="900px" :close-on-click-modal="false" :destroy-on-close="true">
            <el-form :model="formData" ref="formRef" :rules="rules" label-width="140px" label-position="left"
                class="hdh-form">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="Tên nhà cung cấp" prop="tenNhaCungCap">
                            <el-input v-model="formData.tenNhaCungCap"
                                placeholder="Nhập tên nhà cung cấp (ví dụ: Công ty ABC)" autocomplete="on" />
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="Địa chỉ" prop="diaChi">
                            <el-input v-model="formData.diaChi"
                                placeholder="Nhập địa chỉ (ví dụ: 123 Lê Lợi, Q.1, TP.HCM)" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="Số điện thoại" prop="sdt">
                            <el-input v-model="formData.sdt" placeholder="Nhập số điện thoại (ví dụ: 0901234567)" />
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item label="Email" prop="email">
                            <el-input v-model="formData.email" placeholder="Nhập email (ví dụ: example@gmail.com)" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row justify="end" style="margin-top: 30px;">
                    <el-button @click="handleClose" style="margin-right: 10px;">Hủy</el-button>
                    <el-button type="primary" @click="submitForm">{{ isEditMode ? 'Cập nhật' : 'Lưu' }}</el-button>
                </el-row>
            </el-form>
        </el-dialog>

    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, reactive } from 'vue';
import { deleteNCC, getAllNhaCungCapPage, postNCC, putNCC } from '@/Service/Adminservice/Products/ProductAdminService';
import { Edit, Delete, View } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const tableNCC = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const totalItems = ref(0);
const pageSize = 5;
const searchQuery = ref('');
const dialogVisible = ref(false);
const isEditMode = ref(false);
const formRef = ref(null);

const rules = {
    tenNhaCungCap: [
        { required: true, message: 'Vui lòng nhập tên nhà cung cấp', trigger: 'blur' },
        { min: 2, max: 100, message: 'Tên phải từ 2 đến 100 ký tự', trigger: 'blur' }
    ],
    diaChi: [
        { required: true, message: 'Vui lòng nhập địa chỉ', trigger: 'blur' },
        { max: 200, message: 'Địa chỉ tối đa 200 ký tự', trigger: 'blur' }
    ],
    sdt: [
        { required: true, message: 'Vui lòng nhập số điện thoại', trigger: 'blur' },
        {
            pattern: /^(0|\+84)[0-9]{9,10}$/,
            message: 'Số điện thoại không hợp lệ (bắt đầu bằng 0 hoặc +84)',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: 'Vui lòng nhập email', trigger: 'blur' },
        {
            type: 'email',
            message: 'Email không đúng định dạng',
            trigger: ['blur', 'change']
        }
    ]
};


const formData = reactive({
    id: null,
    tenNhaCungCap: '',
    diaChi: '',
    sdt: '',
    email: '',
})

const fromRecord = computed(() => {
    return totalItems.value === 0 ? 0 : (currentPage.value - 1) * pageSize + 1;
});
const toRecord = computed(() => {
    return Math.min(currentPage.value * pageSize, totalItems.value);
});

const loadData = async () => {
    try {
        const response = await getAllNhaCungCapPage(currentPage.value - 1, pageSize, searchQuery.value);
        tableNCC.value = response.content;
        totalPages.value = response.totalPages;
        totalItems.value = response.totalElements;
    } catch (error) {
        console.error('Không thể load dữ liệu:', error);
    }
};

const resetForm = () => {
    formData.id = null;
    formData.tenNhaCungCap = '';
    formData.diaChi = '';
    formData.sdt = '';
    formData.email = '';
};

const handleSearch = () => {
    currentPage.value = 1;
};

const handleRefresh = () => {
    searchQuery.value = '';
    currentPage.value = 1;
};

const submitForm = async () => {
    if (!formRef.value) return;

    try {
        await formRef.value.validate();
        if (isEditMode.value) {
            await putNCC(formData.id, formData);
            ElMessage.success('Cập nhật nhà cung cấp thành công!');
        } else {
            await postNCC(formData);
            ElMessage.success('Thêm mới nhà cung cấp thành công!');
        }

        resetForm();
        dialogVisible.value = false;
        loadData();
    } catch (error) {
        console.error('Lỗi xử lý form', error);
        ElMessage.error('Vui lòng kiểm tra lại thông tin');
    }
}

const openDetail = (row) => {
    isEditMode.value = true;
    Object.assign(formData, row);
    dialogVisible.value = true;
};

const handleCreate = () => {
    resetForm();
    dialogVisible.value = true;
};

const handleClose = () => {
    dialogVisible.value = false;
};


const handleDelete = async (id) => {
    try {
        await ElMessageBox.confirm(
            'Bạn có chắc chắn muốn xoá nhà cung cấp này không?',
            'Xác nhận xoá',
            {
                confirmButtonText: 'Xoá',
                cancelButtonText: 'Huỷ',
                type: 'warning',
            }
        );
        await deleteNCC(id);
        ElMessage.success('Xoá thành công!');
        loadData(); // tải lại danh sách sau khi xoá
    } catch (error) {
        if (error !== 'cancel') {
            ElMessage.error('Xoá thất bại! Vui lòng thử lại.');
        }
    }
};

const indexMethod = (index) => {
    return (currentPage.value - 1) * pageSize + index + 1;
};

const handlePageChange = (newPage) => {
    currentPage.value = newPage;
};

onMounted(() => {
    loadData();
});

watch([currentPage, searchQuery], () => {
    loadData();
});

</script>




<style scoped>
::v-deep(.el-pagination button) {
    font-size: 16px;
    padding: 8px 16px;
}

::v-deep(.el-pagination .el-pager li) {
    font-size: 16px;
    padding: 6px 10px;
}



.pagination-fixed {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin-top: 24px;
    border: 1px solid #e5e7eb;
}


* {
    box-sizing: border-box;
}

/* Dashboard Container */
.container {
    min-height: 100vh;
    background: #f8f9fa;
    padding: 24px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    max-width: 1400px;
    margin: 0 auto;
}

.mt-4 {
    margin-top: 24px;
}

.mb-4 {
    margin-bottom: 32px;
}

.mb-3 {
    margin-bottom: 24px;
}

.w-100 {
    width: 100%;
}

/* ===== SEARCH SECTION ===== */
:deep(.el-row) {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 24px;
    margin-bottom: 24px;
    border: 1px solid #e5e7eb;
}

/* Input Styling */
:deep(.el-input__wrapper) {
    border: 1px solid #d1d5db;
    border-radius: 6px;
    background: white;
    transition: border-color 0.2s ease;
}

:deep(.el-input__wrapper:hover) {
    border-color: #9ca3af;
}

:deep(.el-input__wrapper.is-focus) {
    border-color: #3b82f6;
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
}

:deep(.el-input__inner) {
    font-size: 14px;
    color: #374151;
}

/* ===== BUTTON STYLES ===== */
:deep(.el-button) {
    border-radius: 6px;
    font-weight: 500;
    transition: all 0.2s ease;
    border: 1px solid;
}

:deep(.el-button:hover) {
    transform: translateY(-1px);
}

:deep(.el-button--primary) {
    background: #3b82f6;
    border-color: #3b82f6;
    color: white;
}

:deep(.el-button--primary:hover) {
    background: #2563eb;
    border-color: #2563eb;
}

:deep(.el-button--success) {
    background: #10b981;
    border-color: #10b981;
    color: white;
}

:deep(.el-button--success:hover) {
    background: #059669;
    border-color: #059669;
}

:deep(.el-button--danger) {
    background: #ef4444;
    border-color: #ef4444;
    color: white;
}

:deep(.el-button--danger:hover) {
    background: #dc2626;
    border-color: #dc2626;
}

:deep(.el-button--info) {
    background: #6b7280;
    border-color: #6b7280;
    color: white;
}

:deep(.el-button--info:hover) {
    background: #4b5563;
    border-color: #4b5563;
}

/* Add Product Section */
.mb-3 {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 20px 24px;
    border: 1px solid #e5e7eb;
}

/* ===== TITLE STYLING ===== */
h2 {
    font-size: 24px;
    font-weight: 600;
    margin: 0 0 24px 0;
    color: #1f2937;
    text-align: center;
    padding-bottom: 12px;
    border-bottom: 2px solid #e5e7eb;
}

/* ===== SIMPLE TABLE STYLING - NO STRIPE ===== */
:deep(.el-table) {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    border: 1px solid #e5e7eb;
}

:deep(.el-table__header th) {
    background: #f9fafb !important;
    color: #374151 !important;
    font-weight: 600;
    font-size: 14px;
    border-bottom: 1px solid #e5e7eb !important;
    padding: 12px 16px;
}

/* Remove all striped and hover effects */
:deep(.el-table__body tr) {
    background: white !important;
}

:deep(.el-table__body tr:hover) {
    background: white !important;
}

:deep(.el-table__body tr.el-table__row--striped) {
    background: white !important;
}

:deep(.el-table__body tr.el-table__row--striped:hover) {
    background: white !important;
}

:deep(.el-table td) {
    padding: 12px 16px;
    font-size: 14px;
    color: #374151;
    border-color: #f3f4f6;
    background: white !important;
}

/* Image Styling */
:deep(.el-table td img) {
    width: 50px !important;
    height: 50px !important;
    object-fit: cover;
    border-radius: 6px;
    border: 1px solid #e5e7eb;
}

/* ===== HORIZONTAL ACTION BUTTONS ===== */
.action-buttons-horizontal {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    flex-wrap: nowrap;
}

/* Action Buttons in Table */
:deep(.el-table td .el-button) {
    width: 32px;
    height: 32px;
    padding: 0;
    border-radius: 6px;
    flex-shrink: 0;
}

/* ===== PAGINATION SECTION ===== */
.pagination-fixed {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin-top: 24px;
    border: 1px solid #e5e7eb;
}

.d-flex {
    display: flex;
}

.justify-content-center {
    justify-content: center;
}

.align-items-center {
    align-items: center;
}

.gap-3 {
    gap: 12px;
}

:deep(.el-pagination button) {
    background: white;
    border: 1px solid #d1d5db;
    color: #374151;
    font-size: 14px;
    border-radius: 6px;
    margin: 0 2px;
    padding: 6px 12px;
}

:deep(.el-pagination button:hover) {
    background: #f3f4f6;
    border-color: #9ca3af;
}

:deep(.el-pagination .el-pager li) {
    background: white;
    border: 1px solid #d1d5db;
    color: #374151;
    font-size: 14px;
    border-radius: 6px;
    margin: 0 2px;
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
}

:deep(.el-pagination .el-pager li:hover) {
    background: #f3f4f6;
    border-color: #9ca3af;
}

:deep(.el-pagination .el-pager li.is-active) {
    background: #3b82f6;
    border-color: #3b82f6;
    color: white;
}

/* ===== SKELETON LOADING ===== */
:deep(.el-skeleton) {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 24px;
    border: 1px solid #e5e7eb;
}

/* ===== ALERT ===== */
:deep(.el-alert) {
    border-radius: 8px;
    margin-top: 24px;
}

/* ===== RESPONSIVE DESIGN ===== */
@media (max-width: 768px) {
    .container {
        padding: 16px;
    }

    h2 {
        font-size: 20px;
    }

    :deep(.el-row) {
        padding: 16px;
    }

    :deep(.el-table td) {
        padding: 8px 12px;
        font-size: 13px;
    }

    :deep(.el-table td img) {
        width: 40px !important;
        height: 40px !important;
    }

    .action-buttons-horizontal {
        gap: 4px;
    }

    :deep(.el-table td .el-button) {
        width: 28px;
        height: 28px;
    }

    .pagination-fixed {
        padding: 16px;
    }

    :deep(.el-pagination button) {
        padding: 4px 8px;
        font-size: 12px;
    }

    :deep(.el-pagination .el-pager li) {
        width: 32px;
        height: 32px;
        font-size: 12px;
    }
}
</style>