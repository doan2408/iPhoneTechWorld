<template>
  <div class="terms-page min-h-screen bg-gray-50">
    <!-- Header Card -->
    <div class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-8 lg:py-12">
      <div class="bg-white rounded-xl shadow-xl border border-gray-100 overflow-hidden" style="border: none !important;">
        <!-- Header -->
        <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-6 sm:px-8 py-6 sm:py-8">
          <div class="text-center">
            <h1 class="text-2xl sm:text-3xl lg:text-4xl font-bold text-white mb-2">
              Điều khoản & Điều kiện
            </h1>
            <p class="text-blue-100 text-sm sm:text-base">
              Cửa hàng iPhone chính hãng
            </p>
          </div>
        </div>

        <!-- Content -->
        <div class="px-6 sm:px-8 lg:px-12 py-8 sm:py-10 lg:py-12">
          <div class="max-w-4xl mx-auto">
            <section 
              v-for="section in sections" 
              :key="section.id" 
              :id="section.id" 
              class="mb-10 sm:mb-12 last:mb-8"
            >
              <h2 class="text-xl sm:text-2xl font-bold text-gray-900 mb-4 sm:mb-6 pb-2 border-b-2 border-blue-100">
                {{ section.title }}
              </h2>
              <div class="space-y-4 sm:space-y-5">
                <p 
                  v-for="(paragraph, index) in section.paragraphs" 
                  :key="index"
                  class="text-gray-700 leading-relaxed text-base sm:text-lg pl-4 border-l-4 border-gray-100"
                >
                  {{ paragraph }}
                </p>
              </div>
            </section>
            
            <div class="mt-12 pt-6 border-t-2 border-gray-200 text-center">
              <div class="bg-gray-50 rounded-lg p-4 sm:p-6">
                <p class="text-sm sm:text-base text-gray-600 font-medium">
                  📅 Cập nhật lần cuối: {{ lastUpdated }}
                </p>
                <p class="text-xs sm:text-sm text-gray-500 mt-2">
                  Phiên bản hiện tại của điều khoản dịch vụ
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Sticky Bottom Agreement Section -->
    <div class="fixed bottom-0 left-0 right-0 bg-white/98 backdrop-blur-md border-t-2 border-gray-200 shadow-2xl z-50">
      <div class="max-w-6xl mx-auto px-4 sm:px-6 py-4 sm:py-6">
        <div class="bg-gray-50 rounded-xl p-4 sm:p-6 border border-gray-200">
          <div class="flex justify-center">
            <!-- Checkbox Section -->
            <div class="w-full max-w-2xl">
              <label class="flex items-start gap-4 cursor-pointer group">
                <div class="relative mt-1 flex-shrink-0">
                  <input 
                    v-model="agreed" 
                    type="checkbox" 
                    class="h-5 w-5 text-blue-600 focus:ring-blue-500 focus:ring-2 border-2 border-gray-300 rounded-md transition-all group-hover:border-blue-400"
                    @change="handleAgreementChange"
                  >
                  <div v-if="agreed" class="absolute inset-0 flex items-center justify-center pointer-events-none">
                    <svg class="w-3 h-3 text-white" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                    </svg>
                  </div>
                </div>
                <div class="flex-1">
                  <span class="text-sm sm:text-base font-medium text-gray-900 block">
                    ✅ Xác nhận đồng ý điều khoản
                  </span>
                  <span class="text-xs sm:text-sm text-gray-600 mt-1 block">
                    Tôi đã đọc và đồng ý với Điều khoản & Điều kiện của cửa hàng iPhone chính hãng.
                  </span>
                  <div v-if="agreed" class="mt-3 p-3 bg-green-50 border border-green-200 rounded-lg">
                    <p class="text-sm text-green-700 font-medium flex items-center gap-2">
                      <span>✓</span>
                      <span>Cảm ơn bạn đã đồng ý với điều khoản của chúng tôi!</span>
                    </p>
                  </div>
                </div>
              </label>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Bottom spacer to prevent content hiding -->
    <div class="h-24 sm:h-28"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Reactive data
const agreed = ref(false)
const lastUpdated = '26/08/2025'
const AGREEMENT_KEY = 'tosAcceptedIphone'

