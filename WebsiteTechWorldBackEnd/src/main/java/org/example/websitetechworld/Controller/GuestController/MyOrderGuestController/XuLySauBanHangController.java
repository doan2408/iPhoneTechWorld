package org.example.websitetechworld.Controller.GuestController.MyOrderGuestController;

import org.apache.commons.collections4.Get;
import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.CapNhatTrangThaiDTO;
import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.AccepAndInAccepAction;
import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.ChangeStatusRequest;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateActionBeforeAfter;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateReturnRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.StatsTrangThaiHoaDon;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse;
import org.example.websitetechworld.Entity.XuLySauBanHang;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.example.websitetechworld.Services.ClientServices.DiemServices.LichSuDiemService;
import org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService.XuLySauBanHangServices;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/action-after-case")
public class XuLySauBanHangController {
    private final XuLySauBanHangServices xuLySauBanHangServices;
    private final HoaDonAdminService hoaDonAdminService;
    private final GiaoHangAdminServices giaoHangAdminServices;
    private final LichSuDiemService lichSuDiemService;

    public XuLySauBanHangController(XuLySauBanHangServices xuLySauBanHangServices, HoaDonAdminService hoaDonAdminService, GiaoHangAdminServices giaoHangAdminServices, LichSuDiemService lichSuDiemService) {
        this.xuLySauBanHangServices = xuLySauBanHangServices;
        this.hoaDonAdminService = hoaDonAdminService;
        this.giaoHangAdminServices = giaoHangAdminServices;
        this.lichSuDiemService = lichSuDiemService;
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
    public ResponseEntity<?> getAllLyDoXuLy(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) CaseType status,
            @RequestParam(defaultValue = "asc") String sortDir){
        Page<ActionBeforeCaseResponse> banHangList = xuLySauBanHangServices.getAllLyDoXuLy(pageNo,pageSize,search,status,sortDir);
        return ResponseEntity.ok(banHangList);
    }

    @GetMapping("/detail/{idHoaDon}")
    public ResponseEntity<?> findByIdHoaDon(@PathVariable Integer idHoaDon){
        List<XuLyChiTietResponse> banHangList = xuLySauBanHangServices.findByIdHoaDon(idHoaDon);
        return ResponseEntity.ok(banHangList);
    }

    @PostMapping("/change-status")
    public ResponseEntity<?> changeStatus(@RequestBody ChangeStatusRequest request){
        xuLySauBanHangServices.changeStatus(request);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
    @PutMapping("/{idHoaDon}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer idHoaDon,
                                          @RequestBody CapNhatTrangThaiDTO request) {
        TrangThaiGiaoHang trangThai = TrangThaiGiaoHang.fromDisplayName(request.getTrangThaiDonHang());
        if (trangThai.equals(TrangThaiGiaoHang.RETURNS)){
            giaoHangAdminServices.updateStatus(idHoaDon, trangThai);
        }
        return ResponseEntity.ok("Đã cập nhật trạng thái: " + trangThai.getDisplayName());
    }
    @PutMapping("/update-status")
    public ResponseEntity<?> changeStatus(@RequestBody AccepAndInAccepAction action) {
        xuLySauBanHangServices.changeStatus(action);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
    @GetMapping("/stats")
    public ResponseEntity<?> countDonHangByStatus (){
        StatsTrangThaiHoaDon banHangList = xuLySauBanHangServices.getStatsTrangThaiHoaDon();
        return ResponseEntity.ok(banHangList);
    }
}
