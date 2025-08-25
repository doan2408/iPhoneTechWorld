package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.LichSuHoaDon;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.TabHoaDonAdminResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.LichSuHoaDon;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.HanhDongLichSuHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Repository.LichSuHoaDonRepository;
import org.example.websitetechworld.Repository.NhanVienRepository;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LichSuHoaDonAdminServices {
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final NhanVienRepository nhanVienRepository;
    private final HoaDonRepository hoaDonRepository;
    private static final Logger logger = LoggerFactory.getLogger(HoaDonAdminService.class);

    public LichSuHoaDonAdminServices(LichSuHoaDonRepository lichSuHoaDonRepository, NhanVienRepository nhanVienRepository, HoaDonRepository hoaDonRepository) {
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.hoaDonRepository = hoaDonRepository;
    }

    public LichSuHoaDon createLSHDWithPendingInvoice(Integer hoaDonId, Integer nhanVienId) {
        List<Map<String, String>> errors = new ArrayList<>();
        Integer number = countPendingHoaDonByNhanVien(nhanVienId, TrangThaiThanhToan.PENDING);
        System.out.println();
        if (number > 4) {
            errors.add(Map.of("message", "Mỗi nhân viên chỉ có thể tạo tối đa 5 hóa đơn chờ"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        logger.info("Bắt đầu tạo lịch sử hóa đơn: nhân viên ID = {}, hóa đơn ID = {}", nhanVienId, hoaDonId);

        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId).orElseThrow(
                () -> new IllegalArgumentException("Nhân viên không tồn tại với ID: " + nhanVienId));


        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElseThrow(
                () -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + hoaDonId));
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setIdNhanVien(nhanVien);
        lichSuHoaDon.setIdHoaDon(hoaDon);
        lichSuHoaDon.setHanhDong(HanhDongLichSuHoaDon.CREATE);
        lichSuHoaDon.setThoiGianThayDoi(LocalDate.now());
        lichSuHoaDon.setMoTa("Tạo hóa đơn rỗng");

        LichSuHoaDon saved = lichSuHoaDonRepository.save(lichSuHoaDon);
        logger.info("Tạo lịch sử hóa đơn thành công với ID: {}", saved.getId());
        return saved;
    }


    public List<TabHoaDonAdminResponse> findMaHoaDonPendingByNhanVien(Integer nhanVienId) {
        return lichSuHoaDonRepository.findMaHoaDonPendingByNhanVien(nhanVienId);
    }

    //count pending hoaDon created by one staff member
    public Integer countPendingHoaDonByNhanVien(Integer nhanVienId, TrangThaiThanhToan trangThaiThanhToan) {
        return lichSuHoaDonRepository.countPendingHoaDonByNhanVien(nhanVienId, trangThaiThanhToan);
    }

}
