<script setup>
import { ref, onMounted, watch, reactive } from "vue";
import { getAll, detail, add, update, deletePhieuGiamGia, getAllKhachHang } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";
import { ElMessage } from "element-plus";

const phieuGiamGias = ref([]);
const isLoading = ref(false);
const phieuGiamGiaDialogVisible = ref(false);
const dialog = ref(null);
const khachHangList = ref([]);

const currentPage = ref(0);
const totalPages = ref(0);
const pageSize = ref(5);

const search = ref("");
const trangThaiFilter = ref(null);
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);

const formData = reactive({
    id: null,
    maGiamGia: "#####",
    tenKhuyenMai: "",
    loaiKhuyenMai: "Phần trăm",
    giaTriKhuyenMai: 0,
    giaTriDonHangToiThieu: 0,
    giaTriKhuyenMaiToiDa: 0,
    ngayBatDau: "",
    ngayKetThuc: "",
    hangToiThieu: "MEMBER",
    soDiemCanDeDoi: 0,
    soLuong: 0,
    dieuKienApDung: "",
    trangThai: "ACTIVE",
    isGlobal: true,
    khachHangIds: [],
})

const errors = reactive({
    maGiamGia: '',
    tenKhuyenMai: '',
    giaTriKhuyenMai: '',
    giaTriDonHangToiThieu: '',
    giaTriKhuyenMaiToiDa: '',
    ngayBatDau: '',
    ngayKetThuc: '',
    soDiemCanDeDoi: '',
    soLuong: '',
    dieuKienApDung: '',
})

const resetForm = () => {
    formData.id = null;
    formData.maGiamGia = "#####";
    formData.tenKhuyenMai = "";
    formData.loaiKhuyenMai = "Phần trăm";
    formData.giaTriKhuyenMai = 0;
    formData.giaTriDonHangToiThieu = 0;
    formData.giaTriKhuyenMaiToiDa = 0;
    formData.ngayBatDau = "";
    formData.ngayKetThuc = "";
    formData.hangToiThieu = "MEMBER";
    formData.soDiemCanDeDoi = 0;
    formData.soLuong = 0;
    formData.dieuKienApDung = "";
    formData.trangThai = "ACTIVE";
    formData.isGlobal = true;
    formData.khachHangIds = []
}

const resetErrors = () => {
    errors.maGiamGia = '';
    errors.tenKhuyenMai = '';
    errors.giaTriKhuyenMai = '';
    errors.giaTriDonHangToiThieu = '';
    errors.giaTriKhuyenMaiToiDa = '';
    errors.ngayBatDau = '';
    errors.ngayKetThuc = '';
    errors.soDiemCanDeDoi = '';
    errors.soLuong = '';
    errors.dieuKienApDung = ''
}

const loadPhieuGiamGia = async (page) => {
    try {
        isLoading.value = true;
        const response = await getAll({
            page: page,
            size: pageSize.value,
            search: search.value || null,
            trangThai: trangThaiFilter.value || null,
            ngayBatDau: ngayBatDauFilter.value || null,
            ngayKetThuc: ngayKetThucFilter.value || null,
        });
        phieuGiamGias.value = response.content;
        totalPages.value = response.totalPages;
        currentPage.value = page;
    } catch (err) {
        console.error(err.message || "Lỗi lấy danh sách phiếu giảm giá");
        alert(err.message || "Lỗi lấy danh sách phiếu giảm giá");
    } finally {
        isLoading.value = false;
    }
};

const loadKhachHang = async () => {
    try {
        khachHangList.value = await getAllKhachHang();
        if (!khachHangList.value.length) {
            alert("Không có khách hàng nào trong hệ thống!");
        }
    } catch (err) {
        console.error(err.message || "Lỗi lấy danh sách khách hàng");
        alert(err.message || "Lỗi lấy danh sách khách hàng");
    }
};

const createPhieuGiamGia = () => {
    phieuGiamGiaDialogVisible.value = true;
};

