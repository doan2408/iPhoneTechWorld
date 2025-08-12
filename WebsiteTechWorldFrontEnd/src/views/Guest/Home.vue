<script setup>
import { ref, computed, onMounted, watch } from "vue";
import Banner from "@/components/Client/Banner.vue";
import {
  getAllSanPham,
  getLoai,
} from "@/Service/GuestService/ProductGuestService";
import {
  PriceTag,
  Search,
  Delete,
  Menu,
  ArrowDown,
  Sort,
  View,
} from "@element-plus/icons-vue";
import { h } from "vue";
import CompareBar from "../Client/Compare/CompareBar.vue";

const showClearFilter = ref(false);
const filterKeyword = ref("");
const selectedLoai = ref("");
const priceRange = ref([0, 50000000]); // Default range: 0 - 50 triệu
const currentPage = ref(1);
const pageSize = 16;
const totalProducts = ref(0);
const selectedSort = ref("");
// Product data
const products = ref([]);

// debounce timer
let debounceTimer = null;

// Price range configuration
const minPrice = 0;
const maxPrice = 50000000;
const priceStep = 1000000;

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
    products.value = data.content || data;
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

//debounced search function
const debouncedFetchProducts = () => {
  if (debounceTimer) {
    clearTimeout(debounceTimer);
  }
  debounceTimer = setTimeout(() => {
    currentPage.value = 1;
    fetchProducts();
  }, 500); //500ms delay
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
    console.error("Lỗi khi lấy danh sách loại:", error);
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

//watch for changes in filtes
watch(filterKeyword, () => {
  //only debounced for search keyword
  debouncedFetchProducts();
});

// Watch for changes in filters
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
});
const compareList = ref([]);
const addToCompare = (product) => {
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
            <h2 class="results-title">
              <span class="results-count" v-if="totalProducts > 0">{{
                totalProducts
              }}</span>
              sản phẩm
              <span v-if="filterKeyword" class="search-term"
                >cho "{{ filterKeyword }}"</span
              >
            </h2>
            <p class="results-subtitle">
              Được cập nhật {{ new Date().toLocaleDateString("vi-VN") }}
            </p>
          </div>

          <div class="view-controls">
            <div class="sort-control">
              <el-dropdown trigger="click" class="sort-dropdown">
                <el-button class="sort-btn">
                  <el-icon><Sort /></el-icon>
                  <span>{{ sortLabel }}</span>
                  <el-icon><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu class="custom-dropdown">
                    <el-dropdown-item
                      @click="
                        selectedSort = '';
                        currentPage = 1;
                        fetchProducts();
                      "
                    >
                      <el-icon><Refresh /></el-icon>
                      Mặc định
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click="
                        selectedSort = 'giaAsc';
                        currentPage = 1;
                        fetchProducts();
                      "
                    >
                      <el-icon><SortUp /></el-icon>
                      Giá thấp đến cao
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click="
                        selectedSort = 'giaDesc';
                        currentPage = 1;
                        fetchProducts();
                      "
                    >
                      <el-icon><SortDown /></el-icon>
                      Giá cao đến thấp
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
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
                  router-link
                  :to="`detail/${sp.id}`"
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
              <p>Miễn phí từ 500K</p>
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
              <p>Trong vòng 30 ngày</p>
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
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Hero Search Section */
.hero-search-section {
  margin-top: 10px;
  background: linear-gradient(135deg, #48cbd2 0%, #a7cfeb 100%);
  padding: 60px 0;
  position: relative;
  overflow: hidden;
  border-radius: 30px;
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
      rgba(255, 255, 255, 0.1) 0%,
      transparent 50%
    ),
    radial-gradient(
      circle at 80% 20%,
      rgba(255, 255, 255, 0.1) 0%,
      transparent 50%
    );
}

.search-hero {
  position: relative;
  z-index: 2;
}

.search-content {
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
}

.search-title {
  font-size: 3.5rem;
  font-weight: 800;
  color: white;
  margin-bottom: 16px;
  line-height: 1.2;
}

.gradient-text {
  background: linear-gradient(45deg, #ffd700, #ffed4e);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.search-subtitle {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 40px;
  font-weight: 500;
}

/* Mega Search Bar */
.mega-search-bar {
  background: white;
  border-radius: 24px;
  padding: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.search-input-container {
  margin-bottom: 20px;
}

.mega-search-input {
  border-radius: 16px;
  border: 2px solid #e2e8f0;
  transition: all 0.3s ease;
}

.mega-search-input:focus-within {
  border-color: #0b35f0;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.mega-search-input :deep(.el-input__inner) {
  height: 56px;
  font-size: 16px;
  padding-left: 20px;
  padding-right: 140px;
}

.search-icon {
  color: #94a3b8;
  font-size: 20px;
}

.search-btn {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  background: linear-gradient(45deg, #0015fc, #8ed7df);
  color: white;
  border: none;
  padding: 5px 20px;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 2px;
  transition: all 0.3s ease;
}

.search-btn:hover {
  transform: translateY(-50%) scale(1.05);
  box-shadow: 0 8px 20px rgba(8, 53, 254, 0.4);
}

/* Quick Search Tags */
.quick-search-tags {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  justify-content: center;
}

.tags-label {
  font-size: 14px;
  color: #64748b;
  font-weight: 600;
  white-space: nowrap;
}

.tags-container {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: center;
}

.quick-tag {
  background: #f1f5f9;
  color: #475569;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #e2e8f0;
}

.quick-tag:hover {
  background: #667eea;
  color: white;
  transform: translateY(-1px);
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
  margin-bottom: 32px;
  padding: 5px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.results-info h2 {
  font-size: 2rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.results-count {
  color: #66ea7c;
}

.search-term {
  font-style: italic;
  color: #64748b;
}

.results-subtitle {
  color: #64748b;
  font-size: 14px;
  margin: 0;
}

.view-controls {
  display: flex;
  gap: 16px;
  align-items: center;
}

.sort-btn {
  background: white;
  border: 2px solid #e2e8f0;
  padding: 12px 20px;
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.sort-btn:hover {
  border-color: #0be658;
  background: #f8fafc;
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