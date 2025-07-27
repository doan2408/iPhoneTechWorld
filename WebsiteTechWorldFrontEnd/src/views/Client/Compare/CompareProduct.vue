<template>
    <div class="container">
        <!-- Product Header with two states -->
        <div ref="productHeader" class="product-header" :class="{
            'sticky': isSticky,
            'compact-mode': isSticky, /* Apply compact styles when sticky */
            'full-mode': !isSticky /* Apply full styles when not sticky */
        }">
            <div class="header-content">
                <div class="products-grid">
                    <!-- Loop 3 slot: 2 sản phẩm + 1 thêm sản phẩm -->
                    <div v-for="(product, index) in products" :key="product.id" class="product-item">
                        <!-- Nếu có sản phẩm -->
                        <template v-if="product">
                            <!-- Full Mode -->
                            <div v-if="!isSticky" class="product-full">
                                <button class="remove-btn" @click="removeFromCompare(product)">
                                    <svg class="remove-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M6 18L18 6M6 6l12 12" />
                                    </svg>
                                </button>

                                <div class="product-card">
                                    <div class="product-image-container">
                                        <img :src="product.hinhAnh?.[0] || '/img/no-image.png'"
                                            :alt="product.tenSanPham" class="product-image-large" />
                                    </div>

                                    <div class="product-details">
                                        <div class="price-section-full">
                                            <div class="current-price-large">{{ product.giaBan }} ₫</div>
                                            <div class="original-price-large">{{ product.giaGoc || product.giaBan }} ₫
                                            </div>
                                        </div>
                                        <h3 class="product-name-large">{{ product.tenSanPham }} - {{ product.rom[0].ten
                                            }}</h3>
                                        <button class="buy-now-btn">Mua ngay</button>
                                    </div>
                                </div>
                            </div>

                            <!-- Compact Mode -->
                            <div v-else class="product-compact">
                                <img :src="product.hinhAnh?.[0] || '/img/no-image.png'" :alt="product.tenSanPham"
                                    class="product-image-small" />
                                <div class="product-info-compact">
                                    <h3 class="product-name-small">{{ product.tenSanPham }}</h3>
                                    <div class="price-compact">
                                        <span class="current-price-small">{{ product.giaBan }} ₫</span>
                                        <span class="original-price-small">{{ product.giaGoc || product.giaBan }}
                                            ₫</span>
                                    </div>
                                </div>
                            </div>
                        </template>
                        <template v-if="product.length === 2">
                            <div class="add-product-section">
                                <button class="add-product-btn" @click="goToProductList">
                                    <svg class="plus-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M12 4v16m8-8H4" />
                                    </svg>
                                </button>
                                <span class="add-product-text">Thêm sản phẩm</span>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </div>

        <!-- Spacer when sticky -->
        <div v-if="isSticky" class="sticky-spacer" :style="{ height: initialHeaderHeight + 'px' }"></div>

        <!-- Main Content -->
        <div class="main-content">
            <!-- Tabs and Options -->
            <div class="tabs-container" :class="{ 'slide-up': isVisible }">
                <div class="tabs-header">
                    <div class="tabs-nav">
                        <button @click="activeTab = 'specs'" :class="['tab-btn', { 'active': activeTab === 'specs' }]">
                            Thông số nổi bật
                        </button>
                        <button @click="activeTab = 'details'"
                            :class="['tab-btn', { 'active': activeTab === 'details' }]">
                            Thông tin chi tiết
                        </button>
                    </div>

                    <div class="checkbox-section">
                        <input type="checkbox" id="differences" v-model="showDifferencesOnly" class="checkbox" />
                        <label for="differences" class="checkbox-label">
                            Chỉ xem điểm khác biệt
                        </label>
                    </div>
                </div>

                <!-- Comparison Table -->
                <div class="table-content">
                    <div class="highlight-specs-header">
                        <i class="fas fa-gem"></i> Thông số nổi bật
                    </div>

                    <div v-for="(specGroup, groupIndex) in groupedSpecs" :key="`group-${groupIndex}`">

                        <div class="table-row spec-group-title-row" :style="{ animationDelay: `${groupIndex * 0.1}s` }"
                            :class="{ 'fade-in': isVisible }">
                            <div class="spec-label-group-title">{{ specGroup.title }}</div>
                            <div v-for="n in (3 - 1)" :key="`title-empty-${n}`"
                                class="spec-value empty-cell-title-hidden">—</div>
                        </div>

                        <div v-for="(label, key, itemIndex) in specGroup.items" :key="`spec-item-${groupIndex}-${key}`"
                            class="table-row spec-detail-row"
                            :style="{ animationDelay: `${(groupIndex * 0.1) + ((itemIndex + 1) * 0.05)}s` }"
                            :class="{ 'fade-in': isVisible }">

                            <div class="spec-value first-column-detail-value">
                                {{ thongSoProduct.length > 0 ? getSpecValue(thongSoProduct[0], key) : '—' }}
                            </div>

                            <div v-for="(product, pIndex) in thongSoProduct.slice(1)"
                                :key="`spec-value-${groupIndex}-${pIndex + 1}-${key}`" class="spec-value">
                                {{ getSpecValue(product, key) }}
                            </div>

                            <div v-for="n in (3 - thongSoProduct.length)" :key="`empty-spec-${groupIndex}-${key}-${n}`"
                                class="spec-value empty-cell">
                                —
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Product Information Section -->
            <div class="info-section" :class="{ 'slide-up': isVisible }">
                <button @click="toggleProductInfo" class="info-toggle">
                    <h3 class="info-title">Thông tin hàng hóa</h3>
                    <svg :class="['chevron-icon', { 'rotated': showProductInfo }]" fill="none" stroke="currentColor"
                        viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                    </svg>
                </button>

                <transition name="expand">
                    <div v-if="showProductInfo" class="info-content">
                        <div v-for="(specGroup, groupIndex) in groupThongTinHangHoa" :key="`group-${groupIndex}`">
                            <div class="table-row spec-group-title-row"
                                :style="{ animationDelay: `${groupIndex * 0.1}s` }" :class="{ 'fade-in': isVisible }">
                                <div class="spec-label-group-title"><b>{{ specGroup.title }}</b></div>
                                <div v-for="n in (3 - 1)" :key="`title-empty-${n}`"
                                    class="spec-value empty-cell-title-hidden">—</div>
                            </div>

                            <div v-for="(label, key, itemIndex) in specGroup.items"
                                :key="`spec-item-${groupIndex}-${key}`" class="table-row spec-detail-row"
                                :style="{ animationDelay: `${(groupIndex * 0.1) + ((itemIndex + 1) * 0.05)}s` }"
                                :class="{ 'fade-in': isVisible }">

                                <div class="spec-value first-column-detail-value">
                                    {{ thongSoProduct.length > 0 ? getSpecThongTinHangHoa(thongSoProduct[0], key) : '—'
                                    }}
                                </div>

                                <div v-for="(product, pIndex) in thongSoProduct.slice(1)"
                                    :key="`spec-value-${groupIndex}-${pIndex + 1}-${key}`" class="spec-value">
                                    {{ getSpecThongTinHangHoa(product, key) }}
                                </div>

                                <div v-for="n in (3 - thongSoProduct.length)"
                                    :key="`empty-spec-${groupIndex}-${key}-${n}`" class="spec-value empty-cell">
                                    —
                                </div>
                            </div>
                        </div>
                    </div>
                </transition>
            </div>
            <br>
            <div class="info-section" :class="{ 'slide-up': isVisible }">
                <button @click="toggleBoXuLy" class="info-toggle">
                    <h3 class="info-title">Bộ xử lý</h3>
                    <svg :class="['chevron-icon', { 'rotated': showBoXuLy }]" fill="none" stroke="currentColor"
                        viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                    </svg>
                </button>

                <transition name="expand">
                    <div v-if="showBoXuLy" class="info-content">
                        <div v-for="(specGroup, groupIndex) in groupBoXuLy" :key="`group-${groupIndex}`">
                            <div class="table-row spec-group-title-row"
                                :style="{ animationDelay: `${groupIndex * 0.1}s` }" :class="{ 'fade-in': isVisible }">
                                <div class="spec-label-group-title"><b>{{ specGroup.title }}</b></div>
                                <div v-for="n in (3 - 1)" :key="`title-empty-${n}`"
                                    class="spec-value empty-cell-title-hidden">—</div>
                            </div>

                            <div v-for="(label, key, itemIndex) in specGroup.items"
                                :key="`spec-item-${groupIndex}-${key}`" class="table-row spec-detail-row"
                                :style="{ animationDelay: `${(groupIndex * 0.1) + ((itemIndex + 1) * 0.05)}s` }"
                                :class="{ 'fade-in': isVisible }">

                                <div class="spec-value first-column-detail-value">
                                    {{ thongSoProduct.length > 0 ? getSpecBoXuLy(thongSoProduct[0], key) : '—'
                                    }}
                                </div>

                                <div v-for="(product, pIndex) in thongSoProduct.slice(1)"
                                    :key="`spec-value-${groupIndex}-${pIndex + 1}-${key}`" class="spec-value">
                                    {{ getSpecBoXuLy(product, key) }}
                                </div>

                                <div v-for="n in (3 - thongSoProduct.length)"
                                    :key="`empty-spec-${groupIndex}-${key}-${n}`" class="spec-value empty-cell">
                                    —
                                </div>
                            </div>
                        </div>
                    </div>
                </transition>
            </div>
            <br>
            <div class="info-section" :class="{ 'slide-up': isVisible }">
                <button @click="toggleRamVaRom" class="info-toggle">
                    <h3 class="info-title">Ram & Rom</h3>
                    <svg :class="['chevron-icon', { 'rotated': showRamVaRom }]" fill="none" stroke="currentColor"
                        viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                    </svg>
                </button>

                <transition name="expand">
                    <div v-if="showRamVaRom" class="info-content">
                        <div v-for="(specGroup, groupIndex) in groupRamVaRom" :key="`group-${groupIndex}`">
                            <div class="table-row spec-group-title-row"
                                :style="{ animationDelay: `${groupIndex * 0.1}s` }" :class="{ 'fade-in': isVisible }">
                                <div class="spec-label-group-title"><b>{{ specGroup.title }}</b></div>
                                <div v-for="n in (3 - 1)" :key="`title-empty-${n}`"
                                    class="spec-value empty-cell-title-hidden">—</div>
                            </div>

                            <div v-for="(label, key, itemIndex) in specGroup.items"
                                :key="`spec-item-${groupIndex}-${key}`" class="table-row spec-detail-row"
                                :style="{ animationDelay: `${(groupIndex * 0.1) + ((itemIndex + 1) * 0.05)}s` }"
                                :class="{ 'fade-in': isVisible }">

                                <div class="spec-value first-column-detail-value">
                                    {{ thongSoProduct.length > 0 ? getSpecRamVaRom(thongSoProduct[0], key) : '—'
                                    }}
                                </div>

                                <div v-for="(product, pIndex) in thongSoProduct.slice(1)"
                                    :key="`spec-value-${groupIndex}-${pIndex + 1}-${key}`" class="spec-value">
                                    {{ getSpecRamVaRom(product, key) }}
                                </div>

                                <div v-for="n in (3 - thongSoProduct.length)"
                                    :key="`empty-spec-${groupIndex}-${key}-${n}`" class="spec-value empty-cell">
                                    —
                                </div>
                            </div>
                        </div>
                    </div>
                </transition>
            </div>
            <br>
            <div class="info-section" :class="{ 'slide-up': isVisible }">
                <button @click="toggleManHinh" class="info-toggle">
                    <h3 class="info-title">Màn hình</h3>
                    <svg :class="['chevron-icon', { 'rotated': showManHinh }]" fill="none" stroke="currentColor"
                        viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                    </svg>
                </button>

                <transition name="expand">
                    <div v-if="showManHinh" class="info-content">
                        <div v-for="(specGroup, groupIndex) in groupManHinh" :key="`group-${groupIndex}`">
                            <div class="table-row spec-group-title-row"
                                :style="{ animationDelay: `${groupIndex * 0.1}s` }" :class="{ 'fade-in': isVisible }">
                                <div class="spec-label-group-title"><b>{{ specGroup.title }}</b></div>
                                <div v-for="n in (3 - 1)" :key="`title-empty-${n}`"
                                    class="spec-value empty-cell-title-hidden">—</div>
                            </div>

                            <div v-for="(label, key, itemIndex) in specGroup.items"
                                :key="`spec-item-${groupIndex}-${key}`" class="table-row spec-detail-row"
                                :style="{ animationDelay: `${(groupIndex * 0.1) + ((itemIndex + 1) * 0.05)}s` }"
                                :class="{ 'fade-in': isVisible }">

                                <div class="spec-value first-column-detail-value">
                                    {{ thongSoProduct.length > 0 ? getSpecManHinh(thongSoProduct[0], key) : '—'
                                    }}
                                </div>

                                <div v-for="(product, pIndex) in thongSoProduct.slice(1)"
                                    :key="`spec-value-${groupIndex}-${pIndex + 1}-${key}`" class="spec-value">
                                    {{ getSpecManHinh(product, key) }}
                                </div>

                                <div v-for="n in (3 - thongSoProduct.length)"
                                    :key="`empty-spec-${groupIndex}-${key}-${n}`" class="spec-value empty-cell">
                                    —
                                </div>
                            </div>
                        </div>
                    </div>
                </transition>
            </div>
            <br>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { detailSanPham, getThongSoLimitRomMin  } from '@/Service/ClientService/Products/ProductClientService'

