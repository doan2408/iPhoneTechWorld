<template>
  <el-dialog
    title="Thêm nhà cung cấp mới"
    v-model="dialogVisible"
    width="600px"
    @close="handleClose"
    destroy-on-close
  >
    <el-form :model="newNhaCungCap" ref="formRef" label-position="top" :rules="rules">
      <el-form-item
        label="Tên nhà cung cấp"
        prop="tenNhaCungCap"
      >
        <el-input v-model="newNhaCungCap.tenNhaCungCap" autocomplete="off" />
      </el-form-item>

    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">Hủy</el-button>
      <el-button type="primary" @click="submitNhaCungCap">Lưu</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { postNhaCungCapList } from '@/Service/Adminservice/Products/ProductAdminService'
import { reactive, ref, defineExpose, defineEmits } from 'vue'

const emit = defineEmits(['saved'])

const newNhaCungCap = reactive({
  tenNhaCungCap: ''
})

const dialogVisible = ref(false)
const formRef = ref(null)

const rules = {
  tenNhaCungCap: [
    { required: true, message: 'Vui lòng nhập tên nhà cung cấp', trigger: 'blur' }
  ]
}

function open() {
  dialogVisible.value = true
}

defineExpose({ open })

function handleClose() {
  formRef.value?.resetFields()
  newNhaCungCap.tenNhaCungCap = ''
  dialogVisible.value = false
}

async function submitNhaCungCap() {
  formRef.value?.validate(async valid => {
    if (valid) {
      try {
        // Gửi data lên server
        const savedNCC = await postNhaCungCapList({
          tenNhaCungCap: newNhaCungCap.tenNhaCungCap
        })

        // Phát event trả về cho component cha
        emit('saved', savedNCC)

        // Đóng dialog và reset form
        dialogVisible.value = false
        handleClose()
      } catch (error) {
        console.error('Lỗi khi lưu nhà cung cấp:', error)
        // Bạn có thể hiện thông báo lỗi ở đây
      }
    }
  })
}
</script>


