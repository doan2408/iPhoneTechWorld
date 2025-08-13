package org.example.websitetechworld.Services.ClientServices.HoaDonClientServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.GetAllHoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.HoaDonAndChiTietHoaDonClientResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyReviewClientResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Mapper.Client.MyOrderClientMapper;
import org.example.websitetechworld.Repository.ChiTietHoaDonRepository;
import org.example.websitetechworld.Repository.DanhGiaSanPhamRepository;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Repository.KhachHangRepository;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham.HoaDonChiTiet_SanPhamAdminServices;
import org.example.websitetechworld.Services.ClientServices.GioHangClientService.GioHangClientService;
import org.example.websitetechworld.Services.CommonSerivces.EmailCommonService.EmailServicces;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanFactory;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class MyOrderClientServices {
    private final ImeiDaBanAdminServices imeiDaBanAdminServices;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    MyOrderClientMapper myOrderClientMapper = new MyOrderClientMapper();
    private final HoaDonRepository hoaDonRepository;
    private final ThanhToanFactory thanhToanFactory;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices;
    private final HoaDonChiTiet_SanPhamAdminServices hoaDonChiTiet_sanPhamAdminServices;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietHoaDonClientServices chiTietHoaDonClientServices;
    private final GioHangClientService gioHangClientService;
    private final DanhGiaSanPhamRepository danhGiaSanPhamRepository;
    private final EmailServicces emailServicces;


    public MyOrderClientServices(HoaDonRepository hoaDonRepository, ThanhToanFactory thanhToanFactory, HoaDonChiTiet_ImeiAdminServices hoaDonChiTietImeiAdminServices, HoaDonChiTiet_SanPhamAdminServices hoaDonChiTietSanPhamAdminServices, KhachHangRepository khachHangRepository, ChiTietHoaDonClientServices chiTietHoaDonClientServices, ImeiDaBanAdminServices imeiDaBanAdminServices, GioHangClientService gioHangClientService, EmailServicces emailServicces, DanhGiaSanPhamRepository danhGiaSanPhamRepository, ChiTietHoaDonRepository chiTietHoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.thanhToanFactory = thanhToanFactory;
        hoaDonChiTiet_ImeiAdminServices = hoaDonChiTietImeiAdminServices;
        hoaDonChiTiet_sanPhamAdminServices = hoaDonChiTietSanPhamAdminServices;
        this.khachHangRepository = khachHangRepository;
        this.chiTietHoaDonClientServices = chiTietHoaDonClientServices;
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
        this.gioHangClientService = gioHangClientService;
        this.danhGiaSanPhamRepository = danhGiaSanPhamRepository;
        this.emailServicces = emailServicces;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
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


    public Page<MyReviewClientResponse> getReview(Integer userLoginId, Integer pageNo, Integer pageSize) {
        // Khởi tạo phân trang
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<HoaDon> lstHoaDon = hoaDonRepository.findByIdKhachHang_Id(userLoginId, pageable);

        List<MyReviewClientResponse> content = lstHoaDon
                .stream()
                .map(hoaDon -> {
                    MyReviewClientResponse dto = myOrderClientMapper.toMyReviewClientResponse(hoaDon);

                    // Gọi truy vấn kiểm tra đánh giá và phản hồi
                    Map<String, Boolean> check = danhGiaSanPhamRepository.checkDaDanhGiaVaPhanHoi(hoaDon.getId(), userLoginId);

                    // Xử lý kết quả từ Map
                    if (check != null && check.containsKey("da_danh_gia") && check.containsKey("co_phan_hoi")) {
                        dto.setDaDanhGia(check.get("da_danh_gia"));
                        dto.setCoPhanHoi(check.get("co_phan_hoi"));
                    } else {
                        dto.setDaDanhGia(false);
                        dto.setCoPhanHoi(false);
                    }

                    return dto;
                })
                .toList();

        return new PageImpl<>(content, pageable, lstHoaDon.getTotalElements());
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
        hoaDonRepository.save(hoaDon);

        ThanhToanAdminRequest thanhToanAdminRequest = new ThanhToanAdminRequest();
        thanhToanAdminRequest.setHinhThucThanhToan(requestThanhToanTongHop.getHinhThucThanhToan());
        thanhToanAdminRequest.setSoTienKhachDua(requestThanhToanTongHop.getSoTienKhachDua());

        String hinhThucThanhToan = requestThanhToanTongHop.getHinhThucThanhToan().name();
        ThanhToanStrategy thanhToanStrategy = thanhToanFactory.getStrategy(hinhThucThanhToan);
        ThanhToanAdminResponse response = thanhToanStrategy.thanhToan(hoaDon,thanhToanAdminRequest);

        List<ChiTietHoaDon> danhSachChiTiet =  chiTietHoaDonClientServices.createInvoiceDetail(hoaDon,requestThanhToanTongHop);

        if (TenHinhThuc.VNPAY.equals(requestThanhToanTongHop.getHinhThucThanhToan())){
            hoaDonChiTiet_ImeiAdminServices.ganImeiChoHoaDon(danhSachChiTiet);
            hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.RESERVED);
            hoaDonChiTiet_sanPhamAdminServices.updateSoLuongProdcut(danhSachChiTiet);
        }else {
            for (ChiTietHoaDon chiTietHoaDon: danhSachChiTiet){
                gioHangClientService.xoaAllGioHang(chiTietHoaDon.getIdSanPhamChiTiet());
            }
        }

        sendMailFromInvoice(hoaDon);


        return response;
    }

    public ThanhToanAdminResponse xuLyThanhToanGuest(RequestThanhToanTongHop requestThanhToanTongHop){

        HoaDon hoaDon = new HoaDon();
        hoaDon = saveHoaDon(hoaDon,requestThanhToanTongHop,null);
//        String maVanDon = generateMaVanDon(hoaDon.getId());
//        hoaDon.setMaVanDon(maVanDon);
        hoaDonRepository.save(hoaDon);

        ThanhToanAdminRequest thanhToanAdminRequest = new ThanhToanAdminRequest();
        thanhToanAdminRequest.setHinhThucThanhToan(requestThanhToanTongHop.getHinhThucThanhToan());
        thanhToanAdminRequest.setSoTienKhachDua(requestThanhToanTongHop.getSoTienKhachDua());

        String hinhThucThanhToan = requestThanhToanTongHop.getHinhThucThanhToan().name();
        ThanhToanStrategy thanhToanStrategy = thanhToanFactory.getStrategy(hinhThucThanhToan);
        ThanhToanAdminResponse response = thanhToanStrategy.thanhToan(hoaDon,thanhToanAdminRequest);

        List<ChiTietHoaDon> danhSachChiTiet =  chiTietHoaDonClientServices.createInvoiceDetail(hoaDon,requestThanhToanTongHop);

        hoaDonChiTiet_ImeiAdminServices.ganImeiChoHoaDon(danhSachChiTiet);

        if ("Đặt hàng thành công".equals(response.getMessage())) {
                hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.RESERVED);
            hoaDonChiTiet_sanPhamAdminServices.updateSoLuongProdcut(danhSachChiTiet);
            for (ChiTietHoaDon chiTietHoaDon: danhSachChiTiet){
                gioHangClientService.xoaAllGioHang(chiTietHoaDon.getIdSanPhamChiTiet());
            }
            sendMailFromInvoice(hoaDon);
        }
        return response;
    }
    private HoaDon saveHoaDon(HoaDon hoaDon, RequestThanhToanTongHop requestThanhToanTongHop, KhachHang khachHang){
        if (khachHang != null){
            hoaDon.setIdKhachHang(khachHang);
            hoaDon.setTenNguoiMua(khachHang.getTenKhachHang());
            hoaDon.setSdtNguoiMua(khachHang.getSdt());
        }
        if (requestThanhToanTongHop.getIdPhieuGiamGia() != null){
            PhieuGiamGia phieuGiamGia = new PhieuGiamGia();
            phieuGiamGia.setId(requestThanhToanTongHop.getIdPhieuGiamGia());
            hoaDon.setIdPhieuGiamGia(phieuGiamGia);
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
        String maHoaDon = null;
        if (hoaDon.getId() < 10) {
            maHoaDon =  "HD00" + hoaDon.getId();
        } else if (hoaDon.getId()  < 100) {
            maHoaDon =  "HD0" + hoaDon.getId();
        } else {
            maHoaDon = "HD" + hoaDon.getId();
        }
        String subject = "Xác nhận đơn hàng #" + maHoaDon;

        String html = "<h2 style='color:#2c3e50;'>Cảm ơn bạn đã đặt hàng!</h2>"
                + "<p>Xin chào <b>" + hoaDon.getTenNguoiNhan() + "</b>,</p>"
                + "<p>Đơn hàng <b>" + maHoaDon + "</b> của bạn đã được xác nhận vào ngày "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".</p>"
                + "<p><i>Chúng tôi sẽ sớm liên hệ với bạn để giao hàng.</i></p>"
                + "<br><p>Trân trọng,</p><p>Đội ngũ bán hàng</p>";

        emailServicces.sendHtmlEmail(hoaDon.getEmailNguoiNhan(), subject, html);
    }

    public void sendMailByyMaVanDon(HoaDon hoaDon){
        String maHoaDon = null;
        String subject = "Xác nhận đơn hàng #" + hoaDon.getMaVanDon();

        String html = "<h2 style='color:#2c3e50;'>Cảm ơn bạn đã đặt hàng!</h2>"
                + "<p>Xin chào <b>" + hoaDon.getTenNguoiNhan() + "</b>,</p>"
                + "<p>Đơn hàng <b>" + hoaDon.getMaHoaDon() + "</b> của bạn đã được xác nhận vào ngày "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".</p>"
                + "<p><i>Chúng tôi sẽ sớm liên hệ với bạn để giao hàng.</i></p>"
                + "<br><p>Trân trọng,</p><p>Đội ngũ bán hàng</p>";

        emailServicces.sendHtmlEmail(hoaDon.getEmailNguoiNhan(), subject, html);
    }

    public List<Integer> getHoaDonChiTietIdsByMaHoaDon(String maHoaDon) {
        List<HoaDon> danhSachHoaDon = hoaDonRepository.findByMaHoaDon(maHoaDon);

        if (danhSachHoaDon.isEmpty()) {
            return Collections.emptyList();
        }
        HoaDon hoaDon = danhSachHoaDon.get(0);

        List<ChiTietHoaDon> danhSachHDCT = chiTietHoaDonRepository.findByIdHoaDon_Id(hoaDon.getId());

        return danhSachHDCT.stream()
                .map(ChiTietHoaDon::getId)
                .collect(Collectors.toList());
    }

    public void deleteHoaDonById(Integer id){
        hoaDonRepository.deleteById(id);
    }




}
