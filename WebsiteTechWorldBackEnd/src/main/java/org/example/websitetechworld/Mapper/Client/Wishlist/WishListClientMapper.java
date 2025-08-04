package org.example.websitetechworld.Mapper.Client.Wishlist;

import org.example.websitetechworld.Dto.Response.ClientResponse.WishListClientResponse.WishListByClientResponse;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.Wishlist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class WishListClientMapper {

    public WishListByClientResponse toWishListClientResponse(Wishlist wishlist){
        WishListByClientResponse response = new WishListByClientResponse();
        response.setId(wishlist.getId());
        if (wishlist.getKhacHang() != null){
            response.setIdKhachHang(wishlist.getKhacHang().getId());
            response.setTenKhachHang(wishlist.getKhacHang().getTenKhachHang());
        }
        if (wishlist.getChiTietSanPham() != null){
            response.setIdSp(wishlist.getChiTietSanPham().getIdSanPham().getId());
            response.setTenSanPham(wishlist.getChiTietSanPham().getIdSanPham().getTenSanPham());
            response.setGiaSanPham(wishlist.getChiTietSanPham().getGiaBan());
            response.setMau(wishlist.getChiTietSanPham().getIdMau().getTenMau());
            response.setRom(wishlist.getChiTietSanPham().getIdRom().getDungLuong());
        }
        Set<HinhAnh> hinhAnhs  = wishlist.getChiTietSanPham().getHinhAnhs();
        if (hinhAnhs != null && !hinhAnhs.isEmpty()){
            HinhAnh hinhAnhDauTien = hinhAnhs.iterator().next();
            String urlDauTien = hinhAnhDauTien.getUrl();
            response.setUrlImage(urlDauTien);
        }
        return response;
    }
}
