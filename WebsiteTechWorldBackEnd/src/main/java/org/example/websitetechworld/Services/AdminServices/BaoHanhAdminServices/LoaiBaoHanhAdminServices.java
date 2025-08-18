package org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices;

import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.LoaiBaoHanhAdminResponse;
import org.example.websitetechworld.Entity.LoaiBaoHanh;
import org.example.websitetechworld.Repository.LoaiBaoHanhRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LoaiBaoHanhAdminServices {
    private final LoaiBaoHanhRepository loaiBaohanhRepository;

    public LoaiBaoHanhAdminServices(LoaiBaoHanhRepository loaiBaohanhRepository) {
        this.loaiBaohanhRepository = loaiBaohanhRepository;
    }

    public Page<LoaiBaoHanhAdminResponse> getAllLoaiBaoHanh (String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return loaiBaohanhRepository.findAll(search, pageable)
                .map(loaiBaoHanh -> {
                    LoaiBaoHanhAdminResponse response = new LoaiBaoHanhAdminResponse();
                    response.setId(loaiBaoHanh.getId());
                    response.setTenLoaiBaoHanh(loaiBaoHanh.getTenLoaiBaoHanh());
                    response.setThoiGianThang(loaiBaoHanh.getThoiGianThang());
                    response.setMoTa(loaiBaoHanh.getMoTa());
                    return response;
                });
    }
}
