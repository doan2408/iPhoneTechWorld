package org.example.websitetechworld.Services.ClientServices.HoaDonClientServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.HoaDonAndChiTietHoaDonClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Mapper.Client.MyOrderClientMapper;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham.HoaDonChiTiet_SanPhamAdminServices;
import org.example.websitetechworld.Services.ClientServices.GioHangClientService.GioHangClientService;
import org.example.websitetechworld.Services.CommonSerivces.EmailCommonService.EmailServicces;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanFactory;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
    private final EmailServicces emailServicces;


    public MyOrderClientServices(HoaDonRepository hoaDonRepository, ThanhToanFactory thanhToanFactory, HoaDonChiTiet_ImeiAdminServices hoaDonChiTietImeiAdminServices, HoaDonChiTiet_SanPhamAdminServices hoaDonChiTietSanPhamAdminServices, KhachHangRepository khachHangRepository, ChiTietHoaDonClientServices chiTietHoaDonClientServices, ImeiDaBanAdminServices imeiDaBanAdminServices, GioHangClientService gioHangClientService, EmailServicces emailServicces) {
        this.hoaDonRepository = hoaDonRepository;
        this.thanhToanFactory = thanhToanFactory;
        hoaDonChiTiet_ImeiAdminServices = hoaDonChiTietImeiAdminServices;
        hoaDonChiTiet_sanPhamAdminServices = hoaDonChiTietSanPhamAdminServices;
        this.khachHangRepository = khachHangRepository;
        this.chiTietHoaDonClientServices = chiTietHoaDonClientServices;
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
        this.gioHangClientService = gioHangClientService;
        this.emailServicces = emailServicces;
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
        String maVanDon = generateMaVanDon(hoaDon.getId());
        hoaDon.setMaVanDon(maVanDon);
        hoaDonRepository.save(hoaDon);

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
            sendMailFromInvoice(hoaDon);

        }
        return response;
    }

    public ThanhToanAdminResponse xuLyThanhToanGuest(RequestThanhToanTongHop requestThanhToanTongHop){

        HoaDon hoaDon = new HoaDon();
        hoaDon = saveHoaDon(hoaDon,requestThanhToanTongHop,null);
        String maVanDon = generateMaVanDon(hoaDon.getId());
        hoaDon.setMaVanDon(maVanDon);
        hoaDonRepository.save(hoaDon);

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
        if (khachHang != null){
            hoaDon.setIdKhachHang(khachHang);
            hoaDon.setTenNguoiMua(khachHang.getTenKhachHang());
            hoaDon.setSdtNguoiMua(khachHang.getSdt());
        }
        hoaDon.setIsShipping(true);
        hoaDon.setIsDelete(false);
        hoaDon.setPhiShip(requestThanhToanTongHop.getPhiShip());
        hoaDon.setShippingMethod(requestThanhToanTongHop.getShippingMethod());
        hoaDon.setNgayDatHang(LocalDateTime.now());
        hoaDon.setSdtNguoiNhan(requestThanhToanTongHop.getSdtNguoiNhan());
        hoaDon.setTenNguoiNhan(requestThanhToanTongHop.getTenNguoiNhan());
        hoaDon.setEmailNguoiNhan(requestThanhToanTongHop.getEmailNguoiNhan());
        hoaDon.setDiaChiGiaoHang(requestThanhToanTongHop.getDiaChiGiaoHang());
        hoaDon.setTongTien(requestThanhToanTongHop.getThanhTien());
        hoaDon.setThanhTien(requestThanhToanTongHop.getSoTienKhachDua());
        hoaDon.setNgayTaoHoaDon(LocalDateTime.now());
        hoaDon.setLoaiHoaDon(LoaiHoaDon.ONLINE);
        hoaDon.setTrangThaiDonHang(TrangThaiGiaoHang.PENDING);
        return hoaDonRepository.save(hoaDon);
    }


    //cuong
    public List<HoaDonAndChiTietHoaDonClientResponse> getHoaDonAndChiTiet(Integer idHoaDon) {
        List<Object[]> rawData = hoaDonRepository.findHoaDonAndChiTiet(idHoaDon);

        return rawData.stream()
                .map(row -> new HoaDonAndChiTietHoaDonClientResponse(
                        ((Number) row[0]).intValue(),   // id_hoa_don
                        ((Number) row[1]).intValue(),   // id_chi_tiet_hoa_don
                        ((Number) row[2]).intValue()    // id_san_pham_chi_tiet
                ))
                .collect(Collectors.toList());
    }
    private String generateMaVanDon(Integer invoiceId) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder suffix = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            suffix.append(characters.charAt(random.nextInt(characters.length())));
        }
        return "VD" + invoiceId + "-" + suffix;
    }

    public void sendMailFromInvoice(HoaDon hoaDon){
        String subject = "Xác nhận đơn hàng #" + hoaDon.getMaHoaDon();

        String html = "<h2 style='color:#2c3e50;'>Cảm ơn bạn đã đặt hàng!</h2>"
                + "<p>Xin chào <b>" + hoaDon.getIdKhachHang().getTenKhachHang() + "</b>,</p>"
                + "<p>Đơn hàng <b>" + hoaDon.getMaVanDon() + "</b> của bạn đã được xác nhận vào ngày "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".</p>"
                + "<p><i>Chúng tôi sẽ sớm liên hệ với bạn để giao hàng.</i></p>"
                + "<br><p>Trân trọng,</p><p>Đội ngũ bán hàng</p>";

        emailServicces.sendHtmlEmail(hoaDon.getEmailNguoiNhan(), subject, html);
    }



}
