<script setup>
import { ref, onMounted } from "vue";
import { getAdressesClient, updateAddress } from "@/Service/Adminservice/TaiKhoan/KhachHangServices";
import { useRoute, useRouter } from "vue-router";
import { add } from "@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService";

const addresses = ref([]);
const error = ref(null);
const selectedAddressId = ref(null); // Theo dõi địa chỉ được click để hiện nút
const route = useRoute();
const router = useRouter();

const loadAddresses = async () => {
  try {
    const idKhachHang = route.params.idClient;
    console.log(idKhachHang);
    const data = await getAdressesClient(idKhachHang);
    addresses.value = data;
  } catch (e) {
    error.value = e;
  }
};

// const selectAddress = (id) => {
//   // Chỉ cho phép chọn địa chỉ phụ (không phải mặc định)
//   const address = addresses.value.find((addr) => addr.id === id);
//   if (!address.diaChiChinh) {
//     selectedAddressId.value = selectedAddressId.value === id ? null : id;
//   }
// };

// const setPrimary = async (address) => {
//   // Đặt địa chỉ này làm mặc định
//   addresses.value = addresses.value.map((addr) => ({
//     ...addr,
//     diaChiChinh: addr.id === address.id,
//   }));
//   // Ẩn nút sau khi đặt mặc định
//   selectedAddressId.value = null;
//   // TODO: Gọi API để cập nhật địa chỉ mặc định nếu có, ví dụ:
//   // await updatePrimaryAddressClient(address.id, true)
// };



const goToEdit = (addressId) => {
  router.push({
    path: `/admin/client/address/${addressId}/${route.params.idClient}`,
    query: { redirect: `/admin/client/addresses/${route.params.idClient}` },
  });
};

const back = () => {
  router.push("/admin/client")
};

onMounted(() => {
  loadAddresses();
});
</script>

<template>
  <div class="address-view">
    <h2 v-if="addresses.length == 0" style="text-align: center;">Khách hàng chưa cập nhật địa chỉ</h2>
    <h2 v-else>Địa chỉ giao hàng</h2>
    <div class="address-list">
      <div
        class="address-item"
        v-for="address in addresses"
        :key="address.id"
        :class="{ 'is-default': address.diaChiChinh }"
        @click="!address.diaChiChinh && selectAddress(address.id)"
      >
        <div class="address-content">
          <div class="address-details">
            <p><strong>Người nhận:</strong> {{ address.tenNguoiNhan != null ? address.tenNguoiNhan : 'Không có thông tin' }}</p>
            <p>
              <strong>Số điện thoại người nhận:</strong>
              {{ address.sdtNguoiNhan != null ? address.sdtNguoiNhan : 'Không có thông tin' }}
            </p>
            <p>
              <strong>Địa chỉ:</strong>
              {{ address.soNha != null? address.soNha : '' }}, {{ address.tenDuong != null? address.tenDuong : '' }},
              {{ address.xaPhuong != null? address.xaPhuong : '' }}, {{ address.quanHuyen != null? address.quanHuyen : '' }},
              {{ address.tinhThanhPho != null? address.tinhThanhPho : '' }}
            </p>
          </div>
          <span
            v-if="address.diaChiChinh"
            style="
              color: red;
              border: 1px solid red;
              padding: 2px 6px;
              border-radius: 10px;
            "
          >
            Mặc định
          </span>
          <div class="action-buttons">
            <button
              class="btn-edit"
              title="Chỉnh sửa"
              @click.stop="goToEdit(address.id)"
            >
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button class="btn-delete" title="Xóa" @click.stop>
              <i class="bi bi-trash-fill"></i>
            </button>
            <button
              v-if="selectedAddressId === address.id && !address.diaChiChinh"
              class="btn-set-primary"
              @click.stop="setPrimary(address)"
            >
              Đặt làm mặc định
            </button>
          </div>
        </div>
      </div>
      <button @click="back">Quay lại</button>
    </div>
  </div>
</template>

<style scoped>
.address-view {
  padding: 20px;
}

.address-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.address-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  transition: background-color 0.2s ease;
}

.address-item:not(.is-default) {
  cursor: pointer;
}

.address-item:not(.is-default):hover {
  background-color: #f9f9f9;
}

.is-default {
  cursor: default;
}

.address-content {
  display: flex;
  align-items: center;
  width: 100%;
  gap: 20px;
}

.address-details {
  flex: 1;
}

.action-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
}

.btn-edit,
.btn-delete {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  padding: 5px;
}

.btn-edit:hover {
  color: #007bff;
}

.btn-delete:hover {
  color: #dc3545;
}

.btn-set-primary {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
  transform: translateX(0);
  opacity: 1;
  transition: transform 0.3s ease, opacity 0.3s ease;
  animation: slideIn 0.3s ease forwards;
}

.address-item:not(.selected) .btn-set-primary {
  transform: translateX(100px);
  opacity: 0;
}

@keyframes slideIn {
  from {
    transform: translateX(100px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes slideOut {
  from {
    transform: translateX(0);
    opacity: 1;
  }
  to {
    transform: translateX(-100px);
    opacity: 0;
  }
}
</style>
