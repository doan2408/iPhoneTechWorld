package org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhachHangGiamGiaResponse {

    private Integer id;
    private String maKhachHang;
    private String sdt;
    private String tenKhachHang;
}