const activeTab = ref('specs')
const showDifferencesOnly = ref(false)
const showProductInfo = ref(true)
const showBoXuLy = ref(true)
const showRamVaRom = ref(true)
const showManHinh =  ref(true)
const showCameraSau = ref(true)
const isSticky = ref(false)
const isVisible = ref(false)
const productHeader = ref(null)
const initialHeaderOffsetTop = ref(0);
const initialHeaderHeight = ref(0);

import { useRoute } from 'vue-router'
import { tr } from 'element-plus/es/locales.mjs'

const route = useRoute()

onMounted(async () => {
    const idsParam = route.query.ids // "1,2,3"
    if (!idsParam) return

    const ids = idsParam.split(',').map(id => parseInt(id))
    const fetchedProducts = []

    for (const id of ids) {
        try {
            const data = await detailSanPham(id)
            fetchedProducts.push(data)
        } catch (error) {
            console.error('Lỗi khi lấy sản phẩm ID:', id, error)
        }
    }

    products.value = fetchedProducts
    console.log('prodcut',products.value);
})

const thongSoProduct = ref([]);
onMounted(async () => {
    const idsParam = route.query.ids 
    if (!idsParam) return

    const ids = idsParam.split(',').map(id => parseInt(id))
    const fetchedThongSoProduct = []
    for (const id of ids) {
        try {
            const data = await getThongSoLimitRomMin(id)
            fetchedThongSoProduct.push(data)
        } catch (error) {
            console.error('Lỗi khi lấy sản phẩm ID:', id, error)
        }
    }

    thongSoProduct.value = fetchedThongSoProduct
    console.log('Thong so prodcut', thongSoProduct.value);
})

