package org.example.websitetechworld.Services.AdminServices;

import org.example.websitetechworld.Repository.HoaDonRepository;
import org.springframework.stereotype.Service;

@Service
public class HoaDonAdminService {
    private final HoaDonRepository hoaDonRepository;

    public HoaDonAdminService(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }


}
