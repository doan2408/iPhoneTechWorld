package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class CpuAdminResponse {

    private Integer id;

    private String hangSanXuat;

    private String chipXuLy;

    private String soNhan;

    private String xungNhip;

    private String congNgheSanXuat;

    private String boNhoDem;

    private String tieuThuDienNang;

    private LocalDate namRaMat;
}
