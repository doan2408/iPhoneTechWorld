package org.example.websitetechworld.Services.AdminServices.KhuyenMaiAdminService;

import org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest.KhuyenMaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.KhuyenMaiAdminResponse.KhuyenMaiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Repository.KhuyenMaiRepository;
import org.example.websitetechworld.Repository.KhuyenMaiSanPhamChiTietRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KhuyenMaiAdminService {

    private final KhuyenMaiRepository khuyenMaiRepository;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final KhuyenMaiSanPhamChiTietRepository khuyenMaiSanPhamChiTietRepository;
    private final ModelMapper modelMapper;

    public KhuyenMaiAdminService(KhuyenMaiRepository khuyenMaiRepository, SanPhamRepository sanPhamRepository, SanPhamChiTietRepository sanPhamChiTietRepository, KhuyenMaiSanPhamChiTietRepository khuyenMaiSanPhamChiTietRepository, ModelMapper modelMapper) {
        this.khuyenMaiRepository = khuyenMaiRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.khuyenMaiSanPhamChiTietRepository = khuyenMaiSanPhamChiTietRepository;
        this.modelMapper = modelMapper;
    }

    public Page<KhuyenMaiAdminResponse> getAllKhuyenMai(String search, TrangThaiKhuyenMai trangThai, LocalDate ngayBatDau, LocalDate ngayKetThuc, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime ngayBatDauDateTime = (ngayBatDau != null) ? ngayBatDau.atStartOfDay() : null;
        LocalDateTime ngayKetThucDateTime = (ngayKetThuc != null) ? ngayKetThuc.atTime(23, 59, 59, 998) : null;

        Page<KhuyenMai> khuyenMais = khuyenMaiRepository.findAll(search, trangThai, ngayBatDauDateTime, ngayKetThucDateTime, pageable);

        return khuyenMais.map(khuyenMai -> {
            KhuyenMaiAdminResponse khuyenMaiAdminResponse = new KhuyenMaiAdminResponse();
            khuyenMaiAdminResponse.setId(khuyenMai.getId());
            khuyenMaiAdminResponse.setMaKhuyenMai(khuyenMai.getMaKhuyenMai());
            khuyenMaiAdminResponse.setTenKhuyenMai(khuyenMai.getTenKhuyenMai());
            khuyenMaiAdminResponse.setPhanTramGiam(khuyenMai.getPhanTramGiam());
            khuyenMaiAdminResponse.setDoiTuongApDung(khuyenMai.getDoiTuongApDung());
            khuyenMaiAdminResponse.setMoTa(khuyenMai.getMoTa());
            khuyenMaiAdminResponse.setNgayBatDau(khuyenMai.getNgayBatDau());
            khuyenMaiAdminResponse.setNgayKetThuc(khuyenMai.getNgayKetThuc());
            khuyenMaiAdminResponse.setTrangThai(String.valueOf(khuyenMai.getTrangThai()));

            List<KhuyenMaiSanPhamChiTiet> dsChiTiet = khuyenMaiSanPhamChiTietRepository.findAllByKhuyenMai(khuyenMai);
            List<Integer> idSanPhamChiTietList = dsChiTiet.stream()
                    .map(ct -> ct.getSanPhamChiTiet().getId())
                    .collect(Collectors.toList());
            khuyenMaiAdminResponse.setIdSanPhamChiTietList(idSanPhamChiTietList);

            return khuyenMaiAdminResponse;
        });
    }

    public KhuyenMaiAdminResponse getKhuyenMaiById (Integer id) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));
        return modelMapper.map(khuyenMai, KhuyenMaiAdminResponse.class);
    }

    @Transactional
    public KhuyenMaiAdminResponse createKhuyenMai(KhuyenMaiAdminRequest request) {
        validateRequest(request);

        if (request.getMaKhuyenMai() == null || request.getMaKhuyenMai().trim().isEmpty()) {
            request.setMaKhuyenMai(taoMaKhuyenMaiNgauNhien());
        }

        KhuyenMai khuyenMai = modelMapper.map(request, KhuyenMai.class);
        khuyenMai = khuyenMaiRepository.save(khuyenMai);

        KhuyenMai finalKhuyenMai = khuyenMai;
        List<KhuyenMaiSanPhamChiTiet> dsChiTiet = request.getIdSanPhamChiTietList().stream()
                .map(idSpct -> {
                    KhuyenMaiSanPhamChiTiet chiTiet = new KhuyenMaiSanPhamChiTiet();
                    chiTiet.setId(null);
                    chiTiet.setKhuyenMai(finalKhuyenMai);

                    SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
                    sanPhamChiTiet.setId(idSpct);
                    chiTiet.setSanPhamChiTiet(sanPhamChiTiet);

                    return chiTiet;
                })
                .collect(Collectors.toList());
        khuyenMaiSanPhamChiTietRepository.saveAll(dsChiTiet);
        return modelMapper.map(khuyenMai, KhuyenMaiAdminResponse.class);
    }

    @Transactional
    public KhuyenMaiAdminResponse updateKhuyenMai(Integer id, KhuyenMaiAdminRequest request) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));

        validateRequest(request);

        // Cập nhật thông tin khuyến mại
        modelMapper.map(request, khuyenMai);
        khuyenMai = khuyenMaiRepository.save(khuyenMai);

        // Lấy danh sách hiện tại
        List<KhuyenMaiSanPhamChiTiet> dsCu = khuyenMaiSanPhamChiTietRepository.findByKhuyenMai_Id(id);
        Set<Integer> idSpctCu = dsCu.stream()
                .map(ct -> ct.getSanPhamChiTiet().getId())
                .collect(Collectors.toSet());

        Set<Integer> idSpctMoi = new HashSet<>(request.getIdSanPhamChiTietList());

        // Xoá những spct không còn nữa
        List<KhuyenMaiSanPhamChiTiet> canXoa = dsCu.stream()
                .filter(ct -> !idSpctMoi.contains(ct.getSanPhamChiTiet().getId()))
                .collect(Collectors.toList());

        khuyenMaiSanPhamChiTietRepository.deleteAll(canXoa);

        // Thêm mới những spct chưa có
        KhuyenMai finalKhuyenMai = khuyenMai;
        List<KhuyenMaiSanPhamChiTiet> canThem = idSpctMoi.stream()
                .filter(idSpct -> !idSpctCu.contains(idSpct))
                .map(idSpct -> {
                    KhuyenMaiSanPhamChiTiet chiTiet = new KhuyenMaiSanPhamChiTiet();
                    chiTiet.setKhuyenMai(finalKhuyenMai);

                    SanPhamChiTiet spct = new SanPhamChiTiet();
                    spct.setId(idSpct);
                    chiTiet.setSanPhamChiTiet(spct);

                    return chiTiet;
                }).collect(Collectors.toList());

        khuyenMaiSanPhamChiTietRepository.saveAll(canThem);

        return modelMapper.map(khuyenMai, KhuyenMaiAdminResponse.class);
    }


    private void validateRequest (KhuyenMaiAdminRequest request) {
        if (request.getNgayBatDau().isAfter(request.getNgayKetThuc())) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }

        LocalDateTime currentDate = LocalDateTime.now();
        if (request.getNgayBatDau().isBefore(currentDate)) {
            throw new IllegalStateException("Ngày bắt đầu phải sau ngày hiện tại");
        }
    }

    @Transactional
    public void deleteKhuyenMai (Integer id) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));
        khuyenMaiRepository.delete(khuyenMai);
    }

    public List<SanPhamAdminResponse> layDanhSachSanPham(String timKiem) {
        List<SanPham> sanPhams = sanPhamRepository
                .findByMaSanPhamContainingIgnoreCaseOrTenSanPhamContainingIgnoreCase(timKiem, timKiem);

        return sanPhams.stream().map(sanPham -> {
            SanPhamAdminResponse response = new SanPhamAdminResponse();
            response.setId(sanPham.getId());
            response.setMaSanPham(sanPham.getMaSanPham());
            response.setTenSanPham(sanPham.getTenSanPham());

            List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findAllByIdSanPham(sanPham);
            response.setSanPhamChiTiets(
                    sanPhamChiTiets.stream()
                            .map(this::mapToResponse)
                            .collect(Collectors.toSet())
            );

            return response;
        }).collect(Collectors.toList());
    }



    public List<SanPhamChiTietResponse> getSanPhamChiTietsBySanPhamIds(List<Integer> sanPhamIds) {
        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findBySanPhamIds(sanPhamIds);
        return sanPhamChiTiets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SanPhamChiTietResponse> getSanPhamChiTietByIdKhuyenMai(Integer id) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khuyến mại không tồn tại: " + id));

        List<KhuyenMaiSanPhamChiTiet> dsChiTiet = khuyenMaiSanPhamChiTietRepository.findAllByKhuyenMai(khuyenMai);

        return dsChiTiet.stream()
                .map(KhuyenMaiSanPhamChiTiet::getSanPhamChiTiet)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private SanPhamChiTietResponse mapToResponse (SanPhamChiTiet spct) {
        SanPhamChiTietResponse response = new SanPhamChiTietResponse();
        response.setId(spct.getId());
        response.setMaSanPhamChiTiet(spct.getMaSanPhamChiTiet());
        response.setSoLuongSPCT(spct.getSoLuong());
        response.setTenMau(spct.getIdMau().getTenMau());
        response.setDungLuongRom(spct.getIdRom().getDungLuong());
        response.setGiaBan(spct.getGiaBan());
        response.setIdSanPham(spct.getIdSanPham().getId());

        return response;
    }


    private String getDynamicStatus (KhuyenMai khuyenMai) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(khuyenMai.getNgayBatDau())) {
            return "NOT_STARTED";
        } else if (now.isAfter(khuyenMai.getNgayKetThuc())) {
            return "EXPIRED";
        } else {
            return khuyenMai.getTrangThai().name();
        }
    }

    private String taoMaKhuyenMaiNgauNhien () {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }
}