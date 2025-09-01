package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.AccepAndInAccepAction;
import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.ChangeStatusRequest;
import org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService.XuLySauBanHangServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/action-after-case")
public class XuLySauBanHangAdminController {

    private final XuLySauBanHangServices xuLySauBanHangServices;

    public XuLySauBanHangAdminController(XuLySauBanHangServices xuLySauBanHangServices) {
        this.xuLySauBanHangServices = xuLySauBanHangServices;
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> changeStatus(@RequestBody AccepAndInAccepAction action) {
        xuLySauBanHangServices.changeStatus(action);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    @PostMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestBody ChangeStatusRequest request){
        xuLySauBanHangServices.changeStatus(request);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
}
