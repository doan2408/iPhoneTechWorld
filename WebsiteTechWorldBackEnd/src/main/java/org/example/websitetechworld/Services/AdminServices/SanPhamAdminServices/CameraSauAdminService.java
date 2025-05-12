package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.CameraSau;
import org.example.websitetechworld.Repository.CameraSauRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CameraSauAdminService {
    private final CameraSauRepository cameraSauRepository;

    public Page<CameraSau> getAllCameraSau(Pageable pageable) {
        return cameraSauRepository.findAll(pageable);
    }
}
