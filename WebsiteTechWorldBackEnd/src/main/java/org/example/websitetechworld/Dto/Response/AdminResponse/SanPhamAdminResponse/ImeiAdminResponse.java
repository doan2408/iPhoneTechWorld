package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImeiAdminResponse {
    private Integer id;
    private String soImei;
    private String soImei2;
    private String trangThaiImei;
    private String nhaMang;
    private Integer idSanPhamChiTiet;
    private String maSanPhamChiTiet;
}