const products = ref([]);

const groupedSpecs = ref([
    {
        title: 'Kích thước màn hình',
        items: {
            screen_size: '' 
        }
    },
    {
        title: 'Camera',
        items: {
            camera_specs: '' 
        }
    },
    {
        title: 'RAM',
        items: {
            ram_specs: ''
        }
    }
]);

// Hàm phương thức
const getSpecValue = (product, key) => {
    switch (key) {
        case 'screen_size': 
            return `${product.kichThuoc || '?'} - ${product.tenManHinh || '?'}`;
        case 'camera_specs': 
            return product.doPhanGiai || '—';
        case 'ram_specs': 
            return product.ram || '—';
        default:
            return '—';
    }
};
const groupThongTinHangHoa = ref([
    {
        title: 'Kích thước',
        items: {
            kich_thuoc: ''
        }
    },
    {
        title: 'Xuất xứ',
        items: {
            xuat_xu: '' 
        }
    },
    {
        title: 'Thời gian bảo hành',
        items: {
            thoi_gian_bao_hanh: '' 
        }
    },
    {
        title: 'Loại',
        items: {
            loai: ''
        }
    },
]);

// Hàm phương thức
const getSpecThongTinHangHoa = (product, key) => {
    switch (key) {
        case 'kich_thuoc':
            return product.kichThuoc || '—';
        case 'xuat_xu':
            return product.xuatXu || '—';
        case 'loai':
            return product.tenLoai || '—';
        case 'thoi_gian_bao_hanh':
            return 'Chưa có' || '—';
        default:
            return '—';
    }
};

