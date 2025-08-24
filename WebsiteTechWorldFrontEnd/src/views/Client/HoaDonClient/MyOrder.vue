<template>
  <div class="page-container">
    <!-- left -->
    <aside class="sidebar">
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">Bộ Lọc Đơn Hàng</h2>
          <span v-if="isFiltered" class="filter-badge">Đang lọc</span>
        </div>
        <div class="card-content space-y-6">
          <div class="space-y-2">
            <label for="status-filter" class="label">Trạng thái</label>
            <select
              id="status-filter"
              v-model="filterStatus"
              @change="handleStatusFilterChange"
              class="select-input"
            >
              <option value="all">Tất cả</option>
              <option
                v-for="status in orderStatuses"
                :key="status"
                :value="status"
              >
                {{ status }}
              </option>
            </select>
          </div>
          <div class="space-y-2">
            <label class="label">Khoảng giá</label>
            <div class="flex-group">
              <input
                type="text"
                placeholder="Min"
                v-model="displayMinPrice"
                class="text-input flex-1"
              />
              <span class="separator">-</span>
              <input
                type="text"
                placeholder="Max"
                v-model="displayMaxPrice"
                class="text-input flex-1"
              />
            </div>
            <div
              v-if="
                filterMinPrice &&
                filterMaxPrice &&
                filterMinPrice > filterMaxPrice
              "
              class="validation-error"
            >
              ⚠️ Giá tối thiểu không thể lớn hơn giá tối đa
            </div>
          </div>
          <div class="space-y-2">
            <label class="label">Khoảng ngày</label>
            <div class="space-y-2">
              <div>
                <label for="start-date" class="sr-only">Ngày bắt đầu</label>
                <input
                  id="start-date"
                  type="date"
                  v-model="filterStartDate"
                  class="text-input"
                />
              </div>
              <div>
                <label for="end-date" class="sr-only">Ngày kết thúc</label>
                <input
                  id="end-date"
                  type="date"
                  v-model="filterEndDate"
                  class="text-input"
                />
              </div>
            </div>
            <div
              v-if="
                filterStartDate &&
                filterEndDate &&
                filterStartDate > filterEndDate
              "
              class="validation-error"
            >
              ⚠️ Ngày bắt đầu không thể sau ngày kết thúc
            </div>
          </div>
        </div>
        <div class="card-footer flex-col space-y-2">
          <button
            @click="handleApplyFilters"
            class="button primary-button w-full"
          >
            Áp dụng bộ lọc
          </button>
          <button
            @click="handleResetFilters"
            class="button outline-button w-full"
          >
            Đặt lại bộ lọc
          </button>
          <div v-if="isFiltered" class="filter-info">
            <small>{{ formatCurrency(filterStatusText) }}</small>
            <div class="current-filter-status mt-1">
              <small
                ><strong
                  >Đang lọc:
                  {{
                    currentFilterStatus === "all"
                      ? "Tất cả đơn hàng"
                      : currentFilterStatus
                  }}</strong
                ></small
              >
            </div>
          </div>
        </div>
      </div>
    </aside>

    <!-- right -->
    <main class="main-content">
      <header class="header">
        <div class="header-right">
          <div class="search-container">
            <SearchIcon class="search-icon" />
            <input
              type="search"
              placeholder="tìm theo tên sản phẩm, mã vận"
              class="search-input"
              v-model="searchTerm"
            />
          </div>
        </div>
      </header>

      <main class="main-content">
        <div class="tabs-container">
          <div class="tabs-list">
            <button
              :class="['tab-trigger', { active: isTabActive('all') }]"
              @click="setActiveTab('all')"
            >
              Tất cả
            </button>
            <button
              :class="['tab-trigger', { active: isTabActive('Chờ xử lý') }]"
              @click="setActiveTab('Chờ xử lý')"
            >
              Chờ xử lý
            </button>
            <button
              :class="['tab-trigger', { active: isTabActive('Chờ vận chuyển') }]"
              @click="setActiveTab('Chờ vận chuyển')"
            >
              Chờ vận chuyển
            </button>
            <button
              :class="['tab-trigger', { active: isTabActive('Chờ giao hàng') }]"
              @click="setActiveTab('Chờ giao hàng')"
            >
              Chờ giao hàng
            </button>
            <button
              :class="['tab-trigger', { active: isTabActive('Hoàn thành') }]"
              @click="setActiveTab('Hoàn thành')"
            >
              Hoàn thành
            </button>
            <button
              :class="['tab-trigger', { active: isTabActive('Đã hủy') }]"
              @click="setActiveTab('Đã hủy')"
            >
              Đã hủy
            </button>
            <button
              :class="[
                'tab-trigger',
                { active: isTabActive('Trả hàng/Hoàn tiền') },
              ]"
              @click="setActiveTab('Trả hàng/Hoàn tiền')"
            >
              Trả hàng/Hoàn tiền
            </button>
          </div>
        </div>

        <div class="order-list">
          <div v-if="allOrderValue.length === 0" class="empty-state">
            Không tìm thấy đơn hàng nào phù hợp.
          </div>
          <div
            v-for="order in allOrderValue"
            :key="order.idHoaDon"
            class="order-card"
          >
            <div class="order-mvd" style="margin: 10px 3px">
              <b>Mã hóa đơn: {{ order.maHoaDon }}</b> <b v-if="order.maVanDon">|| Mã vận đơn: {{ order.maVanDon }}</b> 
            </div>
            <div class="order-status-bar">
              <div class="order-status">
                🧾 Trạng thái đơn: <span>{{ order.trangThaiGiaoHang }}</span>
              </div>
              <div class="payment-status">
                💳 Thanh toán: <span>{{ order.trangThaiThanhToan }}</span>
              </div>
            </div>
            <div class="order-products">
              <div
                v-for="product in order.myOrderClientResponseList"
                :key="product.idSanPhamChiTiet"
                class="product-item"
              >
                <img
                  :src="product.urlImage"
                  :alt="product.tenSanPham"
                  class="product-image"
                />
                <div class="product-details">
                  <div class="product-name">{{ product.tenSanPham }}</div>
                  <div class="product-variant">
                    Phân loại hàng:
                    {{ product.colorName + " " + product.dungLuongRom }}
                  </div>
                  <div class="product-quantity">
                    Số lượng: x{{ product.soLuong }}
                  </div>
                </div>
                <div class="product-prices">
                  <span class="discounted-price"
                    >{{ formatCurrency(product.giaSanPham) }}₫</span
                  >
                </div>
              </div>
            </div>

            <div class="order-footer">
              <div class="order-total">
                Thành tiền:
                <span class="total-amount"
                  >{{ formatCurrency(order.thanhTien) }} VNĐ</span
                >
              </div>
              <div class="order-actions">
                <button class="action-button buy-again-button">Mua Lại</button>
                <button
                  class="action-button contact-seller-button"
                  @click="contactSeller"
                >
                  Liên Hệ Người Bán
                </button>
                <button
                  v-if="
                    order.trangThaiThanhToan === 'Hoàn tất' && !order.daDanhGia
                  "
                  class="action-button rate-button"
                  @click="
                    openRateDialog(
                      order.idHoaDon,
                      order.myOrderClientResponseList
                    )
                  "
                >
                  Đánh giá
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="totalElements > pageSizeMyOrder" class="pagination-controls">
          <button
            class="pagination-button"
            :disabled="currentPage === 0"
            @click="prevPage()"
          >
            Trước
          </button>
          <span v-for="page in totalPages" :key="page">
            <button
              :class="[
                'pagination-button',
                { active: currentPage === page - 1 },
              ]"
              @click="changePage(page - 1)"
            >
              {{ page }}
            </button>
          </span>
          <button
            class="pagination-button"
            :disabled="currentPage === totalPages - 1"
            @click="nextPage()"
          >
            Sau
          </button>
        </div>
      </main>
    </main>
    <section class="orders-stats">
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">Thống Kê Đơn Hàng</h2>
        </div>
        <div class="card-content space-y-4">
          <p>
            Tổng đơn: <strong>{{ totalOrders }}</strong>
          </p>
          <p>
            Đã giao: <strong>{{ deliveredOrders }}</strong>
          </p>
          <p>
            Đơn mua trực tiếp: <strong>{{ totalOrders - deliveredOrders }}</strong>
          </p>
          <p>
            Chờ xử lý: <strong>{{ pendingOrders }}</strong>
          </p>
          <p>
            Tổng tiền mua: <strong>₫{{ formatCurrency(totalRevenue) }}</strong>
          </p>
        </div>
      </div>
    </section>
    <!-- Thêm dialog đánh giá -->
    <RateOrderDialog
      :is-open="isRateDialogOpen"
      :order-id="selectedOrderId"
      :order-products="selectedOrderProducts"
      :id-san-pham-chi-tiet-list="idSanPhamChiTietList"
      @close="closeRateDialog"
      @submit="submitRating"
    />
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from "vue";
import { SearchIcon } from "lucide-vue-next";
import {
  getHoaDonAndIdChiTietHoaDon,
  getMyOrder,
} from "@/Service/ClientService/HoaDon/MyOrderClient";
import RateOrderDialog from "@/components/Admin/dialogs/DialogDanhGiaSao.vue";
import { DanhGiaSanPhamClientService } from "@/Service/ClientService/DanhGiaSanPham/DanhGiaSanPhamClientService";
import { MediaDanhGiaClientService } from "@/Service/ClientService/MediaDanhGiaClientService/MediaDanhGiaClientService";
import { useToast } from "vue-toastification";
// Toast instance
const toast = useToast();

