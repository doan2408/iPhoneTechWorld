<template>
  <el-dialog
    title="Thêm dung lượng mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="NewDungLuong" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Dung lượng" prop="dungLuong">
        <el-input v-model="NewDungLuong.dungLuong" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitLoai">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postRamList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const NewDungLuong = reactive({
  dungLuong: '',
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
  NewDungLuong.dungLuong = '';
  dialogVisible.value = false;
}

async function submitLoai() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const savedDungLuong = await postRamList({
          dungLuong: NewDungLuong.dungLuong,
        });
        emit('saved', savedDungLuong);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm dung lượng thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu dung lượng:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm dung lượng!');
      }
    }
  });
}
</script>