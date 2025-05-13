package org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.AdminProductResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.hibernate.query.sqm.EntityTypeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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

    //this đại diện cho instance (thể hiện) của lớp hiện tại
    // – tức là class chứa phương thức getAllSanPham() và convert().
    public Page<AdminProductResponse> getAllSanPham(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> sanPham = sanPhamRepo.findAll(pageable);
        return sanPham.map(this::convert);
    }

    public AdminProductResponse detail(Integer id) {
        Optional<SanPham> sanPham = sanPhamRepo.findById(id);
        if (sanPham.isPresent()) {
            return convert(sanPham.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "khong tim thay san pham hop le:" + id);
        }
    }


}

