package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.AdminProductResponse;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.NhaCungCapRepository;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    public SanPham createSanPhamAdmin(SanPhamAdminRequest sanPhamAdminRequest) {
        SanPham sanPham = modelMapper.map(sanPhamAdminRequest, SanPham.class);
        return sanPhamRepo.save(sanPham);
    }

    public SanPham updateSanPhamAdmin(Integer id, SanPhamAdminRequest sanPhamAdminRequest) {

        SanPham sanPham = sanPhamRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        sanPham.setTenSanPham(sanPhamAdminRequest.getTenSanPham());
        sanPham.setThuongHieu(sanPhamAdminRequest.getThuongHieu());
        sanPham.setSoLuongTonKho(sanPhamAdminRequest.getSoLuongTonKho());

        if (sanPhamAdminRequest.getIdNhaCungCap() != null) {
            NhaCungCap nhaCungCap = nhaCungCapRepository.findById(sanPhamAdminRequest.getIdNhaCungCap())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với ID: " + sanPhamAdminRequest.getIdNhaCungCap()));

            sanPham.setIdNhaCungCap(nhaCungCap);
        }

        return sanPhamRepo.save(sanPham);
    }

}