// ========== STATE VARIABLES ==========
const allOrderValue = ref([]);
const pageNoMyOrder = ref(0);
const pageSizeMyOrder = ref(5);
const totalElements = ref(0);
const totalPages = ref(0);
const currentPage = ref(0);
const searchTerm = ref("");
const isUserMenuOpen = ref(false);
const activeOrderActions = ref(null);
const isRateDialogOpen = ref(false);
const selectedOrderId = ref(null);
const selectedOrderProducts = ref([]);
const idSanPhamChiTietList = ref([]);
const existingRatingData = ref([]);
const user = ref(JSON.parse(localStorage.getItem('user')) || null);



// ========== FILTERING STATE ==========
const activeTab = ref("all");
const filterStatus = ref("all");
const filterMinPrice = ref(null);
const filterMaxPrice = ref(null);
const filterStartDate = ref(null);
const filterEndDate = ref(null);
const orderStatuses = ref([
  "Chờ xử lý",
  "Chờ thanh toán",
  "Chờ vận chuyển",
  "Chờ giao hàng",
  "Hoàn thành",
  "Đã hủy",
  "Trả hàng/Hoàn tiền",
]);

// Computed để đồng bộ tab và combobox
const currentFilterStatus = computed(() => {
  // Ưu tiên tab nếu không phải 'all'
  if (activeTab.value !== "all") {
    return activeTab.value;
  }
  // Nếu không thì dùng filter status
  return filterStatus.value;
});

