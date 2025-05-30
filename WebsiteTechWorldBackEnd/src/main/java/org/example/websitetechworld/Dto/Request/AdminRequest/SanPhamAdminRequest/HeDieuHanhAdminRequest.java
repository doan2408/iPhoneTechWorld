package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HeDieuHanhAdminRequest {
    private Integer id;

    private String phienBan;

    private String nhaPhatTrien;

    private String giaoDienNguoiDung;
}
