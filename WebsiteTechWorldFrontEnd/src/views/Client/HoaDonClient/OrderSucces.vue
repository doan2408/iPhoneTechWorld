<template>
    <div class="order-success-container">
        <div class="card">
            <div class="card-header">
                <CheckCircleIcon class="icon-success" />
                <h1 class="card-title">Đặt hàng thành công!</h1>
                <p class="card-description">
                    Cảm ơn bạn đã mua sắm với chúng tôi. Đơn hàng của bạn đã được xác nhận và sẽ được xử lý sớm.
                </p>
            </div>
            <div class="card-content">
                <p class="info-text">
                    Bạn sẽ nhận được email xác nhận với chi tiết đơn hàng của mình.
                </p>
                <button  class="button primary-button" @click="viewMyOrder">
                    Xem đơn hàng của bạn
                </button>
                <a href="/" class="button secondary-button">
                    Tiếp tục mua sắm
                </a>
            </div>
        </div>
    </div>
</template>

<script setup>
import router from '@/router';
import { CheckCircleIcon } from 'lucide-vue-next';
const viewMyOrder = () => {
    router.push({ name: 'myorder' });
}
</script>

<style scoped>
.order-success-container {
    display: flex;
    min-height: calc(100vh - 60px);
    /* Adjust based on your header/footer height */
    align-items: center;
    justify-content: center;
    padding: 20px;
    background-color: #f3f4f6;
    /* Light gray background */
}

.card {
    width: 100%;
    max-width: 480px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    text-align: center;
    overflow: hidden;
}

.card-header {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 16px;
    padding: 32px;
    border-bottom: 1px solid #e5e7eb;
}

.icon-success {
    height: 64px;
    width: 64px;
    color: #22c55e;
    /* Green color for success */
}

.card-title {
    font-size: 2.25rem;
    /* 36px */
    font-weight: 700;
    /* Bold */
    margin: 0;
    color: #1f2937;
    /* Dark gray text */
}

.card-description {
    font-size: 1rem;
    /* 16px */
    color: #6b7280;
    /* Medium gray text */
    margin: 0;
}

.card-content {
    display: grid;
    gap: 16px;
    padding: 32px;
}

.info-text {
    font-size: 0.875rem;
    /* 14px */
    color: #4b5563;
    /* Slightly darker gray text */
    margin: 0;
}

.button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 12px 24px;
    border-radius: 6px;
    font-size: 1rem;
    font-weight: 600;
    text-decoration: none;
    cursor: pointer;
    transition: background-color 0.2s ease-in-out, border-color 0.2s ease-in-out, color 0.2s ease-in-out;
    width: 100%;
    /* Full width */
    box-sizing: border-box;
    /* Include padding and border in the element's total width and height */
}

.primary-button {
    background-color: #1f2937;
    /* Dark button background */
    color: #ffffff;
    border: 1px solid #1f2937;
}

.primary-button:hover {
    background-color: #374151;
    /* Slightly lighter on hover */
    border-color: #374151;
}

.secondary-button {
    background-color: transparent;
    color: #1f2937;
    border: 1px solid #d1d5db;
    /* Light gray border */
}

.secondary-button:hover {
    background-color: #e5e7eb;
    /* Light gray background on hover */
    border-color: #e5e7eb;
}

/* Responsive adjustments */
@media (max-width: 600px) {
    .card-title {
        font-size: 1.75rem;
        /* 28px */
    }

    .card-header,
    .card-content {
        padding: 24px;
    }
}
</style>