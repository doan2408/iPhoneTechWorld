
package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

//    private String url;

}
