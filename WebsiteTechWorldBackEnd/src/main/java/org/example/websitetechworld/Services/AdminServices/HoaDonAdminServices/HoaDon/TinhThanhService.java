package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon;

import org.example.websitetechworld.Dto.Response.TinhThanhDTO;
import org.example.websitetechworld.Repository.TinhThanhRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TinhThanhService {
    private final TinhThanhRepository tinhThanhRepository;

    public TinhThanhService(TinhThanhRepository tinhThanhRepository) {
        this.tinhThanhRepository = tinhThanhRepository;
    }

    public List<TinhThanhDTO> getAll() {
        return tinhThanhRepository.findAll()
                .stream()
                .map(t -> new TinhThanhDTO(t.getId(), t.getTen(), t.getPhiShip()))
                .collect(Collectors.toList());
    }
}
