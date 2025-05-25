package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.NhaCungCapRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {
    private final SanPhamRepository sanPhamRepo;

    private final NhaCungCapRepository nhaCungCapRepository;

    private final ModelMapper modelMapper;

    public SanPhamAdminResponse convert(SanPham productEntity) {
        SanPhamAdminResponse productResponse = new SanPhamAdminResponse();
        productResponse.setId(productEntity.getId());
        productResponse.setMaSanPham(productEntity.getMaSanPham());
        productResponse.setTenSanPham(productEntity.getTenSanPham());
        productResponse.setThuongHieu(productEntity.getThuongHieu());
        productResponse.setSoLuongTonKho(productEntity.getSoLuongTonKho());
        if (productEntity.getIdNhaCungCap() != null) {
            productResponse.setTenNhaCungCap(productEntity.getIdNhaCungCap().getTenNhaCungCap());
        }

        return productResponse;
    }

    //this đại diện cho instance (thể hiện) của lớp hiện tại
    // – tức là class chứa phương thức getAllSanPham() và convert().
    public Page<SanPhamAdminResponse> getAllSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> sanPham = sanPhamRepo.findAll(pageable);
        return sanPham.map(this::convert);
    }

    public SanPhamAdminResponse detailSanPhamAdmin(Integer id) {
        Optional<SanPham> sanPham = sanPhamRepo.findById(id);
        if (sanPham.isPresent()) {
            return convert(sanPham.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "khong tim thay san pham hop le:" + id);
        }
    }

    @Transactional
    public SanPhamAdminResponse createSanPhamAdmin(SanPhamAdminRequest sanPhamAdminRequest) {
        SanPham sanPham = sanPhamRepo.save(modelMapper.map(sanPhamAdminRequest, SanPham.class));
        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));

            sanPham.setIdNhaCungCap(nhaCungCap);
        }
        return convert(sanPham);
    }

    @Transactional
    public SanPhamAdminResponse updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {

        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));

        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
        sanPham.setSoLuongTonKho(sanPhamAdminRequest.getSoLuongTonKho());

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


