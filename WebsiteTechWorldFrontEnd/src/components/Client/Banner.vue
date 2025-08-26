<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const banners = ref([
  {
    id: 1,
    title: "iPhone 15 Pro Max",
    subtitle: "Giảm giá lên đến 30% - Trả góp 0%",
    price: "29.990.000đ",
    originalPrice: "42.990.000đ",
    discount: "30%",
    buttonText: "Mua ngay",
    features: ["Chip A17 Pro", "Camera 48MP", "Titanium"],
    image: "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=320&h=380&fit=crop&crop=center",
    badge: "Hot Sale"
  },
  {
    id: 2,
    title: "iPhone 16 Pro Max",
    subtitle: "Ưu đãi độc quyền - Quà tặng hấp dẫn",
    price: "34.990.000đ",
    originalPrice: "39.990.000đ",
    discount: "12%",
    buttonText: "Khám phá",
    features: ["Chip A18 Pro", "AI Camera", "Titanium Blue"],
    image: "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=320&h=380&fit=crop&crop=center",
    badge: "Mới nhất"
  },
  {
    id: 3,
    title: "iPhone 15 Plus",
    subtitle: "Giá sốc chỉ từ 15.990.000đ",
    price: "15.990.000đ",
    originalPrice: "24.990.000đ",
    discount: "36%",
    buttonText: "Xem chi tiết",
    features: ["Màn hình 6.7\"", "Camera kép", "5 màu sắc"],
    image: "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=320&h=380&fit=crop&crop=center",
    badge: "Siêu Sale"
  }
]);

const currentSlide = ref(0);
let slideInterval = null;

const goToSlide = (index) => {
  currentSlide.value = index;
};

const prevSlide = () => {
  currentSlide.value = (currentSlide.value - 1 + banners.value.length) % banners.value.length;
};

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % banners.value.length;
};

onMounted(() => {
  slideInterval = setInterval(nextSlide, 5000);
});

onUnmounted(() => {
  if (slideInterval) {
    clearInterval(slideInterval);
  }
});
</script>

<template>
  <section class="banner-slider">
    <!-- Promo Strip -->
    <div class="promo-strip">
      <i class="fa fa-fire"></i> FLASH SALE - Giảm đến 50% - Miễn phí giao hàng toàn quốc
    </div>

    <div class="slider-container">
      <div
        class="slide"
        v-for="(banner, index) in banners"
        :key="banner.id"
        :class="[`slide-${index + 1}`, { active: currentSlide === index }]"
      >
        <!-- Floating Icons -->
        <div class="floating-icons">
          <i class="fa fa-star floating-icon icon-1"></i>
          <i class="fa fa-bolt floating-icon icon-2"></i>
          <i class="fa fa-gift floating-icon icon-3"></i>
        </div>

        <div class="banner-content">
          <!-- Left Content -->
          <div class="banner-left">
            <div class="badge">
              <i class="fa fa-fire"></i> {{ banner.badge }}
            </div>
            
            <h1 class="banner-title">{{ banner.title }}</h1>
            <p class="banner-subtitle">{{ banner.subtitle }}</p>
            
            <div class="price-section">
              <span class="current-price">{{ banner.price }}</span>
              <span class="original-price">{{ banner.originalPrice }}</span>
              <span class="discount">-{{ banner.discount }}</span>
            </div>
            
            <div class="features">
              <span 
                v-for="feature in banner.features" 
                :key="feature"
                class="feature-tag"
              >
                {{ feature }}
              </span>
            </div>
            
            <div class="banner-actions">
              <button class="btn-primary">
                <i class="fa fa-shopping-cart"></i>
                {{ banner.buttonText }}
              </button>
              <button class="btn-secondary">
                Tư vấn ngay
              </button>
            </div>
          </div>
          
          <!-- Right Content -->
          <div class="banner-right">
            <img :src="banner.image" :alt="banner.title" class="product-image">
          </div>
        </div>
      </div>
    </div>

    <!-- Navigation Dots -->
    <div class="navigation">
      <span 
        v-for="(banner, index) in banners"
        :key="index"
        :class="['dot', { active: currentSlide === index }]"
        @click="goToSlide(index)"
      ></span>
    </div>

    <!-- Arrow Buttons -->
    <button class="arrow-btn prev" @click="prevSlide">
      <i class="fa fa-chevron-left"></i>
    </button>
    <button class="arrow-btn next" @click="nextSlide">
      <i class="fa fa-chevron-right"></i>
    </button>
  </section>
</template>

<style scoped>
.banner-slider {
  position: relative;
  width: 100%;
  height: 470px;
  overflow: hidden;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  margin: 1px auto;
  max-width: 1450px;
}

