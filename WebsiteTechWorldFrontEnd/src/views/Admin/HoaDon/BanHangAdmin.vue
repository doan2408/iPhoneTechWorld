<template>
    <div class="pos-system">
        <!-- Header với tabs hóa đơn -->
        <div class="pos-header">
            <div class="left-section">
                <!-- Tabs hóa đơn -->
                <div class="invoice-tabs">
                    <div v-for="invoice in invoices" :key="invoice.id" @click="selectInvoice(invoice.id)"
                        :class="['invoice-tab', { active: currentInvoiceId == invoice.id }]">
                        <!-- Badge hiển thị số lượng -->
                        <div class="badge">
                            {{ invoice.soLuong || 0 }}
                        </div>

                        <span>{{ invoice.maHoaDon }}</span>

                        <button v-if="invoices.length > 1" @click.stop="closeInvoice(invoice.id)" class="close-tab-btn">
                            <X class="close-icon" />
                        </button>
                    </div>

                    <button @click="addNewInvoice" class="add-tab-btn">
                        <Plus class="add-icon" />
                    </button>
                </div>


            </div>

            <!-- Header actions -->
            <div class="header-actions">
                <button @click="lockScreen" class="action-btn" title="Khóa màn hình ( Ctrl + Q )">
                    <Lock class="btn-icon" />&ensp; Khóa màn hình
                </button>
                <ScreenLock ref="screenLock" :autoUnlockMinutes="2" />
                <button class="action-btn" title="Làm mới" @click="reloadPage()">
                    <RotateCcw class="btn-icon" /> &ensp;Làm mới tab
                </button>
            </div>
            <div id="screen-lock-overlay" style="display: none;"></div>
        </div>

        <!-- Main content : nội dung -->
        <div class="pos-main">
            <!--  phần bên trái -->
            <div class="left-panel">
                <!-- Categories : danh mục sản phẩm -->
                <div class="categories-section">
                    <h3>Danh mục sản phẩm</h3>
                    <div class="categories-list">
                        <button class="category-btn" @click="selectedCategory = 'all'; loadProducts()"
                            :class="{ 'active': selectedCategory === 'all' }">
                            <Smartphone class="category-icon" />
                            All
                        </button>
                        <button v-for="category in categoryProduct" :key="category.tenSanPham" @click="selectedCategory = category.tenSanPham,
                            loadProducts()"
                            :class="['category-btn', { active: selectedCategory === category.tenSanPham }]">
                            <component :is="category.icon" class="category-icon" />
                            {{ category.tenSanPham }}
                        </button>
                        <!-- phan trang category -->
                        <div class="pagination-controls">
                            <button @click="prevPage" :disabled="pageNo === 0"> &lt; </button>
                            <span>Trang {{ pageNo + 1 }} / {{ totalPagesCategory }}</span>
                            <button @click="nextPage" :disabled="pageNo + 1 >= totalPagesCategory"> &gt; </button>
                        </div>
                    </div>
                </div>

                <!-- Quick actions : hành động  -->
                <div class="quick-actions">
                    <button class="quick-btn" @click="openSalesHistoryModal()">
                        <History class="btn-icon" />
                        Lịch sử bán hàng
                    </button>
                </div>
            </div>

            <!-- phần bên phải : button chọn khách hàng , sản phẩm , giỏ hàng-->
            <div class="right-panel">
                <!-- Customer search -->
                <div class="customer-search">
                    <div class="search-box">
                        <!-- <select v-model="searchType" class="search-select">
                            <option disabled value="">-- Chọn loại tìm kiếm --</option>
                            <option value="name">Tên</option>
                            <option value="ma">Mã</option>
                        </select>

                        <div class="search-input-wrapper">
                            <Search class="search-icon" />
                            <input v-model="customerSearchQuery" type="text"
                                :placeholder="searchType === 'ma' ? 'Nhập mã sản phẩm' : 'Nhập tên sản phẩm'"
                                class="search-input" @keydown.f4.prevent="searchCustomer" @blur="searchCustomer" />
                        </div> -->
                    </div>
                    <div class="customer-actions">
                        <button @click="listKhachHang(0)" class="customer-btn" title="Thêm khách hàng">
                            <Plus class="btn-icon" /> Thêm khách hàng
                        </button>
                        <button class="customer-btn" title="Lọc" @click="toggleFilter">
                            <Filter class="btn-icon" /> Bộ lọc
                        </button>

                        <div v-if="isFilterOpen" class="filter-overlay">
                            <div class="filter-popup">
                                <h2>Bộ lọc sản phẩm</h2>

                                <!-- Mã sản phẩm chi tiết -->
                                <div class="filter-group">
                                    <label>Mã SPCT</label>
                                    <input type="text" v-model="filters.maSpct"
                                        placeholder="Nhập mã SPCT (VD: SPCT001)" />
                                </div>

                                <!-- Loại sản phẩm -->
                                <div class="filter-group">
                                    <label>Loại sản phẩm</label>
                                    <select v-model="filters.model">
                                        <option value="">Tất cả</option>
                                        <option v-for="loai in pulldownData.loai" :key="loai" :value="loai.id"> {{
                                            loai.tenLoai }}
                                        </option>
                                    </select>
                                </div>

                                <!-- Dung lượng -->
                                <div class="filter-group">
                                    <label>Dung lượng</label>
                                    <select v-model="filters.storage">
                                        <option value="">Tất cả</option>
                                        <option v-for="dungLuongRom in pulldownData.dungLuong" :key="dungLuongRom"
                                            :value="dungLuongRom.id"> {{ dungLuongRom.dungLuong }}
                                        </option>
                                    </select>
                                </div>

                                <!-- Màu sắc -->
                                <div class="filter-group">
                                    <label>Màu sắc</label>
                                    <select v-model="filters.color">
                                        <option value="">Tất cả</option>
                                        <option v-for="mau in pulldownData.mauSac" :key="mau" :value="mau.id"> {{
                                            mau.tenMau }} </option>
                                    </select>
                                </div>

                                <!-- Khoảng giá -->
                                <div class="filter-group">
                                    <label>Khoảng giá (triệu VND)</label>
                                    <div class="range-inputs">
                                        <input type="number" v-model.number="filters.priceMin" placeholder="Từ" />
                                        <span>–</span>
                                        <input type="number" v-model.number="filters.priceMax" placeholder="Đến" />
                                    </div>
                                </div>

                                <!-- Số lượng tồn kho -->
                                <div class="filter-group">
                                    <label>Số lượng tồn kho</label>
                                    <div class="range-inputs">
                                        <input type="number" v-model.number="filters.quantityMin" placeholder="Từ" />
                                        <span>–</span>
                                        <input type="number" v-model.number="filters.quantityMax" placeholder="Đến" />
                                    </div>
                                </div>

                                <!-- Nút hành động -->
                                <div class="filter-actions">
                                    <button class="apply-btn-filter" @click="applyFilters">Áp dụng</button>
                                    <button class="reset-btn-filter" @click="resetFilters">Xóa bộ lọc</button>
                                    <button class="close-btn-filter" @click="toggleFilter">Đóng</button>
                                </div>
                            </div>
                        </div>
                        <button class="customer-btn" @click="openQRModal" title="Quét mã QR">
                            <ScanLine class="btn-icon" /> Quét QR
                        </button>

                        <!-- Modal hiển thị camera -->
                        <el-dialog v-model="showQRModal" width="400px" title="Quét mã sản phẩm" destroy-on-close
                            @close="onCloseModal">
                            <QrScanner @scanned="onScannedImei" />
                        </el-dialog>

                    </div>
                </div>

                <!-- Products grid -->
                <div class="products-section">
                    <div class="products-grid">
                        <div v-for="product in products" :key="product.idSanPhamChiTiet" @click="openImeiModal(product)"
                            class="product-card">
                            <div class="product-image">
                                <img :src="product.url" :alt="product.tenSanPham" />
                                <div v-if="product.soLuong === 0" class="out-of-stock">
                                    Hết hàng
                                </div>
                                <div v-else-if="product.soLuong <= 5" class="low-stock">
                                    {{ product.soLuong }}
                                </div>
                            </div>
                            <div class="product-info">
                                <h4>{{ product.tenSanPham }} {{ product.rom }}</h4>
                                <div class="product-color">{{ product.mau }}</div>
                                <div class="product-price"
                                    v-if="product.giaBan && product.giaBan !== product.giaTruocKhuyenMai">
                                    <span class="product-price-sale">{{ formatCurrency(product.giaBan) }}</span>
                                    <span class="product-price-original">{{ formatCurrency(product.giaTruocKhuyenMai)
                                    }}</span>
                                </div>
                                <div class="product-price" v-else>
                                    <span class="product-price-sale">{{ formatCurrency(product.giaTruocKhuyenMai)
                                    }}</span>
                                </div>
                                <div class="product-stock" :class="{
                                    'out-of-stock-text': product.soLuong === 0,
                                    'low-stock-text': product.soLuong > 0 && product.soLuong <= 5,
                                    'in-stock-text': product.soLuong > 5
                                }">
                                    {{ product.soLuong === 0 ? 'Hết hàng' : 'Số lượng: ' + product.soLuong }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div v-if="isImeiModalOpen" class="modal-overlay">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h2>Chọn IMEI cho {{ selectedProductForImei?.maSanPhamChiTiet }} - {{
                                selectedProductForImei?.tenSanPham }}</h2>
                            <div class="quantity-input-group">
                                <label for="quantityToSelect">Số lượng:</label>
                                <input type="number" id="quantityToSelect" v-model.number="quantityToSelect"
                                    :max="imeiTotalItems" @input="handleQuantityInputChange" />
                            </div>
                            <button @click="closeImeiModal" class="close-button">&times;</button>
                        </div>

                        <div class="imei-search">
                            <div class="search-box">
                                <input v-model="imeiSearchQuery" type="text" placeholder="Tìm kiếm IMEI"
                                    class="search-input" />
                            </div>
                        </div>

                        <div class="modal-body">
                            <div class="imei-container">
                                <!-- Danh sách IMEI có sẵn (bên trái) -->
                                <div class="imei-list-container">
                                    <h3>Danh sách IMEI</h3>
                                    <div class="imei-list">
                                        <div v-for="imei in availableImeis" :key="imei.id" class="imei-item">
                                            <input type="checkbox" :id="`imei-${imei.id}`" :value="imei"
                                                v-model="selectedImeis" />
                                            <label :for="`imei-${imei.id}`">{{ imei.imei }}</label>
                                        </div>
                                    </div>
                                    <div class="pagination-controls">
                                        <button @click="goToImeiPage(imeiCurrentPage - 1)"
                                            :disabled="imeiCurrentPage === 0">Trước</button>
                                        <span>Trang {{ imeiCurrentPage + 1 }} / {{ imeiTotalPages }}</span>
                                        <button @click="goToImeiPage(imeiCurrentPage + 1)"
                                            :disabled="imeiCurrentPage + 1 === imeiTotalPages">Sau</button>
                                    </div>
                                </div>

                                <!-- Danh sách IMEI đã chọn (bên phải) -->
                                <div class="selected-imeis-container" style="max-width: 200px;">
                                    <h3>IMEI đã chọn ({{ selectedImeis.length }}/{{ quantityToSelect || 0 }})</h3>
                                    <div class="selected-imeis">
                                        <ul>
                                            <li v-for="imei in selectedImeis" :key="imei.id">
                                                {{ imei.imei }}
                                                <button @click="removeImei(imei)" class="remove-imei-btn">X</button>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>



                        <div class="modal-footer">
                            <button @click="autoSelectImeis" class="btn auto-select-btn"> Tự động chọn ({{
                                quantityToSelect || 0 }} IMEI)
                            </button>
                            <button @click="confirmImeiSelection" class="btn confirm-btn">
                                Xác nhận ({{ selectedImeis.length }} / {{ quantityToSelect || 0 }} IMEI)
                            </button>
                            <button @click="closeImeiModal" class="btn cancel-btn">Hủy</button>
                        </div>
                    </div>
                </div>




                <!-- Phan trang san pham -->
                <div class="pagination">
                    <button @click="previousPageProduct(currentCategory)" :disabled="pageNoProduct === 0"
                        class="page-btn">
                        <ChevronLeft class="page-icon" />
                    </button>
                    <span class="page-info">{{ pageNoProduct + 1 }}/{{ totalPagesProdut }}</span>
                    <button @click="nextPageProduct(currentCategory)" :disabled="pageNoProduct + 1 === totalPagesProdut"
                        class="page-btn">
                        <ChevronRight class="page-icon" />
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer - Cart & Payment -->
    <div class="pos-footer">
        <div class="cart-summary">
            <!-- Order notes -->
            <!-- <div class="order-notes">
                <Edit class="notes-icon" />
                <input v-model="currentInvoice.notes" type="text" placeholder="Ghi chú đơn hàng" class="notes-input" />
            </div> -->

            <!-- Cart items (if any) : san pham da chon -->
            <div v-if="currentInvoiceDetail?.chiTietHoaDonAdminResponseList?.length > 0" class="cart-items-summary">
                <div class="cart-header">
                    <span>Sản phẩm đã chọn ({{
                        currentInvoiceDetail?.chiTietHoaDonAdminResponseList?.length }})</span>
                    <button @click="showCartDetails = !showCartDetails" class="toggle-cart-btn">
                        <ChevronUp v-if="showCartDetails" class="toggle-icon" />
                        <ChevronDown v-else class="toggle-icon" />
                    </button>
                </div>


                <div v-if="showCartDetails" class="cart-items-list">
                    <div class="select-all-row">
                        <input type="checkbox" id="selectAll" v-model="selectAllItems" @change="toggleSelectAll">
                        <label for="selectAll">Chọn tất cả để trả</label>
                        <button @click="confirmReturnSelected" :disabled="selectedItems.length === 0"
                            class="return-selected-btn">
                            Trả các mục đã chọn
                        </button>
                    </div>
                    <div v-for="item in listHdctByImeiDaBan" :key="item.idImei" class="imei-row">
                        <div class="imei-left">
                            <input type="checkbox" :id="'item-' + item.idImei" :value="item.idImei"
                                v-model="selectedItems">
                            <label :for="'item-' + item.idImei" class="imei-checkbox-label"></label>

                            <span class="imei-name">{{ item.tenSanPham }}</span>
                            <span class="imei-detail">({{ item.dungLuongRom }} • {{ item.tenMau }})</span>
                            <span class="imei-imei">| IMEI: <span class="imei-code">{{ item.soImei }}</span></span>
                            <span class="imei-status">
                                | Trạng thái imei:
                                <span :class="getStatusClass(item.trangThai)">
                                    {{ formatTrangThai(item.trangThai) }}
                                </span>
                            </span>

                        </div>

                        <div class="imei-right">
                            <span class="imei-price">{{ formatCurrency(item.giaBan) }}</span>
                            <button @click="confirmRemoveFromCart(item)" class="remove-btn" title="Xóa IMEI">
                                <Undo class="remove-icon" />
                            </button>
                        </div>
                    </div>

                    <!-- Modal xác nhận -->
                    <Transition name="modal-fade">
                        <div v-if="showDeleteConfirmModal" class="modal-overlay">
                            <div class="modal-content-2">
                                <h2>Xác nhận trả sản phẩm</h2>
                                <p>
                                    Bạn muốn trả IMEI:
                                    <strong>{{ itemToDelete?.soImei }}</strong>
                                    của sản phẩm:
                                    <strong>{{ itemToDelete?.tenSanPham }}</strong>
                                    ({{ itemToDelete?.dungLuongRom }} • {{ itemToDelete?.tenMau }})?
                                </p>

                                <div class="modal-actions">
                                    <button @click="removeFromCart" class="btn btn-danger">
                                        Trả dòng sản phẩm
                                    </button>
                                    <button @click="() => handleRemoveSingleImei(itemToDelete)" class="btn btn-warning">
                                        Trả sản phẩm
                                    </button>
                                    <button @click="closeModal" class="btn btn-secondary">
                                        Hủy
                                    </button>
                                </div>
                            </div>
                        </div>
                    </Transition>
                </div>
            </div>
            <div v-else class="no-product-message">
                Không có sản phẩm được chọn
            </div>

            <!-- Total tong tien hang -->
            <div class="footer-container">
                <div class="customer-display" v-if="currentInvoiceDetail.maKhachHang"
                    style="display: flex; align-items: center; justify-content: space-between;">
                    <span><b>{{ currentInvoiceDetail.maKhachHang }}: {{
                            currentInvoiceDetail.tenKhachHang }}</b></span>
                    <button @click="selectedKhachHang" class="toggle-cart-btn"
                        style="background: none; border: none; padding: 0; cursor: pointer;">
                        <X type="small" style="font-size: 12px; color: red;" />
                    </button>
                </div>
                <div class="total-section">
                    <span class="total-label">Tổng tiền hàng:</span>
                    <span class="total-amount">{{ formatCurrency(totalProductAmount) }}</span>
                </div>
                <div class="shipping-fee-section" v-if="isShipping">
                    <span class="shipping-fee-label">Phí giao hàng:</span>
                    <span class="shipping-fee-amount">50.000</span>
                </div>
                <div class="discount-section" v-if="discountAmount > 0"
                    style="flex-direction: row; max-height: 40.8px;">
                    <span class="discount-label">Giảm giá:</span>
                    <span class="discount-amount">- {{ formatCurrency(discountAmount) }}</span>
                </div>
                <div class="grand-total-section">
                    <span class="grand-total-label">Tổng thanh toán:</span>
                    <span class="grand-total-amount">{{ formatCurrency(grandTotal) }}</span>
                </div>

                <div class="action-section">
                    <div class="switch-container">
                        <label class="switch">
                            <input type="checkbox" v-model="isShipping" @change="toggleShipping">
                            <span class="slider round"></span>
                        </label>
                        <span class="label-text">GIAO HÀNG ( Giao nhanh )</span>
                    </div>

                    <div class="shipping-discount-container">
                        <div v-if="isShipping" class="shipping-section">
                            <h4>Thông tin Giao hàng</h4>
                            <div class="shipping-info-display">
                                <p><strong>Tên người nhận:</strong> {{ shippingInfo.tenNguoiNhan || 'Chưa cập nhật' }}
                                </p>
                                <p><strong>Số điện thoại:</strong> {{ shippingInfo.sdtNguoiNhan || 'Chưa cập nhật' }}
                                </p>
                                <p><strong>Email người nhận:</strong> {{ shippingInfo.emailNguoiNhan || 'Chưa cập nhật'
                                }}
                                </p>
                                <p><strong>Địa chỉ:</strong> {{ shippingInfo.diaChiChiTiet || 'Chưa cập nhật' }}</p>
                                <p><strong>Phí giao hàng:</strong> 50.000 VNĐ </p>
                                <button @click="openShippingPopup" class="update-shipping-btn">Cập nhật thông tin giao
                                    hàng</button>
                            </div>

                            <!-- Popup for updating shipping info -->
                            <div v-if="showShippingPopup" class="popup-overlay">
                                <div class="popup-content">
                                    <h4>Cập nhật thông tin giao hàng</h4>
                                    <div class="popup-form">
                                        <input type="text" v-model="shippingInfo.tenNguoiNhan"
                                            placeholder="Tên người nhận" class="input-field" required>
                                        <input type="tel" v-model="shippingInfo.sdtNguoiNhan"
                                            placeholder="Số điện thoại" class="input-field" required>
                                        <input type="mail" v-model="shippingInfo.emailNguoiNhan"
                                            placeholder="Email người nhận" class="input-field" required>
                                        <div class="address-form">
                                            <label>Chọn tỉnh:</label>
                                            <select v-model="selectedTinh" @change="onTinhChange" class="select-box"
                                                disabled>
                                                <option disabled value="">-- Tỉnh/Thành phố --</option>
                                                <option v-for="t in tinhList" :key="t.code" :value="t">{{ t.name }}
                                                </option>
                                            </select>

                                            <!-- <label>Chọn huyện:</label>
                                            <select v-model="selectedHuyen" @change="onHuyenChange" class="select-box"
                                                :disabled="!selectedTinh">
                                                <option disabled value="">-- Quận/Huyện --</option>
                                                <option v-for="h in huyenList" :key="h.code" :value="h">{{ h.name }}
                                                </option>
                                            </select> -->

                                            <label>Chọn Xã:</label>
                                            <select v-model="selectedXa" @change="onXaChange" class="select-box"
                                                :disabled="!selectedTinh">
                                                <option disabled value="">-- Phường/Xã --</option>
                                                <option v-for="x in xaList" :key="x.code" :value="x">{{ x.name }}
                                                </option>
                                            </select>
                                        </div>
                                        <textarea v-model="shippingInfo.diaChiChiTiet"
                                            placeholder="Số nhà, tên đường..." class="input-field" rows="2"
                                            required></textarea>
                                        <div class="shipping-fee-display">
                                            Phí giao hàng: <span class="fee-amount">50.000 VNĐ</span>
                                        </div>
                                    </div>
                                    <div class="popup-actions">
                                        <button @click="confirmShippingInfoPopup" class="confirm-btn">Xác nhận</button>
                                        <button @click="closeShippingPopup" class="cancel-btn">Hủy</button>
                                    </div>
                                    <ConfirmModal v-if="showMessageConfirmShipping"
                                        message="Bạn có chắc cập nhật thông tin giao hàng không?"
                                        @confirm="confirmShippingInfo" @cancel="onCancelCreate" />
                                </div>
                            </div>
                        </div>

                        <div class="discount-section" style="padding-top: 0; width: 400px;">
                            <h4>Chọn phiếu giảm giá</h4>
                            <div v-if="selectedDiscount">
                                {{ selectedDiscount.tenGiamGia }}
                                <button @click="clearDiscount" class="confirm-btn"
                                    style="background-color: red; color: white; margin-left: 10px;">Xóa</button>
                            </div>
                            <div v-else>
                                <span>Chưa chọn phiếu giảm giá</span>
                            </div>
                            <button @click="openVoucherModal" class="confirm-btn" style="background-color: #000;">
                                Chọn phiếu giảm giá
                            </button>
                        </div>
                    </div>

                    <div class="payment-section">
                        <h3>Chọn phương thức thanh toán</h3>
                        <div class="payment-methods">
                            <label class="payment-method-option" v-for="method in paymentMethods" :key="method.code">
                                <input type="radio" name="paymentMethod" :value="method.code"
                                    v-model="selectedPaymentMethod">
                                <img :src="getIconUrl(method.code)" :alt="method.displayName" class="payment-icon">
                                <span>{{ method.displayName }}</span>
                            </label>
                        </div>

                        <div v-if="selectedPaymentMethod === 'NGAN_HANG'" class="payment-detail bank-transfer-info">
                            <h4>Thông tin tài khoản ngân hàng</h4>
                            <p><strong>Tên ngân hàng:</strong> Ngân hàng TechWord</p>
                            <p><strong>Số tài khoản:</strong> 111111111111111</p>
                            <p><strong>Chủ tài khoản:</strong> CÔNG TY TNHH TechWorld</p>
                            <p><strong>Nội dung chuyển khoản:</strong> [Mã đơn hàng của bạn]</p>
                            <p class="note">Vui lòng chuyển khoản đúng nội dung để đơn hàng đ qược xử lý nhanh chóng.
                            </p>
                        </div>

                        <div class="terms-conditions">
                            <label>
                                <input type="checkbox" v-model="agreedToTerms">
                                Tôi đồng ý với
                                <router-link to="/admin/dieuKhoan" ttarget="blank">
                                    Điều khoản và Điều kiện
                                </router-link>
                                của cửa hàng.
                            </label>
                        </div>

                    </div>


                    <!-- <button @click="processPayment" class="payment-btn">THANH TOÁN</button> -->
                    <button @click="processPayment" class="payment-btn" :disabled="isLoadingPayment">
                        <span v-if="isLoadingPayment">Đang xử lý...</span>
                        <span v-else>THANH TOÁN</span>
                    </button>
                    <InvoicePrint v-if="showInvoice" :idHoaDon="selectedInvoiceId" ref="invoiceRef" />
                </div>
            </div>
        </div>
    </div>

    <!-- Customer Modal -->
    <div v-if="showCustomerModal" class="modal-overlay" @click="showCustomerModal = false">
        <div class="modal-content" @click.stop style="height: 600px;">
            <div class="modal-header">
                <h3>Thêm khách hàng mới</h3>
                <button @click="showCustomerModal = false" class="close-modal-btn">
                    <X class="close-icon" />
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Tên khách hàng *</label>
                    <input v-model="newCustomer.name" type="text" class="form-input" required />
                </div>
                <div class="form-group">
                    <label>Số điện thoại *</label>
                    <input v-model="newCustomer.phone" type="tel" class="form-input" required />
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input v-model="newCustomer.email" type="email" class="form-input" />
                </div>
            </div>
            <div class="modal-footer">
                <button @click="showCustomerModal = false" class="cancel-btn">Hủy</button>
                <button @click="addCustomer" class="save-btn">Lưu</button>
            </div>
        </div>
    </div>
    <div v-if="showCustomerTable" class="modal-overlay" @click="showCustomerTable = false">
        <div class="modal-content" @click.stop style="height: 600px;">
            <div class="modal-close" style="display: flex; justify-content: flex-end;">
                <button @click="showCustomerTable = false" class="close-button">&times;</button>
            </div>

            <div class="modal-header" style="margin-bottom: 0;">
                <h2>{{ currentInvoiceDetail?.maHoaDon }} : Chọn khách hàng</h2> &nbsp;
                <div class="search-box">
                    <input v-model="searchKhachHang" type="text" placeholder="Tìm khách hàng" class="search-input" />
                </div>
            </div>

            <div class="modal-body" style="margin-bottom: 0; padding-bottom: 0;">
                <div class="custome-list">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Stt</th>
                                <th>Mã khách hàng</th>
                                <th>Số điện thoại</th>
                                <th>Tên khách hàng</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(khachHang, index) in khachHangs" :key="khachHang.id"
                                @click="selectedKhachHang(khachHang)" class="custome-item">
                                <td>{{ pageKhachHang * 5 + index + 1 }}</td>
                                <td>{{ khachHang.maKhachHang || "N/A" }}</td>
                                <td>{{ khachHang.sdt }}</td>
                                <td>{{ khachHang.tenKhachHang }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="pagination-controls" style="margin-top: 0;">
                <button @click="previousPage()" :disabled="pageKhachHang === 0">Trước</button>
                <span>Trang {{ pageKhachHang + 1 }} / {{ totalPages }}</span>
                <button @click="nextPageKH()" :disabled="pageKhachHang >= totalPages - 1">Sau</button>
            </div>

            <div class="modal-footer">
                <button @click="showCustomerTable = false, showCustomerModal = true" class="btn confirm-btn">Thêm khách
                    hàng mới</button>
                <button @click="showCustomerTable = false" class="btn cancel-btn">Hủy</button>
            </div>
        </div>
    </div>

    <div v-if="showSalesHistoryModal" class="history-overlay">
        <div class="history-modal">
            <h3 class="history-title">Lịch sử hóa đơn #{{ maHoaDon }}</h3>
            <div class="history-list">
                <div v-for="history in orderHistory" :key="history.idLichSuHoaDon" class="history-item">
                    <div class="history-action">{{ history.hanhDong }}</div>
                    <div class="history-time">{{ formatDate(history.thoiGianThayDoi) }}</div>
                    <div class="history-description">{{ history.moTa }}</div>
                </div>
                <div v-if="!orderHistory.length" class="no-history">
                    Không có lịch sử nào để hiển thị
                </div>
            </div>
            <div class="history-actions">
                <button class="history-btn cancel" @click="closeSalesHistoryModal">Đóng</button>
            </div>
        </div>
    </div>

    <transition name="voucher-fade">
    <div v-if="isVoucherModalOpen" class="voucher-overlay" @click.self="closeVoucherModal">
        <div class="voucher-modal">
        <div class="voucher-header">
            <h3 class="voucher-title">🎟️ Chọn phiếu giảm giá</h3>
            <button class="voucher-close" @click="closeVoucherModal">✕</button>
        </div>

        <div class="voucher-body">
            <p v-if="!discountList || discountList.length === 0" class="voucher-empty">
            Khách hàng không có phiếu giảm giá nào
            </p>

            <ul class="voucher-list">
            <li v-for="discount in discountList" :key="discount.id" class="voucher-item">
                <div class="voucher-info">
                <strong class="voucher-name">{{ discount.tenGiamGia }}</strong>
                <div class="voucher-details">
                    <span v-if="discount.loaiGiamGia === 'Phần trăm'">
                    Giảm: {{ discount.giaTriGiamGia }}% <br>
                    Tối đa: {{ formatCurrency(discount.giaTriGiamGiaToiDa) }}
                    </span>
                    <span v-else>
                    Giảm: {{ formatCurrency(discount.giaTriGiamGia) }}
                    </span>
                </div>
                </div>
                <button @click="applyDiscount(discount)" class="voucher-apply">
                Áp dụng
                </button>
            </li>
            </ul>
        </div>
        </div>
    </div>
    </transition>
</template>

<script setup>
import { ref, reactive, computed, onMounted, markRaw, watch, watchEffect, nextTick, onUnmounted } from 'vue'
import {
    Search, X, Plus, Lock, Undo, RotateCcw, Printer, Menu,
    User, Users, List, Filter, Eye, Edit, ChevronLeft, ChevronRight,
    ChevronUp, ChevronDown, Minus, Trash2, History, FileText,
    Smartphone, Laptop, Watch, Headphones, Camera, Gamepad2, ScanLine
} from 'lucide-vue-next'
import {
    loadSanPhamChiTiet, findSanPhamBanHang, loadCategory, findSanPhamByMa,
    fillDataFulldown
} from '@/Service/Adminservice/Products/ProductAdminService';
import {
    createPendingInvoice, hoaDonDetail, fetchImeisJs, updateTTShipping
    , getTinhThanh, getHuyen, getXa, getLatLon, getDistance, updateSoLuongAndTrangThai
    , loadImeiDaBan, deleteDetailInvoice, addProductIntoInvoice, loadHoaDonByIdNhanVien
    , getListKhachHang, addKhachHang, selectKhachHang, getAllPhieuGiamGia, phieuGiamGia, loadPaymentMethod, thanhToan
    , findHdctByImeiDaBan, findProductByImei,
    updateGia,
    getAllLichSuBanHang,
    viewLichSuHoaDon,
    deleteTTShipping,
    deletePhieuGiamGiaHoaDon
} from '@/Service/Adminservice/HoaDon/HoaDonAdminServices';
import { ca, da } from 'element-plus/es/locales.mjs';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { v4 as uuidv4 } from 'uuid';
import { useToast } from "vue-toastification";
import ConfirmModal from '@/views/Popup/ConfirmModal.vue';
import QrScanner from '@/views/Admin/Qr/QrScanner.vue';
import InvoicePrint from '@/views/Printf/InvoicePrint.vue';
import html2pdf from 'html2pdf.js';
import provinceData from '@/assets/JsonTinhThanh/province.json'
import wardData from '@/assets/JsonTinhThanh/ward.json'
import tienMatPng from '@/assets/HinhAnh/tienmat.png'
import chuyenKhoanPng from '@/assets/HinhAnh/chuyenkhoan.png'
import { nextDelay } from '@/Service/Adminservice/KhuyenMai/KhuyenMaiSanPhamService';
import ScreenLock from '../AdminLock/ScreenLock.vue';
import { deletePhieuGiamGia } from '@/Service/Adminservice/PhieuGiamGia/PhieuGiamGiaAdminService';

// Search queries
const productSearchQuery = ref('')
const customerSearchQuery = ref('')
const selectedCategory = ref('all')
const products = ref([])
const tenSanPham = ref('')
const pageNo = ref(0)
const pageSize = ref(5)
const pageNoProduct = ref(0)
const pageSizeProduct = ref(8)
const categoryProduct = ref([])
const totalPagesCategory = ref(1)
const totalPagesProdut = ref(1)
const currentCategory = ref({ tenSanPham: 'All' })
const tabHoaDon = ref([])
const invoices = ref([]) // hien thi tab
const currentInvoiceId = ref(null)
const route = useRoute();
const router = useRouter();
const currentInvoiceDetail = ref([]);
//ship
const isShipping = ref(false);
const tinhThanhList = ref([])
// const tinhList = ref([])
// const huyenList = ref([])
// const xaList = ref([])
const tinhList = ref(provinceData)
const allXaList = ref(Object.values(wardData))
const xaList = ref([])
const selectedTinh = ref('')
// const selectedHuyen = ref('')
const selectedXa = ref('')
const storeAddress = 'Hà Nội';

//thanhToan
const totalProductAmount = ref(0)
const shippingFee = ref(0)
const discountAmount = ref(0)
const grandTotal = ref(0)

//discount
const discountList = ref([])
const selectedDiscount = ref(null)
const search = ref('')

//search
const searchType = ref('');

watch(selectedDiscount, (newVal) => {
    console.log('PGG đã chọn:', newVal)
})

//toast message
const toast = useToast()

//payment
const paymentMethods = ref([]);
const selectedPaymentMethod = ref(null);
const agreedToTerms = ref(false);
//
const shippingInfo = ref({
    tenNguoiNhan: '',
    sdtNguoiNhan: '',
    emailNguoiNhan: '',
    diaChiChiTiet: '',
    phiShip: 0,
    shippingMethod: 'express', // Đảm bảo khớp với 'express' thay vì 'EXPRESS'
    maVanDon: '',
    isShipping: false
});


const calculateTotal = async () => {
    totalProductAmount.value = 0;
    if (currentInvoiceDetail.value?.chiTietHoaDonAdminResponseList?.length) {
        for (const hd of currentInvoiceDetail.value.chiTietHoaDonAdminResponseList) {
            totalProductAmount.value += hd.soLuong * hd.donGia;
        }
    }

    if (selectedDiscount.value?.loaiGiamGia === 'Phần trăm') {
        discountAmount.value = totalProductAmount.value * selectedDiscount.value.giaTriGiamGia / 100;
        console.log('jj', selectedDiscount.value)
        if (selectedDiscount.value?.giaTriGiamGiaToiDa < discountAmount.value) {
            
            discountAmount.value = selectedDiscount.value?.giaTriGiamGiaToiDa
        }
    } else if (selectedDiscount.value?.giaTriGiamGia) {
        discountAmount.value = selectedDiscount.value.giaTriGiamGia;
    } else {
        discountAmount.value = 0;
    }

    if (shippingInfo.value.phiShip) {
        shippingFee.value = shippingInfo.value.phiShip
    }

    if (isShipping.value) {
        grandTotal.value = totalProductAmount.value + 50000 - discountAmount.value

    }
    else {
        grandTotal.value = totalProductAmount.value - discountAmount.value
    }
}


const loadDiscountList = async () => {
    try {
        const response = await getAllPhieuGiamGia(search.value, currentInvoiceDetail.value.idKhachHang, totalProductAmount.value)
        discountList.value = response.data
    } catch (err) {
        console.error(err || "Lỗi lấy danh sách phiếu giảm giá");
    }
}

const apPhieuGiamGia = async (idPhieuGiamGia) => {
    const storedId = localStorage.getItem("selectedInvoiceId");
    try {
        await phieuGiamGia(storedId, { id: idPhieuGiamGia });
    } catch (error) {
        console.error('Lỗi khi áp phiếu giảm giá:', error);
    }
};

watch(
    () => currentInvoiceDetail.value?.chiTietHoaDonAdminResponseList,
    () => {
        calculateTotal()
        loadDiscountList()

    },
    { immediate: true, deep: true }
);

watch(
    () => selectedDiscount.value,
    () => {
        calculateTotal()
        if (selectedDiscount.value != null) {
            apPhieuGiamGia(selectedDiscount.value.id)
        }
    }
)

watch(
    () => shippingInfo.value.phiShip,
    () => {
        calculateTotal()
    }
)



// category san pham 
const danhMucSanPham = async () => {
    try {
        const response = await loadCategory(pageNo.value, pageSize.value)
        const danhMuc = response.data.content.map(ten => ({
            tenSanPham: ten,
            icon: markRaw(Smartphone)
        }))
        categoryProduct.value = danhMuc
        totalPagesCategory.value = response.data.totalPages
    } catch (error) {
        console.error('Lỗi khi load danh mục sản phẩm:', error)
    }
}

const selectedIdKhachHang = ref(0)
const pulldownData = ref(null)

const loadProducts = async () => {

    const res = await fillDataFulldown();
    pulldownData.value = res.data

    let response;
    if (selectedCategory.value.toLowerCase() === 'all') {
        response = await loadSanPhamChiTiet(pageNoProduct.value, pageSizeProduct.value, selectedIdKhachHang.value);
    } else {
        response = await findSanPhamBanHang(selectedCategory.value, pageNoProduct.value, pageSizeProduct.value, selectedIdKhachHang.value
            , filters.model, filters.priceMin, filters.priceMax, filters.quantityMin, filters.quantityMax, filters.maSpct, filters.storage, filters.color);
    }
    products.value = response.data.content;
    totalPagesProdut.value = response.data.totalPages

};


// Pagination
const itemsPerPage = 12

// load tab hoa don
const loadTabHoaDon = async () => {
    try {
        const response = await loadHoaDonByIdNhanVien();
        if (response.data.length == 0) {
            await addNewInvoice();
            loadTabHoaDon()
        }
        console.log('Danh sách hóa đơn từ backend:', response.data);
        //Load các hóa đơn của nhân viên
        // Cập nhật danh sách hóa đơn từ backend, tránh thêm trùng lặp
        invoices.value = []; // Xóa các hóa đơn hiện có
        response.data.forEach(inv => {
            const existingIndex = invoices.value.findIndex(i => i.id === inv.id);
            if (existingIndex === -1) {
                addOrUpdateInvoice(inv);
            }
        });
        tabHoaDon.value = [...invoices.value];

        const queryId = route.query.invoiceId;
        const storedId = localStorage.getItem("selectedInvoiceId");

        //     // Xác định finalId
        let finalId = null;

        // Ưu tiên storedId nếu nó hợp lệ
        if (storedId && invoices.value.some(i => i.id == storedId)) {
            finalId = storedId;
        } else if (queryId && invoices.value.some(i => i.id == queryId)) {
            finalId = queryId;
        } else if (invoices.value.length > 0) {
            finalId = invoices.value[0].id;
        }

        //Gán finalId chính xác duy nhất ở đây
        if (finalId) {
            currentInvoiceId.value = finalId;
            localStorage.setItem('selectedInvoiceId', finalId);

            // Nếu chưa có chi tiết → gọi thêm
            const selected = invoices.value.find(i => i.id == finalId);
            if (selected && !selected.chiTietHoaDonAdminResponseList) {
                const res = await hoaDonDetail(finalId);
                console.log('test', res.data)
                if (res.data) {
                    addOrUpdateInvoice(res.data);
                    currentInvoiceDetail.value = res.data;
                }
            } else {
                currentInvoiceDetail.value = selected || {};
            }
        }

        if (queryId) {
            router.replace({ query: { ...route.query, invoiceId: undefined } });
        }
    } catch (error) {
        console.error('Lỗi khi tải danh sách hóa đơn:', error);
    }
};

const listHdctByImeiDaBan = ref([]);
const pageNoHdctByImei = ref(0);
const pageSizeHdctByImei = ref(10);
const getHdctByImeiDaBan = async () => {
    const storedId = localStorage.getItem("selectedInvoiceId");
    const res = await findHdctByImeiDaBan(pageNoHdctByImei.value, pageSizeHdctByImei.value, storedId, selectedIdKhachHang.value);
    listHdctByImeiDaBan.value = res.data.content
    updateGiaChiTietDonHang()
}

const updateGiaChiTietDonHang = async () => {
    for (const hdct of listHdctByImeiDaBan.value) {
        try {
            await updateGia(hdct.idHoaDonChiTiet, hdct.giaBan);
        } catch (err) {
            console.error("Có lỗi khi cập nhật giá:", err);
        }
    }
}

const addOrUpdateInvoice = (invoice) => {
    const index = invoices.value.findIndex(i => i.id === invoice.id);
    if (index !== -1) {
        invoices.value[index] = { ...invoices.value[index], ...invoice };
    } else {
        // Tránh thêm nếu đã có 1 hóa đơn cùng maHoaDon
        if (!invoices.value.some(i => i.maHoaDon === invoice.maHoaDon)) {
            invoices.value.push(invoice);
        }
    }
};
let nextInvoiceId = 2

// Customer modal
const showCustomerTable = ref(false)
const showCustomerModal = ref(false)
const newCustomer = reactive({
    name: '',
    phone: '',
    email: '',
})

// Cart display
const showCartDetails = ref(true)
const selectedItems = ref([]);
const selectAllItems = ref(false);

// handle invoice tab logic 
const currentInvoice = computed(() => {
    const id = Number(currentInvoiceId.value);
    const invoice = invoices.value.find(inv => inv.id === id) || invoices.value[0];
    return invoice;
});

// ham chon hoa don tab
const selectInvoice = async (id) => {
    currentInvoiceId.value = id;
    localStorage.setItem('selectedInvoiceId', id);

    const invoice = invoices.value.find(i => i.id === id);
    const hasDetail = false

    if (!hasDetail) {
        try {
            const res = await hoaDonDetail(id);
            if (res.data) {
                addOrUpdateInvoice(res.data);
            }
            loadHoaDon()
            getHdctByImeiDaBan()
            toast.success("Chọn thành công hóa đơn " + res.data.maHoaDon)
        } catch (error) {
            console.error("Lỗi khi load chi tiết hóa đơn:", error);
        }
    }
};

const isLoading = ref(false)
const errorMessage = ref('')
//add pending invoice
const addNewInvoice = async () => {
    isLoading.value = true
    errorMessage.value = ''

    try {
        const response = await createPendingInvoice()
        const data = response.data
        console.log('Response data:', data)

        const newInvoice = {
            id: data.hoaDonId,
            maHoaDon: data.maHoaDon || `HD${data.hoaDonId}`,
            name: `Hóa đơn ${data.hoaDonId}`,
            status: data.status,
            lichSuHoaDonId: data.lichSuHoaDonId,
            items: [],
            customer: { name: '', phone: '', email: '', address: '' },
            notes: '',
            total: 0,
            chiTietHoaDonAdminResponseList: []
        }

        invoices.value.push(newInvoice)
        currentInvoiceId.value = newInvoice.id
        localStorage.setItem('selectedInvoiceId', newInvoice.id)
        loadHoaDon()
        toast.success('Tạo hóa đơn mới thành công!')

    } catch (error) {
        console.error('Error creating invoice:', error)
        console.log('Error response:', error.response)
        console.log('Error response data:', error.response?.data)

        if (error.response?.status === 400 && Array.isArray(error.response?.data)) {
            const messages = error.response.data
                .map(err => err.message)
                .filter(Boolean)
                .join(' | ')

            if (messages) {
                showWarningOnce(messages)
            }
        }
        else {
            toast.error('Đã xảy ra lỗi khi tạo hóa đơn mới!')
        }
    } finally {
        isLoading.value = false
    }
}

const closeInvoice = (id) => {
    if (invoices.value.length === 1) return;

    const index = invoices.value.findIndex(inv => inv.id === id);
    if (index !== -1) {
        invoices.value.splice(index, 1);
    }

    const storedId = localStorage.getItem("selectedInvoiceId");
    const idStr = String(id); // ép kiểu sang string

    if (storedId === idStr || currentInvoiceId.value === idStr) {
        const newId = invoices.value[0]?.id || null;
        currentInvoiceId.value = newId;
        localStorage.setItem("selectedInvoiceId", newId);
    }
};



const showDeleteConfirmModal = ref(false);
const itemToDelete = ref(null);
const showPartialDeleteModal = ref(false);
const quantityToRemove = ref(1); // số lượng mặc định muốn xóa
const maxQuantity = ref(0);

// mo modal xácd nhận muốn xóa
const confirmRemoveFromCart = (item) => {
    itemToDelete.value = item;
    showDeleteConfirmModal.value = true;
};

// đóng modal
const closeModal = () => {
    showDeleteConfirmModal.value = false;
    itemToDelete.value = null; // tra về nulk khi đóng modal
};
//xoas hoa đơn chi tiết
const removeFromCart = async () => {
    try {
        const res = await deleteDetailInvoice(itemToDelete.value.idHoaDonChiTiet);
        await loadTabHoaDon();
        showDeleteConfirmModal.value = false;
        await loadProducts();
        getHdctByImeiDaBan();
        clearDiscount()
        toast.success("Trả lại sản phẩm thành công !");

    } catch (err) {
        console.error("Xóa thất bại", err);
    }
}



watch([searchType, customerSearchQuery], () => {
    searchCustomer();
});

const clearCustomer = () => {
    currentInvoice.value.customer = { name: '', phone: '', email: '', address: '' }
    customerSearchQuery.value = ''
}

const addCustomer = () => {

    if (!newCustomer.name || !newCustomer.name.trim()) {
        toast.error("Vui lòng nhập tên khách hàng");
        return;
    }

    // validate số điện thoại
    if (!newCustomer.phone || !newCustomer.phone.trim()) {
        toast.error("Vui lòng nhập số điện thoại");
        return;
    }
    const phoneRegex = /^[0-9]{10}$/;
    if (!phoneRegex.test(newCustomer.phone)) {
        toast.error("Số điện thoại không đúng định dạng");
        return;
    }

    // validate email (nếu có nhập)
    if (newCustomer.email && newCustomer.email.trim()) {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9][a-zA-Z0-9.-]*[a-zA-Z0-9]\.com$/i;
        if (!emailRegex.test(newCustomer.email)) {
            toast.error("Email không đúng định dạng");
            return;
        }
    }

    currentInvoice.value.customer = { ...newCustomer }
    // customerSearchQuery.value = newCustomer.phone

    const data = {
        tenKhachHang: newCustomer.name,
        sdt: newCustomer.phone,
        email: newCustomer.email
    }

    add(data)

    // Reset form
    Object.assign(newCustomer, { name: '', phone: '', email: '', address: '' })
    showCustomerModal.value = false
}

// chuyen trang category
const nextPage = async () => {
    if (pageNo.value + 1 < totalPagesCategory.value) {
        pageNo.value++
        await danhMucSanPham()
    }

}

const prevPage = async () => {
    if (pageNo.value > 0) {
        pageNo.value--
        await danhMucSanPham()
    }

}
//chuyen trang product
const nextPageProduct = async (category) => {
    if (pageNoProduct.value + 1 < totalPagesProdut.value) {
        pageNoProduct.value++
        await loadProducts()
    }

}

const previousPageProduct = async (category) => {
    if (pageNoProduct.value > 0) {
        pageNoProduct.value--
        await loadProducts()
    }

}

//logic mo modal imei
const isImeiModalOpen = ref(false);
const selectedProductForImei = ref(null); // sản phẩm đng được chọn để mở imei
const availableImeis = ref([]); // danh sách imei hợp lệ cho sp đã chọn
const selectedImeis = ref([]); // danh sách imei mà nhân viên đẫ chọn trong modal
const imeiCurrentPage = ref(0);
const imeiPageSize = ref(9); // Số IMEI mỗi trang
const imeiTotalItems = ref(0); // Tổng số IMEI có sẵn
const imeiTotalPages = ref(0); // Tổng số trang IMEI
const quantityToSelect = ref(1); // lưu số lượng 

// Thêm biến để lưu trữ chuỗi tìm kiếm IMEI
const imeiSearchQuery = ref('');

// mở modal imei
const openImeiModal = async (product) => {
    if (product) {
        selectedProductForImei.value = product

        selectedImeis.value = []; // Reset các IMEI đã chọn trước đó
        imeiCurrentPage.value = 0; // Luôn bắt đầu từ trang 1
        quantityToSelect.value = 1; // <-- Reset về null khi mở modal mới để input trống

        // 1. Tải IMEI từ backend
        await fetchImeis(product.idSanPhamChiTiet, imeiCurrentPage.value, imeiPageSize.value);

        // 2. Mở modal sau khi tải xong (đảm bảo data đã có)
        isImeiModalOpen.value = true;

        // KHÔNG TỰ ĐỘNG GỌI autoSelectImeis Ở ĐÂY NỮA
        // Người dùng sẽ nhấn nút "Tự động chọn" sau khi nhập số lượng
    } else {
        toast.error("Không thể mở modal IMEI. Dữ liệu sản phẩm không hợp lệ.");
    }
};

// --- Hàm để lấy danh sách IMEI từ backend ---
const fetchImeis = async (productId, page, size) => {
    try {
        const response = await fetchImeisJs(productId, page, size, imeiSearchQuery.value.trim())

        let imeis = response.data.content;

        // Sắp xếp danh sách để các IMEI đã chọn xuất hiện đầu tiên
        imeis = imeis.sort((a, b) => {
            const isASelected = selectedImeis.value.some(selected => selected.id === a.id);
            const isBSelected = selectedImeis.value.some(selected => selected.id === b.id);
            if (isASelected && !isBSelected) return -1;
            if (!isASelected && isBSelected) return 1;
            return 0;
        });

        // imeis = imeis.sort((a, b) => {
        //     const isASelected = selectedImeis.value.some(selected => selected.id === a.id);
        //     const isBSelected = selectedImeis.value.some(selected => selected.id === b.id);

        //     // Nếu a được chọn và b không được chọn, a lên đầu
        //     if (isASelected && !isBSelected) return -1;
        //     // Nếu b được chọn và a không được chọn, b lên đầu
        //     if (!isASelected && isBSelected) return 1;
        //     // Nếu cả hai đều được chọn hoặc không được chọn, giữ nguyên thứ tự
        //     return 0;
        // });

        availableImeis.value = imeis;

        imeiTotalItems.value = response.data.totalElements;
        imeiTotalPages.value = response.data.totalPages;
    } catch (error) {
        console.error("Lỗi khi tải IMEI:", error);
        availableImeis.value = [];
        imeiTotalItems.value = 0;
        imeiTotalPages.value = 0;
    }
};

// Hàm xóa một IMEI khỏi danh sách đã chọn
const removeImei = (imei) => {
    selectedImeis.value = selectedImeis.value.filter(item => item.id !== imei.id);
};

// Cập nhật watch để reset tìm kiếm khi mở modal mới
watch(isImeiModalOpen, (newVal) => {
    if (newVal) {
        imeiSearchQuery.value = ''; // Reset chuỗi tìm kiếm khi mở modal
    }
});

watch(imeiSearchQuery, (newValue, oldValue) => {
    goToImeiPage(0);
});

// xử lý phân trang imei
const goToImeiPage = async (page) => {
    console.log('page: ', page);
    if (page >= 0 && page <= imeiTotalPages.value) {
        console.log('page: ', page);
        console.log('imeiTotalPages: ', imeiTotalPages.value);
        imeiCurrentPage.value = page;
        console.log('imeiCurrentPage: ', imeiCurrentPage.value);
        await fetchImeis(selectedProductForImei.value.idSanPhamChiTiet, imeiCurrentPage.value, imeiPageSize.value);
    }
};

let warningMessageInstance = null;

const showWarningOnce = (message) => {
    // k cho spam message 
    if (warningMessageInstance) return;

    warningMessageInstance = toast.error(message, {
        timeout: 3000,
        closeButton: true,
        onClose: () => {
            warningMessageInstance = null;
        },
    });
};

// Hàm xử lý khi input số lượng thay đổi
const handleQuantityInputChange = () => {
    if (quantityToSelect.value === null || isNaN(quantityToSelect.value)) {
        quantityToSelect.value = 0;
        return;
    }
    quantityToSelect.value = parseInt(quantityToSelect.value);
    if (quantityToSelect.value > imeiTotalItems.value) {
        quantityToSelect.value = imeiTotalItems.value;
        showWarningOnce(`Chỉ còn ${imeiTotalItems.value} IMEI có sẵn.`);
    }
    if (quantityToSelect.value <= 0) {
        quantityToSelect.value = 0;
        showWarningOnce(`Số lượng phải lớn hơn 0.`);
    }

    if (selectedImeis.value.length > quantityToSelect.value) {
        selectedImeis.value = selectedImeis.value.slice(0, quantityToSelect.value);
        showWarningOnce(`Đã điều chỉnh danh sách IMEI để phù hợp với số lượng ${quantityToSelect.value}.`);
    }
};

// chọn bỏ chọn imei bằng check box
const toggleImeiSelection = (imei) => {
    const index = selectedImeis.value.findIndex(item => item.id === imei.id);
    if (index > -1) {
        selectedImeis.value.splice(index, 1); // Bỏ chọn
    } else {
        if (selectedImeis.value.length < 1) { // Ví dụ: Giới hạn chỉ chọn 1 IMEI mỗi lần
            selectedImeis.value.push(imei); // Chọn
        } else {
            toast.error(`Bạn chỉ có thể chọn 1 IMEI cho sản phẩm này.`);
        }
    }
};

// kiểm tra xem imei có đnag dc chọn ko
const isImeiSelected = (imei) => {
    return selectedImeis.value.some(item => item.id === imei.id);
};

// tự động chọn imei
const autoSelectImeis = () => {
    const existingSelected = selectedImeis.value.filter(selected =>
        availableImeis.value.some(available => available.id === selected.id)
    );

    const remainingToSelect = quantityToSelect.value - existingSelected.length;

    if (remainingToSelect <= 0) {
        selectedImeis.value = existingSelected.slice(0, quantityToSelect.value);
        return;
    }

    const newSelections = availableImeis.value
        .filter(imei => !existingSelected.some(selected => selected.id === imei.id))
        .slice(0, remainingToSelect);

    selectedImeis.value = [...existingSelected, ...newSelections];

    // Lặp qua danh sách IMEI có sẵn trên trang hiện tại và chọn đủ số lượng
    // const countToSelectOnCurrentPage = Math.min(quantityToSelect.value, availableImeis.value.length);
    // for (let i = 0; i < countToSelectOnCurrentPage; i++) {
    //     selectedImeis.value.push(availableImeis.value[i]);
    // }

    // Ghi chú: Logic này chỉ tự động chọn trên trang hiện tại.
    // Nếu số lượng cần chọn lớn hơn số IMEI trên trang, bạn cần xử lý phức tạp hơn
    // (như tự động chuyển trang hoặc thông báo cho người dùng).
};

// dong modal imei
const closeImeiModal = () => {
    isImeiModalOpen.value = false;
    selectedImeis.value = [];
    selectedProductForImei.value = null;
    quantityToSelect.value = null; // Reset về null khi đóng
};

// dong modal Customer
const closeCustomerModal = () => {
    showCustomerModal.value = false;

};

// hàm xác nhận
const confirmImeiSelection = async () => {
    if (selectedImeis.value.length === quantityToSelect.value) {
        await addToCartWithImeis(selectedProductForImei.value, selectedImeis.value);
        closeImeiModal();
        await loadProducts({});
        getHdctByImeiDaBan();
        loadTabHoaDon()
    } else {
        toast.warning(`Bạn phải chọn chính xác ${quantityToSelect.value || 0} IMEI.`);
    }
};

// them vao gio hang
const addToCartWithImeis = async (product, imeiList) => {
    const storedId = localStorage.getItem("selectedInvoiceId");

    const data = {
        idHoaDon: storedId,
        idSanPhamChiTiet: product.idSanPhamChiTiet,
        tenSanPham: product.tenSanPham,
        // moTa: product.moTa,
        soLuong: imeiList.length,
        imeiIds: imeiList.map(imei => imei.imei),
        idKhachHang: selectedIdKhachHang.value
    };
    console.log(data)
    try {
        const response = await addProductIntoInvoice(storedId, data);
        await loadTabHoaDon(); // Tải lại dữ liệu hóa đơn sau khi thêm
        toast.success("Thêm sản phẩm thành công")
    } catch (err) {
        console.error("Thêm sản phẩm và IMEI thất bại:", err);
        toast.error("Thêm sản phẩm thất bại")
    }
    // calculateTotal()
}

const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN').format(amount)
}


