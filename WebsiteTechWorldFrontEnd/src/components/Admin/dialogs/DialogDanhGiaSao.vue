<template>
  <div v-if="isOpen" class="dialog-overlay">
    <div class="dialog">
      <div class="dialog-header">
        <h2>{{ isEditing ? 'Sửa đánh giá đơn hàng' : 'Đánh giá đơn hàng' }} #{{ orderId }}</h2>
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
                  <span class="debug-info">Rating: {{ productRatings[product.idSanPhamChiTiet] || 'Chưa chọn' }}</span>
                  <span v-for="star in 5" :key="star"
                    :class="['star', { filled: star <= productRatings[product.idSanPhamChiTiet] }]"
                    @click="setRating(product.idSanPhamChiTiet, star)">
                    ★
                  </span>
                </div>
              </div>
              <!-- Bình luận cho sản phẩm -->
              <div class="comment-section">
                <label class="label">Bình luận:</label>
                <textarea
                  :id="'comment-' + product.idSanPhamChiTiet"
                  v-model="productComments[product.idSanPhamChiTiet]"
                  class="text-input"
                  rows="4"
                  :placeholder="'Nhập nhận xét cho ' + product.tenSanPham"
                ></textarea>
                <span class="debug-info">Comment: {{ productComments[product.idSanPhamChiTiet] || 'Trống' }}</span>
              </div>
              <!-- Tải lên ảnh cho sản phẩm -->
              <div class="media-upload">
                <label class="label">Tải lên ảnh (tối đa 5):</label>
                <div
                  class="drop-zone"
                  :class="{ 'drag-over': isImageDragOver[product.idSanPhamChiTiet] }"
                  @dragover.prevent="onDragOver($event, 'image', product.idSanPhamChiTiet)"
                  @dragenter.prevent="onDragEnter($event, 'image', product.idSanPhamChiTiet)"
                  @dragleave.prevent="onDragLeave($event, 'image', product.idSanPhamChiTiet)"
                  @drop.prevent="onImageDrop($event, product.idSanPhamChiTiet)"
                >
                  <div class="drop-zone-content">
                    <svg class="upload-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M7 16V8m0 0l-4 4m4-4l4 4m6 4v-6a2 2 0 00-2-2h-6m-4 4h12" />
                    </svg>
                    <p>Kéo và thả ảnh vào đây hoặc nhấp để chọn (JPG, PNG, tối đa 5MB)</p>
                  </div>
                  <input
                    type="file"
                    accept="image/jpeg,image/png,image/webp"
                    multiple
                    @change="handleImageUpload($event, product.idSanPhamChiTiet)"
                    class="file-input"
                  />
                </div>
                <div class="media-preview">
                  <!-- Hiển thị ảnh hiện có -->
                  <div
                    v-for="(media, index) in (existingMedia[product.idSanPhamChiTiet] || []).filter(m => m.type === 'image')"
                    :key="'existing-image-' + media.id"
                    class="preview-item"
                  >
                    <img :src="media.url" class="preview-image" />
                    <button class="remove-media" @click="removeExistingMedia(product.idSanPhamChiTiet, media.id)">✖</button>
                  </div>
                  <!-- Hiển thị ảnh mới -->
                  <div
                    v-for="(image, index) in (imagePreviews[product.idSanPhamChiTiet] || [])"
                    :key="'new-image-' + index"
                    class="preview-item"
                  >
                    <img :src="image.url" class="preview-image" />
                    <button class="remove-media" @click="removeImage(product.idSanPhamChiTiet, index)">✖</button>
                  </div>
                  <span class="debug-info">Existing Images: {{ (existingMedia[product.idSanPhamChiTiet] || []).filter(m => m.type === 'image').length }} items</span>
                  <span class="debug-info">New Images: {{ (imagePreviews[product.idSanPhamChiTiet] || []).length }} items</span>
                </div>
              </div>
              <!-- Tải lên video cho sản phẩm -->
              <div class="media-upload">
                <label class="label">Tải lên video (tối đa 2):</label>
                <div
                  class="drop-zone"
                  :class="{ 'drag-over': isVideoDragOver[product.idSanPhamChiTiet] }"
                  @dragover.prevent="onDragOver($event, 'video', product.idSanPhamChiTiet)"
                  @dragenter.prevent="onDragEnter($event, 'video', product.idSanPhamChiTiet)"
                  @dragleave.prevent="onDragLeave($event, 'video', product.idSanPhamChiTiet)"
                  @drop.prevent="onVideoDrop($event, product.idSanPhamChiTiet)"
                >
                  <div class="drop-zone-content">
                    <svg class="upload-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M7 16V8m0 0l-4 4m4-4l4 4m6 4v-6a2 2 0 00-2-2h-6m-4 4h12" />
                    </svg>
                    <p>Kéo và thả video vào đây hoặc nhấp để chọn (MP4, WebM, tối đa 50MB)</p>
                  </div>
                  <input
                    type="file"
                    accept="video/mp4,video/webm"
                    multiple
                    @change="handleVideoUpload($event, product.idSanPhamChiTiet)"
                    class="file-input"
                  />
                </div>
                <div class="media-preview">
                  <!-- Hiển thị video hiện có -->
                  <div
                    v-for="(media, index) in (existingMedia[product.idSanPhamChiTiet] || []).filter(m => m.type === 'video')"
                    :key="'existing-video-' + media.id"
                    class="preview-item"
                  >
                    <video :src="media.url" controls class="preview-video"></video>
                    <button class="remove-media" @click="removeExistingMedia(product.idSanPhamChiTiet, media.id)">✖</button>
                  </div>
                  <!-- Hiển thị video mới -->
                  <div
                    v-for="(video, index) in (videoPreviews[product.idSanPhamChiTiet] || [])"
                    :key="'new-video-' + index"
                    class="preview-item"
                  >
                    <video :src="video.url" controls class="preview-video"></video>
                    <button class="remove-media" @click="removeVideo(product.idSanPhamChiTiet, index)">✖</button>
                  </div>
                  <span class="debug-info">Existing Videos: {{ (existingMedia[product.idSanPhamChiTiet] || []).filter(m => m.type === 'video').length }} items</span>
                  <span class="debug-info">New Videos: {{ (videoPreviews[product.idSanPhamChiTiet] || []).length }} items</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="dialog-footer">
          <button class="button primary-button" :disabled="isSubmitting" @click="submitRating">
            {{ isSubmitting ? 'Đang gửi...' : isEditing ? 'Cập nhật đánh giá' : 'Gửi đánh giá' }}
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
import { MediaDanhGiaClientService } from '@/Service/ClientService/MediaDanhGiaClientService/MediaDanhGiaClientService';

