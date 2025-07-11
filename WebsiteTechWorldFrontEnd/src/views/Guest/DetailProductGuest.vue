<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import {
  detailSanPham,
  getChiTietBienThe,
  getThongSo,
  getListAnhByMau,
} from "@/Service/GuestService/ProductGuestService";
import { ShoppingCart } from "@element-plus/icons-vue";

const route = useRoute();
const idSanPham = route.params.id;

const formatPrice = (val) => {
  if (!val) return "0₫";
  return new Intl.NumberFormat("vi-VN").format(val) + "₫";
};

const sanPham = ref(null);
const selectedRom = ref(null);
const selectedMau = ref(null);
const selectedImage = ref("");
const bienThe = ref(null); //anh, gia, soLuong
const thongSo = ref(null); //thong so ky thuat specifications
const quantity = ref(1);

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
    const res = await getChiTietBienThe(
      idSanPham,
      selectedMau.value,
      selectedRom.value
    );
    bienThe.value = res;
    if (res.hinhAnh?.length > 0) selectedImage.value = res.hinhAnh[0];
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

// Gọi API lấy biến thể khi màu hoặc ROM thay đổi
watch([selectedMau], () => {
  fetchChiTietBienThe();
  fetchListAnhByMau();
});

watch([selectedRom], () => {
  fetchChiTietBienThe();
  fetchThongSo(); // chỉ khi ROM đổi mới gọi
});

// Gọi khi component được mount
onMounted(() => {
  fetchSanPhamDetail();
});
</script>

<template>
  <section class="product-detail">
    <div class="product-container">
      <!-- Cột trái: Hình ảnh -->
      <div class="product-image">
        <img
          :src="selectedImage || bienThe?.hinhAnh?.[0] || '/img/no-image.png'"
          alt="Hình ảnh sản phẩm"
          class="main-image"
        />

        <!-- Danh sách ảnh thu nhỏ -->
        <div class="thumbnail-list" v-if="bienThe?.hinhAnh?.length > 1">
          <img
            v-for="(img, index) in bienThe.hinhAnh"
            :key="index"
            :src="img"
            class="thumbnail"
            :class="{ active: img === selectedImage }"
            @click="selectedImage = img"
          />
        </div>
      </div>

      <!-- Cột phải: Thông tin + thông số -->
      <div class="product-right">
        <div class="product-info">
          <h1>{{ sanPham?.tenSanPham }}</h1>
          <p class="price">{{ formatPrice(bienThe?.giaBan) }}</p>

          <!-- Màu sắc -->
          <div class="options">
            <h3>Chọn màu:</h3>
            <div class="option-list">
              <el-radio-group v-model="selectedMau">
                <el-radio-button
                  v-for="m in sanPham?.mau"
                  :key="m.id"
                  :label="m.id"
                >
                  {{ m.ten }}
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>

          <!-- ROM -->
          <div class="options">
            <h3>Chọn ROM:</h3>
            <div class="option-list">
              <el-radio-group v-model="selectedRom">
                <el-radio-button
                  v-for="r in sanPham?.rom"
                  :key="r.id"
                  :label="r.id"
                >
                  {{ r.ten }}
                </el-radio-button>
              </el-radio-group>
            </div>
          </div>

          <!-- Số lượng -->
          <div class="options" v-if="bienThe">
            <h3>Số lượng còn: {{ bienThe.soLuong ?? 0 }}</h3>
          </div>

          <!-- Chọn số lượng -->
          <div class="options quantity-selector" v-if="bienThe?.soLuong > 0">
            <h3>Chọn số lượng:</h3>
            <div class="quantity-control">
              <button @click="decreaseQty" :disabled="quantity <= 1">-</button>
              <input
                type="number"
                v-model="quantity"
                min="1"
                :max="bienThe.soLuong"
              />
              <button
                @click="increaseQty"
                :disabled="quantity >= bienThe.soLuong"
              >
                +
              </button>
            </div>
          </div>

          <!-- Hành động -->
          <div class="button-group">
            <el-button
              type="primary"
              :disabled="!selectedRom || !selectedMau || bienThe?.soLuong <= 0"
              class="buy-btn"
            >
              Mua ngay
            </el-button>

            <el-button
              type="success"
              :icon="ShoppingCart"
              :disabled="!selectedRom || !selectedMau || bienThe?.soLuong <= 0"
              class="cart-btn"
            >
              Thêm vào giỏ hàng
            </el-button>
          </div>
        </div>

        <!-- Thông số kỹ thuật -->
        <div class="product-specs" v-if="thongSo">
          <h2>Thông số kỹ thuật</h2>
          <ul class="spec-list">
            <li><strong>CPU:</strong> {{ thongSo.cpu }}</li>
            <li><strong>RAM:</strong> {{ thongSo.ram }}</li>
            <li>
              <strong>Màn hình:</strong> {{ thongSo.tenManHinh }} ({{
                thongSo.kichThuoc
              }})
            </li>
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
            <li>
              <strong>Thời gian sử dụng:</strong> {{ thongSo.thoiGianSuDung }}
            </li>
            <li>
              <strong>Số lần sạc tối đa:</strong> {{ thongSo.soLanSacToiDa }}
            </li>
            <li><strong>Hệ điều hành:</strong> {{ thongSo.heDieuHanh }}</li>
            <li><strong>Xuất xứ:</strong> {{ thongSo.xuatXu }}</li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.product-detail {
  background: #f8f9fa;
  min-height: 100vh;
  padding: 20px 0;
}

