package org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest.KhuyenMaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.KhuyenMaiAdminResponse.KhuyenMaiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.KhuyenMai;
import org.example.websitetechworld.Entity.KhuyenMaiSanPhamChiTiet;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Events.KhuyenMaiUpdatedEvent;
import org.example.websitetechworld.Repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KhuyenMaiAdminService {

    private final KhuyenMaiRepository khuyenMaiRepository;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final KhuyenMaiSanPhamChiTietRepo khuyenMaiSanPhamChiTietRepo;
    private final HoaDonRepository hoaDonRepository;

    public Page<KhuyenMaiAdminResponse> getAllKhuyenMai(String search, TrangThaiKhuyenMai trangThai,
                                                        LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                        int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime startDateTime = ngayBatDau != null ? ngayBatDau.atStartOfDay() : null;
        LocalDateTime endDateTime = ngayKetThuc != null ? ngayKetThuc.atTime(23, 59, 59, 999_999_999) : null;

        Page<KhuyenMai> khuyenMais = khuyenMaiRepository.findAll(search, trangThai, startDateTime, endDateTime, pageable);
        return khuyenMais.map(this::mapToKhuyenMaiResponse);
    }

    public KhuyenMaiAdminResponse getKhuyenMaiById(Integer id) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));
        return mapToKhuyenMaiResponse(khuyenMai);
    }

    @Transactional
    public KhuyenMaiAdminResponse createKhuyenMai(KhuyenMaiAdminRequest request) {
        validateRequest(request);
        if (!hasNotStarted(request.getNgayBatDau())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải sau ngày hiện tại");
        }
        if (request.getMaKhuyenMai() == null || request.getMaKhuyenMai().trim().isEmpty()) {
            request.setMaKhuyenMai(taoMaGiamGiaNgauNhien());
        }
        KhuyenMai khuyenMai = modelMapper.map(request, KhuyenMai.class);
        khuyenMai.setMucDoUuTien(request.getMucDoUuTien() != null ? request.getMucDoUuTien() : 1);
        LocalDateTime now = LocalDateTime.now();
        khuyenMai.setNgayTao(now);
        khuyenMai.setTrangThai(xacDinhTrangThaiKhuyenMai(khuyenMai, now));
        khuyenMai = khuyenMaiRepository.save(khuyenMai);
        updateKhuyenMaiSanPhamChiTiet(khuyenMai, request.getIdSanPhamChiTietList());
        eventPublisher.publishEvent(new KhuyenMaiUpdatedEvent(khuyenMai.getId()));

        return mapToKhuyenMaiResponse(khuyenMai);
    }

    @Transactional
    public KhuyenMaiAdminResponse updateKhuyenMai(Integer id, KhuyenMaiAdminRequest request) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));
        validateRequest(request);
        if (!khuyenMai.getMaKhuyenMai().equalsIgnoreCase(request.getMaKhuyenMai())) {
            throw new IllegalArgumentException("Mã khuyến mãi không được thay đổi");
        }
        if (!khuyenMai.getTrangThai().equals(TrangThaiKhuyenMai.EXPIRED) && !hasNotStarted(request.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày hiện tại");
        }
        modelMapper.map(request, khuyenMai);
        khuyenMai.setId(id);
        khuyenMai.setMucDoUuTien(request.getMucDoUuTien() != null ? request.getMucDoUuTien() : 1);
        khuyenMai = khuyenMaiRepository.save(khuyenMai);
        updateKhuyenMaiSanPhamChiTiet(khuyenMai, request.getIdSanPhamChiTietList());
        eventPublisher.publishEvent(new KhuyenMaiUpdatedEvent(khuyenMai.getId()));

        return mapToKhuyenMaiResponse(khuyenMai);
    }

    @Transactional
    public void deleteKhuyenMai(Integer id) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));
        if (!hasNotStarted(khuyenMai.getNgayBatDau())) {
            throw new IllegalArgumentException("Không thể xóa khuyến mại đã bắt đầu");
        } else {
            List<KhuyenMaiSanPhamChiTiet> danhSachCanXoa = khuyenMaiSanPhamChiTietRepo.findByIdKhuyenMai_Id(id);
            khuyenMaiSanPhamChiTietRepo.deleteAll(danhSachCanXoa);
            khuyenMaiRepository.delete(khuyenMai);
            eventPublisher.publishEvent(new KhuyenMaiUpdatedEvent(khuyenMai.getId()));
        }
    }

    public List<SanPhamAdminResponse> layDanhSachSanPham(String timKiem, String filter) {
        List<SanPham> sanPhams = switch (filter != null ? filter : "") {
            case "LOW_STOCK" -> sanPhamRepository.findByTongSoLuongLessThan(10);
            case "HIGH_STOCK" -> sanPhamRepository.findByTongSoLuongGreaterThan(100);
            default -> sanPhamRepository.findByMaSanPhamContainingIgnoreCaseOrTenSanPhamContainingIgnoreCase(timKiem, timKiem);
        };

        return sanPhams.stream()
                .map(this::mapToSanPhamResponse)
                .collect(Collectors.toList());
    }

    public List<SanPhamChiTietResponse> getSanPhamChiTietsBySanPhamIds(List<Integer> sanPhamIds) {
        return sanPhamChiTietRepository.findBySanPhamIds(sanPhamIds)
                .stream()
                .map(this::mapToSanPhamChiTietResponse)
                .collect(Collectors.toList());
    }

    public List<SanPhamChiTietResponse> getSanPhamChiTietByIdKhuyenMai(Integer id) {
        List<KhuyenMaiSanPhamChiTiet> danhSachSpct = khuyenMaiSanPhamChiTietRepo.findByIdKhuyenMai_Id(id);

        return danhSachSpct.stream()
                .map(KhuyenMaiSanPhamChiTiet::getIdSanPhamChiTiet)
                .map(this::mapToSanPhamChiTietResponse)
                .collect(Collectors.toList());
    }

    public List<KhuyenMaiAdminResponse> getExistingPromotions(Integer sanPhamChiTietId) {
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(sanPhamChiTietId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại: " + sanPhamChiTietId));

        List<KhuyenMaiSanPhamChiTiet> danhSachKhuyenMai = khuyenMaiSanPhamChiTietRepo.findByIdSanPhamChiTiet_Id(spct.getId());

        if (danhSachKhuyenMai == null || danhSachKhuyenMai.isEmpty()) {
            return Collections.emptyList();
        }

        return danhSachKhuyenMai.stream()
                .map(KhuyenMaiSanPhamChiTiet::getIdKhuyenMai)
                .map(this::mapToKhuyenMaiResponse)
                .collect(Collectors.toList());
    }

    private void validateRequest(KhuyenMaiAdminRequest request) {
        if (request.getNgayBatDau().isAfter(request.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    private void updateKhuyenMaiSanPhamChiTiet(KhuyenMai khuyenMai, List<Integer> spctIds) {

        List<KhuyenMaiSanPhamChiTiet> sanPhamChiTietsCu = khuyenMaiSanPhamChiTietRepo.findByIdKhuyenMai_Id(khuyenMai.getId());
        sanPhamChiTietsCu.stream()
                .filter(kmSpct -> !spctIds.contains(kmSpct.getIdSanPhamChiTiet().getId()))
                .forEach(khuyenMaiSanPhamChiTietRepo::delete);

        spctIds.stream()
                .filter(idSpct -> sanPhamChiTietsCu.stream()
                        .noneMatch(old -> old.getIdSanPhamChiTiet().getId().equals(idSpct)))
                .forEach(idSpct -> {
                    SanPhamChiTiet spct = sanPhamChiTietRepository.findById(idSpct)
                            .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại: " + idSpct));
                    KhuyenMaiSanPhamChiTiet kmSpct = new KhuyenMaiSanPhamChiTiet();
                    kmSpct.setIdKhuyenMai(khuyenMai);
                    kmSpct.setIdSanPhamChiTiet(spct);
                    khuyenMaiSanPhamChiTietRepo.save(kmSpct);
                });
    }

    private KhuyenMaiAdminResponse mapToKhuyenMaiResponse(KhuyenMai khuyenMai) {
        KhuyenMaiAdminResponse response = modelMapper.map(khuyenMai, KhuyenMaiAdminResponse.class);
        response.setIdSanPhamChiTietList(khuyenMaiSanPhamChiTietRepo.findByIdKhuyenMai_Id(khuyenMai.getId())
                .stream().map(kmSpct -> kmSpct.getIdSanPhamChiTiet().getId())
                .collect(Collectors.toList()));
        return response;
    }

    private SanPhamAdminResponse mapToSanPhamResponse(SanPham sanPham) {
        SanPhamAdminResponse response = new SanPhamAdminResponse();
        response.setId(sanPham.getId());
        response.setMaSanPham(sanPham.getMaSanPham());
        response.setTenSanPham(sanPham.getTenSanPham());
        response.setSanPhamChiTiets(sanPhamChiTietRepository.findAllByIdSanPham(sanPham)
                .stream()
                .map(this::mapToSanPhamChiTietResponse)
                .collect(Collectors.toSet()));
        return response;
    }

    private SanPhamChiTietResponse mapToSanPhamChiTietResponse(SanPhamChiTiet spct) {
        SanPhamChiTietResponse response = new SanPhamChiTietResponse();
        response.setId(spct.getId());
        response.setMaSanPhamChiTiet(spct.getMaSanPhamChiTiet());
        response.setSoLuongSPCT(spct.getSoLuong());
        response.setTenMau(spct.getIdMau() != null ? spct.getIdMau().getTenMau() : null);
        response.setDungLuongRom(spct.getIdRom() != null ? spct.getIdRom().getDungLuong() : null);
        response.setGiaBan(spct.getGiaBan());
        response.setIdSanPham(spct.getIdSanPham() != null ? spct.getIdSanPham().getId() : null);
        response.setIdKhuyenMais(
                spct.getDanhSachKhuyenMai() == null ?
                        Collections.emptyList() :
                        spct.getDanhSachKhuyenMai().stream()
                                .map(kmSpct -> kmSpct.getIdKhuyenMai().getId())
                                .toList()
        );
        return response;
    }

    private String taoMaGiamGiaNgauNhien() {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        while (khuyenMaiRepository.existsByMaKhuyenMai(code)) {
            code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
        return code;
    }

    @Transactional
    public void capNhatTrangThaiKhuyenMai (LocalDateTime hienTai) {
        List<KhuyenMai> danhSachKhuyenMai = khuyenMaiRepository.findAllByTrangThaiIn(List.of(TrangThaiKhuyenMai.NOT_STARTED, TrangThaiKhuyenMai.ACTIVE));
        List<KhuyenMai> khuyenMaiCanCapNhat = danhSachKhuyenMai.stream()
                .filter(km -> {
                    TrangThaiKhuyenMai trangThaiMoi = xacDinhTrangThaiKhuyenMai(km, hienTai);
                    if (km.getTrangThai() != trangThaiMoi) {
                        km.setTrangThai(trangThaiMoi);
                        return true;
                    }
                    return false;
                })
                .toList();
        if (!khuyenMaiCanCapNhat.isEmpty()) {
            khuyenMaiRepository.saveAll(khuyenMaiCanCapNhat);
        }
    }

    public BigDecimal tinhGiaSauKhuyenMai (SanPhamChiTiet spct, Integer selectedIdKhachHang) {
        try {
            if (spct == null || spct.getGiaBan() == null) {
                return BigDecimal.ZERO;
            }
            BigDecimal giaGoc = spct.getGiaBan();
            List<KhuyenMai> danhSachKhuyenMai = spct.getDanhSachKhuyenMai().stream()
                    .map(KhuyenMaiSanPhamChiTiet::getIdKhuyenMai)
                    .filter(km -> km.getNgayBatDau() != null && km.getNgayKetThuc() != null)
                    .toList();

            LocalDateTime hienTai = LocalDateTime.now();
            KhuyenMai khuyenMai = danhSachKhuyenMai.stream()
                    .filter(km -> {
                        TrangThaiKhuyenMai trangThai = xacDinhTrangThaiKhuyenMai(km, hienTai);
                        return trangThai == TrangThaiKhuyenMai.ACTIVE;
                    })
                    .max(Comparator
                            .comparingInt((KhuyenMai km) -> km.getMucDoUuTien() != null ? km.getMucDoUuTien() : 1)
                            .thenComparing(KhuyenMai::getNgayTao))
                    .orElse(null);
            if (khuyenMai == null) {
                return giaGoc;
            }
            if (khuyenMai.getTrangThai() != TrangThaiKhuyenMai.ACTIVE) {
                return giaGoc;
            }
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(khuyenMai.getNgayBatDau()) || now.isAfter(khuyenMai.getNgayKetThuc())) {
                return giaGoc;
            }

            Integer discountValue = Optional.ofNullable(khuyenMai.getPhanTramGiam()).orElse(0);
            BigDecimal tyLeGiam = BigDecimal.valueOf(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            DoiTuongApDung doiTuong = khuyenMai.getDoiTuongApDung();
            if (doiTuong == DoiTuongApDung.ALL) {
                return tinhGiaKhuyenMai(giaGoc, tyLeGiam);
            }
            if (selectedIdKhachHang == null || selectedIdKhachHang == 0) {
                return giaGoc;
            }
            boolean khachHangCu = hoaDonRepository.countHoaDonByIdKhachHang(selectedIdKhachHang) > 0;
            boolean khongHopLe =
                    (doiTuong == DoiTuongApDung.NEW_CUSTOMER && khachHangCu) ||
                            (doiTuong == DoiTuongApDung.OLD_CUSTOMER && !khachHangCu);
            if (khongHopLe) {
                return giaGoc;
            }

            return tinhGiaKhuyenMai(giaGoc, tyLeGiam);
        } catch (Exception e) {
            return spct.getGiaBan() != null ? spct.getGiaBan() : BigDecimal.ZERO;
        }
    }

    private BigDecimal tinhGiaKhuyenMai (BigDecimal giaGoc, BigDecimal tyLeGiam) {
        return giaGoc.subtract(giaGoc.multiply(tyLeGiam)).max(BigDecimal.ZERO);
    }

    private TrangThaiKhuyenMai xacDinhTrangThaiKhuyenMai (KhuyenMai khuyenMai, LocalDateTime hienTai) {
        if (hienTai.isBefore(khuyenMai.getNgayBatDau())) return TrangThaiKhuyenMai.NOT_STARTED;
        if (hienTai.isAfter(khuyenMai.getNgayKetThuc())) return TrangThaiKhuyenMai.EXPIRED;
        return TrangThaiKhuyenMai.ACTIVE;
    }

    private boolean hasNotStarted(LocalDateTime time) {
        return LocalDateTime.now().isBefore(time);
    }
}