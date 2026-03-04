<script setup>
import { ref } from "vue";
import { ArrowRight } from "@element-plus/icons-vue";

/**
 * LƯU Ý KHI THAY ẢNH:
 * 1. Để đẹp nhất, bạn nên tải ảnh về và lưu trong thư mục: public/img/banner/
 * 2. Sau đó đổi đường dẫn image thành: "/img/banner/ten-anh.jpg"
 */

const banners = ref([
  {
    id: 1,
    // Ảnh iPhone 15 Pro Max Titanium
    image: "https://images.unsplash.com/photo-1696446701796-da61225697cc?q=80&w=2070&auto=format&fit=crop", 
    title: "iPhone 15 Pro Max",
    subtitle: "Titan Tự Nhiên - Sẵn Sàng Giao Ngay",
    discount: "Giảm 5 Triệu",
    link: "/detail/1"
  },
  {
    id: 2,
    // Ảnh Samsung S24 Ultra
    image: "https://images.unsplash.com/photo-1706606991536-e35392231b5c?q=80&w=2070&auto=format&fit=crop",
    title: "Galaxy S24 Ultra",
    subtitle: "Quyền Năng Galaxy AI - Tiên Phong Công Nghệ",
    discount: "Tặng ốp lưng",
    link: "/detail/2"
  },
  {
    id: 3,
    // Ảnh Laptop/Macbook
    image: "https://images.unsplash.com/photo-1517336714731-489689fd1ca4?q=80&w=2070&auto=format&fit=crop",
    title: "MacBook Air M3",
    subtitle: "Mỏng Nhẹ - Hiệu Năng Vượt Trội",
    discount: "Ưu đãi HSSV",
    link: "/category/laptop"
  },
]);

const subBanners = ref([
  {
    id: 1,
    // Ảnh phụ kiện
    image: "https://images.unsplash.com/photo-1572569028738-411a56103324?q=80&w=1000&auto=format&fit=crop",
    title: "Phụ Kiện Chính Hãng",
    label: "GIẢM 50%"
  },
  {
    id: 2,
    // Ảnh iPad
    image: "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?q=80&w=1000&auto=format&fit=crop",
    title: "iPad Gen 10",
    label: "MỚI VỀ"
  }
]);
</script>

<template>
  <div class="banner-section">
    <div class="container">
      <div class="banner-grid">
        <div class="main-slider">
          <el-carousel trigger="click" height="380px" :interval="5000" arrow="hover">
            <el-carousel-item v-for="item in banners" :key="item.id">
              <div class="slide-item" :style="{ backgroundImage: `url(${item.image})` }">
                <div class="overlay"></div> 
                
                <div class="content-box">
                  <span class="badge-hot">{{ item.discount }}</span>
                  <h2 class="title">{{ item.title }}</h2>
                  <p class="subtitle">{{ item.subtitle }}</p>
                  <button class="btn-buy">
                    Mua Ngay <el-icon><ArrowRight /></el-icon>
                  </button>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>

        <div class="sub-banners">
          <div 
            v-for="sub in subBanners" 
            :key="sub.id" 
            class="sub-item"
            :style="{ backgroundImage: `url(${sub.image})` }"
          >
            <div class="sub-overlay"></div>
            <div class="sub-content">
              <span class="sub-label">{{ sub.label }}</span>
              <h3 class="sub-title">{{ sub.title }}</h3>
            </div>
          </div>
        </div>
      </div>

      <div class="policy-bar">
        <div class="policy-item">
          <div class="icon-box">🛡️</div>
          <div class="info">
            <strong>Bảo hành chính hãng</strong>
            <span>Lỗi 1 đổi 1 trong 30 ngày</span>
          </div>
        </div>
        <div class="policy-item">
          <div class="icon-box">🚚</div>
          <div class="info">
            <strong>Miễn phí vận chuyển</strong>
            <span>Giao hàng tận nơi toàn quốc</span>
          </div>
        </div>
        <div class="policy-item">
          <div class="icon-box">💳</div>
          <div class="info">
            <strong>Trả góp 0% lãi suất</strong>
            <span>Thủ tục đơn giản, nhanh chóng</span>
          </div>
        </div>
        <div class="policy-item">
          <div class="icon-box">🎁</div>
          <div class="info">
            <strong>Quà tặng hấp dẫn</strong>
            <span>Nhiều ưu đãi khi mua kèm</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.banner-section {
  background-color: #f3f4f6;
  padding-top: 20px;
  padding-bottom: 10px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
}

