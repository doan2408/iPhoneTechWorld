package org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.BaoHanhRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.YeuCauBaoHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.YeuCauBaoHanhAdminResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiLichSuBaoHanh;
import org.example.websitetechworld.Mapper.Admin.BaoHanh.BaoHanhAdminMappper;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class BaoHanhAdminServices {
    private final BaoHanhRepository baoHanhRepository;
    private final LoaiBaoHanhRepository loaiBaoHanhRepository;
    private final KhachHangRepository khachHangRepository;
    private final ImeiDaBanRepository imeiDaBanRepository;
    private final BaoHanhAdminMappper baoHanhAdminMappper;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final LichSuBaoHanhRepository lichSuBaoHanhRepository;

    public BaoHanhAdminServices(BaoHanhRepository baoHanhRepository, LoaiBaoHanhRepository loaiBaoHanhRepository, KhachHangRepository khachHangRepository, ImeiDaBanRepository imeiDaBanRepository, BaoHanhAdminMappper baoHanhAdminMappper, LichSuHoaDonRepository lichSuHoaDonRepository, LichSuBaoHanhRepository lichSuBaoHanhRepository) {
        this.baoHanhRepository = baoHanhRepository;
        this.loaiBaoHanhRepository = loaiBaoHanhRepository;
        this.khachHangRepository = khachHangRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.baoHanhAdminMappper = baoHanhAdminMappper;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        this.lichSuBaoHanhRepository = lichSuBaoHanhRepository;
    }

    public Page<BaoHanhAdminResponse> getAllBaoHanh(String search, TrangThaiBaoHanh trangThai,
                                                    LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                    int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BaoHanh> baoHanhs = baoHanhRepository.findAll(search.trim(), trangThai, ngayBatDau, ngayKetThuc, pageable);
        return baoHanhs.map(BaoHanhAdminResponse::convertDto);
    }

    public BaoHanhAdminResponse getBaoHanh(int id) {
        return BaoHanhAdminResponse.convertDto(baoHanhRepository.findById(id).get());
    }

    public BaoHanhAdminResponse addWarranty(BaoHanhRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        if (baoHanhRepository.existsByBaoHanh(request.getIdKhachHang(), request.getIdImeiDaBan(), request.getIdLoaiBaoHanh())) {
            errors.add(Map.of("field", "baoHanh", "message", "Sản phẩm của khách hàng đã được áp dụng bảo hành này"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        BaoHanh baoHanh = BaoHanhRequest.convertDto(request);

        LoaiBaoHanh loaiBaoHanh = loaiBaoHanhRepository.findById(request.getIdLoaiBaoHanh()).orElse(null);
        Integer thoiGianThang = loaiBaoHanh.getThoiGianThang();

        Date ngayBatDau = request.getNgayBatDau();

        Calendar cal = Calendar.getInstance();
        cal.setTime(ngayBatDau);
        cal.add(Calendar.MONTH, thoiGianThang); // startDate + thoi gian thang

        Date ngayKetThuc = new Date(cal.getTimeInMillis());
        baoHanh.setNgayKetThuc(ngayKetThuc);

        BaoHanh baoHanhAdd = baoHanhRepository.save(baoHanh);
        return BaoHanhAdminResponse.convertDto(baoHanhAdd);
    }

    public BaoHanhAdminResponse updateWarranty(Integer id, BaoHanhRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();

        BaoHanh baoHanh = baoHanhRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BaoHanh Not Found"));

        if (
                (
                        !Objects.equals(request.getIdKhachHang(), baoHanh.getIdKhachHang().getId()) ||
                                !Objects.equals(request.getIdImeiDaBan(), baoHanh.getIdImeiDaBan().getId()) ||
                                !Objects.equals(request.getIdLoaiBaoHanh(), baoHanh.getIdLoaiBaoHanh().getId())
                )
                        && baoHanhRepository.existsByBaoHanh(request.getIdKhachHang(), request.getIdImeiDaBan(), request.getIdLoaiBaoHanh())
        ) {
            errors.add(Map.of("field", "baoHanh", "message", "Sản phẩm của khách hàng đã được áp dụng bảo hành này"));

        }
        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        KhachHang khachHang = khachHangRepository.findById(request.getIdKhachHang()).orElse(null);
        baoHanh.setIdKhachHang(khachHang);

        LoaiBaoHanh loaiBaoHanh = loaiBaoHanhRepository.findById(request.getIdLoaiBaoHanh()).orElse(null);
        baoHanh.setIdLoaiBaoHanh(loaiBaoHanh);

        ImeiDaBan imeiDaBan = imeiDaBanRepository.findById(request.getIdImeiDaBan()).orElse(null);
        baoHanh.setIdImeiDaBan(imeiDaBan);

        Integer thoiGianThang = loaiBaoHanh.getThoiGianThang();

        Date ngayBatDau = request.getNgayBatDau();

        Calendar cal = Calendar.getInstance();
        cal.setTime(ngayBatDau);
        cal.add(Calendar.MONTH, thoiGianThang);

        Date ngayKetThuc = new Date(cal.getTimeInMillis());

        baoHanh.setNgayBatDau(ngayBatDau);
        baoHanh.setNgayKetThuc(ngayKetThuc);
        baoHanh.setTrangThaiBaoHanh(request.getTrangThaiBaoHanh());
        BaoHanh updated = baoHanhRepository.save(baoHanh);
        return BaoHanhAdminResponse.convertDto(updated);
    }

    public YeuCauBaoHanhAdminResponse  checkWarranty(String soImei){
        ImeiDaBan imeiDaBan = imeiDaBanRepository.findBySoImeiWithValidWarranty(soImei);
        YeuCauBaoHanhAdminResponse response = baoHanhAdminMappper.toYeuCauBaoHanhAdminResponse(imeiDaBan) ;
        return response;
    }

    public void createRequestWarranty(YeuCauBaoHanhAdminRequest yeuCauBaoHanhAdminRequest){

        BaoHanh baoHanh = baoHanhRepository.findById(yeuCauBaoHanhAdminRequest.getIdBaoHanh()).orElseThrow(
                ()-> new IllegalArgumentException("BaoHanh Not Found")
        );

        LichSuBaoHanh lichSuBaoHanh = new LichSuBaoHanh();
        lichSuBaoHanh.setIdBaoHanh(baoHanh);
        lichSuBaoHanh.setNgayTiepNhan(Date.valueOf(LocalDate.now()));
        lichSuBaoHanh.setMoTaLoi(yeuCauBaoHanhAdminRequest.getLyDoBaoHanh());
        lichSuBaoHanh.setTrangThai(TrangThaiLichSuBaoHanh.IN_REPAIR);

        lichSuBaoHanhRepository.save(lichSuBaoHanh);

    }
}
