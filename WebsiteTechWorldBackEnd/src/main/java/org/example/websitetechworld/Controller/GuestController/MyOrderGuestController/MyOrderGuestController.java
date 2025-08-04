package org.example.websitetechworld.Controller.GuestController.MyOrderGuestController;

import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Services.ClientServices.HoaDonClientServices.MyOrderClientServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my-order")
public class MyOrderGuestController {

    private final MyOrderClientServices myOrderClientServices;

    public MyOrderGuestController(MyOrderClientServices myOrderClientServices) {
        this.myOrderClientServices = myOrderClientServices;
    }

    @PutMapping("/thanh-toan")
    public ResponseEntity<?> thanhToan(@RequestBody RequestThanhToanTongHop shippingConfirm) {
        try {
            ThanhToanAdminResponse response = myOrderClientServices.xuLyThanhToanGuest(shippingConfirm);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
}
