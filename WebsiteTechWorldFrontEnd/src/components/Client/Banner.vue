<script setup>
import { ChevronLeft, ChevronRight } from "lucide-vue-next";
import { ref, onMounted, onUnmounted } from "vue";
import img1 from "@/components/images/iphone-15-promax-denTitan.webp";
import img2 from "@/components/images/iphone-15-promax-TitanTuNhien.webp";
import img3 from "@/components/images/iphone15-plus-hong.webp";

const banners = ref([
  {
    title: "iPhone 15 Pro Max",
    subtitle: "Giảm giá lên đến 30% - Trả góp 0%",
    buttonText: "Mua ngay",
    image: img1,
  },
  {
    title: "iPhone 16 Pro Max",
    subtitle: "Ưu đãi độc quyền - Quà tặng hấp dẫn",
    buttonText: "Khám phá",
    image: img2,
  },
  {
    title: "iPhone 15 Plus",
    subtitle: "Giá sốc chỉ từ 15.990.000đ",
    buttonText: "Xem chi tiết",
    image: img3,
  },
]);


const currentSlide = ref(0);
let slideInterval = null;

const goToSlide = (index) => {
  currentSlide.value = index;
};

const prevSlide = () => {
  currentSlide.value =
    (currentSlide.value - 1 + banners.value.length) % banners.value.length;
};

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % banners.value.length;
};

onMounted(() => {
  slideInterval = setInterval(nextSlide, 4000);
});

onUnmounted(() => {
  if (slideInterval) {
    clearInterval(slideInterval);
  }
});
</script>

<template>
  <section class="banner-slider">
    <div class="slider-container">
      <div
        class="slide"
        v-for="(banner, index) in banners"
        :key="index"
        :class="{ active: currentSlide === index }"
      >
        <div class="banner-content">
          <div class="banner-text">
            <h2>🔥 {{ banner.title }}</h2>
            <p><i class="fa fa-bolt"></i> {{ banner.subtitle }}</p>
            <p class="typing">Khám phá ngay điều bất ngờ...</p>
            <el-button type="primary" size="large" class="banner-btn">
              {{ banner.buttonText }}
            </el-button>
          </div>
          <!-- Phần giữa -->
          <div class="banner-middle-icon pulse-icon">
            <i class="fa fa-star"></i>
          </div>

          
          <div class="banner-image">
            <img :src="banner.image" alt="Banner image" class="banner-img" />
          </div>
        </div>
      </div>
    </div>

    <!-- Dots indicator -->
    <div class="slider-dots">
      <span
        v-for="(banner, index) in banners"
        :key="index"
        :class="['dot', { active: currentSlide === index }]"
        @click="goToSlide(index)"
      ></span>
    </div>

    <!-- Prev/Next buttons -->
    <button class="slider-btn prev-btn" @click="prevSlide">
      <ChevronLeft size="20" />
    </button>
    <button class="slider-btn next-btn" @click="nextSlide">
      <ChevronRight size="20" />
    </button>
  </section>
</template>

<style scoped>
/* Enhanced Banner Slider with Rich Content */
.banner-slider {
  position: relative;
  width: 100%;
  height: 450px;
  overflow: hidden;
  border-radius: 20px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  background: #000;
}

.slider-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.slide {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: all 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-size: 400% 400%;
  animation: gradientMove 15s ease infinite;
}

.slide:nth-child(1) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.slide:nth-child(2) {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.slide:nth-child(3) {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.slide.active {
  opacity: 1;
  transform: scale(1);
}

.slide::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    135deg,
    rgba(0, 0, 0, 0.4) 0%,
    rgba(0, 0, 0, 0.2) 50%,
    rgba(0, 0, 0, 0.4) 100%
  );
  backdrop-filter: blur(2px);
  z-index: 0;
}

.slide::after {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.03'%3E%3Ccircle cx='30' cy='30' r='2'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E") repeat;
  z-index: 0;
}

