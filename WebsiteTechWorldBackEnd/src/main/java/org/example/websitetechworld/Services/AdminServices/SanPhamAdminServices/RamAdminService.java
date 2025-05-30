package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RamAdminResponse;
import org.example.websitetechworld.Entity.Ram;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Repository.RamRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RamAdminService {
    private final RamRepository ramRepository;
    private final ModelMapper modelMapper;

    private RamAdminResponse convert(Ram ram) {
        return modelMapper.map(ram, RamAdminResponse.class);
    }

    public Page<RamAdminResponse> getAllRam(Pageable pageable) {
        Page<Ram> rams = ramRepository.findAll(pageable);
        return rams.map(this::convert);
    }

    @Transactional
    public RamAdminResponse createRam(RamAdminRequest ramAdminRequest) {
        Ram ram = ramRepository.save(modelMapper.map(ramAdminRequest, Ram.class));
        return convert(ram);
    }

    @Transactional
    public RamAdminResponse updateRam(Integer id, RamAdminRequest RamAdminRequest) {
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Ram ID: " + id));
        modelMapper.map(RamAdminRequest, ram);
        ramRepository.save(ram);
        return convert(ram);
    }

    @Transactional
    public RamAdminResponse deleteRam(Integer id) {
        Ram ram = ramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ram ID: " + id));
        ramRepository.deleteById(id);

        return convert(ram);
    }

    public RamAdminResponse detailRam(Integer id) {
        Ram ram = ramRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ram ID:" + id));
        return convert(ram);
    }
}
