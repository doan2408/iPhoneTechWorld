package org.example.websitetechworld.Dto.Response.ClientResponse.PhanHoiDanhGiaClientResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhanHoiDanhGiaClientResponse {
    private Integer idPhanHoi;

    private String noiDungPhanHoi;

    private LocalDateTime ngayPhanHoi;

    private Integer danhGiaSanPham;

    private Integer idNhanVien;

}
