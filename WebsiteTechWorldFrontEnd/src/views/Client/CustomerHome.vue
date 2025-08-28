<!--Start of Tawk.to Script-->
<script type="text/javascript">
var Tawk_API = Tawk_API || {},
  Tawk_LoadStart = new Date();
(function () {
  var s1 = document.createElement("script"),
    s0 = document.getElementsByTagName("script")[0];
  s1.async = true;
  s1.src = "https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5";
  s1.charset = "UTF-8";
  s1.setAttribute("crossorigin", "*");
  s0.parentNode.insertBefore(s1, s0);
})();
</script>
<!--End of Tawk.to Script-->

<script setup>
import { ref, computed, onMounted, watch } from "vue";
import Banner from "@/components/Client/Banner.vue";
import {
  getAllSanPham,
  getLoai,
} from "@/Service/ClientService/Products/ProductClientService";
import { DanhGiaSanPhamClientService } from "@/Service/ClientService/DanhGiaSanPham/DanhGiaSanPhamClientService";
import {
  PriceTag,
  Search,
  Delete,
  Menu,
  ArrowDown,
  Sort,
  View,
  Plus,
} from "@element-plus/icons-vue";
import { h } from "vue";
import CompareBar from "./Compare/CompareBar.vue";
import { useToast } from "vue-toastification";

const showClearFilter = ref(false);
const filterKeyword = ref("");
const selectedLoai = ref("");
const priceRange = ref([0, 50000000]); // default range 0 - 50 millions
const currentPage = ref(1);
const pageSize = 16;
const totalProducts = ref(0);
const selectedSort = ref("");
// Product data
const products = ref([]);
const toast = useToast();

// Debounce timer
let debounceTimer = null;

// Price range configuration
const minPrice = 0;
const maxPrice = 50000000;
const priceStep = 1000000;

// Hàm nhúng Tawk.to
const loadTawkTo = () => {
  // Kiểm tra xem Tawk.to đã được tải chưa để tránh tải lại
  if (window.Tawk_API) return;

  // Tạo script element
  const s1 = document.createElement("script");
  s1.async = true;
  s1.src = "https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5";
  s1.charset = "UTF-8";
  s1.setAttribute("crossorigin", "*");

  // Thêm script vào DOM
  const s0 = document.getElementsByTagName("script")[0];
  s0.parentNode.insertBefore(s1, s0);

  // Khởi tạo Tawk_API
  window.Tawk_API = window.Tawk_API || {};
  window.Tawk_LoadStart = new Date();
};

const fetchProducts = async () => {
  try {
    const params = {
      page: currentPage.value - 1,
      tenSanPham: filterKeyword.value || null,
      idLoai: selectedLoai.value || null,
      giaMin: priceRange.value[0],
      giaMax: priceRange.value[1],
      sort: selectedSort.value || null,
    };

    const data = await getAllSanPham(params);
    console.log(data, "sai mẹ r");
    products.value = await Promise.all(
      (data.content || data).map(async (item) => {
        try {
          const avgRating =
            await DanhGiaSanPhamClientService.tinhDiemTrungBinhSanPham(item.id);
          console.log("tao cường", avgRating);
          return { ...item, averageRating: avgRating || 0 };
        } catch (error) {
          console.error(
            `Lỗi khi lấy điểm trung bình cho sản phẩm ${item.id}:`,
            error
          );
          return { ...item, averageRating: 0 };
        }
      })
    );
    totalProducts.value = data.totalElements || data.length;
    showClearFilter.value =
      filterKeyword.value ||
      selectedLoai.value ||
      priceRange.value[0] !== minPrice ||
      priceRange.value[1] !== maxPrice;
  } catch (error) {
    console.error("Lỗi khi load sản phẩm:", error);
  }
};

// Debounced search function
const debouncedFetchProducts = () => {
  if (debounceTimer) {
    clearTimeout(debounceTimer);
  }
  debounceTimer = setTimeout(() => {
    currentPage.value = 1;
    fetchProducts();
  }, 500); // 500ms delay
};

// Available product types
const loaiOptions = ref([{ label: "Tất cả", value: "" }]);

const hasProduct = computed(() => products.value.length > 0);

const fetchLoai = async () => {
  try {
    const data = await getLoai();
    loaiOptions.value = [
      { label: "Tất cả", value: "" },
      ...data.map((item) => ({
        label: item.tenLoai,
        value: item.idLoai,
      })),
    ];
  } catch (error) {
    console.error("Lỗi khi lấy danh sách loại", error);
  }
};

