<template>
    <div class="track-order-container">
        <div class="track-order-card">
            <h1 class="card-title">Theo Dõi Đơn Hàng Của Bạn</h1>
            <p class="card-description">
                Nhập mã vận đơn của bạn vào ô bên dưới để xem trạng thái cập nhật mới nhất của đơn hàng.
            </p>
            <form @submit.prevent="handleSubmit" class="tracking-form">
                <div class="form-group">
                    <label for="tracking-number" class="form-label">Mã Vận Đơn</label>
                    <input id="tracking-number" type="text" v-model="trackingNumber" placeholder="Ví dụ: VD001"
                        class="form-input" required />
                </div>
                <button type="submit" class="submit-button">
                    <svg class="search-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                        xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                    </svg>
                    Theo Dõi Ngay
                </button>
            </form>
            <p class="card-footer">
                Nếu bạn gặp bất kỳ vấn đề nào hoặc không tìm thấy đơn hàng, vui lòng liên hệ bộ phận hỗ trợ của chúng
                tôi để được giúp đỡ.
            </p>
        </div>
    </div>
</template>

<script setup>
import router from '@/router'
import { ref } from 'vue'
import { findIdHoaDonByMVD } from '@/Service/ClientService/HoaDon/MyOrderClient'

const trackingNumber = ref('')

const handleSubmit = async () => {
    console.log('Mã vận đơn đã nhập:', trackingNumber.value)
    const res = await findIdHoaDonByMVD(trackingNumber.value);
    const data = res.data
    devliveryProcessing(data);

}

const devliveryProcessing = async (data) => {
  router.push({
      name: "orderTracking",
    params: { id: data[0] },
  });
};
</script>
    
<style scoped>
/* Container chính */
.track-order-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background: linear-gradient(135deg, #f0f2f5 0%, #e0e5ec 100%);
    /* Nền gradient nhẹ nhàng */
    padding: 20px;
    box-sizing: border-box;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background-image: url('https://png.pngtree.com/thumb_back/fh260/background/20190221/ourmid/pngtree-move-the-company-goods-transport-image_12000.jpg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    min-height: 100vh;
}

/* Thẻ chứa nội dung */
.track-order-card {
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    /* Đổ bóng mềm mại */
    padding: 40px;
    max-width: 500px;
    width: 100%;
    text-align: center;
    box-sizing: border-box;
    transition: transform 0.3s ease-in-out;
}

.track-order-card:hover {
    transform: translateY(-5px);
    /* Hiệu ứng nhấc nhẹ khi di chuột qua */
}

/* Tiêu đề */
.card-title {
    font-size: 2.5em;
    font-weight: 700;
    color: #333;
    margin-bottom: 15px;
    letter-spacing: -0.5px;
}

/* Mô tả */
.card-description {
    font-size: 1.1em;
    color: #666;
    margin-bottom: 30px;
    line-height: 1.6;
}

/* Form */
.tracking-form {
    display: flex;
    flex-direction: column;
    gap: 25px;
}

.form-group {
    text-align: left;
}

/* Nhãn input */
.form-label {
    display: block;
    font-size: 1.1em;
    font-weight: 600;
    color: #444;
    margin-bottom: 8px;
}

/* Input */
.form-input {
    width: 100%;
    padding: 15px 20px;
    border: 2px solid #ddd;
    border-radius: 8px;
    font-size: 1.1em;
    color: #333;
    outline: none;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
    box-sizing: border-box;
}

.form-input::placeholder {
    color: #aaa;
}

.form-input:focus {
    border-color: #6a82fb;
    /* Màu tím-xanh đẹp cho focus */
    box-shadow: 0 0 0 4px rgba(106, 130, 251, 0.2);
    /* Đổ bóng nhẹ khi focus */
}

/* Nút submit */
.submit-button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    padding: 15px 25px;
    background: linear-gradient(45deg, #6a82fb 0%, #fc5c7d 100%);
    /* Gradient từ tím-xanh sang hồng */
    color: #fff;
    border: none;
    border-radius: 8px;
    font-size: 1.2em;
    font-weight: 700;
    cursor: pointer;
    transition: all 0.3s ease;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.submit-button:hover {
    background: linear-gradient(45deg, #5c70e0 0%, #e04c6c 100%);
    /* Gradient hơi tối hơn khi hover */
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
    transform: translateY(-2px);
    /* Hiệu ứng nhấc nhẹ khi hover */
}

.submit-button:active {
    transform: translateY(0);
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
}

/* Icon tìm kiếm */
.search-icon {
    width: 22px;
    height: 22px;
    margin-right: 10px;
}

/* Chân thẻ */
.card-footer {
    font-size: 0.95em;
    color: #888;
    margin-top: 30px;
    line-height: 1.5;
}

/* Điều chỉnh responsive */
@media (max-width: 600px) {
    .track-order-card {
        padding: 30px 20px;
        margin: 10px;
    }

    .card-title {
        font-size: 2em;
    }

    .card-description {
        font-size: 1em;
    }

    .form-label,
    .form-input,
    .submit-button {
        font-size: 1em;
    }
}
</style>