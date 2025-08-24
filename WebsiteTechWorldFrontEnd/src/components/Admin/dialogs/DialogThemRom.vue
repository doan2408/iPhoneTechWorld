<template>
  <el-dialog title="Thêm dung lượng rom mới" v-model="dialogVisible" width="600px" @close="handleClose"
    destroy-on-close>
    <el-form :model="NewDungLuong" ref="formRef" label-position="top" >
      <el-form-item label="Dung lượng" prop="dungLuong">
        <el-input v-model="NewDungLuong.dungLuong" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitRom">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postRomList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();

const emit = defineEmits(['saved']);

const NewDungLuong = reactive({
  dungLuong: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

// const rules = {
//   dungLuong: [{ required: true, message: 'Vui lòng nhập dung lượng rom', trigger: 'blur' }],
// };

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewDungLuong.dungLuong = '';
  dialogVisible.value = false;
}

async function submitRom() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {

        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm dung lượng rom này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );

        const savedDungLuong = await postRomList({
          dungLuong: NewDungLuong.dungLuong,
        });
        emit('saved', savedDungLuong);
        dialogVisible.value = false;
        handleClose();
        toast.success('Thêm dung lượng rom thành công!'); // Thêm thông báo thành công
      } catch (error) {
        
        if (error === 'cancel' || error === 'close') {
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