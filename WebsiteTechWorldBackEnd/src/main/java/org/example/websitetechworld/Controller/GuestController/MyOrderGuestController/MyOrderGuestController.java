package org.example.websitetechworld.Controller.GuestController.MyOrderGuestController;

import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiTrangHoaDonResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon.HoaDonChiTietAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
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
@RequestMapping("/my-order")
public class MyOrderGuestController {

    private final MyOrderClientServices myOrderClientServices;
    private final HoaDonChiTietAdminServices hoaDonChiTietAdminServices;
    private final ImeiDaBanAdminServices imeiDaBanAdminServices;
    private final HoaDonAdminService hoaDonAdminService;

    public MyOrderGuestController(MyOrderClientServices myOrderClientServices, HoaDonChiTietAdminServices hoaDonChiTietAdminServices, ImeiDaBanAdminServices imeiDaBanAdminServices, HoaDonAdminService hoaDonAdminService) {
        this.myOrderClientServices = myOrderClientServices;
        this.hoaDonChiTietAdminServices = hoaDonChiTietAdminServices;
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
        this.hoaDonAdminService = hoaDonAdminService;
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
    @GetMapping("/{ctHoaDonId}/hdct-by-imei-da-ban")
    public ResponseEntity<List<ImeiTrangHoaDonResponse>> listSpctByImeiDaBan(@PathVariable(value = "ctHoaDonId") Integer ctHoaDonId){
        return ResponseEntity.ok(imeiDaBanAdminServices.imeiTrangHoaDonList(ctHoaDonId));
    }

    @GetMapping("/{idHoaDon}")
    public HoaDonAdminResponse findById(@PathVariable("idHoaDon") int idHoaDon) {
        return hoaDonAdminService.findById(idHoaDon);
    }
}