// thiet ke 
const groupBoXuLy = ref([
    {
        title: 'Chip Xử lý',
        items: {
            cpu: ''
        }
    },
    {
        title: 'Số nhân',
        items: {
            so_nhan: ''
        }
    },
    {
        title: 'Bộ nhớ đệm',
        items: {
            bo_nho_dem: ''
        }
    },
    {
        title: 'Xung nhịp',
        items: {
            xung_nhip: ''
        }
    },
    {
        title: 'Tiêu thụ điện năng',
        items: {
            tieu_thu_dien_nang: ''
        }
    },
    {
        title: 'Năm ra mắt',
        items: {
            nam_ra_mat: ''
        }
    },
]);

const getSpecBoXuLy = (product, key) => {
    switch (key) {
        case 'cpu':
            return product.cpu || '—';
        case 'so_nhan':
            return product.soNhan || '—';
        case 'xung_nhip':
            return product.xungNhip || '—';
        case 'bo_nho_dem':
            return product.boNhoDem || '—';
        case 'tieu_thu_dien_nang':
            return product.tieuThuDienNang || '—';
        case 'nam_ra_mat':
            return product.namRaMat || '—';
        default:
            return '—';
    }
};
const groupRamVaRom = ref([
    {
        title: 'Ram',
        items: {
            ram: ''
        }
    },
    {
        title: 'Rom',
        items: {
            rom: ''
        }
    },
]);

