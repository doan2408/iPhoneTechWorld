package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RomAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RomAdminResponse;
import org.example.websitetechworld.Entity.Ram;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Repository.RomRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RomAdminService {
    private final RomRepository romRepo;
    private final ModelMapper modelMapper;

    private RomAdminResponse convert(Rom rom) {
        return modelMapper.map(rom, RomAdminResponse.class);
    }

    public Page<RomAdminResponse> getAllRoms(Pageable pageable) {
        Page<Rom> romPage = romRepo.findAll(pageable);
        return romPage.map(this::convert);
    }

    @Transactional
    public RomAdminResponse createRom(RomAdminRequest romAdminRequest) {
        Rom rom = romRepo.save(modelMapper.map(romAdminRequest, Rom.class));
        return convert(rom);
    }

    @Transactional
    public RomAdminResponse updateRom(Integer id, RomAdminRequest romAdminRequest) {
        Rom rom = romRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rom ID: " + id));
        modelMapper.map(romAdminRequest, rom);
        romRepo.save(rom);
        return convert(rom);
    }

    @Transactional
    public RomAdminResponse deleteRom(Integer id) {
        Rom rom = romRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rom ID: " + id));
        romRepo.deleteById(id);

        return convert(rom);
    }

    public RomAdminResponse detailRom(Integer id) {
        Rom rom = romRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy rom ID:" + id));
        return convert(rom);
    }
}
