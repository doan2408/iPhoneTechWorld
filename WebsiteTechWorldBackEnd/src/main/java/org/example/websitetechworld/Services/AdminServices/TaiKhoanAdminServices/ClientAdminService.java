package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Entity.GioHang;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.DiaChiRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientAdminService {
    private final KhachHangRepository khachHangRepository;
    private final DiaChiRepository diaChiRepository;

    ModelMapper modelMapper = new ModelMapper();


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

        // Tạo danh sách địa chỉ
        List<AdminDiaChiResponse> diaChiResponses = khachHang.getDiaChis().stream()
                .map(dc -> {
                    AdminDiaChiResponse diaChi = new AdminDiaChiResponse();
                    diaChi.setId(dc.getId());
                    diaChi.setTenKhachHang(dc.getIdKhachHang().getTenKhachHang());
                    diaChi.setTenNguoiNhan(dc.getTenNguoiNhan());
                    diaChi.setSdtNguoiNhan(dc.getSdtNguoiNhan());
                    diaChi.setSoNha(dc.getSoNha());
                    diaChi.setTenDuong(dc.getTenDuong());
                    diaChi.setXaPhuong(dc.getXaPhuong());
                    diaChi.setQuanHuyen(dc.getQuanHuyen());
                    diaChi.setTinhThanhPho(dc.getTinhThanhPho());
                    diaChi.setDiaChiChinh(dc.getDiaChiChinh());
                    return diaChi;
                })
                .collect(Collectors.toList());

        // Thêm danh sách địa chỉ vào response
        adminClientResponse.setDiaChi(diaChiResponses);

        return adminClientResponse;
    }


    //hien thi
    public Page<AdminClientResponse> getAllClient(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> clientPage = khachHangRepository.findAll(pageable);
        return clientPage.map(this::convert);
    }

    //detail client
    public Optional<AdminClientResponse> getClientById(Integer id) {
        return khachHangRepository.findById(id).map(this::convert);
    }

    //add client
    public KhachHang addClient(KhachHang khachHang) {
        khachHang.setAnh("Default.jpg");
        khachHang.setTrangThai(TrangThaiKhachHang.ACTIVE);
        khachHang.setTongDiem(new BigDecimal(0));
        khachHang.setSoDiemHienTai(new BigDecimal(0));
        khachHang.setHangKhachHang(HangKhachHang.MEMBER);

        KhachHang khachHangAdd = khachHangRepository.save(khachHang);

        //tạo giỏ hàng tương ứng với khách hàng mới vừa tạo
        GioHang gioHang = new GioHang();
        gioHang.setIdKhachHang(khachHangAdd); //oneToOne

        //set ngược lại
        khachHangAdd.setGioHang(gioHang);

        //lưu id khách hàng và giỏ hàng cũng sẽ được lưu theo Cascade
        return khachHangRepository.save(khachHangAdd);
    }

    //update khách hàng
    public KhachHang updateClient(Integer id, KhachHang khachHangRequest) {
        KhachHang existing = khachHangRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTenKhachHang(khachHangRequest.getTenKhachHang());
            existing.setSdt(khachHangRequest.getSdt());
            existing.setMatKhau(khachHangRequest.getMatKhau());
            existing.setEmail(khachHangRequest.getEmail());
            existing.setNgaySinh(khachHangRequest.getNgaySinh());
            existing.setGioiTinh(khachHangRequest.getGioiTinh());
            existing.setAnh(khachHangRequest.getAnh());

            return khachHangRepository.save(existing);
        }
        return null;
    }



}