const getSpecRamVaRom = (product, key) => {
    switch (key) {
        case 'ram':
            return product.ram || '—';
        case 'rom':
            return product.rom || '—';
        default:
            return '—';
    }
};

const groupManHinh = ref([
    {
        title: 'Tên màn hình',
        items: {
            ten_man_hinh: ''
        }
    },
    {
        title: 'Kích thước',
        items: {
            kich_thuoc: ''
        }
    },
    {
        title: 'Loại màn hình',
        items: {
            loai_man_hinh: ''
        }
    },
    {
        title: 'Độ phân giải',
        items: {
            do_phan_giai: ''
        }
    },
    {
        title: 'Tần số quét',
        items: {
            tan_so_quet: ''
        }
    },
    {
        title: 'Độ sáng',
        items: {
            do_sang: ''
        }
    },
    {
        title: 'Chất liệu kính',
        items: {
            chat_lieu_kinh: ''
        }
    },
]);


const getSpecManHinh = (product, key) => {
    switch (key) {
        case 'ten_man_hinh':
            return product.tenManHinh || '—';
        case 'kich_thuoc':
            return product.kichThuoc || '—';
        case 'loai_man_hinh':
            return product.loaiManHinh || '—';
        case 'do_phan_giai':
            return product.doPhanGiai || '—';
        case 'tan_so_quet':
            return product.tanSoQuet || '—';
        case 'do_sang':
            return product.doSang || '—';
        case 'chat_lieu_kinh':
            return product.chatLieuKinh || '—';
        default:
            return '—';
    }
};
onMounted(() => {
    isVisible.value = true; 
});

const handleScroll = () => {
    if (productHeader.value) {
        const newIsSticky = window.scrollY > initialHeaderOffsetTop.value;

        if (newIsSticky !== isSticky.value) {
            isSticky.value = newIsSticky;
        }
    }
}

const toggleProductInfo = () => {
    showProductInfo.value = !showProductInfo.value
}
const toggleBoXuLy = () => {
    showBoXuLy.value = !showBoXuLy.value
}
const toggleRamVaRom = () => {
    showRamVaRom.value = !showRamVaRom.value
}
const toggleManHinh = () => {
    showManHinh.value = !showManHinh.value
}
const toggleCameraSau = () => {
    showCameraSau.value = !showCameraSau.value
}

onMounted(() => {
    if (productHeader.value) {
        initialHeaderOffsetTop.value = productHeader.value.offsetTop;
        initialHeaderHeight.value = productHeader.value.offsetHeight;
    }
    window.addEventListener('scroll', handleScroll)

    nextTick(() => {
        isVisible.value = true
    })
})

onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
/* Container */
.container {
    min-height: 100vh;
    background-color: #f9fafb;
}

/* Product Header */
.product-header {
    background-color: white;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
}

.product-header.sticky {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 50;
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    backdrop-filter: blur(10px);
}

.header-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 16px;
    transition: padding 0.4s ease;
}

.product-header.sticky .header-content {
    padding: 12px 16px;
    /* Smaller padding when sticky */
}

.products-grid {
    display: grid;
    /* Changed to grid */
    grid-template-columns: repeat(2, 1fr) 1fr;
    /* 2 product slots + 1 add product slot */
    gap: 20px;
    align-items: flex-start;
}

