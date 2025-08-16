package org.example.websitetechworld.Services.ClientServices.DiemServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse.ViDiemClientResponse;
import org.example.websitetechworld.Entity.HangThanhVien;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.LichSuDiem;
import org.example.websitetechworld.Entity.ViDiem;
import org.example.websitetechworld.Enum.KhachHang.LoaiDiem;
import org.example.websitetechworld.Repository.ChiTietLichSuDiemRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.LichSuDiemRepository;
import org.example.websitetechworld.Repository.ViDiemRepository;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViDiemServices {
    private final ViDiemRepository viDiemRepository;
    private final ModelMapper modelMapper;
    private final HangServices hangServices;
    private final LichSuDiemRepository lichSuDiemRepository;
    private final ChiTietLichSuDiemRepository chiTietLichSuDiemRepository;
    private final KhachHangRepository khachHangRepository;


    // diem kha dung thuc te (có hạn)
    public BigDecimal diemKhaDungCoHan(Integer idKhachHang) {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        // get all lich su diem CONG con han (chua dung het)
        List<LichSuDiem> listDiemCongConHan = lichSuDiemRepository.findLichSuDiemChuaTru(idKhachHang, LoaiDiem.CONG, now);

        BigDecimal diemKhaDung = BigDecimal.ZERO;

        for (LichSuDiem diem : listDiemCongConHan) {
            // tong diem da tru tu 1 ban ghi CONG
            BigDecimal daTru = chiTietLichSuDiemRepository.tongDiemDaTruTheoLichSu(diem.getId());
            //so diem con co the su dung cua ban ghi
            BigDecimal diemConLai = diem.getSoDiem().subtract(daTru != null ? daTru : BigDecimal.ZERO);
            if (diemConLai.compareTo(BigDecimal.ZERO) > 0) {
                diemKhaDung = diemKhaDung.add(diemConLai);
            }
        }
        ViDiem viDiem = viDiemRepository.findByIdKhachHang(idKhachHang).get();
        viDiem.setDiemKhaDung(diemKhaDung);
        viDiemRepository.save(viDiem);
        return diemKhaDung;
    }

    public BigDecimal diemSapHetHan7Ngay(Integer idKhachHang) {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        OffsetDateTime sau7Ngay = now.plusDays(7);

        // get all diem CONG con han (con diem)
        List<LichSuDiem> lichSuDiemConHan = lichSuDiemRepository
                .findLichSuDiemChuaTru(idKhachHang, LoaiDiem.CONG, now);

        BigDecimal tongDiemSapHetHan = BigDecimal.ZERO;

        for (LichSuDiem diem : lichSuDiemConHan) {
            // tinh nhung diem het han trong 7 ngay toi
            if (diem.getHanSuDung() != null &&
                    !diem.getHanSuDung().isBefore(now) && // hsd khong duoc truoc ngay hom nay
                    !diem.getHanSuDung().isAfter(sau7Ngay) // hsd khong duoc sau now + 7days
            ) {
                // tong diem da tru tu 1 ban ghi CONG
                BigDecimal daTru = chiTietLichSuDiemRepository.tongDiemDaTruTheoLichSu(diem.getId());
                //so diem con co the su dung cua ban ghi
                BigDecimal diemConLai = diem.getSoDiem().subtract(daTru != null ? daTru : BigDecimal.ZERO);
                if (diemConLai.compareTo(BigDecimal.ZERO) > 0) {
                    tongDiemSapHetHan = tongDiemSapHetHan.add(diemConLai);
                }
            }
        }
        return tongDiemSapHetHan;
    }

    // get diemKhaDung by idKhachHang
    public ViDiemClientResponse getDiemKhaDung() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idKhachHang = null;
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            idKhachHang = userDetails.getId();
        }
        Optional<ViDiem> optionalViDiem = viDiemRepository.findByIdKhachHang(idKhachHang);
        ViDiem viDiem = optionalViDiem.get();
        hangServices.updateHang(idKhachHang);

        // tinh diem kha dung co han
        BigDecimal diemKhaDungThucTe = diemKhaDungCoHan(idKhachHang);
        ViDiemClientResponse viDiemClientResponse = modelMapper.map(viDiem, ViDiemClientResponse.class);
        viDiemClientResponse.setDiemKhaDung(diemKhaDungThucTe);
        BigDecimal diemSapHetHan = diemSapHetHan7Ngay(idKhachHang);
        viDiemClientResponse.setDiemSapHetHan(diemSapHetHan);
        return viDiemClientResponse;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void onApplicationReady() {
        updateAllCustomerRanksOnStartup();
    }

    @Transactional
    public void updateAllCustomerRanksOnStartup() {
        System.out.println("🚀 App khởi động - Cập nhật hạng cho tất cả khách hàng...");

        try {
            // Fetch eager để tránh lazy loading
            List<KhachHang> allKhachHang = khachHangRepository.findAllWithHangThanhVien();
            int updated = 0;

            for (KhachHang kh : allKhachHang) {
                try {
                    // Lấy thông tin hạng cũ trước khi update
                    String hangCuName = kh.getHangThanhVien() != null ? kh.getHangThanhVien().getTenHang().getDisplayName() : "null";

                    hangServices.updateHang(kh.getId());

                    // Reload với fetch join
                    KhachHang khUpdated = khachHangRepository.findByIdWithHangThanhVien(kh.getId()).orElse(null);
                    if (khUpdated != null) {
                        String hangMoiName = khUpdated.getHangThanhVien() != null ? khUpdated.getHangThanhVien().getTenHang().getDisplayName() : "null";

                        if (!hangCuName.equals(hangMoiName)) {
                            updated++;
                            System.out.println("📈 KH ID " + kh.getId() + ": " + hangCuName + " → " + hangMoiName);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("❌ Lỗi update hạng KH ID " + kh.getId() + ": " + e.getMessage());
                }
            }

            System.out.println("✅ Hoàn thành! " + updated + "/" + allKhachHang.size() + " khách hàng được cập nhật hạng");

        } catch (Exception e) {
            System.err.println("❌ Lỗi nghiêm trọng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
