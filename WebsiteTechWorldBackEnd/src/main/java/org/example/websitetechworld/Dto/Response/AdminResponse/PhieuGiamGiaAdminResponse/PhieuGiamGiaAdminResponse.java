package org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private List<Integer> khachHangIds;
    private Boolean khachHangMoi; // Thêm trường mới
    private Boolean khachHangCu;  // Thêm trường mới
    private TrangThaiPGG trangThai;
}