package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Repository.RomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RomAdminService {
    private final RomRepository romRepo;

    public Page<Rom> getAllRoms(Pageable pageable) {
        return romRepo.findAll(pageable);
    }
}