// Hàm format tiền
const formatCurrency = (value) => {
  if (!value && value !== 0) return "";
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
};

// Computed với getter/setter để hiển thị format nhưng vẫn lưu giá trị số
const displayMinPrice = computed({
  get() {
    return filterMinPrice.value !== null
      ? formatCurrency(filterMinPrice.value)
      : "";
  },
  set(val) {
    // Loại bỏ tất cả ký tự không phải số
    const numeric = val.replace(/\D/g, "");
    filterMinPrice.value = numeric ? parseInt(numeric) : null;
  },
});

const displayMaxPrice = computed({
  get() {
    return filterMaxPrice.value !== null
      ? formatCurrency(filterMaxPrice.value)
      : "";
  },
  set(val) {
    const numeric = val.replace(/\D/g, "");
    filterMaxPrice.value = numeric ? parseInt(numeric) : null;
  },
});

// Thống kê đơn hàng theo logic business mới
const totalOrders = computed(() => allOrderValue.value.length);

const deliveredOrders = computed(
  () =>
    allOrderValue.value.filter(
      (order) =>
        // Đơn online: DELIVERED
        (order.trangThaiDonHang &&
          order.trangThaiDonHang.includes("Đã giao")) ||
        // Đơn offline: COMPLETED
        (order.trangThaiThanhToan &&
          order.trangThaiThanhToan.includes("Hoàn tất"))
    ).length
);

