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
              </div>
              <div class="product-prices">
                <span class="discounted-price">₫{{ formatPrice(product.giaSanPham) }}</span>
              </div>
            </div>
          </div>

          <div class="order-footer">
            <div class="order-total">
              Thành tiền: <span class="total-amount">{{ formatPrice(order.thanhTien) }} VNĐ</span>
            </div>
            <div class="order-actions">
              <button class="action-button buy-again-button" @click="buyAgain(order)">
                Mua lại
              </button>
              <button class="action-button contact-seller-button" @click="contactSeller">
                Liên hệ người bán
              </button>
              <button v-if="!order.daDanhGia && isRateButtonVisible(order.ngayDatHang)"
                class="action-button rate-button"
                @click="openRateDialog(order.idHoaDon, order.myOrderClientResponseList)">
                Đánh giá
              </button>

              <button v-if="order.daDanhGia && !order.coPhanHoi && isRateButtonVisibleUpdate(order.ngayDatHang)" class="action-button edit-rate-button"
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
import { ref, computed, watch, onMounted } from 'vue';
import { SearchIcon } from 'lucide-vue-next';
import { getHoaDonAndIdChiTietHoaDon, getMyReview } from '@/Service/ClientService/HoaDon/MyOrderClient';
import RateOrderDialog from '@/components/Admin/dialogs/DialogDanhGiaSao.vue'; // Giữ import của bạn
import { DanhGiaSanPhamClientService } from '@/Service/ClientService/DanhGiaSanPham/DanhGiaSanPhamClientService';
import { MediaDanhGiaClientService } from '@/Service/ClientService/MediaDanhGiaClientService/MediaDanhGiaClientService';
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

    const ordersWithCheck = await Promise.all(
      orders.map(async (order) => {
        try {
          const response = await DanhGiaSanPhamClientService.checkDanhGiaVaDaPhanHoi(order.idHoaDon, user.value.id);
          return {
            ...order,
            daDanhGia: response.da_danh_gia ?? false,
            coPhanHoi: response.co_phan_hoi ?? false,
          };
        } catch (err) {
          console.error(`Lỗi kiểm tra đánh giá cho đơn hàng ${order.idHoaDon}:`, err);
          return { ...order, daDanhGia: false, coPhanHoi: false };
        }
      })
    );

    allOrders.value = ordersWithCheck;
    filteredOrders.value = ordersWithCheck.filter(order => {
      if (activeTab.value === 'Chưa đánh giá') {
        return !order.daDanhGia && order.trangThaiThanhToan === 'Hoàn tất';
      } else if (activeTab.value === 'Đã đánh giá') {
        return order.daDanhGia && !order.coPhanHoi && order.trangThaiThanhToan === 'Hoàn tất';
      } else if (activeTab.value === 'Đánh giá có phản hồi') {
        return order.daDanhGia && order.coPhanHoi && order.trangThaiThanhToan === 'Hoàn tất';
      }
      return true;
    });


    totalElements.value = filteredOrders.value.length;
    totalPages.value = Math.ceil(totalElements.value / pageSizeMyOrder.value);
    if (currentPage.value >= totalPages.value) {
      currentPage.value = Math.max(0, totalPages.value - 1);
    }
  } catch (error) {
    console.error('Lỗi khi lấy đơn hàng:', error);
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

const isRateButtonVisible = (orderDate) => {
  if (!orderDate) return false;
  const orderTime = new Date(orderDate).getTime();
  const now = Date.now();
  // const ONE_WEEK = 7 * 24 * 60 * 60 * 1000; // 7 ngày
  const TWO_MINUTES = 2 * 60 * 1000; // 120000 ms
  return now - orderTime <= TWO_MINUTES;
};

const isRateButtonVisibleUpdate = (orderDate) => {
  if (!orderDate) return false;
  const orderTime = new Date(orderDate).getTime();
  const now = Date.now();
  // const ONE_WEEK = 7 * 24 * 60 * 60 * 1000; // 7 ngày
  const TWO_MINUTES = 2 * 60 * 1000; // 120000 ms
  return now - orderTime <= TWO_MINUTES;
};

// Hàm mở dialog đánh giá
const openRateDialog = async (orderId, products) => {
  if (!user.value?.id) {
    toast.error('Vui lòng đăng nhập để đánh giá!');
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

// Hàm mở dialog sửa đánh giá
const openEditRateDialog = async (orderId, products) => {
  console.log('openEditRateDialog called with:', { orderId, products });
  if (!user.value?.id) {
    console.warn('User not logged in');
    toast.error('Vui lòng đăng nhập để sửa đánh giá!');
    return;
  }

  selectedOrderId.value = orderId;
  selectedOrderProducts.value = Array.from(products);
  isEditing.value = true;

  try {
    // Lấy chi tiết hóa đơn
    const chiTietList = await getHoaDonAndIdChiTietHoaDon(orderId);
    console.log('Chi tiết hóa đơn response:', chiTietList);

    if (!Array.isArray(chiTietList.data)) {
      console.error('Invalid chiTietList data:', chiTietList);
      toast.error('Dữ liệu chi tiết hóa đơn không hợp lệ!');
      return;
    }

    idSanPhamChiTietList.value = chiTietList.data.map(item => ({
      idSanPhamChiTiet: item.idSanPhamChiTiet,
      idChiTietHoaDon: item.idChiTietHoaDon,
      tenSanPham: products.find(p => p.idSanPhamChiTiet === item.idSanPhamChiTiet)?.tenSanPham || 'Unknown',
    }));
    console.log('idSanPhamChiTietList:', idSanPhamChiTietList.value);

    selectedOrderProducts.value = selectedOrderProducts.value.map(product => ({
      ...product,
      idChiTietHoaDon: chiTietList.data.find(item => item.idSanPhamChiTiet === product.idSanPhamChiTiet)?.idChiTietHoaDon || null,
    }));
    console.log('selectedOrderProducts:', selectedOrderProducts.value);

    if (selectedOrderProducts.value.some(p => !p.idSanPhamChiTiet || !p.idChiTietHoaDon)) {
      console.error('Invalid product data:', selectedOrderProducts.value);
      toast.error('Dữ liệu sản phẩm không hợp lệ!');
      return;
    }

    // Lấy dữ liệu đánh giá hiện có
    const reviewResponse = await DanhGiaSanPhamClientService.layDanhGiaTheoHoaDon(orderId);
    console.log('DanhGia response:', reviewResponse);

    if (!Array.isArray(reviewResponse.data) || reviewResponse.data.length === 0) {
      console.error('No review data found:', reviewResponse);
      toast.error('Không tìm thấy dữ liệu đánh giá!');
      return;
    }

    // Xử lý dữ liệu đánh giá theo từng sản phẩm
    existingRatingData.value = reviewResponse.data.map(review => {
      // Sử dụng reviewClientResponsesList để ánh xạ media
      const mediaList = Array.isArray(review.reviewClientResponsesList) ? review.reviewClientResponsesList : [];
      const media = mediaList.map(item => ({
        id: item.idMedia,
        url: item.urlMedia,
        type: item.loaiMedia.toLowerCase(), // Chuyển IMAGE/VIDEO thành image/video
      }));

      return {
        idSanPhamChiTiet: review.idSanPhamChiTiet,
        idChiTietHoaDon: review.idChiTietHoaDon,
        soSao: review.soSao,
        noiDung: review.noiDung || '',
        media,
        idDanhGia: review.idDanhGia,
      };
    });

    console.log('existingRatingData:', existingRatingData.value);

    // Thêm timeout để đảm bảo dữ liệu được gán trước khi mở dialog
    setTimeout(() => {
      isRateDialogOpen.value = true;
      console.log('Dialog opened after timeout');
    }, 100);
  } catch (error) {
    console.error('Error in openEditRateDialog:', error);
    toast.error('Không thể lấy dữ liệu đánh giá. Vui lòng thử lại.');
  }
};

// Hàm đóng dialog
const closeRateDialog = () => {
  isRateDialogOpen.value = false;
  selectedOrderId.value = null;
  selectedOrderProducts.value = [];
  idSanPhamChiTietList.value = [];
  isEditing.value = false;
  existingRatingData.value = [];
};

// Hàm gửi/cập nhật đánh giá
const submitRating = async ({ payload }) => {
  console.log('submitRating nhận payload:', JSON.stringify(payload, null, 2));
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

    const isValid = ratings.every(rating =>
      chiTietList.data.some(chiTiet => chiTiet.idSanPhamChiTiet === rating.idSanPhamChiTiet)
    );

    if (!isValid) {
      console.error('Dữ liệu đánh giá không hợp lệ:', ratings);
      toast.error('Một hoặc nhiều sản phẩm đánh giá không thuộc hóa đơn này!');
      return;
    }

    if (isEditing.value) {
      // Chế độ chỉnh sửa (giữ nguyên logic hiện tại)
      console.log('Đang xử lý cập nhật cho ratings:', ratings);
      const updatePromises = ratings.map(async (rating) => {
        const existing = existingRatingData.value.find(r => r.idSanPhamChiTiet === rating.idSanPhamChiTiet);
        if (!existing) {
          console.error(`Không tìm thấy đánh giá hiện có cho sản phẩm ${rating.idSanPhamChiTiet}`);
          toast.error(`Không tìm thấy đánh giá hiện có cho sản phẩm ${rating.idSanPhamChiTiet}!`);
          return { status: 'rejected', reason: `Không có đánh giá hiện có cho ${rating.idSanPhamChiTiet}` };
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
          console.log(`Đang cập nhật đánh giá cho sản phẩm ${rating.idSanPhamChiTiet} với idDanhGia ${existing.idDanhGia}`);
          const updateResponse = await DanhGiaSanPhamClientService.capNhatDanhGia(existing.idDanhGia, request);
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
            rating.imageFiles.forEach(file => {
              if (file && file.name && file.size && file.type) {
                mediaPromises.push(
                  MediaDanhGiaClientService.uploadMedia(file, existing.idDanhGia)
                    .then(uploadResponse => {
                      console.log(`Tải lên ảnh ${file.name} thành công`);
                      return { status: 'fulfilled', value: uploadResponse };
                    })
                    .catch(err => {
                      console.error(`Lỗi khi tải lên ảnh ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                      toast.error(`Không thể tải lên ảnh ${file.name}`);
                      return { status: 'rejected', reason: err };
                    })
                );
              } else {
                console.warn(`Tệp ảnh không hợp lệ cho sản phẩm ${rating.idSanPhamChiTiet}:`, file);
              }
            });
          }

          if (Array.isArray(rating.videoFiles)) {
            rating.videoFiles.forEach(file => {
              if (file && file.name && file.size && file.type) {
                mediaPromises.push(
                  MediaDanhGiaClientService.uploadMedia(file, existing.idDanhGia)
                    .then(uploadResponse => {
                      console.log(`Tải lên video ${file.name} thành công`);
                      return { status: 'fulfilled', value: uploadResponse };
                    })
                    .catch(err => {
                      console.error(`Lỗi khi tải lên video ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                      toast.error(`Không thể tải lên video ${file.name}`);
                      return { status: 'rejected', reason: err };
                    })
                );
              } else {
                console.warn(`Tệp video không hợp lệ cho sản phẩm ${rating.idSanPhamChiTiet}:`, file);
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
      const hasErrors = updateResults.some(result => result.status === 'rejected');
      if (hasErrors) {
        console.error('Lỗi cập nhật:', updateResults.filter(r => r.status === 'rejected'));
        toast.error('Cập nhật một số đánh giá thất bại!');
        return;
      }
      toast.success('Cập nhật đánh giá thành công!');
    } else {
      // Chế độ tạo mới
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
          // Xác thực và xử lý tệp ảnh
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
                      console.error(`Lỗi khi tải lên ảnh ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                      toast.error(`Không thể tải lên ảnh ${file.name}`);
                      return { status: 'rejected', reason: err };
                    })
                );
              } else {
                console.warn(`Tệp ảnh không hợp lệ cho sản phẩm ${rating.idSanPhamChiTiet}:`, file);
              }
            });
          } else {
            console.warn(`imageFiles không phải là mảng cho sản phẩm ${rating.idSanPhamChiTiet}:`, rating.imageFiles);
          }

          // Xác thực và xử lý tệp video
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
                      console.error(`Lỗi khi tải lên video ${file.name} cho sản phẩm ${rating.idSanPhamChiTiet}:`, err);
                      toast.error(`Không thể tải lên video ${file.name}`);
                      return { status: 'rejected', reason: err };
                    })
                );
              } else {
                console.warn(`Tệp video không hợp lệ cho sản phẩm ${rating.idSanPhamChiTiet}:`, file);
              }
            });
          } else {
            console.warn(`videoFiles không phải là mảng cho sản phẩm ${rating.idSanPhamChiTiet}:`, rating.videoFiles);
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
      const hasErrors = danhGiaResults.some(result => result.status === 'rejected');
      if (hasErrors) {
        console.error('Lỗi tạo đánh giá:', danhGiaResults.filter(r => r.status === 'rejected'));
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
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.rate-button:hover {
  background-color: #218838;
}

.contact-seller-button,
.buy-again-button,
.edit-rate-button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.contact-seller-button {
  background-color: #007bff;
  color: white;
}

.contact-seller-button:hover {
  background-color: #0056b3;
}

.buy-again-button {
  background-color: #ffc107;
  color: black;
}

.buy-again-button:hover {
  background-color: #e0a800;
}

.edit-rate-button {
  background-color: #17a2b8;
  color: white;
}

.edit-rate-button:hover {
  background-color: #138496;
}

.loading-state {
  text-align: center;
  padding: 20px;
  color: #666;
}
</style>
```