const clearFilters = () => {
  if (debounceTimer) {
    clearTimeout(debounceTimer);
  }
  filterKeyword.value = "";
  selectedLoai.value = "";
  priceRange.value = [minPrice, maxPrice];
  showClearFilter.value = false;
  currentPage.value = 1;
  fetchProducts();
};

const formatPrice = (val) => {
  return new Intl.NumberFormat("vi-VN").format(val) + "₫";
};

const formatPriceShort = (val) => {
  if (val >= 1000000) {
    return (val / 1000000).toFixed(val % 1000000 === 0 ? 0 : 1) + "tr";
  }
  return new Intl.NumberFormat("vi-VN").format(val) + "₫";
};

const handlePageChange = (newPage) => {
  currentPage.value = newPage;
  fetchProducts();
};

// Quick price filter buttons
const quickPriceFilters = [
  { label: "Dưới 5tr", range: [0, 5000000] },
  { label: "5tr - 10tr", range: [5000000, 10000000] },
  { label: "10tr - 20tr", range: [10000000, 20000000] },
  { label: "20tr - 30tr", range: [20000000, 30000000] },
  { label: "Trên 30tr", range: [30000000, 50000000] },
];

const applyQuickPriceFilter = (range) => {
  priceRange.value = [...range];
};

const sortLabel = computed(() => {
  if (selectedSort.value === "") return "Sắp xếp giá";
  if (selectedSort.value === "giaAsc") return "Giá tăng dần";
  if (selectedSort.value === "giaDesc") return "Giá giảm dần";
});

// Watch for changes in filters
watch(filterKeyword, () => {
  debouncedFetchProducts();
});

watch(
  [selectedLoai, priceRange],
  () => {
    currentPage.value = 1;
    fetchProducts();
  },
  { deep: true }
);

watch(currentPage, () => {
  fetchProducts();
});

onMounted(() => {
  fetchLoai();
  fetchProducts();
  loadTawkTo(); // Tải Tawk.to khi component được gắn
});
const compareList = ref([]);
const addToCompare = (product) => {
  if (compareList.value.length >= 3) {
    toast.warning("Bạn chỉ có thể so sánh tối đa 3 sản phẩm");
    return;
  }

  if (!compareList.value.find((p) => p.id === product.id)) {
    compareList.value.push(product);
  }
};
const isInCompareList = (product) => {
  return compareList.value.some((p) => p.id === product.id);
};
const removeCompare = (product) => {
  compareList.value = compareList.value.filter((p) => p.id !== product.id);
};
</script>

