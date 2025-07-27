<template>
  <div class="hang-container">
    <h2>Hạng hiện tại: {{ hang?.tenHang }}</h2>
    <p>Điểm hiện tại: {{ hang?.diemHienTai }}</p>
    <div class="progress-wrapper">
      <div class="progress-bar">
        <div
          class="progress-fill"
          :style="{ width: progress + '%' }"
        ></div>
      </div>
      <p>{{ hang?.diemHienTai }}/{{ hang?.diemDen }} điểm</p>
    </div>
    <p v-if="hang">
      Cần thêm <strong>{{ hang.diemConThieu }}</strong> điểm để lên hạng tiếp theo.
    </p>
    <!-- Nút quay lại -->
    <button class="back-btn" @click="goBack">⬅ Quay lại đổi điểm</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { khoangHang } from "@/Service/ClientService/TichDiem/HangServices";

const hang = ref(null);
const router = useRouter();

const progress = computed(() => {
  if (!hang.value) return 0;
  const range = hang.value.diemDen - hang.value.diemTu;
  const achieved = hang.value.diemHienTai - hang.value.diemTu;
  return Math.min((achieved / range) * 100, 100);
});

onMounted(async () => {
  const data = await khoangHang();
  hang.value = data;
});

const goBack = () => {
  router.push("/client/doiDiem");
};
</script>

<style scoped>
.hang-container {
  max-width: 500px;
  margin: 20px auto;
  padding: 24px;
  text-align: center;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.hang-container h2 {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 16px 0;
}

.hang-container p {
  font-size: 16px;
  color: #6b7280;
  margin: 8px 0;
  line-height: 1.5;
}

.progress-wrapper {
  margin: 24px 0;
  padding: 0 8px;
}

.progress-bar {
  width: 100%;
  height: 24px;
  background: #f3f4f6;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 12px;
  position: relative;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.06);
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #10b981, #059669);
  transition: width 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 12px;
  position: relative;
}

.progress-fill::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  animation: shimmer 2s infinite;
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.progress-wrapper p {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0;
}

.hang-container p strong {
  color: #dc2626;
  font-weight: 700;
}

.back-btn {
  background: linear-gradient(135deg, #3b82f6, #1d4ed8);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-top: 24px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.3);
}

.back-btn:hover {
  background: linear-gradient(135deg, #2563eb, #1e40af);
  transform: translateY(-2px);
  box-shadow: 0 8px 15px -3px rgba(59, 130, 246, 0.4);
}

.back-btn:active {
  transform: translateY(0);
  box-shadow: 0 4px 6px -1px rgba(59, 130, 246, 0.3);
}

.back-btn:focus {
  outline: none;
}

/* Responsive design */
@media (max-width: 640px) {
  .hang-container {
    margin: 16px;
    padding: 20px;
    max-width: none;
  }
  
  .hang-container h2 {
    font-size: 20px;
  }
  
  .progress-bar {
    height: 20px;
  }
  
  .back-btn {
    width: 100%;
    justify-content: center;
  }
}

/* Dark mode support */
@media (prefers-color-scheme: dark) {
  .hang-container {
    background: #1f2937;
    color: #f9fafb;
  }
  
  .hang-container h2 {
    color: #f9fafb;
  }
  
  .hang-container p {
    color: #d1d5db;
  }
  
  .progress-bar {
    background: #374151;
  }
  
  .progress-wrapper p {
    color: #e5e7eb;
  }
}
</style>