//logic tra san pham
const showRemoveImeiModal = ref(false);
const itemToDeleteImei = ref(null);            // Sản phẩm đang được chọn để thao tác
const imeisForCurrentItem = ref([]);       // Danh sách IMEI của sản phẩm trong hóa đơn/giỏ hàng
const selectedImeisChon = ref([]);             // Các IMEI được chọn để xóa


// --- Hàm mở Modal (handleRemovePartial) ---
const handleRemovePartial = async (item) => {
    // Đảm bảo item tồn tại
    if (!item) return;
    console.log(item);
    itemToDeleteImei.value = item;
    imeisForCurrentItem.value = [];
    selectedImeisChon.value = [];

    try {
        const response = await loadImeiDaBan(item.idHoaDonChiTiet);

        const fetchedImeisData = response.data;

        if (Array.isArray(fetchedImeisData)) {
            imeisForCurrentItem.value = fetchedImeisData
                .map(imeiData => ({
                    imei: imeiData.soImei,
                    trangThaiImei: imeiData.trangThaiImei || 'Lỗi'
                }));
            console.log(imeisForCurrentItem.value)
        } else {
            console.warn('API did not return an array for IMEIs:', fetchedImeisData);
            imeisForCurrentItem.value = []; // Đảm bảo là mảng rỗng nếu dữ liệu không hợp lệ
        }

        // Hiển thị modal
        showRemoveImeiModal.value = true;

    } catch (error) {
        console.error('Lỗi khi tải IMEI từ API:', error);
        toast.error('Không thể tải danh sách IMEI. Vui lòng thử lại sau.');
        showRemoveImeiModal.value = false; // Đóng modal nếu có lỗi
    }
};

