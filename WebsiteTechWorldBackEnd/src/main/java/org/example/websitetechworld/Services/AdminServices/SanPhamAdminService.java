package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.SanPhamAdminResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Repository.SanPhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAdminService {

    private final SanPhamRepository sanPhamRepo;

    public SanPhamAdminResponse convert(SanPham productEntity) {
        SanPhamAdminResponse sanPhamAdminRespone = new SanPhamAdminResponse();
        sanPhamAdminRespone.setId(productEntity.getId());
        sanPhamAdminRespone.setMaSanPham(productEntity.getMaSanPham());
        sanPhamAdminRespone.setTenSanPham(productEntity.getTenSanPham());
        sanPhamAdminRespone.setThuongHieu(productEntity.getThuongHieu());
        sanPhamAdminRespone.setSoLuongTonKho(productEntity.getSoLuongTonKho());
        if(productEntity.getIdNhaCungCap() != null) {
            sanPhamAdminRespone.setTenNhaCungCap(productEntity.getIdNhaCungCap().getTenNhaCungCap());
        }
        return sanPhamAdminRespone;
    }

    public List<SanPhamAdminResponse> getAllSanPham() {
        List<SanPham> sanPhamList = sanPhamRepo.findAll();
        return sanPhamList.stream()
                .map(this :: convert)
                .collect(Collectors.toList());
    }


}