const pendingOrders = computed(
  () =>
    allOrderValue.value.filter(
      (order) =>
        // Đơn chờ thanh toán: PENDING
        (order.trangThaiThanhToan &&
          order.trangThaiThanhToan.includes("Chờ xử lý")) ||
        // Đơn đang vận chuyển: CONFIRM, PACKED, SHIPPING
        (order.trangThaiDonHang &&
          (order.trangThaiDonHang.includes("Đã xác nhận") ||
            order.trangThaiDonHang.includes("Đã đóng gói") ||
            order.trangThaiDonHang.includes("Đang giao"))) ||
        // Đơn chờ giao hàng: READYFORPICKUP
        (order.trangThaiDonHang &&
          order.trangThaiDonHang.includes("Sẵn sàng giao"))
    ).length
);

const cancelledOrders = computed(
  () =>
    allOrderValue.value.filter(
      (order) =>
        // Đơn online hủy: CANCELLED
        (order.trangThaiDonHang && order.trangThaiDonHang.includes("Đã hủy")) ||
        // Đơn offline hủy: CANCELLED
        (order.trangThaiThanhToan &&
          order.trangThaiThanhToan.includes("Đã hủy"))
    ).length
);

const returnedOrders = computed(
  () =>
    allOrderValue.value.filter(
      (order) =>
        // Đơn trả hàng: RETURNED, FAILED
        order.trangThaiDonHang &&
        (order.trangThaiDonHang.includes("Đã trả lại") ||
          order.trangThaiDonHang.includes("Giao thất bại"))
    ).length
);
const totalRevenue = computed(() =>
  allOrderValue.value.reduce((sum, order) => sum + order.thanhTien, 0)
);

// Computed cho trạng thái filter
const isFiltered = computed(() => {
  return (
    filterMinPrice.value ||
    filterMaxPrice.value ||
    filterStartDate.value ||
    filterEndDate.value ||
    (searchTerm.value && searchTerm.value.trim().length > 0) ||
    currentFilterStatus.value !== "all"
  );
});

// Computed cho filter status text
const filterStatusText = computed(() => {
  const filters = [];

  const effectiveStatus = currentFilterStatus.value;
  if (effectiveStatus !== "all") {
    filters.push(`Trạng thái: ${effectiveStatus}`);
  }

  if (filterMinPrice.value || filterMaxPrice.value) {
    filters.push(
      `Giá: ${filterMinPrice.value || "0"} - ${filterMaxPrice.value || "∞"}`
    );
  }

  if (filterStartDate.value || filterEndDate.value) {
    filters.push(
      `Từ ${filterStartDate.value || "..."} đến ${filterEndDate.value || "..."}`
    );
  }

  if (searchTerm.value && searchTerm.value.trim()) {
    filters.push(`Tìm: "${searchTerm.value.trim()}"`);
  }

  return filters.length > 0 ? filters.join(" | ") : "Không có bộ lọc";
});

// Computed để kiểm tra tab active
const isTabActive = (tabName) => {
  return activeTab.value === tabName;
};

