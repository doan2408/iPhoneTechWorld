```vue
<template>
  <div class="product-details-container">
    <!-- Header Section -->
    <div class="page-header">
      <div class="header-content">
        <el-button 
          type="primary" 
          :icon="ArrowLeft" 
          circle 
          class="back-btn"
          @click="handleBackClick"
        />
        <div class="header-text">
          <h1 class="page-title">Chi tiết sản phẩm</h1>
          <p class="page-subtitle">Xem thông tin chi tiết và thông số kỹ thuật</p>
        </div>
      </div>
      <el-tag 
        :type="getStatusType(sanPhamModel.trangThaiSanPham)" 
        size="large"
        class="status-tag"
      >
        {{ getTrangThaiLabel(sanPhamModel.trangThaiSanPham) }}
      </el-tag>
    </div>

    <el-row :gutter="24" class="main-content">
      <!-- Left Column - Main Information -->
      <el-col :xs="24" :lg="16">
        <div class="info-sections">
          <!-- General Information -->
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><InfoFilled /></el-icon>
                <span class="header-title">Thông tin chung</span>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Mã sản phẩm</label>
                  <el-input 
                    v-model="sanPhamModel.maSanPham" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Tên sản phẩm</label>
                  <el-input 
                    v-model="sanPhamModel.tenSanPham" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Thương hiệu</label>
                  <el-input 
                    v-model="sanPhamModel.thuongHieu" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Trạng thái</label>
                  <el-input 
                    :value="getTrangThaiLabel(sanPhamModel.trangThaiSanPham)" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
            </el-row>
          </el-card>

          <!-- Supplier Information -->
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><OfficeBuilding /></el-icon>
                <span class="header-title">Thông tin nhà cung cấp</span>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Nhà cung cấp</label>
                  <el-input 
                    v-model="sanPhamModel.tenNhaCungCap" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Địa chỉ</label>
                  <el-input 
                    v-model="sanPhamModel.diaChi" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Số điện thoại</label>
                  <el-input 
                    v-model="sanPhamModel.sdt" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Email</label>
                  <el-input 
                    v-model="sanPhamModel.email" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
            </el-row>
          </el-card>

          <!-- Model Information -->
          <el-card class="info-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Cpu /></el-icon>
                <span class="header-title">Thông tin model</span>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Tên model</label>
                  <el-input 
                    :value="sanPhamModel.modelSanPham?.tenModel || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Mã model</label>
                  <el-input 
                    :value="sanPhamModel.modelSanPham?.maModelSanPham || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Năm ra mắt</label>
                  <el-input 
                    :value="sanPhamModel.modelSanPham?.namRaMat || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
              <el-col :xs="24" :sm="12">
                <div class="form-field">
                  <label class="field-label">Trạng thái model</label>
                  <el-input 
                    :value="getTrangThaiModelLabel(sanPhamModel.modelSanPham?.trangThaiSanPhamModel)" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </el-col>
            </el-row>
          </el-card>

          <!-- Technical Specifications Grid -->
          <el-row :gutter="24">
            <!-- RAM Information -->
            <el-col :xs="24" :md="12">
              <el-card class="spec-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon"><MagicStick /></el-icon>
                    <span class="header-title">Thông tin RAM</span>
                  </div>
                </template>
                <div class="spec-content">
                  <div class="form-field">
                    <label class="field-label">Dung lượng RAM</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.dungLuongRam || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Loại RAM</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.loaiRam || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Tốc độ đọc/ghi</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.tocDoDocGhiRam || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Nhà sản xuất RAM</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.nhaSanXuatRam || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Năm ra mắt RAM</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.namRaMatRam || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-card>
            </el-col>

            <!-- Display Information -->
            <el-col :xs="24" :md="12">
              <el-card class="spec-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon"><Monitor /></el-icon>
                    <span class="header-title">Thông tin màn hình</span>
                  </div>
                </template>
                <div class="spec-content">
                  <div class="form-field">
                    <label class="field-label">Tên màn hình</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.tenManHinh || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Kích thước</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.kichThuoc || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Loại màn hình</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.loaiManHinh || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Độ phân giải</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.doPhanGiaiManHinh || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Tần số quét</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.tanSoQuet || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Độ sáng</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.doSang || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Chất liệu kính</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.chatLieuKinh || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-card>
            </el-col>

            <!-- Operating System Information -->
            <el-col :xs="24" :md="12">
              <el-card class="spec-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon"><Platform /></el-icon>
                    <span class="header-title">Thông tin hệ điều hành</span>
                  </div>
                </template>
                <div class="spec-content">
                  <div class="form-field">
                    <label class="field-label">Phiên bản HĐH</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.phienBanHeDieuHanh || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Nhà phát triển</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.nhaPhatTrien || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Giao diện người dùng</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.giaoDienNguoiDung || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-card>
            </el-col>

            <!-- CPU Information -->
            <el-col :xs="24" :md="12">
              <el-card class="spec-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon"><Cpu /></el-icon>
                    <span class="header-title">Thông tin CPU</span>
                  </div>
                </template>
                <div class="spec-content">
                  <div class="form-field">
                    <label class="field-label">Hãng sản xuất</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.hangSanXuat || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Chip xử lý</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.chipXuLy || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Số nhân</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.soNhan || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Xung nhịp</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.xungNhip || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Công nghệ sản xuất</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.congNgheSanXuat || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Bộ nhớ đệm</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.boNhoDem || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Tiêu thụ điện năng</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.tieuThuDienNang || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Năm ra mắt CPU</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.namRaMatCpu || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-card>
            </el-col>

            <!-- Battery Information -->
            <el-col :xs="24" :md="12">
              <el-card class="spec-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon"><Lightning /></el-icon>
                    <span class="header-title">Thông tin pin</span>
                  </div>
                </template>
                <div class="spec-content">
                  <div class="form-field">
                    <label class="field-label">Phiên bản pin</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.phienBanPin || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Công suất sạc</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.congSuatSac || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Thời gian sử dụng</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.thoiGianSuDung || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Số lần sạc tối đa</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.soLanSacToiDa || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-card>
            </el-col>

            <!-- Origin Information -->
            <el-col :xs="24" :md="12">
              <el-card class="spec-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <el-icon class="header-icon"><Location /></el-icon>
                    <span class="header-title">Thông tin xuất xứ</span>
                  </div>
                </template>
                <div class="spec-content">
                  <div class="form-field">
                    <label class="field-label">Mã xuất xứ</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.maXuatXu || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Quốc gia</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.tenQuocGia || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Loại sản phẩm</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.tenLoai || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- Camera Information -->
          <el-card class="info-card" shadow="hover">
            <template #header>
Cox              <div class="card-header">
                <el-icon class="header-icon"><Camera /></el-icon>
                <span class="header-title">Thông tin camera</span>
              </div>
            </template>
            <el-row :gutter="20">
              <el-col :xs="24" :md="12">
                <div class="camera-section">
                  <h4 class="camera-title">Camera trước</h4>
                  <div class="form-field">
                    <label class="field-label">Loại camera</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.loaiCameraTruoc || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Độ phân giải</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.doPhanGiaiCameraTruoc || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Khẩu độ</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.khauDoCameraTruoc || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Loại zoom</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.loaiZoomCameraTruoc || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Chế độ chụp</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.cheDoChupCameraTruoc || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-col>
              <el-col :xs="24" :md="12">
                <div class="camera-section">
                  <h4 class="camera-title">Camera sau</h4>
                  <div class="form-field">
                    <label class="field-label">Loại camera</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.loaiCameraSau || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Độ phân giải</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.doPhanGiaiCameraSau || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Khẩu độ</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.khauDoCameraSau || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Loại zoom</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.loaiZoomCameraSau || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                  <div class="form-field">
                    <label class="field-label">Chế độ chụp</label>
                    <el-input 
                      :value="sanPhamModel.modelSanPham?.cheDoChupCameraSau || ''" 
                      readonly 
                      class="readonly-input"
                    />
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-card>
        </div>
      </el-col>

      <!-- Right Column - Variants and Details -->
      <el-col :xs="24" :lg="8">
        <div class="sidebar-content">
          <!-- Product Variants -->
          <el-card class="variants-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><Grid /></el-icon>
                <span class="header-title">Biến thể sản phẩm</span>
              </div>
            </template>
            <el-table 
              :data="sanPhamModel.sanPhamChiTiets" 
              class="variants-table"
              @row-click="selectChiTiet"
              :row-class-name="getRowClassName"
            >
              <el-table-column type="index" label="STT" width="60" :index="indexMethod" />
              <el-table-column label="Mã SP chi tiết" prop="maSanPhamChiTiet" width="150" />
              <el-table-column label="Màu sắc" prop="tenMau">
                <template #default="{ row }">
                  <div class="color-cell">
                    <div 
                      class="color-dot" 
                      :style="{ backgroundColor: row.maMau }"
                    ></div>
                    <span class="color-name">{{ row.tenMau }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="dungLuongRom" label="ROM" />
              <el-table-column prop="soLuongSPCT" label="Số lượng" width="120" />
              <el-table-column label="Giá bán" width="120">
                <template #default="{ row }">
                  <span class="price-text">{{ formatPrice(row.giaBan) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </el-card>

          <!-- Selected Variant Details -->
          <el-card v-if="selectedChiTiet !== null" class="variant-details-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <el-icon class="header-icon"><View /></el-icon>
                <span class="header-title">Chi tiết biến thể {{ selectedChiTiet + 1 }}</span>
              </div>
            </template>
            <div class="variant-details">
              <div class="detail-section">
                <h5 class="section-title">Thông tin cơ bản</h5>
                <div class="form-field">
                  <label class="field-label">Mã sản phẩm chi tiết</label>
                  <el-input 
                    :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.maSanPhamChiTiet || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
                <div class="form-field">
                  <label class="field-label">Màu sắc</label>
                  <el-input 
                    :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.tenMau || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
                <div class="form-field">
                  <label class="field-label">Mã màu</label>
                  <el-input 
                    :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.maMau || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
                <div class="form-field">
                  <label class="field-label">Dung lượng ROM</label>
                  <el-input 
                    :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.dungLuongRom || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
                <div class="form-field">
                  <label class="field-label">Giá bán</label>
                  <el-input 
                    :value="formatPrice(sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.giaBan)" 
                    readonly 
                    class="readonly-input price-input"
                  />
                </div>
                <div class="form-field">
                  <label class="field-label">Số lượng</label>
                  <el-input 
                    :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.soLuongSPCT || ''" 
                    readonly 
                    class="readonly-input"
                  />
                </div>
              </div>

              <el-divider />

              <div class="detail-section">
                <h5 class="section-title">IMEI</h5>
                <el-input 
                  type="textarea" 
                  :value="sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.imeisInput || ''" 
                  readonly 
                  :rows="4"
                  class="imei-textarea"
                />
                <div class="imei-count">
                  Số lượng IMEI: {{ sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.soLuongSPCT || 0 }}
                </div>
              </div>

              <el-divider />

              <div class="detail-section">
                <h5 class="section-title">Hình ảnh sản phẩm</h5>
                <div class="image-gallery">
                  <el-image
                    v-for="(img, index) in sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.hinhAnhs || []"
                    :key="index"
                    :src="img.url || defaultImage"
                    :preview-src-list="previewImageList"
                    class="gallery-image"
                    fit="cover"
                    :preview-teleported="true"
                    :lazy="true"
                    @error="handleImageError"
                  />
                  <el-empty
                    v-if="!sanPhamModel.sanPhamChiTiets[selectedChiTiet]?.hinhAnhs?.length"
                    description="Không có hình ảnh"
                    :image-size="80"
                    class="empty-image"
                  />
                </div>
              </div>
            </div>
          </el-card>

          <el-alert 
            v-else 
            title="Vui lòng chọn một biến thể để xem chi tiết" 
            type="info" 
            show-icon 
            :closable="false"
            class="select-variant-alert"
          />
        </div>
      </el-col>
    </el-row>

    <!-- Footer Actions -->
    <div class="footer-actions">
      <el-button 
        type="default" 
        size="large"
        @click="handleBackClick"
        class="back-button"
      >
        <el-icon><ArrowLeft /></el-icon>
        Quay lại
      </el-button>
    </div>

    <el-alert 
      v-if="error" 
      :title="error" 
      type="error" 
      show-icon 
      class="error-alert" 
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  ArrowLeft, 
  InfoFilled, 
  OfficeBuilding, 
  Cpu, 
  MagicStick, 
  Monitor, 
  Lightning, 
  Camera, 
  Grid, 
  View,
  Platform,
  Location
} from '@element-plus/icons-vue'
import {
  getAllNhaCungCapList,
  getViewSanPham,
} from '@/Service/Adminservice/Products/ProductAdminService'

const route = useRoute()
const router = useRouter()
const id = route.params.id

const sanPhamModel = reactive({
  id: null,
  maSanPham: '',
  tenSanPham: '',
  thuongHieu: '',
  trangThaiSanPham: '',
  idNhaCungCap: null,
  tenNhaCungCap: '',
  diaChi: '',
  sdt: '',
  email: '',
  modelSanPham: {},
  sanPhamChiTiets: [],
})

const error = ref('')
const selectedChiTiet = ref(null)
const nhaCungCaps = ref([])

// Default fallback image
const defaultImage = 'https://via.placeholder.com/100?text=No+Image'

// Computed property for image preview list
const previewImageList = computed(() => {
  if (
    selectedChiTiet.value !== null &&
    sanPhamModel.sanPhamChiTiets[selectedChiTiet.value]?.hinhAnhs
  ) {
    return sanPhamModel.sanPhamChiTiets[selectedChiTiet.value].hinhAnhs
      .filter(img => img.url && typeof img.url === 'string')
      .map(img => img.url)
  }
  return []
})

// Handle image loading errors
const handleImageError = (event) => {
  event.target.src = defaultImage
  ElMessage.warning('Không thể tải hình ảnh, hiển thị ảnh mặc định')
}

const danhSachTrangThaiSanPham = [
  { label: 'Đang kinh doanh', value: 'ACTIVE' },
  { label: 'Ngừng kinh doanh', value: 'DISCONTINUED' },
  { label: 'Sắp ra mắt', value: 'COMING_SOON' },
  { label: 'Tạm ngừng bán', value: 'TEMPORARILY_UNAVAILABLE' },
  { label: 'Hết hàng', value: 'OUT_OF_STOCK' },
]

const danhSachTrangThaiModel = ref([
  { value: 'ACTIVE', label: 'Đang hoạt động' },
  { value: 'DISCONTINUED', label: 'Ngừng sản xuất' },
  { value: 'UPCOMING', label: 'Chờ ra mắt' },
  { value: 'HIDDEN', label: 'Ẩn' },
  { value: 'DELETED', label: 'Đã xóa' }
])

const fetchSanPham = async (id) => {
  try {
    const response = await getViewSanPham(id)
    // Ánh xạ thông tin cơ bản
    sanPhamModel.id = response.id || null
    sanPhamModel.maSanPham = response.maSanPham || ''
    sanPhamModel.tenSanPham = response.tenSanPham || ''
    sanPhamModel.thuongHieu = response.thuongHieu || ''
    sanPhamModel.trangThaiSanPham = response.trangThaiSanPham || ''
    
    // Ánh xạ thông tin nhà cung cấp
    if (response.nhaCungCapAdminResponse) {
      sanPhamModel.idNhaCungCap = response.nhaCungCapAdminResponse.id || null
      sanPhamModel.tenNhaCungCap = response.nhaCungCapAdminResponse.tenNhaCungCap || ''
      sanPhamModel.diaChi = response.nhaCungCapAdminResponse.diaChi || ''
      sanPhamModel.sdt = response.nhaCungCapAdminResponse.sdt || ''
      sanPhamModel.email = response.nhaCungCapAdminResponse.email || ''
    } else {
      sanPhamModel.idNhaCungCap = null
      sanPhamModel.tenNhaCungCap = ''
      sanPhamModel.diaChi = ''
      sanPhamModel.sdt = ''
      sanPhamModel.email = ''
    }

    // Ánh xạ thông tin model sản phẩm
    if (response.modelSanPhamAdminResponse) {
      sanPhamModel.modelSanPham = {
        idModelSanPham: response.modelSanPhamAdminResponse.idModelSanPham || null,
        maModelSanPham: response.modelSanPhamAdminResponse.maModelSanPham || '',
        tenModel: response.modelSanPhamAdminResponse.tenModel || '',
        namRaMat: response.modelSanPhamAdminResponse.namRaMat || '',
        moTa: response.modelSanPhamAdminResponse.moTa || '',
        trangThaiSanPhamModel: response.modelSanPhamAdminResponse.trangThaiSanPhamModel || '',
        dungLuongRam: response.modelSanPhamAdminResponse.dungLuongRam || '',
        loaiRam: response.modelSanPhamAdminResponse.loaiRam || '',
        tocDoDocGhiRam: response.modelSanPhamAdminResponse.tocDoDocGhiRam || '',
        nhaSanXuatRam: response.modelSanPhamAdminResponse.nhaSanXuatRam || '',
        namRaMatRam: response.modelSanPhamAdminResponse.namRaMatRam || '',
        tenManHinh: response.modelSanPhamAdminResponse.tenManHinh || '',
        kichThuoc: response.modelSanPhamAdminResponse.kichThuoc || '',
        loaiManHinh: response.modelSanPhamAdminResponse.loaiManHinh || '',
        doPhanGiaiManHinh: response.modelSanPhamAdminResponse.doPhanGiaiManHinh || '',
        tanSoQuet: response.modelSanPhamAdminResponse.tanSoQuet || '',
        doSang: response.modelSanPhamAdminResponse.doSang || '',
        chatLieuKinh: response.modelSanPhamAdminResponse.chatLieuKinh || '',
        phienBanHeDieuHanh: response.modelSanPhamAdminResponse.phienBanHeDieuHanh || '',
        nhaPhatTrien: response.modelSanPhamAdminResponse.nhaPhatTrien || '',
        giaoDienNguoiDung: response.modelSanPhamAdminResponse.giaoDienNguoiDung || '',
        phienBanPin: response.modelSanPhamAdminResponse.phienBanPin || '',
        congSuatSac: response.modelSanPhamAdminResponse.congSuatSac || '',
        thoiGianSuDung: response.modelSanPhamAdminResponse.thoiGianSuDung || '',
        soLanSacToiDa: response.modelSanPhamAdminResponse.soLanSacToiDa || '',
        hangSanXuat: response.modelSanPhamAdminResponse.hangSanXuat || '',
        soNhan: response.modelSanPhamAdminResponse.soNhan || '',
        chipXuLy: response.modelSanPhamAdminResponse.chipXuLy || '',
        xungNhip: response.modelSanPhamAdminResponse.xungNhip || '',
        congNgheSanXuat: response.modelSanPhamAdminResponse.congNgheSanXuat || '',
        boNhoDem: response.modelSanPhamAdminResponse.boNhoDem || '',
        tieuThuDienNang: response.modelSanPhamAdminResponse.tieuThuDienNang || '',
        namRaMatCpu: response.modelSanPhamAdminResponse.namRaMatCpu || '',
        loaiCameraTruoc: response.modelSanPhamAdminResponse.loaiCameraTruoc || '',
        doPhanGiaiCameraTruoc: response.modelSanPhamAdminResponse.doPhanGiaiCameraTruoc || '',
        khauDoCameraTruoc: response.modelSanPhamAdminResponse.khauDoCameraTruoc || '',
        loaiZoomCameraTruoc: response.modelSanPhamAdminResponse.loaiZoomCameraTruoc || '',
        cheDoChupCameraTruoc: response.modelSanPhamAdminResponse.cheDoChupCameraTruoc || '',
        loaiCameraSau: response.modelSanPhamAdminResponse.loaiCameraSau || '',
        doPhanGiaiCameraSau: response.modelSanPhamAdminResponse.doPhanGiaiCameraSau || '',
        khauDoCameraSau: response.modelSanPhamAdminResponse.khauDoCameraSau || '',
        loaiZoomCameraSau: response.modelSanPhamAdminResponse.loaiZoomCameraSau || '',
        cheDoChupCameraSau: response.modelSanPhamAdminResponse.cheDoChupCameraSau || '',
        maXuatXu: response.modelSanPhamAdminResponse.maXuatXu || '',
        tenQuocGia: response.modelSanPhamAdminResponse.tenQuocGia || '',
        tenLoai: response.modelSanPhamAdminResponse.tenLoai || '',
      }
    } else {
      sanPhamModel.modelSanPham = {}
    }

    // Ánh xạ sanPhamChiTiets
    sanPhamModel.sanPhamChiTiets = response.sanPhamChiTiets?.map((chiTiet) => ({
      id: chiTiet.id || null,
      maSanPhamChiTiet: chiTiet.maSanPhamChiTiet || '',
      soLuongSPCT: chiTiet.soLuongSPCT || 0,
      giaBan: chiTiet.giaBan || 0,
      tenMau: chiTiet.tenMau || '',
      maMau: chiTiet.maMau || '',
      dungLuongRom: chiTiet.dungLuongRom || '',
      imeisInput: chiTiet.imeis?.map((i) => i.soImei).join(', ') || '',
      hinhAnhs: chiTiet.hinhAnhs?.map((h) => ({
        name: h.url?.split('/').pop() || '',
        url: h.url || '',
        imagePublicId: h.imagePublicId || '',
      })) || [],
    })) || []

    selectedChiTiet.value = sanPhamModel.sanPhamChiTiets.length > 0 ? 0 : null
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải chi tiết sản phẩm'
    ElMessage.error(error.value)
    console.error('❌ ~ Lỗi khi fetchSanPham:', err)
  }
}

const fetchDanhMuc = async () => {
  try {
    const response = await getAllNhaCungCapList()
    nhaCungCaps.value = response || []
  } catch (err) {
    error.value = err.message || 'Lỗi khi tải danh mục nhà cung cấp'
    ElMessage.error(error.value)
    console.error('❌ ~ Lỗi khi fetchDanhMuc:', err)
  }
}

const getTrangThaiLabel = (value) => {
  const result = danhSachTrangThaiSanPham.find((tt) => tt.value === value)
  return result?.label || 'Không rõ'
}

const getTrangThaiModelLabel = (value) => {
  const result = danhSachTrangThaiModel.value.find((tt) => tt.value === value)
  return result?.label || 'Không rõ'
}

const getStatusType = (value) => {
  const statusMap = {
    'ACTIVE': 'success',
    'DISCONTINUED': 'danger',
    'COMING_SOON': 'warning',
    'TEMPORARILY_UNAVAILABLE': 'info',
    'OUT_OF_STOCK': 'danger'
  }
  return statusMap[value] || 'info'
}

const formatPrice = (value) => {
  if (!value && value !== 0) return ''
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
}

const selectChiTiet = (row, column, event) => {
  const index = sanPhamModel.sanPhamChiTiets.indexOf(row)
  selectedChiTiet.value = index
}

const getRowClassName = ({ rowIndex }) => {
  return selectedChiTiet.value === rowIndex ? 'selected-row' : ''
}

const indexMethod = (index) => index + 1

const handleBackClick = () => {
  router.push('/admin/products')
}

onMounted(async () => {
  await Promise.all([fetchDanhMuc(), fetchSanPham(id)])
})
</script>

<style scoped>
.product-details-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.back-btn {
  width: 48px;
  height: 48px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
}

.header-text {
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
  line-height: 1.2;
}

.page-subtitle {
  font-size: 16px;
  color: #718096;
  margin: 4px 0 0 0;
}

.status-tag {
  font-size: 14px;
  font-weight: 600;
  padding: 8px 16px;
  border-radius: 20px;
}

.main-content {
  margin-bottom: 32px;
}

.info-sections {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info-card, .spec-card, .variants-card, .variant-details-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.info-card:hover, .spec-card:hover, .variants-card:hover, .variant-details-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-icon {
  font-size: 20px;
  color: #409eff;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #1a202c;
}

.form-field {
  margin-bottom: 20px;
}

.field-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #4a5568;
  margin-bottom: 8px;
}

.readonly-input {
  --el-input-bg-color: #f7fafc;
  --el-input-border-color: #e2e8f0;
  --el-input-text-color: #2d3748;
}

.readonly-input :deep(.el-input__wrapper) {
  background-color: #f7fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.readonly-input :deep(.el-input__wrapper:hover) {
  border-color: #cbd5e0;
}

.spec-card {
  height: 100%;
}

.spec-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.camera-section {
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  margin-bottom: 16px;
}

.camera-title {
  font-size: 16px;
  font-weight: 600;
  color: #2d3748;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #e2e8f0;
}

.sidebar-content {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.variants-table {
  --el-table-border-color: #e2e8f0;
  --el-table-header-bg-color: #f7fafc;
}

.variants-table :deep(.el-table__header th) {
  background-color: #f7fafc;
  color: #4a5568;
  font-weight: 600;
  border-bottom: 2px solid #e2e8f0;
}

.variants-table :deep(.el-table__row) {
  cursor: pointer;
  transition: all 0.3s ease;
}

.variants-table :deep(.el-table__row:hover) {
  background-color: #edf2f7;
}

.variants-table :deep(.selected-row) {
  background-color: #ebf8ff !important;
}

.variants-table :deep(.selected-row:hover) {
  background-color: #bee3f8 !important;
}

.color-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.color-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 2px solid #e2e8f0;
  flex-shrink: 0;
}

.color-name {
  font-size: 13px;
  color: #4a5568;
}

.price-text {
  font-weight: 600;
  color: #38a169;
  font-size: 13px;
}

.variant-details {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.detail-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #2d3748;
  margin: 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #e2e8f0;
}

.price-input :deep(.el-input__wrapper) {
  background-color: #f0fff4;
  border-color: #9ae6b4;
}

.imei-textarea :deep(.el-textarea__inner) {
  background-color: #f7fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  line-height: 1.5;
}

.imei-count {
  font-size: 12px;
  color: #718096;
  margin-top: 8px;
  text-align: right;
}

.image-gallery {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 16px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  overflow-x: auto;
}

.gallery-image {
  width: 120px;
  height: 120px;
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  transition: all 0.3s ease;
  cursor: pointer;
  object-fit: cover;
}

.gallery-image:hover {
  border-color: #409eff;
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.empty-image {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.select-variant-alert {
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #e6f3ff 0%, #f0f8ff 100%);
}

.footer-actions {
  display: flex;
  justify-content: center;
  padding: 32px 0;
}

.back-button {
  padding: 12px 32px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.error-alert {
  margin-top: 24px;
  border-radius: 12px;
}

/* Responsive Design */
@media (max-width: 768px) {
  .product-details-container {
    padding: 16px;
  }
  
  .page-header {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .page-subtitle {
    font-size: 14px;
  }
  
  .info-sections {
    gap: 16px;
  }
  
  .spec-content {
    gap: 12px;
  }
  
  .camera-section {
    padding: 12px;
    margin-bottom: 12px;
  }
  
  .image-gallery {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
    gap: 12px;
    padding: 12px;
  }
  
  .gallery-image {
    width: 100px;
    height: 100px;
  }
}

@media (max-width: 480px) {
  .page-title {
    font-size: 20px;
  }
  
  .page-subtitle {
    font-size: 14px;
  }
  
  .header-title {
    font-size: 16px;
  }
  
  .field-label {
    font-size: 13px;
  }
  
  .image-gallery {
    grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
    gap: 8px;
    padding: 8px;
  }
  
  .gallery-image {
    width: 80px;
    height: 80px;
  }
}
</style>
```