<template>
  <div class="page-container">
    <!-- left -->
    <aside class="sidebar">
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">Bộ Lọc Đơn Hàng</h2>
        </div>
        <div class="card-content space-y-6">
          <div class="space-y-2">
            <label for="status-filter" class="label">Trạng thái</label>
            <select id="status-filter" v-model="filterStatus" class="select-input">
              <option value="all">Tất cả</option>
              <option v-for="status in orderStatuses" :key="status" :value="status">
                {{ status }}
              </option>
            </select>
          </div>
          <div class="space-y-2">
            <label class="label">Khoảng giá</label>
            <div class="flex-group">
              <input type="number" placeholder="Min" v-model="filterMinPrice" class="text-input flex-1" />
              <span class="separator">-</span>
              <input type="number" placeholder="Max" v-model="filterMaxPrice" class="text-input flex-1" />
            </div>
          </div>
          <div class="space-y-2">
            <label class="label">Khoảng ngày</label>
            <div class="space-y-2">
              <div>
                <label for="start-date" class="sr-only">Ngày bắt đầu</label>
                <input id="start-date" type="date" v-model="filterStartDate" class="text-input" />
              </div>
              <div>
                <label for="end-date" class="sr-only">Ngày kết thúc</label>
                <input id="end-date" type="date" v-model="filterEndDate" class="text-input" />
              </div>
            </div>
          </div>
        </div>
        <div class="card-footer flex-col space-y-2">
          <button @click="handleApplyFilters" class="button primary-button w-full">
            Áp dụng bộ lọc
          </button>
          <button @click="handleResetFilters" class="button outline-button w-full">
            Đặt lại bộ lọc
          </button>
        </div>
      </div>
    </aside>

    <!-- right -->
    <main class="main-content">
      <header class="header">
        <div class="header-right">
          <div class="search-container">
            <SearchIcon class="search-icon" />
            <input type="search" placeholder="Bạn có thể tìm kiếm theo ID đơn hàng hoặc Tên Sản phẩm"
              class="search-input" v-model="searchTerm" />
          </div>
        </div>
      </header>

      <main class="main-content">
        <div class="tabs-container">
          <div class="tabs-list">
            <button :class="['tab-trigger', { active: activeTab === 'all' }]" @click="setActiveTab('all')">
              Tất cả
            </button>
            <button :class="['tab-trigger', { active: activeTab === 'Chờ thanh toán' }]"
              @click="setActiveTab('Chờ thanh toán')">
              Chờ thanh toán
            </button>
            <button :class="['tab-trigger', { active: activeTab === 'Vận chuyển' }]"
              @click="setActiveTab('Vận chuyển')">
              Vận chuyển
            </button>
            <button :class="['tab-trigger', { active: activeTab === 'Chờ giao hàng' }]"
              @click="setActiveTab('Chờ giao hàng')">
              Chờ giao hàng
            </button>
            <button :class="['tab-trigger', { active: activeTab === 'Hoàn thành' }]"
              @click="setActiveTab('Hoàn thành')">
              Hoàn thành
            </button>
            <button :class="['tab-trigger', { active: activeTab === 'Đã hủy' }]" @click="setActiveTab('Đã hủy')">
              Đã hủy
            </button>
            <button :class="['tab-trigger', { active: activeTab === 'Trả hàng/Hoàn tiền' }]"
              @click="setActiveTab('Trả hàng/Hoàn tiền')">
              Trả hàng/Hoàn tiền
            </button>
          </div>
        </div>

        <div class="order-list">
          <div v-if="allOrderValue.length === 0" class="empty-state">
            Không tìm thấy đơn hàng nào phù hợp.
          </div>
          <div v-for="order in allOrderValue" :key="order.idHoaDon" class="order-card">
            <div class="order-mvd" style="margin: 10px 3px;">
                <b>Mã vận đơn: {{ order.maVanDon }}</b>
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
              <div v-for="product in order.myOrderClientResponseList" :key="product.idSanPhamChiTiet"
                class="product-item">
                <img :src="product.urlImage" :alt="product.tenSanPham" class="product-image" />
                <div class="product-details">
                  <div class="product-name">{{ product.tenSanPham }}</div>
                  <div class="product-variant">Phân loại hàng: {{ product.colorName + product.dungLuongRom }}</div>
                  <div class="product-quantity">x{{ product.soLuong }}</div>
                </div>
                <div class="product-prices">
                  <span class="discounted-price">₫{{ product.giaSanPham }}</span>
                </div>
              </div>
            </div>

            <div class="order-footer">
              <div class="order-total">
                Thành tiền: <span class="total-amount">{{ order.thanhTien }} VNĐ</span>
              </div>
              <div class="order-actions">
                <button class="action-button buy-again-button">Mua Lại</button>
                <button class="action-button contact-seller-button" @click="contactSeller">Liên Hệ Người Bán</button>
                <button v-if="order.trangThaiThanhToan === 'Hoàn tất' && !order.daDanhGia"
                  class="action-button rate-button"
                  @click="openRateDialog(order.idHoaDon, order.myOrderClientResponseList)">
                  Đánh giá
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-if="totalElements > pageSizeMyOrder" class="pagination-controls">
          <button class="pagination-button" :disabled="currentPage === 0" @click="prevPage()">
            Trước
          </button>
          <span v-for="page in totalPages" :key="page">
            <button :class="['pagination-button', { active: currentPage === page - 1 }]" @click="changePage(page - 1)">
              {{ page }}
            </button>
          </span>
          <button class="pagination-button" :disabled="currentPage === totalPages - 1" @click="nextPage()">
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
          <p>Tổng đơn: <strong>{{ totalOrders }}</strong></p>
          <p>Đã giao: <strong>{{ deliveredOrders }}</strong></p>
          <p>Chờ xử lý: <strong>{{ pendingOrders }}</strong></p>
          <p>Tổng doanh thu: <strong>₫{{ totalRevenue }}</strong></p>
        </div>
      </div>
    </section>
    <!-- Thêm dialog đánh giá -->
    <RateOrderDialog :is-open="isRateDialogOpen" :order-id="selectedOrderId" :order-products="selectedOrderProducts"
      :id-san-pham-chi-tiet-list="idSanPhamChiTietList" @close="closeRateDialog" @submit="submitRating" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue';