// ========== MAIN API FUNCTION ==========
const allMyOrder = async () => {
  try {
    if (!user.value?.id) {
      toast.error("Vui lòng đăng nhập để xem đơn hàng!");
      return;
    }

    // Chuẩn bị parameters cho API
    const params = {
      pageNo: currentPage.value,
      pageSize: pageSizeMyOrder.value,
    };

    // Thêm keyword parameter
    if (searchTerm.value && searchTerm.value.trim()) {
      params.keyword = searchTerm.value.trim();
    }

    // Thêm filter parameters
    if (filterMinPrice.value !== null && filterMinPrice.value !== "") {
      params.minPrice = filterMinPrice.value;
    }
    if (filterMaxPrice.value !== null && filterMaxPrice.value !== "") {
      params.maxPrice = filterMaxPrice.value;
    }
    if (filterStartDate.value) {
      params.startDate = filterStartDate.value + "T00:00:00";
    }
    if (filterEndDate.value) {
      params.endDate = filterEndDate.value + "T23:59:59";
    }

    // ✨ QUAN TRỌNG: Xử lý trạng thái từ tab hoặc combobox
    const effectiveStatus = currentFilterStatus.value;
    if (effectiveStatus && effectiveStatus !== "all") {
      // Map frontend status sang backend parameter theo logic business
      const statusMapping = {
        // Tab "Chờ thanh toán" -> Trạng thái thanh toán PENDING
        "Chờ xử lý": "Chờ xử lý",

        // Tab "Vận chuyển" -> Các trạng thái giao hàng: CONFIRM, PACKED, READYFORPICKUP, SHIPPING
        "Chờ vận chuyển": "Chờ vận chuyển",

        // Tab "Chờ giao hàng" -> Trạng thái giao hàng READYFORPICKUP
        "Chờ giao hàng": "Chờ giao hàng",

        // Tab "Hoàn thành" -> Trạng thái giao hàng DELIVERED hoặc thanh toán COMPLETED
        "Hoàn thành": "Hoàn thành",

        // Tab "Đã hủy" -> Trạng thái giao hàng CANCELLED hoặc thanh toán CANCELLED
        "Đã hủy": "Đã hủy",

        // Tab "Trả hàng/Hoàn tiền" -> Trạng thái giao hàng RETURNED hoặc FAILED
        "Trả hàng/Hoàn tiền": "Trả hàng/Hoàn tiền",
      };

      params.trangThaiGiaoHang =
        statusMapping[effectiveStatus] || effectiveStatus;

      console.log(
        `🔄 Frontend status "${effectiveStatus}" -> Backend param "${params.trangThaiGiaoHang}"`
      );
    }

    console.log("Gọi API với parameters:", params);

    const res = await getMyOrder(params);
    console.log("Response từ backend:", res);

    const orders = res.data.content || [];

    // Kiểm tra đánh giá
    const ordersWithCheck = await Promise.all(
      orders.map(async (order) => {
        try {
          const response = await DanhGiaSanPhamClientService.checkDanhGia(
            order.idHoaDon,
            user.value.id
          );
          return { ...order, daDanhGia: response.daDanhGia };
        } catch (err) {
          console.error(
            `Lỗi kiểm tra đánh giá cho đơn hàng ${order.idHoaDon}:`,
            err
          );
          return { ...order, daDanhGia: false };
        }
      })
    );

    // Cập nhật state
    allOrderValue.value = ordersWithCheck;
    totalElements.value = res.data.totalElements || 0;
    totalPages.value = res.data.totalPages || 0;
    console.log(totalElements.value)

    console.log("✅ Kết quả cuối cùng:", ordersWithCheck);
  } catch (error) {
    console.error("❌ Lỗi khi lấy đơn hàng:", error);
    toast.error("Không thể tải danh sách đơn hàng. Vui lòng thử lại.");
  }
};

// ========== TAB AND FILTER FUNCTIONS ==========

// Hàm xử lý khi click tab - đồng bộ với combobox
const setActiveTab = async (tab) => {
  console.log("🏷️ Chọn tab:", tab);

  activeTab.value = tab;

  // Đồng bộ combobox với tab được chọn
  if (tab === "all") {
    filterStatus.value = "all";
  } else {
    filterStatus.value = tab;
  }

  currentPage.value = 0;
  activeOrderActions.value = null;

  await allMyOrder();
};

// Hàm xử lý khi thay đổi combobox - đồng bộ với tab
const handleStatusFilterChange = async () => {
  console.log("🔽 Chọn combobox:", filterStatus.value);

  // Đồng bộ tab với combobox được chọn
  if (filterStatus.value === "all") {
    activeTab.value = "all";
  } else {
    activeTab.value = filterStatus.value;
  }

  currentPage.value = 0;

  await allMyOrder();
};

