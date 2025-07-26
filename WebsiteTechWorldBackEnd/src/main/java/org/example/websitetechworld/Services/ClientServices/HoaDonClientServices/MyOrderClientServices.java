package org.example.websitetechworld.Services.ClientServices.HoaDonClientServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Mapper.Client.MyOrderClientMapper;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham.HoaDonChiTiet_SanPhamAdminServices;
import org.example.websitetechworld.Services.ClientServices.GioHangClientService.GioHangClientService;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanFactory;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MyOrderClientServices {
    private final ImeiDaBanAdminServices imeiDaBanAdminServices;
    MyOrderClientMapper myOrderClientMapper = new MyOrderClientMapper();
    private final HoaDonRepository hoaDonRepository;
    private final ThanhToanFactory thanhToanFactory;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices;
    private final HoaDonChiTiet_SanPhamAdminServices hoaDonChiTiet_sanPhamAdminServices;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietHoaDonClientServices chiTietHoaDonClientServices;
    private final GioHangClientService gioHangClientService;


    public MyOrderClientServices(HoaDonRepository hoaDonRepository, ThanhToanFactory thanhToanFactory, HoaDonChiTiet_ImeiAdminServices hoaDonChiTietImeiAdminServices, HoaDonChiTiet_SanPhamAdminServices hoaDonChiTietSanPhamAdminServices, KhachHangRepository khachHangRepository, ChiTietHoaDonClientServices chiTietHoaDonClientServices, ImeiDaBanAdminServices imeiDaBanAdminServices, GioHangClientService gioHangClientService) {
        this.hoaDonRepository = hoaDonRepository;
        this.thanhToanFactory = thanhToanFactory;
        hoaDonChiTiet_ImeiAdminServices = hoaDonChiTietImeiAdminServices;
        hoaDonChiTiet_sanPhamAdminServices = hoaDonChiTietSanPhamAdminServices;
        this.khachHangRepository = khachHangRepository;
        this.chiTietHoaDonClientServices = chiTietHoaDonClientServices;
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
        this.gioHangClientService = gioHangClientService;
    }


    public Page<MyOrderClientResponse> getOrderByUserLogin(Integer userLoginId, Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<HoaDon> lstHoaDon = hoaDonRepository.findByIdKhachHang_Id(userLoginId,pageable);
        List<MyOrderClientResponse> content = lstHoaDon
                .stream()
                .map(hoaDon -> myOrderClientMapper.toMyOrderClientResponse(hoaDon))
                .toList();
        return new PageImpl<>(content,pageable,lstHoaDon.getTotalElements());
    }

    public List<Integer> findIdHoaDonByMVD(String maVanDon){
        return hoaDonRepository.findIdHoaDonByMVD(maVanDon);
    }

    public HoaDonAdminResponse findById(Integer id){
        return hoaDonRepository.findById(id).map(HoaDonAdminResponse::convertDto).orElseThrow(() -> new RuntimeException("Khong tim thay hoa don"));
    }

    public ThanhToanAdminResponse xuLyThanhToanClient(RequestThanhToanTongHop requestThanhToanTongHop, Integer idKhachHang){
        KhachHang khachHang = khachHangRepository.findById(idKhachHang).orElseThrow(() -> new IllegalArgumentException("Khách hàng không tồn tại"));

        HoaDon hoaDon = new HoaDon();
        hoaDon = saveHoaDon(hoaDon,requestThanhToanTongHop,khachHang);

        ThanhToanAdminRequest thanhToanAdminRequest = new ThanhToanAdminRequest();
        thanhToanAdminRequest.setHinhThucThanhToan(requestThanhToanTongHop.getHinhThucThanhToan());
        thanhToanAdminRequest.setSoTienKhachDua(requestThanhToanTongHop.getSoTienKhachDua());

        String hinhThucThanhToan = requestThanhToanTongHop.getHinhThucThanhToan().name();
        ThanhToanStrategy thanhToanStrategy = thanhToanFactory.getStrategy(hinhThucThanhToan);
        ThanhToanAdminResponse response = thanhToanStrategy.thanhToan(hoaDon,thanhToanAdminRequest);

        List<ChiTietHoaDon> danhSachChiTiet =  chiTietHoaDonClientServices.createInvoiceDetail(hoaDon,requestThanhToanTongHop);
        for (ChiTietHoaDon chiTietHoaDon: danhSachChiTiet){
            gioHangClientService.xoaAllGioHang(chiTietHoaDon.getIdSanPhamChiTiet());
        }
        hoaDonChiTiet_ImeiAdminServices.ganImeiChoHoaDon(danhSachChiTiet);

        if ("Đặt hàng thành công".equals(response.getMessage())) {
            if (TenHinhThuc.NGAN_HANG.equals(requestThanhToanTongHop.getHinhThucThanhToan())){
                hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.SOLD);
            }else {
                hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.RESERVED);
            }
            hoaDonChiTiet_sanPhamAdminServices.updateSoLuongProdcut(danhSachChiTiet);
        }
        return response;
    }
    private HoaDon saveHoaDon(HoaDon hoaDon, RequestThanhToanTongHop requestThanhToanTongHop, KhachHang khachHang){
        hoaDon.setIdKhachHang(khachHang);
        hoaDon.setTenNguoiMua(khachHang.getTenKhachHang());
        hoaDon.setSdtNguoiMua(khachHang.getSdt());
        hoaDon.setIsShipping(true);
        hoaDon.setPhiShip(requestThanhToanTongHop.getPhiShip());
        hoaDon.setShippingMethod(requestThanhToanTongHop.getShippingMethod());
        hoaDon.setNgayDatHang(LocalDateTime.now());
        hoaDon.setSdtNguoiMua(requestThanhToanTongHop.getSdtNguoiNhan());
        hoaDon.setTenNguoiMua(requestThanhToanTongHop.getTenNguoiNhan());
        hoaDon.setDiaChiGiaoHang(requestThanhToanTongHop.getDiaChiGiaoHang());
        hoaDon.setTongTien(requestThanhToanTongHop.getThanhTien());
        hoaDon.setThanhTien(requestThanhToanTongHop.getSoTienKhachDua());
        hoaDon.setNgayTaoHoaDon(LocalDateTime.now());
        hoaDon.setLoaiHoaDon(LoaiHoaDon.ONLINE);
        hoaDon.setTrangThaiDonHang(TrangThaiGiaoHang.PENDING);
        return hoaDonRepository.save(hoaDon);
    }



}
