<script setup>
import { onMounted, reactive, ref } from "vue";
import { detailClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { updateClient } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { useRoute, useRouter } from "vue-router";

import { ElNotification } from 'element-plus'
import { h } from 'vue'

const clientRequest = reactive({
  tenNhanVien: '',
  taiKhoan: '',
  email: '',
  sdt: '',
  diaChiChinh: {},
  trangThai: '',
  chucVu: '',
  gioiTinh: true,
  namSinh: ''
});


const isLoading = ref(false);
const error = ref("");
const route = useRoute();
const router = useRouter();


const id = route.params.id;

const loadClientDetail = async (id) => {
    try {
        const response = await detailClient(id);
        Object.assign(clientRequest, response)
        console.log(clientRequest)
    }
    catch (err) {
        error.value = err.message || "Error while loading client information"
    }
    finally {
        isLoading.value = false;
    }
}


const handldeUpdate = async () => {
    try {
        const id = route.params.id;
        const response = await updateClient(id, clientRequest);

        // Tạo custom notification có thanh progress tụt dần
        ElNotification({
            title: '',
            message: h('div', [
                h('span', 'Cập nhật khách hàng thành công!'),
                h('div', {
                    style: `
                        position: relative;
                        height: 4px;
                        background-color: #e0e0e0;
                        margin-top: 8px;
                        border-radius: 2px;
                        overflow: hidden;
                    `
                }, [
                    h('div', {
                        style: `
                            position: absolute;
                            top: 0;
                            left: 0;
                            height: 100%;
                            background-color: #28a745;
                            width: 100%;
                            animation: progressBar 2s linear forwards;
                        `
                    })
                ])
            ]),
            duration: 2000, // 2 giây
            type: 'success',
            position: 'top-right'
        })
        // router.push("/admin/client")
    }
    catch( err ) {
        error.value = err.message || "Error while loading client information";

        // Hiện thông báo lỗi
        ElNotification({
            title: 'Lỗi',
            message: error.value,
            type: 'error',
            duration: 3000,
            position: 'top-right'
        });
    }
}

onMounted(() => {
  loadClientDetail(id);
});
</script>

<template>
  <div class="client-container">
    <!-- Form thêm khách hàng -->
    <h3>Thêm khách hàng</h3>
    <form @submit.prevent="handleAddClient">
      <div class="row">
        <div class="col-md-6 mb-2">
            Tên Khách Hàng
          <input
            v-model="clientRequest.tenKhachHang"
            placeholder="Tên khách hàng"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Tài khoản
          <input
            v-model="clientRequest.taiKhoan"
            placeholder="Tên đăng nhập"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Email
          <input
            v-model="clientRequest.email"
            placeholder="Email"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Số điện thoại
          <input
            v-model="clientRequest.sdt"
            placeholder="Số điện thoại"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Trạng thái
          <select
            v-model="clientRequest.trangThai"
            placeholder="Trạng thái"
            class="form-select"
          >
            <option value="ACTIVE">Đang làm</option>
            <option value="INACTIVE">Nghỉ</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
            Giới tính
          <select v-model="clientRequest.gioiTinh" class="form-select">
            <option :value="true">Nam</option>
            <option :value="false">Nữ</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
            Năm sinh
          <input
            v-model="clientRequest.ngaySinh"
            placeholder="Năm sinh"
            type="date"
            class="form-control"
          />
        </div>
      </div>
      <button type="submit" @click="handldeUpdate" class="btn btn-success mt-2">Update thông tin</button>
    </form>
  </div>
</template>

<style scoped>
.client-container {
  margin-left: 10px;
  padding: 20px;
  width: 99%;
  box-sizing: border-box;
}

</style>
