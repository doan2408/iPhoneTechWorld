package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.ResourceNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {
    private final SanPhamRepository sanPhamRepo;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final MauSacRepository mauSacRepository;
    private final RamRepository ramRepository;
    private final RomRepository romRepository;
    private final ManHinhRepository manHinhRepository;
    private final HeDieuHanhRepository heDieuHanhRepository;
    private final PinRepository pinRepository;
    private final CpuRepository cpuRepository;
    private final CameraTruocRepository cameraTruocRepository;
    private final CameraSauRepository cameraSauRepository;
    private final XuatXuRepository xuatXuRepository;
    private final LoaiRepository loaiRepository;

    private final ModelMapper modelMapper;

    public SanPhamAdminResponse convert(SanPham productEntity) {
        SanPhamAdminResponse productResponse = new SanPhamAdminResponse();
        productResponse.setId(productEntity.getId());
        productResponse.setMaSanPham(productEntity.getMaSanPham());
        productResponse.setTenSanPham(productEntity.getTenSanPham());
        productResponse.setThuongHieu(productEntity.getThuongHieu());
        if (productEntity.getIdNhaCungCap() != null) {
            productResponse.setTenNhaCungCap(productEntity.getIdNhaCungCap().getTenNhaCungCap());
        }

        return productResponse;
    }

    //this đại diện cho instance (thể hiện) của lớp hiện tại
    // – tức là class chứa phương thức getAllSanPham() và convert().
    public Page<SanPhamHienThiAdminResponse> getAllSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sanPhamRepo.getAllHienThi(pageable);
    }

    public SanPhamAdminResponse detailSanPhamAdmin(Integer id) {
        Optional<SanPham> sanPham = sanPhamRepo.findById(id);
        if (sanPham.isPresent()) {
            return convert(sanPham.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "khong tim thay san pham hop le:" + id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public SanPhamAdminResponse createSanPhamAdmin(SanPhamAdminRequest sanPhamAdminRequest) {
        SanPham sanPham = sanPhamRepo.save(modelMapper.map(sanPhamAdminRequest, SanPham.class));
        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));

            sanPham.setIdNhaCungCap(nhaCungCap);
        }

        sanPham = sanPhamRepo.save(sanPham);

        Set<SanPhamChiTiet> chiTiets = sanPhamAdminRequest.getSanPhamChiTiets().stream().map(rq -> {
            SanPhamChiTiet chiTiet = new SanPhamChiTiet();
            chiTiet.setSoLuong(rq.getSoLuong());
            chiTiet.setGiaBan(rq.getGiaBan());

            chiTiet.setIdMau(mauSacRepository.findById(rq.getIdMau())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màu sắc với ID: " + rq.getIdMau())));
            chiTiet.setIdRam(ramRepository.findById(rq.getIdRam())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RAM với ID: " + rq.getIdRam())));
            chiTiet.setIdRom(romRepository.findById(rq.getIdRom())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ROM với ID: " + rq.getIdRom())));
            chiTiet.setIdManHinh(manHinhRepository.findById(rq.getIdManHinh())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy màn hình với ID: " + rq.getIdManHinh())));
            chiTiet.setIdHeDieuHanh(heDieuHanhRepository.findById(rq.getIdHeDieuHanh())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ điều hành với ID: " + rq.getIdHeDieuHanh())));
            chiTiet.setIdPin(pinRepository.findById(rq.getIdPin())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy pin với ID: " + rq.getIdPin())));
            chiTiet.setIdCpu(cpuRepository.findById(rq.getIdCpu())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CPU với ID: " + rq.getIdCpu())));
            chiTiet.setIdCameraTruoc(cameraTruocRepository.findById(rq.getIdCameraTruoc())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy camera trước với ID: " + rq.getIdCameraTruoc())));
            chiTiet.setIdCameraSau(cameraSauRepository.findById(rq.getIdCameraSau())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy camera sau với ID: " + rq.getIdCameraSau())));
            chiTiet.setIdXuatXu(xuatXuRepository.findById(rq.getIdXuatXu())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xuất xứ với ID: " + rq.getIdXuatXu())));
            chiTiet.setIdLoai(loaiRepository.findById(rq.getIdLoai())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy loại với ID: " + rq.getIdXuatXu())));

            // Gán hình ảnh (nếu có)
            if (rq.getHinhAnhs() != null) {
                for (HinhAnh ha : rq.getHinhAnhs()) {
                    ha.setIdSanPhamChiTiet(chiTiet); // rất quan trọng
                }
                chiTiet.setHinhAnhs(rq.getHinhAnhs());
            }

            // Gán imei (nếu có)
            if (rq.getImeis() != null) {
                for (Imei imei : rq.getImeis()) {
                    imei.setIdSanPhamChiTiet(chiTiet);
                }
                chiTiet.setImeis(rq.getImeis());
            }

            return chiTiet;
        }).collect(Collectors.toSet());

        sanPham.setSanPhamChiTiets(chiTiets);

        sanPham = sanPhamRepo.save(sanPham);

        return modelMapper.map(sanPham, SanPhamAdminResponse.class);
    }

    @Transactional
    public SanPhamAdminResponse updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {

        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());

        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));

            sanPham.setIdNhaCungCap(nhaCungCap);
        }

        SanPham updatedSanPham = sanPhamRepo.save(sanPham);

        SanPhamAdminResponse dto = new SanPhamAdminResponse();
        dto.setId(updatedSanPham.getId());
        dto.setTenSanPham(updatedSanPham.getTenSanPham());
        dto.setThuongHieu(updatedSanPham.getThuongHieu());
        dto.setSoLuongTonKho(updatedSanPham.getSoLuongTonKho());

        if (updatedSanPham.getIdNhaCungCap() != null) {
            dto.setTenNhaCungCap(updatedSanPham.getIdNhaCungCap().getTenNhaCungCap());
        }

        return dto;
    }


    @Transactional
    public SanPhamAdminResponse deleteSanPhamAdmin(Integer id) {

        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        SanPhamAdminResponse sanPhamAdminResponse = new SanPhamAdminResponse();
        sanPhamAdminResponse.setId(sanPham.getId());
        sanPhamAdminResponse.setTenSanPham(sanPham.getTenSanPham());
        sanPhamAdminResponse.setThuongHieu(sanPham.getThuongHieu());

        sanPhamRepo.deleteById(id);

        return sanPhamAdminResponse;
    }

}


