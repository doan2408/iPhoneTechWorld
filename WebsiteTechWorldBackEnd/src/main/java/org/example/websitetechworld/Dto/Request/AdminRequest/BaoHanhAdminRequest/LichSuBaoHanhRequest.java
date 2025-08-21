package org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiLichSuBaoHanh;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichSuBaoHanhRequest {
    private Integer idLichSuBaoHanh;
    private Integer idBaoHanh;
    private Date ngayTiepNhan;
    private Date ngayHoanThanh;
    private String moTaLoi;
    private TrangThaiLichSuBaoHanh trangThai;
}
