<template>
    <div class="modal-overlay" @click.self="emit('cancel')">
        <div class="modal-box">
            <p class="modal-message">{{ message }}</p>
            <div class="modal-actions">
                <button @click="emit('confirm')" class="btn btn-confirm">Xác nhận</button>
                <button @click="emit('cancel')" class="btn btn-cancel">Hủy</button>
            </div>
        </div>
    </div>
</template>

<script setup>
defineProps({ message: String })
const emit = defineEmits(['confirm', 'cancel'])
</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-box {
    background: white;
    padding: 20px;
    border-radius: 8px;
    min-width: 300px;
    text-align: center;
}

.modal-actions {
    margin-top: 15px;
    display: flex;
    justify-content: center;
    gap: 15px;
}

.btn {
    padding: 6px 12px;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
}

.btn-confirm {
    background-color: #28a745;
    color: white;
}

.btn-cancel {
    background-color: #dc3545;
    color: white;
}
</style>
  
