package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.ProductRequest;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {

    private final SanPhamRepository sanPhamRepo;


    public ProductRequest convert(SanPham productEntity) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setId(productEntity.getId());
        productRequest.setMaSanPham(productEntity.getMaSanPham());
        productRequest.setTenSanPham(productEntity.getTenSanPham());
        productRequest.setThuongHieu(productEntity.getThuongHieu());
        productRequest.setSoLuongTonKho(productEntity.getSoLuongTonKho());
        if(productEntity.getIdNhaCungCap() != null) {
            productRequest.setTenNhaCungCap(productEntity.getIdNhaCungCap().getTenNhaCungCap());
        }
        return productRequest;
    }

    public List<ProductRequest> getAllSanPham() {
        List<SanPham> sanPhamList = sanPhamRepo.findAll();
        return sanPhamList.stream()
                .map(this :: convert)
                .collect(Collectors.toList());
    }
}