import { SearchIcon } from 'lucide-vue-next';
import { getHoaDonAndIdChiTietHoaDon, getMyOrder } from '@/Service/ClientService/HoaDon/MyOrderClient';
import RateOrderDialog from '@/components/Admin/dialogs/DialogDanhGiaSao.vue';
import { DanhGiaSanPhamClientService } from '@/Service/ClientService/DanhGiaSanPham/DanhGiaSanPhamClientService';
import { MediaDanhGiaClientService } from '@/Service/ClientService/MediaDanhGiaClientService/MediaDanhGiaClientService';

// State
const allOrderValue = ref([]);
const pageNoMyOrder = ref(0);
const pageSizeMyOrder = ref(5);
const totalElements = ref(0);
const totalPages = ref(0);
const currentPage = ref(0);
const searchTerm = ref('');
const activeTab = ref('all');
const isUserMenuOpen = ref(false);
const activeOrderActions = ref(null);
const isRateDialogOpen = ref(false);
const selectedOrderId = ref(null);
const selectedOrderProducts = ref([]);
const idSanPhamChiTietList = ref([]);
const user = ref(JSON.parse(localStorage.getItem('user')) || null);
import { useToast } from 'vue-toastification';
const toast = useToast();

// Bộ lọc
const filterStatus = ref('all');
const filterMinPrice = ref(null);
const filterMaxPrice = ref(null);
const filterStartDate = ref(null);
const filterEndDate = ref(null);
const orderStatuses = ref([
  'Chờ thanh toán',
  'Vận chuyển',
  'Chờ giao hàng',
  'Hoàn thành',
  'Đã hủy',
  'Trả hàng/Hoàn tiền',
]);

// Thống kê
const totalOrders = computed(() => allOrderValue.value.length);
const deliveredOrders = computed(() =>
  allOrderValue.value.filter(order => order.trangThaiGiaoHang === 'Hoàn thành').length
);
const pendingOrders = computed(() =>
  allOrderValue.value.filter(order => order.trangThaiGiaoHang === 'Chờ thanh toán').length
);
const totalRevenue = computed(() =>
  allOrderValue.value.reduce((sum, order) => sum + order.thanhTien, 0)
);