const handleRemoveSingleImei = async (item) => {
    if (!item) return;

    const storedId = localStorage.getItem("selectedInvoiceId");
    const hdctId = item.idHoaDonChiTiet;
    const imeiToReturn = [item.soImei]; // Trả đúng 1 imei duy nhất

    try {
        await updateSoLuongAndTrangThai(storedId, hdctId, imeiToReturn);
        toast.success(`Đã trả IMEI ${item.soImei} thành công.`);
        showDeleteConfirmModal.value = false
        await loadTabHoaDon(); // Load lại danh sách hóa đơn
        getHdctByImeiDaBan();
        loadProducts()
        clearDiscount()
    } catch (error) {
        console.error('Lỗi khi trả IMEI:', error);
        toast.error(`Lỗi khi trả sản phẩm: ${error.message}`);
    }
};
const confirmReturnSelected = async () => {
    if (selectedItems.value.length === 0) return;

    const storedId = localStorage.getItem("selectedInvoiceId");

    const imeisByHdct = {};

    for (const imeiId of selectedItems.value) {
        const imeiObj = listHdctByImeiDaBan.value.find(i => i.idImei === imeiId);
        if (!imeiObj) continue;

        const hdctId = imeiObj.idHoaDonChiTiet;
        const soImei = imeiObj.soImei;

        if (!imeisByHdct[hdctId]) {
            imeisByHdct[hdctId] = [];
        }

        imeisByHdct[hdctId].push(soImei);
    }

    try {
        for (const hdctId in imeisByHdct) {
            await updateSoLuongAndTrangThai(storedId, hdctId, imeisByHdct[hdctId]);
        }

        toast.success(`Đã trả ${selectedItems.value.length} sản phẩm.`);
        selectedItems.value = [];
        selectAllItems.value = false;
        await loadTabHoaDon();
        loadProducts()
        getHdctByImeiDaBan()
        clearDiscount()
    } catch (error) {
        toast.error(`Lỗi khi trả sản phẩm: ${error.message}`);
    }
};