// Hàm apply filters
const handleApplyFilters = async () => {
  console.log("🔍 Áp dụng bộ lọc:", {
    status: filterStatus.value,
    minPrice: filterMinPrice.value,
    maxPrice: filterMaxPrice.value,
    startDate: filterStartDate.value,
    endDate: filterEndDate.value,
    keyword: searchTerm.value,
  });

  // Validation
  if (filterMinPrice.value && filterMaxPrice.value) {
    if (parseFloat(filterMinPrice.value) > parseFloat(filterMaxPrice.value)) {
      toast.warning("⚠️ Giá tối thiểu không thể lớn hơn giá tối đa!");
      return;
    }
  }

  if (filterStartDate.value && filterEndDate.value) {
    if (new Date(filterStartDate.value) > new Date(filterEndDate.value)) {
      toast.warning("⚠️ Ngày bắt đầu không thể sau ngày kết thúc!");
      return;
    }
  }

  // Reset về trang đầu và gọi API
  currentPage.value = 0;
  await allMyOrder();
  console.log("✅ Đã áp dụng bộ lọc!");
};

// Hàm reset filters
const handleResetFilters = async () => {
  console.log("🔄 Đặt lại tất cả bộ lọc");

  // Reset tất cả filters
  activeTab.value = "all";
  filterStatus.value = "all";
  filterMinPrice.value = null;
  filterMaxPrice.value = null;
  filterStartDate.value = null;
  filterEndDate.value = null;
  searchTerm.value = "";
  currentPage.value = 0;

  await allMyOrder();
  toast.success("Đã đặt lại tất cả bộ lọc!");
};

// ========== PAGINATION FUNCTIONS ==========
const changePage = (page) => {
  currentPage.value = page;
};

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++;
  }
};

// ========== UI FUNCTIONS ==========
const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value;
  activeOrderActions.value = null;
};

const toggleOrderActions = (orderId) => {
  activeOrderActions.value =
    activeOrderActions.value === orderId ? null : orderId;
  isUserMenuOpen.value = false;
};

window.addEventListener('click', (event) => {
  if (!event.target.closest('.dropdown-menu')) {
    isUserMenuOpen.value = false;
    activeOrderActions.value = null;
  }
});


const openRateDialog = async (orderId, products) => {
  if (!user.value?.id) {
    toast.error('Vui lòng đăng nhập để đánh giá!');
    return;
  }

  selectedOrderId.value = orderId;
  selectedOrderProducts.value = Array.from(products);
  // isEditing.value = false;
  existingRatingData.value = [];

  try {
    const chiTietList = await getHoaDonAndIdChiTietHoaDon(orderId);
    if (!Array.isArray(chiTietList.data)) {
      toast.error('Dữ liệu chi tiết hóa đơn không hợp lệ!');
      return;
    }

    idSanPhamChiTietList.value = chiTietList.data.map(item => ({
      idSanPhamChiTiet: item.idSanPhamChiTiet,
      idChiTietHoaDon: item.idChiTietHoaDon,
      tenSanPham: products.find(p => p.idSanPhamChiTiet === item.idSanPhamChiTiet)?.tenSanPham || 'Unknown',
    }));

    selectedOrderProducts.value = selectedOrderProducts.value.map(product => ({
      ...product,
      idChiTietHoaDon: chiTietList.data.find(item => item.idSanPhamChiTiet === product.idSanPhamChiTiet)?.idChiTietHoaDon || null,
    }));

    if (selectedOrderProducts.value.some(p => !p.idSanPhamChiTiet || !p.idChiTietHoaDon)) {
      toast.error('Dữ liệu sản phẩm không hợp lệ!');
      return;
    }

    isRateDialogOpen.value = true;
  } catch (error) {
    console.error('Lỗi khi lấy chi tiết hóa đơn:', error);
    toast.error('Không thể lấy chi tiết hóa đơn. Vui lòng thử lại.');
  }
};

const closeRateDialog = () => {
  isRateDialogOpen.value = false;
  selectedOrderId.value = null;
  selectedOrderProducts.value = [];
  idSanPhamChiTietList.value = [];
};