onMounted(() => {
  window.Tawk_API = window.Tawk_API || {};
  window.Tawk_LoadStart = new Date();
  const s1 = document.createElement('script');
  const s0 = document.getElementsByTagName('script')[0];
  s1.async = true;
  s1.src = 'https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5';
  s1.charset = 'UTF-8';
  s1.setAttribute('crossorigin', '*');
  s0.parentNode.insertBefore(s1, s0);
});


const contactSeller = () => {
  if (window.Tawk_API?.toggle) {
    window.Tawk_API.toggle();
  } else {
    window.Tawk_API.onLoad = () => window.Tawk_API.toggle();
    setTimeout(() => {
      if (!window.Tawk_API?.toggle) {
        toast.error('Không thể mở chat. Vui lòng thử lại sau.');
      }
    }, 5000);
  }
};


// const allMyOrde = async () => {
//   try {
//     const res = await getMyOrder(currentPage.value, pageSizeMyOrder.value);
//     allOrderValue.value = res.data.content || [];
//     totalElements.value = res.data.totalElements || 0;
//     totalPages.value = res.data.totalPages || 0;
//     console.log('Dữ liệu đơn hàng:', res);
//   } catch (error) {
//     console.error('Lỗi khi lấy đơn hàng:', error);
//     alert('Không thể tải danh sách đơn hàng. Vui lòng thử lại.');
//   }
// };

const allMyOrde = async () => {
  try {
    if (!user.value?.id) {
      toast.error('Vui lòng đăng nhập để xem đơn hàng!');
      return;
    }

    const res = await getMyOrder(currentPage.value, pageSizeMyOrder.value, user.value.id);
    console.log("📦 Dữ liệu thô từ getMyOrder:", res);

    const orders = res.data.content || []; // ✅ Sửa ở đây!
    console.log("✅ Danh sách đơn hàng:", orders);

    const ordersWithCheck = await Promise.all(
      orders.map(async (order) => {
        try {
          const response = await DanhGiaSanPhamClientService.checkDanhGia(order.idHoaDon, user.value.id);
          console.log("✅ Kết quả check:", response);
          return { ...order, daDanhGia: response.daDanhGia };
        } catch (err) {
          console.error(`❌ Lỗi kiểm tra đánh giá cho đơn hàng ${order.idHoaDon}:`, err);
          return { ...order, daDanhGia: false };
        }
      })
    );

    allOrderValue.value = ordersWithCheck;
    totalElements.value = res.data.totalElements || 0;
    totalPages.value = res.data.totalPages || 0;

    console.log("🎯 Dữ liệu đơn hàng sau check đánh giá:", ordersWithCheck);
  } catch (error) {
    console.error('❌ Lỗi khi lấy đơn hàng:', error);
    toast.error('Không thể tải danh sách đơn hàng. Vui lòng thử lại.');
  }
};


const changePage = (page) => {
  currentPage.value = page;
};

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) currentPage.value++;
};

watch(currentPage, () => {
  allMyOrde();
});

const handleApplyFilters = () => {
  console.log('Áp dụng bộ lọc:', {
    status: filterStatus.value,
    minPrice: filterMinPrice.value,
    maxPrice: filterMaxPrice.value,
    startDate: filterStartDate.value,
    endDate: filterEndDate.value,
  });
  allMyOrde();
};

const handleResetFilters = () => {
  filterStatus.value = 'all';
  filterMinPrice.value = null;
  filterMaxPrice.value = null;
  filterStartDate.value = null;
  filterEndDate.value = null;
  allMyOrde();
};

const setActiveTab = (tab) => {
  activeTab.value = tab;
  currentPage.value = 1;
  activeOrderActions.value = null;
};

const toggleUserMenu = () => {
  isUserMenuOpen.value = !isUserMenuOpen.value;
  activeOrderActions.value = null;
};

const toggleOrderActions = (orderId) => {
  activeOrderActions.value = activeOrderActions.value === orderId ? null : orderId;
  isUserMenuOpen.value = false;
};

window.addEventListener('click', (event) => {
  if (!event.target.closest('.dropdown-menu')) {
    isUserMenuOpen.value = false;
    activeOrderActions.value = null;
  }
});