// Sections data
const sections = [
  {
    id: 'gioi-thieu',
    title: '1. Giới thiệu',
    paragraphs: [
      'Khi mua sắm tại cửa hàng/website bán iPhone của chúng tôi, khách hàng đồng ý tuân thủ các Điều khoản & Điều kiện dưới đây.',
      'Vui lòng đọc kỹ trước khi đặt hàng.'
    ]
  },
  {
    id: 'san-pham',
    title: '2. Sản phẩm',
    paragraphs: [
      'Tất cả iPhone được bán ra đều là hàng chính hãng Apple, có đầy đủ hộp, phụ kiện và tem bảo hành.',
      'Thông tin về cấu hình, dung lượng bộ nhớ, màu sắc được hiển thị rõ ràng trên website và tại cửa hàng.',
      'Cửa hàng cam kết không bán hàng giả, hàng nhái hoặc hàng dựng.'
    ]
  },
  {
    id: 'gia-thanh-toan',
    title: '3. Giá bán & Thanh toán',
    paragraphs: [
      'Giá iPhone được niêm yết tại website/cửa hàng, đã bao gồm thuế VAT (nếu có).',
      'Khách hàng có thể thanh toán bằng tiền mặt, chuyển khoản ngân hàng hoặc quẹt thẻ.',
      'Với đơn hàng online, thanh toán trước hoặc COD (thanh toán khi nhận hàng) đều được hỗ trợ.'
    ]
  },
  {
    id: 'dat-hang',
    title: '4. Đặt hàng & Xác nhận đơn',
    paragraphs: [
      'Đơn hàng chỉ được xác nhận khi cửa hàng liên hệ lại với khách hàng để xác nhận thông tin.',
      'Cửa hàng có quyền từ chối hoặc hủy đơn trong trường hợp hết hàng, giá niêm yết sai sót hoặc nghi ngờ gian lận.'
    ]
  },
  {
    id: 'giao-hang',
    title: '5. Giao hàng',
    paragraphs: [
      'Giao hàng toàn quốc qua các đơn vị vận chuyển uy tín.',
      'Thời gian giao hàng dự kiến từ 1–5 ngày tùy khu vực.',
      'Khách hàng cần cung cấp địa chỉ và số điện thoại chính xác để tránh giao hàng thất bại.'
    ]
  },
  {
    id: 'bao-hanh',
    title: '6. Bảo hành & Đổi trả',
    paragraphs: [
      'iPhone chính hãng được bảo hành 12 tháng tại trung tâm ủy quyền Apple (AASP).',
      'Khách hàng có quyền đổi mới trong vòng 7 ngày nếu máy bị lỗi do nhà sản xuất (theo chính sách của Apple Việt Nam).',
      'Trường hợp đổi trả khác (không lỗi, đổi màu, đổi dung lượng) sẽ áp dụng theo chính sách riêng của cửa hàng và có thể phát sinh phí.'
    ]
  },
  {
    id: 'trach-nhiem-khach-hang',
    title: '7. Trách nhiệm của khách hàng',
    paragraphs: [
      'Cung cấp thông tin chính xác khi đặt hàng.',
      'Giữ hóa đơn/phiếu bảo hành để được hỗ trợ trong các trường hợp cần thiết.',
      'Không tự ý can thiệp phần cứng/phần mềm làm ảnh hưởng đến chế độ bảo hành.'
    ]
  },
  {
    id: 'so-huu-tri-tue',
    title: '8. Quyền sở hữu trí tuệ',
    paragraphs: [
      'Logo, hình ảnh, mô tả sản phẩm iPhone được sử dụng chỉ nhằm mục đích bán hàng, thuộc bản quyền của Apple và/hoặc cửa hàng.',
      'Khách hàng không được phép sao chép hoặc sử dụng trái phép cho mục đích thương mại.'
    ]
  },
  {
    id: 'luat',
    title: '9. Luật áp dụng & Giải quyết tranh chấp',
    paragraphs: [
      'Các điều khoản này tuân theo pháp luật Việt Nam.',
      'Trường hợp phát sinh tranh chấp, hai bên ưu tiên giải quyết bằng thương lượng; nếu không thành, sẽ đưa ra tòa án có thẩm quyền.'
    ]
  },
  {
    id: 'lien-he',
    title: '10. Liên hệ',
    paragraphs: [
      'Điện thoại: 0123 456 789',
      'Email: support@iphone-store.vn',
      'Địa chỉ: 123 Nguyễn Trãi, Hà Nội'
    ]
  }
]

