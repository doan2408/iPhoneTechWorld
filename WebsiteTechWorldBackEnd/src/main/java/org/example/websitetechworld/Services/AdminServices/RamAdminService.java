package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.Ram;
import org.example.websitetechworld.Repository.RamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RamAdminService {
    private final RamRepository ramRepository;

    public Page<Ram> getAllRam(Pageable pageable) {
        return ramRepository.findAll(pageable);
    }
}
