<template>
    <div v-if="isOpen" class="dialog-overlay">
        <div class="dialog">
            <div class="dialog-header">
                <h2>Đánh giá đơn hàng #{{ orderId }}</h2>
                <button class="close-button" @click="closeDialog">✖</button>
            </div>
            <div class="dialog-content">
                <!-- Danh sách sản phẩm -->
                <div class="product-list">
                    <h3>Sản phẩm</h3>
                    <div v-for="product in orderProducts" :key="product.idSanPhamChiTiet" class="product-item">
                        <img :src="product.urlImage" :alt="product.tenSanPham" class="product-image" />
                        <div class="product-details">
                            <div class="product-name">{{ product.tenSanPham }}</div>
                            <div class="product-variant">Phân loại: {{ product.colorName }} {{ product.dungLuongRom }}</div>
                            <div class="product-quantity">x{{ product.soLuong }}</div>
                            <div class="rating-section">
                                <label class="label">Đánh giá:</label>
                                <div class="star-rating">
                                    <span v-for="star in 5" :key="star"
                                        :class="['star', { filled: star <= productRatings[product.idSanPhamChiTiet] }]"
                                        @click="setRating(product.idSanPhamChiTiet, star)">
                                        ★
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Bình luận -->
                <div class="comment-section">
                    <label for="comment" class="label">Bình luận:</label>
                    <textarea id="comment" v-model="comment" class="text-input" rows="4"
                        placeholder="Nhập nhận xét của bạn..."></textarea>
                </div>
                <!-- Tải lên ảnh -->
                <div class="media-upload">
                    <label class="label">Tải lên ảnh (tối đa 5):</label>
                    <div class="drop-zone" :class="{ 'drag-over': isImageDragOver }" @dragover.prevent="onDragOver($event, 'image')"
                        @dragenter.prevent="onDragEnter($event, 'image')" @dragleave.prevent="onDragLeave($event, 'image')"
                        @drop.prevent="onImageDrop">
                        <div class="drop-zone-content">
                            <svg class="upload-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M7 16V8m0 0l-4 4m4-4l4 4m6 4v-6a2 2 0 00-2-2h-6m-4 4h12" />
                            </svg>
                            <p>Kéo và thả ảnh vào đây hoặc nhấp để chọn (JPG, PNG, tối đa 5MB)</p>
                        </div>
                        <input type="file" accept="image/jpeg,image/png" multiple @change="handleImageUpload"
                            class="file-input" />
                    </div>
                    <div class="media-preview">
                        <div v-for="(image, index) in imagePreviews" :key="index" class="preview-item">
                            <img :src="image.url" class="preview-image" />
                            <button class="remove-media" @click="removeImage(index)">✖</button>
                        </div>
                    </div>
                </div>
                <!-- Tải lên video -->
                <div class="media-upload">
                    <label class="label">Tải lên video (tối đa 2):</label>
                    <div class="drop-zone" :class="{ 'drag-over': isVideoDragOver }" @dragover.prevent="onDragOver($event, 'video')"
                        @dragenter.prevent="onDragEnter($event, 'video')" @dragleave.prevent="onDragLeave($event, 'video')"
                        @drop.prevent="onVideoDrop">
                        <div class="drop-zone-content">
                            <svg class="upload-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M7 16V8m0 0l-4 4m4-4l4 4m6 4v-6a2 2 0 00-2-2h-6m-4 4h12" />
                            </svg>
                            <p>Kéo và thả video vào đây hoặc nhấp để chọn (MP4, WebM, tối đa 50MB)</p>
                        </div>
                        <input type="file" accept="video/mp4,video/webm" multiple @change="handleVideoUpload"
                            class="file-input" />
                    </div>
                    <div class="media-preview">
                        <div v-for="(video, index) in videoPreviews" :key="index" class="preview-item">
                            <video :src="video.url" controls class="preview-video"></video>
                            <button class="remove-media" @click="removeVideo(index)">✖</button>
                        </div>
                    </div>
                </div>
                <div class="dialog-footer">
                    <button class="button primary-button" :disabled="isSubmitting" @click="submitRating">
                        {{ isSubmitting ? 'Đang gửi...' : 'Gửi đánh giá' }}
                    </button>
                    <button class="button outline-button" @click="closeDialog">Hủy</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useToast } from 'vue-toastification';

