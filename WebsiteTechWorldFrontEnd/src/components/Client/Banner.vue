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

<style scoped>

.banner-slider {
  position: relative;
  height: 400px;
  overflow: hidden;
  border-radius: 20px;
}

.slider-container {
  position: relative;
  height: 100%;
}

.slide {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0;
  transition: opacity 0.8s ease-in-out;
  background: linear-gradient(135deg, #0070f8 0%, #42d4e8 100%);
  animation: gradientMove 10s ease infinite;
}

.slide.active {
  opacity: 1;
}

.slide::before {
  content: "";
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(3px);
  z-index: 0;
}

.slide::after {
  content: "";
  background: url("@/assets/apple-logo-light.png") no-repeat center center /
    200px auto;
  opacity: 0.05;
  position: absolute;
  inset: 0;
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
  z-index: 1;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 80px;
  color: white;
}

.banner-text {
  flex: 1.2;
}

.banner-image {
  flex: 0.8;
  display: flex;
  justify-content: center;
  align-items: center;
}

.banner-image img.banner-img {
  width: 100%;
  max-height: 300px;
  object-fit: contain;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.banner-text h2,
.banner-text p {
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.4);
  margin-bottom: 10px;
}

.typing {
  width: 25ch;
  white-space: nowrap;
  overflow: hidden;
  border-right: 3px solid white;
  animation: typing 3s steps(25), blink 0.5s step-end infinite alternate;
  font-weight: 500;
  margin-bottom: 16px;
}

@keyframes typing {
  from {
    width: 0;
  }
}

@keyframes blink {
  50% {
    border-color: transparent;
  }
}

.slider-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255, 255, 255, 0.6);
  border: none;
  padding: 10px 15px;
  border-radius: 50%;
  font-size: 1.2rem;
  cursor: pointer;
  z-index: 10;
  transition: background 0.3s ease;
}

.slider-btn:hover {
  background: white;
}

.prev-btn {
  left: 20px;
}

.next-btn {
  right: 20px;
}

.slider-dots {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 15;
}

.dot {
  display: inline-block;
  width: 10px;
  height: 10px;
  margin: 0 4px;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.dot.active {
  background-color: #ffffff;
}
.banner-middle-icon {
  flex: 0.2;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 36px;
  color: #fff;
}

.pulse-icon {
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.9;
  }
  50% {
    transform: scale(1.2);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 0.9;
  }
}
</style>
