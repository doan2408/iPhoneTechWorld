```vue
<template>
  <div class="page-container">
    <main class="main-content">
      <header class="header">
        <div class="header-right">
          <div class="search-container">
            <SearchIcon class="search-icon" />
            <input type="search" placeholder="Tìm kiếm theo ID đơn hàng hoặc Tên sản phẩm" class="search-input"
              v-model="searchTerm" @input="handleSearch" />
          </div>
        </div>
      </header>

      <div class="tabs-container">
        <div class="tabs-list">
          <button :class="['tab-trigger', { active: activeTab === 'Chưa đánh giá' }]"
            @click="setActiveTab('Chưa đánh giá')">
            Chưa đánh giá
          </button>
          <button :class="['tab-trigger', { active: activeTab === 'Đã đánh giá' }]"
            @click="setActiveTab('Đã đánh giá')">
            Đã đánh giá
          </button>
          <button :class="['tab-trigger', { active: activeTab === 'Đánh giá có phản hồi' }]"
            @click="setActiveTab('Đánh giá có phản hồi')">
            Đánh giá có phản hồi
          </button>
        </div>
      </div>

      <div v-if="isLoading" class="loading-state">
        Đang tải đơn hàng...
      </div>
      <div v-else-if="filteredOrders.length === 0" class="empty-state">
        Không tìm thấy đơn hàng nào phù hợp.
      </div>
      <div v-else class="order-list">
        <div v-for="order in paginatedOrders" :key="order.idHoaDon" class="order-card">
          <div class="order-products">
            <div v-for="product in order.myOrderClientResponseList" :key="product.idSanPhamChiTiet"
              class="product-item">
              <img :src="product.urlImage" :alt="product.tenSanPham" class="product-image" />
              <div class="product-details">
                <div class="product-name">{{ product.tenSanPham }}</div>
                <div class="product-variant">
                  Phân loại: {{ product.colorName }} {{ product.dungLuongRom }}
                </div>
                <div class="product-quantity">x{{ product.soLuong }}</div>
                <!-- Hiển thị số ngày còn lại để đánh giá -->
                <div v-if="activeTab === 'Chưa đánh giá' && !order.daDanhGia && isWithin7Days(order?.ngayNhanhang)"
                  class="remaining-time">
                  {{ getRemainingDays(order?.ngayNhanhang) }}
                </div>
                <!-- Tab "Đã đánh giá" -->
                <div v-if="activeTab === 'Đã đánh giá' && order.reviews && order.reviews[product.idSanPhamChiTiet]"
                  class="review-details">
                  <div class="review-section">
                    <h3 class="section-title">Đánh giá sản phẩm</h3>
                    <!-- Số sao -->
                    <div class="star-rating-section">
                      <h4 class="subsection-title">Số sao:</h4>
                      <div class="star-rating">
                        <span v-for="n in 5" :key="n" class="star"
                          :class="{ filled: n <= order.reviews[product.idSanPhamChiTiet].soSao }">
                          ★
                        </span>
                      </div>
                    </div>
                    <!-- Nội dung đánh giá -->
                    <div v-if="order.reviews[product.idSanPhamChiTiet].noiDung" class="review-content">
                      <h4 class="subsection-title">Nội dung đánh giá:</h4>
                      <span class="review-label">Đánh giá của bạn:</span>
                      {{ order.reviews[product.idSanPhamChiTiet].noiDung }}
                    </div>
                    <!-- Hình ảnh/Video -->
                    <div
                      v-if="order.reviews[product.idSanPhamChiTiet].media && order.reviews[product.idSanPhamChiTiet].media.length"
                      class="review-media">
                      <h4 class="subsection-title">Hình ảnh/Video:</h4>
                      <div class="media-container">
                        <div v-for="media in order.reviews[product.idSanPhamChiTiet].media" :key="media.id"
                          class="media-item">
                          <el-image v-if="media.type === 'image'" :src="media.url"
                            :alt="'Hình ảnh đánh giá ' + product.tenSanPham" class="review-image" fit="cover"
                            :preview-src-list="[media.url]" preview-teleported>
                            <template #error>
                              <div class="image-error">
                                <el-icon>
                                  <Picture />
                                </el-icon>
                              </div>
                            </template>
                          </el-image>
                          <video v-else-if="media.type === 'video'" :src="media.url" controls
                            class="review-video"></video>
                        </div>
                      </div>
                    </div>
                    <!-- Hiển thị thời gian còn lại để sửa đánh giá -->
                    <div v-if="isWithin24Hours(order.reviews)" class="remaining-time">
                      {{ getRemainingHours(order.reviews) }}
                    </div>
                  </div>
                </div>
                <!-- Tab "Đánh giá có phản hồi" -->
                <div
                  v-if="activeTab === 'Đánh giá có phản hồi' && order.reviews && order.reviews[product.idSanPhamChiTiet]"
                  class="review-details">
                  <div class="review-section">
                    <h3 class="section-title">Đánh giá sản phẩm</h3>
                    <!-- Số sao -->
                    <div class="star-rating-section">
                      <h4 class="subsection-title">Số sao:</h4>
                      <div class="star-rating">
                        <span v-for="n in 5" :key="n" class="star"
                          :class="{ filled: n <= order.reviews[product.idSanPhamChiTiet].soSao }">
                          ★
                        </span>
                      </div>
                    </div>
                    <!-- Nội dung đánh giá -->
                    <div v-if="order.reviews[product.idSanPhamChiTiet].noiDung" class="review-content">
                      <h4 class="subsection-title">Nội dung đánh giá:</h4>
                      <span class="review-label">Đánh giá của bạn:</span>
                      {{ order.reviews[product.idSanPhamChiTiet].noiDung }}
                    </div>
                    <!-- Hình ảnh/Video -->
                    <div
                      v-if="order.reviews[product.idSanPhamChiTiet].media && order.reviews[product.idSanPhamChiTiet].media.length"
                      class="review-media">
                      <h4 class="subsection-title">Hình ảnh/Video:</h4>
                      <div class="media-container">
                        <div v-for="media in order.reviews[product.idSanPhamChiTiet].media" :key="media.id"
                          class="media-item">
                          <img v-if="media.type === 'image'" :src="media.url"
                            :alt="'Hình ảnh đánh giá ' + product.tenSanPham" class="review-image" />
                          <video v-else-if="media.type === 'video'" :src="media.url" controls
                            class="review-video"></video>
                        </div>
                      </div>
                    </div>
                    <!-- Phản hồi từ người bán -->
                    <div v-if="order.reviews[product.idSanPhamChiTiet].coPhanHoi" class="seller-response">
                      <h4 class="subsection-title">Phản hồi từ người bán:</h4>
                      <span class="response-label">Phản hồi:</span>
                      {{ order.reviews[product.idSanPhamChiTiet].phanHoi || 'Không có nội dung phản hồi.' }}
                    </div>
                  </div>
                </div>
              </div>
              <div class="product-prices">
                <span class="discounted-price">{{ formatPrice(product.giaSanPham) }} VNĐ</span>
              </div>
            </div>
          </div>

          <div class="order-footer">
            <div class="order-total">
              Thành tiền: <span class="total-amount">{{ formatPrice(order.thanhTien) }} VNĐ</span>
            </div>
            <div class="order-actions">
              <!-- <button class="action-button buy-again-button" @click="buyAgain(order)">
                Mua lại
              </button> -->
              <button class="action-button contact-seller-button" @click="contactSeller">
                Liên hệ người bán
              </button>
              <button v-if="!order.daDanhGia && isWithin7Days(order?.ngayNhanhang)" class="action-button rate-button"
                @click="openRateDialog(order.idHoaDon, order.myOrderClientResponseList)">
                Đánh giá
              </button>
              <button v-if="order.daDanhGia && !order.coPhanHoi && isWithin24Hours(order.reviews)"
                class="action-button edit-rate-button"
                @click="openEditRateDialog(order.idHoaDon, order.myOrderClientResponseList)">
                Sửa đánh giá
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-if="filteredOrders.length > pageSizeMyOrder" class="pagination-controls">
        <button class="pagination-button" :disabled="currentPage === 0" @click="prevPage">
          Trước
        </button>
        <button v-for="page in displayedPages" :key="page"
          :class="['pagination-button', { active: currentPage === page - 1 }]" @click="changePage(page - 1)">
          {{ page }}
        </button>
        <button class="pagination-button" :disabled="currentPage === totalFilteredPages - 1" @click="nextPage">
          Sau
        </button>
      </div>
    </main>

    <RateOrderDialog :is-open="isRateDialogOpen" :order-id="selectedOrderId" :order-products="selectedOrderProducts"
      :id-san-pham-chi-tiet-list="idSanPhamChiTietList" :is-editing="isEditing"
      :existing-rating-data="existingRatingData" @close="closeRateDialog" @submit="submitRating" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { SearchIcon } from 'lucide-vue-next';
import { getHoaDonAndIdChiTietHoaDon, getMyReview } from '@/Service/ClientService/HoaDon/MyOrderClient';
import RateOrderDialog from '@/components/Admin/dialogs/DialogDanhGiaSao.vue';
import { DanhGiaSanPhamClientService } from '@/Service/ClientService/DanhGiaSanPham/DanhGiaSanPhamClientService';
import { MediaDanhGiaClientService } from '@/Service/ClientService/MediaDanhGiaClientService/MediaDanhGiaClientService';
import { PhanHoiDanhGiaClientService } from '@/Service/ClientService/PhanHoiDanhGia/PhanHoiDanhGiaClientService';
import { useToast } from 'vue-toastification';
import { debounce } from 'lodash';

const toast = useToast();

// State
const allOrders = ref([]);
const filteredOrders = ref([]);
const pageSizeMyOrder = ref(5);
const totalElements = ref(0);
const totalPages = ref(0);
const currentPage = ref(0);
const searchTerm = ref('');
const activeTab = ref('Chưa đánh giá');
const isRateDialogOpen = ref(false);
const selectedOrderId = ref(null);
const selectedOrderProducts = ref([]);
const idSanPhamChiTietList = ref([]);
const user = ref(JSON.parse(localStorage.getItem('user')) || null);
const isLoading = ref(false);
const isEditing = ref(false);
const existingRatingData = ref([]);

// Hàm kiểm tra thời gian 7 ngày cho đánh giá
const isWithin7Days = (paymentDate) => {
  if (!paymentDate) return false;
  const paymentDateTime = new Date(paymentDate);
  if (isNaN(paymentDateTime)) return false;
  const currentDate = new Date();
  const diffInTime = currentDate - paymentDateTime;
  const diffInDays = diffInTime / (1000 * 3600 * 24);
  return diffInDays <= 7;
};

// Hàm kiểm tra thời gian 24 giờ cho sửa đánh giá
const isWithin24Hours = (reviews) => {
  console.log('isWithin24Hours called with reviews:', reviews);
  if (!reviews || typeof reviews !== 'object') return false;
  const review = Object.values(reviews)[0]; // Lấy đánh giá đầu tiên
  if (!review?.ngayDanhGia) return false;
  const reviewDateTime = new Date(review.ngayDanhGia);
  if (isNaN(reviewDateTime)) return false;
  const currentDate = new Date();
  const diffInTime = currentDate - reviewDateTime;
  const diffInHours = diffInTime / (1000 * 3600);
  return diffInHours <= 24;
};

// Hàm tính số ngày còn lại để đánh giá
const getRemainingDays = (paymentDate) => {
  if (!paymentDate) return 'Hết hạn';
  const paymentDateTime = new Date(paymentDate);
  if (isNaN(paymentDateTime)) return 'Hết hạn';

  const currentDate = new Date();

  // reset về 00:00 cho cả 2 ngày
  paymentDateTime.setHours(0, 0, 0, 0);
  currentDate.setHours(0, 0, 0, 0);

  const diffInTime = currentDate - paymentDateTime;
  const diffInDays = diffInTime / (1000 * 3600 * 24);
  const remainingDays = 7 - diffInDays;

  return remainingDays > 0
    ? `Còn ${remainingDays} ngày để đánh giá`
    : 'Hết hạn';
};


// Hàm tính số giờ còn lại để sửa đánh giá
const getRemainingHours = (reviews) => {
  if (!reviews || typeof reviews !== 'object') return 'Hết hạn sửa';
  const review = Object.values(reviews)[0]; // Lấy đánh giá đầu tiên
  if (!review?.ngayDanhGia) return 'Hết hạn sửa';
  const reviewDateTime = new Date(review.ngayDanhGia);
  if (isNaN(reviewDateTime)) return 'Hết hạn sửa';
  const currentDate = new Date();
  const diffInTime = currentDate - reviewDateTime;
  const diffInHours = diffInTime / (1000 * 3600);
  const remainingHours = 24 - diffInHours;
  return remainingHours > 0 ? `Còn ${Math.ceil(remainingHours)} giờ để sửa đánh giá` : 'Hết hạn sửa';
};

// Computed: Phân trang phía client
const totalFilteredPages = computed(() => Math.ceil(filteredOrders.value.length / pageSizeMyOrder.value));
const paginatedOrders = computed(() => {
  const start = currentPage.value * pageSizeMyOrder.value;
  return filteredOrders.value.slice(start, start + pageSizeMyOrder.value);
});

const displayedPages = computed(() => {
  const maxPages = 5;
  const start = Math.max(1, currentPage.value - Math.floor(maxPages / 2));
  const end = Math.min(totalFilteredPages.value, start + maxPages - 1);
  return Array.from({ length: end - start + 1 }, (_, i) => start + i);
});

// Nhúng Tawk.to
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

// Hàm xử lý tìm kiếm
const handleSearch = debounce(() => {
  currentPage.value = 0;
  allMyOrders();
}, 500);

// Hàm format giá
const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN').format(price);
};

// Hàm lấy danh sách đơn hàng
const allMyOrders = async () => {
  try {
    if (!user.value?.id) {
      toast.error('Vui lòng đăng nhập để xem đơn hàng!');
      return;
    }

    isLoading.value = true;
    const res = await getMyReview(0, 1000, user.value.id, searchTerm.value);
    const orders = Array.isArray(res.data.content) ? res.data.content : [];
    console.log("Orders fetched:", orders);

    const ordersWithCheck = await Promise.all(
      orders.map(async (order) => {
        try {
          const response = await DanhGiaSanPhamClientService.checkDanhGiaVaDaPhanHoi(order.idHoaDon, user.value.id);
          let reviews = {};
          if (response.da_danh_gia) {
            const reviewResponse = await DanhGiaSanPhamClientService.layDanhGiaTheoHoaDon(order.idHoaDon);
            console.log(`layDanhGiaTheoHoaDon for order ${order.idHoaDon}:`, reviewResponse);
            if (Array.isArray(reviewResponse.data)) {
              reviews = await Promise.all(
                reviewResponse.data.map(async (review) => {
                  const mediaList = Array.isArray(review.reviewClientResponsesList)
                    ? review.reviewClientResponsesList
                    : [];
                  let phanHoi = '';
                  let coPhanHoi = false;
                  let ngayPhanHoi = '';
                  if (review.idDanhGia) {
                    try {
                      const phanHoiResponse = await PhanHoiDanhGiaClientService.layPhanHoiTheoDanhGia(review.idDanhGia);
                      if (Array.isArray(phanHoiResponse) && phanHoiResponse.length > 0) {
                        phanHoi = phanHoiResponse[0].noiDungPhanHoi || '';
                        coPhanHoi = true;
                        ngayPhanHoi = phanHoiResponse[0].ngayPhanHoi || '';
                      }
                      console.log(`phanHoi set for review ${review.idDanhGia}:`, phanHoi);
                    } catch (err) {
                      console.error(`Lỗi khi lấy phản hồi cho đánh giá ${review.idDanhGia}:`, err);
                    }
                  }
                  return {
                    [review.idSanPhamChiTiet]: {
                      soSao: review.soSao,
                      noiDung: review.noiDung || '',
                      media: mediaList.map((item) => ({
                        id: item.idMedia,
                        url: item.urlMedia,
                        type: item.loaiMedia.toLowerCase(),
                      })),
                      phanHoi,
                      coPhanHoi,
                      ngayPhanHoi,
                      idDanhGia: review.idDanhGia,
                      ngayDanhGia: review.ngayDanhGia || null,
                    },
                  };
                })
              ).then((results) => {
                const mergedReviews = results.reduce((acc, curr) => ({ ...acc, ...curr }), {});
                return mergedReviews;
              });
            }
          }
          return {
            ...order,
            daDanhGia: response.da_danh_gia ?? false,
            coPhanHoi: response.co_phan_hoi ?? false,
            reviews,
          };
        } catch (err) {
          return { ...order, daDanhGia: false, coPhanHoi: false, reviews: {} };
        }
      })
    );

    allOrders.value = ordersWithCheck;
    filteredOrders.value = ordersWithCheck.filter((order) => {
      const within7Days = isWithin7Days(order.ngayThanhToan);
      if (activeTab.value === 'Chưa đánh giá') {
        return !order.daDanhGia && order.trangThaiThanhToan === 'Hoàn tất' && within7Days;
      } else if (activeTab.value === 'Đã đánh giá') {
        return order.daDanhGia && order.trangThaiThanhToan === 'Hoàn tất';
      } else if (activeTab.value === 'Đánh giá có phản hồi') {
        return order.daDanhGia && order.coPhanHoi && order.trangThaiThanhToan === 'Hoàn tất';
      }
      return true;
    });
  } catch (error) {
    toast.error('Không thể tải danh sách đơn hàng. Vui lòng thử lại.');
  } finally {
    isLoading.value = false;
  }
};

// Hàm xử lý phân trang
const changePage = (page) => {
  currentPage.value = page;
};

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
  }
};

const nextPage = () => {
  if (currentPage.value < totalFilteredPages.value - 1) {
    currentPage.value++;
  }
};

// Hàm xử lý tab
const setActiveTab = (tab) => {
  activeTab.value = tab;
  currentPage.value = 0;
  allMyOrders();
};

// Hàm liên hệ người bán
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

// Hàm mua lại
const buyAgain = (order) => {
  toast.info('Chức năng mua lại đang được phát triển!');
  console.log('Mua lại đơn hàng:', order);
};

// Hàm mở dialog đánh giá
const openRateDialog = async (orderId, products) => {
  if (!user.value?.id) {
    toast.error('Vui lòng đăng nhập để đánh giá!');
    return;
  }

  const order = allOrders.value.find((o) => o.idHoaDon === orderId);
  if (!order) {
    toast.error('Không tìm thấy đơn hàng!');
    return;
  }

  if (!isWithin7Days(order.ngayThanhToan)) {
    toast.error('Đơn hàng đã quá 7 ngày kể từ ngày thanh toán, không thể đánh giá!');
    return;
  }

  selectedOrderId.value = orderId;
  selectedOrderProducts.value = Array.from(products);
  isEditing.value = false;
  existingRatingData.value = [];

  try {
    const chiTietList = await getHoaDonAndIdChiTietHoaDon(orderId);
    if (!Array.isArray(chiTietList.data)) {
      toast.error('Dữ liệu chi tiết hóa đơn không hợp lệ!');
      return;
    }

    idSanPhamChiTietList.value = chiTietList.data.map((item) => ({
      idSanPhamChiTiet: item.idSanPhamChiTiet,
      idChiTietHoaDon: item.idChiTietHoaDon,
      tenSanPham: products.find((p) => p.idSanPhamChiTiet === item.idSanPhamChiTiet)?.tenSanPham || 'Unknown',
    }));

    selectedOrderProducts.value = selectedOrderProducts.value.map((product) => ({
      ...product,
      idChiTietHoaDon:
        chiTietList.data.find((item) => item.idSanPhamChiTiet === product.idSanPhamChiTiet)?.idChiTietHoaDon || null,
    }));

    if (selectedOrderProducts.value.some((p) => !p.idSanPhamChiTiet || !p.idChiTietHoaDon)) {
      toast.error('Dữ liệu sản phẩm không hợp lệ!');
      return;
    }

    isRateDialogOpen.value = true;
  } catch (error) {
    console.error('Lỗi trong openEditRateDialog:', error);

    let errorMessage = 'Lỗi không xác định';
    if (error.response) {
      const statusCode = error.response.status;
      const backendMsg = error.response.data?.message || 'Lỗi từ server';
      if (statusCode === 400) {
        errorMessage = `Yêu cầu không hợp lệ: ${backendMsg}`;
      } else if (statusCode === 404) {
        errorMessage = `Không tìm thấy tài nguyên: ${backendMsg}`;
      } else if (statusCode === 500) {
        errorMessage = `Lỗi máy chủ: ${backendMsg}`;
      } else {
        errorMessage = `Gửi/cập nhật đánh giá thất bại: ${backendMsg}`;
      }
    } else {
      errorMessage = `Lỗi kết nối hoặc client: ${error.message}`;
    }

    toast.error(errorMessage);
  }
};

// Hàm mở dialog sửa đánh giá
const openEditRateDialog = async (orderId, products) => {
  console.log('openEditRateDialog called with:', { orderId, products });
  if (!user.value?.id) {
    console.warn('User not logged in');
    toast.error('Vui lòng đăng nhập để sửa đánh giá!');
    return;
  }

  const order = allOrders.value.find((o) => o.idHoaDon === orderId);
  if (!order) {
    toast.error('Không tìm thấy đơn hàng!');
    return;
  }

  if (!isWithin24Hours(order.reviews)) {
    toast.error('Đã quá 24 giờ kể từ khi đánh giá, không thể sửa!');
    return;
  }

  selectedOrderId.value = orderId;
  selectedOrderProducts.value = Array.from(products);
  isEditing.value = true;

  try {
    const chiTietList = await getHoaDonAndIdChiTietHoaDon(orderId);
    console.log('Chi tiết hóa đơn response:', chiTietList);

    if (!Array.isArray(chiTietList.data)) {
      console.error('Invalid chiTietList data:', chiTietList);
      toast.error('Dữ liệu chi tiết hóa đơn không hợp lệ!');
      return;
    }

    idSanPhamChiTietList.value = chiTietList.data.map((item) => ({
      idSanPhamChiTiet: item.idSanPhamChiTiet,
      idChiTietHoaDon: item.idChiTietHoaDon,
      tenSanPham: products.find((p) => p.idSanPhamChiTiet === item.idSanPhamChiTiet)?.tenSanPham || 'Unknown',
    }));
    console.log('idSanPhamChiTietList:', idSanPhamChiTietList.value);

    selectedOrderProducts.value = selectedOrderProducts.value.map((product) => ({
      ...product,
      idChiTietHoaDon:
        chiTietList.data.find((item) => item.idSanPhamChiTiet === product.idSanPhamChiTiet)?.idChiTietHoaDon || null,
    }));
    console.log('selectedOrderProducts:', selectedOrderProducts.value);

    if (selectedOrderProducts.value.some((p) => !p.idSanPhamChiTiet || !p.idChiTietHoaDon)) {
      console.error('Invalid product data:', selectedOrderProducts.value);
      toast.error('Dữ liệu sản phẩm không hợp lệ!');
      return;
    }

    const reviewResponse = await DanhGiaSanPhamClientService.layDanhGiaTheoHoaDon(orderId);
    console.log('DanhGia response:', reviewResponse);

    if (!Array.isArray(reviewResponse.data) || reviewResponse.data.length === 0) {
      console.error('No review data found:', reviewResponse);
      toast.error('Không tìm thấy dữ liệu đánh giá!');
      return;
    }

    existingRatingData.value = reviewResponse.data.map((review) => {
      const mediaList = Array.isArray(review.reviewClientResponsesList) ? review.reviewClientResponsesList : [];
      const media = mediaList.map((item) => ({
        id: item.idMedia,
        url: item.urlMedia,
        type: item.loaiMedia.toLowerCase(),
      }));

      return {
        idSanPhamChiTiet: review.idSanPhamChiTiet,
        idChiTietHoaDon: review.idChiTietHoaDon,
        soSao: review.soSao,
        noiDung: review.noiDung || '',
        media,
        idDanhGia: review.idDanhGia,
        ngayDanhGia: review.ngayDanhGia || null,
      };
    });

    console.log('existingRatingData:', existingRatingData.value);
    setTimeout(() => {
      isRateDialogOpen.value = true;
      console.log('Dialog opened after timeout');
    }, 100);
  } catch (error) {
    console.error('Lỗi trong openEditRateDialog:', error);

    let errorMessage = 'Lỗi không xác định';
    if (error.response) {
      const statusCode = error.response.status;
      const backendMsg = error.response.data?.message || 'Lỗi từ server';
      if (statusCode === 400) {
        errorMessage = `Yêu cầu không hợp lệ: ${backendMsg}`;
      } else if (statusCode === 404) {
        errorMessage = `Không tìm thấy tài nguyên: ${backendMsg}`;
      } else if (statusCode === 500) {
        errorMessage = `Lỗi máy chủ: ${backendMsg}`;
      } else {
        errorMessage = `Gửi/cập nhật đánh giá thất bại: ${backendMsg}`;
      }
    } else {
      errorMessage = `Lỗi kết nối hoặc client: ${error.message}`;
    }

    toast.error(errorMessage);
  }
};

// Hàm đóng dialog
const closeRateDialog = () => {
  isRateDialogOpen.value = false;
  // selectedOrderId.value = null;
  // selectedOrderProducts.value = [];
  // idSanPhamChiTietList.value = [];
  // isEditing.value = false;
  // existingRatingData.value = [];
};

// Hàm gửi/cập nhật đánh giá
const submitRating = async ({ payload }) => {
  console.log('submitRating nhận payload:', JSON.stringify(payload, null, 2));
  console.log('isEditing trong parent:', isEditing.value); // Log để debug

  try {
    if (!user.value?.id) {
      toast.error('Vui lòng đăng nhập để đánh giá!');
      return;
    }

    const { idHoaDon, ratings, trangThaiDanhGia } = payload;
    console.log('Đang lấy chiTietList cho idHoaDon:', idHoaDon);
    const chiTietList = await getHoaDonAndIdChiTietHoaDon(idHoaDon);

    if (!Array.isArray(chiTietList.data) || chiTietList.data.length === 0) {
      console.error('Dữ liệu chiTietList không hợp lệ:', chiTietList);
      toast.error('Không tìm thấy chi tiết hóa đơn!');
      return;
    }

    if (chiTietList.data.length !== ratings.length) {
      console.error('Số lượng đánh giá không khớp với chiTietList:', {
        ratingsCount: ratings.length,
        chiTietListCount: chiTietList.data.length,
      });
      toast.error('Số lượng đánh giá không khớp với sản phẩm trong hóa đơn!');
      return;
    }

    const isValid = ratings.every((rating) =>
      chiTietList.data.some((chiTiet) => chiTiet.idSanPhamChiTiet === rating.idSanPhamChiTiet)
    );

    if (!isValid) {
      console.error('Dữ liệu đánh giá không hợp lệ:', ratings);
      toast.error('Một hoặc nhiều sản phẩm đánh giá không thuộc hóa đơn này!');
      return;
    }

    // Xác định chế độ dựa trên idDanhGia
    const isEditingMode = ratings.some(r => r.idDanhGia);
    console.log('Chế độ xử lý:', isEditingMode ? 'Cập nhật' : 'Tạo mới');

    if (isEditingMode) {
      console.log('Đang xử lý cập nhật cho ratings:', ratings);
      const updatePromises = ratings.map(async (rating) => {
        if (!rating.idDanhGia) {
          console.error(`Thiếu idDanhGia cho sản phẩm ${rating.idSanPhamChiTiet}`);
          toast.error(`Không thể cập nhật đánh giá cho sản phẩm ${rating.idSanPhamChiTiet}!`);
          return { status: 'rejected', reason: `Thiếu idDanhGia` };
        }

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
          console.log(`Đang cập nhật đánh giá cho sản phẩm ${rating.idSanPhamChiTiet} với idDanhGia ${rating.idDanhGia}`);
          const updateResponse = await DanhGiaSanPhamClientService.capNhatDanhGia(rating.idDanhGia, request);
          console.log(`Cập nhật thành công cho sản phẩm ${rating.idSanPhamChiTiet}:`, updateResponse);

          if (rating.deletedMediaIds && Array.isArray(rating.deletedMediaIds) && rating.deletedMediaIds.length > 0) {
            console.log(`Đang xóa media cho sản phẩm ${rating.idSanPhamChiTiet}:`, rating.deletedMediaIds);
            const deletePromises = rating.deletedMediaIds.map(async (mediaId) => {
              try {
                await MediaDanhGiaClientService.deleteByIdMedia(mediaId);
                console.log(`Xóa media ${mediaId} thành công`);
                return { status: 'fulfilled', value: mediaId };
              } catch (err) {
                console.error(`Lỗi khi xóa media ${mediaId} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                toast.error(`Không thể xóa media ${mediaId}`);
                return { status: 'rejected', reason: err };
              }
            });
            await Promise.allSettled(deletePromises);
          }

          const mediaPromises = [];
          if (Array.isArray(rating.imageFiles)) {
            rating.imageFiles.forEach((file) => {
              if (file && file.name && file.size && file.type) {
                mediaPromises.push(
                  MediaDanhGiaClientService.uploadMedia(file, rating.idDanhGia)
                    .then((uploadResponse) => {
                      console.log(`Tải lên ảnh ${file.name} thành công`);
                      return { status: 'fulfilled', value: uploadResponse };
                    })
                    .catch((err) => {
                      console.error(`Lỗi khi tải lên ảnh ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                      toast.error(`Không thể tải lên ảnh ${file.name}`);
                      return { status: 'rejected', reason: err };
                    })
                );
              }
            });
          }

          if (Array.isArray(rating.videoFiles)) {
            rating.videoFiles.forEach((file) => {
              if (file && file.name && file.size && file.type) {
                mediaPromises.push(
                  MediaDanhGiaClientService.uploadMedia(file, rating.idDanhGia)
                    .then((uploadResponse) => {
                      console.log(`Tải lên video ${file.name} thành công`);
                      return { status: 'fulfilled', value: uploadResponse };
                    })
                    .catch((err) => {
                      console.error(`Lỗi khi tải lên video ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                      toast.error(`Không thể tải lên video ${file.name}`);
                      return { status: 'rejected', reason: err };
                    })
                );
              }
            });
          }

          await Promise.allSettled(mediaPromises);
          return { status: 'fulfilled', value: updateResponse };
        } catch (error) {
          console.error(`Lỗi khi cập nhật đánh giá cho sản phẩm ${rating.idSanPhamChiTiet}:`, error);
          toast.error(`Cập nhật đánh giá cho sản phẩm ${rating.idSanPhamChiTiet} thất bại`);
          return { status: 'rejected', reason: error };
        }
      });

      const updateResults = await Promise.allSettled(updatePromises);
      const hasErrors = updateResults.some((result) => result.status === 'rejected');
      if (hasErrors) {
        console.error('Lỗi cập nhật:', updateResults.filter((r) => r.status === 'rejected'));
        toast.error('Cập nhật một số đánh giá thất bại!');
        return;
      }
      toast.success('Cập nhật đánh giá thành công!');
    } else {
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
          if (Array.isArray(rating.imageFiles)) {
            rating.imageFiles.forEach((file) => {
              if (file && file.name && file.size && file.type) {
                mediaPromises.push(
                  MediaDanhGiaClientService.uploadMedia(file, danhGiaResponse.idDanhGia)
                    .then((uploadResponse) => {
                      console.log(`Tải lên ảnh ${file.name} thành công`);
                      return { status: 'fulfilled', value: uploadResponse };
                    })
                    .catch((err) => {
                      console.error(`Lỗi khi tải lên ảnh ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                      toast.error(`Không thể tải lên ảnh ${file.name}`);
                      return { status: 'rejected', reason: err };
                    })
                );
              }
            });
          }

          if (Array.isArray(rating.videoFiles)) {
            rating.videoFiles.forEach((file) => {
              if (file && file.name && file.size && file.type) {
                mediaPromises.push(
                  MediaDanhGiaClientService.uploadMedia(file, danhGiaResponse.idDanhGia)
                    .then((uploadResponse) => {
                      console.log(`Tải lên video ${file.name} thành công`);
                      return { status: 'fulfilled', value: uploadResponse };
                    })
                    .catch((err) => {
                      console.error(`Lỗi khi tải lên video ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
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
          toast.error(`Tạo đánh giá cho sản phẩm ${rating.idSanPhamChiTiet} thất bại`);
          return { status: 'rejected', reason: error };
        }
      });

      const danhGiaResults = await Promise.allSettled(danhGiaPromises);
      const hasErrors = danhGiaResults.some((result) => result.status === 'rejected');
      if (hasErrors) {
        console.error('Lỗi tạo đánh giá:', danhGiaResults.filter((r) => r.status === 'rejected'));
        toast.error('Tạo một số đánh giá thất bại!');
        return;
      }
      toast.success('Gửi đánh giá thành công!');
    }

    console.log('Đang làm mới danh sách đơn hàng và đóng dialog');
    await allMyOrders();
    closeRateDialog();
  } catch (error) {
    console.error('Lỗi trong submitRating:', error);
    toast.error(`Gửi/cập nhật đánh giá thất bại: ${error.response?.data?.message || 'Lỗi không xác định'}`);
  }
};

// Khởi tạo dữ liệu
onMounted(async () => {
  await allMyOrders();
});
</script>

<style scoped>
@import '@/style/HoaDon/MyOrder.css';

.rate-button {
  background-color: #28a745;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.rate-button:hover {
  background-color: #218838;
  transform: translateY(-2px);
}

.contact-seller-button,
.buy-again-button,
.edit-rate-button {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.contact-seller-button {
  background-color: #007bff;
  color: white;
}

.contact-seller-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.buy-again-button {
  background-color: #ffc107;
  color: #333;
}

.buy-again-button:hover {
  background-color: #e0a800;
  transform: translateY(-2px);
}

.edit-rate-button {
  background-color: #17a2b8;
  color: white;
}

.edit-rate-button:hover {
  background-color: #138496;
  transform: translateY(-2px);
}

.loading-state {
  text-align: center;
  padding: 24px;
  color: #666;
  font-size: 16px;
  font-weight: 500;
}

.review-details {
  margin-top: 12px;
  padding: 12px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: box-shadow 0.3s ease;
}

.review-details:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.review-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 4px;
}

.subsection-title {
  font-size: 14px;
  font-weight: 600;
  color: #34495e;
  margin-bottom: 6px;
}

.star-rating-section {
  display: flex;
  flex-direction: column;
}

.star-rating {
  display: flex;
  gap: 6px;
  font-size: 18px;
  color: #d1d1d1;
  margin-bottom: 8px;
}

.star.filled {
  color: #f5c518;
  text-shadow: 0 0 4px rgba(245, 197, 24, 0.5);
}

.review-content {
  font-size: 15px;
  color: #333;
  line-height: 1.6;
  font-weight: 400;
  max-width: 100%;
  word-wrap: break-word;
}

.review-content .review-label {
  font-weight: 600;
  color: #2c3e50;
  margin-right: 8px;
}

.review-media {
  margin-top: 8px;
}

.media-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
}

.media-item {
  position: relative;
  width: 120px;
  height: 120px;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.media-item:hover {
  transform: scale(1.05);
}

.review-image,
.review-video {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 8px;
  background-color: #f4f4f4;
}


.review-video {
  background-color: #000;
}

.review-video::-webkit-media-controls-panel {
  background-color: rgba(0, 0, 0, 0.7);
}

.seller-response {
  margin-top: 8px;
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  font-weight: 400;
  background-color: #e8f0fe;
  padding: 10px;
  border-radius: 6px;
  border-left: 4px solid #007bff;
}

.seller-response .response-label {
  font-weight: 600;
  color: #007bff;
  margin-right: 8px;
}

.remaining-time {
  font-size: 14px;
  color: #e74c3c;
  margin-top: 8px;
}

.media-item {
  position: relative;
  width: 120px;
  height: 120px;
  overflow: hidden;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.media-item:hover {
  transform: scale(1.05);
}

.review-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 8px;
  background-color: #f4f4f4;
}
</style>
```