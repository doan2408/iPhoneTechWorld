<script setup>
import { ref, onMounted, watch } from "vue";
import { getAll, detail, add, update } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";

const phieuGiamGias = ref([]);
const isLoading = ref(false);
const error = ref("");

const phieuGiamGiaDialogVisible = ref(false);
const dialog = ref(null);
const selectedPhieuGiamGia = ref(null);

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
        error.value = err.message || "Lỗi lấy phiếu giảm giá";
    } finally {
        isLoading.value = false;
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
        hangToiThieu: 0,
        soDiemCanDeDoi: 0,
        soLuong: 0,
        dieuKienApDung: "",
        trangThai: "ACTIVE"
    };
    phieuGiamGiaDialogVisible.value = true;
};

const savePhieuGiamGia = async () => {
    try {
        isLoading.value = true;
        const data = { ...selectedPhieuGiamGia.value };
        if (data.id) {
            await update(data.id, data);
            alert("Cập nhật thành công!");
        } else {
            await add(data);
            alert("Thêm mới thành công!");
        }

        phieuGiamGiaDialogVisible.value = false;
        dialog.value?.close();
        loadPhieuGiamGia(currentPage.value);
    } catch (error) {
        console.error("Lỗi khi lưu phiếu giảm giá:", error);
        alert("Đã xảy ra lỗi khi lưu phiếu giảm giá.");
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
        selectedPhieuGiamGia.value = response;
        phieuGiamGiaDialogVisible.value = true;
    } catch (error) {
        console.error('Error fetching details:', error);
        alert('Không thể tải chi tiết phiếu giảm giá');
    }
};

const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return `${date.getDate().toString().padStart(2, '0')}/${
    (date.getMonth() + 1).toString().padStart(2, '0')
  }/${date.getFullYear()}`;
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

onMounted(() => {
    loadPhieuGiamGia(currentPage.value);
});
</script>


