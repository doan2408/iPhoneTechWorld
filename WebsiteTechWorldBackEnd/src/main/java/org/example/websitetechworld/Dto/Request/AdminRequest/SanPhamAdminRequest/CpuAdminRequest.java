package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpuAdminRequest {
    private Integer id;

    private String hangSanXuat;

    private String soNhan;

    private Integer soLuong;

    private String xungNhip;

    private String congNgheSanXuat;

    private String boNhoDem;

    private String tieuThuDienNang;

    private LocalDate namRaMat;
}
