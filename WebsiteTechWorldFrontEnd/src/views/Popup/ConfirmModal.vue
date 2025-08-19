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
/* Overlay nền mờ */
.modal-overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.6);
    backdrop-filter: blur(2px);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    animation: fadeIn 0.2s ease-out;
}

/* Hộp modal */
.modal-box {
    background: white;
    padding: 24px 28px;
    border-radius: 12px;
    min-width: 380px;
    max-width: 90%;
    text-align: center;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.25);
    animation: scaleIn 0.25s ease-out;
    font-family: 'Segoe UI', sans-serif;
}

/* Nội dung */
.modal-message {
    font-size: 1.05rem;
    font-weight: 500;
    margin-bottom: 20px;
    color: #333;
    line-height: 1.4;
}

/* Khu vực nút */
.modal-actions {
    display: flex;
    justify-content: center;
    gap: 16px;
}

/* Nút cơ bản */
.btn {
    padding: 10px 18px;
    border-radius: 6px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;
    border: none;
    font-size: 0.95rem;
}

/* Nút xác nhận */
.btn-confirm {
    background: linear-gradient(90deg, #28a745, #34d058);
    color: white;
}

.btn-confirm:hover {
    background: linear-gradient(90deg, #23923d, #2fbb4c);
    transform: translateY(-1px);
}

/* Nút hủy */
.btn-cancel {
    background: linear-gradient(90deg, #dc3545, #e55361);
    color: white;
}

.btn-cancel:hover {
    background: linear-gradient(90deg, #b52a37, #c84752);
    transform: translateY(-1px);
}

/* Hiệu ứng mở */
@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

@keyframes scaleIn {
    from {
        transform: scale(0.9);
        opacity: 0;
    }

    to {
        transform: scale(1);
        opacity: 1;
    }
}
</style>
  
