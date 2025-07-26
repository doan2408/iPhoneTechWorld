
<!--Start of Tawk.to Script-->
<script type="text/javascript">
var Tawk_API=Tawk_API||{}, Tawk_LoadStart=new Date();
(function(){
var s1=document.createElement("script"),s0=document.getElementsByTagName("script")[0];
s1.async=true;
s1.src='https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5';
s1.charset='UTF-8';
s1.setAttribute('crossorigin','*');
s0.parentNode.insertBefore(s1,s0);
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
import { ShoppingCart } from "@element-plus/icons-vue";
import { ElMessage, ElRate, ElUpload } from "element-plus";
import { ElPagination } from "element-plus";


// Hàm nhúng Tawk.to
const loadTawkTo = () => {
  // Kiểm tra xem Tawk.to đã được tải chưa để tránh tải lại
  if (window.Tawk_API) return;

  // Tạo script element
  const s1 = document.createElement("script");
  s1.async = true;
  s1.src = 'https://embed.tawk.to/68836581db7610192eeaacd6/1j10k90i5';
  s1.charset = 'UTF-8';
  s1.setAttribute('crossorigin', '*');

  // Thêm script vào DOM
  const s0 = document.getElementsByTagName("script")[0];
  s0.parentNode.insertBefore(s1, s0);

  // Khởi tạo Tawk_API
  window.Tawk_API = window.Tawk_API || {};
  window.Tawk_LoadStart = new Date();
};
// import { ElMessage } from "element-plus";
// import headerState from '@/components/Client/modules/headerState';

// const count = ref(0)
// const store = useStore()

// if (!store.hasModule('headerState')) {
//   store.registerModule('headerState', headerState)
// }

// const guiLenHeader = () => {
//   store.commit('headerState/setCartItemCount', count.value)
// }


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

// Review-related refs
const rating = ref(0);
const comment = ref("");
const uploadedImages = ref([]);
const reviews = ref([]);
const averageRating = ref(0);
const starDistribution = ref({});
const currentPage = ref(1);
const pageSize = ref(10);
const totalReviews = ref(0);
const selectedStarFilter = ref(0); // 0 là "Tất cả", 1-5 là số sao
const hasMediaFilter = ref(false); // Lọc đánh giá có hình ảnh/video
const isLoading = ref(false); // Trạng thái tải thêm

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
    // Fetch reviews for the selected variant
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

// Review-related Functions
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
  } catch (error) {
    console.error("Lỗi khi lấy đánh giá:", error);
    ElMessage.error("Không thể tải đánh giá!");
  } finally {
    isLoading.value = false;
  }
};

