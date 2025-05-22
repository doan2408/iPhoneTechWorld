package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThongTinNguoiNhanAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonAdminService {
    private final HoaDonRepository hoaDonRepository;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private final GiaoHangRepository giaoHangRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private static final Logger logger = LoggerFactory.getLogger(HoaDonAdminService.class);

    public HoaDonAdminService(HoaDonRepository hoaDonRepository, LichSuHoaDonRepository lichSuHoaDonRepository, ChiTietThanhToanRepository chiTietThanhToanRepository, GiaoHangRepository giaoHangRepository, ChiTietHoaDonRepository chiTietHoaDonRepository, KhachHangRepository khachHangRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.giaoHangRepository = giaoHangRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.khachHangRepository = khachHangRepository;
    }

    public List<HoaDonAdminResponse> getAllHoaDon(){
        return hoaDonRepository.findAll().stream().map(HoaDonAdminResponse::convertDto).collect(Collectors.toList());
    }

    public List<GetAllHoaDonAdminResponse> getPageHoaDon(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        System.out.println("So phan tu: " + pageable.getPageNumber());
        return hoaDonRepository.findAll(pageable).stream().map(GetAllHoaDonAdminResponse::convertDto).toList();

    }

    public HoaDonAdminResponse findById(Integer id){
        return hoaDonRepository.findById(id).map(HoaDonAdminResponse::convertDto).orElseThrow(() -> new RuntimeException("Khong tim thay hoa don"));
    }

    public List<LichSuHoaDonAdminResponse> getPageLichSuHoaDon(Integer hoaDonId,Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return lichSuHoaDonRepository.findByIdHoaDon_Id(hoaDonId,pageable).stream().map(LichSuHoaDonAdminResponse::convertDto).toList();
    }

    public List<ChiTietThanhToanAdminResponse> getPageChiTietThanhToan(Integer hoaDonId,Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return chiTietThanhToanRepository.findByIdHoaDon_Id(hoaDonId,pageable).stream().map(ChiTietThanhToanAdminResponse::convertDto).toList();
    }
    public List<GiaoHangAdminResponse> getPageGiaoHang(Integer hoaDonId,Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return giaoHangRepository.findByIdHoaDon_Id(hoaDonId,pageable).stream().map(GiaoHangAdminResponse::convertDto).toList();
    }

    public HoaDon createPendingInvoice(){

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayTao(LocalDate.now());
        hoaDon.setLoaiHoaDon(LoaiHoaDon.POS);
        hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PENDING);
        return hoaDonRepository.save(hoaDon);
    }

    public void updateTongTien(Integer hoaDonId){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElseThrow();

        List<ChiTietHoaDon> chiTietHoaDonList = chiTietHoaDonRepository.findByIdHoaDon_Id(hoaDonId);

        BigDecimal tongTien = chiTietHoaDonList.stream()
                .map(ct -> ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        hoaDon.setTongTien(tongTien);
        hoaDonRepository.save(hoaDon);
    }

    public void selectKhachHang(Integer hoaDonId, Integer khachHangId){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));

        KhachHang khachHang = khachHangRepository.findById(khachHangId)
                .orElseThrow(()-> new IllegalArgumentException("Khách hang nay khong ton tai"));

        String diaChiDayDu = khachHang.getDiaChis().stream()
                .filter(DiaChi::getDiaChiChinh)
                .findFirst()
                .map(DiaChi::getDiaChiDayDu)
                .orElse(null);

        hoaDon.setIdKhachHang(khachHang);
        hoaDon.setTenNguoiNhan(khachHang.getTenKhachHang());
        hoaDon.setDiaChi(diaChiDayDu);
        hoaDon.setSdt(khachHang.getSdt());
        hoaDonRepository.save(hoaDon);
    }


}
