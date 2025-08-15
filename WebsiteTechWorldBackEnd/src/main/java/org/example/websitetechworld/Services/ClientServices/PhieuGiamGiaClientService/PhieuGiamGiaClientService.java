package org.example.websitetechworld.Services.ClientServices.PhieuGiamGiaClientService;

import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHangGiamGia;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPhatHanh;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Repository.KhachHangGiamGiaRepository;
import org.example.websitetechworld.Repository.PhieuGiamGiaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaClientService {

    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final HoaDonRepository hoaDonRepository;
    private final ModelMapper modelMapper;

    public PhieuGiamGiaClientService(KhachHangGiamGiaRepository khachHangGiamGiaRepository, PhieuGiamGiaRepository phieuGiamGiaRepository, HoaDonRepository hoaDonRepository, ModelMapper modelMapper) {
        this.khachHangGiamGiaRepository = khachHangGiamGiaRepository;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.modelMapper = modelMapper;
    }

    public List<PhieuGiamGiaAdminResponse> layDanhSachPhieuGiamGiaCuaKhach(String timKiem, Integer idKhachHang, BigDecimal giaTriDonHangToiThieu) {
        if (idKhachHang == null) return null;

        List<PhieuGiamGia> phieuList = khachHangGiamGiaRepository.findPhieuGiamGiaCuaKhach(
                idKhachHang,
                giaTriDonHangToiThieu,
                timKiem
        );

        return phieuList.stream()
                .map(phieu -> modelMapper.map(phieu, PhieuGiamGiaAdminResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void xuLyPhieuGiamGia(Integer idHoaDon, PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại: " + idHoaDon));

        Integer idPhieuGiamGia = phieuGiamGiaAdminRequest.getId();
        if (idPhieuGiamGia == null) {
            throw new IllegalArgumentException("Id phiếu giảm giá không được null");
        }

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(idPhieuGiamGia)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với ID: " + idPhieuGiamGia));

        hoaDon.setIdPhieuGiamGia(phieuGiamGia);
        hoaDonRepository.save(hoaDon);
    }
}
