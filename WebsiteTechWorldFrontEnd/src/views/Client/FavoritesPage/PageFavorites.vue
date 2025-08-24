<template>
    <section class="favorites-section">
        <div class="container">
            <div class="header-content">
                <div class="title-group">
                    <h1 class="main-title">Sản Phẩm Yêu Thích Của Bạn</h1>
                    <p class="subtitle">
                        Khám phá những món đồ bạn đã đánh dấu là yêu thích.
                    </p>
                </div>
            </div>

            <div v-if="favoriteProducts.length === 0" class="empty-state">
                <HeartCrackIcon class="empty-icon" />
                <p class="empty-text-lg">Bạn chưa có sản phẩm yêu thích nào.</p>
                <p class="empty-text-sm">Hãy thêm một vài món đồ vào danh sách yêu thích của bạn!</p>
            </div>
            <div v-else class="product-grid">
                <div v-for="product in favoriteProducts" :key="product.idSp" class="product-card"
                    @click="goToProduct(product.idSp)">

                    <img :src="product.urlImage || '/placeholder.svg'" :alt="product.tenSanPham" width="400"
                        height="300" class="product-image" />

                    <div class="product-info">
                        <h3 class="product-name">{{ product.tenSanPham }}</h3>
                        <p class="product-infomation">{{ product.mau }} - {{ product.rom }}</p>
                        <p class="product-description">Sản phẩm chính hãng</p>

                        <div class="product-actions">
                            <p class="product-price">{{ formatCurrency(product.giaSanPham) }}</p>
                            <button @click.stop.prevent="removeFromFavorites(product.idSp)" class="remove-button">
                                <HeartCrackIcon class="remove-icon" />
                                Xóa
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="pagination-controls">
                <button @click="prevPage" :disabled="pageNoWishList === 0"> &lt; </button>
                <span>Trang {{ pageNoWishList + 1 }} / {{ totalPagesCategory }}</span>
                <button @click="nextPage" :disabled="pageNoWishList + 1 >= totalPagesCategory"> &gt; </button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { HeartCrackIcon } from 'lucide-vue-next';
import { getWishListByIdKhachHang, deleteWishList } from '@/Service/ClientService/WishList/WishListService'
import router from '@/router';
import { useToast } from "vue-toastification";


const favoriteProducts = ref([]);

const pageNoWishList = ref(0)
const pageSizeWishList = ref(8)
const totalPagesCategory = ref(1)
const toast = useToast()
const getWishListByIdKhachHangs = async () => {
    try{
        const response = await getWishListByIdKhachHang(pageNoWishList.value, pageSizeWishList.value);
        favoriteProducts.value  = response.data.content;
        totalPagesCategory.value = response.data.totalPages
    }
    catch(err){
        console.error("An error occurred: ",err.message);
    }
}

onMounted(async ()=>{
    await getWishListByIdKhachHangs();
})


const removeFromFavorites = async (id) => {
    try{
        const res = await deleteWishList(null,null,null,id)
        await getWishListByIdKhachHangs()
        toast.success("Xóa thành công khỏi mục yêu thích")
    }catch(err){
        console.error("Có lỗi sảy ra",err);
    }
};

const goToProduct = (idSp) => {
    router.push(`/client/detail/${idSp}`);
};

const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN').format(amount)
}
const nextPage = async () => {
    if (pageNoWishList.value + 1 < totalPagesCategory.value) {
        pageNoWishList.value++
        await getWishListByIdKhachHangs()
    }

}

const prevPage = async () => {
    if (pageNoWishList.value > 0) {
        pageNoWishList.value--
        await getWishListByIdKhachHangs()
    }

}
</script>

<style scoped>
/* General Layout */
.favorites-section {
    width: 100%;
    padding: 4rem 0;
    /* py-16 */
    background-color: #f0f2f5;
    /* Light gray background for a clean look */
    min-height: 100vh;
    /* Ensure it takes full viewport height */
    display: flex;
    flex-direction: column;
    align-items: center;
}