// --- Hàm đóng Modal (closePartialModal) ---
const closePartialModal = () => {
    showRemoveImeiModal.value = false; // Sử dụng tên biến mới
    itemToDeleteImei.value = null;
    imeisForCurrentItem.value = [];
    selectedImeisChon.value = []; // Reset cả IMEI đã chọn
};

// --- Hàm xác nhận Modal (confirmPartialRemove) ---
const confirmPartialRemove = async () => {
    // ... (kiểm tra ban đầu)

    const storedId = localStorage.getItem("selectedInvoiceId");
    const hdctId = itemToDeleteImei.value.idHoaDonChiTiet;
    const imeisToReturn = selectedImeisChon.value; // Danh sách IMEI từ checkbox đã chọn

    try {
        // Gọi hàm service đã sửa đổi với tất cả các tham số cần thiết
        const message = await updateSoLuongAndTrangThai(storedId, hdctId, imeisToReturn);

        toast.success('Cập nhật thành công'); // Hiển thị thông báo thành công

        // ... (cập nhật UI sau khi thành công)
        closePartialModal();
        await loadTabHoaDon()
    } catch (error) {
        console.error('Lỗi khi xác nhận loại bỏ IMEI:', error);
        toast.error(`Đã xảy ra lỗi: ${error.message}`);
    }
};


