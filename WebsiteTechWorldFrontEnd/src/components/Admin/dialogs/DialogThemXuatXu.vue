<template>
  <el-dialog title="Thêm mã xuất xứ mới" v-model="dialogVisible" width="600px" @close="handleClose" destroy-on-close>
    <el-form :model="newXuatXu" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Mã xuất xứ" prop="maXuatXu">
        <el-input v-model="newXuatXu.maXuatXu" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitXuatXu">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postXuatXuList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();


const emit = defineEmits(['saved']);

const newXuatXu = reactive({
  maXuatXu: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  tenLoai: [{ required: true, message: 'Vui lòng nhập mã quốc gia', trigger: 'blur' }],
};

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  newXuatXu.maXuatXu = '';
  dialogVisible.value = false;
}

async function submitXuatXu() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {

        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm mã xuất xứ này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );

        const savedMaXuatXu = await postXuatXuList({
          maXuatXu: newXuatXu.maXuatXu,
        });
        emit('saved', savedMaXuatXu);
        dialogVisible.value = false;
        handleClose();
        toast.success('Thêm mã xuất xứ thành công!'); // Thêm thông báo thành công
      } catch (error) {
        
        if (error && error === 'cancel') {
          toast.info('Đã hủy thao tác');
          return;
        }
        console.error("Lỗi khi lưu mã xuất xứ:", error);

        if (error.response) {
          const { status, data } = error.response;

          if (status === 400) {
            let errorMessage = "Dữ liệu không hợp lệ!";

            if (typeof data.message === "string") {
              // Trường hợp API trả về message là string
              errorMessage = data.message;
            } else if (typeof data.message === "object") {
              // Trường hợp API trả về message là object
              errorMessage = Object.values(data.message).join("\n");
            }

            toast.error(errorMessage);
          } else if (status === 401) {
            toast.error("Không có quyền truy cập. Vui lòng đăng nhập lại!");
          } else if (status === 500) {
            toast.error("Lỗi máy chủ. Vui lòng thử lại sau!");
          } else {
            toast.error(`Lỗi không xác định: ${data.message || "Có lỗi xảy ra!"}`);
          }
        } else if (error.request) {
          toast.error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng!");
        } else {
          toast.error("Có lỗi xảy ra khi thêm mã xuất xứ!");
        }
      }
    }
  });
}
</script>