<script setup>
import { ref, onMounted, watch, nextTick } from "vue";
import { useRoute } from "vue-router";
import {
  detailSanPham,
  getChiTietBienThe,
  getThongSo,
  getListAnhByMau,
} from "@/Service/ClientService/Products/ProductClientService";
import { cartService } from '@/service/ClientService/GioHang/GioHangClientService';
import { ShoppingCart } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";

const user = JSON.parse(localStorage.getItem("user"));

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

const themVaoGio = async () => {
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
      soLuong: 1,
      soLuong: quantity.value
    });

    ElMessage.success("Sản phẩm đã được thêm vào giỏ hàng!");
  } catch (error) {
    console.error("Lỗi khi thêm sản phẩm vào giỏ hàng:", error);
    ElMessage.error(error.response?.data?.message || "Lỗi khi thêm sản phẩm vào giỏ hàng!");
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
  window.scrollTo({ top: 0, left: 0, behavior: "smooth" });
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
        <div class="thumbnail-list" v-if="bienThe?.hinhAnh?.length > 0">
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
              @click="themVaoGio"
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

.product-right {
  flex: 1 1 450px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.product-info {
  background: #ffffff;
  padding: 20px;
  border-radius: 6px;
  border: 1px solid #e5e5e5;
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
}
</style>