// Dialog đánh giá
const openRateDialog = async (orderId, products) => {
  if (!user.value?.id) {
    console.error('Không thể mở dialog vì thiếu thông tin user:', user.value);
    alert('Vui lòng đăng nhập để đánh giá!');
    return;
  }

  console.log('Mở dialog đánh giá cho đơn hàng:', {
    orderId,
    products: Array.from(products), // Log dữ liệu gốc của products
  });

  selectedOrderId.value = orderId; // Chuyển thành chuỗi để tránh lỗi prop
  selectedOrderProducts.value = Array.from(products); // Chuyển Proxy thành mảng

  try {
    const chiTietList = await getHoaDonAndIdChiTietHoaDon(orderId);
    console.log('API getHoaDonAndIdChiTietHoaDon response:', chiTietList.data);

    // Kiểm tra tính hợp lệ của chiTietList
    if (!chiTietList.data || !Array.isArray(chiTietList.data)) {
      console.error('Dữ liệu chiTietList không hợp lệ:', chiTietList.data);
      alert('Không thể lấy chi tiết hóa đơn. Dữ liệu không hợp lệ!');
      return;
    }

    // Tạo idSanPhamChiTietList
    idSanPhamChiTietList.value = chiTietList.data.map(item => {
      const product = products.find(p => p.idSanPhamChiTiet === item.idSanPhamChiTiet);
      return {
        idSanPhamChiTiet: item.idSanPhamChiTiet,
        idChiTietHoaDon: item.idChiTietHoaDon,
        tenSanPham: product?.tenSanPham || 'Unknown',
      };
    });

    // Bổ sung idChiTietHoaDon vào selectedOrderProducts
    selectedOrderProducts.value = selectedOrderProducts.value.map(product => {
      const chiTiet = chiTietList.data.find(item => item.idSanPhamChiTiet === product.idSanPhamChiTiet);
      return {
        ...product,
        idChiTietHoaDon: chiTiet?.idChiTietHoaDon || null,
      };
    });

    // Kiểm tra tính hợp lệ của selectedOrderProducts
    const invalidProducts = selectedOrderProducts.value.filter(p => !p.idSanPhamChiTiet || !p.idChiTietHoaDon);
    if (invalidProducts.length > 0) {
      console.error('Dữ liệu selectedOrderProducts không hợp lệ:', {
        invalidProducts,
        fullOrderProducts: selectedOrderProducts.value,
        chiTietList: chiTietList.data,
      });
      alert('Dữ liệu sản phẩm không hợp lệ! Vui lòng kiểm tra lại.');
      return;
    }

    // Log dữ liệu trước khi mở dialog
    console.log('Dữ liệu truyền vào DialogDanhGiaSao:', {
      isOpen: isRateDialogOpen.value,
      orderId: selectedOrderId.value,
      idKhachHang: user.value?.id,
      orderProducts: selectedOrderProducts.value,
      idSanPhamChiTietList: idSanPhamChiTietList.value,
    });

    isRateDialogOpen.value = true;
  } catch (error) {
    console.error('Lỗi khi lấy chi tiết hóa đơn:', {
      error: error.message,
      stack: error.stack,
    });
    alert('Không thể lấy chi tiết hóa đơn. Vui lòng thử lại.');
  }
};


const closeRateDialog = () => {
  isRateDialogOpen.value = false;
  selectedOrderId.value = null;
  selectedOrderProducts.value = [];
  idSanPhamChiTietList.value = [];
};

