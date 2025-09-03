  <template>
    <div class="container mx-auto p-6">
      <el-card class="shadow-xl bg-white">
        <el-row :gutter="16" class="mb-6">
          <el-col :span="12" class="text-2xl font-bold text-gray-900">
            Danh sách đánh giá sản phẩm
          </el-col>
          <el-col :span="12">
            <el-row :gutter="16" align="middle">
              <el-col :span="16">
                <el-select v-model="filterStar" placeholder="Lọc số sao" @change="loadDanhGia" class="w-full">
                  <el-option label="Tất cả" :value="null" />
                  <el-option label="1 sao" :value="1" />
                  <el-option label="2 sao" :value="2" />
                  <el-option label="3 sao" :value="3" />
                  <el-option label="4 sao" :value="4" />
                  <el-option label="5 sao" :value="5" />
                </el-select>
              </el-col>
              <el-col :span="8">
                <el-button type="default" @click="resetFilters" class="hover:bg-gray-100 transition w-full">
                  Xóa bộ lọc
                </el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="dataReview" style="width: 100%" border class="rounded-lg bg-gray-50"
          :row-class-name="tableRowClassName">
          <el-table-column type="index" label="STT" width="60" :index="indexMethod" align="center" />
          <el-table-column prop="tenKhachHang" label="Khách hàng" width="150" />
          <el-table-column prop="maSanPhamChiTiet" label="Mã sản phẩm" width="120" align="center" />
          <el-table-column prop="tenSanPham" label="Sản phẩm" width="160" />
          <el-table-column prop="tenMau" label="Màu sắc" width="120" align="center" />
          <el-table-column prop="dungLuongRam" label="RAM" width="100" align="center" />
          <el-table-column prop="dungLuongRom" label="ROM" width="100" align="center" />
          <el-table-column label="Sao" width="200" align="center">
            <template #default="{ row }">
              <el-rate v-model="row.soSao" disabled :max="5" class="text-yellow-400" />
            </template>
          </el-table-column>
          <el-table-column prop="noiDung" label="Nội dung" width="400" />
          <el-table-column prop="ngayDanhGia" label="Ngày đánh giá" width="180" align="center" />
          <el-table-column label="Ảnh" width="200" align="center">
            <template #default="{ row }">
              <el-row v-if="row.anhUrls && row.anhUrls.length > 0" :gutter="8">
                <el-col v-for="(img, index) in row.anhUrls" :key="index" :span="6">
                  <el-image
                    :src="img"
                    class="w-24 h-24 rounded-lg border border-gray-200 hover:border-blue-500 transition-all duration-300 shadow-sm bg-white"
                    fit="contain"
                    :preview-src-list="row.anhUrls"
                    :initial-index="index"
                    preview-teleported
                  >
                    <template #error>
                      <div class="image-error">
                        <el-icon><Picture /></el-icon>
                      </div>
                    </template>
                  </el-image>
                </el-col>
              </el-row>
              <span v-else class="text-gray-500">Không có hình ảnh</span>
            </template>
          </el-table-column>
          <el-table-column label="Video" width="200" align="center">
            <template #default="{ row }">
              <el-row v-if="row.videoUrls && row.videoUrls.length > 0" :gutter="8">
                <el-col v-for="(vid, idx) in row.videoUrls" :key="idx" :span="6">
                  <video
                    :src="vid"
                    class="w-16 h-12 rounded-md border border-gray-200 hover:border-blue-400 transition cursor-pointer"
                    controls
                    @click.stop="openVideoDialog(vid)"
                  ></video>
                </el-col>
              </el-row>
              <span v-else class="text-gray-500">Không có video</span>
            </template>
          </el-table-column>
          <el-table-column prop="noiDungPhanHoi" label="Phản hồi" width="300">
            <template #default="{ row }">
              <div v-if="row.noiDungPhanHoi" class="text-sm text-gray-700">
                {{ row.noiDungPhanHoi }}
              </div>
              <span v-else class="text-gray-400 italic">Chưa có phản hồi</span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="Trạng thái" width="150" align="center">
            <template #default="{ row }">
              <span :class="{
                'text-green-600 font-semibold': row.trangThaiDanhGia === 'APPROVED',
                'text-yellow-600 font-semibold': row.trangThaiDanhGia === 'PENDING_APPROVAL',
                'text-red-600 font-semibold': row.trangThaiDanhGia === 'REFUSE',
                'text-gray-600 font-semibold': row.trangThaiDanhGia === 'HIDE',
              }">
                {{ mapStatusToVietnamese(row.trangThaiDanhGia) }}
              </span>
            </template>
          </el-table-column> -->
          <el-table-column label="Hành động" width="400" align="center">
            <template #default="{ row }">
              <el-row :gutter="8" justify="center">
                <el-col :span="12">
                  <el-button
                    v-if="!row.noiDungPhanHoi"
                    type="info"
                    size="small"
                    @click="openReplyDialog(row)"
                    class="hover:bg-blue-600 transition w-full"
                  >
                    Phản hồi
                  </el-button>
                  <el-button
                    v-else
                    type="warning"
                    size="small"
                    @click="openUpdateReplyDialog(row)"
                    class="hover:bg-orange-600 transition w-full"
                  >
                    Cập nhật phản hồi
                  </el-button>
                </el-col>
                <el-col :span="12">
                  <el-button
                    type="danger"
                    size="small"
                    @click="handleDelete(row.idDanhGia)"
                    class="hover:bg-red-600 transition w-full"
                  >
                    Xóa
                  </el-button>
                </el-col>
              </el-row>
            </template>
          </el-table-column>
        </el-table>

        
        <div class="pagination-fixed">
          <!-- <div class="pagination-info">
            <el-tag type="info" effect="light" class="info-tag">
              Hiển thị {{ fromRecord }} - {{ toRecord }} trong tổng số {{ totalItems }} sản phẩm
            </el-tag>
          </div> -->
          <div class="d-flex justify-content-center align-items-center gap-3">
            <el-pagination background layout="prev, pager, next" :page-size="pageSize" :current-page="currentPage"
              :total="totalItems" :pager-count="7" prev-text="< Trước" next-text="Sau >"
              @current-change="handlePageChange" class="simple-pagination" />
          </div>
        </div>

        <!-- <div class="pagination-wrapper mt-6">
          <el-row justify="center">
            <el-col :span="24">
              <el-pagination
                background
                layout="prev, pager, next, total"
                :current-page="currentPage"
                :page-size="pageSize"
                :total="totalItems"
                @current-change="handlePageChange"
                class="bg-white p-2 rounded-lg shadow-sm"
              />
            </el-col>
          </el-row>
        </div> -->
      </el-card>

      <!-- Dialog phản hồi -->
      <el-dialog
        :title="isUpdateMode ? 'Cập nhật phản hồi đánh giá' : 'Phản hồi đánh giá'"
        v-model="dialogReplyVisible"
        width="60%"
        :before-close="handleCloseReplyDialog"
        class="rounded-xl shadow-2xl bg-white"
      >
        <el-form :model="replyForm" :rules="replyRules" ref="replyFormRef" label-position="top" class="p-6">
          <div class="mb-6 bg-gray-50 p-4 rounded-lg shadow-sm">
            <h3 class="text-xl font-semibold text-gray-800 mb-4">Thông tin đánh giá</h3>
            <el-row :gutter="24">
              <el-col :span="12" :xs="24">
                <div class="space-y-3">
                  <p class="text-gray-700"><strong class="text-gray-900">Khách hàng:</strong> {{ replyForm.tenKhachHang }}</p>
                  <p class="text-gray-700"><strong class="text-gray-900">Sản phẩm:</strong> {{ replyForm.tenSanPham }} ({{ replyForm.maSanPhamChiTiet }})</p>
                  <p class="text-gray-700"><strong class="text-gray-900">Màu sắc:</strong> {{ replyForm.tenMau }}</p>
                  <p class="text-gray-700"><strong class="text-gray-900">RAM/ROM:</strong> {{ replyForm.dungLuongRam }}/{{ replyForm.dungLuongRom }}</p>
                  <p class="text-gray-700"><strong class="text-gray-900">Đánh giá:</strong> {{ replyForm.noiDung }}</p>
                  <p class="text-gray-700"><strong class="text-gray-900">Số sao:</strong></p>
                  <el-rate v-model="replyForm.soSao" disabled :max="5" class="text-yellow-400 text-lg" />
                </div>
              </el-col>
              <el-col :span="12" :xs="24">
                <div class="space-y-3">
                  <p class="text-gray-700"><strong class="text-gray-900">Ảnh đánh giá:</strong></p>
                  <el-row :gutter="12">
                    <el-col v-for="(img, index) in replyForm.anhUrls" :key="index" :span="8" :xs="12">
                      <el-image
                        :src="img"
                        class="w-24 h-24 rounded-lg border border-gray-200 hover:border-blue-500 transition-all duration-300 shadow-sm"
                        fit="cover"
                        :preview-src-list="replyForm.anhUrls"
                        :initial-index="index"
                      />
                    </el-col>
                  </el-row>
                  <p class="mt-3 text-gray-700"><strong class="text-gray-900">Video đánh giá:</strong></p>
                  <el-row :gutter="12">
                    <el-col v-for="(vid, idx) in replyForm.videoUrls" :key="idx" :span="8" :xs="12">
                      <video
                        :src="vid"
                        class="w-28 h-20 rounded-lg border border-gray-200 hover:border-blue-500 transition-all duration-300 shadow-sm"
                        controls
                      ></video>
                    </el-col>
                  </el-row>
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- Hiển thị phản hồi hiện tại nếu đang ở chế độ cập nhật -->
          <div v-if="isUpdateMode && originalReplyContent" class="mb-6 bg-blue-50 p-4 rounded-lg shadow-sm">
            <h4 class="text-lg font-semibold text-blue-800 mb-2">Phản hồi hiện tại:</h4>
            <p class="text-blue-700 italic">"{{ originalReplyContent }}"</p>
          </div>

          <el-form-item label="Chọn gợi ý phản hồi" class="mb-6">
            <el-select
              v-model="selectedSuggestion"
              placeholder="Chọn gợi ý phản hồi"
              class="w-full"
              @change="applySuggestion"
              clearable
            >
              <el-option
                v-for="(suggestion, index) in replySuggestions[replyForm.soSao] || []"
                :key="index"
                :label="suggestion"
                :value="suggestion"
              />
            </el-select>
          </el-form-item>
          <el-form-item 
            :label="isUpdateMode ? 'Nội dung phản hồi mới' : 'Nội dung phản hồi'" 
            prop="noiDungPhanHoi" 
            class="mb-6"
          >
            <el-input
              type="textarea"
              v-model="replyForm.noiDungPhanHoi"
              :placeholder="isUpdateMode ? 'Nhập nội dung phản hồi mới' : 'Nhập nội dung phản hồi'"
              :rows="5"
              class="w-full rounded-lg border-gray-300 focus:border-blue-500 focus:ring focus:ring-blue-200 transition-all duration-300"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-row :gutter="16" justify="end" class="p-6">
            <el-col :span="6" :xs="12">
              <el-button
                @click="dialogReplyVisible = false"
                class="bg-gray-100 text-gray-700 hover:bg-gray-200 hover:text-gray-900 transition-all duration-300 rounded-lg w-full"
              >
                Hủy
              </el-button>
            </el-col>
            <el-col :span="6" :xs="12">
              <el-button
                type="primary"
                @click="submitReplyForm"
                :loading="submitting"
                class="bg-blue-600 text-white hover:bg-blue-700 transition-all duration-300 rounded-lg w-full"
              >
                {{ isUpdateMode ? 'Cập nhật' : 'Gửi' }}
              </el-button>
            </el-col>
          </el-row>
        </template>
      </el-dialog>

      <!-- Dialog xem video đánh giá -->
      <el-dialog
        v-model="dialogVideoVisible"
        width="700px"
        class="rounded-xl shadow-2xl bg-white"
        :close-on-click-modal="true"
        title="Xem video đánh giá"
      >
        <video v-if="currentVideoUrl" :src="currentVideoUrl" class="w-full h-auto rounded-lg shadow-sm" controls></video>
      </el-dialog>
    </div>
  </template>

  <script setup>
  import { ref, onMounted } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { DanhGiaSanPhamAdminService } from '@/Service/Adminservice/DanhGiaSanPhamAdminService/DanhGiaSanPhamAdminService';
  import { useToast } from 'vue-toastification';
  import { PhanHoiDanhGiaAdminService } from '@/Service/Adminservice/PhanHoiSanPhamService/PhanHoiDanhGiaAdminService';

  const toast = useToast();
  const dataReview = ref([]);
  const loading = ref(false);
  const submitting = ref(false);
  const currentPage = ref(1);
  const totalPages = ref(1);
  const totalItems = ref(0);
  const pageSize = ref(5);
  const filterStatus = ref(null);
  const filterStar = ref(null);
  const dialogReplyVisible = ref(false);
  const dialogVideoVisible = ref(false);
  const currentVideoUrl = ref('');
  const selectedSuggestion = ref('');
  const isUpdateMode = ref(false);
  const originalReplyContent = ref('');

  const replyForm = ref({
    idDanhGia: null,
    idPhanHoi: null,
    tenKhachHang: '',
    tenSanPham: '',
    maSanPhamChiTiet: '',
    tenMau: '',
    dungLuongRam: '',
    dungLuongRom: '',
    soSao: 0,
    noiDung: '',
    anhUrls: [],
    videoUrls: [],
    noiDungPhanHoi: '',
  });

  const replyRules = ref({
    noiDungPhanHoi: [{ required: true, message: 'Vui lòng nhập nội dung phản hồi', trigger: 'blur' }],
  });

  const replySuggestions = ref({
    1: [
      'Cảm ơn bạn đã chia sẻ trải nghiệm. Chúng tôi rất tiếc vì sản phẩm chưa đáp ứng mong đợi. Vui lòng liên hệ đội ngũ hỗ trợ để chúng tôi có thể giải quyết vấn đề nhanh chóng!',
      'Chúng tôi xin lỗi vì trải nghiệm không như ý. Bạn có thể cung cấp thêm chi tiết về vấn đề gặp phải để chúng tôi cải thiện và hỗ trợ tốt hơn không?',
      'Cảm ơn ý kiến quý giá của bạn. Chúng tôi sẽ xem xét kỹ lưỡng để nâng cấp chất lượng sản phẩm và dịch vụ trong tương lai.'
    ],
    2: [
      'Cảm ơn bạn đã để lại đánh giá. Chúng tôi rất trân trọng ý kiến của bạn và sẽ nỗ lực cải thiện để mang đến trải nghiệm tốt hơn.',
      'Chúng tôi rất tiếc vì sản phẩm chưa hoàn toàn làm bạn hài lòng. Bạn có thể chia sẻ thêm thông tin để chúng tôi hỗ trợ kịp thời không?',
      'Ý kiến của bạn giúp chúng tôi hoàn thiện hơn. Chúng tôi sẽ xem xét và cải tiến sản phẩm dựa trên phản hồi của bạn.'
    ],
    3: [
      'Cảm ơn bạn đã dành thời gian đánh giá. Chúng tôi sẽ tiếp tục cải thiện để mang đến sản phẩm và dịch vụ tốt hơn cho bạn!',
      'Rất trân trọng ý kiến của bạn. Chúng tôi sẽ ghi nhận để nâng cao chất lượng sản phẩm và trải nghiệm khách hàng.',
      'Cảm ơn bạn đã chia sẻ cảm nhận. Chúng tôi hy vọng sẽ mang đến trải nghiệm tuyệt vời hơn trong lần mua sắm tiếp theo!'
    ],
    4: [
      'Cảm ơn bạn đã yêu thích sản phẩm của chúng tôi! Sự hài lòng của bạn là động lực để chúng tôi tiếp tục cải tiến.',
      'Rất vui khi bạn đánh giá cao sản phẩm. Chúng tôi sẽ nỗ lực để duy trì chất lượng và mang đến những trải nghiệm tốt hơn nữa!',
      'Cảm ơn bạn đã ủng hộ! Chúng tôi mong được phục vụ bạn với những sản phẩm chất lượng hơn trong tương lai.'
    ],
    5: [
      'Cảm ơn bạn đã đánh giá 5 sao! Sự yêu mến của bạn là nguồn động lực lớn để chúng tôi tiếp tục mang đến những sản phẩm tuyệt vời.',
      'Chúng tôi rất hạnh phúc khi bạn hài lòng với sản phẩm! Mong được đồng hành cùng bạn trong những lần mua sắm tiếp theo.',
      'Cảm ơn bạn đã tin tưởng và ủng hộ! Chúng tôi sẽ tiếp tục nỗ lực để mang đến những trải nghiệm hoàn hảo hơn.'
    ],
  });

  const replyFormRef = ref(null);
  const user = ref(JSON.parse(localStorage.getItem('user')) || null);

  // Ánh xạ trạng thái sang tiếng Việt
  const mapStatusToVietnamese = (status) => {
    switch (status) {
      case 'PENDING_APPROVAL':
        return 'Chờ duyệt';
      case 'APPROVED':
        return 'Đã duyệt';
      case 'REFUSE':
        return 'Từ chối';
      case 'HIDE':
        return 'Ẩn';
      default:
        return status;
    }
  };

  // Tùy chỉnh lớp CSS cho hàng bảng
  const tableRowClassName = ({ row }) => {
    return row.trangThaiDanhGia === 'APPROVED'
      ? 'bg-green-50'
      : row.trangThaiDanhGia === 'PENDING_APPROVAL'
        ? 'bg-yellow-50'
        : row.trangThaiDanhGia === 'REFUSE'
          ? 'bg-red-50'
          : 'bg-gray-50';
  };

  // Hàm tính toán STT
  const indexMethod = (index) => {
    return index + 1 + (currentPage.value - 1) * pageSize.value;
  };

  // Xóa bộ lọc
  const resetFilters = () => {
    filterStar.value = null;
    filterStatus.value = null;
    currentPage.value = 1;
    loadDanhGia();
  };

  // Tải danh sách đánh giá
  const loadDanhGia = async () => {
    loading.value = true;
    try {
      if (filterStar.value !== null && (filterStar.value < 1 || filterStar.value > 5)) {
        toast.error('Số sao không hợp lệ. Vui lòng chọn từ 1 đến 5.');
        return;
      }
      const response = await DanhGiaSanPhamAdminService.layTatCaDanhGiaAdmin(
        currentPage.value - 1,
        pageSize.value,
        filterStar.value,
        filterStatus.value
      );
      dataReview.value = response.content.map(item => ({
        ...item,
        anhUrls: Array.isArray(item.anhUrls) ? item.anhUrls : item.anhUrls ? item.anhUrls.split(',') : [],
        videoUrls: Array.isArray(item.videoUrls) ? item.videoUrls : item.videoUrls ? item.videoUrls.split(',') : [],
        soSao: Number(item.soSao),
        noiDungPhanHoi: item.noiDungPhanHoi || '',
      }));
      totalPages.value = response.totalPages;
      totalItems.value = response.totalElements;
    } catch (error) {
      toast.error('Không thể tải dữ liệu đánh giá. Vui lòng thử lại sau.');
      console.error('Lỗi khi tải đánh giá:', error);
    } finally {
      loading.value = false;
    }
  };

  // Xử lý thay đổi trang
  const handlePageChange = (page) => {
    currentPage.value = page;
    loadDanhGia();
  };

  // Mở dialog phản hồi mới
  const openReplyDialog = (row) => {
    if (row.noiDungPhanHoi) {
      toast.error('Đánh giá này đã có phản hồi. Vui lòng chọn "Cập nhật phản hồi".');
      return;
    }
    isUpdateMode.value = false;
    originalReplyContent.value = '';
    replyForm.value = {
      idDanhGia: row.idDanhGia,
      idPhanHoi: null,
      tenKhachHang: row.tenKhachHang,
      tenSanPham: row.tenSanPham,
      maSanPhamChiTiet: row.maSanPhamChiTiet,
      tenMau: row.tenMau,
      dungLuongRam: row.dungLuongRam,
      dungLuongRom: row.dungLuongRom,
      soSao: row.soSao,
      noiDung: row.noiDung,
      anhUrls: row.anhUrls,
      videoUrls: row.videoUrls,
      noiDungPhanHoi: '',
    };
    selectedSuggestion.value = '';
    dialogReplyVisible.value = true;
  };

  // Mở dialog cập nhật phản hồi
  // Mở dialog cập nhật phản hồi
  const openUpdateReplyDialog = async (row) => {
    try {
      console.log('Mở dialog cập nhật phản hồi cho đánh giá:', row.idDanhGia);
      isUpdateMode.value = true;
      originalReplyContent.value = row.noiDungPhanHoi;

      // Lấy thông tin chi tiết phản hồi
      const phanHoiData = await DanhGiaSanPhamAdminService.layPhanHoiDanhGia(row.idDanhGia);
      console.log('Dữ liệu phản hồi:', phanHoiData); // Debug log

      // Kiểm tra dữ liệu trả về là mảng và có phần tử đầu tiên
      if (!phanHoiData || !Array.isArray(phanHoiData) || phanHoiData.length === 0 || !phanHoiData[0].idPhanHoi) {
        throw new Error('Không tìm thấy ID phản hồi để cập nhật.');
      }

      // Lấy phần tử đầu tiên của mảng
      const phanHoi = phanHoiData[0];

      replyForm.value = {
        idDanhGia: row.idDanhGia,
        idPhanHoi: phanHoi.idPhanHoi, // Sử dụng idPhanHoi thay vì id
        tenKhachHang: row.tenKhachHang,
        tenSanPham: row.tenSanPham,
        maSanPhamChiTiet: row.maSanPhamChiTiet,
        tenMau: row.tenMau,
        dungLuongRam: row.dungLuongRam,
        dungLuongRom: row.dungLuongRom,
        soSao: row.soSao,
        noiDung: row.noiDung,
        anhUrls: row.anhUrls,
        videoUrls: row.videoUrls,
        noiDungPhanHoi: phanHoi.noiDungPhanHoi || row.noiDungPhanHoi || '',
      };
      selectedSuggestion.value = '';
      dialogReplyVisible.value = true;
    } catch (error) {
      console.error('Lỗi khi lấy thông tin phản hồi:', error);
      toast.error('Không thể tải thông tin phản hồi. Vui lòng thử lại.');
      dialogReplyVisible.value = false;
    }
  };

  // Áp dụng gợi ý phản hồi
  const applySuggestion = (suggestion) => {
    if (suggestion) {
      replyForm.value.noiDungPhanHoi = suggestion;
    }
  };

  // Đóng dialog phản hồi
  const handleCloseReplyDialog = (done) => {
    ElMessageBox.confirm('Bạn có chắc muốn đóng mà không lưu?', 'Xác nhận', {
      confirmButtonText: 'Đồng ý',
      cancelButtonText: 'Hủy',
      type: 'warning',
    })
      .then(() => {
        done();
      })
      .catch(() => {});
  };

  // Gửi phản hồi hoặc cập nhật phản hồi
  const submitReplyForm = () => {
    replyFormRef.value.validate(async (valid) => {
      if (valid) {
        submitting.value = true;
        try {
          const idNhanVien = user.value?.id;
          if (!idNhanVien) {
            throw new Error('ID nhân viên không tồn tại. Vui lòng đăng nhập lại.');
          }

          if (isUpdateMode.value) {
            if (!replyForm.value.idPhanHoi) {
              throw new Error('ID phản hồi không tồn tại để cập nhật.');
            }
            await PhanHoiDanhGiaAdminService.capNhatPhanHoi(
              replyForm.value.idPhanHoi,
              {
                noiDungPhanHoi: replyForm.value.noiDungPhanHoi,
                idNhanVien: idNhanVien,
              }
            );
            toast.success('Cập nhật phản hồi thành công');
          } else {
            await PhanHoiDanhGiaAdminService.phanHoiDanhGia(
              replyForm.value.idDanhGia,
              {
                noiDungPhanHoi: replyForm.value.noiDungPhanHoi,
                idNhanVien: idNhanVien,
              }
            );
            toast.success('Gửi phản hồi thành công');
          }
          
          dialogReplyVisible.value = false;
          loadDanhGia();
        } catch (error) {
          if (error.response && error.response.data && error.response.data.message) {
            toast.error(error.response.data.message);
          } else {
            const action = isUpdateMode.value ? 'cập nhật' : 'gửi';
            toast.error(`Lỗi khi ${action} phản hồi: ${error.message}`);
          }
          console.error('❌ Lỗi khi xử lý phản hồi:', error);
        } finally {
          submitting.value = false;
        }
      } else {
        console.log('Form validation failed');
      }
    });
  };


  const handleApprove = async (id) => {
    try {
      await ElMessageBox.confirm(
        'Bạn có chắc muốn phê duyệt đánh giá này?',
        'Xác nhận',
        {
          confirmButtonText: 'Phê duyệt',
          cancelButtonText: 'Hủy',
          type: 'warning',
        }
      );

      await DanhGiaSanPhamAdminService.pheDuyetDanhGia(id);
      toast.success('✅ Phê duyệt đánh giá thành công');
      loadDanhGia();
    } catch (error) {
      if (error === 'cancel' || error === 'close') return;
      toast.error('❌ Lỗi khi phê duyệt đánh giá. Vui lòng thử lại.');
      console.error('Lỗi khi phê duyệt:', error);
    }
  };

  // ✅ Xử lý từ chối đánh giá
  const handleReject = async (id) => {
    try {
      await ElMessageBox.confirm(
        'Bạn có chắc muốn từ chối đánh giá này?',
        'Xác nhận',
        {
          confirmButtonText: 'Từ chối',
          cancelButtonText: 'Hủy',
          type: 'warning',
        }
      );

      await DanhGiaSanPhamAdminService.tuChoiDanhGia(id);
      toast.success('✅ Từ chối đánh giá thành công');
      loadDanhGia();
    } catch (error) {
      if (error === 'cancel' || error === 'close') return;
      toast.error('❌ Lỗi khi từ chối đánh giá. Vui lòng thử lại.');
      console.error('Lỗi khi từ chối:', error);
    }
  };

  // ✅ Xử lý xóa đánh giá
  const handleDelete = async (id) => {
    try {
      await ElMessageBox.confirm(
        'Bạn có chắc muốn xóa đánh giá này?',
        'Xác nhận',
        {
          confirmButtonText: 'Xóa',
          cancelButtonText: 'Hủy',
          type: 'danger',
        }
      );
      await DanhGiaSanPhamAdminService.xoaDanhGia(id);
      toast.success('✅ Xóa đánh giá thành công');
      loadDanhGia();
    } catch (error) {
      if (error === 'cancel' || error === 'close') return;
      toast.error('❌ Lỗi khi xóa đánh giá. Vui lòng thử lại.');
      console.error('Lỗi khi xóa:', error);
    }
  };

  // Mở dialog xem video đánh giá
  const openVideoDialog = (url) => {
    currentVideoUrl.value = url;
    dialogVideoVisible.value = true;
  };

  onMounted(() => {
    loadDanhGia();
  });
  </script>

  <style scoped>
  /* Container chính */
  .container {
    max-width: 1400px;
    margin: 0 auto;
    padding: 24px;
    background: linear-gradient(135deg, #f4f7fe 0%, #e0e7ff 100%);
    min-height: 100vh;
  }

  .d-flex {
    display: flex;
  }

  .justify-content-center {
    justify-content: center;
  }

  .align-items-center {
    align-items: center;
  }

  .gap-3 {
    gap: 12px;
  }

  .simple-pagination :deep(.el-pagination button) {
    background: white;
    border: 1px solid #dcdfe6;
    color: #606266;
    font-size: 14px;
    border-radius: 4px;
    margin: 0 2px;
    padding: 6px 12px;
    font-weight: 500;
    transition: all 0.3s ease;
  }

  .simple-pagination :deep(.el-pagination button:hover) {
    background: #409eff;
    color: white;
    border-color: #409eff;
  }

  .simple-pagination :deep(.el-pager li) {
    background: white;
    border: 1px solid #dcdfe6;
    color: #606266;
    font-size: 14px;
    border-radius: 4px;
    margin: 0 2px;
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 500;
    transition: all 0.3s ease;
  }

  .simple-pagination :deep(.el-pager li:hover) {
    background: #409eff;
    color: white;
    border-color: #409eff;
  }

  .simple-pagination :deep(.el-pager li.is-active) {
    background: #409eff;
    border-color: #409eff;
    color: white;
  }

  .pagination-fixed {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin-top: 24px;
    border: 1px solid #e9ecef;
  }


  /* Card chứa bảng */
  .el-card {
    border-radius: 16px;
    background: #ffffff;
    box-shadow: 0 6px 24px rgba(0, 0, 0, 0.08);
    padding: 16px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }

  .el-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
  }

  /* Tiêu đề */
  .text-2xl {
    font-size: 1.75rem;
    font-weight: 700;
    color: #1e293b;
    letter-spacing: -0.025em;
  }

  /* Bảng */
  .el-table {
    border-radius: 12px;
    overflow: hidden;
    background: #ffffff;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  }

  .el-table th {
    background: linear-gradient(to bottom, #f8fafc, #e5e7eb);
    font-weight: 600;
    color: #1e293b;
    padding: 12px;
    text-transform: uppercase;
    font-size: 0.85rem;
    letter-spacing: 0.05em;
  }

  .el-table td {
    padding: 12px;
    color: #374151;
    font-size: 0.95rem;
    transition: background-color 0.2s ease;
  }

  .el-table tr:hover {
    background: #f1f5f9;
  }

  /* Màu nền cho trạng thái */
  .el-table .bg-green-50 {
    background: #ecfdf5;
  }

  .el-table .bg-yellow-50 {
    background: #fefce8;
  }

  .el-table .bg-red-50 {
    background: #fef2f2;
  }

  .el-table .bg-gray-50 {
    background: #f9fafb;
  }

  /* Nút */
  .el-button {
    border-radius: 10px;
    font-weight: 500;
    padding: 10px 16px;
    transition: all 0.3s ease;
  }

  .el-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .el-button[type="info"] {
    background: #60a5fa;
    border-color: #60a5fa;
    color: #ffffff;
  }

  .el-button[type="warning"] {
    background: #f59e0b;
    border-color: #f59e0b;
    color: #ffffff;
  }

  .el-button[type="danger"] {
    background: #ef4444;
    border-color: #ef4444;
    color: #ffffff;
  }

  .el-button[type="primary"] {
    background: linear-gradient(90deg, #3b82f6, #2563eb);
    border-color: transparent;
    color: #ffffff;
  }

  .el-button[type="default"] {
    background: #f3f4f6;
    border-color: #d1d5db;
    color: #374151;
  }

  .el-button:hover {
    filter: brightness(1.1);
  }

  /* Rating */
  .el-rate .text-yellow-400 {
    color: #facc15 !important;
  }

  /* Hình ảnh và video */
  .el-image,
  video {
    border-radius: 8px;
    border: 1px solid #e5e7eb;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
    transition: all 0.3s ease;
  }

  .el-image:hover,
  video:hover {
    border-color: #3b82f6;
    transform: scale(1.05);
  }

  .el-image {
    width: 140px !important;
    height: 100px !important;
    background: #f9fafb;
    object-fit: cover;
  }

  video {
    width: 140px !important;
    height: 100px !important;
    background: #f9fafb;
    object-fit: cover;
    cursor: pointer;
  }

  /* Dialog */
  .el-dialog {
    border-radius: 16px;
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  }

  .el-dialog__header {
    background: linear-gradient(to right, #3b82f6, #2563eb);
    padding: 16px 24px;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
  }

  .el-dialog__title {
    font-size: 1.25rem;
    font-weight: 600;
    color: #ffffff;
    letter-spacing: -0.025em;
  }

  .el-dialog__body {
    padding: 24px;
  }

  .el-form-item__label {
    font-weight: 500;
    color: #1e293b;
    font-size: 1rem;
    margin-bottom: 8px;
  }

  .el-input,
  .el-textarea,
  .el-select {
    border-radius: 10px;
  }

  .el-textarea__inner {
    pointer-events: auto !important;
    font-size: 0.95rem;
    line-height: 1.5;
    border-color: #d1d5db;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
  }

  .el-textarea__inner:focus {
    border-color: #3b82f6;
    box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
  }

  .el-select .el-input__inner {
    border-color: #d1d5db;
  }

  .el-select .el-input__inner:focus {
    border-color: #3b82f6;
  }

  /* Phân trang */
  .el-pagination {
    font-weight: 500;
    padding: 12px;
    background: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }

  .el-pagination button,
  .el-pagination span {
    border-radius: 8px;
    transition: all 0.3s ease;
  }

  .el-pagination button:hover {
    background: #3b82f6;
    color: #ffffff;
  }

  /* Dialog phản hồi */
  .mb-6.bg-gray-50 {
    background: #f9fafb;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  }

  .mb-6.bg-blue-50 {
    background: #eff6ff;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  }

  /* Typography trong dialog */
  .text-gray-700 {
    color: #374151;
    font-size: 0.95rem;
  }

  .text-gray-900 {
    color: #1e293b;
    font-weight: 600;
  }

  /* Video trong dialog xem video */
  .el-dialog video {
    width: 100%;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  }

  /* Responsive adjustments */
  @media (max-width: 768px) {
    .el-card {
      padding: 12px;
    }

    .text-2xl {
      font-size: 1.5rem;
    }

    .el-table th,
    .el-table td {
      padding: 8px;
      font-size: 0.85rem;
    }

    .el-image {
      width: 40px !important;
      height: 40px !important;
    }

    video {
      width: 48px !important;
      height: 32px !important;
    }

    .el-dialog {
      width: 90% !important;
    }
  }
  </style>