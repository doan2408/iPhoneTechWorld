package org.example.websitetechworld.Controller.ClientController.HoaDonClientController;

import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.HoaDonAndChiTietHoaDonClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyReviewClientResponse;
import org.example.websitetechworld.Services.ClientServices.HoaDonClientServices.MyOrderClientServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/my-order")
public class MyOrderController {
    private final MyOrderClientServices myOrderClientServices;

    public MyOrderController(MyOrderClientServices myOrderClientServices) {
        this.myOrderClientServices = myOrderClientServices;
    }

    @GetMapping()
    public Page<MyOrderClientResponse> myOrder(@RequestParam(defaultValue = "0", value = "pageNo") int pageNo,
                                               @RequestParam(defaultValue = "10",value = "pageSize") int pageSize) {
        Page<MyOrderClientResponse> myOrderClientResponses = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer userLoginId = customUserDetails.getId();
            myOrderClientResponses = myOrderClientServices.getOrderByUserLogin(userLoginId,pageNo,pageSize);
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
    public List<Integer> findIdHoaDonByMVD(@PathVariable String maVanDon) {
        return myOrderClientServices.findIdHoaDonByMVD(maVanDon);
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
}