// --- Computed property cho "Chọn tất cả" ---
const selectAllImeis = computed({
    get() {
        return selectedImeisChon.value.length === imeisForCurrentItem.value.length && imeisForCurrentItem.value.length > 0;
    },
    set(value) {
        if (value) {
            selectedImeisChon.value = imeisForCurrentItem.value.map(item => item.imei);
        } else {
            selectedImeisChon.value = [];
        }
    }
});


// change status vie css
const getImeiStatusClass = (status) => {
    if (!status) return "status-default";

    const statusMap = {
        'Có sẵn': 'imei-status-co-san',
        "Đã đặt trước": 'imei-status-da-dat-truoc',
        'Đã bán': 'imei-status-da-ban',
        'Đã trả lại': 'imei-status-da-tra-lai',
        'Tân trang': 'imei-status-tan-trang',
        'Bị chặn': 'imei-status-bi-chan',
    };
    // **Bỏ .toLowerCase() ở đây**
    return statusMap[status] || "status-default";
};
console.log("tra ve", getImeiStatusClass("Đã đặt trước"))

// xu ly giao hang
// 593

const getTinhList = async () => {
    try {
        // const res = await getTinhThanh();
        tinhList.value = provinceData
        console.log("Danh sách Tỉnh/Thành phố:", tinhList.value);
    } catch (error) {
        console.error("Lỗi khi lấy danh sách tỉnh:", error);
    }
}