.promo-strip {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  background: linear-gradient(90deg, #0e39f9, #86e7e9);
  color: white;
  text-align: center;
  padding: 8px 0;
  font-size: 0.9rem;
  font-weight: 600;
  z-index: 5;
  animation: slideInDown 0.5s ease-out;
}

.slider-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.slide {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: opacity 0.6s ease-in-out;
  background: linear-gradient(135deg, #002fff 0%, #55eded 100%);
  padding: 10px;

}

.slide.active {
  opacity: 1;
}

.slide-1 { 
  background: linear-gradient(135deg, #00e1ff 0%, #e1ff00 100%); 
}
.slide-2 { 
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); 
}
.slide-3 { 
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); 
}

.floating-icons {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}

.floating-icon {
  position: absolute;
  font-size: 24px;
  opacity: 0.6;
  color: rgba(255, 255, 255, 0.8);
  animation: float 3s ease-in-out infinite;
}

.icon-1 { top: 20%; left: 10%; animation-delay: 0s; }
.icon-2 { top: 60%; right: 15%; animation-delay: 1s; }
.icon-3 { bottom: 30%; left: 5%; animation-delay: 2s; }

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.banner-content {
  display: grid;
  grid-template-columns: 1fr 380px;
  align-items: center;
  height: 100%;
  padding: 40px 60px;
  color: white;
  gap: 40px;
  position: relative;
  z-index: 2;
}

.banner-left {
  display: flex;
  flex-direction: column;
  gap: 20px;
  animation: slideInLeft 0.8s ease-out;
}

.badge {
  display: inline-block;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  width: fit-content;
  border: 1px solid rgba(255, 255, 255, 0.3);
  animation: bounce 2s infinite;
}

.banner-title {
  font-size: 3.2rem;
  font-weight: 800;
  margin: 0;
  line-height: 1.1;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  background: linear-gradient(45deg, #ffffff, #f0f0f0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.banner-subtitle {
  font-size: 1.3rem;
  font-weight: 500;
  margin: 0;
  opacity: 0.95;
  display: flex;
  align-items: center;
  gap: 8px;
}

.price-section {
  display: flex;
  align-items: center;
  gap: 16px;
  margin: 16px 0;
}

.current-price {
  font-size: 2.2rem;
  font-weight: 800;
  color: #ffd700;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3);
}

.original-price {
  font-size: 1.2rem;
  text-decoration: line-through;
  opacity: 0.7;
}

.discount {
  background: #ff4757;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.9rem;
  font-weight: 700;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.features {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.feature-tag {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(5px);
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 0.85rem;
  font-weight: 500;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.feature-tag:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-2px);
}

.banner-actions {
  display: flex;
  gap: 16px;
  margin-top: 24px;
}

.btn-primary {
  background: linear-gradient(45deg, #ff6b6b, #ee5a24);
  color: white;
  border: none;
  padding: 16px 32px;
  border-radius: 30px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 15px rgba(238, 90, 36, 0.4);
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
  overflow: hidden;
}

.btn-primary::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s;
}

.btn-primary:hover::before {
  left: 100%;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(238, 90, 36, 0.6);
}

.btn-secondary {
  background: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.5);
  padding: 14px 28px;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: white;
  transform: translateY(-2px);
}

.banner-right {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  justify-content: center;
  position: relative;
  animation: slideInRight 0.8s ease-out;
}

.product-image {
  width: 100%;
  max-width: 320px;
  height: 380px;
  object-fit: cover;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
  transition: transform 0.4s ease;
}

.product-image:hover {
  transform: scale(1.02) rotateY(5deg);
}

.navigation {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 12px;
  z-index: 10;
}

.dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s ease;
}

.dot:hover {
  background: rgba(255, 255, 255, 0.8);
  transform: scale(1.2);
}

.dot.active {
  background: white;
  transform: scale(1.3);
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.8);
}

.arrow-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.9);
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  cursor: pointer;
  z-index: 10;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #333;
  backdrop-filter: blur(10px);
}

.arrow-btn:hover {
  background: white;
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.prev { left: 20px; }
.next { right: 20px; }

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInDown {
  from {
    opacity: 0;
    transform: translateY(-100%);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
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

/* Responsive Design */
@media (max-width: 1024px) {
  .banner-content {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 30px;
    padding: 30px 40px;
  }
  
  .banner-title {
    font-size: 2.8rem;
  }
}

@media (max-width: 768px) {
  .banner-slider {
    height: 400px;
  }
  
  .banner-content {
    padding: 20px;
    gap: 20px;
  }
  
  .banner-title {
    font-size: 2.4rem;
  }
  
  .current-price {
    font-size: 1.8rem;
  }
  
  .product-image {
    max-width: 250px;
    height: 280px;
  }
  
  .banner-actions {
    flex-direction: column;
    align-items: center;
  }
}

@media (max-width: 480px) {
  .banner-slider {
    height: 350px;
    border-radius: 12px;
  }
  
  .banner-content {
    padding: 15px;
  }
  
  .banner-title {
    font-size: 2rem;
  }
  
  .product-image {
    max-width: 200px;
    height: 240px;
  }
  
  .arrow-btn {
    width: 44px;
    height: 44px;
  }
  
  .prev { left: 15px; }
  .next { right: 15px; }
}
</style>