// Methods
function handleAgreementChange() {
  if (agreed.value) {
    // Auto save when user agrees
    const payload = { 
      accepted: true, 
      timestamp: new Date().toISOString() 
    }
    
    try {
      localStorage.setItem(AGREEMENT_KEY, JSON.stringify(payload))
      showMessage('Cảm ơn bạn đã đồng ý với điều khoản của chúng tôi!', 'success')
    } catch (error) {
      console.error('Error saving agreement:', error)
      showMessage('Có lỗi xảy ra khi lưu thông tin.', 'error')
    }
  } else {
    // Remove agreement when unchecked
    try {
      localStorage.removeItem(AGREEMENT_KEY)
      showMessage('Bạn đã bỏ đồng ý điều khoản.', 'info')
    } catch (error) {
      console.error('Error removing agreement:', error)
    }
  }
}

function showMessage(message, type = 'info') {
  // Simple message display - replace with your UI library's notification
  const colors = {
    success: '#10B981',
    error: '#EF4444', 
    info: '#6B7280'
  }
  
  // Create temporary notification
  const notification = document.createElement('div')
  notification.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: ${colors[type]};
    color: white;
    padding: 12px 24px;
    border-radius: 8px;
    z-index: 9999;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    font-size: 14px;
    max-width: 300px;
  `
  notification.textContent = message
  document.body.appendChild(notification)
  
  setTimeout(() => {
    notification.remove()
  }, 3000)
}

// Lifecycle
onMounted(() => {
  try {
    const saved = localStorage.getItem(AGREEMENT_KEY)
    if (saved) {
      const data = JSON.parse(saved)
      if (data?.accepted) {
        agreed.value = true
      }
    }
  } catch (error) {
    console.error('Error loading saved agreement:', error)
  }
})
</script>

<style scoped>
/* Container styling */
.max-w-5xl {
  max-width: 72rem; /* hơi gọn lại để cân đối */
}

.max-w-6xl {
  max-width: 80rem;
}

/* Smooth scrolling */
html {
  scroll-behavior: smooth;
}

/* Typography improvements */
h1, h2, h3 {
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* Header gradient text */
h1 {
  background: linear-gradient(90deg, #60a5fa, #2563eb);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

/* Section titles */
section h2 {
  position: relative;
}
section h2::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: -4px;
  width: 60px;
  height: 3px;
  border-radius: 2px;
  background: linear-gradient(90deg, #2563eb, #60a5fa);
}

/* Paragraph styling */
section p {
  padding-left: 1rem;
  border-left: 3px solid #e5e7eb;
  transition: border-color 0.3s;
}
section p:hover {
  border-color: #60a5fa;
}

/* Custom checkbox */
input[type="checkbox"] {
  appearance: none;
  -webkit-appearance: none;
  width: 20px;
  height: 20px;
  border: 2px solid #cbd5e1;
  border-radius: 6px;
  background: #fff;
  cursor: pointer;
  position: relative;
  transition: all 0.2s ease;
}
input[type="checkbox"]:checked {
  background: #2563eb;
  border-color: #2563eb;
}
input[type="checkbox"]:checked::after {
  content: "✔";
  position: absolute;
  top: -2px;
  left: 4px;
  font-size: 14px;
  color: #fff;
  transform: scale(0);
  animation: checkmark 0.2s ease forwards;
}
@keyframes checkmark {
  to {
    transform: scale(1);
  }
}

/* Notification popup */
.notification {
  position: fixed;
  top: 20px;
  right: -320px;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 14px;
  color: #fff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  z-index: 9999;
  animation: slideIn 0.4s ease forwards, fadeOut 0.4s ease 2.6s forwards;
}
@keyframes slideIn {
  to { right: 20px; }
}
@keyframes fadeOut {
  to { opacity: 0; transform: translateX(50px); }
}

/* Scrollbar styling */
::-webkit-scrollbar {
  width: 8px;
}
::-webkit-scrollbar-track {
  background: #f9fafb;
}
::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}
::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* Content animation */
.terms-page {
  animation: fadeUp 0.5s ease-out;
}
@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

</style>