<template>
  <el-dialog title="Thêm Cpu mới" v-model="dialogVisible" width="600px" @close="handleClose" destroy-on-close>
    <el-form :model="NewCpu" ref="formRef" label-position="top">
      <el-form-item label="Chíp xử lý" prop="chipXuLy">
        <el-input v-model="NewCpu.chipXuLy" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitCpu">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postCpuList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();

const emit = defineEmits(['saved']);

const NewCpu = reactive({
  chipXuLy: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);


function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewCpu.chipXuLy = '';
  dialogVisible.value = false;
}

async function submitCpu() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {

        await ElMessageBox.confirm(
          'Bạn có chắc chắn muốn thêm chíp xử lý này?',
          'Xác nhận',
          {
            confirmButtonText: 'Đồng ý',
            cancelButtonText: 'Huỷ',
            type: 'warning',
          }
        );

        const savedCpu = await postCpuList({
          chipXuLy: NewCpu.chipXuLy,
        });
        emit('saved', savedCpu);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm chíp xử lý thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error("Lỗi khi lưu chíp xử lý:", error);

        if (error && error === 'cancel') {
          toast.info('Đã hủy thao tác');
          return;
        }

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
          toast.error("Có lỗi xảy ra khi thêm chíp xử lý!");
        }
      }

    }
  });
}
</script>