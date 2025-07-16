package org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderProductClientResponse {
    private Integer idSanPhamChiTiet;
    private String urlImage;
    private String tenSanPham;
    private String colorName;
    private String rom;
    private String giaSanPham;

}
