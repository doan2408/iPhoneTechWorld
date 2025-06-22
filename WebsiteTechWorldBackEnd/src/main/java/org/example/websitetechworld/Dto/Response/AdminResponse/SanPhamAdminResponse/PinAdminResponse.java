package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinAdminResponse {

    private Integer id;

    private String phienBan;

    private String congSuatSac;

    private String thoiGianSuDung;

    private String soLanSacToiDa;
}