const props = defineProps({
    isOpen: Boolean,
    orderId: Number,
    orderProducts: Array,
});

const emit = defineEmits(['close', 'submit']);
const user = ref(JSON.parse(localStorage.getItem('user')) || null);

const productRatings = ref({});
const comment = ref('');
const imageFiles = ref([]); // Lưu file ảnh gốc
const imagePreviews = ref([]); // Lưu URL preview
const videoFiles = ref([]); // Lưu file video gốc
const videoPreviews = ref([]); // Lưu URL preview
const isSubmitting = ref(false);
const isImageDragOver = ref(false); // Trạng thái kéo thả cho ảnh
const isVideoDragOver = ref(false); // Trạng thái kéo thả cho video
 const toast = useToast(); // 👈 nếu chưa khai báo trong scope

// Kiểm tra props
watch(props, () => {
    console.log('Props nhận được:', {
        isOpen: props.isOpen,
        orderId: props.orderId,
        orderProducts: props.orderProducts.map((p, index) => ({
            index,
            idSanPhamChiTiet: p.idSanPhamChiTiet,
            idChiTietHoaDon: p.idChiTietHoaDon,
            tenSanPham: p.tenSanPham,
        })),
    });

    if (!props.orderProducts || !Array.isArray(props.orderProducts)) {
        console.error('Lỗi: orderProducts không hợp lệ', { orderProducts: props.orderProducts });
        toast.error('Dữ liệu sản phẩm không hợp lệ!');
        return;
    }

});

const MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
const MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50MB
const MAX_IMAGES = 5; // Tối đa 5 ảnh
const MAX_VIDEOS = 2; // Tối đa 2 video

const setRating = (productId, value) => {
    console.log('setRating gọi với:', { productId, value });
    const product = props.orderProducts.find(p => p.idSanPhamChiTiet == productId);
    if (!product) {
        console.error(`ID sản phẩm chi tiết không hợp lệ: ${productId}`);
        toast.error(`ID sản phẩm chi tiết không hợp lệ: ${productId}`);
        return;
    }
    productRatings.value[productId] = value;
    console.log('productRatings sau khi set:', productRatings.value);
};

const handleImageUpload = (event) => {
    console.log('handleImageUpload được gọi');
    const files = event.target.files || event.dataTransfer?.files;
    if (!files || files.length === 0) {
        console.warn('Không có file nào được chọn hoặc kéo thả');
        toast.warning('Vui lòng chọn hoặc kéo thả ít nhất một ảnh!');
        return;
    }

    const newFiles = Array.from(files);
    console.log('Danh sách file ảnh:', newFiles.map(f => ({ name: f.name, size: f.size, type: f.type })));

    if (imageFiles.value.length + newFiles.length > MAX_IMAGES) {
        console.warn(`Vượt quá giới hạn ${MAX_IMAGES} ảnh! Hiện có: ${imageFiles.value.length}, thêm: ${newFiles.length}`);
        toast.warning(`Bạn chỉ có thể tải lên tối đa ${MAX_IMAGES} ảnh!`);
        return;
    }

    const invalidFiles = newFiles.filter(file => {
        if (!file.type.startsWith('image/')) {
            console.warn(`Tệp không phải ảnh: ${file.name}`);
            toast.warning(`Tệp ${file.name} không phải là ảnh!`);
            return true;
        }
        if (file.size > MAX_IMAGE_SIZE) {
            console.warn(`Ảnh vượt quá 5MB: ${file.name}`);
            toast.warning(`Ảnh ${file.name} vượt quá 5MB!`);
            return true;
        }
        if (!['image/jpeg', 'image/png'].includes(file.type)) {
            console.warn(`Ảnh không đúng định dạng JPG/PNG: ${file.name}`);
            toast.warning(`Ảnh ${file.name} phải có định dạng JPG hoặc PNG!`);
            return true;
        }
        if (imageFiles.value.some(existingFile => existingFile.name === file.name)) {
            console.warn(`Ảnh đã tồn tại: ${file.name}`);
            toast.warning(`Ảnh ${file.name} đã được thêm trước đó!`);
            return true;
        }
        return false;
    });

    if (invalidFiles.length > 0) {
        console.warn('Có file không hợp lệ, dừng xử lý:', invalidFiles.map(f => f.name));
        return;
    }

    newFiles.forEach(file => {
        const previewUrl = URL.createObjectURL(file);
        imageFiles.value.push(file);
        imagePreviews.value.push({ file, url: previewUrl });
        console.log(`Thêm ảnh: ${file.name}, URL: ${previewUrl}`);
    });

    console.log('imageFiles sau khi thêm:', imageFiles.value.map(f => f.name));
    console.log('imagePreviews sau khi thêm:', imagePreviews.value.map(p => p.file.name));
};

