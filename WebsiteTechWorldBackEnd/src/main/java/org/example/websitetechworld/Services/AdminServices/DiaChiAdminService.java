package org.example.websitetechworld.Services.AdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Repository.DiaChiRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaChiAdminService {
    private final DiaChiRepository diaChiRepository;

    public AdminDiaChiResponse convert(DiaChi diaChi) {
        AdminDiaChiResponse adminDiaChiResponse = new AdminDiaChiResponse();
        adminDiaChiResponse.setId(diaChi.getId());
        adminDiaChiResponse.setTenKhachHang(diaChi.getIdKhachHang().getTenKhachHang());
        adminDiaChiResponse.setTenNguoiNhan(diaChi.getTenNguoiNhan());
        adminDiaChiResponse.setSoNha(diaChi.getSoNha());
        adminDiaChiResponse.setTenDuong(diaChi.getTenDuong());
        adminDiaChiResponse.setXaPhuong(diaChi.getXaPhuong());
        adminDiaChiResponse.setQuanHuyen(diaChi.getQuanHuyen());
        adminDiaChiResponse.setTinhThanhPho(diaChi.getTinhThanhPho());
        adminDiaChiResponse.setDiaChiChinh(diaChi.getDiaChiChinh());
        return adminDiaChiResponse;
    }

    public List<AdminDiaChiResponse> getAllDiaChi() {
        List<DiaChi> diacChi = diaChiRepository.findAll();
        return diacChi.stream()
                .map(this :: convert)
                .collect(Collectors.toList());
    }
}