<template>
  <section class="modern-ecommerce">
    <Banner />

    <!-- Hero Section với Search -->
    <div class="hero-search-section">
      <div class="container">
        <div class="search-hero">
          <div class="search-content">
            <h1 class="search-title">
              <span class="gradient-text">Khám phá</span> sản phẩm yêu thích
            </h1>
            <p class="search-subtitle">
              Tìm kiếm trong hàng nghìn sản phẩm chính hãng với giá tốt nhất
            </p>

            <div class="mega-search-bar">
              <div class="search-input-container">
                <el-input
                  v-model="filterKeyword"
                  placeholder="Tìm kiếm iPhone..."
                  class="mega-search-input"
                  size="large"
                  clearable
                >
                  <template #prefix>
                    <el-icon class="search-icon">
                      <Search />
                    </el-icon>
                  </template>
                </el-input>
              </div>

              <!-- Quick Search Tags -->
              <div class="quick-search-tags">
                <span class="tags-label">Tìm kiếm phổ biến:</span>
                <div class="tags-container">
                  <span class="quick-tag" @click="filterKeyword = 'iPhone 15'"
                    >iPhone 15</span
                  >
                  <span class="quick-tag" @click="filterKeyword = 'iPhone 16'"
                    >iPhone 16</span
                  >
                  <span class="quick-tag" @click="filterKeyword = 'iPhone 14'"
                    >iPhone 14</span
                  >
                  <span class="quick-tag" @click="filterKeyword = 'iPhone 13'"
                    >iPhone 13</span
                  >
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <div class="container">
        <!-- Filters & Results Header -->
        <div class="content-header">
          <div class="results-info">
            <h5 class="results-title">
              <span class="results-count" v-if="totalProducts > 0">{{
                totalProducts
              }}</span>
              sản phẩm
              <span v-if="filterKeyword" class="search-term"
                >cho "{{ filterKeyword }}"</span
              >
            </h5>
            <p class="results-subtitle">
              Được cập nhật {{ new Date().toLocaleDateString("vi-VN") }}
            </p>
          </div>

          <div class="view-controls">
            <div class="sort-control">
              <div class="sort-buttons">
                <button
                  :class="['sort-text-btn', { active: selectedSort === '' }]"
                  @click="
                    selectedSort = '';
                    currentPage = 1;
                    fetchProducts();
                  "
                >
                  Mặc định
                </button>
                <span class="sort-separator">•</span>
                <button
                  :class="[
                    'sort-text-btn',
                    { active: selectedSort === 'giaAsc' },
                  ]"
                  @click="
                    selectedSort = 'giaAsc';
                    currentPage = 1;
                    fetchProducts();
                  "
                >
                  Giá tăng dần
                </button>
                <span class="sort-separator">•</span>
                <button
                  :class="[
                    'sort-text-btn',
                    { active: selectedSort === 'giaDesc' },
                  ]"
                  @click="
                    selectedSort = 'giaDesc';
                    currentPage = 1;
                    fetchProducts();
                  "
                >
                  Giá giảm dần
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Content Layout -->
        <div class="content-layout">
          <!-- Sidebar Filters -->
          <aside class="filters-sidebar">
            <div class="filters-container">
              <div class="filter-header">
                <h3 class="filter-title">
                  <el-icon><Filter /></el-icon>
                  Bộ lọc
                </h3>
                <button
                  v-if="showClearFilter"
                  @click="clearFilters"
                  class="clear-all-btn"
                >
                  <el-icon><Delete /></el-icon>
                  Xóa tất cả
                </button>
              </div>

              <!-- Active Filters -->
              <div class="active-filters" v-if="showClearFilter">
                <div class="active-filters-list">
                  <div
                    class="filter-chip"
                    v-if="
                      priceRange[0] !== minPrice || priceRange[1] !== maxPrice
                    "
                  >
                    <span class="chip-label"
                      >Giá: {{ formatPriceShort(priceRange[0]) }} -
                      {{ formatPriceShort(priceRange[1]) }}</span
                    >
                    <button
                      class="chip-remove"
                      @click="priceRange = [minPrice, maxPrice]"
                    >
                      <el-icon><Close /></el-icon>
                    </button>
                  </div>

                  <div class="filter-chip" v-if="selectedLoai">
                    <span class="chip-label">{{
                      loaiOptions.find((item) => item.value === selectedLoai)
                        ?.label
                    }}</span>
                    <button class="chip-remove" @click="selectedLoai = ''">
                      <el-icon><Close /></el-icon>
                    </button>
                  </div>
                </div>
              </div>

              <!-- Price Filter -->
              <div class="filter-group">
                <div class="filter-group-header">
                  <div class="filter-icon">
                    <el-icon><PriceTag /></el-icon>
                  </div>
                  <div class="filter-info">
                    <h4 class="filter-name">Khoảng giá</h4>
                    <span class="filter-value"
                      >{{ formatPriceShort(priceRange[0]) }} -
                      {{ formatPriceShort(priceRange[1]) }}</span
                    >
                  </div>
                </div>

                <div class="filter-content">
                  <div class="price-slider-wrapper">
                    <el-slider
                      v-model="priceRange"
                      range
                      :min="minPrice"
                      :max="maxPrice"
                      :step="priceStep"
                      :format-tooltip="formatPriceShort"
                      class="modern-price-slider"
                    />
                  </div>

                  <div class="price-quick-filters">
                    <button
                      v-for="filter in quickPriceFilters"
                      :key="filter.label"
                      :class="[
                        'price-quick-btn',
                        {
                          active:
                            priceRange[0] === filter.range[0] &&
                            priceRange[1] === filter.range[1],
                        },
                      ]"
                      @click="applyQuickPriceFilter(filter.range)"
                    >
                      {{ filter.label }}
                    </button>
                  </div>
                </div>
              </div>

              <!-- Category Filter -->
              <div class="filter-group">
                <div class="filter-group-header">
                  <div class="filter-icon">
                    <el-icon><Menu /></el-icon>
                  </div>
                  <div class="filter-info">
                    <h4 class="filter-name">Danh mục</h4>
                  </div>
                </div>

                <div class="filter-content">
                  <div class="category-options">
                    <div
                      v-for="option in loaiOptions"
                      :key="option.value"
                      :class="[
                        'category-option',
                        { active: selectedLoai === option.value },
                      ]"
                      @click="selectedLoai = option.value"
                    >
                      <div class="option-content">
                        <span class="option-label">{{ option.label }}</span>
                      </div>
                      <div class="option-indicator"></div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Feature Highlights -->
              <div class="filter-highlights">
                <h4 class="highlights-title">Tại sao chọn chúng tôi?</h4>
                <div class="highlights-list">
                  <div class="highlight-item">
                    <div class="highlight-icon">🚚</div>
                    <span>Miễn phí vận chuyển</span>
                  </div>
                  <div class="highlight-item">
                    <div class="highlight-icon">🛡️</div>
                    <span>Bảo hành chính hãng</span>
                  </div>
                  <div class="highlight-item">
                    <div class="highlight-icon">⚡</div>
                    <span>Giao hàng nhanh 2h</span>
                  </div>
                </div>
              </div>
            </div>
          </aside>

          <!-- Products Grid -->
          <main class="products-main">
            <div class="products-grid" v-show="hasProduct">
              <div
                v-for="sp in products"
                :key="sp.id"
                class="product-card-modern"
              >
                <router-link
                  :to="`/client/detail/${sp.id}`"
                  class="product-link-modern"
                >
                  <div class="product-card-inner">
                    <!-- Product Image -->
                    <div class="product-image-wrapper">
                      <div class="product-image-container-modern">
                        <img
                          :src="sp.hinhAnh || '/img/no-image.png'"
                          :alt="sp.tenSanPham"
                          class="product-image-modern"
                          loading="lazy"
                        />
                      </div>
                    </div>

                    <!-- Product Info -->
                    <div class="product-info-modern">
                      <div class="product-meta">
                        <span class="product-brand">{{ sp.maXuatXu }}</span>
                        <div class="product-rating-compact">
                          <el-rate
                            v-model="sp.averageRating"
                            disabled
                            allow-half
                            size="small"
                            class="rating-stars"
                          />
                        </div>
                      </div>

                      <h3 class="product-title-modern">{{ sp.tenSanPham }}</h3>

                      <div class="product-features">
                        <span class="feature-tag">Chính hãng</span>
                        <span class="feature-tag">Bảo hành 12 tháng</span>
                      </div>

                      <div class="product-pricing">
                        <div class="price-current">
                          {{ formatPrice(sp.giaBan) }}
                        </div>
                      </div>
                    </div>

                    <!-- Product Actions -->
                    <div class="product-actions-modern">
                      <button
                        class="compare"
                        v-if="!isInCompareList(sp)"
                        @click.stop.prevent="addToCompare(sp)"
                      >
                        <el-icon><Plus /></el-icon>
                        <span>So sánh</span>
                      </button>

                      <button v-else class="compare-btn active" disabled>
                        <el-icon><Check /></el-icon>
                        <span>Đã thêm</span>
                      </button>
                    </div>
                  </div>
                </router-link>
              </div>
            </div>

            <!-- Enhanced Pagination -->
            <div
              class="pagination-wrapper"
              v-show="hasProduct && totalProducts > pageSize"
            >
              <div class="pagination-info">
                <span
                  >Hiển thị {{ (currentPage - 1) * pageSize + 1 }} -
                  {{ Math.min(currentPage * pageSize, totalProducts) }} trong
                  {{ totalProducts }} sản phẩm</span
                >
              </div>

              <el-pagination
                v-model:current-page="currentPage"
                :page-size="pageSize"
                :total="totalProducts"
                layout="prev, pager, next, jumper"
                :background="true"
                class="modern-pagination"
              />
            </div>

            <!-- No Products -->
            <div v-show="!hasProduct" class="no-products-modern">
              <div class="no-products-illustration">
                <div class="illustration-icon">📱</div>
                <div class="illustration-bg"></div>
              </div>
              <h3 class="no-products-title">Không tìm thấy sản phẩm</h3>
              <p class="no-products-subtitle">
                Hãy thử điều chỉnh bộ lọc hoặc tìm kiếm với từ khóa khác
              </p>
              <button class="reset-filters-btn" @click="clearFilters">
                <el-icon><Refresh /></el-icon>
                Đặt lại bộ lọc
              </button>
            </div>
          </main>
        </div>
      </div>
    </div>

    <!-- Trust Indicators -->
    <div class="trust-section">
      <div class="container">
        <div class="trust-indicators">
          <div class="trust-item">
            <div class="trust-icon">🛡️</div>
            <div class="trust-content">
              <h4>Sản phẩm chính hãng</h4>
              <p>100% hàng chính hãng</p>
            </div>
          </div>
          <div class="trust-item">
            <div class="trust-icon">🚚</div>
            <div class="trust-content">
              <h4>Giao hàng toàn quốc</h4>
              <p>Giao tận tay</p>
            </div>
          </div>
          <div class="trust-item">
            <div class="trust-icon">💬</div>
            <div class="trust-content">
              <h4>Hỗ trợ 24/7</h4>
              <p>Tư vấn miễn phí</p>
            </div>
          </div>
          <div class="trust-item">
            <div class="trust-icon">↩️</div>
            <div class="trust-content">
              <h4>Đổi trả dễ dàng</h4>
              <p>Đối với sản phẩm lỗi từ chúng tôi</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <CompareBar :compareList="compareList" @remove="removeCompare" />
