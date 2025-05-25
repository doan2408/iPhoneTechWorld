package org.example.websitetechworld.Controller.AdminController.BaoHanhAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;

import org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices.BaoHanhAdminServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class BaoHanhAdminController {
    private final BaoHanhAdminServices baoHanhAdminServices;

    public BaoHanhAdminController(BaoHanhAdminServices baoHanhAdminServices) {
        this.baoHanhAdminServices = baoHanhAdminServices;
    }
    @GetMapping("/bao-hanh")
    public List<BaoHanhAdminResponse>getAll(){
        return baoHanhAdminServices.getAllBaoHanh();
    }
}
