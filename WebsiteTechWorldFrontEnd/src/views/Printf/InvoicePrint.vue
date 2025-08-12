<template>
    <div id="print-section">
        <div class="header">
            <img src="/src/assets/Logo.png" alt="Tech Work Logo" class="logo" />
            <h2>HÓA ĐƠN BÁN HÀNG</h2>
        </div>

        <div class="info">
            <p><strong>Mã hóa đơn:</strong> {{ invoice?.maHoaDon || 'N/A' }}</p>
            <p><strong>Ngày thanh toán:</strong> {{ invoice?.ngayThanhToan || 'N/A' }}</p>
            <p><strong>Người mua:</strong> {{ invoice?.tenNguoiMua || 'N/A' }} - {{ invoice?.sdtNguoiMua || 'N/A' }}</p>
            <p><strong>Người nhận:</strong> {{ invoice?.tenNguoiNhan || 'N/A' }} - {{ invoice?.sdtNguoiNhan || 'N/A' }}
            </p>
            <p v-if="invoice?.isShipping"><strong>Địa chỉ giao hàng:</strong> {{ invoice?.diaChiGiaoHang || 'N/A' }}</p>
        </div>

        <table class="product-table" v-if="imeiList.length > 0">
            <thead>
                <tr>
                    <th>STT</th>
                    <th>Tên sản phẩm</th>
                    <th>Mô tả</th>
                    <th>IMEI</th>
                    <th>Đơn giá</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in imeiList" :key="index">
                    <td>{{ index + 1 }}</td>
                    <td>{{ item.tenSanPham || 'N/A' }}</td>
                    <td>{{ item.dungLuongRom }} • {{ item.tenMau }}</td>
                    <td>{{ item.soImei || 'N/A' }}</td>
                    <td>{{ formatCurrency(item.giaBan) }}</td>
                </tr>
            </tbody>
        </table>    
        <p v-else>Không có sản phẩm trong hóa đơn.</p>

        <div class="summary">
            <p><strong>Tổng tiền:</strong> {{ formatCurrency(invoice?.tongTien) }}</p>
            <p><strong>Phí Ship:</strong> {{ formatCurrency(invoice?.phiShip) }}</p>
            <p><strong>Giảm giá:</strong> {{ formatCurrency(invoice?.soTienGiam) }}</p>
            <p><strong>Thành tiền:</strong> {{ formatCurrency(invoice?.thanhTien) }}</p>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue';
import axios from 'axios';
import { hoaDonDetail, findHdctByImeiDaBan } from '@/Service/Adminservice/HoaDon/HoaDonAdminServices'
defineEmits(['close']);
const props = defineProps({ idHoaDon: Number });
const invoice = ref(null);
const imeiList = ref([]);

const formatCurrency = (val) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
};
const pageNoHdctByImei = ref(0);
const pageSizeHdctByImei = ref(10);
const getHdctByImeiDaBan = async () => {
    const storedId = props.idHoaDon
    const res = await findHdctByImeiDaBan(pageNoHdctByImei.value, pageSizeHdctByImei.value, storedId);
    imeiList.value = res.data.content ;
    console.log(res.data.content)
    console.log(imeiList.value);
    
}
onMounted(async () => {
    try {
        const res = await hoaDonDetail(props.idHoaDon);
        invoice.value = res.data || {};
        await getHdctByImeiDaBan();
    } catch (err) {
        console.error('Lỗi tải hóa đơn:', err);
    }
});
</script>

<style scoped>
#print-section {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    padding: 40px;
    background: #f8f9fa;
    border: 1px solid #ccc;
    border-radius: 12px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 20px;
}

.header .logo {
    width: 150px;
    height: auto;
}

.info p {
    margin: 4px 0;
}

.product-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
    background-color: #fff;
}

.product-table th,
.product-table td {
    border: 1px solid #000;
    padding: 8px;
    text-align: left;
}

.product-table th {
    background-color: #007bff;
    color: white;
}

.summary {
    margin-top: 20px;
    padding-top: 10px;
    border-top: 2px solid #007bff;
}
</style>