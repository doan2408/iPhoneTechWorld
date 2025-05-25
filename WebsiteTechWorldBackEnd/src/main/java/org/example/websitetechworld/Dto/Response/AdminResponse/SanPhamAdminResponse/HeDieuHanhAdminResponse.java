package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HeDieuHanhAdminResponse {

    private Integer id;

    private String phienBan;

    private String nhaPhatTrien;

    private String giaoDienNguoiDung;
}