.container {
    max-width: 1280px;
    /* Slightly wider container */
    margin-left: auto;
    margin-right: auto;
    padding-left: 1.5rem;
    /* px-6 */
    padding-right: 1.5rem;
    /* px-6 */
    width: 100%;
}

@media (min-width: 768px) {

    /* md breakpoint */
    .favorites-section {
        padding: 5rem 0;
        /* md:py-20 */
    }
}

/* Header Content */
.header-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    margin-bottom: 3rem;
    /* Increased margin-bottom */
}

.title-group {
    margin-bottom: 1rem;
}

.main-title {
    font-size: 2.5rem;
    /* text-4xl */
    font-weight: 800;
    /* Extra bold */
    letter-spacing: -0.03em;
    /* Tighter tracking */
    line-height: 1.1;
    color: #1a202c;
    /* Darker text for prominence */
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.05);
    /* Subtle text shadow */
}

@media (min-width: 640px) {

    /* sm breakpoint */
    .main-title {
        font-size: 3rem;
        /* sm:text-5xl */
    }
}

@media (min-width: 768px) {

    /* md breakpoint */
    .main-title {
        font-size: 3.75rem;
        /* md:text-6xl */
    }
}

.subtitle {
    max-width: 800px;
    /* Wider subtitle */
    color: #4a5568;
    /* Slightly darker muted text */
    font-size: 1.25rem;
    /* md:text-xl */
    line-height: 1.6;
    margin-top: 0.75rem;
}

@media (min-width: 1024px) {

    /* lg breakpoint */
    .subtitle {
        font-size: 1.125rem;
        /* lg:text-lg */
    }
}

/* Empty State */
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 4rem 0;
    /* py-16 */
    text-align: center;
    color: #718096;
    /* Muted gray */
    background-color: #ffffff;
    border-radius: 0.75rem;
    box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
    margin-top: 2rem;
    width: 100%;
    max-width: 600px;
    margin-left: auto;
    margin-right: auto;
    animation: fadeIn 0.8s ease-out;
    /* Fade in animation */
}

.empty-icon {
    width: 4rem;
    /* h-16 */
    height: 4rem;
    /* w-16 */
    margin-bottom: 1.5rem;
    /* mb-6 */
    color: #a0aec0;
    /* Lighter gray for icon */
    animation: bounceIn 1s ease-out;
    /* Bounce in animation for icon */
}

.empty-text-lg {
    font-size: 1.5rem;
    /* text-2xl */
    font-weight: 600;
    margin-bottom: 0.75rem;
    color: #2d3748;
}

.empty-text-sm {
    font-size: 1rem;
    /* text-base */
    color: #4a5568;
}

/* Product Grid */
.product-grid {
    display: grid;
    grid-template-columns: 1fr;
    /* Default to 1 column */
    gap: 1.5rem;
    /* gap-6 */
    margin-top: 2rem;
    /* mt-8 */
}

@media (min-width: 640px) {

    /* sm breakpoint */
    .product-grid {
        grid-template-columns: repeat(2, 1fr);
        /* sm:grid-cols-2 */
    }
}

@media (min-width: 768px) {

    /* md breakpoint */
    .product-grid {
        grid-template-columns: repeat(3, 1fr);
        /* md:grid-cols-3 */
    }
}

@media (min-width: 1024px) {

    /* lg breakpoint */
    .product-grid {
        grid-template-columns: repeat(4, 1fr);
        /* lg:grid-cols-4 */
    }
}

/* Product Card */
.product-card {
    position: relative;
    overflow: hidden;
    border-radius: 0.75rem;
    /* More rounded corners */
    background-color: #ffffff;
    /* White background */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
    /* Softer initial shadow */
    transition: transform 0.3s cubic-bezier(0.25, 0.8, 0.25, 1), box-shadow 0.3s cubic-bezier(0.25, 0.8, 0.25, 1), border-color 0.3s ease;
    /* Smoother transitions */
    border: 1px solid transparent;
    /* Initial transparent border */
}

