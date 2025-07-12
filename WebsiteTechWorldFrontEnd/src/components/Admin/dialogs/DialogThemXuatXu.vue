<template>
  <el-dialog
    title="Thêm mã xuất xứ mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
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
import { ElMessage } from 'element-plus';

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
        const savedMaXuatXu = await postXuatXuList({
          maXuatXu: newXuatXu.maXuatXu,
        });
        emit('saved', savedMaXuatXu);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm mã xuất xứ thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu mã xuất xứ:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm mã xuất xứ!');
      }
    }
  });
}
</script>