const props = defineProps({
  isOpen: Boolean,
  orderId: Number,
  orderProducts: Array,
  idSanPhamChiTietList: Array,
  isEditing: Boolean,
  existingRatingData: Object,
});

const emit = defineEmits(['close', 'submit']);
const toast = useToast();
const user = ref(JSON.parse(localStorage.getItem('user')) || null);

const productRatings = ref({});
const productComments = ref({});
const imageFiles = ref({});
const imagePreviews = ref({});
const videoFiles = ref({});
const videoPreviews = ref({});
const isSubmitting = ref(false);
const isImageDragOver = ref({});
const isVideoDragOver = ref({});
const existingMedia = ref({});
const deletedMediaIds = ref({}); // Thêm trạng thái để lưu ID media đã xóa

const MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
const MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50MB
const MAX_IMAGES = 5; // Tối đa 5 ảnh
const MAX_VIDEOS = 2; // Tối đa 2 video

// Khởi tạo dữ liệu khi chỉnh sửa
watch(
  () => [props.isEditing, props.existingRatingData],
  ([newIsEditing, newRatingData]) => {
    console.log('Watch triggered:', { newIsEditing, newRatingData });

    productRatings.value = {};
    productComments.value = {};
    imageFiles.value = {};
    imagePreviews.value = {};
    videoFiles.value = {};
    videoPreviews.value = {};
    existingMedia.value = {};
    deletedMediaIds.value = {};

    if (newIsEditing && newRatingData) {
      newRatingData.forEach(rating => {
        const productId = rating.idSanPhamChiTiet;
        productRatings.value[productId] = rating.soSao;
        productComments.value[productId] = rating.noiDung || '';
        existingMedia.value[productId] = rating.media || [];
        deletedMediaIds.value[productId] = []; // Khởi tạo mảng rỗng cho deletedMediaIds
      });
      console.log('State after watch:', {
        productRatings: productRatings.value,
        productComments: productComments.value,
        existingMedia: existingMedia.value,
        deletedMediaIds: deletedMediaIds.value,
      });
    } else {
      console.log('Reset state for create mode');
      props.orderProducts.forEach(product => {
        const productId = product.idSanPhamChiTiet;
        productRatings.value[productId] = 0;
        productComments.value[productId] = '';
        imageFiles.value[productId] = [];
        imagePreviews.value[productId] = [];
        videoFiles.value[productId] = [];
        videoPreviews.value[productId] = [];
        existingMedia.value[productId] = [];
        deletedMediaIds.value[productId] = [];
        isImageDragOver.value[productId] = false;
        isVideoDragOver.value[productId] = false;
      });
    }
  },
  { immediate: true, deep: true }
);

