<template>
  <el-dialog title="Thêm Camera sau mới" v-model="dialogVisible" width="600px" @close="handleClose" destroy-on-close>
    <el-form :model="NewCameraSau" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Loại camera" prop="loaiCamera">
        <el-input v-model="NewCameraSau.loaiCamera" placeholder="Nhập loại camera, ví dụ: Wide, Ultra Wide, Telephoto" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraSau" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Độ phân giải" prop="doPhanGiai">
        <el-input v-model="NewCameraSau.doPhanGiai" placeholder="Nhập độ phân giải, ví dụ: 12MP"  autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraSau" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Khẩu độ" placeholder="Nhập khẩu độ, ví dụ: f/1.8"  prop="khauDo">
        <el-input v-model="NewCameraSau.khauDo" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitCameraSau">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postCameraSauList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();

const emit = defineEmits(['saved']);

const NewCameraSau = reactive({
  loaiCamera: '',
  doPhanGiai: '',
  khauDo: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

// const rules = {
//   loaiCamera : [{ required: true, message: 'Vui lòng nhập loại camera', trigger: 'blur' }],
//   doPhanGiai : [{ required: true, message: 'Vui lòng nhập độ phân giải', trigger: 'blur' }],
//   khauDo : [{ required: true, message: 'Vui lòng nhập khẩu độ', trigger: 'blur' }],
// };

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewCameraSau.loaiCamera = '';
  NewCameraSau.doPhanGiai = '';
  NewCameraSau.khauDo = '';
  dialogVisible.value = false;
}

async function submitCameraSau() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {


        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm camera sau này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );

        const savedCameraSau = await postCameraSauList({
          loaiCamera: NewCameraSau.loaiCamera,
          doPhanGiai: NewCameraSau.doPhanGiai,
          khauDo: NewCameraSau.khauDo,
        });
        emit('saved', savedCameraSau);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm camera sau thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu camera sau:', error);

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