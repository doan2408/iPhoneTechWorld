<template>
    <div class="page-container">
      <div class="container">
        <!-- Header với gradient và shadow -->
        <div class="header-section">
          <div class="header-blur"></div>
          <div class="header-content">
            <div class="header-flex">
              <button @click="goBack" class="back-button">
                <ArrowLeftIcon class="back-icon" />
              </button>
              <div>
                <h1 class="page-title">Cập nhật hóa đơn</h1>
                <p class="page-subtitle">
                  <span class="invoice-badge">{{ hoaDon.ma_hoa_don }}</span>
                </p>
              </div>
            </div>
          </div>
        </div>
  
        <!-- Form với animation -->
        <form @submit.prevent="handleSubmit" class="form-container">
          <!-- Thông tin cơ bản -->
          <div class="card-wrapper">
            <div class="card">
              <div class="card-header">
                <div class="header-flex">
                  <div class="icon-container blue">
                    <FileTextIcon class="icon" />
                  </div>
                  <div>
                    <h2 class="card-title">Thông tin cơ bản</h2>
                    <p class="card-subtitle">Thông tin chung của hóa đơn</p>
                  </div>
                </div>
              </div>
              <div class="card-content">
                <div class="grid-2">
                  <div class="form-group">
                    <label for="ma_hoa_don" class="form-label">Mã hóa đơn</label>
                    <input
                      id="ma_hoa_don"
                      v-model="hoaDon.ma_hoa_don"
                      disabled
                      class="form-input disabled"
                    />
                  </div>
  
                  <div class="form-group">
                    <label for="ma_van_don" class="form-label">Mã vận đơn</label>
                    <input
                      id="ma_van_don"
                      v-model="hoaDon.ma_van_don"
                      placeholder="Nhập mã vận đơn"
                      class="form-input"
                    />
                  </div>
  
                  <div class="form-group">
                    <label for="loai_hoa_don" class="form-label">Loại hóa đơn</label>
                    <select
                      id="loai_hoa_don"
                      v-model="hoaDon.loai_hoa_don"
                      class="form-select"
                    >
                      <option value="">Chọn loại hóa đơn</option>
                      <option value="Bán lẻ">Bán lẻ</option>
                      <option value="Bán sỉ">Bán sỉ</option>
                      <option value="Online">Online</option>
                    </select>
                  </div>
  
                  <div class="form-group">
                    <label for="ngay_dat_hang" class="form-label">Ngày đặt hàng</label>
                    <input
                      id="ngay_dat_hang"
                      v-model="hoaDon.ngay_dat_hang"
                      type="date"
                      class="form-input"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
  
          <!-- Thông tin khách hàng -->
          <div class="card-wrapper">
            <div class="card">
              <div class="card-header">
                <div class="header-flex">
                  <div class="icon-container green">
                    <UsersIcon class="icon" />
                  </div>
                  <div>
                    <h2 class="card-title">Thông tin khách hàng</h2>
                    <p class="card-subtitle">Chi tiết về người mua và người nhận</p>
                  </div>
                </div>
              </div>
              <div class="card-content">
                <div class="grid-2">
                  <div class="form-group">
                    <label for="ten_nguoi_mua" class="form-label required">Tên người mua</label>
                    <input
                      id="ten_nguoi_mua"
                      v-model="hoaDon.ten_nguoi_mua"
                      required
                      class="form-input"
                      placeholder="Nhập tên người mua"
                    />
                  </div>
                  <div class="form-group">
                    <label for="sdt_nguoi_mua" class="form-label required">SĐT người mua</label>
                    <input
                      id="sdt_nguoi_mua"
                      v-model="hoaDon.sdt_nguoi_mua"
                      required
                      class="form-input"
                      placeholder="Nhập số điện thoại"
                    />
                  </div>
                </div>
  
                <div class="separator"></div>
  
                <div class="grid-2">
                  <div class="form-group">
                    <label for="ten_nguoi_nhan" class="form-label required">Tên người nhận</label>
                    <input
                      id="ten_nguoi_nhan"
                      v-model="hoaDon.ten_nguoi_nhan"
                      required
                      class="form-input"
                      placeholder="Nhập tên người nhận"
                    />
                  </div>
                  <div class="form-group">
                    <label for="sdt_nguoi_nhan" class="form-label required">SĐT người nhận</label>
                    <input
                      id="sdt_nguoi_nhan"
                      v-model="hoaDon.sdt_nguoi_nhan"
                      required
                      class="form-input"
                      placeholder="Nhập số điện thoại"
                    />
                  </div>
                </div>
  
                <div class="form-group">
                  <label for="dia_chi_giao_hang" class="form-label required">Địa chỉ giao hàng</label>
                  <textarea
                    id="dia_chi_giao_hang"
                    v-model="hoaDon.dia_chi_giao_hang"
                    required
                    rows="3"
                    placeholder="Nhập địa chỉ giao hàng đầy đủ"
                    class="form-textarea"
                  ></textarea>
                </div>
              </div>
            </div>
          </div>
  
          <!-- Thông tin giao hàng -->
          <div class="card-wrapper">
            <div class="card">
              <div class="card-header">
                <div class="header-flex">
                  <div class="icon-container orange">
                    <TruckIcon class="icon" />
                  </div>
                  <div>
                    <h2 class="card-title">Thông tin giao hàng</h2>
                    <p class="card-subtitle">Chi tiết về việc giao hàng</p>
                  </div>
                </div>
              </div>
              <div class="card-content">
                <div class="checkbox-group">
                  <div class="checkbox-wrapper">
                    <input
                      id="is_shipping"
                      v-model="hoaDon.is_shipping"
                      type="checkbox"
                      class="checkbox"
                    />
                    <CheckIcon v-if="hoaDon.is_shipping" class="check-icon" />
                  </div>
                  <label for="is_shipping" class="checkbox-label">Có giao hàng</label>
                </div>
  
                <div v-if="hoaDon.is_shipping" class="shipping-details">
                  <div class="grid-2">
                    <div class="form-group">
                      <label for="shipping_method" class="form-label">Phương thức giao hàng</label>
                      <select
                        id="shipping_method"
                        v-model="hoaDon.shipping_method"
                        class="form-select"
                      >
                        <option value="">Chọn phương thức giao hàng</option>
                        <option value="Giao hàng tiêu chuẩn">Giao hàng tiêu chuẩn</option>
                        <option value="Giao hàng nhanh">Giao hàng nhanh</option>
                        <option value="Giao hàng hỏa tốc">Giao hàng hỏa tốc</option>
                      </select>
                    </div>
  
                    <div class="form-group">
                      <label for="phi_ship" class="form-label">Phí ship (VNĐ)</label>
                      <div class="input-with-icon">
                        <input
                          id="phi_ship"
                          v-model.number="hoaDon.phi_ship"
                          type="number"
                          min="0"
                          class="form-input with-icon"
                          placeholder="0"
                        />
                        <div class="input-icon">₫</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
  
          <!-- Thông tin thanh toán -->
          <div class="card-wrapper">
            <div class="card">
              <div class="card-header">
                <div class="header-flex">
                  <div class="icon-container purple">
                    <CreditCardIcon class="icon" />
                  </div>
                  <div>
                    <h2 class="card-title">Thông tin thanh toán</h2>
                    <p class="card-subtitle">Chi tiết về số tiền và thanh toán</p>
                  </div>
                </div>
              </div>
              <div class="card-content">
                <div class="grid-2">
                  <div class="form-group">
                    <label for="tong_tien" class="form-label required">Tổng tiền (VNĐ)</label>
                    <div class="input-with-icon">
                      <input
                        id="tong_tien"
                        v-model.number="hoaDon.tong_tien"
                        type="number"
                        min="0"
                        required
                        class="form-input with-icon"
                        placeholder="0"
                      />
                      <div class="input-icon">₫</div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="so_tien_giam" class="form-label">Số tiền giảm (VNĐ)</label>
                    <div class="input-with-icon">
                      <input
                        id="so_tien_giam"
                        v-model.number="hoaDon.so_tien_giam"
                        type="number"
                        min="0"
                        class="form-input with-icon"
                        placeholder="0"
                      />
                      <div class="input-icon">₫</div>
                    </div>
                  </div>
                </div>
  
                <div class="form-group">
                  <label for="thanh_tien" class="form-label">Thành tiền</label>
                  <div class="input-with-icon">
                    <input
                      id="thanh_tien"
                      :value="formatCurrency(calculatedThanhTien)"
                      disabled
                      class="form-input disabled total-amount with-icon"
                    />
                    <div class="input-icon total">₫</div>
                  </div>
                </div>
  
                <div class="grid-2">
                  <div class="form-group">
                    <label for="trang_thai_thanh_toan" class="form-label">Trạng thái thanh toán</label>
                    <select
                      id="trang_thai_thanh_toan"
                      v-model="hoaDon.trang_thai_thanh_toan"
                      class="form-select"
                    >
                      <option value="">Chọn trạng thái thanh toán</option>
                      <option value="Chưa thanh toán">Chưa thanh toán</option>
                      <option value="Đã thanh toán">Đã thanh toán</option>
                      <option value="Thanh toán một phần">Thanh toán một phần</option>
                      <option value="Hoàn tiền">Hoàn tiền</option>
                    </select>
                  </div>
  
                  <div class="form-group">
                    <label for="trang_thai_don_hang" class="form-label">Trạng thái đơn hàng</label>
                    <select
                      id="trang_thai_don_hang"
                      v-model="hoaDon.trang_thai_don_hang"
                      class="form-select"
                    >
                      <option value="">Chọn trạng thái đơn hàng</option>
                      <option value="Chờ xử lý">Chờ xử lý</option>
                      <option value="Đã xác nhận">Đã xác nhận</option>
                      <option value="Đang chuẩn bị">Đang chuẩn bị</option>
                      <option value="Đang giao hàng">Đang giao hàng</option>
                      <option value="Đã giao hàng">Đã giao hàng</option>
                      <option value="Đã hủy">Đã hủy</option>
                    </select>
                  </div>
                </div>
  
                <div class="form-group">
                  <label for="ngay_thanh_toan" class="form-label">Ngày thanh toán</label>
                  <input
                    id="ngay_thanh_toan"
                    v-model="hoaDon.ngay_thanh_toan"
                    type="date"
                    class="form-input"
                  />
                </div>
  
                <div class="checkbox-group">
                  <div class="checkbox-wrapper">
                    <input
                      id="is_delete"
                      v-model="hoaDon.is_delete"
                      type="checkbox"
                      class="checkbox danger"
                    />
                    <XIcon v-if="hoaDon.is_delete" class="check-icon danger" />
                  </div>
                  <label for="is_delete" class="checkbox-label danger">Đánh dấu xóa</label>
                </div>
              </div>
            </div>
          </div>
  
          <!-- Nút hành động -->
          <div class="action-buttons">
            <button type="button" @click="goBack" class="btn btn-secondary">
              <XIcon class="btn-icon" />
              Hủy
            </button>
            <button type="submit" :disabled="loading" class="btn btn-primary">
              <div v-if="loading" class="loading-spinner"></div>
              <SaveIcon v-else class="btn-icon" />
              {{ loading ? 'Đang lưu...' : 'Cập nhật' }}
            </button>
          </div>
        </form>
  
        <!-- Toast notification -->
        <Transition name="toast">
          <div v-if="showToast" :class="['toast', toastType === 'success' ? 'toast-success' : 'toast-error']">
            <div class="toast-content">
              <div :class="['toast-icon', toastType === 'success' ? 'success' : 'error']">
                <CheckCircleIcon v-if="toastType === 'success'" class="icon" />
                <XCircleIcon v-else class="icon" />
              </div>
              <span class="toast-message">{{ toastMessage }}</span>
            </div>
          </div>
        </Transition>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue'
  import { 
    ArrowLeftIcon, 
    SaveIcon, 
    CheckCircleIcon, 
    XCircleIcon,
    FileTextIcon,
    UsersIcon,
    TruckIcon,
    CreditCardIcon,
    CheckIcon,
    XIcon
  } from 'lucide-vue-next'
  
  // Reactive data
  const loading = ref(false)
  const showToast = ref(false)
  const toastMessage = ref('')
  const toastType = ref('success')
  
  const hoaDon = ref({
    id_hoa_don: 0,
    ma_hoa_don: '',
    ma_van_don: '',
    id_khach_hang: null,
    id_phieu_giam_gia: null,
    ten_nguoi_mua: '',
    sdt_nguoi_mua: '',
    ten_nguoi_nhan: '',
    sdt_nguoi_nhan: '',
    dia_chi_giao_hang: '',
    ngay_dat_hang: '',
    is_shipping: false,
    shipping_method: '',
    phi_ship: 0,
    tong_tien: 0,
    so_tien_giam: 0,
    thanh_tien: 0,
    ngay_tao_hoa_don: '',
    ngay_tao_don_hang: '',
    loai_hoa_don: '',
    ngay_thanh_toan: '',
    trang_thai_thanh_toan: '',
    is_delete: false,
    trang_thai_don_hang: ''
  })
  
  // Computed
  const calculatedThanhTien = computed(() => {
    const tongTien = hoaDon.value.tong_tien || 0
    const soTienGiam = hoaDon.value.so_tien_giam || 0
    const phiShip = hoaDon.value.is_shipping ? (hoaDon.value.phi_ship || 0) : 0
    return tongTien - soTienGiam + phiShip
  })
  
  // Methods
  const formatCurrency = (value) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(value)
  }
  
  const showToastMessage = (message, type = 'success') => {
    toastMessage.value = message
    toastType.value = type
    showToast.value = true
    setTimeout(() => {
      showToast.value = false
    }, 4000)
  }
  
  const goBack = () => {
    window.history.back()
  }
  
  const handleSubmit = async () => {
    loading.value = true
    
    try {
      hoaDon.value.thanh_tien = calculatedThanhTien.value
      
      console.log('Cập nhật hóa đơn:', hoaDon.value)
      await new Promise(resolve => setTimeout(resolve, 1500))
      
      showToastMessage(`Cập nhật hóa đơn ${hoaDon.value.ma_hoa_don} thành công!`, 'success')
      
      setTimeout(() => {
        goBack()
      }, 2000)
      
    } catch (error) {
      console.error('Lỗi cập nhật hóa đơn:', error)
      showToastMessage('Có lỗi xảy ra khi cập nhật hóa đơn!', 'error')
    } finally {
      loading.value = false
    }
  }
  
  // Load dữ liệu khi component mount
  onMounted(() => {
    const mockData = {
      id_hoa_don: 1,
      ma_hoa_don: 'HD001',
      ma_van_don: 'VD001',
      id_khach_hang: 1,
      id_phieu_giam_gia: null,
      ten_nguoi_mua: 'Nguyễn Văn A',
      sdt_nguoi_mua: '0123456789',
      ten_nguoi_nhan: 'Nguyễn Văn B',
      sdt_nguoi_nhan: '0987654321',
      dia_chi_giao_hang: '123 Đường ABC, Quận 1, TP.HCM',
      ngay_dat_hang: '2024-01-15',
      is_shipping: true,
      shipping_method: 'Giao hàng nhanh',
      phi_ship: 30000,
      tong_tien: 500000,
      so_tien_giam: 50000,
      thanh_tien: 480000,
      ngay_tao_hoa_don: '2024-01-15',
      ngay_tao_don_hang: '2024-01-15',
      loai_hoa_don: 'Bán lẻ',
      ngay_thanh_toan: '2024-01-15',
      trang_thai_thanh_toan: 'Đã thanh toán',
      is_delete: false,
      trang_thai_don_hang: 'Đang giao hàng'
    }
    
    hoaDon.value = mockData
  })
  </script>
  
  <style scoped>
  /* Reset và base styles */
  * {
    box-sizing: border-box;
  }
  
  /* Page container */
  .page-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #f8fafc 0%, #e0f2fe 50%, #e0e7ff 100%);
    padding: 0;
    margin: 0;
  }
  
  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem 1rem;
  }
  
  /* Header styles */
  .header-section {
    position: relative;
    margin-bottom: 2rem;
  }
  
  .header-blur {
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    border-radius: 1rem;
    filter: blur(40px);
    opacity: 0.2;
  }
  
  .header-content {
    position: relative;
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
    border-radius: 1rem;
    border: 1px solid rgba(255, 255, 255, 0.2);
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    padding: 1.5rem;
  }
  
  .header-flex {
    display: flex;
    align-items: center;
    gap: 1rem;
  }
  
  .back-button {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 3rem;
    height: 3rem;
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    border-radius: 0.75rem;
    color: white;
    border: none;
    cursor: pointer;
    box-shadow: 0 10px 25px -5px rgba(59, 130, 246, 0.4);
    transition: all 0.3s ease;
  }
  
  .back-button:hover {
    transform: scale(1.05);
    box-shadow: 0 20px 40px -10px rgba(59, 130, 246, 0.6);
  }
  
  .back-icon {
    width: 1.25rem;
    height: 1.25rem;
    transition: transform 0.3s ease;
  }
  
  .back-button:hover .back-icon {
    transform: translateX(-2px);
  }
  
  .page-title {
    font-size: 1.875rem;
    font-weight: 700;
    background: linear-gradient(135deg, #1f2937 0%, #4b5563 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    margin: 0;
  }
  
  .page-subtitle {
    color: #6b7280;
    margin: 0.25rem 0 0 0;
  }
  
  .invoice-badge {
    display: inline-flex;
    align-items: center;
    padding: 0.25rem 0.75rem;
    border-radius: 9999px;
    font-size: 0.875rem;
    font-weight: 500;
    background-color: #dbeafe;
    color: #1e40af;
  }
  
  /* Form container */
  .form-container {
    display: flex;
    flex-direction: column;
    gap: 2rem;
  }
  
  /* Card styles */
  .card-wrapper {
    transition: all 0.3s ease;
  }
  
  .card-wrapper:hover {
    transform: scale(1.01);
  }
  
  .card {
    background: linear-gradient(135deg, rgba(255,255,255,0.95) 0%, rgba(255,255,255,0.85) 100%);
    backdrop-filter: blur(10px);
    border-radius: 1rem;
    border: 1px solid rgba(255, 255, 255, 0.2);
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    overflow: hidden;
  }
  
  .card-header {
    padding: 1.5rem 2rem;
    border-bottom: 1px solid rgba(229, 231, 235, 0.5);
    background: linear-gradient(135deg, rgba(248,250,252,0.8) 0%, rgba(241,245,249,0.6) 100%);
  }
  
  .card-title {
    font-size: 1.25rem;
    font-weight: 700;
    color: #1f2937;
    margin: 0;
  }
  
  .card-subtitle {
    font-size: 0.875rem;
    color: #6b7280;
    margin: 0.25rem 0 0 0;
  }
  
  .card-content {
    padding: 2rem;
  }
  
  /* Icon containers */
  .icon-container {
    width: 2.5rem;
    height: 2.5rem;
    border-radius: 0.5rem;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .icon-container.blue {
    background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  }
  
  .icon-container.green {
    background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  }
  
  .icon-container.orange {
    background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
  }
  
  .icon-container.purple {
    background: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%);
  }
  
  .icon-container .icon {
    width: 1.25rem;
    height: 1.25rem;
    color: white;
  }
  
  /* Grid layout */
  .grid-2 {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 1.5rem;
  }
  
  @media (max-width: 768px) {
    .grid-2 {
      grid-template-columns: 1fr;
    }
  }
  
  /* Form elements */
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .form-label {
    display: block;
    font-size: 0.875rem;
    font-weight: 600;
    color: #374151;
    margin-bottom: 0.5rem;
  }
  
  .form-label.required::after {
    content: ' *';
    color: #ef4444;
  }
  
  .form-input,
  .form-select,
  .form-textarea {
    width: 100%;
    padding: 0.75rem 1rem;
    border: 2px solid #e5e7eb;
    border-radius: 0.75rem;
    color: #1f2937;
    background: linear-gradient(135deg, rgba(255,255,255,0.9) 0%, rgba(248,250,252,0.9) 100%);
    transition: all 0.3s ease;
    font-size: 1rem;
  }
  
  .form-input:focus,
  .form-select:focus,
  .form-textarea:focus {
    outline: none;
    border-color: #3b82f6;
    box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
  }
  
  .form-input:hover,
  .form-select:hover,
  .form-textarea:hover {
    border-color: #d1d5db;
  }
  
  .form-input.disabled {
    background-color: #f9fafb;
    color: #6b7280;
    cursor: not-allowed;
    border-color: #e5e7eb;
  }
  
  .form-textarea {
    resize: vertical;
    min-height: 80px;
  }
  
  /* Input with icon */
  .input-with-icon {
    position: relative;
  }
  
  .form-input.with-icon {
    padding-left: 3rem;
  }
  
  .input-icon {
    position: absolute;
    left: 0.75rem;
    top: 50%;
    transform: translateY(-50%);
    color: #6b7280;
    font-weight: 500;
  }
  
  .input-icon.total {
    color: #059669;
    font-weight: 700;
    font-size: 1.125rem;
  }
  
  .total-amount {
    font-size: 1.5rem;
    font-weight: 700;
    color: #059669;
  }
  
  /* Checkbox styles */
  .checkbox-group {
    display: flex;
    align-items: center;
    gap: 0.75rem;
    margin-bottom: 1.5rem;
  }
  
  .checkbox-wrapper {
    position: relative;
  }
  
  .checkbox {
    width: 1.25rem;
    height: 1.25rem;
    border: 2px solid #d1d5db;
    border-radius: 0.375rem;
    cursor: pointer;
    transition: all 0.3s ease;
    appearance: none;
    background: linear-gradient(135deg, rgba(255,255,255,0.9) 0%, rgba(248,250,252,0.9) 100%);
  }
  
  .checkbox:checked {
    background: #3b82f6;
    border-color: #3b82f6;
  }
  
  .checkbox.danger:checked {
    background: #ef4444;
    border-color: #ef4444;
  }
  
  .checkbox:focus {
    outline: none;
    box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
  }
  
  .check-icon {
    position: absolute;
    top: 0;
    left: 0;
    width: 1.25rem;
    height: 1.25rem;
    color: white;
    pointer-events: none;
  }
  
  .check-icon.danger {
    color: white;
  }
  
  .checkbox-label {
    font-size: 0.875rem;
    font-weight: 500;
    color: #374151;
    cursor: pointer;
    margin: 0;
  }
  
  .checkbox-label.danger {
    color: #dc2626;
  }
  
  /* Separator */
  .separator {
    border-top: 1px solid #e5e7eb;
    margin: 1.5rem 0;
  }
  
  /* Shipping details animation */
  .shipping-details {
    animation: slideDown 0.5s ease-out;
    margin-top: 1.5rem;
  }
  
  @keyframes slideDown {
    from {
      opacity: 0;
      transform: translateY(-20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
  
  /* Button styles */
  .action-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    padding-top: 1.5rem;
  }
  
  .btn {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 0.5rem;
    padding: 0.75rem 1.5rem;
    border-radius: 0.75rem;
    font-weight: 600;
    transition: all 0.3s ease;
    cursor: pointer;
    min-width: 120px;
    border: none;
    font-size: 1rem;
  }
  
  .btn:hover {
    transform: scale(1.05);
  }
  
  .btn:active {
    transform: scale(0.95);
  }
  
  .btn-primary {
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    color: white;
    box-shadow: 0 10px 25px -5px rgba(59, 130, 246, 0.4);
  }
  
  .btn-primary:hover {
    box-shadow: 0 20px 40px -10px rgba(59, 130, 246, 0.6);
  }
  
  .btn-primary:disabled {
    opacity: 0.5;
    cursor: not-allowed;
    transform: none;
  }
  
  .btn-secondary {
    background: white;
    border: 2px solid #d1d5db;
    color: #374151;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  }
  
  .btn-secondary:hover {
    background: #f9fafb;
    border-color: #9ca3af;
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  }
  
  .btn-icon {
    width: 1.25rem;
    height: 1.25rem;
  }
  
  /* Loading spinner */
  .loading-spinner {
    width: 1.25rem;
    height: 1.25rem;
    border: 2px solid white;
    border-top: 2px solid transparent;
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }
  
  @keyframes spin {
    to {
      transform: rotate(360deg);
    }
  }
  
  /* Toast styles */
  .toast {
    position: fixed;
    top: 1.5rem;
    right: 1.5rem;
    padding: 1.5rem;
    border-radius: 1rem;
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
    z-index: 50;
    max-width: 400px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
  }
  
  .toast-success {
    background: rgba(240, 253, 244, 0.95);
    color: #065f46;
    border-color: #bbf7d0;
  }
  
  .toast-error {
    background: rgba(254, 242, 242, 0.95);
    color: #991b1b;
    border-color: #fecaca;
  }
  
  .toast-content {
    display: flex;
    align-items: center;
    gap: 0.75rem;
  }
  
  .toast-icon {
    width: 2rem;
    height: 2rem;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .toast-icon.success {
    background: #d1fae5;
  }
  
  .toast-icon.error {
    background: #fee2e2;
  }
  
  .toast-icon .icon {
    width: 1.25rem;
    height: 1.25rem;
  }
  
  .toast-icon.success .icon {
    color: #059669;
  }
  
  .toast-icon.error .icon {
    color: #dc2626;
  }
  
  .toast-message {
    font-weight: 500;
  }
  
  /* Toast transitions */
  .toast-enter-active,
  .toast-leave-active {
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  }
  
  .toast-enter-from {
    opacity: 0;
    transform: translateX(100%) scale(0.8);
  }
  
  .toast-leave-to {
    opacity: 0;
    transform: translateX(100%) scale(0.8);
  }
  
  /* Responsive */
  @media (max-width: 768px) {
    .container {
      padding: 1rem;
    }
    
    .card-header {
      padding: 1rem 1.5rem;
    }
    
    .card-content {
      padding: 1.5rem;
    }
    
    .btn {
      padding: 0.5rem 1rem;
      font-size: 0.875rem;
    }
    
    .action-buttons {
      flex-direction: column;
    }
    
    .toast {
      top: 1rem;
      right: 1rem;
      left: 1rem;
      max-width: none;
    }
  }
  
  /* Smooth scrolling */
  html {
    scroll-behavior: smooth;
  }
  
  /* Custom scrollbar */
  ::-webkit-scrollbar {
    width: 8px;
  }
  
  ::-webkit-scrollbar-track {
    background: #f1f5f9;
    border-radius: 4px;
  }
  
  ::-webkit-scrollbar-thumb {
    background: linear-gradient(135deg, #3b82f6 0%, #8b5cf6 100%);
    border-radius: 4px;
  }
  
  ::-webkit-scrollbar-thumb:hover {
    background: linear-gradient(135deg, #2563eb 0%, #7c3aed 100%);
  }
  </style>