<template>
    <div class="container">
        <div class="row g-3 align-items-center">
            <div class="col-auto">
                <input v-model="search" type="text" placeholder="Tìm kiếm mã giảm giá..." class="form-control" />
            </div>
            <div class="col-auto" style="display: flex; justify-content: space-between; width: 40%;">
                <button @click="clear" class="btn btn-primary btn-sm">Làm mới</button>
                <button @click="createPhieuGiamGia" class="btn btn-primary btn-sm">Tạo phiếu</button>
            </div>
        </div>

        <div class="row g-3 align-items-center">
            <div class="col-auto" style="width: 30%;">
                <select v-model="trangThaiFilter" class="form-select">
                    <option :value="null">-- Tất cả trạng thái --</option>
                    <option value="NOT_STARTED">Chưa bắt đầu</option>
                    <option value="ACTIVE">Đang hoạt động</option>
                    <option value="EXPIRED">Đã hết hạn</option>
                </select>
            </div>
            <div class="col-auto" style="width: 30%;">
                <input v-model="ngayBatDauFilter" type="date" class="form-control" placeholder="Ngày bắt đầu từ" />
            </div>
            <div class="col-auto" style="width: 30%;">
                <input v-model="ngayKetThucFilter" type="date" class="form-control" placeholder="Ngày kết thúc đến" />
            </div>
        </div>

        <div v-if="isLoading" class="text-center">
            <p>Đang tải dữ liệu...</p>
        </div>

        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã</th>
                    <th>Giá trị</th>
                    <th>Điều kiện</th>
                    <th>Số lượng</th>
                    <th>Thời gian</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="phieuGiamGia in phieuGiamGias" :key="phieuGiamGia.id">
                    <td>{{ phieuGiamGia.id }}</td>
                    <td>{{ phieuGiamGia.maGiamGia }}</td>
                    <td>{{ phieuGiamGia.loaiKhuyenMai === 'Phần trăm' ? phieuGiamGia.giaTriKhuyenMai + '%' : formatCurrency(phieuGiamGia.giaTriKhuyenMai) }}</td>
                    <td>{{ phieuGiamGia.dieuKienApDung }}</td>
                    <td>{{ phieuGiamGia.soLuong }}</td>
                    <td>{{ formatDate(phieuGiamGia.ngayBatDau) }} - {{ formatDate(phieuGiamGia.ngayKetThuc) }}</td>
                    <td>{{ convertTrangThai(phieuGiamGia.trangThai) }}</td>
                    <td>
                        <button class="btn btn-primary btn-sm" @click="viewUpdate(phieuGiamGia.id)">
                            Cập nhật
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="pagination">
            <button @click="previousPage" :disabled="currentPage === 0" class="btn btn-primary btn-sm">Trang
                trước</button>
            <span>Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
            <button @click="nextPage" :disabled="currentPage >= totalPages - 1" class="btn btn-primary btn-sm">Trang
                sau</button>
        </div>

        <div v-if="phieuGiamGiaDialogVisible" class="dialog-container">
            <form @submit.prevent="savePhieuGiamGia" class="dialog-content p-4">
                <h4 class="mb-3">
                    {{ selectedPhieuGiamGia?.id ? 'Chỉnh sửa Phiếu Giảm Giá' : 'Thêm Phiếu Giảm Giá' }}
                </h4>


                <div v-if="selectedPhieuGiamGia" class="row g-3 align-items-center" style="width: 700px;">
                    <div class="col-auto">
                        <label for="maGiamGia" class="form-label">Mã giảm giá</label>
                        <input id="maGiamGia" v-model="selectedPhieuGiamGia.maGiamGia" type="text" class="form-control"
                            disabled />
                    </div>

                    <div class="col-auto">
                        <label for="tenKhuyenMai" class="form-label">Tên khuyến mại</label>
                        <input id="tenKhuyenMai" v-model="selectedPhieuGiamGia.tenKhuyenMai" type="text"
                            class="form-control" required />
                    </div>

                    <div class="col-auto">
                        <label for="loaiKhuyenMai" class="form-label">Loại khuyến mãi</label>
                        <select id="loaiKhuyenMai" v-model="selectedPhieuGiamGia.loaiKhuyenMai" class="form-select"
                            required>
                            <option value="Phần trăm">Phần trăm (%)</option>
                            <option value="Cố định">VNĐ</option>
                        </select>
                    </div>

                    <div class="col-auto">
                        <label for="giaTriKhuyenMai" class="form-label">Giá trị khuyến mãi</label>
                        <input id="giaTriKhuyenMai" v-model.number="selectedPhieuGiamGia.giaTriKhuyenMai" type="number"
                            class="form-control" min="0" required />
                    </div>

                    <div class="col-auto">
                        <label for="giaTriDonHangToiThieu" class="form-label">Giá trị đơn hàng tối thiểu</label>
                        <input id="giaTriDonHangToiThieu" v-model.number="selectedPhieuGiamGia.giaTriDonHangToiThieu"
                            type="number" class="form-control" min="0" required />
                    </div>

                    <div class="col-auto">
                        <label for="giaTriKhuyenMaiToiDa" class="form-label">Giá trị đơn hàng tối đa</label>
                        <input id="giaTriKhuyenMaiToiDa" v-model.number="selectedPhieuGiamGia.giaTriKhuyenMaiToiDa"
                            type="number" class="form-control" min="0" required />
                    </div>

                    <div class="col-auto">
                        <label for="ngayBatDau" class="form-label">Ngày bắt đầu</label>
                        <input id="ngayBatDau" v-model="selectedPhieuGiamGia.ngayBatDau" type="date"
                            class="form-control" required />
                    </div>

                    <div class="col-auto">
                        <label for="ngayKetThuc" class="form-label">Ngày kết thúc</label>
                        <input id="ngayKetThuc" v-model="selectedPhieuGiamGia.ngayKetThuc" type="date"
                            class="form-control" required />
                    </div>

                    <div class="col-auto" style="width: 33.33%;">
                        <label for="hangToiThieu" class="form-label">Hạng tối thiểu</label>
                        <input id="hangToiThieu" v-model.number="selectedPhieuGiamGia.hangToiThieu" type="number"
                            class="form-control" min="0" required />
                    </div>

                    <div class="col-auto" style="width: 33.33%;">
                        <label for="soDiemCanDeDoi" class="form-label">Số điểm cần để đổi</label>
                        <input id="soDiemCanDeDoi" v-model.number="selectedPhieuGiamGia.soDiemCanDeDoi" type="number"
                            class="form-control" min="0" required />
                    </div>

                    <div class="col-auto" style="width: 33.33%;">
                        <label for="soLuong" class="form-label">Số lượng</label>
                        <input id="soLuong" v-model.number="selectedPhieuGiamGia.soLuong" type="number"
                            class="form-control" min="0" required />
                    </div>

                    <div class="mb-3">
                        <label for="dieuKienApDung" class="form-label">Mô tả điều kiện áp dụng</label>
                        <input id="dieuKienApDung" v-model="selectedPhieuGiamGia.dieuKienApDung" type="text"
                            class="form-control" />
                    </div>
                </div>

                <div class="dialog-actions d-flex justify-content-end gap-2">
                    <button type="button" @click="phieuGiamGiaDialogVisible = false" class="btn btn-secondary">
                        Hủy
                    </button>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</template>

<style scoped>
.container {
    margin-left: 215px;
}

.row {
    margin: 20px auto;
}

span {
    margin: auto 10px;
}

ul {
    list-style: none;
    padding: 0;
}

li {
    padding: 1rem;
    border-bottom: 1px solid #ddd;
    font-size: 1.1rem;
}

.col-auto {
    width: 50%;
}

.dialog-container {
    border: none;
    border-radius: 8px;
    box-shadow: 0 8px 20px rgb(0 0 0 / 0.2);
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    margin: 0;
    padding: 0;
    background-color: #cb8181;
}


.error {
    color: red;
    font-size: 1rem;
}
</style>
