<script setup>
import { onMounted, reactive, ref } from "vue";
import { detailStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { updateStaff } from "@/Service/Adminservice/TaiKhoan/NhanVienServices";
import { useRoute, useRouter } from "vue-router";

import { ElNotification } from 'element-plus'
import { h } from 'vue'

const staffRequest = reactive({
  tenNhanVien: '',
  taiKhoan: '',
  email: '',
  sdt: '',
  diaChi: '',
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

const loadStaffDetail = async (id) => {
    try {
        const response = await detailStaff(id);
        Object.assign(staffRequest, response)
    }
    catch (err) {
        error.value = err.message || "Error while loading staff information"
    }
    finally {
        isLoading.value = false;
    }
}


const handldeUpdate = async () => {
    try {
        const id = route.params.id;
        const response = await updateStaff(id, staffRequest);
        
        // Tạo custom notification có thanh progress tụt dần
        ElNotification({
            title: '',
            message: h('div', [
                h('span', 'Cập nhật nhân viên thành công!'),
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
        // router.push("/admin/staff")
    }
    catch( err ) {
        error.value = err.message || "Error while loading staff information";

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
  loadStaffDetail(id);
});
</script>

<template>
  <div class="staff-container">
    <!-- Form thêm nhân viên -->
    <h3>Thêm nhân viên</h3>
    <form @submit.prevent="handleAddStaff">
      <div class="row">
        <div class="col-md-6 mb-2">
            Tên Nhân Viên
          <input
            v-model="staffRequest.tenNhanVien"
            placeholder="Tên nhân viên"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Tài khoản
          <input
            v-model="staffRequest.taiKhoan"
            placeholder="Tên đăng nhập"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Email
          <input
            v-model="staffRequest.email"
            placeholder="Email"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Số điện thoại
          <input
            v-model="staffRequest.sdt"
            placeholder="Số điện thoại"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Địa chỉ
          <input
            v-model="staffRequest.diaChi"
            placeholder="Địa chỉ"
            class="form-control"
          />
        </div>
        <div class="col-md-6 mb-2">
            Trạng thái
          <select
            v-model="staffRequest.trangThai"
            placeholder="Trạng thái"
            class="form-select"
          >
            <option value="ENABLE">Đang làm</option>
            <option value="DISABLE">Nghỉ</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
            Chức vụ
          <select
            v-model="staffRequest.chucVu"
            placeholder="Chức vụ"
            class="form-select"
          >
            <option value="STAFF">Nhân Viên</option>
            <option value="ADMIN">Quản lý</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
            Giới tính
          <select v-model="staffRequest.gioiTinh" class="form-select">
            <option :value="true">Nam</option>
            <option :value="false">Nữ</option>
          </select>
        </div>
        <div class="col-md-6 mb-2">
            Năm sinh
          <input
            v-model="staffRequest.namSinh"
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
.staff-container {
  margin-left: 10px;
  padding: 20px;
  width: 99%;
  box-sizing: border-box;
}

</style>
