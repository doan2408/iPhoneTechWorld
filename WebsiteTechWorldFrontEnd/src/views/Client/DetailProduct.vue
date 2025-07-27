<script setup>
import { ref, onMounted, watch, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import {
  detailSanPham,
  getChiTietBienThe,
  getThongSo,
  getListAnhByMau,
} from "@/Service/ClientService/Products/ProductClientService";
import { cartService } from "@/service/ClientService/GioHang/GioHangClientService";
import { ShoppingCart } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import headerState from "@/components/Client/modules/headerState";

const count = ref(0);
const store = useStore();
const guiLenHeader = () => {
  store.commit("headerState/setCartItemCount", count.value);
};

const user = JSON.parse(localStorage.getItem("user"));
const route = useRoute();
const idSanPham = route.params.id;
const router = useRouter();

const activeTab = ref("thong-tin-hang-hoa");
const specModalContent = ref(null);
const specTabsContainer = ref(null);

const registerHeaderModule = () => {
  if (!store.hasModule("headerState")) {
    store.registerModule("headerState", headerState);
  }
};

registerHeaderModule();

const formatPrice = (val) => {
  if (!val) return "0₫";
  return new Intl.NumberFormat("vi-VN").format(val) + "₫";
};

const formatDiemThuong = (giaBan) => {
  if (!giaBan) return 0;
  const diem = giaBan * 0.01;
  return new Intl.NumberFormat("vi-VN").format(Math.floor(diem));
};

const sanPham = ref(null);
const selectedRom = ref(null);
const selectedMau = ref(null);
const selectedImage = ref("");
const bienThe = ref(null); //anh, gia, soLuongcon
const thongSo = ref(null); //thong so ky thuat specifications
const quantity = ref(1);

const showSpecModal = ref(false);

const openSpecModal = () => {
  showSpecModal.value = true;
};

const closeSpecModal = () => {
  showSpecModal.value = false;
};

// Function cải thiện cho cả Client và Guest
const scrollTabIntoView = (tabId) => {
  if (specTabsContainer.value) {
    const activeTabElement = specTabsContainer.value.querySelector(
      `[data-tab="${tabId}"]`
    );
    if (activeTabElement) {
      const container = specTabsContainer.value;
      
      // Tính toán vị trí để tab luôn ở góc trái với padding 20px
      const targetScrollLeft = activeTabElement.offsetLeft - 20;
      
      // Scroll container để đưa tab về góc trái
      container.scrollTo({
        left: Math.max(0, targetScrollLeft), // Đảm bảo không scroll âm
        behavior: "smooth"
      });
      
      // Debug log
      console.log('Tab scrolled to left:', tabId, targetScrollLeft);
    }
  }
};

// Nếu bạn muốn có tùy chọn vị trí khác, dùng function này:
const scrollTabIntoViewAdvanced = (tabId, position = 'left') => {
  if (specTabsContainer.value) {
    const activeTabElement = specTabsContainer.value.querySelector(
      `[data-tab="${tabId}"]`
    );
    if (activeTabElement) {
      const container = specTabsContainer.value;
      const containerWidth = container.clientWidth;
      const tabLeft = activeTabElement.offsetLeft;
      const tabWidth = activeTabElement.offsetWidth;
      
      let targetScrollLeft;
      
      switch (position) {
        case 'left':
          // Tab ở góc trái với padding 20px
          targetScrollLeft = tabLeft - 20;
          break;
        case 'center':
          // Tab ở giữa container
          targetScrollLeft = tabLeft - (containerWidth / 2) + (tabWidth / 2);
          break;
        case 'right':
          // Tab ở góc phải
          targetScrollLeft = tabLeft - containerWidth + tabWidth + 20;
          break;
        default:
          targetScrollLeft = tabLeft - 20;
      }
      
      // Scroll container
      container.scrollTo({
        left: Math.max(0, targetScrollLeft),
        behavior: "smooth"
      });
      
      console.log(`Tab "${tabId}" scrolled to ${position}:`, targetScrollLeft);
    }
  }
};

const scrollToSection = (sectionId) => {
  activeTab.value = sectionId;
  scrollTabIntoView(sectionId);

  if (specModalContent.value) {
    // Sử dụng nextTick để đảm bảo DOM đã được cập nhật
    nextTick(() => {
      const targetSection = specModalContent.value.querySelector(
        `[data-section="${sectionId}"]`
      );
      
      if (targetSection) {
        const modalContent = specModalContent.value;
        const headerHeight = 81; // Modal header
        const tabsHeight = 49; // Tabs height
        
        // Tính toán vị trí chính xác của section
        const sectionTop = targetSection.offsetTop;
        const scrollPosition = sectionTop - headerHeight - tabsHeight - 10;
        
        // Debug log để kiểm tra
        console.log('Section ID:', sectionId);
        console.log('Section top:', sectionTop);
        console.log('Scroll to:', scrollPosition);
        
        modalContent.scrollTo({
          top: Math.max(0, scrollPosition), // Đảm bảo không scroll âm
          behavior: "smooth",
        });
      } else {
        console.warn(`Section with data-section="${sectionId}" not found`);
      }
    });
  }
};

const handleScroll = () => {
  if (!specModalContent.value) return;

  const scrollTop = specModalContent.value.scrollTop;
  const headerHeight = 81;
  const tabsHeight = 49;
  const buffer = 50; // Tăng buffer để dễ trigger hơn

  // Get all sections
  const sections = specModalContent.value.querySelectorAll(
    ".spec-section[data-section]"
  );

  let currentSection = "thong-tin-hang-hoa"; // default

  // Tìm section hiện tại dựa trên scroll position
  sections.forEach((section) => {
    const sectionTop = section.offsetTop - headerHeight - tabsHeight;
    const sectionBottom = sectionTop + section.offsetHeight;

    // Kiểm tra nếu phần lớn section đang visible
    if (scrollTop >= sectionTop - buffer && scrollTop < sectionBottom - buffer) {
      currentSection = section.getAttribute("data-section");
    }
  });

  // Cập nhật active tab nếu khác
  if (activeTab.value !== currentSection) {
    activeTab.value = currentSection;
    scrollTabIntoView(currentSection);
  }
};

const increaseQty = () => {
  if (bienThe.value && quantity.value < bienThe.value.soLuong) {
    quantity.value++;
  }
};

const decreaseQty = () => {
  if (quantity.value > 1) {
    quantity.value--;
  }
};

const fetchSanPhamDetail = async () => {
  try {
    const data = await detailSanPham(idSanPham);
    sanPham.value = data;
    if (data.rom.length > 0) selectedRom.value = data.rom[0].id;
    if (data.mau.length > 0) selectedMau.value = data.mau[0].id;
    if (data.hinhAnh?.length > 0) selectedImage.value = data.hinhAnh[0];
  } catch (error) {
    console.error("Lỗi khi tải chi tiết sản phẩm:", error);
  }
};

const fetchThongSo = async () => {
  if (!selectedRom.value) return;
  try {
    const res = await getThongSo(idSanPham, selectedRom.value);
    thongSo.value = res;
  } catch (e) {
    thongSo.value = null;
    console.error("Không lấy được thông số:", e);
  }
};

const fetchChiTietBienThe = async () => {
  if (!selectedRom.value || !selectedMau.value) return;
  try {
    const res = await getChiTietBienThe(
      idSanPham,
      selectedMau.value,
      selectedRom.value
    );
    bienThe.value = res;
    if (res.hinhAnh?.length > 0) selectedImage.value = res.hinhAnh[0];
  } catch (e) {
    bienThe.value = { soLuong: 0 };
    console.error("Không lấy được biến thể:", e);
  }
};

const fetchListAnhByMau = async () => {
  if (!selectedMau.value || !idSanPham) return;
  const hinhAnh = await getListAnhByMau(idSanPham, selectedMau.value);
  if (hinhAnh.length > 0 && bienThe.value) {
    bienThe.value.hinhAnh = hinhAnh;
    selectedImage.value = hinhAnh[0];
  }
};

const themVaoGio = async (buy) => {
  try {
    if (quantity.value < 0) {
      ElMessage.error("Số lượng phải lớn hơn 0!");
      return;
    }
    const soLuongTonKho = bienThe.value.tongSoLuong;
    if (soLuongTonKho < 0) {
      ElMessage.error("Dữ liệu tồn kho không hợp lệ!");
      return;
    }
    const gioHangResponse = await cartService.getCart(user.id);
    let soLuongHienTai = 0;
    if (gioHangResponse && gioHangResponse.items) {
      const item = gioHangResponse.items.find(
        (chiTiet) => chiTiet.idSanPhamChiTiet === bienThe.value.idSpct
      );
      if (item) {
        soLuongHienTai = item.soLuong;
      }
    }
    const tongSoLuong = soLuongHienTai + quantity.value;
    if (tongSoLuong > soLuongTonKho) {
      ElMessage.error(
        `Số lượng vượt quá tồn kho. Trong giỏ hàng đã có ${soLuongHienTai} sản phẩm này.`
      );
      return;
    }
    await cartService.addToCart({
      idKhachHang: user.id,
      idSanPhamChiTiet: bienThe.value.idSpct,
      soLuong: quantity.value,
    });
    if (buy) {
      try {
        await router.push({
          path: "/client/shopping-cart",
          query: { selected: bienThe.value.idSpct },
        });
      } catch (navError) {
        console.error("Lỗi chuyển hướng:", navError);
        ElMessage.error("Không thể chuyển hướng đến giỏ hàng!");
      }
    } else {
      ElMessage.success("Sản phẩm đã được thêm vào giỏ hàng!");
    }
    try {
      count.value = await cartService.cartCount(user.id);
    } catch (error) {
      console.error("Lỗi khi tải giỏ hàng:", error);
    }
    guiLenHeader();
  } catch (error) {
    console.error("Lỗi khi thêm sản phẩm vào giỏ hàng:", error);
    ElMessage.error(
      error.response?.data?.message || "Lỗi khi thêm sản phẩm vào giỏ hàng!"
    );
  }
};

watch([selectedMau], () => {
  fetchChiTietBienThe();
  fetchListAnhByMau();
});

watch([selectedRom], () => {
  fetchChiTietBienThe();
  fetchThongSo(); // chỉ khi ROM đổi mới gọi
});

onMounted(() => {
  window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
  fetchSanPhamDetail();
});

watch(showSpecModal, (newVal) => {
  if (newVal) {
    nextTick(() => {
      if (specModalContent.value) {
        // Reset scroll position về đầu
        specModalContent.value.scrollTop = 0;
        
        // Add scroll listener
        specModalContent.value.addEventListener("scroll", handleScroll, { passive: true });
        
        // Set default active tab
        activeTab.value = "thong-tin-hang-hoa";
        scrollTabIntoView("thong-tin-hang-hoa");
      }
    });
  } else {
    // Cleanup
    if (specModalContent.value) {
      specModalContent.value.removeEventListener("scroll", handleScroll);
    }
  }
});
</script>

<template>
  <section class="product-detail">
    <div class="product-container">
      <!-- Cột trái: Hình ảnh (55%) -->
      <div class="product-image">
        <img
          :src="selectedImage || bienThe?.hinhAnh?.[0] || '/img/no-image.png'"
          alt="Hình ảnh sản phẩm"
          class="main-image"
        />
        <!-- Danh sách ảnh thu nhỏ -->
        <div class="thumbnail-list" v-if="bienThe?.hinhAnh?.length > 0">
          <img
            v-for="(img, index) in bienThe.hinhAnh"
            :key="index"
            :src="img"
            class="thumbnail"
            :class="{ active: img === selectedImage }"
            @click="selectedImage = img"
          />
        </div>

        <!-- Thông số nổi bật -->
        <div class="featured-specs" v-if="thongSo">
          <!-- Featured Specs Header với nút -->
          <div class="featured-specs-header">
            <h3 class="featured-title">Thông số nổi bật</h3>
            <button @click="openSpecModal" class="view-specs-btn">
              Xem thông số
            </button>
          </div>
          <div class="featured-specs-grid">
            <div class="spec-item">
              <i class="fas fa-microchip spec-icon"></i>
              <div class="spec-label">Chip</div>
              <div class="spec-value">{{ thongSo.cpu }}</div>
            </div>

            <div class="spec-item">
              <i class="fas fa-tv spec-icon"></i>
              <div class="spec-label">Màn hình</div>
              <div class="spec-value">{{ thongSo.kichThuoc }}</div>
            </div>

            <div class="spec-item">
              <i class="fas fa-battery-full spec-icon"></i>
              <div class="spec-label">Thời lượng pin</div>
              <div class="spec-value">{{ thongSo.thoiGianSuDung }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Cột phải: Thông tin + thông số (45%) -->
      <div class="product-right">
        <div class="product-info">
          <h1>{{ sanPham?.tenSanPham }}</h1>
          <router-link class="danhGia">Đánh giá</router-link> |
          <button @click="openSpecModal" class="thong-so-btn">
            Xem thông số
          </button>

          <!-- Màu sắc -->
          <div class="options">
            <h3>Chọn màu:</h3>
            <div class="option-list">
              <el-radio-group v-model="selectedMau">
                <el-radio-button
                  v-for="m in sanPham?.mau"
                  :key="m.id"
                  :label="m.id"
                >
                  {{ m.ten }}
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>

          <!-- ROM -->
          <div class="options">
            <h3>Chọn ROM:</h3>
            <div class="option-list">
              <el-radio-group v-model="selectedRom">
                <el-radio-button
                  v-for="r in sanPham?.rom"
                  :key="r.id"
                  :label="r.id"
                >
                  {{ r.ten }}
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>

          <!-- Số lượng -->
          <div class="options" v-if="bienThe">
            <h3>Số lượng còn: {{ bienThe.soLuong ?? 0 }}</h3>
          </div>

          <p class="price">{{ formatPrice(bienThe?.giaBan) }}</p>

          <!-- Chọn số lượng -->
          <div class="options quantity-selector" v-if="bienThe?.soLuong > 0">
            <h3>Chọn số lượng:</h3>
            <div class="quantity-control">
              <button @click="decreaseQty" :disabled="quantity <= 1">-</button>
              <input
                type="number"
                v-model="quantity"
                min="1"
                :max="bienThe.soLuong"
              />
              <button
                @click="increaseQty"
                :disabled="quantity >= bienThe.soLuong"
              >
                +
              </button>
            </div>
          </div>
          <div class="banner-tichDiem">
            <router-link to="/client/doiDiem">
              <img
                src="/src/components/images/bannerTichDiem.jpg"
                alt="Banner tích điểm"
                class="tich-diem-img"
              />
            </router-link>
          </div>

          <div class="diemTichLuy">
            <i class="fas fa-coins xu-icon"></i>
            + {{ formatDiemThuong(bienThe?.giaBan) }} điểm thưởng
          </div>

          <!-- Hành động -->
          <div class="button-group">
            <el-button
              type="primary"
              :disabled="!selectedRom || !selectedMau || bienThe?.soLuong <= 0"
              class="buy-btn"
              @click="themVaoGio(true)"
            >
              Mua ngay
            </el-button>
            <el-button
              type="success"
              :icon="ShoppingCart"
              :disabled="!selectedRom || !selectedMau || bienThe?.soLuong <= 0"
              class="cart-btn"
              @click="themVaoGio(false)"
            >
              Thêm vào giỏ hàng
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <!-- Specifications Modal -->
    <div
      v-if="showSpecModal"
      class="spec-modal-overlay"
      @click="closeSpecModal"
    >
      <div class="spec-modal" @click.stop>
        <div class="spec-modal-header">
          <h2>Thông số sản phẩm</h2>
          <br>
          <h6>{{ sanPham.tenSanPham }} {{ thongSo.rom }}</h6>
          <button @click="closeSpecModal" class="close-btn">×</button>
        </div>
        <div class="spec-modal-content" v-if="thongSo" ref="specModalContent">
          <div class="spec-tabs-wrapper">
            <div class="spec-tabs" ref="specTabsContainer">
              <div
                class="tab-item"
                :class="{ active: activeTab === 'thong-tin-hang-hoa' }"
                @click="scrollToSection('thong-tin-hang-hoa')"
                data-tab="thong-tin-hang-hoa"
              >
                Thông tin hàng hóa
              </div>
              <div
                class="tab-item"
                :class="{ active: activeTab === 'thiet-ke-trong-luong' }"
                @click="scrollToSection('thiet-ke-trong-luong')"
                data-tab="thiet-ke-trong-luong"
              >
                Thiết kế & Trọng lượng
              </div>
              <div
                class="tab-item"
                :class="{ active: activeTab === 'bo-xu-ly' }"
                @click="scrollToSection('bo-xu-ly')"
                data-tab="bo-xu-ly"
              >
                Bộ xử lý
              </div>
              <div
                class="tab-item"
                :class="{ active: activeTab === 'ram' }"
                @click="scrollToSection('ram')"
                data-tab="ram"
              >
                Lưu trữ
              </div>
              <div
                class="tab-item"
                :class="{ active: activeTab === 'man-hinh' }"
                @click="scrollToSection('man-hinh')"
                data-tab="man-hinh"
              >
                Màn hình
              </div>
              <div
                class="tab-item"
                :class="{ active: activeTab === 'camera' }"
                @click="scrollToSection('camera')"
                data-tab="camera"
              >
                Camera
              </div>
              <div
                class="tab-item"
                :class="{ active: activeTab === 'pin-sac' }"
                @click="scrollToSection('pin-sac')"
                data-tab="pin-sac"
              >
                Pin & Sạc
              </div>
            </div>
          </div>
          <div class="spec-details">
            <div class="spec-section" data-section="thong-tin-hang-hoa">
              <h3>Thông tin hàng hóa</h3>
              <div class="spec-row">
                <span class="spec-label">Xuất xứ</span>
                <span class="spec-value">{{ thongSo.xuatXu }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Hệ điều hành</span>
                <span class="spec-value">{{ thongSo.heDieuHanh }}</span>
              </div>
            </div>

            <div class="spec-section" data-section="thiet-ke-trong-luong">
              <h3>Thiết kế & Trọng lượng</h3>
              <div class="spec-row">
                <span class="spec-label">Kích thước</span>
                <span class="spec-value">{{ thongSo.kichThuoc }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Chất liệu kính</span>
                <span class="spec-value">{{ thongSo.chatLieuKinh }}</span>
              </div>
            </div>

            <div class="spec-section" data-section="bo-xu-ly">
              <h3>Bộ xử lý</h3>
              <div class="spec-row">
                <span class="spec-label">CPU</span>
                <span class="spec-value">{{ thongSo.cpu }}</span>
              </div>
            </div>

            <div class="spec-section" data-section="ram">
              <h3>Lưu trữ</h3>
              <div class="spec-row">
                <span class="spec-label">RAM</span>
                <span class="spec-value">{{ thongSo.ram }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">ROM</span>
                <span class="spec-value">{{ thongSo.rom }}</span>
              </div>
            </div>

            <div class="spec-section" data-section="man-hinh">
              <h3>Màn hình</h3>
              <div class="spec-row">
                <span class="spec-label">Tên màn hình</span>
                <span class="spec-value">{{ thongSo.tenManHinh }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Loại màn hình</span>
                <span class="spec-value">{{ thongSo.loaiManHinh }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Độ phân giải</span>
                <span class="spec-value">{{ thongSo.doPhanGiai }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Tần số quét</span>
                <span class="spec-value">{{ thongSo.tanSoQuet }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Độ sáng</span>
                <span class="spec-value">{{ thongSo.doSang }}</span>
              </div>
            </div>

            <div class="spec-section" data-section="camera">
              <h3>Camera</h3>
              <div class="spec-row">
                <span class="spec-label">Camera sau</span>
                <span class="spec-value">{{ thongSo.cameraSau }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Camera trước</span>
                <span class="spec-value">{{ thongSo.cameraTruoc }}</span>
              </div>
            </div>

            <div class="spec-section" data-section="pin-sac">
              <h3>Pin & Sạc</h3>
              <div class="spec-row">
                <span class="spec-label">Pin</span>
                <span class="spec-value">{{ thongSo.phienBanPin }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Công suất sạc</span>
                <span class="spec-value">{{ thongSo.congXuatSac }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Thời gian sử dụng</span>
                <span class="spec-value">{{ thongSo.thoiGianSuDung }}</span>
              </div>
              <div class="spec-row">
                <span class="spec-label">Số lần sạc tối đa</span>
                <span class="spec-value">{{ thongSo.soLanSacToiDa }}</span>
              </div>
            </div>
        </div>
      </div>
    </div>
    </div>
  </section>
</template>

<style scoped>
.product-detail {
  background: #f5f5f5;
  min-height: 100vh;
  padding: 20px 0;
}

.product-container {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  padding: 20px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto;
}

.product-image {
  flex: 0 0 55%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.main-image {
  width: 100%;
  height: 400px;
  object-fit: contain;
  border-radius: 6px;
  background: #ffffff;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
}

.main-image:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.thumbnail-list {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: center;
  padding: 10px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
  width: 100%;
}

.thumbnail {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #fff;
}

.thumbnail:hover {
  border-color: #999;
}

.thumbnail.active {
  border-color: #d70018;
  box-shadow: 0 0 0 1px #d70018;
}

/* Featured Specs Styling */
.featured-specs {
  width: 100%;
  background: #ffffff;
  border: 1px solid #e5e5e5;
  border-radius: 6px;
  padding: 20px;
  margin-top: 10px;
}

/* Featured Specs Header */
.featured-specs-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.view-specs-btn {
  background: transparent;
  border: 1px solid #d70018;
  color: #d70018;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.thong-so-btn {
  background: transparent;
  border: 1px solid #ffffff;
  color: #04b4ff;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.view-specs-btn:hover {
  background: #d70018;
  color: white;
}

.featured-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
  text-align: center;
  border-bottom: 2px solid #d70018;
  padding-bottom: 8px;
}

.featured-specs-grid {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-top: 20px;
}

.spec-item {
  flex: 1;
  text-align: center;
  background-color: #f9fafb;
  padding: 12px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.spec-icon {
  font-size: 24px;
  color: #f59e0b;
  margin-bottom: 6px;
}

.spec-label {
  font-weight: 600;
  color: #374151;
  margin-bottom: 4px;
}

.spec-value {
  font-size: 14px;
  color: #6b7280;
}

.product-right {
  flex: 0 0 calc(45% - 30px);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.product-info {
  background: #ffffff;
  padding: 20px;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
}

.product-info h1 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #333;
  font-weight: 600;
  line-height: 1.4;
}

.price {
  font-size: 28px;
  color: #d70018;
  font-weight: 700;
  margin: 15px 0;
  display: inline-block;
}

.danhGia {
  background: transparent;
  border: 1px solid #ffffff;
  color: #04b4ff;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  text-decoration: none;
}

/* Banner Tích Điểm Styling */
.banner-tichDiem {
  margin-top: 15px;
  width: 100%;
}

.tich-diem-img {
  width: 100%;
  height: auto; /* tự co chiều cao */
  max-height: 80px; /* không vượt quá chiều cao này */
  border-radius: 6px;
  border: 1px solid #e5e5e5;
  transition: all 0.3s ease;
  cursor: pointer;
}

.tich-diem-img:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: scale(1.02);
}

.diemTichLuy {
  margin-top: 8px;
  font-size: 14px;
  color: #f59e0b; /* màu vàng cam */
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.xu-icon {
  width: 16px;
  height: 16px;
  object-fit: contain;
}

/* Options styling */
.options {
  padding: 5px 0;
}

.options h3 {
  font-size: 16px;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.option-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

::v-deep(.option-list .el-radio-button) {
  margin: 5px;
}

::v-deep(.option-list .el-radio-button__inner) {
  border-radius: 10px !important;
  padding: 8px 16px;
  min-width: 70px;
  font-size: 14px;
  text-align: center;
  background-color: #ffffff;
  border: 1px solid #d9d9d9;
  transition: all 0.2s ease;
  color: #333;
}

::v-deep(.option-list .el-radio-button__inner:hover) {
  background-color: #f0f8ff;
  border-color: #1890ff;
}

::v-deep(.el-radio-button.is-active .el-radio-button__inner) {
  background-color: #1890ff;
  color: #fff;
  border-color: #1890ff;
}

/* Quantity styles */
.quantity-selector {
  margin-top: 15px;
  background: transparent;
  border: none;
  padding: 12px 0;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 2px;
  max-width: 140px;
  border: 2px solid #e8e8e8;
  border-radius: 12px;
  overflow: hidden;
  background: #ffffff;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.12);
  padding: 3px;
}

.quantity-control button {
  width: 38px;
  height: 38px;
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(145deg, #f1f3f4 0%, #e8eaed 100%);
  color: #333;
  border: none;
  cursor: pointer;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.quantity-control button:hover:not(:disabled) {
  background: linear-gradient(145deg, #1976d2 0%, #2196f3 100%);
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(25, 118, 210, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.quantity-control button:active:not(:disabled) {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.3);
}

.quantity-control button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  background: #f5f5f5;
  transform: none;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.quantity-control input {
  width: 50px;
  height: 38px;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  border: none;
  background: transparent;
  color: #333;
  outline: none;
  margin: 0;
}

.quantity-control input:focus {
  background: rgba(25, 118, 210, 0.05);
  border-radius: 6px;
  color: #1976d2;
}

.quantity-control input::-webkit-outer-spin-button,
.quantity-control input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Button group */
.button-group {
  display: flex;
  gap: 12px;
  margin-top: 25px;
}

.buy-btn,
.cart-btn {
  flex: 1;
  font-size: 14px;
  padding: 12px 20px;
  text-align: center;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.2s ease;
  border: none;
  cursor: pointer;
  height: 44px;
}

.buy-btn {
  background: #d70018;
  color: white;
}

.buy-btn:hover:not(:disabled) {
  background: #b8001a;
}

.cart-btn {
  background: #1890ff;
  color: white;
}

.cart-btn:hover:not(:disabled) {
  background: #096dd9;
}

.buy-btn:disabled,
.cart-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Modal Styles */
.spec-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  display: flex;
  justify-content: flex-end;
  animation: fadeIn 0.3s ease;
}

.spec-modal {
  width: 500px;
  height: 100vh;
  background: white;
  box-shadow: -2px 0 10px rgba(0, 0, 0, 0.1);
  animation: slideInRight 0.3s ease;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.spec-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e5e5e5;
  background: white;
  z-index: 20;
  flex-shrink: 0;
}

.spec-modal-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.close-btn:hover {
  background: #f5f5f5;
  color: #333;
}

.spec-modal-content {
  flex: 1;
  overflow-y: auto;
  scroll-behavior: smooth;
  display: flex;
  flex-direction: column;
}

.spec-tabs-wrapper {
  position: sticky;
  top: 0;
  z-index: 15;
  background: white;
  border-bottom: 1px solid #e5e5e5;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.spec-tabs {
  display: flex;
  overflow-x: auto;
  background: #f8f9fa;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.spec-tabs::-webkit-scrollbar {
  display: none;
}

.tab-item {
  padding: 12px 16px;
  font-size: 13px;
  font-weight: 500;
  color: #666;
  cursor: pointer;
  white-space: nowrap;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.tab-item.active {
  color: #d70018;
  border-bottom-color: #d70018;
  background: white;
  font-weight: 600;
  position: relative;
}

.tab-item.active::after {
  content: "";
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 2px;
  background: #d70018;
}

.tab-item:hover {
  color: #d70018;
}

.spec-details {
  padding: 20px;
  flex: 1;
}

.spec-section {
  margin-bottom: 25px;
  scroll-margin-top: 100px; /* Offset for sticky header */
}

.spec-section h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  padding-bottom: 6px;
  border-bottom: 1px solid #e5e5e5;
}

.spec-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}

.spec-row:last-child {
  border-bottom: none;
}

.spec-label {
  font-weight: 500;
  color: #555;
  font-size: 14px;
  flex: 0 0 40%;
}

.spec-value {
  font-weight: 400;
  color: #333;
  font-size: 14px;
  text-align: right;
  flex: 1;
}

/* Animations */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideInRight {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

/* Mobile responsive for modal */
@media (max-width: 768px) {
  .spec-modal {
    width: 100vw;
  }

  .spec-tabs {
    padding: 0 10px;
  }

  .tab-item {
    padding: 10px 12px;
    font-size: 12px;
  }

  .spec-details {
    padding: 15px;
  }

  .spec-row {
    flex-direction: column;
    gap: 4px;
    align-items: flex-start;
  }

  .spec-label {
    flex: none;
  }

  .spec-value {
    text-align: left;
  }
}
</style>