const handleVideoUpload = (event) => {
    console.log('handleVideoUpload được gọi');
    const files = Array.from(event.target.files || event.dataTransfer?.files);
    console.log('Danh sách file video:', files.map(f => ({ name: f.name, size: f.size, type: f.type })));

    if (videoFiles.value.length + files.length > MAX_VIDEOS) {
        console.warn(`Vượt quá giới hạn ${MAX_VIDEOS} video! Hiện có: ${videoFiles.value.length}, thêm: ${files.length}`);
        toast.warning(`Bạn chỉ có thể tải lên tối đa ${MAX_VIDEOS} video!`);
        return;
    }

    const invalidFiles = files.filter(file => {
        if (!file.type.startsWith('video/')) {
            console.warn(`Tệp không phải video: ${file.name}`);
            toast.warning(`Tệp ${file.name} không phải là video!`);
            return true;
        }
        if (file.size > MAX_VIDEO_SIZE) {
            console.warn(`Video vượt quá 50MB: ${file.name}`);
            toast.warning.warning(`Video ${file.name} vượt quá 50MB!`);
            return true;
        }
        if (!['video/mp4', 'video/webm'].includes(file.type)) {
            console.warn(`Video không đúng định dạng MP4/WebM: ${file.name}`);
            toast.warning(`Video ${file.name} phải có định dạng MP4 hoặc WebM!`);
            return true;
        }
        if (videoFiles.value.some(existingFile => existingFile.name === file.name)) {
            console.warn(`Video đã tồn tại: ${file.name}`);
            toast.warning(`Video ${file.name} đã được thêm trước đó!`);
            return true;
        }
        return false;
    });

    if (invalidFiles.length > 0) {
        console.warn('Có file không hợp lệ, dừng xử lý:', invalidFiles.map(f => f.name));
        return;
    }

    files.forEach(file => {
        const previewUrl = URL.createObjectURL(file);
        videoFiles.value.push(file);
        videoPreviews.value.push({ file, url: previewUrl });
        console.log(`Thêm video: ${file.name}, URL: ${previewUrl}`);
    });

    console.log('videoFiles sau khi thêm:', videoFiles.value.map(f => f.name));
    console.log('videoPreviews sau khi thêm:', videoPreviews.value.map(p => p.file.name));
};

const onDragOver = (event, type) => {
    event.preventDefault();
    console.log(`onDragOver gọi với type: ${type}`);
    if (type === 'image') {
        isImageDragOver.value = true;
    } else {
        isVideoDragOver.value = true;
    }
    console.log(`Trạng thái kéo thả: isImageDragOver=${isImageDragOver.value}, isVideoDragOver=${isVideoDragOver.value}`);
};

const onDragEnter = (event, type) => {
    event.preventDefault();
    console.log(`onDragEnter gọi với type: ${type}`);
    if (type === 'image') {
        isImageDragOver.value = true;
    } else {
        isVideoDragOver.value = true;
    }
    console.log(`Trạng thái kéo thả: isImageDragOver=${isImageDragOver.value}, isVideoDragOver=${isVideoDragOver.value}`);
};

const onDragLeave = (event, type) => {
    event.preventDefault();
    console.log(`onDragLeave gọi với type: ${type}`);
    if (type === 'image') {
        isImageDragOver.value = false;
    } else {
        isVideoDragOver.value = false;
    }
    console.log(`Trạng thái kéo thả: isImageDragOver=${isImageDragOver.value}, isVideoDragOver=${isVideoDragOver.value}`);
};

