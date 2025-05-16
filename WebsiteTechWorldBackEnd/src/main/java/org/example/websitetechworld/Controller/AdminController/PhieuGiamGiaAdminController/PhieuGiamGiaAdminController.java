package org.example.websitetechworld.Controller.AdminController.PhieuGiamGiaAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices.PhieuGiamGiaAdminServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/admin/phieu-giam-gia")
public class PhieuGiamGiaAdminController {

    private final PhieuGiamGiaAdminServices phieuGiamGiaAdminServices;

    public PhieuGiamGiaAdminController(PhieuGiamGiaAdminServices phieuGiamGiaAdminServices) {
        this.phieuGiamGiaAdminServices = phieuGiamGiaAdminServices;
    }

    @GetMapping
    public List<PhieuGiamGiaAdminResponse> getAll (
            @RequestParam(defaultValue = "0",value = "pageNo") int pageNo,
            @RequestParam(defaultValue = "2",value = "pageSize") int pageSize
    ) {
        return phieuGiamGiaAdminServices.getPagePhieuGiamGia(pageNo, pageSize);
    }

    @GetMapping ("/{id}")
    public PhieuGiamGiaAdminResponse getPhieuGiamGia (
            @PathVariable int id
    ) {
        return phieuGiamGiaAdminServices.getPhieuGiamGia(id);
    }

    @PostMapping
    public PhieuGiamGiaAdminResponse addPhieuGiamGia (
            @RequestBody PhieuGiamGiaAdminResponse phieuGiamGiaResponse
    ) {
        return phieuGiamGiaAdminServices.addPhieuGiamGia(phieuGiamGiaResponse);
    }

    @PutMapping ("/{id}")
    public PhieuGiamGiaAdminResponse updatePhieuGiamGia (
            @PathVariable int id,
            @RequestBody PhieuGiamGiaAdminResponse phieuGiamGiaResponse
    ) {
        return phieuGiamGiaAdminServices.updatePhieuGiamGia(id, phieuGiamGiaResponse);
    }

    @DeleteMapping ("/{id}")
    public String deletePhieuGiamGia (
            @PathVariable int id
    ) {
        return phieuGiamGiaAdminServices.deletePhieuGiamGia(id);
    }
}
