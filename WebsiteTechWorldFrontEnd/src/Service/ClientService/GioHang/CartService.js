import { computed, ref } from 'vue';

const cartData = ref([]);

const khoiTaoGioHang = () => {
  try {
    const storedCart = localStorage.getItem('shoppingCart');
    cartData.value = storedCart ? JSON.parse(storedCart) : [];
    if (!Array.isArray(cartData.value)) {
      cartData.value = [];
      localStorage.setItem('shoppingCart', JSON.stringify(cartData.value));
    }
  } catch (error) {
    console.error('Lỗi khi khởi tạo giỏ hàng:', error);
    cartData.value = [];
    localStorage.setItem('shoppingCart', JSON.stringify(cartData.value));
  }
};

const luuGioHangVaoLocalStorage = () => {
  try {
    localStorage.setItem('shoppingCart', JSON.stringify(cartData.value));
  } catch (error) {
    console.error('Lỗi khi lưu giỏ hàng vào localStorage:', error);
  }
};

const setupStorageListener = () => {
  window.addEventListener('storage', (event) => {
    if (event.key === 'shoppingCart') {
      try {
        cartData.value = JSON.parse(event.newValue || '[]');
      } catch (error) {
        console.error('Lỗi khi đồng bộ giỏ hàng từ storage:', error);
        cartData.value = [];
      }
    }
  });
};

const themVaoGio = (
  idSanPhamChiTiet,
  soLuong,
  tenSanPham,
  phienBan,
  imageUrl,
  gia,
  soLuongTon
) => {
  try {
    const itemHienTai = cartData.value.find(
      (item) => item.idSanPhamChiTiet === idSanPhamChiTiet
    );
    const soLuongHienTai = itemHienTai ? itemHienTai.soLuong : 0;

    if (soLuongHienTai + soLuong > soLuongTon) {
      return false; 
    }

    if (itemHienTai) {
      itemHienTai.soLuong = soLuongHienTai + soLuong;
      itemHienTai.ngayThem = new Date().toISOString();
    } else {
      cartData.value.push({
        idGioHangChiTiet: Date.now(),
        idSanPhamChiTiet,
        soLuong,
        tenSanPham,
        phienBan,
        imageUrl,
        gia,
        soLuongTon,
        ngayThem: new Date().toISOString(),
      });
    }

    luuGioHangVaoLocalStorage();
    return true;
  } catch (error) {
    console.error('Lỗi khi thêm sản phẩm vào giỏ hàng:', error);
    return false;
  }
};

const xoaSanPhamKhoiGio = (idSanPhamChiTiet) => {
  try {
    const initialLength = cartData.value.length;
    cartData.value = cartData.value.filter(
      (item) => item.idSanPhamChiTiet !== idSanPhamChiTiet
    );
    luuGioHangVaoLocalStorage();
    return cartData.value.length < initialLength;
  } catch (error) {
    console.error('Lỗi khi xóa sản phẩm khỏi giỏ hàng:', error);
    return false;
  }
};

const capNhatSoLuong = (idSanPhamChiTiet, soLuong) => {
  try {
    const item = cartData.value.find(
      (item) => item.idSanPhamChiTiet === idSanPhamChiTiet
    );
    if (item && soLuong > 0 && soLuong <= item.soLuongTon) {
      item.soLuong = soLuong;
      item.ngayThem = new Date().toISOString();
      luuGioHangVaoLocalStorage();
      return true;
    }
    return false;
  } catch (error) {
    console.error('Lỗi khi cập nhật số lượng sản phẩm:', error);
    return false;
  }
};

const cartCount = computed(() => {
  return cartData.value.length;
});

const clearCart = () => {
  cartData.value = [];
  luuGioHangVaoLocalStorage();
};

khoiTaoGioHang();
setupStorageListener();

export const CartService = {
  cartData,
  themVaoGio,
  xoaSanPhamKhoiGio,
  capNhatSoLuong,
  cartCount,
  clearCart,
};