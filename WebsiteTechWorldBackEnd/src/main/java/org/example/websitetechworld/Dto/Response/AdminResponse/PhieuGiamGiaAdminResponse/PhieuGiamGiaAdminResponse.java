package org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhieuGiamGiaAdminResponse {

    private Integer id;

    private String maGiamGia;

    private String tenKhuyenMai;

    private String loaiKhuyenMai;

    private BigDecimal giaTriKhuyenMai;

    private BigDecimal giaTriDonHangToiThieu;

    private BigDecimal giaTriKhuyenMaiToiDa;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThuc;

    private String dieuKienApDung;

    private String hangToiThieu;

    private Integer soLuong;

    private BigDecimal soDiemCanDeDoi;

    private Boolean isGlobal;

    private TrangThaiPGG trangThai;

//    public static PhieuGiamGiaAdminResponse convertDto (PhieuGiamGia phieuGiamGia){
//
//        PhieuGiamGiaAdminResponse phieuGiamGiaAdminResponse = new PhieuGiamGiaAdminResponse();
//
//        phieuGiamGiaAdminResponse.setId(phieuGiamGia.getId());
//        phieuGiamGiaAdminResponse.setMaGiamGia(phieuGiamGia.getMaGiamGia());
//        phieuGiamGiaAdminResponse.setTenKhuyenMai(phieuGiamGia.getTenKhuyenMai());
//        phieuGiamGiaAdminResponse.setLoaiKhuyenMai(phieuGiamGia.getLoaiKhuyenMai());
//        phieuGiamGiaAdminResponse.setGiaTriKhuyenMai(phieuGiamGia.getGiaTriKhuyenMai());
//        phieuGiamGiaAdminResponse.setGiaTriDonHangToiThieu(phieuGiamGia.getGiaTriDonHangToiThieu());
//        phieuGiamGiaAdminResponse.setGiaTriKhuyenMaiToiDa(phieuGiamGia.getGiaTriKhuyenMaiToiDa());
//        phieuGiamGiaAdminResponse.setNgayBatDau(phieuGiamGia.getNgayBatDau());
//        phieuGiamGiaAdminResponse.setNgayKetThuc(phieuGiamGia.getNgayKetThuc());
//        phieuGiamGiaAdminResponse.setDieuKienApDung(phieuGiamGia.getDieuKienApDung());
//        phieuGiamGiaAdminResponse.setHangToiThieu(phieuGiamGia.getHangToiThieu());
//        phieuGiamGiaAdminResponse.setSoLuong(phieuGiamGia.getSoLuong());
//        phieuGiamGiaAdminResponse.setSoDiemCanDeDoi(phieuGiamGia.getSoDiemCanDeDoi());
//        phieuGiamGiaAdminResponse.setIsGlobal(phieuGiamGia.getIsGlobal());
//        phieuGiamGiaAdminResponse.setTrangThai(phieuGiamGia.getTrangThai());
//
//        return phieuGiamGiaAdminResponse;
//    }
}