// Kiểm tra props
watch(
  () => props,
  (newProps) => {
    console.log('Props nhận được:', {
      isOpen: newProps.isOpen,
      orderId: newProps.orderId,
      isEditing: newProps.isEditing,
      orderProducts: newProps.orderProducts?.map((p, index) => ({
        index,
        idSanPhamChiTiet: p.idSanPhamChiTiet,
        idChiTietHoaDon: p.idChiTietHoaDon,
        tenSanPham: p.tenSanPham,
      })),
      existingRatingData: newProps.existingRatingData,
    });

    if (!newProps.orderProducts || !Array.isArray(newProps.orderProducts)) {
      console.error('Lỗi: orderProducts không hợp lệ', { orderProducts: newProps.orderProducts });
      toast.error('Dữ liệu sản phẩm không hợp lệ!');
    }
  },
  { immediate: true, deep: true }
);

const setRating = (productId, value) => {
  console.log('setRating gọi với:', { productId, value });
  const product = props.orderProducts.find(p => p.idSanPhamChiTiet === productId);
  if (!product) {
    console.error(`ID sản phẩm chi tiết không hợp lệ: ${productId}`);
    toast.error(`ID sản phẩm chi tiết không hợp lệ: ${productId}`);
    return;
  }
  productRatings.value = { ...productRatings.value, [productId]: value };
  console.log('productRatings sau khi set:', productRatings.value);
};

const handleImageUpload = (event, productId) => {
  console.log('handleImageUpload được gọi cho productId:', productId);
  const files = event.target.files || event.dataTransfer?.files;
  if (!files || files.length === 0) {
    console.warn('Không có file nào được chọn hoặc kéo thả');
    toast.warning('Vui lòng chọn hoặc kéo thả ít nhất một ảnh!');
    return;
  }

  const newFiles = Array.from(files);
  console.log('Danh sách file ảnh:', newFiles.map(f => ({ name: f.name, size: f.size, type: f.type })));

  if (((imageFiles.value[productId] || []).length + (existingMedia.value[productId] || []).filter(m => m.type === 'image').length + newFiles.length) > MAX_IMAGES) {
    console.warn(`Vượt quá giới hạn ${MAX_IMAGES} ảnh cho sản phẩm ${productId}!`);
    toast.warning(`Bạn chỉ có thể tải lên tối đa ${MAX_IMAGES} ảnh cho sản phẩm này!`);
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
    if (!['image/jpeg', 'image/png', 'image/webp'].includes(file.type)) {
      console.warn(`Ảnh không đúng định dạng JPG/PNG: ${file.name}`);
      toast.warning(`Ảnh ${file.name} phải có định dạng JPG hoặc PNG!`);
      return true;
    }
    if ((imageFiles.value[productId] || []).some(existingFile => existingFile.name === file.name)) {
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
    imageFiles.value[productId] = [...(imageFiles.value[productId] || []), file];
    imagePreviews.value[productId] = [...(imagePreviews.value[productId] || []), { file, url: previewUrl }];
    console.log(`Thêm ảnh cho productId ${productId}: ${file.name}, URL: ${previewUrl}`);
  });

  // Cập nhật imageFiles.value để đảm bảo Vue reactivity
  imageFiles.value = { ...imageFiles.value };
};

