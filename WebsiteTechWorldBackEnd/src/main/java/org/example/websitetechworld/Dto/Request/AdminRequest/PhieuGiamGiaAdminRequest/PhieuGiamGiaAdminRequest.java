package org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhieuGiamGiaAdminRequest {

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

}
