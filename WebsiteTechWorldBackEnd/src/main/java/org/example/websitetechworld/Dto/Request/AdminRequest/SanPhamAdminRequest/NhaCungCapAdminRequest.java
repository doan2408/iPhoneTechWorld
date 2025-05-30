package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCapAdminRequest {
    private Integer id;

    private String tenNhaCungCap;

    private String diaChi;

    private String sdt;

    private String email;
}