/* Product Item Base Styles */
.product-item {
    flex: 1;
    transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Full Mode Styles (Default when not sticky) */
.product-full {
    position: relative;
    padding: 16px;
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    background: white;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;
}

.product-full:hover {
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
}

.remove-btn {
    position: absolute;
    top: -8px;
    right: -8px;
    width: 24px;
    height: 24px;
    background-color: #6b7280;
    color: white;
    border: none;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    z-index: 10;
    transition: all 0.3s ease;
}

.remove-btn:hover {
    background-color: #374151;
    transform: scale(1.1);
}

.remove-icon {
    width: 12px;
    height: 12px;
}

.product-image-container {
    text-align: center;
    margin-bottom: 16px;
}

.product-image-large {
    width: 120px;
    height: 120px;
    border-radius: 12px;
    object-fit: cover;
}

.product-details {
    text-align: center;
}

.price-section-full {
    margin-bottom: 12px;
}

.current-price-large {
    font-size: 20px;
    font-weight: bold;
    color: #dc2626;
    display: block;
}

.original-price-large {
    font-size: 14px;
    color: #6b7280;
    text-decoration: line-through;
    margin-top: 4px;
}

.product-name-large {
    font-weight: 500;
    color: #111827;
    font-size: 14px;
    line-height: 1.4;
    margin-bottom: 16px;
}

.buy-now-btn {
    width: 100%;
    background-color: #dc2626;
    color: white;
    border: none;
    padding: 10px 16px;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
}

.buy-now-btn:hover {
    background-color: #b91c1c;
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(220, 38, 38, 0.3);
}

.buy-now-btn:active {
    transform: translateY(0);
}

/* Compact Mode Styles (When sticky) */
.product-compact {
    display: flex;
    gap: 12px;
    align-items: center;
    /* Align items vertically in compact mode */
    padding: 8px;
    border-radius: 8px;
    transition: all 0.3s ease;
}

.product-image-small {
    width: 60px;
    height: 60px;
    border-radius: 8px;
    border: 1px solid #e5e7eb;
    object-fit: cover;
}

.product-info-compact {
    flex: 1;
}

.product-name-small {
    font-weight: 500;
    color: #111827;
    margin-bottom: 4px;
    /* Reduced margin */
    font-size: 14px;
    line-height: 1.3;
}

.price-compact {
    display: flex;
    flex-direction: row;
    /* Horizontal layout for price in compact mode */
    align-items: baseline;
    gap: 8px;
    /* Space between prices */
}

.current-price-small {
    font-size: 16px;
    font-weight: bold;
    color: #dc2626;
}

.original-price-small {
    font-size: 12px;
    color: #6b7280;
    text-decoration: line-through;
}

/* Add Product Section */
.add-product-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px 0;
    transition: all 0.4s ease;
}

.add-product-btn {
    width: 48px;
    height: 48px;
    border-radius: 50%;
    border: 2px dashed #60a5fa;
    background: transparent;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 8px;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.add-product-btn:hover {
    border-color: #2563eb;
    background-color: rgba(59, 130, 246, 0.05);
    transform: scale(1.05);
}

.plus-icon {
    width: 24px;
    height: 24px;
    color: #3b82f6;
}

.add-product-text {
    font-size: 14px;
    color: #2563eb;
    font-weight: 500;
}

/* Sticky Spacer */
.sticky-spacer {
    /* Height will be set dynamically by Vue */
    transition: height 0.4s ease;
}

/* Main Content */
.main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 16px;
}