const onTinhChange = async () => {
    // selectedHuyen.value = null;
    selectedXa.value = null;
    // huyenList.value = [];
    xaList.value = [];
    if (selectedTinh.value?.name) {
        try {
            // const res = await getHuyen(selectedTinh.value.code);
            // const data = res.data;
            // huyenList.value = data.districts || [];
            // console.log("Danh sách Huyện/Quận:", huyenList.value);
            xaList.value = allXaList.value.filter(ward =>
                ward.path.includes(selectedTinh.value.name)
            )
            console.log("Danh sách Phường/Xã:", xaList.value)
        } catch (error) {
            console.error("Lỗi khi lấy danh sách huyện:", error);
        }
    }
    // updatePhiShip();
};

// Xử lý khi Quận/Huyện thay đổi
// const onHuyenChange = async () => {
//     selectedXa.value = null;
//     xaList.value = [];
//     if (selectedHuyen.value?.code) {
//         try {
//             const res = await getXa(selectedHuyen.value.code);
//             const data = res.data;
//             xaList.value = data.wards || [];
//             console.log("Danh sách Phường/Xã:", xaList.value);
//         } catch (error) {
//             console.error("Lỗi khi lấy danh sách xã:", error);
//         }
//     }
//     // updatePhiShip();
// };

const onXaChange = async () => {
    // console.log("Xã được chọn:", selectedXa.value);
    // shippingInfo.value.phiShip = null;
    // if (selectedXa.value) {
    //     console.log('Calculating shipping fee after selecting ward:', selectedXa.value.name);
    //     await updatePhiShip();
    // }
};

watch(isShipping, async (newVal) => {
    if (!newVal) {
        console.log('Tắt giao hàng, xóa thông tin giao hàng');
        shippingInfo.value = {
            tenNguoiNhan: '',
            sdtNguoiNhan: '',
            emailNguoiNhan: '',
            diaChiChiTiet: '',
            phiShip: null,
            shippingMethod: 'express'
        };
        selectedTinh.value = null;
        selectedXa.value = null;
        xaList.value = [];
        const storedId = localStorage.getItem("selectedInvoiceId");
        const fullAddressForDB = ''
        await deleteTTShipping(storedId, shippingInfo.value, fullAddressForDB, isShipping.value)
        calculateTotal()
    } else {
        console.log('Đã bật giao hàng');
    }
});

// ham update phi ship 
const updatePhiShip = async () => {
    if (!isShipping.value ||
        shippingInfo.value.shippingMethod !== 'express' ||
        !selectedTinh.value?.name ||
        // !selectedHuyen.value?.name ||
        !selectedXa.value?.name
    ) {
        console.warn("Điều kiện không thỏa mãn:");
        if (!isShipping.value) console.warn("  - isShipping là false");
        if (shippingInfo.value.shippingMethod !== 'express') console.warn("  - shippingMethod không phải 'express'");
        if (!selectedTinh.value?.name) console.warn("  - selectedTinh.name rỗng");
        // if (!selectedHuyen.value?.name) console.warn("  - selectedHuyen.name rỗng");
        if (!selectedXa.value?.name) console.warn("  - selectedXa.name rỗng");
        shippingInfo.value.phiShip = null;
        console.warn("Chưa đủ thông tin địa chỉ hoặc giao hàng không được bật/chọn phương thức express. Phí ship: null.");
        return;
    }

    const fullAddress = `${selectedTinh.value.name}`;

    console.log("Địa chỉ người nhận đầy đủ (fullAddress):", fullAddress);
    console.log("Địa chỉ cửa hàng (storeAddress):", storeAddress);

    try {
        const [from, to] = await Promise.all([
            getLatLonFromAddress(storeAddress, selectedTinh, selectedXa),
            getLatLonFromAddress(fullAddress, selectedTinh, selectedXa),
        ]);

        console.log("Tọa độ cửa hàng (from):", from);
        console.log("Tọa độ người nhận (to):", to);

        if (!from || !to) {
            shippingInfo.value.phiShip = 30000; // Phí mặc định
            console.warn("Không tìm thấy tọa độ cho ít nhất một trong hai địa chỉ. Áp dụng phí mặc định: 30,000 VNĐ.");
            return;
        }

        const distance = await getDistanceInKm(from, to);
        console.log("Khoảng cách tính được:", distance);

        // const maxDistance = 50;
        // let adjustedDistance = distance;
        // if (distance > maxDistance) {
        //     console.warn(`Khoảng cách quá lớn (${distance} km), giới hạn về ${maxDistance} km.`);
        //     adjustedDistance = maxDistance;
        // }

        shippingInfo.value.phiShip = calcPhiShip(distance);
        console.log(
            `Khoảng cách: ${distance} km, Phí ship: ${shippingInfo.value.phiShip.toLocaleString('vi-VN')} VNĐ`
        );
    } catch (err) {
        console.error("Lỗi khi tính phí ship:", err);
        shippingInfo.value.phiShip = 30000; // Phí mặc định
        console.log("Áp dụng phí mặc định do lỗi: 30,000 VNĐ");
    }
};


// Hàm lấy tọa độ từ địa chỉ
const getLatLonFromAddress = async (address, selectedTinh, selectedXa) => {
    console.log("Đang gọi API lấy tọa độ cho:", address);
    try {
        // Thử địa chỉ gốc
        const res = await getLatLon(address);
        const parsedData = res.data;
        console.log("Phản hồi từ API /geo (địa chỉ gốc):", parsedData);

        if (Array.isArray(parsedData) && parsedData.length > 0 && parsedData[0].lat && parsedData[0].lon) {
            const coords = {
                lat: parseFloat(parsedData[0].lat),
                lon: parseFloat(parsedData[0].lon),
            };
            console.log(`Tọa độ trả về cho ${address}:`, coords);
            return coords;
        }

        // Thử định dạng thay thế (xóa dấu phẩy thừa)
        const cleanAddress = address.replace(/^,\s*/, '').trim();
        console.log("Thử định dạng thay thế:", cleanAddress);
        const altRes = await getLatLon(cleanAddress);
        const altParsedData = altRes.data;
        console.log("Phản hồi từ API /geo (thay thế):", altParsedData);

        if (Array.isArray(altParsedData) && altParsedData.length > 0 && altParsedData[0].lat && altParsedData[0].lon) {
            const coords = {
                lat: parseFloat(altParsedData[0].lat),
                lon: parseFloat(altParsedData[0].lon),
            };
            console.log(`Tọa độ trả về cho ${cleanAddress}:`, coords);
            return coords;
        }

        // Thử cấp huyện
        if (selectedTinh?.value?.name) {
            const huyenAddress = `${selectedTinh.value.name}, Việt Nam`;
            console.log("Thử tìm theo huyện:", huyenAddress);
            const huyenRes = await getLatLon(huyenAddress);
            const huyenParsedData = huyenRes.data;
            console.log("Phản hồi từ API /geo (huyện):", huyenParsedData);

            if (Array.isArray(huyenParsedData) && huyenParsedData.length > 0 && huyenParsedData[0].lat && huyenParsedData[0].lon) {
                const coords = {
                    lat: parseFloat(huyenParsedData[0].lat),
                    lon: parseFloat(huyenParsedData[0].lon),
                };
                console.log(`Tọa độ trả về cho ${huyenAddress}:`, coords);
                return coords;
            }
        } else {
            console.warn("Không thể thử cấp huyện do thiếu selectedTinh:", {
                selectedTinh: selectedTinh?.value,
            });
        }

        // Thử cấp tỉnh
        if (selectedTinh?.value?.name) {
            const tinhAddress = `${selectedTinh.value.name}, Việt Nam`;
            console.log("Thử tìm theo tỉnh:", tinhAddress);
            const tinhRes = await getLatLon(tinhAddress);
            const tinhParsedData = tinhRes.data;
            console.log("Phản hồi từ API /geo (tỉnh):", tinhParsedData);

            if (Array.isArray(tinhParsedData) && tinhParsedData.length > 0 && tinhParsedData[0].lat && tinhParsedData[0].lon) {
                const coords = {
                    lat: parseFloat(tinhParsedData[0].lat),
                    lon: parseFloat(tinhParsedData[0].lon),
                };
                console.log(`Tọa độ trả về cho ${tinhAddress}:`, coords);
                return coords;
            }
        } else {
            console.warn("Không thể thử cấp tỉnh do thiếu selectedTinh:", selectedTinh?.value);
        }

        console.warn("Không tìm thấy tọa độ hợp lệ cho địa chỉ:", address, parsedData);
        return null;
    } catch (error) {
        console.error("Lỗi khi lấy tọa độ cho địa chỉ:", address, error);
        return null;
    }
};
// Hàm tính khoảng cách giữa 2 tọa độ (đã có sẵn)
const getDistanceInKm = async (from, to) => {
    try {
        const res = await getDistance(from, to);
        const parsedData = res.data;
        const distanceInMeters = parsedData?.routes?.[0]?.distance;

        if (distanceInMeters != null) {
            return Math.ceil(distanceInMeters / 1000);
        }

        console.warn('Không lấy được khoảng cách giữa 2 điểm (kiểm tra parsedData):', parsedData);
        return 0;

    } catch (error) {
        console.error('Lỗi khi tính khoảng cách giữa 2 điểm:', error);
        return 0;
    }
};

