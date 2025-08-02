package org.example.websitetechworld.Services.ClientServices.HoaDonClientServices;

import org.example.websitetechworld.Dto.Response.ClientResponse.WishListClientResponse.WishListByClientResponse;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Entity.Wishlist;
import org.example.websitetechworld.Mapper.Client.Wishlist.WishListClientMapper;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.example.websitetechworld.Repository.WishlistRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListServices {
    private final WishlistRepostory wishlistRepostory;
    private final WishListClientMapper wishListClientMapper;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamRepository sanPhamRepository;

    public WishListServices(WishlistRepostory wishlistRepostory, WishListClientMapper wishListClientMapper, KhachHangRepository khachHangRepository, SanPhamChiTietRepository sanPhamChiTietRepository, SanPhamRepository sanPhamRepository) {
        this.wishlistRepostory = wishlistRepostory;
        this.wishListClientMapper = wishListClientMapper;
        this.khachHangRepository = khachHangRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    public Page<WishListByClientResponse> getWishlists(Integer khachHangId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return wishlistRepostory.findWishlistByKhacHang_Id(khachHangId,pageable).map(wishListClientMapper::toWishListClientResponse);
    }

    public boolean existsWishlist(Integer idKhachHangId,Integer idChiTietSanPham) {
        return wishlistRepostory.existsWishList(idKhachHangId,idChiTietSanPham);
    }

    public void addNewWishList(Integer idKhachHangId,Integer idChiTietSanPham) {
        KhachHang khachHang = khachHangRepository.findById(idKhachHangId).orElseThrow(
                () -> new RuntimeException("Khách hàng không tồn tại")
        );

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idChiTietSanPham).orElseThrow(
                () -> new RuntimeException("Sản phẩm không tồn tại")
        );
        Wishlist wishlist = new Wishlist();
        wishlist.setKhacHang(khachHang);
        wishlist.setChiTietSanPham(sanPhamChiTiet);

        wishlistRepostory.save(wishlist);
    }

    public Integer findByIdSpct(Integer idSp, Integer idRom, Integer idMauSac){
        return sanPhamRepository.findIdSpct(idSp,idRom,idMauSac);
    }

    public void deleteById(Integer idSpct){
        wishlistRepostory.deleteByChiTietSanPham_Id(idSpct);
    }
}
