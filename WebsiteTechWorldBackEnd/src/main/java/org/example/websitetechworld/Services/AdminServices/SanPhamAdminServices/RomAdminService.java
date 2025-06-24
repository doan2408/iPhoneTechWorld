package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RomAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RomAdminResponse;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Repository.RomRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RomAdminService {
    private final RomRepository romRepo;
    private final ModelMapper modelMapper;

    private RomAdminResponse convert(Rom rom) {
        return modelMapper.map(rom, RomAdminResponse.class);
    }

    private List<RomAdminResponse> convertList(List<Rom> roms) {
        return roms.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public List<RomAdminResponse> getAllRomsList() {
        List<Rom> list = romRepo.findAll();
        return convertList(list);
    }

    public Page<RomAdminResponse> getAllRoms(Pageable pageable) {
        Page<Rom> romPage = romRepo.findAll(pageable);
        return romPage.map(this::convert);
    }

    public Page<RomAdminResponse> searchRoms(String search, Pageable pageable) {
        Page<Rom> romPage;
        if (StringUtils.hasText(search)) {
            romPage = romRepo.findByDungLuongContainingIgnoreCaseOrNhaSanXuatContainingIgnoreCase(search, search, pageable);
        } else {
            romPage = romRepo.findAll(pageable);
        }
        return romPage.map(this::convert);
    }

    @Transactional
    public RomAdminResponse createRom(RomAdminRequest romAdminRequest) {
        if (romRepo.existsByDungLuong(romAdminRequest.getDungLuong())) {
            throw new IllegalArgumentException("Dung lượng đã tồn tại");
        }
        Rom rom = modelMapper.map(romAdminRequest, Rom.class);
        Rom saved = romRepo.save(rom);
        return convert(saved);
    }

    @Transactional
    public RomAdminResponse updateRom(Integer id, RomAdminRequest romAdminRequest) {
        Rom rom = romRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROM ID: " + id));
        if (romRepo.existsByDungLuongAndIdNot(romAdminRequest.getDungLuong(), id)) {
            throw new IllegalArgumentException("Dung lượng đã tồn tại");
        }
        modelMapper.map(romAdminRequest, rom);
        Rom saved = romRepo.save(rom);
        return convert(saved);
    }

    @Transactional
    public RomAdminResponse deleteRom(Integer id) {
        Rom rom = romRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROM ID: " + id));
        romRepo.deleteById(id);
        return convert(rom);
    }

    public RomAdminResponse detailRom(Integer id) {
        Rom rom = romRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROM ID: " + id));
        return convert(rom);
    }

    public boolean existsByDungLuong(String dungLuong) {
        return romRepo.existsByDungLuong(dungLuong);
    }

    public boolean existsByDungLuongAndIdNot(String dungLuong, Integer id) {
        return romRepo.existsByDungLuongAndIdNot(dungLuong, id);
    }
}