/* Tabs Container */
.tabs-container {
    background-color: white;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    margin-bottom: 20px;
    overflow: hidden;
    transform: translateY(20px);
    opacity: 0;
    transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.tabs-container.slide-up {
    transform: translateY(0);
    opacity: 1;
}

.tabs-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px;
    border-bottom: 1px solid #e5e7eb;
    background: linear-gradient(135deg, #f8fafc 0%, #ffffff 100%);
}

.tabs-nav {
    display: flex;
    gap: 4px;
}

.tab-btn {
    padding: 12px 20px;
    font-size: 14px;
    font-weight: 600;
    border-radius: 8px;
    border: none;
    background: none;
    color: #6b7280;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
}

.tab-btn:hover {
    color: #111827;
    background-color: rgba(220, 38, 38, 0.05);
}

.tab-btn.active {
    background-color: #dc2626;
    color: white;
    box-shadow: 0 4px 12px rgba(220, 38, 38, 0.3);
}

.checkbox-section {
    display: flex;
    align-items: center;
    gap: 8px;
}

.checkbox {
    width: 16px;
    height: 16px;
    border-radius: 4px;
    border: 2px solid #d1d5db;
    transition: all 0.3s ease;
}

.checkbox:checked {
    background-color: #dc2626;
    border-color: #dc2626;
}

.checkbox-label {
    font-size: 14px;
    color: #6b7280;
    font-weight: 500;
}

/* Comparison Table */
.comparison-table {
    background-color: white;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.table-header {
    background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%);
    color: white;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 12px;
    position: relative;
    overflow: hidden;
}

.table-header::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="25" cy="25" r="1" fill="white" opacity="0.1"/><circle cx="75" cy="75" r="1" fill="white" opacity="0.1"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
    opacity: 0.1;
}

.circle-icon {
    width: 16px;
    height: 16px;
    background-color: white;
    border-radius: 50%;
    position: relative;
    z-index: 1;
}

.table-title {
    font-weight: 700;
    margin: 0;
    font-size: 18px;
    position: relative;
    z-index: 1;
}

.table-content {
    border: 1px solid #f0f0f0;
    border-radius: 8px;
    overflow: hidden;
    margin-top: 20px;
    padding: 0;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    background-color: white;
}

.highlight-specs-header {
    background-color: #e0443c;
    color: white;
    font-weight: bold;
    padding: 12px 15px;
    display: flex;
    align-items: center;
    font-size: 1.1em;
}

.highlight-specs-header i {
    margin-right: 8px;
    color: #ffd700;
}

/* Kiểu chung cho các hàng */
.table-row {
    /* Đổi grid-template-columns: 1fr repeat(3, 1fr); để khớp với cách bạn muốn sắp xếp */
    /* Cột 1 cho tiêu đề/thông số đầu tiên, cột 2 cho SP2, cột 3 cho SP3 */
    grid-template-columns: 1fr 1fr 1fr;
    /* 3 cột bằng nhau (hoặc điều chỉnh nếu cần) */
    display: grid;
    transition: all 0.3s ease;
    opacity: 0;
    transform: translateX(-20px);
    align-items: center;
    padding: 0;
    /* Loại bỏ padding ở đây để kiểm soát padding cho từng ô */
}

.table-row.fade-in {
    opacity: 1;
    transform: translateX(0);
}

.table-row:hover {
    background-color: #f8fafc;
}

/* Style cho hàng tiêu đề nhóm (Chip, Kích thước màn hình, v.v.) */
.table-row.spec-group-title-row {
    background-color: #fff;
    font-weight: bold;
    color: #333;
    border-bottom: 1px dashed #e0e0e0;
    /* Đường kẻ dưới nét đứt */
    padding: 10px 0;

    /* Padding trên dưới */
    /* Đảm bảo không có hover effect nếu không muốn */
    &:hover {
        background-color: #fff;
    }
}

.spec-label-group-title {
    grid-column: 1 / span 3;
    /* Tiêu đề nhóm chiếm toàn bộ 3 cột */
    text-align: left;
    /* Căn trái */
    padding: 10px 20px;
    /* Padding phù hợp */
}

/* Ẩn các ô trống trong hàng tiêu đề nhóm nếu tiêu đề chiếm hết */
.spec-group-title-row .empty-cell-title-hidden {
    display: none;
    /* Ẩn hoàn toàn các ô trống này */
}


/* Style cho các hàng thông số chi tiết (Apple A18 Pro, 6.9 inch...) */
.table-row.spec-detail-row {
    border-bottom: 1px dashed #e0e0e0;
    /* Đường kẻ dưới nét đứt */
    padding: 0;
    /* Padding ở đây không cần, sẽ đặt ở .spec-value */
}

/* Loại bỏ đường gạch dưới cho hàng chi tiết cuối cùng của mỗi nhóm */
.spec-detail-row:last-of-type {
    border-bottom: none;
}

/* Cột giá trị thông số */
.spec-value {
    padding: 8px 20px;
    /* Padding cho mỗi ô giá trị */
    color: #333;
    font-weight: normal;
    /* Đảm bảo không đậm */
    text-align: left;
    /* Căn trái giá trị */
    border-left: 1px solid #f0f0f0;
    /* Đường kẻ dọc */
}

