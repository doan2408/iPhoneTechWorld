package org.example.websitetechworld.Dto.Response.AdminResponse.PhanHoiDanhGiaAdminResponse;


import lombok.Data;
import java.time.LocalDateTime;
@Data
public class PhanHoiDanhGiaAdminResponse {

    private Integer idPhanHoi;

    private String noiDungPhanHoi;

    private LocalDateTime ngayPhanHoi;

    private Integer danhGiaSanPham;

    private Integer idNhanVien;

}