const submitRating = async ({ payload }) => {
  console.log('submitRating nhận payload:', JSON.stringify(payload, null, 2));
  try {
    // 1. Kiểm tra đăng nhập
    if (!user.value?.id) {
      toast.warning("⚠️ Vui lòng đăng nhập để đánh giá!");
      return;
    }

    const { idHoaDon, ratings, trangThaiDanhGia } = payload;
    console.log('Đang lấy chiTietList cho idHoaDon:', idHoaDon);

    const chiTietList = await getHoaDonAndIdChiTietHoaDon(idHoaDon);

    // 2. Kiểm tra dữ liệu chi tiết hóa đơn
    if (!Array.isArray(chiTietList.data) || chiTietList.data.length === 0) {
      console.error('Dữ liệu chiTietList không hợp lệ:', chiTietList);
      toast.error('Không tìm thấy chi tiết hóa đơn!');
      return;
    }

    if (chiTietList.data.length !== ratings.length) {
      console.error('Số lượng đánh giá không khớp:', {
        ratingsCount: ratings.length,
        chiTietListCount: chiTietList.data.length,
      });
      toast.error('Số lượng đánh giá không khớp với sản phẩm trong hóa đơn!');
      return;
    }

    const isValid = ratings.every(rating =>
      chiTietList.data.some(chiTiet => chiTiet.idSanPhamChiTiet === rating.idSanPhamChiTiet)
    );
    if (!isValid) {
      console.error('Dữ liệu đánh giá không hợp lệ:', ratings);
      toast.error('Một hoặc nhiều sản phẩm đánh giá không thuộc hóa đơn này!');
      return;
    }

    // 3. Xử lý tạo đánh giá
    console.log('Đang xử lý tạo mới cho ratings:', ratings);
    const danhGiaPromises = ratings.map(async (rating) => {
      const request = {
        idHoaDon,
        idSanPhamChiTiet: rating.idSanPhamChiTiet,
        idChiTietHoaDon: rating.idChiTietHoaDon,
        idKhachHang: user.value.id,
        soSao: rating.soSao,
        noiDung: rating.noiDung,
        trangThaiDanhGia,
      };
      try {
        const danhGiaResponse = await DanhGiaSanPhamClientService.taoMoiDanhGia(request);
        console.log(`Tạo đánh giá cho sản phẩm ${rating.idSanPhamChiTiet}:`, danhGiaResponse);

        const mediaPromises = [];

        // Upload ảnh
        if (Array.isArray(rating.imageFiles)) {
          rating.imageFiles.forEach(file => {
            if (file && file.name && file.size && file.type) {
              mediaPromises.push(
                MediaDanhGiaClientService.uploadMedia(file, danhGiaResponse.idDanhGia)
                  .then(uploadResponse => {
                    console.log(`Tải lên ảnh ${file.name} thành công`);
                    return { status: 'fulfilled', value: uploadResponse };
                  })
                  .catch(err => {
                    console.error(`Lỗi khi tải ảnh ${file.name}:`, err);
                    toast.error(`Không thể tải lên ảnh ${file.name}`);
                    return { status: 'rejected', reason: err };
                  })
              );
            }
          });
        }

        // Upload video
        if (Array.isArray(rating.videoFiles)) {
          rating.videoFiles.forEach(file => {
            if (file && file.name && file.size && file.type) {
              mediaPromises.push(
                MediaDanhGiaClientService.uploadMedia(file, danhGiaResponse.idDanhGia)
                  .then(uploadResponse => {
                    console.log(`Tải lên video ${file.name} thành công`);
                    return { status: 'fulfilled', value: uploadResponse };
                  })
                  .catch(err => {
                    console.error(`Lỗi khi tải video ${file.name}:`, err);
                    toast.error(`Không thể tải lên video ${file.name}`);
                    return { status: 'rejected', reason: err };
                  })
              );
            }
          });
        }

        await Promise.allSettled(mediaPromises);

        return { status: 'fulfilled', value: danhGiaResponse };
      } catch (error) {
        console.error(`Lỗi khi tạo đánh giá cho sản phẩm ${rating.idSanPhamChiTiet}:`, error);

        const backendMsg =
          error?.response?.data?.message ||
          error?.message ||
          'Lỗi không xác định từ server';

        toast.error(
          `Tạo đánh giá cho sản phẩm ${rating.idSanPhamChiTiet} thất bại: ${backendMsg}`
        );

        return { status: 'rejected', reason: error };
      }
    });

    const danhGiaResults = await Promise.allSettled(danhGiaPromises);

    // 4. Tổng hợp kết quả
    const successCount = danhGiaResults.filter(r => r.status === 'fulfilled').length;
    const errorCount = danhGiaResults.filter(r => r.status === 'rejected').length;

    if (errorCount > 0 && successCount === 0) {
      toast.error('Gửi tất cả đánh giá thất bại!');
      return;
    } else if (errorCount > 0 && successCount > 0) {
      toast.warning(`Gửi thành công ${successCount}, thất bại ${errorCount} đánh giá!`);
      return;
    } else {
      toast.success('Gửi đánh giá thành công!');
    }

    // 5. Làm mới đơn hàng
    console.log('Đang làm mới danh sách đơn hàng và đóng dialog');
    await allMyOrder();
    closeRateDialog();

  } catch (error) {
    console.error('Lỗi trong submitRating:', error);

    const statusCode = error.response?.status;
    const backendMsg = error.response?.data?.message || error.message || 'Lỗi không xác định';

    if (statusCode === 400) {
      toast.error(`Yêu cầu không hợp lệ: ${backendMsg}`);
    } else if (statusCode === 404) {
      toast.error(`Không tìm thấy tài nguyên: ${backendMsg}`);
    } else if (statusCode === 500) {
      toast.error(`Lỗi máy chủ: ${backendMsg}`);
    } else {
      toast.error(`Gửi/cập nhật đánh giá thất bại: ${backendMsg}`);

    }
  }
};




