<template>
  <el-dialog
    title="Thêm màu sắc"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="NewMauSac" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Tên màu sắc" prop="tenMau">
        <el-input v-model="NewMauSac.tenMau" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewMauSac" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Mã màu" prop="maMau">
        <el-input v-model="NewMauSac.maMau" autocomplete="off" />
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
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const NewMauSac = reactive({
  tenMau: '',
  maMau: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  tenMau: [{ required: true, message: 'Vui lòng nhập tên màu sắc', trigger: 'blur' }],
  maMau: [{ required: true, message: 'Vui lòng nhập mã màu', trigger: 'blur' }],
};

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
        const newMauSac = await postMauSacList({
          tenMau: NewMauSac.tenMau,
          maMau: NewMauSac.maMau
        });
        emit('saved', newMauSac);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm màu sắc thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu màu sắc:', error);
        ElMessage.error('Có lỗi xảy ra khi màu sắc!');
      }
    }
  });
}
</script>