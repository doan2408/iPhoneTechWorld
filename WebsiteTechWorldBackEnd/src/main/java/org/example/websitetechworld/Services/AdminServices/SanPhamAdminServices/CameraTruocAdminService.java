package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Repository.CameraTruocRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CameraTruocAdminService {
    private final CameraTruocRepository cameraTruocRepository;

    public Page<CameraTruoc> getAllCameraTruoc(Pageable pageable) {
        return cameraTruocRepository.findAll(pageable);
    }
}
