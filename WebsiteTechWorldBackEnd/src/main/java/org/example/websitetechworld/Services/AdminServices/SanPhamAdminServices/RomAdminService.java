package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RomAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.RomQuickAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.RomAdminResponse;
import org.example.websitetechworld.Entity.Rom;
import org.example.websitetechworld.Repository.RomRepository;
import org.example.websitetechworld.exception.BusinessException;
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

        if (!romAdminRequest.getDungLuong().trim().matches("^\\d+GB$")) {
            throw new BusinessException("Dung lượng phải là số, không khoảng trắng và kết thúc bằng 'GB' (ví dụ: 128GB, 64GB).");
        }

        if (!romAdminRequest.getLoai().trim().matches("^[a-zA-Z0-9]+$")) {
            throw new BusinessException("Loại chỉ được chứa chữ cái và số, không được chứa khoảng trắng hoặc ký tự đặc biệt.");
        }

        if (!romAdminRequest.getTocDoDocGhi().trim().matches("^\\d+\\s?MB/s$")) {
            throw new BusinessException("Tốc độ đọc/ghi phải là số và kết thúc bằng 'MB/s' (ví dụ: 500MB/s, 1000 MB/s).");
        }

        if (romAdminRequest.getNamRaMat().getYear() < 2007) {
            throw new BusinessException("Năm ra mắt ROM của iPhone phải từ 2007 trở đi");
        }

        if (romRepo.existsByDungLuong(romAdminRequest.getDungLuong())) {
            throw new BusinessException("Dung lượng đã tồn tại");
        }

        romAdminRequest.setDungLuong(romAdminRequest.getDungLuong().trim());
        romAdminRequest.setLoai(romAdminRequest.getLoai().trim());
        romAdminRequest.setTocDoDocGhi(romAdminRequest.getTocDoDocGhi().trim());

        Rom rom = modelMapper.map(romAdminRequest, Rom.class);
        Rom saved = romRepo.save(rom);
        return convert(saved);
    }

    @Transactional
    public RomAdminResponse createRomQuick(RomQuickAdminRequest romAdminRequest) {
        if (!romAdminRequest.getDungLuong().trim().matches("^\\d+GB$")) {
            throw new BusinessException("Dung lượng phải là số, không khoảng trắng và kết thúc bằng 'GB' (ví dụ: 128GB, 64GB).");
        }

        if (romRepo.existsByDungLuong(romAdminRequest.getDungLuong())) {
            throw new BusinessException("Dung lượng đã tồn tại");
        }

        romAdminRequest.setDungLuong(romAdminRequest.getDungLuong().trim());

        Rom rom = modelMapper.map(romAdminRequest, Rom.class);
        Rom saved = romRepo.save(rom);
        return convert(saved);
    }

    @Transactional
    public RomAdminResponse updateRom(Integer id, RomAdminRequest romAdminRequest) {
        Rom rom = romRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROM ID: " + id));

        if (!romAdminRequest.getDungLuong().trim().matches("^\\d+GB$")) {
            throw new BusinessException("Dung lượng phải là số, không khoảng trắng và kết thúc bằng 'GB' (ví dụ: 128GB, 64GB).");
        }

        if (!romAdminRequest.getLoai().trim().matches("^[a-zA-Z0-9]+$")) {
            throw new BusinessException("Loại chỉ được chứa chữ cái và số, không được chứa khoảng trắng hoặc ký tự đặc biệt.");
        }

        if (!romAdminRequest.getTocDoDocGhi().trim().matches("^\\d+\\s?MB/s$")) {
            throw new BusinessException("Tốc độ đọc/ghi phải là số và kết thúc bằng 'MB/s' (ví dụ: 500MB/s, 1000 MB/s).");
        }

        if (romAdminRequest.getNamRaMat().getYear() < 2007) {
            throw new BusinessException("Năm ra mắt ROM của iPhone phải từ 2007 trở đi");
        }

        if (romRepo.existsByDungLuongAndIdNot(romAdminRequest.getDungLuong(), rom.getId())) {
            throw new BusinessException("Dung lượng đã tồn tại");
        }

        romAdminRequest.setDungLuong(romAdminRequest.getDungLuong().trim());
        romAdminRequest.setLoai(romAdminRequest.getLoai().trim());
        romAdminRequest.setTocDoDocGhi(romAdminRequest.getTocDoDocGhi().trim());

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

}