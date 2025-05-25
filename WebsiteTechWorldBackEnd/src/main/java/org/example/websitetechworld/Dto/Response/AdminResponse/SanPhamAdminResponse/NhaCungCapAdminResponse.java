package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class NhaCungCapAdminResponse {

    private Integer id;

    private String tenNhaCungCap;

    private String diaChi;

    private String sdt;

    private String email;
}
