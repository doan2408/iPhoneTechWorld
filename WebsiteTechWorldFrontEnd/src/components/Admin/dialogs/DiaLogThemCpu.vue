<template>
  <el-dialog
    title="Thêm Cpu mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="NewCpu" ref="formRef" label-position="top" :rules="rules">
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
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const NewCpu = reactive({
  chipXuLy: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  tenLoai: [{ required: true, message: 'Vui lòng nhập chíp xử lý', trigger: 'blur' }],
};

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
        const savedCpu = await postCpuList({
          chipXuLy: NewCpu.chipXuLy,
        });
        emit('saved', savedCpu);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm chíp xử lý thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu chíp xử lý:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm chíp xử lý!');
      }
    }
  });
}
</script>