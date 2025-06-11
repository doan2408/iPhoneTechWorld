package org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminDiaChiRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Repository.DiaChiRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaChiAdminService {
    private final DiaChiRepository diaChiRepository;
    private final KhachHangRepository khachHangRepository;

    public AdminDiaChiResponse convertToResponse(DiaChi diaChi) {
        AdminDiaChiResponse adminDiaChiRequest = new AdminDiaChiResponse();
        adminDiaChiRequest.setId(diaChi.getId());
        adminDiaChiRequest.setTenKhachHang(diaChi.getIdKhachHang().getTenKhachHang());
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

    @Transactional
    public AdminDiaChiRequest updateDiaChi(int idDiaChi, AdminDiaChiRequest adminDiaChiRequest) {
        DiaChi diaChiExisting = diaChiRepository.findById(idDiaChi)
                .orElseThrow(() -> new IllegalArgumentException("Địa chỉ không tồn tại"));

        // Kiểm tra nếu địa chỉ này hiện đang là địa chỉ chính
        boolean isCurrentlyMain = Boolean.TRUE.equals(diaChiExisting.getDiaChiChinh());
        // Giá trị mới muốn cập nhật
        Boolean newDiaChiChinh = adminDiaChiRequest.getDiaChiChinh();

        if (isCurrentlyMain && Boolean.FALSE.equals(newDiaChiChinh)) {
            // Nếu đang là chính mà sửa thành phụ => kiểm tra xem còn địa chỉ chính khác không
            List<DiaChi> mainAddresses = diaChiRepository.findByIdKhachHangAndDiaChiChinh(
                    diaChiExisting.getIdKhachHang().getId(), true);

            if (mainAddresses.size() == 1) {
                // Chỉ có 1 địa chỉ chính duy nhất là địa chỉ này => không được phép đổi
                throw new IllegalArgumentException("Phải luôn có 1 địa chỉ chính, không thể chuyển địa chỉ chính thành phụ.");
            }
        }

        if (Boolean.TRUE.equals(newDiaChiChinh)) {
            // Nếu cập nhật thành địa chỉ chính thì reset các địa chỉ khác thành phụ
            if (diaChiExisting.getIdKhachHang() == null) {
                throw new IllegalArgumentException("Khách hàng không tồn tại cho địa chỉ này.");
            }
            diaChiRepository.updateAllDiaChiPhu(diaChiExisting.getIdKhachHang().getId(), idDiaChi);
        }

        convertToDiaChiEntity(diaChiExisting, adminDiaChiRequest);
        DiaChi diaChiUpdate = diaChiRepository.save(diaChiExisting);
        return convertToRequest(diaChiUpdate);
    }


    public List<DiaChi> getAddressesByClientId(Integer clientId) {
        return diaChiRepository.findByIdKhachHang(clientId);
    }
}
