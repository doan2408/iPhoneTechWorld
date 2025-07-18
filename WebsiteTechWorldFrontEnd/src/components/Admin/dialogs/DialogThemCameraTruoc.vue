<template>
  <el-dialog
    title="Thêm Camera trước mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="NewCameraTruoc" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Loại camera" prop="loaiCamera">
        <el-input v-model="NewCameraTruoc.loaiCamera" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraTruoc" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Độ phân giải" prop="doPhanGiai">
        <el-input v-model="NewCameraTruoc.doPhanGiai" autocomplete="off" />
      </el-form-item>
    </el-form>

    <el-form :model="NewCameraTruoc" ref="formRef" label-position="top" :rules="rules">
      <el-form-item label="Khẩu độ" prop="khauDo">
        <el-input v-model="NewCameraTruoc.khauDo" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitCameraTruoc">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postCameraTruocList } from '@/Service/Adminservice/Products/ProductAdminService';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['saved']);

const NewCameraTruoc = reactive({
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
  NewCameraTruoc.loaiCamera = '';
  NewCameraTruoc.doPhanGiai = '';
  NewCameraTruoc.khauDo = '';
  dialogVisible.value = false;
}

async function submitCameraTruoc() {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const savedCameraTruoc = await postCameraTruocList({
          loaiCamera: NewCameraTruoc.loaiCamera,
          doPhanGiai: NewCameraTruoc.doPhanGiai,
          khauDo: NewCameraTruoc.khauDo,
        });
        emit('saved', savedCameraTruoc);
        dialogVisible.value = false;
        handleClose();
        ElMessage.success('Thêm camera trước thành công!'); // Thêm thông báo thành công
      } catch (error) {
        console.error('Lỗi khi lưu camera trước:', error);
        ElMessage.error('Có lỗi xảy ra khi thêm camera trước!');
      }
    }
  });
}
</script>