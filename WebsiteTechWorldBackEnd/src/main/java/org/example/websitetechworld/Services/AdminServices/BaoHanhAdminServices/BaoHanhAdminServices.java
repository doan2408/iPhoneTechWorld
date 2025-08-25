package org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.BaoHanhRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.YeuCauBaoHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhHistoryAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.DonBaoHanhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.YeuCauBaoHanhAdminResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiLichSuBaoHanh;
import org.example.websitetechworld.Mapper.Admin.BaoHanh.BaoHanhAdminMappper;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.CommonSerivces.EmailCommonService.EmailServicces;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final EmailServicces emailServicces;

    public BaoHanhAdminServices(BaoHanhRepository baoHanhRepository, LoaiBaoHanhRepository loaiBaoHanhRepository, KhachHangRepository khachHangRepository, ImeiDaBanRepository imeiDaBanRepository, BaoHanhAdminMappper baoHanhAdminMappper, LichSuHoaDonRepository lichSuHoaDonRepository, LichSuBaoHanhRepository lichSuBaoHanhRepository, EmailServicces emailServicces) {
        this.baoHanhRepository = baoHanhRepository;
        this.loaiBaoHanhRepository = loaiBaoHanhRepository;
        this.khachHangRepository = khachHangRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.baoHanhAdminMappper = baoHanhAdminMappper;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        this.lichSuBaoHanhRepository = lichSuBaoHanhRepository;
        this.emailServicces = emailServicces;
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
        if (baoHanhRepository.existsByBaoHanh(request.getIdImeiDaBan(), request.getIdLoaiBaoHanh())) {
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
        cal.add(Calendar.MONTH, thoiGianThang);

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
//                        !Objects.equals(request.getIdKhachHang(), baoHanh.getIdKhachHang().getId()) ||
                                !Objects.equals(request.getIdImeiDaBan(), baoHanh.getIdImeiDaBan().getId()) ||
                                !Objects.equals(request.getIdLoaiBaoHanh(), baoHanh.getIdLoaiBaoHanh().getId())
                )
                        && baoHanhRepository.existsByBaoHanh(request.getIdImeiDaBan(), request.getIdLoaiBaoHanh())
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
        YeuCauBaoHanhAdminResponse response = null;
        if (imeiDaBan != null){
            response = baoHanhAdminMappper.toYeuCauBaoHanhAdminResponse(imeiDaBan) ;
        }
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

        sendMailTaoBaoHanh(baoHanh, lichSuBaoHanh);

    }

    public void sendMailTaoBaoHanh(BaoHanh baoHanh, LichSuBaoHanh lichSuBaoHanh) {
        String subject = "Xác nhận yêu cầu bảo hành cho sản phẩm #"
                + baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getTenSanPham();

        String html =
                "<div style='font-family:Arial, sans-serif; max-width:600px; margin:0 auto; padding:20px; " +
                        "border:1px solid #e0e0e0; border-radius:10px; background-color:#f9f9f9;'>" +

                        "<h2 style='color:#2c3e50; text-align:center;'>XÁC NHẬN YÊU CẦU BẢO HÀNH</h2>" +

                        "<p style='font-size:14px; color:#333;'>Xin chào <b style='color:#2980b9;'>"
                        + baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getIdHoaDon().getTenNguoiNhan() +
                        "</b>,</p>" +

                        "<p style='font-size:14px; color:#333;'>Yêu cầu bảo hành cho sản phẩm <b style='color:#27ae60;'>"
                        + baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getTenSanPham() +
                        "</b> đã được tiếp nhận vào ngày <b>"
                        + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</b>.</p>" +

                        "<p style='font-size:14px; color:#333;'><b>Lý do bảo hành:</b> "
                        + lichSuBaoHanh.getMoTaLoi() + "</p>" +

                        "<p style='font-size:14px;'><b>Trạng thái hiện tại:</b> " +
                        "<span style='color:#e67e22; font-weight:bold;'>"
                        + lichSuBaoHanh.getTrangThai().getDisplayName() + "</span></p>" +

                        "<div style='margin:20px 0; padding:15px; background-color:#ecf0f1; border-left:5px solid #3498db;'>" +
                        "<p style='margin:0; font-size:14px; color:#555;'>Chúng tôi sẽ sớm liên hệ lại để thông báo tiến trình xử lý.</p>" +
                        "</div>" +

                        "<p style='font-size:14px; color:#333;'>Trân trọng,</p>" +
                        "<p style='font-size:14px; font-weight:bold; color:#2c3e50;'>Đội ngũ bảo hành</p>" +

                        "</div>";

        // gửi mail
        emailServicces.sendHtmlEmail(
                baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getIdHoaDon().getEmailNguoiNhan(),
                subject,
                html
        );
    }


    public Page<DonBaoHanhAdminResponse> findDonBaoHanh(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<LichSuBaoHanh> resLst = lichSuBaoHanhRepository.findDonBaoHanh(pageable);
        return resLst.map(BaoHanhAdminMappper::toDonBaoHanhAdminResponse);
    }

    public void hoanThanhXuLy(Integer idCtbh){
        LichSuBaoHanh lichSuBaoHanh =  lichSuBaoHanhRepository.findById(idCtbh).orElseThrow(
                () -> new IllegalArgumentException("LichSuBaoHanh Not Found")
        );
        lichSuBaoHanh.setNgayHoanThanh(Date.valueOf(LocalDate.now()));
        lichSuBaoHanh.setTrangThai(TrangThaiLichSuBaoHanh.REPAIRED);

        lichSuBaoHanhRepository.save(lichSuBaoHanh);

        sendMailHoanThanhBaoHanh(lichSuBaoHanh.getIdBaoHanh(),lichSuBaoHanh);
    }


    public List<BaoHanhHistoryAdminResponse> findHistory(String soImei){
        return lichSuBaoHanhRepository.findHistory(soImei);
    }

    public void sendMailHoanThanhBaoHanh(BaoHanh baoHanh, LichSuBaoHanh lichSuBaoHanh) {
        String subject = "Hoàn tất bảo hành cho sản phẩm #"
                + baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getTenSanPham();

        String html =
                "<div style='font-family:Arial, sans-serif; max-width:600px; margin:0 auto; padding:20px; " +
                        "border:1px solid #e0e0e0; border-radius:10px; background-color:#f9f9f9;'>" +

                        "<h2 style='color:#27ae60; text-align:center;'>✔ BẢO HÀNH ĐÃ HOÀN TẤT</h2>" +

                        "<p style='font-size:14px; color:#333;'>Xin chào <b style='color:#2980b9;'>"
                        + baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getIdHoaDon().getTenNguoiNhan() +
                        "</b>,</p>" +

                        "<p style='font-size:14px; color:#333;'>Sản phẩm <b style='color:#27ae60;'>"
                        + baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getTenSanPham() +
                        "</b> đã được bảo hành và hoàn tất xử lý vào ngày <b>"
                        + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</b>.</p>" +

                        "<p style='font-size:14px; color:#333;'><b>Nội dung xử lý:</b> "
                        + lichSuBaoHanh.getMoTaLoi() + "</p>" +

                        "<p style='font-size:14px;'><b>Trạng thái hiện tại:</b> " +
                        "<span style='color:#27ae60; font-weight:bold;'>"
                        + lichSuBaoHanh.getTrangThai().getDisplayName() + "</span></p>" +

                        "<div style='margin:20px 0; padding:15px; background-color:#eafaf1; border-left:5px solid #2ecc71;'>" +
                        "<p style='margin:0; font-size:14px; color:#555;'>Khi đến lấy vui lòng mang phiếu bảo hành hoặc giấy tờ liên quan. " +
                        "Cảm ơn bạn đã tin tưởng dịch vụ của chúng tôi!</p>" +
                        "</div>" +

                        "<p style='font-size:14px; color:#333;'>Trân trọng,</p>" +
                        "<p style='font-size:14px; font-weight:bold; color:#2c3e50;'>Đội ngũ bảo hành</p>" +

                        "</div>";

        // gửi mail
        emailServicces.sendHtmlEmail(
                baoHanh.getIdImeiDaBan().getIdHoaDonChiTiet().getIdHoaDon().getEmailNguoiNhan(),
                subject,
                html
        );
    }
}
