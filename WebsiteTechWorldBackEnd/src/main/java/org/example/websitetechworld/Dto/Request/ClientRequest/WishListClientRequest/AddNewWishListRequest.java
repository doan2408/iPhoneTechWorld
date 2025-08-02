package org.example.websitetechworld.Dto.Request.ClientRequest.WishListClientRequest;

import lombok.Data;

@Data
public class AddNewWishListRequest {
    private Integer idSanPham;
    private Integer idRom;
    private Integer idMauSac;
    private Integer idSpct;
}
