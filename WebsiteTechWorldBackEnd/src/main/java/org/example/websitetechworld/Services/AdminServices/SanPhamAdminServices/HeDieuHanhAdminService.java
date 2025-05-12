package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.HeDieuHanh;
import org.example.websitetechworld.Repository.HeDieuHanhRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeDieuHanhAdminService {
    private final HeDieuHanhRepository heDieuHanhRepository;

    public Page<HeDieuHanh> getAllHeDieuHanh(Pageable pageable) {
        return heDieuHanhRepository.findAll(pageable);
    }
}