const onImageDrop = (event) => {
    console.log('onImageDrop được gọi');
    isImageDragOver.value = false;
    handleImageUpload(event);
};

const onVideoDrop = (event) => {
    console.log('onVideoDrop được gọi');
    isVideoDragOver.value = false;
    handleVideoUpload(event);
};

const removeImage = (index) => {
    console.log(`Xóa ảnh tại index ${index}:`, imageFiles.value[index]?.name);
    URL.revokeObjectURL(imagePreviews.value[index].url);
    imageFiles.value.splice(index, 1);
    imagePreviews.value.splice(index, 1);
    console.log('imageFiles sau khi xóa:', imageFiles.value.map(f => f.name));
    console.log('imagePreviews sau khi xóa:', imagePreviews.value.map(p => p.file?.name));
};

const removeVideo = (index) => {
    console.log(`Xóa video tại index ${index}:`, videoFiles.value[index]?.name);
    URL.revokeObjectURL(videoPreviews.value[index].url);
    videoFiles.value.splice(index, 1);
    videoPreviews.value.splice(index, 1);
    console.log('videoFiles sau khi xóa:', videoFiles.value.map(f => f.name));
    console.log('videoPreviews sau khi xóa:', videoPreviews.value.map(p => p.file?.name));
};

const closeDialog = () => {
    console.log('closeDialog được gọi');
    imagePreviews.value.forEach(item => {
        console.log(`Thu hồi URL ảnh: ${item.file?.name}`);
        URL.revokeObjectURL(item.url);
    });
    videoPreviews.value.forEach(item => {
        console.log(`Thu hồi URL video: ${item.file?.name}`);
        URL.revokeObjectURL(item.url);
    });
    console.log('Reset trạng thái dialog');
    productRatings.value = {};
    comment.value = '';
    imageFiles.value = [];
    imagePreviews.value = [];
    videoFiles.value = [];
    videoPreviews.value = [];
    isImageDragOver.value = false;
    isVideoDragOver.value = false;
    console.log('Trạng thái sau reset:', {
        productRatings: productRatings.value,
        comment: comment.value,
        imageFiles: imageFiles.value,
        imagePreviews: imagePreviews.value,
        videoFiles: videoFiles.value,
        videoPreviews: videoPreviews.value,
        isImageDragOver: isImageDragOver.value,
        isVideoDragOver: isVideoDragOver.value,
    });
    emit('close');
};

const submitRating = async () => {
  console.log('submitRating được gọi');
  console.log('Trạng thái trước khi gửi:', {
    productRatings: productRatings.value,
    comment: comment.value,
    imageFiles: imageFiles.value.map(f => ({ name: f.name, size: f.size, type: f.type })),
    videoFiles: videoFiles.value.map(f => ({ name: f.name, size: f.size, type: f.type })),
  });

  const soSao = Object.keys(productRatings.value).map(productId => {
    const product = props.orderProducts.find(p => p.idSanPhamChiTiet == productId);
    if (!product) {
      console.error(`Không tìm thấy sản phẩm với idSanPhamChiTiet: ${productId}`);
      return null;
    }
    return {
      idSanPhamChiTiet: product.idSanPhamChiTiet,
      idChiTietHoaDon: product.idChiTietHoaDon,
      soSao: productRatings.value[productId],
    };
  }).filter(r => r !== null);

  console.log('Ratings trước khi kiểm tra:', soSao);
  if (soSao.length === 0 || soSao.some(r => !r.soSao)) {
    console.warn('Không có đánh giá hợp lệ hoặc chưa đánh giá sản phẩm');
    toast.error('Vui lòng đánh giá ít nhất một sản phẩm!');
    isSubmitting.value = false;
    return;
  }

  isSubmitting.value = true;
  console.log('isSubmitting được đặt thành true');
  try {
    const payload = {
      idHoaDon: props.orderId,
      idKhachHang: user.value.id,
      soSao,
      noiDung: comment.value,
      trangThaiDanhGia: 'PENDING_APPROVAL',
      imageFiles: imageFiles.value, // Thêm imageFiles
      videoFiles: videoFiles.value, // Thêm videoFiles
    };

    emit('submit', { payload });
    console.log('Đã emit sự kiện submit với payload');
  } catch (error) {
    console.error('Lỗi khi gửi đánh giá:', error);
    toast.error('Gửi đánh giá thất bại. Vui lòng thử lại.');
  } finally {
    isSubmitting.value = false;
    console.log('isSubmitting được đặt thành false');
  }
};

