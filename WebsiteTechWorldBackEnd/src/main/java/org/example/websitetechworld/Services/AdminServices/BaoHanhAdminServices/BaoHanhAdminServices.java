package org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices;

import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Entity.BaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Repository.BaoHanhRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BaoHanhAdminServices {
    private final BaoHanhRepository baoHanhRepository;

    public BaoHanhAdminServices(BaoHanhRepository baoHanhRepository) {
        this.baoHanhRepository = baoHanhRepository;
    }

    public Page<BaoHanhAdminResponse> getAllBaoHanh (String search, TrangThaiBaoHanh trangThai,
                                                        LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                        int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BaoHanh> baoHanhs = baoHanhRepository.findAll(search, trangThai, ngayBatDau, ngayKetThuc, pageable);
        return baoHanhs.map(BaoHanhAdminResponse::convertDto);
    }
}
