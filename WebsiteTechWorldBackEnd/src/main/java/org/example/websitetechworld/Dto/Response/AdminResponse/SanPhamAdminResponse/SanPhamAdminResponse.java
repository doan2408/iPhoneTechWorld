
package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SanPhamAdminResponse {

    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String thuongHieu;

    @Enumerated(EnumType.STRING)
    private TrangThaiSanPham trangThaiSanPham;

    private Integer idNhaCungCap;

//    private String url;

    private Set<SanPhamChiTietResponse> sanPhamChiTiets;



}