onMounted(async () => {
  await allMyOrder();
});


// ========== UTILITY FUNCTIONS ==========
const getOrderStatusClass = (status) => {
  switch (status) {
    case "Hoàn thành":
      return "status-completed";
    case "Đang vận chuyển":
      return "status-shipping";
    case "Chờ thanh toán":
      return "status-pending-payment";
    case "Chờ vận chuyển":
      return "status-in-transit";
    case "Chờ giao hàng":
      return "status-awaiting-delivery";
    case "Đã hủy":
      return "status-cancelled";
    case "Trả hàng/Hoàn tiền":
      return "status-return-refund";
    default:
      return "";
  }
};

// ========== WATCHERS ==========

// Debounce search
let searchTimeout = null;
watch(searchTerm, (newValue) => {
  if (searchTimeout) {
    clearTimeout(searchTimeout);
  }

  searchTimeout = setTimeout(async () => {
    if (newValue.trim().length >= 2 || newValue.trim().length === 0) {
      currentPage.value = 0;
      await allMyOrder();
    }
  }, 500);
});

// Watch pagination
watch(currentPage, async () => {
  await allMyOrder();
});

// Watch để đảm bảo đồng bộ khi có thay đổi từ bên ngoài
watch(filterStatus, (newValue, oldValue) => {
  // Tránh infinite loop
  if (newValue !== oldValue && newValue !== currentFilterStatus.value) {
    handleStatusFilterChange();
  }
});

// ========== LIFECYCLE HOOKS ==========
onMounted(async () => {
  // Initialize Tawk chat
  window.Tawk_API = window.Tawk_API || {};
  window.Tawk_LoadStart = new Date();
  const s1 = document.createElement("script");
  const s0 = document.getElementsByTagName("script")[0];
  s1.async = true;
  s1.src = "https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5";
  s1.charset = "UTF-8";
  s1.setAttribute("crossorigin", "*");
  s0.parentNode.insertBefore(s1, s0);

  // Load orders
  await allMyOrder();
});

// Event listener for dropdown menus
window.addEventListener("click", (event) => {
  if (!event.target.closest(".dropdown-menu")) {
    isUserMenuOpen.value = false;
    activeOrderActions.value = null;
  }
});
</script>

<style scoped src="@/style/HoaDon/MyOrder.css">
.rate-button {
  background-color: #28a745;
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.rate-button:hover {
  background-color: #218838;
}
</style>
