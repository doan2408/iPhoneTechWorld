package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.LichSuBaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiLichSuBaoHanh;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LichSuBaoHanhAdminResponse {
    private Integer idLichSuBaoHanh;

    private Integer idSanPhamBaoHanh;
    private String tenLoaiBaoHanh;
    private Integer thoiGianThang;


    private LocalDate ngayTiepNhan;

    private LocalDate ngayHoanThanh;

    private String moTaLoi;

    private TrangThaiLichSuBaoHanh trangThai;

    public static LichSuBaoHanhAdminResponse convertDto(LichSuBaoHanh lichSuBaoHanh) {
        LichSuBaoHanhAdminResponse lichSuBaoHanhAdminResponse = new LichSuBaoHanhAdminResponse();
        lichSuBaoHanhAdminResponse.setIdLichSuBaoHanh(lichSuBaoHanh.getId());
        if (lichSuBaoHanh.getIdSanPhamBaoHanh() != null) {
            lichSuBaoHanhAdminResponse.setIdSanPhamBaoHanh(lichSuBaoHanh.getIdSanPhamBaoHanh().getId());
            lichSuBaoHanhAdminResponse.setTenLoaiBaoHanh(lichSuBaoHanh.getIdSanPhamBaoHanh().getIdLoaiBaoHanh().getTenLoaiBaoHanh());
            lichSuBaoHanhAdminResponse.setThoiGianThang(lichSuBaoHanh.getIdSanPhamBaoHanh().getIdLoaiBaoHanh().getThoiGianThang());
        }
        lichSuBaoHanhAdminResponse.setNgayTiepNhan(lichSuBaoHanh.getNgayTiepNhan());
        lichSuBaoHanhAdminResponse.setNgayHoanThanh(lichSuBaoHanh.getNgayHoanThanh());
        lichSuBaoHanhAdminResponse.setMoTaLoi(lichSuBaoHanh.getMoTaLoi());
        lichSuBaoHanhAdminResponse.setTrangThai(lichSuBaoHanh.getTrangThai());


        return lichSuBaoHanhAdminResponse;
    }
}
