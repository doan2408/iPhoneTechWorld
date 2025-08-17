<script type="text/javascript">
var Tawk_API = Tawk_API || {}, Tawk_LoadStart = new Date();
(function () {
  var s1 = document.createElement("script"), s0 = document.getElementsByTagName("script")[0];
  s1.async = true;
  s1.src = 'https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5';
  s1.charset = 'UTF-8';
  s1.setAttribute('crossorigin', '*');
  s0.parentNode.insertBefore(s1, s0);
})();
</script>
<script setup>
import { ref, onMounted, watch, nextTick, reactive } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import {
  detailSanPham,
  getChiTietBienThe,
  getThongSo,
  getListAnhByMau,
} from "@/Service/ClientService/Products/ProductClientService";
import { checkExistsWishList, addNewWishList, deleteWishList } from "@/Service/ClientService/WishList/WishListService";
import { cartService } from "@/service/ClientService/GioHang/GioHangClientService";
import { DanhGiaSanPhamClientService } from "@/Service/ClientService/DanhGiaSanPham/DanhGiaSanPhamClientService";
import { PhanHoiDanhGiaClientService } from "@/Service/ClientService/PhanHoiDanhGia/PhanHoiDanhGiaClientService";
import { ShoppingCart } from "@element-plus/icons-vue";
import headerState from "@/components/Client/modules/headerState";
import axios from "axios";
import { useToast } from "vue-toastification";
const toast = useToast()
const count = ref(0);
const store = useStore();
import { ElMessage, ElRate, ElUpload } from "element-plus";
import { ElPagination } from "element-plus";

const loadTawkTo = () => {
  if (window.Tawk_API) return;
  const s1 = document.createElement("script");
  s1.async = true;
  s1.src = 'https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5';
  s1.charset = 'UTF-8';
  s1.setAttribute('crossorigin', '*');
  const s0 = document.getElementsByTagName("script")[0];
  s0.parentNode.insertBefore(s1, s0);
  window.Tawk_API = window.Tawk_API || {};
  window.Tawk_LoadStart = new Date();
};


if (!store.hasModule('headerState')) {
  store.registerModule('headerState', headerState)
}


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
const selectedImage = ref(null);
const sanPham = ref(null);
const selectedRom = ref(null);
const selectedMau = ref(null);
const bienThe = ref(null); //anh, gia, soLuongcon
const thongSo = ref(null); //thong so ky thuat specifications
const quantity = ref(1);

const rating = ref(0);
const comment = ref("");
const uploadedImages = ref([]);
const reviews = ref([]);
const averageRating = ref(0);
const starDistribution = reactive({});
const currentPage = ref(1);
const pageSize = ref(10);
const totalReviews = ref(0);
const selectedStarFilter = ref(0);
const hasMediaFilter = ref(false);
const isLoading = ref(false);
const phanHoiByDanhGia = ref({});

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