</script>

<style scoped>
.dialog-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.dialog {
    background: white;
    border-radius: 8px;
    width: 600px;
    max-width: 90%;
    max-height: 80vh;
    overflow-y: auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    border-bottom: 1px solid #e5e7eb;
}

.dialog-header h2 {
    margin: 0;
    font-size: 1.25rem;
    font-weight: 600;
    color: #1f2937;
}

.close-button {
    background: none;
    border: none;
    font-size: 1.2rem;
    cursor: pointer;
    color: #6b7280;
}

.close-button:hover {
    color: #374151;
}

.dialog-content {
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.product-list h3 {
    font-size: 1.1rem;
    font-weight: 600;
    margin-bottom: 12px;
    color: #1f2937;
}

.product-item {
    display: flex;
    gap: 16px;
    padding: 12px 0;
    border-bottom: 1px solid #e5e7eb;
}

.product-image {
    width: 60px;
    height: 60px;
    object-fit: cover;
    border-radius: 4px;
}

.product-details {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.product-name {
    font-weight: 500;
    color: #1f2937;
}

.product-variant,
.product-quantity {
    color: #6b7280;
    font-size: 0.875rem;
}

.rating-section {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.label {
    font-weight: 500;
    color: #374151;
}

.star-rating {
    display: flex;
    gap: 4px;
}

.star {
    font-size: 1.5rem;
    color: #d1d5db;
    cursor: pointer;
    transition: color 0.2s;
}

.star.filled {
    color: #facc15;
}

.star:hover,
.star.filled:hover {
    color: #eab308;
}

.comment-section {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.text-input {
    width: 100%;
    padding: 10px;
    border: 1px solid #d1d5db;
    border-radius: 4px;
    resize: vertical;
    font-size: 0.875rem;
    color: #1f2937;
}

.text-input:focus {
    outline: none;
    border-color: #28a745;
    box-shadow: 0 0 0 3px rgba(40, 167, 69, 0.1);
}

.media-upload {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.drop-zone {
    border: 2px dashed #d1d5db;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    background: #f9fafb;
    cursor: pointer;
    position: relative;
    transition: all 0.3s ease;
}

.drop-zone.drag-over {
    border-color: #28a745;
    background: #e6f4ea;
}

.drop-zone-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
}

.upload-icon {
    width: 40px;
    height: 40px;
    color: #6b7280;
}

.drop-zone.drag-over .upload-icon {
    color: #28a745;
}

.drop-zone p {
    margin: 0;
    color: #6b7280;
    font-size: 0.875rem;
}

.drop-zone.drag-over p {
    color: #28a745;
}

.file-input {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
    cursor: pointer;
}

.media-preview {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
}

.preview-item {
    position: relative;
    width: 80px;
    height: 80px;
}

.preview-image,
.preview-video {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 4px;
    border: 1px solid #e5e7eb;
}

.remove-media {
    position: absolute;
    top: -6px;
    right: -6px;
    background: #ef4444;
    color: white;
    border: none;
    border-radius: 50%;
    width: 20px;
    height: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    font-size: 0.75rem;
}

.remove-media:hover {
    background: #dc2626;
}

.dialog-footer {
    padding: 16px 20px;
    border-top: 1px solid #e5e7eb;
    display: flex;
    gap: 12px;
    justify-content: flex-end;
}

.button {
    padding: 8px 16px;
    border-radius: 4px;
    cursor: pointer;
    font-weight: 500;
    transition: all 0.2s;
}

.primary-button {
    background-color: #28a745;
    color: white;
    border: none;
}

.primary-button:hover {
    background-color: #218838;
}

.primary-button:disabled {
    background-color: #6c757d;
    cursor: not-allowed;
}

.outline-button {
    background: none;
    border: 1px solid #d1d5db;
    color: #374151;
}

.outline-button:hover {
    background-color: #f3f4f6;
}
</style>