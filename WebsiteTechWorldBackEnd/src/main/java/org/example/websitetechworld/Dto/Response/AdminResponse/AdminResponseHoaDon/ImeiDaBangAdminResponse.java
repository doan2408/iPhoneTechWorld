package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImeiDaBangAdminResponse {
    private Integer id;
    private String soImei;
    private String trangThaiImei;
    private Integer idHoaDonChiTiet;
}
