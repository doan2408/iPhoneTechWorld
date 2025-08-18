package org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService;

import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.ChangeStatusRequest;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateActionBeforeAfter;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateReturnRequest;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        xuLySauBanHang.setThoiGianYeuCau(LocalDateTime.now());
        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findByLoaiVuViec(CaseType.CANCELLED);
        xuLySauBanHang.setIdLyDo(lyDoXuLy);
        xuLySauBanHang.setHanhDongSauVuViec(ActionAfterCase.PENDING);
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
                            xuLy.setThoiGianYeuCau(LocalDateTime.now());
                            xuLy.setIdLyDo(lyDoXuLy);
                            xuLy.setHanhDongSauVuViec(ActionAfterCase.PENDING);
                            xuLy.setDaKiemTra(false);
                            return xuLy;
                        })
                )
                .collect(Collectors.toList());
        if (!lstXuLySauBanHang.isEmpty()) {
            xuLySauBanHangRepository.saveAll(lstXuLySauBanHang);
        }
    }
    public void taoDonTraHang(CreateReturnRequest request) {
        HoaDon hoaDon = hoaDonRepository.getReferenceById(request.getIdHoaDon());

        List<XuLySauBanHang> lstXuLySauBanHang = request.getImeis()
                .stream()
                .map(imeiReq -> {
                    ImeiDaBan imei = imeiDaBanRepository.findByIdHoaDonChiTiet_IdAndSoImei(
                            imeiReq.getIdHoaDonChiTiet(),
                            imeiReq.getSoImei()
                    );

                    LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findById(imeiReq.getIdFailReason())
                            .orElseThrow(() -> new IllegalArgumentException("Lý do không tồn tại"));

                    XuLySauBanHang xuLy = new XuLySauBanHang();
                    xuLy.setIdHoaDon(hoaDon);
                    xuLy.setIdImeiDaBan(imei);
                    xuLy.setLoaiVuViec(lyDoXuLy.getLoaiVuViec());
                    xuLy.setThoiGianYeuCau(LocalDateTime.now());
                    xuLy.setIdLyDo(lyDoXuLy);
                    xuLy.setHanhDongSauVuViec(ActionAfterCase.PENDING);
                    xuLy.setDaKiemTra(false);
                    return xuLy;
                })
                .collect(Collectors.toList());

        if (!lstXuLySauBanHang.isEmpty()) {
            xuLySauBanHangRepository.saveAll(lstXuLySauBanHang);
        }
    }

    public Page<ActionBeforeCaseResponse> getAllLyDoXuLy(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return xuLySauBanHangRepository.getAllLyDoXuLy(pageable);
    }

    public List<XuLyChiTietResponse> findByIdHoaDon(Integer idHoaDon){
        return xuLySauBanHangRepository.getAllCtXuLy(idHoaDon);
    }

    public void changeStatus(ChangeStatusRequest request) {
        XuLySauBanHang xuLySauBanHang = xuLySauBanHangRepository.findXuLySauBanHangByIdImeiDaBan_SoImei(request.getSoImei());
        xuLySauBanHang.setHanhDongSauVuViec(request.getStatus());
        xuLySauBanHang.setThoiGianXuLy(LocalDateTime.now());
        xuLySauBanHang.setDaKiemTra(true);
        xuLySauBanHangRepository.save(xuLySauBanHang);
    }
}
