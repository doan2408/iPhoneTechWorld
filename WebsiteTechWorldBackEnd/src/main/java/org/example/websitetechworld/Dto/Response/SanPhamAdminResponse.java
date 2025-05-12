package org.example.websitetechworld.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamAdminResponse {
    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String thuongHieu;

    private Integer soLuongTonKho;

    private String tenNhaCungCap;

    private String url;

}
