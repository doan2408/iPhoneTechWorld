package org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService;

import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.LyDoXuLy;
import org.example.websitetechworld.Entity.XuLySauBanHang;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Repository.LyDoXuLyRepository;
import org.example.websitetechworld.Repository.XuLySauBanHangRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class XuLySauBanHangServices {
    private final XuLySauBanHangRepository xuLySauBanHangRepository;
    private final LyDoXuLyRepository lyDoXuLyRepository;

    public XuLySauBanHangServices(XuLySauBanHangRepository xuLySauBanHangRepository, LyDoXuLyRepository lyDoXuLyRepository) {
        this.xuLySauBanHangRepository = xuLySauBanHangRepository;
        this.lyDoXuLyRepository = lyDoXuLyRepository;
    }

    public void taoDonHoanTienHuyHang(HoaDon hoaDon){
        XuLySauBanHang xuLySauBanHang = new XuLySauBanHang();
        xuLySauBanHang.setIdHoaDon(hoaDon);
        xuLySauBanHang.setLoaiVuViec(CaseType.CANCELLED);
        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findByLoaiVuViec(CaseType.CANCELLED);
        xuLySauBanHang.setIdLyDo(lyDoXuLy);
        xuLySauBanHang.setHanhDongSauVuViec(ActionAfterCase.HOLD);
        xuLySauBanHang.setDaKiemTra(false);
        xuLySauBanHangRepository.save(xuLySauBanHang);
    }
}
