<template>
    <div class="delivery-container">
        <div class="main-wrapper">
            <!-- Header -->
            <div class="header-section">
                <h1 class="main-title">Quản lý giao hàng</h1>
                <p class="main-subtitle">Danh sách và chi tiết các đơn giao hàng</p>
            </div>

            <!-- Search Section -->
            <div class="search-card">
                <div class="search-content">
                    <div class="search-input-group">
                        <label class="search-label">
                            Tìm kiếm theo mã hóa đơn
                        </label>
                        <div class="input-wrapper">
                            <input v-model="searchQuery" type="text" placeholder="Nhập mã hóa đơn..."
                                class="search-input" @input="handleSearch" />
                            <svg class="search-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                            </svg>
                        </div>
                    </div>
                    <div class="search-actions">
                        <button @click="clearSearch" class="btn btn-outline">
                            <svg class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12"></path>
                            </svg>
                            Xóa bộ lọc
                        </button>
                        <select v-model="statusFilter" class="status-select">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Chờ xử lý">Chờ xử lý</option>
                            <option value="Đang giao">Đang giao</option>
                            <option value="Đã giao">Đã giao</option>
                            <option value="Đã hủy">Đã hủy</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Statistics Cards -->
            <div class="stats-grid">
                <div class="stat-card stat-total">
                    <div class="stat-content">
                        <div class="stat-icon stat-icon-blue">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2M4 13h2m13-8l-4 4m0 0l-4-4m4 4V3">
                                </path>
                            </svg>
                        </div>
                        <div class="stat-info">
                            <p class="stat-label">Tổng đơn hàng</p>
                            <p class="stat-value">{{ deliveries.length }}</p>
                        </div>
                    </div>
                </div>
                <div class="stat-card stat-pending">
                    <div class="stat-content">
                        <div class="stat-icon stat-icon-yellow">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                        </div>
                        <div class="stat-info">
                            <p class="stat-label">Chờ xử lý</p>
                            <p class="stat-value">{{ getStatusCount('Chờ xử lý') }}</p>
                        </div>
                    </div>
                </div>
                <div class="stat-card stat-shipping">
                    <div class="stat-content">
                        <div class="stat-icon stat-icon-blue">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M13 10V3L4 14h7v7l9-11h-7z"></path>
                            </svg>
                        </div>
                        <div class="stat-info">
                            <p class="stat-label">Đang giao</p>
                            <p class="stat-value">{{ getStatusCount('Đang giao') }}</p>
                        </div>
                    </div>
                </div>
                <div class="stat-card stat-completed">
                    <div class="stat-content">
                        <div class="stat-icon stat-icon-green">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M5 13l4 4L19 7"></path>
                            </svg>
                        </div>
                        <div class="stat-info">
                            <p class="stat-label">Đã giao</p>
                            <p class="stat-value">{{ getStatusCount('Đã giao') }}</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Delivery List -->
            <div v-if="!selectedDelivery" class="delivery-list-card">
                <div class="card-header">
                    <h2 class="card-title">
                        <svg class="title-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2">
                            </path>
                        </svg>
                        Danh sách đơn giao hàng ({{ filteredDeliveries.length }})
                    </h2>
                    <div class="view-controls">
                        <!-- Nút chuyển đổi chế độ xem -->
                        <div class="view-toggle-buttons">
                            <button @click="viewMode = 'table'" class="view-toggle-btn"
                                :class="{ active: viewMode === 'table' }" title="Xem dạng bảng">
                                <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" class="view-icon">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M3 10h18M3 14h18M3 18h18M3 6h18"></path>
                                </svg>
                            </button>
                            <button @click="viewMode = 'grid'" class="view-toggle-btn"
                                :class="{ active: viewMode === 'grid' }" title="Xem dạng lưới">
                                <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" class="view-icon">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M4 5a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1H5a1 1 0 01-1-1V5zm10 0a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1h-4a1 1 0 01-1-1V5zM4 15a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1H5a1 1 0 01-1-1v-4zm10 0a1 1 0 011-1h4a1 1 0 011 1v4a1 1 0 01-1 1h-4a1 1 0 01-1-1v-4z">
                                    </path>
                                </svg>
                            </button>
                        </div>

                        <div class="selection-controls">
                            <button @click="selectAll" class="btn btn-outline btn-sm"
                                v-if="selectedItems.length < filteredDeliveries.length">
                                Chọn tất cả
                            </button>
                            <button @click="deselectAll" class="btn btn-outline btn-sm" v-else>
                                Bỏ chọn tất cả
                            </button>
                            <span v-if="selectedItems.length > 0" class="selected-count">
                                Đã chọn {{ selectedItems.length }} đơn hàng
                            </span>
                        </div>
                    </div>
                </div>

                <!-- Table View -->
                <div v-if="viewMode === 'table'" class="table-container">
                    <table class="delivery-table">
                        <thead class="table-header">
                            <tr>
                                <th class="table-th checkbox-column">
                                    <input type="checkbox"
                                        :checked="selectedItems.length === filteredDeliveries.length && filteredDeliveries.length > 0"
                                        @change="toggleSelectAll" class="header-checkbox" />
                                </th>
                                <th class="table-th">Mã giao hàng</th>
                                <th class="table-th">Khách hàng</th>
                                <th class="table-th">Mã hóa đơn</th>
                                <th class="table-th">Ngày đặt hàng</th>
                                <th class="table-th">Tổng giá trị</th>
                                <th class="table-th">Trạng thái</th>
                                <th class="table-th">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody class="table-body">
                            <tr v-for="delivery in filteredDeliveries" :key="delivery.idGiaoHang" class="table-row"
                                :class="{ 'selected-row': isSelected(delivery.idGiaoHang) }">
                                <td class="table-td checkbox-column" @click.stop>
                                    <input type="checkbox" :checked="isSelected(delivery.idGiaoHang)"
                                        @change="toggleSelect(delivery.idGiaoHang)" class="row-checkbox" />
                                </td>
                                <td class="table-td" @click="viewDeliveryDetail(delivery)">
                                    <div class="delivery-code">
                                        <div class="status-dot" :class="getStatusClass(delivery.trangThaiDonHang)">
                                        </div>
                                        {{ delivery.maGiaoHang }}
                                    </div>
                                </td>
                                <td class="table-td" @click="viewDeliveryDetail(delivery)">
                                    <div class="customer-info">
                                        <div class="customer-avatar">
                                            {{ getInitials(delivery.tenKhachHang) }}
                                        </div>
                                        <div class="customer-details">
                                            <div class="customer-name">{{ delivery.tenKhachHang }}</div>
                                            <div class="customer-code">{{ delivery.maKhachHang }}</div>
                                        </div>
                                    </div>
                                </td>
                                <td class="table-td" @click="viewDeliveryDetail(delivery)">
                                    <div class="invoice-code">
                                        {{ delivery.maHoaDon }}
                                    </div>
                                </td>
                                <td class="table-td order-date" @click="viewDeliveryDetail(delivery)">
                                    {{ formatDate(delivery.ngayDatHang) }}
                                </td>
                                <td class="table-td order-total" @click="viewDeliveryDetail(delivery)">
                                    {{ formatCurrency(delivery.tongGiaTriDonHang) }}
                                </td>
                                <td class="table-td" @click="viewDeliveryDetail(delivery)">
                                    <span :class="getStatusClass(delivery.trangThaiDonHang)" class="status-badge">
                                        {{ delivery.trangThaiDonHang }}
                                    </span>
                                </td>
                                <td class="table-td">
                                    <button @click.stop="viewDeliveryDetail(delivery)" class="btn btn-primary btn-sm">
                                        Xem chi tiết
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- Grid View -->
                <div v-if="viewMode === 'grid'" class="delivery-grid">
                    <div v-for="delivery in filteredDeliveries" :key="delivery.idGiaoHang" class="delivery-card"
                        :class="{ 'selected': isSelected(delivery.idGiaoHang) }">
                        <div class="card-checkbox">
                            <input type="checkbox" :checked="isSelected(delivery.idGiaoHang)"
                                @click.stop="toggleSelect(delivery.idGiaoHang)" class="delivery-checkbox" />
                        </div>
                        <div class="card-content" @click="viewDeliveryDetail(delivery)">
                            <div class="card-header-row">
                                <div class="delivery-code">
                                    <div class="status-dot" :class="getStatusClass(delivery.trangThaiDonHang)"></div>
                                    {{ delivery.maGiaoHang }}
                                </div>
                                <span :class="getStatusClass(delivery.trangThaiDonHang)" class="status-badge">
                                    {{ delivery.trangThaiDonHang }}
                                </span>
                            </div>

                            <div class="card-body">
                                <div class="customer-info">
                                    <div class="customer-avatar">
                                        {{ getInitials(delivery.tenKhachHang) }}
                                    </div>
                                    <div class="customer-details">
                                        <div class="customer-name">{{ delivery.tenKhachHang }}</div>
                                        <div class="customer-code">{{ delivery.maKhachHang }}</div>
                                    </div>
                                </div>

                                <div class="card-details">
                                    <div class="detail-item">
                                        <span class="detail-label">Mã hóa đơn:</span>
                                        <span class="detail-value invoice-code">{{ delivery.maHoaDon }}</span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Ngày đặt:</span>
                                        <span class="detail-value">{{ formatDate(delivery.ngayDatHang) }}</span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Tổng giá trị:</span>
                                        <span class="detail-value order-total">{{
                                            formatCurrency(delivery.tongGiaTriDonHang) }}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="card-footer">
                                <button @click.stop="viewDeliveryDetail(delivery)" class="btn btn-primary btn-sm">
                                    Xem chi tiết
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Empty State -->
                <div v-if="filteredDeliveries.length === 0" class="empty-state">
                    <div class="empty-icon">
                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2M4 13h2m13-8l-4 4m0 0l-4-4m4 4V3">
                            </path>
                        </svg>
                    </div>
                    <h3 class="empty-title">Không tìm thấy đơn giao hàng</h3>
                    <p class="empty-subtitle">Thử thay đổi từ khóa tìm kiếm hoặc bộ lọc của bạn.</p>
                    <button @click="clearSearch" class="btn btn-primary">
                        Xóa bộ lọc
                    </button>
                </div>
            </div>

            <!-- pagingation : phân trang -->
            <div class="pagination-section" v-if="!selectedDelivery && filteredDeliveries.length > 0">
                <div class="pagination-info">
                    Hiển thị {{ (pageNo * pageSize) + 1 }} - {{ Math.min((pageNo * pageSize) + pageSize, totalElement)
                    }}
                    trong tổng số {{ totalElement }} hóa đơn
                </div>
                <div class="pagination-controls">
                    <button class="pagination-btn" :disabled="pageNo === 0" @click="changePage(pageNo - 1)">
                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7">
                            </path>
                        </svg>
                        Trước
                    </button>

                    <div class="page-numbers">
                        <button v-for="pageIndex in visiblePages" :key="pageIndex" class="page-btn"
                            :class="{ active: pageIndex === pageNo }" @click="pageIndex !== -1 && changePage(pageIndex)"
                            :disabled="pageIndex === -1">
                            {{ pageIndex === -1 ? '...' : pageIndex + 1 }}
                        </button>
                    </div>

                    <button class="pagination-btn" :disabled="pageNo === totalPage - 1" @click="changePage(pageNo + 1)">
                        Sau
                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7">
                            </path>
                        </svg>
                    </button>
                </div>
            </div>

            <!-- Delivery Detail -->
            <div v-if="selectedDelivery" class="detail-card">
                <div class="detail-header">
                    <h2 class="detail-title">
                        <svg class="title-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z">
                            </path>
                        </svg>
                        Chi tiết đơn giao hàng - {{ selectedDelivery.maGiaoHang }}
                    </h2>
                    <button @click="backToList" class="btn btn-outline" style="margin-left: 10px;">
                        <svg class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                        </svg>
                        Quay lại danh sách
                    </button>
                </div>

                <div class="detail-content">
                    <div class="detail-grid">
                        <!-- Thông tin đơn hàng -->
                        <div class="info-section">
                            <div class="info-card order-info">
                                <h3 class="info-title">
                                    <svg class="info-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z"></path>
                                    </svg>
                                    Thông tin đơn hàng
                                </h3>
                                <div class="info-list">
                                    <div class="info-item">
                                        <span class="info-label">ID Giao hàng:</span>
                                        <span class="info-value">{{ selectedDelivery.idGiaoHang }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Mã giao hàng:</span>
                                        <span class="info-value">{{ selectedDelivery.maGiaoHang }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Mã hóa đơn:</span>
                                        <span class="info-value invoice-highlight">{{ selectedDelivery.maHoaDon
                                            }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Ngày đặt hàng:</span>
                                        <span class="info-value">{{ formatDate(selectedDelivery.ngayDatHang) }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Tổng giá trị:</span>
                                        <span class="info-value price-highlight">{{
                                            formatCurrency(selectedDelivery.tongGiaTriDonHang) }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Trạng thái:</span>
                                        <span :class="getStatusClass(selectedDelivery.trangThaiDonHang)"
                                            class="status-badge">
                                            {{ selectedDelivery.trangThaiDonHang }}
                                        </span>
                                    </div>
                                </div>
                                <div v-if="selectedDelivery.chiTietGiaoHangResponseAdminList && selectedDelivery.chiTietGiaoHangResponseAdminList.length"
                                    class="table-container">
                                    <table class="product-table">
                                        <thead>
                                            <tr>
                                                <th>Sản phẩm</th>
                                                <th class="text-center">Số lượng</th>
                                                <th class="text-right"> Mã chi tiết đơn hàng</th>
                                                <th class="text-right">Đơn giá</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="item in selectedDelivery.chiTietGiaoHangResponseAdminList"
                                                :key="item.idChiTietHoaDon" class="product-row">
                                                <td class="product-name">{{ item.tenSanPham || 'N/A' }}</td>
                                                <td class="text-center">{{ item.soLuong || 'N/A' }}</td>
                                                <td class="text-center">{{ item.maChiTietGiaoHang }}</td>
                                                <td class="text-right">{{ formatCurrency(item.donGia) }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <!-- Thông tin khách hàng -->
                        <div class="info-section">
                            <div class="info-card customer-info-card">
                                <h3 class="info-title">
                                    <svg class="info-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z">
                                        </path>
                                    </svg>
                                    Thông tin khách hàng
                                </h3>
                                <div class="info-list">
                                    <div class="info-item">
                                        <span class="info-label">ID Khách hàng:</span>
                                        <span class="info-value">{{ selectedDelivery.idKhachHang }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Mã khách hàng:</span>
                                        <span class="info-value">{{ selectedDelivery.maKhachHang }}</span>
                                    </div>
                                    <div class="info-item">
                                        <span class="info-label">Tên khách hàng:</span>
                                        <span class="info-value customer-name-highlight">{{
                                            selectedDelivery.tenKhachHang }}</span>
                                    </div>
                                </div>
                            </div>

                            <div class="info-card address-info">
                                <h3 class="info-title">
                                    <svg class="info-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z">
                                        </path>
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
                                    </svg>
                                    Địa chỉ giao hàng
                                </h3>
                                <div class="address-content">
                                    <p class="address-text">{{ selectedDelivery.diaChiGiaoHang }}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="action-buttons">
                        <button class="btn btn-primary btn-lg">
                            <svg class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15">
                                </path>
                            </svg>
                            Cập nhật trạng thái
                        </button>
                        <button class="btn btn-success btn-lg">
                            <svg class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z">
                                </path>
                            </svg>
                            In phiếu giao hàng
                        </button>
                        <button class="btn btn-outline btn-lg">
                            <svg class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z">
                                </path>
                            </svg>
                            Liên hệ khách hàng
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { giaoHangGetAll } from '@/Service/Adminservice/GiaoHang/GiaoHangServices'
import { giaoHangDetail } from '@/Service/Adminservice/GiaoHang/GiaoHangServices'


// State reactive
const searchQuery = ref('')
const statusFilter = ref('')
const selectedDelivery = ref(null)
const deliveries = ref([])
const pageNo = ref(0)
const pageSize = ref(5) // Số lượng hiển thị trên mỗi trang
const totalPage = ref(0)
const totalElement = ref(0)
const selectedItems = ref([]) // Mảng lưu các ID đã được chọn
const viewMode = ref('table') // Chế độ xem mặc định là dạng bảng
const isLoading = ref(false)

const visiblePages = computed(() => {
    const pages = [];
    const total = totalPage.value;
    const current = pageNo.value;

    if (total <= 5) {
        // nếu tổng trang nhỏ hơn hoặc bằng 5, hiện tất cả
        for (let i = 0; i < total; i++) {
            pages.push(i);
        }
    } else {
        // luôn hiện trang đầu
        pages.push(0);

        if (current > 3) {
            pages.push(-1); // -1 đại diện cho dấu ...
        }

        // lấy khoảng 3 trang xung quanh trang hiện tại
        const start = Math.max(1, current - 1);
        const end = Math.min(total - 2, current + 1);

        for (let i = start; i <= end; i++) {
            pages.push(i);
        }
        if (current < total - 4) {
            // nếu trang hiện tại còn cách cuối > 3 trang thì thêm ...
            pages.push(-1);
        }

        // luôn hiện trang cuối
        pages.push(total - 1);
    }

    return pages;
});

// Hàm gọi API lấy danh sách đơn giao hàng
async function loadData() {
    isLoading.value = true;
    try {
        const response = await giaoHangGetAll(pageNo.value, pageSize.value)
        if (Array.isArray(response.data.content)) {
            deliveries.value = response.data.content
            totalPage.value = response.data.totalPages || 0;
            totalElement.value = response.data.totalElements || 0;
        } else {
            deliveries.value = [];
            totalPage.value = 0;
        }
    } catch (error) {
        console.error('Lấy danh sách giao hàng lỗi:', error)
    }finally{
        isLoading.value = false;
    }
}

const changePage = (newPage) => {
    pageNo.value = newPage;
    loadData();
    // Xóa các item đã chọn khi chuyển trang
    selectedItems.value = [];
};

// Computed lọc đơn giao hàng
const filteredDeliveries = computed(() => {
    let filtered = deliveries.value
    if (searchQuery.value.trim()) {
        const q = searchQuery.value.toLowerCase()
        filtered = filtered.filter(delivery =>
            delivery.maHoaDon.toLowerCase().includes(q) ||
            delivery.tenKhachHang.toLowerCase().includes(q) ||
            delivery.maGiaoHang.toLowerCase().includes(q)
        )
    }

    if (statusFilter.value) {
        filtered = filtered.filter(delivery => delivery.trangThaiDonHang === statusFilter.value)
    }

    return filtered
})

// Methods
function handleSearch() {
    // Xử lý tìm kiếm
}

function clearSearch() {
    searchQuery.value = ''
    statusFilter.value = ''
}
const viewDeliveryDetail = async (delivery) =>{
    isLoading.value = true
    try {
        const response = await giaoHangDetail(delivery.idGiaoHang);
        selectedDelivery.value = response.data;
    } catch (error) {
        selectedDelivery.value = delivery;
    }
    finally{
        isLoading.value = false;
    }
}

function backToList() {
    selectedDelivery.value = null
}

const formatDate = (dateString) => {
    if (!dateString) {
        return 'N/A';
    }
    const date = new Date(dateString);
    if (isNaN(date.getTime())) {
        return 'Invalid Date';
    }

    const day = date.getDate().toString().padStart(2, '0');      // ngày, 2 chữ số
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // tháng, 2 chữ số (tháng bắt đầu từ 0)
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
};

const formatCurrency = (amount) => {
    if (!amount || isNaN(amount)) {
        return '0 ₫';
    }
    try {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    } catch (error) {
        return `${amount} ₫`;
    }
};

function getStatusClass(status) {
    if(!status) return 'status-default';
    const statusClasses = {
        'chờ xử lý': 'status-pending',
        'đang giao': 'status-shipping',
        'đã giao': 'status-dlivered',
        'giao thất bại': 'status-failed',
        'đã hoàn tiền': 'status-returned',
        'đã đóng gói': 'status-packed',

    }
    return statusClasses[status.toLowerCase()] || 'status-default'
}

function getStatusCount(status) {
    return deliveries.value.filter(delivery => delivery.trangThaiDonHang === status).length
}

function getInitials(name) {
    return name.split(' ').map(word => word[0]).join('').toUpperCase().slice(0, 2)
}

// Các hàm xử lý checkbox
function toggleSelect(id) {
    const index = selectedItems.value.indexOf(id);
    if (index === -1) {
        selectedItems.value.push(id);
    } else {
        selectedItems.value.splice(index, 1);
    }
}

function isSelected(id) {
    return selectedItems.value.includes(id);
}

function selectAll() {
    selectedItems.value = filteredDeliveries.value.map(delivery => delivery.idGiaoHang);
}

function deselectAll() {
    selectedItems.value = [];
}

function toggleSelectAll(event) {
    if (event.target.checked) {
        selectAll();
    } else {
        deselectAll();
    }
}

// Gọi API khi component mount
onMounted(() => {
    loadData()
})
</script>

<style scoped src="@/style/GiaoHang/GiaoHang.css">
</style>