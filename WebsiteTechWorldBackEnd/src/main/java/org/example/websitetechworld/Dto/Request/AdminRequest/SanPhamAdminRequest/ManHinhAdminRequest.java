package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManHinhAdminRequest {
    private Integer id;

    private String tenManHinh;

    private String kichThuoc;

    private String loaiManHinh;

    private String doPhanGiai;

    private String tanSoQuet;

    private String doSang;

    private String chatLieuKinh;
}
