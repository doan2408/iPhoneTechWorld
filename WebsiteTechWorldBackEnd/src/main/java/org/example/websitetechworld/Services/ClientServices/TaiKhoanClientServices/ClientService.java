package org.example.websitetechworld.Services.ClientServices.TaiKhoanClientServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.TaiKhoanClientReponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Entity.GioHang;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final KhachHangRepository khachHangRepository;

    //convert
    private TaiKhoanClientReponse convert(KhachHang khachHang) {
        TaiKhoanClientReponse clientResponse = new TaiKhoanClientReponse();
        clientResponse.setId(khachHang.getId());
        clientResponse.setMaKhachHang(khachHang.getMaKhachHang());
        clientResponse.setTenKhachHang(khachHang.getTenKhachHang());
        clientResponse.setSdt(khachHang.getSdt());
        clientResponse.setTaiKhoan(khachHang.getTaiKhoan());
        clientResponse.setMatKhau(khachHang.getMatKhau());
        clientResponse.setEmail(khachHang.getEmail());
        clientResponse.setNgaySinh(khachHang.getNgaySinh());
        clientResponse.setGioiTinh(khachHang.getGioiTinh());
        clientResponse.setAnh(khachHang.getAnh());
        clientResponse.setTongDiem(khachHang.getTongDiem());
        clientResponse.setSoDiemHienTai(khachHang.getSoDiemHienTai());
        clientResponse.setHangKhachHang(khachHang.getHangKhachHang().name());
        clientResponse.setTrangThai(khachHang.getTrangThai().name());

        // Lọc địa chỉ chính
        DiaChi diaChiChinh = khachHang.getDiaChis().stream()
                .filter(dc -> dc.getDiaChiChinh() == true) // Giả sử 1 là địa chỉ chính
                .findFirst() // Lấy địa chỉ chính đầu tiên nếu có
                .orElse(null); // Nếu không có địa chỉ chính, trả về null

        // Chỉ lấy địa chỉ chính nếu có
        if (diaChiChinh != null) {
            AdminDiaChiResponse diaChiResponse = new AdminDiaChiResponse();
            diaChiResponse.setId(diaChiChinh.getId());
            diaChiResponse.setTenKhachHang(diaChiChinh.getIdKhachHang().getTenKhachHang());
            diaChiResponse.setTenNguoiNhan(diaChiChinh.getTenNguoiNhan());
            diaChiResponse.setSoNha(diaChiChinh.getSoNha());
            diaChiResponse.setTenDuong(diaChiChinh.getTenDuong());
            diaChiResponse.setXaPhuong(diaChiChinh.getXaPhuong());
            diaChiResponse.setQuanHuyen(diaChiChinh.getQuanHuyen());
            diaChiResponse.setTinhThanhPho(diaChiChinh.getTinhThanhPho());
            diaChiResponse.setDiaChiChinh(diaChiChinh.getDiaChiChinh());

            clientResponse.setDiaChiChinh(diaChiResponse);
        }
        return clientResponse;
    }

    //SignUp
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

    public Optional<TaiKhoanClientReponse> hienThi(Integer id) {
        return khachHangRepository.findById(id).map(this::convert);
    }

    //update thong tin ca nhan (client)
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
