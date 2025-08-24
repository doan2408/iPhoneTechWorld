<template>
    <div v-if="visible" class="screen-lock-overlay">
        <div class="message">
            🔒 Màn hình đã bị khóa
            <button @click="unlock" class="unlock-btn">Mở khóa</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, defineProps, defineExpose } from 'vue'

const props = defineProps({
    autoUnlockMinutes: Number // ví dụ: 2 sẽ tự mở sau 2 phút
})

const visible = ref(false)
let timer = null

function lock() {
    visible.value = true
    document.body.style.overflow = 'hidden'

    if (props.autoUnlockMinutes) {
        timer = setTimeout(() => {
            unlock()
        }, props.autoUnlockMinutes * 60 * 1000)
    }
}

function unlock() {
    visible.value = false
    document.body.style.overflow = ''
    if (timer) clearTimeout(timer)
}

defineExpose({ lock, unlock })

onUnmounted(() => {
    if (timer) clearTimeout(timer)
})

onMounted(() => {
    window.addEventListener('keydown', handleKey)
})


function handleKey(e) {
    if (e.key === 'Escape') {
        unlock()
    }
    if (e.ctrlKey && e.key.toLowerCase() === 'q') {
        lock()
    }
}
</script>

<style scoped>
.screen-lock-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(255, 255, 255, 0.4);
    z-index: 999999;
    pointer-events: all;
    cursor: not-allowed;
    display: flex;
    align-items: center;
    justify-content: center;
}

.message {
    font-size: 1.2rem;
    color: #333;
    text-align: center;
}

.unlock-btn {
    margin-top: 1rem;
    padding: 0.5rem 1rem;
    font-size: 1rem;
    cursor: pointer;
}
.unlock-btn {
    background-color: var(--btn-color, #007bff);
    /* mặc định xanh dương */
    color: white;
    border: none;
    border-radius: 8px;
    padding: 0.6rem 1.2rem;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
}

.unlock-btn:hover {
    background-color: #0056b3;
    transform: scale(1.05);
}

.unlock-btn:active {
    transform: scale(0.98);
    box-shadow: none;
}
</style>