const submitRating = async (ratingData) => {
  try {
    if (!user.value?.id) {
      toast.warning('⚠️ Vui lòng đăng nhập để đánh giá!');
      return;
    }

    const data = ratingData.payload;

    console.log("🔍 Dữ liệu nhận từ dialog:", data);
    console.log("🔍 idHoaDon:", data.idHoaDon);

    const chiTietList = await getHoaDonAndIdChiTietHoaDon(data.idHoaDon);
    const chiTietArray = chiTietList.data;

    if (!chiTietArray || !Array.isArray(chiTietArray) || chiTietArray.length === 0) {
      console.error('Không có chi tiết hóa đơn!');
      toast.error('❌ Không tìm thấy chi tiết hóa đơn.');
      return;
    }

    // Sử dụng data.ratings thay vì data.soSao
    if (chiTietArray.length !== data.ratings.length) {
      console.error('Số lượng chi tiết hóa đơn không khớp với số lượng đánh giá!');
      toast.error('❌ Số lượng đánh giá không khớp với sản phẩm trong hóa đơn.');
      return;
    }

    const isValid = data.ratings.every((rating) => {
      return chiTietArray.some((chiTiet) => chiTiet.idSanPhamChiTiet === rating.idSanPhamChiTiet);
    });

    console.log('🔍 Dữ liệu chi tiết hóa đơn:', chiTietArray);

    if (!isValid) {
      console.error('Dữ liệu không khớp: Có sản phẩm đánh giá không thuộc hóa đơn!');
      toast.error('❌ Một hoặc nhiều sản phẩm đánh giá không thuộc hóa đơn này.');
      return;
    }

    if (data.ratings.length > chiTietArray.length) {
      console.error('Dữ liệu không khớp: Số lượng đánh giá vượt quá số sản phẩm trong hóa đơn!');
      toast.error('❌ Số lượng đánh giá vượt quá số sản phẩm trong hóa đơn.');
      return;
    }

    const danhGiaPromises = data.ratings.map(async (rating, index) => {
      if (!chiTietArray[index]?.idChiTietHoaDon) {
        throw new Error(`Không tìm thấy idChiTietHoaDon cho sản phẩm tại index ${index}`);
      }

      const danhGiaRequest = {
        idHoaDon: data.idHoaDon,
        idSanPhamChiTiet: chiTietArray[index].idSanPhamChiTiet,
        idChiTietHoaDon: chiTietArray[index].idChiTietHoaDon,
        idKhachHang: user.value.id,
        soSao: rating.soSao,
        noiDung: rating.noiDung,
        trangThaiDanhGia: data.trangThaiDanhGia,
      };

      console.log('📤 Gửi yêu cầu đánh giá:', danhGiaRequest);
      return await DanhGiaSanPhamClientService.taoMoiDanhGia(danhGiaRequest);
    });

    const danhGiaResponses = await Promise.all(danhGiaPromises);
    console.log('✅ Phản hồi đánh giá:', danhGiaResponses);

    // Upload media
    const mediaPromises = [];
    const idDanhGia = danhGiaResponses[0]?.idDanhGia;
    if (!idDanhGia) {
      console.error('Không có idDanhGia trong phản hồi:', danhGiaResponses);
      toast.error('❌ Không nhận được id đánh giá từ máy chủ.');
      return;
    }

    for (const rating of data.ratings) {
      for (const file of rating.imageFiles) {
        console.log("📂 Image file chuẩn bị upload:", file.name, "👉 idDanhGia:", idDanhGia);
        mediaPromises.push(MediaDanhGiaClientService.uploadMedia(file, idDanhGia));
      }
      for (const file of rating.videoFiles) {
        console.log("📹 Video file chuẩn bị upload:", file.name, "👉 idDanhGia:", idDanhGia);
        mediaPromises.push(MediaDanhGiaClientService.uploadMedia(file, idDanhGia));
      }
    }

    await Promise.all(mediaPromises);

    toast.success('🎉 Gửi đánh giá thành công!');
    await allMyOrde();
    closeRateDialog();
  } catch (error) {
    console.error('❌ Lỗi khi gửi đánh giá:', error);

    if (
      error.response &&
      error.response.status === 500 &&
      error.response.data.message === 'Sản phẩm này đã được đánh giá rồi'
    ) {
      toast.error('⚠️ Sản phẩm này đã được đánh giá trước đó. Bạn không thể gửi thêm đánh giá.');
    } else {
      toast.error('❌ Gửi đánh giá thất bại. Vui lòng thử lại.');
    }
  }
};


onMounted(async () => {
  await allMyOrde();
});

const getOrderStatusClass = (status) => {
  switch (status) {
    case 'Hoàn thành':
      return 'status-completed';
    case 'Đang vận chuyển':
      return 'status-shipping';
    case 'Chờ thanh toán':
      return 'status-pending-payment';
    case 'Vận chuyển':
      return 'status-in-transit';
    case 'Chờ giao hàng':
      return 'status-awaiting-delivery';
    case 'Đã hủy':
      return 'status-cancelled';
    case 'Trả hàng/Hoàn tiền':
      return 'status-return-refund';
    default:
      return '';
  }
};
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