<script setup>
import { ref, computed, onMounted, watch } from "vue";
import Banner from "@/components/Client/Banner.vue"; // Đảm bảo import đúng
import {
  getAllSanPham,
  getLoai,
} from "@/Service/GuestService/ProductGuestService";
import {
  Search,
  Filter,
  Close,
  Sort,
  Plus,
  Check,
} from "@element-plus/icons-vue";
import CompareBar from "../Client/Compare/CompareBar.vue";
import { useToast } from "vue-toastification";

// --- STATE MANAGEMENT ---
const showClearFilter = ref(false);
const filterKeyword = ref("");
const selectedLoai = ref("");
const priceRange = ref([0, 50000000]); // Default range: 0 - 50 triệu
const currentPage = ref(1);
const pageSize = 16;
const totalProducts = ref(0);
const selectedSort = ref("");
const products = ref([]);
const toast = useToast();
let debounceTimer = null;

// Cấu hình thanh trượt giá
const minPrice = 0;
const maxPrice = 50000000;
const priceStep = 1000000;

// Các mức giá nhanh
const quickPriceFilters = [
  { label: "Dưới 5tr", range: [0, 5000000] },
  { label: "5tr - 10tr", range: [5000000, 10000000] },
  { label: "10tr - 20tr", range: [10000000, 20000000] },
  { label: "Trên 20tr", range: [20000000, 50000000] },
];

// Danh sách loại sản phẩm
const loaiOptions = ref([{ label: "Tất cả", value: "" }]);

// Danh sách so sánh
const compareList = ref([]);

const hasProduct = computed(() => products.value.length > 0);

// --- API CALLS ---
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

// --- HELPER FUNCTIONS ---
const debouncedFetchProducts = () => {
  if (debounceTimer) clearTimeout(debounceTimer);
  debounceTimer = setTimeout(() => {
    currentPage.value = 1;
    fetchProducts();
  }, 500);
};

