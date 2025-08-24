<template>
  <el-dialog title="Thêm Camera trước mới" v-model="dialogVisible" width="600px" @close="handleClose" destroy-on-close>
    <el-form :model="NewCameraTruoc" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Loại camera" prop="loaiCamera">
        <el-input v-model="NewCameraTruoc.loaiCamera" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraTruoc" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Độ phân giải" prop="doPhanGiai">
        <el-input v-model="NewCameraTruoc.doPhanGiai" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraTruoc" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Khẩu độ" prop="khauDo">
        <el-input v-model="NewCameraTruoc.khauDo" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitCameraTruoc">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postCameraTruocList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();

const emit = defineEmits(['saved']);

const NewCameraTruoc = reactive({
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
  NewCameraTruoc.loaiCamera = '';
  NewCameraTruoc.doPhanGiai = '';
  NewCameraTruoc.khauDo = '';
  dialogVisible.value = false;
}

async function submitCameraTruoc() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {

        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm camera trước này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );

        const savedCameraTruoc = await postCameraTruocList({
          loaiCamera: NewCameraTruoc.loaiCamera,
          doPhanGiai: NewCameraTruoc.doPhanGiai,
          khauDo: NewCameraTruoc.khauDo,
        });
        emit('saved', savedCameraTruoc);
        dialogVisible.value = false;
        handleClose();
        toast.success('Thêm camera trước thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu camera trước:', error);

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