const savePhieuGiamGia = async () => {
    try {
        isLoading.value = true;

        if (formData.id) {
            await update(formData.id, formData);
            alert("Cập nhật phiếu giảm giá thành công!");
        } else {
            await add(formData);
            alert("Thêm phiếu giảm giá thành công!");
        }
        phieuGiamGiaDialogVisible.value = false;
        dialog.value?.close();
        loadPhieuGiamGia(currentPage.value);
    } catch (error) {
        console.log(error)
        errors.maGiamGia = error.message.maGiamGia || ''
        errors.tenKhuyenMai = error.message.tenKhuyenMai || ''
        errors.giaTriKhuyenMai = error.message.giaTriKhuyenMai || ''
        errors.giaTriDonHangToiThieu = error.message.giaTriDonHangToiThieu || ''
        errors.giaTriKhuyenMaiToiDa = error.message.giaTriKhuyenMaiToiDa || ''
        errors.ngayBatDau = error.message.ngayBatDau || ''
        errors.ngayKetThuc = error.message.ngayKetThuc || ''
        errors.soDiemCanDeDoi = error.message.soDiemCanDeDoi || ''
        errors.soLuong = error.message.soLuong || ''
        errors.dieuKienApDung = error.message.dieuKienApDung || ''

        const errorMessages = [];
        if (errors.maGiamGia) errorMessages.push(errors.maGiamGia);
        if (errors.tenKhuyenMai) errorMessages.push(errors.tenKhuyenMai);
        if (errors.giaTriKhuyenMai) errorMessages.push(errors.giaTriKhuyenMai);
        if (errors.giaTriDonHangToiThieu) errorMessages.push(errors.giaTriDonHangToiThieu);
        if (errors.giaTriKhuyenMaiToiDa) errorMessages.push(errors.giaTriKhuyenMaiToiDa);
        if (errors.ngayBatDau) errorMessages.push(errors.ngayBatDau);
        if (errors.ngayKetThuc) errorMessages.push(errors.ngayKetThuc);
        if (errors.soDiemCanDeDoi) errorMessages.push(errors.soDiemCanDeDoi);
        if (errors.soLuong) errorMessages.push(errors.soLuong);
        if (errors.dieuKienApDung) errorMessages.push(errors.dieuKienApDung);

        if (errorMessages.length > 0) {
            ElMessage.error('Đã xảy ra lỗi không xác định');
        } else {
            ElMessage.error(error.message || 'Đã xảy ra lỗi không xác định');
        }
    } finally {
        isLoading.value = false;
    }
};

const handleDeletePhieuGiamGia = async (id) => {
    if (!confirm("Bạn có chắc chắn muốn xóa phiếu giảm giá này?")) return;
    try {
        isLoading.value = true;
        await deletePhieuGiamGia(id);
        alert("Xóa phiếu giảm giá thành công!");
        loadPhieuGiamGia(currentPage.value);
    } catch (error) {
        console.error(error.message || "Lỗi khi xóa phiếu giảm giá");
        alert(error.message || "Lỗi khi xóa phiếu giảm giá");
    } finally {
        isLoading.value = false;
    }
};

const nextPage = () => {
    if (currentPage.value < totalPages.value - 1) {
        loadPhieuGiamGia(currentPage.value + 1);
    }
};

const previousPage = () => {
    if (currentPage.value > 0) {
        loadPhieuGiamGia(currentPage.value - 1);
    }
};

const clear = () => {
    currentPage.value = 0;
    search.value = "";
    trangThaiFilter.value = null;
    ngayBatDauFilter.value = null;
    ngayKetThucFilter.value = null;
    loadPhieuGiamGia(0);
};

const trangThaiLabels = {
    NOT_STARTED: "Chưa bắt đầu",
    ACTIVE: "Đang hoạt động",
    EXPIRED: "Đã hết hạn",
};

const convertTrangThai = (trangThai) => {
    return trangThaiLabels[trangThai] || "Không xác định";
};

