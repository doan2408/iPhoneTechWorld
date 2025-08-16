package org.example.websitetechworld.Controller.GuestController.MyOrderGuestController;

import org.example.websitetechworld.Dto.Request.CommonRequt.CreateActionBeforeAfter;
import org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService.XuLySauBanHangServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/action-after-case")
public class XuLySauBanHangController {
    private final XuLySauBanHangServices xuLySauBanHangServices;

    public XuLySauBanHangController(XuLySauBanHangServices xuLySauBanHangServices) {
        this.xuLySauBanHangServices = xuLySauBanHangServices;
    }

    @PostMapping("")
    public ResponseEntity<?> listXuLySauBanHang(@RequestBody CreateActionBeforeAfter createActionBeforeAfter) {
        xuLySauBanHangServices.taoDonXuLySauBanHang(createActionBeforeAfter);
        return ResponseEntity.ok("Tạo đơn xử lý sau bán hàng thành công");
    }
}
