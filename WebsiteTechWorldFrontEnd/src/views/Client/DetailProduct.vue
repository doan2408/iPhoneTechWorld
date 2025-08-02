```vue
<!--Start of Tawk.to Script-->
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
<!--End of Tawk.to Script-->

<script setup>
import { ref, onMounted, watch, nextTick } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from 'vuex';
import {
  detailSanPham,
  getChiTietBienThe,
  getThongSo,
  getListAnhByMau,
} from "@/Service/ClientService/Products/ProductClientService";
import { cartService } from "@/service/ClientService/GioHang/GioHangClientService";
import { DanhGiaSanPhamClientService } from "@/Service/ClientService/DanhGiaSanPham/DanhGiaSanPhamClientService";
import { PhanHoiDanhGiaClientService } from "@/Service/ClientService/PhanHoiDanhGia/PhanHoiDanhGiaClientService";
import { ShoppingCart } from "@element-plus/icons-vue";
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

import headerState from '@/components/Client/modules/headerState';

const count = ref(0)
const store = useStore()

if (!store.hasModule('headerState')) {
  store.registerModule('headerState', headerState)
}

const guiLenHeader = () => {
  store.commit('headerState/setCartItemCount', count.value)
}

const user = JSON.parse(localStorage.getItem("user"));
const route = useRoute();
const idSanPham = route.params.id;

const router = useRouter();

const formatPrice = (val) => {
  if (!val) return "0₫";
  return new Intl.NumberFormat("vi-VN").format(val) + "₫";
};

const sanPham = ref(null);
const selectedRom = ref(null);
const selectedMau = ref(null);
const selectedImage = ref(null);
const bienThe = ref(null);
const thongSo = ref(null);
const quantity = ref(1);

const rating = ref(0);
const comment = ref("");
const uploadedImages = ref([]);
const reviews = ref([]);
const averageRating = ref(0);
const starDistribution = ref({});
const currentPage = ref(1);
const pageSize = ref(10);
const totalReviews = ref(0);
const selectedStarFilter = ref(0);
const hasMediaFilter = ref(false);
const isLoading = ref(false);
const phanHoiByDanhGia = ref({});

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
    const res = await getChiTietBienThe(idSanPham, selectedMau.value, selectedRom.value);
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
      ElMessage.error(`Số lượng vượt quá tồn kho. Trong giỏ hàng đã có ${soLuongHienTai} sản phẩm này.`);
      return;
    }
    await cartService.addToCart({
      idKhachHang: user.id,
      idSanPhamChiTiet: bienThe.value.idSpct,
      soLuong: quantity.value
    });

    if (buy) {
      try {
        await router.push({ path: '/client/shopping-cart', query: { selected: bienThe.value.idSpct } });
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
      console.error('Lỗi khi tải giỏ hàng:', error);
    }
    guiLenHeader()
  } catch (error) {
    console.error("Lỗi khi thêm sản phẩm vào giỏ hàng:", error);
    ElMessage.error(error.response?.data?.message || "Lỗi khi thêm sản phẩm vào giỏ hàng!");
  }
};

const fetchReviews = async () => {
  try {
    isLoading.value = true;
    const response = await DanhGiaSanPhamClientService.layDanhGiaTheoSanPham(
      bienThe.value.idSpct,
      currentPage.value - 1,
      pageSize.value,
      selectedStarFilter.value === 0 ? null : selectedStarFilter.value,
      hasMediaFilter.value ? true : null
    );

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
  try {
    const distribution = await DanhGiaSanPhamClientService.thongKeSoSaoSanPham(bienThe.value.idSpct);
    starDistribution.value = distribution;
  } catch (error) {
    console.error("Lỗi khi lấy thống kê sao:", error);
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

watch([selectedMau, selectedRom], () => {
  fetchChiTietBienThe();
  fetchListAnhByMau();
  fetchThongSo();
});

onMounted(() => {
  window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
  fetchSanPhamDetail();
  setupInfiniteScroll();
});
</script>

<template>
  <section class="product-detail">
    <div class="product-container">
      <div class="product-image">
        <img :src="selectedImage || bienThe?.hinhAnh?.[0] || '/img/no-image.png'" alt="Hình ảnh sản phẩm"
          class="main-image" />
        <div class="thumbnail-list" v-if="bienThe?.hinhAnh?.length > 0">
          <img v-for="(img, index) in bienThe.hinhAnh" :key="index" :src="img" class="thumbnail"
            :class="{ active: img === selectedImage }" @click="selectedImage = img" />
        </div>
      </div>

      <div class="product-right">
        <div class="product-info">
          <h1>{{ sanPham?.tenSanPham }}</h1>
          <p class="price">{{ formatPrice(bienThe?.giaBan) }}</p>
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
          <div class="options quantity-selector" v-if="bienThe?.soLuong > 0">
            <h3>Chọn số lượng:</h3>
            <div class="quantity-control">
              <button @click="decreaseQty" :disabled="quantity <= 1">-</button>
              <input type="number" v-model="quantity" min="1" :max="bienThe.soLuong" />
              <button @click="increaseQty" :disabled="quantity >= bienThe.soLuong">+</button>
            </div>
          </div>
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

        <div class="product-specs" v-if="thongSo">
          <h2>Thông số kỹ thuật</h2>
          <ul class="spec-list">
            <li><strong>CPU:</strong> {{ thongSo.cpu }}</li>
            <li><strong>RAM:</strong> {{ thongSo.ram }}</li>
            <li><strong>Màn hình:</strong> {{ thongSo.tenManHinh }} ({{ thongSo.kichThuoc }})</li>
            <li><strong>Rom:</strong> {{ thongSo.rom }}</li>
            <li><strong>Loại màn hình:</strong> {{ thongSo.loaiManHinh }}</li>
            <li><strong>Độ phân giải:</strong> {{ thongSo.doPhanGiai }}</li>
            <li><strong>Tần số quét:</strong> {{ thongSo.tanSoQuet }}</li>
            <li><strong>Độ sáng:</strong> {{ thongSo.doSang }}</li>
            <li><strong>Chất liệu kính:</strong> {{ thongSo.chatLieuKinh }}</li>
            <li><strong>Camera sau:</strong> {{ thongSo.cameraSau }}</li>
            <li><strong>Camera trước:</strong> {{ thongSo.cameraTruoc }}</li>
            <li><strong>Pin:</strong> {{ thongSo.phienBanPin }}</li>
            <li><strong>Công suất sạc:</strong> {{ thongSo.congXuatSac }}</li>
            <li><strong>Thời gian sử dụng:</strong> {{ thongSo.thoiGianSuDung }}</li>
            <li><strong>Số lần sạc tối đa:</strong> {{ thongSo.soLanSacToiDa }}</li>
            <li><strong>Hệ điều hành:</strong> {{ thongSo.heDieuHanh }}</li>
            <li><strong>Xuất xứ:</strong> {{ thongSo.xuatXu }}</li>
          </ul>
        </div>
      </div>


    </div>

    <div class="product-reviews">
      <h2>Đánh giá sản phẩm</h2>
      <div class="rating-summary">
        <h3>Điểm trung bình: {{ averageRating.toFixed(1) }} / 5</h3>
        <el-rate v-model="averageRating" disabled allow-half />
        <div class="star-distribution">
          <div v-for="star in [5, 4, 3, 2, 1]" :key="star" class="star-bar">
            <span>{{ star }} sao:</span>
            <div class="progress-bar">
              <div :style="{ width: ((starDistribution[star] || 0) / totalReviews * 100) + '%' }" class="progress">
              </div>
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
        <div v-for="review in reviews" :key="review.idDanhGia" class="review-item">
          <div class="review-header">
            <span class="review-user">{{ review.anonymous ? 'Người mua' : review.tenKhachHang }}</span>
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
  flex: 1 1 450px;
  max-width: 500px;
  max-height: 600px;
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

.product-right {
  flex: 1 1 450px;
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
  font-size: 28px;
  color: #d70018;
  font-weight: 700;
  margin: 15px 0;
  display: inline-block;
}

.options {
  margin-top: 20px;
  padding: 12px 0;
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

.product-specs {
  background: #ffffff;
  padding: 20px;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
}

.product-specs h2 {
  margin-bottom: 15px;
  font-size: 18px;
  color: #333;
  font-weight: 600;
  border-bottom: 1px solid #e5e5e5;
  padding-bottom: 8px;
}

.spec-list {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
  color: #666;
}

.spec-list li {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 8px 0;
  border-bottom: 1px solid #f5f5f5;
}

.spec-list li:last-child {
  border-bottom: none;
}

.spec-list li strong {
  color: #333;
  font-weight: 500;
  flex: 0 0 35%;
  margin-right: 15px;
}

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
  .product-container {
    flex-direction: column;
    padding: 15px;
    gap: 20px;
  }

  .main-image {
    height: 300px;
  }

  .product-info {
    padding: 15px;
  }

  .product-info h1 {
    font-size: 20px;
  }

  .price {
    font-size: 24px;
  }

  .product-specs {
    padding: 15px;
  }

  .button-group {
    flex-direction: column;
    gap: 10px;
  }

  .buy-btn,
  .cart-btn {
    width: 100%;
    padding: 12px 0;
  }

  .spec-list li {
    flex-direction: column;
    gap: 4px;
    align-items: flex-start;
    padding: 10px 0;
  }

  .spec-list li strong {
    flex: none;
    margin-right: 0;
    margin-bottom: 2px;
  }

  .thumbnail {
    width: 50px;
    height: 50px;
  }

  .quantity-control {
    max-width: 110px;
  }

  .quantity-control button {
    width: 28px;
    height: 28px;
    font-size: 14px;
  }

  .quantity-control input {
    width: 54px;
    height: 28px;
    font-size: 13px;
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
```