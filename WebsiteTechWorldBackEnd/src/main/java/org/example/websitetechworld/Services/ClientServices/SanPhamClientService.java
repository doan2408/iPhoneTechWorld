package org.example.websitetechworld.Services.ClientServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.AdminProductRequest;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamClientService {
    private final SanPhamRepository sanPhamRepo;


    public AdminProductRequest convert(SanPham productEntity) {
        AdminProductRequest productRequest = new AdminProductRequest();
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

    //hiển thị sản phẩm phía client
    public List<AdminProductRequest> getAllSanPham() {
        List<SanPham> sanPhamList = sanPhamRepo.findAll();
        return sanPhamList.stream()
                .map(this :: convert)
                .collect(Collectors.toList());
    }
}