const clearFilters = () => {
  if (debounceTimer) clearTimeout(debounceTimer);
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

const applyQuickPriceFilter = (range) => {
  priceRange.value = [...range];
};

// --- COMPARE LOGIC ---
const addToCompare = (product) => {
  if (compareList.value.length >= 3) {
    toast.warning("Bạn chỉ có thể so sánh tối đa 3 sản phẩm");
    return;
  }
  if (!compareList.value.find((p) => p.id === product.id)) {
    compareList.value.push(product);
    toast.success("Đã thêm vào so sánh");
  }
};

const isInCompareList = (product) => {
  return compareList.value.some((p) => p.id === product.id);
};

const removeCompare = (product) => {
  compareList.value = compareList.value.filter((p) => p.id !== product.id);
};

// --- WATCHERS & LIFECYCLE ---
watch(filterKeyword, () => {
  debouncedFetchProducts();
});

watch(
  [selectedLoai, priceRange],
  () => {
    currentPage.value = 1;
    fetchProducts();
  },
  { deep: true },
);

watch(currentPage, () => {
  fetchProducts();
});

onMounted(() => {
  fetchLoai();
  fetchProducts();
});
</script>

<template>
  <section class="sale-page">
    <Banner />

    <div class="hero-container">
      <div class="container">
        <div class="hero-content">
          <h1 class="hero-title">
            Tìm <span class="highlight">Dế Yêu</span> - Giá
            <span class="highlight">Siêu Tốt</span>
          </h1>

          <div class="search-box-wrapper">
            <el-input
              v-model="filterKeyword"
              placeholder="Bạn muốn tìm mẫu điện thoại nào? (Ví dụ: iPhone 15 Pro Max)"
              class="hero-search-input"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon class="search-icon"><Search /></el-icon>
              </template>
            </el-input>
          </div>

          <div class="quick-tags">
            <span class="tag-label">Từ khóa hot:</span>
            <div class="tag-list">
              <span class="hot-tag" @click="filterKeyword = 'iPhone 15'"
                >iPhone 15</span
              >
              <span class="hot-tag" @click="filterKeyword = 'Samsung S24'"
                >Samsung S24</span
              >
              <span class="hot-tag" @click="filterKeyword = 'Xiaomi 14'"
                >Xiaomi 14</span
              >
              <span class="hot-tag" @click="filterKeyword = 'Oppo'">Oppo</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-body">
      <div class="container">
        <div class="toolbar">
          <div class="toolbar-left">
            <h2 class="section-title">
              Điện thoại chính hãng
              <span class="count-badge" v-if="totalProducts > 0">{{
                totalProducts
              }}</span>
            </h2>
          </div>

          <div class="toolbar-right">
            <div class="sort-group">
              <span class="sort-label">Sắp xếp:</span>
              <button
                :class="['sort-chip', { active: selectedSort === '' }]"
                @click="
                  selectedSort = '';
                  currentPage = 1;
                  fetchProducts();
                "
              >
                Nổi bật
              </button>
              <button
                :class="['sort-chip', { active: selectedSort === 'giaAsc' }]"
                @click="
                  selectedSort = 'giaAsc';
                  currentPage = 1;
                  fetchProducts();
                "
              >
                <el-icon><Sort /></el-icon> Giá thấp - cao
              </button>
              <button
                :class="['sort-chip', { active: selectedSort === 'giaDesc' }]"
                @click="
                  selectedSort = 'giaDesc';
                  currentPage = 1;
                  fetchProducts();
                "
              >
                <el-icon><Sort /></el-icon> Giá cao - thấp
              </button>
            </div>
          </div>
        </div>

        <div class="layout-grid">
          <aside class="sidebar">
            <div class="filter-box">
              <div class="filter-header">
                <h3>
                  <el-icon><Filter /></el-icon> Bộ lọc tìm kiếm
                </h3>
                <button
                  v-if="showClearFilter"
                  @click="clearFilters"
                  class="reset-btn"
                >
                  Bỏ chọn
                </button>
              </div>

              <div class="selected-tags" v-if="showClearFilter">
                <div class="tag-chip" v-if="selectedLoai">
                  {{
                    loaiOptions.find((item) => item.value === selectedLoai)
                      ?.label
                  }}
                  <el-icon @click="selectedLoai = ''"><Close /></el-icon>
                </div>
                <div
                  class="tag-chip"
                  v-if="
                    priceRange[0] !== minPrice || priceRange[1] !== maxPrice
                  "
                >
                  {{ formatPriceShort(priceRange[0]) }} -
                  {{ formatPriceShort(priceRange[1]) }}
                  <el-icon @click="priceRange = [minPrice, maxPrice]"
                    ><Close
                  /></el-icon>
                </div>
              </div>

              <div class="filter-section">
                <h4>Khoảng giá</h4>
                <div class="price-inputs">
                  <span>{{ formatPriceShort(priceRange[0]) }}</span>
                  <span class="sep">-</span>
                  <span>{{ formatPriceShort(priceRange[1]) }}</span>
                </div>
                <el-slider
                  v-model="priceRange"
                  range
                  :min="minPrice"
                  :max="maxPrice"
                  :step="priceStep"
                  :show-tooltip="false"
                  class="custom-slider"
                />
                <div class="quick-ranges">
                  <button
                    v-for="filter in quickPriceFilters"
                    :key="filter.label"
                    @click="applyQuickPriceFilter(filter.range)"
                    :class="{
                      active:
                        priceRange[0] === filter.range[0] &&
                        priceRange[1] === filter.range[1],
                    }"
                  >
                    {{ filter.label }}
                  </button>
                </div>
              </div>

              <div class="filter-section">
                <h4>Hãng sản xuất</h4>
                <div class="brand-list">
                  <div
                    v-for="option in loaiOptions"
                    :key="option.value"
                    :class="[
                      'brand-item',
                      { active: selectedLoai === option.value },
                    ]"
                    @click="selectedLoai = option.value"
                  >
                    <span class="checkbox-mock"></span>
                    {{ option.label }}
                  </div>
                </div>
              </div>
            </div>

            <div class="sidebar-promo">
              <div class="promo-item">
                <span class="icon">🚚</span>
                <div class="text">
                  <strong>FreeShip</strong>
                  <small>Đơn từ 500k</small>
                </div>
              </div>
              <div class="promo-item">
                <span class="icon">🛡️</span>
                <div class="text">
                  <strong>Bảo hành</strong>
                  <small>Chính hãng 12T</small>
                </div>
              </div>
            </div>
          </aside>

          <main class="product-area">
            <div class="grid-container" v-show="hasProduct">
              <div v-for="sp in products" :key="sp.id" class="product-card">
                <router-link :to="`detail/${sp.id}`" class="product-link">
                  <div class="card-badges">
                    <span class="badge installment">Trả góp 0%</span>
                    <span class="badge discount" v-if="sp.giaBan < 30000000"
                      >Giảm sốc</span
                    >
                  </div>

                  <div class="img-wrapper">
                    <img
                      :src="sp.hinhAnh || '/img/no-image.png'"
                      :alt="sp.tenSanPham"
                      loading="lazy"
                    />
                  </div>

                  <h3 class="product-name">{{ sp.tenSanPham }}</h3>

                  <div class="product-specs">
                    <span>Chính hãng</span>
                    <span>BH 12T</span>
                  </div>

                  <div class="price-box">
                    <span class="current-price">{{
                      formatPrice(sp.giaBan)
                    }}</span>
                    <span class="old-price">{{
                      formatPrice(sp.giaBan * 1.1)
                    }}</span>
                    <span class="discount-rate">-10%</span>
                  </div>

                  <div class="rating-row">
                    <el-rate
                      v-model="sp.averageRating"
                      disabled
                      allow-half
                      size="small"
                    />
                    <span class="review-count"
                      >({{ Math.floor(Math.random() * 50) + 10 }})</span
                    >
                  </div>
                </router-link>

                <div class="card-actions">
                  <button
                    class="action-btn compare"
                    @click.stop.prevent="addToCompare(sp)"
                    :class="{ added: isInCompareList(sp) }"
                    :disabled="isInCompareList(sp)"
                  >
                    <el-icon v-if="!isInCompareList(sp)"><Plus /></el-icon>
                    <el-icon v-else><Check /></el-icon>
                    {{ isInCompareList(sp) ? "Đã thêm" : "So sánh" }}
                  </button>
                </div>
              </div>
            </div>

            <div v-show="!hasProduct" class="empty-state">
              <div class="empty-icon">📱</div>
              <h3>Không tìm thấy sản phẩm nào</h3>
              <p>Hãy thử thay đổi tiêu chí lọc hoặc tìm kiếm từ khóa khác</p>
              <button @click="clearFilters">Xóa bộ lọc</button>
            </div>

            <div
              class="pagination-container"
              v-show="hasProduct && totalProducts > pageSize"
            >
              <el-pagination
                v-model:current-page="currentPage"
                :page-size="pageSize"
                :total="totalProducts"
                layout="prev, pager, next"
                background
                class="sale-pagination"
              />
            </div>
          </main>
        </div>
      </div>
    </div>
  </section>

  <CompareBar :compareList="compareList" @remove="removeCompare" />
