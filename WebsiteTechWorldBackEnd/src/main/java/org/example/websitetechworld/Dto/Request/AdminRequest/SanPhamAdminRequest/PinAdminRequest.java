package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PinAdminRequest {
    private Integer id;

    private String phienBan;

    private String congSuatSac;

    private String thoiGianSuDung;

    private String soLanSacToiDa;
}
