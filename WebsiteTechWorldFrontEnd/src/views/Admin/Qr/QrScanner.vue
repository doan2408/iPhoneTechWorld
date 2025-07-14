<template>
    <div id="reader" style="width: 100%; height: 300px;"></div>
</template>

<script setup>
import { onMounted, onBeforeUnmount } from 'vue'
import { Html5Qrcode } from 'html5-qrcode'

const emit = defineEmits(['scanned'])

let html5QrCode = null


// Tắt camera an toàn
const stopCamera = async () => {
    if (html5QrCode) {
        try {
            await html5QrCode.stop()
            await html5QrCode.clear()
            console.log('Camera đã tắt')
        } catch (err) {
            console.error('Lỗi khi tắt camera:', err)
        }
    }
}


onMounted(async () => {
    const config = { fps: 10, qrbox: 250 }

    html5QrCode = new Html5Qrcode('reader')

    try {
        await html5QrCode.start(
            { facingMode: 'environment' },
            config,
            async (decodedText) => {
                emit('scanned', decodedText)
                await stopCamera()
            },
            (err) => {
                // Lỗi khi đang quét (tạm thời)
                console.warn("Scan error", err)
            }
        )
    } catch (err) {
        console.error('Không thể khởi động camera:', err)
    }
})

onBeforeUnmount(() => {
    if (html5QrCode) {
        html5QrCode.stop()
            .then(() => html5QrCode.clear())
            .catch(err => console.error("Stop error", err))
    }
    stopCamera()
})
</script>