</template>

<style scoped>
/* Modern E-commerce Layout */
.modern-ecommerce {
  background: #f8fafc;
  min-height: 100vh;
}

.container {
  max-width: 1450px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Hero Search Section */
.hero-search-section {
  margin-top: 10px;
  margin-inline: 35px;
  background: linear-gradient(
    135deg,
    #1a2a6c 0%,
    #2e8bc0 40%,
    #90e0ef 70%,
    #caf0f8 100%
  );
  background-size: 400% 400%;
  animation: gradientShift 8s ease infinite;
  padding: 80px 0;
  position: relative;
  overflow: hidden;
  border-radius: 32px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
}

@keyframes gradientShift {
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

.hero-search-section::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
      circle at 20% 80%,
      rgba(255, 255, 255, 0.15) 0%,
      transparent 60%
    ),
    radial-gradient(
      circle at 80% 20%,
      rgba(255, 255, 255, 0.12) 0%,
      transparent 60%
    ),
    radial-gradient(
      circle at 40% 40%,
      rgba(255, 255, 255, 0.08) 0%,
      transparent 50%
    );
}

.hero-search-section::after {
  content: "";
  position: absolute;
  top: -50%;
  left: -50%;
  width: 1200%;
  height: 200%;
  background: conic-gradient(
    from 0deg,
    transparent,
    rgba(255, 255, 255, 0.1),
    transparent
  );
  animation: rotate 20s linear infinite;
  pointer-events: none;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.search-hero {
  position: relative;
  z-index: 2;
}

.search-content {
  text-align: center;
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.search-title {
  font-size: 4rem;
  font-weight: 900;
  color: white;
  margin-bottom: 20px;
  line-height: 1.1;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  letter-spacing: -0.02em;
}

.gradient-text {
  background: linear-gradient(45deg, #ffd700, #ffed4e, #fff176, #ffd54f);
  background-size: 200% 200%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: textShimmer 3s ease-in-out infinite;
}

@keyframes textShimmer {
  0%,
  100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.search-subtitle {
  font-size: 1.3rem;
  color: rgba(255, 255, 255, 0.95);
  margin-bottom: 50px;
  font-weight: 500;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  line-height: 1.6;
}

/* Mega Search Bar */
.mega-search-bar {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 28px;
  padding: 20px;
  box-shadow: 0 25px 60px rgba(0, 0, 0, 0.25),
    0 0 0 1px rgba(255, 255, 255, 0.3), inset 0 1px 0 rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transform: translateY(0);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.mega-search-bar:hover {
  transform: translateY(-2px);
  box-shadow: 0 35px 80px rgba(0, 0, 0, 0.3), 0 0 0 1px rgba(255, 255, 255, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
}

.search-input-container {
  margin-bottom: 24px;
  position: relative;
}

.mega-search-input {
  border-radius: 20px;
  border: 2px solid #e2e8f0;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  background: white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.mega-search-input:focus-within {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.15),
    0 8px 25px rgba(102, 126, 234, 0.2);
  transform: translateY(-1px);
}

.mega-search-input :deep(.el-input__inner) {
  height: 64px;
  font-size: 17px;
  padding-left: 24px;
  padding-right: 160px;
  font-weight: 500;
}

.search-icon {
  color: #94a3b8;
  font-size: 22px;
  transition: color 0.3s ease;
}

.mega-search-input:focus-within .search-icon {
  color: #667eea;
}

.search-btn {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: linear-gradient(135deg, #667eea 0%, #4ba252 100%);
  color: white;
  border: none;
  padding: 8px 24px;
  border-radius: 16px;
  font-weight: 700;
  font-size: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.search-btn:hover {
  transform: translateY(-50%) scale(1.05);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.6);
  background: linear-gradient(135deg, #5a67d8 0%, #46c148 100%);
}

.search-btn:active {
  transform: translateY(-50%) scale(0.98);
}

/* Quick Search Tags */
.quick-search-tags {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  justify-content: center;
}

.tags-label {
  font-size: 15px;
  color: #64748b;
  font-weight: 700;
  white-space: nowrap;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.tags-container {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: center;
}

.quick-tag {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  color: #475569;
  padding: 8px 16px;
  border-radius: 24px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(226, 232, 240, 0.8);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;
}

.quick-tag::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.4),
    transparent
  );
  transition: left 0.5s ease;
}

.quick-tag:hover {
  background: linear-gradient(135deg, #667eea 0%, #4ba24e 100%);
  color: white;
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
  border-color: transparent;
}

.quick-tag:hover::before {
  left: 100%;
}

/* Responsive Design */
@media (max-width: 768px) {
  .search-title {
    font-size: 2.5rem;
  }

  .search-subtitle {
    font-size: 1.1rem;
  }

  .mega-search-bar {
    padding: 16px;
    margin: 0 10px;
  }

  .mega-search-input :deep(.el-input__inner) {
    height: 56px;
    font-size: 16px;
    padding-right: 140px;
  }

  .search-btn {
    padding: 6px 18px;
    font-size: 14px;
  }
}

/* Main Content */
.main-content {
  padding: 40px 0;
}

/* Content Header */
.content-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 24px 0;
  border-bottom: 1px solid #e2e8f0;
  margin-bottom: 32px;
  gap: 24px;
}

.results-info {
  flex: 1;
}

.results-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
  line-height: 1.3;
}

.results-count {
  color: #667eea;
  font-weight: 800;
}

.search-term {
  color: #64748b;
  font-weight: 600;
  font-style: italic;
}

.results-subtitle {
  font-size: 0.875rem;
  color: #64748b;
  margin: 0;
  font-weight: 500;
}

.view-controls {
  display: flex;
  align-items: center;
  gap: 16px;
}

.sort-control {
  display: flex;
  align-items: center;
}

.sort-buttons {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #f8fafc;
  padding: 6px 12px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.sort-text-btn {
  background: none;
  border: none;
  color: #64748b;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 8px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  white-space: nowrap;
}

.sort-text-btn:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

.sort-text-btn.active {
  color: #667eea;
  background: white;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.15);
  font-weight: 700;
}

.sort-separator {
  color: #cbd5e1;
  font-weight: 400;
  user-select: none;
}

@media (max-width: 768px) {
  .content-header {
    flex-direction: column;
    align-items: stretch;
    gap: 16px;
  }

  .results-title {
    font-size: 1.25rem;
  }

  .sort-buttons {
    justify-content: center;
    flex-wrap: wrap;
  }
}

.view-toggle {
  display: flex;
  background: #f1f5f9;
  padding: 4px;
  border-radius: 10px;
}

.view-btn {
  background: transparent;
  border: none;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #64748b;
}

.view-btn.active {
  background: white;
  color: #6dea66;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Content Layout */
.content-layout {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 32px;
  align-items: start;
}

/* Filters Sidebar */
.filters-sidebar {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 20px;
  height: fit-content;
  max-height: calc(100vh - 40px);
  overflow-y: auto;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid #f1f5f9;
}

.filter-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1e293b;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
}

.clear-all-btn {
  background: #fee2e2;
  color: #dc2626;
  border: none;
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.clear-all-btn:hover {
  background: #fecaca;
}

/* Active Filters */
.active-filters {
  margin-bottom: 24px;
}

.active-filters-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.filter-chip {
  background: linear-gradient(45deg, #001aff, #a2e3e8);
  color: white;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.chip-remove {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: white;
  transition: all 0.3s ease;
}

.chip-remove:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* Filter Groups */
.filter-group {
  margin-bottom: 32px;
  border: 1px solid #f1f5f9;
  border-radius: 12px;
  overflow: hidden;
}

.filter-group-header {
  background: #f8fafc;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid #f1f5f9;
}

.filter-icon {
  width: 36px;
  height: 36px;
  background: linear-gradient(45deg, #002aff, #99cbfa);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.filter-info {
  flex: 1;
}

.filter-name {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 4px 0;
}

.filter-value {
  font-size: 12px;
  color: #ff0000;
  font-weight: 500;
}

.filter-content {
  padding: 20px;
}

/* Price Slider */
.price-slider-wrapper {
  margin-bottom: 20px;
  padding: 0 8px;
}

.modern-price-slider :deep(.el-slider__runway) {
  background: #f1f5f9;
  height: 8px;
}

.modern-price-slider :deep(.el-slider__bar) {
  background: linear-gradient(45deg, #002fff, #7bd5e5);
  height: 8px;
}

.modern-price-slider :deep(.el-slider__button) {
  width: 20px;
  height: 20px;
  background: white;
  border: 3px solid #012fff;
}

.price-quick-filters {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}

.price-quick-btn {
  background: #f8fafc;
  border: 2px solid #e2e8f0;
  color: #64748b;
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.price-quick-btn:hover,
.price-quick-btn.active {
  background: linear-gradient(45deg, #00bbff, #558f9e);
  color: white;
  border-color: #66eab7;
}

/* Category Options */
.category-options {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.category-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

/* Tiếp tục từ category-option */
.category-option:hover {
  background: #f8fafc;
  border-color: #e2e8f0;
}

.category-option.active {
  background: linear-gradient(45deg, #021ffe, #76e6dc);
  color: white;
  border-color: #66eaea;
}

.category-option.active .option-count {
  color: rgba(255, 255, 255, 0.8);
}

.option-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.option-label {
  font-weight: 500;
  font-size: 14px;
}

.option-count {
  font-size: 12px;
  color: #64748b;
  font-weight: 600;
}

.option-indicator {
  width: 4px;
  height: 20px;
  background: transparent;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.category-option.active .option-indicator {
  background: rgba(255, 255, 255, 0.3);
}

/* Filter Highlights */
.filter-highlights {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.highlights-title {
  font-size: 14px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 16px 0;
  text-align: center;
}

.highlights-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.highlight-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 0;
}

.highlight-icon {
  font-size: 20px;
  width: 32px;
  text-align: center;
}

.highlight-item span {
  font-size: 13px;
  color: #475569;
  font-weight: 500;
}

/* Products Grid */
.products-main {
  min-height: 600px;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4 phần tử mỗi dòng */
  gap: 24px;
  margin-bottom: 40px;
}

/* Modern Product Cards */
.product-card-modern {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  border: 1px solid #f1f5f9;
  position: relative;
}

.product-card-modern:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  border-color: #667eea;
}

.product-link-modern {
  text-decoration: none;
  color: inherit;
  display: block;
}

.product-card-inner {
  position: relative;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-badges {
  position: absolute;
  top: 16px;
  left: 16px;
  z-index: 3;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 10px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.badge.hot {
  background: linear-gradient(45deg, #ff6b6b, #ee5a52);
  color: white;
}

.badge.discount {
  background: linear-gradient(45deg, #4ecdc4, #44a08d);
  color: white;
}

/* Product Image Section - Fixed */
.product-image-wrapper {
  position: relative;
  background: #f8fafc;
  padding: 20px;
}

.product-image-container-modern {
  position: relative;
  width: 100%;
  height: 200px;
  border-radius: 12px;
  overflow: hidden;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-image-modern {
  width: 100%;
  height: 100%;
  object-fit: contain; /* Thay đổi từ cover thành contain */
  object-position: center; /* Đảm bảo ảnh được căn giữa */
  transition: all 0.4s ease;
  max-width: 100%;
  max-height: 100%;
}

.product-card-modern:hover .product-image-modern {
  transform: scale(1.02);
}

/* Product Overlay */
.product-overlay-modern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
}

.product-card-modern:hover .product-overlay-modern {
  opacity: 1;
  visibility: visible;
}

.overlay-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.action-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 12px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 600;
  backdrop-filter: blur(10px);
}

.action-btn:hover {
  background: white;
  transform: translateY(-2px);
}

.action-btn.primary {
  padding: 12px 20px;
}

.action-btn.secondary {
  width: 44px;
  height: 44px;
  justify-content: center;
}

/* Product Info Section */
.product-info-modern {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.product-brand {
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.product-rating-compact {
  display: flex;
  align-items: center;
  gap: 4px;
}

.rating-stars :deep(.el-rate__item) {
  margin-right: 2px;
}

.rating-count {
  font-size: 11px;
  color: #64748b;
  font-weight: 500;
}

.product-title-modern {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  line-height: 1.4;
  margin: 0;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-features {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.feature-tag {
  background: #ecfdf5;
  color: #059669;
  font-size: 10px;
  font-weight: 600;
  padding: 4px 8px;
  border-radius: 12px;
  border: 1px solid #d1fae5;
}

/* Product Pricing */
.product-pricing {
  margin-top: auto;
}

.price-current {
  font-size: 20px;
  font-weight: 800;
  color: #dc2626;
  margin-bottom: 4px;
}

.price-original {
  font-size: 14px;
  color: #94a3b8;
  text-decoration: line-through;
  font-weight: 500;
}

.price-save {
  font-size: 12px;
  color: #059669;
  font-weight: 600;
  margin-top: 4px;
}

/* Product Actions */
.product-actions-modern {
  padding: 16px 20px;
  background: #f8fafc;
  border-top: 1px solid #f1f5f9;
  display: flex;
  gap: 8px;
}

.compare {
  flex: 1;
  background: linear-gradient(45deg, #003cff, #3fb5f0);
  color: white;
  border: none;
  padding: 12px 16px;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  transition: all 0.3s ease;
  font-size: 14px;
}

.compare:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.compare-btn {
  background: white;
  border: 2px solid #e2e8f0;
  color: #64748b;
  padding: 12px;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.compare-btn:hover {
  border-color: #667eea;
  color: #667eea;
}

.compare-btn.active {
  background: #ecfdf5;
  border-color: #10b981;
  color: #059669;
  flex: 1;
  padding: 12px 16px;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  transition: all 0.3s ease;
  font-size: 14px;
}

/* Pagination */
.pagination-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-top: 32px;
}

.pagination-info {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

.modern-pagination :deep(.el-pager li) {
  background: white;
  border: 2px solid #f1f5f9;
  margin: 0 4px;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.modern-pagination :deep(.el-pager li:hover),
.modern-pagination :deep(.el-pager li.is-active) {
  background: #667eea;
  border-color: #667eea;
  color: white;
}

.modern-pagination :deep(.btn-prev),
.modern-pagination :deep(.btn-next) {
  background: white;
  border: 2px solid #f1f5f9;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.modern-pagination :deep(.btn-prev:hover),
.modern-pagination :deep(.btn-next:hover) {
  background: #667eea;
  border-color: #667eea;
  color: white;
}

/* No Products State */
.no-products-modern {
  text-align: center;
  padding: 80px 20px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.no-products-illustration {
  position: relative;
  margin-bottom: 32px;
  display: inline-block;
}

.illustration-icon {
  font-size: 80px;
  position: relative;
  z-index: 2;
}

.illustration-bg {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, #f1f5f9, #e2e8f0);
  border-radius: 50%;
  z-index: 1;
  opacity: 0.5;
}

.no-products-title {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 12px 0;
}

.no-products-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0 0 32px 0;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

.reset-filters-btn {
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
}

.reset-filters-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

/* Trust Section */
.trust-section {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  padding: 60px 0;
  margin-top: 60px;
}

.trust-indicators {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 32px;
}

.trust-item {
  display: flex;
  align-items: center;
  gap: 16px;
  text-align: left;
}

.trust-icon {
  font-size: 32px;
  width: 56px;
  height: 56px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.trust-content h4 {
  font-size: 18px;
  font-weight: 700;
  color: white;
  margin: 0 0 4px 0;
}

.trust-content p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

/* Responsive Design */
@media (max-width: 1200px) {
  .content-layout {
    grid-template-columns: 280px 1fr;
    gap: 24px;
  }

  .products-grid {
    grid-template-columns: repeat(4, 1fr); /* 4 phần tử mỗi dòng */
  }
}

@media (max-width: 1024px) {
  .content-layout {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .filters-sidebar {
    position: static;
    max-height: none;
    order: 2;
  }

  .products-main {
    order: 1;
  }

  .content-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .view-controls {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 0 16px;
  }

  .search-title {
    font-size: 2.5rem;
  }

  .hero-search-section {
    padding: 40px 0;
  }

  .mega-search-bar {
    padding: 12px;
  }

  .mega-search-input :deep(.el-input__inner) {
    height: 48px;
    font-size: 14px;
    padding-right: 120px;
  }

  .search-btn {
    padding: 10px 16px;
    font-size: 14px;
  }

  .quick-search-tags {
    justify-content: flex-start;
  }

  .products-grid {
    grid-template-columns: repeat(4, 1fr); /* 4 phần tử mỗi dòng */
    gap: 16px;
  }

  .trust-indicators {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .pagination-wrapper {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .search-title {
    font-size: 2rem;
  }

  .search-subtitle {
    font-size: 1rem;
  }

  .products-grid {
    grid-template-columns: repeat(4, 1fr); /* 4 phần tử mỗi dòng */
  }

  .product-actions-modern {
    flex-direction: column;
  }

  .compare-btn {
    justify-content: center;
  }
}
</style>
