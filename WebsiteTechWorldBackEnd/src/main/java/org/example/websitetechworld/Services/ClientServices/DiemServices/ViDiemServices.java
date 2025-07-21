package org.example.websitetechworld.Services.ClientServices.DiemServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.ViDiem;
import org.example.websitetechworld.Repository.ViDiemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViDiemServices {
    private final ViDiemRepository viDiemRepository;

    // get diemKhaDung by idKhachHang
    public Optional<ViDiem> getDiemKhaDung(int idKhachHang) {
        return viDiemRepository.findByIdKhachHang(idKhachHang);
    }

}