.banner-grid {
  display: grid;
  grid-template-columns: 2.2fr 1fr;
  gap: 15px;
  margin-bottom: 20px;
}

/* --- MAIN SLIDER --- */
.main-slider {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 20px rgba(0,0,0,0.15);
  background: #2c2c2c; /* Màu nền dự phòng nếu ảnh lỗi */
}

.slide-item {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
  padding-left: 60px;
  background-repeat: no-repeat;
}

/* Overlay chỉnh lại: Trong suốt hơn để thấy ảnh nền */
.overlay {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  /* Gradient từ đen sang trong suốt, giảm độ đậm */
  background: linear-gradient(90deg, rgba(0,0,0,0.8) 0%, rgba(0,0,0,0.3) 50%, transparent 100%);
  z-index: 1;
}

.content-box {
  position: relative;
  z-index: 2;
  max-width: 500px;
  color: #fff;
  animation: slideUp 0.8s ease-out;
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.badge-hot {
  background: #d70018;
  color: #fff;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 700;
  text-transform: uppercase;
  display: inline-block;
  margin-bottom: 15px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.2);
}

.title {
  font-size: 42px;
  font-weight: 800;
  margin: 0 0 10px 0;
  line-height: 1.1;
  text-shadow: 0 2px 15px rgba(0,0,0,0.5); /* Thêm bóng chữ để dễ đọc trên mọi nền */
}

.subtitle {
  font-size: 18px;
  margin: 0 0 30px 0;
  opacity: 0.95; /* Tăng độ rõ */
  font-weight: 500;
  text-shadow: 0 1px 4px rgba(0,0,0,0.6);
}

.btn-buy {
  background: #fff;
  color: #000;
  border: none;
  padding: 12px 30px;
  border-radius: 30px;
  font-weight: 700;
  font-size: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(0,0,0,0.3);
}

.btn-buy:hover {
  background: #d70018;
  color: #fff;
  transform: translateX(5px);
}

/* --- SUB BANNERS --- */
.sub-banners {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.sub-item {
  flex: 1;
  border-radius: 12px;
  overflow: hidden;
  background-size: cover;
  background-position: center;
  position: relative;
  cursor: pointer;
  display: flex;
  align-items: flex-end;
  padding: 20px;
  transition: transform 0.3s ease;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
  background-color: #333; /* Màu nền dự phòng */
}

.sub-item:hover { transform: translateY(-5px); }

.sub-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(0deg, rgba(0,0,0,0.7) 0%, transparent 100%);
  z-index: 1;
}

.sub-content {
  position: relative;
  z-index: 2;
  color: #fff;
}

.sub-label {
  background: #ffd700;
  color: #000;
  font-size: 11px;
  font-weight: 800;
  padding: 3px 8px;
  border-radius: 4px;
  margin-bottom: 6px;
  display: inline-block;
}

.sub-title {
  font-size: 20px;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0,0,0,0.5);
}

/* --- POLICY BAR --- */
.policy-bar {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0,0,0,0.03);
}

.policy-item {
  display: flex;
  align-items: center;
  gap: 15px;
  flex: 1;
  padding: 0 10px;
  border-right: 1px solid #eee;
}

.policy-item:last-child { border-right: none; }

.icon-box {
  width: 45px;
  height: 45px;
  background: #fcebeb;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
}

.info { display: flex; flex-direction: column; }
.info strong { font-size: 14px; color: #333; margin-bottom: 2px; }
.info span { font-size: 12px; color: #777; }

/* --- RESPONSIVE --- */
@media (max-width: 1024px) {
  .banner-grid { grid-template-columns: 1fr; }
  .sub-banners { flex-direction: row; height: 160px; }
}

@media (max-width: 768px) {
  .slide-item { padding-left: 20px; }
  .title { font-size: 28px; }
  .sub-banners { display: none; }
  .policy-bar { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; }
  .policy-item { border: none; padding: 0; }
}
</style>