</template>

<style scoped>
/* ================= GLOBAL SETUP ================= */
.sale-page {
  background-color: #f3f4f6;
  min-height: 100vh;
  color: #333;
  font-family: "Inter", sans-serif;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
}

/* ================= HERO SECTION ================= */
.hero-container {
  background: #212121; /* Dark theme premium */
  padding: 30px 0;
  margin-bottom: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.hero-content {
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  color: #fff;
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 20px;
}

.highlight {
  color: #ffd700;
  font-style: italic;
}

.search-box-wrapper {
  margin-bottom: 15px;
}

.hero-search-input :deep(.el-input__wrapper) {
  border-radius: 50px;
  padding: 10px 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.hero-search-input :deep(.el-input__inner) {
  font-size: 16px;
  height: 40px;
}
.search-icon {
  font-size: 20px;
  color: #666;
}

.quick-tags {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.tag-label {
  color: #aaa;
  font-size: 14px;
}

.hot-tag {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  padding: 5px 12px;
  border-radius: 15px;
  font-size: 13px;
  cursor: pointer;
  transition: 0.3s;
}

.hot-tag:hover {
  background: #ffd700;
  color: #000;
}

/* ================= TOOLBAR ================= */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 15px;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #111;
  display: flex;
  align-items: center;
  gap: 10px;
}

.count-badge {
  background: #eee;
  color: #666;
  font-size: 0.9rem;
  padding: 2px 8px;
  border-radius: 10px;
}

.sort-group {
  display: flex;
  align-items: center;
  gap: 10px;
}
.sort-label {
  font-size: 14px;
  color: #666;
}

.sort-chip {
  background: #fff;
  border: 1px solid #ddd;
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: 0.2s;
  color: #444;
}

.sort-chip:hover {
  border-color: #333;
}
.sort-chip.active {
  background: #333;
  color: #fff;
  border-color: #333;
}

/* ================= LAYOUT GRID ================= */
.layout-grid {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 20px;
}

/* ================= SIDEBAR ================= */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.filter-box {
  background: #fff;
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.filter-header h3 {
  font-size: 15px;
  font-weight: 700;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 6px;
}

.reset-btn {
  background: none;
  border: none;
  color: #d70018;
  font-size: 13px;
  cursor: pointer;
}

.selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-bottom: 15px;
}

.tag-chip {
  background: #fcebeb;
  color: #d70018;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 4px;
  border: 1px solid #fcc;
  cursor: pointer;
}

.filter-section {
  margin-bottom: 20px;
}
.filter-section h4 {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 10px;
  color: #333;
}

.price-inputs {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  font-weight: bold;
  color: #d70018;
  margin-bottom: 5px;
}

.custom-slider :deep(.el-slider__bar) {
  background-color: #d70018;
}
.custom-slider :deep(.el-slider__button) {
  border-color: #d70018;
}

.quick-ranges {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-top: 10px;
}

.quick-ranges button {
  background: #f8f8f8;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 11px;
  cursor: pointer;
  width: 47%;
  flex-grow: 1;
}

.quick-ranges button.active {
  border-color: #d70018;
  color: #d70018;
  background: #fff5f5;
  font-weight: bold;
}

.brand-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.brand-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  cursor: pointer;
  padding: 6px;
  border-radius: 6px;
  transition: 0.2s;
}

.brand-item:hover {
  background: #f5f5f5;
}

.checkbox-mock {
  width: 16px;
  height: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #fff;
}

.brand-item.active .checkbox-mock {
  background: #d70018;
  border-color: #d70018;
  position: relative;
}

.brand-item.active .checkbox-mock::after {
  content: "✓";
  color: #fff;
  font-size: 10px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.sidebar-promo {
  background: #e8f4fd;
  padding: 15px;
  border-radius: 12px;
}
.promo-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}
.promo-item .icon {
  font-size: 20px;
}
.promo-item .text {
  display: flex;
  flex-direction: column;
  font-size: 13px;
  color: #0056b3;
}

/* ================= PRODUCT GRID ================= */
.grid-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4 cột */
  gap: 15px;
}

