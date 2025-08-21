package org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.BaoHanhRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Entity.BaoHanh;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.LoaiBaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Repository.BaoHanhRepository;
import org.example.websitetechworld.Repository.ImeiDaBanRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.LoaiBaoHanhRepository;
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

    public BaoHanhAdminServices(BaoHanhRepository baoHanhRepository, LoaiBaoHanhRepository loaiBaoHanhRepository, KhachHangRepository khachHangRepository, ImeiDaBanRepository imeiDaBanRepository) {
        this.baoHanhRepository = baoHanhRepository;
        this.loaiBaoHanhRepository = loaiBaoHanhRepository;
        this.khachHangRepository = khachHangRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
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
}
