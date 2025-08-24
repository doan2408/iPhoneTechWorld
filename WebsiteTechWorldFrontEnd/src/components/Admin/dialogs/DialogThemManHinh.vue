<template>
  <el-dialog
    title="Thêm kích thước màn hình mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >

  
    <el-form :model="NewManHinh" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Tên màn hình" prop="tenManHinh">
        <el-input v-model="NewManHinh.tenManHinh" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewManHinh" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Kích thước màn hình" prop="kichThuoc">
        <el-input v-model="NewManHinh.kichThuoc" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewManHinh" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Loại màn hình" prop="loaiManHinh">
        <el-input v-model="NewManHinh.loaiManHinh" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitMH">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postManHinhList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();

const emit = defineEmits(['saved']);

const NewManHinh = reactive({
  kichThuoc: '',
  loaiManHinh: '',
  tenManHinh: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

// const rules = {
//   tenManHinh: [{ required: true, message: 'Vui lòng nhập tên màn hình', trigger: 'blur' }],
//   kichThuoc: [{ required: true, message: 'Vui lòng nhập kích thước màn hình', trigger: 'blur' }],
//   loaiManHinh: [{ required: true, message: 'Vui lòng nhập loại màn hình', trigger: 'blur' }],
// };

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewManHinh.kichThuoc = '';
  NewManHinh.loaiManHinh = '';
  NewManHinh.tenManHinh = '';
  dialogVisible.value = false;
}



async function submitMH() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {

        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm Màn hình này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );

        const savedManHinh = await postManHinhList({
          kichThuoc: NewManHinh.kichThuoc,
          loaiManHinh: NewManHinh.loaiManHinh,
          tenManHinh: NewManHinh.tenManHinh,
        });

        emit('saved', savedManHinh);
        dialogVisible.value = false;
        handleClose();
        toast.success('Thêm màn hình thành công!');
      } catch (error) {
        console.error('Lỗi khi lưu màn hình:', error);

        if (error && error === 'cancel') {
          toast.info('Đã hủy thao tác');
          return;
        }

        if (error.response && error.response.data) {
          const { message, error: serverError, errors: fieldErrors } = error.response.data;

          if (fieldErrors) {
            const msgs = Object.values(fieldErrors).flat().join('\n');
            toast.error(msgs);
          } else if (message && typeof message === 'object') {
            const msgs = Object.values(message).join('\n');
            toast.error(msgs);
          } else if (message) {
            toast.error(message);
          } else if (serverError) {
            toast.error(typeof serverError === 'string' ? serverError : JSON.stringify(serverError));
          } else {
            toast.error('Dữ liệu không hợp lệ, vui lòng kiểm tra lại!');
          }
        } else if (error.response) {
          const status = error.response.status;
          if (status === 401) toast.error('Bạn không có quyền thực hiện hành động này!');
          else if (status === 403) toast.error('Bạn không có quyền truy cập tài nguyên này!');
          else toast.error('Đã xảy ra lỗi, vui lòng thử lại!');
        } else {
          toast.error('Không thể kết nối đến server!');
        }
      }
    }
  });
}

</script>