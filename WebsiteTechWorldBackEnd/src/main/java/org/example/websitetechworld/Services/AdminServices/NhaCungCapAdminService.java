package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Repository.NhaCungCapRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NhaCungCapAdminService {
    private final NhaCungCapRepository nhaCungCapRepository;

    public Page<NhaCungCap> getAllNhaCungCap(Pageable pageable) {
        return nhaCungCapRepository.findAll(pageable);
    }
}
