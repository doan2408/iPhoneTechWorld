package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ManHinhAdminResponse {

    private Integer id;

    private String tenManHinh;

    private String kichThuoc;

    private String loaiManHinh;

    private String doPhanGiai;

    private String tanSoQuet;

    private String doSang;

    private String chatLieuKinh;
}
