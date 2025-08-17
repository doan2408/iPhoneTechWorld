package org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest.KhuyenMaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.KhuyenMaiAdminResponse.KhuyenMaiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.KhuyenMai;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Events.KhuyenMaiUpdatedEvent;
import org.example.websitetechworld.Events.PhieuGiamGiaUpdatedEvent;
import org.example.websitetechworld.Repository.KhuyenMaiRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KhuyenMaiAdminService {

    private final KhuyenMaiRepository khuyenMaiRepository;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

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
        validateSanPhamChiTiet(request.getIdSanPhamChiTietList(), null);

        if (request.getMaKhuyenMai() == null || request.getMaKhuyenMai().trim().isEmpty()) {
            request.setMaKhuyenMai(taoMaGiamGiaNgauNhien());
        }

        KhuyenMai khuyenMai = modelMapper.map(request, KhuyenMai.class);
        khuyenMai = khuyenMaiRepository.save(khuyenMai);
        updateSanPhamChiTiet(khuyenMai, request.getIdSanPhamChiTietList());
        eventPublisher.publishEvent(new KhuyenMaiUpdatedEvent(khuyenMai.getId()));

        return mapToKhuyenMaiResponse(khuyenMai);
    }

    @Transactional
    public KhuyenMaiAdminResponse updateKhuyenMai(Integer id, KhuyenMaiAdminRequest request) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));
        validateRequest(request);
        validateSanPhamChiTiet(request.getIdSanPhamChiTietList(), id);

        modelMapper.map(request, khuyenMai);
        khuyenMai = khuyenMaiRepository.save(khuyenMai);

        List<SanPhamChiTiet> currentSpcts = sanPhamChiTietRepository.findAllByIdKhuyenMai_Id(id);
        currentSpcts.stream()
                .filter(spct -> !request.getIdSanPhamChiTietList().contains(spct.getId()))
                .forEach(spct -> {
                    spct.setIdKhuyenMai(null);
                    sanPhamChiTietRepository.save(spct);
                });
        updateSanPhamChiTiet(khuyenMai, request.getIdSanPhamChiTietList());
        eventPublisher.publishEvent(new KhuyenMaiUpdatedEvent(khuyenMai.getId()));
        return mapToKhuyenMaiResponse(khuyenMai);
    }

    @Transactional
    public void deleteKhuyenMai(Integer id) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));
        sanPhamChiTietRepository.findAllByIdKhuyenMai_Id(id)
                .forEach(spct -> {
                    spct.setIdKhuyenMai(null);
                    sanPhamChiTietRepository.save(spct);
                });
        eventPublisher.publishEvent(new KhuyenMaiUpdatedEvent(khuyenMai.getId()));
        khuyenMaiRepository.delete(khuyenMai);
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
        return sanPhamChiTietRepository.findAllByIdKhuyenMai_Id(id)
                .stream()
                .map(this::mapToSanPhamChiTietResponse)
                .collect(Collectors.toList());
    }

    public KhuyenMaiAdminResponse getExistingPromotions(Integer sanPhamChiTietId) {
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(sanPhamChiTietId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại: " + sanPhamChiTietId));
        return spct.getIdKhuyenMai() != null ? mapToKhuyenMaiResponse(spct.getIdKhuyenMai()) : null;
    }

    @Transactional
    public SanPhamChiTietResponse removeProductFromPromotions(Integer sanPhamChiTietId, Integer khuyenMaiId) {
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(sanPhamChiTietId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại: " + sanPhamChiTietId));
        if (spct.getIdKhuyenMai() != null
                && khuyenMaiId != null
                && khuyenMaiId.equals(spct.getIdKhuyenMai().getId())) {
            spct.setIdKhuyenMai(null);
        }
        return mapToSanPhamChiTietResponse(sanPhamChiTietRepository.save(spct));
    }

    private void validateRequest(KhuyenMaiAdminRequest request) {
        if (request.getNgayBatDau().isAfter(request.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    private void validateSanPhamChiTiet(List<Integer> spctIds, Integer khuyenMaiId) {
        spctIds.forEach(spctId -> {
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId)
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại: " + spctId));
            if (spct.getIdKhuyenMai() != null && (!spct.getIdKhuyenMai().getId().equals(khuyenMaiId))) {
                throw new IllegalStateException("Sản phẩm chi tiết ID " + spctId + " đã được áp dụng trong khuyến mãi: " + spct.getIdKhuyenMai().getTenKhuyenMai());
            }
        });
    }

    private void updateSanPhamChiTiet(KhuyenMai khuyenMai, List<Integer> spctIds) {
        spctIds.forEach(spctId -> {
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId)
                    .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại: " + spctId));
            spct.setIdKhuyenMai(khuyenMai);
            sanPhamChiTietRepository.save(spct);
        });
    }

    private KhuyenMaiAdminResponse mapToKhuyenMaiResponse(KhuyenMai khuyenMai) {
        KhuyenMaiAdminResponse response = modelMapper.map(khuyenMai, KhuyenMaiAdminResponse.class);
        response.setIdSanPhamChiTietList(sanPhamChiTietRepository.findAllByIdKhuyenMai_Id(khuyenMai.getId())
                .stream()
                .map(SanPhamChiTiet::getId)
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
        response.setIdKhuyenMai(spct.getIdKhuyenMai() != null ? spct.getIdKhuyenMai().getId() : null);
        response.setMaKhuyenMai(spct.getIdKhuyenMai() != null ? spct.getIdKhuyenMai().getMaKhuyenMai() : null);
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
        List<KhuyenMai> danhSachKhuenMai = khuyenMaiRepository.findAllByTrangThaiIn(List.of(TrangThaiKhuyenMai.NOT_STARTED, TrangThaiKhuyenMai.ACTIVE));
        for (KhuyenMai khuyenMai : danhSachKhuenMai) {
            TrangThaiKhuyenMai trangThaiMoi = xacDinhTrangThaiKhuyenMai(khuyenMai, hienTai);
            if (khuyenMai.getTrangThai() != trangThaiMoi) {
                khuyenMai.setTrangThai(trangThaiMoi);
                if (khuyenMai.getTrangThai() == TrangThaiKhuyenMai.EXPIRED) {
                    sanPhamChiTietRepository.findAllByIdKhuyenMai_Id(khuyenMai.getId())
                            .forEach(spct -> {
                                spct.setIdKhuyenMai(null);
                                sanPhamChiTietRepository.save(spct);
                            });
                }
                khuyenMaiRepository.save(khuyenMai);
            }
        }
    }

    private TrangThaiKhuyenMai xacDinhTrangThaiKhuyenMai (KhuyenMai khuyenMai, LocalDateTime hienTai) {
        if (hienTai.isBefore(khuyenMai.getNgayBatDau())) return TrangThaiKhuyenMai.NOT_STARTED;
        if (hienTai.isAfter(khuyenMai.getNgayKetThuc())) return TrangThaiKhuyenMai.EXPIRED;
        return TrangThaiKhuyenMai.ACTIVE;
    }
}