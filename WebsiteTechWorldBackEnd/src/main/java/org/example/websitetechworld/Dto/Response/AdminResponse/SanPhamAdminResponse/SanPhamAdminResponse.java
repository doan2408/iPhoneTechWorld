
package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SanPhamAdminResponse {

    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String thuongHieu;

    private Integer soLuongTonKho;

    private String tenNhaCungCap;

    private String url;

    private Set<SanPhamChiTietResponse> sanPhamChiTiets;

}
