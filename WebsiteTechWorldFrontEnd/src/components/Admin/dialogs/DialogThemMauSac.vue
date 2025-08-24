<template>
  <el-dialog title="Thêm màu sắc" v-model="dialogVisible" width="600px" @close="handleClose" destroy-on-close>

    <el-form :model="NewMauSac" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Tên màu sắc" prop="tenMau">
        <el-input v-model="NewMauSac.tenMau" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Mã màu" prop="maMau">
        <el-color-picker v-model="NewMauSac.maMau" show-alpha />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitMauSac">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postMauSacList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref, watch } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import chroma from 'chroma-js'
import { useToast } from "vue-toastification";
const toast = useToast();

const emit = defineEmits(['saved']);

const NewMauSac = reactive({
  tenMau: '',
  maMau: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

// const rules = {
//   tenMau: [{ required: true, message: 'Vui lòng nhập tên màu sắc', trigger: 'blur' }],
//   maMau: [{ required: true, message: 'Vui lòng nhập mã màu', trigger: 'blur' }],
// };

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewMauSac.tenMau = '';
  NewMauSac.maMau = '';
  dialogVisible.value = false;
}

async function submitMauSac() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {

        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm màu sắc này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );

        const newMauSac = await postMauSacList({
          tenMau: NewMauSac.tenMau,
          maMau: NewMauSac.maMau
        });
        emit('saved', newMauSac);
        dialogVisible.value = false;
        handleClose();
        toast.success('Thêm màu sắc thành công!'); // Thêm thông báo thành công
      } catch (error) {
        if (error === 'cancel' || error === 'close') {
          toast.info('Đã hủy thao tác');
          return;
        }
        console.error('Lỗi khi lưu màu sắc:', error);

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


watch(() => NewMauSac.maMau, (newVal) => {
  if (!newVal) return;
  try {
    const hex = chroma(newVal).hex('rgba');
    NewMauSac.maMau = hex.toLowerCase();
    console.log("11111111111111111111", newVal);
  } catch (err) {
    console.warn("Mã màu không hợp lệ:", newVal);
  }
});
</script>