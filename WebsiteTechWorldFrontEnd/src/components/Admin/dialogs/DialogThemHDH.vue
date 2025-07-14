<template>
  <el-dialog
    title="Thêm phiên bản hệ điều hành mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="newHDH" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Tên phiên bản" prop="phienBan">
        <el-input v-model="newHDH.phienBan" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitHDH">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postHDHList} from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const newHDH = reactive({
  phienBan: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  phienBan: [{ required: true, message: 'Vui lòng nhập tên phiên bản', trigger: 'blur' }],
};

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  newHDH.phienBan = '';
  dialogVisible.value = false;
}

async function submitHDH() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const savedHDH = await postHDHList({
          phienBan: newHDH.phienBan,
        });
        emit('saved', savedHDH);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm phiên bản thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu phiên bản:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm phiên bản!');
      }
    }
  });
}
</script>