package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Response.TinhThanhDTO;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.TinhThanhService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/tinh-thanh")
public class TinhThanhController {
    private final TinhThanhService tinhThanhService;

    public TinhThanhController(TinhThanhService tinhThanhService) {
        this.tinhThanhService = tinhThanhService;
    }

    @GetMapping
    public List<TinhThanhDTO> getAll() {
        return tinhThanhService.getAll();
    }
}
