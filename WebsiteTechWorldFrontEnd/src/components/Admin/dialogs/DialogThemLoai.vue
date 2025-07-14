<template>
  <el-dialog
    title="Thêm loại mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="newTenLoai" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Tên loại" prop="tenLoai">
        <el-input v-model="newTenLoai.tenLoai" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitLoai">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postLoai } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const newTenLoai = reactive({
  tenLoai: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  tenLoai: [{ required: true, message: 'Vui lòng nhập tên loại', trigger: 'blur' }],
};

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  newTenLoai.tenLoai = '';
  dialogVisible.value = false;
}

async function submitLoai() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const savedLoai = await postLoai({
          tenLoai: newTenLoai.tenLoai,
        });
        emit('saved', savedLoai);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm loại thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu loại:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm loại!');
      }
    }
  });
}
</script>