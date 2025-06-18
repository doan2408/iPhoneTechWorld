package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SanPhamAdminUpdateResponse {
    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String thuongHieu;

    private TrangThaiSanPham trangThaiSanPham;

    private Integer idNhaCungCap;

    private Set<SanPhamChiTietAdminDetailResponse> sanPhamChiTiets;

}
