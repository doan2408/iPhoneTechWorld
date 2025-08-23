package org.example.websitetechworld.Controller.ClientController.HoaDonClientController;

import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.LichSuHoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.HoaDonAndChiTietHoaDonClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyReviewClientResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon.HoaDonChiTietAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.example.websitetechworld.Services.ClientServices.HoaDonClientServices.MyOrderClientServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/client/my-order")
public class MyOrderController {
    private final MyOrderClientServices myOrderClientServices;
    private final HoaDonChiTietAdminServices hoaDonChiTietAdminServices;
    private final HoaDonAdminService hoaDonAdminService;

    public MyOrderController(MyOrderClientServices myOrderClientServices, HoaDonChiTietAdminServices hoaDonChiTietAdminServices, HoaDonAdminService hoaDonAdminService) {
        this.myOrderClientServices = myOrderClientServices;
        this.hoaDonChiTietAdminServices = hoaDonChiTietAdminServices;
        this.hoaDonAdminService = hoaDonAdminService;
    }

    @GetMapping()
    public Page<MyOrderClientResponse> myOrder(@RequestParam(defaultValue = "0", value = "pageNo") int pageNo,
                                               @RequestParam(defaultValue = "10",value = "pageSize") int pageSize,
                                               @RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "minPrice", required = false)BigDecimal minPrice,
                                               @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                                               @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
                                               @RequestParam(value = "endDate", required = false) LocalDateTime endDate,
                                               @RequestParam(value = "trangThaiGiaoHang", required = false) String trangThaiGiaoHang
                                               ) {
        Page<MyOrderClientResponse> myOrderClientResponses = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer userLoginId = customUserDetails.getId();
            myOrderClientResponses = myOrderClientServices.getOrderByUserLogin(userLoginId, pageNo, pageSize, keyword, minPrice, maxPrice, startDate, endDate, trangThaiGiaoHang);
        }
        return myOrderClientResponses;
        }

    @GetMapping("/review")
    public Page<MyReviewClientResponse> myReview(@RequestParam(defaultValue = "0", value = "pageNo") int pageNo,
                                               @RequestParam(defaultValue = "10",value = "pageSize") int pageSize) {
        Page<MyReviewClientResponse> myOrderClientResponses = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer userLoginId = customUserDetails.getId();
            myOrderClientResponses = myOrderClientServices.getReview(userLoginId,pageNo,pageSize);
        }
        return myOrderClientResponses;
    }

    @GetMapping("/mvd/{maVanDon}")
    public List<Integer> findIdHoaDonByMVD(@PathVariable String maVanDon, @RequestParam String sdt) {
        return myOrderClientServices.findIdHoaDonByMVDAndSdt(sdt, maVanDon);
    }

    @GetMapping("/{idHoaDon}")
    public HoaDonAdminResponse findById(@PathVariable("idHoaDon") int idHoaDon) {
        return myOrderClientServices.findById(idHoaDon);
    }

    @PutMapping("/thanh-toan")
    public ResponseEntity<?> thanhToan(@RequestBody RequestThanhToanTongHop shippingConfirm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userLoginId = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            userLoginId = customUserDetails.getId();
        }
        try {
            ThanhToanAdminResponse response = myOrderClientServices.xuLyThanhToanClient(shippingConfirm,userLoginId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }


    //cuong
    @GetMapping("/{idHoaDon}/chi-tiet")
    public List<HoaDonAndChiTietHoaDonClientResponse> getHoaDonChiTiet(@PathVariable Integer idHoaDon) {
        return myOrderClientServices.getHoaDonAndChiTiet(idHoaDon);
    }

    @DeleteMapping("/hdct/{maHd}")
    public ResponseEntity<?> deleteChiTietHoaDon(@PathVariable String maHd) {
        try {
            List<Integer> listIdHdct = myOrderClientServices.getHoaDonChiTietIdsByMaHoaDon(maHd);
            if (listIdHdct.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy chi tiết hóa đơn để xóa.");
            }
            for (Integer hdctId : listIdHdct) {
                hoaDonChiTietAdminServices.deleleHdct(hdctId);
            }
            String idHoaDonString = maHd.substring(2);
            Integer idHoaDon = Integer.parseInt(idHoaDonString);
            myOrderClientServices.deleteHoaDonById(idHoaDon);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xóa thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/{idHoaDon}/lich-su")
    public ResponseEntity<?> getPageLichSu(@PathVariable Integer idHoaDon) {
        return ResponseEntity.ok(myOrderClientServices.getLichSuHoaDon(idHoaDon));
    }

}
