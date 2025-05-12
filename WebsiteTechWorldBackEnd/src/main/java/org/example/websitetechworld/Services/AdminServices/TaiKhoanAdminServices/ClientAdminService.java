package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientAdminService {
    private final KhachHangRepository khachHangRepository;

    //convert
    private AdminClientResponse convert(KhachHang khachHang) {
        AdminClientResponse adminClientResponse = new AdminClientResponse();
        adminClientResponse.setId(khachHang.getId());
        adminClientResponse.setMaKhachHang(khachHang.getMaKhachHang());
        adminClientResponse.setTenKhachHang(khachHang.getTenKhachHang());
        adminClientResponse.setSdt(khachHang.getSdt());
        adminClientResponse.setTaiKhoan(khachHang.getTaiKhoan());
        adminClientResponse.setMatKhau(khachHang.getMatKhau());
        adminClientResponse.setEmail(khachHang.getEmail());
        adminClientResponse.setNgaySinh(khachHang.getNgaySinh());
        adminClientResponse.setGioiTinh(khachHang.getGioiTinh());
        adminClientResponse.setAnh(khachHang.getAnh());
        adminClientResponse.setTongDiem(khachHang.getTongDiem());
        adminClientResponse.setSoDiemHienTai(khachHang.getSoDiemHienTai());
        adminClientResponse.setHangKhachHang(khachHang.getHangKhachHang().name());
        adminClientResponse.setTrangThai(khachHang.getTrangThai().name());

        // Lọc địa chỉ chính
        DiaChi diaChiChinh = khachHang.getDiaChis().stream()
                .filter(dc -> dc.getDiaChiChinh() == true) // Giả sử 1 là địa chỉ chính
                .findFirst() // Lấy địa chỉ chính đầu tiên nếu có
                .orElse(null); // Nếu không có địa chỉ chính, trả về null

        // Chỉ lấy địa chỉ chính nếu có
        if (diaChiChinh != null) {
            AdminDiaChiResponse adminDiaChiResponse = new AdminDiaChiResponse();
            adminDiaChiResponse.setId(diaChiChinh.getId());
            adminDiaChiResponse.setTenKhachHang(diaChiChinh.getIdKhachHang().getTenKhachHang());
            adminDiaChiResponse.setTenNguoiNhan(diaChiChinh.getTenNguoiNhan());
            adminDiaChiResponse.setSoNha(diaChiChinh.getSoNha());
            adminDiaChiResponse.setTenDuong(diaChiChinh.getTenDuong());
            adminDiaChiResponse.setXaPhuong(diaChiChinh.getXaPhuong());
            adminDiaChiResponse.setQuanHuyen(diaChiChinh.getQuanHuyen());
            adminDiaChiResponse.setTinhThanhPho(diaChiChinh.getTinhThanhPho());
            adminDiaChiResponse.setDiaChiChinh(diaChiChinh.getDiaChiChinh());

            adminClientResponse.setDiaChiChinh(adminDiaChiResponse);
        }
        return adminClientResponse;
    }

    public Page<AdminClientResponse> getAllClient(int page, int size) {
        Pageable pageable  = PageRequest.of(page, size);
        Page<KhachHang> clientPage = khachHangRepository.findAll(pageable);
        return clientPage.map(this :: convert);

    }

    public Optional<AdminClientResponse> getClientById(Integer id) {
        return khachHangRepository.findById(id).map(this :: convert);
    }
}
