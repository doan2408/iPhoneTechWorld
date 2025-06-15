package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamAdminRequest {
//    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String thuongHieu;

    private Integer idNhaCungCap;

    private Set<SanPhamChiTietAdminRepuest> sanPhamChiTiets;

}