const handleVideoUpload = (event, productId) => {
  console.log('handleVideoUpload được gọi cho productId:', productId);
  const files = Array.from(event.target.files || event.dataTransfer?.files);
  console.log('Danh sách file video:', files.map(f => ({ name: f.name, size: f.size, type: f.type })));

  if (((videoFiles.value[productId] || []).length + (existingMedia.value[productId] || []).filter(m => m.type === 'video').length + files.length) > MAX_VIDEOS) {
    console.warn(`Vượt quá giới hạn ${MAX_VIDEOS} video cho sản phẩm ${productId}!`);
    toast.warning(`Bạn chỉ có thể tải lên tối đa ${MAX_VIDEOS} video cho sản phẩm này!`);
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
      toast.warning(`Video ${file.name} vượt quá 50MB!`);
      return true;
    }
    if (!['video/mp4', 'video/webm'].includes(file.type)) {
      console.warn(`Video không đúng định dạng MP4/WebM: ${file.name}`);
      toast.warning(`Video ${file.name} phải có định dạng MP4 hoặc WebM!`);
      return true;
    }
    if ((videoFiles.value[productId] || []).some(existingFile => existingFile.name === file.name)) {
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
    videoFiles.value[productId] = [...(videoFiles.value[productId] || []), file];
    videoPreviews.value[productId] = [...(videoPreviews.value[productId] || []), { file, url: previewUrl }];
    console.log(`Thêm video cho productId ${productId}: ${file.name}, URL: ${previewUrl}`);
  });
};

const onDragOver = (event, type, productId) => {
  event.preventDefault();
  if (type === 'image') isImageDragOver.value[productId] = true;
  else isVideoDragOver.value[productId] = true;
};

const onDragEnter = (event, type, productId) => {
  event.preventDefault();
  if (type === 'image') isImageDragOver.value[productId] = true;
  else isVideoDragOver.value[productId] = true;
};

const onDragLeave = (event, type, productId) => {
  event.preventDefault();
  if (type === 'image') isImageDragOver.value[productId] = false;
  else isVideoDragOver.value[productId] = false;
};

const onImageDrop = (event, productId) => {
  isImageDragOver.value[productId] = false;
  handleImageUpload(event, productId);
};

const onVideoDrop = (event, productId) => {
  isVideoDragOver.value[productId] = false;
  handleVideoUpload(event, productId);
};

const removeImage = (productId, index) => {
  console.log(`Xóa ảnh tại index ${index} cho productId ${productId}:`, imageFiles.value[productId][index]?.name);
  URL.revokeObjectURL(imagePreviews.value[productId][index].url);
  imageFiles.value[productId].splice(index, 1);
  imagePreviews.value[productId].splice(index, 1);
  imageFiles.value = { ...imageFiles.value };
  imagePreviews.value = { ...imagePreviews.value };
};

const removeVideo = (productId, index) => {
  console.log(`Xóa video tại index ${index} cho productId ${productId}:`, videoFiles.value[productId][index]?.name);
  URL.revokeObjectURL(videoPreviews.value[productId][index].url);
  videoFiles.value[productId].splice(index, 1);
  videoPreviews.value[productId].splice(index, 1);
  videoFiles.value = { ...videoFiles.value };
  videoPreviews.value = { ...videoPreviews.value };
};

const removeExistingMedia = (productId, id) => {
  console.log(`Đánh dấu xóa media với id ${id} cho productId ${productId}:`, existingMedia.value[productId].find(m => m.id === id)?.url);
  // Không gọi API xóa ngay lập tức
  existingMedia.value[productId] = existingMedia.value[productId].filter(m => m.id !== id); // Cập nhật giao diện
  deletedMediaIds.value[productId] = [...(deletedMediaIds.value[productId] || []), id]; // Lưu ID media để xóa sau
  existingMedia.value = { ...existingMedia.value };
  deletedMediaIds.value = { ...deletedMediaIds.value };
  console.log(`Updated deletedMediaIds for productId ${productId}:`, deletedMediaIds.value[productId]);
};

