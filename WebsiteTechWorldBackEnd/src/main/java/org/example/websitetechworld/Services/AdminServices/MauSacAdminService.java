package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.MauSac;
import org.example.websitetechworld.Repository.MauSacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MauSacAdminService {
    private final MauSacRepository mauSacRepository;

    public Page<MauSac> getAllMauSac(Pageable pageable) {
        return mauSacRepository.findAll(pageable);
    }
}
