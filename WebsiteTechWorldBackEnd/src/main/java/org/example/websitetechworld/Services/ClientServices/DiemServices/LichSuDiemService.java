package org.example.websitetechworld.Services.ClientServices.DiemServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.example.websitetechworld.Dto.Response.ClientResponse.LichSuDiemResponse.LichSuDiemResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhachHang.LoaiDiem;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LichSuDiemService {

    private final LichSuDiemRepository lichSuDiemRepository;
    private final ViDiemRepository viDiemRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final ChiTietLichSuDiemRepository chiTietLichSuDiemRepository;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;

    private LichSuDiemResponse convertToResponse(LichSuDiem entity) {
        return new LichSuDiemResponse(
                entity.getId(),
                entity.getLoaiDiem().name(),
                entity.getSoDiem(),
                entity.getGhiChu(),
                entity.getThoiGian(),
                entity.getHanSuDung()
        );
    }

    public Page<LichSuDiemResponse> getLichSuDiemByKhachHang(OffsetDateTime fromDate, OffsetDateTime toDate, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idKhachHang = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            idKhachHang = userDetails.getId();
        }
        Page<LichSuDiem> pageResult = lichSuDiemRepository.getLichSuDiem(idKhachHang, fromDate, toDate, pageable);
        return pageResult.map(this::convertToResponse);
    }


    @Transactional
    public String doiDiemNhanVoucher(Integer idPhieuGiamGia) {
        List<Map<String, String>> errors = new ArrayList<>();
        // Lấy id khách hàng từ security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idKhachHang = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            idKhachHang = userDetails.getId();
        } else {
            errors.add(Map.of("field", "taiKhoan", "message", "Không xác định được khách hàng"));
        }

        // Lấy ví điểm
        ViDiem viDiem = viDiemRepository.findByIdKhachHang(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví điểm"));

        // Lấy phiếu giảm giá
        PhieuGiamGia phieu = phieuGiamGiaRepository.findById(idPhieuGiamGia)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu giảm giá"));

        BigDecimal diemCanDoi = phieu.getSoDiemCanDeDoi();

        // Kiểm tra điểm khả dụng trong ví
        if (viDiem.getDiemKhaDung().compareTo(diemCanDoi) < 0) {
            errors.add(Map.of("field", "point", "message", "Không đủ điểm để đổi Voucher"));
        }

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        System.out.println("Thời gian offset: " + now);
        // Trừ điểm theo FIFO : ngày cũ nhất tới mới nhất
        // lấy ds điểm cộng của client còn hạn và chưa sd hết
        List<LichSuDiem> lichSuCong = lichSuDiemRepository.findLichSuDiemChuaTru(idKhachHang, LoaiDiem.CONG, now);
        BigDecimal diemConLai = diemCanDoi;

        for (LichSuDiem lichSu : lichSuCong) {
            BigDecimal daTru = chiTietLichSuDiemRepository.tongDiemDaTruTheoLichSu(lichSu.getId());
            BigDecimal diemCon = lichSu.getSoDiem().subtract(daTru); // số điểm khả dụng từ 1 bản ghi lịch sử


            if (diemCon.compareTo(BigDecimal.ZERO) <= 0) continue;

            // điểm khả dụng của 1 bản ghi lịch sử < điểm cần đổi => trừ điểm khả dụng của 1 bản ghi lịch sử về 0
            BigDecimal diemTru = diemCon.min(diemConLai);

            // trừ xong thì tạo 1 bản ghi chi tiết trừ
            ChiTietLichSuDiem chiTiet = new ChiTietLichSuDiem();
            chiTiet.setKhachHang(viDiem.getKhachHang());
            chiTiet.setLichSuDiem(lichSu);
            chiTiet.setSoDiemDaTru(diemTru);
            chiTiet.setNgayTru(Instant.now());
            chiTietLichSuDiemRepository.save(chiTiet);

            // cập nhật lại xem số điểm còn trừ là bao nhiêu nữa để đổi voucher
            diemConLai = diemConLai.subtract(diemTru);
            if (diemConLai.compareTo(BigDecimal.ZERO) <= 0) break;
        }

        if (diemConLai.compareTo(BigDecimal.ZERO) > 0) {
            errors.add(Map.of("field", "diemConLai", "message", "Không đủ điểm để đổi Voucher"));
        }

        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Trừ điểm trong ví
        viDiem.setDiemKhaDung(viDiem.getDiemKhaDung().subtract(diemCanDoi));
        viDiemRepository.save(viDiem);

        // Tạo bản ghi lịch sử trừ điểm : 1 lần trừ là 1 bản ghi
        LichSuDiem lichSuTru = new LichSuDiem();
        lichSuTru.setViDiem(viDiem);
        lichSuTru.setSoDiem(diemCanDoi);
        lichSuTru.setLoaiDiem(LoaiDiem.TRU);
        lichSuTru.setGhiChu("Đổi điểm lấy voucher " + phieu.getTenKhuyenMai());
        lichSuTru.setThoiGian(OffsetDateTime.now(ZoneOffset.UTC));
        lichSuDiemRepository.save(lichSuTru);


        // Tạo voucher cho khách hàng
        KhachHangGiamGia khachHangGiamGia = new KhachHangGiamGia();
        khachHangGiamGia.setIdKhachHang(viDiem.getKhachHang());
        khachHangGiamGia.setIdPhieuGiamGia(phieu);
        khachHangGiamGia.setIsUser(false);
        khachHangGiamGia.setNgayCap(LocalDate.now());
        khachHangGiamGia.setDoiBangDiem(true);

        khachHangGiamGiaRepository.save(khachHangGiamGia);

        return "Đổi điểm thành công. Bạn đã nhận được voucher.";
    }
}
