package org.example.websitetechworld.Services.ClientServices.ThanhToanClientService;

import jakarta.servlet.http.HttpServletRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Entity.ChiTietThanhToan;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Repository.ChiTietThanhToanRepository;
import org.example.websitetechworld.Repository.PhuongThucThanhToanRepository;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.VnpayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class VnpayStrategy implements ThanhToanStrategy {
    private final VnpayService vnpayService;
    private final HttpServletRequest request;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private static final Logger logger = LoggerFactory.getLogger(VnpayStrategy.class);

    public VnpayStrategy(VnpayService vnpayService, HttpServletRequest request, PhuongThucThanhToanRepository phuongThucThanhToanRepository, ChiTietThanhToanRepository chiTietThanhToanRepository) {
        this.vnpayService = vnpayService;
        this.request = request;
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
    }

    @Override
    public ThanhToanAdminResponse thanhToan(HoaDon hoaDon, ThanhToanAdminRequest thanhToanAdminRequest) {
        ThanhToanAdminResponse response = new ThanhToanAdminResponse();
        try {
            String ipAddress = getClientIp(request);

            BigDecimal soTien = thanhToanAdminRequest.getSoTienKhachDua();

            if (soTien == null
                    || soTien.compareTo(BigDecimal.valueOf(5_000)) < 0
                    || soTien.compareTo(BigDecimal.valueOf(1_000_000_000)) > 0) {
                throw new IllegalArgumentException("Số tiền không hợp lệ cho VNPay");
            }

            long amount = soTien.longValueExact();

            System.out.println("[DEBUG BACKEND] Số tiền nhận từ FE: " + soTien);
            System.out.println("[DEBUG BACKEND] Số tiền gửi VNPay: " + amount);

            String paymentUrl = vnpayService.createPaymentUrl(
                    amount,
                    "Thanh toan don hang " + hoaDon.getMaVanDon(),
                    ipAddress
            );

            response.setMessage("REDIRECT_VNPAY");
            response.setPaymentUrl(paymentUrl);
            response.setTienThua(BigDecimal.ZERO);

            PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository
                    .findOneByTenPhuongThuc(thanhToanAdminRequest.getHinhThucThanhToan());
            if (phuongThucThanhToan == null) {
                throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " +
                        thanhToanAdminRequest.getHinhThucThanhToan());
            }

            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PAID);
            hoaDon.setNgayThanhToan(LocalDateTime.now());

            ChiTietThanhToan cttt = new ChiTietThanhToan();
            cttt.setIdHoaDon(hoaDon);
            cttt.setSoTienThanhToan(hoaDon.getThanhTien());
            cttt.setIdPhuongThucThanhToan(phuongThucThanhToan);
            chiTietThanhToanRepository.save(cttt);

        } catch (Exception e) {
            response.setMessage("Lỗi khi tạo link VNPay: " + e.getMessage());
            e.printStackTrace();
            logger.error("Đã xảy ra lỗi: {}", e.getMessage(), e);
        }
        return response;
    }


    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
