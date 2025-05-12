package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Pin;
import org.example.websitetechworld.Repository.PinRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PinAdminService {
    private final PinRepository pinRepository;

    public Page<Pin> getAllPin(Pageable pageable) {
        return pinRepository.findAll(pageable);
    }
}
