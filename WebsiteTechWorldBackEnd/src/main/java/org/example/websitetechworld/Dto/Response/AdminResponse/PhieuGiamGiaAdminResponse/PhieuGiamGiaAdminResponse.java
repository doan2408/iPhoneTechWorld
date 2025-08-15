package org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPhatHanh;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhieuGiamGiaAdminResponse {

    private Integer id;
    private String maGiamGia;
    private String tenGiamGia;
    private String loaiGiamGia;
    private BigDecimal giaTriGiamGia;
    private BigDecimal giaTriDonHangToiThieu;
    private BigDecimal giaTriGiamGiaToiDa;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String dieuKienApDung;
    private String hangToiThieu;
    private Integer soLuong;
    private BigDecimal soDiemCanDeDoi;
    private TrangThaiPGG trangThaiPhieuGiamGia;
    private TrangThaiPhatHanh trangThaiPhatHanh;
}