<script setup>
import { ref, computed, onMounted, watch } from "vue";
import Banner from "@/components/Client/Banner.vue";
import { getAllSanPham, getLoai } from "@/Service/ClientService/Products/ProductClientService";
import { 
  PriceTag, 
  Search, 
  Delete, 
  Menu, 
  ArrowDown, 
  Sort, 
  View 
} from '@element-plus/icons-vue'
import { h } from 'vue';
import CompareBar from "./Compare/CompareBar.vue";

const showClearFilter = ref(false);
const filterKeyword = ref("");
const selectedLoai = ref("");
const priceRange = ref([0, 50000000]);  //default range 0 - 50 milions
const currentPage = ref(1);
const pageSize = 16;
const totalProducts = ref(0);
const selectedSort = ref("");
// Product data
const products = ref([]);

// Debounce timer
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

const fetchLoai = async() => {
  try {
    const data = await getLoai();
    loaiOptions.value = [
      {label: "Tất cả", value: ""},
      ...data.map((item) => ({
        label: item.tenLoai,
        value: item.idLoai,
      })),
    ];
  } catch (error) {
    console.error("Lỗi khi lấy danh sách loại", error);
  }
}

const clearFilters = () => {
  // Clear debounce timer when clearing filters
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
watch(
  filterKeyword,
  () => {
    // Chỉ debounce cho search keyword
    debouncedFetchProducts();
  }
);

watch(
  [selectedLoai, priceRange],
  () => {
    // Không debounce cho filter loại và giá (cần response ngay lập tức)
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
const compareList = ref([])
const addToCompare = (product) => {
  if (!compareList.value.find(p => p.id === product.id)) {
    compareList.value.push(product)
  }
}
const isInCompareList = (product) => {
  return compareList.value.some(p => p.id === product.id)
}
const removeCompare = (product) => {
  compareList.value = compareList.value.filter(p => p.id !== product.id)
}
</script>

<template>
  <section class="main-section">
    <Banner />

    <!-- Nội dung chính với cột trái (bộ lọc) và cột phải (sản phẩm) -->
    <div class="content-wrapper">
      <!-- Cột trái: Bộ lọc -->
      <div class="filter-section">
        <!-- Danh mục hãng -->
        <div class="companyMenu group flexContain">
          <!-- Add company filters if needed -->
        </div>

        <!-- Price Range Filter -->
        <div class="price-filter">
          <div class="filter-header">
            <div class="filter-icon">
              <el-icon>
                <PriceTag />
              </el-icon>
            </div>
            <div class="filter-content">
              <span class="filter-label">Khoảng giá</span>
              <span class="price-display">
                {{ formatPriceShort(priceRange[0]) }} -
                {{ formatPriceShort(priceRange[1]) }}
              </span>
            </div>
          </div>
          <div class="price-slider-container">
            <el-slider v-model="priceRange" range :min="minPrice" :max="maxPrice" :step="priceStep"
              :format-tooltip="formatPriceShort" class="price-slider" />
          </div>
          <div class="quick-price-filters">
            <el-button v-for="filter in quickPriceFilters" :key="filter.label" size="small" :type="
                priceRange[0] === filter.range[0] &&
                priceRange[1] === filter.range[1]
                  ? 'primary'
                  : 'default'
              " @click="applyQuickPriceFilter(filter.range)" class="quick-filter-btn">
              {{ filter.label }}
            </el-button>
          </div>
        </div>

        <!-- Bộ lọc đang chọn -->
        <div class="chosen-filters" v-if="showClearFilter">
          <div class="chosen-filters-header">
            <span class="chosen-label">Bộ lọc đang áp dụng:</span>
            <el-button type="danger" size="small" @click="clearFilters" class="clear-btn">
              <el-icon>
                <Delete />
              </el-icon>
              Xóa tất cả
            </el-button>
          </div>
          <div class="chosen-tags">
            <el-tag v-if="priceRange[0] !== minPrice || priceRange[1] !== maxPrice" closable
              @close="priceRange = [minPrice, maxPrice]" type="info" class="filter-tag">
              Giá: {{ formatPriceShort(priceRange[0]) }} -
              {{ formatPriceShort(priceRange[1]) }}
            </el-tag>
            <el-tag v-if="selectedLoai" closable @close="selectedLoai = ''" type="info" class="filter-tag">
              Loại: {{ loaiOptions.find((item) => item.value === selectedLoai)?.label }}
            </el-tag>
            <el-tag v-if="filterKeyword" closable @close="filterKeyword = ''" type="info" class="filter-tag">
              Từ khóa: {{ filterKeyword }}
            </el-tag>
          </div>
        </div>

        <!-- Additional filters -->
        <div class="additional-filters">
          <div class="filter-item">
            <el-dropdown trigger="click">
              <el-button class="filter-btn">
                <el-icon>
                  <Menu />
                </el-icon>
                <span>
                  {{ selectedLoai ? loaiOptions.find((item) => item.value ===selectedLoai)?.label : "Loại" }}
                </span>
                <el-icon class="el-icon--right">
                  <ArrowDown />
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-for="option in loaiOptions" :key="option.value"
                    @click="selectedLoai = option.value">
                    {{ option.label }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div class="filter-item">
            <el-dropdown trigger="click">
              <el-button class="filter-btn">
                <el-icon>
                  <Sort />
                </el-icon>
                <span> {{ sortLabel }}</span>
                <el-icon class="el-icon--right">
                  <ArrowDown />
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="
                      selectedSort = '';
                      currentPage = 1;
                      fetchProducts();
                    ">
                    Hủy sắp xếp
                  </el-dropdown-item>
                  <el-dropdown-item @click="
                      selectedSort = 'giaAsc';
                      currentPage = 1;
                      fetchProducts();
                    ">
                    Giá thấp đến cao
                  </el-dropdown-item>
                  <el-dropdown-item @click="
                      selectedSort = 'giaDesc';
                      currentPage = 1;
                      fetchProducts();
                    ">
                    Giá cao đến thấp
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>

      <!-- Cột phải: Sản phẩm -->
      <div class="products-container">
        <!-- Search bar -->
        <div class="search-section">
          <div class="search-wrapper">
            <el-input v-model="filterKeyword" placeholder="Tìm kiếm sản phẩm..." class="search-input" clearable>
              <template #prefix>
                <el-icon>
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </div>
          <div class="products-count" v-if="totalProducts > 0">
            Có {{ totalProducts }} sản phẩm
          </div>
        </div>

        <!-- Products grid -->
        <div class="products-grid" v-show="hasProduct">
          <el-row :gutter="24" class="homeproduct">
            <el-col v-for="sp in products" :key="sp.id" :xs="24" :sm="12" :md="8" :lg="6" :xl="6" class="product-col">
              <router-link :to="`/client/detail/${sp.id}`" class="product-link">
                <div class="product-card">
                  <div class="product-image-container">
                    <img :src="sp.hinhAnh || '/img/no-image.png'" alt="product" class="product-image" />
                    <div class="product-overlay">
                      <div class="quick-view-btn">
                        <el-icon>
                          <View />
                        </el-icon>
                      </div>
                    </div>
                  </div>
                  <div class="product-info">
                    <h3 class="product-name">{{ sp.tenSanPham }} - {{ sp.maXuatXu }}</h3>
                    <div class="product-price">{{ formatPrice(sp.giaBan) }}</div>
                  </div>
                </div>
              </router-link>
              <el-button v-if="!isInCompareList(sp)" class="add-to-compare" size="small"
                @click.stop.prevent="addToCompare(sp)">
                <el-icon>
                  <Plus />
                </el-icon>
                Thêm vào so sánh
              </el-button>

              <el-button v-else class="already-added" size="small" disabled>
                <el-icon>
                  <Check />
                </el-icon>
                Đã thêm vào so sánh
              </el-button>
              <br><br>
            </el-col>
          </el-row>
        </div>

        <!-- Pagination -->
        <div class="pagination-container" v-show="hasProduct && totalProducts > pageSize">
          <el-pagination v-model:current-page="currentPage" :page-size="pageSize" :total="totalProducts"
            layout="prev, pager, next, jumper" :background="true" class="custom-pagination" />
        </div>

        <!-- Không có sản phẩm -->
        <div v-show="!hasProduct" class="no-products">
          <i class="fa fa-times-circle"></i>
          Không có sản phẩm nào
        </div>
      </div>
    </div>

    <!-- Khung khác -->
    <div class="contain-khungSanPham"></div>
  </section>
  <CompareBar :compareList="compareList" @remove="removeCompare" />
</template>

<style scoped>
.main-section {
  padding: 20px;
  box-sizing: border-box;
  max-width: 1400px;
  margin: 0 auto;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

.content-wrapper {
  display: flex;
  gap: 20px;
}

.filter-section {
  width: 25%;
  background: #ffffff;
  padding: 15px;
  border: 1px solid #dee2e6;
  border-radius: 8px;
}

.products-container {
  width: 75%;
  background: #ffffff;
  border: 1px solid #dee2e6;
  border-radius: 12px;
  padding: 20px;
}

.filter-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.filter-icon {
  width: 36px;
  height: 36px;
  background: #0d6efd;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 14px;
}

.filter-content {
  flex: 1;
}

.filter-label {
  display: block;
  font-weight: 600;
  color: #212529;
  font-size: 14px;
  margin-bottom: 2px;
}

.price-display {
  font-weight: 600;
  color: #0d6efd;
  font-size: 15px;
}

.price-slider-container {
  margin-bottom: 16px;
  padding: 0 8px;
}

.quick-price-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.quick-filter-btn {
  font-size: 12px;
  padding: 6px 12px;
  border-radius: 16px;
  transition: all 0.2s ease;
}

.price-filter,
.chosen-filters,
.additional-filters {
  margin-bottom: 20px;
}

.chosen-filters {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
  border: 1px solid #dee2e6;
}

.chosen-filters-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.chosen-label {
  font-weight: 600;
  color: #495057;
  font-size: 14px;
}

.clear-btn {
  background: #dc3545;
  border: none;
  color: white;
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 12px;
}

.chosen-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.filter-tag {
  background: rgba(13, 110, 253, 0.1);
  color: #0d6efd;
  border: 1px solid rgba(13, 110, 253, 0.2);
  border-radius: 4px;
  padding: 4px 8px;
  font-size: 12px;
}

/* Additional Filters - Horizontal Layout */
.additional-filters {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.filter-item {
  flex: 1;
  min-width: 0;
}

.filter-btn {
  width: 100%;
  justify-content: space-between;
  font-size: 13px;
  padding: 8px 12px;
  border: 1px solid #dee2e6;
  background: #ffffff;
  transition: all 0.2s ease;
}

.filter-btn:hover {
  background: #f8f9fa;
  border-color: #0d6efd;
}

.search-section {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
}

.search-wrapper {
  margin-bottom: 12px;
}

.search-input {
  border-radius: 15px;
  border: 1px solid #1172d4;
  padding: 10px 14px;
  font-size: 14px;
}

.search-input:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 2px rgba(13, 110, 253, 0.1);
}

.products-count {
  font-size: 13px;
  color: #6c757d;
  font-weight: 500;
  text-align: center;
  margin-top: 8px;
  padding: 8px;
  background: #e9ecef;
  border-radius: 6px;
}

.products-grid {
  margin-bottom: 24px;
}

.product-link {
  text-decoration: none;
  color: inherit;
  display: block;
}

.product-card {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.2s ease;
  border: 1px solid #e9ecef;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
  border-color: #0d6efd;
}

.product-image-container {
  height: 180px;
  position: relative;
  background: #f8f9fa;
  padding: 12px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 156px;
  object-fit: contain;
  transition: transform 0.2s ease;
}

.product-card:hover .product-image {
  transform: scale(1.02);
}

.product-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(13, 110, 253, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.quick-view-btn {
  width: 40px;
  height: 40px;
  background: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0d6efd;
  font-size: 16px;
}

.product-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 14px;
  font-weight: 600;
  color: #212529;
  margin: 0 0 8px 0;
  line-height: 1.3;
  height: 36px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient: vertical;
}

.product-price {
  font-size: 16px;
  font-weight: 700;
  color: #dc3545;
  margin-bottom: 12px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #dee2e6;
}

.no-products {
  text-align: center;
  padding: 60px 20px;
  color: #6c757d;
  background: #f8f9fa;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Responsive design */
@media (max-width: 1024px) {
  .content-wrapper {
    flex-direction: column;
  }

  .filter-section {
    width: 100%;
    margin-bottom: 20px;
  }

  .products-container {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .main-section {
    padding: 12px;
    border-radius: 8px;
  }

  .search-section {
    padding: 12px;
  }

  .additional-filters {
    flex-direction: column;
    gap: 8px;
  }

  .filter-btn {
    padding: 8px 12px;
    font-size: 13px;
  }

  .product-image-container {
    height: 160px;
  }

  .product-image {
    height: 136px;
  }

  .product-name {
    font-size: 13px;
    height: 32px;
  }

  .product-price {
    font-size: 15px;
  }

  .products-count {
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .additional-filters {
    flex-direction: column;
    width: 100%;
    gap: 8px;
  }

  .filter-btn {
    width: 100%;
    justify-content: center;
  }

  .quick-price-filters {
    justify-content: center;
  }

  .quick-filter-btn {
    flex: 1;
    min-width: 0;
  }

  .chosen-filters-header {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }

  .search-wrapper {
    margin-bottom: 8px;
  }
}
.el-button.add-to-compare {
  background-color: #e6f7ff;
  border: 1px dashed #409EFF;
  color: #409EFF;
  font-weight: 600;
  font-size: 13px;
  width: 100%;
  border-radius: 6px;
  transition: all 0.3s ease;
  padding: 6px 0;
}

.el-button.add-to-compare:hover {
  background-color: #d0ecff;
  color: #1d78c1;
}

.el-button.already-added {
  background-color: #67c23a;
  color: white;
  width: 100%;
  font-weight: 600;
  font-size: 13px;
  border: none;
  border-radius: 6px;
  padding: 6px 0;
}

.el-icon {
  margin-right: 4px;
}
</style>