const fetchPhanHoi = async (idDanhGia) => {
  try {
    const phanHoi = await PhanHoiDanhGiaClientService.layPhanHoiTheoDanhGia(idDanhGia);
    phanHoiByDanhGia.value[idDanhGia] = phanHoi;
  } catch (error) {
    console.error(`Lỗi khi lấy phản hồi cho đánh giá ${idDanhGia}:`, error);
    ElMessage.error("Không thể tải phản hồi!");
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
    await checkIfFavorite();
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
    console.log('hehe', user.id)
    const res = await getChiTietBienThe(idSanPham, selectedMau.value, selectedRom.value, user.id);
    bienThe.value = res;
    if (res.hinhAnh?.length > 0) selectedImage.value = res.hinhAnh[0];
    await fetchReviews();
    await fetchAverageRating();
    await fetchStarDistribution();
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
  await fetchStarDistribution();
};

const themVaoGio = async (buy) => {
  try {
    if (quantity.value < 0) {
      ElMessage.error("Số lượng phải lớn hơn 0!");
      return;
    }
    const soLuongTonKho = bienThe.value.soLuong;
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
    console.log(soLuongTonKho);

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

const reviewsSection = ref(null)

const scrollToReviews = async () => {
  await nextTick()
  reviewsSection.value?.scrollIntoView({ behavior: 'smooth' })
}

const fetchReviews = async () => {
  try {
    isLoading.value = true;
    const response = await DanhGiaSanPhamClientService.layDanhGiaTheoSanPham(
      bienThe.value.idSpct,
      currentPage.value - 1,
      pageSize.value,
      selectedStarFilter.value === 0 ? null : selectedStarFilter.value,
      hasMediaFilter.value ? true : null,
      user?.id
    );

    console.log("Đánh giá111:", response);

    reviews.value = currentPage.value === 1 ? (response.content || response) : [...reviews.value, ...(response.content || response)];
    totalReviews.value = response.totalElements || response.length;

    for (const review of reviews.value) {
      await fetchPhanHoi(review.idDanhGia);
    }
  } catch (error) {
    console.error("Lỗi khi lấy đánh giá:", error);
    ElMessage.error("Không thể tải đánh giá!");
  } finally {
    isLoading.value = false;
  }
};

const fetchAverageRating = async () => {
  try {
    const avg = await DanhGiaSanPhamClientService.tinhDiemTrungBinhSanPhamSpct(bienThe.value.idSpct);
    averageRating.value = avg || 0;
  } catch (error) {
    console.error("Lỗi khi lấy điểm trung bình:", error);
  }
};

const fetchStarDistribution = async () => {
  if (!bienThe.value?.idSpct) return;
  try {
    const distribution = await DanhGiaSanPhamClientService.thongKeSoSaoSanPham(bienThe.value.idSpct);
    // ép về reactive object để Vue track thay đổi
    Object.keys(starDistribution).forEach(k => delete starDistribution[k]); // xóa cũ
    Object.entries(distribution).forEach(([star, count]) => {
      starDistribution[star] = count;
    });

    totalReviews.value = Object.values(distribution).reduce((a, b) => a + b, 0);
    const totalScore = Object.entries(distribution).reduce((sum, [star, count]) => sum + star * count, 0);
    averageRating.value = totalReviews.value ? totalScore / totalReviews.value : 0;
  } catch (error) {
    console.error("❌ Lỗi khi lấy thống kê sao:", error);
  }
};

const filterReviewsByStar = async (star) => {
  selectedStarFilter.value = star;
  currentPage.value = 1;
  await fetchReviews();
};

const filterReviewsByMedia = async () => {
  currentPage.value = 1;
  await fetchReviews();
};

const loadMoreReviews = async () => {
  currentPage.value++;
  await fetchReviews();
};

const setupInfiniteScroll = () => {
  const observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting && reviews.value.length < totalReviews.value && !isLoading.value) {
        loadMoreReviews();
      }
    },
    { threshold: 0.1 }
  );

  nextTick(() => {
    const loadMoreElement = document.querySelector('.reviews-list');
    if (loadMoreElement) observer.observe(loadMoreElement);
  });
};

const handlePageChange = (page) => {
  currentPage.value = page;
  fetchReviews();
};


watch([selectedMau], () => {
  fetchChiTietBienThe();
  fetchListAnhByMau();
  checkIfFavorite();
});

watch([selectedRom], () => {
  fetchChiTietBienThe();
  fetchThongSo(); // chỉ khi ROM đổi mới gọi
  checkIfFavorite();
});

