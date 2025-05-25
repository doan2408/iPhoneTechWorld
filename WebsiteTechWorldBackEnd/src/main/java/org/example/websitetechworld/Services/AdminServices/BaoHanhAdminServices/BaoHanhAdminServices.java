package org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices;


import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Repository.BaoHanhRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaoHanhAdminServices {
    private final BaoHanhRepository baoHanhRepository;

    public BaoHanhAdminServices(BaoHanhRepository baoHanhRepository) {
        this.baoHanhRepository = baoHanhRepository;
    }
    public List<BaoHanhAdminResponse> getAllBaoHanh(){
        return baoHanhRepository.findAll().stream().map(BaoHanhAdminResponse::convertDto).collect(Collectors.toList());
    }





}
