package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiDaBangAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiTrangHoaDonResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Repository.ImeiDaBanRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImeiDaBanAdminServices {
    private final ImeiDaBanRepository imeiDaBanRepository;
    private final HoaDonRepository hoaDonRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public ImeiDaBanAdminServices(ImeiDaBanRepository imeiDaBanRepository, HoaDonRepository hoaDonRepository, SanPhamChiTietRepository sanPhamChiTietRepository) {
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
    }

    // tao imei da ban
    public List<ImeiDaBan> generateImeiDaBan(ChiTietHoaDon chiTietHoaDon, List<Imei> imeis,TrangThaiImei trangThaiImei) {
        return imeis.stream().map(imei -> {
            ImeiDaBan imeiDaBan = new ImeiDaBan();
            imeiDaBan.setIdHoaDonChiTiet(chiTietHoaDon);
            imeiDaBan.setSoImei(imei.getSoImei());
            imeiDaBan.setTrangThai(trangThaiImei);
            return imeiDaBan;
        }).toList();
    }

    public List<ImeiDaBangAdminResponse> getImeiDaBanByIdCthd(Integer idCthd) {
        List<ImeiDaBan> imeiEntities = imeiDaBanRepository.findByIdHoaDonChiTiet_Id(idCthd);
        return imeiEntities.stream()
                .map(entity -> new ImeiDaBangAdminResponse(entity.getId(), entity.getSoImei(),entity.getTrangThai().getDisplayName(), entity.getIdHoaDonChiTiet().getId()))
                .collect(Collectors.toList());
    }

    public Page<ImeiTrangHoaDonResponse> imeiTrangHoaDonList(int pageNo, int pageSize,Integer idHoaDon, Integer idKhachHang) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return imeiDaBanRepository.imeiTrongHdct(idHoaDon,pageable)
                .map(imeiTrangHoaDonResponse -> {
                    imeiTrangHoaDonResponse.setGiaBan(tinhGiaKhuyenMai(imeiTrangHoaDonResponse.getIdSanPhamChiTiet(), idKhachHang));
                    return imeiTrangHoaDonResponse;
                });
    }

    public BigDecimal tinhGiaKhuyenMai(Integer idSpct, Integer selectedIdKhachHang) {
        try {
            if (idSpct == null) {
                return BigDecimal.ZERO;
            }
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(idSpct)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm chi tiết với ID: " + idSpct));

            BigDecimal giaGoc = spct.getGiaBan();
            if (giaGoc == null) {
                return BigDecimal.ZERO;
            }
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
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal tinhGiaKhuyenMai (BigDecimal giaGoc, BigDecimal tyLeGiam) {
        return giaGoc.subtract(giaGoc.multiply(tyLeGiam)).max(BigDecimal.ZERO);
    }

    public List<ImeiTrangHoaDonResponse> imeiTrangHoaDonList(Integer ctHoaDonId) {
        return imeiDaBanRepository.imeiTrongHdct(ctHoaDonId);
    }
}
