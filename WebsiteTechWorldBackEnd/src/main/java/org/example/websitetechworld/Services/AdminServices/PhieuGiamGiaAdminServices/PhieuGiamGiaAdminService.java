package org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPhatHanh;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.LoginServices.MailService;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaAdminService {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    private final Set<Integer> idKhachHangDaGuiMail = new HashSet<>();

    public PhieuGiamGiaAdminService(PhieuGiamGiaRepository phieuGiamGiaRepository,
                                    KhachHangGiamGiaRepository khachHangGiamGiaRepository,
                                    KhachHangRepository khachHangRepository,
                                    ModelMapper modelMapper,
                                    SanPhamChiTietRepository sanPhamChiTietRepository, MailService mailService) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangGiamGiaRepository = khachHangGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.modelMapper = modelMapper;
        this.mailService = mailService;
        cauHinhModelMapper();
    }

    private void cauHinhModelMapper () {
        modelMapper.typeMap(PhieuGiamGiaAdminRequest.class, PhieuGiamGia.class);
        modelMapper.typeMap(PhieuGiamGia.class, PhieuGiamGiaAdminResponse.class);
    }

    public Page<PhieuGiamGiaAdminResponse> layDanhSachPhieuGiamGia (String timKiem, TrangThaiPGG trangThai,
                                                                  LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                                  int trang, int kichThuoc, String sapXepTheo, String huongSapXep) {
        Sort sapXep = huongSapXep.equalsIgnoreCase("desc") ? Sort.by(sapXepTheo).descending() : Sort.by(sapXepTheo).ascending();
        Pageable phanTrang = PageRequest.of(trang, kichThuoc, sapXep);
        Page<PhieuGiamGia> trangPhieuGiamGia = phieuGiamGiaRepository.findAll(timKiem, trangThai, ngayBatDau, ngayKetThuc, phanTrang);
        return trangPhieuGiamGia.map(this::anhXaPhieuGiamGia);
    }

    public PhieuGiamGiaAdminResponse layPhieuGiamGiaTheoId (Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));
        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    @Transactional
    public PhieuGiamGiaAdminResponse themPhieuGiamGia (PhieuGiamGiaAdminRequest request) {
        kiemTraNgayHopLe(request.getNgayBatDau(), request.getNgayKetThuc());
//        kiemTraPhieuChoSp(request);

        LocalDate currentDate = LocalDate.now();
        if (request.getNgayBatDau().isBefore(currentDate)) {
            throw new IllegalStateException("Ngày bắt đầu phải sau ngày hiện tại");
        }

        if (request.getMaGiamGia() == null || request.getMaGiamGia().trim().isEmpty()) {
            request.setMaGiamGia(taoMaGiamGiaNgauNhien());
        }

        PhieuGiamGia phieuGiamGia = modelMapper.map(request, PhieuGiamGia.class);
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

//        if (!phieuGiamGia.getCongKhai()) {
//            xuLyKhachHangGiamGia(phieuGiamGia, layDanhSachIdKhachHangTuYeuCau(request));
//        }

        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

    @Transactional
    public PhieuGiamGiaAdminResponse capNhatPhieuGiamGia(Integer id, PhieuGiamGiaAdminRequest request) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        LocalDate currentDate = LocalDate.now();
        boolean hasStarted = !currentDate.isBefore(phieuGiamGia.getNgayBatDau());

        if (isTrangThaiPhatHanhModified(phieuGiamGia, request)) {
            if (phieuGiamGia.getTrangThaiPhatHanh() == TrangThaiPhatHanh.ISSUED
                    && request.getTrangThaiPhatHanh() == TrangThaiPhatHanh.STOP_ISSUED) {

                notifyAffectedCustomers(phieuGiamGia);
                khachHangGiamGiaRepository.deleteByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
            }
        }

        boolean isSavedByCustomers = khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);

        if (isSavedByCustomers && isHangToiThieuModified(phieuGiamGia, request)) {
            notifyAffectedCustomers(phieuGiamGia);
        } else if (isHangToiThieuModified(phieuGiamGia, request)) {
            phieuGiamGia.setHangToiThieu(request.getHangToiThieu());
        }

        kiemTraNgayHopLe(request.getNgayBatDau(), request.getNgayKetThuc());

        if (!hasStarted) {
            modelMapper.map(request, phieuGiamGia);
        } else {

            if (phieuGiamGia.getNgayKetThuc().equals(request.getNgayKetThuc())) {
                notifyAffectedCustomers(phieuGiamGia);
            }
            phieuGiamGia.setTenKhuyenMai(request.getTenKhuyenMai());
            phieuGiamGia.setSoLuong(request.getSoLuong());
            phieuGiamGia.setNgayKetThuc(request.getNgayKetThuc());
            phieuGiamGia.setSoDiemCanDeDoi(request.getSoDiemCanDeDoi());
            phieuGiamGia.setDieuKienApDung(request.getDieuKienApDung());
            phieuGiamGia.setCongKhai(request.getCongKhai());
            phieuGiamGia.setTrangThaiPhatHanh(request.getTrangThaiPhatHanh());
        }

        phieuGiamGia.setId(id);

        xoaKhachHangGiamGiaChuaSuDung(phieuGiamGia);

        return modelMapper.map(phieuGiamGiaRepository.save(phieuGiamGia), PhieuGiamGiaAdminResponse.class);
    }

    private String taoMaGiamGiaNgauNhien() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int idx = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(idx));
        }
        return sb.toString();
    }

    @Transactional
    public String xoaPhieuGiamGia(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với ID: " + id));

        LocalDate currentDate = LocalDate.now();
        boolean hasStarted = !currentDate.isBefore(phieuGiamGia.getNgayBatDau());

        if (hasStarted) {
            throw new IllegalStateException("Không thể xóa phiếu giảm giá đã bắt đầu.");
        }

        boolean isSavedByCustomers = khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
        if (isSavedByCustomers) {
            notifyAffectedCustomers(phieuGiamGia);
        }

        if (khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIsUser(phieuGiamGia, true)) {
            throw new IllegalStateException("Không thể xóa phiếu giảm giá đã được sử dụng");
        }

        khachHangGiamGiaRepository.deleteByIdPhieuGiamGiaId(id);
        phieuGiamGiaRepository.delete(phieuGiamGia);
        return "Đã xóa thành công phiếu giảm giá với ID: " + id;
    }

    private boolean isTrangThaiPhatHanhModified(PhieuGiamGia existing, PhieuGiamGiaAdminRequest request) {
        return existing.getTrangThaiPhatHanh() != request.getTrangThaiPhatHanh();
    }

    private boolean isHangToiThieuModified(PhieuGiamGia existing, PhieuGiamGiaAdminRequest request) {
        return (existing.getHangToiThieu() != null && !existing.getHangToiThieu().equals(request.getHangToiThieu())) ||
                (existing.getHangToiThieu() == null && request.getHangToiThieu() != null);
    }

    @Async
    public void notifyAffectedCustomers(PhieuGiamGia phieu) {
        try {
            List<KhachHangGiamGia> lst = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieu, false);
            for (KhachHangGiamGia khgg : lst) {
                Integer idKhach = khgg.getIdKhachHang().getId();
                if (!idKhachHangDaGuiMail.contains(idKhach)) {
                    KhachHang kh = khgg.getIdKhachHang();
                    mailService.sendMail(
                            kh.getEmail(),
                            "Thông báo về phiếu giảm giá",
                            String.format("""
                                    Kính gửi %s,
                                    
                                    Phiếu giảm giá %s đã được cập nhật/hủy bỏ. Vui lòng kiểm tra lại trong hệ thống.
                                    
                                    TechWorld""", kh.getTenKhachHang(), phieu.getTenKhuyenMai()));
                    idKhachHangDaGuiMail.add(idKhach);
                }
            }
            idKhachHangDaGuiMail.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    public Page<SanPhamChiTietResponse> layDanhSachSanPham (String timKiem, int trang, int kichThuoc) {
//        Pageable phanTrang = PageRequest.of(trang, kichThuoc);
//        Page<SanPhamChiTiet> sanphamChiTiets = (timKiem == null || timKiem.isEmpty()) ?
//                sanPhamChiTietRepository.findAll(phanTrang) :
//                sanPhamChiTietRepository.findByIdSanPham_TenSanPhamContaining(timKiem, phanTrang);
//        return sanphamChiTiets.map(spct -> {
//            SanPhamChiTietResponse res = new SanPhamChiTietResponse();
//            res.setId(spct.getId());
//            res.setMaSanPhamChiTiet(spct.getMaSanPhamChiTiet());
//            res.setTenSanPham(spct.getIdSanPham().getTenSanPham());
//            res.setTenMau(spct.getIdMau().getTenMau());
//            res.setGiaBan(spct.getGiaBan());
//            return res;
//        });
//
//    }

    @Transactional
    public void capNhatTrangThaiPhieuGiamGia () {
        LocalDate hienTai = LocalDate.now();
        List<PhieuGiamGia> danhSachPhieu = phieuGiamGiaRepository.findAll();
        for (PhieuGiamGia phieu : danhSachPhieu) {
            TrangThaiPGG trangThaiMoi = xacDinhTrangThaiPhieu(phieu, hienTai);
            if (phieu.getTrangThaiPhieuGiamGia() != trangThaiMoi) {
                phieu.setTrangThaiPhieuGiamGia(trangThaiMoi);
                phieuGiamGiaRepository.save(phieu);
            }
        }
    }

//    @Transactional
//    public void themKhuyenMaiSanPham (PhieuGiamGia phieuGiamGia, List<Integer> danhSachIdSanPham) {
//
//        if (phieuGiamGia == null) {
//            throw new IllegalArgumentException("Phiếu giảm giá không được null");
//        }
//
//        if (danhSachIdSanPham == null || danhSachIdSanPham.isEmpty()) {
//            throw new IllegalArgumentException("Danh sách ID sản phẩm không được rỗng");
//        }
//
//        List<Integer> idSanPhamKhongHopLe = danhSachIdSanPham.stream()
//                .filter(id -> !sanPhamChiTietRepository.existsById(id))
//                .toList();
//        if (!idSanPhamKhongHopLe.isEmpty()) {
//            throw new IllegalArgumentException("Các ID sản phẩm không tồn tại: " + idSanPhamKhongHopLe);
//        }
//
//        List<KhuyenMaiSanPham> list = new ArrayList<>();
//
//        for (Integer idSanPham : danhSachIdSanPham) {
//            KhuyenMaiSanPham khuyenMaiSanPham = new KhuyenMaiSanPham();
//            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSanPham)
//                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + idSanPham));
//
//            khuyenMaiSanPham.setPhieuGiamGia(phieuGiamGia);
//            khuyenMaiSanPham.setSanPhamChiTiet(sanPhamChiTiet);
//            khuyenMaiSanPham.setTenSanPham(sanPhamChiTiet.getIdSanPham().getTenSanPham());
//            khuyenMaiSanPham.setGiaGoc(sanPhamChiTiet.getGiaBan());
//            khuyenMaiSanPham.setGiaKhuyenMai(tinhGiaKhuyenMai(sanPhamChiTiet, phieuGiamGia));
//            khuyenMaiSanPham.setNgayCap(LocalDate.now());
//            list.add(khuyenMaiSanPham);
//        }
//        khuyenMaiSanPhamRepository.saveAll(list);
//
//    }


    private void kiemTraNgayHopLe (LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        if (ngayBatDau.isAfter(ngayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

//    private void kiemTraPhieuChoSp (PhieuGiamGiaAdminRequest request) {
//        if (!request.getLoaiApDung() && (request.getSanPhamIds() == null || request.getSanPhamIds().isEmpty())) {
//            throw new IllegalArgumentException("Phiếu giảm giá cho sản phẩm phải có ít nhất một sản phẩm được chọn");
//        }
//    }

    private Set<Integer> layDanhSachIdKhachHangTuYeuCau (PhieuGiamGiaAdminRequest request) {
        Set<Integer> danhSachIdKhachHang = new HashSet<>();
        if (request.getHangToiThieu() != null) {
            danhSachIdKhachHang.addAll(khachHangRepository.findByHangKhachHang(HangKhachHang.valueOf(request.getHangToiThieu()))
                    .stream()
                    .map(KhachHang::getId)
                    .collect(Collectors.toSet()));
        }
        kiemTraIdKhachHangHopLe(danhSachIdKhachHang);
        return danhSachIdKhachHang;
    }

    private void kiemTraIdKhachHangHopLe (Set<Integer> danhSachIdKhachHang) {
        List<Integer> idKhongHopLe = danhSachIdKhachHang.stream()
                .filter(id -> !khachHangRepository.existsById(id))
                .toList();
        if (!idKhongHopLe.isEmpty()) {
            throw new IllegalArgumentException("Các ID khách hàng không tồn tại: " + idKhongHopLe);
        }
    }

    private void xuLyKhachHangGiamGia (PhieuGiamGia phieuGiamGia, Set<Integer> danhSachIdKhachHang) {
        xoaKhachHangGiamGiaChuaSuDung(phieuGiamGia);
        for (Integer idKhachHang : danhSachIdKhachHang) {
            KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                    .orElseThrow(() -> new IllegalArgumentException("Khách hàng ID " + idKhachHang + " không tồn tại"));
            if (!khachHangGiamGiaRepository.existsByIdPhieuGiamGiaAndIdKhachHang(phieuGiamGia, khachHang)) {
                taoKhachHangGiamGia(phieuGiamGia, khachHang);
            }
        }
    }

    private void xoaKhachHangGiamGiaChuaSuDung (PhieuGiamGia phieuGiamGia) {
        List<KhachHangGiamGia> banGhiChuaSuDung = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIsUser(phieuGiamGia, false);
        khachHangGiamGiaRepository.deleteAll(banGhiChuaSuDung);
    }

    private void taoKhachHangGiamGia (PhieuGiamGia phieuGiamGia, KhachHang khachHang) {
        KhachHangGiamGia khachHangGiamGia = new KhachHangGiamGia();
        khachHangGiamGia.setIdPhieuGiamGia(phieuGiamGia);
        khachHangGiamGia.setIdKhachHang(khachHang);
        khachHangGiamGia.setIsUser(false);
        khachHangGiamGia.setNgayCap(LocalDate.now());
        khachHangGiamGiaRepository.save(khachHangGiamGia);
    }

    private TrangThaiPGG xacDinhTrangThaiPhieu (PhieuGiamGia phieu, LocalDate hienTai) {
        if (hienTai.isBefore(phieu.getNgayBatDau())) {
            return TrangThaiPGG.NOT_STARTED;
        } else if (hienTai.isAfter(phieu.getNgayKetThuc())) {
            return TrangThaiPGG.EXPIRED;
        }
        return TrangThaiPGG.ACTIVE;
    }

    private PhieuGiamGiaAdminResponse anhXaPhieuGiamGia (PhieuGiamGia phieuGiamGia) {
        return modelMapper.map(phieuGiamGia, PhieuGiamGiaAdminResponse.class);
    }

//    private BigDecimal tinhGiaKhuyenMai(SanPhamChiTiet sanPhamChiTiet, PhieuGiamGia phieuGiamGia) {
//        BigDecimal giaGoc = sanPhamChiTiet.getGiaBan();
//        BigDecimal giaTriKhuyenMai = phieuGiamGia.getGiaTriKhuyenMai();
//
//        if (giaGoc == null || giaTriKhuyenMai == null) {
//            throw new IllegalArgumentException("Giá gốc hoặc giá trị khuyến mãi không được null");
//        }
//
//        if (giaTriKhuyenMai.compareTo(BigDecimal.ZERO) < 0) {
//            throw new IllegalArgumentException("Giá trị khuyến mãi không được âm");
//        }
//
//        boolean laKhuyenMaiPhanTram = phieuGiamGia.getLoaiKhuyenMai().equalsIgnoreCase("Phần trăm");
//        if (laKhuyenMaiPhanTram && giaTriKhuyenMai.compareTo(BigDecimal.valueOf(100)) > 0) {
//            throw new IllegalArgumentException("Giá trị khuyến mãi phần trăm không được vượt quá 100");
//        }
//
//        BigDecimal giaKhuyenMai;
//        if (laKhuyenMaiPhanTram) {
//            // Tính giá khuyến mãi theo phần trăm: giaGoc * (1 - giaTriKhuyenMai/100)
//            BigDecimal tyLeGiam = giaTriKhuyenMai.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
//            BigDecimal soTienGiam = giaGoc.multiply(tyLeGiam);
//            giaKhuyenMai = giaGoc.subtract(soTienGiam);
//        } else {
//            // Tính giá khuyến mãi cố định: giaGoc - giaTriKhuyenMai
//            giaKhuyenMai = giaGoc.subtract(giaTriKhuyenMai);
//        }
//
//        // Đảm bảo giá không âm và làm tròn đến 2 chữ số thập phân
//        return giaKhuyenMai.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : giaKhuyenMai.setScale(2, RoundingMode.HALF_UP);
//    }

    //Hoa don
    public List<PhieuGiamGiaAdminResponse> layDanhSachPhieuGiamGiaCuaKhach(String timKiem, Integer idKhachHang, BigDecimal giaTriDonHangToiThieu) {

        Set<PhieuGiamGia> phieuGiamGiaGop = new HashSet<>();

        List<PhieuGiamGia> phieuCongKhai = phieuGiamGiaRepository
                .timPhieuGiamGiaCongKhai(
                        BigDecimal.ZERO,
                        giaTriDonHangToiThieu,
                        TrangThaiPhatHanh.ISSUED,
                        true,
                        TrangThaiPGG.ACTIVE,
                        timKiem
                );
        phieuGiamGiaGop.addAll(phieuCongKhai);

        if (idKhachHang != null) {
            List<KhachHangGiamGia> khachHangGiamGias = khachHangGiamGiaRepository.findByIdKhachHang_Id(idKhachHang);

            List<PhieuGiamGia> phieuRiengTu = khachHangGiamGias
                    .stream()
                    .filter(khgg -> !khgg.getIsUser())
                    .map(KhachHangGiamGia::getIdPhieuGiamGia)
                    .filter(pgg -> pgg.getTrangThaiPhatHanh() == TrangThaiPhatHanh.ISSUED
                            && pgg.getGiaTriDonHangToiThieu().compareTo(giaTriDonHangToiThieu) <= 0
                            && pgg.getTrangThaiPhieuGiamGia() == TrangThaiPGG.ACTIVE
                            && pgg.getMaGiamGia().toLowerCase().contains(timKiem == null ? "" : timKiem.toLowerCase()))
                    .toList();
            phieuGiamGiaGop.addAll(phieuRiengTu);
        }

        return phieuGiamGiaGop
                .stream()
                .map(this::anhXaPhieuGiamGia)
                .collect(Collectors.toList());
    }

    // Show the vouchers in client when redeeming points
    public Page<PhieuGiamGiaAdminResponse> getVoucherRedeeming (String timKiem, TrangThaiPGG trangThai,
                                                                    LocalDate ngayBatDau, LocalDate ngayKetThuc,
                                                                    HangKhachHang hangKhachHang,
                                                                    int trang, int kichThuoc, String sapXepTheo, String huongSapXep) {
        Sort sapXep = huongSapXep.equalsIgnoreCase("desc") ? Sort.by(sapXepTheo).descending() : Sort.by(sapXepTheo).ascending();
        Pageable phanTrang = PageRequest.of(trang, kichThuoc, sapXep);
        Page<PhieuGiamGia> trangPhieuGiamGia = phieuGiamGiaRepository.getDoiVoucher(timKiem, trangThai, ngayBatDau, ngayKetThuc, hangKhachHang, phanTrang);
        return trangPhieuGiamGia.map(this::anhXaPhieuGiamGia);
    }



}