.product-card {
  background: #fff;
  border-radius: 10px;
  padding: 10px;
  position: relative;
  border: 1px solid transparent;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  height: 100%;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  border-color: #eee;
  z-index: 2;
}

.product-link {
  text-decoration: none;
  color: inherit;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.card-badges {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 2;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.badge {
  font-size: 10px;
  padding: 3px 6px;
  border-radius: 4px;
  font-weight: 600;
  text-transform: uppercase;
}

.badge.installment {
  background: #f1f1f1;
  color: #333;
}
.badge.discount {
  background: #d70018;
  color: #fff;
}

.img-wrapper {
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15px;
  transition: 0.3s;
  padding: 10px;
}

.product-card:hover .img-wrapper {
  transform: scale(1.05);
}

.img-wrapper img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  line-height: 1.4;
  margin: 0 0 8px 0;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 40px;
}

.product-specs {
  display: flex;
  gap: 5px;
  margin-bottom: 8px;
}
.product-specs span {
  font-size: 10px;
  background: #f3f4f6;
  padding: 2px 6px;
  border-radius: 4px;
  color: #666;
  border: 1px solid #eee;
}

.price-box {
  margin-top: auto;
  margin-bottom: 8px;
}
.current-price {
  display: block;
  font-size: 16px;
  font-weight: bold;
  color: #d70018;
}
.old-price {
  font-size: 12px;
  text-decoration: line-through;
  color: #999;
  margin-right: 5px;
}
.discount-rate {
  font-size: 12px;
  color: #d70018;
  font-weight: 600;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 10px;
}
.review-count {
  font-size: 11px;
  color: #777;
}

.card-actions {
  margin-top: auto;
  border-top: 1px solid #f1f1f1;
  padding-top: 10px;
}

.action-btn.compare {
  width: 100%;
  background: #f8f9fa;
  border: 1px solid #eee;
  color: #555;
  padding: 8px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  transition: 0.2s;
}

.action-btn.compare:hover {
  background: #e9ecef;
}
.action-btn.compare.added {
  background: #e6f7ff;
  color: #007bff;
  border-color: #007bff;
}

/* ================= PAGINATION & EMPTY ================= */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
.sale-pagination :deep(.el-pager li.is-active) {
  background-color: #333 !important;
}

.empty-state {
  grid-column: 1 / -1;
  text-align: center;
  padding: 50px 0;
  background: #fff;
  border-radius: 12px;
}
.empty-icon {
  font-size: 60px;
  margin-bottom: 10px;
}
.empty-state h3 {
  margin: 15px 0 5px;
  color: #333;
}
.empty-state button {
  margin-top: 15px;
  background: #d70018;
  color: #fff;
  border: none;
  padding: 8px 20px;
  border-radius: 20px;
  cursor: pointer;
}

/* ================= RESPONSIVE ================= */
@media (max-width: 1024px) {
  .layout-grid {
    grid-template-columns: 200px 1fr;
  }
  .grid-container {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .layout-grid {
    display: block;
  }
  .sidebar {
    display: none;
  }
  .toolbar {
    flex-direction: column;
    align-items: flex-start;
  }
  .grid-container {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
  .product-name {
    font-size: 13px;
  }
  .current-price {
    font-size: 15px;
  }
  .hero-title {
    font-size: 1.5rem;
  }
}
</style>
