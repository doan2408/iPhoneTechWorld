<template>
  <el-dialog
    title="Thêm kích thước màn hình mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="NewManHinh" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Tên kích thước màn hình" prop="kichThuoc">
        <el-input v-model="NewManHinh.kichThuoc" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitMH">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postManHinhList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const NewManHinh = reactive({
  kichThuoc: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  kichThuoc: [{ required: true, message: 'Vui lòng nhập tên kích thước màn hình', trigger: 'blur' }],
};

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewManHinh.kichThuoc = '';
  dialogVisible.value = false;
}

async function submitMH() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const savedManHinh = await postManHinhList({
          kichThuoc: NewManHinh.kichThuoc,
        });
        emit('saved', savedManHinh);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm kích thước màn hình thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu kích thước màn hình:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm kích thước màn hình!');
      }
    }
  });
}
</script>