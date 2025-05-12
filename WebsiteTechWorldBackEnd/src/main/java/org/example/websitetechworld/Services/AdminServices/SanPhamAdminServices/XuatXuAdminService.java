package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.XuatXu;
import org.example.websitetechworld.Repository.XuatXuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class XuatXuAdminService {
    private final XuatXuRepository xuatXuRepo;

    public Page<XuatXu> getAllXuatXu(Pageable pageable) {
        return xuatXuRepo.findAll(pageable);
    }
}
