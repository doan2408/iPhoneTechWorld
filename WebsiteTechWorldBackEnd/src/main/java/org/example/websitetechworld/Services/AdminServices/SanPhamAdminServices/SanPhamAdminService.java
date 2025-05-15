package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.AdminProductResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.NhaCungCapRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {
    private final SanPhamRepository sanPhamRepo;

    private final NhaCungCapRepository nhaCungCapRepository;

    private final ModelMapper modelMapper;

    public AdminProductResponse convert(SanPham productEntity) {
        AdminProductResponse productResponse = new AdminProductResponse();
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

    public List<AdminProductResponse> getAllSanPham() {
        List<SanPham> sanPhamList = sanPhamRepo.findAll();
        return sanPhamList.stream().map(this::convert).collect(Collectors.toList());
    }

    @Transactional
    public SanPham createSanPhamAdmin(SanPhamAdminRequest sanPhamAdminRequest) {
        SanPham sanPham = modelMapper.map(sanPhamAdminRequest, SanPham.class);
        return sanPhamRepo.save(sanPham);
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

    public SanPhamAdminResponse detailSanPhamAdmin(Integer id) {
        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id));


        SanPhamAdminResponse sanPhamAdminResponse = new SanPhamAdminResponse();
        sanPhamAdminResponse.setId(sanPham.getId());
        sanPhamAdminResponse.setMaSanPham(sanPham.getMaSanPham());
        sanPhamAdminResponse.setTenSanPham(sanPham.getTenSanPham());
        sanPhamAdminResponse.setThuongHieu(sanPham.getThuongHieu());
        sanPhamAdminResponse.setSoLuongTonKho(sanPham.getSoLuongTonKho());

        NhaCungCap nhaCungCap = sanPham.getIdNhaCungCap();
        if (nhaCungCap != null) {
            sanPhamAdminResponse.setTenNhaCungCap(nhaCungCap.getTenNhaCungCap());
        }

        return sanPhamAdminResponse;
    }
}


