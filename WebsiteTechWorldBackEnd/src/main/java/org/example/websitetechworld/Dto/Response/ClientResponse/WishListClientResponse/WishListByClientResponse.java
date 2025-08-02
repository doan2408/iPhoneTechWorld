package org.example.websitetechworld.Dto.Response.ClientResponse.WishListClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WishListByClientResponse {
    private Integer id;
    private Integer idKhachHang;
    private String tenKhachHang;
    private Integer idSp;
    private String tenSanPham;
    private String mau;
    private String rom;
    private BigDecimal giaSanPham;
    private String urlImage;

}
