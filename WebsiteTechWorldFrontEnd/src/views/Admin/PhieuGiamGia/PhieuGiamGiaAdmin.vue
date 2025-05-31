<script setup>
import { ref, onMounted, watch } from "vue";
import { getAll, detail, add, update, deletePhieuGiamGia, getAllKhachHang } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";

const phieuGiamGias = ref([]);
const isLoading = ref(false);
const phieuGiamGiaDialogVisible = ref(false);
const dialog = ref(null);
const selectedPhieuGiamGia = ref(null);
const khachHangList = ref([]);

const currentPage = ref(0);
const totalPages = ref(0);
const search = ref("");
const trangThaiFilter = ref(null);
const ngayBatDauFilter = ref(null);
const ngayKetThucFilter = ref(null);

const loadPhieuGiamGia = async (page) => {
    try {
        isLoading.value = true;
        const response = await getAll({
            page: page,
            size: 3,
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
        console.log("Danh sách khách hàng:", khachHangList.value); 
        if (!khachHangList.value.length) {
            alert("Không có khách hàng nào trong hệ thống!");
        }
    } catch (err) {
        console.error(err.message || "Lỗi lấy danh sách khách hàng");
        alert(err.message || "Lỗi lấy danh sách khách hàng");
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

const createPhieuGiamGia = () => {
    selectedPhieuGiamGia.value = {
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
        isGlobal: false,
        khachHangIds: [],
    };
    phieuGiamGiaDialogVisible.value = true;
};

const savePhieuGiamGia = async () => {
    try {
        isLoading.value = true;
        const data = { ...selectedPhieuGiamGia.value };
        console.log("Dữ liệu gửi đi:", data); 
        if (data.isGlobal && data.khachHangIds.length === 0) {
            throw new Error("Vui lòng chọn ít nhất một khách hàng cho phiếu giảm giá riêng tư");
        }
        if (new Date(data.ngayBatDau) > new Date(data.ngayKetThuc)) {
            throw new Error("Ngày bắt đầu phải trước ngày kết thúc");
        }
        if (data.id) {
            await update(data.id, data);
            alert("Cập nhật phiếu giảm giá thành công!");
        } else {
            await add(data);
            alert("Thêm phiếu giảm giá thành công!");
        }
        phieuGiamGiaDialogVisible.value = false;
        dialog.value?.close();
        loadPhieuGiamGia(currentPage.value);
    } catch (error) {
        console.error(error.message || "Lỗi khi lưu phiếu giảm giá");
        alert(error.message || "Lỗi khi lưu phiếu giảm giá");
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
        selectedPhieuGiamGia.value = { ...response, khachHangIds: response.khachHangIds || [] };
        console.log("Chi tiết phiếu:", selectedPhieuGiamGia.value); 
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

watch(() => selectedPhieuGiamGia.value?.isGlobal, (newIsGlobal) => {
    if (!newIsGlobal) {
        selectedPhieuGiamGia.value.khachHangIds = [];
    }
});

onMounted(() => {
    loadPhieuGiamGia(currentPage.value);
    loadKhachHang();
});
</script>

<template>
    <div class="container mt-4">
        <!-- Bộ lọc -->
        <div class="row g-3 mb-3">
            <div class="col-md-4">
                <input v-model="search" type="text" placeholder="Tìm kiếm mã hoặc tên..." class="form-control" />
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
                <input v-model="ngayKetThucFilter" type="date" class="form-control" placeholder="Ngày kết thúc" />
            </div>
            <div class="col-md-2 d-flex gap-2">
                <button @click="clear" class="btn btn-outline-secondary">Làm mới</button>
                <button @click="createPhieuGiamGia" class="btn btn-primary">Tạo phiếu</button>
            </div>
        </div>

        <!-- Bảng phiếu giảm giá -->
        <div v-if="isLoading" class="text-center">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Đang tải...</span>
            </div>
        </div>
        <table v-else class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
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
                <tr v-for="phieuGiamGia in phieuGiamGias" :key="phieuGiamGia.id">
                    <td>{{ phieuGiamGia.maGiamGia }}</td>
                    <td>{{ phieuGiamGia.tenKhuyenMai }}</td>
                    <td>{{ phieuGiamGia.loaiKhuyenMai === 'Phần trăm' ? phieuGiamGia.giaTriKhuyenMai + '%' : formatCurrency(phieuGiamGia.giaTriKhuyenMai) }}</td>
                    <td>{{ formatDate(phieuGiamGia.ngayBatDau) }}</td>
                    <td>{{ formatDate(phieuGiamGia.ngayKetThuc) }}</td>
                    <td>{{ phieuGiamGia.soLuong }}</td>
                    <td>{{ convertTrangThai(phieuGiamGia.trangThai) }}</td>
                    <td>{{ phieuGiamGia.isGlobal ? 'Riêng tư' : 'Công khai' }}</td>
                    <td>
                        <button class="btn btn-sm btn-primary me-2" @click="viewUpdate(phieuGiamGia.id)">Cập nhật</button>
                        <button class="btn btn-sm btn-danger" @click="handleDeletePhieuGiamGia(phieuGiamGia.id)">Xóa</button>
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- Phân trang -->
        <div class="d-flex justify-content-between align-items-center" style="width: 25%;">
            <button @click="previousPage" :disabled="currentPage === 0" class="btn btn-outline-primary">Trang trước</button>
            <span>Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
            <button @click="nextPage" :disabled="currentPage >= totalPages - 1" class="btn btn-outline-primary">Trang sau</button>
        </div>

        <!-- Dialog thêm/sửa -->
        <dialog ref="dialog" class="dialog p-4 rounded shadow-lg" style="width: 800px; margin: auto;">
            <form @submit.prevent="savePhieuGiamGia">
                <h4 class="mb-4">{{ selectedPhieuGiamGia?.id ? 'Chỉnh sửa Phiếu Giảm Giá' : 'Thêm Phiếu Giảm Giá' }}</h4>
                <div v-if="selectedPhieuGiamGia" class="row g-3">
                    <div class="col-md-6">
                        <label for="maGiamGia" class="form-label">Mã giảm giá</label>
                        <input id="maGiamGia" v-model="selectedPhieuGiamGia.maGiamGia" type="text" class="form-control" disabled />
                    </div>
                    <div class="col-md-6">
                        <label for="tenKhuyenMai" class="form-label">Tên khuyến mại</label>
                        <input id="tenKhuyenMai" v-model="selectedPhieuGiamGia.tenKhuyenMai" type="text" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label for="loaiKhuyenMai" class="form-label">Loại khuyến mãi</label>
                        <select id="loaiKhuyenMai" v-model="selectedPhieuGiamGia.loaiKhuyenMai" class="form-select" required>
                            <option value="Phần trăm">Phần trăm (%)</option>
                            <option value="Cố định">VNĐ</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="giaTriKhuyenMai" class="form-label">Giá trị khuyến mãi</label>
                        <input id="giaTriKhuyenMai" v-model.number="selectedPhieuGiamGia.giaTriKhuyenMai" type="number" class="form-control" min="0" required />
                    </div>
                    <div class="col-md-6">
                        <label for="giaTriDonHangToiThieu" class="form-label">Giá trị đơn hàng tối thiểu</label>
                        <input id="giaTriDonHangToiThieu" v-model.number="selectedPhieuGiamGia.giaTriDonHangToiThieu" type="number" class="form-control" min="0" required />
                    </div>
                    <div class="col-md-6">
                        <label for="giaTriKhuyenMaiToiDa" class="form-label">Giá trị khuyến mãi tối đa</label>
                        <input id="giaTriKhuyenMaiToiDa" v-model.number="selectedPhieuGiamGia.giaTriKhuyenMaiToiDa" type="number" class="form-control" min="0" required />
                    </div>
                    <div class="col-md-6">
                        <label for="ngayBatDau" class="form-label">Ngày bắt đầu</label>
                        <input id="ngayBatDau" v-model="selectedPhieuGiamGia.ngayBatDau" type="date" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label for="ngayKetThuc" class="form-label">Ngày kết thúc</label>
                        <input id="ngayKetThuc" v-model="selectedPhieuGiamGia.ngayKetThuc" type="date" class="form-control" required />
                    </div>
                    <div class="col-md-6">
                        <label for="hangToiThieu" class="form-label">Hạng tối thiểu</label>
                        <select id="hangToiThieu" v-model="selectedPhieuGiamGia.hangToiThieu" class="form-select" required>
                            <option value="MEMBER">Thành viên</option>
                            <option value="SILVER">Bạc</option>
                            <option value="GOLD">Vàng</option>
                            <option value="DIAMOND">Kim cương</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="soDiemCanDeDoi" class="form-label">Số điểm cần để đổi</label>
                        <input id="soDiemCanDeDoi" v-model.number="selectedPhieuGiamGia.soDiemCanDeDoi" type="number" class="form-control" min="0" required />
                    </div>
                    <div class="col-md-6">
                        <label for="soLuong" class="form-label">Số lượng</label>
                        <input id="soLuong" v-model.number="selectedPhieuGiamGia.soLuong" type="number" class="form-control" min="0" required />
                    </div>
                    <div class="col-md-6">
                        <label for="isGlobal" class="form-label">Loại phiếu</label>
                        <select id="isGlobal" v-model="selectedPhieuGiamGia.isGlobal" class="form-select" required>
                            <option :value="false">Công khai</option>
                            <option :value="true">Riêng tư</option>
                        </select>
                    </div>
                    <div v-if="selectedPhieuGiamGia.isGlobal" class="col-md-12">
                        <label for="khachHangIds" class="form-label">Khách hàng áp dụng <small>(Giữ Ctrl/Cmd để chọn nhiều)</small></label>
                        <select id="khachHangIds" v-model="selectedPhieuGiamGia.khachHangIds" multiple class="form-select" size="5">
                            <option v-for="khachHang in khachHangList" :key="khachHang.id" :value="khachHang.id">
                                {{ khachHang.tenKhachHang }}
                            </option>
                        </select>
                    </div>
                    <div class="col-md-12">
                        <label for="dieuKienApDung" class="form-label">Mô tả điều kiện áp dụng</label>
                        <input id="dieuKienApDung" v-model="selectedPhieuGiamGia.dieuKienApDung" type="text" class="form-control" />
                    </div>
                </div>
                <div class="mt-4 d-flex justify-content-end gap-2">
                    <button type="button" @click="phieuGiamGiaDialogVisible = false" class="btn btn-secondary">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </dialog>
    </div>
</template>

<style scoped>
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