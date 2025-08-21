package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.LichSuBaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiLichSuBaoHanh;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LichSuBaoHanhAdminResponse {
    private Integer idLichSuBaoHanh;

    private Integer idBaoHanh;
    private String tenLoaiBaoHanh;
    private Integer thoiGianThang;


    private Date ngayTiepNhan;

    private Date ngayHoanThanh;

    private String moTaLoi;

    private TrangThaiLichSuBaoHanh trangThai;

    public static LichSuBaoHanhAdminResponse convertDto(LichSuBaoHanh lichSuBaoHanh) {
        LichSuBaoHanhAdminResponse lichSuBaoHanhAdminResponse = new LichSuBaoHanhAdminResponse();
        lichSuBaoHanhAdminResponse.setIdLichSuBaoHanh(lichSuBaoHanh.getId());
        if (lichSuBaoHanh.getIdBaoHanh() != null) {
            lichSuBaoHanhAdminResponse.setIdBaoHanh(lichSuBaoHanh.getIdBaoHanh().getId());
            lichSuBaoHanhAdminResponse.setTenLoaiBaoHanh(lichSuBaoHanh.getIdBaoHanh().getIdLoaiBaoHanh().getTenLoaiBaoHanh());
            lichSuBaoHanhAdminResponse.setThoiGianThang(lichSuBaoHanh.getIdBaoHanh().getIdLoaiBaoHanh().getThoiGianThang());
        }
        lichSuBaoHanhAdminResponse.setNgayTiepNhan(lichSuBaoHanh.getNgayTiepNhan());
        lichSuBaoHanhAdminResponse.setNgayHoanThanh(lichSuBaoHanh.getNgayHoanThanh());
        lichSuBaoHanhAdminResponse.setMoTaLoi(lichSuBaoHanh.getMoTaLoi());
        lichSuBaoHanhAdminResponse.setTrangThai(lichSuBaoHanh.getTrangThai());


        return lichSuBaoHanhAdminResponse;
    }
}
