package org.example.websitetechworld.Services.ClientServices.DiemServices;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse.HangClientResponse;
import org.example.websitetechworld.Entity.HangThanhVien;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.ViDiem;
import org.example.websitetechworld.Repository.HangThanhVienRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Repository.ViDiemRepository;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HangServices {
    private final HangThanhVienRepository hangRepo;
    private final ModelMapper modelMapper;
    private final ViDiemRepository viDiemRepository;
    private final KhachHangRepository khachHangRepository;

    // update hang by diem
    public void updateHang(Integer idKhachHang) {
        Optional<ViDiem> optionalViDiem = viDiemRepository.findByIdKhachHang(idKhachHang);
        if(optionalViDiem.isEmpty()) return;
        ViDiem viDiem = optionalViDiem.get();
        Integer idViDiem = viDiem.getId();

        // tinh diem xet hang con hieu luc (diem Cong trong table_lich_su_diem)
        BigDecimal tongDiem = hangRepo.diemXetHang(idViDiem, OffsetDateTime.now());
        if(tongDiem == null) tongDiem = BigDecimal.ZERO;

        // set hang theo diem
        HangThanhVien hangMoi = hangRepo.getHangThanhVien(tongDiem); // hạng mới nếu mua hàng có mức điểm vượt khoảng điểm cũ trong xét hạng
        if(hangMoi == null) return;

        KhachHang khachHang = viDiem.getKhachHang();
        HangThanhVien hangHienTai = khachHang.getHangThanhVien();

        // hang moi khac hang hien tai thi update
        if(hangHienTai == null || !hangHienTai.getId().equals(hangMoi.getId())) {
            khachHang.setHangThanhVien(hangMoi);
            khachHangRepository.save(khachHang);
        }
    }

    public String tenHang() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idKhachHang = null;
        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            idKhachHang = userDetails.getId();
        }
        updateHang(idKhachHang);
        return hangRepo.tenHangThanhVien(idKhachHang);
    }

    public List<HangClientResponse> getAllHang() {
        return hangRepo.getListHangThanhVien().stream()
                .map(h -> modelMapper.map(h, HangClientResponse.class))
                .collect(Collectors.toList());
    }

    // tong diem CONG còn hạn của 1 khách
    public BigDecimal diemXetHang(Integer idViDiem) {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        updateHang(idViDiem);
        return hangRepo.diemXetHang(idViDiem, now);
    }

    // hiem thi diem xet hang hien tai theo khoang
    public HangClientResponse rankRange() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idKhachHang = null;
        if(authentication != null && authentication.getPrincipal() instanceof CustomUserDetails){
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            idKhachHang = userDetails.getId();
        }
        if (idKhachHang == null) {
            // chua dang nhap
            return null;
        }
        Integer tongDiemXetHang = diemXetHang(idKhachHang).intValue();
        return hangRepo.findHangInfoWithMissingPoints(idKhachHang, tongDiemXetHang);
    }

}