const viewUpdate = async (id) => {
    try {
        const response = await detail(id);
        Object.assign(formData, { ...response, khachHangIds: response.khachHangIds || [] })
        phieuGiamGiaDialogVisible.value = true;
    } catch (error) {
        console.error(error.message || "Lỗi khi tải chi tiết phiếu giảm giá");
        alert(error.message || "Lỗi khi tải chi tiết phiếu giảm giá");
    }
};

const formatDate = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    return `${date.getDate().toString().padStart(2, "0")}/${(date.getMonth() + 1).toString().padStart(2, "0")}/${date.getFullYear()}`;
};

const formatCurrency = (value) => {
    return new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND" }).format(value);
};

watch([search, trangThaiFilter, ngayBatDauFilter, ngayKetThucFilter], () => {
    loadPhieuGiamGia(0);
});

watch(phieuGiamGiaDialogVisible, (visible) => {
    if (visible) {
        dialog.value?.showModal();
    } else {
        dialog.value?.close();
    }
});

watch(() => formData.value?.isGlobal, (newIsGlobal) => {
    if (!newIsGlobal) {
        formData.value.khachHangIds = [];
    }
});

onMounted(() => {
    loadPhieuGiamGia(currentPage.value);
    loadKhachHang();
});
</script>

<template>
    <div class="container">
        <div class="header-content">
            <div class="title-section">
                <h1 class="dashboard-title">Quản lý khuyến mãi</h1>
            </div>
            <div class="header-actions">
                <button @click="createPhieuGiamGia" class="btn btn-outline-primary">
                    <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4">
                        </path>
                    </svg>Tạo khuyến mãi mới
                </button>
            </div>
        </div>

        <!-- Bộ lọc -->
        <div class="search-card">
            <div class="search-content">
                <div class="row g-3">
                    <div class="col-md-4">
                        <input v-model="search" type="text" placeholder="Tìm kiếm theo mã hoặc tên..."
                            class="form-control" />
                    </div>
                    <div class="col-md-2">
                        <select v-model="trangThaiFilter" class="form-select">
                            <option :value="null">-- Tất cả trạng thái --</option>
                            <option value="NOT_STARTED">Chưa bắt đầu</option>
                            <option value="ACTIVE">Đang hoạt động</option>
                            <option value="EXPIRED">Đã hết hạn</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <input v-model="ngayBatDauFilter" type="date" class="form-control" placeholder="Ngày bắt đầu" />
                    </div>
                    <div class="col-md-2">
                        <input v-model="ngayKetThucFilter" type="date" class="form-control"
                            placeholder="Ngày kết thúc" />
                    </div>
                    <div class="col-md-2">
                        <button @click="clear" class="btn btn-outline-secondary" style="margin: 0 auto;">Làm
                            mới</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bảng phiếu giảm giá -->
        <div v-if="isLoading" class="text-center">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Đang tải...</span>
            </div>
        </div>
        <table v-else class="table">
            <thead>
                <tr>
                    <th>STT</th>
                    <th>Mã</th>
                    <th>Tên</th>
                    <th>Giá trị giảm</th>
                    <th>Ngày bắt đầu</th>
                    <th>Ngày kết thúc</th>
                    <th>Số lượng</th>
                    <th>Trạng thái</th>
                    <th>Loại</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <tr v-if="phieuGiamGias.length === 0">
                    <td colspan="10" class="text-center">Không có phiếu giảm giá nào.</td>
                </tr>
                <tr v-else v-for="(phieuGiamGia, index) in phieuGiamGias" :key="index">
                    <template v-if="phieuGiamGia">
                        <td>{{ index + 1 + currentPage * pageSize }}</td>
                        <td>{{ phieuGiamGia.maGiamGia }}</td>
                        <td>{{ phieuGiamGia.tenKhuyenMai }}</td>
                        <td>
                            {{ phieuGiamGia.loaiKhuyenMai === 'Phần trăm'
                                ? phieuGiamGia.giaTriKhuyenMai + '%'
                                : formatCurrency(phieuGiamGia.giaTriKhuyenMai) }}
                        </td>
                        <td>{{ formatDate(phieuGiamGia.ngayBatDau) }}</td>
                        <td>{{ formatDate(phieuGiamGia.ngayKetThuc) }}</td>
                        <td>{{ phieuGiamGia.soLuong }}</td>
                        <td>{{ convertTrangThai(phieuGiamGia.trangThai) }}</td>
                        <td>{{ phieuGiamGia.isGlobal ? 'Công khai' : 'Riêng tư' }}</td>
                        <td>
                            <button class="btn btn-outline-primary btn-sm" title="Chỉnh sửa"
                                @click="viewUpdate(phieuGiamGia.id)">
                                <i class="bi bi-pencil-square"></i>
                            </button>
                            <button class="btn btn-outline-danger btn-sm" style="margin-left: 5%;"
                                @click="handleDeletePhieuGiamGia(phieuGiamGia.id)">
                                <i class="bi bi-trash"></i>
                            </button>
                        </td>
                    </template>
                </tr>
            </tbody>
        </table>


        <!-- Phân trang -->
        <div class="pagination">
            <button @click="previousPage" :disabled="currentPage === 0" class="btn btn-outline-primary">Trang
                trước</button>
            <span style="margin: auto 10px;">Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
            <button @click="nextPage" :disabled="currentPage >= totalPages - 1" class="btn btn-outline-primary">Trang
                sau</button>
        </div>

        <!-- Dialog thêm/sửa -->
        <dialog ref="dialog" class="dialog p-4 rounded shadow-lg" style="width: 800px; margin: auto;">
            <form :model="formData" @submit.prevent="savePhieuGiamGia">
                <h4 class="mb-4">{{ formData?.id ? 'Chỉnh sửa Phiếu Giảm Giá' : 'Thêm Phiếu Giảm Giá' }}
                </h4>
                <div v-if="formData" class="row g-3">
                    <div class="col-md-6">
                        <label for="maGiamGia" class="form-label">Mã giảm giá</label>
                        <input id="maGiamGia" v-model="formData.maGiamGia" type="text" class="form-control" disabled />
                    </div>
                    <div class="col-md-6">
                        <label for="tenKhuyenMai" class="form-label">Tên khuyến mại</label>
                        <input id="tenKhuyenMai" v-model="formData.tenKhuyenMai" type="text" class="form-control" />
                        <div v-if="errors.tenKhuyenMai" class="text-danger mt-1">{{ errors.tenKhuyenMai }}</div>
                    </div>
                    <div class="col-md-6">
                        <label for="loaiKhuyenMai" class="form-label">Loại khuyến mãi</label>
                        <select id="loaiKhuyenMai" v-model="formData.loaiKhuyenMai" class="form-select">
                            <option value="Phần trăm">Phần trăm (%)</option>
                            <option value="Cố định">VNĐ</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="giaTriKhuyenMai" class="form-label">Giá trị khuyến mãi</label>
                        <input id="giaTriKhuyenMai" v-model.number="formData.giaTriKhuyenMai" type="number" class="form-control" min="0"/>
                    </div>
                    <div class="col-md-6">
                        <label for="giaTriDonHangToiThieu" class="form-label">Giá trị đơn hàng tối thiểu</label>
                        <input id="giaTriDonHangToiThieu" v-model.number="formData.giaTriDonHangToiThieu" type="number"
                            class="form-control" min="0" required />
                    </div>
                    <div class="col-md-6">
                        <label for="giaTriKhuyenMaiToiDa" class="form-label">Giá trị khuyến mãi tối đa</label>
                        <input id="giaTriKhuyenMaiToiDa" v-model.number="formData.giaTriKhuyenMaiToiDa" type="number"
                            class="form-control" min="0" required />
                    </div>
                    <div class="col-md-6">
                        <label for="ngayBatDau" class="form-label">Ngày bắt đầu</label>
                        <input id="ngayBatDau" v-model="formData.ngayBatDau" type="date" class="form-control"
                            required />
                    </div>
                    <div class="col-md-6">
                        <label for="ngayKetThuc" class="form-label">Ngày kết thúc</label>
                        <input id="ngayKetThuc" v-model="formData.ngayKetThuc" type="date" class="form-control"
                            required />
                    </div>
                    <div class="col-md-6">
                        <label for="hangToiThieu" class="form-label">Hạng tối thiểu</label>
                        <select id="hangToiThieu" v-model="formData.hangToiThieu" class="form-select">
                            <option value="MEMBER">Thành viên</option>
                            <option value="SILVER">Bạc</option>
                            <option value="GOLD">Vàng</option>
                            <option value="DIAMOND">Kim cương</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="soDiemCanDeDoi" class="form-label">Số điểm cần để đổi</label>
                        <input id="soDiemCanDeDoi" v-model.number="formData.soDiemCanDeDoi" type="number"
                            class="form-control" min="0" />
                    </div>
                    <div class="col-md-6">
                        <label for="soLuong" class="form-label">Số lượng</label>
                        <input id="soLuong" v-model.number="formData.soLuong" type="number" class="form-control"
                            min="0" />
                    </div>
                    <div class="col-md-6">
                        <label for="isGlobal" class="form-label">Loại phiếu</label>
                        <select id="isGlobal" v-model="formData.isGlobal" class="form-select">
                            <option :value="true">Công khai</option>
                            <option :value="false">Riêng tư</option>
                        </select>
                    </div>
                    <div v-if="!formData.isGlobal" class="col-md-12">
                        <label for="khachHangIds" class="form-label">Khách hàng áp dụng <small>(Giữ Ctrl/Cmd để chọn
                                nhiều)</small></label>
                        <select id="khachHangIds" v-model="formData.khachHangIds" multiple class="form-select" size="5">
                            <option v-for="khachHang in khachHangList" :key="khachHang.id" :value="khachHang.id">
                                {{ khachHang.tenKhachHang }}
                            </option>
                        </select>
                    </div>
                    <div class="col-md-12">
                        <label for="dieuKienApDung" class="form-label">Mô tả điều kiện áp dụng</label>
                        <input id="dieuKienApDung" v-model="formData.dieuKienApDung" type="text" class="form-control" />
                    </div>
                </div>
                <div class="mt-4 d-flex justify-content-end gap-2">
                    <button type="button" @click="phieuGiamGiaDialogVisible = false"
                        class="btn btn-secondary">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </dialog>
    </div>