@keyframes gradientMove {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.banner-content {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: 1.2fr 0.3fr 1fr;
  align-items: center;
  height: 100%;
  padding: 0 60px;
  color: white;
  max-width: 1400px;
  margin: 0 auto;
  gap: 40px;
}

.banner-text {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.banner-text h2 {
  font-size: 3rem;
  font-weight: 800;
  margin: 0;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.6);
  line-height: 1.1;
  background: linear-gradient(45deg, #ffffff, #f0f0f0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.banner-subtitle {
  font-size: 1.3rem;
  margin: 0;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  gap: 10px;
}

.banner-subtitle i {
  color: #ffd700;
  font-size: 1.4rem;
  animation: flash 2s infinite;
}

@keyframes flash {
  0%, 50%, 100% {
    opacity: 1;
  }
  25%, 75% {
    opacity: 0.5;
  }
}

.banner-features {
  list-style: none;
  padding: 0;
  margin: 16px 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.banner-features li {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1rem;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.4);
  opacity: 0;
  animation: slideInLeft 0.6s ease forwards;
}

.banner-features li:nth-child(1) {
  animation-delay: 0.2s;
}

.banner-features li:nth-child(2) {
  animation-delay: 0.4s;
}

.banner-features li:nth-child(3) {
  animation-delay: 0.6s;
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.banner-features li::before {
  content: "✓";
  background: linear-gradient(45deg, #00ff88, #00cc6a);
  color: white;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
}

.typing {
  font-size: 1.1rem;
  font-weight: 600;
  color: #ffd700;
  margin: 16px 0;
  white-space: nowrap;
  overflow: hidden;
  border-right: 3px solid #ffd700;
  animation: typing 4s steps(30), blink 0.8s step-end infinite alternate;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.6);
}

@keyframes typing {
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
}

@keyframes blink {
  50% {
    border-color: transparent;
  }
}

.banner-actions {
  display: flex;
  gap: 16px;
  margin-top: 20px;
}

.banner-btn {
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  color: white;
  border: none;
  padding: 14px 28px;
  border-radius: 30px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 6px 20px rgba(255, 107, 107, 0.4);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  position: relative;
  overflow: hidden;
}

.banner-btn::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s;
}

.banner-btn:hover::before {
  left: 100%;
}

.banner-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(255, 107, 107, 0.5);
}

.banner-btn-secondary {
  background: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.8);
  padding: 12px 24px;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.banner-btn-secondary:hover {
  background: rgba(255, 255, 255, 0.2);
  border-color: white;
  transform: translateY(-2px);
}

.banner-middle-icon {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  min-width: 100px;
}

.pulse-icon {
  font-size: 48px;
  color: #ffd700;
  animation: pulse 2s infinite;
  text-shadow: 0 0 20px rgba(255, 215, 0, 0.6);
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.9;
  }
  50% {
    transform: scale(1.3);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 0.9;
  }
}

.banner-badge {
  background: linear-gradient(45deg, #ff6b6b, #ff8e8e);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

.banner-image {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  justify-content: center;
  position: relative;
}

.banner-img {
  width: 100%;
  max-width: 320px;
  height: 350px;
  object-fit: contain;
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
  transition: all 0.4s ease;
  filter: drop-shadow(0 0 20px rgba(255, 255, 255, 0.1));
}

.banner-img:hover {
  transform: scale(1.05) rotateY(5deg);
  box-shadow: 0 16px 50px rgba(0, 0, 0, 0.5);
}

.banner-price {
  background: linear-gradient(45deg, #00ff88, #00cc6a);
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 1.2rem;
  font-weight: 800;
  margin-top: 16px;
  box-shadow: 0 4px 15px rgba(0, 255, 136, 0.3);
  animation: priceGlow 2s ease-in-out infinite alternate;
}

@keyframes priceGlow {
  from {
    box-shadow: 0 4px 15px rgba(0, 255, 136, 0.3);
  }
  to {
    box-shadow: 0 6px 25px rgba(0, 255, 136, 0.6);
  }
}

.slider-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.9);
  border: none;
  padding: 16px;
  border-radius: 50%;
  cursor: pointer;
  z-index: 10;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.slider-btn:hover {
  background: white;
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.3);
}

.prev-btn {
  left: 24px;
}

.next-btn {
  right: 24px;
}

.slider-dots {
  position: absolute;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 15;
  display: flex;
  gap: 12px;
}

.dot {
  width: 14px;
  height: 14px;
  background-color: rgba(255, 255, 255, 0.4);
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.dot:hover {
  background-color: rgba(255, 255, 255, 0.7);
  transform: scale(1.2);
}

.dot.active {
  background-color: #ffffff;
  transform: scale(1.4);
  border-color: rgba(255, 255, 255, 0.5);
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
}

/* Responsive Design */
@media (max-width: 1024px) {
  .banner-content {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 24px;
    padding: 40px;
  }
  
  .banner-middle-icon {
    order: -1;
  }
  
  .banner-text h2 {
    font-size: 2.5rem;
  }
}

@media (max-width: 768px) {
  .banner-slider {
    height: 350px;
  }
  
  .banner-content {
    padding: 24px;
    gap: 20px;
  }
  
  .banner-text h2 {
    font-size: 2rem;
  }
  
  .banner-subtitle {
    font-size: 1.1rem;
  }
  
  .banner-features {
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .banner-img {
    max-width: 240px;
    height: 240px;
  }
  
  .banner-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .typing {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .banner-slider {
    height: 300px;
    border-radius: 16px;
  }
  
  .banner-content {
    padding: 20px;
  }
  
  .banner-text h2 {
    font-size: 1.6rem;
  }
  
  .banner-subtitle {
    font-size: 1rem;
  }
  
  .banner-img {
    max-width: 180px;
    height: 180px;
  }
  
  .slider-btn {
    width: 44px;
    height: 44px;
    padding: 12px;
  }
  
  .prev-btn {
    left: 16px;
  }
  
  .next-btn {
    right: 16px;
  }
  
  .banner-btn {
    padding: 12px 20px;
    font-size: 1rem;
  }
  
  .pulse-icon {
    font-size: 36px;
  }
}
</style>