const closeDialog = () => {
  console.log('closeDialog được gọi');
  Object.keys(imagePreviews.value).forEach(productId => {
    imagePreviews.value[productId].forEach(item => {
      console.log(`Thu hồi URL ảnh cho productId ${productId}: ${item.file?.name}`);
      URL.revokeObjectURL(item.url);
    });
  });
  Object.keys(videoPreviews.value).forEach(productId => {
    videoPreviews.value[productId].forEach(item => {
      console.log(`Thu hồi URL video cho productId ${productId}: ${item.file?.name}`);
      URL.revokeObjectURL(item.url);
    });
  });
  productRatings.value = {};
  productComments.value = {};
  imageFiles.value = {};
  imagePreviews.value = {};
  videoFiles.value = {};
  videoPreviews.value = {};
  existingMedia.value = {};
  deletedMediaIds.value = {};
  isImageDragOver.value = {};
  isVideoDragOver.value = {};
  emit('close');
};

const submitRating = async () => {
  console.log('submitRating được gọi');
  console.log('Trạng thái trước khi gửi:', {
    productRatings: productRatings.value,
    productComments: productComments.value,
    imageFiles: Object.keys(imageFiles.value).reduce((acc, productId) => ({
      ...acc,
      [productId]: imageFiles.value[productId].map(f => ({ name: f.name, size: f.size, type: f.type })),
    }), {}),
    videoFiles: Object.keys(videoFiles.value).reduce((acc, productId) => ({
      ...acc,
      [productId]: videoFiles.value[productId].map(f => ({ name: f.name, size: f.size, type: f.type })),
    }), {}),
    existingMedia: existingMedia.value,
    deletedMediaIds: deletedMediaIds.value,
  });

  const ratings = props.idSanPhamChiTietList.map(item => {
    const product = props.orderProducts.find(p => p.idSanPhamChiTiet === item.idSanPhamChiTiet);
    if (!product) {
      console.error(`Không tìm thấy sản phẩm với idSanPhamChiTiet: ${item.idSanPhamChiTiet}`);
      return null;
    }
    return {
      idSanPhamChiTiet: item.idSanPhamChiTiet,
      idChiTietHoaDon: item.idChiTietHoaDon,
      soSao: productRatings.value[item.idSanPhamChiTiet] || 1,
      noiDung: productComments.value[item.idSanPhamChiTiet] || '',
      imageFiles: imageFiles.value[item.idSanPhamChiTiet] || [],
      videoFiles: videoFiles.value[item.idSanPhamChiTiet] || [],
      existingMedia: existingMedia.value[item.idSanPhamChiTiet] || [],
      idDanhGia: props.existingRatingData?.find(r => r.idSanPhamChiTiet === item.idSanPhamChiTiet)?.idDanhGia || null,
      deletedMediaIds: deletedMediaIds.value[item.idSanPhamChiTiet] || [], // Thêm deletedMediaIds
    };
  }).filter(r => r !== null);

  if (ratings.length === 0 || ratings.some(r => !r.soSao)) {
    console.warn('Không có đánh giá hợp lệ hoặc chưa đánh giá sản phẩm');
    toast.error('Vui lòng đánh giá ít nhất một sản phẩm!');
    isSubmitting.value = false;
    return;
  }

  isSubmitting.value = true;
  try {
    const payload = {
      idHoaDon: props.orderId,
      ratings,
      trangThaiDanhGia: 'APPROVED',
    };
    console.log('Payload gửi đi:', JSON.stringify(payload, null, 2));
    await emit('submit', { payload });
  } catch (error) {
    console.error('Lỗi khi gửi đánh giá:', error);
    toast.error('Gửi đánh giá thất bại. Vui lòng thử lại.');
  } finally {
    isSubmitting.value = false;
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
  gap: 12px; /* Tăng khoảng cách để chứa comment và media */
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
  align-items: center;
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

.debug-info {
  font-size: 0.75rem;
  color: #6b7280;
  margin-right: 8px;
}
</style>