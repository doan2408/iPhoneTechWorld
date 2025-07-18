<template>
  <div class="app-wrapper">

    <!-- Footer always at bottom -->
    <footer class="app-footer">
      <div class="footer-container">
        <!-- Footer Top Section -->
        <div class="footer-top">
          <div class="footer-section">
            <div class="footer-logo">
              <h3>TechWorld</h3>
              <p>Cửa hàng công nghệ hàng đầu Việt Nam</p>
            </div>
          </div>

          <div class="footer-section">
            <h4>Sản phẩm</h4>
            <ul class="footer-links">
              <li><a href="#">Điện thoại</a></li>
              <li><a href="#">Laptop</a></li>
              <li><a href="#">Tablet</a></li>
              <li><a href="#">Phụ kiện</a></li>
            </ul>
          </div>

          <div class="footer-section">
            <h4>Hỗ trợ</h4>
            <ul class="footer-links">
              <li><a href="#">Chính sách bảo hành</a></li>
              <li><a href="#">Hướng dẫn mua hàng</a></li>
              <li><a href="#">Chính sách đổi trả</a></li>
              <li><a href="#">Liên hệ</a></li>
            </ul>
          </div>

          <div class="footer-section">
            <h4>Kết nối với chúng tôi</h4>
            <div class="social-links">
              <a href="#" class="social-link facebook">
                <span class="social-icon">📘</span>
                Facebook
              </a>
              <a href="#" class="social-link instagram">
                <span class="social-icon">📷</span>
                Instagram
              </a>
              <a href="#" class="social-link youtube">
                <span class="social-icon">📺</span>
                YouTube
              </a>
            </div>
            <div class="contact-info">
              <p><span class="contact-icon">📞</span> Hotline: 1900-1234</p>
              <p><span class="contact-icon">✉️</span> Email: support@techworld.vn</p>
            </div>
          </div>
        </div>

        <!-- Footer Bottom Section -->
        <div class="footer-bottom">
          <div class="footer-bottom-content">
            <p>&copy; 2025 TechWorld. Tất cả quyền được bảo lưu.</p>
            <div class="footer-bottom-links">
              <a href="#">Điều khoản sử dụng</a>
              <span class="separator">|</span>
              <a href="#">Chính sách bảo mật</a>
              <span class="separator">|</span>
              <a href="#">Sitemap</a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// Demo data - replace with your actual product data
const currentYear = new Date().getFullYear()

onMounted(() => {
  // Any initialization logic
  console.log('Footer component mounted')
})
</script>

<style scoped>
html, body {
  height: 100%;
  margin: 0;
  padding: 0;
}

.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 60vh;
}

/* Footer Styling */
.app-footer {
  background: linear-gradient(135deg, #1a202c 0%, #2d3748 100%);
  color: #e2e8f0;
  margin-top: auto;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.15);
  position: relative;
  padding-top: 40px;
}

.app-footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
}

.footer-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-top {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  padding: 50px 0 40px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-section h4 {
  color: #ffffff;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  position: relative;
  padding-bottom: 8px;
}

.footer-section h4::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 30px;
  height: 2px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 1px;
}

.footer-logo h3 {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.footer-logo p {
  color: #a0aec0;
  font-size: 14px;
  line-height: 1.5;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 12px;
}

.footer-links a {
  color: #cbd5e0;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
  display: inline-block;
  position: relative;
}

.footer-links a::before {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background: linear-gradient(90deg, #667eea, #764ba2);
  transition: width 0.3s ease;
}

.footer-links a:hover {
  color: #ffffff;
  transform: translateX(4px);
}

.footer-links a:hover::before {
  width: 100%;
}

.social-links {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 25px;
}

.social-link {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #cbd5e0;
  text-decoration: none;
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.social-link:hover {
  color: #ffffff;
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.social-link.facebook:hover {
  background: rgba(24, 119, 242, 0.2);
  border-color: rgba(24, 119, 242, 0.3);
}

.social-link.instagram:hover {
  background: rgba(225, 48, 108, 0.2);
  border-color: rgba(225, 48, 108, 0.3);
}

.social-link.youtube:hover {
  background: rgba(255, 0, 0, 0.2);
  border-color: rgba(255, 0, 0, 0.3);
}

.contact-info {
  margin-top: 20px;
}

.contact-info p {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #a0aec0;
  font-size: 14px;
  margin-bottom: 8px;
}

.footer-bottom {
  padding: 25px 0;
  background: rgba(0, 0, 0, 0.2);
}

.footer-bottom-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.footer-bottom-content p {
  color: #a0aec0;
  font-size: 14px;
  margin: 0;
}

.footer-bottom-links {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.footer-bottom-links a {
  color: #cbd5e0;
  text-decoration: none;
  font-size: 13px;
  transition: color 0.3s ease;
}

.footer-bottom-links a:hover {
  color: #ffffff;
}

.separator {
  color: #4a5568;
  font-size: 12px;
}

/* Responsive Design */
@media (max-width: 768px) {
  .footer-top {
    grid-template-columns: 1fr;
    gap: 30px;
    padding: 40px 0 30px;
  }

  .footer-container {
    padding: 0 15px;
  }

  .social-links {
    flex-direction: row;
    flex-wrap: wrap;
  }

  .footer-bottom-content {
    flex-direction: column;
    text-align: center;
    gap: 15px;
  }

  .footer-bottom-links {
    justify-content: center;
  }
}

@media (max-width: 480px) {
  .footer-top {
    padding: 30px 0 20px;
  }

  .footer-bottom {
    padding: 20px 0;
  }

  .social-links {
    flex-direction: column;
  }

  .footer-bottom-links {
    flex-direction: column;
    gap: 10px;
  }

  .separator {
    display: none;
  }
}

</style>