.product-container {
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  padding: 40px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  max-width: 1400px;
  margin: 0 auto;
  border: 1px solid #e9ecef;
}

.product-image {
  flex: 1 1 500px;
  max-width: 550px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.main-image {
  width: 100%;
  height: 450px;
  object-fit: contain;
  border-radius: 8px;
  background: #ffffff;
  border: 1px solid #dee2e6;
  transition: box-shadow 0.2s ease;
}

.main-image:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.thumbnail-list {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  justify-content: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.thumbnail {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 6px;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.2s ease;
  background: #fff;
}

.thumbnail:hover {
  border-color: #6c757d;
}

.thumbnail.active {
  border-color: #0d6efd;
  box-shadow: 0 0 0 1px #0d6efd;
}

.product-right {
  flex: 1 1 500px;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.product-info {
  background: #ffffff;
  padding: 30px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.product-info h1 {
  font-size: 28px;
  margin-bottom: 15px;
  color: #212529;
  font-weight: 600;
  line-height: 1.3;
}

.price {
  font-size: 32px;
  color: #dc3545;
  font-weight: 700;
  margin: 20px 0;
  padding: 15px 20px;
  background: #fff5f5;
  border-radius: 8px;
  border-left: 4px solid #dc3545;
  display: inline-block;
}

.options {
  margin-top: 25px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.options h3 {
  font-size: 18px;
  margin-bottom: 15px;
  color: #495057;
  font-weight: 600;
}

.option-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.option-list .el-radio-button {
  border-radius: 6px !important;
  transition: all 0.2s ease;
}

.option-list .el-radio-button:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Quantity Selector Styles */
.quantity-selector {
  margin-top: 20px;
  background: #e8f4fd;
  border: 1px solid #b3d9ff;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 0;
  max-width: 140px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  overflow: hidden;
  background: #ffffff;
}

.quantity-control button {
  width: 40px;
  height: 40px;
  font-size: 18px;
  font-weight: 600;
  background: #f8f9fa;
  color: #495057;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantity-control button:hover:not(:disabled) {
  background: #e9ecef;
  color: #212529;
}

.quantity-control button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
  background: #f8f9fa;
}

.quantity-control input {
  width: 60px;
  height: 40px;
  text-align: center;
  font-size: 16px;
  font-weight: 600;
  border: none;
  border-left: 1px solid #dee2e6;
  border-right: 1px solid #dee2e6;
  background: #ffffff;
  color: #212529;
  outline: none;
}

.quantity-control input:focus {
  background: #f8f9fa;
}

/* Remove spinner arrows from number input */
.quantity-control input::-webkit-outer-spin-button,
.quantity-control input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}


.button-group {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

.buy-btn,
.cart-btn {
  flex: 1;
  font-size: 16px;
  padding: 14px 0;
  text-align: center;
  border-radius: 6px;
  font-weight: 600;
  transition: all 0.2s ease;
}

.buy-btn {
  background: #0d6efd;
  border: none;
  color: white;
}

.buy-btn:hover {
  background: #0b5ed7;
  transform: translateY(-1px);
}

.cart-btn {
  background: #198754;
  border: none;
  color: white;
}

.cart-btn:hover {
  background: #157347;
  transform: translateY(-1px);
}

/* Thông số kỹ thuật */
.product-specs {
  background: #ffffff;
  padding: 30px;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  transition: box-shadow 0.2s ease;
}

.product-specs:hover {
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.product-specs h2 {
  margin-bottom: 25px;
  font-size: 22px;
  color: #212529;
  font-weight: 600;
  border-bottom: 2px solid #0d6efd;
  padding-bottom: 10px;
}

.spec-list {
  list-style: none;
  padding: 0;
  margin: 0;
  font-size: 15px;
  line-height: 1.6;
  color: #495057;
}

.spec-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  margin-bottom: 4px;
  border-radius: 6px;
  transition: background-color 0.2s ease;
}

.spec-list li:nth-child(even) {
  background: #f8f9fa;
}

.spec-list li:hover {
  background: #e9ecef;
}

.spec-list li strong {
  color: #212529;
  font-weight: 500;
  flex: 0 0 40%;
}

.spec-list li span {
  flex: 0 0 55%;
  text-align: right;
  color: #495057;
  font-weight: 400;
}

/* Responsive */
@media (max-width: 768px) {
  .product-container {
    flex-direction: column;
    align-items: center;
    padding: 20px;
    gap: 25px;
  }

  .main-image {
    height: 350px;
  }

  .product-right {
    width: 100%;
  }

  .product-info {
    width: 100%;
    padding: 20px;
  }

  .product-info h1 {
    font-size: 24px;
  }

  .price {
    font-size: 28px;
  }

  .product-specs {
    width: 100%;
    margin: 20px 0;
    padding: 20px;
  }

  .button-group {
    flex-direction: column;
    gap: 12px;
  }

  .buy-btn,
  .cart-btn {
    width: 100%;
    padding: 12px 0;
  }

  .spec-list li {
    flex-direction: column;
    gap: 5px;
    text-align: left;
    padding: 12px 15px;
  }

  .spec-list li strong,
  .spec-list li span {
    flex: none;
    text-align: left;
    width: 100%;
  }

  .thumbnail {
    width: 60px;
    height: 60px;
  }

  .options {
    padding: 15px;
  }

  .quantity-control {
    max-width: 120px;
  }

  .quantity-control button {
    width: 35px;
    height: 35px;
    font-size: 16px;
  }

  .quantity-control input {
    width: 50px;
    height: 35px;
    font-size: 14px;
  }
}
</style>
