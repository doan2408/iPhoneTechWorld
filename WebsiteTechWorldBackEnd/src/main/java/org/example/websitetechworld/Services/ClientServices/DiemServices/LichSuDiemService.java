package org.example.websitetechworld.Services.ClientServices.DiemServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.LichSuDiemResponse.LichSuDiemResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
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
import java.math.RoundingMode;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LichSuDiemService {

    private final LichSuDiemRepository lichSuDiemRepository;
    private final ViDiemRepository viDiemRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final ChiTietLichSuDiemRepository chiTietLichSuDiemRepository;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HangServices hangServices;
    private final ViDiemServices viDiemServices;

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
        hangServices.updateHang(idKhachHang);
        viDiemServices.getDiemKhaDung();
        Page<LichSuDiem> pageResult = lichSuDiemRepository.getLichSuDiem(idKhachHang, fromDate, toDate, pageable);
        return pageResult.map(this::convertToResponse);
    }


    @Transactional
    public String doiDiemNhanVoucher(Integer idPhieuGiamGia) {
        viDiemServices.getDiemKhaDung();
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

        // ngay doi hien tai
        LocalDate ngayHienTai = LocalDate.now();
        YearMonth thangNamHienTai = YearMonth.from(ngayHienTai);

        boolean daDoiTrongThang = khachHangGiamGiaRepository
                .checkSoLanDoiTrong1Thang(idKhachHang, idPhieuGiamGia, thangNamHienTai.getMonthValue(), thangNamHienTai.getYear());

        if(daDoiTrongThang) {
            errors.add(Map.of("field", "voucher", "message", "Bạn đã đổi voucher này trong tháng rồi !"));
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
            // tong diem da tru trong 1 ban ghi lich su
            BigDecimal daTru = chiTietLichSuDiemRepository.tongDiemDaTruTheoLichSu(lichSu.getId());
            // số điểm khả dụng từ 1 bản ghi lịch sử
            BigDecimal diemCon = lichSu.getSoDiem().subtract(daTru);

            if (diemCon.compareTo(BigDecimal.ZERO) <= 0) continue;

            // nếu điểm khả dụng của 1 bản ghi lịch sử < điểm cần đổi => trừ điểm khả dụng của 1 bản ghi lịch sử về 0
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
        hangServices.updateHang(idKhachHang);

        return "Đổi điểm thành công. Bạn đã nhận được voucher.";
    }

    public void congDiemTuHoaDon(int idHoaDon) {
        List<Map<String, String>> errors = new ArrayList<>();

        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if(optionalHoaDon.isEmpty()) {
            errors.add(Map.of("field", "hoaDon", "message", "Không tìm thấy hóa đơn với id: " + idHoaDon));
        }
        if(!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        HoaDon hoaDon = optionalHoaDon.get();

        // check cong diem
        boolean daCongDiem = lichSuDiemRepository.existsByHoaDon(hoaDon);
        if(daCongDiem) {
            return;
        }

        // check valid status
        if (hoaDon.getIsShipping() != null) {
            boolean isShipping = hoaDon.getIsShipping();
            String trangThaiThanhToan = hoaDon.getTrangThaiThanhToan().name();
            String trangThaiGiaoHang = hoaDon.getTrangThaiDonHang().name();

            boolean valid = (isShipping && "COMPLETED".equalsIgnoreCase(trangThaiThanhToan) && "DELIVERED".equalsIgnoreCase(trangThaiGiaoHang))
                    || (!isShipping && "COMPLETED".equalsIgnoreCase(trangThaiThanhToan));

            if(!valid) {
                return;
            }
        }

        KhachHang khachHang = hoaDon.getIdKhachHang();

        // Kiểm tra null trước khi gọi bất kỳ phương thức nào
        if (khachHang == null) {
            return; // Khách vãng lai, không cộng điểm
        }

        Integer idKhachHang = khachHang.getId();


        //get ví điểm
        Optional<ViDiem> optionalViDiem  = viDiemRepository.findByIdKhachHang(idKhachHang);
        if(optionalViDiem.isEmpty()) {
            throw new RuntimeException("Không tìm thấy ví điểm của khách hàng có id: " + idKhachHang);
        }

        ViDiem viDiem = optionalViDiem.get();

        // tính điểm dựa trên 1% của đơn hàng (có tính giảm giá + không tính phí ship)
        BigDecimal diemCong =  hoaDon.getThanhTien()
                .subtract(hoaDon.getPhiShip() != null ? hoaDon.getPhiShip() : BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(0.01))
                .setScale(2, RoundingMode.HALF_UP);

        // cộng điểm vào ví
        viDiem.setDiemKhaDung(viDiem.getDiemKhaDung().add(diemCong));
        viDiemRepository.save(viDiem);

        // save history
        LichSuDiem lichSuDiem = new LichSuDiem();
        lichSuDiem.setViDiem(viDiem);
        lichSuDiem.setHoaDon(hoaDon);
        lichSuDiem.setSoDiem(diemCong);
        lichSuDiem.setLoaiDiem(LoaiDiem.CONG);
        lichSuDiem.setGhiChu("Cộng điểm từ đơn hàng: " + hoaDon.getMaHoaDon());
        lichSuDiem.setThoiGian(OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        lichSuDiem.setHanSuDung(OffsetDateTime.now().plusYears(1)); // +1 năm hsd tính tại lúc điểm vào
        lichSuDiemRepository.save(lichSuDiem);
        hangServices.updateHang(idKhachHang);
        viDiemServices.getDiemKhaDung();
    }

}
