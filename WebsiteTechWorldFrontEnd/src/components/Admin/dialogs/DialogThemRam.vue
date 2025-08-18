<template>
  <el-dialog title="Thêm dung lượng ram mới" v-model="dialogVisible" width="600px" @close="handleClose"
    destroy-on-close>
    <el-form :model="NewDungLuong" ref="formRef" label-position="top" >
      <el-form-item label="Dung lượng" prop="dungLuong">
        <el-input v-model="NewDungLuong.dungLuong" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewDungLuong" ref="formRef" label-position="top" >
      <el-form-item label="Loại ram" prop="loai">
        <el-input v-model="NewDungLuong.loai" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitRam">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postRamList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useToast } from "vue-toastification";
const toast = useToast();

const emit = defineEmits(['saved']);

const NewDungLuong = reactive({
  dungLuong: '',
  loai: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewDungLuong.dungLuong = '';
  NewDungLuong.loai = '';
  dialogVisible.value = false;
}

async function submitRam() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const savedDungLuong = await postRamList({
          dungLuong: NewDungLuong.dungLuong,
          loai: NewDungLuong.loai,
        });
        emit('saved', savedDungLuong);
        dialogVisible.value = false;
        handleClose();
        ElMestoastsage.success('Thêm dung lượng thành công!');
      } catch (error) {
        console.error("Lỗi khi lưu dung lượng:", error);

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
          toast.error("Có lỗi xảy ra khi thêm dung lượng!");
        }
      }
    }
  });
}
</script>