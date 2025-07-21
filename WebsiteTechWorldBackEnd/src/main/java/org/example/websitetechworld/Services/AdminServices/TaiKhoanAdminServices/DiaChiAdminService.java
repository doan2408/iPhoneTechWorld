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
        if(diaChi.getIdKhachHang() != null) {
            adminDiaChiRequest.setIdKhachHang(diaChi.getIdKhachHang().getId());
        }
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
        if(diaChi.getIdKhachHang() != null) {
            adminDiaChiRequest.setIdKhachHang(diaChi.getIdKhachHang().getId());
        }
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
        if (adminDiaChiRequest.getIdKhachHang() != null) {
            // Ép kiểu idKhachHang -> entity
            KhachHang khachHang = khachHangRepository.findById(adminDiaChiRequest.getIdKhachHang())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID = " + adminDiaChiRequest.getIdKhachHang()));
            diaChi.setIdKhachHang(khachHang);
        }
    }

    //get all dia chi theo id khach hang
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


    @Transactional
    public AdminDiaChiRequest addDiaChi(AdminDiaChiRequest adminDiaChiRequest) {
        DiaChi diaChi = new DiaChi();

        if (adminDiaChiRequest.getIdKhachHang() == null) {
            throw new IllegalArgumentException("Client does not exist");
        }

        // Kiểm tra xem khách hàng đã có địa chỉ nào chưa
        boolean hasAnyDiaChi = diaChiRepository.existsByIdKhachHang_Id(adminDiaChiRequest.getIdKhachHang());

        // Nếu chưa có địa chỉ nào => đặt là địa chỉ chính
        if (!hasAnyDiaChi) {
            adminDiaChiRequest.setDiaChiChinh(true);
        }

        if (Boolean.TRUE.equals(adminDiaChiRequest.getDiaChiChinh())) {
            if(adminDiaChiRequest.getIdKhachHang() == null) {
                throw new IllegalArgumentException("Client does not exist");
            }
            diaChiRepository.updateAllDiaChiPhu(adminDiaChiRequest.getIdKhachHang(), null);
        }
        convertToDiaChiEntity(diaChi, adminDiaChiRequest);
        DiaChi diaChiAdd = diaChiRepository.save(diaChi);
        return convertToRequest(diaChiAdd);
    }


    public List<DiaChi> getAddressesByClientId(Integer clientId) {
        return diaChiRepository.findByIdKhachHang(clientId);
    }
}
