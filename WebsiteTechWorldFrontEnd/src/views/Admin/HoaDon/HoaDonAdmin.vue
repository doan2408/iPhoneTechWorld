<template>
    <div class="dashboard">
        <!-- Header Section: Phần tiêu đề -->
        <div class="dashboard-header">
            <div class="header-content">
                <div class="title-section">
                    <h1 class="dashboard-title">Quản lý hóa đơn</h1>
                    <p class="dashboard-subtitle">Theo dõi và quản lý tất cả hóa đơn của bạn</p>
                </div>
                <div class="header-actions">
                    <button class="btn btn-primary">
                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4">
                            </path>
                        </svg>
                        Tạo hóa đơn mới
                    </button>
                </div>
            </div>
        </div>

        <!-- Stats Cards: thẻ thống kê -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z">
                        </path>
                    </svg>
                </div>
                <div class="stat-content">
                    <h3 class="stat-number">{{ hoaDons.length }}</h3>
                    <p class="stat-label">Tổng hóa đơn</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1">
                        </path>
                    </svg>
                </div>
                <div class="stat-content">
                    <h3 class="stat-number">₫2.5M</h3>
                    <p class="stat-label">Doanh thu tháng</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </div>
                <div class="stat-content">
                    <h3 class="stat-number">15</h3>
                    <p class="stat-label">Chờ xử lý</p>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z">
                        </path>
                    </svg>
                </div>
                <div class="stat-content">
                    <h3 class="stat-number">85%</h3>
                    <p class="stat-label">Tỷ lệ hoàn thành</p>
                </div>
            </div>
        </div>

        <!-- Filters and Search : lọc và tìm kiếm  -->
        <div class="filters-section">
            <div class="search-container">
                <div class="search-box">
                    <svg class="search-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                    </svg>
                    <input type="text" placeholder="Tìm kiếm theo mã hóa đơn, tên khách hàng..." class="search-input"
                        v-model="searchQuery">
                </div>
            </div>

            <div class="filter-controls">
                <select class="filter-select" v-model="statusFilter">
                    <option value="">Tất cả trạng thái</option>
                    <option value="completed">Hoàn thành</option>
                    <option value="pending">Chờ xử lý</option>
                    <option value="cancelled">Đã hủy</option>
                </select>

                <select class="filter-select" v-model="typeFilter">
                    <option value="">Loại hóa đơn</option>
                    <option value="online">Trực tuyến</option>
                    <option value="offline">Tại cửa hàng</option>
                </select>

                <input type="date" class="filter-date" v-model="dateFilter">

                <div class="export-actions">
                    <button class="btn btn-outline">
                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z">
                            </path>
                        </svg>
                        Xuất Excel
                    </button>
                    <button class="btn btn-outline">
                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M17 17h2a2 2 0 002-2v-4a2 2 0 00-2-2H5a2 2 0 00-2 2v4a2 2 0 002 2h2m2 4h6a2 2 0 002-2v-4a2 2 0 00-2-2H9a2 2 0 00-2 2v4a2 2 0 002 2zm8-12V5a2 2 0 00-2-2H9a2 2 0 00-2 2v4h10z">
                            </path>
                        </svg>
                        In
                    </button>
                </div>
            </div>
        </div>

        <!-- Invoice Table -->
        <div class="table-section">
            <!-- chuyển chế độ xem -->
            <div class="table-header">
                <h2 class="table-title">Danh sách hóa đơn</h2>
                <div class="table-actions">
                    <button class="view-toggle active" @click="viewMode = 'table'">
                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M4 6h16M4 10h16M4 14h16M4 18h16"></path>
                        </svg>
                    </button>
                    <button class="view-toggle" @click="viewMode = 'grid'">
                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z">
                            </path>
                        </svg>
                    </button>
                </div>
            </div>

            <!-- Table View: chế độ xem bảng -->
            <div v-if="viewMode === 'table'" class="table-container">
                <table class="modern-table">
                    <thead>
                        <tr>
                            <th>
                                <input type="checkbox" class="checkbox">
                            </th>
                            <th>Mã hóa đơn</th>
                            <th>Khách hàng</th>
                            <th>Ngày tạo</th>
                            <th>Tổng tiền</th>
                            <th>Loại</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(hoaDon, index) in paginatedInvoices" :key="hoaDon.id" class="table-row">
                            <td>
                                <input type="checkbox" class="checkbox">
                            </td>
                            <td>
                                <div class="invoice-code">{{ hoaDon.maHoaDon }}</div>
                            </td>
                            <td>
                                <div class="customer-info">
                                    <div class="customer-avatar">{{ getInitials(hoaDon.tenKhachHang) }}</div>
                                    <div class="customer-name">{{ hoaDon.tenKhachHang }}</div>
                                </div>
                            </td>
                            <td>
                                <div class="date">{{ formatDate(hoaDon.ngayTao) }}</div>
                            </td>
                            <td>
                                <div class="amount">{{ formatCurrency(hoaDon.tongTien) }}</div>
                            </td>
                            <td>
                                <span class="type-badge" :class="getTypeBadgeClass(hoaDon.loaiHoaDon)">
                                    {{ hoaDon.loaiHoaDon }}
                                </span>
                            </td>
                            <td>
                                <span class="status-badge" :class="getStatusBadgeClass(hoaDon.trangThaiThanhToan)">
                                    {{ hoaDon.trangThaiThanhToan }}
                                </span>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <button class="action-btn view-btn" title="Xem chi tiết"
                                        @click="viewInvoiceDetails(hoaDon)">
                                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z">
                                            </path>
                                        </svg>
                                    </button>
                                    <button class="action-btn edit-btn" title="Chỉnh sửa">
                                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z">
                                            </path>
                                        </svg>
                                    </button>
                                    <button class="action-btn delete-btn" title="Xóa">
                                        <svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16">
                                            </path>
                                        </svg>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Grid View: Xem dạng lưới -->
            <div v-else class="grid-container">
                <div v-for="hoaDon in paginatedInvoices" :key="hoaDon.id" class="invoice-card">
                    <div class="card-header">
                        <div class="invoice-code">{{ hoaDon.maHoaDon }}</div>
                        <span class="status-badge" :class="getStatusBadgeClass(hoaDon.trangThaiThanhToan)">
                            {{ hoaDon.trangThaiThanhToan }}
                        </span>
                    </div>
                    <div class="card-content">
                        <div class="customer-section">
                            <div class="customer-avatar">{{ getInitials(hoaDon.tenKhachHang) }}</div>
                            <div class="customer-details">
                                <div class="customer-name">{{ hoaDon.tenKhachHang }}</div>
                                <div class="invoice-date">{{ formatDate(hoaDon.ngayTao) }}</div>
                            </div>
                        </div>
                        <div class="amount-section">
                            <div class="amount">{{ formatCurrency(hoaDon.tongTien) }}</div>
                            <span class="type-badge" :class="getTypeBadgeClass(hoaDon.loaiHoaDon)">
                                {{ hoaDon.loaiHoaDon }}
                            </span>
                        </div>
                    </div>
                    <div class="card-actions">
                        <button class="card-btn primary" @click="viewInvoiceDetails(hoaDon)">Xem chi tiết</button>
                        <button class="card-btn secondary">Chỉnh sửa</button>
                    </div>
                </div>
            </div>

            <!-- Pagination: Phân trang -->
            <div class="pagination-section">
                <div class="pagination-info">
                    Hiển thị {{ (pageNo * pageSize) + 1 }} - {{ Math.min((pageNo + 1) * pageSize,
                    filteredInvoices.length) }}
                    trong tổng số {{ hoaDons.length }} hóa đơn
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
            <!-- Modal xem chi tiết hóa đơn -->
            <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
                <div class="modal-content" @click.stop>
                    <!-- Header -->
                    <div class="modal-header">
                        <div class="header-title">
                            <svg class="header-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z">
                                </path>
                            </svg>
                            <h3>
                                <span v-if="loading">Đang tải...</span>
                                <span v-else-if="selectedInvoice">Chi tiết hóa đơn {{ selectedInvoice.maHoaDon }}</span>
                                <span v-else>Chi tiết hóa đơn</span>
                            </h3>
                        </div>
                        <button class="modal-close" @click="closeModal">
                            <svg class="close-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12"></path>
                            </svg>
                        </button>
                    </div>

                    <!-- Body -->
                    <div class="modal-body">
                        <!-- Loading State -->
                        <div v-if="loading" class="loading-container">
                            <div class="loading-spinner"></div>
                            <p>Đang tải thông tin hóa đơn...</p>
                        </div>

                        <!-- Error State -->
                        <div v-else-if="error" class="error-container">
                            <svg class="error-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                            </svg>
                            <h4>Có lỗi xảy ra</h4>
                            <p>{{ error }}</p>
                            <button class="retry-btn" @click="retryFetch">
                                <svg class="retry-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                        d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15">
                                    </path>
                                </svg>
                                Thử lại
                            </button>
                        </div>

                        <!-- Invoice Content -->
                        <div v-else-if="selectedInvoice">   
                            <!-- Invoice Information Card -->
                            <div class="info-card">
                                <div class="card-header">
                                    <svg class="card-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z">
                                        </path>
                                    </svg>
                                    <h4>Thông tin hóa đơn</h4>
                                </div>
                                <div class="card-content">
                                    <div class="info-grid">
                                        <!-- Left Column -->
                                        <div class="info-column">
                                            <div class="info-row">
                                                <span class="info-label">Mã hóa đơn:</span>
                                                <span class="info-value font-semibold">{{ selectedInvoice.maHoaDon
                                                    }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Khách hàng:</span>
                                                <span class="info-value">{{ selectedInvoice.tenKhachHang || 'N/A'
                                                    }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Ngày tạo:</span>
                                                <span class="info-value">{{ formatDate(selectedInvoice.ngayTao)
                                                    }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Loại hóa đơn:</span>
                                                <span class="badge"
                                                    :class="getTypeBadgeClass(selectedInvoice.loaiHoaDon)">
                                                    {{ selectedInvoice.loaiHoaDon || 'N/A' }}
                                                </span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Trạng thái:</span>
                                                <span class="badge"
                                                    :class="getStatusBadgeClass(selectedInvoice.trangThaiThanhToan)">
                                                    {{ selectedInvoice.trangThaiThanhToan || 'N/A' }}
                                                </span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Ngày thanh toán:</span>
                                                <span class="info-value">{{ formatDate(selectedInvoice.ngayThanhToan) ||
                                                    'N/A' }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Mã giảm giá:</span>
                                                <span class="info-value">{{ selectedInvoice.maPhieuGiamGia || 'N/A'
                                                    }}</span>
                                            </div>
                                        </div>

                                        <!-- Right Column -->
                                        <div class="info-column">
                                            <div class="info-row">
                                                <span class="info-label">Người nhận:</span>
                                                <span class="info-value">{{ selectedInvoice.tenNguoiNhan || 'N/A'
                                                    }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">SĐT:</span>
                                                <span class="info-value">{{ selectedInvoice.sdt || 'N/A' }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Địa chỉ:</span>
                                                <span class="info-value address"
                                                    :title="selectedInvoice.diaChi || 'N/A'">
                                                    {{ selectedInvoice.diaChi || 'N/A' }}
                                                </span>
                                            </div>

                                            <div class="separator"></div>

                                            <div class="info-row">
                                                <span class="info-label">Tổng tiền:</span>
                                                <span class="info-value">{{ formatCurrency(selectedInvoice.tongTien)
                                                    }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Phí ship:</span>
                                                <span class="info-value">{{ formatCurrency(selectedInvoice.phiShip)
                                                    }}</span>
                                            </div>
                                            <div class="info-row">
                                                <span class="info-label">Số tiền giảm:</span>
                                                <span class="info-value discount">-{{
                                                    formatCurrency(selectedInvoice.soTienGiam) }}</span>
                                            </div>
                                            <div class="info-row total-row">
                                                <span class="info-label total-label">Thành tiền:</span>
                                                <span class="info-value total-value">{{
                                                    formatCurrency(selectedInvoice.thanhTien) }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Product Details Card -->
                            <div class="info-card">
                                <div class="card-header">
                                    <svg class="card-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
                                    </svg>
                                    <h4>Chi tiết sản phẩm</h4>
                                </div>
                                <div class="card-content">
                                    <div v-if="selectedInvoice.chiTietHoaDonAdminResponseList && selectedInvoice.chiTietHoaDonAdminResponseList.length"
                                        class="table-container">
                                        <table class="product-table">
                                            <thead>
                                                <tr>
                                                    <th>Sản phẩm</th>
                                                    <th class="text-center">Số lượng</th>
                                                    <th class="text-right">Đơn giá</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="item in selectedInvoice.chiTietHoaDonAdminResponseList"
                                                    :key="item.idChiTietHoaDon" class="product-row">
                                                    <td class="product-name">{{ item.tenSanPham || 'N/A' }}</td>
                                                    <td class="text-center">{{ item.soLuong || 'N/A' }}</td>
                                                    <td class="text-right">{{ formatCurrency(item.donGia) }}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div v-else class="no-products">
                                        <svg class="no-products-icon" fill="none" stroke="currentColor"
                                            viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4">
                                            </path>
                                        </svg>
                                        <p>Không có chi tiết sản phẩm.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Footer -->
                    <div class="modal-footer">
                        <button class="modal-btn" @click="closeModal">
                            <svg class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M6 18L18 6M6 6l12 12"></path>
                            </svg>
                            Đóng
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup>
import axios from 'axios';
import { computed, onMounted, ref, watch } from 'vue';

const hoaDons = ref([]);
const pageNo = ref(0);
const pageSize = ref(5);
const totalPage = ref(0);
const viewMode = ref('table'); // nếu view là table sẽ hiển thị dưới dạng table còn ko hiển thị grid 
const searchQuery = ref('');
const statusFilter = ref('');
const typeFilter = ref('');
const dateFilter = ref('');
const isLoading = ref(false);
// Thêm các ref để quản lý modal
const showModal = ref(false);
const selectedInvoice = ref(null);

// Hàm mở modal và lấy chi tiết hóa đơn
const viewInvoiceDetails = async (invoice) => {
    isLoading.value = true;
    try {
        const response = await axios.get(`/admin/hoa-don/${invoice.idHoaDon}`); // Sử dụng idHoaDon
        selectedInvoice.value = response.data;
        console.log('Dữ liệu chi tiết hóa đơn:', response.data); // Kiểm tra dữ liệu
        showModal.value = true;
    } catch (error) {
        console.error('Error fetching invoice details:', error);
        selectedInvoice.value = invoice;
        showModal.value = true;
    } finally {
        isLoading.value = false;
    }
};

// Hàm đóng modal
const closeModal = () => {
    showModal.value = false;
    selectedInvoice.value = null;
};

// là một computed property nhiệm vụ lọc danh sách hóa đơn trả về tất cả phần tử đã lọc nhưng chưa phân trang
const filteredInvoices = computed(() => {
    let filtered = hoaDons.value || [];

    // Apply filters: lọc theo trạng thái tìm kiếm 
    if (searchQuery.value) {
        filtered = filtered.filter(invoice => {
            const maHoaDon = invoice.maHoaDon || '';
            const tenKhachHang = invoice.tenKhachHang || '';
            const searchTerm = searchQuery.value.toLowerCase();

            return maHoaDon.toLowerCase().includes(searchTerm) ||
                tenKhachHang.toLowerCase().includes(searchTerm);
        });
    }
    
    // lọc theo trạng thái thanh toán
    if (statusFilter.value) {
        filtered = filtered.filter(invoice =>
            invoice.trangThaiThanhToan &&
            invoice.trangThaiThanhToan.toLowerCase().includes(statusFilter.value.toLowerCase())
        );
    }

    // lọc theo loại hóa đơn
    if (typeFilter.value) {
        filtered = filtered.filter(invoice =>
            invoice.loaiHoaDon &&
            invoice.loaiHoaDon.toLowerCase().includes(typeFilter.value.toLowerCase())
        );
    }

    return filtered; 
});

// trả về tất cả phần tử đã lọc và phân trang
const paginatedInvoices = computed(() => {
    const start = pageNo.value * pageSize.value;
    const end = start + pageSize.value;
    return filteredInvoices.value.slice(start, end); 
});

// hiển thị số nút chuyển trang ví dụ 1 2 3 4 5 chỉ hiện 5 trang dù có 10 hay 11 trang
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



// hàm call api load dữ liệu
const loadData = async () => {
    isLoading.value = true;
    try {
        const response = await axios.get('/admin/hoa-don');

        //Nếu response.data là object chứa content - trong backend trả về page là trả về object json
        if (Array.isArray(response.data)) {
            hoaDons.value = response.data;
        }else if (Array.isArray(response.data.content)) {
            hoaDons.value = response.data.content;
            totalPage.value = response.data.totalPage || 0;
        }else{
            hoaDons.value = [];
        }
        totalPage.value = Math.ceil(filteredInvoices.value.length / pageSize.value);  // math.ceil là làm tròn lên 3.1 vẫn làm tròn lên 4
    } catch (error) {
        console.error('Error loading data:', error);
        hoaDons.value = [];
        totalPage.value = 0;
    } finally {
        isLoading.value = false;
    }
};

const changePage = (newPage) => {
    pageNo.value = newPage;
    // Recalculate pagination for filtered data
    totalPage.value = Math.ceil(filteredInvoices.value.length / pageSize.value);
};

// hàm để tạo tên viết tắt trước tên 
const getInitials = (name) => {
    if (!name || typeof name !== 'string') { // nếu null , rỗng hoặc không phải string trả về N/A
        return 'N/A';
    }
    const words = name.trim().split(/\s+/); // tách tên theo khoảng trắng

    if (words.length === 1) {
        const word = words[0];
        return word[0].toUpperCase();
    }

    const firstInitial = words[0][0]; // kí tự đầu của họ
    const lastWord = words[words.length - 1]; // lấy ra tên 
    const lastInitial = lastWord[0]; // kí tự đầu của tên

    return (firstInitial + lastInitial).toUpperCase();
};

// định dạng ngày
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

const getStatusBadgeClass = (status) => {
    if (!status) return 'status-default';
    const statusMap = {
        'completed': 'status-completed',
        'pending': 'status-pending',
        'cancelled': 'status-cancelled',
        'refunded': 'status-refunded',
        'paid': 'status-paid',
    };
    return statusMap[status.toLowerCase()] || 'status-default';
};

const getTypeBadgeClass = (type) => {
    if (!type) return 'type-default';

    const typeMap = {
        'online': 'type-online',
        'offline': 'type-offline',
        'trực tuyến': 'type-online',
        'pos': 'type-offline'
    };
    return typeMap[type.toLowerCase()] || 'type-default'; // trả về định dạng css
};

// Theo dõi các thay đổi bộ lọc để cập nhật phân trang
watch([searchQuery, statusFilter, typeFilter], () => {
    pageNo.value = 0; // Khi bộ lọc thay đổi, quay về trang đầu tiên
    totalPage.value = Math.ceil(filteredInvoices.value.length / pageSize.value);
    // Tính lại tổng số trang dựa vào số lượng hóa đơn sau khi lọc
});

onMounted(() => loadData());
</script>

<style scoped src="@/style/HoaDon/HoaDon.css"></style>
