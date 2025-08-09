package org.example.websitetechworld.Controller.GuestController.MyOrderGuestController;

import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon.HoaDonChiTietAdminServices;
import org.example.websitetechworld.Services.ClientServices.HoaDonClientServices.MyOrderClientServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my-order")
public class MyOrderGuestController {

    private final MyOrderClientServices myOrderClientServices;
    private final HoaDonChiTietAdminServices hoaDonChiTietAdminServices;

    public MyOrderGuestController(MyOrderClientServices myOrderClientServices, HoaDonChiTietAdminServices hoaDonChiTietAdminServices) {
        this.myOrderClientServices = myOrderClientServices;
        this.hoaDonChiTietAdminServices = hoaDonChiTietAdminServices;
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
}