</template>

<style scoped>
* {
    box-sizing: border-box;
}

.container {
    padding: 1.5rem;
    background-color: #f5f7fa;
    min-height: 100vh;
}

.icon {
    width: 1em;
    height: 1em;
    margin-right: 0.5em;
}

.dashboard-header {
    background: #ffffff;
    border-bottom: 1px solid #e0e0e0;
    padding: 1.5rem;
}

.header-content {
    max-width: 1400px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.dashboard-title {
    font-size: 1.75rem;
    font-weight: 600;
    color: #333333;
    margin: 0;
}

.search-card {
    background-color: white;
    border-radius: 0.5rem;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    padding: 1.25rem;
    margin: 1.5rem auto;
}

.table {
    min-height: 350px;
    background-color: #fff;
    border-radius: 0.5rem;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

thead {
    height: 20%;
    min-height: 50px;
}

.pagination {
    display: flex;
    justify-content: space-around;
    align-items: center;
    justify-content: center;
    width: 100%;
    margin: 1.5rem auto;
    min-height: 40px;
    padding: 0.5rem;
    background-color: #fff;
    border-radius: 0.5rem;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.dialog {
    border: none;
    border-radius: 8px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
    background-color: #fff;
}

.form-select[multiple] {
    padding: 8px;
    cursor: pointer;
}

.form-select[multiple] option {
    padding: 8px;
}

.form-select[multiple] option:checked {
    background-color: #007bff;
    color: white;
}
</style>
