<template>
  <el-dialog title="Thêm phiên bản pin mới" v-model="dialogVisible" width="600px" @close="handleClose" destroy-on-close>
    <el-form :model="NewPin" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Tên phiên bản pin" prop="phienBan">
        <el-input v-model="NewPin.phienBan" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitPin">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postPinList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();


const emit = defineEmits(['saved']);

const NewPin = reactive({
  phienBan: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

// const rules = {
//   phienBan: [{ required: true, message: 'Vui lòng nhập tên phiên bản pin', trigger: 'blur' }],
// };

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewPin.phienBan = '';
  dialogVisible.value = false;
}

async function submitPin() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {

        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm phiên bản pin này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );
        const newPin = await postPinList({
          phienBan: NewPin.phienBan,
        });
        emit('saved', newPin);
        dialogVisible.value = false;
        handleClose();
        toast.success('Thêm phiên bản pin thành công!'); // Thêm thông báo thành công
      } catch (error) {

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