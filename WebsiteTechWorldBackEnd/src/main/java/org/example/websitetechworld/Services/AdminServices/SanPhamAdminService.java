package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Request.AdminRequest.AdminProductRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminProductResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {
    private final SanPhamRepository sanPhamRepo;


    public AdminProductResponse convert(SanPham productEntity) {
        AdminProductResponse productResponse = new AdminProductResponse();
        productResponse.setId(productEntity.getId());
        productResponse.setMaSanPham(productEntity.getMaSanPham());
        productResponse.setTenSanPham(productEntity.getTenSanPham());
        productResponse.setThuongHieu(productEntity.getThuongHieu());
        productResponse.setSoLuongTonKho(productEntity.getSoLuongTonKho());
        if(productEntity.getIdNhaCungCap() != null) {
            productResponse.setTenNhaCungCap(productEntity.getIdNhaCungCap().getTenNhaCungCap());
        }
        return productResponse;
    }

    public List<AdminProductResponse> getAllSanPham() {
        List<SanPham> sanPhamList = sanPhamRepo.findAll();
        return sanPhamList.stream().map(this::convert).collect(Collectors.toList());
    }


}