.product-card:hover {
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.15);
    /* More pronounced hover shadow */
    transform: translateY(-0.75rem) scale(1.02);
    /* Lift and slightly scale */
    border-color: #007bff;
    /* Highlight border on hover */
}

.product-link {
    position: absolute;
    inset: 0;
    z-index: 10;
}

.sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    white-space: nowrap;
    border-width: 0;
}

.product-image {
    object-fit: contain; /* luôn hiển thị đủ ảnh */
    width: 100%;
    height: 12rem;
    border-top-left-radius: 0.75rem;
    border-top-right-radius: 0.75rem;
    background: #f9f9f9; /* thêm nền nhạt cho đẹp nếu ảnh nhỏ hơn khung */
}

.product-info {
    padding: 1.25rem;
    /* More padding */
}

.product-name {
    font-size: 1.25rem;
    /* text-xl */
    font-weight: 700;
    /* font-bold */
    color: #2d3748;
    /* Darker text */
    margin-bottom: 0.5rem;
    line-height: 1.3;
}

@media (min-width: 768px) {

    /* md breakpoint */
    .product-name {
        font-size: 1.375rem;
        /* Slightly larger on md */
    }
}

.product-description {
    font-size: 0.9375rem;
    /* Slightly larger text-sm */
    color: #718096;
    /* Muted gray */
    margin-bottom: 1rem;
    /* More space */
    line-height: 1.5;
}

.product-actions {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 0.75rem;
    /* mt-3 */
}

.product-price {
    font-size: 1.375rem;
    /* text-xl */
    font-weight: 800;
    /* Extra bold */
    color: #007bff;
    /* A vibrant blue for price */
}

.remove-button {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #e2e8f0;
    /* Light gray border */
    border-radius: 0.5rem;
    /* More rounded */
    background-color: #f7fafc;
    /* Light background */
    padding: 0.625rem 1rem;
    /* More padding */
    font-size: 0.9375rem;
    /* Slightly larger text */
    font-weight: 600;
    /* Semi-bold */
    color: #4a5568;
    /* Darker text */
    cursor: pointer;
    transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out, border-color 0.2s ease-in-out, transform 0.1s ease-out;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    /* Subtle button shadow */
}

.remove-button:hover {
    background-color: #e2e8f0;
    /* Lighter gray on hover */
    color: #2d3748;
    /* Darker text on hover */
    border-color: #cbd5e0;
}

.remove-button:active {
    transform: scale(0.98);
    /* Slight press effect */
    box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
    /* Inset shadow on press */
}

.remove-button:focus-visible {
    outline: 2px solid #007bff;
    /* Focus ring */
    outline-offset: 2px;
}

.remove-icon {
    width: 1.125rem;
    /* h-4.5 */
    height: 1.125rem;
    /* w-4.5 */
    margin-right: 0.5rem;
    /* mr-2 */
}

/* Keyframe Animations */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes bounceIn {
    0% {
        opacity: 0;
        transform: scale(0.3);
    }

    50% {
        opacity: 1;
        transform: scale(1.05);
    }

    70% {
        transform: scale(0.9);
    }

    100% {
        transform: scale(1);
    }
}
.pagination-controls {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 25px;
    gap: 15px;
    /* Thêm padding ngang để căn chỉnh với imei-list */
    padding: 0 10px;
}

.pagination-controls button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 18px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.95em;
    transition: background-color 0.2s ease;
}

.pagination-controls button:hover:not(:disabled) {
    background-color: #0056b3;
}

.pagination-controls button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
    opacity: 0.7;
}

.pagination-controls span {
    font-weight: 500;
    color: #666;
    font-size: 1em;
}
</style>