const fetchAverageRating = async () => {
  try {
    const avg = await DanhGiaSanPhamClientService.tinhDiemTrungBinhSanPham(bienThe.value.idSpct);
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

const handleImageUpload = async (file) => {
  try {
    const imageUrl = await DanhGiaSanPhamClientService.uploadImage(file.raw);
    uploadedImages.value.push(imageUrl);
    ElMessage.success("Tải ảnh lên thành công!");
  } catch (error) {
    console.error("Lỗi khi tải ảnh:", error);
    ElMessage.error("Không thể tải ảnh lên!");
  }
};

const submitReview = async () => {
  if (!user) {
    ElMessage.error("Vui lòng đăng nhập để đánh giá!");
    return;
  }
  if (rating.value < 1 || rating.value > 5) {
    ElMessage.error("Vui lòng chọn số sao từ 1 đến 5!");
    return;
  }
  try {
    const reviewData = {
      idSanPhamChiTiet: bienThe.value.idSpct,
      idKhachHang: user.id,
      soSao: rating.value,
      noiDung: comment.value,
      anonymous: true, // Có thể thêm tùy chọn ẩn danh trong UI
    };
    await DanhGiaSanPhamClientService.taoMoiDanhGia(reviewData);
    ElMessage.success("Đánh giá đã được gửi!");
    rating.value = 0;
    comment.value = "";
    uploadedImages.value = [];
    await fetchReviews();
    await fetchAverageRating();
    await fetchStarDistribution();
  } catch (error) {
    console.error("Lỗi khi gửi đánh giá:", error);
    ElMessage.error(error.response?.data?.message || "Lỗi khi gửi đánh giá!");
  }
};

const filterReviewsByStar = async (star) => {
  selectedStarFilter.value = star;
  currentPage.value = 1;
  console.log("Đã lọc theo số sao:", star);
  console.log("Lọc: soSao =", selectedStarFilter.value, ", hasMedia =", hasMediaFilter.value);
  await fetchReviews();
};

const filterReviewsByMedia = async () => {
  currentPage.value = 1;
  console.log("Đã lọc theo hình ảnh/video:", hasMediaFilter.value);
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

// Watchers
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
      <!-- Product Image Section -->
      <div class="product-image">
        <img
          :src="selectedImage || bienThe?.hinhAnh?.[0] || '/img/no-image.png'"
          alt="Hình ảnh sản phẩm"
          class="main-image"
        />

        <div class="thumbnail-list" v-if="bienThe?.hinhAnh?.length > 0">
          <img v-for="(img, index) in bienThe.hinhAnh" :key="index" :src="img" class="thumbnail"
            :class="{ active: img === selectedImage }" @click="selectedImage = img" />
        </div>
      </div>

      <!-- Product Right Section -->
      <div class="product-right">
        <!-- Product Info -->
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

        <!-- Product Specs -->
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

        <!-- Reviews Section -->
        <div class="product-reviews">
          <h2>Đánh giá sản phẩm</h2>
          <div class="rating-summary">
            <h3>Điểm trung bình: {{ averageRating.toFixed(1) }} / 5</h3>
            <el-rate v-model="averageRating" disabled allow-half />
            <div class="star-distribution">
              <div v-for="star in [5, 4, 3, 2, 1]" :key="star" class="star-bar">
                <span>{{ star }} sao:</span>
                <div class="progress-bar">
                  <div
                    :style="{ width: ((starDistribution[star] || 0) / totalReviews * 100) + '%' }"
                    class="progress"
                  ></div>
                </div>
                <span>{{ starDistribution[star] || 0 }} đánh giá</span>
              </div>
            </div>
          </div>

          <!-- Review Filters -->
          <div class="review-filters">
            <el-button
              v-for="star in [0, 5, 4, 3, 2, 1]"
              :key="star"
              @click="filterReviewsByStar(star)"
              :type="selectedStarFilter === star ? 'primary' : 'default'"
            >
              {{ star === 0 ? 'Tất cả' : `${star} sao` }}
            </el-button>
            <el-button
              @click="hasMediaFilter = !hasMediaFilter; filterReviewsByMedia()"
              :type="hasMediaFilter ? 'primary' : 'default'"
            >
              Có hình ảnh/video
            </el-button>
          </div>

          <!-- Add Review Form -->
          <div class="add-review" v-if="user">
            <h3>Viết đánh giá của bạn</h3>
            <el-rate v-model="rating" />
            <el-input
              type="textarea"
              v-model="comment"
              placeholder="Nhập bình luận của bạn..."
              :rows="4"
              class="comment-input"
            />
            <el-upload
              :file-list="uploadedImages"
              :on-change="handleImageUpload"
              :auto-upload="true"
              list-type="picture-card"
              :limit="5"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-button type="primary" @click="submitReview">Gửi đánh giá</el-button>
          </div>
          <div v-else class="login-prompt">
            <p>Vui lòng <a href="/login">đăng nhập</a> để viết đánh giá.</p>
          </div>

          <!-- Reviews List -->
          <div class="reviews-list">
            <div v-for="review in reviews" :key="review.id" class="review-item">
              <div class="review-header">
                <span class="review-user">{{ review.anonymous ? 'Người mua' : review.tenKhachHang }}</span>
                <span class="purchased-label" v-if="review.hasPurchased">Đã mua hàng</span>
                <el-rate v-model="review.soSao" disabled />
                <span class="review-date">{{ new Date(review.ngayDanhGia).toLocaleDateString("vi-VN") }}</span>
              </div>
              <p class="review-comment">{{ review.noiDung }}</p>
              <div class="review-media" v-if="review.hinhAnh?.length || review.video">
                <div class="review-images" v-if="review.hinhAnh?.length">
                  <img
                    v-for="(img, index) in review.hinhAnh"
                    :key="index"
                    :src="img"
                    alt="Hình ảnh đánh giá"
                    class="review-image"
                  />
                </div>
                <video
                  v-if="review.video"
                  :src="review.video"
                  controls
                  class="review-video"
                ></video>
              </div>
            </div>
            <div v-if="isLoading" class="loading">Đang tải thêm...</div>
          </div>

          <!-- Load More Button -->
          <div class="load-more" v-if="reviews.length < totalReviews && !isLoading">
            <el-button type="primary" @click="loadMoreReviews">Xem thêm</el-button>
          </div>

          <!-- Pagination (Optional) -->
          <el-pagination
            v-if="totalReviews > pageSize"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="totalReviews"
            layout="prev, pager, next"
            @current-change="handlePageChange"
          />
        </div>
      </div>
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
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto;
}

/* Product Image */
.product-image {
  flex: 1 1 450px;
  max-width: 500px;
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

/* Product Right */
.product-right {
  flex: 1 1 450px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Product Info */
/* .product菜
  background: #ffffff;
  padding: 30px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
} */
  /* padding: 20px;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
} */

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

/* Options styling */
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


/* Quantity styles */
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


/* Button group */
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

/* Specifications */
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

/* Stock info */
.options:has(.quantity-selector) h3 {
  color: #52c41a;
  font-weight: 500;
}

/* Reviews Section */
.product-reviews {
  background: #ffffff;
  padding: 30px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  margin-top: 30px;
}

.product-reviews h2 {
  margin-bottom: 25px;
  font-size: 22px;
  color: #212529;
  font-weight: 600;
  border-bottom: 2px solid #0d6efd;
  padding-bottom: 10px;
}

.rating-summary {
  margin-bottom: 30px;
}

.rating-summary h3 {
  font-size: 18px;
  color: #495057;
  margin-bottom: 10px;
}

.star-distribution {
  margin-top: 15px;
}

.star-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.progress-bar {
  flex: 1;
  height: 10px;
  background: #e9ecef;
  border-radius: 5px;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: #0d6efd;
  transition: width 0.3s ease;
}

.review-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.review-filters .el-button {
  border-radius: 20px;
  padding: 8px 16px;
  font-size: 14px;
}

.add-review {
  margin-bottom: 30px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.add-review h3 {
  font-size: 18px;
  color: #495057;
  margin-bottom: 15px;
}

.comment-input {
  margin: 15px 0;
}

.reviews-list {
  margin-bottom: 20px;
}

.review-item {
  padding: 15px;
  border-bottom: 1px solid #e9ecef;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}

.review-user {
  font-weight: 600;
  color: #212529;
}

.purchased-label {
  background: #e7f3ff;
  color: #0d6efd;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.review-date {
  color: #6c757d;
  font-size: 14px;
}

.review-comment {
  color: #495057;
  line-height: 1.6;
  margin-bottom: 10px;
}

.review-media {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.review-images {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.review-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #dee2e6;
}

.review-video {
  width: 300px;
  height: auto;
  border-radius: 6px;
}

.load-more {
  text-align: center;
  margin-top: 20px;
}

.load-more .el-button {
  border-radius: 20px;
  padding: 10px 30px;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #6c757d;
}

.login-prompt {
  padding: 15px;
  background: #fff3cd;
  border: 1px solid #ffeeba;
  border-radius: 6px;
  color: #856404;
  text-align: center;
}

.login-prompt a {
  color: #0d6efd;
  text-decoration: none;
}

.login-prompt a:hover {
  text-decoration: underline;
}

/* Responsive */
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
</style>