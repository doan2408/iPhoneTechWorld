package org.example.websitetechworld.Services.ClientServices.DanhGiaSanPhamClientService;


import org.example.websitetechworld.Dto.Request.ClientRequest.DanhGiaSanPhamClientRequest.DanhGiaSanPhamClientRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.DanhGiaSanPhamClientResponse.DanhGiaAndHoaDonClientResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.DanhGiaSanPhamClientResponse.DanhGiaSanPhamClientResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.MediaDanhGiaClientResponse.MediaReviewClientResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiMedia;
import org.example.websitetechworld.Repository.ChiTietHoaDonRepository;
import org.example.websitetechworld.Repository.DanhGiaSanPhamRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DanhGiaSanPhamClientService {

    @Autowired
    private DanhGiaSanPhamRepository danhGiaRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    public DanhGiaSanPhamClientResponse taoMoiDanhGia(DanhGiaSanPhamClientRequest request) {
        // 1. Validate số sao
        if (request.getSoSao() == null || request.getSoSao() < 1 || request.getSoSao() > 5) {
            throw new IllegalArgumentException("Số sao phải từ 1 đến 5");
        }

        // 2. Tạo mới đối tượng đánh giá
        DanhGiaSanPham danhGia = new DanhGiaSanPham();
        danhGia.setSoSao(request.getSoSao());
        danhGia.setNoiDung(request.getNoiDung());
        danhGia.setNgayDanhGia(LocalDateTime.now());
        danhGia.setTrangThaiDanhGia(
                request.getTrangThaiDanhGia() != null ? request.getTrangThaiDanhGia() : TrangThaiDanhGia.APPROVED
        );

        // 3. Kiểm tra và set khách hàng
        KhachHang khachHang = khachHangRepository.findById(request.getIdKhachHang())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        danhGia.setIdKhachHang(khachHang);

        // 4. Kiểm tra và set sản phẩm chi tiết
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));
        danhGia.setIdSanPhamChiTiet(sanPhamChiTiet);

        // 5. Kiểm tra chi tiết hóa đơn
        ChiTietHoaDon chiTietHoaDon = chiTietHoaDonRepository.findById(request.getIdChiTietHoaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết hóa đơn"));

        //  Kiểm tra chi tiết hóa đơn có thuộc về khách hàng không
        if (!chiTietHoaDon.getIdHoaDon().getIdKhachHang().getId().equals(request.getIdKhachHang())) {
            throw new RuntimeException("Chi tiết hóa đơn không thuộc về khách hàng này");
        }

        //  Kiểm tra chi tiết hóa đơn có đúng sản phẩm không
        if (!chiTietHoaDon.getIdSanPhamChiTiet().getId().equals(request.getIdSanPhamChiTiet())) {
            throw new RuntimeException("Chi tiết hóa đơn không đúng với sản phẩm đã mua");
        }

        //  Kiểm tra đã đánh giá chưa
        boolean daDanhGia = danhGiaRepository.existsByChiTietHoaDonId(request.getIdChiTietHoaDon());
        if (daDanhGia) {
            throw new RuntimeException("Sản phẩm này đã được đánh giá rồi");
        }

        LocalDateTime ngayMua = chiTietHoaDon.getIdHoaDon().getNgayThanhToan();
        if (ngayMua.plusMinutes(1).isBefore(LocalDateTime.now())) {
            throw new BusinessException("Hết thời gian cho phép đánh giá (sau 1 phút kể từ ngày mua)");
        }

        danhGia.setIdChiTietHoaDon(chiTietHoaDon);

        // 6. Lưu đánh giá
        DanhGiaSanPham savedDanhGia = danhGiaRepository.save(danhGia);
        return chuyenDoiSangResponse(savedDanhGia);
    }


    public DanhGiaSanPhamClientResponse capNhatDanhGia(Integer id, DanhGiaSanPhamClientRequest request) {
        DanhGiaSanPham danhGia = danhGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá với ID: " + id));

        // Validate dữ liệu
        if (request.getSoSao() != null && (request.getSoSao() < 1 || request.getSoSao() > 5)) {
            throw new IllegalArgumentException("Số sao phải từ 1 đến 5");
        }

        // Cập nhật thông tin
        if (request.getSoSao() != null) {
            danhGia.setSoSao(request.getSoSao());
        }
        if (request.getNoiDung() != null) {
            danhGia.setNoiDung(request.getNoiDung());
        }
        if (request.getTrangThaiDanhGia() != null) {
            danhGia.setTrangThaiDanhGia(request.getTrangThaiDanhGia());
        }

        DanhGiaSanPham updatedDanhGia = danhGiaRepository.save(danhGia);
        return chuyenDoiSangResponse(updatedDanhGia);
    }

    public void xoaDanhGia(Integer id) {
        if (!danhGiaRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đánh giá với ID: " + id);
        }
        danhGiaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public DanhGiaSanPhamClientResponse layDanhGiaTheoId(Integer id) {
        DanhGiaSanPham danhGia = danhGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá với ID: " + id));
        return chuyenDoiSangResponse(danhGia);
    }

    @Transactional(readOnly = true)
    public List<DanhGiaSanPhamClientResponse> layTatCaDanhGia() {
        List<DanhGiaSanPham> danhGiaList = danhGiaRepository.findAll();
        return danhGiaList.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DanhGiaSanPhamClientResponse> layDanhGiaTheoSanPham(Integer idSanPhamChiTiet) {
        List<DanhGiaSanPham> danhGiaList = danhGiaRepository
                .findByIdSanPhamChiTiet_Id(idSanPhamChiTiet);
        return danhGiaList.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DanhGiaSanPhamClientResponse> layDanhGiaTheoSanPhamChiTiet(Integer idSanPhamChiTiet, Integer soSao, Boolean hasMedia, Integer idKhachHang) {
        List<DanhGiaSanPham> danhGias = danhGiaRepository.findBySanPhamChiTietAndFilters(idSanPhamChiTiet, soSao, hasMedia, idKhachHang);

        return danhGias.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public List<DanhGiaSanPhamClientResponse> layDanhGiaTheoKhachHang(Integer idKhachHang) {
        List<DanhGiaSanPham> danhGiaList = danhGiaRepository
                .findByIdKhachHang_Id(idKhachHang);
        return danhGiaList.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DanhGiaSanPhamClientResponse> layDanhGiaTheoTrangThai(TrangThaiDanhGia trangThai) {
        List<DanhGiaSanPham> danhGiaList = danhGiaRepository.findByTrangThaiDanhGia(trangThai);
        return danhGiaList.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DanhGiaSanPhamClientResponse> layDanhGiaTheoSoSao(Integer soSao) {
        List<DanhGiaSanPham> danhGiaList = danhGiaRepository.findBySoSao(soSao);
        return danhGiaList.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DanhGiaSanPhamClientResponse> layDanhGiaTheoKhoangThoiGian(LocalDateTime tuNgay, LocalDateTime denNgay) {
        List<DanhGiaSanPham> danhGiaList = danhGiaRepository.findByNgayDanhGiaBetween(tuNgay, denNgay);
        return danhGiaList.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Double tinhDiemTrungBinhSanPhamChiTiet(Integer idSanPhamChiTiet) {
        Double diemTrungBinh = danhGiaRepository.tinhDiemTrungBinhSanPhamChiTiet(idSanPhamChiTiet);
        return diemTrungBinh != null ? Math.round(diemTrungBinh * 10.0) / 10.0 : 0.0;
    }

    @Transactional(readOnly = true)
    public Double tinhDiemTrungBinhSanPham(Integer idSanPham) {
        Double diemTrungBinh = danhGiaRepository.tinhDiemTrungBinhTheoSanPham(idSanPham);
        return diemTrungBinh != null ? Math.round(diemTrungBinh * 10.0) / 10.0 : 0.0;
    }

    @Transactional(readOnly = true)
    public Map<Integer, Long> thongKeSoSaoSanPham(Integer idSanPhamChiTiet) {
        List<Object[]> results = danhGiaRepository.demSoLuongTheoSoSao(idSanPhamChiTiet);
        Map<Integer, Long> thongKe = new HashMap<>();

        // Khởi tạo tất cả các sao từ 1-5 với giá trị 0
        for (int i = 1; i <= 5; i++) {
            thongKe.put(i, 0L);
        }

        // Cập nhật số liệu thực tế
        for (Object[] result : results) {
            Integer soSao = (Integer) result[0];
            Long soLuong = (Long) result[1];
            thongKe.put(soSao, soLuong);
        }

        return thongKe;
    }

    @Transactional(readOnly = true)
    public List<DanhGiaSanPhamClientResponse> layDanhGiaCoPhanhoi() {
        List<DanhGiaSanPham> danhGiaList = danhGiaRepository.findDanhGiaCoPhanhoi();
        return danhGiaList.stream()
                .map(this::chuyenDoiSangResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DanhGiaSanPhamClientResponse> layDanhGiaTheoTrang(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DanhGiaSanPham> danhGiaPage = danhGiaRepository.findAll(pageable);

        return danhGiaPage.map(this::chuyenDoiSangResponse);
    }

    public List<DanhGiaAndHoaDonClientResponse> getDanhGiaByHoaDon(Integer idHoaDon) {
        // Kiểm tra đầu vào
        if (idHoaDon == null) {
            throw new IllegalArgumentException("ID hóa đơn không được để trống");
        }

        // Gọi repository để lấy dữ liệu
        List<Object[]> results = danhGiaRepository.getDanhGiaByHoaDon(idHoaDon);
        Map<Integer, DanhGiaAndHoaDonClientResponse> danhGiaMap = new HashMap<>();

        // Chuyển đổi kết quả từ Object[] sang DanhGiaHoaDonResponse
        for (Object[] result : results) {
            Integer idDanhGia = result[0] != null ? (Integer) result[0] : null;
            DanhGiaAndHoaDonClientResponse dto = danhGiaMap.computeIfAbsent(idDanhGia, k -> {
                DanhGiaAndHoaDonClientResponse newDto = new DanhGiaAndHoaDonClientResponse();
                newDto.setIdDanhGia(idDanhGia);
                newDto.setIdHoaDon(result[1] != null ? (Integer) result[1] : null);
                newDto.setIdSanPhamChiTiet(result[2] != null ? (Integer) result[2] : null);
                newDto.setIdChiTietHoaDon(result[3] != null ? (Integer) result[3] : null);
                newDto.setIdKhachHang(result[4] != null ? (Integer) result[4] : null);
                newDto.setSoSao(result[5] != null ? (Integer) result[5] : null);
                newDto.setNoiDung(result[6] != null ? (String) result[6] : null);
                newDto.setTrangThaiDanhGia(result[7] != null ? TrangThaiDanhGia.valueOf((String) result[7]) : null);
                newDto.setReviewClientResponsesList(new ArrayList<>());
                return newDto;
            });

            // Xử lý media
            if (result[8] != null) { // Kiểm tra xem có bản ghi media không
                MediaReviewClientResponse media = new MediaReviewClientResponse();
                media.setIdMedia(result[8] != null ? (Integer) result[8] : null);
                media.setLoaiMedia(result[9] != null ? (String) result[9] : null);
                media.setUrlMedia(result[10] != null ? (String) result[10] : null);
                media.setNgayUpload(result[11] != null ? ((Timestamp) result[11]).toLocalDateTime() : null);
                media.setTrangThaiMedia(result[12] != null ? TrangThaiMedia.valueOf((String) result[12]) : null);
                media.setIdDanhGia(idDanhGia);
                dto.getReviewClientResponsesList().add(media);
            }
        }

        return new ArrayList<>(danhGiaMap.values());
    }

    public Map<String, Object> checkDaDanhGia(Integer idHoaDon, Integer idKhachHang) {
        boolean daDanhGia = danhGiaRepository.checkDaDanhGia(idHoaDon, idKhachHang);
        Map<String, Object> response = new HashMap<>();
        response.put("idHoaDon", idHoaDon);
        response.put("idKhachHang", idKhachHang);
        response.put("daDanhGia", daDanhGia);
        return response;
    }

    public Map<String, Boolean> checkDaDanhGiaVaPhanHoi(Integer idHoaDon, Integer idKhachHang) {
        // Kiểm tra tham số đầu vào
        if (idHoaDon == null || idKhachHang == null) {
            throw new IllegalArgumentException("idHoaDon và idKhachHang không được null");
        }

        // Gọi repository để thực hiện truy vấn
        Map<String, Boolean> result = danhGiaRepository.checkDaDanhGiaVaPhanHoi(idHoaDon, idKhachHang);

        // Nếu không có kết quả, trả về giá trị mặc định
        if (result == null) {
            Map<String, Boolean> defaultResult = new HashMap<>();
            defaultResult.put("daDanhGia", false);
            defaultResult.put("coPhanHoi", false);
            return defaultResult;
        }

        return result;
    }

    // Helper method to convert entity to response DTO
    private DanhGiaSanPhamClientResponse chuyenDoiSangResponse(DanhGiaSanPham danhGia) {
        DanhGiaSanPhamClientResponse response = new DanhGiaSanPhamClientResponse();
        response.setIdDanhGia(danhGia.getIdDanhGia());
        response.setSoSao(danhGia.getSoSao());
        response.setNoiDung(danhGia.getNoiDung());
        response.setNgayDanhGia(danhGia.getNgayDanhGia());
        response.setTrangThaiDanhGia(danhGia.getTrangThaiDanhGia());

        // Set customer info
        if (danhGia.getIdKhachHang() != null) {
            response.setIdKhachHang(danhGia.getIdKhachHang().getId());
            response.setTenKhachHang(danhGia.getIdKhachHang().getTenKhachHang());
        }

        // Set product info
        if (danhGia.getIdSanPhamChiTiet() != null) {
            response.setIdSanPhamChiTiet(danhGia.getIdSanPhamChiTiet().getId());
            response.setTenSanPham(danhGia.getIdSanPhamChiTiet().getIdSanPham().getTenSanPham());
        }

        // Set order detail info
        if (danhGia.getIdChiTietHoaDon() != null) {
            response.setIdChiTietHoaDon(danhGia.getIdChiTietHoaDon().getId());
        }

        // Set media URLs
        if (danhGia.getMediaList() != null && !danhGia.getMediaList().isEmpty()) {
            List<String> mediaUrls = danhGia.getMediaList().stream()
                    .map(MediaDanhGia::getUrlMedia)
                    .collect(Collectors.toList());
            response.setMediaUrls(mediaUrls);
        }

        // Set response count
        if (danhGia.getPhanHoiList() != null) {
            response.setSoPhanHoi(danhGia.getPhanHoiList().size());
        } else {
            response.setSoPhanHoi(0);
        }

        return response;
    }
}
