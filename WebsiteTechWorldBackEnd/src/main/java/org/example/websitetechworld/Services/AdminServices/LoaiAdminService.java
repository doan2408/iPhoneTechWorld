package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Loai;
import org.example.websitetechworld.Repository.LoaiRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoaiAdminService {
    private final LoaiRepository loaiRepository;

    public Page<Loai> getAllLoai(Pageable pageable) {
        return loaiRepository.findAll(pageable);
    }
}