onMounted(() => {
  window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
  fetchSanPhamDetail();
  setupInfiniteScroll();
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
const isFavorite = ref(false)
const checkIfFavorite = async () => {
  try {
    const res = await checkExistsWishList(idSanPham, selectedRom.value, selectedMau.value);
    isFavorite.value = res.data
  } catch (err) {
    console.error('Lỗi kiểm tra wishlist:', err)
  }
}

const toggleWishlist = async () => {
  try {
    if (isFavorite.value) {
      await deleteWishList(idSanPham, selectedRom.value, selectedMau.value, null)
      toast.success("Bỏ yêu thích thành công")
    } else {
      await addNewWishList(idSanPham, selectedRom.value, selectedMau.value, null)
      toast.success("Thêm thành công vào yêu thích")
    }
    isFavorite.value = !isFavorite.value
  } catch (err) {
    console.error('Lỗi toggle wishlist:', err)
  }
}
// onMounted(checkIfFavorite)
</script>

<template>
  <section class="product-detail">
    <div class="product-container">
      <!-- Cột trái: Hình ảnh (55%) -->
      <div class="product-image">
        <img :src="selectedImage || bienThe?.hinhAnh?.[0] || '/img/no-image.png'" alt="Hình ảnh sản phẩm"
          class="main-image" />
        <!-- Danh sách ảnh thu nhỏ -->

        <div class="thumbnail-list" v-if="bienThe?.hinhAnh?.length > 0">
          <img v-for="(img, index) in bienThe.hinhAnh" :key="index" :src="img" class="thumbnail"
            :class="{ active: img === selectedImage }" @click="selectedImage = img" />
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
          <h1>{{ sanPham?.tenSanPham }} - {{ sanPham?.maXuatXu }}</h1>
          <a href="#" class="danhGia" @click.prevent="scrollToReviews">Đánh giá</a> |
          <button @click="openSpecModal" class="thong-so-btn">
            Xem thông số
          </button> |
          <button @click="toggleWishlist" :class="['wishlist-btn', isFavorite ? 'active' : '']"
            :title="isFavorite ? 'Bỏ khỏi yêu thích' : 'Thêm vào yêu thích'">
            <span v-if="isFavorite">❤️ Bỏ khỏi yêu thích</span>
            <span v-else>🤍 Thêm vào yêu thích</span>
          </button>


          <!-- Màu sắc -->
          <div class="options">
            <h3>Chọn màu:</h3>
            <div class="option-list">
              <el-radio-group v-model="selectedMau">
                <el-radio-button v-for="m in sanPham?.mau" :key="m.id" :label="m.id">
                  {{ m.ten }}
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="options">
            <h3>Chọn ROM:</h3>
            <div class="option-list">
              <el-radio-group v-model="selectedRom">
                <el-radio-button v-for="r in sanPham?.rom" :key="r.id" :label="r.id">
                  {{ r.ten }}
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="options" v-if="bienThe">
            <h3>Số lượng còn: {{ bienThe.soLuong ?? 0 }}</h3>
          </div>

          <p class="price">
            <span v-if="bienThe?.giaTruocKhuyenMai && bienThe.giaTruocKhuyenMai !== bienThe.giaBan" class="old-price">
              {{ formatPrice(bienThe.giaTruocKhuyenMai) }}
            </span>
            <span class="current-price">
              {{ formatPrice(bienThe?.giaBan) }}
            </span>
            <span v-if="bienThe?.giaTruocKhuyenMai && bienThe.giaTruocKhuyenMai > bienThe.giaBan"
              class="discount-badge">
              -{{ Math.round(((bienThe.giaTruocKhuyenMai - bienThe.giaBan) / bienThe.giaTruocKhuyenMai) * 100) }}%
            </span>
          </p>

          <!-- Chọn số lượng -->
          <div class="options quantity-selector" v-if="bienThe?.soLuong > 0">
            <h3>Chọn số lượng:</h3>
            <div class="quantity-control">
              <button @click="decreaseQty" :disabled="quantity <= 1">-</button>

              <input type="number" v-model="quantity" min="1" :max="bienThe.soLuong" />
              <button @click="increaseQty" :disabled="quantity >= bienThe.soLuong">
                +
              </button>
            </div>
          </div>
          <div class="banner-tichDiem">
            <router-link to="/client/doiDiem">
              <img src="/src/components/images/bannerTichDiem.jpg" alt="Banner tích điểm" class="tich-diem-img" />
            </router-link>
          </div>

          <div class="diemTichLuy">
            <i class="fas fa-coins xu-icon"></i>
            + {{ formatDiemThuong(bienThe?.giaBan) }} điểm thưởng
          </div>

          <!-- Hành động -->
          <div class="button-group">
            <el-button type="primary" :disabled="!selectedRom || !selectedMau || bienThe?.soLuong <= 0" class="buy-btn"
              @click="themVaoGio(true)">
              Mua ngay
            </el-button>
            <el-button type="success" :icon="ShoppingCart"
              :disabled="!selectedRom || !selectedMau || bienThe?.soLuong <= 0" class="cart-btn"
              @click="themVaoGio(false)">
              Thêm vào giỏ hàng
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <!-- Specifications Modal -->
    <div v-if="showSpecModal" class="spec-modal-overlay" @click="closeSpecModal">
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
              <div class="tab-item" :class="{ active: activeTab === 'thong-tin-hang-hoa' }"
                @click="scrollToSection('thong-tin-hang-hoa')" data-tab="thong-tin-hang-hoa">
                Thông tin hàng hóa
              </div>
              <div class="tab-item" :class="{ active: activeTab === 'thiet-ke-trong-luong' }"
                @click="scrollToSection('thiet-ke-trong-luong')" data-tab="thiet-ke-trong-luong">
                Thiết kế & Trọng lượng
              </div>
              <div class="tab-item" :class="{ active: activeTab === 'bo-xu-ly' }" @click="scrollToSection('bo-xu-ly')"
                data-tab="bo-xu-ly">
                Bộ xử lý
              </div>
              <div class="tab-item" :class="{ active: activeTab === 'ram' }" @click="scrollToSection('ram')"
                data-tab="ram">
                Lưu trữ
              </div>
              <div class="tab-item" :class="{ active: activeTab === 'man-hinh' }" @click="scrollToSection('man-hinh')"
                data-tab="man-hinh">
                Màn hình
              </div>
              <div class="tab-item" :class="{ active: activeTab === 'camera' }" @click="scrollToSection('camera')"
                data-tab="camera">
                Camera
              </div>
              <div class="tab-item" :class="{ active: activeTab === 'pin-sac' }" @click="scrollToSection('pin-sac')"
                data-tab="pin-sac">
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
    <div class="product-reviews" ref="reviewsSection">
      <h2>Đánh giá sản phẩm</h2>
      <div class="rating-summary">
        <h3>Điểm trung bình: {{ averageRating.toFixed(1) }} / 5</h3>
        <el-rate v-model="averageRating" disabled allow-half />
        <div class="star-distribution">
          <div v-for="star in [5, 4, 3, 2, 1]" :key="star" class="star-bar">
            <span>{{ star }} sao:</span>
            <div class="progress-bar">
              <div class="progress" :style="{
                width: totalReviews === 0
                  ? '100%'
                  : ((starDistribution[star] || 0) / totalReviews * 100) + '%'
              }"></div>
            </div>
            <span>{{ starDistribution[star] || 0 }} đánh giá</span>
          </div>
        </div>

      </div>

      <div class="review-filters">
        <el-button v-for="star in [0, 5, 4, 3, 2, 1]" :key="star" @click="filterReviewsByStar(star)"
          :type="selectedStarFilter === star ? 'primary' : 'default'">
          {{ star === 0 ? 'Tất cả' : `${star} sao` }}
        </el-button>
        <el-button @click="hasMediaFilter = !hasMediaFilter; filterReviewsByMedia()"
          :type="hasMediaFilter ? 'primary' : 'default'">
          Có hình ảnh/video
        </el-button>
      </div>

      <div class="reviews-list">
        <div class="review-item" v-for="review in reviews" :key="review.idDanhGia">
          <div class="review-header">
            <span class="review-user">{{ review.anonymous ? 'Người mua' : review.tenKhachHang }}</span>
            <span v-if="review.trangThaiDanhGia === 'PENDING' && review.idKhachHang === user.id" class="pending-label">
              Chờ duyệt
            </span>
            <span class="purchased-label" v-if="review.idChiTietHoaDon">Đã mua hàng</span>
            <el-rate v-model="review.soSao" disabled />
            <span class="review-date">{{ new Date(review.ngayDanhGia).toLocaleDateString("vi-VN") }}</span>
          </div>
          <p class="review-comment">{{ review.noiDung }}</p>
          <div class="review-media" v-if="review.mediaUrls?.length">
            <div class="review-images"
              v-if="review.mediaUrls?.filter(url => url.match(/\.(png|jpg|jpeg|gif)$/i)).length">
              <img v-for="(img, index) in review.mediaUrls.filter(url => url.match(/\.(png|jpg|jpeg|gif)$/i))"
                :key="index" :src="img" alt="Hình ảnh đánh giá" class="review-image"
                @error="console.log('Lỗi tải ảnh:', img)" />
            </div>
            <video v-for="(video, index) in review.mediaUrls.filter(url => url.match(/\.(mp4|mov|avi|webm)$/i))"
              :key="'video-' + index" :src="video" controls class="review-video"
              @error="console.log('Lỗi tải video:', video)"></video>
          </div>
          <div class="review-responses">
            <el-button v-if="!phanHoiByDanhGia[review.idDanhGia]" @click="fetchPhanHoi(review.idDanhGia)" size="small">
              Tải phản hồi
            </el-button>
            <div v-else-if="phanHoiByDanhGia[review.idDanhGia]?.length">
              <h4>Phản hồi từ cửa hàng:</h4>
              <div v-for="phanHoi in phanHoiByDanhGia[review.idDanhGia]" :key="phanHoi.idPhanHoi" class="response-item">
                <p class="response-content">{{ phanHoi.noiDungPhanHoi }}</p>
                <span class="response-date">{{ new Date(phanHoi.ngayPhanHoi).toLocaleDateString("vi-VN") }}</span>
              </div>
            </div>
            <div v-else class="no-response">
              Chưa có phản hồi từ cửa hàng.
            </div>
          </div>
        </div>
        <div v-if="isLoading" class="loading">Đang tải thêm...</div>
      </div>

      <div class="load-more" v-if="reviews.length < totalReviews && !isLoading">
        <el-button type="primary" @click="loadMoreReviews">Xem thêm</el-button>
      </div>

      <el-pagination v-if="totalReviews > pageSize" :current-page="currentPage" :page-size="pageSize"
        :total="totalReviews" layout="prev, pager, next" @current-change="handlePageChange" />
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
  /* border-radius: 8px; */
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

.product-info h1 {
  font-size: 24px;
  margin-bottom: 10px;
  color: #333;
  font-weight: 600;
  line-height: 1.4;
}

.price {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 15px 0;
  flex-wrap: wrap;
}

.old-price {
  font-size: 18px;
  color: #999;
  text-decoration: line-through;
}

.current-price {
  font-size: 28px;
  color: #d70018;
  font-weight: 700;
}

.discount-badge {
  background: #ffeee8;
  color: #d70018;
  font-size: 14px;
  font-weight: 600;
  padding: 3px 8px;
  border-radius: 4px;
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
  height: auto;
  /* tự co chiều cao */
  max-height: 80px;
  /* không vượt quá chiều cao này */
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
  color: #f59e0b;
  /* màu vàng cam */
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
  background: linear-gradient(45deg, #1d37fd, #76e6dc) !important;
  color: #fff;
  border-color: #1890ff;
}

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
  background: linear-gradient(90deg, #ff001e, #fdf512) !important;
  color: white;
}

.buy-btn:hover:not(:disabled) {
  background: #b8001a;
}

.cart-btn {
  background: linear-gradient(45deg, #1d37fd, #76e6dc) !important;
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
  scroll-margin-top: 100px;
  /* Offset for sticky header */
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
.options:has(.quantity-selector) h3 {
  color: #52c41a;
  font-weight: 500;
}

.product-reviews {
  background: #ffffff;
  padding: 30px;
  /* border-radius: 12px; */
  border: 1px solid #e9ecef;
  margin-top: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  max-width: 1200px;
  margin: 0 auto;
}

.product-reviews h2 {
  margin-bottom: 25px;
  font-size: 24px;
  color: #1a1a1a;
  font-weight: 700;
  border-bottom: 2px solid #1890ff;
  padding-bottom: 12px;
  letter-spacing: 0.5px;
}

.rating-summary {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  transition: all 0.3s ease;
}

.rating-summary h3 {
  font-size: 20px;
  color: #2c3e50;
  margin-bottom: 12px;
  font-weight: 600;
}

.star-distribution {
  margin-top: 20px;
}

.star-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #34495e;
}

.progress-bar {
  flex: 1;
  height: 12px;
  background: #e9ecef;
  border-radius: 6px;
  overflow: hidden;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.progress {
  height: 100%;
  background: linear-gradient(90deg, #1890ff, #40c4ff);
  transition: width 0.5s ease-in-out;
}

.review-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 25px;
  padding: 10px;
  background: #f1f3f5;
  border-radius: 8px;
}

.review-filters .el-button {
  border-radius: 20px;
  padding: 8px 18px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.review-filters .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.reviews-list {
  margin-bottom: 20px;
}

.review-item {
  padding: 20px;
  border-bottom: 1px solid #e9ecef;
  background: #ffffff;
  border-radius: 8px;
  margin-bottom: 15px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.review-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.review-header {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 15px;
  margin-bottom: 12px;
}

.review-user {
  font-weight: 600;
  color: #2c3e50;
  font-size: 16px;
}

.purchased-label {
  background: linear-gradient(90deg, #e7f3ff, #d0e6ff);
  color: #1890ff;
  padding: 5px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.review-date {
  color: #7f8c8d;
  font-size: 13px;
  margin-left: auto;
}

.review-comment {
  color: #34495e;
  line-height: 1.7;
  margin-bottom: 15px;
  font-size: 15px;
}

.review-media {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 15px;
}

.review-images {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.review-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #dee2e6;
  transition: all 0.3s ease;
}

.review-image:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.review-video {
  width: 300px;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.review-responses {
  margin-top: 20px;
  padding: 15px;
  background: #f9fbfc;
  border-radius: 8px;
  border-left: 4px solid #1890ff;
  transition: all 0.3s ease;
}

.review-responses h4 {
  font-size: 16px;
  color: #2c3e50;
  margin-bottom: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.review-responses h4::before {
  content: '\f3e5';
  font-family: 'Font Awesome 5 Free';
  font-weight: 900;
  color: #1890ff;
  font-size: 14px;
}

.response-item {
  margin-bottom: 15px;
  padding: 10px;
  background: #ffffff;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.response-content {
  color: #34495e;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 8px;
}

.response-date {
  font-size: 12px;
  color: #95a5a6;
  display: block;
}

.no-response {
  color: #7f8c8d;
  font-size: 14px;
  font-style: italic;
  padding: 10px;
  background: #f1f3f5;
  border-radius: 6px;
}

.load-more {
  text-align: center;
  margin-top: 25px;
}

.load-more .el-button {
  border-radius: 20px;
  padding: 12px 35px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.load-more .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.loading {
  text-align: center;
  padding: 20px;
  color: #7f8c8d;
  font-size: 16px;
  font-weight: 500;
}

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

  .product-reviews {
    padding: 20px;
  }

  .review-image {
    width: 80px;
    height: 80px;
  }

  .review-video {
    width: 100%;
  }

  .star-bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .progress-bar {
    width: 100%;
  }
}

.wishlist-btn {
  border: none;
  background: transparent;
  font-size: 14px;
  cursor: pointer;
  transition: transform 0.2s;
  color: #555;
}

.wishlist-btn.active {
  color: red;
}

.wishlist-btn:hover {
  transform: scale(1.05);
}

.wishlist-btn.active {
  color: red;
}

@media (max-width: 480px) {
  .product-container {
    padding: 10px;
  }

  .product-info {
    padding: 12px;
  }

  .product-specs {
    padding: 12px;
  }

  .options {
    padding: 8px 0;
  }

  .option-list {
    gap: 6px;
  }

  ::v-deep(.option-list .el-radio-button__inner) {
    padding: 6px 12px;
    font-size: 13px;
    min-width: 60px;
  }

  .review-item {
    padding: 15px;
  }

  .review-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .review-date {
    margin-left: 0;
  }
}
</style>
