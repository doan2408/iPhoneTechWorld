package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImeiAdminRequest {
    private Integer id;

    private String soImei;

    private TrangThaiImei trangThaiImei;

    private String nhaMang;

    private Integer idSanPhamChiTiet;

}
