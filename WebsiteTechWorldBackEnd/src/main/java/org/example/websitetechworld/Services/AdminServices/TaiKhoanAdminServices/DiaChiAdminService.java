package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminDiaChiRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Repository.DiaChiRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaChiAdminService {
    private final DiaChiRepository diaChiRepository;

    public AdminDiaChiResponse convertToResponse(DiaChi diaChi) {
        AdminDiaChiResponse adminDiaChiRequest = new AdminDiaChiResponse();
        adminDiaChiRequest.setId(diaChi.getId());
        adminDiaChiRequest.setTenKhachHang(diaChi.getIdKhachHang().getTenKhachHang());
        adminDiaChiRequest.setTenNguoiNhan(diaChi.getTenNguoiNhan());
        adminDiaChiRequest.setTenNguoiNhan(diaChi.getSdtNguoiNhan());
        adminDiaChiRequest.setSoNha(diaChi.getSoNha());
        adminDiaChiRequest.setTenDuong(diaChi.getTenDuong());
        adminDiaChiRequest.setXaPhuong(diaChi.getXaPhuong());
        adminDiaChiRequest.setQuanHuyen(diaChi.getQuanHuyen());
        adminDiaChiRequest.setTinhThanhPho(diaChi.getTinhThanhPho());
        adminDiaChiRequest.setDiaChiChinh(diaChi.getDiaChiChinh());
        return adminDiaChiRequest;
    }

    public AdminDiaChiRequest convertToRequest(DiaChi diaChi) {
        AdminDiaChiRequest adminDiaChiRequest = new AdminDiaChiRequest();
        adminDiaChiRequest.setId(diaChi.getId());
        adminDiaChiRequest.setTenNguoiNhan(diaChi.getTenNguoiNhan());
        adminDiaChiRequest.setSdtNguoiNhan(diaChi.getSdtNguoiNhan());
        adminDiaChiRequest.setSoNha(diaChi.getSoNha());
        adminDiaChiRequest.setTenDuong(diaChi.getTenDuong());
        adminDiaChiRequest.setXaPhuong(diaChi.getXaPhuong());
        adminDiaChiRequest.setQuanHuyen(diaChi.getQuanHuyen());
        adminDiaChiRequest.setTinhThanhPho(diaChi.getTinhThanhPho());
        adminDiaChiRequest.setDiaChiChinh(diaChi.getDiaChiChinh());
        return adminDiaChiRequest;
    }

    public void convertToDiaChiEntity(DiaChi diaChi,AdminDiaChiRequest adminDiaChiRequest) {
        diaChi.setId(adminDiaChiRequest.getId());
        diaChi.setTenNguoiNhan(adminDiaChiRequest.getTenNguoiNhan());
        diaChi.setSdtNguoiNhan(adminDiaChiRequest.getSdtNguoiNhan());
        diaChi.setSoNha(adminDiaChiRequest.getSoNha());
        diaChi.setTenDuong(adminDiaChiRequest.getTenDuong());
        diaChi.setXaPhuong(adminDiaChiRequest.getXaPhuong());
        diaChi.setQuanHuyen(adminDiaChiRequest.getQuanHuyen());
        diaChi.setTinhThanhPho(adminDiaChiRequest.getTinhThanhPho());
        diaChi.setDiaChiChinh(adminDiaChiRequest.getDiaChiChinh());
    }

    public List<AdminDiaChiResponse> getAllDiaChi(int idKhachHang) {
        return diaChiRepository.getAllDiaChi(idKhachHang);
    }

    public AdminDiaChiResponse getDiaChiById(int idDiaChi) {
        return diaChiRepository.getDiaChi(idDiaChi);
    }

    public AdminDiaChiRequest updateDiaChi(int idDiaChi, AdminDiaChiRequest adminDiaChiRequest) {
        DiaChi diaChiExisting = diaChiRepository.findById(idDiaChi).
                orElseThrow(() -> new IllegalArgumentException("Dia Chi Not Found"));
        convertToDiaChiEntity(diaChiExisting, adminDiaChiRequest);
        DiaChi diaChiUpdate = diaChiRepository.save(diaChiExisting);
        return convertToRequest(diaChiUpdate);
    }
}
