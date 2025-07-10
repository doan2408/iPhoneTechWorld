<template>
  <el-dialog
    title="Thêm phiên bản pin mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
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
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const NewPin = reactive({
  phienBan: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  phienBan: [{ required: true, message: 'Vui lòng nhập tên phiên bản pin', trigger: 'blur' }],
};

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
        const newPin = await postPinList({
          phienBan: NewPin.phienBan,
        });
        emit('saved', newPin);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm phiên bản pin thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu phiên bản pin:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm phiên bản pin!');
      }
    }
  });
}
</script>