const calcPhiShip = (km) => {
    const baseFee = 15000;
    const additionalFeePerKm = 2000;

    if (km <= 2) return baseFee;

    const calculatedFee = baseFee + (km - 2) * additionalFeePerKm;

    return calculatedFee;
};

// load thong tin hoa don
const hoaDonList = ref(null);
const loadHoaDon = async () => {
    const storedId = localStorage.getItem("selectedInvoiceId");
    if (!storedId) {
        console.warn("Không tìm thấy ID hóa đơn trong localStorage. Không thể tải chi tiết hóa đơn.");
        return;
    }
    try {
        const response = await hoaDonDetail(storedId);
        const hoaDon = response.data;
        console.log("hoaDon: ", hoaDon);

        if (hoaDon) {
            hoaDonList.value = hoaDon;
            console.log("discountList: ", discountList);
            if (hoaDon && hoaDon.idPhieuGiamGia) {
                const selected = discountList.value.find(
                    discount => discount.id === hoaDon.idPhieuGiamGia
                );
                selectedDiscount.value = selected || '';
            } else {
                selectedDiscount.value = null;
            }
            if (hoaDon.isShipping) {
                isShipping.value = true;
                shippingInfo.value = {
                    tenNguoiNhan: hoaDon.tenNguoiNhan || '',
                    sdtNguoiNhan: hoaDon.sdtNguoiNhan || '',
                    emailNguoiNhan: hoaDon.emailNguoiNhan || '',
                    diaChiChiTiet: hoaDon.diaChiGiaoHang || '',
                    phiShip: hoaDon.phiShip || null,
                    shippingMethod: 'express'
                };

                if (tinhList.value.length === 0) {
                    await getTinhList();
                }
                console.log("Danh sách tỉnh đã có:", tinhList.value);

                if (hoaDon.maTinh) {
                    const foundTinh = tinhList.value.find(tinh => tinh.code === hoaDon.maTinh);
                    if (foundTinh) {
                        selectedTinh.value = foundTinh;
                        console.log("Đã gán tỉnh từ DB:", selectedTinh.value);
                        await onTinhChange();
                    } else {
                        console.warn("Không tìm thấy tỉnh với mã:", hoaDon.maTinh);
                    }
                } else {
                    console.log("Không có mã tỉnh trong hóa đơn.");
                }
                // if (hoaDon.maHuyen) {
                //     await new Promise(resolve => setTimeout(resolve, 50));
                //     if (huyenList.value.length > 0) {
                //         const foundHuyen = huyenList.value.find(huyen => huyen.code === hoaDon.maHuyen);
                //         if (foundHuyen) {
                //             selectedHuyen.value = foundHuyen;
                //             console.log("Đã gán huyện từ DB:", selectedHuyen.value);
                //             await onHuyenChange();
                //         } else {
                //             console.warn("Không tìm thấy huyện với mã:", hoaDon.maHuyen);
                //         }
                //     } else {
                //         console.warn("Danh sách huyện trống, không thể gán huyện từ DB.");
                //     }
                // } else {
                //     console.log("Không có mã huyện trong hóa đơn.");
                // }

                if (hoaDon.maXa) {
                    await new Promise(resolve => setTimeout(resolve, 50));
                    if (xaList.value.length > 0) {
                        const foundXa = xaList.value.find(xa => xa.code === hoaDon.maXa);
                        if (foundXa) {
                            selectedXa.value = foundXa;
                            console.log("Đã gán xã từ DB:", selectedXa.value);
                        } else {
                            console.warn("Không tìm thấy xã với mã:", hoaDon.maXa);
                        }
                    } else {
                        console.warn("Danh sách xã trống, không thể gán xã từ DB.");
                    }
                } else {
                    console.log("Không có mã xã trong hóa đơn.");
                }


            } else {
                isShipping.value = false;
                shippingInfo.value = {
                    tenNguoiNhan: '',
                    sdtNguoiNhan: '',
                    diaChiChiTiet: '',
                    phiShip: null,
                    shippingMethod: 'express'
                };
                selectedTinh.value = null;
                // selectedHuyen.value = null;
                selectedXa.value = null;
                // huyenList.value = [];
                xaList.value = [];
            }
        }

    } catch (error) {
        console.error("Lỗi khi tải thông tin hóa đơn chi tiết:", error);
    }
};
watch(currentInvoiceId, async (newId) => {
    if (!newId) return;

    const selected = invoices.value.find(i => i.id == newId);
    // Nếu chưa có chi tiết thì mới gọi
    if (!selected?.chiTietHoaDonAdminResponseList) {
        try {
            const res = await hoaDonDetail(newId);
            console.log("Reload chi tiết khi tab change:", res.data);
            if (res.data) {
                addOrUpdateInvoice(res.data);
                currentInvoiceDetail.value = res.data; // Gán lại chi tiết mới
                selectedIdKhachHang.value = res.data.idKhachHang
            }
        } catch (e) {
            console.error("Lỗi khi load detail khi tab change:", e);
        }
    } else {
        currentInvoiceDetail.value = selected;
    }
});

watch(quantityToSelect, (newValue, oldValue) => {
    if (newValue !== oldValue) {
        selectedImeis.value = [];
    }
});

let statusUpdateInterval = null;

const getNextUpdateDelay = async () => {
    try {
        const response = await nextDelay()
        const delay = Math.max(1000, response.delay || 60_000);
        console.log('1', delay)
        return delay;
    } catch (error) {
        if (error.name !== 'AbortError') {
            console.error('Không thể lấy thời gian cập nhật:', error);
            toast.error('Không thể lấy thời gian cập nhật');
        }
        return 60_000;
    }
};
// Initialize
onMounted(async () => {
    await danhMucSanPham();
    selectedCategory.value = 'all';
    await loadProducts({});
    await loadTabHoaDon();
    await loadHoaDon();
    await getTinhList();
    await loadDiscountList();
    fetchPaymentMethods();
    await getHdctByImeiDaBan();
    const initialDelay = await getNextUpdateDelay();
    statusUpdateInterval = setInterval(() => {
        loadProducts({});
        loadTabHoaDon();
        getHdctByImeiDaBan()
    }, initialDelay);
});

onUnmounted(() => {
    if (statusUpdateInterval) {
        clearInterval(statusUpdateInterval);
        statusUpdateInterval = null;
    }
});

//Khach hang
const khachHangs = ref([])
const pageKhachHang = ref(0)
const totalPages = ref(0)
const searchKhachHang = ref(null)


const listKhachHang = async (page) => {
    showCustomerTable.value = true
    try {
        const response = await getListKhachHang(searchKhachHang.value, page, 5)
        khachHangs.value = response.data.content;
        totalPages.value = response.data.totalPages;
        pageKhachHang.value = page;
    } catch (err) {
        console.error(err || "Lỗi lấy danh sách khách hàng");
    }
};

const nextPageKH = () => {
    if (pageKhachHang.value < totalPages.value - 1) {
        listKhachHang(pageKhachHang.value + 1);
    }
};

const previousPage = () => {
    if (pageKhachHang.value > 0) {
        listKhachHang(pageKhachHang.value - 1);
    }
};

const selectedKhachHang = async (khachHang) => {
    const storedId = localStorage.getItem("selectedInvoiceId");
    if (!storedId) {
        console.warn("Không tìm thấy ID hóa đơn trong localStorage.");
        return;
    }
    const selected = {
        khachHangId: khachHang.id
    }
    try {
        await selectKhachHang(storedId, selected);

        const response = await hoaDonDetail(storedId);
        if (response?.data) {
            addOrUpdateInvoice(response.data);
            currentInvoiceDetail.value = response.data;
        }
        if (selected.khachHangId == undefined || selected.khachHangId == null) {
            selectedIdKhachHang.value = 0
            toast.success("Bỏ chọn khách hàng thành công")
        } else {
            selectedIdKhachHang.value = khachHang.id
            // toast.success("Chọn khách hàng thành công id: " + khachHang.id)
            toast.success("Chọn khách hàng thành công");
        }
    } catch (err) {
        toast.error("Thêm khách hàng vào hóa đơn thất bại")
        console.error("Thêm khách hàng vào hóa đơn thất bại:", err);
    }
    showCustomerTable.value = false;
}

const add = async (data) => {
    try {
        const response = await addKhachHang(data);
        console.log(response.data)
        selectedKhachHang(response.data)
    } catch (err) {
        toast.error(err.response.data.message || "Thêm khách hàng thất bại");
        console.error("Thêm khách hàng thất bại:", err);
    }
    showCustomerModal.value = false;
}

watch(searchKhachHang, () => {
    listKhachHang(0);
});

watch(selectedIdKhachHang, () => {
    loadProducts();
    getHdctByImeiDaBan();
});


// popup cap nhat thong tin giao hang
const showShippingPopup = ref(false);
const openShippingPopup = () => {
    showShippingPopup.value = true;
    shippingInfo.value.diaChiChiTiet = '';
    const haNoi = Object.values(tinhList.value).flat().find(t => t.name === "Hà Nội")
    if (haNoi) {
        selectedTinh.value = haNoi
    }
    selectedXa.value = null;
    // huyenList.value = [];
    xaList.value = [];
    if (selectedTinh.value?.name) {
        try {
            // const res = await getHuyen(selectedTinh.value.code);
            // const data = res.data;
            // huyenList.value = data.districts || [];
            // console.log("Danh sách Huyện/Quận:", huyenList.value);
            xaList.value = allXaList.value.filter(ward =>
                ward.path.includes(selectedTinh.value.name)
            )
            console.log("Danh sách Phường/Xã:", xaList.value)
        } catch (error) {
            console.error("Lỗi khi lấy danh sách huyện:", error);
        }
    }
};

const closeShippingPopup = () => {
    showShippingPopup.value = false;
    shippingInfo.value.tenNguoiNhan = ''
    shippingInfo.value.sdtNguoiNhan = ''
    shippingInfo.value.emailNguoiNhan = ''
    shippingInfo.value.diaChiChiTiet = ''
    shippingInfo.value.tinh
};

