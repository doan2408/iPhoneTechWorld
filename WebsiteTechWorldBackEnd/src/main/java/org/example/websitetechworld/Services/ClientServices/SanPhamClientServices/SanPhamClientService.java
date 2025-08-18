package org.example.websitetechworld.Services.ClientServices.SanPhamClientServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SanPhamClientService {
    private final SanPhamRepository sanPhamRepo;
    private final MauSacRepository mauSacRepository;
    private final RomRepository romRepository;
    private final ModelMapper modelMapper;
    private final LoaiRepository loaiRepository;
    private final CameraSauRepository cameraSauRepository;
    private final HoaDonRepository hoaDonRepository;

    //hien thi san pham len trang chủ
    public Page<ClientProductResponse> getAllSanPhamHome(int page, int size, String tenSanPham, Integer idLoai, BigDecimal giaMin, BigDecimal giaMax, String sort) {
        Page<ClientProductResponse> homePage;

        Pageable pageable = PageRequest.of(page, size);


        if (tenSanPham != null) {
            tenSanPham = tenSanPham.trim();
            if (tenSanPham.isEmpty()) {
                tenSanPham = null;
            }
        }

        homePage = sanPhamRepo.getSanPhamHome(tenSanPham, idLoai, giaMin, giaMax, sort, pageable);
        return homePage;
    }

    //click 1 sp trang chủ -> chon bien the de mua
    public ClientProductDetailResponse getSanPhamDetail(int idSanPham) {
        SanPham sanPham = sanPhamRepo.findById(idSanPham).orElse(null);
        if (sanPham == null) return null;
        XuatXu xuatXu = sanPhamRepo.getXuatXuByIdSp(sanPham.getId());

        List<ClientProductDetailResponse.ThuocTinh> listMau = new ArrayList<>();
        for (Object[] row : mauSacRepository.getMauByIdSanPham(idSanPham)) {
            Integer idMauSac = (Integer) row[0];
            String tenMau = (String) row[1];
            listMau.add(new ClientProductDetailResponse.ThuocTinh(idMauSac, tenMau));
        }

        List<ClientProductDetailResponse.ThuocTinh> listRom = new ArrayList<>();
        for (Object[] row : romRepository.getRomByIdSanPham(idSanPham)) {
            Integer idRom = (Integer) row[0];
            String tenRom = (String) row[1];
            listRom.add(new ClientProductDetailResponse.ThuocTinh(idRom, tenRom));
        }
        System.out.println("list rom: " + listRom.getFirst().getTen());

        ClientProductDetailResponse response = new ClientProductDetailResponse();
        response.setIdSpct(sanPham.getId());
        response.setTenSanPham(sanPham.getTenSanPham());
        List<String> hinhAnh = sanPhamRepo.getFirstAnh(idSanPham);
        response.setHinhAnh(hinhAnh);
        BigDecimal giaBan = sanPhamRepo.giaThapNhat(idSanPham);
        response.setGiaBan(giaBan);
        response.setMau(listMau);
        response.setRom(listRom);
        Integer tongSoLuong = sanPhamRepo.tongSoLuong(idSanPham);
        response.setTongSoLuong(tongSoLuong);
        response.setMaXuatXu(xuatXu.getMaXuatXu());
        return response;
    }

    //hien thi thong so tuong ung voi rom
    public ThongSoResponse getThongSo(Integer idSp, Integer idRom) {
        ThongSoResponse response = sanPhamRepo.getThongSoByIdSpct(idSp, idRom);
        return response !=null ? response : new ThongSoResponse();
    }

    public ThongSoCompareResponse getThongSoLimitRomMin(Integer idSp) {
        ThongSoCompareResponse response = sanPhamRepo.getThongSoByIdSp(idSp);
        return response !=null ? response : new ThongSoCompareResponse();
    }

    public List<CameraSauResponse> findCameraSauByIdSanPham(Integer idSanPham){
        return cameraSauRepository.findCameraSauByIdSanPham(idSanPham);
    }

    //khi chon mau + rom
    public ClientProductDetailResponse getChiTietBienThe(Integer idSanPham, Integer idMau, Integer idRom, Integer selectedIdKhachHang) {

        Optional<SanPhamChiTiet> chiTietOtp = sanPhamRepo.getSpctByMauAndRom(idSanPham, idMau, idRom);
        if (chiTietOtp.isEmpty()) {
            throw new RuntimeException("Không tim thấy sản phẩm với màu và rom tương ứng");
        }

        SanPhamChiTiet spct = chiTietOtp.get();
        SanPham sp = spct.getIdSanPham();

        ClientProductDetailResponse response = new ClientProductDetailResponse();
        response.setIdSpct(spct.getId());
        response.setTenSanPham(sp.getTenSanPham());
        List<String> hinhAnh = sanPhamRepo.getListAnhByMau(idSanPham, idMau);
        response.setHinhAnh(hinhAnh);
        response.setGiaTruocKhuyenMai(spct.getGiaBan());

        List<ClientProductDetailResponse.ThuocTinh> listRom = sp.getSanPhamChiTiets().stream()
                .map(SanPhamChiTiet::getIdRom)
                .distinct()
                .map(r -> new ClientProductDetailResponse.ThuocTinh(r.getId(), r.getDungLuong()))
                .toList();
        response.setRom(listRom);

        List<ClientProductDetailResponse.ThuocTinh> listMau = sp.getSanPhamChiTiets().stream()
                .map(SanPhamChiTiet::getIdMau)
                .distinct()
                .map(m -> new ClientProductDetailResponse.ThuocTinh(m.getId(), m.getTenMau()))
                .toList();
        response.setMau(listMau);

        response.setSoLuong(spct.getSoLuong());
        response.setTongSoLuong(sp.getSanPhamChiTiets().stream()
                .mapToInt(SanPhamChiTiet::getSoLuong).sum()
        );
        response.setGiaBan(tinhGiaKhuyenMai(spct, selectedIdKhachHang));

        return response;
    }

    public List<String> getListAnhByMau(Integer idSp, Integer idMau) {
        return sanPhamRepo.getListAnhByMau(idSp, idMau);
    }

    public List<LoaiClientResponse> getLoaiClientResponses() {
        return loaiRepository.getLoaiClientResponse();
    }

    private BigDecimal tinhGiaKhuyenMai(SanPhamChiTiet spct, Integer selectedIdKhachHang) {
        try {
            if (spct == null || spct.getGiaBan() == null) {
                return BigDecimal.ZERO;
            }
            BigDecimal giaGoc = spct.getGiaBan();
            KhuyenMai khuyenMai = spct.getIdKhuyenMai();
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

            if (selectedIdKhachHang == null || selectedIdKhachHang == 0) {
                return giaGoc;
            }

            boolean khachHangCu = hoaDonRepository.countHoaDonByIdKhachHang(selectedIdKhachHang) > 0;
            DoiTuongApDung doiTuong = khuyenMai.getDoiTuongApDung();
            boolean khongHopLe =
                    (doiTuong == DoiTuongApDung.NEW_CUSTOMER && khachHangCu) ||
                            (doiTuong == DoiTuongApDung.OLD_CUSTOMER && !khachHangCu);
            if (khongHopLe) {
                return giaGoc;
            }

            Integer discountValue = Optional.ofNullable(khuyenMai.getPhanTramGiam()).orElse(0);
            BigDecimal tyLeGiam = BigDecimal.valueOf(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal giaKhuyenMai = giaGoc.subtract(giaGoc.multiply(tyLeGiam));

            return giaKhuyenMai.max(BigDecimal.ZERO);
        } catch (Exception e) {
            return spct.getGiaBan() != null ? spct.getGiaBan() : BigDecimal.ZERO;
        }
    }
}
