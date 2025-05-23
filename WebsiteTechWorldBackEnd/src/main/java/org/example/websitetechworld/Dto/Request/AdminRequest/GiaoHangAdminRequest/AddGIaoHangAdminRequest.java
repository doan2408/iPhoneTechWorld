package org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddGIaoHangAdminRequest {
    private Integer idHoaDon;
    private String diaChiGiaoHang;

}
