<template>
    <h3>Hóa đơn</h3>
    <button type="button" class="btn btn-success">Bán hàng</button>

    <!-- dropdow ( xuat excel va print ) -->
    <div class="btn-group">
        <button type="button" class="btn btn-danger">Tùy chọn</button>
        <button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown"
            aria-expanded="false">
            <span class="visually-hidden">Toggle Dropdown</span>
        </button>
        <ul class="dropdown-menu">
            <button class="dropdown-item">Print</button>
            <button class="dropdown-item">Xuất Excel</button>
        </ul>
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
            </tr>
        </thead>
        <tbody class="table-group-divider">
            <tr v-for="(hoaDon,index) in hoaDons" :key="hoaDon.id">
                <th scope="row">{{ index + 1 + pageNo * pageSize }}</th>
                <td>{{ hoaDon.maHoaDon }}</td>
                <td>{{ hoaDon.ngayTao }}</td>
                <td>{{ hoaDon.tenKhachHang }}</td>
                <td>{{ hoaDon.tongTien }}</td>
                <td>{{ hoaDon.loaiHoaDon }}</td>
                <td>{{ hoaDon.trangThaiThanhToan }}</td>
            </tr>
        </tbody>
    </table>
    <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
        <div class="btn-group me-2" role="group" aria-label="First group">
            <button type="button" class="btn btn-secondary" :disabled="pageNo === 0" @click="changePage(pageNo - 1)">
                Pre
            </button>
            <button type="button" class="btn btn-primary" v-for="(pageIndex) in totalPagesArray" :key="pageIndex"
                @click="changePage(pageIndex)">
                {{ pageIndex + 1 }}
            </button>
            <button type="button" class="btn btn-secondary" :disabled="pageNo === totalPage - 1 "
                @click="changePage(pageNo+1)">
                Next
            </button>
            <h5> &nbsp; &nbsp;Page {{ pageNo + 1 }} / {{ totalPage }}</h5>
        </div>
    </div>

</template>
<script setup>
import { computed, onMounted, ref } from 'vue';
import { hoaDonGetAll } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';

const hoaDons = ref([]);
const pageNo = ref(0);
const pageSize = ref(5);
const totalPage = ref(0)

const totalPagesArray = computed(() =>
    Array.from({length: totalPage.value}, (_,i) => i),
)

const loadData =async () => {
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