/* Cột giá trị đầu tiên (nằm dưới label) */
.spec-value.first-column-detail-value {
    border-left: none;
    /* Không có đường kẻ trái cho cột đầu tiên */
    font-weight: normal;
    /* Đảm bảo không đậm */
    color: #555;
    /* Màu chữ nhạt hơn một chút */
}


/* Các ô trống (dấu gạch ngang) */
.spec-value.empty-cell {
    color: #aaa;
    text-align: left;
    /* Căn trái dấu gạch ngang */
}


/* Info Section (giữ nguyên nếu nó là container bao ngoài) */
.info-section {
    background-color: white;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    overflow: hidden;
    transform: translateY(20px);
    opacity: 0;
    transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1) 0.2s;
}

.info-section.slide-up {
    transform: translateY(0);
    opacity: 1;
}

/* Animation */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.info-toggle {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px;
    background: none;
    border: none;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;
}

.info-toggle:hover {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
}

.info-title {
    font-size: 18px;
    font-weight: 700;
    color: #111827;
    margin: 0;
}

.chevron-icon {
    width: 20px;
    height: 20px;
    color: #6b7280;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.chevron-icon.rotated {
    transform: rotate(180deg);
}

/* Expand Transition */
.expand-enter-active,
.expand-leave-active {
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
    max-height: 0;
    opacity: 0;
    transform: translateY(-10px);
}

.expand-enter-to,
.expand-leave-from {
    max-height: 200px;
    /* Adjust based on content height */
    opacity: 1;
    transform: translateY(0);
}

.info-content {
    border-top: 1px solid #e5e7eb;
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
}

.info-row {
    display: grid;
    grid-template-columns: 1fr repeat(3, 1fr);
    /* 1 label column + 3 product data columns */
    border-bottom: 1px solid #e5e7eb;
    transition: all 0.3s ease;
}

.info-row:hover {
    background-color: rgba(59, 130, 246, 0.02);
}

.info-row:last-child {
    border-bottom: none;
}

.info-label {
    padding: 16px 20px;
    background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%);
    font-weight: 600;
    color: #374151;
    border-right: 1px solid #e5e7eb;
}

.info-value {
    padding: 16px 20px;
    color: #111827;
    font-weight: 500;
}

.info-value.empty-cell {
    color: #6b7280;
    /* Color for the dash */
    text-align: center;
    /* Center the dash */
}

/* Demo Content */
.demo-content {
    margin-top: 40px;
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.demo-card {
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    padding: 24px;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    border: 1px solid #e5e7eb;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    opacity: 0;
    transform: translateY(20px);
}

.demo-card.fade-in {
    opacity: 1;
    transform: translateY(0);
}

.demo-card:hover {
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
    transform: translateY(-4px);
}

.demo-title {
    font-size: 18px;
    font-weight: 700;
    margin-bottom: 12px;
    color: #111827;
}

.demo-text {
    color: #6b7280;
    line-height: 1.6;
    margin: 0;
}

/* Animations */
@keyframes slideInUp {
    from {
        opacity: 0;
        transform: translateY(30px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes expandIn {
    from {
        opacity: 0;
        transform: scale(0.95);
    }

    to {
        opacity: 1;
        transform: scale(1);
    }
}

@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

@keyframes pulse {

    0%,
    100% {
        transform: scale(1);
        box-shadow: 0 0 0 0 rgba(59, 130, 246, 0.4);
    }

    50% {
        transform: scale(1.05);
        box-shadow: 0 0 0 10px rgba(59, 130, 246, 0);
    }
}

/* Responsive */
@media (max-width: 768px) {
    .products-grid {
        grid-template-columns: 1fr;
        /* Stack products on mobile */
        gap: 12px;
    }

    .product-full {
        padding: 12px;
    }

    .product-image-large {
        width: 80px;
        height: 80px;
    }

    .table-row,
    .info-row {
        grid-template-columns: 1fr;
        /* Stack columns on mobile */
    }

    .spec-label,
    .info-label {
        border-right: none;
        border-bottom: 1px solid #e5e7eb;
    }

    .tabs-header {
        flex-direction: column;
        gap: 16px;
        align-items: flex-start;
    }

    .sticky-spacer {
        height: 300px;
        /* Adjust based on full header height on mobile */
    }
}
</style>
