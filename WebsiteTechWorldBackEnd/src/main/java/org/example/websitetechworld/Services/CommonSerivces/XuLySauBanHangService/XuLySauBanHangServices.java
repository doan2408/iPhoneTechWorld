package org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService;

import org.example.websitetechworld.Dto.Request.CommonRequt.CreateActionBeforeAfter;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class XuLySauBanHangServices {
    private final XuLySauBanHangRepository xuLySauBanHangRepository;
    private final LyDoXuLyRepository lyDoXuLyRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final HoaDonRepository hoaDonRepository;
    private final ImeiDaBanRepository imeiDaBanRepository;

    public XuLySauBanHangServices(XuLySauBanHangRepository xuLySauBanHangRepository, LyDoXuLyRepository lyDoXuLyRepository, ChiTietHoaDonRepository chiTietHoaDonRepository, HoaDonRepository hoaDonRepository, ImeiDaBanRepository imeiDaBanRepository) {
        this.xuLySauBanHangRepository = xuLySauBanHangRepository;
        this.lyDoXuLyRepository = lyDoXuLyRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
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

    public void taoDonXuLySauBanHang(CreateActionBeforeAfter actionBeforeAfter){
        HoaDon hoaDon = hoaDonRepository.getReferenceById(actionBeforeAfter.getIdHoaDon());
        List<ChiTietHoaDon> lstChiTietHoaDon = chiTietHoaDonRepository.findByIdHoaDon_Id(actionBeforeAfter.getIdHoaDon());
        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findById(actionBeforeAfter.getIdFailReason()).orElseThrow(
                () -> new IllegalArgumentException("Loai ly do ko ton tai")
        );
        if (lstChiTietHoaDon.isEmpty()) {
            return;
        }
        List<XuLySauBanHang> lstXuLySauBanHang = chiTietHoaDonRepository.findByIdHoaDon_Id(actionBeforeAfter.getIdHoaDon())
                .stream()
                .flatMap(ct -> imeiDaBanRepository.findByIdHoaDonChiTiet_Id(ct.getId()).stream()
                        .map(imei -> {
                            XuLySauBanHang xuLy = new XuLySauBanHang();
                            xuLy.setIdHoaDon(hoaDon);
                            xuLy.setIdImeiDaBan(imei);
                            xuLy.setLoaiVuViec(lyDoXuLy.getLoaiVuViec());
                            xuLy.setIdLyDo(lyDoXuLy);
                            xuLy.setHanhDongSauVuViec(ActionAfterCase.HOLD);
                            xuLy.setDaKiemTra(false);
                            return xuLy;
                        })
                )
                .collect(Collectors.toList());
        if (!lstXuLySauBanHang.isEmpty()) {
            xuLySauBanHangRepository.saveAll(lstXuLySauBanHang);
        }
    }
}
