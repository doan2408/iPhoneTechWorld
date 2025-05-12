package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Repository.ManHinhRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManHinhAdminService {
    private final ManHinhRepository manHinhRepository;

    public Page<ManHinh> getAllManHinh(Pageable pageable) {
        return manHinhRepository.findAll(pageable);
    }
}
