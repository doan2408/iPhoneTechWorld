package org.example.websitetechworld.Controller.ClientController.WishListController;

import org.example.websitetechworld.Dto.Request.ClientRequest.WishListClientRequest.AddNewWishListRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.WishListClientResponse.WishListByClientResponse;
import org.example.websitetechworld.Services.ClientServices.HoaDonClientServices.WishListServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/wish-list")
public class WishListController {
    private final WishListServices wishListServices;

    public WishListController(WishListServices wishListServices) {
        this.wishListServices = wishListServices;
    }

    @GetMapping()
    public ResponseEntity<?> getWishListById(@RequestParam(defaultValue = "0",value = "pageNo") Integer pageNo,
                                             @RequestParam(defaultValue = "5",value = "pageSize") Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Page<WishListByClientResponse> wishlists = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer userLoginId = customUserDetails.getId();
            wishlists = wishListServices.getWishlists(userLoginId,pageNo,pageSize);
        }
        return ResponseEntity.ok(wishlists);
    }

    @GetMapping("/check-ton-tai")
    public ResponseEntity<Boolean> checkExistWishList(@RequestParam Integer idSanPham,
                                                      @RequestParam Integer idRom,
                                                      @RequestParam Integer idMauSac) {
        Integer idSanPhamChiTiet = wishListServices.findByIdSpct(idSanPham,idRom,idMauSac);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean existWishList = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer userLoginId = customUserDetails.getId();
            existWishList = wishListServices.existsWishlist(userLoginId,idSanPhamChiTiet);
        }
        return ResponseEntity.ok(existWishList);
    }

    @PostMapping("")
    public ResponseEntity<String> saveWishList(@RequestBody AddNewWishListRequest request){
        Integer idSanPhamChiTiet = wishListServices.findByIdSpct(request.getIdSanPham(),request.getIdRom(),request.getIdMauSac());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer userLoginId = customUserDetails.getId();
            wishListServices.addNewWishList(userLoginId,idSanPhamChiTiet);
        }
        return ResponseEntity.ok("Đã thêm vào danh sách yêu thích");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWishList(@RequestBody AddNewWishListRequest request) {
        Integer idSanPhamChiTiet = null;
        if (request.getIdSpct() != null){
            idSanPhamChiTiet = request.getIdSpct();
        }else {
            idSanPhamChiTiet = wishListServices.findByIdSpct(request.getIdSanPham(),request.getIdRom(),request.getIdMauSac());
        }
        wishListServices.deleteById(idSanPhamChiTiet);
        return ResponseEntity.ok("Đã xóa khỏi danh sách yêu thích");
    }
}
