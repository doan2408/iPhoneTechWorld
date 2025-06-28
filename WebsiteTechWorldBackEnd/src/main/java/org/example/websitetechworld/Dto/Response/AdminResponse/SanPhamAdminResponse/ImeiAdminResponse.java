package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImeiAdminResponse {
    private Integer id;
    private String soImei;
    private TrangThaiImei trangThaiImei;
    private Integer idSanPhamChiTiet;
    private String maSanPhamChiTiet;
}
