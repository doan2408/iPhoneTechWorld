<template>
    <h3>Hóa đơn</h3>


    <div class="d-flex" style="gap: 20px;">
        <!-- Cột tìm kiếm -->
        <div style="flex: 0 0 27%; max-width: 27%;">
            <div class="mb-3">
                <label for="formGroupExampleInput" class="form-label">Tìm kiếm</label>
                <input type="text" class="form-control mb-3" id="formGroupExampleInput" placeholder="Theo mã hóa đơn">
                <input type="text" class="form-control mb-3" id="formGroupExampleInput"
                    placeholder="Theo tên khách hàng">
                <input type="text" class="form-control mb-3" id="formGroupExampleInput" placeholder="Theo Imei">
                <input type="text" class="form-control mb-3" id="formGroupExampleInput" placeholder="Theo mã vận đơn">

            </div>
            <div class="mb-3">
                <label for="exampleFormControlTextarea1" class="form-label">Địa chỉ</label>
                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"
                    placeholder="Theo địa chỉ"></textarea>
            </div>
            <div class="mb-3">
                <label for="formGroupExampleInput" class="form-label">Theo thời gian</label>
                <input type="text" class="form-control mb-3" id="formGroupExampleInput" placeholder="Toàn thời gian">
                <input type="text" class="form-control mb-3" id="formGroupExampleInput" placeholder="Lựa chọn khác">
            </div>
            <div>
                <select class="form-select mb-3" aria-label="Default select example">
                    <option selected>Trạng thái</option>
                    <option value="1">Mẫu 1</option>
                    <option value="2">Mẫu 2</option>
                    <option value="3">Mẫu 3</option>
                </select>
            </div>
        </div>
        <div style="flex: 0 0 73%; max-width: 73%;">
            <!-- biến phần tử thành flex container, tạo khoảng trắng giữa các nút  -->
            <div class="d-flex justify-content-end mb-3 gap-2">
                <button type="button" class="btn btn-success">Bán hàng</button>

                <!-- dropdow ( xuat excel va print ) -->
                <div class="btn-group">
                    <button type="button" class="btn btn-danger">Tùy chọn</button>
                    <button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split"
                        data-bs-toggle="dropdown" aria-expanded="false">
                        <span class="visually-hidden">Toggle Dropdown</span>
                    </button>
                    <ul class="dropdown-menu">
                        <button class="dropdown-item">Print</button>
                        <button class="dropdown-item">Xuất Excel</button>
                    </ul>
                </div>
            </div>

            <table class="table table-hover table-info">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Mã hóa đơn</th>
                        <th scope="col">Ngày tạo</th>
                        <th scope="col">Khách hàng</th>
                        <th scope="col">Tổng tiền hàng</th>
                        <th scope="col">Loại hóa đơn</th>
                        <td scope="col">Trạng thái hóa đơn</td>
                        <td scope="col">Hành động</td>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    <tr v-for="(hoaDon, index) in hoaDons" :key="hoaDon.id">
                        <th scope="row">{{ index + 1 + pageNo * pageSize }}</th>
                        <td>{{ hoaDon.maHoaDon }}</td>
                        <td>{{ hoaDon.ngayTao }}</td>
                        <td>{{ hoaDon.tenKhachHang }}</td>
                        <td>{{ hoaDon.tongTien }}</td>
                        <td>{{ hoaDon.loaiHoaDon }}</td>
                        <td>{{ hoaDon.trangThaiThanhToan }}</td>
                        <td>
                            <button type="button" class="btn btn-primary">Detail</button>
                            <button type="button" class="btn btn-danger">Delete</button>

                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="d-flex justify-content-center mt-3">
                <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                    <div class="btn-group me-2" role="group" aria-label="First group">
                        <button type="button" class="btn btn-secondary" :disabled="pageNo === 0"
                            @click="changePage(pageNo - 1)">
                            Pre
                        </button>
                        <button type="button" class="btn btn-primary" v-for="(pageIndex) in totalPagesArray"
                            :key="pageIndex" @click="changePage(pageIndex)">
                            {{ pageIndex + 1 }}
                        </button>
                        <button type="button" class="btn btn-secondary" :disabled="pageNo === totalPage - 1"
                            @click="changePage(pageNo + 1)">
                            Next
                        </button>
                        <h5> &nbsp; &nbsp;Page {{ pageNo + 1 }} / {{ totalPage }}</h5>
                    </div>
                </div>@
            </div>
        </div>
    </div>

</template>
<script setup>
import { computed, onMounted, ref } from 'vue';
import { hoaDonGetAll } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';

const hoaDons = ref([]);
const pageNo = ref(0);
const pageSize = ref(10);
const totalPage = ref(0)

const totalPagesArray = computed(() =>
    Array.from({ length: totalPage.value }, (_, i) => i),
)

const loadData = async () => {
    const ref = await hoaDonGetAll(pageNo.value, pageSize.value)
    hoaDons.value = ref.data.content
    totalPage.value = ref.data.totalPages
}

const changePage = (newPage) => {
    pageNo.value = newPage
    loadData()
}

onMounted(() => loadData())
</script>