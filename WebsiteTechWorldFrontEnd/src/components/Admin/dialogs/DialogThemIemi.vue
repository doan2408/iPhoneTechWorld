<!-- DialogThemImei.vue -->
<template>
  <el-dialog v-model="visible" title="Nhập danh sách IMEI" width="500px">
    <el-input type="textarea" v-model="imeiInput" placeholder="Nhập IMEI cách nhau bằng dấu phẩy (,)" :rows="5" />

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="closeDialog">Hủy</el-button>
        <el-button type="primary" @click="confirm">Xác nhận</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, defineEmits, defineProps } from 'vue';

const props = defineProps({
  modelValue: Boolean
});

const emit = defineEmits(['update:modelValue', 'confirm']);

const visible = ref(false);
const imeiInput = ref('');

// Đồng bộ v-model từ ngoài vào dialog
watch(
  () => props.modelValue,
  (val) => {
    visible.value = val;
    if (val) imeiInput.value = ''; // reset khi mở
  }
);

// Đóng dialog
const closeDialog = () => {
  emit('update:modelValue', false);
};

// Xác nhận nhập IMEI
import { ElMessage } from 'element-plus';

const confirm = () => {
  const imeis = imeiInput.value
    .split(',')
    .map(i => i.trim())
    .filter(i => i !== '')
    .map(soImei => ({ soImei }));

  if (imeis.length === 0) {
    ElMessage.warning('Vui lòng nhập ít nhất 1 IMEI!');
    return;
  }

  emit('confirm', imeis, props.row);
  emit('update:modelValue', false);
};

</script>