const showMessageConfirmShipping = ref(null)
const confirmShippingInfoPopup = () => {
    showMessageConfirmShipping.value = true
}
const confirmShippingInfo = async () => {

    const storedId = localStorage.getItem("selectedInvoiceId");

    // Kiểm tra dữ liệu bắt buộc
    if (!selectedTinh.value?.name || !selectedXa.value?.name) {
        toast.warning('Vui lòng chọn đầy đủ Tỉnh, Huyện, Xã.');
        return;
    }

    if (!shippingInfo.value.tenNguoiNhan || !shippingInfo.value.sdtNguoiNhan || !shippingInfo.value.emailNguoiNhan) {
        toast.warning('Vui lòng nhập đầy đủ Tên người nhận, Số điện thoại và Email người nhận.');
        return;
    }

    if (!storedId) {
        toast.error('Không tìm thấy ID hóa đơn.');
        return;
    }

    // Tạo địa chỉ đầy đủ cho DB
    const fullAddressForDB = shippingInfo.value.diaChiChiTiet
        ? `${shippingInfo.value.diaChiChiTiet}, ${selectedXa.value.name}, ${selectedTinh.value.name}`
        : `${selectedXa.value.name}, ${selectedTinh.value.name}`;

    try {
        const response = await updateTTShipping(storedId, shippingInfo.value, fullAddressForDB, isShipping.value)

        shippingInfo.value.diaChiChiTiet = fullAddressForDB;
        console.log('Phản hồi từ API /update-invoice:', response.data);
        calculateTotal()
        toast.success('Cập nhật thông tin giao hàng thành công!');

        showMessageConfirmShipping.value = false;
        showShippingPopup.value = false;
    } catch (error) {
        console.error('Lỗi khi cập nhật hóa đơn:', error);
        const status = error.response?.status;
        const data = error.response?.data;

        if (status === 401) {
            toast.error('Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.');
            return;
        }

        if (data?.errors && typeof data.errors === 'object') {
            Object.values(data.errors).forEach(msg => {
                toast.warning(msg);
            });
            return;
        }

        if (data?.message) {
            toast.error(data.message);
            return;
        }

        if (status === 500) {
            toast.error('Lỗi server: ' + (data?.message || 'Không thể cập nhật hóa đơn.'));
        } else {
            toast.error('Có lỗi xảy ra khi cập nhật thông tin giao hàng: ' + error.message);
        }
    }
};

const onCancelCreate = () => {
    showMessageConfirmShipping.value = false;
}

const toggleShipping = () => {
    if (!isShipping.value) {
        console.log('Tắt giao hàng, xóa thông tin giao hàng'); shippingInfo.value = { tenNguoiNhan: '', sdtNguoiNhan: '', diaChiChiTiet: '', phiShip: 0, shippingMethod: 'express' };
        selectedTinh.value = null;
        selectedXa.value = null;
        xaList.value = [];
    } else {
        console.log('Đã bật giao hàng');
        calculateTotal()
    }
};

// Hàm để lấy danh sách phương thức thanh toán từ API
const fetchPaymentMethods = async () => {
    try {
        const response = await loadPaymentMethod();
        paymentMethods.value = response.data;
        if (paymentMethods.value.length > 0) {
            selectedPaymentMethod.value = paymentMethods.value[0].code;
        }
    } catch (error) {
        console.error('Lỗi khi tải phương thức thanh toán:', error);
        toast.error('Không thể tải các phương thức thanh toán. Vui lòng thử lại sau.');
    }
};

const getIconUrl = (code) => {
    switch (code) {
        case 'TIEN_MAT':
            return tienMatPng;
        case 'CHUYEN_KHOAN':
            return chuyenKhoanPng;
        default:
            return '/icons/default.png';
    }
};

const showInvoice = ref(false);
const selectedInvoiceId = ref(localStorage.getItem("selectedInvoiceId") || null);
const invoiceRef = ref(null);
// Hàm xử lý thanh toán

const isLoadingPayment = ref(false)
const processPayment = async () => {
    if (isLoadingPayment.value) return; // tránh double click
    isLoadingPayment.value = true;

    const storedId = localStorage.getItem("selectedInvoiceId");

    if (!selectedPaymentMethod.value) {
        toast.warning('Vui lòng chọn phương thức thanh toán.');
        isLoadingPayment.value = false;
        return;
    }
    if (!agreedToTerms.value) {
        toast.warning('Bạn phải đồng ý với Điều khoản và Điều kiện để tiếp tục.');
        isLoadingPayment.value = false;
        return;
    }
    if (currentInvoiceDetail.value.chiTietHoaDonAdminResponseList?.length === 0) {
        toast.error('Hóa đơn không có sản phẩm nào để thanh toán.');
        isLoadingPayment.value = false;
        return;
    }

    const paymentPayload = {
        hinhThucThanhToan: selectedPaymentMethod.value,
        soTienKhachDua: grandTotal.value,
        soTienGiam: discountAmount.value
    };

    try {
        const response = await thanhToan(storedId, paymentPayload);
        if (response.data && response.data.message === "Thanh toán thành công") {
            toast.success('Thanh toán thành công!');
            selectedInvoiceId.value = storedId;
            showInvoice.value = true;
            await nextTick();
            setTimeout(() => {
                printInvoice();
            }, 1200);
            setTimeout(() => {
                window.location.reload();
            }, 3200);
        } else {
            toast.error('Thanh toán không thành công: ' + (response.data?.message || 'Lỗi không xác định.'));
        }
    } catch (error) {
        console.error('Lỗi khi xử lý thanh toán:', error);
        if (error.response?.data?.message) {
            toast.error('Lỗi thanh toán: ' + error.response.data.message);
        } else {
            toast.error('Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại.');
        }
    } finally {
        isLoadingPayment.value = false;
    }
};

const printInvoice = () => {
    if (invoiceRef.value) {
        const element = invoiceRef.value.$el;
        const opt = {
            margin: 1,
            filename: `hoa_don_${selectedInvoiceId.value || 'unknown'}.pdf`,
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: { scale: 2 },
            jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
        };
        html2pdf().set(opt).from(element).toPdf().get('pdf').then((pdf) => {
            pdf.autoPrint();
            window.open(pdf.output('bloburl'), '_blank');
        }).catch((error) => {
            console.error('Lỗi khi tạo PDF:', error);
        });
    } else {
        console.error('invoiceRef không được định nghĩa hoặc component chưa được render.');
    }
};

const handleClose = () => {
    showInvoice.value = false;
};

const formatTrangThaiThanhToan = (trangThai) => {
    const mapping = {
        PENDING: 'Chờ thanh toán',
        CONFIRMED: 'Đã xác nhận',
        PAID: 'Đã thanh toán',
        CANCELLED: 'Đã hủy',
        REFUNDED: 'Đã hoàn tiền',
        COMPLETED: 'Hoàn tất',
    };
    return mapping[trangThai] || 'Không rõ';
};

const formatTrangThai = (trangThai) => {
    const mapping = {
        AVAILABLE: 'Có sẵn',
        RESERVED: 'Đã đặt trước',
        SOLD: 'Đã bán',
        RETURNED: 'Đã trả lại',
        REFURBISHED: 'Tân trang',
        BLACKLISTED: 'Bị chặn',
    };
    return mapping[trangThai] || 'Không rõ';
};



const getStatusClass = (trangThai) => {
    const classMap = {
        AVAILABLE: 'text-green-600',
        RESERVED: 'text-blue-500',
        SOLD: 'text-red-600',
        RETURNED: 'text-yellow-500',
        REFURBISHED: 'text-indigo-600',
        BLACKLISTED: 'text-gray-500 line-through',
    };
    return classMap[trangThai] || 'text-gray-400';
};

const toggleSelectAll = () => {
    if (selectAllItems.value) {
        selectedItems.value = listHdctByImeiDaBan.value.map(item => item.idImei);
    } else {
        selectedItems.value = [];
    }
};

watch(selectedItems, (newVal) => {
    selectAllItems.value = newVal.length === listHdctByImeiDaBan.value.length;
});

const showQRModal = ref(false)
const openQRModal = () => {
    showQRModal.value = true
}
const onProductScanned = (maSanPham) => {
    console.log("Mã sản phẩm quét được:", maSanPham)
    // Gọi API hoặc thêm vào giỏ hàng tại đây
}
const onCloseModal = () => {
    console.log('Đã đóng modal quét QR')
}

const onScannedImei = async (soImei) => {
    const product = await findProductByImei(soImei)
    console.log(product.data)
    console.log(product.data.tenSanPham)
    if (product) {
        await addToCartWithImeis(
            {
                idSanPhamChiTiet: product.data.idSanPhamChiTiet,
                tenSanPham: product.data.tenSanPham
            },
            [{ imei: product.data.soImei }]
        )
        getHdctByImeiDaBan()
        toast.success("Đã thêm sản phẩm từ IMEI: " + product.data.soImei)
        // Nếu bạn muốn đóng modal sau khi quét xong:
        showQRModal.value = false
    }
}
const reloadPage = () => {
    loadTabHoaDon()
    toast.success("Làm mới tab hóa đơn thành công")
}

const screenLock = ref(null)

function lockScreen() {
    screenLock.value.lock()
}

const isVoucherModalOpen = ref(false);

const openVoucherModal = () => {
    isVoucherModalOpen.value = true;
};

const closeVoucherModal = () => {
    isVoucherModalOpen.value = false;
};

const applyDiscount = (discount) => {
    console.log('ll', discount)
    selectedDiscount.value = discount;
    isVoucherModalOpen.value = false;
};

const clearDiscount = async ()  => {
    const storedId = localStorage.getItem("selectedInvoiceId");
    await deletePhieuGiamGiaHoaDon(storedId)
    selectedDiscount.value = null;
    toast.success("Hủy áp dụng phiếu giảm giá thành công")
};

// Biến cho modal lịch sử bán hàng
const orderHistory = ref([]);
const showSalesHistoryModal = ref(false);
const maHoaDon = ref();

const openSalesHistoryModal = async () => {
    showSalesHistoryModal.value = true;
    await xemLichSu();
};

const closeSalesHistoryModal = () => {
    showSalesHistoryModal.value = false;
};

const xemLichSu = async () => {
    try {
        const storedId = localStorage.getItem("selectedInvoiceId");
        const response = await viewLichSuHoaDon(storedId);
        maHoaDon.value = response.data[0].maHoaDon
        orderHistory.value = response.data;
        showSalesHistoryModal.value = true;
    } catch (error) {
        console.error('Lỗi khi lấy lịch sử hóa đơn:', error);
        toast.error('Không thể tải lịch sử hóa đơn');
    }
};

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

const isFilterOpen = ref(false)

const filters = reactive({
    model: '',
    storage: '',
    color: '',
    priceMin: null,
    priceMax: null,
    quantityMin: null,
    quantityMax: null,
    maSpct: ''
})

const toggleFilter = () => {
    isFilterOpen.value = !isFilterOpen.value
}

const applyFilters = async () => {
    const response = await findSanPhamBanHang(selectedCategory.value, pageNoProduct.value, pageSizeProduct.value, selectedIdKhachHang.value
        , filters.model, filters.priceMin, filters.priceMax, filters.quantityMin, filters.quantityMax, filters.maSpct, filters.storage, filters.color
    );

    products.value = response.data.content;
    totalPagesProdut.value = response.data.totalPages
    isFilterOpen.value = false
}

const resetFilters = () => {
    filters.model = ''
    filters.storage = ''
    filters.color = ''
    filters.priceMin = null
    filters.priceMax = null
    filters.quantityMin = null
    filters.quantityMax = null
    filters.maSpct = null
}
</script>

<style scoped src="@/style/HoaDon/BanHang.css"></style>
