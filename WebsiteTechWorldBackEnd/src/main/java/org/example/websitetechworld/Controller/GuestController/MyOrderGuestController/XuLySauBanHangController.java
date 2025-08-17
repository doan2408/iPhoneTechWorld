package org.example.websitetechworld.Controller.GuestController.MyOrderGuestController;

import org.example.websitetechworld.Dto.Request.CommonRequest.CreateActionBeforeAfter;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateReturnRequest;
import org.example.websitetechworld.Dto.Response.CommonResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Entity.XuLySauBanHang;
import org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService.XuLySauBanHangServices;
import org.springframework.data.domain.Page;
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

    @PostMapping("/tra-hang")
    public ResponseEntity<?> traHang(@RequestBody CreateReturnRequest request) {
        xuLySauBanHangServices.taoDonTraHang(request);
        return ResponseEntity.ok("Tạo đơn trả hàng thành công");
    }

    @GetMapping
    public ResponseEntity<?> getAllLyDoXuLy(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "5") Integer pageSIze){
        Page<ActionBeforeCaseResponse> banHangList = xuLySauBanHangServices.getAllLyDoXuLy(pageNo,pageSIze);
        return ResponseEntity.ok(banHangList);
    }
}
