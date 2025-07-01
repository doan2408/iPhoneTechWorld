package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import jakarta.persistence.EntityManager;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.KhachHangGiamGia;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Repository.KhachHangGiamGiaRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.PhieuGiamGiaRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaAdminService {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final ModelMapper modelMapper;

    // Cấu hình ModelMapper trong constructor
    public PhieuGiamGiaAdminService(PhieuGiamGiaRepository phieuGiamGiaRepository,
                                    KhachHangGiamGiaRepository khachHangGiamGiaRepository,
                                    KhachHangRepository khachHangRepository,
                                    ModelMapper modelMapper,
                                    EntityManager entityManager) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangGiamGiaRepository = khachHangGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
        cauHinhModelMapper();
    }

    // Cấu hình ánh xạ DTO-Entity
    private void cauHinhModelMapper() {
        modelMapper.typeMap(PhieuGiamGiaAdminRequest.class, PhieuGiamGia.class)
                .addMappings(mapper -> {
                    mapper.skip(PhieuGiamGia::setKhachHangGiamGias);
                });
        modelMapper.typeMap(PhieuGiamGia.class, PhieuGiamGiaAdminResponse.class);
    }

    /**
     * Lấy danh sách phiếu giảm giá theo phân trang và bộ lọc.
     */
    public Page<PhieuGiamGiaAdminResponse> layDanhSachPhieuGiamGia (String timKiem, TrangThaiPGG trangThai,
                                                                  LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                                  int trang, int kichThuoc, String sapXepTheo, String huongSapXep) {
        Sort sapXep = huongSapXep.equalsIgnoreCase("desc") ? Sort.by(sapXepTheo).descending() : Sort.by(sapXepTheo).ascending();
        Pageable phanTrang = PageRequest.of(trang, kichThuoc, sapXep);
        Page<PhieuGiamGia> trangPhieuGiamGia = phieuGiamGiaRepository.findAll(timKiem, trangThai, ngayBatDau, ngayKetThuc, phanTrang);
        return trangPhieuGiamGia.map(this::anhXaSangPhanHoi);
    }

    /**
     * Lấy thông tin chi tiết của phiếu giảm giá theo ID.
     */
    public PhieuGiamGiaAdminResponse layPhieuGiamGiaTheoId (Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));
        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    /**
     * Thêm mới phiếu giảm giá.
     */
    @Transactional
    public PhieuGiamGiaAdminResponse themPhieuGiamGia (PhieuGiamGiaAdminRequest request) {
        kiemTraNgayHopLe(request.getNgayBatDau(), request.getNgayKetThuc());
        kiemTraPhieuRiengTu(request);

        PhieuGiamGia phieuGiamGia = modelMapper.map(request, PhieuGiamGia.class);
        phieuGiamGia.setKhachHangGiamGias(new HashSet<>());
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        if (!phieuGiamGia.getCongKhai()) {
            xuLyKhachHangGiamGia(phieuGiamGia, layDanhSachIdKhachHangTuYeuCau(request));
        }

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    /**
     * Cập nhật phiếu giảm giá.
     */
    @Transactional
    public PhieuGiamGiaAdminResponse capNhatPhieuGiamGia (Integer id, PhieuGiamGiaAdminRequest request) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        kiemTraNgayHopLe(request.getNgayBatDau(), request.getNgayKetThuc());
        kiemTraPhieuRiengTu(request);

        modelMapper.map(request, phieuGiamGia);

        if (phieuGiamGia.getCongKhai()) {
            xoaKhachHangGiamGiaChuaSuDung(phieuGiamGia);
        } else {
            xuLyKhachHangGiamGia(phieuGiamGia, layDanhSachIdKhachHangTuYeuCau(request));
        }

        return modelMapper.map(phieuGiamGiaRepository.save(phieuGiamGia), PhieuGiamGiaAdminResponse.class);
    }

    /**
     * Xóa phiếu giảm giá.
     */
    @Transactional
    public String xoaPhieuGiamGia (Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        if (khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIsUser(phieuGiamGia, true)) {
            throw new IllegalStateException("Không thể xóa phiếu giảm giá đã được sử dụng");
        }

        khachHangGiamGiaRepository.deleteByIdPhieuGiamGiaId(id);
        phieuGiamGiaRepository.delete(phieuGiamGia);
        return "Đã xóa thành công phiếu giảm giá với ID: " + id;
    }


    public Page<KhachHangGiamGiaResponse> layDanhSachKhachHang (String timKiem, int trang, int kichThuoc) {
        Pageable phanTrang = PageRequest.of(trang, kichThuoc);
        Page<KhachHang> trangKhachHang = (timKiem == null || timKiem.isEmpty()) ?
                khachHangRepository.findTrangThai_Active(phanTrang) :
                khachHangRepository.findByTenKhachHangContainingIgnoreCaseAndTrangThai_Active(timKiem, phanTrang);
        return trangKhachHang.map(khachHang -> new KhachHangGiamGiaResponse(
                khachHang.getId(), khachHang.getMaKhachHang(), khachHang.getTenKhachHang()));
    }

    @Transactional
    public void capNhatTrangThaiPhieuGiamGia () {
        LocalDate hienTai = LocalDate.now();
        List<PhieuGiamGia> danhSachPhieu = phieuGiamGiaRepository.findAll(); // Tối ưu truy vấn
        for (PhieuGiamGia phieu : danhSachPhieu) {
            TrangThaiPGG trangThaiMoi = xacDinhTrangThaiPhieu(phieu, hienTai);
            if (phieu.getTrangThai() != trangThaiMoi) {
                phieu.setTrangThai(trangThaiMoi);
                phieuGiamGiaRepository.save(phieu);
            }
        }
    }

    // --- Các phương thức hỗ trợ ---

    /**
     * Kiểm tra tính hợp lệ của ngày bắt đầu và ngày kết thúc.
     */
    private void kiemTraNgayHopLe (LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        if (ngayBatDau.isAfter(ngayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    /**
     * Kiểm tra logic cho phiếu giảm giá riêng tư.
     */
    private void kiemTraPhieuRiengTu (PhieuGiamGiaAdminRequest request) {
        if (!request.getCongKhai() && (request.getKhachHangIds() == null || request.getKhachHangIds().isEmpty())) {
            throw new IllegalArgumentException("Phiếu giảm giá riêng tư phải có ít nhất một khách hàng hoặc nhóm khách hàng được chọn");
        }
    }

    /**
     * Lấy danh sách ID khách hàng từ yêu cầu.
     */
    private Set<Integer> layDanhSachIdKhachHangTuYeuCau (PhieuGiamGiaAdminRequest request) {
        Set<Integer> danhSachIdKhachHang = new HashSet<>();
        if (request.getKhachHangIds() != null) {
            danhSachIdKhachHang.addAll(request.getKhachHangIds());
        }
        if (request.getHangToiThieu() != null) {
            danhSachIdKhachHang.addAll(khachHangRepository.findByHangKhachHang(HangKhachHang.valueOf(request.getHangToiThieu()))
                    .stream()
                    .map(KhachHang::getId)
                    .collect(Collectors.toSet()));
        }
        kiemTraIdKhachHangHopLe(danhSachIdKhachHang);
        return danhSachIdKhachHang;
    }

    /**
     * Kiểm tra tính hợp lệ của danh sách ID khách hàng.
     */
    private void kiemTraIdKhachHangHopLe (Set<Integer> danhSachIdKhachHang) {
        List<Integer> idKhongHopLe = danhSachIdKhachHang.stream()
                .filter(id -> !khachHangRepository.existsById(id))
                .toList();
        if (!idKhongHopLe.isEmpty()) {
            throw new IllegalArgumentException("Các ID khách hàng không tồn tại: " + idKhongHopLe);
        }
    }

    /**
     * Xử lý danh sách khách hàng cho phiếu giảm giá riêng tư.
     */
    private void xuLyKhachHangGiamGia (PhieuGiamGia phieuGiamGia, Set<Integer> danhSachIdKhachHang) {
        xoaKhachHangGiamGiaChuaSuDung(phieuGiamGia);
        for (Integer idKhachHang : danhSachIdKhachHang) {
            KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                    .orElseThrow(() -> new IllegalArgumentException("Khách hàng ID " + idKhachHang + " không tồn tại"));
            if (!khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIdKhachHang(phieuGiamGia, khachHang)) {
                taoKhachHangGiamGia(phieuGiamGia, khachHang);
            }
        }
    }

    /**
     * Xóa các bản ghi KhachHangGiamGia chưa được sử dụng.
     */
    private void xoaKhachHangGiamGiaChuaSuDung (PhieuGiamGia phieuGiamGia) {
        List<KhachHangGiamGia> banGhiChuaSuDung = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
        khachHangGiamGiaRepository.deleteAll(banGhiChuaSuDung);
        phieuGiamGia.getKhachHangGiamGias().clear();
    }

    /**
     * turnout mới bản ghi KhachHangGiamGia.
     */
    private void taoKhachHangGiamGia (PhieuGiamGia phieuGiamGia, KhachHang khachHang) {
        KhachHangGiamGia khachHangGiamGia = new KhachHangGiamGia();
        khachHangGiamGia.setIdPhieuGiamGia(phieuGiamGia);
        khachHangGiamGia.setIdKhachHang(khachHang);
        khachHangGiamGia.setIsUser(false);
        khachHangGiamGia.setNgayCap(LocalDate.now());
        phieuGiamGia.getKhachHangGiamGias().add(khachHangGiamGia);
        khachHangGiamGiaRepository.save(khachHangGiamGia);
    }

    /**
     * Xác định trạng thái của phiếu giảm giá dựa trên ngày hiện tại.
     */
    private TrangThaiPGG xacDinhTrangThaiPhieu (PhieuGiamGia phieu, LocalDate hienTai) {
        if (hienTai.isBefore(phieu.getNgayBatDau())) {
            return TrangThaiPGG.NOT_STARTED;
        } else if (hienTai.isAfter(phieu.getNgayKetThuc())) {
            return TrangThaiPGG.EXPIRED;
        }
        return TrangThaiPGG.ACTIVE;
    }

    /**
     * Ánh xạ entity sang DTO response.
     */
    private PhieuGiamGiaAdminResponse anhXaSangPhanHoi (PhieuGiamGia phieuGiamGia) {
        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }
}