<template>
  <el-dialog
    title="Thêm Camera sau mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="NewCameraSau" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Loại camera" prop="loaiCamera">
        <el-input v-model="NewCameraSau.loaiCamera" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraSau" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Độ phân giải" prop="doPhanGiai">
        <el-input v-model="NewCameraSau.doPhanGiai" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraSau" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Khẩu độ" prop="khauDo">
        <el-input v-model="NewCameraSau.khauDo" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitCameraSau">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postCameraSauList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const NewCameraSau = reactive({
  loaiCamera: '',
  doPhanGiai: '',
  khauDo: '',
});

const dialogVisible = ref(false);
const formRef = ref(null);

const rules = {
  loaiCamera : [{ required: true, message: 'Vui lòng nhập loại camera', trigger: 'blur' }],
  doPhanGiai : [{ required: true, message: 'Vui lòng nhập độ phân giải', trigger: 'blur' }],
  khauDo : [{ required: true, message: 'Vui lòng nhập khẩu độ', trigger: 'blur' }],
};

function open() {
  dialogVisible.value = true;
}

defineExpose({ open });

function handleClose() {
  formRef.value?.resetFields();
  NewCameraSau.loaiCamera = '';
  NewCameraSau.doPhanGiai = '';
  NewCameraSau.khauDo = '';
  dialogVisible.value = false;
}

async function submitCameraSau() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const savedCameraSau = await postCameraSauList({
          loaiCamera: NewCameraSau.loaiCamera,
          doPhanGiai: NewCameraSau.doPhanGiai,
          khauDo: NewCameraSau.khauDo,
        });
        emit('saved', savedCameraSau);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm camera sau thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu camera sau:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm camera sau!');
      }
    }
  });
}
</script>